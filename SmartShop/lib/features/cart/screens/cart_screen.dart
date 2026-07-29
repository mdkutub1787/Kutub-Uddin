import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../models/cart_model.dart';
import '../../user/models/user_model.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../order/models/order_model.dart';
import '../../../models/coupon_model.dart';
import '../../../core/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/empty_state_widget.dart';
import '../../../widgets/product_list_item.dart';
import 'package:intl/intl.dart';
import '../../../widgets/loading_overlay.dart';
import 'package:geolocator/geolocator.dart';
import 'package:supabase_flutter/supabase_flutter.dart';

class CartScreen extends ConsumerStatefulWidget {
  const CartScreen({super.key});

  @override
  ConsumerState<CartScreen> createState() => _CartScreenState();
}

class _CartScreenState extends ConsumerState<CartScreen> {
  final TextEditingController _couponController = TextEditingController();
  String _paymentMethod = 'COD';

  @override
  Widget build(BuildContext context) {
    final cart = ref.watch(cartNotifierProvider);
    final cartItems = cart.items;
    final settings = ref.watch(settingsProvider);
    final currency = settings.currencySymbol;
    
    final int cartItemCount = cartItems.fold(0, (sum, item) => sum + item.quantity);
    final double totalOriginalPrice = cartItems.fold(0.0, (sum, item) => sum + (item.product.originalPrice) * item.quantity);
    final double totalProductDiscount = totalOriginalPrice - cart.subtotal;

    final auth = ref.watch(authNotifierProvider).value;

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: CustomAppBar(title: AppStrings.myCart.tr()),
      body: Stack(
        children: [
          if (cartItems.isNotEmpty)
            Column(
              children: [
                _buildProgressStepper(settings),
                Expanded(
                  child: SingleChildScrollView(
                    child: Column(
                      children: [
                        _buildAddressSection(context, auth, settings),
                        _buildDeliverySection(context, cart, settings, currency),
                        const SizedBox(height: 10),
                        _buildItemsList(context, cartItems, settings, currency),
                        const SizedBox(height: 10),
                        _buildCouponSection(context, cart.appliedCoupon, settings),
                        const SizedBox(height: 10),
                        _buildPaymentSection(context, settings),
                        _buildOrderSummary(context, cartItemCount, totalOriginalPrice, totalProductDiscount, cart, settings, currency),
                        const SizedBox(height: 140),
                      ],
                    ),
                  ),
                ),
              ],
            )
          else
            EmptyStateWidget(
              icon: Icons.shopping_cart_outlined,
              title: AppStrings.cartEmpty.tr(),
              subtitle: AppStrings.cartEmptySubtitle.tr(),
              actionText: AppStrings.browseProducts.tr(),
              onAction: () => ref.read(navigationNotifierProvider.notifier).setIndex(0),
            ),
          
          if (cartItems.isNotEmpty)
            Positioned(
              bottom: 0, left: 0, right: 0,
              child: _buildFloatingCheckoutDock(context, cartItems, cart.totalAmount, cart.deliveryFee, settings, currency),
            ),
        ],
      ),
    );
  }

  Widget _buildProgressStepper(dynamic settings) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 24),
      decoration: BoxDecoration(color: Colors.white, boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.02), blurRadius: 10, offset: const Offset(0, 2))]),
      child: Row(
        children: [
          _stepItem("Cart", Icons.shopping_cart_rounded, true, settings),
          _stepLine(true, settings),
          _stepItem("Address", Icons.location_on_rounded, true, settings),
          _stepLine(false, settings),
          _stepItem("Payment", Icons.account_balance_wallet_rounded, false, settings),
        ],
      ),
    );
  }

  Widget _stepItem(String label, IconData icon, bool isActive, dynamic settings) {
    return Column(
      children: [
        Container(
          padding: const EdgeInsets.all(8),
          decoration: BoxDecoration(color: isActive ? settings.primaryColor : Colors.grey[200], shape: BoxShape.circle),
          child: Icon(icon, color: isActive ? Colors.white : Colors.grey, size: 16),
        ),
        const SizedBox(height: 4),
        Text(label, style: TextStyle(fontSize: 10, fontWeight: isActive ? FontWeight.bold : FontWeight.normal, color: isActive ? settings.primaryColor : Colors.grey)),
      ],
    );
  }

  Widget _stepLine(bool isActive, dynamic settings) {
    return Expanded(child: Container(height: 2, margin: const EdgeInsets.only(left: 8, right: 8, bottom: 15), color: isActive ? settings.primaryColor : Colors.grey[200]));
  }

  Widget _buildFloatingCheckoutDock(BuildContext context, List<CartItem> cartItems, double totalAmount, double deliveryFee, dynamic settings, String currency) {
    return Container(
      padding: const EdgeInsets.fromLTRB(20, 16, 20, 32),
      decoration: BoxDecoration(color: Colors.white, borderRadius: const BorderRadius.vertical(top: Radius.circular(30)), boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.1), blurRadius: 20, offset: const Offset(0, -5))]),
      child: Row(
        children: [
          Expanded(
            flex: 2,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text("Total Payable", style: TextStyle(color: Colors.grey, fontSize: 12, fontWeight: FontWeight.w600)),
                const SizedBox(height: 4),
                Text("$currency${NumberFormat('#,##,###').format(totalAmount.toInt())}", style: TextStyle(fontSize: 22, fontWeight: FontWeight.w900, color: settings.primaryColor)),
              ],
            ),
          ),
          Expanded(
            flex: 3,
            child: SizedBox(
              height: 55,
              child: ElevatedButton(
                onPressed: () => _handleCheckout(context, cartItems, totalAmount, deliveryFee),
                style: ElevatedButton.styleFrom(backgroundColor: settings.primaryColor, foregroundColor: Colors.white, elevation: 5, shadowColor: settings.primaryColor.withValues(alpha: 0.3), shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18))),
                child: const Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text("CHECKOUT", style: TextStyle(fontSize: 16, fontWeight: FontWeight.w900, letterSpacing: 1.2)),
                    SizedBox(width: 8),
                    Icon(Icons.arrow_forward_rounded, size: 20),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildPaymentSection(BuildContext context, dynamic settings) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(24), border: Border.all(color: Colors.grey.withValues(alpha: 0.1)), boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.02), blurRadius: 15)]),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Row(
            children: [
              Icon(Icons.payment_rounded, color: Colors.blueAccent, size: 20),
              SizedBox(width: 8),
              Text("Payment Method", style: TextStyle(fontWeight: FontWeight.w900, fontSize: 16)),
            ],
          ),
          const SizedBox(height: 15),
          _paymentOption(title: "Cash on Delivery", subtitle: "Pay when you receive the items", icon: Icons.money_rounded, value: 'COD', settings: settings),
          const SizedBox(height: 10),
          _paymentOption(title: "Online Payment", subtitle: "Pay via bKash, Nagad or Cards", icon: Icons.account_balance_wallet_rounded, value: 'Online', settings: settings),
        ],
      ),
    );
  }

  Widget _paymentOption({required String title, required String subtitle, required IconData icon, required String value, required dynamic settings}) {
    bool isSelected = _paymentMethod == value;
    return InkWell(
      onTap: () => setState(() => _paymentMethod = value),
      borderRadius: BorderRadius.circular(16),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 250),
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(borderRadius: BorderRadius.circular(16), border: Border.all(color: isSelected ? settings.primaryColor : Colors.grey.withValues(alpha: 0.1), width: 2), color: isSelected ? settings.primaryColor.withValues(alpha: 0.03) : Colors.transparent),
        child: Row(
          children: [
            Icon(icon, color: isSelected ? settings.primaryColor : Colors.grey),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(title, style: TextStyle(fontWeight: FontWeight.bold, color: isSelected ? Colors.black : Colors.grey[700])),
                  Text(subtitle, style: const TextStyle(fontSize: 12, color: Colors.grey)),
                ],
              ),
            ),
            if (isSelected) Icon(Icons.check_circle_rounded, color: settings.primaryColor),
          ],
        ),
      ),
    );
  }

  Widget _buildDeliverySection(BuildContext context, CartState cart, dynamic settings, String currency) {
    final arrivalDate = DateFormat('dd MMM').format(DateTime.now().add(Duration(days: cart.deliveryMethod == 'express' ? 2 : 5)));
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(24), border: Border.all(color: Colors.grey.withValues(alpha: 0.1))),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(AppStrings.deliveryArea.tr(), style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16)),
              Container(padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4), decoration: BoxDecoration(color: Colors.orange.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(8)), child: Text("Arrives by $arrivalDate", style: const TextStyle(color: Colors.orange, fontSize: 11, fontWeight: FontWeight.bold))),
            ],
          ),
          const SizedBox(height: 12),
          Row(
            children: [
              Expanded(child: _selectableChip(label: AppStrings.insideDhaka.tr(), isSelected: cart.isInsideDhaka, onTap: () => ref.read(cartNotifierProvider.notifier).setInsideDhaka(true), settings: settings)),
              const SizedBox(width: 10),
              Expanded(child: _selectableChip(label: AppStrings.outsideDhaka.tr(), isSelected: !cart.isInsideDhaka, onTap: () => ref.read(cartNotifierProvider.notifier).setInsideDhaka(false), settings: settings)),
            ],
          ),
          const SizedBox(height: 20),
          Text(AppStrings.deliveryMethod.tr(), style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 15, color: Colors.grey)),
          const SizedBox(height: 12),
          _deliveryMethodCard(context, method: 'standard', title: AppStrings.standardDelivery.tr(), subtitle: "3-5 Business Days", price: cart.isInsideDhaka ? "60" : "150", currentMethod: cart.deliveryMethod, settings: settings, currency: currency),
          const SizedBox(height: 10),
          _deliveryMethodCard(context, method: 'express', title: AppStrings.expressDelivery.tr(), subtitle: "1-2 Business Days", price: cart.isInsideDhaka ? "100" : "250", currentMethod: cart.deliveryMethod, settings: settings, currency: currency),
        ],
      ),
    );
  }

  Widget _selectableChip({required String label, required bool isSelected, required VoidCallback onTap, required dynamic settings}) {
    return InkWell(onTap: onTap, borderRadius: BorderRadius.circular(12), child: AnimatedContainer(duration: const Duration(milliseconds: 200), padding: const EdgeInsets.symmetric(vertical: 12), alignment: Alignment.center, decoration: BoxDecoration(color: isSelected ? settings.primaryColor : Colors.grey[100], borderRadius: BorderRadius.circular(12)), child: Text(label, style: TextStyle(color: isSelected ? Colors.white : Colors.black87, fontWeight: isSelected ? FontWeight.bold : FontWeight.w500))));
  }

  Widget _deliveryMethodCard(BuildContext context, {required String method, required String title, required String subtitle, required String price, required String currentMethod, required dynamic settings, required String currency}) {
    bool isSelected = currentMethod == method;
    return InkWell(
      onTap: () => ref.read(cartNotifierProvider.notifier).setDeliveryMethod(method),
      borderRadius: BorderRadius.circular(16),
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(borderRadius: BorderRadius.circular(16), border: Border.all(color: isSelected ? settings.primaryColor : Colors.grey.withValues(alpha: 0.1), width: 2), color: isSelected ? settings.primaryColor.withValues(alpha: 0.05) : Colors.transparent),
        child: Row(
          children: [
            Icon(method == 'express' ? Icons.bolt_rounded : Icons.local_shipping_rounded, color: isSelected ? settings.primaryColor : Colors.grey),
            const SizedBox(width: 16),
            Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Text(title, style: const TextStyle(fontWeight: FontWeight.bold)), Text(subtitle, style: const TextStyle(fontSize: 12, color: Colors.grey))])),
            Text("$currency$price", style: TextStyle(fontWeight: FontWeight.w900, fontSize: 16, color: isSelected ? settings.primaryColor : Colors.black87)),
          ],
        ),
      ),
    );
  }

  Widget _buildAddressSection(BuildContext context, dynamic auth, dynamic settings) {
    return Container(
      margin: const EdgeInsets.all(12),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(gradient: LinearGradient(colors: [settings.primaryColor, settings.primaryColor.withValues(alpha: 0.8)]), borderRadius: BorderRadius.circular(24), boxShadow: [BoxShadow(color: settings.primaryColor.withValues(alpha: 0.3), blurRadius: 10, offset: const Offset(0, 5))]),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Row(children: [Icon(Icons.location_on_rounded, color: Colors.white, size: 20), SizedBox(width: 8), Text("Shipping Address", style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16))]),
              GestureDetector(onTap: () => Navigator.pushNamed(context, AppRoutes.editProfile), child: Container(padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6), decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.2), borderRadius: BorderRadius.circular(10)), child: const Text("Change", style: TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold)))),
            ],
          ),
          const SizedBox(height: 15),
          Text(auth?.address ?? "Please set your address in profile", style: const TextStyle(color: Colors.white, fontSize: 14)),
        ],
      ),
    );
  }

  Widget _buildItemsList(BuildContext context, List<CartItem> items, dynamic settings, String currency) {
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
            decoration: BoxDecoration(color: Colors.grey[100], borderRadius: BorderRadius.circular(10)),
            child: Row(
              children: [
                _qtyBtn(Icons.remove, () => ref.read(cartNotifierProvider.notifier).updateQuantity(item.product.id, item.quantity - 1)),
                Padding(padding: const EdgeInsets.symmetric(horizontal: 8), child: Text("${item.quantity}", style: const TextStyle(fontWeight: FontWeight.bold))),
                _qtyBtn(Icons.add, () => ref.read(cartNotifierProvider.notifier).addToCart(item.product)),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget _qtyBtn(IconData icon, VoidCallback onTap) {
    return GestureDetector(onTap: onTap, child: Container(padding: const EdgeInsets.all(6), child: Icon(icon, size: 18)));
  }

  Widget _buildCouponSection(BuildContext context, CouponModel? appliedCoupon, dynamic settings) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 12),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(24), border: Border.all(color: Colors.grey.withValues(alpha: 0.1))),
      child: Column(
        children: [
          Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [const Text("Promo Code", style: TextStyle(fontWeight: FontWeight.bold)), TextButton(onPressed: () => Navigator.pushNamed(context, AppRoutes.offers), child: Text("View Offers", style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold)))]),
          TextField(
            controller: _couponController,
            decoration: InputDecoration(
              hintText: "Enter code...",
              suffixIcon: TextButton(
                onPressed: () async {
                  if (_couponController.text.isEmpty) return;
                  String res = await ref.read(cartNotifierProvider.notifier).applyCoupon(_couponController.text.trim());
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(res == 'Success' ? "Coupon Applied!" : res), backgroundColor: res == 'Success' ? Colors.green : Colors.red));
                },
                child: const Text("Apply"),
              ),
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(15), borderSide: BorderSide.none),
              filled: true,
              fillColor: Colors.grey[100],
            ),
          ),
          if (appliedCoupon != null) ...[const SizedBox(height: 10), Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [Text("Applied: '${appliedCoupon.code}'", style: const TextStyle(color: Colors.green, fontWeight: FontWeight.bold)), IconButton(icon: const Icon(Icons.cancel, color: Colors.red, size: 18), onPressed: () => ref.read(cartNotifierProvider.notifier).removeCoupon())])],
        ],
      ),
    );
  }

  Widget _buildOrderSummary(BuildContext context, int cartItemCount, double totalOriginalPrice, double totalProductDiscount, CartState cart, dynamic settings, String currency) {
    final bool isPlural = cartItemCount > 1;
    return Container(
      margin: const EdgeInsets.all(12),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(24), border: Border.all(color: Colors.grey.withValues(alpha: 0.1))),
      child: Column(
        children: [
          _summaryRow("Price ($cartItemCount ${isPlural ? "items" : "item"})", "$currency${totalOriginalPrice.toInt()}"),
          const SizedBox(height: 12),
          if (totalProductDiscount > 0) ...[_summaryRow("Product Discount", "-$currency${totalProductDiscount.toInt()}", color: Colors.green), const SizedBox(height: 12)],
          _summaryRow("Subtotal", "$currency${cart.subtotal.toInt()}"),
          const SizedBox(height: 12),
          _summaryRow("Delivery Fee", "$currency${cart.deliveryFee.toInt()}"),
          const SizedBox(height: 12),
          if (cart.couponDiscount > 0) ...[_summaryRow("Coupon Discount", "-$currency${cart.couponDiscount.toInt()}", color: Colors.green), const SizedBox(height: 12)],
          const Divider(),
          const SizedBox(height: 8),
          Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [const Text("Payable Amount", style: TextStyle(fontSize: 18, fontWeight: FontWeight.w900)), Text("$currency${cart.totalAmount.toInt()}", style: TextStyle(fontSize: 22, fontWeight: FontWeight.w900, color: settings.primaryColor))]),
        ],
      ),
    );
  }

  Widget _summaryRow(String label, String value, {Color? color}) {
    return Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [Text(label, style: const TextStyle(color: Colors.grey)), Text(value, style: TextStyle(fontWeight: FontWeight.bold, color: color))]);
  }

  void _handleCheckout(BuildContext context, List<CartItem> cartItems, double totalAmount, double deliveryFee) async {
    final auth = ref.read(authNotifierProvider).value;
    if (auth == null) { ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.pleaseLogin.tr()))); return; }
    if (_paymentMethod == 'Online') { _showOnlinePaymentDummy(context, cartItems, totalAmount, deliveryFee, auth); return; }
    _processOrder(context, cartItems, totalAmount, deliveryFee, auth, 'COD', false);
  }

  void _showOnlinePaymentDummy(BuildContext context, List<CartItem> cartItems, double totalAmount, double deliveryFee, UserModel auth) {
    String selectedProvider = 'bKash';
    String cardType = 'Visa';
    final numberController = TextEditingController(text: auth.phoneNumber);
    final cardNoController = TextEditingController();
    final expiryController = TextEditingController();
    final cvvController = TextEditingController();

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (ctx) => StatefulBuilder(
        builder: (context, setModalState) => Container(
          padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom, left: 20, right: 20, top: 20),
          decoration: const BoxDecoration(color: Colors.white, borderRadius: BorderRadius.vertical(top: Radius.circular(30))),
          child: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Container(width: 50, height: 5, decoration: BoxDecoration(color: Colors.grey[300], borderRadius: BorderRadius.circular(10))),
                const SizedBox(height: 20),
                const Text("Secure Checkout", style: TextStyle(fontSize: 22, fontWeight: FontWeight.w900)),
                const SizedBox(height: 25),
                SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      _paymentProviderTab('bKash', Icons.account_balance_wallet_rounded, Colors.pink, selectedProvider, (p) => setModalState(() => selectedProvider = p)),
                      const SizedBox(width: 15),
                      _paymentProviderTab('Nagad', Icons.wallet_giftcard_rounded, Colors.orange, selectedProvider, (p) => setModalState(() => selectedProvider = p)),
                      const SizedBox(width: 15),
                      _paymentProviderTab('Rocket', Icons.rocket_launch_rounded, Colors.deepPurple, selectedProvider, (p) => setModalState(() => selectedProvider = p)),
                      const SizedBox(width: 15),
                      _paymentProviderTab('Card', Icons.credit_card_rounded, Colors.blue, selectedProvider, (p) => setModalState(() => selectedProvider = p)),
                    ],
                  ),
                ),
                const SizedBox(height: 30),
                if (selectedProvider == 'Card') ...[
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      _cardTypeChip('Visa', 'assets/images/visa.png', cardType, (t) => setModalState(() => cardType = t)),
                      _cardTypeChip('MasterCard', 'assets/images/mastercard.png', cardType, (t) => setModalState(() => cardType = t)),
                      _cardTypeChip('Amex', 'assets/images/amex.png', cardType, (t) => setModalState(() => cardType = t)),
                    ],
                  ),
                  const SizedBox(height: 20),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text("Card Information", style: TextStyle(fontWeight: FontWeight.bold)),
                    TextButton.icon(
                      onPressed: () {
                        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Launching Camera for Card OCR...")));
                      },
                      icon: const Icon(Icons.camera_enhance_rounded, size: 18),
                      label: const Text("Scan Card", style: TextStyle(fontWeight: FontWeight.bold)),
                    ),
                  ],
                ),
                _payTextField(cardNoController, "Card Number", Icons.credit_card, "XXXX XXXX XXXX XXXX"),
                  const SizedBox(height: 15),
                  Row(
                    children: [
                      Expanded(child: _payTextField(expiryController, "Expiry", Icons.calendar_month, "MM/YY")),
                      const SizedBox(width: 15),
                      Expanded(child: _payTextField(cvvController, "CVV", Icons.lock, "XXX")),
                    ],
                  ),
                ] else ...[
                  _payTextField(numberController, "$selectedProvider Number", Icons.phone_android_rounded, "01XXXXXXXXX"),
                ],
                const SizedBox(height: 25),
                Container(
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(color: Colors.blue.withValues(alpha: 0.05), borderRadius: BorderRadius.circular(15), border: Border.all(color: Colors.blue.withValues(alpha: 0.1))),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      const Text("Payable Total:", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                      Text("৳${totalAmount.toInt()}", style: const TextStyle(fontSize: 24, fontWeight: FontWeight.w900, color: Colors.blue)),
                    ],
                  ),
                ),
                const SizedBox(height: 30),
                SizedBox(
                  width: double.infinity,
                  height: 60,
                  child: ElevatedButton(
                    onPressed: () {
                      if (selectedProvider == 'Card') {
                         if (cardNoController.text.length < 12) { ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Enter valid card details"))); return; }
                      } else {
                         if (numberController.text.length < 11) { ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Enter valid wallet number"))); return; }
                      }
                      Navigator.pop(ctx);
                      _processOrder(context, cartItems, totalAmount, deliveryFee, auth, 'Online', true, transactionId: 'TRX${DateTime.now().millisecondsSinceEpoch}');
                    },
                    style: ElevatedButton.styleFrom(backgroundColor: Colors.blue[700], foregroundColor: Colors.white, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)), elevation: 4),
                    child: Text("PAY NOW WITH ${selectedProvider == 'Card' ? cardType : selectedProvider}".toUpperCase(), style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16)),
                  ),
                ),
                const SizedBox(height: 30),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _cardTypeChip(String type, String asset, String selected, Function(String) onTap) {
    bool isSelected = selected == type;
    return GestureDetector(
      onTap: () => onTap(type),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        decoration: BoxDecoration(
          color: isSelected ? Colors.blue.withValues(alpha: 0.1) : Colors.grey[50],
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: isSelected ? Colors.blue : Colors.grey[300]!),
        ),
        child: Text(type, style: TextStyle(fontWeight: isSelected ? FontWeight.bold : FontWeight.normal, color: isSelected ? Colors.blue : Colors.black54)),
      ),
    );
  }

  Widget _payTextField(TextEditingController ctrl, String label, IconData icon, String hint) {
    return TextField(
      controller: ctrl,
      decoration: InputDecoration(
        labelText: label, hintText: hint,
        prefixIcon: Icon(icon),
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
        filled: true, fillColor: Colors.grey[50],
      ),
    );
  }

  Widget _paymentProviderTab(String name, IconData icon, Color color, String selected, Function(String) onTap) {
    bool isSelected = selected == name;
    return GestureDetector(
      onTap: () => onTap(name),
      child: Column(
        children: [
          AnimatedContainer(
            duration: const Duration(milliseconds: 200),
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: isSelected ? color.withValues(alpha: 0.1) : Colors.grey[100],
              shape: BoxShape.circle,
              border: Border.all(color: isSelected ? color : Colors.transparent, width: 2),
            ),
            child: Icon(icon, color: isSelected ? color : Colors.grey, size: 28),
          ),
          const SizedBox(height: 8),
          Text(name, style: TextStyle(fontSize: 12, fontWeight: isSelected ? FontWeight.bold : FontWeight.normal, color: isSelected ? color : Colors.black)),
        ],
      ),
    );
  }

  void _processOrder(BuildContext context, List<CartItem> cartItems, double totalAmount, double deliveryFee, UserModel auth, String method, bool isPaid, {String? transactionId}) async {
    LoadingOverlay.show(context);
    double? lat; double? lng;
    try {
      Position pos = await Geolocator.getCurrentPosition(desiredAccuracy: LocationAccuracy.high).timeout(const Duration(seconds: 3));
      lat = pos.latitude; lng = pos.longitude;
    } catch (e) { debugPrint("Location fetch failed or timed out: $e"); }

    final shopId = cartItems.isNotEmpty ? cartItems.first.product.shopId : '';
    String? shopName; String? shopAddress;
    try {
      if (shopId.isNotEmpty) {
        final res = await ref.read(supabaseClientProvider).from(AppConstants.shopsTable).select().eq('id', shopId).maybeSingle();
        if (res != null) { shopName = res['name']; shopAddress = res['address']; }
      }
    } catch (e) { }

    final newOrder = OrderModel(id: '', shopId: shopId, userId: auth.uid, userName: auth.name, userPhone: auth.phoneNumber, userAddress: auth.address, items: cartItems, totalAmount: totalAmount, deliveryFee: deliveryFee, date: DateTime.now(), status: 'Pending', paymentMethod: method, isPaid: isPaid, transactionId: transactionId, customerLatitude: lat, customerLongitude: lng, shopName: shopName, shopAddress: shopAddress);
    
    bool success = await ref.read(orderNotifierProvider.notifier).placeOrder(newOrder);
    if (context.mounted) LoadingOverlay.hide(context);
    
    if (success) {
      ref.read(cartNotifierProvider.notifier).clearCart();
      if (context.mounted) {
        ref.read(navigationNotifierProvider.notifier).setIndex(1);
        showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(25)),
            title: const Text("Order Successful"),
            content: Text(method == 'Online' ? "Payment received and order placed!" : "Your order has been placed successfully."),
            actions: [TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("OK"))],
          ),
        );
      }
    } else {
      if (context.mounted) ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Order failed.")));
    }
  }
}

enum DeliveryMethod { standard, express }
