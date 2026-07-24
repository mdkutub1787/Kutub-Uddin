import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class SettingsState {
  final ThemeMode themeMode;
  final Color primaryColor;

  SettingsState({
    this.themeMode = ThemeMode.system,
    this.primaryColor = Colors.deepPurple,
  });
}

class SettingsNotifier extends Notifier<SettingsState> {
  @override
  SettingsState build() {
    return SettingsState();
  }

  void setThemeMode(ThemeMode mode) {
    state = SettingsState(themeMode: mode, primaryColor: state.primaryColor);
  }

  void setPrimaryColor(Color color) {
    state = SettingsState(themeMode: state.themeMode, primaryColor: color);
  }
}

final settingsProvider = NotifierProvider<SettingsNotifier, SettingsState>(() {
  return SettingsNotifier();
});
