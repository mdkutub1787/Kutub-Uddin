import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ThemeProvider extends ChangeNotifier {
  Color _primaryColor = const Color(0xFF1A237E); // Default Deep Indigo
  bool _isDarkMode = false;

  Color get primaryColor => _primaryColor;
  bool get isDarkMode => _isDarkMode;

  ThemeProvider() {
    _loadTheme();
  }

  void _loadTheme() async {
    final prefs = await SharedPreferences.getInstance();
    int? colorValue = prefs.getInt('primary_color');
    bool? darkMode = prefs.getBool('is_dark_mode');
    
    if (colorValue != null) _primaryColor = Color(colorValue);
    if (darkMode != null) _isDarkMode = darkMode;
    
    notifyListeners();
  }

  void setPrimaryColor(Color color) async {
    _primaryColor = color;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt('primary_color', color.value);
    notifyListeners();
  }

  void toggleDarkMode() async {
    _isDarkMode = !_isDarkMode;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setBool('is_dark_mode', _isDarkMode);
    notifyListeners();
  }
}
