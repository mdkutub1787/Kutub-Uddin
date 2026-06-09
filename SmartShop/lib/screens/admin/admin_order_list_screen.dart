import 'package:flutter/material.dart';
import '../../models/order_model.dart';
import '../../repositories/order_repository.dart';
import '../../widgets/app_card.dart';
import '../../widgets/custom_app_bar.dart';
import '../../view_models/settings_view_model.dart';
import 'package:provider/provider.dart';
import '../../utils/constants/app_strings.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:intl/intl.dart';

class AdminOrderListScreen extends StatelessWidget {
  const AdminOrderListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: CustomAppBar(
        title: "Manage Orders",
      ),
      body: StreamBuilder<List<OrderModel>>(
        stream: OrderRepository().getAllOrders(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text("No orders found"));
          }

          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: snapshot.data!.length,
            itemBuilder: (context, index) {
              final order = snapshot.data![index];
              return _buildOrderCard(context, order);
            },
          );
        },
      ),
    );
  }

  Widget _buildOrderCard(BuildContext context, OrderModel order) {
    final settings = context.watch<SettingsViewModel>();
    
    return AppCard(
      margin: const EdgeInsets.only(bottom: 20),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Header Section
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: settings.primaryColor.withValues(alpha: 0.05),
              borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        order.userName,
                        style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 18),
                      ),
                      const SizedBox(height: 4),
                      Row(
                        children: [
                          Icon(Icons.tag, size: 14, color: settings.primaryColor),
                          const SizedBox(width: 4),
                          SelectableText(
                            order.id.substring(0, 12).toUpperCase(),
                            style: TextStyle(
                              color: settings.primaryColor, 
                              fontSize: 12, 
                              fontWeight: FontWeight.bold,
                              letterSpacing: 1
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                _statusChip(order.status),
              ],
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    const Icon(Icons.access_time_rounded, size: 16, color: Colors.grey),
                    const SizedBox(width: 6),
                    Text(
                      DateFormat('dd MMM yyyy, hh:mm a').format(order.date),
                      style: const TextStyle(fontSize: 13, color: Colors.grey, fontWeight: FontWeight.w500),
                    ),
                  ],
                ),
                const Padding(
                  padding: EdgeInsets.symmetric(vertical: 12),
                  child: Divider(height: 1),
                ),
                
                _infoRow(Icons.phone_iphone_rounded, "Contact", order.userPhone, settings.primaryColor),
                const SizedBox(height: 8),
                _infoRow(Icons.location_on_rounded, "Delivery", order.userAddress, settings.primaryColor),
                
                const SizedBox(height: 20),
                Text(
                  "ORDER ITEMS",
                  style: TextStyle(
                    fontWeight: FontWeight.w900, 
                    fontSize: 12, 
                    color: Colors.grey[600],
                    letterSpacing: 1.2
                  ),
                ),
                const SizedBox(height: 10),
                AppCard(
                  elevation: 0,
                  borderRadius: 15,
                  color: Colors.grey[50],
                  border: Border.all(color: Colors.grey[200]!),
                  padding: const EdgeInsets.all(12),
                  child: Column(
                    children: order.items.map((item) {
                      return Padding(
                        padding: const EdgeInsets.only(bottom: 8),
                        child: Row(
                          children: [
                            Container(
                              width: 6,
                              height: 6,
                              decoration: BoxDecoration(
                                color: settings.primaryColor.withValues(alpha: 0.3),
                                shape: BoxShape.circle,
                              ),
                            ),
                            const SizedBox(width: 10),
                            Expanded(
                              child: Text(
                                item.product.name,
                                style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 14),
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                              ),
                            ),
                            Text.rich(
                              TextSpan(
                                children: [
                                  TextSpan(
                                    text: "${AppStrings.currency.tr()} ",
                                    style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold, fontSize: 11),
                                  ),
                                  TextSpan(
                                    text: "${NumberFormat('#,##,###').format(item.product.price.toInt())} x ${item.quantity} = ",
                                    style: TextStyle(color: Colors.grey[700], fontSize: 12, fontWeight: FontWeight.w600),
                                  ),
                                  TextSpan(
                                    text: "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format((item.product.price * item.quantity).toInt())}",
                                    style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.w900, fontSize: 12),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                      );
                    }).toList(),
                  ),
                ),
                const SizedBox(height: 16),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      "Total Amount", 
                      style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)
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
                              fontWeight: FontWeight.w900,
                              fontSize: 24,
                              color: settings.primaryColor,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 24),
                Row(
                  children: [
                    _actionBtn(context, order, "Shipped", Colors.indigo, Icons.local_shipping_rounded),
                    const SizedBox(width: 10),
                    _actionBtn(context, order, "Delivered", Colors.green[700]!, Icons.verified_rounded),
                    const SizedBox(width: 10),
                    _actionBtn(context, order, "Cancelled", Colors.red[700]!, Icons.cancel_rounded),
                  ],
                )
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _infoRow(IconData icon, String label, String value, Color primaryColor) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          padding: const EdgeInsets.all(6),
          decoration: BoxDecoration(
            color: primaryColor.withValues(alpha: 0.1),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Icon(icon, size: 16, color: primaryColor),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(label, style: TextStyle(color: Colors.grey[500], fontSize: 11, fontWeight: FontWeight.bold)),
              Text(value, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
            ],
          ),
        ),
      ],
    );
  }

  Widget _statusChip(String status) {
    Color color = Colors.blue;
    if (status == 'Pending') color = Colors.orange;
    if (status == 'Shipped') color = Colors.indigo;
    if (status == 'Delivered') color = Colors.green;
    if (status == 'Cancelled') color = Colors.red;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.15),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withValues(alpha: 0.3), width: 1.5),
      ),
      child: Text(
        status.toUpperCase(),
        style: TextStyle(color: color, fontWeight: FontWeight.w900, fontSize: 10, letterSpacing: 1),
      ),
    );
  }

  Widget _actionBtn(BuildContext context, OrderModel order, String status, Color color, IconData icon) {
    bool isCurrent = order.status == status;
    bool canCancel = order.status == 'Pending';
    
    bool isDisabled = (status == 'Cancelled' && !canCancel) || 
                      (status != 'Cancelled' && order.status == 'Cancelled') ||
                      isCurrent;

    return Expanded(
      child: AppCard(
        elevation: isDisabled ? 0 : 4,
        borderRadius: 12,
        color: isDisabled ? Colors.grey[200] : color,
        onTap: isDisabled ? null : () async {
          if (status == 'Cancelled') {
            await OrderRepository().cancelOrder(order);
          } else {
            await OrderRepository().updateOrderStatus(order.id, status);
          }
        },
        child: Container(
          height: 45,
          alignment: Alignment.center,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(icon, size: 18, color: isDisabled ? Colors.grey[500] : Colors.white),
              const SizedBox(height: 2),
              Text(
                status, 
                style: TextStyle(
                  fontSize: 9, 
                  fontWeight: FontWeight.bold, 
                  color: isDisabled ? Colors.grey[500] : Colors.white
                )
              ),
            ],
          ),
        ),
      ),
    );
  }
}
