import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart' as sb;
import '../repositories/auth_repository.dart';
import '../../user/models/user_model.dart';

class AuthNotifier extends AsyncNotifier<UserModel?> {
  late final AuthRepository _repository;

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

  Future<UserModel> _fetchUserData(sb.User user) async {
    try {
      final response = await sb.Supabase.instance.client
          .from('users')
          .select()
          .eq('id', user.id)
          .maybeSingle();

      if (response != null) {
        return UserModel.fromJson(response);
      }
    } catch (e) {
      // print('Error fetching user data from DB: $e');
    }
    
    // Fallback to auth metadata if DB row not found
    final metadata = user.userMetadata ?? {};
    return UserModel(
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
  }

  String? error;

  Future<void> signIn(String email, String password) async {
    state = const AsyncLoading();
    try {
      await _repository.signIn(email, password);
      error = null;
      final user = _repository.currentUser;
      state = AsyncData(user != null ? await _fetchUserData(user) : null);
    } catch (e, st) {
      error = e.toString();
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
    // dummy implementation for now
    if (adminCode == '1234') {
      if (state.value != null) {
         state = AsyncData(state.value!.copyWith(role: 'admin'));
      }
      return true;
    }
    error = 'Invalid code';
    return false;
  }

  Future<void> refreshUserData() async {
    if (state.value != null && _repository.currentUser != null) {
      final freshData = await _fetchUserData(_repository.currentUser!);
      state = AsyncData(freshData);
    }
  }
}

final authNotifierProvider = AsyncNotifierProvider<AuthNotifier, UserModel?>(() {
  return AuthNotifier();
});
