import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import 'package:hijri/hijri_calendar.dart';
import 'package:adhan/adhan.dart';
import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';
import 'set_masjid_times_screen.dart';

class MasjidDetailScreen extends ConsumerStatefulWidget {
  const MasjidDetailScreen({super.key});

  @override
  ConsumerState<MasjidDetailScreen> createState() => _MasjidDetailScreenState();
}

class _MasjidDetailScreenState extends ConsumerState<MasjidDetailScreen> {
  final PageController _pageController = PageController(initialPage: 0);
  int _currentPageIndex = 0;
  Timer? _timer;
  String _timeRemaining = "";

  @override
  void initState() {
    super.initState();
    _startTimer();
  }

  void _startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (!mounted) return;
      // We will update the time remaining in the build method dynamically
      setState(() {}); 
    });
  }

  @override
  void dispose() {
    _timer?.cancel();
    _pageController.dispose();
    super.dispose();
  }

  String _formatTime(DateTime time) {
    return DateFormat('h:mm a').format(time);
  }

  @override
  Widget build(BuildContext context) {
    final sevenDaysAsync = ref.watch(sevenDaysPrayerProvider);
    final iqamahTimes = ref.watch(iqamahTimesProvider);
    final prayerDataAsync = ref.watch(prayerTimesProvider);

    return Scaffold(
      backgroundColor: const Color(0xFFF4F7F6),
      appBar: AppBar(
        title: Text(
          prayerDataAsync.valueOrNull?.city ?? 'Loading...',
          style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.white, fontSize: 18),
        ),
        backgroundColor: const Color(0xFF1B2A3B), // Dark slate blue
        elevation: 0,
        centerTitle: false,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back_ios_new, color: Colors.white, size: 18),
          onPressed: () => Navigator.pop(context),
        ),
        actions: [
          IconButton(icon: const Icon(Icons.refresh, color: Colors.white), onPressed: () {
            ref.invalidate(sevenDaysPrayerProvider);
            ref.invalidate(prayerTimesProvider);
          }),
          IconButton(icon: const Icon(Icons.favorite_border, color: Colors.white), onPressed: () {}),
          IconButton(icon: const Icon(Icons.more_vert, color: Colors.white), onPressed: () {}),
        ],
      ),
      body: sevenDaysAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (error, stack) => Center(child: Text('Error: $error')),
        data: (sevenDaysList) {
          if (sevenDaysList.isEmpty) return const SizedBox();
          
          final currentDayTimes = sevenDaysList[_currentPageIndex];
          final currentDate = DateTime.now().add(Duration(days: _currentPageIndex));
          final hijri = HijriCalendar.fromDate(currentDate);

          // Calculate time till next prayer
          Prayer nextPrayer = currentDayTimes.nextPrayer();
          DateTime nextPrayerTime = currentDayTimes.timeForPrayer(nextPrayer) ?? DateTime.now();
          if (nextPrayer == Prayer.none && _currentPageIndex == 0) {
            // Next prayer is tomorrow fajr
            final tmrw = sevenDaysList.length > 1 ? sevenDaysList[1] : currentDayTimes;
            nextPrayerTime = tmrw.fajr;
          }
          
          Duration diff = nextPrayerTime.difference(DateTime.now());
          if (diff.isNegative) diff = const Duration(seconds: 0);
          final hours = diff.inHours;
          final mins = diff.inMinutes.remainder(60);
          final secs = diff.inSeconds.remainder(60);
          _timeRemaining = '${hours}h ${mins}m ${secs}s till next Athan';

          return SingleChildScrollView(
            child: Column(
              children: [
                // Top Header Card
                Container(
                  margin: const EdgeInsets.all(16),
                  height: 120,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(16),
                    image: const DecorationImage(
                      image: AssetImage('assets/quran_pattern_v2.jpg'), // Using available asset
                      fit: BoxFit.cover,
                      colorFilter: ColorFilter.mode(Colors.black38, BlendMode.darken),
                    ),
                  ),
                  child: Stack(
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              DateFormat('EEEE, MMM d').format(currentDate),
                              style: const TextStyle(color: Colors.white, fontSize: 14),
                            ),
                            Text(
                              '${hijri.hDay} ${hijri.longMonthName}',
                              style: const TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                      ),
                      Positioned(
                        bottom: 16,
                        right: 16,
                        child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                          decoration: BoxDecoration(
                            color: Colors.black54,
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text(
                            _currentPageIndex == 0 ? _timeRemaining : 'Swipe to return to today',
                            style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 12),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),

                // Main Timings Table Section
                Container(
                  margin: const EdgeInsets.symmetric(horizontal: 16),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                    boxShadow: [
                      BoxShadow(color: Colors.black.withAlpha(10), blurRadius: 10, offset: const Offset(0, 4)),
                    ],
                  ),
                  child: Column(
                    children: [
                      // Table Header
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                        decoration: const BoxDecoration(
                          color: Color(0xFF1B2A3B),
                          borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
                        ),
                        child: Row(
                          children: const [
                            Spacer(),
                            SizedBox(width: 60, child: Text('Starts', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold))),
                            SizedBox(width: 80, child: Text('Iqamah', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold), textAlign: TextAlign.right)),
                          ],
                        ),
                      ),
                      
                      // Swipable 7-day Table Body
                      SizedBox(
                        height: 380,
                        child: PageView.builder(
                          controller: _pageController,
                          onPageChanged: (index) {
                            setState(() {
                              _currentPageIndex = index;
                            });
                          },
                          itemCount: sevenDaysList.length,
                          itemBuilder: (context, pageIndex) {
                            return _buildTimingsTable(sevenDaysList[pageIndex], iqamahTimes, currentDate);
                          },
                        ),
                      ),
                      
                      // Dots Indicator
                      Padding(
                        padding: const EdgeInsets.symmetric(vertical: 12.0),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: List.generate(sevenDaysList.length, (index) {
                            return Container(
                              margin: const EdgeInsets.symmetric(horizontal: 4),
                              width: 8,
                              height: 8,
                              decoration: BoxDecoration(
                                shape: BoxShape.circle,
                                color: _currentPageIndex == index ? const Color(0xFF1B2A3B) : Colors.grey.shade300,
                              ),
                            );
                          }),
                        ),
                      ),
                      
                      // Bottom Link
                      Container(
                        width: double.infinity,
                        padding: const EdgeInsets.symmetric(vertical: 16),
                        decoration: const BoxDecoration(
                          color: Color(0xFF1B2A3B),
                          borderRadius: BorderRadius.vertical(bottom: Radius.circular(16)),
                        ),
                        child: const Text(
                          'Monthly Prayer Timings',
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            color: Colors.white,
                            decoration: TextDecoration.underline,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                
                const SizedBox(height: 16),
                
                // Sunrise and Sunset Cards
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16.0),
                  child: Row(
                    children: [
                      Expanded(
                        child: _buildSunCard(Icons.wb_twilight, 'Sunrise', _formatTime(currentDayTimes.sunrise)),
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: _buildSunCard(Icons.wb_sunny_outlined, 'Sunset', _formatTime(currentDayTimes.maghrib)),
                      ),
                    ],
                  ),
                ),
                
                const SizedBox(height: 32),
              ],
            ),
          );
        },
      ),
    );
  }

  Widget _buildTimingsTable(PrayerTimes pt, Map<String, String> iqamah, DateTime date) {
    bool isFriday = date.weekday == DateTime.friday;

    return Column(
      children: [
        _buildTimingRow('Fajr', Icons.wb_twilight, _formatTime(pt.fajr), iqamah['Fajr'] ?? '0:00', pt.currentPrayer() == Prayer.fajr),
        _buildTimingRow('Dhuhr', Icons.wb_sunny, _formatTime(pt.dhuhr), iqamah['Dhuhr'] ?? '0:00', pt.currentPrayer() == Prayer.dhuhr),
        _buildTimingRow('Asr', Icons.wb_cloudy_outlined, _formatTime(pt.asr), iqamah['Asr'] ?? '0:00', pt.currentPrayer() == Prayer.asr),
        _buildTimingRow('Maghrib', Icons.nights_stay_outlined, _formatTime(pt.maghrib), iqamah['Maghrib'] ?? '0:00', pt.currentPrayer() == Prayer.maghrib),
        _buildTimingRow('Isha', Icons.nights_stay, _formatTime(pt.isha), iqamah['Isha'] ?? '0:00', pt.currentPrayer() == Prayer.isha),
        
        if (isFriday)
          Container(
            width: double.infinity,
            color: Colors.blue.shade700,
            padding: const EdgeInsets.symmetric(vertical: 12),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: const [
                Icon(Icons.nightlight_round, color: Colors.white, size: 16),
                SizedBox(width: 8),
                Text('Jumu\'ah', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16)),
              ],
            ),
          ),
        if (isFriday)
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Column(
                  children: [
                    Text(_formatTime(pt.dhuhr), style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                    const Text('ATHAN', style: TextStyle(color: Colors.grey, fontSize: 12)),
                  ],
                ),
                Column(
                  children: [
                    Text(iqamah['Jummah'] ?? '1:30 PM', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                    const Text('JUMU\'AH', style: TextStyle(color: Colors.grey, fontSize: 12)),
                  ],
                ),
              ],
            ),
          ),
      ],
    );
  }

  Widget _buildTimingRow(String name, IconData icon, String starts, String iqamah, bool isCurrent) {
    return Container(
      color: isCurrent ? Colors.blue.shade600 : Colors.transparent,
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      child: Row(
        children: [
          Icon(icon, color: isCurrent ? Colors.yellow : Colors.orange, size: 24),
          const SizedBox(width: 16),
          Expanded(
            child: Text(
              name,
              style: TextStyle(
                color: isCurrent ? Colors.white : Colors.black87,
                fontSize: 16,
              ),
            ),
          ),
          SizedBox(
            width: 60,
            child: Text(
              starts,
              style: TextStyle(
                color: isCurrent ? Colors.white : Colors.black87,
              ),
            ),
          ),
          SizedBox(
            width: 80,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                Icon(Icons.volume_off, color: isCurrent ? Colors.white70 : Colors.grey, size: 16),
                const SizedBox(width: 4),
                Text(
                  iqamah,
                  style: TextStyle(
                    color: isCurrent ? Colors.white : Colors.black,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSunCard(IconData icon, String title, String time) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF1B2A3B),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Row(
        children: [
          Icon(icon, color: Colors.white, size: 32),
          const SizedBox(width: 12),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(title, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
              Text(time, style: const TextStyle(color: Colors.white70, fontSize: 12)),
            ],
          ),
        ],
      ),
    );
  }
}
