import 'package:flutter/material.dart';
import 'package:quran/quran.dart' as quran;
import 'package:deen_life/core/localization/app_localizations.dart';
import 'surah_detail_page.dart';
import 'quran_search_page.dart';
import 'quran_mushaf_page.dart';

class QuranPage extends StatelessWidget {
  const QuranPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(context.tr('Al-Quran')),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.book),
            tooltip: context.tr('Mushaf (Hafizi)'),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const QuranMushafPage()),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.search),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const QuranSearchPage()),
              );
            },
          ),
        ],
      ),
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/quran_pattern.jpg'),
            fit: BoxFit.cover,
            opacity: 0.15, // Make it very faint like a watermark
          ),
        ),
        child: RefreshIndicator(
          onRefresh: () async {
            await Future.delayed(const Duration(milliseconds: 1000));
          },
          child: ListView.builder(
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
                  '${quran.getSurahNameEnglish(surahNumber)} • ${quran.getVerseCount(surahNumber)} ${context.tr('Ayahs')}',
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
        ),
      ),
    );
  }
}
