import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../riverpod/coupon_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../models/coupon_model.dart';
import 'package:intl/intl.dart';

class OffersScreen extends ConsumerStatefulWidget {
  const OffersScreen({super.key});

  @override
  ConsumerState<OffersScreen> createState() => _OffersScreenState();
}

class _OffersScreenState extends ConsumerState<OffersScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(couponNotifierProvider.notifier).loadCoupons();
    });
  }

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    final couponState = ref.watch(couponNotifierProvider);
    final cart = ref.watch(cartNotifierProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text("Available Offers", style: TextStyle(fontWeight: FontWeight.bold)),
        centerTitle: true,
      ),
      body: couponState.when(
        data: (coupons) => coupons.isEmpty
            ? const Center(child: Text("No offers available at the moment"))
            : ListView.builder(
                padding: const EdgeInsets.all(16),
                itemCount: coupons.length,
                itemBuilder: (context, index) {
                  final coupon = coupons[index];
                  bool isApplied = cart.appliedCoupon?.code == coupon.code;
                  bool canApply = cart.subtotal >= coupon.minPurchase;
                  
                  return _buildCouponCard(context, ref, coupon, settings, isApplied, canApply);
                },
              ),
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (e, st) => Center(child: Text("Error: $e")),
      ),
    );
  }

  Widget _buildCouponCard(BuildContext context, WidgetRef ref, CouponModel coupon, dynamic settings, bool isApplied, bool canApply) {
    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 10,
            offset: const Offset(0, 5),
          )
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(20),
        child: IntrinsicHeight(
          child: Row(
            children: [
              Container(
                width: 100,
                color: settings.primaryColor.withValues(alpha: 0.1),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      coupon.type == CouponType.percentage 
                        ? "${coupon.discountValue.toInt()}%" 
                        : coupon.type == CouponType.freeDelivery ? "FREE" : "৳${coupon.discountValue.toInt()}",
                      style: TextStyle(
                        fontSize: 24, 
                        fontWeight: FontWeight.bold, 
                        color: settings.primaryColor
                      ),
                    ),
                    Text(
                      coupon.type == CouponType.percentage 
                        ? "OFF" 
                        : coupon.type == CouponType.freeDelivery ? "DELIVERY" : "CASHBACK",
                      style: TextStyle(
                        fontSize: 12, 
                        fontWeight: FontWeight.bold, 
                        color: settings.primaryColor
                      ),
                    ),
                  ],
                ),
              ),
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        coupon.title,
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        coupon.description,
                        style: TextStyle(color: Colors.grey[600], fontSize: 12),
                      ),
                      const SizedBox(height: 8),
                      if (!canApply && coupon.minPurchase > 0)
                        Text(
                          "Min. Purchase: ৳${coupon.minPurchase.toInt()}",
                          style: const TextStyle(color: Colors.orange, fontSize: 10, fontWeight: FontWeight.bold),
                        ),
                      const Spacer(),
                      Row(
                        children: [
                          Icon(Icons.timer_outlined, size: 14, color: Colors.grey[400]),
                          const SizedBox(width: 4),
                          Text(
                            "Expires: ${DateFormat('dd MMM yyyy').format(coupon.expiryDate)}",
                            style: TextStyle(color: Colors.grey[400], fontSize: 10),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
              Container(
                width: 80,
                padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 8),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        color: settings.primaryColor.withValues(alpha: 0.1),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: SelectableText(
                        coupon.code,
                        style: TextStyle(
                          fontWeight: FontWeight.bold, 
                          fontSize: 12, 
                          color: settings.primaryColor
                        ),
                      ),
                    ),
                    const SizedBox(height: 8),
                    ElevatedButton(
                      onPressed: isApplied 
                        ? () {
                            ref.read(cartNotifierProvider.notifier).removeCoupon();
                          }
                        : (canApply ? () async {
                            String result = await ref.read(cartNotifierProvider.notifier).applyCoupon(coupon.code);
                            if (result == 'Success') {
                              if (context.mounted) {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(
                                    content: Text("Coupon Applied Successfully!"),
                                    backgroundColor: Colors.green,
                                    duration: Duration(seconds: 1),
                                  ),
                                );
                                Navigator.pop(context);
                              }
                            } else {
                              if (context.mounted) {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  SnackBar(content: Text(result), backgroundColor: Colors.red),
                                );
                              }
                            }
                          } : null),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: isApplied ? Colors.red : settings.primaryColor,
                        foregroundColor: Colors.white,
                        padding: EdgeInsets.zero,
                        minimumSize: const Size(60, 30),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                      ),
                      child: Text(isApplied ? "Remove" : "Apply", style: const TextStyle(fontSize: 10)),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
