import 'package:flutter/material.dart';

class LoadingViewModel extends ChangeNotifier {
  bool _isLoading = false;
  String _message = "";

  bool get isLoading => _isLoading;
  String get message => _message;

  void show({String message = ""}) {
    _isLoading = true;
    _message = message;
    notifyListeners();
  }

  void hide() {
    _isLoading = false;
    _message = "";
    notifyListeners();
  }
}
