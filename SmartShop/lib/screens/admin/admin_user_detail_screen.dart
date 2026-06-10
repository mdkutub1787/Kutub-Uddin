import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../../models/user_model.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../widgets/custom_app_bar.dart';
import '../order/order_details_screen.dart';

class AdminUserDetailScreen extends StatelessWidget {
  final UserModel user;

  const AdminUserDetailScreen({super.key, required this.user});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final orderVM = context.watch<OrderViewModel>();
    
    final userOrders = orderVM.allOrders.where((o) => o.userId == user.uid).toList();

    return Scaffold(
      appBar: CustomAppBar(title: user.name),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // User Info Card
            _buildInfoCard(context, settings),
            
            const SizedBox(height: 24),
            
            Text(
              "User Orders (${userOrders.length})",
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900),
            ),
            const SizedBox(height: 12),
            
            if (userOrders.isEmpty)
              const Center(child: Padding(
                padding: EdgeInsets.all(20.0),
                child: Text("No orders placed by this user"),
              ))
            else
              ListView.builder(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                itemCount: userOrders.length,
                itemBuilder: (context, index) {
                  final order = userOrders[index];
                  return Card(
                    margin: const EdgeInsets.only(bottom: 10),
                    elevation: 0,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(15),
                      side: BorderSide(color: Colors.grey.withValues(alpha: 0.1)),
                    ),
                    child: ListTile(
                      title: Text("Order ID: ${order.id}", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                      subtitle: Text(DateFormat('dd MMM yyyy').format(order.date), style: const TextStyle(fontSize: 12)),
                      trailing: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          Text("৳${order.totalAmount.toInt()}", style: TextStyle(fontWeight: FontWeight.bold, color: settings.primaryColor)),
                          Text(order.status, style: TextStyle(fontSize: 10, color: _getStatusColor(order.status))),
                        ],
                      ),
                      onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => OrderDetailsScreen(order: order))),
                    ),
                  );
                },
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildInfoCard(BuildContext context, SettingsViewModel settings) {
    return Card(
      elevation: 0,
      margin: EdgeInsets.zero,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(16),
        side: BorderSide(color: Colors.grey.withValues(alpha: 0.1)),
      ),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            CircleAvatar(
              radius: 40,
              backgroundColor: settings.primaryColor.withValues(alpha: 0.1),
              child: Text(user.name[0].toUpperCase(), style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold, color: settings.primaryColor)),
            ),
            const SizedBox(height: 16),
            Text(user.name, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            Text(user.email, style: const TextStyle(color: Colors.grey)),
            const Divider(height: 32),
            _infoRow(Icons.phone_android, "Phone", user.phoneNumber),
            const SizedBox(height: 12),
            _infoRow(Icons.location_on_outlined, "Address", user.address),
            const SizedBox(height: 12),
            _infoRow(Icons.security, "Role", user.role.toUpperCase()),
            const SizedBox(height: 12),
            _infoRow(Icons.toggle_on, "Status", user.isActive ? "Active" : "Inactive", color: user.isActive ? Colors.green : Colors.red),
          ],
        ),
      ),
    );
  }

  Widget _infoRow(IconData icon, String label, String value, {Color? color}) {
    return Row(
      children: [
        Icon(icon, size: 18, color: Colors.grey[600]),
        const SizedBox(width: 12),
        Text("$label: ", style: const TextStyle(color: Colors.grey, fontSize: 13)),
        Expanded(child: Text(value, style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: color))),
      ],
    );
  }

  Color _getStatusColor(String status) {
    if (status == 'Pending') return Colors.orange;
    if (status == 'Delivered') return Colors.green;
    if (status == 'Cancelled') return Colors.red;
    return Colors.blue;
  }
}
