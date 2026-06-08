import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:fflipy/core/constants/app_constants.dart';

final localeProvider = StateNotifierProvider<LocaleNotifier, Locale>(
      (ref) => LocaleNotifier(),
);

class LocaleNotifier extends StateNotifier<Locale> {
  LocaleNotifier() : super(const Locale('en')) {
    _loadLocale();
  }

  Future<void> _loadLocale() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final savedLanguage = prefs.getString(AppConstants.languageKey);

      if (savedLanguage != null) {
        state = Locale(savedLanguage);
      } else {
        final systemLocale = WidgetsBinding.instance.window.locale;
        if (systemLocale.languageCode == 'es') {
          state = const Locale('es');
        } else if (systemLocale.languageCode == 'bn') {
          state = const Locale('bn');
        } else {
          state = const Locale('en');
        }
      }
    } catch (e) {
      state = const Locale('en');
    }
  }

  Future<void> setEnglish() async {
    await setLocale(const Locale('en'));
  }

  Future<void> setSpanish() async {
    await setLocale(const Locale('es'));
  }

  Future<void> setBangla() async {
    await setLocale(const Locale('bn'));
  }

  Future<void> setLocale(Locale locale) async {
    try {
      state = locale;

      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(AppConstants.languageKey, locale.languageCode);
    } catch (e) {
      // Handle error silently
    }
  }

  Future<void> toggleLanguage() async {
    if (state.languageCode == 'en') {
      await setSpanish();
    } else if (state.languageCode == 'es') {
      await setBangla();
    } else {
      await setEnglish();
    }
  }

  String getCurrentLanguageName() {
    switch (state.languageCode) {
      case 'es':
        return 'Spanish';
      case 'bn':
        return 'Bangla';
      default:
        return 'English';
    }
  }

  bool get isSpanish => state.languageCode == 'es';
  bool get isEnglish => state.languageCode == 'en';
  bool get isBangla => state.languageCode == 'bn';
}

final supportedLocalesProvider = Provider<List<Locale>>(
      (ref) => const [
    Locale('en'),
    Locale('es'),
    Locale('bn'),
  ],
);

final isSpanishProvider = Provider<bool>(
      (ref) => ref.watch(localeProvider).languageCode == 'es',
);

final isEnglishProvider = Provider<bool>(
      (ref) => ref.watch(localeProvider).languageCode == 'en',
);

final isBanglaProvider = Provider<bool>(
      (ref) => ref.watch(localeProvider).languageCode == 'bn',
);

final currentLanguageNameProvider = Provider<String>(
      (ref) {
    final locale = ref.watch(localeProvider);
    switch (locale.languageCode) {
      case 'es':
        return 'Spanish';
      case 'bn':
        return 'Bangla';
      default:
        return 'English';
    }
  },
);
