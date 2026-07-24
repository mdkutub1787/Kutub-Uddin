import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final notificationNotifierProvider = AsyncNotifierProvider<NotificationNotifier, List<dynamic>>(() {
  return NotificationNotifier();
});

class NotificationNotifier extends AsyncNotifier<List<dynamic>> {
  @override
  FutureOr<List<dynamic>> build() async {
    return [];
  }

  Future<void> loadNotifications() async {
    state = const AsyncLoading();
    try {
      // Fetch from Supabase in real implementation
      state = const AsyncData([]);
    } catch (e, st) {
      state = AsyncError(e, st);
    }
  }

  Future<void> deleteNotification(String id) async {
    // Real implementation: delete from Supabase
    if (state.value != null) {
      state = AsyncData(state.value!.where((n) => n.id != id).toList());
    }
  }
}
