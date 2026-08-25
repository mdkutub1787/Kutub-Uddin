import 'package:flutter/material.dart';
import 'package:just_audio/just_audio.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

class RadioStation {
  final String title;
  final String subtitle;
  final String url;
  final bool isLive;

  RadioStation({
    required this.title,
    required this.subtitle,
    required this.url,
    this.isLive = false,
  });
}

class RadioPage extends StatefulWidget {
  const RadioPage({super.key});

  @override
  State<RadioPage> createState() => _RadioPageState();
}

class _RadioPageState extends State<RadioPage> {
  late AudioPlayer _audioPlayer;
  int? _playingIndex;
  bool _isPlaying = false;
  String? _loadingError;

  final List<RadioStation> stations = [
    RadioStation(
      title: 'Makkah Live (Quran)',
      subtitle: '24/7 Live Quran from Makkah',
      url: 'https://qurango.net/radio/makkah',
      isLive: true,
    ),
    RadioStation(
      title: 'Madinah Live (Quran)',
      subtitle: '24/7 Live Quran from Madinah',
      url: 'https://qurango.net/radio/madinah',
      isLive: true,
    ),
    RadioStation(
      title: 'Quran Radio - Mishary Alafasy',
      subtitle: 'Continuous Recitation',
      url: 'https://stream.radiojar.com/8s5u8tp48vduv',
    ),
    RadioStation(
      title: 'Quran Radio - English Translation',
      subtitle: 'Quran with English Meaning',
      url: 'https://stream.zeno.fm/3r77vwa8mreuv',
    ),
    RadioStation(
      title: 'Quran Radio - Bangla Translation',
      subtitle: 'আল-কুরআন (বাংলা অনুবাদসহ)',
      url: 'https://qurango.net/radio/tarjumat_bangla',
    ),
  ];

  @override
  void initState() {
    super.initState();
    _audioPlayer = AudioPlayer();
    
    // Listen to player state to update UI
    _audioPlayer.playerStateStream.listen((state) {
      if (mounted) {
        setState(() {
          _isPlaying = state.playing;
          if (state.processingState == ProcessingState.completed) {
            _playingIndex = null;
          }
        });
      }
    }, onError: (Object e, StackTrace stackTrace) {
      debugPrint('A stream error occurred: $e');
      setState(() {
        _loadingError = e.toString();
      });
    });
  }

  @override
  void dispose() {
    _audioPlayer.dispose();
    super.dispose();
  }

  Future<void> _togglePlay(int index) async {
    try {
      setState(() {
        _loadingError = null;
      });

      if (_playingIndex == index) {
        if (_isPlaying) {
          await _audioPlayer.pause();
        } else {
          await _audioPlayer.play();
        }
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
      debugPrint("Error playing radio: $e");
      if (mounted) {
        setState(() {
          _loadingError = e.toString();
          _isPlaying = false;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error playing radio: $e'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(context.tr('Islamic Radio')),
        centerTitle: true,
      ),
      body: Column(
        children: [
          // Now Playing Header
          Container(
            padding: const EdgeInsets.all(24),
            width: double.infinity,
            decoration: BoxDecoration(
              color: Theme.of(context).colorScheme.primary.withOpacity(0.9),
              borderRadius: const BorderRadius.only(
                bottomLeft: Radius.circular(32),
                bottomRight: Radius.circular(32),
              ),
            ),
            child: Column(
              children: [
                const Icon(Icons.radio, size: 64, color: Colors.white70),
                const SizedBox(height: 16),
                Text(
                  _playingIndex != null ? stations[_playingIndex!].title : 'Select a Station',
                  style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.white),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 8),
                Text(
                  _playingIndex != null ? stations[_playingIndex!].subtitle : 'Ready to play',
                  style: const TextStyle(color: Colors.white70),
                ),
                const SizedBox(height: 24),
                if (_playingIndex != null)
                  StreamBuilder<PlayerState>(
                    stream: _audioPlayer.playerStateStream,
                    builder: (context, snapshot) {
                      final processingState = snapshot.data?.processingState;
                      if (processingState == ProcessingState.loading || processingState == ProcessingState.buffering) {
                        return const CircularProgressIndicator(color: Colors.white);
                      }
                      
                      if (_loadingError != null) {
                        return IconButton(
                          icon: const Icon(Icons.refresh, color: Colors.white, size: 40),
                          onPressed: () => _togglePlay(_playingIndex!),
                        );
                      }

                      return FloatingActionButton(
                        onPressed: () => _togglePlay(_playingIndex!),
                        backgroundColor: Colors.white,
                        foregroundColor: Theme.of(context).colorScheme.primary,
                        child: Icon(_isPlaying ? Icons.pause : Icons.play_arrow, size: 32),
                      );
                    },
                  ),
                if (_loadingError != null)
                  Padding(
                    padding: const EdgeInsets.only(top: 8.0),
                    child: Text(
                      'Connection Error',
                      style: TextStyle(color: Colors.red[100], fontSize: 12),
                    ),
                  ),
              ],
            ),
          ),
          const SizedBox(height: 16),
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: stations.length,
              itemBuilder: (context, index) {
                final station = stations[index];
                final isCurrentlyPlaying = _playingIndex == index;

                return Card(
                  margin: const EdgeInsets.only(bottom: 12),
                  elevation: isCurrentlyPlaying ? 4 : 1,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(16),
                    side: isCurrentlyPlaying
                        ? BorderSide(color: Theme.of(context).colorScheme.primary, width: 2)
                        : BorderSide.none,
                  ),
                  child: ListTile(
                    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                    leading: CircleAvatar(
                      backgroundColor: isCurrentlyPlaying ? Theme.of(context).colorScheme.primary : Colors.grey[200],
                      child: Icon(
                        isCurrentlyPlaying && _isPlaying ? Icons.pause : Icons.play_arrow,
                        color: isCurrentlyPlaying ? Colors.white : Colors.grey[700],
                      ),
                    ),
                    title: Text(
                      station.title,
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        color: isCurrentlyPlaying ? Theme.of(context).colorScheme.primary : null,
                      ),
                    ),
                    subtitle: Text(station.subtitle),
                    trailing: station.isLive 
                      ? Container(
                          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                          decoration: BoxDecoration(color: Colors.red, borderRadius: BorderRadius.circular(4)),
                          child: const Text('LIVE', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                        )
                      : null,
                    onTap: () => _togglePlay(index),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
