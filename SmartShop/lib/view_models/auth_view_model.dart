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

  Future<bool> register(String email, String password, {String displayName = "", String phoneNumber = ""}) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      UserCredential credential = await _authService.register(email, password, displayName: displayName, phoneNumber: phoneNumber);
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

  // Verify admin credentials and update role
  Future<bool> requestAdminAccess(String adminCode) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      if (firebaseUser == null) {
        _error = "User not authenticated";
        _isLoading = false;
        notifyListeners();
        return false;
      }

      bool isAdminCodeValid = await _authService.verifyAdminCredentials(firebaseUser!.uid, adminCode);

      if (isAdminCodeValid) {
        _userModel = await _authService.getUserData(firebaseUser!.uid);
        _isLoading = false;
        notifyListeners();
        return true;
      } else {
        _error = "Invalid admin code";
        _isLoading = false;
        notifyListeners();
        return false;
      }
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
