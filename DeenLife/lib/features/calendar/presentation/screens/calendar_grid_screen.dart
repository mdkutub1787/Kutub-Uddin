import 'dart:convert';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:hijri/hijri_calendar.dart';
import 'package:bangla_utilities/bangla_utilities.dart';

class CalendarGridScreen extends StatefulWidget {
  const CalendarGridScreen({super.key});

  @override
  State<CalendarGridScreen> createState() => _CalendarGridScreenState();
}

class _CalendarGridScreenState extends State<CalendarGridScreen> {
  final DateTime _today = DateTime.now();
  late PageController _pageController;
  final int _initialPage = 1200; 
  late DateTime _currentMonth;

  final List<String> _weekdays = ['রবি', 'সোম', 'মঙ্গল', 'বুধ', 'বৃহঃ', 'শুক্র', 'শনি'];
  final List<String> _englishMonths = [
    'জানুয়ারি', 'ফেব্রুয়ারি', 'মার্চ', 'এপ্রিল', 'মে', 'জুন',
    'জুলাই', 'আগস্ট', 'সেপ্টেম্বর', 'অক্টোবর', 'নভেম্বর', 'ডিসেম্বর'
  ];

  @override
  void initState() {
    super.initState();
    _currentMonth = DateTime(_today.year, _today.month, 1);
    _pageController = PageController(initialPage: _initialPage);
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  void _onPageChanged(int index) {
    int monthOffset = index - _initialPage;
    setState(() {
      _currentMonth = DateTime(_today.year, _today.month + monthOffset, 1);
    });
  }

  int _daysInMonth(DateTime date) {
    return DateTime(date.year, date.month + 1, 0).day;
  }

  String _getEnglishMonthString(DateTime date) {
    return '${_englishMonths[date.month - 1]} ${date.year}';
  }

  String _getBanglaMonthRange(DateTime date) {
    final start = BanglaDate.fromDateTime(DateTime(date.year, date.month, 1));
    final end = BanglaDate.fromDateTime(DateTime(date.year, date.month, _daysInMonth(date)));
    if (start.monthName == end.monthName) {
      return '${start.monthName} ${start.year} বাংলা';
    }
    return '${start.monthName} -- ${end.monthName} ${start.year} বাংলা';
  }

  String _getHijriMonthRange(DateTime date) {
    final start = HijriCalendar.fromDate(DateTime(date.year, date.month, 1));
    final end = HijriCalendar.fromDate(DateTime(date.year, date.month, _daysInMonth(date)));
    if (start.hMonth == end.hMonth) {
      return '${start.longMonthName} ${start.hYear} হিজরি';
    }
    return '${start.longMonthName} -- ${end.longMonthName} ${start.hYear} হিজরি';
  }

  @override
  Widget build(BuildContext context) {
    final String todayEnglish = '${_today.day} ${_englishMonths[_today.month - 1]} ${_today.year} ইংরেজি';
    final String todayDay = 'রোজ - ${_weekdays[_today.weekday % 7]}';

    return Scaffold(
      extendBodyBehindAppBar: true,
      appBar: AppBar(
        title: const Text('ক্যালেন্ডার'),
        backgroundColor: Colors.transparent,
        elevation: 0,
        centerTitle: true,
        foregroundColor: Colors.white,
      ),
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/calendar_bg.jpg'),
            fit: BoxFit.cover,
            colorFilter: ColorFilter.mode(Colors.black87, BlendMode.darken),
          ),
        ),
        child: SafeArea(
          child: Column(
            children: [
              // Today's Date Info
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 8.0),
                child: Column(
                  children: [
                    Text('আজ $todayEnglish', style: const TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.w500)),
                    const SizedBox(height: 4),
                    Text(todayDay, style: const TextStyle(color: Colors.white70, fontSize: 16)),
                  ],
                ),
              ),
              const SizedBox(height: 16),
              
              // Calendar Swiper
              Expanded(
                child: PageView.builder(
                  controller: _pageController,
                  onPageChanged: _onPageChanged,
                  itemBuilder: (context, index) {
                    int monthOffset = index - _initialPage;
                    DateTime pageMonth = DateTime(_today.year, _today.month + monthOffset, 1);
                    return _buildCalendarPage(pageMonth);
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildCalendarPage(DateTime pageMonth) {
    return SingleChildScrollView(
      physics: const BouncingScrollPhysics(),
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: Column(
        children: [
          // Elegant Month Header
          ClipRRect(
            borderRadius: BorderRadius.circular(20),
            child: BackdropFilter(
              filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
              child: Container(
                width: double.infinity,
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.white.withAlpha(20),
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: Colors.white.withAlpha(40), width: 1),
                ),
                child: Column(
                  children: [
                    Text(
                      _getEnglishMonthString(pageMonth),
                      style: const TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      _getBanglaMonthRange(pageMonth),
                      style: const TextStyle(color: Colors.amberAccent, fontSize: 15, fontWeight: FontWeight.w500),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      _getHijriMonthRange(pageMonth),
                      style: const TextStyle(color: Colors.cyanAccent, fontSize: 15, fontWeight: FontWeight.w500),
                    ),
                  ],
                ),
              ),
            ),
          ),
          
          const SizedBox(height: 20),
          
          // Premium Grid
          ClipRRect(
            borderRadius: BorderRadius.circular(20),
            child: BackdropFilter(
              filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
              child: Container(
                decoration: BoxDecoration(
                  color: Colors.black.withAlpha(60),
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: Colors.white.withAlpha(30), width: 1),
                ),
                child: Column(
                  children: [
                    // Weekday Headers
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 12),
                      child: Row(
                        children: List.generate(7, (index) {
                          bool isFriday = index == 5;
                          bool isSaturday = index == 6;
                          return Expanded(
                            child: Text(
                              _weekdays[index],
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                color: isFriday ? Colors.redAccent : (isSaturday ? Colors.orangeAccent : Colors.white70),
                                fontWeight: FontWeight.bold,
                                fontSize: 14,
                              ),
                            ),
                          );
                        }),
                      ),
                    ),
                    const Divider(color: Colors.white24, height: 1),
                    
                    // Days Grid
                    Padding(
                      padding: const EdgeInsets.only(bottom: 12),
                      child: _buildGridCells(pageMonth),
                    ),
                  ],
                ),
              ),
            ),
          ),

          const SizedBox(height: 24),
          _buildHolidaysList(pageMonth),
          const SizedBox(height: 40),
        ],
      ),
    );
  }

  Widget _buildGridCells(DateTime pageMonth) {
    int daysInMonth = _daysInMonth(pageMonth);
    int firstWeekday = pageMonth.weekday % 7;

    return Column(
      children: List.generate(6, (rowIndex) {
        return Row(
          children: List.generate(7, (colIndex) {
            int cellIndex = rowIndex * 7 + colIndex;
            int dayNumber = cellIndex - firstWeekday + 1;

            bool isCurrentMonth = dayNumber > 0 && dayNumber <= daysInMonth;
            if (!isCurrentMonth) {
              return Expanded(
                child: Container(height: 65), // Empty cell
              );
            }

            DateTime cellDate = DateTime(pageMonth.year, pageMonth.month, dayNumber);
            BanglaDate bangla = BanglaDate.fromDateTime(cellDate);
            HijriCalendar hijri = HijriCalendar.fromDate(cellDate);

            bool isToday = _today.year == cellDate.year && _today.month == cellDate.month && _today.day == cellDate.day;
            bool isFriday = colIndex == 5;
            bool isSaturday = colIndex == 6;

            Color textColor = Colors.white;
            if (isFriday) textColor = Colors.redAccent;
            if (isSaturday) textColor = Colors.orangeAccent;
            if (isToday) textColor = Colors.white;

            return Expanded(
              child: GestureDetector(
                onTap: () => _showDateDetails(cellDate, bangla, hijri),
                child: Container(
                  height: 65,
                  margin: const EdgeInsets.all(2),
                  decoration: BoxDecoration(
                    color: isToday ? Colors.teal.withOpacity(0.6) : Colors.transparent,
                    borderRadius: BorderRadius.circular(12),
                    border: isToday ? Border.all(color: Colors.tealAccent.withOpacity(0.5)) : null,
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        _toBanglaDigit(dayNumber),
                        style: TextStyle(
                          fontSize: 20,
                          fontWeight: isToday ? FontWeight.bold : FontWeight.w500,
                          color: textColor,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          Text(
                            bangla.day,
                            style: const TextStyle(fontSize: 10, color: Colors.amberAccent),
                          ),
                          Text(
                            _toBanglaDigit(hijri.hDay),
                            style: const TextStyle(fontSize: 10, color: Colors.cyanAccent),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            );
          }),
        );
      }),
    );
  }

  Widget _buildHolidaysList(DateTime pageMonth) {
    return FutureBuilder<String>(
      future: rootBundle.loadString('assets/data/holidays.json'),
      builder: (context, snapshot) {
        if (!snapshot.hasData) return const SizedBox();
        List<dynamic> allHolidays = json.decode(snapshot.data!);
        
        List<dynamic> monthHolidays = allHolidays.where((h) => h['month'] == pageMonth.month).toList();

        if (monthHolidays.isEmpty) {
          return Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: Colors.white.withAlpha(15),
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: Colors.white.withAlpha(30), width: 1),
            ),
            child: const Center(
              child: Text(
                'এই মাসে কোনো সরকারি ছুটি নেই',
                style: TextStyle(color: Colors.white70, fontSize: 16),
              ),
            ),
          );
        }

        return Container(
          width: double.infinity,
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Colors.white.withAlpha(15),
            borderRadius: BorderRadius.circular(20),
            border: Border.all(color: Colors.white.withAlpha(30), width: 1),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: const [
                  Icon(Icons.event_note, color: Colors.amberAccent, size: 24),
                  SizedBox(width: 8),
                  Text(
                    'সরকারি ছুটির দিনসমূহ',
                    style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              ...monthHolidays.map((h) {
                return Padding(
                  padding: const EdgeInsets.only(bottom: 12),
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                        decoration: BoxDecoration(
                          color: Colors.redAccent.withAlpha(50),
                          borderRadius: BorderRadius.circular(8),
                          border: Border.all(color: Colors.redAccent.withAlpha(100)),
                        ),
                        child: Text(
                          '${_toBanglaDigit(h['day'])} ${_englishMonths[pageMonth.month - 1]}',
                          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Padding(
                          padding: const EdgeInsets.only(top: 6),
                          child: Text(
                            h['name'],
                            style: const TextStyle(color: Colors.white70, fontSize: 15),
                          ),
                        ),
                      ),
                    ],
                  ),
                );
              }).toList(),
            ],
          ),
        );
      },
    );
  }

  void _showDateDetails(DateTime date, BanglaDate bangla, HijriCalendar hijri) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          backgroundColor: const Color(0xFF1E3A5F),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
          title: Text(
            '${_toBanglaDigit(date.day)} ${_englishMonths[date.month - 1]} ${date.year}',
            textAlign: TextAlign.center,
            style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
          ),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Divider(color: Colors.white24),
              const SizedBox(height: 12),
              _buildDetailRow('বাংলা তারিখ:', '${bangla.day} ${bangla.monthName} ${bangla.year}', Colors.amberAccent),
              const SizedBox(height: 12),
              _buildDetailRow('হিজরি তারিখ:', '${_toBanglaDigit(hijri.hDay)} ${hijri.longMonthName} ${hijri.hYear}', Colors.cyanAccent),
              const SizedBox(height: 12),
              _buildDetailRow('বার:', _weekdays[date.weekday % 7], Colors.white70),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('বন্ধ করুন', style: TextStyle(color: Colors.tealAccent, fontSize: 16)),
            ),
          ],
        );
      },
    );
  }

  Widget _buildDetailRow(String label, String value, Color valueColor) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(label, style: const TextStyle(color: Colors.white70, fontSize: 16)),
        Text(value, style: TextStyle(color: valueColor, fontSize: 16, fontWeight: FontWeight.w600)),
      ],
    );
  }

  String _toBanglaDigit(int number) {
    const englishToBangla = {
      '0': '০', '1': '১', '2': '২', '3': '৩', '4': '৪',
      '5': '৫', '6': '৬', '7': '৭', '8': '৮', '9': '৯',
    };
    return number.toString().split('').map((e) => englishToBangla[e] ?? e).join('');
  }
}

