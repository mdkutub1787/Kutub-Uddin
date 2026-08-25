import 'package:flutter/material.dart';

class RadioStation {
  final String title;
  final String subtitle;
  final bool isLive;

  RadioStation({required this.title, required this.subtitle, this.isLive = false});
}

class RadioPage extends StatefulWidget {
  const RadioPage({super.key});

  @override
  State<RadioPage> createState() => _RadioPageState();
}

class _RadioPageState extends State<RadioPage> {
  int? _playingIndex;
  bool _isPlaying = false;

  final List<RadioStation> stations = [
    RadioStation(title: 'Makkah Live (Masjid al-Haram)', subtitle: '24/7 Live Broadcast', isLive: true),
    RadioStation(title: 'Madinah Live (Al-Masjid an-Nabawi)', subtitle: '24/7 Live Broadcast', isLive: true),
    RadioStation(title: 'Quran Radio - Mishary Alafasy', subtitle: 'Continuous Recitation'),
    RadioStation(title: 'Quran Radio - Abdul Basit', subtitle: 'Continuous Recitation'),
    RadioStation(title: 'Islamic Lectures (English)', subtitle: 'Various Scholars'),
  ];

  void _togglePlay(int index) {
    setState(() {
      if (_playingIndex == index) {
        _isPlaying = !_isPlaying;
      } else {
        _playingIndex = index;
        _isPlaying = true;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Islamic Radio'),
        centerTitle: true,
      ),
      body: Column(
        children: [
          // Now Playing Header
          Container(
            padding: const EdgeInsets.all(24),
            decoration: const BoxDecoration(
              image: DecorationImage(
                image: AssetImage('assets/radio_bg.jpg'),
                fit: BoxFit.cover,
                colorFilter: ColorFilter.mode(
                  Colors.black54, // Dark overlay
                  BlendMode.darken,
                ),
              ),
              borderRadius: BorderRadius.only(
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
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      IconButton(
                        onPressed: () {},
                        icon: const Icon(Icons.skip_previous, size: 32, color: Colors.white),
                      ),
                      const SizedBox(width: 16),
                      FloatingActionButton(
                        onPressed: () => _togglePlay(_playingIndex!),
                        elevation: 0,
                        backgroundColor: Theme.of(context).colorScheme.primary,
                        foregroundColor: Colors.white,
                        child: Icon(_isPlaying ? Icons.pause : Icons.play_arrow, size: 32),
                      ),
                      const SizedBox(width: 16),
                      IconButton(
                        onPressed: () {},
                        icon: const Icon(Icons.skip_next, size: 32, color: Colors.white),
                      ),
                    ],
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
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(16),
                    side: isCurrentlyPlaying
                        ? BorderSide(color: Theme.of(context).colorScheme.primary, width: 2)
                        : BorderSide.none,
                  ),
                  child: ListTile(
                    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                    leading: Container(
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: isCurrentlyPlaying
                            ? Theme.of(context).colorScheme.primary
                            : Colors.grey[200],
                        shape: BoxShape.circle,
                      ),
                      child: Icon(
                        isCurrentlyPlaying && _isPlaying ? Icons.pause : Icons.play_arrow,
                        color: isCurrentlyPlaying ? Colors.white : Colors.grey[700],
                      ),
                    ),
                    title: Row(
                      children: [
                        Expanded(
                          child: Text(
                            station.title,
                            style: const TextStyle(fontWeight: FontWeight.bold),
                          ),
                        ),
                        if (station.isLive)
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                            decoration: BoxDecoration(
                              color: Colors.red,
                              borderRadius: BorderRadius.circular(4),
                            ),
                            child: const Text(
                              'LIVE',
                              style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
                            ),
                          ),
                      ],
                    ),
                    subtitle: Text(station.subtitle),
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
