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
      title: 'Quran Radio - English',
      subtitle: 'Quran with English Meaning',
      url: 'https://stream.zeno.fm/3r77vwa8mreuv',
    ),
    RadioStation(
      title: 'Quran Radio - Bangla',
      subtitle: 'আল-কুরআন (বাংলা অনুবাদসহ)',
      url: 'https://qurango.net/radio/tarjumat_bangla',
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
            if (state.processingState == ProcessingState.completed)
              _playingIndex = null;
          });
        }
      },
      onError: (Object e, StackTrace stackTrace) {
        setState(() => _loadingError = e.toString());
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
      if (mounted)
        setState(() {
          _loadingError = e.toString();
          _isPlaying = false;
        });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: Text(
          context.tr('Islamic Radio'),
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
      ),
      body: Column(
        children: [
          _buildNowPlayingHeader(),
          Expanded(child: _buildStationList()),
        ],
      ),
    );
  }

  Widget _buildNowPlayingHeader() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(32),
      decoration: const BoxDecoration(
        color: Color(0xFF1E3A5F),
        borderRadius: BorderRadius.only(
          bottomLeft: Radius.circular(40),
          bottomRight: Radius.circular(40),
        ),
      ),
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(20),
            decoration: BoxDecoration(
              color: Colors.white.withAlpha(20),
              shape: BoxShape.circle,
            ),
            child: const Icon(Icons.headset, size: 60, color: Colors.white70),
          ),
          const SizedBox(height: 24),
          Text(
            _playingIndex != null
                ? stations[_playingIndex!].title
                : context.tr('Select a Station'),
            style: const TextStyle(
              fontSize: 22,
              fontWeight: FontWeight.bold,
              color: Colors.white,
            ),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 8),
          Text(
            _playingIndex != null
                ? stations[_playingIndex!].subtitle
                : context.tr('Ready to play'),
            style: const TextStyle(color: Colors.white70, fontSize: 14),
          ),
          const SizedBox(height: 32),
          if (_playingIndex != null) _buildPlayControl(),
          if (_loadingError != null)
            Text(
              'Connection Error',
              style: TextStyle(color: Colors.red[100], fontSize: 12),
            ),
        ],
      ),
    );
  }

  Widget _buildPlayControl() {
    return StreamBuilder<PlayerState>(
      stream: _audioPlayer.playerStateStream,
      builder: (context, snapshot) {
        final state = snapshot.data?.processingState;
        if (state == ProcessingState.loading ||
            state == ProcessingState.buffering) {
          return const CircularProgressIndicator(color: Colors.white);
        }
        return FloatingActionButton.large(
          onPressed: () => _togglePlay(_playingIndex!),
          backgroundColor: Colors.white,
          foregroundColor: const Color(0xFF1E3A5F),
          child: Icon(_isPlaying ? Icons.pause : Icons.play_arrow, size: 48),
        );
      },
    );
  }

  Widget _buildStationList() {
    return ListView.builder(
      padding: const EdgeInsets.all(20),
      itemCount: stations.length,
      itemBuilder: (context, index) {
        final station = stations[index];
        final isPlaying = _playingIndex == index;
        return Container(
          margin: const EdgeInsets.only(bottom: 12),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16),
            border: isPlaying
                ? Border.all(color: const Color(0xFF1E3A5F), width: 2)
                : null,
            boxShadow: [
              BoxShadow(
                color: Colors.black.withAlpha(5),
                blurRadius: 10,
                offset: const Offset(0, 4),
              ),
            ],
          ),
          child: ListTile(
            contentPadding: const EdgeInsets.symmetric(
              horizontal: 20,
              vertical: 8,
            ),
            leading: CircleAvatar(
              backgroundColor: isPlaying
                  ? const Color(0xFF1E3A5F)
                  : const Color(0xFFF1F3F5),
              child: Icon(
                isPlaying && _isPlaying ? Icons.pause : Icons.play_arrow,
                color: isPlaying ? Colors.white : Colors.grey[600],
              ),
            ),
            title: Text(
              station.title,
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
            ),
            subtitle: Text(
              station.subtitle,
              style: const TextStyle(fontSize: 12),
            ),
            trailing: station.isLive ? _liveIndicator() : null,
            onTap: () => _togglePlay(index),
          ),
        );
      },
    );
  }

  Widget _liveIndicator() {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: Colors.red,
        borderRadius: BorderRadius.circular(6),
      ),
      child: const Text(
        'LIVE',
        style: TextStyle(
          color: Colors.white,
          fontSize: 10,
          fontWeight: FontWeight.bold,
        ),
      ),
    );
  }
}
