import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';

class HomePage extends ConsumerWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final prayerTimesAsync = ref.watch(prayerTimesProvider);

    return Scaffold(
      body: SafeArea(
        child: prayerTimesAsync.when(
          data: (prayerData) {
            return CustomScrollView(
              slivers: [
                SliverAppBar(
                  expandedHeight: 250.0,
                  floating: false,
                  pinned: true,
                  flexibleSpace: FlexibleSpaceBar(
                    title: const Text('DeenLife'),
                    background: Container(
                      decoration: const BoxDecoration(
                        gradient: LinearGradient(
                          colors: [Color(0xFF00B4D8), Color(0xFF0077B6)],
                          begin: Alignment.topLeft,
                          end: Alignment.bottomRight,
                        ),
                      ),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          const SizedBox(height: 40),
                          const Text(
                            'Next Prayer',
                            style: TextStyle(
                              color: Colors.white70,
                              fontSize: 16,
                            ),
                          ),
                          const SizedBox(height: 8),
                          Text(
                            prayerData.nextPrayerName,
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 36,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          Text(
                            DateFormat.jm().format(prayerData.nextPrayerTime),
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 24,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
                SliverToBoxAdapter(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          'Today\'s Prayers',
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 16),
                        _PrayerTile(name: 'Fajr', time: prayerData.fajr, isNext: prayerData.nextPrayerName == 'Fajr'),
                        _PrayerTile(name: 'Sunrise', time: prayerData.sunrise, isNext: prayerData.nextPrayerName == 'Sunrise'),
                        _PrayerTile(name: 'Dhuhr', time: prayerData.dhuhr, isNext: prayerData.nextPrayerName == 'Dhuhr'),
                        _PrayerTile(name: 'Asr', time: prayerData.asr, isNext: prayerData.nextPrayerName == 'Asr'),
                        _PrayerTile(name: 'Maghrib', time: prayerData.maghrib, isNext: prayerData.nextPrayerName == 'Maghrib'),
                        _PrayerTile(name: 'Isha', time: prayerData.isha, isNext: prayerData.nextPrayerName == 'Isha'),
                      ],
                    ),
                  ),
                ),
              ],
            );
          },
          loading: () => const Center(child: CircularProgressIndicator()),
          error: (error, stack) => Center(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Text(
                'Error: ${error.toString()}\nPlease enable location services.',
                textAlign: TextAlign.center,
                style: const TextStyle(color: Colors.red),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _PrayerTile extends StatelessWidget {
  final String name;
  final DateTime time;
  final bool isNext;

  const _PrayerTile({
    required this.name,
    required this.time,
    required this.isNext,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
      decoration: BoxDecoration(
        color: isNext ? Theme.of(context).colorScheme.primary.withOpacity(0.1) : Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(16),
        border: isNext ? Border.all(color: Theme.of(context).colorScheme.primary, width: 2) : Border.all(color: Colors.grey.withOpacity(0.2)),
        boxShadow: [
          if (!isNext)
            BoxShadow(
              color: Colors.black.withOpacity(0.05),
              blurRadius: 10,
              offset: const Offset(0, 4),
            ),
        ],
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            name,
            style: TextStyle(
              fontSize: 18,
              fontWeight: isNext ? FontWeight.bold : FontWeight.normal,
              color: isNext ? Theme.of(context).colorScheme.primary : null,
            ),
          ),
          Text(
            DateFormat.jm().format(time),
            style: TextStyle(
              fontSize: 18,
              fontWeight: isNext ? FontWeight.bold : FontWeight.w500,
              color: isNext ? Theme.of(context).colorScheme.primary : null,
            ),
          ),
        ],
      ),
    );
  }
}
