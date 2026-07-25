import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:image_picker/image_picker.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../widgets/custom_app_bar.dart';

class EditProfileScreen extends ConsumerStatefulWidget {
  const EditProfileScreen({super.key});

  @override
  ConsumerState<EditProfileScreen> createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends ConsumerState<EditProfileScreen> {
  late TextEditingController _nameController, _phoneController, _addressController, _shopNameController;
  bool _isSaving = false;
  File? _imageFile;
  final _picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    final user = ref.read(authNotifierProvider).value;
    _nameController = TextEditingController(text: user?.name ?? "");
    _phoneController = TextEditingController(text: user?.phoneNumber ?? "");
    _addressController = TextEditingController(text: user?.address ?? "");
    _shopNameController = TextEditingController(text: user?.shopName ?? "");
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
      final fileName = '$userId-${DateTime.now().millisecondsSinceEpoch}.jpg';
      final path = 'avatars/$fileName';
      await Supabase.instance.client.storage.from('app_images').upload(path, _imageFile!);
      final url = Supabase.instance.client.storage.from('app_images').getPublicUrl(path);
      return url;
    } catch (e) {
      debugPrint("Image upload error: $e");
      return null;
    }
  }

  Future<void> _updateProfile() async {
    FocusManager.instance.primaryFocus?.unfocus();
    setState(() => _isSaving = true);
    try {
      final user = ref.read(authNotifierProvider).value;
      if (user != null) {
        String? imageUrl = await _uploadImage(user.uid);
        
        final updateData = {
          'id': user.uid,
          'name': _nameController.text.trim(),
          'phoneNumber': _phoneController.text.trim(),
          'address': _addressController.text.trim(),
          'shopName': _shopNameController.text.trim(),
          'role': user.role,
        };
        
        if (imageUrl != null) updateData['imageUrl'] = imageUrl;

        await Supabase.instance.client.from('users').upsert(updateData);
        
        // Also update Supabase Auth metadata to keep them in sync
        await Supabase.instance.client.auth.updateUser(
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
        if (mounted) Navigator.pop(context);
      }
    } catch (e) {
      if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Error: $e")));
    } finally {
      if (mounted) setState(() => _isSaving = false);
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
          
          _isSaving 
            ? const Center(child: CircularProgressIndicator()) 
            : SingleChildScrollView(
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
      child: TextField(
        controller: controller,
        maxLines: lines,
        keyboardType: keyboardType,
        autofillHints: autofillHints,
        style: const TextStyle(fontWeight: FontWeight.w500),
        decoration: InputDecoration(
          labelText: label,
          prefixIcon: Icon(icon, color: Theme.of(context).primaryColor),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(20),
            borderSide: BorderSide.none,
          ),
          filled: true,
          fillColor: Colors.transparent,
          contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
        ),
      ),
    );
  }
}
