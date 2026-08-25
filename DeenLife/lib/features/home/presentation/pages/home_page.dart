import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';
import '../../../prayer_times/presentation/providers/prayer_completion_provider.dart';
import '../../../emotions/presentation/pages/emotions_page.dart';
import '../../../prayer_times/domain/models/prayer_data.dart';
import '../../../masjid/presentation/pages/set_masjid_times_page.dart';

class HomePage extends ConsumerWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final prayerDataAsync = ref.watch(prayerTimesProvider);
    final userIqamahTimes = ref.watch(iqamahTimesProvider);
    final prayerCompletion = ref.watch(prayerCompletionProvider);

    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      body: prayerDataAsync.when(
        data: (prayerData) {
          return RefreshIndicator(
            onRefresh: () async {
              ref.invalidate(prayerTimesProvider);
            },
            child: CustomScrollView(
              slivers: [
                _buildSliverAppBar(context, prayerData, userIqamahTimes),
                SliverToBoxAdapter(
                  child: Column(
                    children: [
                      const SizedBox(height: 16),
                      _buildPrayerProgress(context, prayerCompletion),
                      _buildHowDoYouFeel(context),

                      const SizedBox(height: 24),
                      _buildSectionTitle(context, 'Today\'s Prayers'),
                      _buildPrayerTimeline(prayerData),

                      const SizedBox(height: 24),
                      _buildSectionTitle(context, 'Daily Inspiration'),
                      _buildDailyFeed(context),
                      const SizedBox(height: 32),
                    ],
                  ),
                ),
              ],
            ),
          );
        },
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (error, stack) => Center(child: Text(error.toString())),
      ),
    );
  }

  Widget _buildSliverAppBar(
    BuildContext context,
    PrayerData prayerData,
    Map<String, String> userIqamahTimes,
  ) {
    return SliverAppBar(
      expandedHeight: 320,
      pinned: true,
      backgroundColor: const Color(0xFF1E3A5F),
      flexibleSpace: FlexibleSpaceBar(
        background: Stack(
          fit: StackFit.expand,
          children: [
            Image.asset('assets/home_header_bg.jpg', fit: BoxFit.cover),
            Container(
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                  colors: [
                    Colors.black.withAlpha(50),
                    Colors.black.withAlpha(180),
                  ],
                ),
              ),
            ),
            SafeArea(
              child: Padding(
                padding: const EdgeInsets.symmetric(
                  horizontal: 20.0,
                  vertical: 12.0,
                ),
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              prayerData.city,
                              style: const TextStyle(
                                color: Colors.white,
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            Text(
                              prayerData.hijriDate,
                              style: TextStyle(
                                color: Colors.white.withAlpha(200),
                                fontSize: 14,
                              ),
                            ),
                          ],
                        ),
                        Row(
                          children: [
                            _iconButton(Icons.notifications_none),
                            _iconButton(Icons.location_on_outlined),
                          ],
                        ),
                      ],
                    ),
                    const Spacer(),
                    _buildNextPrayerWidget(
                      context,
                      prayerData,
                      userIqamahTimes,
                    ),
                    const Spacer(),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildNextPrayerWidget(
    BuildContext context,
    PrayerData prayerData,
    Map<String, String> userIqamahTimes,
  ) {
    final now = DateTime.now();
    final remaining = prayerData.nextPrayerTime.difference(now);
    final hours = remaining.inHours;
    final minutes = remaining.inMinutes % 60;
    String timeStr = hours > 0 ? '$hours hr $minutes min' : '$minutes min';

    String iqamahTime = userIqamahTimes[prayerData.nextPrayerName] ?? 'Not Set';

    return Column(
      children: [
        Text(
          timeStr,
          style: const TextStyle(
            color: Colors.white,
            fontSize: 48,
            fontWeight: FontWeight.bold,
            letterSpacing: -1,
          ),
        ),
        Text(
          'Left until \${prayerData.nextPrayerName}',
          style: TextStyle(color: Colors.white.withAlpha(220), fontSize: 16),
        ),
        const SizedBox(height: 24),
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
          decoration: BoxDecoration(
            color: Colors.black.withAlpha(80),
            borderRadius: BorderRadius.circular(20),
            border: Border.all(color: Colors.white.withAlpha(30)),
          ),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Icon(Icons.mosque, color: Colors.white70, size: 20),
              const SizedBox(width: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Iqamah Time',
                    style: TextStyle(color: Colors.white70, fontSize: 12),
                  ),
                  Text(
                    iqamahTime,
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
              const SizedBox(width: 24),
              GestureDetector(
                onTap: () => Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const SetMasjidTimesPage(),
                  ),
                ),
                child: const Icon(Icons.edit, color: Colors.white54, size: 18),
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _iconButton(IconData icon) => Container(
    margin: const EdgeInsets.only(left: 8),
    padding: const EdgeInsets.all(8),
    decoration: BoxDecoration(
      color: Colors.white.withAlpha(50),
      shape: BoxShape.circle,
    ),
    child: Icon(icon, color: Colors.white, size: 20),
  );

  Widget _buildSectionTitle(BuildContext context, String title) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 8.0),
      child: Align(
        alignment: Alignment.centerLeft,
        child: Text(
          context.tr(title),
          style: const TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
            color: Color(0xFF1E3A5F),
          ),
        ),
      ),
    );
  }

  Widget _buildPrayerProgress(
    BuildContext context,
    Map<String, bool> completion,
  ) {
    int prayedCount = completion.values.where((v) => v).length;
    double progress = prayedCount / 5;

    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 20),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(color: Colors.black.withAlpha(5), blurRadius: 10),
        ],
      ),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                context.tr('Prayer Progress'),
                style: const TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 16,
                ),
              ),
              Text(
                '$prayedCount/5',
                style: const TextStyle(
                  fontWeight: FontWeight.bold,
                  color: Colors.blue,
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          LinearProgressIndicator(
            value: progress,
            backgroundColor: Colors.blue[50],
            color: Colors.blue,
            minHeight: 8,
            borderRadius: BorderRadius.circular(4),
          ),
        ],
      ),
    );
  }

  Widget _buildHowDoYouFeel(BuildContext context) => GestureDetector(
    onTap: () => Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => const EmotionsPage()),
    ),
    child: Container(
      margin: const EdgeInsets.only(left: 20, right: 20, top: 16),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF1E3A5F),
        borderRadius: BorderRadius.circular(16),
      ),
      child: Row(
        children: [
          const Icon(
            Icons.sentiment_satisfied_alt,
            color: Colors.white,
            size: 28,
          ),
          const SizedBox(width: 16),
          Text(
            context.tr('How are you feeling today?'),
            style: const TextStyle(
              color: Colors.white,
              fontSize: 16,
              fontWeight: FontWeight.w600,
            ),
          ),
          const Spacer(),
          Icon(
            Icons.arrow_forward_ios,
            color: Colors.white.withAlpha(150),
            size: 14,
          ),
        ],
      ),
    ),
  );

  Widget _buildPrayerTimeline(PrayerData prayerData) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: Column(
        children: [
          _PrayerTile(
            name: 'Fajr',
            time: prayerData.fajr,
            isNext: prayerData.nextPrayerName == 'Fajr',
          ),
          _PrayerTile(
            name: 'Sunrise',
            time: prayerData.sunrise,
            isNext: prayerData.nextPrayerName == 'Sunrise',
          ),
          _PrayerTile(
            name: 'Dhuhr',
            time: prayerData.dhuhr,
            isNext: prayerData.nextPrayerName == 'Dhuhr',
          ),
          _PrayerTile(
            name: 'Asr',
            time: prayerData.asr,
            isNext: prayerData.nextPrayerName == 'Asr',
          ),
          _PrayerTile(
            name: 'Maghrib',
            time: prayerData.maghrib,
            isNext: prayerData.nextPrayerName == 'Maghrib',
          ),
          _PrayerTile(
            name: 'Isha',
            time: prayerData.isha,
            isNext: prayerData.nextPrayerName == 'Isha',
          ),
        ],
      ),
    );
  }

  Widget _buildDailyFeed(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: Column(
        children: [
          _buildFeedCard(
            context,
            title: 'Ayat of the Day',
            content: 'Indeed, Allah is with the patient.',
            reference: 'Surah Al-Baqarah 2:153',
            icon: Icons.menu_book,
            color: Colors.teal,
          ),
          const SizedBox(height: 16),
          _buildFeedCard(
            context,
            title: 'Hadith of the Day',
            content: 'The best among you are those who have the best manners and character.',
            reference: 'Sahih al-Bukhari 3559',
            icon: Icons.auto_awesome,
            color: Colors.brown,
          ),
        ],
      ),
    );
  }

  Widget _buildFeedCard(
    BuildContext context, {
    required String title,
    required String content,
    required String reference,
    required IconData icon,
    required Color color,
  }) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withAlpha(5),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(icon, color: color, size: 20),
              const SizedBox(width: 8),
              Text(
                context.tr(title),
                style: TextStyle(
                  color: color,
                  fontWeight: FontWeight.bold,
                  fontSize: 14,
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          Text(
            content,
            style: const TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w500,
              fontStyle: FontStyle.italic,
            ),
          ),
          const SizedBox(height: 12),
          Text(
            reference,
            style: const TextStyle(
              fontSize: 12,
              color: Colors.grey,
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
    );
  }
}

class _PrayerTile extends ConsumerWidget {
  final String name;
  final DateTime time;
  final bool isNext;

  const _PrayerTile({
    required this.name,
    required this.time,
    required this.isNext,
  });

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final completion = ref.watch(prayerCompletionProvider);
    final isPrayed = completion[name] ?? false;
    final isSunrise = name.toLowerCase() == 'sunrise';

    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: isNext ? const Color(0xFF1E3A5F).withAlpha(15) : Colors.white,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(
          color: isNext
              ? const Color(0xFF1E3A5F).withAlpha(50)
              : Colors.transparent,
        ),
        boxShadow: isNext
            ? []
            : [BoxShadow(color: Colors.black.withAlpha(2), blurRadius: 5)],
      ),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            CircleAvatar(
              backgroundColor: isNext
                  ? const Color(0xFF1E3A5F)
                  : Colors.grey.withAlpha(20),
              child: Icon(
                _getIconForPrayer(name),
                color: isNext ? Colors.white : Colors.grey[600],
              ),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: Text(
                name,
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: isNext ? FontWeight.bold : FontWeight.normal,
                ),
              ),
            ),
            Text(
              DateFormat.jm().format(time),
              style: TextStyle(
                fontWeight: isNext ? FontWeight.bold : FontWeight.w600,
                fontSize: 15,
              ),
            ),
            const SizedBox(width: 16),
            if (!isSunrise)
              GestureDetector(
                onTap: () => ref
                    .read(prayerCompletionProvider.notifier)
                    .togglePrayer(name),
                child: Icon(
                  isPrayed ? Icons.check_circle : Icons.radio_button_unchecked,
                  color: isPrayed ? Colors.green : Colors.grey[300],
                ),
              )
            else
              const SizedBox(width: 24),
          ],
        ),
      ),
    );
  }

  IconData _getIconForPrayer(String name) {
    switch (name.toLowerCase()) {
      case 'fajr':
        return Icons.wb_twilight;
      case 'sunrise':
        return Icons.wb_sunny_outlined;
      case 'dhuhr':
        return Icons.wb_sunny;
      case 'asr':
        return Icons.wb_cloudy_outlined;
      case 'maghrib':
        return Icons.nights_stay_outlined;
      case 'isha':
        return Icons.nights_stay;
      default:
        return Icons.access_time;
    }
  }
}
