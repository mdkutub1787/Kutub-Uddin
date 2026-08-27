import 'package:flutter/material.dart';
import 'package:just_audio/just_audio.dart';
import 'package:cached_network_image/cached_network_image.dart';

class RadioStation {
  final String title;
  final String subtitle;
  final String url;
  final String imageUrl;
  final String language;
  final bool isLive;

  RadioStation({
    required this.title,
    required this.subtitle,
    required this.url,
    required this.imageUrl,
    required this.language,
    this.isLive = false,
  });
}

class RadioScreen extends StatefulWidget {
  const RadioScreen({super.key});

  @override
  State<RadioScreen> createState() => _RadioScreenState();
}

class _RadioScreenState extends State<RadioScreen> {
  late AudioPlayer _audioPlayer;
  int? _playingIndex;
  bool _isPlaying = false;
  String? _loadingError;

  final List<RadioStation> stations = [
    // 🌍 GLOBAL / HOLY PLACES
    RadioStation(
      title: 'Quran Radio Makkah',
      subtitle: 'إذَاعَةُ القُرآنِ الكَرِيم مِن مَكَّة',
      url: 'https://stream.radiojar.com/0tpy1h0kxtzuv',
      imageUrl: 'https://flagcdn.com/w320/sa.png',
      language: 'Arabic',
      isLive: true,
    ),
    RadioStation(
      title: 'Main Radio Mix',
      subtitle: 'مختارات من أجمل التلاوات',
      url: 'https://backup.qurango.net/radio/mix',
      imageUrl: 'unsplash', // Trigger generic icon
      language: 'Arabic',
      isLive: true,
    ),

    // 🕌 BEST RECITERS
    RadioStation(
      title: 'Abdulbasit Abdulsamad',
      subtitle: 'عبدالباسط عبدالصمد',
      url: 'https://backup.qurango.net/radio/abdulbasit_abdulsamad',
      imageUrl: 'unsplash',
      language: 'Arabic',
    ),
    RadioStation(
      title: 'Mishary Alafasy',
      subtitle: 'مشاري العفاسي',
      url: 'https://backup.qurango.net/radio/mishary_alafasi',
      imageUrl: 'unsplash',
      language: 'Arabic',
    ),
    RadioStation(
      title: 'Maher Al Meaqli',
      subtitle: 'ماهر المعيقلي',
      url: 'https://backup.qurango.net/radio/maher',
      imageUrl: 'unsplash',
      language: 'Arabic',
    ),
    RadioStation(
      title: 'Yasser Al-Dosari',
      subtitle: 'ياسر الدوسري',
      url: 'https://backup.qurango.net/radio/yasser_aldosari',
      imageUrl: 'unsplash',
      language: 'Arabic',
    ),

    // 🌍 TRANSLATION RADIOS (VERIFIED URLs)
    RadioStation(
      title: 'Urdu Radio',
      subtitle: 'قرآن ترجمہ اردو',
      url: 'https://backup.qurango.net/radio/translation_quran_urdu_basit',
      imageUrl: 'https://flagcdn.com/w320/pk.png',
      language: 'Urdu',
    ),
    RadioStation(
      title: 'English Radio',
      subtitle: 'Quran with English Meaning',
      url: 'https://backup.qurango.net/radio/translation_quran_english_basit',
      imageUrl: 'https://flagcdn.com/w320/gb.png',
      language: 'English',
    ),
    RadioStation(
      title: 'Turkish Radio',
      subtitle: 'Kur\'an Çevirisi',
      url: 'https://backup.qurango.net/radio/translation_quran_turkish',
      imageUrl: 'https://flagcdn.com/w320/tr.png',
      language: 'Turkish',
    ),
    RadioStation(
      title: 'Radio Islámica',
      subtitle: 'Español',
      url: 'https://backup.qurango.net/radio/translation_quran_spanish_afs',
      imageUrl: 'https://flagcdn.com/w320/es.png',
      language: 'Spanish',
    ),
    RadioStation(
      title: 'Francophone',
      subtitle: 'Français',
      url: 'https://backup.qurango.net/radio/translation_quran_french',
      imageUrl: 'https://flagcdn.com/w320/fr.png',
      language: 'French',
    ),
    RadioStation(
      title: 'Radio Islam',
      subtitle: 'Deutsch',
      url: 'https://backup.qurango.net/radio/translation_quran_german',
      imageUrl: 'https://flagcdn.com/w320/de.png',
      language: 'German',
    ),
    RadioStation(
      title: 'Persian Radio',
      subtitle: 'ترجمه قرآن فارسی',
      url: 'https://backup.qurango.net/radio/translation_quran_farsi',
      imageUrl: 'https://flagcdn.com/w320/ir.png',
      language: 'Persian',
    ),
    RadioStation(
      title: 'Kurdish Radio',
      subtitle: 'وەرگێڕانی قورئان',
      url: 'https://backup.qurango.net/radio/translation_quran_kurdish',
      imageUrl: 'https://flagcdn.com/w320/iq.png',
      language: 'Kurdish',
    ),
    RadioStation(
      title: 'Hausa Radio',
      subtitle: 'Fassarar Alkur\'ani',
      url: 'https://backup.qurango.net/radio/Translation_Quran_Hausa',
      imageUrl: 'https://flagcdn.com/w320/ng.png',
      language: 'Hausa',
    ),
    RadioStation(
      title: 'Bosnian Radio',
      subtitle: 'Prijevod Kur\'ana',
      url: 'https://backup.qurango.net/radio/translation_quran_bosnia',
      imageUrl: 'https://flagcdn.com/w320/ba.png',
      language: 'Bosnian',
    ),
    RadioStation(
      title: 'Albanian Radio',
      subtitle: 'Përkthimi i Kuranit',
      url: 'https://backup.qurango.net/radio/translation_quran_albanian',
      imageUrl: 'https://flagcdn.com/w320/al.png',
      language: 'Albanian',
    ),
    RadioStation(
      title: 'Radio China',
      subtitle: '中文',
      url: 'https://backup.qurango.net/radio/translation_quran_chinese',
      imageUrl: 'https://flagcdn.com/w320/cn.png',
      language: 'Chinese',
    ),
  ];

  @override
  void initState() {
    super.initState();
    _audioPlayer = AudioPlayer();
    _audioPlayer.playerStateStream.listen(
      (state) {
        if (mounted) {
          setState(() {
            _isPlaying = state.playing;
            if (state.processingState == ProcessingState.completed) {
              _playingIndex = null;
            }
          });
        }
      },
      onError: (Object e, StackTrace stackTrace) {
        if (mounted) {
          setState(() => _loadingError = 'Connection Error');
        }
      },
    );
  }

  @override
  void dispose() {
    _audioPlayer.dispose();
    super.dispose();
  }

  Future<void> _togglePlay(int index) async {
    try {
      setState(() => _loadingError = null);
      if (_playingIndex == index) {
        _isPlaying ? await _audioPlayer.pause() : await _audioPlayer.play();
      } else {
        setState(() {
          _playingIndex = index;
          _isPlaying = true;
        });
        await _audioPlayer.stop();
        await _audioPlayer.setUrl(stations[index].url);
        await _audioPlayer.play();
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _loadingError = 'Connection Error';
          _isPlaying = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: const Text(
          'Islamic Bulletin Radio',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 22),
        ),
        centerTitle: true,
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
        elevation: 0,
      ),
      body: Stack(
        children: [
          CustomScrollView(
            slivers: [
              _buildTopBanner(),
              _buildGridList(),
              const SliverToBoxAdapter(child: SizedBox(height: 100)), // Bottom padding for mini player
            ],
          ),
          if (_playingIndex != null)
            Align(
              alignment: Alignment.bottomCenter,
              child: _buildMiniPlayer(),
            ),
        ],
      ),
    );
  }

  Widget _buildTopBanner() {
    return SliverToBoxAdapter(
      child: Container(
        margin: const EdgeInsets.fromLTRB(16, 16, 16, 8),
        height: 140,
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(12),
          image: const DecorationImage(
            image: NetworkImage(
              'https://images.unsplash.com/photo-1584286595398-a59f21d313f5?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80',
            ),
            fit: BoxFit.cover,
            colorFilter: ColorFilter.mode(Colors.black45, BlendMode.darken),
          ),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              'Listen to the',
              style: TextStyle(color: Colors.white, fontSize: 14),
            ),
            const SizedBox(height: 4),
            const Text(
              'BEST RECITERS',
              style: TextStyle(
                color: Colors.white,
                fontSize: 28,
                fontWeight: FontWeight.bold,
                letterSpacing: 1.2,
              ),
            ),
            const SizedBox(height: 4),
            const Text(
              'الاستماع إلى أفضل القراء',
              style: TextStyle(color: Colors.white, fontSize: 16),
            ),
            const SizedBox(height: 12),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 6),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(20),
              ),
              child: const Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    'Play',
                    style: TextStyle(
                      color: Color(0xFF1E3A5F),
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  SizedBox(width: 8),
                  Icon(Icons.launch, size: 16, color: Color(0xFF1E3A5F)),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildGridList() {
    return SliverPadding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      sliver: SliverGrid(
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          mainAxisSpacing: 16.0,
          crossAxisSpacing: 16.0,
          childAspectRatio: 0.82,
        ),
        delegate: SliverChildBuilderDelegate(
          (BuildContext context, int index) {
            final station = stations[index];
            final isPlaying = _playingIndex == index;

            return GestureDetector(
              onTap: () => _togglePlay(index),
              child: Container(
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(12),
                  border: isPlaying ? Border.all(color: Colors.blue, width: 2) : Border.all(color: Colors.grey.shade200),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.05),
                      blurRadius: 4,
                      offset: const Offset(0, 2),
                    ),
                  ],
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Top Image
                    ClipRRect(
                      borderRadius: const BorderRadius.only(
                        topLeft: Radius.circular(12),
                        topRight: Radius.circular(12),
                      ),
                      child: SizedBox(
                        height: 80,
                        width: double.infinity,
                        child: station.imageUrl.contains('flagcdn')
                            ? CachedNetworkImage(
                                imageUrl: station.imageUrl,
                                fit: BoxFit.cover,
                                placeholder: (context, url) => Container(color: Colors.grey.shade200),
                                errorWidget: (context, url, error) => Container(color: Colors.grey.shade300, child: const Icon(Icons.flag)),
                              )
                            : Container(
                                decoration: BoxDecoration(
                                  gradient: LinearGradient(
                                    colors: [Colors.blueGrey.shade700, Colors.blueGrey.shade900],
                                    begin: Alignment.topLeft,
                                    end: Alignment.bottomRight,
                                  ),
                                ),
                                child: const Center(
                                  child: Icon(Icons.menu_book, color: Colors.white54, size: 40),
                                ),
                              ),
                      ),
                    ),
                    Expanded(
                      child: Padding(
                        padding: const EdgeInsets.all(12.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  station.title,
                                  style: const TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontSize: 14,
                                  ),
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  station.subtitle,
                                  style: TextStyle(
                                    fontSize: 12,
                                    color: Colors.grey.shade700,
                                  ),
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                              ],
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Row(
                                  children: [
                                    Icon(Icons.translate, size: 14, color: Colors.grey.shade600),
                                    const SizedBox(width: 4),
                                    Text(
                                      station.language,
                                      style: TextStyle(
                                        fontSize: 12,
                                        color: Colors.grey.shade800,
                                      ),
                                    ),
                                  ],
                                ),
                                Icon(
                                  isPlaying && _isPlaying ? Icons.pause : Icons.play_arrow,
                                  size: 20,
                                  color: Colors.black,
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            );
          },
          childCount: stations.length,
        ),
      ),
    );
  }

  Widget _buildMiniPlayer() {
    final station = stations[_playingIndex!];
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      decoration: BoxDecoration(
        color: const Color(0xFF1E3A5F),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Row(
        children: [
          ClipRRect(
            borderRadius: BorderRadius.circular(8),
            child: station.imageUrl.contains('flagcdn')
                ? CachedNetworkImage(
                    imageUrl: station.imageUrl,
                    width: 40,
                    height: 40,
                    fit: BoxFit.cover,
                    errorWidget: (context, url, error) => Container(color: Colors.grey, width: 40, height: 40),
                  )
                : Container(
                    width: 40,
                    height: 40,
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        colors: [Colors.blueGrey.shade700, Colors.blueGrey.shade900],
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                      ),
                    ),
                    child: const Icon(Icons.menu_book, color: Colors.white54, size: 20),
                  ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  station.title,
                  style: const TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                    fontSize: 14,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                if (_loadingError != null)
                  Text(
                    _loadingError!,
                    style: const TextStyle(color: Colors.redAccent, fontSize: 12),
                  )
                else
                  Text(
                    station.subtitle,
                    style: const TextStyle(color: Colors.white70, fontSize: 12),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
              ],
            ),
          ),
          StreamBuilder<PlayerState>(
            stream: _audioPlayer.playerStateStream,
            builder: (context, snapshot) {
              final state = snapshot.data?.processingState;
              if (state == ProcessingState.loading || state == ProcessingState.buffering) {
                return const Padding(
                  padding: EdgeInsets.all(12.0),
                  child: SizedBox(
                    width: 24,
                    height: 24,
                    child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2),
                  ),
                );
              }
              return IconButton(
                icon: Icon(
                  _isPlaying ? Icons.pause_circle_filled : Icons.play_circle_filled,
                  color: Colors.white,
                  size: 40,
                ),
                onPressed: () => _togglePlay(_playingIndex!),
              );
            },
          ),
        ],
      ),
    );
  }
}
