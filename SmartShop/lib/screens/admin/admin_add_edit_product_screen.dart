import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../models/product_model.dart';
import '../../utils/constants/app_colors.dart';
import '../../view_models/product_view_model.dart';
import '../../view_models/category_view_model.dart';
import '../../utils/constants/app_strings.dart';

class AdminAddEditProductScreen extends StatefulWidget {
  final ProductModel? product;
  const AdminAddEditProductScreen({super.key, this.product});

  @override
  State<AdminAddEditProductScreen> createState() => _AdminAddEditProductScreenState();
}

class _AdminAddEditProductScreenState extends State<AdminAddEditProductScreen> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _nameController;
  late TextEditingController _descController;
  late TextEditingController _priceController;
  late TextEditingController _imageController;
  String? _selectedCategoryId;
  bool _isSaving = false;

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.product?.name ?? '');
    _descController = TextEditingController(text: widget.product?.description ?? '');
    _priceController = TextEditingController(text: widget.product?.price.toString() ?? '');
    _imageController = TextEditingController(text: widget.product?.imageUrl ?? '');
    _selectedCategoryId = widget.product?.categoryId;
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descController.dispose();
    _priceController.dispose();
    _imageController.dispose();
    super.dispose();
  }

  void _saveForm() async {
    if (_formKey.currentState!.validate()) {
      setState(() => _isSaving = true);
      try {
        final productViewModel = context.read<ProductViewModel>();
        final newProduct = ProductModel(
          id: widget.product?.id ?? '',
          name: _nameController.text.trim(),
          description: _descController.text.trim(),
          price: double.parse(_priceController.text.trim()),
          imageUrl: _imageController.text.trim(),
          categoryId: _selectedCategoryId ?? '',
          rating: widget.product?.rating ?? 4.5,
        );

        if (widget.product == null) {
          await productViewModel.addProduct(newProduct);
        } else {
          await productViewModel.updateProduct(newProduct);
        }
        
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text("Product saved successfully!")),
          );
          Navigator.pop(context);
        }
      } catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text("Error: ${e.toString()}")),
          );
        }
      } finally {
        if (mounted) setState(() => _isSaving = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final categoryViewModel = context.watch<CategoryViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.product == null ? "Add New Product" : "Edit Product"),
        centerTitle: true,
      ),
      body: _isSaving 
        ? const Center(child: CircularProgressIndicator())
        : SingleChildScrollView(
            padding: const EdgeInsets.all(20.0),
            child: Form(
              key: _formKey,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _buildSectionTitle("General Information"),
                  const SizedBox(height: 15),
                  TextFormField(
                    controller: _nameController,
                    decoration: _inputDecoration("Product Name", Icons.shopping_bag_outlined),
                    validator: (value) => value!.isEmpty ? "Please enter product name" : null,
                  ),
                  const SizedBox(height: 15),
                  TextFormField(
                    controller: _descController,
                    decoration: _inputDecoration("Description", Icons.description_outlined),
                    maxLines: 4,
                    validator: (value) => value!.isEmpty ? "Please enter description" : null,
                  ),
                  const SizedBox(height: 25),
                  _buildSectionTitle("Pricing & Category"),
                  const SizedBox(height: 15),
                  Row(
                    children: [
                      Expanded(
                        child: TextFormField(
                          controller: _priceController,
                          decoration: _inputDecoration("Price", Icons.money),
                          keyboardType: TextInputType.number,
                          validator: (value) {
                            if (value!.isEmpty) return "Enter price";
                            if (double.tryParse(value) == null) return "Invalid price";
                            return null;
                          },
                        ),
                      ),
                      const SizedBox(width: 15),
                      Expanded(
                        child: DropdownButtonFormField<String>(
                          value: _selectedCategoryId,
                          decoration: _inputDecoration("Category", Icons.category_outlined),
                          items: categoryViewModel.categories.map((cat) {
                            return DropdownMenuItem(value: cat.id, child: Text(cat.name));
                          }).toList(),
                          onChanged: (val) => setState(() => _selectedCategoryId = val),
                          validator: (value) => value == null ? "Select category" : null,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 25),
                  _buildSectionTitle("Media"),
                  const SizedBox(height: 15),
                  TextFormField(
                    controller: _imageController,
                    decoration: _inputDecoration("Image URL", Icons.image_outlined),
                    onChanged: (val) => setState(() {}),
                    validator: (value) => value!.isEmpty ? "Please enter image url" : null,
                  ),
                  if (_imageController.text.isNotEmpty)
                    Padding(
                      padding: const EdgeInsets.only(top: 15),
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(15),
                        child: Image.network(
                          _imageController.text,
                          height: 200,
                          width: double.infinity,
                          fit: BoxFit.cover,
                          errorBuilder: (_, __, ___) => const Center(child: Text("Invalid Image URL")),
                        ),
                      ),
                    ),
                  const SizedBox(height: 40),
                  SizedBox(
                    width: double.infinity,
                    height: 55,
                    child: ElevatedButton(
                      onPressed: _saveForm,
                      style: ElevatedButton.styleFrom(
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                      ),
                      child: Text(
                        widget.product == null ? "CREATE PRODUCT" : "UPDATE PRODUCT",
                        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                ],
              ),
            ),
          ),
    );
  }

  Widget _buildSectionTitle(String title) {
    return Text(
      title,
      style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.grey),
    );
  }

  InputDecoration _inputDecoration(String label, IconData icon) {
    return InputDecoration(
      labelText: label,
      prefixIcon: Icon(icon),
      border: OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(15),
        borderSide: BorderSide(color: AppColors.slate200),
      ),
    );
  }
}
