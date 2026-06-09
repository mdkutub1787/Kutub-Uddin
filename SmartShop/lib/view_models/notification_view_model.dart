import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';
import '../models/notification_model.dart';

class NotificationViewModel extends ChangeNotifier {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref().child('notifications');
  List<NotificationModel> _notifications = [];
  bool _isLoading = false;

  List<NotificationModel> get notifications => _notifications;
  bool get isLoading => _isLoading;

  NotificationViewModel() {
    fetchNotifications();
  }

  void fetchNotifications() {
    _isLoading = true;
    _dbRef.onValue.listen((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data != null) {
        _notifications = data.entries.map((e) {
          return NotificationModel.fromMap(e.value, e.key);
        }).toList();
        _notifications.sort((a, b) => b.timestamp.compareTo(a.timestamp));
      } else {
        _notifications = [];
      }
      _isLoading = false;
      notifyListeners();
    });
  }

  Future<void> addNotification(String title, String message, {String? imageUrl}) async {
    await _dbRef.push().set({
      'title': title,
      'message': message,
      'timestamp': ServerValue.timestamp,
      'imageUrl': imageUrl,
    });
  }

  Future<void> updateNotification(String id, String title, String message, {String? imageUrl}) async {
    await _dbRef.child(id).update({
      'title': title,
      'message': message,
      'imageUrl': imageUrl,
    });
  }

  Future<void> deleteNotification(String id) async {
    await _dbRef.child(id).remove();
  }
}
