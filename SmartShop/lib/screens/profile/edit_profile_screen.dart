import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:firebase_database/firebase_database.dart';
import '../../view_models/auth_view_model.dart';

class EditProfileScreen extends StatefulWidget {
  const EditProfileScreen({super.key});

  @override
  State<EditProfileScreen> createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends State<EditProfileScreen> {
  late TextEditingController _nameController, _phoneController, _addressController;
  bool _isSaving = false;

  @override
  void initState() {
    super.initState();
    final user = context.read<AuthViewModel>().user;
    _nameController = TextEditingController(text: user?.name ?? "");
    _phoneController = TextEditingController(text: user?.phoneNumber ?? "");
    _addressController = TextEditingController(text: user?.address ?? "");
  }

  Future<void> _updateProfile() async {
    setState(() => _isSaving = true);
    try {
      final authVM = context.read<AuthViewModel>();
      if (authVM.user != null) {
        await FirebaseDatabase.instance.ref().child('users').child(authVM.user!.uid).update({
          'name': _nameController.text.trim(),
          'phoneNumber': _phoneController.text.trim(),
          'address': _addressController.text.trim(),
        });
        await authVM.refreshUserData();
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
    return Scaffold(
      appBar: AppBar(title: const Text("Update Profile")),
      body: _isSaving ? const Center(child: CircularProgressIndicator()) : SingleChildScrollView(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          children: [
            _field(_nameController, "Full Name", Icons.person),
            const SizedBox(height: 15),
            _field(_phoneController, "Phone Number", Icons.phone),
            const SizedBox(height: 15),
            _field(_addressController, "Full Address", Icons.location_on, lines: 2),
            const SizedBox(height: 30),
            SizedBox(width: double.infinity, height: 50, child: ElevatedButton(onPressed: _updateProfile, child: const Text("SAVE CHANGES"))),
          ],
        ),
      ),
    );
  }

  Widget _field(TextEditingController controller, String label, IconData icon, {int lines = 1}) {
    return TextField(
      controller: controller,
      maxLines: lines,
      decoration: InputDecoration(labelText: label, prefixIcon: Icon(icon), border: OutlineInputBorder(borderRadius: BorderRadius.circular(15))),
    );
  }
}
