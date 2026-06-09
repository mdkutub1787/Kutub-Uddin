import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../view_models/auth_view_model.dart';
import '../../routes/app_routes.dart';
import '../../utils/constants/app_strings.dart';

import '../../view_models/loading_view_model.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _phoneController = TextEditingController();
  final _addressController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();

  bool _isPasswordVisible = false;

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _phoneController.dispose();
    _addressController.dispose();
    _passwordController.dispose();
    _confirmPasswordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final authViewModel = context.watch<AuthViewModel>();

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: AppBar(
        elevation: 0,
        backgroundColor: Colors.transparent,
        iconTheme: IconThemeData(color: Theme.of(context).primaryColor),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 30),
            child: AutofillGroup(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    AppStrings.registerTitle.tr(),
                    style: TextStyle(
                      fontSize: 32,
                      fontWeight: FontWeight.bold,
                      color: Theme.of(context).textTheme.displayLarge?.color,
                    ),
                  ),
                  const SizedBox(height: 10),
                  const Text(
                    "Join us to start shopping!",
                    style: TextStyle(color: Colors.grey, fontSize: 16),
                  ),
                  const SizedBox(height: 30),
                  
                  // Full Name
                  _buildTextField(_nameController, AppStrings.nameLabel.tr(), Icons.person_outline, TextInputType.name),
                  const SizedBox(height: 15),

                  // Phone Number
                  _buildTextField(_phoneController, AppStrings.phoneLabel.tr(), Icons.phone_android_outlined, TextInputType.phone),
                  const SizedBox(height: 15),

                  // Full Address
                  _buildTextField(_addressController, "Full Address", Icons.location_on_outlined, TextInputType.streetAddress),
                  const SizedBox(height: 15),

                  // Email
                  _buildTextField(_emailController, AppStrings.emailLabel.tr(), Icons.email_outlined, TextInputType.emailAddress),
                  const SizedBox(height: 15),

                  // Password
                  TextField(
                    controller: _passwordController,
                    obscureText: !_isPasswordVisible,
                    decoration: InputDecoration(
                      labelText: AppStrings.passwordLabel.tr(),
                      prefixIcon: const Icon(Icons.lock_outline_rounded),
                      suffixIcon: IconButton(
                        icon: Icon(_isPasswordVisible ? Icons.visibility : Icons.visibility_off),
                        onPressed: () => setState(() => _isPasswordVisible = !_isPasswordVisible),
                      ),
                      border: OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
                    ),
                  ),
                  const SizedBox(height: 15),

                  // Confirm Password
                  TextField(
                    controller: _confirmPasswordController,
                    obscureText: !_isPasswordVisible,
                    decoration: InputDecoration(
                      labelText: AppStrings.confirmPasswordLabel.tr(),
                      prefixIcon: const Icon(Icons.check_circle_outline_rounded),
                      border: OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
                    ),
                  ),
                  const SizedBox(height: 30),

                  // Register Button
                  SizedBox(
                    width: double.infinity,
                    height: 55,
                    child: ElevatedButton(
                      onPressed: authViewModel.isLoading ? null : () => _handleRegister(authViewModel),
                      style: ElevatedButton.styleFrom(
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                      ),
                      child: authViewModel.isLoading
                          ? const CircularProgressIndicator(color: Colors.white)
                          : const Text("REGISTER NOW", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                    ),
                  ),
                  const SizedBox(height: 20),
                  
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(AppStrings.noAccount.tr()),
                      TextButton(
                        onPressed: () => Navigator.pop(context),
                        child: Text(AppStrings.loginTitle.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
                      ),
                    ],
                  ),
                  const SizedBox(height: 30),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildTextField(TextEditingController controller, String label, IconData icon, TextInputType type) {
    return TextField(
      controller: controller,
      keyboardType: type,
      textInputAction: TextInputAction.next,
      decoration: InputDecoration(
        labelText: label,
        prefixIcon: Icon(icon),
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
      ),
    );
  }

  Future<void> _handleRegister(AuthViewModel authViewModel) async {
    if (_nameController.text.isEmpty || _phoneController.text.isEmpty || _emailController.text.isEmpty || _addressController.text.isEmpty || _passwordController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Please fill all fields")));
      return;
    }

    if (_passwordController.text != _confirmPasswordController.text) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.passwordMismatch.tr())));
      return;
    }

    final loading = context.read<LoadingViewModel>();
    loading.show(message: "Creating your account...");

    bool success = await authViewModel.register(
      _emailController.text.trim(),
      _passwordController.text,
      name: _nameController.text.trim(),
      phoneNumber: _phoneController.text.trim(),
      address: _addressController.text.trim(),
    );

    loading.hide();

    if (success && mounted) {
      await authViewModel.logout();
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Registration Successful! Please login.")));
        Navigator.pushReplacementNamed(context, AppRoutes.login);
      }
    }
  }
}
