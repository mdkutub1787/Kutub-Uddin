import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../models/parcel_model.dart';
import '../riverpod/parcel_notifier.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/loading_overlay.dart';
import '../../../core/constants/constants.dart';
import 'package:geolocator/geolocator.dart';

class CreateParcelScreen extends ConsumerStatefulWidget {
  const CreateParcelScreen({super.key});

  @override
  ConsumerState<CreateParcelScreen> createState() => _CreateParcelScreenState();
}

class _CreateParcelScreenState extends ConsumerState<CreateParcelScreen> {
  final _formKey = GlobalKey<FormState>();
  
  final _pickupAddressCtrl = TextEditingController();
  final _receiverNameCtrl = TextEditingController();
  final _receiverPhoneCtrl = TextEditingController();
  final _deliveryAddressCtrl = TextEditingController();
  final _itemDescriptionCtrl = TextEditingController();
  final _weightCtrl = TextEditingController(text: '1.0');

  double _pickupLat = 0.0;
  double _pickupLng = 0.0;
  double _deliveryLat = 0.0;
  double _deliveryLng = 0.0;
  
  bool _isLoadingLocation = false;
  
  @override
  void initState() {
    super.initState();
    final user = ref.read(authNotifierProvider).value;
    if (user != null) {
      _pickupAddressCtrl.text = user.address ?? '';
    }
  }

  Future<void> _getCurrentLocation() async {
    setState(() => _isLoadingLocation = true);
    try {
      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
      }
      if (permission == LocationPermission.whileInUse || permission == LocationPermission.always) {
        Position position = await Geolocator.getCurrentPosition(desiredAccuracy: LocationAccuracy.high).timeout(const Duration(seconds: 5));
        _pickupLat = position.latitude;
        _pickupLng = position.longitude;
        if (mounted) ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Location fetched successfully!")));
      }
    } catch (e) {
      if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Could not get location: $e")));
    } finally {
      if (mounted) setState(() => _isLoadingLocation = false);
    }
  }

  double _calculateCharge(double weight) {
    // Base logic for P2P parcel: base 60 Tk, +20 Tk per kg over 1kg
    double charge = 60.0;
    if (weight > 1.0) charge += (weight - 1.0).ceil() * 20.0;
    return charge;
  }

  void _submitParcel() async {
    if (!_formKey.currentState!.validate()) return;
    final user = ref.read(authNotifierProvider).value;
    if (user == null) return;

    LoadingOverlay.show(context);
    
    double weight = double.tryParse(_weightCtrl.text) ?? 1.0;
    
    final parcel = ParcelModel(
      id: '', // Supabase handles UUID
      senderId: user.uid,
      senderName: user.name ?? 'Sender',
      senderPhone: user.phoneNumber ?? '',
      pickupAddress: _pickupAddressCtrl.text.trim(),
      pickupLatitude: _pickupLat,
      pickupLongitude: _pickupLng,
      receiverName: _receiverNameCtrl.text.trim(),
      receiverPhone: _receiverPhoneCtrl.text.trim(),
      dropoffAddress: _deliveryAddressCtrl.text.trim(),
      dropoffLatitude: _deliveryLat,
      dropoffLongitude: _deliveryLng,
      parcelType: _itemDescriptionCtrl.text.trim(),
      weightKg: weight,
      deliveryCharge: _calculateCharge(weight),
      deliveryZoneId: user.deliveryZoneId ?? '',
      createdAt: DateTime.now(),
      status: 'Pending',
    );

    final success = await ref.read(parcelNotifierProvider.notifier).createParcel(parcel);
    if (mounted) LoadingOverlay.hide(context);
    
    if (true) {
      if (mounted) {
        Navigator.pop(context);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Parcel request created successfully!")));
      }
    } else {
      if (mounted) ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Failed to create parcel request.")));
    }
  }

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;
    
    double currentWeight = double.tryParse(_weightCtrl.text) ?? 1.0;
    double charge = _calculateCharge(currentWeight);

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: const CustomAppBar(title: "Send Parcel"),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildSectionTitle("Pickup Details", primaryColor),
              Card(
                elevation: 2,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      Row(
                        children: [
                          Expanded(
                            child: TextFormField(
                              controller: _pickupAddressCtrl,
                              decoration: const InputDecoration(labelText: "Pickup Address", prefixIcon: Icon(Icons.my_location)),
                              validator: (v) => v!.isEmpty ? "Required" : null,
                            ),
                          ),
                          IconButton(
                            icon: _isLoadingLocation ? const SizedBox(width: 20, height: 20, child: CircularProgressIndicator(strokeWidth: 2)) : const Icon(Icons.gps_fixed, color: Colors.blue),
                            onPressed: _getCurrentLocation,
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 20),
              
              _buildSectionTitle("Receiver Details", primaryColor),
              Card(
                elevation: 2,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      TextFormField(
                        controller: _receiverNameCtrl,
                        decoration: const InputDecoration(labelText: "Receiver Name", prefixIcon: Icon(Icons.person)),
                        validator: (v) => v!.isEmpty ? "Required" : null,
                      ),
                      const SizedBox(height: 12),
                      TextFormField(
                        controller: _receiverPhoneCtrl,
                        decoration: const InputDecoration(labelText: "Receiver Phone", prefixIcon: Icon(Icons.phone)),
                        keyboardType: TextInputType.phone,
                        validator: (v) => v!.isEmpty ? "Required" : null,
                      ),
                      const SizedBox(height: 12),
                      TextFormField(
                        controller: _deliveryAddressCtrl,
                        decoration: const InputDecoration(labelText: "Delivery Address", prefixIcon: Icon(Icons.location_on)),
                        validator: (v) => v!.isEmpty ? "Required" : null,
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 20),
              
              _buildSectionTitle("Parcel Details", primaryColor),
              Card(
                elevation: 2,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      TextFormField(
                        controller: _itemDescriptionCtrl,
                        decoration: const InputDecoration(labelText: "What are you sending? (e.g. Documents, Clothes)", prefixIcon: Icon(Icons.description)),
                        validator: (v) => v!.isEmpty ? "Required" : null,
                      ),
                      const SizedBox(height: 12),
                      TextFormField(
                        controller: _weightCtrl,
                        decoration: const InputDecoration(labelText: "Weight (KG)", prefixIcon: Icon(Icons.scale)),
                        keyboardType: TextInputType.number,
                        onChanged: (val) => setState(() {}),
                        validator: (v) => v!.isEmpty ? "Required" : null,
                      ),
                    ],
                  ),
                ),
              ),
              
              const SizedBox(height: 30),
              
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: primaryColor.withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(16),
                  border: Border.all(color: primaryColor.withValues(alpha: 0.3)),
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text("Estimated Charge:", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                    Text("৳${charge.toInt()}", style: TextStyle(fontWeight: FontWeight.w900, fontSize: 20, color: primaryColor)),
                  ],
                ),
              ),
              
              const SizedBox(height: 30),
              
              SizedBox(
                width: double.infinity,
                height: 55,
                child: ElevatedButton(
                  onPressed: _submitParcel,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: primaryColor,
                    foregroundColor: Colors.white,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                  ),
                  child: const Text("REQUEST PARCEL DELIVERY", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, letterSpacing: 1.2)),
                ),
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildSectionTitle(String title, Color color) {
    return Padding(
      padding: const EdgeInsets.only(left: 4, bottom: 8),
      child: Text(
        title,
        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: color),
      ),
    );
  }
}
