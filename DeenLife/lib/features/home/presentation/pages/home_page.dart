import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';
import '../../../asmaul_husna/presentation/pages/asmaul_husna_page.dart';
import '../../../zakat/presentation/pages/zakat_calculator_page.dart';
import '../../../kalima/presentation/pages/kalima_page.dart';
import '../../../hadith/presentation/pages/hadith_page.dart';
import '../../../quiz/presentation/pages/quiz_page.dart';
import '../../../settings/presentation/pages/settings_page.dart';

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
                  actions: [
                    IconButton(
                      icon: const Icon(Icons.settings, color: Colors.white),
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => const SettingsPage()),
                        );
                      },
                    ),
                  ],
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
                        const SizedBox(height: 24),
                        const Text(
                          'Quick Links',
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 16),
                        Row(
                          children: [
                            Expanded(
                              child: GestureDetector(
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (context) => AsmaulHusnaPage()),
                                  );
                                },
                                child: Container(
                                  padding: const EdgeInsets.all(16),
                                  decoration: BoxDecoration(
                                    gradient: const LinearGradient(
                                      colors: [Color(0xFF00B4D8), Color(0xFF90E0EF)],
                                      begin: Alignment.topLeft,
                                      end: Alignment.bottomRight,
                                    ),
                                    borderRadius: BorderRadius.circular(16),
                                  ),
                                  child: const Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Icon(Icons.star, color: Colors.white, size: 32),
                                      SizedBox(height: 8),
                                      Text(
                                        'Asmaul Husna',
                                        style: TextStyle(
                                          color: Colors.white,
                                          fontSize: 16,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            ),
                            const SizedBox(width: 16),
                            Expanded(
                              child: GestureDetector(
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (context) => KalimaPage()),
                                  );
                                },
                                child: Container(
                                  padding: const EdgeInsets.all(16),
                                  decoration: BoxDecoration(
                                    gradient: const LinearGradient(
                                      colors: [Color(0xFFFF9F1C), Color(0xFFFFBF69)],
                                      begin: Alignment.topLeft,
                                      end: Alignment.bottomRight,
                                    ),
                                    borderRadius: BorderRadius.circular(16),
                                  ),
                                  child: const Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Icon(Icons.menu_book, color: Colors.white, size: 32),
                                      SizedBox(height: 8),
                                      Text(
                                        '6 Kalimas',
                                        style: TextStyle(
                                          color: Colors.white,
                                          fontSize: 16,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            ),
                            const SizedBox(width: 16),
                            Expanded(
                              child: GestureDetector(
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (context) => const ZakatCalculatorPage()),
                                  );
                                },
                                child: Container(
                                  padding: const EdgeInsets.all(16),
                                  decoration: BoxDecoration(
                                    gradient: const LinearGradient(
                                      colors: [Color(0xFF2EC4B6), Color(0xFFCBF3F0)],
                                      begin: Alignment.topLeft,
                                      end: Alignment.bottomRight,
                                    ),
                                    borderRadius: BorderRadius.circular(16),
                                  ),
                                  child: const Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Icon(Icons.calculate, color: Colors.white, size: 32),
                                      SizedBox(height: 8),
                                      Text(
                                        'Zakat Calc',
                                        style: TextStyle(
                                          color: Colors.white,
                                          fontSize: 16,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            ),
                            const SizedBox(width: 16),
                            Expanded(
                              child: GestureDetector(
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (context) => HadithPage()),
                                  );
                                },
                                child: Container(
                                  padding: const EdgeInsets.all(16),
                                  decoration: BoxDecoration(
                                    gradient: const LinearGradient(
                                      colors: [Color(0xFF8338EC), Color(0xFFCBA6F7)],
                                      begin: Alignment.topLeft,
                                      end: Alignment.bottomRight,
                                    ),
                                    borderRadius: BorderRadius.circular(16),
                                  ),
                                  child: const Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Icon(Icons.auto_stories, color: Colors.white, size: 32),
                                      SizedBox(height: 8),
                                      Text(
                                        'Hadith',
                                        style: TextStyle(
                                          color: Colors.white,
                                          fontSize: 16,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 16),
                        GestureDetector(
                          onTap: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(builder: (context) => const QuizPage()),
                            );
                          },
                          child: Container(
                            padding: const EdgeInsets.all(20),
                            decoration: BoxDecoration(
                              gradient: const LinearGradient(
                                colors: [Color(0xFFFF595E), Color(0xFFFF924C)],
                                begin: Alignment.topLeft,
                                end: Alignment.bottomRight,
                              ),
                              borderRadius: BorderRadius.circular(16),
                            ),
                            child: const Row(
                              children: [
                                Icon(Icons.quiz, color: Colors.white, size: 36),
                                SizedBox(width: 16),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        'Islamic Quiz',
                                        style: TextStyle(
                                          color: Colors.white,
                                          fontSize: 18,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                      Text(
                                        'Test your Islamic knowledge',
                                        style: TextStyle(
                                          color: Colors.white70,
                                          fontSize: 14,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                                Icon(Icons.arrow_forward_ios, color: Colors.white),
                              ],
                            ),
                          ),
                        ),
                        const SizedBox(height: 32),
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

class _PrayerTile extends StatefulWidget {
  final String name;
  final DateTime time;
  final bool isNext;

  const _PrayerTile({
    required this.name,
    required this.time,
    required this.isNext,
  });

  @override
  State<_PrayerTile> createState() => _PrayerTileState();
}

class _PrayerTileState extends State<_PrayerTile> {
  bool _isPrayed = false;

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 12),
      decoration: BoxDecoration(
        color: widget.isNext ? Theme.of(context).colorScheme.primary.withOpacity(0.1) : Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(16),
        border: widget.isNext ? Border.all(color: Theme.of(context).colorScheme.primary, width: 2) : Border.all(color: Colors.grey.withOpacity(0.2)),
        boxShadow: [
          if (!widget.isNext)
            BoxShadow(
              color: Colors.black.withOpacity(0.05),
              blurRadius: 10,
              offset: const Offset(0, 4),
            ),
        ],
      ),
      child: Row(
        children: [
          Checkbox(
            value: _isPrayed,
            onChanged: (val) {
              setState(() {
                _isPrayed = val ?? false;
              });
            },
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(4)),
            activeColor: Theme.of(context).colorScheme.primary,
          ),
          Expanded(
            child: Text(
              widget.name,
              style: TextStyle(
                fontSize: 18,
                fontWeight: widget.isNext ? FontWeight.bold : FontWeight.normal,
                color: widget.isNext ? Theme.of(context).colorScheme.primary : null,
              ),
            ),
          ),
          Text(
            DateFormat.jm().format(widget.time),
            style: TextStyle(
              fontSize: 18,
              fontWeight: widget.isNext ? FontWeight.bold : FontWeight.w500,
              color: widget.isNext ? Theme.of(context).colorScheme.primary : null,
            ),
          ),
          const SizedBox(width: 8),
        ],
      ),
    );
  }
}

