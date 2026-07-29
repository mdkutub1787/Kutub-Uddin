import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart' as sb;
import 'package:firebase_messaging/firebase_messaging.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';
import '../repositories/auth_repository.dart';
import '../../user/models/user_model.dart';

class AuthNotifier extends AsyncNotifier<UserModel?> {
  late AuthRepository _repository;

  @override
  Future<UserModel?> build() async {
    _repository = ref.watch(authRepositoryProvider);
    
    // Listen to auth state changes
    _repository.authStateChanges.listen((data) async {
      if (data.session?.user != null) {
        final userData = await _fetchUserData(data.session!.user!);
        state = AsyncData(userData);
      } else {
        state = const AsyncData(null);
      }
    });

    final sbUser = _repository.currentUser;
    return sbUser != null ? await _fetchUserData(sbUser) : null;
  }

  Future<void> loadUser() async {
    final sbUser = _repository.currentUser;
    if (sbUser != null) {
      final userData = await _fetchUserData(sbUser);
      state = AsyncData(userData);
    } else {
      state = const AsyncData(null);
    }
  }

  Future<UserModel> _fetchUserData(sb.User user) async {
    final supabase = ref.read(supabaseClientProvider);
    try {
      final response = await supabase
          .from(AppConstants.usersTable)
          .select()
          .eq('id', user.id)
          .maybeSingle();

      if (response != null) {
        final existingUser = UserModel.fromJson(response);
        await _updateFcmToken(user.id);
        return existingUser;
      }
    } catch (e) {
      // print('Error fetching user data from DB: $e');
    }
    
    // Fallback to auth metadata if DB row not found
    final metadata = user.userMetadata ?? {};
    var newUser = UserModel(
      uid: user.id,
      email: user.email ?? '',
      name: metadata['full_name'] ?? metadata['name'] ?? '',
      phoneNumber: metadata['phone_number'] ?? metadata['phoneNumber'] ?? '',
      address: metadata['address'] ?? '',
      role: metadata['role'] ?? 'user',
      shopId: metadata['shopId'],
      shopName: metadata['shop_name'] ?? metadata['shopName'],
      imageUrl: metadata['image_url'] ?? metadata['imageUrl'] ?? metadata['avatar_url'],
    );
    
    // Auto-create shop if role is owner and shopId is missing
    if (newUser.role == 'owner' && newUser.shopId == null) {
      try {
        final shopName = (newUser.shopName != null && newUser.shopName!.isNotEmpty) 
            ? newUser.shopName! 
            : '${newUser.name}\'s Shop';
        final shopData = {
           'ownerId': newUser.uid,
           'name': shopName,
           'address': newUser.address.isNotEmpty ? newUser.address : 'Default Address',
           'phone': newUser.phoneNumber.isNotEmpty ? newUser.phoneNumber : 'N/A',
           'isOnlineOrderEnabled': true,
           'isPosEnabled': true,
           'latitude': metadata['latitude'] ?? 23.8103, // Default to Dhaka if not provided
           'longitude': metadata['longitude'] ?? 90.4125, // Default to Dhaka if not provided
           'createdAt': DateTime.now().toIso8601String(),
        };
        final insertedShop = await supabase.from(AppConstants.shopsTable).insert(shopData).select().single();
        newUser = newUser.copyWith(
          shopId: insertedShop['id'],
          shopName: insertedShop['name'],
        );
      } catch (e) {
        // Ignore shop creation failure
      }
    }
    
    // Auto-insert the missing user into the database
    try {
      await supabase.from(AppConstants.usersTable).insert({
        'id': newUser.uid,
        'email': newUser.email,
        'name': newUser.name,
        'phoneNumber': newUser.phoneNumber,
        'address': newUser.address,
        'role': newUser.role,
        'shopId': newUser.shopId,
        'shopName': newUser.shopName,
        'imageUrl': newUser.imageUrl,
        'isActive': true,
        'isAvailable': false,
        'created_at': DateTime.now().toIso8601String(),
      });
    } catch (e) {
      // Ignore if it fails due to RLS or other reasons
    }
    
    await _updateFcmToken(newUser.uid);
    return newUser;
  }

  Future<void> _updateFcmToken(String userId) async {
    try {
      final fcmToken = await FirebaseMessaging.instance.getToken();
      if (fcmToken != null) {
        await ref.read(supabaseClientProvider)
            .from(AppConstants.usersTable)
            .update({'fcm_token': fcmToken})
            .eq('id', userId);
      }
    } catch (e) {
      // Firebase might not be fully initialized or permissions denied
    }
  }

  String? error;

  Future<void> signIn(String email, String password) async {
    state = const AsyncLoading();
    try {
      await _repository.signIn(email, password);
      error = null;
      final user = _repository.currentUser;
      if (user != null) {
        final userData = await _fetchUserData(user);
        if (!userData.isActive) {
          await _repository.signOut();
          throw Exception('Your account has been deactivated by the Admin.');
        }
        state = AsyncData(userData);
      } else {
        state = const AsyncData(null);
      }
    } catch (e, st) {
      error = e.toString().replaceAll('Exception: ', '');
      state = AsyncError(e, st);
      rethrow; 
    }
  }

  Future<void> signUp(String email, String password, Map<String, dynamic> metadata) async {
    state = const AsyncLoading();
    try {
      await _repository.signUp(email, password, metadata: metadata);
      error = null;
      final user = _repository.currentUser;
      state = AsyncData(user != null ? await _fetchUserData(user) : null);
    } catch (e, st) {
      error = e.toString();
      state = AsyncError(e, st);
      rethrow;
    }
  }

  Future<void> signOut() async {
    state = const AsyncLoading();
    try {
      await _repository.signOut();
      error = null;
    } catch (e, st) {
      error = e.toString();
      state = AsyncError(e, st);
    }
  }

  Future<bool> requestAdminAccess(String adminCode) async {
    final user = state.value;
    if (user == null) return false;

    // Standard admin verification logic
    if (adminCode == '889900') { // High security code
      try {
        await ref.read(supabaseClientProvider)
            .from(AppConstants.usersTable)
            .update({'role': 'admin'})
            .eq('id', user.uid);
            
        state = AsyncData(user.copyWith(role: 'admin'));
        return true;
      } catch (e) {
        error = e.toString();
        return false;
      }
    }
    error = 'Invalid admin security code';
    return false;
  }

  Future<void> refreshUserData() async {
    if (state.value != null && _repository.currentUser != null) {
      final freshData = await _fetchUserData(_repository.currentUser!);
      state = AsyncData(freshData);
    }
  }

  Future<void> updateDeliveryAvailability(bool isAvailable) async {
    final user = state.value;
    if (user != null) {
      try {
        await ref.read(supabaseClientProvider)
            .from(AppConstants.usersTable)
            .update({'isAvailable': isAvailable})
            .eq('id', user.uid);
            
        // Optimistic UI update
        state = AsyncData(user.copyWith(isAvailable: isAvailable));
      } catch (e) {
        // Handle error if needed
      }
    }
  }

  Future<void> changePassword(String newPassword) async {
    try {
      await _repository.updatePassword(newPassword);
    } catch (e) {
      rethrow;
    }
  }
}

final authNotifierProvider = AsyncNotifierProvider<AuthNotifier, UserModel?>(() {
  return AuthNotifier();
});
