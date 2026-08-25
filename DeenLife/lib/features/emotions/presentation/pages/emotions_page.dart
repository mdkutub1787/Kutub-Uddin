import 'package:flutter/material.dart';

class MoodResult {
  final String source;
  final String text;
  final String care;

  MoodResult({required this.source, required this.text, this.care = ''});
}

class EmotionMood {
  final String id;
  final String nameEn;
  final List<MoodResult> results;

  EmotionMood({required this.id, required this.nameEn, required this.results});
}

class EmotionsPage extends StatefulWidget {
  const EmotionsPage({super.key});

  @override
  State<EmotionsPage> createState() => _EmotionsPageState();
}

class _EmotionsPageState extends State<EmotionsPage> {
  String? _activeMoodId;
  final Map<String, int> _moodIndices = {};

  final List<EmotionMood> _moods = [
    EmotionMood(
      id: 'sad',
      nameEn: 'Sad',
      results: [
        MoodResult(
          source: 'Quran 9:40',
          text: 'Do not grieve; indeed Allah is with us.',
          care: 'Cry to Him. He hears everything you don\'t say.',
        ),
      ],
    ),
    EmotionMood(
      id: 'anxious',
      nameEn: 'Anxious',
      results: [
        MoodResult(
          source: 'Quran 13:28',
          text: 'Verily, in the remembrance of Allah do hearts find rest.',
        ),
      ],
    ),
  ];

  void _onMoodSelected(String id) {
    setState(() {
      _activeMoodId = id;
      if (!_moodIndices.containsKey(id)) {
        _moodIndices[id] = 0;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('How are you feeling?'),
        centerTitle: true,
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          await Future.delayed(const Duration(milliseconds: 1000));
        },
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Wrap(
                spacing: 8,
                runSpacing: 8,
                alignment: WrapAlignment.center,
                children: _moods.map((mood) {
                  final isActive = _activeMoodId == mood.id;
                  return ChoiceChip(
                    label: Text(mood.nameEn),
                    selected: isActive,
                    onSelected: (_) => _onMoodSelected(mood.id),
                  );
                }).toList(),
              ),
              const SizedBox(height: 32),
              if (_activeMoodId != null) _buildResultCard(),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildResultCard() {
    final mood = _moods.firstWhere((m) => m.id == _activeMoodId);
    final idx = _moodIndices[mood.id] ?? 0;
    final result = mood.results[idx];

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(result.source, style: const TextStyle(fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Text(result.text, style: const TextStyle(fontSize: 18)),
          ],
        ),
      ),
    );
  }
}
