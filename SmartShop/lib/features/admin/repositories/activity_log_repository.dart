import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../../core/constants/constants.dart';
import '../../../../models/activity_log_model.dart';

class ActivityLogRepository {
  final SupabaseClient _supabase;

  ActivityLogRepository(this._supabase);

  Future<void> logActivity(ActivityLogModel log) async {
    await _supabase.from(AppConstants.activityLogsTable).insert(log.toMap());
  }

  Future<List<ActivityLogModel>> getAllLogs() async {
    final response = await _supabase
        .from(AppConstants.activityLogsTable)
        .select()
        .order('timestamp', ascending: false);
    
    return (response as List).map((json) => ActivityLogModel.fromMap(json, json['id'].toString())).toList();
  }
}
