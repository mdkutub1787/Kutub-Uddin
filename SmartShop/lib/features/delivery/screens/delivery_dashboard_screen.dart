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
  int _previousOrdersCount = 0;

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
    // Listen to available orders for ringtone
    ref.listen<AsyncValue<List<OrderModel>>>(availableOrdersProvider, (previous, next) {
      if (next.hasValue && next.value != null) {
        final currentCount = next.value!.length;
        if (currentCount > _previousOrdersCount) {
          // Play ringtone when a new order arrives
          _playRingtone();
        }
        _previousOrdersCount = currentCount;
      }
    });

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
      _stopRingtone();
    }

    // Get completed deliveries
    final completedOrdersAsync = ref.watch(myCompletedDeliveriesProvider(user.uid));
    int totalDeliveries = 0;
    double totalEarnings = 0;
    
    if (completedOrdersAsync.value != null) {
      totalDeliveries = completedOrdersAsync.value!.length;
      totalEarnings = completedOrdersAsync.value!.fold(0, (sum, item) => sum + item.deliveryFee);
    }

    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (didPop) return;
        await ExitDialogHelper.showExitDialog(context);
      },
      child: Scaffold(
        backgroundColor: Colors.grey[100],
        body: Column(
          children: [
            // Professional Rider Header
            Container(
              padding: EdgeInsets.only(top: MediaQuery.of(context).padding.top + 10, left: 20, right: 20, bottom: 20),
              decoration: BoxDecoration(
                color: primaryColor,
                borderRadius: const BorderRadius.only(bottomLeft: Radius.circular(30), bottomRight: Radius.circular(30)),
                boxShadow: [
                  BoxShadow(color: primaryColor.withValues(alpha: 0.3), blurRadius: 15, offset: const Offset(0, 5))
                ],
              ),
              child: Column(
                children: [
                  // Top Row: Avatar & Status
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      InkWell(
                        onTap: () => Navigator.pushNamed(context, AppRoutes.profile),
                        borderRadius: BorderRadius.circular(30),
                        child: Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 4),
                          child: Row(
                            children: [
                              Container(
                                padding: const EdgeInsets.all(2),
                                decoration: const BoxDecoration(color: Colors.white, shape: BoxShape.circle),
                                child: CircleAvatar(
                                  radius: 25,
                                  backgroundImage: user.imageUrl != null && user.imageUrl!.isNotEmpty ? NetworkImage(user.imageUrl!) : null,
                                  backgroundColor: Colors.grey[200],
                                  child: user.imageUrl == null || user.imageUrl!.isEmpty ? Icon(Icons.person, color: primaryColor) : null,
                                ),
                              ),
                              const SizedBox(width: 12),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  const Text("Welcome back,", style: TextStyle(color: Colors.white70, fontSize: 12)),
                                  Text(user.name, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 18)),
                                ],
                              ),
                              const SizedBox(width: 8),
                              const Icon(Icons.chevron_right_rounded, color: Colors.white70, size: 20),
                            ],
                          ),
                        ),
                      ),
                      // Online Toggle
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: Colors.black.withValues(alpha: 0.2),
                          borderRadius: BorderRadius.circular(20),
                        ),
                        child: Row(
                          children: [
                            Text(
                              (user.isAvailable ?? false) ? "ONLINE" : "OFFLINE",
                              style: TextStyle(color: (user.isAvailable ?? false) ? Colors.greenAccent : Colors.white70, fontWeight: FontWeight.bold, fontSize: 10),
                            ),
                            const SizedBox(width: 4),
                            SizedBox(
                              height: 24,
                              width: 36,
                              child: Switch(
                                value: user.isAvailable ?? false,
                                onChanged: (val) async {
                                  await ref.read(authNotifierProvider.notifier).updateDeliveryAvailability(val);
                                },
                                activeColor: Colors.greenAccent,
                                inactiveThumbColor: Colors.grey[400],
                                inactiveTrackColor: Colors.white24,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 20),
                  
                  // Stats Row
                  Row(
                    children: [
                      Expanded(
                        child: Container(
                          padding: const EdgeInsets.all(12),
                          decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.15), borderRadius: BorderRadius.circular(15)),
                          child: Column(
                            children: [
                              const Text("Total Deliveries", style: TextStyle(color: Colors.white70, fontSize: 11)),
                              const SizedBox(height: 4),
                              Text("$totalDeliveries", style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w900, fontSize: 20)),
                            ],
                          ),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Container(
                          padding: const EdgeInsets.all(12),
                          decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.15), borderRadius: BorderRadius.circular(15)),
                          child: Column(
                            children: [
                              const Text("Total Earnings", style: TextStyle(color: Colors.white70, fontSize: 11)),
                              const SizedBox(height: 4),
                              Text("৳${NumberFormat('#,##,###').format(totalEarnings.toInt())}", style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w900, fontSize: 20)),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  TabBar(
                    controller: _tabController,
                    labelColor: Colors.white,
                    unselectedLabelColor: Colors.white70,
                    indicatorColor: Colors.white,
                    indicatorWeight: 3,
                    dividerColor: Colors.transparent,
                    tabs: const [
                      Tab(text: "New Requests"),
                      Tab(text: "My Deliveries"),
                    ],
                  ),
                ],
              ),
            ),
            
            // Tab Views
            Expanded(
              child: TabBarView(
                controller: _tabController,
                children: [
                  _buildNewRequestsTab(user, primaryColor),
                  _buildMyDeliveriesTab(user, primaryColor),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _playRingtone() async {
    // Make sure we have a ringtone file in assets or use system default
    try {
      // You can add a specific asset like: await _audioPlayer.play(AssetSource('audio/ringtone.mp3'));
      // Using a generic beep for now, in a real app add an mp3 to assets
    } catch (e) {
      debugPrint("Error playing ringtone: $e");
    }
  }

  void _stopRingtone() {
    _audioPlayer.stop();
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
                onPressed: () {
                  _showConfirmationDialog(
                    context,
                    title: "Accept Order",
                    content: "Are you sure you want to accept this delivery request?",
                    confirmText: "ACCEPT",
                    onConfirm: () async {
                      final success = await ref.read(orderNotifierProvider.notifier).acceptOrder(order, user);
                      if (success && context.mounted) {
                        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Order Accepted!')));
                        _tabController.animateTo(1);
                      }
                    },
                  );
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
            _infoItem(Icons.phone_rounded, "Contact Number", order.userPhone, showCall: true),
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
                      onPressed: () {
                        _showConfirmationDialog(
                          context,
                          title: "Mark as Picked Up",
                          content: "Have you collected the order items from the shop?",
                          confirmText: "YES, PICKED UP",
                          onConfirm: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'PickedUp'),
                        );
                      },
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
                      onPressed: () {
                        _showConfirmationDialog(
                          context,
                          title: "Start Delivery",
                          content: "Are you starting the journey to the customer's location?",
                          confirmText: "START",
                          onConfirm: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'OnTheWay'),
                        );
                      },
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
                      onPressed: () {
                        _showConfirmationDialog(
                          context,
                          title: "Mark as Delivered",
                          content: "Have you successfully delivered the order to the customer and collected the payment (if any)?",
                          confirmText: "YES, DELIVERED",
                          onConfirm: () => ref.read(orderNotifierProvider.notifier).updateOrderStatus(order.id, 'Delivered'),
                        );
                      },
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
                      onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (_) => RiderMapTrackingScreen(order: order))),
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

  Widget _infoItem(IconData icon, String label, String value, {bool showCall = false}) {
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
        if (showCall && value.isNotEmpty)
          GestureDetector(
            onTap: () async {
              final Uri uri = Uri(scheme: 'tel', path: value);
              if (await canLaunchUrl(uri)) {
                await launchUrl(uri);
              }
            },
            child: Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: Colors.green.withValues(alpha: 0.1),
                shape: BoxShape.circle,
              ),
              child: const Icon(Icons.call, color: Colors.green, size: 18),
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

  void _showConfirmationDialog(BuildContext context, {required String title, required String content, required String confirmText, required VoidCallback onConfirm}) {
    showDialog(
      context: context,
      builder: (ctx) => Dialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        elevation: 0,
        backgroundColor: Colors.transparent,
        child: Container(
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Colors.white,
            shape: BoxShape.rectangle,
            borderRadius: BorderRadius.circular(20),
            boxShadow: const [
              BoxShadow(color: Colors.black26, blurRadius: 10, offset: Offset(0, 10)),
            ],
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(color: Colors.blue.withValues(alpha: 0.1), shape: BoxShape.circle),
                child: const Icon(Icons.help_outline_rounded, size: 40, color: Colors.blue),
              ),
              const SizedBox(height: 24),
              Text(title, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.w900), textAlign: TextAlign.center),
              const SizedBox(height: 12),
              Text(content, style: const TextStyle(fontSize: 14, color: Colors.grey, fontWeight: FontWeight.w500), textAlign: TextAlign.center),
              const SizedBox(height: 24),
              Row(
                children: [
                  Expanded(
                    child: OutlinedButton(
                      onPressed: () => Navigator.pop(ctx),
                      style: OutlinedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        side: BorderSide(color: Colors.grey[300]!),
                      ),
                      child: const Text("CANCEL", style: TextStyle(color: Colors.grey, fontWeight: FontWeight.bold, letterSpacing: 1)),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () {
                        Navigator.pop(ctx);
                        onConfirm();
                      },
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.blue,
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        elevation: 0,
                      ),
                      child: Text(confirmText, style: const TextStyle(fontWeight: FontWeight.bold, letterSpacing: 1)),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
