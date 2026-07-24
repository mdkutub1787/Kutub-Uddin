import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
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
      final response = await supabase.from('users').select();
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
    try {
      await supabase.from('users').update(user.toJson()).eq('id', user.uid);
      await loadUsers(); // Refresh the list
    } catch (e) {
      rethrow;
    }
  }

  Future<void> deleteUser(String uid) async {
    final supabase = ref.read(supabaseClientProvider);
    try {
      await supabase.from('users').delete().eq('id', uid);
      await loadUsers(); // Refresh the list
    } catch (e) {
      rethrow;
    }
  }
}
