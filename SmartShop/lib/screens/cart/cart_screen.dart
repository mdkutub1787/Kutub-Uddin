import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../view_models/cart_view_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/navigation_view_model.dart';
import '../../models/order_model.dart';
import '../../models/coupon_model.dart';
import '../../utils/constants/app_strings.dart';
import '../../routes/app_routes.dart';
import '../../widgets/custom_app_bar.dart';
import '../../widgets/empty_state_widget.dart';
import '../../widgets/product_list_item.dart';
import 'package:intl/intl.dart';

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
                  subtitle: AppStrings.cartEmptySubtitle.tr(),
                  actionText: AppStrings.browseProducts.tr(),
                  onAction: () => context.read<NavigationViewModel>().setIndex(0),
                )
              : SingleChildScrollView(
                  child: Column(
                    children: [
                      _buildAddressSection(context, auth, settings),
                      _buildDeliverySection(context, cart, settings),
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

  Widget _buildDeliverySection(BuildContext context, CartViewModel cart, SettingsViewModel settings) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.02),
            blurRadius: 15,
            offset: const Offset(0, 5),
          )
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(Icons.local_shipping_outlined, color: settings.primaryColor, size: 20),
              const SizedBox(width: 8),
              Text(
                AppStrings.deliveryArea.tr(),
                style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16),
              ),
            ],
          ),
          const SizedBox(height: 12),
          Row(
            children: [
              Expanded(
                child: _selectableChip(
                  label: AppStrings.insideDhaka.tr(),
                  isSelected: cart.isInsideDhaka,
                  onTap: () => cart.setInsideDhaka(true),
                  settings: settings,
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: _selectableChip(
                  label: AppStrings.outsideDhaka.tr(),
                  isSelected: !cart.isInsideDhaka,
                  onTap: () => cart.setInsideDhaka(false),
                  settings: settings,
                ),
              ),
            ],
          ),
          const SizedBox(height: 20),
          Text(
            AppStrings.deliveryMethod.tr(),
            style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16),
          ),
          const SizedBox(height: 12),
          _deliveryMethodCard(
            context,
            method: DeliveryMethod.standard,
            title: AppStrings.standardDelivery.tr(),
            subtitle: "3-5 Business Days",
            price: cart.isInsideDhaka ? "60" : "150",
            cart: cart,
            settings: settings,
          ),
          const SizedBox(height: 10),
          _deliveryMethodCard(
            context,
            method: DeliveryMethod.express,
            title: AppStrings.expressDelivery.tr(),
            subtitle: "1-2 Business Days",
            price: cart.isInsideDhaka ? "100" : "250",
            cart: cart,
            settings: settings,
          ),
        ],
      ),
    );
  }

  Widget _selectableChip({required String label, required bool isSelected, required VoidCallback onTap, required SettingsViewModel settings}) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(12),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        padding: const EdgeInsets.symmetric(vertical: 12),
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: isSelected ? settings.primaryColor : Colors.grey[100],
          borderRadius: BorderRadius.circular(12),
        ),
        child: Text(
          label,
          style: TextStyle(
            color: isSelected ? Colors.white : Colors.black87,
            fontWeight: isSelected ? FontWeight.bold : FontWeight.w500,
          ),
        ),
      ),
    );
  }

  Widget _deliveryMethodCard(BuildContext context, {required DeliveryMethod method, required String title, required String subtitle, required String price, required CartViewModel cart, required SettingsViewModel settings}) {
    bool isSelected = cart.deliveryMethod == method;
    return InkWell(
      onTap: () => cart.setDeliveryMethod(method),
      borderRadius: BorderRadius.circular(16),
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          border: Border.all(
            color: isSelected ? settings.primaryColor : Colors.grey.withValues(alpha: 0.2),
            width: 2,
          ),
          color: isSelected ? settings.primaryColor.withValues(alpha: 0.05) : Colors.transparent,
        ),
        child: Row(
          children: [
            Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: isSelected ? settings.primaryColor : Colors.grey[100],
                shape: BoxShape.circle,
              ),
              child: Icon(
                method == DeliveryMethod.express ? Icons.bolt_rounded : Icons.local_shipping_rounded,
                color: isSelected ? Colors.white : Colors.grey[600],
                size: 20,
              ),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(title, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15)),
                  Text(subtitle, style: TextStyle(color: Colors.grey[600], fontSize: 12)),
                ],
              ),
            ),
            Text(
              "৳$price",
              style: TextStyle(
                fontWeight: FontWeight.w900,
                fontSize: 16,
                color: isSelected ? settings.primaryColor : Colors.black87,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildAddressSection(BuildContext context, AuthViewModel auth, SettingsViewModel settings) {
    return Container(
      margin: const EdgeInsets.fromLTRB(12, 12, 12, 6),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
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
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
              ),
              TextButton(
                onPressed: () => Navigator.pushNamed(context, AppRoutes.editProfile),
                style: TextButton.styleFrom(padding: EdgeInsets.zero, minimumSize: const Size(50, 30), tapTargetSize: MaterialTapTargetSize.shrinkWrap),
                child: Text(AppStrings.change.tr(), style: const TextStyle(fontSize: 13)),
              )
            ],
          ),
          const SizedBox(height: 6),
          Row(
            children: [
              Icon(Icons.location_on_rounded, color: settings.primaryColor, size: 18),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  auth.user?.address ?? AppStrings.noAddress.tr(),
                  style: TextStyle(color: Colors.grey[800], fontSize: 13),
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
      padding: const EdgeInsets.symmetric(horizontal: 12),
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
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(AppStrings.haveCoupon.tr(), style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.black87)),
              TextButton(
                onPressed: () => Navigator.pushNamed(context, AppRoutes.offers),
                child: Text(AppStrings.viewOffers.tr(), style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold)),
              ),
            ],
          ),
        ),
        Container(
          margin: const EdgeInsets.symmetric(horizontal: 12),
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
          decoration: BoxDecoration(
            color: Theme.of(context).cardColor,
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
                  decoration: InputDecoration(
                    hintText: AppStrings.enterCoupon.tr(),
                    border: InputBorder.none,
                  ),
                ),
              ),
              TextButton(
                onPressed: () {
                  if (_couponController.text.isEmpty) return;
                  try {
                    String result = cart.applyCoupon(_couponController.text);
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        content: Text(result == 'Success' ? AppStrings.couponApplied.tr() : result),
                        backgroundColor: result == 'Success' ? Colors.green : Colors.red,
                      ),
                    );
                    if (result == 'Success') _couponController.clear();
                  } catch (e) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text(AppStrings.invalidCoupon.tr()), backgroundColor: Colors.red),
                    );
                  }
                },
                child: Text(AppStrings.apply.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
              ),
            ],
          ),
        ),
        if (cart.appliedCoupon != null)
          Container(
            margin: const EdgeInsets.fromLTRB(16, 8, 16, 0),
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            decoration: BoxDecoration(
              color: Colors.green.withValues(alpha: 0.1),
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: Colors.green.withValues(alpha: 0.2)),
            ),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Icon(Icons.check_circle_outline, color: Colors.green, size: 16),
                const SizedBox(width: 8),
                Text(
                  "${AppStrings.couponApplied.tr()} '${cart.appliedCoupon!.code}'",
                  style: const TextStyle(color: Colors.green, fontWeight: FontWeight.bold, fontSize: 12),
                ),
                const SizedBox(width: 8),
                GestureDetector(
                  onTap: () => cart.removeCoupon(),
                  child: const Icon(Icons.cancel, color: Colors.green, size: 16),
                ),
              ],
            ),
          ),
      ],
    );
  }

  Widget _buildOrderSummary(BuildContext context, CartViewModel cart, SettingsViewModel settings) {
    final currency = AppStrings.currency.tr();
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 15),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
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
          _summaryRow(AppStrings.priceItems.tr(args: [cart.itemCount.toString()]), "$currency ${NumberFormat('#,##,###').format(cart.totalOriginalPrice.toInt())}"),
          const SizedBox(height: 12),
          if (cart.totalProductDiscount > 0) ...[
            _summaryRow(AppStrings.productDiscount.tr(), "-$currency ${NumberFormat('#,##,###').format(cart.totalProductDiscount.toInt())}", color: Colors.green),
            const SizedBox(height: 12),
          ],
          _summaryRow(AppStrings.subtotal.tr(), "$currency ${NumberFormat('#,##,###').format(cart.subtotal.toInt())}"),
          const SizedBox(height: 12),
          _summaryRow(AppStrings.deliveryFee.tr(), "$currency ${NumberFormat('#,##,###').format(cart.deliveryFee.toInt())}"),
          const SizedBox(height: 12),
          if (cart.couponDiscount > 0 || cart.appliedCoupon?.type == CouponType.freeDelivery) ...[
            _summaryRow(
              "${AppStrings.couponDiscount.tr()} ${cart.appliedCouponDetails.isNotEmpty ? '(${cart.appliedCouponDetails})' : ''}", 
              cart.appliedCoupon?.type == CouponType.freeDelivery 
                  ? "FREE" 
                  : "-$currency ${NumberFormat('#,##,###').format(cart.couponDiscount.toInt())}", 
              color: Colors.green
            ),
            const SizedBox(height: 12),
          ],
          const Divider(),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                AppStrings.totalAmount.tr(),
                style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              Text.rich(
                TextSpan(
                  children: [
                    TextSpan(
                      text: "$currency ",
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
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.pleaseLogin.tr())));
      return;
    }

    final items = cart.items.values.toList();
    final shopId = items.isNotEmpty ? items.first.product.shopId : '';

    final newOrder = OrderModel(
      id: '',
      shopId: shopId,
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
    loading.show(message: AppStrings.loading.tr());

    bool success = await context.read<OrderViewModel>().placeOrder(newOrder);
    
    loading.hide();
    if (success) {
      cart.clearCart();
      if (context.mounted) {
        // Switch to "My Orders" tab (Index 1)
        context.read<NavigationViewModel>().setIndex(1);
        
        showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(25)),
            title: Row(
              children: [
                const Icon(Icons.check_circle_rounded, color: Colors.green, size: 28),
                const SizedBox(width: 12),
                Text(AppStrings.orderSuccessful.tr()),
              ],
            ),
            content: Text(AppStrings.orderSuccessMsg.tr()),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(ctx),
                child: Text(AppStrings.ok.tr()),
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
