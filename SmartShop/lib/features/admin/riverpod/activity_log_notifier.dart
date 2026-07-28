import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../../../models/activity_log_model.dart';
import '../repositories/activity_log_repository.dart';

final activityLogRepositoryProvider = Provider<ActivityLogRepository>((ref) {
  return ActivityLogRepository(ref.watch(supabaseClientProvider));
});

final activityLogNotifierProvider = AsyncNotifierProvider<ActivityLogNotifier, List<ActivityLogModel>>(() {
  return ActivityLogNotifier();
});

class ActivityLogNotifier extends AsyncNotifier<List<ActivityLogModel>> {
  late ActivityLogRepository _repository;

  @override
  FutureOr<List<ActivityLogModel>> build() async {
    _repository = ref.watch(activityLogRepositoryProvider);
    return await _loadLogs();
  }

  Future<List<ActivityLogModel>> _loadLogs() async {
    try {
      return await _repository.getAllLogs();
    } catch (e) {
      return [];
    }
  }

  Future<void> loadLogs() async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() => _loadLogs());
  }

  Future<void> logAction({
    required String adminId,
    required String adminName,
    required String action,
    required String targetId,
    required String details,
  }) async {
    final log = ActivityLogModel(
      id: '',
      adminId: adminId,
      adminName: adminName,
      action: action,
      targetId: targetId,
      details: details,
      timestamp: DateTime.now(),
    );
    
    try {
      await _repository.logActivity(log);
      await loadLogs();
    } catch (e) {
      // Log failed
    }
  }
}
