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
    try {
      final users = await _fetchAllUsers();
      state = AsyncValue.data(users);
    } catch (e, stackTrace) {
      state = AsyncValue.error(e, stackTrace);
    }
  }

  Future<void> updateUser(UserModel user) async {
    final supabase = ref.read(supabaseClientProvider);
    final previousState = state.value;
    
    // Optimistic UI update
    if (previousState != null) {
      state = AsyncData(previousState.map((e) => e.uid == user.uid ? user : e).toList());
    }

    try {
      await supabase.from(AppConstants.usersTable).update(user.toJson()).eq('id', user.uid);
      
      // Log Activity
      final admin = ref.read(authNotifierProvider).value;
      if (admin != null) {
        try {
          await ref.read(activityLogNotifierProvider.notifier).logAction(
            adminId: admin.uid,
            adminName: admin.name,
            action: 'User Updated',
            targetId: user.name,
            details: 'User "${user.name}" status or role was changed to ${user.isActive ? "Active" : "Inactive"}.',
          );
        } catch (_) {
          // Ignore log action failures
        }
      }
    } catch (e) {
      // Revert Optimistic UI update
      if (previousState != null) {
        state = AsyncData(previousState);
      }
      rethrow;
    }
  }

  Future<void> deleteUser(String uid) async {
    final supabase = ref.read(supabaseClientProvider);
    try {
      await supabase.from(AppConstants.usersTable).delete().eq('id', uid);

      // Log Activity
      final admin = ref.read(authNotifierProvider).value;
      if (admin != null) {
        await ref.read(activityLogNotifierProvider.notifier).logAction(
          adminId: admin.uid,
          adminName: admin.name,
          action: 'User Deleted',
          targetId: uid,
          details: 'User with UID: $uid was permanently deleted.',
        );
      }

      await loadUsers(); // Refresh the list
    } catch (e) {
      rethrow;
    }
  }
}
