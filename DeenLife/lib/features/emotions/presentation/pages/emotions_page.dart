import 'package:flutter/material.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

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
      if (!_moodIndices.containsKey(id)) _moodIndices[id] = 0;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: Text(
          context.tr('How are you feeling?'),
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          children: [
            Wrap(
              spacing: 12,
              runSpacing: 12,
              alignment: WrapAlignment.center,
              children: _moods.map((mood) {
                final isActive = _activeMoodId == mood.id;
                return ChoiceChip(
                  label: Text(mood.nameEn),
                  selected: isActive,
                  onSelected: (_) => _onMoodSelected(mood.id),
                  selectedColor: const Color(0xFF1E3A5F),
                  labelStyle: TextStyle(
                    color: isActive ? Colors.white : const Color(0xFF1E3A5F),
                    fontWeight: FontWeight.bold,
                  ),
                );
              }).toList(),
            ),
            const SizedBox(height: 40),
            if (_activeMoodId != null) _buildResultCard(),
          ],
        ),
      ),
    );
  }

  Widget _buildResultCard() {
    final mood = _moods.firstWhere((m) => m.id == _activeMoodId);
    final idx = _moodIndices[mood.id] ?? 0;
    final result = mood.results[idx];

    return Container(
      padding: const EdgeInsets.all(32),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withAlpha(5),
            blurRadius: 20,
            offset: const Offset(0, 10),
          ),
        ],
      ),
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
            decoration: BoxDecoration(
              color: const Color(0xFF1E3A5F).withAlpha(20),
              borderRadius: BorderRadius.circular(20),
            ),
            child: Text(
              result.source,
              style: const TextStyle(
                color: Color(0xFF1E3A5F),
                fontWeight: FontWeight.bold,
                fontSize: 12,
              ),
            ),
          ),
          const SizedBox(height: 24),
          Text(
            result.text,
            textAlign: TextAlign.center,
            style: const TextStyle(
              fontSize: 20,
              height: 1.6,
              fontWeight: FontWeight.w500,
              color: Color(0xFF1E3A5F),
            ),
          ),
          if (result.care.isNotEmpty) ...[
            const SizedBox(height: 24),
            const Divider(),
            const SizedBox(height: 24),
            Text(
              result.care,
              textAlign: TextAlign.center,
              style: const TextStyle(
                fontStyle: FontStyle.italic,
                color: Colors.grey,
              ),
            ),
          ],
        ],
      ),
    );
  }
}
