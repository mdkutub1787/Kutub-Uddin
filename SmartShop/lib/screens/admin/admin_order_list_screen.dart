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

class AdminOrderListScreen extends StatefulWidget {
  const AdminOrderListScreen({super.key});

  @override
  State<AdminOrderListScreen> createState() => _AdminOrderListScreenState();
}

class _AdminOrderListScreenState extends State<AdminOrderListScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  final TextEditingController _searchController = TextEditingController();
  String _searchQuery = "";

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 5, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    _searchController.dispose();
    super.dispose();
  }

  List<OrderModel> _filterOrders(List<OrderModel> orders, int tabIndex) {
    List<OrderModel> filtered = orders;
    
    // Status Filtering
    if (tabIndex == 1) filtered = orders.where((o) => o.status == 'Pending').toList();
    if (tabIndex == 2) filtered = orders.where((o) => o.status == 'Shipped').toList();
    if (tabIndex == 3) filtered = orders.where((o) => o.status == 'Delivered').toList();
    if (tabIndex == 4) filtered = orders.where((o) => o.status == 'Cancelled').toList();

    // Search Filtering
    if (_searchQuery.isNotEmpty) {
      filtered = filtered.where((o) => 
        o.userName.toLowerCase().contains(_searchQuery.toLowerCase()) || 
        o.id.toLowerCase().contains(_searchQuery.toLowerCase()) ||
        o.userPhone.contains(_searchQuery)
      ).toList();
    }

    // Sort by Date (Newest first)
    filtered.sort((a, b) => b.date.compareTo(a.date));
    
    return filtered;
  }

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final primaryColor = settings.primaryColor;

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: const Text("Manage Orders", style: TextStyle(fontWeight: FontWeight.w900)),
        backgroundColor: primaryColor,
        foregroundColor: Colors.white,
        elevation: 0,
        bottom: PreferredSize(
          preferredSize: const Size.fromHeight(110),
          child: Column(
            children: [
              // Search Bar
              Padding(
                padding: const EdgeInsets.fromLTRB(16, 0, 16, 10),
                child: Container(
                  height: 45,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: TextField(
                    controller: _searchController,
                    onChanged: (val) => setState(() => _searchQuery = val),
                    decoration: InputDecoration(
                      hintText: "Search by Name, ID or Phone...",
                      hintStyle: const TextStyle(fontSize: 14),
                      prefixIcon: Icon(Icons.search_rounded, color: primaryColor),
                      border: InputBorder.none,
                      contentPadding: const EdgeInsets.symmetric(vertical: 10),
                    ),
                  ),
                ),
              ),
              // Status Tabs
              TabBar(
                controller: _tabController,
                isScrollable: true,
                indicatorColor: Colors.white,
                indicatorWeight: 4,
                labelColor: Colors.white,
                unselectedLabelColor: Colors.white.withValues(alpha: 0.6),
                labelStyle: const TextStyle(fontWeight: FontWeight.w900, fontSize: 15, letterSpacing: 0.5),
                unselectedLabelStyle: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                indicatorSize: TabBarIndicatorSize.label,
                padding: const EdgeInsets.symmetric(horizontal: 10),
                tabs: const [
                  Tab(text: "All"),
                  Tab(text: "Pending"),
                  Tab(text: "Shipped"),
                  Tab(text: "Delivered"),
                  Tab(text: "Cancelled"),
                ],
              ),
            ],
          ),
        ),
      ),
      body: StreamBuilder<List<OrderModel>>(
        stream: OrderRepository().getAllOrders(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return _emptyState();
          }

          return TabBarView(
            controller: _tabController,
            children: List.generate(5, (index) {
              final filteredOrders = _filterOrders(snapshot.data!, index);
              
              if (filteredOrders.isEmpty) {
                return _emptyState("No orders in this category");
              }

              return ListView.builder(
                padding: const EdgeInsets.fromLTRB(16, 16, 16, 100),
                itemCount: filteredOrders.length,
                itemBuilder: (context, i) {
                  return _buildOrderCard(context, filteredOrders[i]);
                },
              );
            }),
          );
        },
      ),
    );
  }

  Widget _emptyState([String message = "No orders found"]) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.receipt_long_outlined, size: 80, color: Colors.grey[300]),
          const SizedBox(height: 16),
          Text(message, style: TextStyle(color: Colors.grey[400], fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }

  Widget _buildOrderCard(BuildContext context, OrderModel order) {
    final settings = context.watch<SettingsViewModel>();
    final primaryColor = settings.primaryColor;
    
    return AppCard(
      margin: const EdgeInsets.only(bottom: 20),
      borderRadius: 25,
      child: ExpansionTile(
        shape: const RoundedRectangleBorder(side: BorderSide.none),
        tilePadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              order.userName,
              style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 18),
            ),
            const SizedBox(height: 2),
            Text(
              "# ${order.id.substring(0, 12).toUpperCase()}",
              style: TextStyle(color: primaryColor, fontSize: 12, fontWeight: FontWeight.bold),
            ),
          ],
        ),
        trailing: _statusChip(order.status),
        childrenPadding: const EdgeInsets.fromLTRB(20, 0, 20, 20),
        children: [
          const Divider(height: 20),
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
          const SizedBox(height: 15),
          
          _infoRow(Icons.phone_iphone_rounded, "Contact", order.userPhone, primaryColor),
          const SizedBox(height: 12),
          _infoRow(Icons.location_on_rounded, "Delivery", order.userAddress, primaryColor),
          
          const SizedBox(height: 20),
          Align(
            alignment: Alignment.centerLeft,
            child: Text(
              "ORDER ITEMS",
              style: TextStyle(fontWeight: FontWeight.w900, fontSize: 12, color: Colors.grey[600], letterSpacing: 1.2),
            ),
          ),
          const SizedBox(height: 10),
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: Colors.grey[50],
              borderRadius: BorderRadius.circular(15),
              border: Border.all(color: Colors.grey[200]!),
            ),
            child: Column(
              children: order.items.map((item) {
                return Padding(
                  padding: const EdgeInsets.only(bottom: 8),
                  child: Row(
                    children: [
                      Container(width: 6, height: 6, decoration: BoxDecoration(color: primaryColor.withValues(alpha: 0.3), shape: BoxShape.circle)),
                      const SizedBox(width: 10),
                      Expanded(
                        child: Text(item.product.name, style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 13)),
                      ),
                      Text(
                        "${NumberFormat('#,##,###').format(item.product.price.toInt())} x ${item.quantity} = ",
                        style: TextStyle(color: Colors.grey[600], fontSize: 12),
                      ),
                      Text(
                        "৳ ${NumberFormat('#,##,###').format((item.product.price * item.quantity).toInt())}",
                        style: TextStyle(color: primaryColor, fontWeight: FontWeight.w900, fontSize: 13),
                      ),
                    ],
                  ),
                );
              }).toList(),
            ),
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text("Total Amount", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
              Text(
                "৳ ${NumberFormat('#,##,###').format(order.totalAmount.toInt())}",
                style: TextStyle(fontWeight: FontWeight.w900, fontSize: 22, color: primaryColor),
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
    );
  }

  Widget _infoRow(IconData icon, String label, String value, Color primaryColor) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          padding: const EdgeInsets.all(6),
          decoration: BoxDecoration(color: primaryColor.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(8)),
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
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withValues(alpha: 0.3)),
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
    bool isDisabled = (status == 'Cancelled' && !canCancel) || (status != 'Cancelled' && order.status == 'Cancelled') || isCurrent;

    return Expanded(
      child: InkWell(
        onTap: isDisabled ? null : () async {
          if (status == 'Cancelled') {
            await OrderRepository().cancelOrder(order);
          } else {
            await OrderRepository().updateOrderStatus(order.id, status);
          }
        },
        child: Container(
          height: 45,
          decoration: BoxDecoration(
            color: isDisabled ? Colors.grey[100] : color.withValues(alpha: 0.1),
            borderRadius: BorderRadius.circular(12),
            border: Border.all(color: isDisabled ? Colors.grey[300]! : color, width: 1.5),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(icon, size: 16, color: isDisabled ? Colors.grey[400] : color),
              Text(status, style: TextStyle(fontSize: 9, fontWeight: FontWeight.w900, color: isDisabled ? Colors.grey[400] : color)),
            ],
          ),
        ),
      ),
    );
  }
}
