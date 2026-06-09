import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../view_models/product_view_model.dart';
import '../../models/product_model.dart';
import 'admin_add_edit_product_screen.dart';

class AdminProductListScreen extends StatelessWidget {
  const AdminProductListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final productViewModel = context.watch<ProductViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: const Text("Manage Products"),
        actions: [
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () => Navigator.push(
              context,
              MaterialPageRoute(builder: (_) => const AdminAddEditProductScreen()),
            ),
          ),
        ],
      ),
      body: ListView.builder(
        itemCount: productViewModel.featuredProducts.length,
        itemBuilder: (context, index) {
          final product = productViewModel.featuredProducts[index];
          return ListTile(
            leading: Image.network(product.imageUrl, width: 50, height: 50, fit: BoxFit.cover),
            title: Text(product.name),
            subtitle: Text("৳${product.price}"),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                IconButton(
                  icon: const Icon(Icons.edit, color: Colors.blue),
                  onPressed: () => Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => AdminAddEditProductScreen(product: product)),
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.delete, color: Colors.red),
                  onPressed: () {
                    showDialog(
                      context: context,
                      builder: (ctx) => AlertDialog(
                        title: const Text("Delete Product"),
                        content: const Text("Are you sure you want to delete this product?"),
                        actions: [
                          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("Cancel")),
                          TextButton(
                            onPressed: () {
                              productViewModel.deleteProduct(product.id);
                              Navigator.pop(ctx);
                            },
                            child: const Text("Delete", style: TextStyle(color: Colors.red)),
                          ),
                        ],
                      ),
                    );
                  },
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}
