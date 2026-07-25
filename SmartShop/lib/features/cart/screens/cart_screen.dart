import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../models/cart_model.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../order/models/order_model.dart';
import '../../../models/coupon_model.dart';
import '../../../core/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/empty_state_widget.dart';
import '../../../widgets/product_list_item.dart';
import 'package:intl/intl.dart';
import '../../../widgets/loading_overlay.dart';

class CartScreen extends ConsumerStatefulWidget {
  const CartScreen({super.key});

  @override
  ConsumerState<CartScreen> createState() => _CartScreenState();
}

class _CartScreenState extends ConsumerState<CartScreen> {
  final TextEditingController _couponController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final cartItems = ref.watch(cartNotifierProvider) ?? [];
    // Dummy cart logic properties
    final int cartItemCount = cartItems.fold(0, (sum, item) => sum + item.quantity);
    final double totalOriginalPrice = cartItems.fold(0.0, (sum, item) => sum + (item.product.originalPrice ?? item.product.price) * item.quantity);
    final double subtotal = cartItems.fold(0.0, (sum, item) => sum + item.product.price * item.quantity);
    final double totalProductDiscount = totalOriginalPrice - subtotal;
    
    // Delivery info (dummy state for now, assuming standard inside dhaka)
    bool isInsideDhaka = true; 
    DeliveryMethod deliveryMethod = DeliveryMethod.standard;
    double deliveryFee = isInsideDhaka ? (deliveryMethod == DeliveryMethod.express ? 100 : 60) : (deliveryMethod == DeliveryMethod.express ? 250 : 150);
    
    // Coupon (dummy state)
    CouponModel? appliedCoupon = null;
    double couponDiscount = 0.0;
    String appliedCouponDetails = '';

    double totalAmount = subtotal + deliveryFee - couponDiscount;

    final settings = ref.watch(settingsProvider);
    final auth = ref.watch(authNotifierProvider).value;
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
          
          cartItems.isEmpty
              ? EmptyStateWidget(
                  icon: Icons.shopping_cart_outlined,
                  title: AppStrings.cartEmpty.tr(),
                  subtitle: AppStrings.cartEmptySubtitle.tr(),
                  actionText: AppStrings.browseProducts.tr(),
                  onAction: () => ref.read(navigationNotifierProvider.notifier).setIndex(0),
                )
              : SingleChildScrollView(
                  child: Column(
                    children: [
                      _buildAddressSection(context, auth, settings),
                      _buildDeliverySection(context, isInsideDhaka, deliveryMethod, settings),
                      _buildItemsList(context, cartItems, settings),
                      _buildCouponSection(context, appliedCoupon, settings),
                      _buildOrderSummary(context, cartItemCount, totalOriginalPrice, totalProductDiscount, subtotal, deliveryFee, couponDiscount, appliedCoupon, appliedCouponDetails, totalAmount, settings),
                      const SizedBox(height: 120),
                    ],
                  ),
                ),
        ],
      ),
      bottomSheet: cartItems.isEmpty ? null : _buildCheckoutButton(context, cartItems, totalAmount, deliveryFee, settings),
    );
  }

  Widget _buildDeliverySection(BuildContext context, bool isInsideDhaka, DeliveryMethod deliveryMethod, dynamic settings) {
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
                  isSelected: isInsideDhaka,
                  onTap: () {
                     // ref.read(cartNotifierProvider.notifier).setInsideDhaka(true);
                  },
                  settings: settings,
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: _selectableChip(
                  label: AppStrings.outsideDhaka.tr(),
                  isSelected: !isInsideDhaka,
                  onTap: () {
                     // ref.read(cartNotifierProvider.notifier).setInsideDhaka(false);
                  },
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
            price: isInsideDhaka ? "60" : "150",
            currentMethod: deliveryMethod,
            settings: settings,
          ),
          const SizedBox(height: 10),
          _deliveryMethodCard(
            context,
            method: DeliveryMethod.express,
            title: AppStrings.expressDelivery.tr(),
            subtitle: "1-2 Business Days",
            price: isInsideDhaka ? "100" : "250",
            currentMethod: deliveryMethod,
            settings: settings,
          ),
        ],
      ),
    );
  }

  Widget _selectableChip({required String label, required bool isSelected, required VoidCallback onTap, required dynamic settings}) {
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

  Widget _deliveryMethodCard(BuildContext context, {required DeliveryMethod method, required String title, required String subtitle, required String price, required DeliveryMethod currentMethod, required dynamic settings}) {
    bool isSelected = currentMethod == method;
    return InkWell(
      onTap: () {
         // ref.read(cartNotifierProvider.notifier).setDeliveryMethod(method);
      },
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

  Widget _buildAddressSection(BuildContext context, dynamic auth, dynamic settings) {
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
                  auth?.address ?? AppStrings.noAddress.tr(),
                  style: TextStyle(color: Colors.grey[800], fontSize: 13),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildItemsList(BuildContext context, List<dynamic> items, dynamic settings) {
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
                _qtyBtn(Icons.remove, () {
                  ref.read(cartNotifierProvider.notifier).removeFromCart(item.product.id);
                }),
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 8),
                  child: Text(
                    "${item.quantity}",
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                _qtyBtn(Icons.add, () {
                   ref.read(cartNotifierProvider.notifier).addToCart(item.product);
                }),
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

  Widget _buildCouponSection(BuildContext context, CouponModel? appliedCoupon, dynamic settings) {
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
                  // Dummy coupon logic
                },
                child: Text(AppStrings.apply.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
              ),
            ],
          ),
        ),
        if (appliedCoupon != null)
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
                  "${AppStrings.couponApplied.tr()} '${appliedCoupon.code}'",
                  style: const TextStyle(color: Colors.green, fontWeight: FontWeight.bold, fontSize: 12),
                ),
                const SizedBox(width: 8),
                GestureDetector(
                  onTap: () {
                     // ref.read(cartNotifierProvider.notifier).removeCoupon();
                  },
                  child: const Icon(Icons.cancel, color: Colors.green, size: 16),
                ),
              ],
            ),
          ),
      ],
    );
  }

  Widget _buildOrderSummary(BuildContext context, int cartItemCount, double totalOriginalPrice, double totalProductDiscount, double subtotal, double deliveryFee, double couponDiscount, CouponModel? appliedCoupon, String appliedCouponDetails, double totalAmount, dynamic settings) {
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
          _summaryRow(AppStrings.priceItems.tr(args: [cartItemCount.toString()]), "$currency ${NumberFormat('#,##,###').format(totalOriginalPrice.toInt())}"),
          const SizedBox(height: 12),
          if (totalProductDiscount > 0) ...[
            _summaryRow(AppStrings.productDiscount.tr(), "-$currency ${NumberFormat('#,##,###').format(totalProductDiscount.toInt())}", color: Colors.green),
            const SizedBox(height: 12),
          ],
          _summaryRow(AppStrings.subtotal.tr(), "$currency ${NumberFormat('#,##,###').format(subtotal.toInt())}"),
          const SizedBox(height: 12),
          _summaryRow(AppStrings.deliveryFee.tr(), "$currency ${NumberFormat('#,##,###').format(deliveryFee.toInt())}"),
          const SizedBox(height: 12),
          if (couponDiscount > 0 || appliedCoupon?.type == CouponType.freeDelivery) ...[
            _summaryRow(
              "${AppStrings.couponDiscount.tr()} ${appliedCouponDetails.isNotEmpty ? '($appliedCouponDetails)' : ''}", 
              appliedCoupon?.type == CouponType.freeDelivery 
                  ? "FREE" 
                  : "-$currency ${NumberFormat('#,##,###').format(couponDiscount.toInt())}", 
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
                      text: NumberFormat('#,##,###').format(totalAmount.toInt()),
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

  Widget _buildCheckoutButton(BuildContext context, List<dynamic> cartItems, double totalAmount, double deliveryFee, dynamic settings) {
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
            onPressed: () => _handleCheckout(context, cartItems, totalAmount, deliveryFee),
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

  void _handleCheckout(BuildContext context, List<dynamic> cartItems, double totalAmount, double deliveryFee) async {
    final auth = ref.read(authNotifierProvider).value;
    if (auth == null) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.pleaseLogin.tr())));
      return;
    }

    final shopId = cartItems.isNotEmpty ? cartItems.first.product.shopId : '';

    final newOrder = OrderModel(
      id: '',
      shopId: shopId,
      userId: auth.uid,
      userName: auth.name,
      userPhone: auth.phoneNumber ?? '',
      userAddress: auth.address ?? '',
      items: cartItems.map((item) => item as CartItem).toList(),
      totalAmount: totalAmount,
      deliveryFee: deliveryFee,
      date: DateTime.now(),
      status: 'Pending',
    );

    LoadingOverlay.show(context);
    
    // Simulate placing order
    // bool success = await ref.read(orderNotifierProvider.notifier).placeOrder(newOrder);
    bool success = true; 
    
    LoadingOverlay.hide(context);
    if (success) {
      ref.read(cartNotifierProvider.notifier).clearCart();
      if (context.mounted) {
        // Switch to "My Orders" tab (Index 1)
        ref.read(navigationNotifierProvider.notifier).setIndex(1);
        
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

// Dummy DeliveryMethod enum if not imported properly
enum DeliveryMethod { standard, express }
