import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/order_model.dart';
import '../../repositories/order_repository.dart';
import 'package:intl/intl.dart';

class AdminOrderListScreen extends StatelessWidget {
  const AdminOrderListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final orderRepo = OrderRepository();

    return Scaffold(
      appBar: AppBar(
        title: const Text("Manage Orders"),
        centerTitle: true,
      ),
      body: StreamBuilder<List<OrderModel>>(
        stream: orderRepo.getAllOrders(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text("No orders found."));
          }

          final orders = snapshot.data!;

          return ListView.builder(
            itemCount: orders.length,
            itemBuilder: (context, index) {
              final order = orders[index];
              return Card(
                margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                child: ExpansionTile(
                  title: Text("Order #${order.id.substring(0, 8)}"),
                  subtitle: Text("Total: ৳${order.totalAmount} - ${DateFormat('dd MMM yyyy').format(order.date)}"),
                  trailing: _buildStatusBadge(order.status),
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text("Items:", style: TextStyle(fontWeight: FontWeight.bold)),
                          ...order.items.map((item) => Text("- ${item.product.name} x ${item.quantity}")),
                          const SizedBox(height: 10),
                          Text("Address: ${order.shippingAddress}"),
                          const SizedBox(height: 20),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              _buildActionButton(context, order, "Processing", Colors.blue),
                              _buildActionButton(context, order, "Shipped", Colors.orange),
                              _buildActionButton(context, order, "Delivered", Colors.green),
                              _buildActionButton(context, order, "Cancelled", Colors.red),
                            ],
                          )
                        ],
                      ),
                    )
                  ],
                ),
              );
            },
          );
        },
      ),
    );
  }

  Widget _buildStatusBadge(String status) {
    Color color = Colors.blue;
    if (status == 'Pending') color = Colors.orange;
    if (status == 'Delivered') color = Colors.green;
    if (status == 'Cancelled') color = Colors.red;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
      decoration: BoxDecoration(
        color: color.withOpacity(0.1),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Text(
        status,
        style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 12),
      ),
    );
  }

  Widget _buildActionButton(BuildContext context, OrderModel order, String status, Color color) {
    return ElevatedButton(
      onPressed: () async {
        await OrderRepository().updateOrderStatus(order.id, status);
        if (context.mounted) {
          ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Order status updated to $status")));
        }
      },
      style: ElevatedButton.styleFrom(
        backgroundColor: color,
        foregroundColor: Colors.white,
        padding: const EdgeInsets.symmetric(horizontal: 10),
        textStyle: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold),
      ),
      child: Text(status),
    );
  }
}
