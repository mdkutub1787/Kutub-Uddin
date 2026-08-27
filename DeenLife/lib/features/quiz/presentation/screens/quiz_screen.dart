import 'package:flutter/material.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

class QuizQuestion {
  final String question;
  final List<String> options;
  final int correctIndex;
  QuizQuestion({
    required this.question,
    required this.options,
    required this.correctIndex,
  });
}

class QuizScreen extends StatefulWidget {
  const QuizScreen({super.key});
  @override
  State<QuizScreen> createState() => _QuizScreenState();
}

class _QuizScreenState extends State<QuizScreen> {
  int _currentIndex = 0;
  int _score = 0;
  bool _answered = false;
  int? _selectedIndex;

  final List<QuizQuestion> _questions = [
    QuizQuestion(
      question: 'Which Surah is considered the "Heart of the Quran"?',
      options: [
        'Surah Yaseen',
        'Surah Al-Fatiha',
        'Surah Ar-Rahman',
        'Surah Al-Baqarah',
      ],
      correctIndex: 0,
    ),
    QuizQuestion(
      question: 'How many times is the Prophet Muhammad (PBUH) mentioned by name in the Quran?',
      options: ['1', '4', '25', '99'],
      correctIndex: 1,
    ),
  ];

  void _submitAnswer(int index) {
    if (_answered) return;
    setState(() {
      _answered = true;
      _selectedIndex = index;
      if (index == _questions[_currentIndex].correctIndex) _score++;
    });
    Future.delayed(const Duration(seconds: 2), () {
      if (_currentIndex < _questions.length - 1) {
        setState(() {
          _currentIndex++;
          _answered = false;
          _selectedIndex = null;
        });
      } else {
        _showResults();
      }
    });
  }

  void _showResults() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
        title: const Text(
          'Quiz Completed!',
          textAlign: TextAlign.center,
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              'Your Score: $_score / ${_questions.length}',
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                color: Color(0xFF1E3A5F),
              ),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              Navigator.pop(context);
            },
            child: const Text('Back to Home'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final question = _questions[_currentIndex];
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: Text(
          context.tr('Islamic Quiz'),
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          children: [
            LinearProgressIndicator(
              value: (_currentIndex + 1) / _questions.length,
              backgroundColor: Colors.white,
              color: const Color(0xFF1E3A5F),
              minHeight: 10,
              borderRadius: BorderRadius.circular(5),
            ),
            const SizedBox(height: 40),
            Container(
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
                  Text(
                    'Question ${_currentIndex + 1}',
                    style: const TextStyle(
                      color: Colors.grey,
                      fontWeight: FontWeight.bold,
                      fontSize: 14,
                    ),
                  ),
                  const SizedBox(height: 16),
                  Text(
                    question.question,
                    textAlign: TextAlign.center,
                    style: const TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1E3A5F),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 32),
            Expanded(
              child: ListView.builder(
                itemCount: question.options.length,
                itemBuilder: (context, index) {
                  Color color = Colors.white;
                  if (_answered) {
                    if (index == question.correctIndex)
                      color = Colors.green[100]!;
                    else if (index == _selectedIndex)
                      color = Colors.red[100]!;
                  }
                  return Container(
                    margin: const EdgeInsets.only(bottom: 12),
                    child: ElevatedButton(
                      onPressed: () => _submitAnswer(index),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: color,
                        foregroundColor: const Color(0xFF1E3A5F),
                        elevation: 0,
                        padding: const EdgeInsets.all(20),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(16),
                          side: BorderSide(
                            color: const Color(0xFF1E3A5F).withAlpha(30),
                          ),
                        ),
                      ),
                      child: Text(
                        question.options[index],
                        style: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 16,
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}

