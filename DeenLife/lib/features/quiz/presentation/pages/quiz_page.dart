import 'package:flutter/material.dart';

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

class QuizPage extends StatefulWidget {
  const QuizPage({super.key});

  @override
  State<QuizPage> createState() => _QuizPageState();
}

class _QuizPageState extends State<QuizPage> {
  int _currentIndex = 0;
  int _score = 0;
  bool _answered = false;
  int? _selectedIndex;

  final List<QuizQuestion> _questions = [
    QuizQuestion(
      question: 'Which Surah is considered the "Heart of the Quran"?',
      options: ['Surah Yaseen', 'Surah Al-Fatiha', 'Surah Ar-Rahman', 'Surah Al-Baqarah'],
      correctIndex: 0,
    ),
    QuizQuestion(
      question: 'How many times is the Prophet Muhammad (PBUH) mentioned by name in the Quran?',
      options: ['1', '4', '25', '99'],
      correctIndex: 1,
    ),
    QuizQuestion(
      question: 'In which month was the Quran first revealed?',
      options: ['Muharram', 'Rajab', 'Ramadan', 'Shawwal'],
      correctIndex: 2,
    ),
    QuizQuestion(
      question: 'Who was the first person to accept Islam?',
      options: ['Abu Bakr (RA)', 'Ali (RA)', 'Khadija (RA)', 'Zayd (RA)'],
      correctIndex: 2,
    ),
    QuizQuestion(
      question: 'Which Prophet built the Kaaba with his son?',
      options: ['Prophet Musa', 'Prophet Ibrahim', 'Prophet Nuh', 'Prophet Isa'],
      correctIndex: 1,
    ),
  ];

  void _submitAnswer(int index) {
    if (_answered) return;
    
    setState(() {
      _answered = true;
      _selectedIndex = index;
      if (index == _questions[_currentIndex].correctIndex) {
        _score++;
      }
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
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: const Text('Quiz Completed!', textAlign: TextAlign.center),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              'Your Score: $_score / ${_questions.length}',
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            Text(
              _score == _questions.length ? 'Perfect! MashaAllah!' : 'Good effort! Keep learning.',
              textAlign: TextAlign.center,
              style: const TextStyle(color: Colors.grey),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context); // Close dialog
              Navigator.pop(context); // Go back to home
            },
            child: const Text('Back to Home'),
          ),
          ElevatedButton(
            onPressed: () {
              Navigator.pop(context);
              setState(() {
                _currentIndex = 0;
                _score = 0;
                _answered = false;
                _selectedIndex = null;
              });
            },
            child: const Text('Retry'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final question = _questions[_currentIndex];

    return Scaffold(
      appBar: AppBar(
        title: const Text('Islamic Quiz'),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            LinearProgressIndicator(
              value: (_currentIndex + 1) / _questions.length,
              backgroundColor: Colors.grey[300],
              color: Theme.of(context).colorScheme.primary,
              minHeight: 8,
              borderRadius: BorderRadius.circular(4),
            ),
            const SizedBox(height: 24),
            Text(
              'Question ${_currentIndex + 1} of ${_questions.length}',
              style: const TextStyle(
                color: Colors.grey,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 12),
            Text(
              question.question,
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                height: 1.4,
              ),
            ),
            const SizedBox(height: 32),
            ...List.generate(question.options.length, (index) {
              Color buttonColor = Theme.of(context).cardColor;
              Color textColor = Theme.of(context).textTheme.bodyLarge?.color ?? Colors.black;
              
              if (_answered) {
                if (index == question.correctIndex) {
                  buttonColor = Colors.green;
                  textColor = Colors.white;
                } else if (index == _selectedIndex) {
                  buttonColor = Colors.red;
                  textColor = Colors.white;
                }
              }

              return Padding(
                padding: const EdgeInsets.only(bottom: 16.0),
                child: ElevatedButton(
                  onPressed: () => _submitAnswer(index),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: buttonColor,
                    foregroundColor: textColor,
                    padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 20),
                    alignment: Alignment.centerLeft,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(16),
                      side: BorderSide(
                        color: _answered ? Colors.transparent : Colors.grey.withOpacity(0.3),
                      ),
                    ),
                  ),
                  child: Text(
                    question.options[index],
                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
                  ),
                ),
              );
            }),
          ],
        ),
      ),
    );
  }
}
