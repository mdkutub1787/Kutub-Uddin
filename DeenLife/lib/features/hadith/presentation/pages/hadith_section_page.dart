import 'package:flutter/material.dart';
import 'package:flutter_tts/flutter_tts.dart';

class HadithSectionPage extends StatefulWidget {
  final String bookName;
  final String sectionName;
  final List<dynamic> sectionHadiths;

  const HadithSectionPage({
    super.key,
    required this.bookName,
    required this.sectionName,
    required this.sectionHadiths,
  });

  @override
  State<HadithSectionPage> createState() => _HadithSectionPageState();
}

class _HadithSectionPageState extends State<HadithSectionPage> {
  final FlutterTts _flutterTts = FlutterTts();
  int? _playingIndex;

  @override
  void initState() {
    super.initState();
    _initTts();
  }

  Future<void> _initTts() async {
    await _flutterTts.setLanguage("bn-BD");
    await _flutterTts.setSpeechRate(0.5);
    await _flutterTts.setVolume(1.0);
    await _flutterTts.setPitch(1.0);

    _flutterTts.setCompletionHandler(() {
      if (mounted) {
        setState(() {
          _playingIndex = null;
        });
      }
    });
  }

  @override
  void dispose() {
    _flutterTts.stop();
    super.dispose();
  }

  Future<void> _speak(String text, int index) async {
    if (_playingIndex == index) {
      await _flutterTts.stop();
      setState(() {
        _playingIndex = null;
      });
      return;
    }

    setState(() {
      _playingIndex = index;
    });

    await _flutterTts.stop();
    await _flutterTts.speak(text);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          widget.sectionName.isEmpty ? 'Chapter' : widget.sectionName,
        ),
        centerTitle: true,
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          await Future.delayed(const Duration(milliseconds: 1000));
        },
        child: ListView.builder(
          padding: const EdgeInsets.all(16.0),
          itemCount: widget.sectionHadiths.length,
          itemBuilder: (context, index) {
            final h = widget.sectionHadiths[index];
            return _buildOnlineCard(context, h, widget.bookName, index);
          },
        ),
      ),
    );
  }

  Widget _buildOnlineCard(
    BuildContext context,
    dynamic hadithJson,
    String bookName,
    int index,
  ) {
    final textBn = hadithJson['text'] ?? '';
    final refBook = hadithJson['reference']?['book'] ?? '';
    final refHadith = hadithJson['reference']?['hadith'] ?? '';
    final number = hadithJson['hadithnumber'] ?? '';
    final isPlaying = _playingIndex == index;

    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Container(
                  padding: const EdgeInsets.all(8),
                  decoration: BoxDecoration(
                    color: Theme.of(context).colorScheme.primary
                        .withOpacity(0.1),
                    shape: BoxShape.circle,
                  ),
                  child: Icon(
                    Icons.book,
                    color: Theme.of(context).colorScheme.primary,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Text(
                    'Hadith Number: $number',
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                IconButton(
                  onPressed: () => _speak(textBn, index),
                  icon: Icon(
                    isPlaying ? Icons.stop_circle : Icons.volume_up,
                    color: isPlaying
                        ? Colors.red
                        : Theme.of(context).colorScheme.primary,
                    size: 28,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            Text(
              textBn,
              style: const TextStyle(
                fontSize: 16,
                height: 1.5,
                color: Colors.black87,
              ),
            ),
            const SizedBox(height: 16),
            Align(
              alignment: Alignment.centerRight,
              child: Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 12,
                  vertical: 6,
                ),
                decoration: BoxDecoration(
                  color: Colors.grey[200],
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Text(
                  '$bookName (Book $refBook, Hadith $refHadith)',
                  style: TextStyle(
                    fontSize: 12,
                    color: Colors.grey[700],
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
