import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:intl/intl.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../utils/constants/app_strings.dart';
import '../../repositories/order_repository.dart';

import '../../routes/app_routes.dart';

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
        title: Text(AppStrings.myOrdersMenu.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
        centerTitle: true,
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          final auth = context.read<AuthViewModel>();
          if (auth.user != null) {
            await context.read<OrderViewModel>().refreshUserOrders(auth.user!.uid);
          }
        },
        child: orderViewModel.isLoading && orderViewModel.userOrders.isEmpty
            ? const Center(child: CircularProgressIndicator())
            : orderViewModel.userOrders.isEmpty
                ? Center(
                    child: ListView(
                      physics: const AlwaysScrollableScrollPhysics(),
                      children: const [
                        SizedBox(height: 200),
                        Center(child: Text("No orders yet!")),
                      ],
                    ),
                  )
                : ListView.builder(
                    padding: const EdgeInsets.only(top: 8, bottom: 20),
                    physics: const AlwaysScrollableScrollPhysics(),
                    itemCount: orderViewModel.userOrders.length,
                    itemBuilder: (context, index) {
                      final order = orderViewModel.userOrders[index];
                      return Card(
                        margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                        elevation: 2,
                        shadowColor: Colors.black12,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                        child: InkWell(
                          borderRadius: BorderRadius.circular(15),
                          onTap: () {
                            Navigator.pushNamed(
                              context,
                              AppRoutes.orderDetails,
                              arguments: order,
                            );
                          },
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
                                            "Order ID: ${order.id}",
                                            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                                            maxLines: 1,
                                            overflow: TextOverflow.ellipsis,
                                          ),
                                          const SizedBox(height: 4),
                                          Text(
                                            DateFormat('dd MMM yyyy, hh:mm a').format(order.date),
                                            style: const TextStyle(color: Colors.grey, fontSize: 12),
                                          ),
                                        ],
                                      ),
                                    ),
                                    _buildStatusBadge(order.status),
                                  ],
                                ),
                                const Divider(height: 24),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text(
                                      "${order.items.length} Items",
                                      style: const TextStyle(fontWeight: FontWeight.w500),
                                    ),
                                    Text(
                                      "Total: ৳${order.totalAmount}",
                                      style: TextStyle(
                                        fontWeight: FontWeight.bold,
                                        fontSize: 16,
                                        color: Theme.of(context).primaryColor,
                                      ),
                                    ),
                                  ],
                                ),
                                const SizedBox(height: 12),
                                Row(
                                  children: [
                                    const Icon(Icons.info_outline, size: 16, color: Colors.blueGrey),
                                    const SizedBox(width: 4),
                                    const Text(
                                      "Tap to view details & items",
                                      style: TextStyle(fontSize: 12, color: Colors.blueGrey),
                                    ),
                                    const Spacer(),
                                    Icon(Icons.arrow_forward_ios, size: 14, color: Colors.grey[400]),
                                  ],
                                ),
                              ],
                            ),
                          ),
                        ),
                      );
                    },
                  ),
      ),
    );
  }

  Widget _buildStatusBadge(String status) {
    Color color = Colors.blue;
    if (status == 'Pending') color = Colors.orange;
    if (status == 'Delivered') color = Colors.green;
    if (status == 'Cancelled') color = Colors.red;
    if (status == 'Shipped') color = Colors.purple;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Text(
        status,
        style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 12),
      ),
    );
  }
}
