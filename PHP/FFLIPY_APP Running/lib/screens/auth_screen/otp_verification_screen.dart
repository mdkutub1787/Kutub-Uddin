import 'dart:async';
import 'dart:ui';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/theme/primary_button.dart';
import 'package:fflipy/models/auth/mail_verify_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../core/utils/dialog_helper.dart';
import '../../core/widgets/brand_app_bar.dart';
import '../../models/auth/resend_code_model.dart';
import '../../providers/auth_providers.dart';
import '../../viewmodels/auth_viewmodel.dart';

class OtpVerificationScreen extends ConsumerStatefulWidget {
  final String email;

  const OtpVerificationScreen({Key? key, required this.email}) : super(key: key);

  @override
  ConsumerState<OtpVerificationScreen> createState() =>
      _OtpVerificationScreenState();
}

class _OtpVerificationScreenState extends ConsumerState<OtpVerificationScreen> {
  final List<TextEditingController> _otpControllers =
      List.generate(6, (_) => TextEditingController());
  final List<FocusNode> _focusNodes = List.generate(6, (_) => FocusNode());
  Timer? _timer;
  int _start = 59;

  @override
  void initState() {
    super.initState();
    startTimer();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (mounted) {
        _focusNodes[0].requestFocus();
      }
    });
  }

  @override
  void dispose() {
    for (var controller in _otpControllers) {
      controller.dispose();
    }
    for (var node in _focusNodes) {
      node.dispose();
    }
    _timer?.cancel();
    super.dispose();
  }

  void startTimer() {
    _timer?.cancel();
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (!mounted) {
        timer.cancel();
        return;
      }
      if (_start == 0) {
        setState(() => timer.cancel());
      } else {
        setState(() => _start--);
      }
    });
  }

  void _verifyOtp() async {
    final otpCode = _otpControllers.map((c) => c.text).join();
    if (otpCode.length < 6) {
       DialogHelper.showSnackBar(context, context.tr("Please enter complete OTP"), isError: true);
       return;
    }

    final authState = ref.read(authViewModelProvider);
    final token = authState.tempToken ?? authState.responseModelUser?.token;

    if (token != null) {
      FocusScope.of(context).unfocus();
      final success = await ref
          .read(authViewModelProvider.notifier)
          .mailVerify(MailVerifyModel(code: otpCode, token: token));
      if (success && mounted) {
        context.go(AppRouter.home);
      }
    } else {
      if (mounted) {
        DialogHelper.showSnackBar(context, context.tr("Token not found. Please login again."), isError: true);
      }
    }
  }

  void _resendOtp() {
    final authState = ref.read(authViewModelProvider);
    final token = authState.tempToken ?? authState.responseModelUser?.token;

    if (token != null) {
      ref
          .read(authViewModelProvider.notifier)
          .resendCode(
            ResendCodeRequest(type: 'email', token: token, email: widget.email),
          );
      
      for (var controller in _otpControllers) {
        controller.clear();
      }

      setState(() {
        _start = 59;
      });
      startTimer();
    } else {
       if (mounted) {
        DialogHelper.showSnackBar(context, context.tr("Token not found. Please login again."), isError: true);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authViewModelProvider);
    final theme = Theme.of(context);

    ref.listen<AuthState>(authViewModelProvider, (previous, next) {
      if (next.error != null && next.error!.isNotEmpty) {
        DialogHelper.showSnackBar(context, context.tr(next.error!), isError: true);
      }
      if (next.successMessage != null && next.successMessage!.isNotEmpty) {
         DialogHelper.showSnackBar(context, context.tr(next.successMessage!));
      }
    });

    return Scaffold(
      appBar: BrandAppBar(
        leading: IconButton(
          icon: Icon(Icons.arrow_back_ios_new, color: theme.colorScheme.onSurface),
          onPressed: () => context.go(AppRouter.login),
        ),
      ),
      body: Stack(
        children: [
          SafeArea(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 24.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  const SizedBox(height: 20),
                  Align(
                    alignment: Alignment.center,
                    child: Container(
                      width: 80,
                      height: 80,
                      decoration: BoxDecoration(
                        color: theme.colorScheme.primaryContainer.withOpacity(0.4),
                        shape: BoxShape.circle,
                      ),
                      child: Icon(
                        Icons.lock_person_rounded,
                        size: 40,
                        color: theme.colorScheme.primary,
                      ),
                    ),
                  ),
                  const SizedBox(height: 32),
                  Text(
                    context.tr("Email Verification"),
                    style: theme.textTheme.headlineSmall?.copyWith(
                      fontWeight: FontWeight.w900,
                      color: theme.colorScheme.onSurface,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 12),
                  RichText(
                    textAlign: TextAlign.center,
                    text: TextSpan(
                      style: theme.textTheme.bodyMedium?.copyWith(
                        color: theme.colorScheme.onSurfaceVariant,
                      ),
                      children: [
                        TextSpan(text: context.tr("Email verification code has been sent")),
                        TextSpan(
                          text: "\n${widget.email}",
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: theme.colorScheme.primary,
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 48),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: List.generate(6, (index) => _buildOtpBox(index)),
                  ),
                  const SizedBox(height: 48),
                  PrimaryButton(
                    onPressed: authState.isLoading ? null : _verifyOtp,
                    text: context.tr('Confirm'),
                    isLoading: false,
                  ),
                  const SizedBox(height: 24),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        context.tr("Didn't get Code? Click to"),
                        style: theme.textTheme.bodyMedium?.copyWith(
                          color: theme.colorScheme.onSurfaceVariant,
                        ),
                      ),
                      const SizedBox(width: 4),
                      GestureDetector(
                        onTap: _start == 0 ? _resendOtp : null,
                        child: Text(
                          _start == 0
                              ? context.tr("Resend code")
                              : '${context.tr("Resend in")} $_start s',
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                            color: _start == 0 ? theme.colorScheme.primary : theme.disabledColor,
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildOtpBox(int index) {
    final theme = Theme.of(context);
    return Container(
      width: 48,
      height: 58,
      decoration: BoxDecoration(
        color: theme.colorScheme.surface,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: theme.shadowColor.withOpacity(0.06),
            offset: const Offset(0, 4),
            blurRadius: 12,
          ),
        ],
      ),
      child: TextField(
        controller: _otpControllers[index],
        focusNode: _focusNodes[index],
        keyboardType: TextInputType.number,
        textAlign: TextAlign.center,
        style: theme.textTheme.headlineSmall?.copyWith(
          fontWeight: FontWeight.bold,
          color: theme.colorScheme.onSurface,
          fontFeatures: const [FontFeature.disable('liga')],
        ),
        inputFormatters: [
          LengthLimitingTextInputFormatter(1),
          FilteringTextInputFormatter.digitsOnly,
        ],
        decoration: InputDecoration(
          contentPadding: EdgeInsets.zero,
          hintText: '-',
          hintStyle: TextStyle(
            fontSize: 24,
            color: theme.colorScheme.outline.withOpacity(0.5),
            fontWeight: FontWeight.w400,
          ),
          counterText: '',
          filled: true,
          fillColor: theme.colorScheme.surfaceContainerHighest.withOpacity(0.2),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(16),
            borderSide: BorderSide(
              color: theme.colorScheme.outline.withOpacity(0.1),
            ),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(16),
            borderSide: BorderSide(
              color: theme.colorScheme.primary,
              width: 2,
            ),
          ),
        ),
        onChanged: (value) {
          if (value.length > 1) {
          }
          
          if (value.isNotEmpty) {
            if (index < 5) {
              _focusNodes[index + 1].requestFocus();
            } else {
              _focusNodes[index].unfocus();
            }
          } else {
             if (value.isEmpty && index > 0) {
               _focusNodes[index - 1].requestFocus();
             }
          }
        },
      ),
    );
  }
}
