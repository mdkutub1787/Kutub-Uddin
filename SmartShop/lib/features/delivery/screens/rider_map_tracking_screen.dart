import 'dart:async';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'package:url_launcher/url_launcher.dart';
import '../../order/models/order_model.dart';
import '../../../core/riverpod/settings_notifier.dart';

class RiderMapTrackingScreen extends ConsumerStatefulWidget {
  final OrderModel order;

  const RiderMapTrackingScreen({super.key, required this.order});

  @override
  ConsumerState<RiderMapTrackingScreen> createState() => _RiderMapTrackingScreenState();
}

class _RiderMapTrackingScreenState extends ConsumerState<RiderMapTrackingScreen> {
  final Completer<GoogleMapController> _controller = Completer<GoogleMapController>();
  StreamSubscription<Position>? _positionStreamSubscription;
  
  LatLng? _currentLocation;
  LatLng? _destinationLocation;
  List<LatLng> _routePoints = [];
  bool _isPickupPhase = false;
  
  BitmapDescriptor? _riderIcon;
  BitmapDescriptor? _destinationIcon;

  @override
  void initState() {
    super.initState();
    
    // Determine delivery phase
    _isPickupPhase = widget.order.status == 'Assigned' || widget.order.status == 'Confirmed' || widget.order.status == 'Pending';
    
    // Set destination based on phase
    if (_isPickupPhase && widget.order.shopLatitude != null && widget.order.shopLongitude != null) {
      _destinationLocation = LatLng(widget.order.shopLatitude!, widget.order.shopLongitude!);
    } else if (!_isPickupPhase && widget.order.customerLatitude != null && widget.order.customerLongitude != null) {
      _destinationLocation = LatLng(widget.order.customerLatitude!, widget.order.customerLongitude!);
    }

    _loadCustomMarkers();
    _startLocationTracking();
  }
  
  Future<void> _loadCustomMarkers() async {
    // Attempt to load standard marker icons or custom images
    _riderIcon = BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueBlue);
    _destinationIcon = BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueRed);
  }

  Future<void> _startLocationTracking() async {
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) return;

    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) return;
    }
    if (permission == LocationPermission.deniedForever) return;

    // Get initial position quickly
    Position initialPos = await Geolocator.getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
    if (mounted) {
      setState(() {
        _currentLocation = LatLng(initialPos.latitude, initialPos.longitude);
      });
      _fetchRoute();
      _adjustCameraBounds();
    }

    // Start listening to location changes
    _positionStreamSubscription = Geolocator.getPositionStream(
      locationSettings: const LocationSettings(
        accuracy: LocationAccuracy.high,
        distanceFilter: 10,
      ),
    ).listen((Position position) {
      if (mounted) {
        setState(() {
          _currentLocation = LatLng(position.latitude, position.longitude);
        });
        _fetchRoute();
      }
    });
  }

  Future<void> _fetchRoute() async {
    if (_currentLocation == null || _destinationLocation == null) return;
    
    try {
      final String url = 'http://router.project-osrm.org/route/v1/driving/${_currentLocation!.longitude},${_currentLocation!.latitude};${_destinationLocation!.longitude},${_destinationLocation!.latitude}?geometries=geojson';
      final response = await http.get(Uri.parse(url));
      
      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        if (data['routes'] != null && data['routes'].isNotEmpty) {
          final geometry = data['routes'][0]['geometry']['coordinates'] as List;
          setState(() {
            _routePoints = geometry.map((coord) => LatLng(coord[1] as double, coord[0] as double)).toList();
          });
        }
      }
    } catch (e) {
      // Ignore
    }
  }

  Future<void> _adjustCameraBounds() async {
    final GoogleMapController controller = await _controller.future;
    
    if (_currentLocation == null && _destinationLocation == null) return;

    if (_currentLocation != null && _destinationLocation == null) {
      controller.animateCamera(CameraUpdate.newLatLngZoom(_currentLocation!, 15));
      return;
    }
    
    if (_currentLocation == null && _destinationLocation != null) {
      controller.animateCamera(CameraUpdate.newLatLngZoom(_destinationLocation!, 15));
      return;
    }

    final double minLat = _currentLocation!.latitude < _destinationLocation!.latitude ? _currentLocation!.latitude : _destinationLocation!.latitude;
    final double maxLat = _currentLocation!.latitude > _destinationLocation!.latitude ? _currentLocation!.latitude : _destinationLocation!.latitude;
    final double minLng = _currentLocation!.longitude < _destinationLocation!.longitude ? _currentLocation!.longitude : _destinationLocation!.longitude;
    final double maxLng = _currentLocation!.longitude > _destinationLocation!.longitude ? _currentLocation!.longitude : _destinationLocation!.longitude;
    
    controller.animateCamera(
      CameraUpdate.newLatLngBounds(
        LatLngBounds(
          southwest: LatLng(minLat, minLng),
          northeast: LatLng(maxLat, maxLng),
        ),
        100.0, // padding
      )
    );
  }

  @override
  void dispose() {
    _positionStreamSubscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;

    Set<Marker> markers = {};
    
    if (_destinationLocation != null) {
      markers.add(
        Marker(
          markerId: const MarkerId('destination'),
          position: _destinationLocation!,
          icon: _destinationIcon ?? BitmapDescriptor.defaultMarker,
          infoWindow: InfoWindow(title: _isPickupPhase ? 'Shop Location' : 'Customer Location'),
        ),
      );
    }
    
    if (_currentLocation != null) {
      markers.add(
        Marker(
          markerId: const MarkerId('rider'),
          position: _currentLocation!,
          icon: _riderIcon ?? BitmapDescriptor.defaultMarker,
          infoWindow: const InfoWindow(title: 'You are here'),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('Live Navigation', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.my_location),
            onPressed: () => _adjustCameraBounds(),
          )
        ],
      ),
      body: Stack(
        children: [
          GoogleMap(
            initialCameraPosition: CameraPosition(
              target: _currentLocation ?? _destinationLocation ?? const LatLng(0,0),
              zoom: _currentLocation != null || _destinationLocation != null ? 14 : 2,
            ),
            markers: markers,
            polylines: {
              if (_routePoints.isNotEmpty)
                Polyline(
                  polylineId: const PolylineId('route'),
                  points: _routePoints,
                  color: primaryColor,
                  width: 5,
                  jointType: JointType.round,
                ),
            },
            onMapCreated: (GoogleMapController controller) {
              _controller.complete(controller);
            },
            myLocationEnabled: false,
            zoomControlsEnabled: false,
            mapToolbarEnabled: false,
          ),
          
          // Bottom Card Overlay
          Align(
            alignment: Alignment.bottomCenter,
            child: Container(
              margin: const EdgeInsets.all(16),
              padding: const EdgeInsets.all(20),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(24),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.1),
                    blurRadius: 20,
                    offset: const Offset(0, 10),
                  )
                ]
              ),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      CircleAvatar(
                        backgroundColor: primaryColor.withValues(alpha: 0.1),
                        radius: 24,
                        child: Icon(_isPickupPhase ? Icons.storefront : Icons.person, color: primaryColor),
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(_isPickupPhase ? (widget.order.shopName ?? 'Shop') : widget.order.userName, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                            const SizedBox(height: 4),
                            Text(_isPickupPhase ? "Pick Up From" : "Deliver To", style: TextStyle(color: Colors.grey[600], fontSize: 13)),
                          ],
                        ),
                      ),
                      if (!_isPickupPhase)
                        IconButton(
                          onPressed: () {
                            launchUrl(Uri.parse('tel:${widget.order.userPhone}'));
                          },
                          style: IconButton.styleFrom(
                            backgroundColor: Colors.green.withValues(alpha: 0.1),
                            padding: const EdgeInsets.all(12),
                          ),
                          icon: const Icon(Icons.call, color: Colors.green),
                        ),
                    ],
                  ),
                  const Padding(
                    padding: EdgeInsets.symmetric(vertical: 16),
                    child: Divider(),
                  ),
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Icon(Icons.location_on, color: Colors.red, size: 20),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(_isPickupPhase ? "Shop Address" : "Delivery Address", style: const TextStyle(color: Colors.grey, fontSize: 12)),
                            const SizedBox(height: 4),
                            Text(
                              _isPickupPhase ? (widget.order.shopAddress ?? 'Unknown Address') : widget.order.userAddress, 
                              style: const TextStyle(fontWeight: FontWeight.w500, fontSize: 14),
                              maxLines: 2,
                              overflow: TextOverflow.ellipsis,
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
