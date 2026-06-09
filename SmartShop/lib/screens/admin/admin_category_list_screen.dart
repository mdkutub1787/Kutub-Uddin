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
        title: const Text("Manage Categories"),
        actions: [
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () => _showCategoryDialog(context),
          ),
        ],
      ),
      body: ListView.builder(
        itemCount: categoryViewModel.categories.length,
        itemBuilder: (context, index) {
          final category = categoryViewModel.categories[index];
          return ListTile(
            leading: CircleAvatar(
              backgroundColor: category.color.withOpacity(0.1),
              child: Icon(category.icon, color: category.color),
            ),
            title: Text(category.name),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                IconButton(
                  icon: const Icon(Icons.edit, color: Colors.blue),
                  onPressed: () => _showCategoryDialog(context, category: category),
                ),
                IconButton(
                  icon: const Icon(Icons.delete, color: Colors.red),
                  onPressed: () {
                    categoryViewModel.deleteCategory(category.id);
                  },
                ),
              ],
            ),
          );
        },
      ),
    );
  }

  void _showCategoryDialog(BuildContext context, {CategoryModel? category}) {
    final nameController = TextEditingController(text: category?.name ?? '');
    
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text(category == null ? "Add Category" : "Edit Category"),
        content: TextField(
          controller: nameController,
          decoration: const InputDecoration(labelText: "Category Name"),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("Cancel")),
          TextButton(
            onPressed: () {
              final catViewModel = context.read<CategoryViewModel>();
              if (category == null) {
                catViewModel.addCategory(CategoryModel(
                  id: '',
                  name: nameController.text,
                  icon: Icons.category,
                  color: Colors.blue,
                ));
              } else {
                catViewModel.updateCategory(CategoryModel(
                  id: category.id,
                  name: nameController.text,
                  icon: category.icon,
                  color: category.color,
                ));
              }
              Navigator.pop(ctx);
            },
            child: const Text("Save"),
          ),
        ],
      ),
    );
  }
}
