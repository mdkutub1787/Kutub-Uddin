import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:supabase_flutter/supabase_flutter.dart';

import '../riverpod/auth_notifier.dart';
import '../../../routes/app_routes.dart';
import '../../../theme/app_colors.dart';
import '../../../core/app_strings.dart';
import '../../../core/widgets/curved_header.dart';
import '../../../core/utils/exit_dialog_helper.dart';

class LoginScreen extends ConsumerStatefulWidget {
  const LoginScreen({super.key});

  @override
  ConsumerState<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends ConsumerState<LoginScreen> {
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  bool _isPasswordVisible = false;
  bool _rememberMe = false;

  @override
  void initState() {
    super.initState();
    _loadSavedCredentials();
  }

  Future<void> _loadSavedCredentials() async {
    final prefs = await SharedPreferences.getInstance();
    final savedEmail = prefs.getString('saved_email');
    final savedPassword = prefs.getString('saved_password');
    final remember = prefs.getBool('remember_me') ?? false;

    if (remember) {
      setState(() {
        _emailController.text = savedEmail ?? '';
        _passwordController.text = savedPassword ?? '';
        _rememberMe = true;
      });
    }
  }

  Future<void> _saveCredentials() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setBool('remember_me', _rememberMe);
    if (_rememberMe) {
      await prefs.setString('saved_email', _emailController.text.trim());
      await prefs.setString('saved_password', _passwordController.text);
    } else {
      await prefs.remove('saved_email');
      await prefs.remove('saved_password');
    }
  }

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authNotifierProvider);
    final isLoading = authState.isLoading;

    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (didPop) return;
        await ExitDialogHelper.showExitDialog(context);
      },
      child: Scaffold(
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
                    "Welcome\nback.",
                    style: Theme.of(context).textTheme.displayLarge?.copyWith(
                      fontSize: 40,
                      height: 1.1,
                      color: const Color(0xFF1B3128), // Dark teal for high contrast
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "Sign in to continue",
                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                      color: const Color(0xFF50685E), // Muted dark teal
                    ),
                  ),
                  
                  const SizedBox(height: 60),
                  
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
                            controller: _emailController,
                            hint: "username@gmail.com",
                            icon: Icons.email_outlined,
                            keyboardType: TextInputType.emailAddress,
                            autofillHints: const [AutofillHints.email],
                          ),
                          _buildModernField(
                            controller: _passwordController,
                            hint: "••••••",
                            icon: Icons.lock_outline_rounded,
                            obscure: !_isPasswordVisible,
                            autofillHints: const [AutofillHints.password],
                          ),
                          
                          Align(
                            alignment: Alignment.centerRight,
                            child: TextButton(
                              onPressed: _handleForgotPassword,
                              child: Text(
                                "Forgot password?",
                                style: TextStyle(
                                  color: Theme.of(context).primaryColor, 
                                  fontSize: 13, 
                                  fontWeight: FontWeight.bold
                                ),
                              ),
                            ),
                          ),
                          
                          const SizedBox(height: 20),
                          
                          // LOGIN BUTTON
                          SizedBox(
                            height: 55,
                            child: ElevatedButton(
                              onPressed: isLoading ? null : _handleLogin,
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
                                      "Continue", 
                                      style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold)
                                    ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  
                  const SizedBox(height: 40),
                  
                  // Register Link
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text("New here? ", style: TextStyle(color: Colors.black54)),
                      GestureDetector(
                        onTap: () => Navigator.pushNamed(context, AppRoutes.register),
                        child: const Text(
                          "Create account",
                          style: TextStyle(color: Color(0xFF1B3128), fontWeight: FontWeight.w900, decoration: TextDecoration.underline),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 32),
                  Row(
                    children: [
                      Expanded(child: Divider(color: Theme.of(context).colorScheme.outlineVariant)),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 16),
                        child: Text(
                          context.tr("Quick Access"),
                          style: Theme.of(context).textTheme.labelMedium?.copyWith(color: Theme.of(context).colorScheme.outline),
                        ),
                      ),
                      Expanded(child: Divider(color: Theme.of(context).colorScheme.outlineVariant)),
                    ],
                  ),
                  const SizedBox(height: 20),
                  Wrap(
                    alignment: WrapAlignment.center,
                    spacing: 12,
                    runSpacing: 12,
                    children: [
                      _buildQuickLoginButton(
                        name: 'Admin',
                        username: 'mdkutub150@gmail.com',
                        password: '000000',
                        theme: Theme.of(context),
                      ),
                      _buildQuickLoginButton(
                        name: 'Owner',
                        username: 'mdkutub15@gmail.com',
                        password: '000000',
                        theme: Theme.of(context),
                      ),
                      _buildQuickLoginButton(
                        name: 'Customer',
                        username: 'mdkutub1@gmail.com',
                        password: '000000',
                        theme: Theme.of(context),
                      ),
                      _buildQuickLoginButton(
                        name: 'Rider',
                        username: 'mdkutub@gmail.com',
                        password: '000000',
                        theme: Theme.of(context),
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
    ));
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
    Iterable<String>? autofillHints,
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
                autofillHints: autofillHints,
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

  void _handleForgotPassword() {
    if (_emailController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(AppStrings.enterEmailFirst.tr())),
      );
      return;
    }
    // Implement forgot password logic
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(AppStrings.resetEmailSent.tr())),
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
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.02),
            blurRadius: 10,
            offset: const Offset(0, 4),
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
        style: const TextStyle(fontWeight: FontWeight.w500, fontSize: 14),
        decoration: InputDecoration(
          labelText: label,
          hintText: hint,
          prefixIcon: Icon(icon, color: Theme.of(context).primaryColor, size: 20),
          suffixIcon: suffix,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(16),
            borderSide: BorderSide.none,
          ),
          filled: true,
          fillColor: Colors.transparent,
          contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
        ),
      ),
    );
  }

  Future<void> _handleLogin() async {
    FocusScope.of(context).unfocus();
    if (_emailController.text.isEmpty || _passwordController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(AppStrings.fillAllFields.tr())),
      );
      return;
    }

    try {
      await ref.read(authNotifierProvider.notifier).signIn(
        _emailController.text.trim(),
        _passwordController.text,
      );
      
      if (!mounted) return;
      await _saveCredentials();
      TextInput.finishAutofillContext();
      if (context.mounted) {
        final user = ref.read(authNotifierProvider).value;
        if (user != null) {
          if (user.role == 'admin' || user.role == 'super_admin' || user.role == 'owner') {
            Navigator.pushReplacementNamed(context, AppRoutes.adminDashboard);
          } else if (user.role == 'delivery_man') {
            Navigator.pushReplacementNamed(context, AppRoutes.deliveryDashboard);
          } else {
            Navigator.pushReplacementNamed(context, AppRoutes.main);
          }
        }
      }
    } catch (e) {
      if (!mounted) return;
      
      String errorMsg = e.toString();
      if (e is AuthException) {
        errorMsg = e.message;
      } else if (errorMsg.contains('AuthApiException') || errorMsg.contains('AuthException')) {
        errorMsg = "Invalid email or password.";
      } else {
        errorMsg = errorMsg.split(':').last.trim();
      }
      
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(AppStrings.loginFailed.tr(args: [errorMsg])),
          backgroundColor: AppColors.error,
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
  }

  Widget _buildQuickLoginButton({
    required String name,
    required String username,
    required String password,
    required ThemeData theme,
  }) {
    return ActionChip(
      label: Text(name),
      avatar: const Icon(Icons.person, size: 16),
      onPressed: () {
        _emailController.text = username;
        _passwordController.text = password;
        _handleLogin();
      },
      backgroundColor: theme.colorScheme.surface,
      side: BorderSide(color: theme.colorScheme.outlineVariant),
      labelStyle: theme.textTheme.labelMedium,
    );
  }
}
