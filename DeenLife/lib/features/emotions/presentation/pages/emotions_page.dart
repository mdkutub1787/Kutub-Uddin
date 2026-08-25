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
        MoodResult(
          source: 'Hadith',
          text: 'No fatigue, nor disease, nor sorrow, nor sadness, nor hurt, nor distress befalls a Muslim... but that Allah expiates some of his sins for that.',
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
    EmotionMood(
      id: 'angry',
      nameEn: 'Angry',
      results: [
        MoodResult(
          source: 'Hadith',
          text: 'The strong is not the one who overcomes the people by his strength, but the strong is the one who controls himself while in anger.',
          care: 'Seek refuge in Allah from Satan, and perform Wudu.',
        ),
      ],
    ),
    EmotionMood(
      id: 'grateful',
      nameEn: 'Grateful',
      results: [
        MoodResult(
          source: 'Quran 14:7',
          text: 'If you are grateful, I will surely increase you [in favor].',
        ),
      ],
    ),
    EmotionMood(
      id: 'lonely',
      nameEn: 'Lonely',
      results: [
        MoodResult(
          source: 'Quran 50:16',
          text: 'And We are closer to him than [his] jugular vein.',
        ),
      ],
    ),
    EmotionMood(
      id: 'lazy',
      nameEn: 'Lazy / Unmotivated',
      results: [
        MoodResult(
          source: 'Dua',
          text: 'O Allah, I take refuge in You from anxiety and sorrow, weakness and laziness, miserliness and cowardice...',
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

  void _nextResult(EmotionMood mood) {
    setState(() {
      _moodIndices[mood.id] = ((_moodIndices[mood.id] ?? 0) + 1) % mood.results.length;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('How are you feeling?'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
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
                  selectedColor: Theme.of(context).colorScheme.primary.withOpacity(0.2),
                  labelStyle: TextStyle(
                    color: isActive ? Theme.of(context).colorScheme.primary : null,
                    fontWeight: isActive ? FontWeight.bold : FontWeight.normal,
                  ),
                );
              }).toList(),
            ),
            const SizedBox(height: 32),
            if (_activeMoodId != null) ...[
              _buildResultCard(),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildResultCard() {
    final mood = _moods.firstWhere((m) => m.id == _activeMoodId);
    final idx = _moodIndices[mood.id] ?? 0;
    final result = mood.results[idx];

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Container(
          padding: const EdgeInsets.all(24),
          decoration: BoxDecoration(
            color: Theme.of(context).cardColor,
            borderRadius: BorderRadius.circular(16),
            border: Border.all(color: Theme.of(context).colorScheme.primary.withOpacity(0.3)),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.05),
                blurRadius: 10,
                offset: const Offset(0, 4),
              ),
            ],
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Text(
                  result.source,
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                    color: Theme.of(context).colorScheme.primary,
                  ),
                ),
              ),
              const SizedBox(height: 16),
              Text(
                result.text,
                style: const TextStyle(fontSize: 18, height: 1.5),
              ),
              if (result.care.isNotEmpty) ...[
                const SizedBox(height: 16),
                Container(
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: Colors.amber.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(12),
                    border: Border.all(color: Colors.amber.withOpacity(0.5)),
                  ),
                  child: Row(
                    children: [
                      const Icon(Icons.favorite, color: Colors.amber, size: 20),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          result.care,
                          style: const TextStyle(fontStyle: FontStyle.italic),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ],
          ),
        ),
        if (mood.results.length > 1) ...[
          const SizedBox(height: 16),
          TextButton.icon(
            onPressed: () => _nextResult(mood),
            icon: const Icon(Icons.shuffle),
            label: const Text('Show another'),
          ),
        ],
      ],
    );
  }
}
