import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:geolocator/geolocator.dart';
import '../../order/models/order_model.dart';
import '../../order/riverpod/order_notifier.dart';

class RiderMapTrackingScreen extends ConsumerStatefulWidget {
  final OrderModel order;
  const RiderMapTrackingScreen({super.key, required this.order});

  @override
  ConsumerState<RiderMapTrackingScreen> createState() => _RiderMapTrackingScreenState();
}

class _RiderMapTrackingScreenState extends ConsumerState<RiderMapTrackingScreen> {
  GoogleMapController? _controller;
  Position? _currentPosition;
  final Set<Marker> _markers = {};

  @override
  void initState() {
    super.initState();
    _getCurrentLocation();
  }

  Future<void> _getCurrentLocation() async {
    final position = await Geolocator.getCurrentPosition();
    setState(() {
      _currentPosition = position;
      _updateMarkers(position);
    });
  }

  void _updateMarkers(Position riderPos) {
    setState(() {
      _markers.clear();
      // Rider Marker
      _markers.add(Marker(
        markerId: const MarkerId('rider'),
        position: LatLng(riderPos.latitude, riderPos.longitude),
        icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueAzure),
        infoWindow: const InfoWindow(title: 'You are here'),
      ));

      // Customer Marker
      if (widget.order.customerLatitude != null && widget.order.customerLongitude != null) {
        _markers.add(Marker(
          markerId: const MarkerId('customer'),
          position: LatLng(widget.order.customerLatitude!, widget.order.customerLongitude!),
          icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueRed),
          infoWindow: const InfoWindow(title: 'Customer Location'),
        ));
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Delivery Navigation"),
        backgroundColor: Colors.black,
        foregroundColor: Colors.white,
      ),
      body: _currentPosition == null
          ? const Center(child: CircularProgressIndicator())
          : GoogleMap(
              initialCameraPosition: CameraPosition(
                target: LatLng(_currentPosition!.latitude, _currentPosition!.longitude),
                zoom: 15,
              ),
              markers: _markers,
              onMapCreated: (controller) => _controller = controller,
              myLocationEnabled: true,
              myLocationButtonEnabled: true,
            ),
      bottomNavigationBar: _buildBottomPanel(),
    );
  }

  Widget _buildBottomPanel() {
    return Container(
      padding: const EdgeInsets.all(24),
      decoration: const BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.vertical(top: Radius.circular(30)),
        boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 10)],
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Row(
            children: [
              const CircleAvatar(radius: 25, backgroundColor: Colors.blueAccent, child: Icon(Icons.person, color: Colors.white)),
              const SizedBox(width: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(widget.order.userName, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                  Text(widget.order.userPhone, style: const TextStyle(color: Colors.grey)),
                ],
              ),
              const Spacer(),
              IconButton(
                icon: const Icon(Icons.call, color: Colors.green),
                onPressed: () {},
              ),
            ],
          ),
          const SizedBox(height: 20),
          SizedBox(
            width: double.infinity,
            height: 55,
            child: ElevatedButton(
              onPressed: () async {
                 await ref.read(orderNotifierProvider.notifier).updateOrderStatus(widget.order.id, 'Delivered');
                 if (mounted) Navigator.pop(context);
              },
              style: ElevatedButton.styleFrom(backgroundColor: Colors.green, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15))),
              child: const Text("MARK AS DELIVERED", style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white)),
            ),
          ),
        ],
      ),
    );
  }
}
