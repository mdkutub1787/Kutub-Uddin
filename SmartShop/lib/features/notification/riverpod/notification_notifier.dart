import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';
import '../../../models/notification_model.dart';
import '../repositories/notification_repository.dart';

final notificationRepositoryProvider = Provider<NotificationRepository>((ref) {
  return NotificationRepository(ref.watch(supabaseClientProvider));
});

final notificationNotifierProvider = AsyncNotifierProvider<NotificationNotifier, List<NotificationModel>>(() {
  return NotificationNotifier();
});

class NotificationNotifier extends AsyncNotifier<List<NotificationModel>> {
  late NotificationRepository _repository;

  @override
  FutureOr<List<NotificationModel>> build() async {
    _repository = ref.watch(notificationRepositoryProvider);
    return await _fetchNotifications();
  }

  Future<List<NotificationModel>> _fetchNotifications() async {
    try {
      return await _repository.getNotifications();
    } catch (e) {
      return [];
    }
  }

  Future<void> loadNotifications() async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() => _fetchNotifications());
  }

  Future<void> deleteNotification(String id) async {
    try {
      await _repository.deleteNotification(id);
      await loadNotifications();
    } catch (e) {
      // Delete failed
    }
  }

  Future<void> sendNotification({
    required String title,
    required String message,
    String? imageUrl,
  }) async {
    final notification = NotificationModel(
      id: '',
      title: title,
      message: message,
      timestamp: DateTime.now(),
      imageUrl: imageUrl,
    );
    
    try {
      final supabase = ref.read(supabaseClientProvider);
      await supabase.from(AppConstants.notificationsTable).insert(notification.toMap());
      await loadNotifications();
    } catch (e) {
      // Send failed
    }
  }

  Future<void> updateNotification(String id, String title, String message) async {
    try {
      final supabase = ref.read(supabaseClientProvider);
      await supabase.from(AppConstants.notificationsTable).update({
        'title': title,
        'message': message,
      }).eq('id', id);
      await loadNotifications();
    } catch (e) {
      // Update failed
    }
  }
}
