import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../../core/constants/constants.dart';
import '../../../../models/notification_model.dart';

class NotificationRepository {
  final SupabaseClient _supabase;

  NotificationRepository(this._supabase);

  Future<List<NotificationModel>> getNotifications() async {
    final response = await _supabase
        .from(AppConstants.notificationsTable)
        .select()
        .order('timestamp', ascending: false);
    
    return (response as List).map((json) => NotificationModel.fromMap(json, json['id'].toString())).toList();
  }

  Future<void> deleteNotification(String id) async {
    await _supabase.from(AppConstants.notificationsTable).delete().eq('id', id);
  }
}
