import 'dart:async';
import 'package:dropdown_button2/dropdown_button2.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/utils/dialog_helper.dart';
import 'package:fflipy/core/widgets/empty_state_widget.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/models/beneficiary/beneficiary_model.dart';
import 'package:fflipy/models/send_money/send_money_payment_details.dart';
import 'package:fflipy/providers/profile_providers.dart';
import 'package:fflipy/providers/send_money_providers.dart';
import 'package:fflipy/viewmodels/send_money_view_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';
import '../../core/widgets/brand_app_bar.dart';
import 'package:pin_code_fields/pin_code_fields.dart';
import '../../core/routing/app_router.dart';
import '../../models/send_money/send_money_cal_service_crg.dart';

class SendMoneyScreen extends ConsumerStatefulWidget {
  const SendMoneyScreen({super.key});

  @override
  ConsumerState<SendMoneyScreen> createState() => _SendMoneyScreenState();
}

class _SendMoneyScreenState extends ConsumerState<SendMoneyScreen> {
  int _currentStep = 0;
  final TextEditingController _amountController = TextEditingController();
  final TextEditingController _remarksController = TextEditingController();
  final TextEditingController _searchController = TextEditingController();
  Timer? _debounce;
  String? _amountErrorText;

  @override
  void initState() {
    super.initState();
    _remarksController.text = ref.read(remarksProvider);
    _searchController.addListener(() {
      setState(() {
      });
    });
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(sendMoneyViewModelProvider.notifier).getBeneficiaries();
    });
  }

  void _initializeCountries() {
    final paymentDetails = ref.read(sendMoneyViewModelProvider).paymentDetailsResponse.asData?.value?.data;
    if (paymentDetails != null) {
      ref.read(selectedFromCountryProvider.notifier).state = paymentDetails.senderCountry;
      ref.read(selectedToCountryProvider.notifier).state = paymentDetails.receiverCountry;
    }
  }

  @override
  void dispose() {
    _amountController.dispose();
    _remarksController.dispose();
    _searchController.dispose();
    _debounce?.cancel();
    super.dispose();
  }

  void _showOtpDialog({
    required String email,
    required String transactionToken,
    required int expiresIn,
  }) {
    final otpController = TextEditingController();
    final ValueNotifier<int> countdown = ValueNotifier<int>(expiresIn);
    final ValueNotifier<bool> isLoading = ValueNotifier<bool>(false);
    final ValueNotifier<bool> showPreloader = ValueNotifier<bool>(false);
    Timer? timer;

    void startTimer(int seconds) {
      countdown.value = seconds;
      timer?.cancel();
      timer = Timer.periodic(const Duration(seconds: 1), (t) {
        if (countdown.value > 0) {
          countdown.value--;
        } else {
          t.cancel();
        }
      });
    }

    startTimer(expiresIn);

    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (dialogContext) {
        final theme = Theme.of(dialogContext);
        return PopScope(
          canPop: false,
          child: AlertDialog(
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
            title: Column(
              children: [
                Icon(Icons.email_outlined, size: 40, color: theme.primaryColor),
                const SizedBox(height: 8),
                Text(
                  context.tr('OTP Verification'),
                  textAlign: TextAlign.center,
                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
                ),
              ],
            ),
            content: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    context.tr('A 6-digit code has been sent to your email:'),
                    textAlign: TextAlign.center,
                    style: const TextStyle(fontSize: 16),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    email,
                    textAlign: TextAlign.center,
                    style: theme.textTheme.bodyMedium?.copyWith(
                      fontWeight: FontWeight.bold,
                      color: theme.colorScheme.primary,
                    ),
                  ),
                  const SizedBox(height: 20),
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 0, vertical: 0),
                    decoration: BoxDecoration(
                      color: Colors.transparent,
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: PinCodeTextField(
                      appContext: context,
                      length: 6,
                      controller: otpController,
                      autoFocus: true,
                      animationType: AnimationType.fade,
                      keyboardType: TextInputType.number,
                      pinTheme: PinTheme(
                        shape: PinCodeFieldShape.box,
                        borderRadius: BorderRadius.circular(10),
                        fieldHeight: 48,
                        fieldWidth: 40,
                        activeColor: theme.colorScheme.primary,
                        selectedColor: theme.colorScheme.primary,
                        inactiveColor: theme.dividerColor,
                        activeFillColor: theme.cardColor,
                        selectedFillColor: theme.cardColor,
                        inactiveFillColor: theme.cardColor,
                        borderWidth: 1.5,
                      ),
                      animationDuration: const Duration(milliseconds: 200),
                      enableActiveFill: true,
                      onChanged: (value) {},
                      cursorColor: theme.colorScheme.primary,
                      textStyle: theme.textTheme.titleLarge?.copyWith(
                        color: theme.colorScheme.onSurface,
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  ValueListenableBuilder<int>(
                    valueListenable: countdown,
                    builder: (context, value, child) {
                      if (value > 0) {
                        return Column(
                          children: [
                            Text(
                              context.tr('OTP will expire in'),
                              style: TextStyle(color: theme.disabledColor),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              '${(value ~/ 60).toString().padLeft(2, '0')}:${(value % 60).toString().padLeft(2, '0')}',
                              style: theme.textTheme.bodyLarge?.copyWith(
                                fontWeight: FontWeight.bold,
                                color: theme.colorScheme.error,
                              ),
                            ),
                            const SizedBox(height: 8),
                            Text(
                              context.tr('Didn\'t receive the code?'),
                              style: TextStyle(color: theme.disabledColor),
                            ),
                            Text(
                              context.tr('You can resend OTP in {value} seconds').replaceAll('{value}', value.toString()),
                              style: theme.textTheme.bodySmall?.copyWith(
                                color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
                              ),
                            ),
                          ],
                        );
                      }
                      return TextButton(
                        onPressed: () async {
                          final response = await ref.read(sendMoneyViewModelProvider.notifier).resendOtp(transactionToken: transactionToken);
                          if (response != null && response.success) {
                            startTimer(response.data?.expiresIn ?? 120);
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text(context.tr('A new OTP has been sent')), backgroundColor: theme.colorScheme.secondary),
                            );
                          } else {
                            final error = ref.read(sendMoneyViewModelProvider).resendOtpResponse.error;
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text(error != null ? context.tr(ErrorHandler.getErrorMessage(error)) : context.tr('Failed to resend OTP')), backgroundColor: theme.colorScheme.error),
                            );
                          }
                        },
                        child: Text(context.tr('Resend OTP')),
                      );
                    },
                  ),
                ],
              ),
            ),
            actionsAlignment: MainAxisAlignment.center,
            actions: [
              TextButton(
                onPressed: () {
                  timer?.cancel();
                  ref.read(sendMoneyViewModelProvider.notifier).resetBeneficiarySelection();
                  ref.read(sendMoneyViewModelProvider.notifier).resetCalculation();
                  _clearAmountAndRelatedInputs();
                  setState(() => _currentStep = 0);
                  Navigator.of(dialogContext).pop();
                },
                child: Text(context.tr('Cancel')),
              ),
              ValueListenableBuilder<bool>(
                valueListenable: isLoading,
                builder: (context, loading, child) {
                  return ValueListenableBuilder<bool>(
                    valueListenable: showPreloader,
                    builder: (context, preloader, _) {
                      return ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 14),
                          backgroundColor: theme.primaryColor,
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                        ),
                        onPressed: loading || preloader ? null : () async {
                          final purposeId = ref.read(selectedPurposeIdProvider);
                          final remarks = ref.read(remarksProvider);
                          final otpCode = otpController.text.trim();
                          if (otpCode.length < 6) {
                            ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(context.tr('Please enter the 6-digit OTP.'))));
                            return;
                          }
                          if (purposeId == null) {
                            ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(context.tr('Please select a purpose of transfer.'))));
                            return;
                          }
                          isLoading.value = true;
                          showPreloader.value = true;
                          try {
                            final response = await ref.read(sendMoneyViewModelProvider.notifier).verifyOtp(
                              transactionToken: transactionToken,
                              otp: otpCode,
                              purposeOfTransfer: purposeId,
                              remarks: remarks,
                            );
                            if (!mounted) return;
                            if (response != null && response.success == true && response.data?.referenceNumber != null) {
                              final referenceNumber = response.data!.referenceNumber;
                              final message = response.message;
                              timer?.cancel();
                              Navigator.of(dialogContext).pop();
                              context.goNamed(
                                'sendMoneySuccess',
                                extra: {'reference_number': referenceNumber, 'message': message},
                              );
                            } else if (response != null && response.success == true) {
                              // fallback for old bool response
                              final message = response.message;
                              timer?.cancel();
                              Navigator.of(dialogContext).pop();
                              context.goNamed('sendMoneySuccess', extra: {'message': message});
                            } else {
                              throw Exception('Transaction failed. Please check your OTP.');
                            }
                            } catch (e) {
                              if (mounted) {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  SnackBar(content: Text(context.tr(ErrorHandler.getErrorMessage(e))), backgroundColor: theme.colorScheme.error),
                                );
                              }
                            } finally {
                            isLoading.value = false;
                            showPreloader.value = false;
                          }
                        },
                        child: preloader
                          ? const Preloader()
                          : loading
                            ? SizedBox(width: 20, height: 20, child: CircularProgressIndicator(strokeWidth: 2, color: theme.colorScheme.onPrimary))
                            : Text(context.tr('Confirm'), style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                      );
                    },
                  );
                },
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final sendMoneyState = ref.watch(sendMoneyViewModelProvider);
    Theme.of(context);

    ref.listen<SendMoneyState>(sendMoneyViewModelProvider, (previous, next) {
      final prevOtpData = previous?.otpGenerateResponse.asData?.value;
      final nextOtpData = next.otpGenerateResponse.asData?.value;
      if (nextOtpData != null && prevOtpData != nextOtpData && nextOtpData.success) {
        final senderEmail = next.step3StoreResponse.asData?.value?.data?.senderInfo.email;
        final transactionToken = next.step2StoreResponse.asData?.value?.data?.transactionToken;
        if (senderEmail != null && transactionToken != null) {
          _showOtpDialog(
            email: senderEmail,
            transactionToken: transactionToken,
            expiresIn: nextOtpData.data?.expiresIn ?? 120,
          );
        }
      }
      final prevPaymentDetails = previous?.paymentDetailsResponse.asData?.value;
      final nextPaymentDetails = next.paymentDetailsResponse.asData?.value;
      if (nextPaymentDetails != null && prevPaymentDetails != nextPaymentDetails) {
        _initializeCountries();
      }
    });

    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) {
        if (didPop) return;
        Future.microtask(() {
          ref.read(sendMoneyViewModelProvider.notifier).resetBeneficiarySelection();
          _clearAmountAndRelatedInputs();
        });
        context.pop();
      },
      child: Scaffold(
        appBar: BrandAppBar(
          title: Text(sendMoneyState.selectedBeneficiary == null ? context.tr('Available Beneficiary') : context.tr('Send Money')),
        ),
        body: Stack(
          children: [
            sendMoneyState.selectedBeneficiary == null
                ? _buildBeneficiarySelection(sendMoneyState)
                : Column(
              children: [
                _buildStepIndicator(context),
                const SizedBox(height: 8),
                Expanded(
                  child: AnimatedSwitcher(
                    duration: const Duration(milliseconds: 300),
                    child: _currentStep == 0
                        ? _buildAmountStep(context, sendMoneyState.selectedBeneficiary!)
                        : _buildConfirmStep(context),
                  ),
                ),
              ],
            ),
            if (sendMoneyState.isLoading)
              const Preloader(),
          ],
        ),
        floatingActionButton: sendMoneyState.selectedBeneficiary == null
            ? FloatingActionButton.extended(
          onPressed: () => context.push(AppRouter.addBeneficiary).then((_) {
            ref.read(sendMoneyViewModelProvider.notifier).getBeneficiaries();
          }),
          label: Text(context.tr('Add Beneficiary')),
          icon: const Icon(Icons.add),
          elevation: 4,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        )
            : null,
      ),
    );
  }

  Widget _buildBeneficiarySelection(SendMoneyState sendMoneyState) {
    return sendMoneyState.beneficiaryListResponse.when(
      loading: () => const SizedBox.shrink(),
      error: (error, stack) => Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(context.tr(ErrorHandler.getErrorMessage(error))),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: () => ref.read(sendMoneyViewModelProvider.notifier).getBeneficiaries(),
              child: Text(context.tr('Retry')),
            ),
          ],
        ),
      ),
      data: (data) {
        final List<BeneficiaryModel> beneficiaries = data?.data.beneficiaries.data ?? <BeneficiaryModel>[];
        if (beneficiaries.isEmpty) {
          return RefreshIndicator(
            onRefresh: () async {
              await ref.read(sendMoneyViewModelProvider.notifier).getBeneficiaries();
            },
            child: ListView(
              physics: const AlwaysScrollableScrollPhysics(),
              children: [
                SizedBox(height: MediaQuery.of(context).size.height * 0.6, child: EmptyStateWidget(message: context.tr('No beneficiaries found. Add one!'))),
              ],
            ),
          );
        }

        return RefreshIndicator(
          onRefresh: () async {
            await ref.read(sendMoneyViewModelProvider.notifier).getBeneficiaries();
          },
          child: ListView.builder(
            physics: const AlwaysScrollableScrollPhysics(),
            padding: const EdgeInsets.only(top: 10, bottom: 80),
            itemCount: beneficiaries.length,
            itemBuilder: (context, index) {
              final beneficiary = beneficiaries[index];
              final isSelected = sendMoneyState.selectedBeneficiary?.id == beneficiary.id;

              return BeneficiaryListItem(
                beneficiary: beneficiary,
                isSelected: isSelected,
                onTap: () async {
                  if (isSelected) {
                    ref.read(sendMoneyViewModelProvider.notifier).unselectBeneficiary();
                  } else {
                    await ref.read(sendMoneyViewModelProvider.notifier).selectBeneficiary(beneficiary);
                    _clearAmountAndRelatedInputs();
                  }
                },
                onProceed: () {
                  setState(() => _currentStep = 1);
                },
              );
            },
          ),
        );
      },
    );
  }

  Widget _buildStepIndicator(BuildContext context) {
    final theme = Theme.of(context);
    return Container(
        color: theme.cardColor,
        padding: const EdgeInsets.symmetric(vertical: 20.0, horizontal: 40.0),
        child: Row(
          children: [
            _buildStep(context, icon: Icons.money, title: context.tr('Amount'), isActive: _currentStep >= 0),
            Expanded(child: _buildStepConnector(isCompleted: _currentStep > 0)),
            _buildStep(context, icon: Icons.check_circle_outline, title: context.tr('Confirmation'), isActive: _currentStep >= 1),
          ],
        ));
  }

  Widget _buildStep(BuildContext context, {required IconData icon, required String title, bool isActive = false}) {
    final theme = Theme.of(context);
    final color = isActive
        ? theme.colorScheme.primary
        : theme.colorScheme.onSurface.withAlpha((255 * 0.8).toInt());

    return Column(
      children: [
        Icon(icon, color: color, size: 28),
        const SizedBox(height: 8),
        Text(
          title,
          style: theme.textTheme.bodyMedium?.copyWith(
            color: color,
            fontWeight: isActive ? FontWeight.bold : FontWeight.normal,
          ),
        ),
      ],
    );
  }

  Widget _buildStepConnector({bool isCompleted = false}) {
    final theme = Theme.of(context);
    return Container(
      height: 4,
      margin: const EdgeInsets.symmetric(horizontal: 12.0),
      alignment: Alignment.center,
      child: Container(
        height: 3,
        width: double.infinity,
        decoration: BoxDecoration(
          color: isCompleted 
              ? theme.colorScheme.secondary
              : theme.colorScheme.secondary,
          borderRadius: BorderRadius.circular(2),
        ),
      ),
    );
  }

  Widget _buildAmountStep(BuildContext context, BeneficiaryModel beneficiary) {
    final theme = Theme.of(context);
    final calculationAsync = ref.watch(sendMoneyViewModelProvider.select((s) => s.calculationResponse));
    final fromCountry = ref.watch(selectedFromCountryProvider);
    final toCountry = ref.watch(selectedToCountryProvider);
    final availableCountries = ref.watch(sendMoneyViewModelProvider.select((s) => s.beneficiaryListResponse.asData?.value?.data.countries ?? []));

    ref.listen<AsyncValue<SendMoneyCalServiceCrgResponse?>>(
      sendMoneyViewModelProvider.select((s) => s.calculationResponse),
          (_, next) {
        if (next is AsyncError) {
          final error = next as AsyncError;
          if (mounted) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text(context.tr(ErrorHandler.getErrorMessage(error.error))), backgroundColor: theme.colorScheme.error),
            );
          }
        }
      },
    );

    final buttonStyle = ElevatedButton.styleFrom(
      padding: const EdgeInsets.symmetric(vertical: 16),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      textStyle: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
    ).copyWith(
      backgroundColor: WidgetStateProperty.resolveWith<Color?>((states) => states.contains(WidgetState.disabled) ? theme.disabledColor : theme.primaryColor),
      foregroundColor: WidgetStateProperty.resolveWith<Color?>((states) => states.contains(WidgetState.disabled) ? theme.colorScheme.onSurface.withAlpha((255 * 0.38).toInt()) : theme.colorScheme.onPrimary),
    );

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        _buildBeneficiaryInfo(beneficiary, theme),
        Expanded(
          child: SingleChildScrollView(
            key: const ValueKey('amount_step'),
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                _buildAmountConversionCard(
                  context: context,
                  amountController: _amountController,
                  fromCountry: fromCountry,
                  toCountry: toCountry,
                  errorText: _amountErrorText,
                  availableCountries: availableCountries,
                  onCountryChanged: (newCountry) => _onFromCountryChanged(newCountry, toCountry?.id),
                  onAmountChanged: (value) => _onAmountChanged(value, fromCountry, toCountry?.id),
                  calculationAsync: calculationAsync,
                ),
                const SizedBox(height: 32),
                _buildNavigationButtons(
                  context,
                  onPrevious: () {
                    ref.read(sendMoneyViewModelProvider.notifier).resetBeneficiarySelection();
                    setState(() => _currentStep = 0);
                  },
                  onNext: _amountErrorText == null && calculationAsync.hasValue && !calculationAsync.isLoading
                      ? () async {
                    final profileData = ref.read(profileViewModelProvider).asData?.value;
                    if (profileData != null) {
                      final user = profileData.userProfile;
                      final monthlyIncome = double.tryParse(user.yearlyIncome ?? '0') ?? 0;
                      final dailyLimit = double.tryParse(user.dailyLimit ?? '0') ?? 0;
                      final monthlyLimit = double.tryParse(user.monthlyLimit ?? '0') ?? 0;
                      final sendAmount = double.tryParse(_amountController.text) ?? 0;

                      if (monthlyIncome > 0 && sendAmount > monthlyIncome) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(context.tr('Transfer amount exceeds your monthly income ({limit}). Please update your profile if your income has changed.').replaceAll('{limit}', monthlyIncome.toStringAsFixed(2))),
                            backgroundColor: theme.colorScheme.error,
                          ),
                        );
                        return;
                      }

                      if (dailyLimit > 0 && sendAmount > dailyLimit) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(context.tr('Transfer amount exceeds your daily limit ({limit}).').replaceAll('{limit}', dailyLimit.toStringAsFixed(2))),
                            backgroundColor: theme.colorScheme.error,
                          ),
                        );
                        return;
                      }

                      if (monthlyLimit > 0 && sendAmount > monthlyLimit) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(context.tr('Transfer amount exceeds your monthly limit ({limit}).').replaceAll('{limit}', monthlyLimit.toStringAsFixed(2))),
                            backgroundColor: theme.colorScheme.error,
                          ),
                        );
                        return;
                      }
                    }

                    final success = await ref.read(sendMoneyViewModelProvider.notifier).proceedToConfirmation();
                    if (success && mounted) {
                      setState(() => _currentStep = 1);
                    } else if (mounted) {
                      final error = ref.read(sendMoneyViewModelProvider).step2StoreResponse.error ?? ref.read(sendMoneyViewModelProvider).step3StoreResponse.error;
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text(error != null ? context.tr(ErrorHandler.getErrorMessage(error)) : context.tr('Failed to proceed. Please try again.')), backgroundColor: theme.colorScheme.error),
                      );
                    }
                  }
                      : null,
                  buttonStyle: buttonStyle,
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildBeneficiaryInfo(BeneficiaryModel beneficiary, ThemeData theme) {
    final String initials = (beneficiary.firstName.isNotEmpty && beneficiary.lastName.isNotEmpty)
        ? '${beneficiary.firstName[0]}${beneficiary.lastName[0]}'.toUpperCase()
        : '?';

    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: theme.cardColor,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withAlpha((255 * 0.06).toInt()),
            blurRadius: 24,
            offset: const Offset(0, 8),
            spreadRadius: -4,
          ),
        ],
      ),
      child: Row(
        children: [
          Container(
            height: 56,
            width: 56,
            decoration: BoxDecoration(
              color: theme.colorScheme.secondary.withAlpha((255 * 0.3).toInt()),
              shape: BoxShape.circle,
              border: Border.all(
                color: theme.colorScheme.secondary.withAlpha((255 * 0.6).toInt()),
                width: 3,
              ),
            ),
            child: Center(
              child: Text(
                initials,
                style: theme.textTheme.titleLarge?.copyWith(
                  color: theme.colorScheme.secondary,
                  fontWeight: FontWeight.w700,
                  fontSize: 20,
                ),
              ),
            ),
          ),
          const SizedBox(width: 16),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(context.tr('Sending to'),
                  style: theme.textTheme.bodySmall?.copyWith(
                    color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
                    letterSpacing: 0.5,
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  '${beneficiary.firstName} ${beneficiary.lastName}',
                  style: theme.textTheme.titleMedium?.copyWith(
                    fontWeight: FontWeight.w700,
                    fontSize: 16,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                if (beneficiary.phoneNumber != null) ...[
                  const SizedBox(height: 2),
                  Row(
                    children: [
                      Icon(
                        Icons.phone_iphone_rounded,
                        size: 14,
                        color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
                      ),
                      const SizedBox(width: 4),
                      Text(
                        beneficiary.phoneNumber!,
                        style: theme.textTheme.bodyMedium?.copyWith(
                          color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ],
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }


  void _onFromCountryChanged(Country newCountry, int? toCountryId) {
    ref.read(sendMoneyViewModelProvider.notifier).changeFromCountry(newCountry);
    final sendAmount = double.tryParse(_amountController.text) ?? 0.0;
    if (sendAmount > 0 && toCountryId != null) {
      if (newCountry.id == toCountryId) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(context.tr('Sender and receiver country cannot be the same.'))),
        );
        ref.read(sendMoneyViewModelProvider.notifier).resetCalculation();
        return;
      }
      ref.read(sendMoneyViewModelProvider.notifier).calculateServiceCharge(
        amount: sendAmount,
        fromCountryId: newCountry.id,
        toCountryId: toCountryId,
      );
    }
  }

  void _onAmountChanged(String value, CountryDetails? fromCountry, int? toId) {
    if (_debounce?.isActive ?? false) _debounce!.cancel();
    _debounce = Timer(const Duration(milliseconds: 500), () {
      final sendAmount = double.tryParse(value) ?? 0.0;
      ref.read(amountProvider.notifier).state = sendAmount;
      final fromId = fromCountry?.id;

      if (fromId != null && toId != null) {
        if (fromId == toId) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(context.tr('Sender and receiver country cannot be the same.'))),
          );
          ref.read(sendMoneyViewModelProvider.notifier).resetCalculation();
          return;
        }

        final minAmount = fromCountry?.minimumAmount?.toDouble() ?? 0.0;
        if (sendAmount > 0 && sendAmount < minAmount) {
          setState(() {
            _amountErrorText = context.tr('Minimum Amount Is').replaceAll('{min}', minAmount.toStringAsFixed(2));
          });
          ref.read(sendMoneyViewModelProvider.notifier).resetCalculation();
        } else {
          setState(() {
            _amountErrorText = null;
          });
          if (sendAmount > 0) {
            ref.read(sendMoneyViewModelProvider.notifier).calculateServiceCharge(
              amount: sendAmount,
              fromCountryId: fromId,
              toCountryId: toId,
            );
          } else {
            ref.read(sendMoneyViewModelProvider.notifier).resetCalculation();
          }
        }
      }
    });
  }

  Widget _buildNavigationButtons(BuildContext context, {required VoidCallback onPrevious, required VoidCallback? onNext, required ButtonStyle buttonStyle}) {
    return Row(
      children: [
        Expanded(
          child: OutlinedButton(
            style: OutlinedButton.styleFrom(padding: const EdgeInsets.symmetric(vertical: 16)),
            onPressed: onPrevious,
            child: Text(context.tr('Cancel')),
          ),
        ),
        const SizedBox(width: 16),
        Expanded(
          child: ElevatedButton(
            onPressed: onNext,
            style: OutlinedButton.styleFrom(padding: const EdgeInsets.symmetric(vertical: 16)),
            child: Text(context.tr('Continue')),
          ),
        ),
      ],
    );
  }

  Widget _buildConfirmStep(BuildContext context) {
    final theme = Theme.of(context);
    final colorScheme = theme.colorScheme;
    final step3Async =
    ref.watch(sendMoneyViewModelProvider.select((s) => s.step3StoreResponse));
    final confirmDetailsChecked = ref.watch(confirmDetailsProvider);

    final buttonStyle = ElevatedButton.styleFrom(
      padding: const EdgeInsets.symmetric(vertical: 18),
      elevation: 0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      textStyle: GoogleFonts.poppins(fontSize: 16, fontWeight: FontWeight.w600),
    ).copyWith(
      backgroundColor: WidgetStateProperty.resolveWith<Color?>((states) =>
      states.contains(WidgetState.disabled)
          ? theme.disabledColor
          : theme.primaryColor),
      foregroundColor: WidgetStateProperty.resolveWith<Color?>((states) =>
      states.contains(WidgetState.disabled)
          ? theme.colorScheme.onSurface.withAlpha((255 * 0.38).toInt())
          : theme.colorScheme.onPrimary),
    );

    return step3Async.when(
      loading: () => const Center(
          child: CircularProgressIndicator.adaptive()),
      error: (err, stack) => Center(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(Icons.error_outline, size: 48, color: colorScheme.error),
              const SizedBox(height: 16),
              Text(
                'Something went wrong',
                style: theme.textTheme.titleMedium,
              ),
              Text(
                context.tr(ErrorHandler.getErrorMessage(err)),
                textAlign: TextAlign.center,
                style: theme.textTheme.bodySmall,
              ),
            ],
          ),
        ),
      ),
      data: (step3data) {
        if (step3data?.data == null) {
          return EmptyStateWidget(
              message: context.tr('Could not load confirmation details. Please go back and try again.'));
        }
        final details = step3data!.data!;
        final beneficiaryDetails = details.beneficiary;
        final nameParts = beneficiaryDetails.fullName.split(' ');
        final walletNumber = beneficiaryDetails.walletNumber;
        final accountNumber = beneficiaryDetails.accountNumber;
        final initials = nameParts.isNotEmpty
            ? nameParts[0][0].toUpperCase() +
            (nameParts.length > 1 ? nameParts[1][0].toUpperCase() : '')
            : '?';
        final purposes = details.sendingPurposes;
        final summary = details.transactionSummary;

        return SingleChildScrollView(
          key: const ValueKey('confirm_step'),
          padding: const EdgeInsets.fromLTRB(20, 10, 20, 30),
          physics: const BouncingScrollPhysics(),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(context.tr('Review Transfer'),
                  style: theme.textTheme.headlineSmall?.copyWith(
                    fontWeight: FontWeight.w700,
                    letterSpacing: -0.5,
                  )),
              const SizedBox(height: 8),
              Text(context.tr('Please review the details below'),
                  style: theme.textTheme.bodyMedium
                      ?.copyWith(color: theme.colorScheme.onSurface.withOpacity(0.7))),
              const SizedBox(height: 24),

              Container(
                decoration: BoxDecoration(
                  color: theme.cardColor,
                  borderRadius: BorderRadius.circular(20),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withAlpha((255 * 0.05).toInt()),
                      blurRadius: 20,
                      offset: const Offset(0, 4),
                    )
                  ],
                ),
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(20),
                      child: Row(
                        children: [
                          Container(
                            width: 56,
                            height: 56,
                            decoration: BoxDecoration(
                              color: theme.colorScheme.secondary.withAlpha((255 * 0.3).toInt()),
                              shape: BoxShape.circle,
                              border: Border.all(
                                color: theme.colorScheme.secondary.withAlpha((255 * 0.6).toInt()),
                                width: 3,
                              ),
                            ),
                            child: Center(
                              child: Text(
                                initials,
                                style: theme.textTheme.titleLarge?.copyWith(
                                  color: theme.colorScheme.secondary,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(width: 16),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  beneficiaryDetails.fullName,
                                  style: theme.textTheme.titleMedium?.copyWith(
                                    fontWeight: FontWeight.w700,
                                    fontSize: 18,
                                  ),
                                ),
                                const SizedBox(height: 4),
                                Row(
                                  children: [
                                    Container(
                                      padding: const EdgeInsets.all(4),
                                      decoration: BoxDecoration(
                                        color: theme.colorScheme.secondary.withAlpha((255 * 0.1).toInt()),
                                        shape: BoxShape.circle,
                                      ),
                                      child: Icon(
                                        walletNumber != null
                                            ? Icons.account_balance_wallet_outlined
                                            : Icons.account_balance_outlined,
                                        size: 14,
                                        color: theme.colorScheme.secondary,
                                      ),
                                    ),
                                    const SizedBox(width: 6),
                                    Text(
                                      walletNumber ?? accountNumber ?? 'N/A',
                                      style: theme.textTheme.bodyMedium?.copyWith(
                                        color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
                                        fontWeight: FontWeight.w500,
                                      ),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                          IconButton(
                            onPressed: () {
                              ref.read(sendMoneyViewModelProvider.notifier).resetBeneficiarySelection();
                              _clearAmountAndRelatedInputs();
                              setState(() => _currentStep = 0);
                            },
                            icon: const Icon(Icons.edit_outlined),
                                  style: IconButton.styleFrom(
                              foregroundColor: theme.colorScheme.secondary,
                              backgroundColor: theme.colorScheme.secondary.withAlpha((255 * 0.1).toInt()),
                            ),
                          ),
                        ],
                      ),
                    ),
                    Divider(height: 1, color: theme.dividerColor.withAlpha((255 * 0.5).toInt())),

                    Padding(
                      padding: const EdgeInsets.all(20),
                      child: Column(
                        children: [
                          _buildModernDetailRow(
                              context,
                              context.tr('You Send'),
                              '${summary.sendAmount} ${summary.senderCurrency.code}',
                              isBold: true),
                          const SizedBox(height: 12),
                          _buildModernDetailRow(
                              context,
                              context.tr('They Receive'),
                              '${summary.receivedAmount.toStringAsFixed(2)} ${summary.receiverCurrency.code}',
                              isBold: true,
                              valueColor: Colors.green),
                          const SizedBox(height: 12),
                          _buildModernDetailRow(
                            context,
                            context.tr('Exchange Rate'),
                            '1 ${summary.senderCurrency.code} = ${summary.exchangeRate.toStringAsFixed(4)} ${summary.receiverCurrency.code}',
                            icon: Icons.currency_exchange,
                          ),
                          const SizedBox(height: 12),
                          _buildModernDetailRow(
                            context,
                            context.tr('Fees'),
                            '${summary.fee} ${summary.senderCurrency.code}',
                            icon: Icons.receipt_long,
                          ),
                          if (beneficiaryDetails.email.isNotEmpty) ...[
                            const SizedBox(height: 12),
                            _buildModernDetailRow(
                              context,
                              context.tr('Recipient'),
                              beneficiaryDetails.email,
                              icon: Icons.email_outlined,
                            ),
                          ],
                        ],
                      ),
                    ),

                    Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 20, vertical: 16),
                      decoration: BoxDecoration(
                        color: theme.colorScheme.surfaceContainerHighest.withAlpha((255 * 0.3).toInt()),
                        borderRadius: const BorderRadius.only(
                          bottomLeft: Radius.circular(20),
                          bottomRight: Radius.circular(20),
                        ),
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(context.tr('Total Payable'),
                              style: theme.textTheme.titleMedium
                                  ?.copyWith(color: theme.colorScheme.onSurface.withOpacity(0.7))),
                          Text(
                            '${summary.totalPayable} ${summary.senderCurrency.code}',
                            style: theme.textTheme.headlineSmall?.copyWith(
                              fontWeight: FontWeight.w800,
                              color: theme.colorScheme.secondary,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 24),

              Text(context.tr('Select Transfer Reason *'),
                  style: theme.textTheme.titleMedium),
              const SizedBox(height: 4),

              DropdownButtonFormField2<int>(
                value: ref.watch(selectedPurposeIdProvider),
                isExpanded: true,
                decoration: InputDecoration(
                  hintText: context.tr('Transfer Reason'),
                  prefixIcon: Icon(Icons.category_outlined, color: theme.colorScheme.secondary),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  filled: true,
                  fillColor: theme.cardColor,
                  contentPadding: const EdgeInsets.symmetric(vertical: 10, horizontal: 12),
                ),
                buttonStyleData: ButtonStyleData(
                  height: 30,
                  padding: const EdgeInsets.only(left: 12, right: 12),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12),
                    color: theme.cardColor,
                  ),
                ),
                iconStyleData: IconStyleData(
                  icon: const Icon(Icons.keyboard_arrow_down_rounded),
                  iconSize: 24,
                  iconEnabledColor: theme.colorScheme.secondary,
                ),
                dropdownStyleData: DropdownStyleData(
                  maxHeight: 300,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12),
                    color: theme.cardColor,
                  ),
                ),
                items: purposes
                    .map((p) => DropdownMenuItem<int>(
                          value: p.id,
                          child: Text(p.title, style: theme.textTheme.bodyMedium),
                        ))
                    .toList(),
                onChanged: (value) =>
                    ref.read(selectedPurposeIdProvider.notifier).state = value,
                validator: (value) => value == null ? context.tr('Please select a Transfer reason') : null,
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _remarksController,
                maxLines: 3,
                decoration: InputDecoration(
                  labelText: context.tr('Remarks (Optional)'),
                  hintText: context.tr('Write a note...'),
                  alignLabelWithHint: true,
                  prefixIcon: Padding(
                    padding: const EdgeInsets.only(bottom: 40),
                    child: Icon(Icons.note_alt_outlined, color: theme.colorScheme.secondary),
                  ),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  filled: true,
                  fillColor: theme.cardColor,
                ),
                onChanged: (value) =>
                ref.read(remarksProvider.notifier).state = value,
              ),

              const SizedBox(height: 20),

              Container(
                decoration: BoxDecoration(
                  border: Border.all(color: theme.dividerColor),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: CheckboxListTile(
                  contentPadding: const EdgeInsets.symmetric(horizontal: 8),
                  title: Text(context.tr(
                    'I confirm that the payment details above are correct.'),
                    style: theme.textTheme.bodyMedium,
                  ),
                  value: confirmDetailsChecked,
                  onChanged: (value) => ref
                      .read(confirmDetailsProvider.notifier)
                      .state = value ?? false,
                  controlAffinity: ListTileControlAffinity.leading,
                  activeColor: theme.primaryColor,
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12)),
                ),
              ),

              const SizedBox(height: 32),

              Row(
                children: [
                  Expanded(
                    child: OutlinedButton(
                      style: OutlinedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 18),
                        side: BorderSide(color: theme.dividerColor),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(16),
                        ),
                      ),
                      onPressed: () {
                        ref.read(sendMoneyViewModelProvider.notifier).resetBeneficiarySelection();
                        _clearAmountAndRelatedInputs();
                        setState(() => _currentStep = 0);
                      },
                      child: Text(context.tr('Back'),
                          style: TextStyle(color: theme.textTheme.bodyLarge?.color)),
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    flex: 2,
                    child: ElevatedButton(
                      style: buttonStyle,
                      onPressed: confirmDetailsChecked
                          ? () async {
                        final selectedReason = ref.read(selectedPurposeIdProvider);
                        if (selectedReason == null) {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text(context.tr('Please select a reason'))),
                          );
                          return;
                        }

                        final confirmed = await DialogHelper.showConfirmationDialog(
                          context: context,
                          title: context.tr('Confirm Payment'),
                          message: context.tr('Are you sure you want to proceed with this transfer?'),
                        );
                        if (confirmed != true) return;

                        final transactionToken = ref
                            .read(sendMoneyViewModelProvider)
                            .step2StoreResponse
                            .asData
                            ?.value
                            ?.data
                            ?.transactionToken;
                        if (transactionToken != null) {
                          ref
                              .read(sendMoneyViewModelProvider.notifier)
                              .generateOtp(transactionToken: transactionToken);
                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text(context.tr('Transaction Token missing. Please try again.'))),
                          );
                        }
                      }
                          : null,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(context.tr('Confirm & Pay')),
                          const SizedBox(width: 8),
                          const Icon(Icons.arrow_forward_rounded, size: 20),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 20),
            ],
          ),
        );
      },
    );
  }

  Widget _buildModernDetailRow(
      BuildContext context, String title, String value,
      {bool isBold = false,
        IconData? icon,
        Color? valueColor}) {
    final theme = Theme.of(context);
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Row(
          children: [
            if (icon != null) ...[
              Container(
                padding: const EdgeInsets.all(6),
                decoration: BoxDecoration(
                  color: theme.colorScheme.secondary.withAlpha((255 * 0.1).toInt()),
                  shape: BoxShape.circle,
                  border: Border.all(
                    color: theme.colorScheme.secondary.withAlpha((255 * 0.2).toInt()),
                    width: 1,
                  ),
                ),
                child: Icon(icon, size: 16, color: theme.colorScheme.secondary),
              ),
              const SizedBox(width: 8),
            ],
            Text(
              title,
              style: theme.textTheme.bodyMedium?.copyWith(
                color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
              ),
            ),
          ],
        ),
        Text(
          value,
          style: theme.textTheme.bodyMedium?.copyWith(
            fontWeight: isBold ? FontWeight.bold : FontWeight.w500,
            fontSize: isBold ? 16 : 14,
            color: valueColor ?? theme.textTheme.bodyMedium?.color,
          ),
        ),
      ],
    );
  }

  Widget _buildAmountConversionCard({
    required BuildContext context,
    required TextEditingController amountController,
    required CountryDetails? fromCountry,
    required CountryDetails? toCountry,
    String? errorText,
    required ValueChanged<String> onAmountChanged,
    required List<Country> availableCountries,
    required ValueChanged<Country> onCountryChanged,
    required AsyncValue<SendMoneyCalServiceCrgResponse?> calculationAsync,
  }) {
    final theme = Theme.of(context);
    return Card(
      elevation: 1,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(context.tr('You Send'), style: theme.textTheme.bodyMedium),
            const SizedBox(height: 8),
            Row(
              children: [
                Expanded(
                  child: TextFormField(
                    controller: amountController,
                    onChanged: onAmountChanged,
                    keyboardType: const TextInputType.numberWithOptions(decimal: true),
                    style: theme.textTheme.headlineMedium?.copyWith(fontWeight: FontWeight.bold),
                    decoration: InputDecoration(
                      border: InputBorder.none,
                      errorText: errorText,
                      hintText: '0.00',
                    ),
                  ),
                ),
                const SizedBox(width: 12),
                if (fromCountry != null && availableCountries.isNotEmpty)
                  DropdownButtonHideUnderline(
                    child: DropdownButton<Country>(
                      value: availableCountries.firstWhere((c) => c.id == fromCountry.id, orElse: () => availableCountries.first),
                      items: availableCountries.map((Country country) {
                        return DropdownMenuItem<Country>(
                          value: country,
                          child: Row(
                            children: [
                              Image.network(country.flag, width: 24, height: 16, fit: BoxFit.cover, errorBuilder: (c, e, s) => Icon(Icons.flag_circle_outlined, size: 16, color: theme.disabledColor)),
                              const SizedBox(width: 8),
                              Text(country.code, style: const TextStyle(fontWeight: FontWeight.bold)),
                            ],
                          ),
                        );
                      }).toList(),
                      onChanged: (Country? newValue) {
                        if (newValue != null) onCountryChanged(newValue);
                      },
                    ),
                  )
                else
                  const SizedBox(width: 24, height: 24, child: Preloader()),
              ],
            ),
            const Divider(height: 24),
              Text(context.tr('Recipient Gets'), style: theme.textTheme.bodyMedium),
            const SizedBox(height: 8),
            Row(
              children: [
                Expanded(
                  child: TextFormField(
                    enabled: false,
                    controller: TextEditingController(
                      text: calculationAsync.when(
                        data: (calc) => double.tryParse(calc?.data?.receivedAmount ?? '0.0')?.toStringAsFixed(2) ?? '0.00',
                        loading: () => '0.00',
                        error: (e, st) => '0.00',
                      ),
                    ),
                    style: theme.textTheme.headlineMedium?.copyWith(fontWeight: FontWeight.bold, color: theme.primaryColor),
                    decoration: const InputDecoration(
                      border: InputBorder.none,
                      hintText: '0.00',
                    ),
                  ),
                ),
                const SizedBox(width: 12),
                if (toCountry != null)
                  Row(
                    children: [
                      Image.network(toCountry.flag, width: 24, height: 16, fit: BoxFit.cover, errorBuilder: (c, e, s) => Icon(Icons.flag_circle_outlined, size: 16, color: theme.colorScheme.secondary)),
                      const SizedBox(width: 8),
                      Text(toCountry.code, style: const TextStyle(fontWeight: FontWeight.bold)),
                      const SizedBox(width: 45),
                    ],
                  )
                else
                  const SizedBox(width: 24, height: 24, child: Preloader()),
              ],
            ),
            calculationAsync.when(
              data: (calc) {
                if (calc?.data == null) return const SizedBox.shrink();
                final data = calc!.data!;
                return Padding(
                  padding: const EdgeInsets.only(top: 16.0),
                  child: Container(
                    padding: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      color: theme.primaryColor.withAlpha((255 * 0.05).toInt()),
                      borderRadius: BorderRadius.circular(16),
                      border: Border.all(color: theme.primaryColor.withAlpha((255 * 0.1).toInt())),
                    ),
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Row(
                              children: [
                                Container(
                                  padding: const EdgeInsets.all(8),
                                  decoration: BoxDecoration(
                                    color: theme.colorScheme.secondary.withAlpha((255 * 0.15).toInt()),
                                    shape: BoxShape.circle,
                                    border: Border.all(
                                      color: theme.primaryColor.withAlpha((255 * 0.3).toInt()),
                                      width: 1.5,
                                    ),
                                    boxShadow: [BoxShadow(color: theme.shadowColor.withAlpha((255 * 0.1).toInt()), blurRadius: 4)],
                                  ),
                                  child: Icon(Icons.currency_exchange, size: 18, color: theme.colorScheme.secondary),
                                ),
                                const SizedBox(width: 12),
                                Text(
                                  context.tr('Exchange Rate'),
                                  style: theme.textTheme.bodyMedium?.copyWith(
                                    color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
                                    fontWeight: FontWeight.w500,
                                  ),
                                ),
                              ],
                            ),
                            Text(
                              '1 ${data.senderCurrencyCode} = ${double.tryParse(data.exchangeRate)?.toStringAsFixed(4)} ${data.receiverCurrencyCode}',
                              style: theme.textTheme.bodyMedium?.copyWith(
                                fontWeight: FontWeight.bold,
                                overflow: TextOverflow.ellipsis,
                              ),
                              maxLines: 1,
                              softWrap: false,
                            ),
                          ],
                        ),

                        const SizedBox(height: 16),

                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Row(
                              children: [
                                Container(
                                  padding: const EdgeInsets.all(8),
                                  decoration: BoxDecoration(
                                    color: theme.colorScheme.secondary.withAlpha((255 * 0.15).toInt()),
                                    shape: BoxShape.circle,
                                    border: Border.all(
                                      color: theme.colorScheme.secondary.withAlpha((255 * 0.3).toInt()),
                                      width: 1.5,
                                    ),
                                    boxShadow: [BoxShadow(color: theme.shadowColor.withAlpha((255 * 0.1).toInt()), blurRadius: 4)],
                                  ),
                                  child: Icon(Icons.receipt_long, size: 18, color: theme.colorScheme.secondary),
                                ),
                                const SizedBox(width: 12),
                                Text(
                                  context.tr('Transfer Fee'),
                                  style: theme.textTheme.bodyMedium?.copyWith(
                                    color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
                                    fontWeight: FontWeight.w500,
                                  ),
                                ),
                              ],
                            ),
                            Text(
                              '${double.tryParse(data.fee)?.toStringAsFixed(2)} ${data.senderCurrencyCode}',
                              style: theme.textTheme.bodyMedium?.copyWith(
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ],
                        ),

                        Padding(
                          padding: const EdgeInsets.symmetric(vertical: 16),
                          child: Divider(height: 1, color: theme.dividerColor),
                        ),

                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(context.tr('Total Amount'),
                              style: theme.textTheme.titleMedium?.copyWith(
                                  fontWeight: FontWeight.bold,
                              ),
                            ),
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                              decoration: BoxDecoration(
                                color: theme.primaryColor,
                                borderRadius: BorderRadius.circular(20),
                                boxShadow: [
                                  BoxShadow(
                                    color: theme.primaryColor.withAlpha((255 * 0.3).toInt()),
                                    blurRadius: 8,
                                    offset: const Offset(0, 4),
                                  )
                                ],
                              ),
                              child: Text(
                                '${((double.tryParse(amountController.text) ?? 0.0) + (double.tryParse(data.fee) ?? 0.0)).toStringAsFixed(2)} ${data.senderCurrencyCode}',
                                style: theme.textTheme.titleMedium?.copyWith(
                                  fontWeight: FontWeight.bold,
                                  color: theme.colorScheme.secondary,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                );
              },
              loading: () => Padding(
                padding: const EdgeInsets.only(top: 16.0),
                child: Center(child: Text(context.tr('Calculating...'))),
              ),
              error: (e, st) => const SizedBox.shrink(),
            ),
          ],
        ),
      ),
    );
  }

  void _clearAmountAndRelatedInputs() {
    _amountController.clear();
    setState(() {});
  }

}

class BeneficiaryListItem extends StatelessWidget {
  final BeneficiaryModel beneficiary;
  final bool isSelected;
  final VoidCallback onTap;
  final VoidCallback onProceed;

  const BeneficiaryListItem({
    super.key,
    required this.beneficiary,
    required this.isSelected,
    required this.onTap,
    required this.onProceed,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final primaryColor = theme.colorScheme.primary;
    final successColor = theme.colorScheme.secondary;

    String subtitleText;
    // Bank (Transaction Type 1)
    if (beneficiary.transactionType == '1' || beneficiary.bnkInfo != null) {
      subtitleText = '${beneficiary.bnkInfo?.bankName ?? context.tr('Bank')} • ${beneficiary.accountNumber ?? ''}';
    }
    // Wallet (Transaction Type 6)
    else if (beneficiary.transactionType == '6') {
      final walletProviderName = beneficiary.countryService?.name;
      subtitleText = '${walletProviderName ?? context.tr('Wallet Top Up')} • ${beneficiary.walletNumber ?? ''}';
    }
    else {
      subtitleText = beneficiary.transactionTypeName ?? beneficiary.country?.name ?? context.tr('Receiving Method');
    }

    return AnimatedContainer(
      duration: const Duration(milliseconds: 300),
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      decoration: BoxDecoration(
        color: isSelected ? successColor.withAlpha(20) : theme.cardColor,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(
          color: isSelected ? successColor : Colors.transparent,
          width: isSelected ? 2 : 0,
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.red.withAlpha((0.1 * 255).toInt()),
            blurRadius: 8,
            offset: const Offset(-4, -4),
          ),
          BoxShadow(
            color: Colors.green.withAlpha((0.1 * 255).toInt()),
            blurRadius: 8,
            offset: const Offset(4, -4),
          ),
          BoxShadow(
            color: Colors.amber.withAlpha((0.1 * 255).toInt()),
            blurRadius: 8,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Material(
        color: Colors.transparent,
        borderRadius: BorderRadius.circular(16),
        child: InkWell(
          onTap: onTap,
          borderRadius: BorderRadius.circular(16),
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Row(
              children: [
                Stack(
                  children: [
                    Container(
                      padding: const EdgeInsets.all(2),
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        border: Border.all(
                          color: isSelected ? successColor : Colors.transparent,
                          width: 2,
                        ),
                      ),
                      child: CircleAvatar(
                        radius: 24,
                        backgroundColor: isSelected
                            ? successColor.withAlpha((255 * 0.3).toInt())
                            : primaryColor.withAlpha((255 * 0.25).toInt()),
                        child: Text(
                          (beneficiary.firstName.isNotEmpty)
                              ? beneficiary.firstName[0].toUpperCase() + (beneficiary.lastName.isNotEmpty ? beneficiary.lastName[0].toUpperCase() : '')
                              : '?',
                          style: TextStyle(
                            color: isSelected ? successColor : primaryColor,
                            fontWeight: FontWeight.bold,
                            fontSize: 18,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(width: 16),

                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '${beneficiary.firstName} ${beneficiary.lastName}',
                        style: theme.textTheme.titleMedium?.copyWith(
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        subtitleText,
                        style: theme.textTheme.bodySmall?.copyWith(
                          color: theme.colorScheme.onSurface.withAlpha((255 * 0.7).toInt()),
                          fontWeight: FontWeight.w500,
                          fontSize: 12
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ],
                  ),
                ),

                if (isSelected)
                  ElevatedButton(
                    onPressed: onProceed,
                    style: ElevatedButton.styleFrom(
                      elevation: 4,
                      backgroundColor: successColor,
                      foregroundColor: theme.colorScheme.onPrimary,
                      shape: const CircleBorder(),
                      padding: const EdgeInsets.all(12),
                    ),
                    child: const Icon(Icons.arrow_forward_ios_rounded, size: 16),
                  )
                else
                  Icon(Icons.chevron_right_rounded, color: theme.colorScheme.onSurface.withAlpha((255 * 0.8).toInt()), size: 28),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class OtpDialog extends ConsumerStatefulWidget {
  final String email;
  final String transactionToken;
  final int expiresIn;
  const OtpDialog({
    super.key,
    required this.email,
    required this.transactionToken,
    required this.expiresIn,
  });

  @override
  ConsumerState<OtpDialog> createState() => _OtpDialogState();
}

class _OtpDialogState extends ConsumerState<OtpDialog> {
  final TextEditingController otpController = TextEditingController();
  Timer? _timer;
  late int _countdown;

  @override
  void initState() {
    super.initState();
    _countdown = widget.expiresIn;
    startTimer();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(sendMoneyViewModelProvider.notifier).getBeneficiaries();
    });
  }

  void startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_countdown > 0) {
        setState(() => _countdown--);
      } else {
        timer.cancel();
      }
    });
  }

  @override
  void dispose() {
    _timer?.cancel();
    otpController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final resendState = ref.watch(sendMoneyViewModelProvider).resendOtpResponse;
    final canResendAfter = ref.watch(sendMoneyViewModelProvider).otpGenerateResponse.asData?.value?.data?.canResendAfter ?? 60;
    final theme = Theme.of(context);

    return AlertDialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      title: Text(context.tr('Enter OTP'), textAlign: TextAlign.center),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          RichText(
            textAlign: TextAlign.center,
            text: TextSpan(
              style: theme.textTheme.bodyMedium,
              children: [
                TextSpan(text: context.tr('An OTP has been sent to your email:\n')),
                TextSpan(text: widget.email, style: theme.textTheme.bodyMedium?.copyWith(fontWeight: FontWeight.bold)),
              ],
            ),
          ),
          const SizedBox(height: 20),
          TextField(
            controller: otpController,
            maxLength: 6,
            textAlign: TextAlign.center,
            keyboardType: TextInputType.number,
            style: theme.textTheme.titleLarge,
            decoration: InputDecoration(
              counterText: '',
              hintText: '_ _ _ _ _ _',
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
              filled: true,
              fillColor: theme.cardColor,
              contentPadding: const EdgeInsets.symmetric(vertical: 16),
            ),
          ),
          const SizedBox(height: 16),
          if (_countdown > 0)
            Text(context.tr('Resend OTP in {seconds} seconds').replaceAll('{seconds}', _countdown.toString()), style: theme.textTheme.bodySmall?.copyWith(color: Colors.grey))
          else
            resendState.isLoading
                ? const Preloader()
                : TextButton.icon(
              icon: const Icon(Icons.refresh, size: 18),
              label: Text(context.tr('Resend OTP')),
              style: TextButton.styleFrom(
                foregroundColor: theme.colorScheme.primary,
              ),
              onPressed: () async {
                final response = await ref.read(sendMoneyViewModelProvider.notifier).resendOtp(transactionToken: widget.transactionToken);
                if (!mounted) return;
                if (response != null && response.success) {
                  setState(() => _countdown = response.data?.expiresIn ?? canResendAfter);
                  startTimer();
                } else {
                  final error = ref.read(sendMoneyViewModelProvider).resendOtpResponse.error;
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text(error != null ? context.tr(ErrorHandler.getErrorMessage(error)) : context.tr('Failed to resend OTP.'))),
                  );
                }
              },
            ),
        ],
      ),
      actionsAlignment: MainAxisAlignment.center,
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: Text(context.tr('Cancel')),
        ),
        ElevatedButton.icon(
          icon: const Icon(Icons.check_circle_outline),
          label: Text(context.tr('Confirm')),
          style: ElevatedButton.styleFrom(
            padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 12),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          ),
          onPressed: () async {
            final purposeId = ref.read(selectedPurposeIdProvider);
            final remarks = ref.read(remarksProvider);

            if (otpController.text.length < 6) {
              ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(context.tr('Please enter the 6-digit OTP.'))));
              return;
            }
            if (purposeId == null) {
              ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(context.tr('Please select a purpose of transfer.'))));
              return;
            }

            final response = await ref.read(sendMoneyViewModelProvider.notifier).verifyOtp(
              transactionToken: widget.transactionToken,
              otp: otpController.text,
              purposeOfTransfer: purposeId,
              remarks: remarks,
            );

            if (!mounted) return;

            if (response != null && response.success == true) {
              Navigator.of(context).pop();
              context.go(AppRouter.home);
            } else {
              final error = ref.read(sendMoneyViewModelProvider).verifyOtpResponse.error;
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text(error != null ? context.tr(ErrorHandler.getErrorMessage(error)) : context.tr('Invalid OTP or failed to verify.'))),
              );
            }
          },
        ),
      ],
    );
  }
}
