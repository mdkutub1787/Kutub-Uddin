import 'package:flutter/material.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

import '../../../../core/data/learning_data.dart';

class QuranLearningPage extends StatelessWidget {
  const QuranLearningPage({super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Scaffold(
        backgroundColor: const Color(0xFFF8F9FA),
        appBar: AppBar(
          title: Text(
            context.tr('Quran Education Hub'),
            style: const TextStyle(fontWeight: FontWeight.bold),
          ),
          backgroundColor: const Color(0xFF1E3A5F),
          foregroundColor: Colors.white,
          centerTitle: true,
          bottom: TabBar(
            indicatorColor: Colors.white,
            labelColor: Colors.white,
            tabs: [
              Tab(text: context.tr('Lessons')),
              Tab(text: context.tr('Learning Books')),
            ],
          ),
        ),
        body: TabBarView(
          children: [
            _buildLessonsGrid(),
            _buildResourceList(quranLearningResources),
          ],
        ),
      ),
    );
  }

  Widget _buildLessonsGrid() {
    final lessons = [
      'Alphabet',
      'Joining',
      'Harakat',
      'Tanween',
      'Madd',
      'Tajweed',
    ];
    return GridView.builder(
      padding: const EdgeInsets.all(20),
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        crossAxisSpacing: 16,
        mainAxisSpacing: 16,
        childAspectRatio: 1.2,
      ),
      itemCount: lessons.length,
      itemBuilder: (context, index) {
        return Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16),
            boxShadow: [
              BoxShadow(color: Colors.black.withAlpha(5), blurRadius: 10),
            ],
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.menu_book, color: Colors.orange[700], size: 32),
              const SizedBox(height: 8),
              Text(
                lessons[index],
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
            ],
          ),
        );
      },
    );
  }

  Widget _buildResourceList(List<IslamicResource> resources) {
    return ListView.builder(
      padding: const EdgeInsets.all(20),
      itemCount: resources.length,
      itemBuilder: (context, index) {
        final res = resources[index];
        return Card(
          margin: const EdgeInsets.only(bottom: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
          ),
          child: ListTile(
            contentPadding: const EdgeInsets.all(16),
            title: Text(
              res.title,
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
            subtitle: Text(res.subtitle),
            trailing: const Icon(Icons.download, color: Colors.grey),
          ),
        );
      },
    );
  }
}
