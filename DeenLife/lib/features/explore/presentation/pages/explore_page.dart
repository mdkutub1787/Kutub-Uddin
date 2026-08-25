import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

import '../../../asmaul_husna/presentation/pages/asmaul_husna_page.dart';
import '../../../zakat/presentation/pages/zakat_calculator_page.dart';
import '../../../kalima/presentation/pages/kalima_page.dart';
import '../../../hadith/presentation/pages/hadith_page.dart';
import '../../../quiz/presentation/pages/quiz_page.dart';
import '../../../emotions/presentation/pages/emotions_page.dart';
import '../../../radio/presentation/pages/radio_page.dart';
import '../../../quran/presentation/pages/tafsir_page.dart';
import '../../../duas/presentation/pages/dua_page.dart';
import '../../../qibla/presentation/pages/qibla_page.dart';
import '../../../tasbeeh/presentation/pages/tasbeeh_page.dart';

class ExplorePage extends ConsumerWidget {
  const ExplorePage({super.key});

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
        'page': DuaPage(),
        'color': Colors.red,
      },
      {
        'icon': Icons.touch_app,
        'label': 'Tasbeeh',
        'page': const TasbeehPage(),
        'color': Colors.indigo,
      },
      {
        'icon': Icons.explore,
        'label': 'Qibla',
        'page': const QiblaPage(),
        'color': Colors.deepOrange,
      },
      {
        'icon': Icons.library_books,
        'label': 'Hadith',
        'page': const HadithPage(),
        'color': Colors.brown,
      },
      {
        'icon': Icons.menu_book,
        'label': '6 Kalimas',
        'page': KalimaPage(),
        'color': Colors.orange,
      },
      {
        'icon': Icons.book,
        'label': 'Tafsir',
        'page': const TafsirPage(),
        'color': Colors.green,
      },
    ];
  }

  List<Map<String, dynamic>> _getLearningFeatures() {
    return [
      {
        'icon': Icons.star,
        'label': 'Asmaul Husna',
        'page': const AsmaulHusnaPage(),
        'color': Colors.blue,
      },
      {
        'icon': Icons.calculate,
        'label': 'Zakat Calc',
        'page': const ZakatCalculatorPage(),
        'color': Colors.teal,
      },
      {
        'icon': Icons.quiz,
        'label': 'Islamic Quiz',
        'page': const QuizPage(),
        'color': Colors.pink,
      },
      {
        'icon': Icons.mood,
        'label': 'Emotions',
        'page': const EmotionsPage(),
        'color': Colors.cyan,
      },
      {
        'icon': Icons.radio,
        'label': 'Radio',
        'page': const RadioPage(),
        'color': Colors.purple,
      },
    ];
  }
}
