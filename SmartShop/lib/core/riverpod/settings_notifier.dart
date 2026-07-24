import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SettingsState {
  final ThemeMode themeMode;
  final Color primaryColor;
  final String shopName;

  SettingsState({
    this.themeMode = ThemeMode.system,
    this.primaryColor = Colors.deepPurple,
    this.shopName = 'Smart Shop',
  });

  SettingsState copyWith({
    ThemeMode? themeMode,
    Color? primaryColor,
    String? shopName,
  }) {
    return SettingsState(
      themeMode: themeMode ?? this.themeMode,
      primaryColor: primaryColor ?? this.primaryColor,
      shopName: shopName ?? this.shopName,
    );
  }
}

class SettingsNotifier extends Notifier<SettingsState> {
  late SharedPreferences _prefs;

  @override
  SettingsState build() {
    _initPrefs();
    return SettingsState();
  }

  Future<void> _initPrefs() async {
    _prefs = await SharedPreferences.getInstance();
    final isDark = _prefs.getBool('isDarkMode');
    final colorValue = _prefs.getInt('primaryColor');
    
    state = state.copyWith(
      themeMode: isDark == null ? ThemeMode.system : (isDark ? ThemeMode.dark : ThemeMode.light),
      primaryColor: colorValue != null ? Color(colorValue) : state.primaryColor,
    );
  }

  void setThemeMode(ThemeMode mode) {
    state = state.copyWith(themeMode: mode);
    _prefs.setBool('isDarkMode', mode == ThemeMode.dark);
  }

  void setPrimaryColor(Color color) {
    state = state.copyWith(primaryColor: color);
    _prefs.setInt('primaryColor', color.toARGB32());
  }
}

final settingsProvider = NotifierProvider<SettingsNotifier, SettingsState>(() {
  return SettingsNotifier();
});
