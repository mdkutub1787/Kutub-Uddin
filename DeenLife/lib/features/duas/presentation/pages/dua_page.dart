import 'package:flutter/material.dart';

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
      pronunciation: 'Alhamdu lillahil-ladhi ahyana ba\'da ma amatana wa ilaihin-nushur',
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
      appBar: AppBar(
        title: const Text('Hisnul Muslim (Duas)'),
        centerTitle: true,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16.0),
        itemCount: duas.length,
        itemBuilder: (context, index) {
          final dua = duas[index];
          return Card(
            elevation: 2,
            margin: const EdgeInsets.only(bottom: 16),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
            child: ExpansionTile(
              title: Text(
                dua.title,
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
              ),
              childrenPadding: const EdgeInsets.all(16),
              children: [
                Text(
                  dua.arabic,
                  textAlign: TextAlign.right,
                  style: const TextStyle(
                    fontSize: 24,
                    height: 1.8,
                  ),
                ),
                const SizedBox(height: 16),
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: Theme.of(context).colorScheme.primary.withOpacity(0.05),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        'Pronunciation:',
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        dua.pronunciation,
                        style: const TextStyle(fontStyle: FontStyle.italic),
                      ),
                      const SizedBox(height: 12),
                      const Text(
                        'Meaning:',
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 4),
                      Text(dua.meaning),
                    ],
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}
