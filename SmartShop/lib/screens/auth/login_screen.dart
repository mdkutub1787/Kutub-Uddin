import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../view_models/auth_view_model.dart';
import '../../routes/app_routes.dart';
import '../../view_models/loading_view_model.dart';
import '../../utils/constants/app_colors.dart';
import '../../utils/constants/app_strings.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
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
    } else {
      // Ensure fields are clear if not remembered
      setState(() {
        _emailController.clear();
        _passwordController.clear();
        _rememberMe = false;
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
    final authViewModel = context.watch<AuthViewModel>();
    final size = MediaQuery.of(context).size;
    final primaryColor = Theme.of(context).primaryColor;

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
                  SizedBox(height: size.height * 0.08),
                  
                  // App Logo
                  Hero(
                    tag: 'app_logo',
                    child: Container(
                      padding: const EdgeInsets.all(25),
                      decoration: BoxDecoration(
                        color: Theme.of(context).cardColor,
                        borderRadius: BorderRadius.circular(40),
                        boxShadow: [
                          BoxShadow(
                            color: primaryColor.withValues(alpha: 0.1),
                            blurRadius: 30,
                            offset: const Offset(0, 10),
                          )
                        ],
                      ),
                      child: Image.asset(
                        'assets/images/app_icon.png',
                        height: 90,
                        width: 90,
                      ),
                    ),
                  ),
                  const SizedBox(height: 32),
                  
                  // App Title
                  Text(
                    AppStrings.appName.tr(),
                    style: Theme.of(context).textTheme.displayLarge?.copyWith(
                      fontSize: 42,
                      fontWeight: FontWeight.w900,
                      color: primaryColor,
                      letterSpacing: -1,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    AppStrings.welcomeMessage.tr(),
                    style: TextStyle(
                      fontSize: 16,
                      color: Colors.grey[600],
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  
                  const SizedBox(height: 50),
                  
                  // Form Fields
                  AutofillGroup(
                    child: Column(
                      children: [
                        _buildTextField(
                          controller: _emailController,
                          label: AppStrings.emailLabel.tr(),
                          hint: "example@mail.com",
                          icon: Icons.alternate_email_rounded,
                          keyboardType: TextInputType.emailAddress,
                          action: TextInputAction.next,
                          autofillHints: [AutofillHints.email],
                        ),
                        const SizedBox(height: 24),
                        _buildTextField(
                          controller: _passwordController,
                          label: AppStrings.passwordLabel.tr(),
                          hint: "••••••••",
                          icon: Icons.lock_outline_rounded,
                          obscure: !_isPasswordVisible,
                          action: TextInputAction.done,
                          autofillHints: [AutofillHints.password],
                          suffix: IconButton(
                            icon: Icon(
                              _isPasswordVisible
                                  ? Icons.visibility_off_rounded
                                  : Icons.visibility_rounded,
                              color: Colors.grey,
                            ),
                            onPressed: () => setState(() => _isPasswordVisible = !_isPasswordVisible),
                          ),
                          onSubmitted: (_) => _handleLogin(authViewModel),
                        ),
                        
                        // Remember Me & Forgot Password
                        Row(
                          children: [
                            SizedBox(
                              height: 24,
                              width: 24,
                              child: Checkbox(
                                value: _rememberMe,
                                activeColor: primaryColor,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(5)),
                                onChanged: (val) => setState(() => _rememberMe = val ?? false),
                              ),
                            ),
                            const SizedBox(width: 8),
                            Text(AppStrings.rememberMe.tr(), style: TextStyle(color: Colors.grey[700], fontWeight: FontWeight.w500)),
                            const Spacer(),
                            TextButton(
                              onPressed: () {
                                if (_emailController.text.isEmpty) {
                                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.enterEmailFirst.tr())));
                                  return;
                                }
                                authViewModel.forgotPassword(_emailController.text.trim());
                                ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.resetEmailSent.tr())));
                              },
                              child: Text(
                                AppStrings.forgotPassword.tr(),
                                style: TextStyle(
                                    color: primaryColor,
                                    fontWeight: FontWeight.bold),
                              ),
                            ),
                          ],
                        ),
                        
                        const SizedBox(height: 32),
                        
                        // LOGIN BUTTON (Now Big and Awesome)
                        SizedBox(
                          width: double.infinity,
                          height: 60,
                          child: ElevatedButton(
                            onPressed: authViewModel.isLoading
                                ? null
                                : () => _handleLogin(authViewModel),
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
                                    AppStrings.loginTitle.tr().toUpperCase(),
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
                  
                  const SizedBox(height: 40),
                  
                  // Register Section
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        AppStrings.noAccount.tr(),
                        style: TextStyle(color: Colors.grey[600], fontSize: 15),
                      ),
                      TextButton(
                        onPressed: () => Navigator.pushNamed(context, AppRoutes.register),
                        child: Text(
                          AppStrings.registerNow.tr(),
                          style: TextStyle(
                            color: primaryColor,
                            fontWeight: FontWeight.w900,
                            fontSize: 16,
                          ),
                        ),
                      ),
                    ],
                  ),
                  SizedBox(height: size.height * 0.05),
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

  Future<void> _handleLogin(AuthViewModel authViewModel) async {
    FocusScope.of(context).unfocus();
    if (_emailController.text.isEmpty || _passwordController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(AppStrings.fillAllFields.tr())),
      );
      return;
    }

    final loading = context.read<LoadingViewModel>();
    loading.show(message: AppStrings.authenticating.tr());

    bool success = await authViewModel.login(
      _emailController.text.trim(),
      _passwordController.text,
    );
    
    loading.hide();

    if (success && mounted) {
      await _saveCredentials();
      TextInput.finishAutofillContext();
      Navigator.pushReplacementNamed(context, AppRoutes.main);
    } else if (authViewModel.error != null) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(authViewModel.error!),
          backgroundColor: AppColors.error,
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
  }
}
