import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../product/models/product_model.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/app_card.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'admin_add_edit_product_screen.dart';
import 'package:smart_shop/utils/constants/app_colors.dart';

class AdminProductListScreen extends ConsumerWidget {
  const AdminProductListScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final productState = ref.watch(productNotifierProvider);
    final products = productState.featuredProducts ?? [];
    final isLoading = productState.isLoading;

    return Scaffold(
      appBar: const CustomAppBar(
        title: "Product Management",
      ),
      body: isLoading && products.isEmpty
          ? const Center(child: CircularProgressIndicator())
          : Column(
              children: [
                _buildHeader(context, products.length),
                Expanded(
                  child: ListView.builder(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                    itemCount: products.length,
                    itemBuilder: (context, index) {
                      final product = products[index];
                      return _buildProductCard(context, ref, product);
                    },
                  ),
                ),
              ],
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const AdminAddEditProductScreen()),
        ),
        child: const Icon(Icons.add),
      ),
    );
  }

  Widget _buildHeader(BuildContext context, int count) {
    return Container(
      padding: const EdgeInsets.all(20),
      width: double.infinity,
      color: Theme.of(context).primaryColor.withValues(alpha: 0.1),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text("Inventory", style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
              Text("Total $count items in shop", style: const TextStyle(color: AppColors.slate500)),
            ],
          ),
          const Icon(Icons.inventory_2_outlined, size: 40, color: AppColors.slate400),
        ],
      ),
    );
  }

  Widget _buildProductCard(BuildContext context, WidgetRef ref, ProductModel product) {
    return AppCard(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(12),
              child: Image.network(
                product.imageUrl,
                width: 75,
                height: 75,
                fit: BoxFit.cover,
                errorBuilder: (_, __, ___) => Container(
                  width: 75, height: 75, color: AppColors.slate100,
                  child: const Icon(Icons.image_not_supported),
                ),
              ),
            ),
            const SizedBox(width: 15),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    product.name,
                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 5),
                  Text(
                    "৳${product.price.toInt()}",
                    style: TextStyle(
                      color: Theme.of(context).primaryColor,
                      fontWeight: FontWeight.w900,
                      fontSize: 16,
                    ),
                  ),
                  const SizedBox(height: 5),
                  Row(
                    children: [
                      Icon(Icons.inventory_2_outlined, size: 12, color: product.stock > 0 ? Colors.green : Colors.red),
                      const SizedBox(width: 4),
                      Text(
                        "${AppStrings.stock.tr()}: ${product.stock}",
                        style: TextStyle(
                          fontSize: 12, 
                          color: product.stock > 0 ? Colors.green : Colors.red,
                          fontWeight: FontWeight.bold
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            Column(
              children: [
                IconButton(
                  icon: const Icon(Icons.edit_note_rounded, color: AppColors.info),
                  onPressed: () => Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => AdminAddEditProductScreen(product: product)),
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.delete_sweep_rounded, color: AppColors.error),
                  onPressed: () => _showDeleteConfirm(context, ref, product),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }

  void _showDeleteConfirm(BuildContext context, WidgetRef ref, ProductModel product) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Delete Product?"),
        content: Text("Are you sure you want to delete '${product.name}'? This action cannot be undone."),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("CANCEL")),
          ElevatedButton(
            onPressed: () {
              ref.read(productNotifierProvider.notifier).deleteProduct(product.id);
              Navigator.pop(ctx);
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text("Product deleted")),
              );
            },
            style: ElevatedButton.styleFrom(backgroundColor: AppColors.error),
            child: const Text("DELETE", style: TextStyle(color: Colors.white)),
          ),
        ],
      ),
    );
  }
}
