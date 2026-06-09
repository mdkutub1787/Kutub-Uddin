import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../view_models/product_view_model.dart';
import '../../models/product_model.dart';
import 'admin_add_edit_product_screen.dart';
import 'package:smart_shop/utils/constants/app_colors.dart';

class AdminProductListScreen extends StatelessWidget {
  const AdminProductListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final productViewModel = context.watch<ProductViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: const Text("Product Management"),
        centerTitle: true,
      ),
      body: productViewModel.isLoading && productViewModel.featuredProducts.isEmpty
          ? const Center(child: CircularProgressIndicator())
          : Column(
              children: [
                _buildHeader(context, productViewModel.featuredProducts.length),
                Expanded(
                  child: ListView.builder(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                    itemCount: productViewModel.featuredProducts.length,
                    itemBuilder: (context, index) {
                      final product = productViewModel.featuredProducts[index];
                      return _buildProductCard(context, product, productViewModel);
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

  Widget _buildProductCard(BuildContext context, ProductModel product, ProductViewModel vm) {
    return Card(
      elevation: 0,
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(15),
        side: const BorderSide(color: AppColors.slate200),
      ),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(12),
              child: Image.network(
                product.imageUrl,
                width: 70,
                height: 70,
                fit: BoxFit.cover,
                errorBuilder: (_, __, ___) => Container(
                  width: 70, height: 70, color: AppColors.slate100,
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
                    "৳${product.price}",
                    style: TextStyle(
                      color: Theme.of(context).primaryColor,
                      fontWeight: FontWeight.w900,
                    ),
                  ),
                  const SizedBox(height: 5),
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                    decoration: BoxDecoration(
                      color: AppColors.slate100,
                      borderRadius: BorderRadius.circular(5),
                    ),
                    child: Text(
                      "Rating: ${product.rating}",
                      style: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold),
                    ),
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
                  onPressed: () => _showDeleteConfirm(context, product, vm),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }

  void _showDeleteConfirm(BuildContext context, ProductModel product, ProductViewModel vm) {
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
              vm.deleteProduct(product.id);
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
