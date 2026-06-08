import 'package:fflipy/core/utils/custom_text_field.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/providers/auth_providers.dart';
import 'package:fflipy/viewmodels/auth_viewmodel.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/utils/validators.dart';

import '../../core/theme/app_theme.dart';
import '../../core/widgets/brand_app_bar.dart';
import '../../core/theme/primary_button.dart';

class ForgotPasswordScreen extends ConsumerStatefulWidget {
  const ForgotPasswordScreen({Key? key}) : super(key: key);

  @override
  ConsumerState<ForgotPasswordScreen> createState() => _ForgotPasswordScreenState();
}

class _ForgotPasswordScreenState extends ConsumerState<ForgotPasswordScreen> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _emailController;
  bool _emailSent = false;

  @override
  void initState() {
    super.initState();
    _emailController = TextEditingController();
  }

  @override
  void dispose() {
    _emailController.dispose();
    super.dispose();
  }

  Future<void> _handlePasswordReset() async {
    if (_formKey.currentState?.validate() ?? false) {
      await ref.read(authViewModelProvider.notifier).forgotPassword(_emailController.text.trim());
    }
  }

  @override
  Widget build(BuildContext context) {
    final localizations = AppLocalizations.of(context);
    final theme = Theme.of(context);
    
    final authState = ref.watch(authViewModelProvider);
    
    ref.listen<AuthState>(authViewModelProvider, (previous, next) {
      if (next.forgotPasswordResponse != null && next.forgotPasswordResponse!.success) {
        setState(() {
          _emailSent = true;
        });
        if (next.successMessage != null) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(next.successMessage!),
              backgroundColor: theme.colorScheme.success,
            ),
          );
        }
      }
      
      if (next.error != null && next.error != previous?.error) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(next.error!),
            backgroundColor: theme.colorScheme.error,
          ),
        );
      }
    });

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
                    _emailSent
                        ? _buildSuccessView(context)
                        : _buildResetForm(context, localizations, authState),
                  ],
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

  Widget _buildResetForm(
      BuildContext context,
      AppLocalizations localizations,
      AuthState authState,
      ) {
    final theme = Theme.of(context);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Text(
          context.tr("Forgot Password?"),
          style: theme.textTheme.headlineMedium?.copyWith(
            fontWeight: FontWeight.bold,
            color: theme.colorScheme.onSurface,
          ),
        ),
        const SizedBox(height: 12),
        Text(
          context.tr("Enter your registered email below to receive password reset instructions."),
          textAlign: TextAlign.center,
          style: theme.textTheme.bodyMedium?.copyWith(
            color: theme.colorScheme.onSurfaceVariant,
            height: 1.5,
          ),
        ),
        const SizedBox(height: 32),

        Form(
          key: _formKey,
          child: Column(
            children: [
              CustomTextField(
                controller: _emailController,
                keyboardType: TextInputType.emailAddress,
                labelText: context.tr("Email Address"),
                hintText: context.tr("your.email@example.com"),
                prefixIcon: Icon(Icons.email_outlined, color: theme.colorScheme.primary),
                validator: (value) => Validators.validateEmail(value, localizations),
              ),
              const SizedBox(height: 32),

              PrimaryButton(
                onPressed: authState.isLoading ? null : _handlePasswordReset,
                text: context.tr("Send Link"),
                isLoading: authState.isLoading,
              ),
              const SizedBox(height: 16),

              const SizedBox(height: 24),
              TextButton(
                onPressed: () => context.pop(),
                style: TextButton.styleFrom(
                  foregroundColor: theme.colorScheme.primary,
                  textStyle: const TextStyle(fontWeight: FontWeight.w600),
                ),
                child: Text(context.tr("Back to Login")),
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildSuccessView(
      BuildContext context,
      ) {
    final theme = Theme.of(context);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Container(
          width: 100,
          height: 100,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: theme.colorScheme.success.withValues(alpha: 0.1),
          ),
          child: Icon(
            Icons.mark_email_read_outlined,
            size: 50,
            color: theme.colorScheme.success,
          ),
        ),
        const SizedBox(height: 24),

        Text(
          context.tr("Check Your Email"),
          style: theme.textTheme.headlineMedium?.copyWith(
            fontWeight: FontWeight.bold,
            color: theme.colorScheme.onSurface,
          ),
        ),
        const SizedBox(height: 8),

        Text(
          '${context.tr("We have sent a password reset link to")} ${_emailController.text}. ${context.tr("Please check your inbox.")}',
          textAlign: TextAlign.center,
          style: theme.textTheme.bodyMedium?.copyWith(
            color: theme.colorScheme.onSurfaceVariant,
            height: 1.5,
          ),
        ),
        const SizedBox(height: 32),

        PrimaryButton(
          onPressed: () => context.go('/login'),
          text: context.tr("Back to Login"),
        ),
        const SizedBox(height: 16),

        TextButton(
          onPressed: () {
            setState(() => _emailSent = false);
            _emailController.clear();
          },
          style: TextButton.styleFrom(
            foregroundColor: theme.colorScheme.primary,
            textStyle: const TextStyle(fontWeight: FontWeight.w600),
          ),
          child: Text(context.tr("Try another email")),
        ),
      ],
    );
  }
}
