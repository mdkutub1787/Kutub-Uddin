import 'package:flutter/material.dart';
import 'package:quran/quran.dart' as quran;

class SurahDetailPage extends StatelessWidget {
  final int surahNumber;
  final String surahName;

  const SurahDetailPage({
    super.key,
    required this.surahNumber,
    required this.surahName,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(surahName),
        centerTitle: true,
      ),
      body: SafeArea(
        child: ListView.separated(
          padding: const EdgeInsets.all(16.0),
          itemCount: quran.getVerseCount(surahNumber),
          separatorBuilder: (context, index) => const Divider(height: 32),
          itemBuilder: (context, index) {
            final verseNumber = index + 1;
            return Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                // Verse header (number)
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                  decoration: BoxDecoration(
                    color: Theme.of(context).colorScheme.primary.withOpacity(0.05),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Row(
                    children: [
                      CircleAvatar(
                        radius: 14,
                        backgroundColor: Theme.of(context).colorScheme.primary,
                        child: Text(
                          '$verseNumber',
                          style: const TextStyle(
                            fontSize: 12,
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 16),
                
                // Arabic text
                Text(
                  quran.getVerse(surahNumber, verseNumber, verseEndSymbol: true),
                  textAlign: TextAlign.right,
                  style: const TextStyle(
                    fontSize: 24,
                    height: 1.8,
                  ),
                ),
                
                const SizedBox(height: 16),
                
                // Translation (optional if we had one in this package)
                // For now, the quran package provides English translation
                // actually wait, the 'quran' package provides Arabic text.
                // Let's check if it provides translation, usually it's in another package or requires config.
                // For MVP we will just show the Arabic text.
              ],
            );
          },
        ),
      ),
    );
  }
}
