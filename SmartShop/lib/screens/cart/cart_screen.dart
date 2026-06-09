import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../view_models/cart_view_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/navigation_view_model.dart';
import '../../models/order_model.dart';
import '../../utils/constants/app_strings.dart';
import '../../widgets/custom_app_bar.dart';
import '../../widgets/empty_state_widget.dart';
import 'package:intl/intl.dart';
import '../../widgets/app_card.dart';
import '../../widgets/product_list_item.dart';

import '../../view_models/loading_view_model.dart';

class CartScreen extends StatefulWidget {
  const CartScreen({super.key});

  @override
  State<CartScreen> createState() => _CartScreenState();
}

class _CartScreenState extends State<CartScreen> {
  final TextEditingController _couponController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final cart = context.watch<CartViewModel>();
    final settings = context.watch<SettingsViewModel>();
    final auth = context.watch<AuthViewModel>();
    final size = MediaQuery.of(context).size;

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: CustomAppBar(title: AppStrings.myCart.tr()),
      body: Stack(
        children: [
          // Decorative Background Elements
          Positioned(
            top: -size.height * 0.1,
            right: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.4,
              backgroundColor: settings.primaryColor.withValues(alpha: 0.05),
            ),
          ),
          Positioned(
            bottom: -size.height * 0.1,
            left: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.3,
              backgroundColor: settings.primaryColor.withValues(alpha: 0.05),
            ),
          ),
          
          cart.items.isEmpty
              ? EmptyStateWidget(
                  icon: Icons.shopping_cart_outlined,
                  title: AppStrings.cartEmpty.tr(),
                  subtitle: "Add some products to your cart and start shopping!",
                  actionText: "Browse Products",
                  onAction: () => context.read<NavigationViewModel>().setIndex(0),
                )
              : SingleChildScrollView(
                  child: Column(
                    children: [
                      _buildAddressSection(context, auth, settings),
                      _buildDeliveryMethodSection(context, cart, settings),
                      _buildItemsList(context, cart, settings),
                      _buildCouponSection(context, cart, settings),
                      _buildOrderSummary(context, cart, settings),
                      const SizedBox(height: 120),
                    ],
                  ),
                ),
        ],
      ),
      bottomSheet: cart.items.isEmpty ? null : _buildCheckoutButton(context, cart, settings),
    );
  }

  Widget _buildDeliveryMethodSection(BuildContext context, CartViewModel cart, SettingsViewModel settings) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: Colors.grey.withValues(alpha: 0.1)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            "Delivery Area",
            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
          ),
          const SizedBox(height: 12),
          Row(
            children: [
              Expanded(
                child: _deliveryOption(
                  context, 
                  "Inside Dhaka", 
                  "60", 
                  cart.isInsideDhaka, 
                  () => cart.setInsideDhaka(true),
                  settings
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: _deliveryOption(
                  context, 
                  "Outside Dhaka", 
                  "150", 
                  !cart.isInsideDhaka, 
                  () => cart.setInsideDhaka(false),
                  settings
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _deliveryOption(BuildContext context, String title, String price, bool isSelected, VoidCallback onTap, SettingsViewModel settings) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 8),
        decoration: BoxDecoration(
          color: isSelected ? settings.primaryColor.withValues(alpha: 0.05) : Colors.grey[50],
          borderRadius: BorderRadius.circular(15),
          border: Border.all(
            color: isSelected ? settings.primaryColor : Colors.grey.withValues(alpha: 0.2),
            width: 1.5,
          ),
        ),
        child: Column(
          children: [
            Text(
              title,
              style: TextStyle(
                fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                color: isSelected ? settings.primaryColor : Colors.black54,
                fontSize: 13,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              "৳$price",
              style: TextStyle(
                fontWeight: FontWeight.w900,
                color: isSelected ? settings.primaryColor : Colors.black87,
                fontSize: 16,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildAddressSection(BuildContext context, AuthViewModel auth, SettingsViewModel settings) {
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: Colors.grey.withValues(alpha: 0.1)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                AppStrings.shippingAddress.tr(),
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
              ),
              TextButton(
                onPressed: () {},
                child: const Text("Change"),
              )
            ],
          ),
          const SizedBox(height: 8),
          Row(
            children: [
              Icon(Icons.location_on_rounded, color: settings.primaryColor, size: 20),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  auth.user?.address ?? "No address set. Please update your profile.",
                  style: const TextStyle(color: Colors.black87),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildItemsList(BuildContext context, CartViewModel cart, SettingsViewModel settings) {
    final items = cart.items.values.toList();
    return ListView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      padding: const EdgeInsets.symmetric(horizontal: 16),
      itemCount: items.length,
      itemBuilder: (context, index) {
        final item = items[index];
        return ProductListItem(
          product: item.product,
          trailing: Container(
            decoration: BoxDecoration(
              color: Colors.grey[100],
              borderRadius: BorderRadius.circular(10),
            ),
            child: Row(
              children: [
                _qtyBtn(Icons.remove, () => cart.removeSingleItem(item.product.id)),
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 8),
                  child: Text(
                    "${item.quantity}",
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                _qtyBtn(Icons.add, () => cart.addItem(item.product)),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget _qtyBtn(IconData icon, VoidCallback onTap) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.all(6),
        child: Icon(icon, size: 18),
      ),
    );
  }

  Widget _buildCouponSection(BuildContext context, CartViewModel cart, SettingsViewModel settings) {
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(15),
        border: Border.all(color: Colors.grey.withValues(alpha: 0.1)),
      ),
      child: Row(
        children: [
          const Icon(Icons.confirmation_num_outlined, color: Colors.grey),
          const SizedBox(width: 10),
          Expanded(
            child: TextField(
              controller: _couponController,
              decoration: const InputDecoration(
                hintText: "Enter Coupon Code",
                border: InputBorder.none,
              ),
            ),
          ),
          TextButton(
            onPressed: () {
              bool success = cart.applyCoupon(_couponController.text);
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(success ? "Coupon Applied!" : "Invalid Coupon"),
                  backgroundColor: success ? Colors.green : Colors.red,
                ),
              );
              if (success) _couponController.clear();
            },
            child: const Text("Apply", style: TextStyle(fontWeight: FontWeight.bold)),
          ),
        ],
      ),
    );
  }

  Widget _buildOrderSummary(BuildContext context, CartViewModel cart, SettingsViewModel settings) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.02),
            blurRadius: 10,
            offset: const Offset(0, 4),
          )
        ],
      ),
      child: Column(
        children: [
          _summaryRow("Subtotal", "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(cart.subtotal.toInt())}"),
          const SizedBox(height: 12),
          _summaryRow("Delivery Fee", "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(cart.deliveryFee.toInt())}"),
          const SizedBox(height: 12),
          if (cart.discountAmount > 0) ...[
            _summaryRow("Discount", "-${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(cart.discountAmount.toInt())}", color: Colors.green),
            const SizedBox(height: 12),
          ],
          const Divider(),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text(
                "Total Amount",
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              Text.rich(
                TextSpan(
                  children: [
                    TextSpan(
                      text: "${AppStrings.currency.tr()} ",
                      style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: settings.primaryColor),
                    ),
                    TextSpan(
                      text: NumberFormat('#,##,###').format(cart.totalAmount.toInt()),
                      style: TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.w900,
                        color: settings.primaryColor,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _summaryRow(String label, String value, {Color? color}) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(label, style: const TextStyle(color: Colors.grey, fontSize: 15)),
        Text(
          value,
          style: TextStyle(
            fontWeight: FontWeight.bold,
            fontSize: 15,
            color: color,
          ),
        ),
      ],
    );
  }

  Widget _buildCheckoutButton(BuildContext context, CartViewModel cart, SettingsViewModel settings) {
    return Container(
      padding: const EdgeInsets.fromLTRB(20, 10, 20, 20),
      decoration: BoxDecoration(
        color: Theme.of(context).scaffoldBackgroundColor,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 10,
            offset: const Offset(0, -5),
          )
        ],
      ),
      child: SafeArea(
        child: SizedBox(
          width: double.infinity,
          height: 60,
          child: ElevatedButton(
            onPressed: () => _handleCheckout(context, cart),
            style: ElevatedButton.styleFrom(
              backgroundColor: settings.primaryColor,
              foregroundColor: Colors.white,
              elevation: 8,
              shadowColor: settings.primaryColor.withValues(alpha: 0.4),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(20),
              ),
            ),
            child: Text(
              AppStrings.checkout.tr().toUpperCase(),
              style: const TextStyle(
                fontSize: 18, 
                fontWeight: FontWeight.w900,
                letterSpacing: 1.2
              ),
            ),
          ),
        ),
      ),
    );
  }

  void _handleCheckout(BuildContext context, CartViewModel cart) async {
    final auth = context.read<AuthViewModel>();
    if (auth.user == null) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Please login to order")));
      return;
    }

    final newOrder = OrderModel(
      id: '',
      userId: auth.user!.uid,
      userName: auth.user!.name,
      userPhone: auth.user!.phoneNumber,
      userAddress: auth.user!.address,
      items: cart.items.values.toList(),
      totalAmount: cart.totalAmount,
      deliveryFee: cart.deliveryFee,
      date: DateTime.now(),
      status: 'Pending',
    );

    final loading = context.read<LoadingViewModel>();
    loading.show(message: "Placing your order...");

    bool success = await context.read<OrderViewModel>().placeOrder(newOrder);
    
    loading.hide();
    if (success) {
      cart.clearCart();
      if (context.mounted) {
        showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            title: const Text("Order Successful!"),
            content: const Text("Thank you for shopping with us. Your order has been placed successfully."),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(ctx);
                  Navigator.pop(context);
                },
                child: const Text("OK"),
              )
            ],
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
