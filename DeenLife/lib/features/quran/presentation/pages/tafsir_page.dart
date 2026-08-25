import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:deen_life/core/localization/app_localizations.dart';
import '../providers/surah_tafsir_provider.dart';
import 'tafsir_surah_list_page.dart';

class TafsirPage extends ConsumerWidget {
  const TafsirPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tafsirsAsync = ref.watch(onlineTafsirsProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text(context.tr('Tafsir Collection')),
        centerTitle: true,
      ),
      body: tafsirsAsync.when(
        data: (tafsirs) {
          return GridView.builder(
            padding: const EdgeInsets.all(16.0),
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2,
              crossAxisSpacing: 16,
              mainAxisSpacing: 16,
              childAspectRatio: 0.75,
            ),
            itemCount: tafsirs.length,
            itemBuilder: (context, index) {
              final tafsir = tafsirs[index];
              final id = tafsir['id'] as int;
              final name = tafsir['name'] ?? '';
              final authorName = tafsir['author_name'] ?? '';
              final languageName = tafsir['language_name'] ?? '';
              
              return GestureDetector(
                onTap: () {
                  ref.read(selectedTafsirIdProvider.notifier).state = id;
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => TafsirSurahListPage(
                        tafsirId: id,
                        tafsirName: name,
                      ),
                    ),
                  );
                },
                child: Container(
                  padding: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      color: const Color(0xFF137085), // Teal-ish color from screenshot
                      borderRadius: BorderRadius.circular(20),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.1),
                          blurRadius: 10,
                          offset: const Offset(0, 4),
                        ),
                      ],
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Icon(Icons.menu_book, color: Colors.white, size: 40),
                        const SizedBox(height: 12),
                        Text(
                          name,
                          style: const TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 16,
                          ),
                          textAlign: TextAlign.center,
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 8),
                        Text(
                          authorName,
                          style: TextStyle(
                            color: Colors.white.withOpacity(0.7),
                            fontSize: 11,
                          ),
                          textAlign: TextAlign.center,
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                        ),
                        Text(
                          '(${languageName.toUpperCase()})',
                          style: TextStyle(
                            color: Colors.white.withOpacity(0.5),
                            fontSize: 10,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
              );
            },
          );
        },
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (error, stack) => Center(
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              'Error loading tafsirs.\nPlease check your connection.',
              textAlign: TextAlign.center,
              style: const TextStyle(color: Colors.red),
            ),
          ),
        ),
      ),
    );
  }
}
