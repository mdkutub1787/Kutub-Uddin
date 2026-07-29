import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../constants/constants.dart';

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
  RealtimeChannel? _systemConfigChannel;

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
    
    _syncWithSupabase();
  }

  Future<void> _syncWithSupabase() async {
    try {
      final supabase = Supabase.instance.client;
      // Fetch initial
      final response = await supabase.from(AppConstants.systemConfigTable).select();
      
      Color? newColor;
      
      for (var row in response) {
        if (row['key'] == 'primary_color') {
          final colorStr = row['value'].toString();
          if (colorStr.isNotEmpty) {
            newColor = Color(int.parse(colorStr));
          }
        }
      }
      
      if (newColor != null && newColor != state.primaryColor) {
        state = state.copyWith(primaryColor: newColor);
        _prefs.setInt('primaryColor', newColor.toARGB32());
      }
      
      // Setup Realtime
      _systemConfigChannel = supabase.channel('public:system_config')
        .onPostgresChanges(
          event: PostgresChangeEvent.all,
          schema: 'public',
          table: AppConstants.systemConfigTable,
          callback: (payload) {
            final record = payload.newRecord;
            if (record.isNotEmpty && record['key'] == 'primary_color') {
              final colorStr = record['value'].toString();
              if (colorStr.isNotEmpty) {
                final c = Color(int.parse(colorStr));
                state = state.copyWith(primaryColor: c);
                _prefs.setInt('primaryColor', c.toARGB32());
              }
            }
          }
        )
        .subscribe();
        
    } catch (e) {
      debugPrint("Error syncing settings: $e");
    }
  }

  void setThemeMode(ThemeMode mode) {
    state = state.copyWith(themeMode: mode);
    _prefs.setBool('isDarkMode', mode == ThemeMode.dark);
  }

  // Used by Admin to save color globally
  Future<void> setPrimaryColor(Color color) async {
    state = state.copyWith(primaryColor: color);
    _prefs.setInt('primaryColor', color.toARGB32());
    
    try {
      await Supabase.instance.client.from(AppConstants.systemConfigTable).upsert({
        'key': 'primary_color',
        'value': color.toARGB32().toString(),
      });
    } catch (e) {
      debugPrint("Error saving primary color to DB: $e");
    }
  }

  void setCurrency(String symbol) {
    state = state.copyWith(currencySymbol: symbol);
    _prefs.setString(_currencyKey, symbol);
  }
}

final settingsProvider = NotifierProvider<SettingsNotifier, SettingsState>(() {
  return SettingsNotifier();
});
