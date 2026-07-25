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
import '../../../core/utils/exit_dialog_helper.dart';

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

    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (didPop) return;
        await ExitDialogHelper.showExitDialog(context);
      },
      child: Scaffold(
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
    ));
  }

  Widget _buildStatusHeader(UserModel user, Color primaryColor) {
    bool isAvailable = user.isAvailable ?? false;
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(25),
      decoration: BoxDecoration(
        color: isAvailable ? Colors.green.withValues(alpha: 0.9) : Colors.grey[800],
        borderRadius: const BorderRadius.vertical(bottom: Radius.circular(35)),
        boxShadow: [
          BoxShadow(
            color: (isAvailable ? Colors.green : Colors.grey).withValues(alpha: 0.3),
            blurRadius: 20,
            offset: const Offset(0, 10),
          )
        ],
      ),
      child: Row(
        children: [
          Container(
            padding: const EdgeInsets.all(4),
            decoration: BoxDecoration(
              color: Colors.white,
              shape: BoxShape.circle,
              boxShadow: [
                BoxShadow(color: Colors.black.withValues(alpha: 0.1), blurRadius: 10)
              ],
            ),
            child: CircleAvatar(
              radius: 30,
              backgroundColor: isAvailable ? Colors.green[100] : Colors.grey[200],
              child: Icon(Icons.delivery_dining_rounded, color: isAvailable ? Colors.green : Colors.grey[600], size: 30),
            ),
          ),
          const SizedBox(width: 20),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(user.name, style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 24, color: Colors.white, letterSpacing: -0.5)),
                const SizedBox(height: 4),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: Colors.white24,
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(isAvailable ? Icons.check_circle_rounded : Icons.pause_circle_rounded, color: Colors.white, size: 12),
                      const SizedBox(width: 4),
                      Text(
                        isAvailable ? "ONLINE & READY" : "OFFLINE",
                        style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w900, fontSize: 10, letterSpacing: 1),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildOrderCard(BuildContext context, OrderModel order, Color primaryColor) {
    return Container(
      margin: const EdgeInsets.only(bottom: 20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 20,
            offset: const Offset(0, 10),
          )
        ],
      ),
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text("ORDER ID", style: TextStyle(fontSize: 10, color: Colors.grey, fontWeight: FontWeight.w900, letterSpacing: 1)),
                    Text("#${order.id.substring(order.id.length - 8)}", style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16)),
                  ],
                ),
                _statusChip(order.status),
              ],
            ),
            const Padding(
              padding: EdgeInsets.symmetric(vertical: 16),
              child: Divider(height: 1, thickness: 1, color: Color(0xFFF5F5F5)),
            ),
            _infoItem(Icons.person_rounded, "Customer Name", order.userName),
            const SizedBox(height: 16),
            _infoItem(Icons.phone_rounded, "Contact Number", order.userPhone),
            const SizedBox(height: 16),
            _infoItem(Icons.location_on_rounded, "Delivery Address", order.userAddress),
            const SizedBox(height: 24),
            Row(
              children: [
                if (order.status == 'Assigned')
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'PickedUp'),
                      icon: const Icon(Icons.inventory_2_rounded, size: 18),
                      label: const Text("MARK AS PICKED UP", style: TextStyle(fontSize: 12, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.orange, 
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                        elevation: 0,
                      ),
                    ),
                  ),
                if (order.status == 'PickedUp')
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'OnTheWay'),
                      icon: const Icon(Icons.directions_bike_rounded, size: 18),
                      label: const Text("START DELIVERY", style: TextStyle(fontSize: 12, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.blue, 
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                        elevation: 0,
                      ),
                    ),
                  ),
                if (order.status == 'OnTheWay')
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'Delivered'),
                      icon: const Icon(Icons.check_circle_rounded, size: 18),
                      label: const Text("MARK AS DELIVERED", style: TextStyle(fontSize: 12, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.green, 
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                        elevation: 0,
                      ),
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
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1), 
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: color.withValues(alpha: 0.3)),
      ),
      child: Text(status.toUpperCase(), style: TextStyle(color: color, fontSize: 10, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
    );
  }
}
