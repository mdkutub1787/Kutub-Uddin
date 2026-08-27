import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:adhan/adhan.dart';
import 'package:intl/intl.dart';
import 'package:geolocator/geolocator.dart';
import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';

class PermanentPrayerTimesScreen extends ConsumerWidget {
  const PermanentPrayerTimesScreen({super.key});

  final List<String> _banglaMonths = const [
    'জানুয়ারী', 'ফেব্রুয়ারী', 'মার্চ', 'এপ্রিল', 'মে', 'জুন',
    'জুলাই', 'আগস্ট', 'সেপ্টেম্বর', 'অক্টোবর', 'নভেম্বর', 'ডিসেম্বর'
  ];

  String _toBanglaDigit(int number) {
    const englishToBangla = {
      '0': '০', '1': '১', '2': '২', '3': '৩', '4': '৪',
      '5': '৫', '6': '৬', '7': '৭', '8': '৮', '9': '৯',
    };
    return number.toString().split('').map((e) => englishToBangla[e] ?? e).join('');
  }

  String _formatTime(DateTime time) {
    final formatted = DateFormat('h:mm').format(time);
    return formatted.split('').map((e) {
      const englishToBangla = {
        '0': '০', '1': '১', '2': '২', '3': '৩', '4': '৪',
        '5': '৫', '6': '৬', '7': '৭', '8': '৮', '9': '৯',
      };
      return englishToBangla[e] ?? e;
    }).join('');
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final locationAsync = ref.watch(locationProvider);
    final calcMethod = ref.watch(calculationMethodProvider);

    return Scaffold(
      backgroundColor: const Color(0xFFF3F4F6),
      appBar: AppBar(
        title: const Text('নামাজ ও রোজার স্থায়ী সময়সূচী'),
        backgroundColor: const Color(0xFF1B3B2B), // Deep Islamic Green
        foregroundColor: Colors.white,
        centerTitle: true,
      ),
      body: locationAsync.when(
        data: (position) {
          return ListView.builder(
            padding: const EdgeInsets.all(12),
            itemCount: 12,
            itemBuilder: (context, monthIndex) {
              return _buildMonthTable(context, monthIndex + 1, position, calcMethod);
            },
          );
        },
        loading: () => const Center(child: CircularProgressIndicator(color: Color(0xFF1B3B2B))),
        error: (err, stack) => Center(
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Text(
              'লোকেশন পাওয়া যায়নি। দয়া করে আপনার ডিভাইসের লোকেশন চালু করুন।\n\nError: $err',
              textAlign: TextAlign.center,
              style: const TextStyle(color: Colors.red),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildMonthTable(BuildContext context, int month, Position position, CalculationMethod calcMethod) {
    final coordinates = Coordinates(position.latitude, position.longitude);
    final params = calcMethod.getParameters();
    params.madhab = Madhab.hanafi;

    final targetDays = [1, 5, 10, 15, 20, 25];
    final int currentYear = DateTime.now().year;

    return Container(
      margin: const EdgeInsets.only(bottom: 24),
      decoration: BoxDecoration(
        color: const Color(0xFFFAF7F2), // Warm paper-like background
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: const Color(0xFF4A3423), width: 1.5),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withAlpha(20),
            blurRadius: 5,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        children: [
          // Month Header
          Container(
            padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
            decoration: const BoxDecoration(
              color: Color(0xFF4A3423), // Dark brown header
              borderRadius: BorderRadius.only(topLeft: Radius.circular(6), topRight: Radius.circular(6)),
            ),
            child: Row(
              children: [
                Text(
                  _banglaMonths[month - 1],
                  style: const TextStyle(color: Colors.amber, fontSize: 22, fontWeight: FontWeight.bold),
                ),
                const SizedBox(width: 12),
                const Text(
                  'মাসের স্থায়ী সময়সূচী (বর্তমান লোকেশন অনুযায়ী)',
                  style: TextStyle(color: Colors.white, fontSize: 13),
                ),
              ],
            ),
          ),

          // Table Headers
          Table(
            border: TableBorder.all(color: const Color(0xFF4A3423), width: 1),
            columnWidths: const {
              0: FlexColumnWidth(1),   // Date
              1: FlexColumnWidth(1.2), // Sehri End
              2: FlexColumnWidth(1.1), // Fajr
              3: FlexColumnWidth(1.1), // Sunrise
              4: FlexColumnWidth(1.1), // Dhuhr
              5: FlexColumnWidth(1.1), // Asr
              6: FlexColumnWidth(1.2), // Maghrib/Iftar
              7: FlexColumnWidth(1.1), // Isha
            },
            children: [
              TableRow(
                decoration: const BoxDecoration(color: Color(0xFF8B5A2B)), // Lighter brown for table header
                children: [
                  _headerCell('তারিখ'),
                  _headerCell('সেহরীর\nশেষ সময়'),
                  _headerCell('ফজর'),
                  _headerCell('সূর্য উদয়'),
                  _headerCell('যোহরের\nসময় শুরু'),
                  _headerCell('আছরের\nসময় শুরু'),
                  _headerCell('মাগরিব\nইফতার'),
                  _headerCell('এশার\nসময় শুরু'),
                ],
              ),
              // Data Rows
              ...targetDays.map((day) {
                final date = DateComponents(currentYear, month, day);
                final prayerTimes = PrayerTimes(coordinates, date, params);

                // Sehri end is roughly 5-10 minutes before Fajr depending on exact local FIqh,
                // but commonly it is 5 mins before Fajr start in BD.
                final sehriEnd = prayerTimes.fajr.subtract(const Duration(minutes: 5));

                return TableRow(
                  decoration: BoxDecoration(color: day % 2 == 0 ? const Color(0xFFF3E7D3) : Colors.white),
                  children: [
                    _dataCell(_toBanglaDigit(day)),
                    _dataCell(_formatTime(sehriEnd)),
                    _dataCell(_formatTime(prayerTimes.fajr)),
                    _dataCell(_formatTime(prayerTimes.sunrise)),
                    _dataCell(_formatTime(prayerTimes.dhuhr)),
                    _dataCell(_formatTime(prayerTimes.asr)),
                    _dataCell(_formatTime(prayerTimes.maghrib)),
                    _dataCell(_formatTime(prayerTimes.isha)),
                  ],
                );
              }).toList(),
            ],
          ),
        ],
      ),
    );
  }

  Widget _headerCell(String text) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 2),
      child: Text(
        text,
        textAlign: TextAlign.center,
        style: const TextStyle(
          color: Colors.amberAccent,
          fontSize: 10,
          fontWeight: FontWeight.bold,
          height: 1.2,
        ),
      ),
    );
  }

  Widget _dataCell(String text) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10.0, horizontal: 2),
      child: Text(
        text,
        textAlign: TextAlign.center,
        style: const TextStyle(
          color: Color(0xFF4A3423),
          fontSize: 11,
          fontWeight: FontWeight.w600,
        ),
      ),
    );
  }
}

