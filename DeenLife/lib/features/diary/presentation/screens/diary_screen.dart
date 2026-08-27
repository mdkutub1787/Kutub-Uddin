import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:intl/intl.dart';
import '../../domain/models/diary_entry.dart';
import '../providers/diary_provider.dart';

class DiaryScreen extends ConsumerWidget {
  final DateTime? selectedDate;
  const DiaryScreen({super.key, this.selectedDate});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entries = ref.watch(diaryProvider);

    return Scaffold(
      backgroundColor: const Color(0xFFF4F7F6),
      body: Stack(
        children: [
          // Background Gradient Header
          Positioned(
            top: 0,
            left: 0,
            right: 0,
            height: 280,
            child: Container(
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  colors: [Color(0xFF1B3B2B), Color(0xFF2E5E47)],
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                ),
                borderRadius: BorderRadius.only(
                  bottomLeft: Radius.circular(40),
                  bottomRight: Radius.circular(40),
                ),
              ),
            ),
          ),
          
          // Main Content
          SafeArea(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Custom App Bar
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
                  child: Row(
                    children: [
                      Container(
                        decoration: BoxDecoration(
                          color: Colors.white.withAlpha(50),
                          shape: BoxShape.circle,
                        ),
                        child: IconButton(
                          icon: const Icon(Icons.arrow_back, color: Colors.white),
                          onPressed: () => Navigator.pop(context),
                        ),
                      ),
                      const SizedBox(width: 16),
                      Text(
                        'ডায়েরি',
                        style: GoogleFonts.tiroBangla(
                          fontSize: 24,
                          fontWeight: FontWeight.bold,
                          color: Colors.white,
                        ),
                      ),
                      const Spacer(),
                      Container(
                        decoration: BoxDecoration(
                          color: Colors.white.withAlpha(50),
                          shape: BoxShape.circle,
                        ),
                        child: IconButton(
                          icon: const Icon(Icons.search, color: Colors.white),
                          onPressed: () {},
                        ),
                      ),
                    ],
                  ),
                ),
                
                const SizedBox(height: 20),
                
                // Header Text
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 24.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'মুহাসাবাহ (আত্মসমালোচনা)',
                        style: GoogleFonts.tiroBangla(
                          fontSize: 28,
                          fontWeight: FontWeight.bold,
                          color: Colors.white,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        'নিজেকে জানার এবং প্রতিদিন একটু একটু করে ভালো হওয়ার যাত্রা।',
                        style: GoogleFonts.hindSiliguri(
                          fontSize: 15,
                          color: Colors.white70,
                          height: 1.4,
                        ),
                      ),
                    ],
                  ),
                ),
                
                const SizedBox(height: 40),
                
                // Diary Entries List
                Expanded(
                  child: entries.isEmpty 
                    ? _buildEmptyState(context)
                    : ListView.builder(
                        padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 10.0),
                        physics: const BouncingScrollPhysics(),
                        itemCount: entries.length,
                        itemBuilder: (context, index) {
                          return _buildDiaryCard(context, entries[index]);
                        },
                      ),
                ),
              ],
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _showAddEntrySheet(context, ref),
        backgroundColor: const Color(0xFFE5A023), // Warm golden accent
        elevation: 8,
        icon: const Icon(Icons.edit_note, color: Colors.white),
        label: Text(
          'নতুন পাতা',
          style: GoogleFonts.hindSiliguri(
            fontWeight: FontWeight.bold,
            color: Colors.white,
            fontSize: 16,
          ),
        ),
      ),
    );
  }

  Widget _buildEmptyState(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.note_alt_outlined, size: 80, color: Colors.grey.shade300),
          const SizedBox(height: 16),
          Text(
            'এখনো কোনো পাতা লেখা হয়নি।',
            style: GoogleFonts.hindSiliguri(
              fontSize: 18,
              color: Colors.grey.shade500,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDiaryCard(BuildContext context, DiaryEntry entry) {
    final dayStr = _toBanglaDigit(entry.date.day);
    final monthStr = _getBanglaMonth(entry.date.month);
    final timeStr = _toBanglaTime(entry.date);

    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withAlpha(10),
            blurRadius: 20,
            offset: const Offset(0, 10),
          ),
        ],
      ),
      child: Material(
        color: Colors.transparent,
        borderRadius: BorderRadius.circular(20),
        child: InkWell(
          onTap: () {},
          borderRadius: BorderRadius.circular(20),
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Date Column
                Column(
                  children: [
                    Text(
                      dayStr,
                      style: GoogleFonts.montserrat(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                        color: const Color(0xFF1B3B2B),
                      ),
                    ),
                    Text(
                      monthStr,
                      style: GoogleFonts.hindSiliguri(
                        fontSize: 14,
                        fontWeight: FontWeight.w600,
                        color: Colors.grey.shade600,
                      ),
                    ),
                  ],
                ),
                
                const SizedBox(width: 20),
                
                // Divider
                Container(
                  width: 1.5,
                  height: 50,
                  color: Colors.grey.shade200,
                ),
                
                const SizedBox(width: 20),
                
                // Content Column
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Text(
                              entry.title,
                              style: GoogleFonts.tiroBangla(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: const Color(0xFF1B3B2B),
                              ),
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                            ),
                          ),
                          Icon(
                            _getMoodIcon(entry.mood),
                            color: _getMoodColor(entry.mood),
                            size: 20,
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Text(
                        entry.content,
                        style: GoogleFonts.hindSiliguri(
                          fontSize: 14,
                          color: Colors.grey.shade700,
                          height: 1.4,
                        ),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                      const SizedBox(height: 12),
                      Row(
                        children: [
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                            decoration: BoxDecoration(
                              color: const Color(0xFF1B3B2B).withAlpha(15),
                              borderRadius: BorderRadius.circular(10),
                            ),
                            child: Text(
                              entry.tag,
                              style: GoogleFonts.hindSiliguri(
                                fontSize: 12,
                                fontWeight: FontWeight.w600,
                                color: const Color(0xFF1B3B2B),
                              ),
                            ),
                          ),
                          const Spacer(),
                          Text(
                            timeStr,
                            style: GoogleFonts.hindSiliguri(
                              fontSize: 12,
                              color: Colors.grey.shade500,
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _showAddEntrySheet(BuildContext context, WidgetRef ref) {
    final titleController = TextEditingController();
    final contentController = TextEditingController();
    final tagController = TextEditingController();
    String selectedMood = 'happy';

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (context) => StatefulBuilder(
        builder: (context, setSheetState) => Container(
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.vertical(top: Radius.circular(30)),
          ),
          padding: EdgeInsets.only(
            bottom: MediaQuery.of(context).viewInsets.bottom,
            top: 24,
            left: 24,
            right: 24,
          ),
          child: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Center(
                  child: Container(
                    width: 40,
                    height: 4,
                    decoration: BoxDecoration(
                      color: Colors.grey.shade300,
                      borderRadius: BorderRadius.circular(2),
                    ),
                  ),
                ),
                const SizedBox(height: 24),
                Text(
                  'আজকের পাতা',
                  style: GoogleFonts.tiroBangla(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    color: const Color(0xFF1B3B2B),
                  ),
                ),
                const SizedBox(height: 20),
                
                // Mood Selection
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    _moodOption('happy', Icons.sentiment_very_satisfied, Colors.green.shade400, selectedMood, (mood) {
                      setSheetState(() => selectedMood = mood);
                    }),
                    _moodOption('neutral', Icons.sentiment_neutral, Colors.amber.shade400, selectedMood, (mood) {
                      setSheetState(() => selectedMood = mood);
                    }),
                    _moodOption('sad', Icons.sentiment_dissatisfied, Colors.blueGrey.shade400, selectedMood, (mood) {
                      setSheetState(() => selectedMood = mood);
                    }),
                  ],
                ),
                const SizedBox(height: 24),
                
                TextField(
                  controller: titleController,
                  style: GoogleFonts.tiroBangla(),
                  decoration: _inputDecoration('শিরোনাম (যেমন: আজকের আমল)'),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: tagController,
                  style: GoogleFonts.hindSiliguri(),
                  decoration: _inputDecoration('ট্যাগ (যেমন: কৃতজ্ঞতা, আমল)'),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: contentController,
                  maxLines: 5,
                  style: GoogleFonts.hindSiliguri(),
                  decoration: _inputDecoration('আপনার মনের কথা লিখুন...'),
                ),
                const SizedBox(height: 24),
                
                ElevatedButton(
                  onPressed: () {
                    if (titleController.text.isNotEmpty && contentController.text.isNotEmpty) {
                      final newEntry = DiaryEntry(
                        id: DateTime.now().toString(),
                        title: titleController.text,
                        content: contentController.text,
                        mood: selectedMood,
                        tag: tagController.text.isEmpty ? 'সাধারণ' : tagController.text,
                        date: DateTime.now(),
                      );
                      ref.read(diaryProvider.notifier).addEntry(newEntry);
                      Navigator.pop(context);
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('ডায়েরিতে নতুন পাতা যুক্ত হয়েছে!')),
                      );
                    }
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFF1B3B2B),
                    foregroundColor: Colors.white,
                    minimumSize: const Size(double.infinity, 54),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                  ),
                  child: Text(
                    'সংরক্ষণ করুন',
                    style: GoogleFonts.hindSiliguri(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                ),
                const SizedBox(height: 24),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _moodOption(String mood, IconData icon, Color color, String currentMood, Function(String) onSelect) {
    bool isSelected = mood == currentMood;
    return GestureDetector(
      onTap: () => onSelect(mood),
      child: Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: isSelected ? color.withAlpha(40) : Colors.transparent,
          borderRadius: BorderRadius.circular(16),
          border: Border.all(color: isSelected ? color : Colors.grey.shade200, width: 2),
        ),
        child: Icon(icon, color: isSelected ? color : Colors.grey.shade400, size: 32),
      ),
    );
  }

  InputDecoration _inputDecoration(String hint) {
    return InputDecoration(
      hintText: hint,
      hintStyle: GoogleFonts.hindSiliguri(color: Colors.grey.shade400),
      filled: true,
      fillColor: Colors.grey.shade50,
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(16),
        borderSide: BorderSide.none,
      ),
      contentPadding: const EdgeInsets.all(16),
    );
  }

  IconData _getMoodIcon(String mood) {
    switch (mood) {
      case 'happy': return Icons.sentiment_very_satisfied;
      case 'neutral': return Icons.sentiment_neutral;
      case 'sad': return Icons.sentiment_dissatisfied;
      default: return Icons.mood;
    }
  }

  Color _getMoodColor(String mood) {
    switch (mood) {
      case 'happy': return Colors.green.shade400;
      case 'neutral': return Colors.amber.shade400;
      case 'sad': return Colors.blueGrey.shade400;
      default: return Colors.grey;
    }
  }

  String _toBanglaDigit(int number) {
    const englishToBangla = {
      '0': '০', '1': '১', '2': '২', '3': '৩', '4': '৪',
      '5': '৫', '6': '৬', '7': '৭', '8': '৮', '9': '৯',
    };
    return number.toString().split('').map((e) => englishToBangla[e] ?? e).join('');
  }

  String _getBanglaMonth(int month) {
    const months = [
      'জানুয়ারি', 'ফেব্রুয়ারি', 'মার্চ', 'এপ্রিল', 'মে', 'জুন',
      'জুলাই', 'আগস্ট', 'সেপ্টেম্বর', 'অক্টোবর', 'নভেম্বর', 'ডিসেম্বর'
    ];
    return months[month - 1];
  }

  String _toBanglaTime(DateTime date) {
    final hour = date.hour > 12 ? date.hour - 12 : (date.hour == 0 ? 12 : date.hour);
    final period = date.hour >= 12 ? 'বিকাল/রাত' : 'সকাল/দুপুর';
    final minute = date.minute.toString().padLeft(2, '0');
    
    return '${_toBanglaDigit(hour)}:${_toBanglaDigit(int.parse(minute))} $period';
  }
}
