import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../order/models/order_model.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../user/models/user_model.dart';
import '../../order/repositories/order_repository.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../widgets/app_card.dart';

class DeliveryDashboardScreen extends ConsumerStatefulWidget {
  const DeliveryDashboardScreen({super.key});

  @override
  ConsumerState<DeliveryDashboardScreen> createState() => _DeliveryDashboardScreenState();
}

class _DeliveryDashboardScreenState extends ConsumerState<DeliveryDashboardScreen> {
  /* final OrderRepository _orderRepo = ref.read(orderNotifierProvider.notifier); */

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authNotifierProvider);
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;
    final user = authState.value;

    if (user == null) return const Scaffold(body: Center(child: CircularProgressIndicator()));

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: const Text("Delivery Dashboard", style: TextStyle(fontWeight: FontWeight.w900)),
        backgroundColor: primaryColor,
        foregroundColor: Colors.white,
        actions: [
          Switch(
            value: user.isAvailable ?? false,
            onChanged: (val) async {
               // ref.read(authNotifierProvider.notifier).updateDeliveryAvailability(val);
            },
            activeColor: Colors.white,
            activeTrackColor: Colors.greenAccent,
          ),
          const SizedBox(width: 8),
        ],
      ),
      body: Column(
        children: [
          _buildStatusHeader(user, primaryColor),
          Expanded(
            child: StreamBuilder<List<OrderModel>>(
              stream: const Stream.empty(), // In production, filter by deliveryManId in repository
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) return const Center(child: CircularProgressIndicator());
                
                final myOrders = snapshot.data?.where((o) => o.deliveryManId == user.uid && o.status != 'Delivered' && o.status != 'Cancelled').toList() ?? [];

                if (myOrders.isEmpty) {
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.delivery_dining_outlined, size: 80, color: Colors.grey[300]),
                        const SizedBox(height: 16),
                        const Text("No active deliveries", style: TextStyle(color: Colors.grey, fontWeight: FontWeight.bold)),
                      ],
                    ),
                  );
                }

                return ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: myOrders.length,
                  itemBuilder: (context, index) => _buildOrderCard(context, myOrders[index], primaryColor),
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStatusHeader(UserModel user, Color primaryColor) {
    bool isAvailable = user.isAvailable ?? false;
    return Container(
      padding: const EdgeInsets.all(20),
      color: primaryColor.withValues(alpha: 0.1),
      child: Row(
        children: [
          CircleAvatar(
            radius: 25,
            backgroundColor: isAvailable ? Colors.green : Colors.grey,
            child: const Icon(Icons.person, color: Colors.white),
          ),
          const SizedBox(width: 15),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(user.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                Text(
                  isAvailable ? "You are Online & Available" : "You are Offline",
                  style: TextStyle(color: isAvailable ? Colors.green : Colors.grey[600], fontWeight: FontWeight.w500),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildOrderCard(BuildContext context, OrderModel order, Color primaryColor) {
    return AppCard(
      margin: const EdgeInsets.only(bottom: 16),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("Order #${order.id.substring(order.id.length - 6)}", style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.grey)),
                _statusChip(order.status),
              ],
            ),
            const Divider(height: 24),
            _infoItem(Icons.person, "Customer", order.userName),
            const SizedBox(height: 12),
            _infoItem(Icons.phone, "Phone", order.userPhone),
            const SizedBox(height: 12),
            _infoItem(Icons.location_on, "Address", order.userAddress),
            const SizedBox(height: 20),
            Row(
              children: [
                if (order.status == 'Assigned')
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'PickedUp'),
                      icon: const Icon(Icons.inventory_2_rounded),
                      label: const Text("PICKED UP"),
                      style: ElevatedButton.styleFrom(backgroundColor: Colors.orange, foregroundColor: Colors.white),
                    ),
                  ),
                if (order.status == 'PickedUp')
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'OnTheWay'),
                      icon: const Icon(Icons.directions_bike_rounded),
                      label: const Text("START DELIVERY"),
                      style: ElevatedButton.styleFrom(backgroundColor: Colors.blue, foregroundColor: Colors.white),
                    ),
                  ),
                if (order.status == 'OnTheWay')
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'Delivered'),
                      icon: const Icon(Icons.check_circle_rounded),
                      label: const Text("MARK DELIVERED"),
                      style: ElevatedButton.styleFrom(backgroundColor: Colors.green, foregroundColor: Colors.white),
                    ),
                  ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _infoItem(IconData icon, String label, String value) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Icon(icon, size: 18, color: Colors.grey),
        const SizedBox(width: 12),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(label, style: const TextStyle(fontSize: 10, color: Colors.grey, fontWeight: FontWeight.bold)),
              Text(value, style: const TextStyle(fontWeight: FontWeight.w600)),
            ],
          ),
        ),
      ],
    );
  }

  Widget _statusChip(String status) {
    Color color = Colors.orange;
    if (status == 'PickedUp') color = Colors.blue;
    if (status == 'OnTheWay') color = Colors.indigo;
    if (status == 'Delivered') color = Colors.green;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(color: color.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(12)),
      child: Text(status, style: TextStyle(color: color, fontSize: 10, fontWeight: FontWeight.bold)),
    );
  }
}
