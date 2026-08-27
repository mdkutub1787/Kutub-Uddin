import 'package:flutter/material.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

class Kalima {
  final String title;
  final String arabic;
  final String pronunciation;
  final String meaning;

  Kalima({
    required this.title,
    required this.arabic,
    required this.pronunciation,
    required this.meaning,
  });
}

class KalimaScreen extends StatelessWidget {
  KalimaScreen({super.key});

  final List<Kalima> kalimas = [
    Kalima(
      title: '1. Kalima Tayyibah (Word of Purity)',
      arabic: 'لَا إِلٰهَ إِلَّا الله مُহَمَّদٌ রَسُولُ الله',
      pronunciation: 'La ilaha illallah muhammadur rasulullah',
      meaning: 'There is no God but Allah, [and] Muhammad is the messenger of Allah.',
    ),
    Kalima(
      title: '2. Kalima Shahadat (Word of Evidence)',
      arabic: 'أَشْهَدُ أنْ لا إلَٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأشْهَدُ أنَّ مُחَمَّدًا عَبْدُهُ وَرَسُولُهُ',
      pronunciation: 'Ash-hadu an la ilaha illallahu wahdahu la sharika lahu, wa ash-hadu anna muhammadan \'abduhu wa rasuluh.',
      meaning: 'I bear witness that there is no God but Allah alone, He has no partner, and I bear witness that Muhammad is His servant and messenger.',
    ),
    Kalima(
      title: '3. Kalima Tamjeed (Word of Majesty)',
      arabic: 'سُبْحَان اللهِ وَالْحَمْدُلِلّهِ وَلا إِلهَ إِلّااللّهُ وَاللّهُ أكْبَرُ وَلا حَوْلَ وَلاَ قُوَّةَ إِلَّا بِاللّهِ الْعَلِيِّ الْعَظِيْم',
      pronunciation: 'Subhanallahi wal hamdulillahi wa la ilaha illallahu wallahu akbar, wa la hawla wa la quwwata illa billahil \'aliyyil \'azeem.',
      meaning: 'Glory be to Allah and all praise be to Allah, there is no God but Allah, and Allah is the Greatest. There is no might or power except with Allah, the Exalted, the Great.',
    ),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: Text(
          context.tr('Kalima Collection'),
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
      ),
      body: RefreshIndicator(
        onRefresh: () async =>
            await Future.delayed(const Duration(milliseconds: 1000)),
        child: ListView.builder(
          padding: const EdgeInsets.all(20.0),
          itemCount: kalimas.length,
          itemBuilder: (context, index) {
            final kalima = kalimas[index];
            return Container(
              margin: const EdgeInsets.only(bottom: 20),
              padding: const EdgeInsets.all(24),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(16),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withAlpha(10),
                    blurRadius: 10,
                    offset: const Offset(0, 4),
                  ),
                ],
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text(
                    kalima.title,
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1E3A5F),
                    ),
                  ),
                  const SizedBox(height: 20),
                  Text(
                    kalima.arabic,
                    textAlign: TextAlign.center,
                    style: const TextStyle(
                      fontSize: 28,
                      height: 1.8,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1E3A5F),
                    ),
                  ),
                  const SizedBox(height: 20),
                  const Divider(),
                  const SizedBox(height: 20),
                  _kalimaSection(
                    context,
                    'Pronunciation',
                    kalima.pronunciation,
                    italic: true,
                  ),
                  const SizedBox(height: 16),
                  _kalimaSection(context, 'Meaning', kalima.meaning),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  Widget _kalimaSection(
    BuildContext context,
    String label,
    String content, {
    bool italic = false,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          context.tr(label),
          style: const TextStyle(
            fontWeight: FontWeight.bold,
            color: Colors.grey,
            fontSize: 12,
          ),
        ),
        const SizedBox(height: 6),
        Text(
          content,
          style: TextStyle(
            fontSize: 14,
            height: 1.5,
            fontStyle: italic ? FontStyle.italic : FontStyle.normal,
            color: Colors.black87,
          ),
        ),
      ],
    );
  }
}

