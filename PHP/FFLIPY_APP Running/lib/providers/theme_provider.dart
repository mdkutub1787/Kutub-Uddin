import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:fflipy/core/constants/app_constants.dart';
import 'package:fflipy/core/theme/app_theme.dart';

final themeModeProvider = StateNotifierProvider<ThemeModeNotifier, ThemeMode>(
  (ref) => ThemeModeNotifier(),
);

class ThemeModeNotifier extends StateNotifier<ThemeMode> {
  ThemeModeNotifier() : super(ThemeMode.light) {
    _loadThemeMode();
  }

  Future<void> _loadThemeMode() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final savedTheme = prefs.getString(AppConstants.themeKey);

      if (savedTheme != null) {
        state = savedTheme == 'light' ? ThemeMode.light : ThemeMode.dark;
      } else {
        state = ThemeMode.light;
      }
    } catch (e) {
      state = ThemeMode.dark;
    }
  }

  Future<void> toggleTheme() async {
    try {
      final newTheme = state == ThemeMode.dark ? ThemeMode.light : ThemeMode.dark;
      state = newTheme;

      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(
        AppConstants.themeKey,
        newTheme == ThemeMode.light ? 'light' : 'dark',
      );
    } catch (e) {
      // Handle error silently
    }
  }

  Future<void> setThemeMode(ThemeMode themeMode) async {
    try {
      state = themeMode;

      final prefs = await SharedPreferences.getInstance();
      String themeString;

      if (themeMode == ThemeMode.light) {
        themeString = 'light';
      } else if (themeMode == ThemeMode.dark) {
        themeString = 'dark';
      } else {
        themeString = 'system';
      }

      await prefs.setString(AppConstants.themeKey, themeString);
    } catch (e) {
      // Handle error silently
    }
  }
}

final lightThemeProvider = Provider<ThemeData>(
  (ref) => AppTheme.lightTheme(),
);

final darkThemeProvider = Provider<ThemeData>(
  (ref) => AppTheme.darkTheme(),
);

final currentThemeProvider = Provider<ThemeData>(
  (ref) {
    final themeMode = ref.watch(themeModeProvider);
    final brightness = MediaQueryData.fromView(
      WidgetsBinding.instance.platformDispatcher.views.first,
    ).platformBrightness;

    if (themeMode == ThemeMode.dark) {
      return ref.watch(darkThemeProvider);
    } else if (themeMode == ThemeMode.light) {
      return ref.watch(lightThemeProvider);
    } else {
      return brightness == Brightness.dark
          ? ref.watch(darkThemeProvider)
          : ref.watch(lightThemeProvider);
    }
  },
);

final isDarkModeProvider = Provider<bool>(
  (ref) {
    final themeMode = ref.watch(themeModeProvider);
    final brightness = MediaQueryData.fromView(
      WidgetsBinding.instance.platformDispatcher.views.first,
    ).platformBrightness;

    if (themeMode == ThemeMode.dark) {
      return true;
    } else if (themeMode == ThemeMode.light) {
      return false;
    } else {
      return brightness == Brightness.dark;
    }
  },
);

