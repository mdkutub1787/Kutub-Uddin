import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../riverpod/category_notifier.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../../routes/app_routes.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../core/riverpod/settings_notifier.dart';
import 'package:smart_shop/widgets/custom_loading.dart';

class AllCategoriesScreen extends ConsumerWidget {
  const AllCategoriesScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final categoryState = ref.watch(categoryNotifierProvider);
    final settings = ref.watch(settingsProvider);

    return Scaffold(
      appBar: const CustomAppBar(title: "All Categories"),
      body: categoryState.when(
        data: (categories) {
          if (categories.isEmpty) {
            return const Center(child: Text("No categories found"));
          }

          return GridView.builder(
            padding: const EdgeInsets.all(16),
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 3,
              childAspectRatio: 0.8,
              crossAxisSpacing: 16,
              mainAxisSpacing: 16,
            ),
            itemCount: categories.length,
            itemBuilder: (context, index) {
              final cat = categories[index];
              bool hasImage = cat.imageUrl.isNotEmpty && cat.imageUrl.startsWith('http');

              return GestureDetector(
                onTap: () {
                  ref.read(productNotifierProvider.notifier).filterByCategory(cat.id);
                  Navigator.pushNamed(
                    context, 
                    AppRoutes.allProducts, 
                    arguments: {'title': cat.name, 'categoryId': cat.id}
                  );
                },
                child: Column(
                  children: [
                    Expanded(
                      child: Container(
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(20),
                          image: hasImage 
                            ? DecorationImage(image: NetworkImage(cat.imageUrl), fit: BoxFit.cover)
                            : null,
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withValues(alpha: 0.05),
                              blurRadius: 10,
                              offset: const Offset(0, 5),
                            )
                          ],
                          border: Border.all(color: Colors.grey[100]!),
                        ),
                        child: !hasImage 
                          ? Icon(Icons.category, color: settings.primaryColor, size: 32)
                          : null,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      cat.name,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                      textAlign: TextAlign.center,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                ),
              );
            },
          );
        },
        loading: () => const Center(child: CustomLoading()),
        error: (e, st) => Center(child: Text("Error: $e")),
      ),
    );
  }
}
