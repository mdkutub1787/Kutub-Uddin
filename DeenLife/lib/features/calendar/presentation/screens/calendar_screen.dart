import 'dart:ui';
import 'package:flutter/material.dart';
import 'calendar_grid_screen.dart';
import 'permanent_prayer_times_screen.dart';
import '../../../masjid/presentation/screens/masjid_list_screen.dart';
import '../../../qibla/presentation/screens/qibla_screen.dart';
import '../../../diary/presentation/screens/diary_screen.dart';

class CalendarScreen extends StatefulWidget {
  const CalendarScreen({super.key});

  @override
  State<CalendarScreen> createState() => _CalendarScreenState();
}

class _CalendarScreenState extends State<CalendarScreen> {
  final DateTime _today = DateTime.now();

  final List<String> _weekdays = ['রবি', 'সোম', 'মঙ্গল', 'বুধ', 'বৃহঃ', 'শুক্র', 'শনি'];
  final List<String> _englishMonths = [
    'জানুয়ারি', 'ফেব্রুয়ারি', 'মার্চ', 'এপ্রিল', 'মে', 'জুন',
    'জুলাই', 'আগস্ট', 'সেপ্টেম্বর', 'অক্টোবর', 'নভেম্বর', 'ডিসেম্বর'
  ];

  String _toBanglaDigit(int number) {
    const englishToBangla = {
      '0': '০', '1': '১', '2': '২', '3': '৩', '4': '৪',
      '5': '৫', '6': '৬', '7': '৭', '8': '৮', '9': '৯',
    };
    return number.toString().split('').map((e) => englishToBangla[e] ?? e).join('');
  }

  @override
  Widget build(BuildContext context) {
    final String todayEnglish = '${_toBanglaDigit(_today.day)} ${_englishMonths[_today.month - 1]} ${_toBanglaDigit(_today.year)} ইংরেজি';
    final String todayDay = 'রোজ - ${_weekdays[_today.weekday % 7]}';

    return Scaffold(
      extendBodyBehindAppBar: true,
      appBar: AppBar(
        title: const Text('ক্যালেন্ডার (ইংরেজি, বাংলা ও আরবি)', style: TextStyle(fontSize: 16)),
        backgroundColor: const Color(0xFF0D1B2A),
        elevation: 0,
        centerTitle: true,
        foregroundColor: Colors.white,
      ),
      body: Container(
        decoration: const BoxDecoration(
          color: Color(0xFF081C15), // Deep dark green/blue mix
          image: DecorationImage(
            image: AssetImage('assets/quran_pattern_v2.jpg'), // Using existing pattern
            fit: BoxFit.cover,
            colorFilter: ColorFilter.mode(Colors.black54, BlendMode.darken),
          ),
        ),
        child: SafeArea(
          child: Column(
            children: [
              const SizedBox(height: 16),
              // Header Dates
              Text(
                'আজ $todayEnglish',
                style: const TextStyle(color: Colors.white, fontSize: 20, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 4),
              Text(
                todayDay,
                style: const TextStyle(color: Colors.white70, fontSize: 18),
              ),
              const SizedBox(height: 32),
              
              // The Ornate Vertical Menu
              Expanded(
                child: Center(
                  child: Container(
                    width: 250,
                    decoration: BoxDecoration(
                      color: const Color(0xFF1E3A5F).withAlpha(200),
                      borderRadius: BorderRadius.circular(100), // Makes it pill shaped/ornate
                      border: Border.all(color: Colors.amberAccent.withAlpha(150), width: 2),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.amberAccent.withAlpha(20),
                          blurRadius: 20,
                          spreadRadius: 5,
                        ),
                      ],
                    ),
                    padding: const EdgeInsets.symmetric(vertical: 24, horizontal: 16),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        _buildMenuButton(
                          iconPath: Icons.mosque,
                          color: Colors.amber,
                          onTap: () {
                            Navigator.push(context, MaterialPageRoute(builder: (context) => const QiblaScreen()));
                          },
                        ),
                        const SizedBox(height: 24),
                        _buildMenuButton(
                          iconPath: Icons.event_note,
                          color: Colors.orange,
                          onTap: () {
                            Navigator.push(context, MaterialPageRoute(builder: (context) => const DiaryScreen()));
                          },
                        ),
                        const SizedBox(height: 24),
                        _buildMenuButton(
                          iconPath: Icons.calendar_month,
                          color: Colors.blueAccent,
                          label: "Calendar",
                          onTap: () {
                            Navigator.push(context, MaterialPageRoute(builder: (context) => const CalendarGridScreen()));
                          },
                        ),
                        const SizedBox(height: 24),
                        _buildMenuButton(
                          iconPath: Icons.access_time_filled,
                          color: Colors.redAccent,
                          onTap: () {
                            Navigator.push(context, MaterialPageRoute(builder: (context) => const PermanentPrayerTimesScreen()));
                          },
                        ),
                        const SizedBox(height: 24),
                        _buildMenuButton(
                          iconPath: Icons.people_alt,
                          color: Colors.tealAccent,
                          onTap: () {
                            Navigator.push(context, MaterialPageRoute(builder: (context) => const MasjidListScreen()));
                          },
                        ),
                      ],
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 40),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildMenuButton({
    required IconData iconPath,
    required Color color,
    String? label,
    required VoidCallback onTap,
  }) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: 80,
        height: 80,
        decoration: BoxDecoration(
          color: Colors.white,
          shape: BoxShape.circle,
          boxShadow: [
            BoxShadow(
              color: Colors.black.withAlpha(50),
              blurRadius: 10,
              offset: const Offset(0, 5),
            ),
          ],
          border: Border.all(color: color, width: 3),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(iconPath, color: color, size: label == null ? 40 : 32),
            if (label != null) ...[
              const SizedBox(height: 2),
              Text(
                label,
                style: TextStyle(
                  color: color,
                  fontSize: 10,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

