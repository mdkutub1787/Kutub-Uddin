import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../category/riverpod/category_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../category/models/category_model.dart';
import '../../../widgets/custom_app_bar.dart';

class AdminCategoryListScreen extends ConsumerStatefulWidget {
  const AdminCategoryListScreen({super.key});

  @override
  ConsumerState<AdminCategoryListScreen> createState() => _AdminCategoryListScreenState();
}

class _AdminCategoryListScreenState extends ConsumerState<AdminCategoryListScreen> {
  @override
  Widget build(BuildContext context) {
    final categoryState = ref.watch(categoryNotifierProvider);
    final categories = categoryState.value ?? [];
    final isLoading = categoryState.isLoading;

    return Scaffold(
      appBar: const CustomAppBar(title: "Category Manager"),
      body: isLoading && categories.isEmpty
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: categories.length,
              itemBuilder: (context, index) {
                final category = categories[index];
                return Card(
                  margin: const EdgeInsets.only(bottom: 12),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                  child: ListTile(
                    leading: Container(
                      width: 50,
                      height: 50,
                      decoration: BoxDecoration(
                        color: Colors.grey[200],
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: category.imageUrl.isNotEmpty 
                          ? ClipRRect(
                              borderRadius: BorderRadius.circular(10),
                              child: Image.network(category.imageUrl, fit: BoxFit.cover, errorBuilder: (_,__,___) => const Icon(Icons.image_not_supported)),
                            )
                          : const Icon(Icons.category, color: Colors.grey),
                    ),
                    title: Text(category.name, style: const TextStyle(fontWeight: FontWeight.bold)),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(icon: const Icon(Icons.edit_rounded, color: Colors.blue), onPressed: () => _showCategoryDialog(context, category: category)),
                        IconButton(icon: const Icon(Icons.delete_outline_rounded, color: Colors.red), onPressed: () => _confirmDelete(context, category.id)),
                      ],
                    ),
                  ),
                );
              },
            ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _showCategoryDialog(context),
        label: const Text("Add New"),
        icon: const Icon(Icons.add),
      ),
    );
  }

  void _confirmDelete(BuildContext context, String id) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Delete Category?"),
        content: const Text("This action cannot be undone."),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("CANCEL")),
          TextButton(onPressed: () { 
            ref.read(categoryNotifierProvider.notifier).deleteCategory(id); 
            Navigator.pop(ctx); 
          }, child: const Text("DELETE", style: TextStyle(color: Colors.red))),
        ],
      ),
    );
  }

  void _showCategoryDialog(BuildContext context, {CategoryModel? category}) {
    final nameController = TextEditingController(text: category?.name ?? '');
    final imageController = TextEditingController(text: category?.imageUrl ?? '');

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(25))),
      builder: (ctx) => StatefulBuilder(
        builder: (context, setModalState) => Container(
          padding: EdgeInsets.only(bottom: MediaQuery.of(ctx).viewInsets.bottom, left: 20, right: 20, top: 20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(category == null ? "Add New Category" : "Update Category", style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
              const SizedBox(height: 20),
              TextField(controller: nameController, decoration: const InputDecoration(labelText: "Name", border: OutlineInputBorder())),
              const SizedBox(height: 20),
              TextField(controller: imageController, decoration: const InputDecoration(labelText: "Image URL", border: OutlineInputBorder())),
              const SizedBox(height: 30),
              SizedBox(
                width: double.infinity,
                height: 55,
                child: ElevatedButton(
                  onPressed: () async {
                    if (nameController.text.isEmpty) return;
                    final authUser = ref.read(authNotifierProvider).value;
                    final shopId = authUser?.shopId ?? '';
                    
                    final newCat = CategoryModel(
                      id: category?.id ?? '',
                      shopId: shopId,
                      name: nameController.text.trim(),
                      imageUrl: imageController.text.trim(),
                    );
                    
                    if (category == null) {
                      await ref.read(categoryNotifierProvider.notifier).addCategory(newCat);
                    } else {
                      await ref.read(categoryNotifierProvider.notifier).updateCategory(newCat);
                    }
                    
                    if (mounted) Navigator.pop(ctx);
                  },
                  child: const Text("SAVE CATEGORY"),
                ),
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
}
