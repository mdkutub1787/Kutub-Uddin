import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class DiaryScreen extends StatelessWidget {
  final DateTime? selectedDate;
  const DiaryScreen({super.key, this.selectedDate});

  @override
  Widget build(BuildContext context) {
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
                  child: ListView.builder(
                    padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 10.0),
                    physics: const BouncingScrollPhysics(),
                    itemCount: _dummyEntries.length,
                    itemBuilder: (context, index) {
                      return _buildDiaryCard(_dummyEntries[index]);
                    },
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('ডায়েরি লেখার অপশন শীঘ্রই যুক্ত করা হবে!')),
          );
        },
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

  Widget _buildDiaryCard(Map<String, dynamic> entry) {
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
                      entry['day'],
                      style: GoogleFonts.montserrat(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                        color: const Color(0xFF1B3B2B),
                      ),
                    ),
                    Text(
                      entry['month'],
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
                              entry['title'],
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
                            _getMoodIcon(entry['mood']),
                            color: _getMoodColor(entry['mood']),
                            size: 20,
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Text(
                        entry['preview'],
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
                              entry['tag'],
                              style: GoogleFonts.hindSiliguri(
                                fontSize: 12,
                                fontWeight: FontWeight.w600,
                                color: const Color(0xFF1B3B2B),
                              ),
                            ),
                          ),
                          const Spacer(),
                          Text(
                            entry['time'],
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
}

// Dummy Data
final List<Map<String, dynamic>> _dummyEntries = [
  {
    'day': '২৭',
    'month': 'আগস্ট',
    'title': 'আজকের আমল',
    'preview': 'আলহামদুলিল্লাহ, আজ ফজর জামাতে পড়তে পেরেছি। কোরআন তেলাওয়াত মনকে খুব প্রশান্তি দিয়েছে।',
    'tag': 'আমল',
    'time': 'রাত ১০:৩০',
    'mood': 'happy',
  },
  {
    'day': '২৬',
    'month': 'আগস্ট',
    'title': 'শুকরিয়া আদায়',
    'preview': 'আজকের দিনটা বেশ ব্যস্ততায় কেটেছে, তবে আল্লাহর রহমতে সব কাজ ঠিকঠাক শেষ করতে পেরেছি।',
    'tag': 'কৃতজ্ঞতা',
    'time': 'রাত ১১:১৫',
    'mood': 'happy',
  },
  {
    'day': '২৫',
    'month': 'আগস্ট',
    'title': 'নিজেকে শুধরানোর চেষ্টা',
    'preview': 'আজ একটু রেগে গিয়েছিলাম। নবীজির (সা.) ধৈর্য্যের কথা মনে করে নিজেকে সামলে নিই।',
    'tag': 'আত্মশুদ্ধি',
    'time': 'রাত ৯:৪৫',
    'mood': 'neutral',
  },
];
