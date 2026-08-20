import 'package:flutter/material.dart';
import 'package:quran/quran.dart' as quran;
import 'surah_detail_page.dart';

class QuranPage extends StatelessWidget {
  const QuranPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Al-Quran'),
        centerTitle: true,
      ),
      body: ListView.builder(
        itemCount: quran.totalSurahCount,
        itemBuilder: (context, index) {
          final surahNumber = index + 1;
          return ListTile(
            leading: CircleAvatar(
              backgroundColor: Theme.of(context).colorScheme.primary.withOpacity(0.1),
              child: Text(
                '$surahNumber',
                style: TextStyle(
                  color: Theme.of(context).colorScheme.primary,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            title: Text(
              quran.getSurahName(surahNumber),
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
            subtitle: Text(
              '${quran.getSurahNameEnglish(surahNumber)} • ${quran.getVerseCount(surahNumber)} Ayahs',
            ),
            trailing: Text(
              quran.getSurahNameArabic(surahNumber),
              style: const TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => SurahDetailPage(
                    surahNumber: surahNumber,
                    surahName: quran.getSurahName(surahNumber),
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
