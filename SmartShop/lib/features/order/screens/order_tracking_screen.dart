import 'dart:async';
import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../models/order_model.dart';
import '../../../core/riverpod/settings_notifier.dart';
import 'package:url_launcher/url_launcher.dart';

class OrderTrackingScreen extends ConsumerStatefulWidget {
  final OrderModel order;

  const OrderTrackingScreen({super.key, required this.order});

  @override
  ConsumerState<OrderTrackingScreen> createState() => _OrderTrackingScreenState();
}

class _OrderTrackingScreenState extends ConsumerState<OrderTrackingScreen> {
  final Completer<GoogleMapController> _controller = Completer<GoogleMapController>();
  RealtimeChannel? _subscription;
  
  LatLng? _deliveryLocation;
  final LatLng _destinationLocation = const LatLng(23.8103, 90.4125); // Replace with actual order destination parsing if available
  
  @override
  void initState() {
    super.initState();
    if (widget.order.deliveryLatitude != null && widget.order.deliveryLongitude != null) {
      _deliveryLocation = LatLng(widget.order.deliveryLatitude!, widget.order.deliveryLongitude!);
    }
    _setupRealtimeSubscription();
  }

  void _setupRealtimeSubscription() {
    // Listen to changes for this specific order
    _subscription = Supabase.instance.client
        .channel('public:orders:id=eq.${widget.order.id}')
        .onPostgresChanges(
          event: PostgresChangeEvent.update,
          schema: 'public',
          table: 'orders',
          filter: PostgresChangeFilter(
            type: PostgresChangeFilterType.eq,
            column: 'id',
            value: widget.order.id,
          ),
          callback: (payload) {
            final newLat = payload.newRecord['deliveryLatitude'];
            final newLng = payload.newRecord['deliveryLongitude'];
            
            if (newLat != null && newLng != null) {
              setState(() {
                _deliveryLocation = LatLng((newLat as num).toDouble(), (newLng as num).toDouble());
              });
              _adjustCameraBounds();
            }
          },
        )
        .subscribe();
  }
  
  Future<void> _adjustCameraBounds() async {
    if (_deliveryLocation == null) return;
    
    final GoogleMapController controller = await _controller.future;
    
    LatLngBounds bounds;
    if (_deliveryLocation!.latitude > _destinationLocation.latitude) {
      bounds = LatLngBounds(southwest: _destinationLocation, northeast: _deliveryLocation!);
    } else {
      bounds = LatLngBounds(southwest: _deliveryLocation!, northeast: _destinationLocation);
    }
    
    // Adjust bounds logic for all cases (SW to NE correctly)
    final double minLat = _deliveryLocation!.latitude < _destinationLocation.latitude ? _deliveryLocation!.latitude : _destinationLocation.latitude;
    final double maxLat = _deliveryLocation!.latitude > _destinationLocation.latitude ? _deliveryLocation!.latitude : _destinationLocation.latitude;
    final double minLng = _deliveryLocation!.longitude < _destinationLocation.longitude ? _deliveryLocation!.longitude : _destinationLocation.longitude;
    final double maxLng = _deliveryLocation!.longitude > _destinationLocation.longitude ? _deliveryLocation!.longitude : _destinationLocation.longitude;
    
    bounds = LatLngBounds(
      southwest: LatLng(minLat, minLng),
      northeast: LatLng(maxLat, maxLng),
    );

    controller.animateCamera(CameraUpdate.newLatLngBounds(bounds, 80)); // 80 is padding
  }

  @override
  void dispose() {
    _subscription?.unsubscribe();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    
    // Initial camera position (either delivery location or a default one)
    final CameraPosition initialPosition = CameraPosition(
      target: _deliveryLocation ?? _destinationLocation,
      zoom: 15.0,
    );

    // Markers
    final Set<Marker> markers = {};
    
    if (_deliveryLocation != null) {
      markers.add(
        Marker(
          markerId: const MarkerId('delivery_man'),
          position: _deliveryLocation!,
          infoWindow: const InfoWindow(title: 'Delivery Man', snippet: 'On the way'),
          icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueGreen),
          // We can replace with a bike icon later
        )
      );
    }
    
    // Assuming destination is known (for now we use a hardcoded dhaka location, 
    // but in a real app, parse lat/lng from userAddress or use geocoding)
    markers.add(
      Marker(
        markerId: const MarkerId('destination'),
        position: _destinationLocation,
        infoWindow: const InfoWindow(title: 'Delivery Destination'),
        icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueRed),
      )
    );

    return Scaffold(
      appBar: AppBar(
        title: const Text('Live Tracking', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: settings.primaryColor,
        foregroundColor: Colors.white,
      ),
      body: Stack(
        children: [
          GoogleMap(
            mapType: MapType.normal,
            initialCameraPosition: initialPosition,
            markers: markers,
            onMapCreated: (GoogleMapController controller) {
              _controller.complete(controller);
              if (_deliveryLocation != null) {
                Future.delayed(const Duration(milliseconds: 500), () {
                  _adjustCameraBounds();
                });
              }
            },
            myLocationEnabled: true,
          ),
          Positioned(
            left: 0,
            right: 0,
            bottom: 0,
            child: Container(
              padding: const EdgeInsets.all(24),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: const BorderRadius.vertical(top: Radius.circular(30)),
                boxShadow: [
                  BoxShadow(color: Colors.black.withValues(alpha: 0.1), blurRadius: 20, offset: const Offset(0, -5))
                ]
              ),
              child: SafeArea(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    // Drag Handle
                    Container(
                      width: 40,
                      height: 5,
                      decoration: BoxDecoration(
                        color: Colors.grey[300],
                        borderRadius: BorderRadius.circular(10),
                      ),
                    ),
                    const SizedBox(height: 20),
                    
                    // Status text
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              widget.order.status == 'PickedUp' || widget.order.status == 'OnTheWay' 
                                  ? "On the way to deliver" 
                                  : "Preparing your order",
                              style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 20, letterSpacing: -0.5),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              "Arriving in 15-20 mins", // Placeholder for actual ETA
                              style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold, fontSize: 14),
                            ),
                          ],
                        ),
                        Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: settings.primaryColor.withValues(alpha: 0.1),
                            shape: BoxShape.circle,
                          ),
                          child: Icon(Icons.timer_outlined, color: settings.primaryColor),
                        ),
                      ],
                    ),
                    
                    const SizedBox(height: 24),
                    const Divider(height: 1),
                    const SizedBox(height: 24),
                    
                    // Rider Info
                    Row(
                      children: [
                        // Avatar
                        Container(
                          padding: const EdgeInsets.all(3),
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            border: Border.all(color: Colors.grey[200]!, width: 2),
                          ),
                          child: widget.order.deliveryManImage != null && widget.order.deliveryManImage!.isNotEmpty
                              ? CircleAvatar(
                                  radius: 26,
                                  backgroundImage: NetworkImage(widget.order.deliveryManImage!),
                                )
                              : CircleAvatar(
                                  backgroundColor: settings.primaryColor.withValues(alpha: 0.1),
                                  radius: 26,
                                  child: Icon(Icons.delivery_dining_rounded, color: settings.primaryColor, size: 30),
                                ),
                        ),
                        const SizedBox(width: 16),
                        
                        // Details
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                widget.order.deliveryManName ?? 'Delivery Man',
                                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                              ),
                              const SizedBox(height: 2),
                              Row(
                                children: [
                                  Icon(Icons.star_rounded, color: Colors.orange[400], size: 16),
                                  const SizedBox(width: 4),
                                  const Text("4.9", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                                  const SizedBox(width: 8),
                                  Text(
                                    "• ${widget.order.deliveryManPhone ?? 'Contact via Support'}",
                                    style: const TextStyle(color: Colors.grey, fontSize: 13),
                                  ),
                                ],
                              ),
                            ],
                          ),
                        ),
                        
                        // Call Button
                        Container(
                          decoration: BoxDecoration(
                            color: Colors.green,
                            borderRadius: BorderRadius.circular(15),
                            boxShadow: [
                              BoxShadow(
                                color: Colors.green.withValues(alpha: 0.3),
                                blurRadius: 8,
                                offset: const Offset(0, 4),
                              )
                            ],
                          ),
                          child: IconButton(
                            onPressed: () async {
                              final phone = widget.order.deliveryManPhone;
                              if (phone != null && phone.isNotEmpty) {
                                final Uri uri = Uri(scheme: 'tel', path: phone);
                                if (await canLaunchUrl(uri)) {
                                  await launchUrl(uri);
                                }
                              }
                            },
                            icon: const Icon(Icons.call_rounded, color: Colors.white),
                            tooltip: "Call Delivery Man",
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          )
        ],
      ),
    );
  }
}
