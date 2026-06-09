import 'package:flutter/material.dart';
import '../services/auth_service.dart';
import 'package:firebase_auth/firebase_auth.dart';
import '../models/user_model.dart';

class AuthViewModel extends ChangeNotifier {
  final AuthService _authService = AuthService();
  bool _isLoading = false;
  String? _error;
  UserModel? _userModel;

  bool get isLoading => _isLoading;
  String? get error => _error;
  User? get firebaseUser => _authService.currentUser;
  UserModel? get user => _userModel;
  bool get isAdmin => _userModel?.role == 'admin';

  AuthViewModel() {
    _init();
  }

  void _init() {
    _authService.userStream.listen((user) async {
      if (user != null) {
        _userModel = await _authService.getUserData(user.uid);
      } else {
        _userModel = null;
      }
      notifyListeners();
    });
  }

  Future<bool> login(String email, String password) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      UserCredential credential = await _authService.signIn(email, password);
      _userModel = await _authService.getUserData(credential.user!.uid);
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<bool> register(String email, String password, {String name = "", String phoneNumber = "", String address = ""}) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      UserCredential credential = await _authService.register(email, password, name: name, phoneNumber: phoneNumber, address: address);
      _userModel = await _authService.getUserData(credential.user!.uid);
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<bool> requestAdminAccess(String adminCode) async {
    _isLoading = true;
    _error = null;
    notifyListeners();
    try {
      if (firebaseUser == null) return false;
      bool success = await _authService.verifyAdminCredentials(firebaseUser!.uid, adminCode);
      if (success) {
        await refreshUserData();
      }
      _isLoading = false;
      notifyListeners();
      return success;
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<bool> forgotPassword(String email) async {
    _isLoading = true;
    notifyListeners();
    try {
      await _authService.sendPasswordResetEmail(email);
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  // Renamed to changePassword to fix the error in profile_screen.dart
  Future<bool> changePassword(String oldPassword, String newPassword) async {
    _isLoading = true;
    _error = null;
    notifyListeners();
    try {
      if (firebaseUser == null || firebaseUser!.email == null) {
        _error = "User not logged in";
        _isLoading = false;
        notifyListeners();
        return false;
      }

      bool isAuthed = await _authService.reauthenticate(firebaseUser!.email!, oldPassword);
      if (!isAuthed) {
        _error = "Incorrect old password";
        _isLoading = false;
        notifyListeners();
        return false;
      }

      await _authService.updatePassword(newPassword);
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<void> logout() async {
    await _authService.signOut();
    _userModel = null;
    notifyListeners();
  }

  Future<void> refreshUserData() async {
    if (firebaseUser != null) {
      _userModel = await _authService.getUserData(firebaseUser!.uid);
      notifyListeners();
    }
  }
}
