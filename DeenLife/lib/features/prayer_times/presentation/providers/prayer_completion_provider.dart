import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:intl/intl.dart';

final prayerCompletionProvider =
    StateNotifierProvider<PrayerCompletionNotifier, Map<String, bool>>((ref) {
      return PrayerCompletionNotifier();
    });

class PrayerCompletionNotifier extends StateNotifier<Map<String, bool>> {
  PrayerCompletionNotifier()
    : super({
        'Fajr': false,
        'Dhuhr': false,
        'Asr': false,
        'Maghrib': false,
        'Isha': false,
      }) {
    _loadState();
  }

  String get _todayKey => DateFormat('yyyy-MM-dd').format(DateTime.now());

  Future<void> _loadState() async {
    final prefs = await SharedPreferences.getInstance();
    final savedDate = prefs.getString('last_prayer_date') ?? '';

    // Reset if it's a new day
    if (savedDate != _todayKey) {
      await prefs.setString('last_prayer_date', _todayKey);
      for (var key in state.keys) {
        await prefs.setBool('prayer_$key', false);
      }
    } else {
      final Map<String, bool> loaded = {};
      state.keys.forEach((prayer) {
        loaded[prayer] = prefs.getBool('prayer_$prayer') ?? false;
      });
      state = loaded;
    }
  }

  Future<void> togglePrayer(String prayer) async {
    final prefs = await SharedPreferences.getInstance();
    final newValue = !(state[prayer] ?? false);
    await prefs.setBool('prayer_$prayer', newValue);
    state = {...state, prayer: newValue};
  }
}
