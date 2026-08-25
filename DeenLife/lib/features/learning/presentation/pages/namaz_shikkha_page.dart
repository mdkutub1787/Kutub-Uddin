import 'package:flutter/material.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

import '../../../../core/data/learning_data.dart';

class NamazShikkhaPage extends StatelessWidget {
  const NamazShikkhaPage({super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Scaffold(
        backgroundColor: const Color(0xFFF8F9FA),
        appBar: AppBar(
          title: Text(
            context.tr('Namaz Resource Hub'),
            style: const TextStyle(fontWeight: FontWeight.bold),
          ),
          backgroundColor: const Color(0xFF1E3A5F),
          foregroundColor: Colors.white,
          centerTitle: true,
          bottom: TabBar(
            indicatorColor: Colors.white,
            labelColor: Colors.white,
            tabs: [
              Tab(text: context.tr('Basic Steps')),
              Tab(text: context.tr('All Prayer Books')),
            ],
          ),
        ),
        body: TabBarView(
          children: [_buildStepsList(), _buildResourceList(namazResources)],
        ),
      ),
    );
  }

  Widget _buildStepsList() {
    final steps = [
      {'title': 'Niyyat', 'desc': 'Intention for prayer'},
      {'title': 'Takbir', 'desc': 'Saying Allahu Akbar'},
      {'title': 'Qiyam', 'desc': 'Standing position'},
      {'title': 'Ruku', 'desc': 'Bowing down'},
      {'title': 'Sujud', 'desc': 'Prostration'},
    ];

    return ListView.builder(
      padding: const EdgeInsets.all(20),
      itemCount: steps.length,
      itemBuilder: (context, index) {
        return Card(
          margin: const EdgeInsets.only(bottom: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
          ),
          child: ListTile(
            leading: CircleAvatar(
              backgroundColor: const Color(0xFF1E3A5F),
              child: Text(
                '${index + 1}',
                style: const TextStyle(color: Colors.white),
              ),
            ),
            title: Text(
              steps[index]['title']!,
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
            subtitle: Text(steps[index]['desc']!),
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
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16),
            boxShadow: [
              BoxShadow(color: Colors.black.withAlpha(5), blurRadius: 10),
            ],
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                res.title,
                style: const TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF1E3A5F),
                ),
              ),
              const SizedBox(height: 4),
              Text(
                res.subtitle,
                style: const TextStyle(color: Colors.grey, fontSize: 14),
              ),
              const Divider(height: 24),
              Text(
                res.content,
                style: const TextStyle(fontSize: 15, height: 1.5),
              ),
            ],
          ),
        );
      },
    );
  }
}
