import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../riverpod/product_notifier.dart';
import '../../../widgets/product_card.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../core/riverpod/settings_notifier.dart';

class AllProductsScreen extends ConsumerWidget {
  final String title;
  final String? categoryId;

  const AllProductsScreen({
    super.key, 
    required this.title, 
    this.categoryId,
  });

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final productState = ref.watch(productNotifierProvider);
    final settings = ref.watch(settingsProvider);
    
    final products = categoryId != null 
        ? productState.allProducts.where((p) => p.categoryId == categoryId).toList()
        : productState.allProducts;

    return Scaffold(
      appBar: CustomAppBar(title: title),
      body: products.isEmpty
          ? const Center(child: Text("No products found"))
          : GridView.builder(
              padding: const EdgeInsets.all(16),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                childAspectRatio: 0.68,
                crossAxisSpacing: 16,
                mainAxisSpacing: 16,
              ),
              itemCount: products.length,
              itemBuilder: (context, index) => ProductCard(
                product: products[index],
                heroTag: 'all-${products[index].id}',
              ),
            ),
    );
  }
}
