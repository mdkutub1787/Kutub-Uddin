import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';

import '../riverpod/auth_notifier.dart';
import '../../../theme/app_colors.dart';
import '../../../core/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../../core/widgets/curved_header.dart';

class RegisterScreen extends ConsumerStatefulWidget {
  const RegisterScreen({super.key});

  @override
  ConsumerState<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends ConsumerState<RegisterScreen> {
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
    final authState = ref.watch(authNotifierProvider);
    final isLoading = authState.isLoading;

    return Scaffold(
      backgroundColor: const Color(0xFFE2F3ED),
      body: Stack(
        children: [
          // Background blobs
          Positioned(
            top: -100,
            left: -80,
            child: Container(
              width: 300,
              height: 300,
              decoration: const BoxDecoration(
                color: Color(0xFF75CDB3), // Lighter teal blob
                shape: BoxShape.circle,
              ),
            ),
          ),
          Positioned(
            bottom: -150,
            right: -120,
            child: Container(
              width: 300,
              height: 300,
              decoration: const BoxDecoration(
                color: Color(0xFF54B599), // Darker teal blob
                shape: BoxShape.circle,
              ),
            ),
          ),
          
          SafeArea(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  GestureDetector(
                    onTap: () => Navigator.pop(context),
                    child: const Icon(Icons.arrow_back_ios, color: Colors.black87, size: 20),
                  ),
                  const SizedBox(height: 30),
                  Text(
                    "Create\naccount.",
                    style: Theme.of(context).textTheme.displayLarge?.copyWith(
                      fontSize: 40,
                      height: 1.1,
                      color: const Color(0xFF1B3128), // Dark teal for high contrast
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "Fill in your details to continue",
                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                      color: const Color(0xFF50685E), // Muted dark teal
                    ),
                  ),
                  
                  const SizedBox(height: 40),
                  
                  // Form Container
                  Container(
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(30),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withValues(alpha: 0.05),
                          blurRadius: 20,
                          offset: const Offset(0, 10),
                        )
                      ],
                    ),
                    padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 30),
                    child: AutofillGroup(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          _buildModernField(
                            controller: _nameController,
                            hint: "username",
                            icon: Icons.person_outline_rounded,
                          ),
                          _buildModernField(
                            controller: _phoneController,
                            hint: "phone number",
                            icon: Icons.phone_android_rounded,
                            keyboardType: TextInputType.phone,
                          ),
                          _buildModernField(
                            controller: _addressController,
                            hint: "address",
                            icon: Icons.location_on_outlined,
                          ),
                          _buildModernField(
                            controller: _emailController,
                            hint: "username@gmail.com",
                            icon: Icons.email_outlined,
                            keyboardType: TextInputType.emailAddress,
                          ),
                          _buildModernField(
                            controller: _passwordController,
                            hint: "••••••",
                            icon: Icons.lock_outline_rounded,
                            obscure: !_isPasswordVisible,
                            suffix: IconButton(
                              icon: Icon(
                                _isPasswordVisible ? Icons.visibility_off : Icons.visibility,
                                color: Colors.black26,
                                size: 20,
                              ),
                              onPressed: () => setState(() => _isPasswordVisible = !_isPasswordVisible),
                            ),
                          ),
                          
                          const SizedBox(height: 20),
                          
                          // REGISTER BUTTON
                          SizedBox(
                            height: 55,
                            child: ElevatedButton(
                              onPressed: isLoading ? null : _handleRegister,
                              style: ElevatedButton.styleFrom(
                                backgroundColor: const Color(0xFF1B3128), // Premium Dark Teal/Black
                                foregroundColor: Colors.white,
                                elevation: 5,
                                shadowColor: Colors.black26,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(30),
                                ),
                              ),
                              child: isLoading
                                  ? const CircularProgressIndicator(color: Colors.white)
                                  : const Text(
                                      "Create Account", 
                                      style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold)
                                    ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  
                  const SizedBox(height: 30),
                  
                  // Login Link
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text("Already have account? ", style: TextStyle(color: Colors.black54)),
                      GestureDetector(
                        onTap: () => Navigator.pop(context),
                        child: const Text(
                          "Login",
                          style: TextStyle(color: Color(0xFF1B3128), fontWeight: FontWeight.w900, decoration: TextDecoration.underline),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 40),
                ],
              ),
            ),
          ),
          
          // Language Switcher
          Positioned(
            top: MediaQuery.of(context).padding.top + 10,
            right: 20,
            child: _buildLangSwitcher(context),
          ),
        ],
      ),
    );
  }

  Widget _buildLangSwitcher(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: Colors.black.withValues(alpha: 0.05),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          _langBtn("EN", context, const Locale('en', 'US')),
          const Text(" | ", style: TextStyle(color: Colors.black38)),
          _langBtn("BN", context, const Locale('bn', 'BD')),
        ],
      ),
    );
  }

  Widget _langBtn(String code, BuildContext context, Locale locale) {
    bool isSel = context.locale.languageCode == locale.languageCode;
    return GestureDetector(
      onTap: () => context.setLocale(locale),
      child: Text(
        code,
        style: TextStyle(
          fontSize: 12,
          fontWeight: isSel ? FontWeight.bold : FontWeight.normal,
          color: isSel ? Colors.black87 : Colors.black38,
        ),
      ),
    );
  }

  Widget _buildModernField({
    required TextEditingController controller,
    required String hint,
    required IconData icon,
    bool obscure = false,
    TextInputType? keyboardType,
    Widget? suffix,
  }) {
    return Column(
      children: [
        Row(
          children: [
            Icon(icon, color: Colors.black38, size: 20),
            const SizedBox(width: 15),
            Expanded(
              child: TextField(
                controller: controller,
                obscureText: obscure,
                keyboardType: keyboardType,
                style: const TextStyle(color: Colors.black87, fontSize: 16),
                decoration: InputDecoration(
                  hintText: hint,
                  hintStyle: const TextStyle(color: Colors.black26),
                  border: InputBorder.none,
                ),
              ),
            ),
            if (suffix != null) suffix,
          ],
        ),
        const Divider(color: Colors.black12, height: 1),
      ],
    );
  }

  Future<void> _handleRegister() async {
    FocusManager.instance.primaryFocus?.unfocus();
    
    if (_nameController.text.isEmpty || _phoneController.text.isEmpty || _emailController.text.isEmpty || _addressController.text.isEmpty || _passwordController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.fillAllFields.tr())));
      return;
    }

    try {
      final metadata = {
        'full_name': _nameController.text.trim(),
        'phone_number': _phoneController.text.trim(),
        'address': _addressController.text.trim(),
        'role': 'user', 
      };

      await ref.read(authNotifierProvider.notifier).signUp(
        _emailController.text.trim(),
        _passwordController.text,
        metadata,
      );
      
      if (!mounted) return;
      TextInput.finishAutofillContext();
      
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("Account created successfully! Please login."),
          backgroundColor: Colors.green,
        ),
      );
      
      // Go to Login Page
      Navigator.pushNamedAndRemoveUntil(context, AppRoutes.login, (route) => false);

    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(AppStrings.registrationFailed.tr(args: [e.toString().split(':').last.trim()])),
          backgroundColor: AppColors.error,
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
  }
}
