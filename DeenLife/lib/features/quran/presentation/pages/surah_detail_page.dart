import 'package:flutter/material.dart';
import 'package:quran/quran.dart' as quran;
import 'package:just_audio/just_audio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:deen_life/core/localization/app_localizations.dart';
import 'package:flutter_widget_from_html/flutter_widget_from_html.dart';
import 'package:google_fonts/google_fonts.dart';
import '../providers/surah_tafsir_provider.dart';
import '../../../../core/data/tafsir_data.dart';

class SurahDetailPage extends ConsumerStatefulWidget {
  final int surahNumber;
  final String surahName;

  const SurahDetailPage({
    super.key,
    required this.surahNumber,
    required this.surahName,
  });

  @override
  ConsumerState<SurahDetailPage> createState() => _SurahDetailPageState();
}

class _SurahDetailPageState extends ConsumerState<SurahDetailPage> {
  late AudioPlayer _audioPlayer;
  bool _isInit = false;

  @override
  void initState() {
    super.initState();
    _audioPlayer = AudioPlayer();
    _initAudioPlaylist();
  }

  Future<void> _initAudioPlaylist() async {
    final playlist = ConcatenatingAudioSource(
      useLazyPreparation: true,
      children: List.generate(
        quran.getVerseCount(widget.surahNumber),
        (index) => AudioSource.uri(
          Uri.parse(quran.getAudioURLByVerse(widget.surahNumber, index + 1)),
        ),
      ),
    );

    try {
      await _audioPlayer.setAudioSource(playlist, initialIndex: 0, initialPosition: Duration.zero);
      setState(() {
        _isInit = true;
      });
    } catch (e) {
      debugPrint("Error loading audio: $e");
    }
  }

  @override
  void dispose() {
    _audioPlayer.dispose();
    super.dispose();
  }

  void _showTafsirSelectionSheet() {
    final currentTafsirId = ref.read(selectedTafsirIdProvider);

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (context) {
        return DraggableScrollableSheet(
          initialChildSize: 0.6,
          minChildSize: 0.4,
          maxChildSize: 0.9,
          expand: false,
          builder: (context, scrollController) {
            return Column(
              children: [
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Text(
                    context.tr('Select Translation / Tafsir'),
                    style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                ),
                Expanded(
                  child: GridView.builder(
                    controller: scrollController,
                    padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      crossAxisSpacing: 16,
                      mainAxisSpacing: 16,
                      childAspectRatio: 0.85,
                    ),
                    itemCount: allTafsirs.length + 1,
                    itemBuilder: (context, index) {
                      final bool isDefault = index == 0;
                      final String titleBn = isDefault ? 'অনুবাদ' : allTafsirs[index - 1].name;
                      final String subtitleEn = isDefault ? 'Translation (Default)' : allTafsirs[index - 1].authorName;
                      final int? tafsirId = isDefault ? null : allTafsirs[index - 1].id;
                      final bool isSelected = currentTafsirId == tafsirId;

                      return GestureDetector(
                        onTap: () {
                          ref.read(selectedTafsirIdProvider.notifier).state = tafsirId;
                          Navigator.pop(context);
                        },
                        child: Container(
                          padding: const EdgeInsets.all(12),
                          decoration: BoxDecoration(
                            gradient: LinearGradient(
                              colors: isSelected
                                  ? [
                                      Theme.of(context).colorScheme.primary,
                                      Theme.of(context).colorScheme.primary.withOpacity(0.8),
                                    ]
                                  : [
                                      Colors.grey[400]!,
                                      Colors.grey[500]!,
                                    ],
                              begin: Alignment.topLeft,
                              end: Alignment.bottomRight,
                            ),
                            borderRadius: BorderRadius.circular(16),
                            boxShadow: [
                              if (isSelected)
                                BoxShadow(
                                  color: Theme.of(context).colorScheme.primary.withOpacity(0.3),
                                  blurRadius: 10,
                                  offset: const Offset(0, 4),
                                )
                            ],
                          ),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              const Icon(Icons.menu_book, color: Colors.white, size: 40),
                              const SizedBox(height: 12),
                              Text(
                                titleBn,
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontWeight: FontWeight.bold,
                                  fontSize: 16,
                                ),
                                textAlign: TextAlign.center,
                                maxLines: 2,
                                overflow: TextOverflow.ellipsis,
                              ),
                              const SizedBox(height: 4),
                              Text(
                                subtitleEn,
                                style: TextStyle(
                                  color: Colors.white.withOpacity(0.8),
                                  fontSize: 12,
                                ),
                                textAlign: TextAlign.center,
                                maxLines: 2,
                                overflow: TextOverflow.ellipsis,
                              ),
                              if (isSelected) ...[
                                const SizedBox(height: 8),
                                const Icon(Icons.check_circle, color: Colors.white, size: 20),
                              ],
                            ],
                          ),
                        ),
                      );
                    },
                  ),
                ),
              ],
            );
          },
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final tafsirAsync = ref.watch(surahTafsirProvider(widget.surahNumber));

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.surahName),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.library_books),
            tooltip: context.tr('Select Tafsir'),
            onPressed: _showTafsirSelectionSheet,
          ),
          StreamBuilder<PlayerState>(
            stream: _audioPlayer.playerStateStream,
            builder: (context, snapshot) {
              final playerState = snapshot.data;
              final processingState = playerState?.processingState;
              final playing = playerState?.playing ?? false;

              if (processingState == ProcessingState.loading || processingState == ProcessingState.buffering) {
                return const Padding(
                  padding: EdgeInsets.all(16.0),
                  child: SizedBox(
                    width: 24, height: 24,
                    child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2),
                  ),
                );
              } else if (playing) {
                return IconButton(
                  icon: const Icon(Icons.pause_circle_filled, size: 32),
                  onPressed: _audioPlayer.pause,
                );
              } else {
                return IconButton(
                  icon: const Icon(Icons.play_circle_filled, size: 32),
                  onPressed: _isInit ? _audioPlayer.play : null,
                );
              }
            },
          ),
        ],
      ),
      body: SafeArea(
        child: RefreshIndicator(
          onRefresh: () async {
            await Future.delayed(const Duration(milliseconds: 1000));
          },
          child: ListView.separated(
          padding: const EdgeInsets.all(16.0),
          itemCount: quran.getVerseCount(widget.surahNumber),
          separatorBuilder: (context, index) => const Divider(height: 32),
          itemBuilder: (context, index) {
            final verseNumber = index + 1;
            
            return StreamBuilder<int?>(
              stream: _audioPlayer.currentIndexStream,
              builder: (context, snapshot) {
                final playingIndex = snapshot.data;
                final isPlayingThisVerse = playingIndex == index;
                
                String verseText = quran.getVerse(widget.surahNumber, verseNumber, verseEndSymbol: true);
                
                // If it's the first verse and not Surah Fatiha (1) or Tawbah (9), 
                // remove Bismillah from the verse text because we'll show it as a header
                if (verseNumber == 1 && widget.surahNumber != 1 && widget.surahNumber != 9) {
                  // Some versions of the text include the Basmala at the start of the first verse
                  // Try removing it using the package's constant and a common literal string
                  verseText = verseText.replaceFirst(quran.basmala, "");
                  verseText = verseText.replaceFirst("بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ", "");
                  verseText = verseText.replaceFirst("بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ", "");
                  verseText = verseText.trim();
                }

                return Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    // Show Bismillah as a separate header at the very top (not as a verse number)
                    if (index == 0 && widget.surahNumber != 1 && widget.surahNumber != 9)
                      Padding(
                        padding: const EdgeInsets.only(bottom: 24.0),
                        child: Text(
                          quran.basmala,
                          textAlign: TextAlign.center,
                          style: GoogleFonts.amiri(
                            fontSize: 32,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    
                    Container(
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        color: isPlayingThisVerse 
                            ? Theme.of(context).colorScheme.primary.withOpacity(0.1) 
                            : Colors.transparent,
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          // Verse header (number & play btn)
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
                                const SizedBox(width: 8),
                                Text(
                                  '${context.tr('Verse')} $verseNumber',
                                  style: TextStyle(
                                    color: Theme.of(context).colorScheme.primary,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                const Spacer(),
                                IconButton(
                                  padding: EdgeInsets.zero,
                                  constraints: const BoxConstraints(),
                                  icon: Icon(
                                    isPlayingThisVerse ? Icons.volume_up : Icons.play_arrow,
                                    color: isPlayingThisVerse ? Colors.red : Theme.of(context).colorScheme.primary,
                                  ),
                                  onPressed: () {
                                    if (isPlayingThisVerse && _audioPlayer.playing) {
                                      _audioPlayer.pause();
                                    } else {
                                      _audioPlayer.seek(Duration.zero, index: index);
                                      _audioPlayer.play();
                                    }
                                  },
                                )
                              ],
                            ),
                          ),
                          const SizedBox(height: 24),
                          
                          // Arabic text
                          Text(
                            verseText,
                            textAlign: TextAlign.center,
                            style: GoogleFonts.lateef(
                              fontSize: 32,
                              height: 1.5,
                            ),
                          ),
                          
                          const SizedBox(height: 16),
                          
                          // Tafsir / Translation
                          tafsirAsync.when(
                            data: (tafsirMap) {
                              final text = tafsirMap[verseNumber] ?? '';
                              if (text.isEmpty) return const SizedBox.shrink();
                              return Card(
                                margin: const EdgeInsets.only(top: 16),
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                                elevation: 1,
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
                                              color: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                                              shape: BoxShape.circle,
                                            ),
                                            child: Icon(
                                              Icons.menu_book,
                                              color: Theme.of(context).colorScheme.primary,
                                            ),
                                          ),
                                          const SizedBox(width: 12),
                                          Expanded(
                                            child: Text(
                                              context.tr('Translation / Tafsir'),
                                              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                                            ),
                                          ),
                                        ],
                                      ),
                                      const SizedBox(height: 16),
                                      HtmlWidget(
                                        text,
                                        textStyle: const TextStyle(
                                          fontSize: 16,
                                          height: 1.6,
                                          color: Colors.black87,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              );
                            },
                            loading: () => const Text('Loading tafsir...', style: TextStyle(color: Colors.grey)),
                            error: (err, stack) => const Text('Tap to download tafsir offline', style: TextStyle(color: Colors.grey)),
                          ),
                        ],
                      ),
                    ),
                  ],
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
