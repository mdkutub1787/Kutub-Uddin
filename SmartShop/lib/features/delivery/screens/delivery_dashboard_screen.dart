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

import 'rider_map_tracking_screen.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:audioplayers/audioplayers.dart';
import '../../../routes/app_routes.dart';

class DeliveryDashboardScreen extends ConsumerStatefulWidget {
  const DeliveryDashboardScreen({super.key});

  @override
  ConsumerState<DeliveryDashboardScreen> createState() => _DeliveryDashboardScreenState();
}

class _DeliveryDashboardScreenState extends ConsumerState<DeliveryDashboardScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  StreamSubscription<Position>? _positionStreamSubscription;
  bool _isTrackingStarted = false;
  final AudioPlayer _audioPlayer = AudioPlayer();

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
  }

  @override
  void dispose() {
    _audioPlayer.dispose();
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

    _positionStreamSubscription = Geolocator.getPositionStream(
      locationSettings: const LocationSettings(accuracy: LocationAccuracy.high, distanceFilter: 10),
    ).listen((Position position) {
      final myDeliveries = ref.read(myDeliveriesProvider(user.uid)).value ?? [];
      for (var order in myDeliveries) {
        if (order.status == 'Assigned' || order.status == 'OnTheWay' || order.status == 'PickedUp') {
           ref.read(orderNotifierProvider.notifier).updateOrderLocation(order.id, position.latitude, position.longitude);
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
    final currency = settings.currencySymbol;
    final user = authState.value;

    if (user == null) return const Scaffold(body: Center(child: CircularProgressIndicator()));

    if (user.isAvailable == true && !_isTrackingStarted) {
      _startLocationTracking(user);
    } else if (user.isAvailable != true) {
      _stopLocationTracking();
    }

    final completedOrdersAsync = ref.watch(myCompletedDeliveriesProvider(user.uid));
    int totalDeliveries = completedOrdersAsync.value?.length ?? 0;
    double totalEarnings = completedOrdersAsync.value?.fold(0.0, (sum, item) => sum! + item.deliveryFee) ?? 0.0;

    return Scaffold(
        backgroundColor: Colors.grey[100],
        body: Column(
          children: [
            Container(
              padding: EdgeInsets.only(top: MediaQuery.of(context).padding.top + 10, left: 20, right: 20, bottom: 20),
              decoration: BoxDecoration(
                color: primaryColor,
                borderRadius: const BorderRadius.only(bottomLeft: Radius.circular(30), bottomRight: Radius.circular(30)),
                boxShadow: [BoxShadow(color: primaryColor.withValues(alpha: 0.3), blurRadius: 15, offset: const Offset(0, 5))],
              ),
              child: Column(
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Row(
                        children: [
                          CircleAvatar(
                            radius: 25,
                            backgroundImage: user.imageUrl != null && user.imageUrl!.isNotEmpty ? NetworkImage(user.imageUrl!) : null,
                            backgroundColor: Colors.white24,
                            child: user.imageUrl == null || user.imageUrl!.isEmpty ? const Icon(Icons.person, color: Colors.white) : null,
                          ),
                          const SizedBox(width: 12),
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text("Rider Panel", style: TextStyle(color: Colors.white70, fontSize: 12)),
                              Text(user.name, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 18)),
                            ],
                          ),
                        ],
                      ),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(color: Colors.black.withValues(alpha: 0.2), borderRadius: BorderRadius.circular(20)),
                        child: Row(
                          children: [
                            Text((user.isAvailable ?? false) ? "ONLINE" : "OFFLINE", style: TextStyle(color: (user.isAvailable ?? false) ? Colors.greenAccent : Colors.white70, fontWeight: FontWeight.bold, fontSize: 10)),
                            const SizedBox(width: 4),
                            Switch(
                              value: user.isAvailable ?? false,
                              onChanged: (val) => ref.read(authNotifierProvider.notifier).updateDeliveryAvailability(val),
                              activeColor: Colors.greenAccent,
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 20),
                  Row(
                    children: [
                      _statCard("Deliveries", "$totalDeliveries", Icons.shopping_bag),
                      const SizedBox(width: 12),
                      _statCard("Earnings", "$currency${totalEarnings.toInt()}", Icons.account_balance_wallet),
                    ],
                  ),
                  const SizedBox(height: 16),
                  TabBar(
                    controller: _tabController,
                    labelColor: Colors.white,
                    unselectedLabelColor: Colors.white70,
                    indicatorColor: Colors.white,
                    tabs: const [Tab(text: "New Requests"), Tab(text: "My Deliveries")],
                  ),
                ],
              ),
            ),
            Expanded(
              child: TabBarView(
                controller: _tabController,
                children: [
                  _buildNewRequestsTab(user, primaryColor, currency),
                  _buildMyDeliveriesTab(user, primaryColor, currency),
                ],
              ),
            ),
          ],
        ),
    );
  }

  Widget _statCard(String label, String value, IconData icon) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.15), borderRadius: BorderRadius.circular(15)),
        child: Column(
          children: [
            Text(label, style: const TextStyle(color: Colors.white70, fontSize: 11)),
            const SizedBox(height: 4),
            Text(value, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w900, fontSize: 20)),
          ],
        ),
      ),
    );
  }

  Widget _buildNewRequestsTab(UserModel user, Color primaryColor, String currency) {
    if (!(user.isAvailable ?? false)) return _emptyState("You are offline", "Go online to receive delivery requests", Icons.offline_bolt_rounded);
    final availableOrders = ref.watch(availableOrdersProvider);
    return availableOrders.when(
      data: (orders) => orders.isEmpty ? _emptyState("No new requests", "Searching for nearby orders...", Icons.radar_rounded) : ListView.builder(padding: const EdgeInsets.all(16), itemCount: orders.length, itemBuilder: (context, index) => _buildOrderCard(orders[index], primaryColor, true, user, currency)),
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (e, _) => Center(child: Text("Error: $e")),
    );
  }

  Widget _buildMyDeliveriesTab(UserModel user, Color primaryColor, String currency) {
    final myDeliveries = ref.watch(myDeliveriesProvider(user.uid));
    return myDeliveries.when(
      data: (orders) => orders.isEmpty ? _emptyState("No active deliveries", "Accept an order to start delivering", Icons.delivery_dining_rounded) : ListView.builder(padding: const EdgeInsets.all(16), itemCount: orders.length, itemBuilder: (context, index) => _buildOrderCard(orders[index], primaryColor, false, user, currency)),
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (e, _) => Center(child: Text("Error: $e")),
    );
  }

  Widget _buildOrderCard(OrderModel order, Color primaryColor, bool isPool, UserModel user, String currency) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("#${order.id.substring(order.id.length - 8)}", style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.grey)),
                _statusChip(order.status),
              ],
            ),
            const SizedBox(height: 16),
            _infoRow(Icons.person_rounded, "Customer", order.userName),
            const SizedBox(height: 8),
            _infoRow(Icons.location_on_rounded, "Address", order.userAddress),
            const SizedBox(height: 8),
            _infoRow(Icons.money, "Amount", "$currency${order.totalAmount.toInt()}"),
            const SizedBox(height: 20),
            Row(
              children: [
                if (isPool)
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () => ref.read(orderNotifierProvider.notifier).acceptOrder(order, user),
                      style: ElevatedButton.styleFrom(backgroundColor: primaryColor, foregroundColor: Colors.white, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)), padding: const EdgeInsets.symmetric(vertical: 14)),
                      child: const Text("ACCEPT REQUEST", style: TextStyle(fontWeight: FontWeight.bold)),
                    ),
                  )
                else ...[
                  Expanded(
                    child: OutlinedButton.icon(
                      onPressed: () => _callCustomer(order.userPhone),
                      icon: const Icon(Icons.call, size: 18),
                      label: const Text("CALL"),
                      style: OutlinedButton.styleFrom(shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12))),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (_) => RiderMapTrackingScreen(order: order))),
                      icon: const Icon(Icons.map_rounded, size: 18),
                      label: const Text("MAP"),
                      style: ElevatedButton.styleFrom(backgroundColor: Colors.black, foregroundColor: Colors.white, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12))),
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

  Widget _infoRow(IconData icon, String label, String value) {
    return Row(
      children: [
        Icon(icon, size: 18, color: Colors.grey),
        const SizedBox(width: 8),
        Text("$label: ", style: const TextStyle(color: Colors.grey, fontSize: 13)),
        Expanded(child: Text(value, style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 13), maxLines: 1, overflow: TextOverflow.ellipsis)),
      ],
    );
  }

  Widget _statusChip(String status) {
    Color color = Colors.orange;
    if (status == 'Delivered') color = Colors.green;
    if (status == 'OnTheWay') color = Colors.blue;
    return Container(padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4), decoration: BoxDecoration(color: color.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(8)), child: Text(status.toUpperCase(), style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 10)));
  }

  Widget _emptyState(String title, String sub, IconData icon) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: 80, color: Colors.grey[300]),
          const SizedBox(height: 16),
          Text(title, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
          Text(sub, style: const TextStyle(color: Colors.grey)),
        ],
      ),
    );
  }

  void _callCustomer(String phone) async {
    final Uri url = Uri(scheme: 'tel', path: phone);
    if (await canLaunchUrl(url)) await launchUrl(url);
  }
}
