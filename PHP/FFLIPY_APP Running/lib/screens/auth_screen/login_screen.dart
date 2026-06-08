import 'package:country_picker/country_picker.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/utils/custom_text_field.dart';
import 'package:fflipy/models/auth/login_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/theme/primary_button.dart';
import 'package:fflipy/core/utils/validators.dart';
import 'package:fflipy/models/auth/registration_model.dart';
import 'package:fflipy/core/utils/dialog_helper.dart';
import '../../providers/app_info_provider.dart';

import '../../providers/auth_providers.dart';
import '../../providers/localization_provider.dart';
import '../../viewmodels/auth_viewmodel.dart';

class LoginScreen extends ConsumerStatefulWidget {
  const LoginScreen({super.key});

  @override
  ConsumerState<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends ConsumerState<LoginScreen>
    with SingleTickerProviderStateMixin {
  final _loginFormKey = GlobalKey<FormState>();
  final _signupFormKey = GlobalKey<FormState>();
  late final TabController _tabController;
  late final TextEditingController _usernameController;
  late final TextEditingController _passwordController;
  late final TextEditingController _signupFirstnameController;
  late final TextEditingController _signupLastnameController;
  late final TextEditingController _signupUsernameController;
  late final TextEditingController _signupEmailController;
  late final TextEditingController _signupPhoneController;
  late final TextEditingController _signupPasswordController;
  late final TextEditingController _signupConfirmPasswordController;

  double _passwordStrength = 0;
  String _strengthText = '';
  Color _strengthColor = Colors.transparent;

  bool _isPasswordVisible = false;
  bool _isSignupPasswordVisible = false;
  bool _isSignupConfirmPasswordVisible = false;
  bool _agreeToTerms = false;

  Country _selectedCountry = Country(
    phoneCode: '880',
    countryCode: 'BD',
    e164Sc: 0,
    geographic: true,
    level: 1,
    name: 'Bangladesh',
    example: '1712345678',
    displayName: 'Bangladesh (BD) [+880]',
    displayNameNoCountryCode: 'Bangladesh (BD)',
    e164Key: '880-BD-0',
  );

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
    _tabController.addListener(() {
      if (!_tabController.indexIsChanging) {
        setState(() {});
      }
    });
    _usernameController = TextEditingController();
    _passwordController = TextEditingController();
    _signupFirstnameController = TextEditingController();
    _signupLastnameController = TextEditingController();
    _signupUsernameController = TextEditingController();
    _signupEmailController = TextEditingController();
    _signupPhoneController = TextEditingController();
    _signupPasswordController = TextEditingController();
    _signupConfirmPasswordController = TextEditingController();

    _signupPasswordController.addListener(_checkPasswordStrength);
  }

  void _checkPasswordStrength() {
    final password = _signupPasswordController.text;
    if (password.isEmpty) {
      setState(() {
        _passwordStrength = 0;
        _strengthText = '';
        _strengthColor = Colors.transparent;
      });
      return;
    }

    double strength = 0;
    if (password.length >= 8) strength += 0.2;
    if (RegExp(r'[A-Z]').hasMatch(password)) strength += 0.2;
    if (RegExp(r'[a-z]').hasMatch(password)) strength += 0.2;
    if (RegExp(r'[0-9]').hasMatch(password)) strength += 0.2;
    if (RegExp(r'[@$!%*?&]').hasMatch(password)) strength += 0.2;

    setState(() {
      _passwordStrength = strength;
      if (strength <= 0.2) {
        _strengthText = context.tr('Very Weak');
        _strengthColor = Colors.red;
      } else if (strength <= 0.4) {
        _strengthText = context.tr('Weak');
        _strengthColor = Colors.orange;
      } else if (strength <= 0.6) {
        _strengthText = context.tr('Medium');
        _strengthColor = Colors.amber;
      } else if (strength <= 0.8) {
        _strengthText = context.tr('Strong');
        _strengthColor = Colors.blue;
      } else {
        _strengthText = context.tr('Very Strong');
        _strengthColor = Colors.green;
      }
    });
  }

  @override
  void dispose() {
    _tabController.dispose();
    _usernameController.dispose();
    _passwordController.dispose();
    _signupFirstnameController.dispose();
    _signupLastnameController.dispose();
    _signupUsernameController.dispose();
    _signupEmailController.dispose();
    _signupPhoneController.dispose();
    _signupPasswordController.dispose();
    _signupConfirmPasswordController.dispose();
    super.dispose();
  }

  void _handleLogin() {
    if (_loginFormKey.currentState?.validate() ?? false) {
      FocusScope.of(context).unfocus();
      ref.read(authViewModelProvider.notifier).login(
            LoginRequest(
              username: _usernameController.text.trim(),
              password: _passwordController.text,
            ),
          );
    }
  }

  void _handleSignup() {
    if (_signupFormKey.currentState?.validate() ?? false) {
      if (!_agreeToTerms) {
        DialogHelper.showSnackBar(
          context,
          context.tr('Please agree to the Terms and Conditions'),
          isError: true,
        );
        return;
      }
      FocusScope.of(context).unfocus();
      final fullPhoneNumber = '+${_selectedCountry.phoneCode}${_signupPhoneController.text.trim()}';
      ref.read(authViewModelProvider.notifier).register(
            RegistrationRequest(
              firstname: _signupFirstnameController.text.trim(),
              lastname: _signupLastnameController.text.trim(),
              username: _signupUsernameController.text.trim(),
              email: _signupEmailController.text.trim(),
              phone: fullPhoneNumber,
              password: _signupPasswordController.text,
            ),
          );
    }
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context);
    final theme = Theme.of(context);
    final authState = ref.watch(authViewModelProvider);
    final screenSize = MediaQuery.of(context).size;
    final isSmallScreen = screenSize.width < 600;
    final currentLocale = ref.watch(localeProvider);
    final isKeyboardVisible = MediaQuery.of(context).viewInsets.bottom > 0;

    ref.listen<AuthState>(authViewModelProvider, (previous, next) {
      if (next.responseModelUser?.user?.emailVerification == '1') {
        context.go(AppRouter.home);
      } else if (next.tempToken != null && next.emailForVerification != null) {
        context.goNamed(
          AppRouter.otpVerification,
          pathParameters: {'email': next.emailForVerification!},
        );
      } else if (next.validationErrors != null) {
        final errorMessages = next.validationErrors!.entries
            .map((e) => e.value.join('\n'))
            .join('\n');
        DialogHelper.showErrorDialog(
          context: context,
          title: context.tr('Validation Error'),
          message: errorMessages,
        );
      } else if (next.error != null && (previous?.isLoading ?? false)) {
        DialogHelper.showErrorDialog(
          context: context,
          title: context.tr('Error'),
          message: context.tr(next.error!),
        );
      }
    });

    return PopScope(
      canPop: false,
      onPopInvoked: (bool didPop) async {
        if (didPop) return;
        final bool shouldExit = await DialogHelper.showAppExitConfirmation(context);
        if (shouldExit && context.mounted) {
          SystemNavigator.pop();
        }
      },
      child: Scaffold(
        resizeToAvoidBottomInset: true,
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
                      const SizedBox(height: 80),
                      // App Logo
                      Hero(
                        tag: 'app_logo',
                        child: Image.asset(
                          'assets/logo/logo.png',
                          height: 50,
                          fit: BoxFit.contain,
                        ),
                      ),
                      const SizedBox(height: 32),
                      Text(
                        _tabController.index == 0
                            ? context.tr('Welcome Back!')
                            : context.tr('Create Account'),
                        textAlign: TextAlign.center,
                        style: theme.textTheme.headlineMedium?.copyWith(
                          fontWeight: FontWeight.bold,
                          color: theme.colorScheme.onSurface,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        _tabController.index == 0
                            ? context.tr('Sign in to continue your journey')
                            : context.tr('Join us and start sending money easily'),
                        textAlign: TextAlign.center,
                        style: theme.textTheme.bodyMedium?.copyWith(
                          color: theme.colorScheme.onSurfaceVariant,
                        ),
                      ),
                      const SizedBox(height: 32),
                      _buildToggleButtons(context, theme),
                      const SizedBox(height: 32),
                      AnimatedSwitcher(
                        duration: const Duration(milliseconds: 300),
                        child: _tabController.index == 0
                            ? _buildLoginForm(context, loc, theme, authState.isLoading, isKeyboardVisible)
                            : _buildSignupForm(context, loc, theme, authState.isLoading),
                      ),
                      const SizedBox(height: 24),
                    ],
                  ),
                ),
                Positioned(
                  top: 16,
                  right: 16,
                  child: _buildLanguageSelector(context, theme, currentLocale),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildLanguageSelector(BuildContext context, ThemeData theme, Locale currentLocale) {
     return PopupMenuButton<String>(
      onSelected: (String languageCode) {
        if (languageCode == 'en') {
          ref.read(localeProvider.notifier).setEnglish();
        } else if (languageCode == 'es') {
          ref.read(localeProvider.notifier).setSpanish();
        } else {
          ref.read(localeProvider.notifier).setBangla();
        }
      },
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(12),
      ),
      offset: const Offset(0, 40),
      itemBuilder: (BuildContext context) => <PopupMenuEntry<String>>[
        PopupMenuItem<String>(
          value: 'en',
          child: Row(
            children: [
              const Text('🇺🇸', style: TextStyle(fontSize: 20)),
              const SizedBox(width: 10),
              Text(context.tr('English')),
            ],
          ),
        ),
        PopupMenuItem<String>(
          value: 'es',
          child: Row(
            children: [
              const Text('🇪🇸', style: TextStyle(fontSize: 20)),
              const SizedBox(width: 10),
              Text(context.tr('Spanish')),
            ],
          ),
        ),
        PopupMenuItem<String>(
          value: 'bn',
          child: Row(
            children: [
              const Text('🇧🇩', style: TextStyle(fontSize: 20)),
              const SizedBox(width: 10),
              Text(context.tr('Bangla')),
            ],
          ),
        ),
      ],
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
        decoration: BoxDecoration(
          color: theme.colorScheme.surfaceVariant.withOpacity(0.9),
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: theme.colorScheme.outline.withOpacity(0.2),
          ),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.language, size: 18, color: theme.colorScheme.primary),
            const SizedBox(width: 6),
            Text(
              currentLocale.languageCode == 'en'
                  ? context.tr('English')
                  : currentLocale.languageCode == 'es'
                      ? context.tr('Spanish')
                      : context.tr('Bangla'),
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: theme.colorScheme.onSurfaceVariant,
              ),
            ),
            const Icon(Icons.arrow_drop_down, size: 18),
          ],
        ),
      ),
    );
  }

  Widget _buildToggleButtons(BuildContext context, ThemeData theme) {
    return Container(
      padding: const EdgeInsets.all(4),
      decoration: BoxDecoration(
        color: theme.colorScheme.surfaceVariant.withOpacity(0.3),
        borderRadius: BorderRadius.circular(25),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          _buildToggleButton(context.tr('Sign In'), 0, theme),
          const SizedBox(width: 5),
          _buildToggleButton(context.tr('Sign Up'), 1, theme),
        ],
      ),
    );
  }

  Widget _buildToggleButton(String title, int index, ThemeData theme) {
    final isSelected = _tabController.index == index;
    return InkWell(
      onTap: () => _tabController.animateTo(index),
      borderRadius: BorderRadius.circular(20),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 10),
        decoration: BoxDecoration(
          color: isSelected ? theme.colorScheme.primary : Colors.transparent,
          borderRadius: BorderRadius.circular(20),
        ),
        child: Text(
          title,
          style: TextStyle(
            color: isSelected
                ? theme.colorScheme.onPrimary
                : theme.colorScheme.onSurfaceVariant,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }

  Widget _buildLoginForm(BuildContext context, AppLocalizations loc, ThemeData theme, bool isLoading, bool isKeyboardVisible) {
    final packageInfoAsync = ref.watch(packageInfoProvider);

    return Form(
      key: _loginFormKey,
      child: Column(
        children: [
          CustomTextField(
            controller: _usernameController,
            labelText: context.tr('Username or Email'),
            prefixIcon: Icon(Icons.account_circle_outlined, color: theme.colorScheme.primary),
            validator: (value) => Validators.validateUsername(value, loc),
          ),
          const SizedBox(height: 18),
          CustomTextField(
            controller: _passwordController,
            obscureText: !_isPasswordVisible,
            labelText: context.tr('Password'),
            hintText: context.tr('Enter Password'),
            prefixIcon: Icon(Icons.lock_outline_rounded, color: theme.colorScheme.primary),
            suffixIcon: IconButton(
              icon: Icon(_isPasswordVisible ? Icons.visibility : Icons.visibility_off, color: theme.colorScheme.outline),
              onPressed: () => setState(() => _isPasswordVisible = !_isPasswordVisible),
            ),
            validator: (value) => Validators.validatePassword(value, loc),
          ),
          Align(
            alignment: Alignment.centerRight,
            child: TextButton(
              onPressed: () => context.push(AppRouter.forgotPassword),
              style: TextButton.styleFrom(
                foregroundColor: theme.colorScheme.primary,
                textStyle: const TextStyle(fontWeight: FontWeight.w600),
              ),
              child: Text(context.tr('Forgot Password?')),
            ),
          ),
          const SizedBox(height: 16),
          PrimaryButton(
            onPressed: isLoading ? null : _handleLogin,
            text: context.tr('Login'),
            isLoading: false,
          ),
          const SizedBox(height: 32),
          Row(
            children: [
              Expanded(child: Divider(color: theme.colorScheme.outlineVariant)),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                child: Text(
                  context.tr("Quick Access"),
                  style: theme.textTheme.labelMedium?.copyWith(color: theme.colorScheme.outline),
                ),
              ),
              Expanded(child: Divider(color: theme.colorScheme.outlineVariant)),
            ],
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              _buildQuickLoginButton(
                name: 'Raju',
                username: 'ahmed',
                password: 'Ahmed12@',
                theme: theme,
              ),
              const SizedBox(width: 16),
              _buildQuickLoginButton(
                name: 'Kutub',
                username: 'kutub',
                password: 'Kutub1787@',
                theme: theme,
              ),
            ],
          ),
          const SizedBox(height: 150),
          packageInfoAsync.when(
            data: (packageInfo) {
              final appVersion = packageInfo.version;
              final currentYear = DateTime.now().year;
              return Column(
                children: [
                  Text(
                    '${context.tr("Version")} $appVersion',
                    style: theme.textTheme.bodySmall?.copyWith(color: theme.colorScheme.outline),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    '© $currentYear FFlipy. ${context.tr("All rights reserved.")}',
                    style: theme.textTheme.bodySmall?.copyWith(color: theme.colorScheme.outline, fontSize: 10),
                  ),
                ],
              );
            },
            loading: () => const SizedBox(height: 20, width: 20, child: CircularProgressIndicator(strokeWidth: 2)),
            error: (err, stack) => Text(context.tr(ErrorHandler.getErrorMessage(err))),
          ),
          const SizedBox(height: 4),
        ],
      ),
    );
  }

  Widget _buildQuickLoginButton({required String name, required String username, required String password, required ThemeData theme}) {
    return OutlinedButton(
      onPressed: () {
        setState(() {
          _usernameController.text = username;
          _passwordController.text = password;
        });
      },
      style: OutlinedButton.styleFrom(
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        side: BorderSide(color: theme.colorScheme.outlineVariant),
      ),
      child: Text(name, style: TextStyle(color: theme.colorScheme.primary, fontWeight: FontWeight.bold)),
    );
  }

  Widget _buildSignupForm(BuildContext context, AppLocalizations loc, ThemeData theme, bool isLoading) {
    return Form(
      key: _signupFormKey,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Row(
            children: [
              Expanded(
                child: CustomTextField(
                  controller: _signupFirstnameController,
                  labelText: context.tr('First Name'),
                  prefixIcon: Icon(Icons.person_outline, color: theme.colorScheme.primary),
                  validator: (value) => Validators.validateName(value, loc),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: CustomTextField(
                  controller: _signupLastnameController,
                  labelText: context.tr('Last Name'),
                  prefixIcon: Icon(Icons.person_outline, color: theme.colorScheme.primary),
                  validator: (value) => Validators.validateName(value, loc),
                ),
              ),
            ],
          ),
          const SizedBox(height: 18),
          CustomTextField(
            controller: _signupUsernameController,
            labelText: context.tr('Username'),
            prefixIcon: Icon(Icons.account_box_outlined, color: theme.colorScheme.primary),
            validator: (value) => Validators.validateUsername(value, loc),
          ),
          const SizedBox(height: 18),
          CustomTextField(
            controller: _signupEmailController,
            keyboardType: TextInputType.emailAddress,
            labelText: context.tr('Email Address'),
            hintText: context.tr('your.email@example.com'),
            prefixIcon: Icon(Icons.email_outlined, color: theme.colorScheme.primary),
            validator: (value) => Validators.validateEmail(value, loc),
          ),
          const SizedBox(height: 18),
          CustomTextField(
            controller: _signupPhoneController,
            keyboardType: TextInputType.phone,
            labelText: context.tr('Phone Number'),
            inputFormatters: [
              FilteringTextInputFormatter.digitsOnly,
              LengthLimitingTextInputFormatter(15),
            ],
            prefixIcon: InkWell(
              onTap: () {
                showCountryPicker(
                  context: context,
                  showPhoneCode: true,
                  countryListTheme: CountryListThemeData(
                    borderRadius: BorderRadius.circular(20),
                    inputDecoration: InputDecoration(
                      labelText: context.tr('Search Country'),
                      prefixIcon: const Icon(Icons.search),
                      border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                  ),
                  onSelect: (Country country) {
                    setState(() {
                      _selectedCountry = country;
                    });
                  },
                );
              },
              child: Container(
                padding: const EdgeInsets.only(left: 12.0, right: 8.0),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text(_selectedCountry.flagEmoji, style: const TextStyle(fontSize: 20)),
                    const SizedBox(width: 4),
                    Text('+${_selectedCountry.phoneCode}', style: theme.textTheme.titleSmall?.copyWith(fontWeight: FontWeight.bold)),
                    Icon(Icons.arrow_drop_down, size: 20, color: theme.colorScheme.outline),
                  ],
                ),
              ),
            ),
            validator: (value) {
              if (value == null || value.isEmpty) {
                return context.tr('Phone number is required');
              }
              if (value.length < 7) {
                return context.tr('Please enter a valid phone number');
              }
              return null;
            },
          ),
          const SizedBox(height: 18),
          CustomTextField(
            controller: _signupPasswordController,
            obscureText: !_isSignupPasswordVisible,
            labelText: context.tr('Password'),
            hintText: context.tr('At least 8 chars, 1 uppercase, 1 number & 1 symbol'),
            prefixIcon: Icon(Icons.lock_outline_rounded, color: theme.colorScheme.primary),
            suffixIcon: IconButton(
              icon: Icon(_isSignupPasswordVisible ? Icons.visibility : Icons.visibility_off, color: theme.colorScheme.outline),
              onPressed: () => setState(() => _isSignupPasswordVisible = !_isSignupPasswordVisible),
            ),
            validator: (value) => Validators.validatePassword(value, AppLocalizations.of(context)),
          ),
          if (_signupPasswordController.text.isNotEmpty) ...[
            const SizedBox(height: 8),
            ClipRRect(
              borderRadius: BorderRadius.circular(10),
              child: LinearProgressIndicator(
                value: _passwordStrength,
                backgroundColor: theme.colorScheme.outlineVariant.withOpacity(0.3),
                valueColor: AlwaysStoppedAnimation<Color>(_strengthColor),
                minHeight: 6,
              ),
            ),
            const SizedBox(height: 4),
            Align(
              alignment: Alignment.centerRight,
              child: Text(
                _strengthText,
                style: theme.textTheme.bodySmall?.copyWith(
                  color: _strengthColor,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ],
          const SizedBox(height: 18),
          CustomTextField(
            controller: _signupConfirmPasswordController,
            obscureText: !_isSignupConfirmPasswordVisible,
            labelText: context.tr('Confirm Password'),
            prefixIcon: Icon(Icons.lock_clock_outlined, color: theme.colorScheme.primary),
            suffixIcon: IconButton(
              icon: Icon(_isSignupConfirmPasswordVisible ? Icons.visibility : Icons.visibility_off, color: theme.colorScheme.outline),
              onPressed: () => setState(() => _isSignupConfirmPasswordVisible = !_isSignupConfirmPasswordVisible),
            ),
            validator: (value) => Validators.validateConfirmPassword(value, _signupPasswordController.text, AppLocalizations.of(context)),
          ),
          const SizedBox(height: 12),
          CheckboxListTile(
            title: Text(
              context.tr('I agree to the Terms and Conditions'),
              style: theme.textTheme.bodySmall?.copyWith(fontWeight: FontWeight.w500),
            ),
            value: _agreeToTerms,
            onChanged: (newValue) {
              setState(() {
                _agreeToTerms = newValue!;
              });
            },
            controlAffinity: ListTileControlAffinity.leading,
            contentPadding: EdgeInsets.zero,
            activeColor: theme.colorScheme.primary,
            checkboxShape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(4)),
          ),
          const SizedBox(height: 16),
          PrimaryButton(
            onPressed: isLoading ? null : _handleSignup,
            text: context.tr('Create Account'),
            isLoading: false,
          ),
        ],
      ),
    );
  }
}
