import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

import '../../../asmaul_husna/presentation/screens/asmaul_husna_screen.dart';
import '../../../tasbeeh/presentation/screens/tasbeeh_screen.dart';
import '../../../zakat/presentation/screens/zakat_calculator_screen.dart';
import '../../../kalima/presentation/screens/kalima_screen.dart';
import '../../../hadith/presentation/screens/hadith_screen.dart';
import '../../../quiz/presentation/screens/quiz_screen.dart';
import '../../../emotions/presentation/screens/emotions_screen.dart';
import '../../../radio/presentation/screens/radio_screen.dart';
import '../../../quran/presentation/screens/tafsir_screen.dart';
import '../../../duas/presentation/screens/dua_screen.dart';
import '../../../qibla/presentation/screens/qibla_screen.dart';
import '../../../tasbeeh/presentation/screens/tasbeeh_screen.dart';
import '../../../learning/presentation/screens/namaz_shikkha_screen.dart';
import '../../../learning/presentation/screens/knowledge_hub_screen.dart';
import '../../../calendar/presentation/screens/calendar_screen.dart';
import '../../../learning/presentation/screens/knowledge_hub_screen.dart';
import '../../../calendar/presentation/screens/calendar_screen.dart';

class ExploreScreen extends ConsumerWidget {
  const ExploreScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: Text(context.tr('Explore Tools')),
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 16),
            _buildSectionTitle(context, 'Essentials'),
            _buildFeaturesGrid(context, _getEssentialsFeatures()),

            _buildSectionTitle(context, 'Learning & Utilities'),
            _buildFeaturesGrid(context, _getLearningFeatures()),
          ],
        ),
      ),
    );
  }

  Widget _buildSectionTitle(BuildContext context, String title) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 8.0),
      child: Text(
        context.tr(title),
        style: const TextStyle(
          fontSize: 18,
          fontWeight: FontWeight.bold,
          color: Color(0xFF1E3A5F),
        ),
      ),
    );
  }

  Widget _buildFeaturesGrid(
    BuildContext context,
    List<Map<String, dynamic>> features,
  ) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 8.0),
      child: Wrap(
        spacing: 20,
        runSpacing: 20,
        alignment: WrapAlignment.start,
        children: features.map((f) => _featureItem(context, f)).toList(),
      ),
    );
  }

  Widget _featureItem(BuildContext context, Map<String, dynamic> f) {
    return GestureDetector(
      onTap: () => Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => f['page'] as Widget),
      ),
      child: SizedBox(
        width:
            (MediaQuery.of(context).size.width - 80) /
            3, // slightly adjusted width for 3 items
        child: Column(
          children: [
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: (f['color'] as Color).withAlpha(30),
                shape: BoxShape.circle,
              ),
              child: Icon(
                f['icon'] as IconData,
                color: f['color'] as Color,
                size: 28,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              context.tr(f['label'] as String),
              style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w600),
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }

  List<Map<String, dynamic>> _getEssentialsFeatures() {
    return [
      {
        'icon': Icons.favorite,
        'label': 'Duas',
        'page': DuaScreen(),
        'color': Colors.red,
      },
      {
        'icon': Icons.touch_app,
        'label': 'Tasbeeh',
        'page': const TasbeehScreen(),
        'color': Colors.indigo,
      },
      {
        'icon': Icons.explore,
        'label': 'Qibla',
        'page': const QiblaScreen(),
        'color': Colors.deepOrange,
      },
      {
        'icon': Icons.library_books,
        'label': 'Hadith',
        'page': const HadithScreen(),
        'color': Colors.brown,
      },
      {
        'icon': Icons.menu_book,
        'label': '6 Kalimas',
        'page': KalimaScreen(),
        'color': Colors.orange,
      },
      {
        'icon': Icons.calendar_month,
        'label': 'Calendar',
        'page': const CalendarScreen(),
        'color': Colors.purple,
      },
      {
        'icon': Icons.book,
        'label': 'Tafsir',
        'page': const TafsirScreen(),
        'color': Colors.green,
      },
    ];
  }

  List<Map<String, dynamic>> _getLearningFeatures() {
    return [
      {
        'icon': Icons.menu_book,
        'label': 'Namaz',
        'page': const NamazShikkhaScreen(),
        'color': Colors.deepPurple,
      },
      {
        'icon': Icons.library_books,
        'label': 'Library',
        'page': const KnowledgeHubScreen(),
        'color': Colors.amber,
      },
      {
        'icon': Icons.star,
        'label': 'Asmaul Husna',
        'page': const AsmaulHusnaScreen(),
        'color': Colors.blue,
      },
      {
        'icon': Icons.calculate,
        'label': 'Zakat Calc',
        'page': const ZakatCalculatorScreen(),
        'color': Colors.teal,
      },
      {
        'icon': Icons.quiz,
        'label': 'Islamic Quiz',
        'page': const QuizScreen(),
        'color': Colors.pink,
      },
      {
        'icon': Icons.mood,
        'label': 'Emotions',
        'page': const EmotionsScreen(),
        'color': Colors.cyan,
      },
      {
        'icon': Icons.radio,
        'label': 'Radio',
        'page': const RadioScreen(),
        'color': Colors.purple,
      },
    ];
  }
}

