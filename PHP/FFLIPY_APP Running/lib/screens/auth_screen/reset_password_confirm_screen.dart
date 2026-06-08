import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/theme/primary_button.dart';
import 'package:fflipy/core/utils/custom_text_field.dart';
import 'package:fflipy/core/utils/validators.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class ResetPasswordConfirmScreen extends StatefulWidget {
  final String? token;
  final String? email;

  const ResetPasswordConfirmScreen({Key? key, this.token, this.email}) : super(key: key);

  @override
  State<ResetPasswordConfirmScreen> createState() => _ResetPasswordConfirmScreenState();
}

class _ResetPasswordConfirmScreenState extends State<ResetPasswordConfirmScreen> {
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;
  bool _isPasswordVisible = false;
  bool _isConfirmPasswordVisible = false;

  @override
  void dispose() {
    _passwordController.dispose();
    _confirmPasswordController.dispose();
    super.dispose();
  }

  void _handlePasswordUpdate() {
    if (_formKey.currentState?.validate() ?? false) {
      setState(() {
        _isLoading = true;
      });

      Future.delayed(const Duration(seconds: 2), () {
        if (mounted) {
          setState(() {
            _isLoading = false;
          });
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(context.tr('Functionality coming soon (Waiting for API)'))),
          );
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final localizations = AppLocalizations.of(context);
    final theme = Theme.of(context);
    final screenSize = MediaQuery.of(context).size;
    final isSmallScreen = screenSize.width < 600;

    return Scaffold(
      body: Container(
        width: screenSize.width,
        height: screenSize.height,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              theme.colorScheme.primary.withValues(alpha: 0.1),
              theme.colorScheme.surface,
            ],
          ),
        ),
        child: SafeArea(
          child: Stack(
            children: [
              SingleChildScrollView(
                padding: EdgeInsets.symmetric(
                  horizontal: isSmallScreen ? 24.0 : screenSize.width * 0.15,
                ),
                child: Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      const SizedBox(height: 40),
                      // App Logo
                      Hero(
                        tag: 'app_logo',
                        child: Image.asset(
                          'assets/logo/logo.png',
                          height: 80,
                          fit: BoxFit.contain,
                        ),
                      ),
                      const SizedBox(height: 32),
                      Text(
                        context.tr('Set New Password'),
                        textAlign: TextAlign.center,
                        style: theme.textTheme.headlineMedium?.copyWith(
                          fontWeight: FontWeight.bold,
                          color: theme.colorScheme.onSurface,
                        ),
                      ),
                      const SizedBox(height: 12),
                      Text(
                        context.tr('Create a secure new password for your account.'),
                        textAlign: TextAlign.center,
                        style: theme.textTheme.bodyMedium?.copyWith(
                          color: theme.colorScheme.onSurfaceVariant,
                          height: 1.5,
                        ),
                      ),
                      const SizedBox(height: 32),
                      if (widget.email != null)
                        Container(
                          margin: const EdgeInsets.only(bottom: 24),
                          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                          decoration: BoxDecoration(
                            color: theme.colorScheme.surfaceVariant.withValues(alpha: 0.3),
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(color: theme.colorScheme.outlineVariant.withValues(alpha: 0.5)),
                          ),
                          child: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Icon(Icons.mark_email_read_outlined, size: 18, color: theme.colorScheme.primary),
                              const SizedBox(width: 8),
                              Text(
                                widget.email!,
                                style: theme.textTheme.bodyMedium?.copyWith(fontWeight: FontWeight.w600),
                              ),
                            ],
                          ),
                        ),
                      CustomTextField(
                        controller: _passwordController,
                        obscureText: !_isPasswordVisible,
                        labelText: context.tr('New Password'),
                        hintText: context.tr('Enter new password'),
                        prefixIcon: Icon(Icons.lock_outline_rounded, color: theme.colorScheme.primary),
                        suffixIcon: IconButton(
                          icon: Icon(_isPasswordVisible ? Icons.visibility : Icons.visibility_off, color: theme.colorScheme.outline),
                          onPressed: () => setState(() => _isPasswordVisible = !_isPasswordVisible),
                        ),
                        validator: (value) => Validators.validatePassword(value, localizations),
                      ),
                      const SizedBox(height: 18),
                      CustomTextField(
                        controller: _confirmPasswordController,
                        obscureText: !_isConfirmPasswordVisible,
                        labelText: context.tr('Confirm Password'),
                        hintText: context.tr('Confirm new password'),
                        prefixIcon: Icon(Icons.lock_clock_outlined, color: theme.colorScheme.primary),
                        suffixIcon: IconButton(
                          icon: Icon(_isConfirmPasswordVisible ? Icons.visibility : Icons.visibility_off, color: theme.colorScheme.outline),
                          onPressed: () => setState(() => _isConfirmPasswordVisible = !_isConfirmPasswordVisible),
                        ),
                        validator: (value) => Validators.validateConfirmPassword(value, _passwordController.text, localizations),
                      ),
                      const SizedBox(height: 32),
                      PrimaryButton(
                        onPressed: _isLoading ? null : _handlePasswordUpdate,
                        text: context.tr('Update Password'),
                        isLoading: _isLoading,
                      ),
                      const SizedBox(height: 24),
                      TextButton(
                        onPressed: () => context.go('/login'),
                        style: TextButton.styleFrom(
                          foregroundColor: theme.colorScheme.primary,
                          textStyle: const TextStyle(fontWeight: FontWeight.w600),
                        ),
                        child: Text(context.tr('Back to Login')),
                      ),
                      const SizedBox(height: 24),
                    ],
                  ),
                ),
              ),
              Positioned(
                top: 8,
                left: 8,
                child: IconButton(
                  icon: const Icon(Icons.arrow_back_ios_new_rounded),
                  onPressed: () => context.pop(),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
