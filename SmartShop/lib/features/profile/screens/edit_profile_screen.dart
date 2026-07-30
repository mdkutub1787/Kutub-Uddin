import '../../delivery/riverpod/zone_notifier.dart';
import '../../delivery/models/delivery_zone_model.dart';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:image_picker/image_picker.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/loading_overlay.dart';

class EditProfileScreen extends ConsumerStatefulWidget {
  const EditProfileScreen({super.key});

  @override
  ConsumerState<EditProfileScreen> createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends ConsumerState<EditProfileScreen> {
  late TextEditingController _nameController, _phoneController, _addressController, _shopNameController;
  File? _imageFile;
  final _picker = ImagePicker();
  String? _selectedZoneId;
  String? _selectedZoneName;

  @override
  void initState() {
    super.initState();
    final user = ref.read(authNotifierProvider).value;
    _nameController = TextEditingController(text: user?.name ?? "");
    _phoneController = TextEditingController(text: user?.phoneNumber ?? "");
    _addressController = TextEditingController(text: user?.address ?? "");
    _shopNameController = TextEditingController(text: user?.shopName ?? "");
    _selectedZoneId = user?.deliveryZoneId;
    _selectedZoneName = user?.deliveryZoneName;
  }

  Future<void> _pickImage() async {
    final pickedFile = await _picker.pickImage(source: ImageSource.gallery, imageQuality: 70);
    if (pickedFile != null) {
      setState(() => _imageFile = File(pickedFile.path));
    }
  }

  Future<String?> _uploadImage(String userId) async {
    if (_imageFile == null) return null;
    try {
      final supabase = ref.read(supabaseClientProvider);
      final fileName = '$userId-${DateTime.now().millisecondsSinceEpoch}.jpg';
      final path = 'avatars/$fileName';
      await supabase.storage.from(AppConstants.appImagesBucket).upload(path, _imageFile!);
      final url = supabase.storage.from(AppConstants.appImagesBucket).getPublicUrl(path);
      return url;
    } catch (e) {
      debugPrint("Image upload error: $e");
      return null;
    }
  }

  Future<void> _updateProfile() async {
    FocusManager.instance.primaryFocus?.unfocus();
    LoadingOverlay.show(context);
    try {
      final user = ref.read(authNotifierProvider).value;
      final supabase = ref.read(supabaseClientProvider);
      if (user != null) {
        String? imageUrl = await _uploadImage(user.uid);
        
        final updateData = {
          'id': user.uid,
          'name': _nameController.text.trim(),
          'phoneNumber': _phoneController.text.trim(),
          'address': _addressController.text.trim(),
          'shopName': _shopNameController.text.trim(),
          'role': user.role,
          'deliveryZoneId': _selectedZoneId,
          'deliveryZoneName': _selectedZoneName,
        };
        
        if (imageUrl != null) updateData['imageUrl'] = imageUrl;

        await supabase.from(AppConstants.usersTable).upsert(updateData);
        
        // Also update Supabase Auth metadata to keep them in sync
        await supabase.auth.updateUser(
          UserAttributes(
            data: {
              'full_name': _nameController.text.trim(),
              'phone_number': _phoneController.text.trim(),
              'address': _addressController.text.trim(),
              'shop_name': _shopNameController.text.trim(),
              if (imageUrl != null) 'image_url': imageUrl,
            },
          ),
        );

        await ref.read(authNotifierProvider.notifier).refreshUserData();
        if (mounted) {
          LoadingOverlay.hide(context);
          Navigator.pop(context);
        }
      } else {
        if (mounted) LoadingOverlay.hide(context);
      }
    } catch (e) {
      if (mounted) {
        LoadingOverlay.hide(context);
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Error: $e")));
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final user = ref.watch(authNotifierProvider).value;
    final primaryColor = Theme.of(context).primaryColor;
    final size = MediaQuery.of(context).size;

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: const CustomAppBar(
        title: "Edit Profile",
      ),
      body: Stack(
        children: [
          // Decorative Background Elements
          Positioned(
            top: -size.height * 0.1,
            right: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.4,
              backgroundColor: primaryColor.withValues(alpha: 0.05),
            ),
          ),
          Positioned(
            bottom: -size.height * 0.1,
            left: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.3,
              backgroundColor: primaryColor.withValues(alpha: 0.05),
            ),
          ),
          
          SingleChildScrollView(
            padding: const EdgeInsets.symmetric(horizontal: 28, vertical: 20),
            child: Column(
              children: [
                // Profile Image Picker
                Center(
                  child: Stack(
                    children: [
                      Container(
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          border: Border.all(color: primaryColor.withValues(alpha: 0.2), width: 4),
                        ),
                        child: CircleAvatar(
                          radius: 60,
                          backgroundColor: Colors.grey[200],
                          backgroundImage: _imageFile != null 
                            ? FileImage(_imageFile!) 
                            : (user?.imageUrl != null ? NetworkImage(user!.imageUrl!) : null) as ImageProvider?,
                          child: _imageFile == null && user?.imageUrl == null 
                            ? Icon(Icons.person, size: 60, color: Colors.grey[400]) 
                            : null,
                        ),
                      ),
                      Positioned(
                        bottom: 0,
                        right: 0,
                        child: GestureDetector(
                          onTap: _pickImage,
                          child: Container(
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                              color: primaryColor,
                              shape: BoxShape.circle,
                              boxShadow: [BoxShadow(color: Colors.black26, blurRadius: 10)],
                            ),
                            child: const Icon(Icons.camera_alt, color: Colors.white, size: 20),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 30),
                
                _field(_nameController, "Full Name", Icons.person_outline_rounded, autofillHints: [AutofillHints.name]),
                const SizedBox(height: 20),
                
                if (user?.role == 'owner' || user?.role == 'admin' || _shopNameController.text.isNotEmpty) ...[
                  _field(_shopNameController, "Shop Name", Icons.store_rounded),
                  const SizedBox(height: 20),
                ],
                
                _field(_phoneController, "Phone Number", Icons.phone_android_rounded, keyboardType: TextInputType.phone, autofillHints: [AutofillHints.telephoneNumber]),
                const SizedBox(height: 20),
                _field(_addressController, "Full Address", Icons.location_on_outlined, lines: 2, autofillHints: [AutofillHints.fullStreetAddress]),
                const SizedBox(height: 20),
                _buildZoneDropdown(primaryColor),
                const SizedBox(height: 40),
                
                SizedBox(
                  width: double.infinity,
                  height: 60,
                  child: ElevatedButton(
                    onPressed: _updateProfile,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: primaryColor,
                      foregroundColor: Colors.white,
                      elevation: 8,
                      shadowColor: primaryColor.withValues(alpha: 0.4),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                    ),
                    child: const Text(
                      "SAVE CHANGES",
                      style: TextStyle(
                        fontSize: 18, 
                        fontWeight: FontWeight.w900,
                        letterSpacing: 1.2
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _field(TextEditingController controller, String label, IconData icon, {int lines = 1, TextInputType? keyboardType, Iterable<String>? autofillHints}) {
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.03),
            blurRadius: 15,
            offset: const Offset(0, 5),
          )
        ],
      ),
      child: TextFormField(
        controller: controller,
        maxLines: lines,
        keyboardType: keyboardType,
        autofillHints: autofillHints,
        style: const TextStyle(fontWeight: FontWeight.w500),
        decoration: InputDecoration(
          labelText: label,
          prefixIcon: Icon(icon, color: Theme.of(context).primaryColor),
          border: InputBorder.none,
          filled: false,
          contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 15),
        ),
      ),
    );
  }

  Widget _buildZoneDropdown(Color primaryColor) {
    final activeZones = ref.watch(activeZonesProvider);
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.03),
            blurRadius: 15,
            offset: const Offset(0, 5),
          )
        ],
      ),
      child: activeZones.when(
        data: (zones) {
          if (zones.isEmpty) return const SizedBox.shrink();
          return Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
            child: Row(
              children: [
                Icon(Icons.map_outlined, color: primaryColor),
                const SizedBox(width: 15),
                Expanded(
                  child: DropdownButtonHideUnderline(
                    child: DropdownButton<String>(
                      isExpanded: true,
                      hint: const Text("Select your zone (optional)"),
                      value: _selectedZoneId,
                      items: zones.map((zone) {
                        return DropdownMenuItem<String>(
                          value: zone.id,
                          child: Text(zone.zoneName, style: const TextStyle(fontWeight: FontWeight.w500)),
                        );
                      }).toList(),
                      onChanged: (val) {
                        if (val != null) {
                          final selected = zones.firstWhere((z) => z.id == val);
                          setState(() {
                            _selectedZoneId = selected.id;
                            _selectedZoneName = selected.zoneName;
                          });
                        }
                      },
                    ),
                  ),
                ),
              ],
            ),
          );
        },
        loading: () => const Padding(padding: EdgeInsets.all(15), child: Center(child: CircularProgressIndicator())),
        error: (err, st) => Padding(padding: const EdgeInsets.all(15), child: Text("Failed to load zones", style: TextStyle(color: Colors.red[300]))),
      ),
    );
  }
}
