import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:intl/intl.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../utils/constants/app_strings.dart';

class MyOrdersScreen extends StatefulWidget {
  const MyOrdersScreen({super.key});

  @override
  State<MyOrdersScreen> createState() => _MyOrdersScreenState();
}

class _MyOrdersScreenState extends State<MyOrdersScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final auth = context.read<AuthViewModel>();
      if (auth.user != null) {
        context.read<OrderViewModel>().fetchUserOrders(auth.user!.uid);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final orderViewModel = context.watch<OrderViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: Text(AppStrings.myOrdersMenu.tr()),
        centerTitle: true,
      ),
      body: orderViewModel.isLoading
          ? const Center(child: CircularProgressIndicator())
          : orderViewModel.userOrders.isEmpty
              ? const Center(child: Text("No orders yet!"))
              : ListView.builder(
                  itemCount: orderViewModel.userOrders.length,
                  itemBuilder: (context, index) {
                    final order = orderViewModel.userOrders[index];
                    return Card(
                      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      child: ListTile(
                        title: Text("Order ID: ${order.id.substring(0, 8)}..."),
                        subtitle: Text("Total: ৳${order.totalAmount} - ${DateFormat('dd MMM yyyy').format(order.date)}"),
                        trailing: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                          decoration: BoxDecoration(
                            color: _getStatusColor(order.status).withValues(alpha: 0.1),
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: Text(
                            order.status,
                            style: TextStyle(color: _getStatusColor(order.status), fontWeight: FontWeight.bold),
                          ),
                        ),
                      ),
                    );
                  },
                ),
    );
  }

  Color _getStatusColor(String status) {
    switch (status) {
      case 'Pending': return Colors.orange;
      case 'Delivered': return Colors.green;
      case 'Cancelled': return Colors.red;
      default: return Colors.blue;
    }
  }
}
