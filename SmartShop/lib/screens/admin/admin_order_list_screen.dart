import 'package:flutter/material.dart';
import '../../models/order_model.dart';
import '../../repositories/order_repository.dart';
import 'package:intl/intl.dart';

class AdminOrderListScreen extends StatelessWidget {
  const AdminOrderListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Manage Orders", style: TextStyle(fontWeight: FontWeight.bold)),
        centerTitle: true,
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
    return Card(
      elevation: 0,
      margin: const EdgeInsets.only(bottom: 20),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(16),
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
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        order.userName,
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                      ),
                      const SizedBox(height: 2),
                      SelectableText(
                        "ID: ${order.id}",
                        style: TextStyle(color: Colors.grey[600], fontSize: 12),
                      ),
                    ],
                  ),
                ),
                _statusChip(order.status),
              ],
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                const Icon(Icons.calendar_today, size: 14, color: Colors.grey),
                const SizedBox(width: 4),
                Text(
                  DateFormat('dd MMM yyyy, hh:mm a').format(order.date),
                  style: const TextStyle(fontSize: 12, color: Colors.grey),
                ),
              ],
            ),
            const Divider(height: 24),
            _infoRow(Icons.phone_outlined, "Phone", order.userPhone),
            _infoRow(Icons.location_on_outlined, "Address", order.userAddress),
            const SizedBox(height: 16),
            const Text(
              "Order Items:",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
            ),
            const SizedBox(height: 8),
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Colors.grey[50],
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: Colors.grey[200]!),
              ),
              child: Column(
                children: order.items.map((item) {
                  return Padding(
                    padding: const EdgeInsets.only(bottom: 6),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                          child: Text(
                            "• ${item.product.name}",
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                        Text(
                          "৳${item.product.price} x ${item.quantity}",
                          style: const TextStyle(fontWeight: FontWeight.w500),
                        ),
                      ],
                    ),
                  );
                }).toList(),
              ),
            ),
            const SizedBox(height: 12),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text("Total Amount", style: TextStyle(fontWeight: FontWeight.w500)),
                Text(
                  "৳${order.totalAmount}",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 18,
                    color: Theme.of(context).primaryColor,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 20),
            Row(
              children: [
                _actionBtn(context, order, "Shipped", Colors.blue, Icons.local_shipping_outlined),
                const SizedBox(width: 8),
                _actionBtn(context, order, "Delivered", Colors.green, Icons.check_circle_outline),
                const SizedBox(width: 8),
                _actionBtn(context, order, "Cancelled", Colors.red, Icons.cancel_outlined),
              ],
            )
          ],
        ),
      ),
    );
  }

  Widget _infoRow(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 6),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, size: 16, color: Colors.blueGrey),
          const SizedBox(width: 8),
          Text("$label: ", style: const TextStyle(color: Colors.grey, fontWeight: FontWeight.w500)),
          Expanded(child: Text(value, style: const TextStyle(fontWeight: FontWeight.w500))),
        ],
      ),
    );
  }

  Widget _statusChip(String status) {
    Color color = Colors.blue;
    if (status == 'Pending') color = Colors.orange;
    if (status == 'Shipped') color = Colors.purple;
    if (status == 'Delivered') color = Colors.green;
    if (status == 'Cancelled') color = Colors.red;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: color.withValues(alpha: 0.2)),
      ),
      child: Text(
        status,
        style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 12),
      ),
    );
  }

  Widget _actionBtn(BuildContext context, OrderModel order, String status, Color color, IconData icon) {
    bool isCurrent = order.status == status;
    bool canCancel = order.status == 'Pending';
    
    // Only allow clicking Cancel if it's Pending
    // Only allow clicking other status if not already that status and not cancelled
    bool isDisabled = (status == 'Cancelled' && !canCancel) || 
                      (status != 'Cancelled' && order.status == 'Cancelled') ||
                      isCurrent;

    return Expanded(
      child: ElevatedButton.icon(
        onPressed: isDisabled ? null : () async {
          if (status == 'Cancelled') {
            await OrderRepository().cancelOrder(order);
          } else {
            await OrderRepository().updateOrderStatus(order.id, status);
          }
        },
        icon: Icon(icon, size: 16, color: Colors.white),
        label: Text(status, style: const TextStyle(fontSize: 10, color: Colors.white)),
        style: ElevatedButton.styleFrom(
          backgroundColor: color,
          disabledBackgroundColor: color.withValues(alpha: 0.3),
          padding: EdgeInsets.zero,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
          elevation: 0,
        ),
      ),
    );
  }
}
