import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';
import '../models/user_model.dart';

class UserViewModel extends ChangeNotifier {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref().child('users');
  List<UserModel> _users = [];
  bool _isLoading = false;

  List<UserModel> get users => _users;
  bool get isLoading => _isLoading;

  void fetchAllUsers() {
    _isLoading = true;
    _dbRef.onValue.listen((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data != null) {
        _users = data.entries.map((e) {
          return UserModel.fromMap(Map<String, dynamic>.from(e.value), e.key);
        }).toList();
      } else {
        _users = [];
      }
      _isLoading = false;
      notifyListeners();
    });
  }

  Future<void> toggleUserStatus(String uid, bool currentStatus) async {
    await _dbRef.child(uid).update({
      'isActive': !currentStatus,
    });
  }
}
