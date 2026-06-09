import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/product_model.dart';
import '../../view_models/product_view_model.dart';
import '../../view_models/category_view_model.dart';

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
      final productViewModel = context.read<ProductViewModel>();
      final newProduct = ProductModel(
        id: widget.product?.id ?? '',
        name: _nameController.text,
        description: _descController.text,
        price: double.parse(_priceController.text),
        imageUrl: _imageController.text,
        categoryId: _selectedCategoryId ?? '',
        rating: widget.product?.rating ?? 0.0,
      );

      if (widget.product == null) {
        await productViewModel.addProduct(newProduct);
      } else {
        await productViewModel.updateProduct(newProduct);
      }
      if (mounted) Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    final categoryViewModel = context.watch<CategoryViewModel>();

    return Scaffold(
      appBar: AppBar(title: Text(widget.product == null ? "Add Product" : "Edit Product")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(labelText: "Product Name"),
                validator: (value) => value!.isEmpty ? "Enter name" : null,
              ),
              TextFormField(
                controller: _descController,
                decoration: const InputDecoration(labelText: "Description"),
                maxLines: 3,
                validator: (value) => value!.isEmpty ? "Enter description" : null,
              ),
              TextFormField(
                controller: _priceController,
                decoration: const InputDecoration(labelText: "Price"),
                keyboardType: TextInputType.number,
                validator: (value) => value!.isEmpty ? "Enter price" : null,
              ),
              TextFormField(
                controller: _imageController,
                decoration: const InputDecoration(labelText: "Image URL"),
                validator: (value) => value!.isEmpty ? "Enter image url" : null,
              ),
              const SizedBox(height: 10),
              DropdownButtonFormField<String>(
                value: _selectedCategoryId,
                decoration: const InputDecoration(labelText: "Category"),
                items: categoryViewModel.categories.map((cat) {
                  return DropdownMenuItem(value: cat.id, child: Text(cat.name));
                }).toList(),
                onChanged: (val) => setState(() => _selectedCategoryId = val),
                validator: (value) => value == null ? "Select category" : null,
              ),
              const SizedBox(height: 30),
              ElevatedButton(
                onPressed: _saveForm,
                child: const Text("Save Product"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
