import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:deen_life/core/localization/app_localizations.dart';
import '../../../../core/data/learning_providers.dart';
import 'guide_detail_screen.dart';

class NamazShikkhaScreen extends ConsumerWidget {
  const NamazShikkhaScreen({super.key});

  IconData _getIconData(String iconName) {
    switch (iconName) {
      case 'water_drop':
        return Icons.water_drop;
      case 'campaign':
        return Icons.campaign;
      case 'mosque':
        return Icons.mosque;
      case 'star':
        return Icons.star;
      case 'menu_book':
        return Icons.menu_book;
      default:
        return Icons.article;
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final asyncData = ref.watch(namazShikkhaProvider);

    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: Text(
          context.tr('Namaz Shikkha'),
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
        centerTitle: true,
      ),
      body: asyncData.when(
        data: (categories) {
          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: categories.length,
            itemBuilder: (context, index) {
              final category = categories[index];
              return Card(
                margin: const EdgeInsets.only(bottom: 16),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16),
                ),
                elevation: 2,
                child: ExpansionTile(
                  leading: CircleAvatar(
                    backgroundColor: const Color(0xFF1E3A5F).withAlpha(30),
                    child: Icon(
                      _getIconData(category.icon),
                      color: const Color(0xFF1E3A5F),
                    ),
                  ),
                  title: Text(
                    category.title,
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                  ),
                  children: category.topics.map((topic) {
                    return ListTile(
                      contentPadding: const EdgeInsets.symmetric(horizontal: 32, vertical: 4),
                      title: Text(topic.title, style: const TextStyle(fontSize: 15)),
                      trailing: const Icon(Icons.arrow_forward_ios, size: 14, color: Colors.grey),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => GuideDetailScreen(topic: topic),
                          ),
                        );
                      },
                    );
                  }).toList(),
                ),
              );
            },
          );
        },
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (error, stack) => Center(
          child: Text('Error loading data: $error'),
        ),
      ),
    );
  }
}

