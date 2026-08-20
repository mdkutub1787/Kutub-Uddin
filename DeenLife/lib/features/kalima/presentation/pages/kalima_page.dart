import 'package:flutter/material.dart';

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

class KalimaPage extends StatelessWidget {
  KalimaPage({super.key});

  final List<Kalima> kalimas = [
    Kalima(
      title: '1. Kalima Tayyibah (Word of Purity)',
      arabic: 'لَا إِلٰهَ إِلَّا الله مُحَمَّدٌ رَسُولُ الله',
      pronunciation: 'La ilaha illallah muhammadur rasulullah',
      meaning: 'There is no God but Allah, [and] Muhammad is the messenger of Allah.',
    ),
    Kalima(
      title: '2. Kalima Shahadat (Word of Evidence)',
      arabic: 'أَشْهَدُ أنْ لا إلَٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأشْهَدُ أنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ',
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
      appBar: AppBar(
        title: const Text('Kalima Collection'),
        centerTitle: true,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16.0),
        itemCount: kalimas.length,
        itemBuilder: (context, index) {
          final kalima = kalimas[index];
          return Card(
            elevation: 2,
            margin: const EdgeInsets.only(bottom: 16),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
            child: Padding(
              padding: const EdgeInsets.all(20.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text(
                    kalima.title,
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      color: Theme.of(context).colorScheme.primary,
                    ),
                  ),
                  const SizedBox(height: 16),
                  Text(
                    kalima.arabic,
                    textAlign: TextAlign.right,
                    style: const TextStyle(
                      fontSize: 28,
                      height: 1.8,
                    ),
                  ),
                  const SizedBox(height: 16),
                  Container(
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: Theme.of(context).colorScheme.primary.withOpacity(0.05),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          'Pronunciation:',
                          style: TextStyle(fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          kalima.pronunciation,
                          style: const TextStyle(fontStyle: FontStyle.italic),
                        ),
                        const SizedBox(height: 12),
                        const Text(
                          'Meaning:',
                          style: TextStyle(fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 4),
                        Text(kalima.meaning),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
