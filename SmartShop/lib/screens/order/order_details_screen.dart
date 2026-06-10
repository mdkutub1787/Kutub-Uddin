import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import '../../models/order_model.dart';
import '../../view_models/settings_view_model.dart';
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
      appBar: CustomAppBar(
        title: AppStrings.orderDetails.tr(),
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
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _buildOrderInfoCard(context, settings),
                const SizedBox(height: 20),
                Text(
                  AppStrings.orderedItems.tr(),
                  style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900, letterSpacing: -0.5),
                ),
                const SizedBox(height: 10),
                _buildItemsList(context, settings),
                const SizedBox(height: 20),
                _buildPriceSummary(context, settings),
                const SizedBox(height: 30),
                if (order.status == 'Pending')
                  _buildCancelButton(context),
                const SizedBox(height: 40),
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
      margin: EdgeInsets.zero,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(15),
        side: BorderSide(color: Colors.grey.withValues(alpha: 0.1)),
      ),
      child: Padding(
        padding: const EdgeInsets.all(12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(AppStrings.status.tr(), style: const TextStyle(color: Colors.grey, fontSize: 13)),
                _buildStatusBadge(order.status),
              ],
            ),
            const Divider(height: 16),
            Text(AppStrings.orderId.tr(), style: const TextStyle(color: Colors.grey, fontSize: 13)),
            const SizedBox(height: 2),
            SelectableText(
              order.id,
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(AppStrings.date.tr(), style: const TextStyle(color: Colors.grey, fontSize: 13)),
                      const SizedBox(height: 2),
                      Text(
                        DateFormat('dd MMM yyyy, hh:mm a').format(order.date),
                        style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 14),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(AppStrings.paymentMethod.tr(), style: const TextStyle(color: Colors.grey, fontSize: 13)),
                      const SizedBox(height: 2),
                      Text(AppStrings.cashOnDelivery.tr(), style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 14)),
                    ],
                  ),
                ),
              ],
            ),
            const Divider(height: 24),
            Text(AppStrings.shippingAddress.tr(), style: const TextStyle(color: Colors.grey, fontSize: 13)),
            const SizedBox(height: 4),
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Icon(Icons.location_on_outlined, size: 18, color: settings.primaryColor),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    order.userAddress,
                    style: const TextStyle(fontWeight: FontWeight.w500, fontSize: 14),
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
          margin: const EdgeInsets.only(bottom: 8),
          padding: const EdgeInsets.all(10),
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
                  width: 50,
                  height: 50,
                  fit: BoxFit.cover,
                  errorBuilder: (context, error, stackTrace) => Container(
                    width: 50,
                    height: 50,
                    color: Colors.grey[200],
                    child: const Icon(Icons.image_not_supported, size: 20),
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
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 2),
                    Text(
                      "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(item.product.price.toInt())} x ${item.quantity}",
                      style: const TextStyle(color: Colors.grey, fontWeight: FontWeight.w500, fontSize: 12),
                    ),
                  ],
                ),
              ),
              Text(
                "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format((item.product.price * item.quantity).toInt())}",
                style: TextStyle(fontWeight: FontWeight.w900, fontSize: 15, color: settings.primaryColor),
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
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: settings.primaryColor.withValues(alpha: 0.05),
        borderRadius: BorderRadius.circular(15),
      ),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(AppStrings.subtotal.tr(), style: const TextStyle(fontSize: 14)),
              Text("${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(subtotal.toInt())}", style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold)),
            ],
          ),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(AppStrings.deliveryFee.tr(), style: const TextStyle(fontSize: 14)),
              Text("${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(order.deliveryFee.toInt())}", style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold)),
            ],
          ),
          const Divider(height: 24),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                AppStrings.totalAmount.tr(),
                style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              Text.rich(
                TextSpan(
                  children: [
                    TextSpan(
                      text: "${AppStrings.currency.tr()} ",
                      style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: settings.primaryColor),
                    ),
                    TextSpan(
                      text: NumberFormat('#,##,###').format(order.totalAmount.toInt()),
                      style: TextStyle(
                        fontSize: 20,
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
      height: 55,
      child: ElevatedButton(
        onPressed: () => _showCancelDialog(context),
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.white,
          foregroundColor: Colors.red,
          elevation: 0,
          side: const BorderSide(color: Colors.red, width: 1.2),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
        ),
        child: Text(
          AppStrings.cancelOrder.tr(),
          style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16, letterSpacing: 1.1),
        ),
      ),
    );
  }

  Widget _buildStatusBadge(String status) {
    Color color = Colors.blue;
    String statusText = status;
    if (status == 'Pending') {
      color = Colors.orange;
      statusText = AppStrings.statusPending.tr();
    } else if (status == 'Delivered') {
      color = Colors.green;
      statusText = AppStrings.statusDelivered.tr();
    } else if (status == 'Cancelled') {
      color = Colors.red;
      statusText = AppStrings.statusCancelled.tr();
    } else if (status == 'Shipped') {
      color = Colors.purple;
      statusText = AppStrings.statusShipped.tr();
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Text(
        statusText.toUpperCase(),
        style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 12),
      ),
    );
  }

  void _showCancelDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text(AppStrings.cancelConfirmTitle.tr()),
        content: Text(AppStrings.cancelConfirmMsg.tr()),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: Text(AppStrings.no.tr())),
          TextButton(
            onPressed: () async {
              final success = await OrderRepository().cancelOrder(order);
              if (context.mounted) {
                Navigator.pop(ctx); // Close dialog
                if (success) {
                  Navigator.pop(context); // Go back to orders list
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text(AppStrings.cancelSuccess.tr())),
                  );
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text(AppStrings.cancelFailed.tr()), backgroundColor: Colors.red),
                  );
                }
              }
            },
            child: Text(AppStrings.yesCancel.tr(), style: const TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }
}
