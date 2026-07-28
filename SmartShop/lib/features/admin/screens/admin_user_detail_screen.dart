import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../user/models/user_model.dart';
import '../../order/models/order_model.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../admin/riverpod/activity_log_notifier.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../core/riverpod/settings_notifier.dart';

class AdminUserDetailScreen extends ConsumerWidget {
  final UserModel user;
  const AdminUserDetailScreen({super.key, required this.user});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final orderState = ref.watch(orderNotifierProvider);
    final settings = ref.watch(settingsProvider);
    final currency = settings.currencySymbol;
    
    final userOrders = (orderState.value ?? []).where((o) => o.userId == user.uid).toList();
    double totalSpent = userOrders.where((o) => o.status == 'Delivered').fold(0, (sum, o) => sum + o.totalAmount);

    return Scaffold(
      appBar: CustomAppBar(title: "User Profile"),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            CircleAvatar(
              radius: 50,
              backgroundColor: settings.primaryColor.withValues(alpha: 0.1),
              backgroundImage: user.imageUrl != null ? NetworkImage(user.imageUrl!) : null,
              child: user.imageUrl == null ? Icon(Icons.person, size: 50, color: settings.primaryColor) : null,
            ),
            const SizedBox(height: 16),
            Text(user.name, style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
            Text(user.email, style: const TextStyle(color: Colors.grey)),
            const SizedBox(height: 24),
            
            Row(
              children: [
                _statItem("Orders", "${userOrders.length}", Colors.blue),
                const SizedBox(width: 15),
                _statItem("Spent", "$currency${totalSpent.toInt()}", Colors.green),
              ],
            ),
            
            const SizedBox(height: 30),
            _infoRow(Icons.phone, "Phone", user.phoneNumber),
            _infoRow(Icons.location_on, "Address", user.address),
            _infoRow(Icons.security, "Role", user.role.toUpperCase()),
            
            const SizedBox(height: 30),
            const Align(alignment: Alignment.centerLeft, child: Text("Order History", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold))),
            const SizedBox(height: 12),
            
            if (userOrders.isEmpty)
              const Padding(padding: EdgeInsets.only(top: 20), child: Text("No orders placed yet"))
            else
              ListView.builder(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                itemCount: userOrders.length,
                itemBuilder: (context, index) {
                  final order = userOrders[index];
                  return Card(
                    margin: const EdgeInsets.only(bottom: 10),
                    child: ListTile(
                      title: Text("Order #\${order.id.substring(0, 8)}"),
                      subtitle: Text(DateFormat('dd MMM yyyy').format(order.date)),
                      trailing: Text("$currency\${order.totalAmount.toInt()}", style: const TextStyle(fontWeight: FontWeight.bold)),
                    ),
                  );
                },
              ),
          ],
        ),
      ),
    );
  }

  Widget _statItem(String label, String value, Color color) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(color: color.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(15)),
        child: Column(
          children: [
            Text(value, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: color)),
            Text(label, style: const TextStyle(fontSize: 12, color: Colors.grey)),
          ],
        ),
      ),
    );
  }

  Widget _infoRow(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16),
      child: Row(
        children: [
          Icon(icon, color: Colors.grey, size: 20),
          const SizedBox(width: 12),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(label, style: const TextStyle(fontSize: 11, color: Colors.grey)),
              Text(value, style: const TextStyle(fontWeight: FontWeight.w500)),
            ],
          ),
        ],
      ),
    );
  }
}
