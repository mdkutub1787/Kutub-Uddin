import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SettingsState {
  final ThemeMode themeMode;
  final Color primaryColor;
  final String shopName;
  final String currencySymbol;

  SettingsState({
    this.themeMode = ThemeMode.system,
    this.primaryColor = const Color(0xFF54B599), 
    this.shopName = 'Smart Shop',
    this.currencySymbol = '৳',
  });

  SettingsState copyWith({
    ThemeMode? themeMode,
    Color? primaryColor,
    String? shopName,
    String? currencySymbol,
  }) {
    return SettingsState(
      themeMode: themeMode ?? this.themeMode,
      primaryColor: primaryColor ?? this.primaryColor,
      shopName: shopName ?? this.shopName,
      currencySymbol: currencySymbol ?? this.currencySymbol,
    );
  }
}

class SettingsNotifier extends Notifier<SettingsState> {
  late SharedPreferences _prefs;
  static const String _currencyKey = 'currency_symbol';

  @override
  SettingsState build() {
    _initPrefs();
    return SettingsState();
  }

  Future<void> _initPrefs() async {
    _prefs = await SharedPreferences.getInstance();
    final isDark = _prefs.getBool('isDarkMode');
    final colorValue = _prefs.getInt('primaryColor');
    final currency = _prefs.getString(_currencyKey);
    
    state = state.copyWith(
      themeMode: isDark == null ? ThemeMode.system : (isDark ? ThemeMode.dark : ThemeMode.light),
      primaryColor: colorValue != null ? Color(colorValue) : state.primaryColor,
      currencySymbol: currency ?? state.currencySymbol,
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

  void setCurrency(String symbol) {
    state = state.copyWith(currencySymbol: symbol);
    _prefs.setString(_currencyKey, symbol);
  }
}

final settingsProvider = NotifierProvider<SettingsNotifier, SettingsState>(() {
  return SettingsNotifier();
});
