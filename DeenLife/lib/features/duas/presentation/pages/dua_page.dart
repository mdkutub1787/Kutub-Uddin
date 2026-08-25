import 'package:flutter/material.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

class Dua {
  final String title;
  final String arabic;
  final String pronunciation;
  final String meaning;

  Dua({
    required this.title,
    required this.arabic,
    required this.pronunciation,
    required this.meaning,
  });
}

class DuaPage extends StatelessWidget {
  DuaPage({super.key});

  final List<Dua> duas = [
    Dua(
      title: 'Morning Dua',
      arabic: 'الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ',
      pronunciation:
          'Alhamdu lillahil-ladhi ahyana ba\'da ma amatana wa ilaihin-nushur',
      meaning: 'Praise is to Allah Who gives us life after He has caused us to die and to Him is the return.',
    ),
    Dua(
      title: 'Sleeping Dua',
      arabic: 'بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا',
      pronunciation: 'Bismika Allahumma amutu wa ahya',
      meaning: 'In Your name O Allah, I live and die.',
    ),
    Dua(
      title: 'Entering the Restroom',
      arabic: 'اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبُثِ وَالْخَبَائِثِ',
      pronunciation: 'Allahumma inni a\'udhu bika minal khubuthi wal khaba-ith',
      meaning: 'O Allah, I seek refuge with You from all offensive and wicked things.',
    ),
    Dua(
      title: 'Leaving the Restroom',
      arabic: 'غُفْرَانَكَ',
      pronunciation: 'Ghufranaka',
      meaning: 'I ask Your forgiveness.',
    ),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: Text(
          context.tr('Daily Duas'),
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
        centerTitle: true,
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          await Future.delayed(const Duration(milliseconds: 1000));
        },
        child: ListView.builder(
          padding: const EdgeInsets.all(16.0),
          itemCount: duas.length,
          itemBuilder: (context, index) {
            final dua = duas[index];
            return Container(
              margin: const EdgeInsets.only(bottom: 16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(16),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withAlpha(10),
                    blurRadius: 10,
                    offset: const Offset(0, 4),
                  ),
                ],
              ),
              child: ExpansionTile(
                tilePadding: const EdgeInsets.symmetric(
                  horizontal: 20,
                  vertical: 8,
                ),
                shape: const RoundedRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(16)),
                ),
                collapsedShape: const RoundedRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(16)),
                ),
                leading: Container(
                  padding: const EdgeInsets.all(8),
                  decoration: BoxDecoration(
                    color: const Color(0xFF1E3A5F).withAlpha(30),
                    shape: BoxShape.circle,
                  ),
                  child: const Icon(
                    Icons.favorite,
                    color: Color(0xFF1E3A5F),
                    size: 20,
                  ),
                ),
                title: Text(
                  dua.title,
                  style: const TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 16,
                    color: Color(0xFF1E3A5F),
                  ),
                ),
                childrenPadding: const EdgeInsets.all(20),
                children: [
                  Text(
                    dua.arabic,
                    textAlign: TextAlign.center,
                    style: const TextStyle(
                      fontSize: 26,
                      height: 1.8,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1E3A5F),
                    ),
                  ),
                  const SizedBox(height: 16),
                  _duaDetailSection(
                    context,
                    'Pronunciation',
                    dua.pronunciation,
                    italic: true,
                  ),
                  const SizedBox(height: 12),
                  _duaDetailSection(context, 'Meaning', dua.meaning),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  Widget _duaDetailSection(
    BuildContext context,
    String label,
    String content, {
    bool italic = false,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          context.tr(label),
          style: const TextStyle(
            fontWeight: FontWeight.bold,
            color: Colors.grey,
            fontSize: 12,
          ),
        ),
        const SizedBox(height: 4),
        Text(
          content,
          style: TextStyle(
            fontSize: 14,
            height: 1.5,
            fontStyle: italic ? FontStyle.italic : FontStyle.normal,
            color: Colors.black87,
          ),
        ),
      ],
    );
  }
}
