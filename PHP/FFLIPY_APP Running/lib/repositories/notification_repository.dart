import 'package:fflipy/models/notification_model/notification_model.dart';
import 'package:fflipy/services/notification_service.dart';

class NotificationRepository {
  final NotificationService _notificationService;

  NotificationRepository(this._notificationService);

  Future<NotificationResponse> getNotifications() async {
    return await _notificationService.getNotifications();
  }

  Future<void> markAsRead(int id) async {
    return await _notificationService.markAsRead(id);
  }
}
