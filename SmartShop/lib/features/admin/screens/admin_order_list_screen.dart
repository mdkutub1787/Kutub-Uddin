import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../order/models/order_model.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../user/models/user_model.dart';
import '../../user/riverpod/user_notifier.dart';
import '../../../widgets/app_card.dart';
import '../../../core/riverpod/settings_notifier.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:intl/intl.dart';
import '../../order/services/pdf_invoice_service.dart';
import 'admin_invoice_preview_screen.dart';
import '../../order/screens/order_tracking_screen.dart';

class AdminOrderListScreen extends ConsumerStatefulWidget {
  const AdminOrderListScreen({super.key});

  @override
  ConsumerState<AdminOrderListScreen> createState() => _AdminOrderListScreenState();
}

class _AdminOrderListScreenState extends ConsumerState<AdminOrderListScreen> with SingleTickerProviderStateMixin {
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
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;
    
    final orderState = ref.watch(orderNotifierProvider);
    final isLoading = orderState.isLoading;
    final allOrders = orderState.value ?? [];

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
      body: isLoading && allOrders.isEmpty
          ? const Center(child: CircularProgressIndicator())
          : allOrders.isEmpty
              ? _emptyState()
              : TabBarView(
                  controller: _tabController,
                  children: List.generate(5, (index) {
                    final filteredOrders = _filterOrders(allOrders, index);
                    
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
                ),
    );
  }

  void _showAssignDeliverySheet(BuildContext context, OrderModel order) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (context) => Container(
        height: MediaQuery.of(context).size.height * 0.7,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.vertical(top: Radius.circular(30)),
        ),
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Center(child: Container(width: 40, height: 4, decoration: BoxDecoration(color: Colors.grey[300], borderRadius: BorderRadius.circular(2)))),
            const SizedBox(height: 24),
            const Text("Assign Delivery Man", style: TextStyle(fontSize: 20, fontWeight: FontWeight.w900)),
            const Text("Select an available delivery person for this order", style: TextStyle(color: Colors.grey)),
            const SizedBox(height: 24),
            Expanded(
              child: Consumer(
                builder: (context, ref, child) {
                  final userState = ref.watch(userNotifierProvider);
                  if (userState.isLoading) return const Center(child: CircularProgressIndicator());
                  
                  final users = userState.value ?? [];
                  final deliveryMen = users.where((u) => u.role == 'delivery_man' && u.isActive).toList();
                  
                  if (deliveryMen.isEmpty) {
                    return Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(Icons.person_off_rounded, size: 64, color: Colors.grey[300]),
                          const SizedBox(height: 16),
                          const Text("No delivery men available", style: TextStyle(fontWeight: FontWeight.bold, color: Colors.grey)),
                        ],
                      ),
                    );
                  }

                  return ListView.builder(
                    itemCount: deliveryMen.length,
                    itemBuilder: (context, index) {
                      final dm = deliveryMen[index];
                      return ListTile(
                        leading: CircleAvatar(backgroundColor: Colors.blue.withValues(alpha: 0.1), child: const Icon(Icons.person, color: Colors.blue)),
                        title: Text(dm.name, style: const TextStyle(fontWeight: FontWeight.bold)),
                        subtitle: Text(dm.phoneNumber),
                        trailing: const Icon(Icons.chevron_right_rounded),
                        onTap: () async {
                          // await ref.read(orderNotifierProvider.notifier).assignDeliveryMan(order.id, dm);
                          if (context.mounted) Navigator.pop(context);
                        },
                      );
                    },
                  );
                },
              ),
            ),
          ],
        ),
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
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;
    bool isPos = order.orderType == 'pos';
    
    return AppCard(
      margin: const EdgeInsets.only(bottom: 16),
      borderRadius: 20,
      child: ExpansionTile(
        shape: const RoundedRectangleBorder(side: BorderSide.none),
        tilePadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        title: Row(
          children: [
            Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: (isPos ? Colors.teal : Colors.blue).withValues(alpha: 0.1),
                shape: BoxShape.circle,
              ),
              child: Icon(
                isPos ? Icons.storefront_rounded : Icons.language_rounded,
                size: 20,
                color: isPos ? Colors.teal : Colors.blue,
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    order.userName,
                    style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16, letterSpacing: -0.5),
                  ),
                  const SizedBox(height: 2),
                  Row(
                    children: [
                      Text(
                        order.id.length > 8 ? "#${order.id.substring(order.id.length - 8)}" : "#${order.id}",
                        style: TextStyle(color: Colors.grey[500], fontSize: 11, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(width: 8),
                      Text(
                        DateFormat('dd MMM yyyy, hh:mm a').format(order.date),
                        style: TextStyle(color: Colors.grey[400], fontSize: 10),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(
                  "৳${NumberFormat('#,##,###').format(order.totalAmount.toInt())}",
                  style: TextStyle(fontWeight: FontWeight.w900, fontSize: 16, color: primaryColor),
                ),
                const SizedBox(height: 4),
                _statusChip(order.status),
              ],
            ),
          ],
        ),
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
              TextButton.icon(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => AdminInvoicePreviewScreen(
                        order: order,
                        shopName: settings.shopName,
                      ),
                    ),
                  );
                },
                icon: const Icon(Icons.picture_as_pdf_rounded, color: Colors.redAccent),
                label: const Text("Download Invoice", style: TextStyle(fontWeight: FontWeight.bold, color: Colors.redAccent)),
                style: TextButton.styleFrom(
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                  backgroundColor: Colors.redAccent.withValues(alpha: 0.1),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  const Text("Total Amount", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12, color: Colors.grey)),
                  Text(
                    "৳ ${NumberFormat('#,##,###').format(order.totalAmount.toInt())}",
                    style: TextStyle(fontWeight: FontWeight.w900, fontSize: 22, color: primaryColor),
                  ),
                ],
              ),
            ],
          ),
          const SizedBox(height: 24),
          if (order.orderType != 'pos')
            Column(
              children: [
                Row(
                  children: [
                    _actionBtn(context, order, "Shipped", Colors.indigo, Icons.local_shipping_rounded),
                    const SizedBox(width: 10),
                    _actionBtn(context, order, "Delivered", Colors.green[700]!, Icons.verified_rounded),
                    const SizedBox(width: 10),
                    _actionBtn(context, order, "Cancelled", Colors.red[700]!, Icons.cancel_rounded),
                  ],
                ),
                const SizedBox(height: 12),
                if (order.status == 'Pending' || order.status == 'Confirmed')
                  SizedBox(
                    width: double.infinity,
                    child: OutlinedButton.icon(
                      onPressed: () => _showAssignDeliverySheet(context, order),
                      icon: const Icon(Icons.person_add_alt_1_rounded),
                      label: Text(order.deliveryManId == null ? "ASSIGN DELIVERY MAN" : "CHANGE DELIVERY MAN"),
                      style: OutlinedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 12),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        side: BorderSide(color: primaryColor),
                        foregroundColor: primaryColor,
                      ),
                    ),
                  ),
                if (order.deliveryManName != null)
                  Padding(
                    padding: const EdgeInsets.only(top: 12),
                    child: Container(
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: Colors.blue.withValues(alpha: 0.05),
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(color: Colors.blue.withValues(alpha: 0.1)),
                      ),
                      child: Row(
                        children: [
                          const Icon(Icons.delivery_dining_rounded, color: Colors.blue),
                          const SizedBox(width: 12),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Text("Assigned Delivery Man", style: TextStyle(fontSize: 10, color: Colors.grey, fontWeight: FontWeight.bold)),
                                Text(order.deliveryManName!, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                              ],
                            ),
                          ),
                          Text(order.deliveryManPhone ?? "", style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w500)),
                        ],
                      ),
                    ),
                  ),
                if ((order.status == 'PickedUp' || order.status == 'OnTheWay') && order.deliveryManId != null)
                  Padding(
                    padding: const EdgeInsets.only(top: 12),
                    child: SizedBox(
                      width: double.infinity,
                      child: ElevatedButton.icon(
                        onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (_) => OrderTrackingScreen(order: order))),
                        icon: const Icon(Icons.map_rounded),
                        label: const Text("TRACK LIVE MAP", style: TextStyle(fontWeight: FontWeight.w900, letterSpacing: 1)),
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.black,
                          foregroundColor: Colors.white,
                          padding: const EdgeInsets.symmetric(vertical: 14),
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        ),
                      ),
                    ),
                  ),
              ],
            )
          else
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Colors.green.withValues(alpha: 0.05),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: Colors.green.withValues(alpha: 0.1)),
              ),
              child: const Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.check_circle_rounded, color: Colors.green, size: 16),
                  const SizedBox(width: 8),
                  Text(
                    "DIRECT STORE SALE COMPLETED",
                    style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold, fontSize: 11, letterSpacing: 0.5),
                  ),
                ],
              ),
            ),
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
            await ref.read(orderNotifierProvider.notifier).cancelOrder(order);
          } else {
            await ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, status);
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
