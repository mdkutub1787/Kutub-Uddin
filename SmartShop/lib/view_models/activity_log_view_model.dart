import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';
import '../models/activity_log_model.dart';

class ActivityLogViewModel extends ChangeNotifier {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref().child('activity_logs');
  List<ActivityLogModel> _logs = [];
  bool _isLoading = false;

  List<ActivityLogModel> get logs => _logs;
  bool get isLoading => _isLoading;

  void fetchLogs() {
    _isLoading = true;
    _dbRef.orderByChild('timestamp').onValue.listen((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data != null) {
        _logs = data.entries.map((e) {
          return ActivityLogModel.fromMap(Map<String, dynamic>.from(e.value), e.key);
        }).toList();
        _logs.sort((a, b) => b.timestamp.compareTo(a.timestamp));
      } else {
        _logs = [];
      }
      _isLoading = false;
      notifyListeners();
    });
  }

  Future<void> logAction({
    required String adminId,
    required String adminName,
    required String action,
    required String targetId,
    required String details,
  }) async {
    final newLog = ActivityLogModel(
      id: '',
      adminId: adminId,
      adminName: adminName,
      action: action,
      targetId: targetId,
      details: details,
      timestamp: DateTime.now(),
    );
    await _dbRef.push().set(newLog.toMap());
  }
}
