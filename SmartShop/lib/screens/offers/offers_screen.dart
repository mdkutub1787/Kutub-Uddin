import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smart_shop/view_models/cart_view_model.dart';
import 'package:smart_shop/view_models/settings_view_model.dart';
import 'package:smart_shop/models/coupon_model.dart';
import 'package:intl/intl.dart';

class OffersScreen extends StatelessWidget {
  const OffersScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final cart = context.watch<CartViewModel>();
    final settings = context.watch<SettingsViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: const Text("Available Offers", style: TextStyle(fontWeight: FontWeight.bold)),
        centerTitle: true,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: cart.availableCoupons.length,
        itemBuilder: (context, index) {
          final coupon = cart.availableCoupons[index];
          return _buildCouponCard(context, coupon, cart, settings);
        },
      ),
    );
  }

  Widget _buildCouponCard(BuildContext context, CouponModel coupon, CartViewModel cart, SettingsViewModel settings) {
    bool isApplied = cart.appliedCoupon?.id == coupon.id;
    bool canApply = cart.subtotal >= coupon.minPurchase;

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
              // Left side - Discount Value
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
              
              // Middle - Details
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

              // Right - Apply Button
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
                        border: Border.all(color: settings.primaryColor.withValues(alpha: 0.3), style: BorderStyle.none),
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
                        ? () => cart.removeCoupon() 
                        : (canApply ? () => cart.applyCoupon(coupon.code) : null),
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
