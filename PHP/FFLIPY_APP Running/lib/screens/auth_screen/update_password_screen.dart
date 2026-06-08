import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/utils/custom_text_field.dart';
import 'package:fflipy/core/utils/validators.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:fflipy/providers/auth_providers.dart';
import 'package:fflipy/viewmodels/auth_viewmodel.dart';
import 'package:go_router/go_router.dart';

import '../../core/theme/app_theme.dart';
import '../../core/theme/primary_button.dart';
import '../../core/widgets/brand_app_bar.dart';

class UpdatePasswordScreen extends ConsumerWidget {
  const UpdatePasswordScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final theme = Theme.of(context);
    final screenSize = MediaQuery.of(context).size;
    final isSmallScreen = screenSize.width < 600;

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('Update Password')),
      ),
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
          child: SingleChildScrollView(
            padding: EdgeInsets.symmetric(
              horizontal: isSmallScreen ? 20.0 : screenSize.width * 0.15,
              vertical: 24,
            ),
            child: const Column(
              children: [
                SizedBox(height: 10),
                UpdatePasswordForm(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class UpdatePasswordForm extends ConsumerStatefulWidget {
  const UpdatePasswordForm({super.key});

  @override
  ConsumerState<UpdatePasswordForm> createState() => _UpdatePasswordFormState();
}

class _UpdatePasswordFormState extends ConsumerState<UpdatePasswordForm> {
  final _formKey = GlobalKey<FormState>();
  final _currentPasswordController = TextEditingController();
  final _newPasswordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();

  bool _isCurrentPasswordVisible = false;
  bool _isNewPasswordVisible = false;
  bool _isConfirmPasswordVisible = false;

  @override
  void dispose() {
    _currentPasswordController.dispose();
    _newPasswordController.dispose();
    _confirmPasswordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authViewModelProvider);
    final authViewModel = ref.read(authViewModelProvider.notifier);
    final theme = Theme.of(context);
    final localizations = AppLocalizations.of(context);

    ref.listen<AuthState>(authViewModelProvider, (previous, next) {
      if (next.successMessage != null && next.successMessage != previous?.successMessage) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
              content: Text(next.successMessage!),
              backgroundColor: theme.colorScheme.success),
        );
        Future.delayed(const Duration(milliseconds: 500), () {
          if (context.mounted) {
            context.go(AppRouter.home);
          }
        });
      }
      if (next.error != null && next.error != previous?.error) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
              content: Text(next.error!),
              backgroundColor: theme.colorScheme.error),
        );
      }
    });

    return Form(
      key: _formKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Text(
            context.tr('Create New Password'),
            textAlign: TextAlign.center,
            style: theme.textTheme.headlineMedium?.copyWith(
              fontWeight: FontWeight.bold,
              color: theme.colorScheme.onSurface,
            ),
          ),
          const SizedBox(height: 12),
          Text(
            context.tr('Your new password must be different from previously used passwords for better security.'),
            textAlign: TextAlign.center,
            style: theme.textTheme.bodyMedium?.copyWith(
              color: theme.colorScheme.onSurfaceVariant,
              height: 1.5,
            ),
          ),
          const SizedBox(height: 32),
          _buildPasswordField(
              controller: _currentPasswordController,
              labelText: context.tr('Current Password'),
              isVisible: _isCurrentPasswordVisible,
              onToggleVisibility: () {
                setState(() {
                  _isCurrentPasswordVisible = !_isCurrentPasswordVisible;
                });
              },
              validator: (value) => Validators.validatePassword(value, localizations),
          ),
          const SizedBox(height: 16),
          _buildPasswordField(
              controller: _newPasswordController,
              labelText: context.tr('New Password'),
              isVisible: _isNewPasswordVisible,
              onToggleVisibility: () {
                setState(() {
                  _isNewPasswordVisible = !_isNewPasswordVisible;
                });
              },
              validator: (value) => Validators.validatePassword(value, localizations),
          ),
          const SizedBox(height: 16),
          _buildPasswordField(
              controller: _confirmPasswordController,
              labelText: context.tr('Confirm Password'),
              isVisible: _isConfirmPasswordVisible,
              onToggleVisibility: () {
                setState(() {
                  _isConfirmPasswordVisible = !_isConfirmPasswordVisible;
                });
              },
              validator: (value) => Validators.validateConfirmPassword(
                  value, _newPasswordController.text, localizations),
          ),
          const SizedBox(height: 32),
          PrimaryButton(
            onPressed: authState.isLoading
                ? null
                : () {
                    FocusScope.of(context).unfocus();
                    if (_formKey.currentState!.validate()) {
                      authViewModel.updatePassword(
                        _currentPasswordController.text,
                        _newPasswordController.text,
                      );
                    }
                  },
            text: context.tr('Update Password'),
            isLoading: authState.isLoading,
          ),
        ],
      ),
    );
  }

  Widget _buildPasswordField({
    required TextEditingController controller,
    required String labelText,
    required bool isVisible,
    required VoidCallback onToggleVisibility,
    FormFieldValidator<String>? validator,
  }) {
    final theme = Theme.of(context);
    return CustomTextField(
      controller: controller,
      obscureText: !isVisible,
      labelText: labelText,
      prefixIcon: Icon(Icons.lock_outline_rounded, color: theme.colorScheme.primary),
      suffixIcon: IconButton(
        icon: Icon(isVisible ? Icons.visibility : Icons.visibility_off, color: theme.colorScheme.outline),
        onPressed: onToggleVisibility,
      ),
      validator: validator ??
          (value) {
            if (value == null || value.isEmpty) {
              return context.tr('This field is required');
            }
            return null;
          },
    );
  }
}
