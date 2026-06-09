import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:image_picker/image_picker.dart';
import '../../models/product_model.dart';
import '../../view_models/product_view_model.dart';
import '../../view_models/category_view_model.dart';
import '../../widgets/custom_app_bar.dart';

class AdminAddEditProductScreen extends StatefulWidget {
  final ProductModel? product;
  const AdminAddEditProductScreen({super.key, this.product});

  @override
  State<AdminAddEditProductScreen> createState() => _AdminAddEditProductScreenState();
}

class _AdminAddEditProductScreenState extends State<AdminAddEditProductScreen> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _nameController, _descController, _priceController, _imageController, _stockController;
  late TextEditingController _discountController;
  String _discountType = 'none';
  String? _selectedCategoryId;
  bool _isSaving = false;

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.product?.name ?? '');
    _descController = TextEditingController(text: widget.product?.description ?? '');
    _priceController = TextEditingController(text: widget.product?.originalPrice.toString() ?? '');
    _imageController = TextEditingController(text: widget.product?.imageUrl ?? '');
    _stockController = TextEditingController(text: widget.product?.stock.toString() ?? '10');
    _discountController = TextEditingController(text: widget.product?.discountValue.toString() ?? '0');
    _discountType = widget.product?.discountType ?? 'none';
    _selectedCategoryId = widget.product?.categoryId;
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descController.dispose();
    _priceController.dispose();
    _imageController.dispose();
    _stockController.dispose();
    _discountController.dispose();
    super.dispose();
  }

  Future<void> _pickImage(ImageSource source) async {
    try {
      final picker = ImagePicker();
      final XFile? pickedFile = await picker.pickImage(source: source);
      if (pickedFile != null) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text("Image selected. Please provide a URL to save permanently.")),
          );
        }
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Error picking image: $e")),
        );
      }
    }
  }

  double _calculateFinalPrice() {
    double original = double.tryParse(_priceController.text) ?? 0;
    double discVal = double.tryParse(_discountController.text) ?? 0;
    if (_discountType == 'none') return original;
    if (_discountType == 'flat') return original - discVal;
    if (_discountType == 'percentage') return original - (original * discVal / 100);
    return original;
  }

  @override
  Widget build(BuildContext context) {
    final categoryViewModel = context.watch<CategoryViewModel>();
    return Scaffold(
      appBar: CustomAppBar(title: widget.product == null ? "Add Product" : "Edit Product"),
      body: _isSaving ? const Center(child: CircularProgressIndicator()) : SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _field(_nameController, "Product Name", Icons.shopping_bag),
              const SizedBox(height: 15),
              _field(_descController, "Description", Icons.description, lines: 3),
              const SizedBox(height: 15),
              Row(
                children: [
                  Expanded(child: _field(_priceController, "Regular Price", Icons.attach_money, type: TextInputType.number)),
                  const SizedBox(width: 15),
                  Expanded(child: _field(_stockController, "Stock", Icons.inventory, type: TextInputType.number)),
                ],
              ),
              const SizedBox(height: 15),
              const Text("Discount System", style: TextStyle(fontWeight: FontWeight.bold, color: Colors.blue)),
              Row(
                children: [
                  Expanded(
                    child: DropdownButtonFormField<String>(
                      value: _discountType,
                      decoration: const InputDecoration(labelText: "Type"),
                      items: ['none', 'flat', 'percentage'].map((e) => DropdownMenuItem(value: e, child: Text(e.toUpperCase()))).toList(),
                      onChanged: (val) => setState(() {
                        _discountType = val!;
                        if (_discountType == 'none') {
                          _discountController.text = '0';
                        }
                      }),
                    ),
                  ),
                  const SizedBox(width: 15),
                  Expanded(child: _field(_discountController, "Value (Tk/%)", Icons.discount, type: TextInputType.number, enabled: _discountType != 'none')),
                ],
              ),
              const SizedBox(height: 10),
              Text("Final Price: ৳${_calculateFinalPrice().toStringAsFixed(2)}", style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.green)),
              const SizedBox(height: 20),
              _field(_imageController, "Image URL", Icons.link),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  IconButton(onPressed: () => _pickImage(ImageSource.camera), icon: const Icon(Icons.camera_alt, color: Colors.blue)),
                  IconButton(onPressed: () => _pickImage(ImageSource.gallery), icon: const Icon(Icons.image, color: Colors.blue)),
                  const Text("Helper (Camera/Gallery)"),
                ],
              ),
              const SizedBox(height: 15),
              DropdownButtonFormField<String>(
                value: _selectedCategoryId,
                decoration: InputDecoration(
                  labelText: "Category", 
                  prefixIcon: const Icon(Icons.category), 
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(15))
                ),
                items: categoryViewModel.categories.map((cat) => DropdownMenuItem(value: cat.id, child: Text(cat.name))).toList(),
                onChanged: (val) => setState(() => _selectedCategoryId = val),
              ),
              const SizedBox(height: 30),
              SizedBox(
                width: double.infinity, 
                height: 55, 
                child: ElevatedButton(
                  onPressed: _saveForm, 
                  child: const Text("SAVE PRODUCT", style: TextStyle(fontWeight: FontWeight.bold))
                )
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _saveForm() async {
    if (_formKey.currentState!.validate()) {
      setState(() => _isSaving = true);
      try {
        final newProduct = ProductModel(
          id: widget.product?.id ?? '',
          name: _nameController.text.trim(),
          description: _descController.text.trim(),
          originalPrice: double.parse(_priceController.text.trim()),
          price: _calculateFinalPrice(),
          discountValue: double.parse(_discountController.text.trim()),
          discountType: _discountType,
          imageUrl: _imageController.text.trim(),
          categoryId: _selectedCategoryId ?? '',
          rating: widget.product?.rating ?? 4.5,
          stock: int.parse(_stockController.text.trim()),
        );

        if (widget.product == null) {
          await context.read<ProductViewModel>().addProduct(newProduct);
        } else {
          await context.read<ProductViewModel>().updateProduct(newProduct);
        }
        if (mounted) Navigator.pop(context);
      } catch (e) {
        if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Error: $e")));
      } finally {
        if (mounted) setState(() => _isSaving = false);
      }
    }
  }

  Widget _field(TextEditingController controller, String label, IconData icon, {int lines = 1, TextInputType type = TextInputType.text, bool enabled = true}) {
    return TextFormField(
      controller: controller,
      maxLines: lines,
      keyboardType: type,
      enabled: enabled,
      decoration: InputDecoration(
        labelText: label, 
        prefixIcon: Icon(icon), 
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
        filled: !enabled,
        fillColor: enabled ? null : Colors.grey[200],
      ),
      validator: (v) => v!.isEmpty && enabled ? "Required" : null,
    );
  }
}
