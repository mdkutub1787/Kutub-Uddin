import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../product/models/product_model.dart';
import '../../order/models/order_model.dart';
import '../../cart/models/cart_model.dart';
import '../../user/models/user_model.dart';
import '../../category/models/category_model.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../category/riverpod/category_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../user/riverpod/user_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../core/app_strings.dart';
import '../../../widgets/custom_app_bar.dart';

class AdminPosScreen extends ConsumerStatefulWidget {
  const AdminPosScreen({super.key});

  @override
  ConsumerState<AdminPosScreen> createState() => _AdminPosScreenState();
}

class _AdminPosScreenState extends ConsumerState<AdminPosScreen> {
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
      ref.read(userNotifierProvider.notifier).loadUsers();
      ref.read(categoryNotifierProvider.notifier).loadCategories();
    });
  }

  void _selectUser(UserModel user, Function? setModalState) {
    setState(() {
      _customerPhoneController.text = user.phoneNumber;
      _customerNameController.text = user.name;
    });
    if (setModalState != null) {
      setModalState(() {
        _suggestedUsers = [];
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final productState = ref.watch(productNotifierProvider);
    final categoryState = ref.watch(categoryNotifierProvider);
    final settings = ref.watch(settingsProvider);
    final currency = settings.currencySymbol;

    final products = productState.featuredProducts;
    final categories = categoryState.value ?? [];

    final filteredProducts = products.where((p) {
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
          _buildSearchAndFilter(categories, settings.primaryColor),
          Expanded(
            child: filteredProducts.isEmpty
                ? _buildEmptyState(Icons.inventory_2_outlined, "No products found")
                : GridView.builder(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      childAspectRatio: 0.68,
                      crossAxisSpacing: 12,
                      mainAxisSpacing: 12,
                    ),
                    itemCount: filteredProducts.length,
                    itemBuilder: (context, index) => _buildProductCard(filteredProducts[index], settings.primaryColor, currency),
                  ),
          ),
          _buildBottomCartBar(settings.primaryColor, currency),
        ],
      ),
    );
  }

  Widget _buildSearchAndFilter(List<CategoryModel> categories, Color primaryColor) {
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
              itemCount: categories.length + 1,
              itemBuilder: (context, index) {
                bool isAll = index == 0;
                String catId = isAll ? "" : categories[index - 1].id;
                String name = isAll ? "All Items" : categories[index - 1].name;
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

  Widget _buildProductCard(ProductModel product, Color primaryColor, String currency) {
    bool hasStock = product.stock > 0;
    int cartQty = _posCart[product.id]?.quantity ?? 0;

    return Container(
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
                GestureDetector(
                  onTap: hasStock ? () => _addToCart(product) : null,
                  child: ClipRRect(
                    borderRadius: const BorderRadius.vertical(top: Radius.circular(14)),
                    child: Image.network(
                      product.imageUrl,
                      width: double.infinity,
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => Container(color: Colors.grey[100], child: const Icon(Icons.image_outlined, color: Colors.grey)),
                    ),
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
                    Text("$currency${product.price.toInt()}", style: TextStyle(color: primaryColor, fontWeight: FontWeight.w900, fontSize: 14)),
                    Text("Stk: ${product.stock}", style: TextStyle(color: hasStock ? Colors.grey : Colors.red, fontSize: 10)),
                  ],
                ),
                if (cartQty > 0) ...[
                  const SizedBox(height: 8),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      _qtyCardBtn(Icons.remove, () => _removeFromCart(product.id), Colors.red),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 12),
                        child: Text("$cartQty", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                      ),
                      _qtyCardBtn(Icons.add, () => _addToCart(product.id), Colors.green),
                    ],
                  ),
                ] else if (hasStock) ...[
                  const SizedBox(height: 8),
                  SizedBox(
                    width: double.infinity,
                    height: 30,
                    child: OutlinedButton(
                      onPressed: () => _addToCart(product),
                      style: OutlinedButton.styleFrom(
                        side: BorderSide(color: primaryColor),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                        padding: EdgeInsets.zero,
                      ),
                      child: Text("ADD", style: TextStyle(color: primaryColor, fontSize: 12, fontWeight: FontWeight.bold)),
                    ),
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _qtyCardBtn(IconData icon, VoidCallback onTap, Color color) {
    return InkWell(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.all(4),
        decoration: BoxDecoration(
          color: color.withValues(alpha: 0.1),
          borderRadius: BorderRadius.circular(6),
        ),
        child: Icon(icon, size: 16, color: color),
      ),
    );
  }

  Widget _buildBottomCartBar(Color primaryColor, String currency) {
    if (_posCart.isEmpty) return const SizedBox.shrink();

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: const BoxDecoration(
        color: Colors.white,
        boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 10, offset: Offset(0, -5))],
      ),
      child: SafeArea(
        child: Row(
          children: [
            Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text("${_posCart.length} Items", style: const TextStyle(color: Colors.grey, fontSize: 12, fontWeight: FontWeight.bold)),
                Text("$currency${_subtotal.toInt()}", style: TextStyle(color: primaryColor, fontSize: 20, fontWeight: FontWeight.w900)),
              ],
            ),
            const SizedBox(width: 20),
            Expanded(
              child: SizedBox(
                height: 50,
                child: ElevatedButton(
                  onPressed: () => _showCheckoutSheet(primaryColor, currency),
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

  void _showCheckoutSheet(Color primaryColor, String currency) {
    final userState = ref.read(userNotifierProvider);
    final users = userState.value ?? [];

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
              _buildPhoneField(users, setModalState, primaryColor),
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
                    subtitle: Text("$currency${item.product.price.toInt()} x ${item.quantity}"),
                    trailing: Text("$currency${(item.product.price * item.quantity).toInt()}", style: const TextStyle(fontWeight: FontWeight.bold)),
                  )).toList(),
                ),
              ),
              const Divider(height: 30),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text("Total Amount", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                  Text("$currency${_subtotal.toInt()}", style: TextStyle(fontSize: 24, fontWeight: FontWeight.w900, color: primaryColor)),
                ],
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                height: 55,
                child: ElevatedButton(
                  onPressed: () => _processCheckout(),
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
                    _selectUser(u, setModalState);
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

  void _removeFromCart(String productId) {
    setState(() {
      if (_posCart.containsKey(productId)) {
        if (_posCart[productId]!.quantity > 1) {
          _posCart.update(productId, (v) => CartItem(product: v.product, quantity: v.quantity - 1));
        } else {
          _posCart.remove(productId);
        }
      }
    });
  }

  void _processCheckout() async {
    FocusManager.instance.primaryFocus?.unfocus();
    final authState = ref.read(authNotifierProvider);
    final shopId = authState.value?.shopId ?? '';
    final order = OrderModel(
      id: '', shopId: shopId, userId: 'pos',
      userName: _customerNameController.text.isEmpty ? 'Walk-in Customer' : _customerNameController.text.trim(),
      userPhone: _customerPhoneController.text.isEmpty ? 'N/A' : _customerPhoneController.text.trim(),
      userAddress: 'Store Sale',
      items: _posCart.values.toList(),
      totalAmount: _subtotal,
      deliveryFee: 0, date: DateTime.now(),
      status: 'Delivered', orderType: 'pos',
      paymentMethod: 'Cash', // Add this
      isPaid: true, // Add this
    );
    bool success = await ref.read(orderNotifierProvider.notifier).placeOrder(order);
    if (success && mounted) {
      Navigator.pop(context);
      setState(() { _posCart.clear(); _customerNameController.clear(); _customerPhoneController.clear(); });
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Sale Completed Successfully!"), backgroundColor: Colors.green));
    }
  }
}
