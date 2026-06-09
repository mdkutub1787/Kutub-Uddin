import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../view_models/category_view_model.dart';
import '../../models/category_model.dart';

class AdminCategoryListScreen extends StatelessWidget {
  const AdminCategoryListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final categoryViewModel = context.watch<CategoryViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: const Text("Category Management"),
        centerTitle: true,
      ),
      body: categoryViewModel.isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: categoryViewModel.categories.length,
              itemBuilder: (context, index) {
                final category = categoryViewModel.categories[index];
                return Card(
                  margin: const EdgeInsets.only(bottom: 12),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                  child: ListTile(
                    contentPadding: const EdgeInsets.all(12),
                    leading: Container(
                      padding: const EdgeInsets.all(10),
                      decoration: BoxDecoration(
                        color: category.color.withValues(alpha: 0.1),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Icon(category.icon, color: category.color, size: 30),
                    ),
                    title: Text(
                      category.name,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                    ),
                    subtitle: Text("ID: ${category.id}"),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(
                          icon: const Icon(Icons.edit_rounded, color: Colors.blue),
                          onPressed: () => _showCategoryDialog(context, category: category),
                        ),
                        IconButton(
                          icon: const Icon(Icons.delete_forever_rounded, color: Colors.red),
                          onPressed: () => _showDeleteConfirm(context, category, categoryViewModel),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _showCategoryDialog(context),
        label: const Text("Add Category"),
        icon: const Icon(Icons.add),
      ),
    );
  }

  void _showDeleteConfirm(BuildContext context, CategoryModel category, CategoryViewModel vm) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Delete Category?"),
        content: Text("Delete '${category.name}'? All products in this category might be affected."),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("CANCEL")),
          TextButton(
            onPressed: () {
              vm.deleteCategory(category.id);
              Navigator.pop(ctx);
            },
            child: const Text("DELETE", style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }

  void _showCategoryDialog(BuildContext context, {CategoryModel? category}) {
    final nameController = TextEditingController(text: category?.name ?? '');
    IconData selectedIcon = category?.icon ?? Icons.category;
    Color selectedColor = category?.color ?? Colors.blue;

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(25))),
      builder: (ctx) => Padding(
        padding: EdgeInsets.only(
          bottom: MediaQuery.of(ctx).viewInsets.bottom,
          left: 20, right: 20, top: 20,
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              category == null ? "Add New Category" : "Edit Category",
              style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: nameController,
              decoration: InputDecoration(
                labelText: "Category Name",
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
              ),
            ),
            const SizedBox(height: 20),
            const Text("Select Icon", style: TextStyle(fontWeight: FontWeight.bold)),
            const SizedBox(height: 10),
            SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: [
                  Icons.category, Icons.shopping_bag, Icons.laptop, Icons.face, Icons.home, Icons.fastfood,
                  Icons.electrical_services, Icons.watch, Icons.directions_car
                ].map((icon) {
                  return IconButton(
                    icon: Icon(icon, color: selectedIcon == icon ? Colors.blue : Colors.grey),
                    onPressed: () => selectedIcon = icon,
                  );
                }).toList(),
              ),
            ),
            const SizedBox(height: 30),
            SizedBox(
              width: double.infinity,
              height: 55,
              child: ElevatedButton(
                onPressed: () {
                  if (nameController.text.trim().isEmpty) return;
                  final catViewModel = context.read<CategoryViewModel>();
                  final newCat = CategoryModel(
                    id: category?.id ?? '',
                    name: nameController.text.trim(),
                    icon: selectedIcon,
                    color: selectedColor,
                  );

                  if (category == null) {
                    catViewModel.addCategory(newCat);
                  } else {
                    catViewModel.updateCategory(newCat);
                  }
                  Navigator.pop(ctx);
                },
                style: ElevatedButton.styleFrom(shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15))),
                child: const Text("SAVE CATEGORY"),
              ),
            ),
            const SizedBox(height: 20),
          ],
        ),
      ),
    );
  }
}
