import 'package:flutter/material.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

import '../../../../core/data/learning_data.dart';
import 'four_imams_page.dart';

class KnowledgeHubPage extends StatelessWidget {
  const KnowledgeHubPage({super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Scaffold(
        backgroundColor: const Color(0xFFF8F9FA),
        appBar: AppBar(
          title: Text(
            context.tr('Islamic Library'),
            style: const TextStyle(fontWeight: FontWeight.bold),
          ),
          backgroundColor: const Color(0xFF1E3A5F),
          foregroundColor: Colors.white,
          centerTitle: true,
          bottom: TabBar(
            indicatorColor: Colors.white,
            labelColor: Colors.white,
            tabs: [
              Tab(text: context.tr('Categories')),
              Tab(text: context.tr('All Books')),
            ],
          ),
        ),
        body: TabBarView(
          children: [_buildCategories(), _buildResourceList(islamicBooks)],
        ),
      ),
    );
  }

  Widget _buildCategories() {
    final categories = [
      {
        'title': 'The 4 Imams',
        'icon': Icons.people,
        'color': Colors.deepPurple,
        'page': const FourImamsPage(),
      },
      {
        'title': 'Tawheed',
        'icon': Icons.brightness_high,
        'color': Colors.blue,
        'page': null,
      },
      {
        'title': 'Seerah',
        'icon': Icons.history_edu,
        'color': Colors.orange,
        'page': null,
      },
      {
        'title': 'Fiqh',
        'icon': Icons.gavel,
        'color': Colors.green,
        'page': null,
      },
    ];
    return GridView.builder(
      padding: const EdgeInsets.all(20),
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        crossAxisSpacing: 16,
        mainAxisSpacing: 16,
        childAspectRatio: 1.1,
      ),
      itemCount: categories.length,
      itemBuilder: (context, index) {
        final cat = categories[index];
        return GestureDetector(
          onTap: () {
            if (cat['page'] != null) {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => cat['page'] as Widget),
              );
            }
          },
          child: Container(
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(20),
              boxShadow: [
                BoxShadow(color: Colors.black.withAlpha(5), blurRadius: 10),
              ],
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  cat['icon'] as IconData,
                  color: cat['color'] as Color,
                  size: 40,
                ),
                const SizedBox(height: 12),
                Text(
                  cat['title'] as String,
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
              ],
            ),
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
        return Container(
          margin: const EdgeInsets.only(bottom: 16),
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16),
            border: Border.all(color: Colors.grey.withAlpha(30)),
          ),
          child: Row(
            children: [
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: const Color(0xFF1E3A5F).withAlpha(20),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: const Icon(Icons.book, color: Color(0xFF1E3A5F)),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      res.title,
                      style: const TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 16,
                      ),
                    ),
                    Text(
                      res.category,
                      style: TextStyle(
                        color: Colors.blue[700],
                        fontSize: 12,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ),
              const Icon(Icons.arrow_forward_ios, size: 14, color: Colors.grey),
            ],
          ),
        );
      },
    );
  }
}
