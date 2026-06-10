import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../models/product_model.dart';
import '../../models/order_model.dart';
import '../../models/cart_model.dart';
import '../../models/user_model.dart';
import '../../models/category_model.dart';
import '../../view_models/product_view_model.dart';
import '../../view_models/category_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/user_view_model.dart';
import '../../view_models/loading_view_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../utils/constants/app_strings.dart';
import '../../widgets/custom_app_bar.dart';

class AdminPosScreen extends StatefulWidget {
  const AdminPosScreen({super.key});

  @override
  State<AdminPosScreen> createState() => _AdminPosScreenState();
}

class _AdminPosScreenState extends State<AdminPosScreen> {
  final TextEditingController _searchController = TextEditingController();
  final TextEditingController _customerPhoneController = TextEditingController();
  final TextEditingController _customerNameController = TextEditingController();
  
  final Map<String, CartItem> _posCart = {};
  String _searchQuery = "";
  String _selectedCategoryId = "";
  List<UserModel> _suggestedUsers = [];

  double get _subtotal {
    return _posCart.values.fold(0, (sum, item) => sum + (item.product.price * item.quantity));
  }

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final user = context.read<AuthViewModel>().user;
      final shopId = user?.shopId;
      context.read<UserViewModel>().fetchUsers(shopId: shopId, isSuperAdmin: user?.role == 'super_admin');
      context.read<CategoryViewModel>().fetchCategories(shopId: shopId);
      context.read<ProductViewModel>().initStream(shopId: shopId);
    });
  }

  void _onPhoneChanged(String value, List<UserModel> allUsers) {
    if (value.length < 3) {
      setState(() => _suggestedUsers = []);
      return;
    }
    setState(() {
      _suggestedUsers = allUsers.where((u) => 
        u.phoneNumber.contains(value) || 
        u.name.toLowerCase().contains(value.toLowerCase())
      ).take(5).toList();
    });
  }

  void _selectUser(UserModel user) {
    setState(() {
      _customerPhoneController.text = user.phoneNumber;
      _customerNameController.text = user.name;
      _suggestedUsers = [];
    });
  }

  @override
  Widget build(BuildContext context) {
    final productVM = context.watch<ProductViewModel>();
    final categoryVM = context.watch<CategoryViewModel>();
    final settings = context.watch<SettingsViewModel>();

    final filteredProducts = productVM.featuredProducts.where((p) {
      bool matchesSearch = p.name.toLowerCase().contains(_searchQuery.toLowerCase());
      bool matchesCategory = _selectedCategoryId.isEmpty || p.categoryId == _selectedCategoryId;
      return matchesSearch && matchesCategory;
    }).toList();

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: CustomAppBar(
        title: "Point of Sale",
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh_rounded, color: Colors.white),
            onPressed: () => setState(() => _posCart.clear()),
          ),
        ],
      ),
      body: Column(
        children: [
          _buildSearchAndFilter(categoryVM, settings.primaryColor),
          Expanded(
            child: filteredProducts.isEmpty
                ? _buildEmptyState(Icons.inventory_2_outlined, "No products found")
                : GridView.builder(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      childAspectRatio: 0.8,
                      crossAxisSpacing: 12,
                      mainAxisSpacing: 12,
                    ),
                    itemCount: filteredProducts.length,
                    itemBuilder: (context, index) => _buildProductCard(filteredProducts[index], settings.primaryColor),
                  ),
          ),
          _buildBottomCartBar(settings.primaryColor),
        ],
      ),
    );
  }

  Widget _buildSearchAndFilter(CategoryViewModel categoryVM, Color primaryColor) {
    return Container(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          TextField(
            controller: _searchController,
            onChanged: (v) => setState(() => _searchQuery = v),
            decoration: InputDecoration(
              hintText: "Search products...",
              prefixIcon: Icon(Icons.search, color: primaryColor),
              filled: true,
              fillColor: Colors.white,
              contentPadding: const EdgeInsets.symmetric(horizontal: 20),
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(30), borderSide: BorderSide.none),
              enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(30), borderSide: BorderSide(color: Colors.grey[200]!)),
            ),
          ),
          const SizedBox(height: 12),
          SizedBox(
            height: 35,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              itemCount: categoryVM.categories.length + 1,
              itemBuilder: (context, index) {
                bool isAll = index == 0;
                String catId = isAll ? "" : categoryVM.categories[index - 1].id;
                String name = isAll ? "All Items" : categoryVM.categories[index - 1].name;
                bool isSelected = _selectedCategoryId == catId;

                return Padding(
                  padding: const EdgeInsets.only(right: 8),
                  child: ChoiceChip(
                    label: Text(name, style: TextStyle(fontSize: 12, color: isSelected ? Colors.white : Colors.black87)),
                    selected: isSelected,
                    onSelected: (v) => setState(() => _selectedCategoryId = catId),
                    selectedColor: primaryColor,
                    backgroundColor: Colors.white,
                    showCheckmark: false,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildProductCard(ProductModel product, Color primaryColor) {
    bool hasStock = product.stock > 0;
    int cartQty = _posCart[product.id]?.quantity ?? 0;

    return GestureDetector(
      onTap: hasStock ? () => _addToCart(product) : null,
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16),
          border: Border.all(color: cartQty > 0 ? primaryColor : Colors.transparent, width: 2),
          boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.03), blurRadius: 10, offset: const Offset(0, 4))],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              child: Stack(
                children: [
                  ClipRRect(
                    borderRadius: const BorderRadius.vertical(top: Radius.circular(14)),
                    child: Image.network(
                      product.imageUrl,
                      width: double.infinity,
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => Container(color: Colors.grey[100], child: const Icon(Icons.image_outlined, color: Colors.grey)),
                    ),
                  ),
                  if (!hasStock)
                    Container(
                      decoration: BoxDecoration(color: Colors.black45, borderRadius: const BorderRadius.vertical(top: Radius.circular(14))),
                      child: const Center(child: Text("OUT OF STOCK", style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold))),
                    ),
                  if (cartQty > 0)
                    Positioned(
                      top: 8, right: 8,
                      child: CircleAvatar(radius: 12, backgroundColor: primaryColor, child: Text("$cartQty", style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold))),
                    ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(product.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13), maxLines: 1, overflow: TextOverflow.ellipsis),
                  const SizedBox(height: 4),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text("৳${product.price.toInt()}", style: TextStyle(color: primaryColor, fontWeight: FontWeight.w900, fontSize: 14)),
                      Text("Stk: ${product.stock}", style: TextStyle(color: hasStock ? Colors.grey : Colors.red, fontSize: 10)),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildBottomCartBar(Color primaryColor) {
    if (_posCart.isEmpty) return const SizedBox.shrink();

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 10, offset: const Offset(0, -5))],
      ),
      child: SafeArea(
        child: Row(
          children: [
            Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text("${_posCart.length} Items", style: const TextStyle(color: Colors.grey, fontSize: 12, fontWeight: FontWeight.bold)),
                Text("৳${_subtotal.toInt()}", style: TextStyle(color: primaryColor, fontSize: 20, fontWeight: FontWeight.w900)),
              ],
            ),
            const SizedBox(width: 20),
            Expanded(
              child: SizedBox(
                height: 50,
                child: ElevatedButton(
                  onPressed: () => _showCheckoutSheet(primaryColor),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: primaryColor,
                    foregroundColor: Colors.white,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  ),
                  child: const Text("PROCEED TO CHECKOUT", style: TextStyle(fontWeight: FontWeight.bold, letterSpacing: 1)),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _showCheckoutSheet(Color primaryColor) {
    final userVM = context.read<UserViewModel>();
    final authVM = context.read<AuthViewModel>();

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(25))),
      builder: (context) => StatefulBuilder(
        builder: (ctx, setModalState) => Padding(
          padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom, left: 20, right: 20, top: 20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text("Finalize Sale", style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                  IconButton(icon: const Icon(Icons.close), onPressed: () => Navigator.pop(context)),
                ],
              ),
              const SizedBox(height: 20),
              const Text("CUSTOMER DETAILS", style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.grey)),
              const SizedBox(height: 10),
              _buildPhoneField(userVM.users, setModalState, primaryColor),
              const SizedBox(height: 12),
              _posTextField(_customerNameController, "Customer Name", Icons.person_outline_rounded),
              const SizedBox(height: 20),
              const Text("CART SUMMARY", style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.grey)),
              const SizedBox(height: 10),
              ConstrainedBox(
                constraints: const BoxConstraints(maxHeight: 200),
                child: ListView(
                  shrinkWrap: true,
                  children: _posCart.values.map((item) => ListTile(
                    contentPadding: EdgeInsets.zero,
                    title: Text(item.product.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                    subtitle: Text("৳${item.product.price.toInt()} x ${item.quantity}"),
                    trailing: Text("৳${(item.product.price * item.quantity).toInt()}", style: const TextStyle(fontWeight: FontWeight.bold)),
                  )).toList(),
                ),
              ),
              const Divider(height: 30),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text("Total Amount", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                  Text("৳${_subtotal.toInt()}", style: TextStyle(fontSize: 24, fontWeight: FontWeight.w900, color: primaryColor)),
                ],
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                height: 55,
                child: ElevatedButton(
                  onPressed: () => _processCheckout(authVM),
                  style: ElevatedButton.styleFrom(backgroundColor: primaryColor, foregroundColor: Colors.white, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15))),
                  child: const Text("CONFIRM & SELL", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                ),
              ),
              const SizedBox(height: 30),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildPhoneField(List<UserModel> allUsers, Function setModalState, Color primaryColor) {
    return Stack(
      clipBehavior: Clip.none,
      children: [
        TextField(
          controller: _customerPhoneController,
          keyboardType: TextInputType.phone,
          onChanged: (v) {
            if (v.length < 3) {
              setModalState(() => _suggestedUsers = []);
              return;
            }
            setModalState(() {
              _suggestedUsers = allUsers.where((u) => u.phoneNumber.contains(v) || u.name.toLowerCase().contains(v.toLowerCase())).take(5).toList();
            });
          },
          decoration: InputDecoration(
            hintText: "Mobile Number",
            prefixIcon: const Icon(Icons.phone_android_rounded),
            filled: true,
            fillColor: Colors.grey[50],
            border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide(color: Colors.grey[200]!)),
          ),
        ),
        if (_suggestedUsers.isNotEmpty)
          Positioned(
            top: 60, left: 0, right: 0,
            child: Material(
              elevation: 8,
              borderRadius: BorderRadius.circular(12),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: _suggestedUsers.map((u) => ListTile(
                  title: Text(u.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                  subtitle: Text(u.phoneNumber, style: const TextStyle(fontSize: 11)),
                  onTap: () {
                    _selectUser(u);
                    setModalState(() => _suggestedUsers = []);
                  },
                )).toList(),
              ),
            ),
          ),
      ],
    );
  }

  Widget _posTextField(TextEditingController ctrl, String hint, IconData icon) {
    return TextField(
      controller: ctrl,
      decoration: InputDecoration(
        hintText: hint,
        prefixIcon: Icon(icon),
        filled: true,
        fillColor: Colors.grey[50],
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide(color: Colors.grey[200]!)),
      ),
    );
  }

  Widget _buildEmptyState(IconData icon, String msg) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: 40, color: Colors.grey[300]),
          const SizedBox(height: 8),
          Text(msg, style: TextStyle(color: Colors.grey[400], fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }

  void _addToCart(dynamic productOrId) {
    setState(() {
      String id = productOrId is ProductModel ? productOrId.id : productOrId as String;
      ProductModel product = productOrId is ProductModel ? productOrId : _posCart[id]!.product;
      if (_posCart.containsKey(id)) {
        if (_posCart[id]!.quantity < product.stock) {
          _posCart.update(id, (v) => CartItem(product: v.product, quantity: v.quantity + 1));
        }
      } else {
        _posCart[id] = CartItem(product: product, quantity: 1);
      }
    });
  }

  void _processCheckout(AuthViewModel authVM) async {
    final loading = context.read<LoadingViewModel>();
    loading.show(message: "Completing Sale...");
    final shopId = authVM.user?.shopId ?? '';
    final order = OrderModel(
      id: '', shopId: shopId, userId: 'pos',
      userName: _customerNameController.text.isEmpty ? 'Walk-in Customer' : _customerNameController.text.trim(),
      userPhone: _customerPhoneController.text.isEmpty ? 'N/A' : _customerPhoneController.text.trim(),
      userAddress: 'Store Sale',
      items: _posCart.values.toList(),
      totalAmount: _subtotal,
      deliveryFee: 0, date: DateTime.now(),
      status: 'Delivered', orderType: 'pos',
    );
    bool success = await context.read<OrderViewModel>().placeOrder(order);
    loading.hide();
    if (success && mounted) {
      Navigator.pop(context);
      setState(() { _posCart.clear(); _customerNameController.clear(); _customerPhoneController.clear(); });
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Sale Completed Successfully!"), backgroundColor: Colors.green));
    }
  }
}
