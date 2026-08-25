import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

final iqamahTimesProvider =
    StateNotifierProvider<IqamahNotifier, Map<String, String>>((ref) {
      return IqamahNotifier();
    });

class IqamahNotifier extends StateNotifier<Map<String, String>> {
  IqamahNotifier()
    : super({
        'Fajr': '5:15 AM',
        'Dhuhr': '1:30 PM',
        'Asr': '4:45 PM',
        'Maghrib': '6:15 PM',
        'Isha': '8:00 PM',
      }) {
    _loadTimes();
  }

  Future<void> _loadTimes() async {
    final prefs = await SharedPreferences.getInstance();
    final Map<String, String> loaded = {};
    state.keys.forEach((prayer) {
      loaded[prayer] = prefs.getString('iqamah_$prayer') ?? state[prayer]!;
    });
    state = loaded;
  }

  Future<void> updateTime(String prayer, String time) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('iqamah_$prayer', time);
    state = {...state, prayer: time};
  }
}

class SetMasjidTimesPage extends ConsumerWidget {
  const SetMasjidTimesPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final times = ref.watch(iqamahTimesProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text(context.tr('Set Masjid Times')),
        centerTitle: true,
      ),
      body: ListView(
        padding: const EdgeInsets.all(20),
        children: [
          Text(
            context.tr('Set Iqamah times for your local masjid'),
            style: const TextStyle(color: Colors.grey, fontSize: 14),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 24),
          ...times.entries
              .map(
                (entry) => _buildTimeTile(context, ref, entry.key, entry.value),
              )
              .toList(),
        ],
      ),
    );
  }

  Widget _buildTimeTile(
    BuildContext context,
    WidgetRef ref,
    String prayer,
    String time,
  ) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: ListTile(
        title: Text(
          prayer,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        trailing: Container(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
          decoration: BoxDecoration(
            color: Theme.of(context).colorScheme.primary.withAlpha(20),
            borderRadius: BorderRadius.circular(20),
          ),
          child: Text(
            time,
            style: TextStyle(
              color: Theme.of(context).colorScheme.primary,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        onTap: () async {
          final TimeOfDay? picked = await showTimePicker(
            context: context,
            initialTime: TimeOfDay.now(),
          );
          if (picked != null) {
            final formatted = picked.format(context);
            ref
                .read(iqamahTimesProvider.notifier)
                .updateTime(prayer, formatted);
          }
        },
      ),
    );
  }
}
