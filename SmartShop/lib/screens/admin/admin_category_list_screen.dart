import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../view_models/category_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../models/category_model.dart';
import '../../widgets/custom_app_bar.dart';

class AdminCategoryListScreen extends StatefulWidget {
  const AdminCategoryListScreen({super.key});

  @override
  State<AdminCategoryListScreen> createState() => _AdminCategoryListScreenState();
}

class _AdminCategoryListScreenState extends State<AdminCategoryListScreen> {
  @override
  Widget build(BuildContext context) {
    final categoryViewModel = context.watch<CategoryViewModel>();

    return Scaffold(
      appBar: const CustomAppBar(title: "Category Manager"),
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
                    leading: Container(
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        color: category.color.withValues(alpha: 0.1),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: Icon(category.icon, color: category.color),
                    ),
                    title: Text(category.name, style: const TextStyle(fontWeight: FontWeight.bold)),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(icon: const Icon(Icons.edit_rounded, color: Colors.blue), onPressed: () => _showCategoryDialog(context, category: category)),
                        IconButton(icon: const Icon(Icons.delete_outline_rounded, color: Colors.red), onPressed: () => _confirmDelete(context, category.id, categoryViewModel)),
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

  void _confirmDelete(BuildContext context, String id, CategoryViewModel vm) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Delete Category?"),
        content: const Text("This action cannot be undone."),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("CANCEL")),
          TextButton(onPressed: () { vm.deleteCategory(id); Navigator.pop(ctx); }, child: const Text("DELETE", style: TextStyle(color: Colors.red))),
        ],
      ),
    );
  }

  void _showCategoryDialog(BuildContext context, {CategoryModel? category}) {
    final nameController = TextEditingController(text: category?.name ?? '');
    IconData selectedIcon = category?.icon ?? Icons.category;
    
    final List<IconData> icons = [
      Icons.category, Icons.shopping_bag, Icons.laptop, Icons.face, Icons.home, 
      Icons.fastfood, Icons.electrical_services, Icons.watch, Icons.directions_car, 
      Icons.medical_services, Icons.checkroom, Icons.smartphone, Icons.tv, 
      Icons.restaurant, Icons.local_pizza, Icons.icecream, Icons.chair, 
      Icons.bed, Icons.kitchen, Icons.health_and_safety, Icons.medication, 
      Icons.sports_basketball, Icons.fitness_center, Icons.brush, Icons.toys, 
      Icons.local_grocery_store, Icons.build, Icons.child_care, Icons.book, 
      Icons.pets, Icons.dry_cleaning, Icons.style
    ];

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
              const Align(alignment: Alignment.centerLeft, child: Text("Choose Icon:", style: TextStyle(fontWeight: FontWeight.bold))),
              const SizedBox(height: 10),
              SizedBox(
                height: 150,
                child: GridView.builder(
                  gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 6, mainAxisSpacing: 10, crossAxisSpacing: 10),
                  itemCount: icons.length,
                  itemBuilder: (context, index) => GestureDetector(
                    onTap: () => setModalState(() => selectedIcon = icons[index]),
                    child: Container(
                      decoration: BoxDecoration(
                        color: selectedIcon == icons[index] ? Theme.of(context).primaryColor : Colors.grey[100],
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: Icon(icons[index], color: selectedIcon == icons[index] ? Colors.white : Colors.grey[600]),
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 30),
              SizedBox(
                width: double.infinity,
                height: 55,
                child: ElevatedButton(
                  onPressed: () async {
                    if (nameController.text.isEmpty) return;
                    final authVM = context.read<AuthViewModel>();
                    final shopId = authVM.user?.shopId ?? '';
                    final vm = context.read<CategoryViewModel>();
                    final newCat = CategoryModel(
                      id: category?.id ?? '',
                      shopId: shopId,
                      name: nameController.text.trim(),
                      icon: selectedIcon,
                      color: category?.color ?? Colors.blue,
                    );
                    if (category == null) await vm.addCategory(newCat);
                    else await vm.updateCategory(newCat);
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
