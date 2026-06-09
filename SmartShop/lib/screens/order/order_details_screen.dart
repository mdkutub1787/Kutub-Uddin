import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import '../../models/order_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../view_models/order_view_model.dart';
import '../../repositories/order_repository.dart';

import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../widgets/custom_app_bar.dart';

class OrderDetailsScreen extends StatelessWidget {
  final OrderModel order;

  const OrderDetailsScreen({super.key, required this.order});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final primaryColor = settings.primaryColor;
    final size = MediaQuery.of(context).size;
    
    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: const CustomAppBar(
        title: "Order Details",
      ),
      body: Stack(
        children: [
          // Decorative Background Elements
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
          
          SingleChildScrollView(
            padding: const EdgeInsets.all(24.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _buildOrderInfoCard(context, settings),
                const SizedBox(height: 30),
                const Text(
                  "Ordered Items",
                  style: TextStyle(fontSize: 22, fontWeight: FontWeight.w900, letterSpacing: -0.5),
                ),
                const SizedBox(height: 15),
                _buildItemsList(context, settings),
                const SizedBox(height: 30),
                _buildPriceSummary(context, settings),
                const SizedBox(height: 40),
                if (order.status == 'Pending')
                  _buildCancelButton(context),
                const SizedBox(height: 50),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildOrderInfoCard(BuildContext context, SettingsViewModel settings) {
    return Card(
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(15),
        side: BorderSide(color: Colors.grey.withValues(alpha: 0.2)),
      ),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text("Status", style: TextStyle(color: Colors.grey)),
                _buildStatusBadge(order.status),
              ],
            ),
            const Divider(height: 24),
            const Text("Order ID", style: TextStyle(color: Colors.grey)),
            const SizedBox(height: 4),
            SelectableText(
              order.id,
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
            const SizedBox(height: 16),
            Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text("Date", style: TextStyle(color: Colors.grey)),
                      const SizedBox(height: 4),
                      Text(
                        DateFormat('dd MMM yyyy, hh:mm a').format(order.date),
                        style: const TextStyle(fontWeight: FontWeight.w600),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text("Payment Method", style: TextStyle(color: Colors.grey)),
                      const SizedBox(height: 4),
                      const Text("Cash on Delivery", style: TextStyle(fontWeight: FontWeight.w600)),
                    ],
                  ),
                ),
              ],
            ),
            const Divider(height: 32),
            const Text("Shipping Address", style: TextStyle(color: Colors.grey)),
            const SizedBox(height: 8),
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Icon(Icons.location_on_outlined, size: 20, color: settings.primaryColor),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    order.userAddress,
                    style: const TextStyle(fontWeight: FontWeight.w500),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildItemsList(BuildContext context, SettingsViewModel settings) {
    return ListView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      itemCount: order.items.length,
      itemBuilder: (context, index) {
        final item = order.items[index];
        return Container(
          margin: const EdgeInsets.only(bottom: 12),
          padding: const EdgeInsets.all(12),
          decoration: BoxDecoration(
            color: Theme.of(context).cardColor,
            borderRadius: BorderRadius.circular(12),
            border: Border.all(color: Colors.grey.withValues(alpha: 0.1)),
          ),
          child: Row(
            children: [
              ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: Image.network(
                  item.product.imageUrl,
                  width: 60,
                  height: 60,
                  fit: BoxFit.cover,
                  errorBuilder: (context, error, stackTrace) => Container(
                    width: 60,
                    height: 60,
                    color: Colors.grey[200],
                    child: const Icon(Icons.image_not_supported),
                  ),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      item.product.name,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 4),
                    Text(
                      "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(item.product.price.toInt())} x ${item.quantity}",
                      style: const TextStyle(color: Colors.grey, fontWeight: FontWeight.w500),
                    ),
                  ],
                ),
              ),
              Text(
                "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format((item.product.price * item.quantity).toInt())}",
                style: TextStyle(fontWeight: FontWeight.w900, fontSize: 16, color: settings.primaryColor),
              ),
            ],
          ),
        );
      },
    );
  }

  Widget _buildPriceSummary(BuildContext context, SettingsViewModel settings) {
    final double subtotal = order.totalAmount - order.deliveryFee;
    
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: settings.primaryColor.withValues(alpha: 0.05),
        borderRadius: BorderRadius.circular(15),
      ),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text("Subtotal", style: TextStyle(fontSize: 16)),
              Text("${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(subtotal.toInt())}", style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            ],
          ),
          const SizedBox(height: 12),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text("Delivery Fee", style: TextStyle(fontSize: 16)),
              Text("${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(order.deliveryFee.toInt())}", style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            ],
          ),
          const Divider(height: 32),
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
                      text: NumberFormat('#,##,###').format(order.totalAmount.toInt()),
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

  Widget _buildCancelButton(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      height: 60,
      child: ElevatedButton(
        onPressed: () => _showCancelDialog(context),
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.white,
          foregroundColor: Colors.red,
          elevation: 0,
          side: const BorderSide(color: Colors.red, width: 1.5),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        ),
        child: const Text(
          "CANCEL ORDER",
          style: TextStyle(fontWeight: FontWeight.w900, fontSize: 18, letterSpacing: 1.2),
        ),
      ),
    );
  }

  Widget _buildStatusBadge(String status) {
    Color color = Colors.blue;
    if (status == 'Pending') color = Colors.orange;
    if (status == 'Delivered') color = Colors.green;
    if (status == 'Cancelled') color = Colors.red;
    if (status == 'Shipped') color = Colors.purple;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Text(
        status.toUpperCase(),
        style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 12),
      ),
    );
  }

  void _showCancelDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Cancel Order?"),
        content: const Text("Are you sure you want to cancel this order? The items will be returned to stock."),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("NO")),
          TextButton(
            onPressed: () async {
              final success = await OrderRepository().cancelOrder(order);
              if (context.mounted) {
                Navigator.pop(ctx); // Close dialog
                if (success) {
                  Navigator.pop(context); // Go back to orders list
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text("Order cancelled and stock updated")),
                  );
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text("Failed to cancel order. It might already be processed."), backgroundColor: Colors.red),
                  );
                }
              }
            },
            child: const Text("YES, CANCEL", style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }
}
