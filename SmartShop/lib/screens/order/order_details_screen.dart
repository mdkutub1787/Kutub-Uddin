import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import '../../models/order_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/activity_log_view_model.dart';
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
    final authVM = context.read<AuthViewModel>();
    
    bool isAdminView = authVM.isAdmin;
    bool isSuperAdmin = authVM.user?.role == 'super_admin';

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: CustomAppBar(
        title: AppStrings.orderDetails.tr(),
        actions: [
          if (isSuperAdmin)
            IconButton(
              icon: const Icon(Icons.delete_forever_rounded, color: Colors.white),
              onPressed: () => _showDeleteOrderDialog(context, authVM),
            ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildOrderInfoCard(context, settings),
            const SizedBox(height: 20),
            if (isAdminView) ...[
              _buildAdminActionSection(context, settings, authVM),
              const SizedBox(height: 20),
            ],
            Text(AppStrings.orderedItems.tr(), style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900, letterSpacing: -0.5)),
            const SizedBox(height: 10),
            _buildItemsList(context, settings),
            const SizedBox(height: 20),
            _buildPriceSummary(context, settings),
            const SizedBox(height: 30),
            if (!isAdminView && order.status == 'Pending')
              _buildCancelButton(context),
            const SizedBox(height: 40),
          ],
        ),
      ),
    );
  }

  Widget _buildAdminActionSection(BuildContext context, SettingsViewModel settings, AuthViewModel authVM) {
    return Card(
      elevation: 0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16), side: BorderSide(color: settings.primaryColor.withValues(alpha: 0.2))),
      color: settings.primaryColor.withValues(alpha: 0.05),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text("ADMIN: UPDATE STATUS", style: TextStyle(fontWeight: FontWeight.w900, fontSize: 12, color: Colors.grey, letterSpacing: 1)),
            const SizedBox(height: 12),
            Row(
              children: [
                _statusBtn(context, "Shipped", Colors.indigo, authVM),
                const SizedBox(width: 8),
                _statusBtn(context, "Delivered", Colors.green, authVM),
                const SizedBox(width: 8),
                _statusBtn(context, "Cancelled", Colors.red, authVM),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _statusBtn(BuildContext context, String status, Color color, AuthViewModel authVM) {
    bool isCurrent = order.status == status;
    return Expanded(
      child: ElevatedButton(
        onPressed: isCurrent ? null : () => _updateStatus(context, status, authVM),
        style: ElevatedButton.styleFrom(
          backgroundColor: color,
          foregroundColor: Colors.white,
          padding: EdgeInsets.zero,
          elevation: isCurrent ? 0 : 2,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
        ),
        child: Text(status, style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold)),
      ),
    );
  }

  Future<void> _updateStatus(BuildContext context, String newStatus, AuthViewModel authVM) async {
    await OrderRepository().updateOrderStatus(order.id, newStatus);
    if (context.mounted) {
      context.read<ActivityLogViewModel>().logAction(
        adminId: authVM.user!.uid,
        adminName: authVM.user!.name,
        action: "Updated Order Status",
        targetId: order.id,
        details: "Changed from ${order.status} to $newStatus for ${order.userName}",
      );
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Order status updated to $newStatus")));
      Navigator.pop(context);
    }
  }

  void _showDeleteOrderDialog(BuildContext context, AuthViewModel authVM) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Delete Order Permanently?"),
        content: const Text("This action cannot be undone. The order will be removed from all records."),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("CANCEL")),
          TextButton(
            onPressed: () async {
              await OrderRepository().deleteOrder(order.id);
              if (context.mounted) {
                context.read<ActivityLogViewModel>().logAction(
                  adminId: authVM.user!.uid,
                  adminName: authVM.user!.name,
                  action: "Deleted Order",
                  targetId: order.id,
                  details: "Permanently removed order of ${order.userName}",
                );
                Navigator.pop(ctx);
                Navigator.pop(context);
                ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Order deleted permanently")));
              }
            },
            child: const Text("DELETE", style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }

  Widget _buildOrderInfoCard(BuildContext context, SettingsViewModel settings) {
    return Card(
      elevation: 0,
      margin: EdgeInsets.zero,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15), side: BorderSide(color: Colors.grey.withValues(alpha: 0.1))),
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
            SelectableText(order.id, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15)),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Text(AppStrings.date.tr(), style: const TextStyle(color: Colors.grey, fontSize: 13)), Text(DateFormat('dd MMM yyyy, hh:mm a').format(order.date), style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 14))])),
                Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Text(AppStrings.paymentMethod.tr(), style: const TextStyle(color: Colors.grey, fontSize: 13)), Text(AppStrings.cashOnDelivery.tr(), style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 14))])),
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
                Expanded(child: Text(order.userAddress, style: const TextStyle(fontWeight: FontWeight.w500, fontSize: 14))),
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
          decoration: BoxDecoration(color: Theme.of(context).cardColor, borderRadius: BorderRadius.circular(12), border: Border.all(color: Colors.grey.withValues(alpha: 0.1))),
          child: Row(
            children: [
              ClipRRect(borderRadius: BorderRadius.circular(8), child: Image.network(item.product.imageUrl, width: 50, height: 50, fit: BoxFit.cover, errorBuilder: (context, error, stackTrace) => Container(width: 50, height: 50, color: Colors.grey[200], child: const Icon(Icons.image_not_supported, size: 20)))),
              const SizedBox(width: 12),
              Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Text(item.product.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14), maxLines: 1, overflow: TextOverflow.ellipsis), Text("${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(item.product.price.toInt())} x ${item.quantity}", style: const TextStyle(color: Colors.grey, fontSize: 12))])),
              Text("${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format((item.product.price * item.quantity).toInt())}", style: TextStyle(fontWeight: FontWeight.w900, fontSize: 15, color: settings.primaryColor)),
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
      decoration: BoxDecoration(color: settings.primaryColor.withValues(alpha: 0.05), borderRadius: BorderRadius.circular(16)),
      child: Column(
        children: [
          _sumRow(AppStrings.subtotal.tr(), subtotal.toInt()),
          const SizedBox(height: 8),
          _sumRow(AppStrings.deliveryFee.tr(), order.deliveryFee.toInt()),
          const Divider(height: 24),
          Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [Text(AppStrings.totalAmount.tr(), style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)), Text("${AppStrings.currency.tr()} ${order.totalAmount.toInt()}", style: TextStyle(fontSize: 20, fontWeight: FontWeight.w900, color: settings.primaryColor))]),
        ],
      ),
    );
  }

  Widget _sumRow(String label, int val) => Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [Text(label, style: const TextStyle(fontSize: 14, color: Colors.black54)), Text("${AppStrings.currency.tr()} $val", style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold))]);

  Widget _buildCancelButton(BuildContext context) {
    return SizedBox(width: double.infinity, height: 55, child: OutlinedButton(onPressed: () => _showCancelDialog(context), style: OutlinedButton.styleFrom(foregroundColor: Colors.red, side: const BorderSide(color: Colors.red), shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15))), child: Text(AppStrings.cancelOrder.tr(), style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16))));
  }

  Widget _buildStatusBadge(String status) {
    Color color = Colors.blue;
    String text = status;
    if (status == 'Pending') { color = Colors.orange; text = AppStrings.statusPending.tr(); }
    else if (status == 'Delivered') { color = Colors.green; text = AppStrings.statusDelivered.tr(); }
    else if (status == 'Cancelled') { color = Colors.red; text = AppStrings.statusCancelled.tr(); }
    else if (status == 'Shipped') { color = Colors.purple; text = AppStrings.statusShipped.tr(); }
    return Container(padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4), decoration: BoxDecoration(color: color.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(8)), child: Text(text.toUpperCase(), style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 10)));
  }

  void _showCancelDialog(BuildContext context) {
    showDialog(context: context, builder: (ctx) => AlertDialog(title: Text(AppStrings.cancelConfirmTitle.tr()), content: Text(AppStrings.cancelConfirmMsg.tr()), actions: [TextButton(onPressed: () => Navigator.pop(ctx), child: Text(AppStrings.no.tr())), TextButton(onPressed: () async { await OrderRepository().cancelOrder(order); if (context.mounted) { Navigator.pop(ctx); Navigator.pop(context); ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.cancelSuccess.tr()))); } }, child: Text(AppStrings.yesCancel.tr(), style: const TextStyle(color: Colors.red)))]));
  }
}
