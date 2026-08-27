import 'package:flutter/material.dart';
import 'package:quran/quran.dart' as quran;
import 'package:deen_life/core/localization/app_localizations.dart';

import 'surah_detail_screen.dart';

class TafsirSurahListScreen extends StatelessWidget {
  final int tafsirId;
  final String tafsirName;

  const TafsirSurahListScreen({
    super.key,
    required this.tafsirId,
    required this.tafsirName,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              tafsirName,
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            Text(
              context.tr('Select a Surah to read Tafsir'),
              style: const TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.normal,
              ),
            ),
          ],
        ),
        centerTitle: true,
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          await Future.delayed(const Duration(seconds: 1));
        },
        child: ListView.builder(
          physics: const AlwaysScrollableScrollPhysics(),
          itemCount: quran.totalSurahCount,
        itemBuilder: (context, index) {
          final surahNumber = index + 1;
          return ListTile(
            leading: CircleAvatar(
              backgroundColor: Theme.of(context).colorScheme.primary
                  .withOpacity(0.1),
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
            trailing: const Icon(Icons.arrow_forward_ios, size: 16),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => SurahDetailScreen(
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
    );
  }
}

