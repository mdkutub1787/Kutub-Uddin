import 'package:flutter/material.dart';
import 'package:quran/quran.dart' as quran;
import 'surah_detail_page.dart';

class QuranSearchPage extends StatefulWidget {
  const QuranSearchPage({super.key});

  @override
  State<QuranSearchPage> createState() => _QuranSearchPageState();
}

class SearchResult {
  final int surahNumber;
  final int verseNumber;
  final String text;
  final String surahName;

  SearchResult({
    required this.surahNumber,
    required this.verseNumber,
    required this.text,
    required this.surahName,
  });
}

class _QuranSearchPageState extends State<QuranSearchPage> {
  final TextEditingController _searchController = TextEditingController();
  List<SearchResult> _results = [];
  bool _isSearching = false;

  void _performSearch(String query) {
    if (query.trim().isEmpty) {
      setState(() {
        _results = [];
        _isSearching = false;
      });
      return;
    }

    setState(() {
      _isSearching = true;
    });

    final queryLower = query.toLowerCase().trim();
    List<SearchResult> results = [];

    // Check if query is Surah + Ayat (e.g., "Baqarah 255" or "2 255")
    final regex = RegExp(r'([a-zA-Z]+|\d+)\s+(\d+)');
    final match = regex.firstMatch(queryLower);
    
    if (match != null) {
      String surahPart = match.group(1)!;
      int? ayahPart = int.tryParse(match.group(2)!);
      
      int? surahNum = int.tryParse(surahPart);
      if (surahNum == null) {
        // Try to match surah name
        for (int i = 1; i <= 114; i++) {
          if (quran.getSurahName(i).toLowerCase().contains(surahPart) ||
              quran.getSurahNameEnglish(i).toLowerCase().contains(surahPart)) {
            surahNum = i;
            break;
          }
        }
      }
      
      if (surahNum != null && surahNum > 0 && surahNum <= 114) {
        if (ayahPart != null && ayahPart > 0 && ayahPart <= quran.getVerseCount(surahNum)) {
           results.add(SearchResult(
             surahNumber: surahNum,
             verseNumber: ayahPart,
             text: quran.getVerse(surahNum, ayahPart, verseEndSymbol: true),
             surahName: quran.getSurahName(surahNum),
           ));
           setState(() {
             _results = results;
             _isSearching = false;
           });
           return;
        }
      }
    }

    // Normal Text Search (Arabic or Surah Name)
    // First search Surah Names
    for (int i = 1; i <= 114; i++) {
      if (quran.getSurahName(i).toLowerCase().contains(queryLower) ||
          quran.getSurahNameEnglish(i).toLowerCase().contains(queryLower)) {
        results.add(SearchResult(
          surahNumber: i,
          verseNumber: 1, // point to first ayah
          text: "Surah ${quran.getSurahName(i)} matched your search.",
          surahName: quran.getSurahName(i),
        ));
      }
    }

    // Then search Arabic Text (Limit to 50 to avoid lag if too broad)
    int count = 0;
    for (int s = 1; s <= 114; s++) {
      int vCount = quran.getVerseCount(s);
      for (int v = 1; v <= vCount; v++) {
        String arabicText = quran.getVerse(s, v);
        // Remove diacritics for easier search? The quran package returns text with diacritics.
        // For simplicity, we just do a direct match
        if (arabicText.contains(queryLower)) {
          results.add(SearchResult(
            surahNumber: s,
            verseNumber: v,
            text: arabicText,
            surahName: quran.getSurahName(s),
          ));
          count++;
          if (count > 50) break;
        }
      }
      if (count > 50) break;
    }

    setState(() {
      _results = results;
      _isSearching = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: TextField(
          controller: _searchController,
          autofocus: true,
          decoration: const InputDecoration(
            hintText: 'Search Surah (Baqarah), Ayah (2 255), or Text',
            border: InputBorder.none,
            hintStyle: TextStyle(color: Colors.white60),
          ),
          style: const TextStyle(color: Colors.white, fontSize: 18),
          onChanged: _performSearch,
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.clear),
            onPressed: () {
              _searchController.clear();
              _performSearch('');
            },
          )
        ],
      ),
      body: _isSearching
          ? const Center(child: CircularProgressIndicator())
          : _results.isEmpty && _searchController.text.isNotEmpty
              ? const Center(child: Text('No results found.'))
              : ListView.separated(
                  itemCount: _results.length,
                  separatorBuilder: (context, index) => const Divider(),
                  itemBuilder: (context, index) {
                    final res = _results[index];
                    return ListTile(
                      title: Text(
                        'Surah ${res.surahName} - Verse ${res.verseNumber}',
                        style: const TextStyle(fontWeight: FontWeight.bold),
                      ),
                      subtitle: Text(
                        res.text,
                        textAlign: res.text.contains(RegExp(r'[a-zA-Z]')) ? TextAlign.left : TextAlign.right,
                        style: TextStyle(
                          fontSize: res.text.contains(RegExp(r'[a-zA-Z]')) ? 14 : 20,
                        ),
                      ),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => SurahDetailPage(
                              surahNumber: res.surahNumber,
                              surahName: res.surahName,
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
