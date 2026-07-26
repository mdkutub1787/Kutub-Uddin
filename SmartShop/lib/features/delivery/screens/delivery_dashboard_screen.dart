import 'dart:async';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../order/models/order_model.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../user/models/user_model.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../core/utils/exit_dialog_helper.dart';

import '../../order/screens/order_tracking_screen.dart';

class DeliveryDashboardScreen extends ConsumerStatefulWidget {
  const DeliveryDashboardScreen({super.key});

  @override
  ConsumerState<DeliveryDashboardScreen> createState() => _DeliveryDashboardScreenState();
}

class _DeliveryDashboardScreenState extends ConsumerState<DeliveryDashboardScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  StreamSubscription<Position>? _positionStreamSubscription;
  bool _isTrackingStarted = false;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
  }

  @override
  void dispose() {
    _positionStreamSubscription?.cancel();
    _tabController.dispose();
    super.dispose();
  }

  Future<void> _startLocationTracking(UserModel user) async {
    if (_isTrackingStarted) return;
    _isTrackingStarted = true;
    
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      _isTrackingStarted = false;
      return;
    }

    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        _isTrackingStarted = false;
        return;
      }
    }
    if (permission == LocationPermission.deniedForever) {
      _isTrackingStarted = false;
      return;
    }

    _positionStreamSubscription?.cancel();
    _positionStreamSubscription = Geolocator.getPositionStream(
      locationSettings: const LocationSettings(
        accuracy: LocationAccuracy.high,
        distanceFilter: 10, // Only update if moved by 10 meters
      ),
    ).listen((Position position) {
      // Find all active orders for this user and update their location
      final myDeliveries = ref.read(myDeliveriesProvider(user.uid));
      if (myDeliveries.value != null) {
        for (var order in myDeliveries.value!) {
          if (order.status == 'Assigned' || order.status == 'OnTheWay' || order.status == 'PickedUp') {
            ref.read(orderNotifierProvider.notifier).updateOrderLocation(order.id, position.latitude, position.longitude);
          }
        }
      }
    });
  }

  void _stopLocationTracking() {
    _positionStreamSubscription?.cancel();
    _positionStreamSubscription = null;
    _isTrackingStarted = false;
  }

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authNotifierProvider);
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;
    final user = authState.value;

    if (user == null) return const Scaffold(body: Center(child: CircularProgressIndicator()));

    // Start or stop location tracking based on availability
    if (user.isAvailable == true && !_isTrackingStarted) {
      _startLocationTracking(user);
    } else if (user.isAvailable != true) {
      _stopLocationTracking();
    }

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
          bottom: TabBar(
            controller: _tabController,
            labelColor: Colors.white,
            unselectedLabelColor: Colors.white70,
            indicatorColor: Colors.white,
            indicatorWeight: 4,
            tabs: const [
              Tab(text: "New Requests"),
              Tab(text: "My Deliveries"),
            ],
          ),
          actions: [
            Row(
              children: [
                Text(
                  (user.isAvailable ?? false) ? "ONLINE" : "OFFLINE",
                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                ),
                Switch(
                  value: user.isAvailable ?? false,
                  onChanged: (val) async {
                    // Update delivery availability
                    await ref.read(authNotifierProvider.notifier).updateDeliveryAvailability(val);
                  },
                  activeThumbColor: Colors.white,
                  activeTrackColor: Colors.greenAccent,
                ),
              ],
            ),
            const SizedBox(width: 8),
          ],
        ),
        body: TabBarView(
          controller: _tabController,
          children: [
            _buildNewRequestsTab(user, primaryColor),
            _buildMyDeliveriesTab(user, primaryColor),
          ],
        ),
      ),
    );
  }

  Widget _buildNewRequestsTab(UserModel user, Color primaryColor) {
    if (!(user.isAvailable ?? false)) {
      return _emptyState("You are offline", "Go online to receive delivery requests", Icons.offline_bolt_rounded);
    }

    final availableOrders = ref.watch(availableOrdersProvider);

    return availableOrders.when(
      data: (orders) {
        if (orders.isEmpty) {
          return _emptyState("No new requests", "Searching for nearby orders...", Icons.radar_rounded);
        }
        return ListView.builder(
          padding: const EdgeInsets.all(16),
          itemCount: orders.length,
          itemBuilder: (context, index) => _buildPoolOrderCard(context, orders[index], primaryColor, user),
        );
      },
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (e, st) => Center(child: Text('Error: $e')),
    );
  }

  Widget _buildMyDeliveriesTab(UserModel user, Color primaryColor) {
    final myDeliveries = ref.watch(myDeliveriesProvider(user.uid));

    return myDeliveries.when(
      data: (orders) {
        if (orders.isEmpty) {
          return _emptyState("No active deliveries", "Accept an order to start delivering", Icons.delivery_dining_rounded);
        }
        return ListView.builder(
          padding: const EdgeInsets.all(16),
          itemCount: orders.length,
          itemBuilder: (context, index) => _buildActiveOrderCard(context, orders[index], primaryColor),
        );
      },
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (e, st) => Center(child: Text('Error: $e')),
    );
  }

  Widget _emptyState(String title, String message, IconData icon) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: 80, color: Colors.grey[300]),
          const SizedBox(height: 16),
          Text(title, style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 18, color: Colors.grey)),
          const SizedBox(height: 4),
          Text(message, style: TextStyle(color: Colors.grey[400], fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }

  Widget _buildPoolOrderCard(BuildContext context, OrderModel order, Color primaryColor, UserModel user) {
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
                    const Text("NEW REQUEST", style: TextStyle(fontSize: 10, color: Colors.green, fontWeight: FontWeight.w900, letterSpacing: 1)),
                    Text("#${order.id.substring(order.id.length - 8)}", style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16)),
                  ],
                ),
                Text(
                  "৳ ${NumberFormat('#,##,###').format(order.deliveryFee.toInt())}",
                  style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 20, color: Colors.green),
                ),
              ],
            ),
            const Padding(
              padding: EdgeInsets.symmetric(vertical: 16),
              child: Divider(height: 1, thickness: 1, color: Color(0xFFF5F5F5)),
            ),
            _infoItem(Icons.location_on_rounded, "Delivery To", order.userAddress),
            const SizedBox(height: 24),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton.icon(
                onPressed: () async {
                  final success = await ref.read(orderNotifierProvider.notifier).acceptOrder(order, user);
                  if (success && context.mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Order Accepted!')));
                    _tabController.animateTo(1);
                  }
                },
                icon: const Icon(Icons.check_circle_rounded, size: 20),
                label: const Text("ACCEPT ORDER", style: TextStyle(fontSize: 14, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
                style: ElevatedButton.styleFrom(
                  backgroundColor: primaryColor,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                  elevation: 5,
                  shadowColor: primaryColor.withValues(alpha: 0.5),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildActiveOrderCard(BuildContext context, OrderModel order, Color primaryColor) {
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
                    const Text("ACTIVE ORDER", style: TextStyle(fontSize: 10, color: Colors.blue, fontWeight: FontWeight.w900, letterSpacing: 1)),
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
            const SizedBox(height: 16),
            _infoItem(Icons.attach_money_rounded, "Collect Amount", "৳ ${NumberFormat('#,##,###').format(order.totalAmount.toInt())}"),
            const SizedBox(height: 24),
            Row(
              children: [
                if (order.status == 'Assigned' || order.status == 'Pending' || order.status == 'Confirmed')
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
                if (order.status == 'PickedUp' || order.status == 'OnTheWay') ...[
                  const SizedBox(width: 8),
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (_) => OrderTrackingScreen(order: order))),
                      icon: const Icon(Icons.map_rounded, size: 18),
                      label: const Text("MAP", style: TextStyle(fontSize: 12, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.black, 
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                        elevation: 0,
                      ),
                    ),
                  ),
                ],
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
