import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';

import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';
import '../../../prayer_times/domain/models/prayer_data.dart';
import '../../../masjid/presentation/screens/set_masjid_times_screen.dart';
import '../../../masjid/presentation/screens/masjid_detail_screen.dart';
import '../../../qibla/presentation/screens/qibla_screen.dart';
import '../../../emotions/presentation/screens/emotions_screen.dart';
import '../../../duas/presentation/screens/dua_screen.dart';

class HomeScreen extends ConsumerWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final prayerDataAsync = ref.watch(prayerTimesProvider);
    final userIqamahTimes = ref.watch(iqamahTimesProvider);

    return Scaffold(
      backgroundColor: Colors.white,
      body: prayerDataAsync.when(
        data: (prayerData) {
          return RefreshIndicator(
            onRefresh: () async {
              ref.invalidate(prayerTimesProvider);
            },
            child: CustomScrollView(
              physics: const BouncingScrollPhysics(),
              slivers: [
                _buildSliverAppBar(context, prayerData, userIqamahTimes, ref),
                SliverToBoxAdapter(
                  child: Column(
                    children: [
                      _buildQuickTabs(context),
                      const Divider(height: 1, color: Colors.black12),
                      const SizedBox(height: 24),
                      _buildFeatureGrid(context),
                      const SizedBox(height: 24),
                      _buildMoodTracker(context),
                      const SizedBox(height: 24),
                      _buildDailyContentFeed(context),
                      const SizedBox(height: 24),
                      _buildNearbyMasjidBanner(context),
                      const SizedBox(height: 24),
                      _buildTodayPrayersList(context, prayerData, userIqamahTimes),
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
    WidgetRef ref,
  ) {
    return SliverAppBar(
      expandedHeight: 270,
      pinned: true,
      backgroundColor: const Color(0xFF1B3B2B),
      elevation: 0,
      title: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            prayerData.city,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 22,
              fontWeight: FontWeight.bold,
            ),
          ),
          Text(
            prayerData.hijriDate,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 14,
            ),
          ),
        ],
      ),
      actions: [
        _iconButton(Icons.search, () {
          context.push('/quran_search');
        }),
        _iconButton(Icons.refresh, () {
          ref.invalidate(prayerTimesProvider);
        }),
        _iconButton(Icons.location_on_outlined, () {
          context.push('/masjid_list');
        }),
        const SizedBox(width: 8),
      ],
      flexibleSpace: FlexibleSpaceBar(
        background: Stack(
          fit: StackFit.expand,
          children: [
            Image.asset(
              'assets/home_page_header_${DateTime.now().weekday}.jpg',
              fit: BoxFit.cover,
            ),
            // Gradient Overlay
            Container(
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                  colors: [
                    Colors.black.withAlpha(100),
                    const Color(0xFFC7A55C).withAlpha(120),
                  ],
                ),
              ),
            ),
            SafeArea(
              child: Padding(
                padding: const EdgeInsets.only(top: kToolbarHeight, left: 20, right: 20, bottom: 35),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    // Hexagon Next Prayer
                    Expanded(
                      flex: 5,
                      child: _buildHexagonPrayer(prayerData, userIqamahTimes[prayerData.nextPrayerName] ?? "Not Set"),
                    ),
                    // Right Side Info
                    Expanded(
                      flex: 4,
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.end,
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          _buildCountdown(prayerData),
                          const SizedBox(height: 8),
                          const Text(
                            'Prayer timing for',
                            style: TextStyle(color: Colors.white, fontSize: 12),
                            textAlign: TextAlign.right,
                          ),
                          const Text(
                            'Gulshan Society Jame Masjid', // Hardcoded for demo, normally dynamic
                            style: TextStyle(
                              color: Colors.white,
                              fontSize: 14,
                              fontWeight: FontWeight.bold,
                            ),
                            textAlign: TextAlign.right,
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildHexagonPrayer(PrayerData prayerData, String iqamahTime) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.end,
      children: [
        SizedBox(
          width: 120,
          height: 130,
          child: Stack(
            alignment: Alignment.center,
            children: [
              CustomPaint(
                size: const Size(120, 130),
                painter: HexagonPainter(
                  color: Colors.white.withAlpha(50),
                  strokeColor: Colors.white.withAlpha(150),
                ),
              ),
              Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    prayerData.nextPrayerName.toUpperCase(),
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 10,
                      fontWeight: FontWeight.w600,
                      letterSpacing: 1.2,
                    ),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    _formatTimeOnly(prayerData.nextPrayerTime),
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 26,
                      fontWeight: FontWeight.bold,
                      height: 1.0,
                    ),
                  ),
                  Text(
                    _formatAmPm(prayerData.nextPrayerTime),
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 14,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Row(
                    mainAxisSize: MainAxisSize.min,
                    children: const [
                      Text(
                        'All Timings',
                        style: TextStyle(color: Colors.white, fontSize: 9),
                      ),
                      Icon(Icons.arrow_forward, color: Colors.white, size: 10),
                    ],
                  ),
                ],
              ),
            ],
          ),
        ),
        const SizedBox(height: 16),
        Text(
          'Iqamah $iqamahTime',
          style: const TextStyle(
            color: Colors.white,
            fontSize: 14,
            fontWeight: FontWeight.w600,
          ),
        ),
      ],
    );
  }

  Widget _buildCountdown(PrayerData prayerData) {
    return StreamBuilder(
      stream: Stream.periodic(const Duration(seconds: 1)),
      builder: (context, snapshot) {
        final now = DateTime.now();
        final remaining = prayerData.nextPrayerCountdownTime.difference(now);
        final hours = remaining.inHours;
        final minutes = remaining.inMinutes % 60;
        
        String timeStr;
        if (remaining.isNegative) {
           timeStr = "0 min";
        } else if (hours > 0) {
           timeStr = '$hours hr $minutes min';
        } else {
           timeStr = '$minutes min';
        }

        return Column(
          crossAxisAlignment: CrossAxisAlignment.end,
          children: [
            Text(
              timeStr,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 28,
                fontWeight: FontWeight.bold,
              ),
            ),
            Text(
              'till ${prayerData.nextPrayerName}',
              style: const TextStyle(
                color: Colors.white70,
                fontSize: 14,
              ),
            ),
          ],
        );
      }
    );
  }

  Widget _buildQuickTabs(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          _buildQuickTabItem(Icons.menu_book, 'VERSE', context, () => context.push('/quran')),
          _buildVerticalDivider(),
          _buildQuickTabItem(Icons.star_border, 'HADITH', context, () => context.push('/hadith')),
          _buildVerticalDivider(),
          _buildQuickTabItem(Icons.clean_hands_outlined, 'DUA', context, () => context.push('/dua')),
        ],
      ),
    );
  }

  Widget _buildVerticalDivider() {
    return Container(
      height: 40,
      width: 1,
      color: Colors.black12,
    );
  }

  Widget _buildQuickTabItem(IconData icon, String label, BuildContext context, VoidCallback onTap) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(12),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: [
            Icon(icon, color: Colors.grey.shade400, size: 32),
            const SizedBox(height: 8),
            Text(
              label,
              style: TextStyle(
                color: Colors.grey.shade400,
                fontSize: 12,
                fontWeight: FontWeight.bold,
                letterSpacing: 1.2,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildFeatureGrid(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          _buildGridItem(Icons.explore, 'Qibla', const Color(0xFFE5F8ED), Colors.green, () {
            Navigator.push(context, MaterialPageRoute(builder: (context) => const QiblaScreen()));
          }),
          _buildGridItem(Icons.front_hand, 'Duas', const Color(0xFFFFF3E0), Colors.orange, () {
            Navigator.push(context, MaterialPageRoute(builder: (context) => DuaScreen()));
          }),
          _buildGridItem(Icons.ac_unit, 'Yaqeen', const Color(0xFFE3F2FD), Colors.blue, () {}),
          _buildGridItem(Icons.play_circle_fill, 'Media', const Color(0xFFE8F5E9), Colors.green.shade600, () {}),
        ],
      ),
    );
  }

  Widget _buildGridItem(IconData icon, String label, Color bgColor, Color iconColor, VoidCallback onTap) {
    return GestureDetector(
      onTap: onTap,
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: bgColor,
              borderRadius: BorderRadius.circular(16),
            ),
            child: Icon(icon, color: iconColor, size: 32),
          ),
          const SizedBox(height: 8),
          Text(
            label,
            style: const TextStyle(
              color: Colors.black87,
              fontSize: 14,
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildMoodTracker(BuildContext context) {
    return GestureDetector(
      onTap: () => Navigator.push(context, MaterialPageRoute(builder: (context) => const EmotionsScreen())),
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 20),
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
        decoration: BoxDecoration(
          color: const Color(0xFF2A1B54), // Deep purple pattern
          borderRadius: BorderRadius.circular(8),
          image: const DecorationImage(
            image: AssetImage('assets/quran_pattern_v2.jpg'), // Reusing an existing pattern
            fit: BoxFit.cover,
            opacity: 0.3,
          ),
        ),
        child: Row(
          children: const [
            Icon(Icons.sentiment_satisfied_alt, color: Colors.white, size: 28),
            SizedBox(width: 16),
            Text(
              'How do you feel?',
              style: TextStyle(
                color: Colors.white,
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildDailyContentFeed(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Padding(
          padding: EdgeInsets.symmetric(horizontal: 20),
          child: Text(
            'Daily Inspiration',
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.bold,
              color: Color(0xFF1B3B2B),
            ),
          ),
        ),
        const SizedBox(height: 12),
        SizedBox(
          height: 180,
          child: ListView(
            scrollDirection: Axis.horizontal,
            physics: const BouncingScrollPhysics(),
            padding: const EdgeInsets.symmetric(horizontal: 16),
            children: [
              _buildDailyCard(
                title: 'Ayat of the Day',
                content: 'Indeed, Allah is with the patient.',
                reference: 'Quran 2:153',
                icon: Icons.menu_book,
                color: Colors.teal.shade800,
              ),
              _buildDailyCard(
                title: 'Hadith of the Day',
                content: 'The most beloved of deeds to Allah are those that are most consistent, even if it is small.',
                reference: 'Sahih Bukhari',
                icon: Icons.format_quote,
                color: Colors.indigo.shade800,
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildDailyCard({required String title, required String content, required String reference, required IconData icon, required Color color}) {
    return Container(
      width: 280,
      margin: const EdgeInsets.symmetric(horizontal: 4),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: color,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: color.withAlpha(50),
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
              Icon(icon, color: Colors.white70, size: 20),
              const SizedBox(width: 8),
              Text(
                title,
                style: const TextStyle(color: Colors.white70, fontWeight: FontWeight.w600, fontSize: 12),
              ),
            ],
          ),
          const SizedBox(height: 12),
          Expanded(
            child: Text(
              '"$content"',
              style: const TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold, height: 1.4),
              maxLines: 3,
              overflow: TextOverflow.ellipsis,
            ),
          ),
          Text(
            reference,
            style: const TextStyle(color: Colors.amberAccent, fontSize: 12, fontWeight: FontWeight.bold),
          ),
        ],
      ),
    );
  }

  Widget _buildNearbyMasjidBanner(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.push(context, MaterialPageRoute(builder: (context) => const SetMasjidTimesScreen()));
      },
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 20),
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(8),
          border: Border.all(color: Colors.blue.shade200),
        ),
        child: Row(
          children: [
            Icon(Icons.near_me, color: Colors.blue.shade700),
            const SizedBox(width: 12),
            Text(
              'NEARBY MASJID',
              style: TextStyle(
                color: Colors.blue.shade700,
                fontWeight: FontWeight.bold,
                fontSize: 14,
              ),
            ),
            const Spacer(),
            Text(
              '24 kilometers',
              style: TextStyle(
                color: Colors.blue.shade700,
                fontWeight: FontWeight.bold,
                fontSize: 14,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildTodayPrayersList(BuildContext context, PrayerData prayerData, Map<String, String> userIqamahTimes) {
    // This replicates the Masjid card shown in the screenshot
    return GestureDetector(
      onTap: () {
        Navigator.push(context, MaterialPageRoute(builder: (context) => const SetMasjidTimesScreen()));
      },
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 20),
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: const Color(0xFFF9F9F9),
          borderRadius: BorderRadius.circular(12),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Container(
                  padding: const EdgeInsets.all(8),
                  decoration: BoxDecoration(
                    color: Colors.red.shade50,
                    shape: BoxShape.circle,
                  ),
                  child: Icon(Icons.mosque, color: Colors.red.shade700, size: 24),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Local Masjid (${prayerData.city})',
                        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                      Text(
                        prayerData.city,
                        style: TextStyle(fontSize: 14, color: Colors.blue.shade700),
                      ),
                    ],
                  ),
                ),
                const Icon(Icons.favorite, color: Colors.red),
              ],
            ),
          const Padding(
            padding: EdgeInsets.symmetric(vertical: 12.0),
            child: Divider(),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    '${userIqamahTimes[prayerData.nextPrayerName] ?? "1:15PM"} ${prayerData.nextPrayerName.toUpperCase()}',
                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const Text(
                    'IQAMAH NEXT',
                    style: TextStyle(fontSize: 10, color: Colors.grey, fontWeight: FontWeight.bold),
                  ),
                ],
              ),
              OutlinedButton(
                onPressed: () {
                  Navigator.push(context, MaterialPageRoute(builder: (context) => const MasjidDetailScreen()));
                },
                style: OutlinedButton.styleFrom(
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                  side: BorderSide(color: Colors.blue.shade700),
                ),
                child: Row(
                  children: [
                    Text('See More', style: TextStyle(color: Colors.blue.shade700)),
                    const SizedBox(width: 4),
                    Icon(Icons.keyboard_double_arrow_right, color: Colors.blue.shade700, size: 16),
                  ],
                ),
              ),
            ],
          ),
        ],
      ),
    ),
  );
}

  Widget _iconButton(IconData icon, VoidCallback onPressed) => Padding(
    padding: const EdgeInsets.only(left: 8),
    child: InkWell(
      onTap: onPressed,
      customBorder: const CircleBorder(),
      child: Container(
        padding: const EdgeInsets.all(8),
        decoration: BoxDecoration(
          border: Border.all(color: Colors.white, width: 1.5),
          shape: BoxShape.circle,
        ),
        child: Icon(icon, color: Colors.white, size: 20),
      ),
    ),
  );

  String _formatTimeOnly(DateTime time) {
    return DateFormat('h:mm').format(time);
  }
  
  String _formatAmPm(DateTime time) {
    return DateFormat('a').format(time);
  }
}

// Custom Hexagon Drawer
class HexagonPainter extends CustomPainter {
  final Color color;
  final Color strokeColor;

  HexagonPainter({required this.color, required this.strokeColor});

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = color
      ..style = PaintingStyle.fill;
      
    final strokePaint = Paint()
      ..color = strokeColor
      ..style = PaintingStyle.stroke
      ..strokeWidth = 3.0;

    final path = Path();
    final double width = size.width;
    final double height = size.height;
    
    // Draw a pointed-top hexagon
    path.moveTo(width / 2, 0); // Top Center
    path.lineTo(width, height * 0.25); // Top Right
    path.lineTo(width, height * 0.75); // Bottom Right
    path.lineTo(width / 2, height); // Bottom Center
    path.lineTo(0, height * 0.75); // Bottom Left
    path.lineTo(0, height * 0.25); // Top Left
    path.close();

    canvas.drawPath(path, paint);
    canvas.drawPath(path, strokePaint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) => false;
}
