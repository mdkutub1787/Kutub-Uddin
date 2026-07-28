import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:image_picker/image_picker.dart';
import '../../product/models/product_model.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../category/riverpod/category_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import 'dart:io';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';
import '../../../core/utils/image_optimizer.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../widgets/custom_app_bar.dart';

class AdminAddEditProductScreen extends ConsumerStatefulWidget {
  final ProductModel? product;
  const AdminAddEditProductScreen({super.key, this.product});

  @override
  ConsumerState<AdminAddEditProductScreen> createState() => _AdminAddEditProductScreenState();
}

class _AdminAddEditProductScreenState extends ConsumerState<AdminAddEditProductScreen> {
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
      final XFile? pickedFile = await picker.pickImage(source: source, maxWidth: 800, imageQuality: 80);
      if (pickedFile != null) {
        setState(() => _isSaving = true);
        
        File file = File(pickedFile.path);
        
        final optimizedFile = await ImageOptimizer.compressImage(file);
        if (optimizedFile != null) {
          file = optimizedFile;
        }

        final fileExt = pickedFile.path.split('.').last;
        final fileName = 'prod_${DateTime.now().millisecondsSinceEpoch}.$fileExt';
        final supabase = ref.read(supabaseClientProvider);
        
        try {
          await supabase.storage
              .from(AppConstants.productBucket)
              .upload(fileName, file);
              
          final publicUrl = supabase.storage
              .from(AppConstants.productBucket)
              .getPublicUrl(fileName);
              
          setState(() {
            _imageController.text = publicUrl;
            _isSaving = false;
          });
          
          if (mounted) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text("Image uploaded successfully!")),
            );
          }
        } catch (e) {
          setState(() => _isSaving = false);
          if (mounted) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text("Upload failed. Error: $e")),
            );
          }
        }
      }
    } catch (e) {
      if (mounted) {
        setState(() => _isSaving = false);
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
    final categoryState = ref.watch(categoryNotifierProvider);
    final categories = categoryState.value ?? [];
    
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;
    final currency = settings.currencySymbol;
    final size = MediaQuery.of(context).size;

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: CustomAppBar(title: widget.product == null ? "Add Product" : "Edit Product"),
      body: Stack(
        children: [
          Positioned(
            top: -size.height * 0.1,
            right: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.4,
              backgroundColor: primaryColor.withValues(alpha: 0.05),
            ),
          ),
          Positioned(
            bottom: -size.height * 0.1,
            left: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.3,
              backgroundColor: primaryColor.withValues(alpha: 0.05),
            ),
          ),
          
          _isSaving 
            ? const Center(child: CircularProgressIndicator()) 
            : SingleChildScrollView(
                padding: const EdgeInsets.all(28),
                child: Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _field(_nameController, "Product Name", Icons.shopping_bag_outlined, primaryColor),
                      const SizedBox(height: 20),
                      _field(_descController, "Description", Icons.description_outlined, primaryColor, lines: 3),
                      const SizedBox(height: 20),
                      Row(
                        children: [
                          Expanded(child: _field(_priceController, "Price", Icons.attach_money_rounded, primaryColor, type: TextInputType.number)),
                          const SizedBox(width: 15),
                          Expanded(child: _field(_stockController, "Stock", Icons.inventory_2_outlined, primaryColor, type: TextInputType.number)),
                        ],
                      ),
                      const SizedBox(height: 30),
                      const Text(
                        "Discount Settings", 
                        style: TextStyle(fontWeight: FontWeight.w900, fontSize: 18, letterSpacing: -0.5)
                      ),
                      const SizedBox(height: 15),
                      Container(
                        decoration: _fieldDecoration(enabled: true),
                        child: DropdownButtonFormField<String>(
                          value: _discountType,
                          decoration: InputDecoration(
                            labelText: "Type",
                            prefixIcon: Icon(Icons.style_outlined, color: primaryColor),
                            border: InputBorder.none,
                            contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 15),
                          ),
                          items: ['none', 'flat', 'percentage']
                              .map((e) => DropdownMenuItem(value: e, child: Text(e.toUpperCase(), style: const TextStyle(fontWeight: FontWeight.w500))))
                              .toList(),
                          onChanged: (val) => setState(() {
                            _discountType = val!;
                            if (_discountType == 'none') {
                              _discountController.text = '0';
                            }
                          }),
                        ),
                      ),
                      const SizedBox(height: 20),
                      _field(_discountController, "Discount Value", Icons.discount_outlined, primaryColor, type: TextInputType.number, enabled: _discountType != 'none'),
                      const SizedBox(height: 12),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                        decoration: BoxDecoration(
                          color: Colors.green.withValues(alpha: 0.1),
                          borderRadius: BorderRadius.circular(15),
                        ),
                        child: Text(
                          "Final Price: $currency${_calculateFinalPrice().toStringAsFixed(2)}", 
                          style: const TextStyle(fontWeight: FontWeight.w900, color: Colors.green, fontSize: 16)
                        ),
                      ),
                      const SizedBox(height: 30),
                      _field(_imageController, "Image URL", Icons.link_rounded, primaryColor),
                      const SizedBox(height: 10),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          _imagePickerBtn(Icons.camera_alt_rounded, "Camera", () => _pickImage(ImageSource.camera)),
                          const SizedBox(width: 20),
                          _imagePickerBtn(Icons.image_rounded, "Gallery", () => _pickImage(ImageSource.gallery)),
                        ],
                      ),
                      const SizedBox(height: 30),
                      Container(
                        decoration: _fieldDecoration(enabled: true),
                        child: DropdownButtonFormField<String>(
                          value: _selectedCategoryId,
                          decoration: InputDecoration(
                            labelText: "Category", 
                            prefixIcon: Icon(Icons.category_outlined, color: primaryColor), 
                            border: InputBorder.none,
                            contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 15),
                          ),
                          items: categories.map((cat) => DropdownMenuItem(value: cat.id, child: Text(cat.name, style: const TextStyle(fontWeight: FontWeight.w500)))).toList(),
                          onChanged: (val) => setState(() => _selectedCategoryId = val),
                          validator: (v) => v == null ? "Required" : null,
                        ),
                      ),
                      const SizedBox(height: 50),
                      SizedBox(
                        width: double.infinity, 
                        height: 60, 
                        child: ElevatedButton(
                          onPressed: _saveForm,
                          style: ElevatedButton.styleFrom(
                            backgroundColor: primaryColor,
                            foregroundColor: Colors.white,
                            elevation: 8,
                            shadowColor: primaryColor.withValues(alpha: 0.4),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(20),
                            ),
                          ),
                          child: const Text(
                            "SAVE PRODUCT", 
                            style: TextStyle(fontSize: 18, fontWeight: FontWeight.w900, letterSpacing: 1.2)
                          )
                        )
                      ),
                      const SizedBox(height: 50),
                    ],
                  ),
                ),
              ),
        ],
      ),
    );
  }

  Widget _imagePickerBtn(IconData icon, String label, VoidCallback onTap) {
    return TextButton.icon(
      onPressed: onTap, 
      icon: Icon(icon, size: 20), 
      label: Text(label),
      style: TextButton.styleFrom(
        foregroundColor: Theme.of(context).primaryColor,
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
        backgroundColor: Theme.of(context).primaryColor.withValues(alpha: 0.05),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      ),
    );
  }

  BoxDecoration _fieldDecoration({bool enabled = true}) {
    return BoxDecoration(
      color: enabled ? Theme.of(context).cardColor : Colors.grey[100],
      borderRadius: BorderRadius.circular(20),
      boxShadow: [
        BoxShadow(
          color: Colors.black.withValues(alpha: 0.03),
          blurRadius: 15,
          offset: const Offset(0, 5),
        )
      ],
    );
  }

  void _saveForm() async {
    if (_formKey.currentState!.validate()) {
      setState(() => _isSaving = true);
      try {
        final authState = ref.read(authNotifierProvider);
        final shopId = authState.value?.shopId ?? '';
        
        final newProduct = ProductModel(
          id: widget.product?.id ?? '',
          shopId: shopId,
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
          await ref.read(productNotifierProvider.notifier).addProduct(newProduct);
        } else {
          await ref.read(productNotifierProvider.notifier).updateProduct(newProduct);
        }
        if (mounted) Navigator.pop(context);
      } catch (e) {
        if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Error: $e")));
      } finally {
        if (mounted) setState(() => _isSaving = false);
      }
    }
  }

  Widget _field(TextEditingController controller, String label, IconData icon, Color primaryColor, {int lines = 1, TextInputType type = TextInputType.text, bool enabled = true}) {
    return Container(
      decoration: _fieldDecoration(enabled: enabled),
      child: TextFormField(
        controller: controller,
        maxLines: lines,
        keyboardType: type,
        enabled: enabled,
        style: const TextStyle(fontWeight: FontWeight.w500),
        decoration: InputDecoration(
          labelText: label, 
          prefixIcon: Icon(icon, color: primaryColor),
          border: InputBorder.none,
          filled: false,
          contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 15),
        ),
        validator: (v) => v!.isEmpty && enabled ? "Required" : null,
      ),
    );
  }
}
