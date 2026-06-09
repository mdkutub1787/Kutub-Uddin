import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/settings_view_model.dart';

class AdminVerificationScreen extends StatefulWidget {
  const AdminVerificationScreen({super.key});

  @override
  State<AdminVerificationScreen> createState() => _AdminVerificationScreenState();
}

class _AdminVerificationScreenState extends State<AdminVerificationScreen> {
  final TextEditingController _adminCodeController = TextEditingController();
  bool _isObscured = true;

  @override
  void dispose() {
    _adminCodeController.dispose();
    super.dispose();
  }

  void _submitAdminCode(BuildContext context) async {
    final authViewModel = context.read<AuthViewModel>();
    final adminCode = _adminCodeController.text.trim();

    if (adminCode.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Please enter admin code')));
      return;
    }

    final success = await authViewModel.requestAdminAccess(adminCode);

    if (mounted) {
      if (success) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Admin access granted!')));
        Navigator.pop(context, true);
      } else {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(authViewModel.error ?? 'Invalid code')));
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final authViewModel = context.watch<AuthViewModel>();
    final settings = context.watch<SettingsViewModel>();

    return Scaffold(
      appBar: AppBar(title: const Text('Admin Verification'), centerTitle: true),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            const SizedBox(height: 20),
            CircleAvatar(
              radius: 50,
              backgroundColor: settings.primaryColor.withValues(alpha: 0.1),
              child: Icon(Icons.admin_panel_settings, size: 50, color: settings.primaryColor),
            ),
            const SizedBox(height: 30),
            TextField(
              controller: _adminCodeController,
              obscureText: _isObscured,
              decoration: InputDecoration(
                labelText: "Admin Access Code",
                suffixIcon: IconButton(icon: Icon(_isObscured ? Icons.visibility_off : Icons.visibility), onPressed: () => setState(() => _isObscured = !_isObscured)),
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
              ),
            ),
            const SizedBox(height: 30),
            SizedBox(
              width: double.infinity,
              height: 50,
              child: ElevatedButton(
                onPressed: authViewModel.isLoading ? null : () => _submitAdminCode(context),
                child: authViewModel.isLoading ? const CircularProgressIndicator(color: Colors.white) : const Text("VERIFY CODE"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
