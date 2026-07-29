import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../admin/riverpod/activity_log_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/providers.dart';
import '../../../core/constants/constants.dart';
import '../models/user_model.dart';

final userNotifierProvider = AsyncNotifierProvider<UserNotifier, List<UserModel>>(() {
  return UserNotifier();
});

class UserNotifier extends AsyncNotifier<List<UserModel>> {
  @override
  FutureOr<List<UserModel>> build() async {
    return await _fetchAllUsers();
  }

  Future<List<UserModel>> _fetchAllUsers() async {
    final supabase = ref.watch(supabaseClientProvider);
    try {
      final response = await supabase.from(AppConstants.usersTable).select();
      return (response as List).map((json) => UserModel.fromJson(json)).toList();
    } catch (e) {
      return [];
    }
  }

  Future<void> loadUsers() async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(() => _fetchAllUsers());
  }

  Future<void> toggleUserStatus(String uid, bool isActive) async {
    final supabase = ref.read(supabaseClientProvider);
    try {
      await supabase.from(AppConstants.usersTable).update({'isActive': isActive}).eq('id', uid);
      
      // Update local state for instant UI change
      if (state.value != null) {
        state = AsyncData(state.value!.map((u) => u.uid == uid ? u.copyWith(isActive: isActive) : u).toList());
      }
      
      // Log Activity
      final admin = ref.read(authNotifierProvider).value;
      if (admin != null) {
        ref.read(activityLogNotifierProvider.notifier).logAction(
          adminId: admin.uid,
          adminName: admin.name,
          action: isActive ? 'User Unblocked' : 'User Blocked',
          targetId: uid,
          details: 'User status changed to ${isActive ? "Active" : "Inactive"}',
        );
      }
    } catch (e) {
      // Revert or show error
    }
  }

  Future<void> deleteUser(String uid) async {
    final supabase = ref.read(supabaseClientProvider);
    try {
      await supabase.from(AppConstants.usersTable).delete().eq('id', uid);
      await loadUsers();
    } catch (e) {
      rethrow;
    }
  }
}
