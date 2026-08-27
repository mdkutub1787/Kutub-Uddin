import 'package:flutter/material.dart';
import 'package:quran/quran.dart' as quran;
import 'package:google_fonts/google_fonts.dart';

class QuranMushafScreen extends StatefulWidget {
  const QuranMushafScreen({super.key});

  @override
  State<QuranMushafScreen> createState() => _QuranMushafScreenState();
}

class _QuranMushafScreenState extends State<QuranMushafScreen> {
  final PageController _pageController = PageController();
  int _currentPage = 1;

  @override
  Widget build(BuildContext context) {
    // Get info for the header based on current page
    final pageData = quran.getPageData(_currentPage);
    String surahName = "";
    int juzNumber = 1;

    if (pageData.isNotEmpty) {
      final firstEntry = pageData.first;
      surahName = quran.getSurahName(firstEntry['surah']);
      juzNumber = quran.getJuzNumber(firstEntry['surah'], firstEntry['start']);
    }

    return Scaffold(
      backgroundColor: const Color(0xFFF4ECD8), // Traditional cream paper color
      appBar: AppBar(
        title: const Text('Al-Quran (Mushaf)'),
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
        centerTitle: true,
      ),
      body: Column(
        children: [
          // Info Header
          Container(
            padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
            color: Colors.white.withOpacity(0.5),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'Juz $juzNumber',
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
                Text(
                  'Page $_currentPage',
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
                Text(
                  surahName,
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
              ],
            ),
          ),

          Expanded(
            child: PageView.builder(
              controller: _pageController,
              onPageChanged: (page) {
                setState(() {
                  _currentPage = page + 1;
                });
              },
              itemCount: 604, // Total pages in standard Mushaf
              itemBuilder: (context, index) {
                final currentPageNumber = index + 1;
                final currentPageData = quran.getPageData(currentPageNumber);

                // Check if this page starts a new Surah to show header
                bool startsNewSurah = false;
                int? newSurahNumber;
                if (currentPageData.isNotEmpty) {
                  final firstVerse = currentPageData.first;
                  if (firstVerse['start'] == 1) {
                    startsNewSurah = true;
                    newSurahNumber = firstVerse['surah'];
                  }
                }

                return SingleChildScrollView(
                  padding: const EdgeInsets.all(24),
                  child: Column(
                    children: [
                      // Surah Header if page starts a new Surah
                      if (startsNewSurah && newSurahNumber != null)
                        Container(
                          margin: const EdgeInsets.only(bottom: 24),
                          width: double.infinity,
                          padding: const EdgeInsets.symmetric(
                            vertical: 12,
                            horizontal: 32,
                          ),
                          decoration: BoxDecoration(
                            color: const Color(0xFFE8DCC4),
                            borderRadius: BorderRadius.circular(8),
                            border: Border.all(color: const Color(0xFFC4B494)),
                          ),
                          child: Text(
                            quran.getSurahNameArabic(newSurahNumber),
                            textAlign: TextAlign.center,
                            style: GoogleFonts.amiri(
                              fontSize: 28,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),

                      // Arabic Text Block
                      SelectableText(
                        currentPageData
                            .map((e) {
                              return quran.getVerse(
                                e['surah'],
                                e['start'],
                                verseEndSymbol: true,
                              );
                            })
                            .join(' '),
                        textAlign: TextAlign.center,
                        style: GoogleFonts.lateef(
                          fontSize: 34,
                          height: 1.8,
                          color: Colors.black87,
                        ),
                      ),
                    ],
                  ),
                );
              },
            ),
          ),

          // Bottom Navigation
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ElevatedButton(
                  onPressed: _currentPage > 1
                      ? () => _pageController.previousPage(
                          duration: const Duration(milliseconds: 300),
                          curve: Curves.easeInOut,
                        )
                      : null,
                  child: const Text('Previous'),
                ),
                ElevatedButton(
                  onPressed: _currentPage < 604
                      ? () => _pageController.nextPage(
                          duration: const Duration(milliseconds: 300),
                          curve: Curves.easeInOut,
                        )
                      : null,
                  child: const Text('Next'),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

