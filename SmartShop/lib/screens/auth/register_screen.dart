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
  Widget build(BuildContext context) {
    final authViewModel = context.watch<AuthViewModel>();
    final primaryColor = Theme.of(context).primaryColor;
    final size = MediaQuery.of(context).size;

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
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
          
          SafeArea(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 28),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(height: 10),
                  Align(
                    alignment: Alignment.topLeft,
                    child: IconButton(
                      icon: const Icon(Icons.arrow_back_ios_new_rounded),
                      onPressed: () => Navigator.pop(context),
                    ),
                  ),
                  
                  const SizedBox(height: 10),
                  // App Title
                  Text(
                    AppStrings.registerTitle.tr(),
                    style: Theme.of(context).textTheme.displayLarge?.copyWith(
                      fontSize: 42,
                      fontWeight: FontWeight.w900,
                      color: primaryColor,
                      letterSpacing: -1,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    "Create your account to start shopping",
                    style: TextStyle(
                      fontSize: 16,
                      color: Colors.grey[600],
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  
                  const SizedBox(height: 40),
                  
                  // Form Fields
                  AutofillGroup(
                    child: Column(
                      children: [
                        _buildTextField(
                          controller: _nameController,
                          label: AppStrings.nameLabel.tr(),
                          hint: "Your Full Name",
                          icon: Icons.person_outline_rounded,
                          action: TextInputAction.next,
                          autofillHints: [AutofillHints.name],
                        ),
                        const SizedBox(height: 20),
                        
                        _buildTextField(
                          controller: _phoneController,
                          label: AppStrings.phoneLabel.tr(),
                          hint: "01XXXXXXXXX",
                          icon: Icons.phone_android_rounded,
                          keyboardType: TextInputType.phone,
                          action: TextInputAction.next,
                          autofillHints: [AutofillHints.telephoneNumber],
                        ),
                        const SizedBox(height: 20),
                        
                        _buildTextField(
                          controller: _addressController,
                          label: "Delivery Address",
                          hint: "House, Road, Area...",
                          icon: Icons.location_on_outlined,
                          action: TextInputAction.next,
                          autofillHints: [AutofillHints.fullStreetAddress],
                        ),
                        const SizedBox(height: 20),
                        
                        _buildTextField(
                          controller: _emailController,
                          label: AppStrings.emailLabel.tr(),
                          hint: "example@mail.com",
                          icon: Icons.alternate_email_rounded,
                          keyboardType: TextInputType.emailAddress,
                          action: TextInputAction.next,
                          autofillHints: [AutofillHints.email],
                        ),
                        const SizedBox(height: 20),
                        
                        _buildTextField(
                          controller: _passwordController,
                          label: AppStrings.passwordLabel.tr(),
                          hint: "••••••••",
                          icon: Icons.lock_outline_rounded,
                          obscure: !_isPasswordVisible,
                          action: TextInputAction.next,
                          autofillHints: [AutofillHints.newPassword],
                          suffix: IconButton(
                            icon: Icon(
                              _isPasswordVisible
                                  ? Icons.visibility_off_rounded
                                  : Icons.visibility_rounded,
                              color: Colors.grey,
                            ),
                            onPressed: () => setState(() => _isPasswordVisible = !_isPasswordVisible),
                          ),
                        ),
                        const SizedBox(height: 20),
                        
                        _buildTextField(
                          controller: _confirmPasswordController,
                          label: AppStrings.confirmPasswordLabel.tr(),
                          hint: "••••••••",
                          icon: Icons.verified_user_outlined,
                          obscure: !_isPasswordVisible,
                          action: TextInputAction.done,
                          autofillHints: [AutofillHints.password],
                          onSubmitted: (_) => _handleRegister(authViewModel),
                        ),
                        
                        const SizedBox(height: 40),
                        
                        // REGISTER BUTTON
                        SizedBox(
                          width: double.infinity,
                          height: 60,
                          child: ElevatedButton(
                            onPressed: authViewModel.isLoading
                                ? null
                                : () => _handleRegister(authViewModel),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: primaryColor,
                              foregroundColor: Colors.white,
                              elevation: 8,
                              shadowColor: primaryColor.withValues(alpha: 0.4),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(20),
                              ),
                            ),
                            child: authViewModel.isLoading
                                ? const SizedBox(
                                    height: 24,
                                    width: 24,
                                    child: CircularProgressIndicator(
                                      color: Colors.white,
                                      strokeWidth: 3,
                                    ),
                                  )
                                : Text(
                                    AppStrings.registerTitle.tr().toUpperCase(),
                                    style: const TextStyle(
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
                  
                  const SizedBox(height: 30),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        "Already have an account?",
                        style: TextStyle(color: Colors.grey[600], fontSize: 15),
                      ),
                      TextButton(
                        onPressed: () => Navigator.pop(context),
                        child: Text(
                          AppStrings.loginTitle.tr(),
                          style: TextStyle(
                            color: primaryColor,
                            fontWeight: FontWeight.w900,
                            fontSize: 16,
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 40),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildTextField({
    required TextEditingController controller,
    required String label,
    required String hint,
    required IconData icon,
    bool obscure = false,
    TextInputType? keyboardType,
    TextInputAction? action,
    Widget? suffix,
    Function(String)? onSubmitted,
    Iterable<String>? autofillHints,
  }) {
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
        obscureText: obscure,
        keyboardType: keyboardType,
        textInputAction: action,
        onSubmitted: onSubmitted,
        autofillHints: autofillHints,
        style: const TextStyle(fontWeight: FontWeight.w500),
        decoration: InputDecoration(
          labelText: label,
          hintText: hint,
          prefixIcon: Icon(icon, color: Theme.of(context).primaryColor),
          suffixIcon: suffix,
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

  Future<void> _handleRegister(AuthViewModel authViewModel) async {
    FocusManager.instance.primaryFocus?.unfocus();
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
      TextInput.finishAutofillContext();
      await authViewModel.logout();
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Registration Successful! Please login.")));
        Navigator.pushReplacementNamed(context, AppRoutes.login);
      }
    }
  }
}
