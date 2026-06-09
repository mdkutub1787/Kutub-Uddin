import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../view_models/cart_view_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../models/order_model.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../utils/constants/app_strings.dart';
import '../../widgets/custom_app_bar.dart';

class CartScreen extends StatelessWidget {
  const CartScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final cart = context.watch<CartViewModel>();
    final settings = context.watch<SettingsViewModel>();

    return Scaffold(
      appBar: CustomAppBar(title: AppStrings.myCart.tr()),
      body: cart.items.isEmpty
          ? Center(child: Text(AppStrings.cartEmpty.tr()))
          : Column(
              children: [
                Expanded(
                  child: ListView.builder(
                    itemCount: cart.items.length,
                    itemBuilder: (context, index) {
                      final item = cart.items.values.toList()[index];
                      return Card(
                        margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Row(
                            children: [
                              ClipRRect(
                                borderRadius: BorderRadius.circular(10),
                                child: Image.network(
                                  item.product.imageUrl,
                                  width: 80,
                                  height: 80,
                                  fit: BoxFit.cover,
                                  errorBuilder: (_, __, ___) => Container(
                                    width: 80, height: 80, color: Colors.grey[200], 
                                    child: const Icon(Icons.image_not_supported)
                                  ),
                                ),
                              ),
                              const SizedBox(width: 15),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      item.product.name, 
                                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16), 
                                      maxLines: 1, 
                                      overflow: TextOverflow.ellipsis
                                    ),
                                    const SizedBox(height: 5),
                                    Text(
                                      "৳${item.product.price}", 
                                      style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold)
                                    ),
                                  ],
                                ),
                              ),
                              Row(
                                children: [
                                  IconButton(
                                    icon: const Icon(Icons.remove_circle_outline), 
                                    onPressed: () => cart.removeSingleItem(item.product.id)
                                  ),
                                  Text("${item.quantity}", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                                  IconButton(
                                    icon: const Icon(Icons.add_circle_outline), 
                                    onPressed: () => cart.addItem(item.product)
                                  ),
                                ],
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
                ),
                _buildCheckoutSection(context, cart, settings),
              ],
            ),
    );
  }

  Widget _buildCheckoutSection(BuildContext context, CartViewModel cart, SettingsViewModel settings) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: const BorderRadius.vertical(top: Radius.circular(30)),
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 10, offset: const Offset(0, -5))],
      ),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text("${AppStrings.total.tr()}:", style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
              Text(
                "৳${cart.totalAmount.toStringAsFixed(2)}", 
                style: TextStyle(fontSize: 22, fontWeight: FontWeight.w900, color: settings.primaryColor)
              ),
            ],
          ),
          const SizedBox(height: 20),
          SizedBox(
            width: double.infinity,
            height: 55,
            child: ElevatedButton(
              onPressed: () => _handleCheckout(context, cart),
              child: const Text("PLACE ORDER", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            ),
          ),
        ],
      ),
    );
  }

  void _handleCheckout(BuildContext context, CartViewModel cart) async {
    final auth = context.read<AuthViewModel>();
    if (auth.user == null) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Please login to order")));
      return;
    }

    // Professional order with user details
    final newOrder = OrderModel(
      id: '',
      userId: auth.user!.uid,
      userName: auth.user!.name,
      userPhone: auth.user!.phoneNumber,
      userAddress: auth.user!.address,
      items: cart.items.values.toList(),
      totalAmount: cart.totalAmount,
      date: DateTime.now(),
      status: 'Pending',
    );

    bool success = await context.read<OrderViewModel>().placeOrder(newOrder);
    if (success) {
      cart.clearCart();
      if (context.mounted) {
        showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            title: const Text("Success!"),
            content: const Text("Order placed. Stock updated automatically."),
            actions: [TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("OK"))],
          ),
        );
      }
    } else {
      if (context.mounted) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Failed to place order. Check stock.")));
      }
    }
  }
}
