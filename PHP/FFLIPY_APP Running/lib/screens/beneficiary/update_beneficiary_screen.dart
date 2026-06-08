import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/utils/validators.dart';
import 'package:fflipy/models/beneficiary/update_beneficiary_request.dart';
import 'package:fflipy/models/beneficiary/update_beneficiary_response.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:fflipy/models/beneficiary/beneficiary_model.dart';
import 'package:go_router/go_router.dart';
import '../../core/utils/dialog_helper.dart';
import '../../core/widgets/brand_app_bar.dart';
import '../../providers/beneficiary_providers.dart';
import '../../providers/auth_providers.dart';
import '../../core/theme/primary_button.dart';
import '../../core/utils/custom_text_field.dart';

class UpdateBeneficiaryScreen extends ConsumerStatefulWidget {
  final BeneficiaryModel beneficiary;
  final int transactionId;
  const UpdateBeneficiaryScreen({super.key, required this.beneficiary, required this.transactionId});

  static Widget fromGoRouterState(BuildContext context, GoRouterState state) {
    if (state.extra is Map<String, dynamic>) {
      final extra = state.extra as Map<String, dynamic>;
      final beneficiary = extra['beneficiary'] as BeneficiaryModel;
      final transactionId = extra['transactionId'] as int? ?? 0;
      return UpdateBeneficiaryScreen(beneficiary: beneficiary, transactionId: transactionId);
    } else if (state.extra is BeneficiaryModel) {
      final beneficiary = state.extra as BeneficiaryModel;
      return UpdateBeneficiaryScreen(beneficiary: beneficiary, transactionId: 0);
    }
    
    return Scaffold(
      appBar: BrandAppBar(title: Text(context.tr("Error"))),
      body: Center(
        child: Text(context.tr('Could not open update screen due to invalid parameters.')),
      ),
    );
  }

  @override
  ConsumerState<UpdateBeneficiaryScreen> createState() => _UpdateBeneficiaryScreenState();
}

class _UpdateBeneficiaryScreenState extends ConsumerState<UpdateBeneficiaryScreen> {
  final _formKey = GlobalKey<FormState>();

  final _firstNameController = TextEditingController();
  final _lastNameController = TextEditingController();
  final _addressController = TextEditingController();
  final _phoneController = TextEditingController();
  final _accountNumberController = TextEditingController();
  final _walletNumberController = TextEditingController();
  late final localizations = AppLocalizations.of(context);

  @override
  void initState() {
    super.initState();
    _firstNameController.text = widget.beneficiary.firstName;
    _lastNameController.text = widget.beneficiary.lastName;
    _addressController.text = widget.beneficiary.address ?? '';
    _phoneController.text = widget.beneficiary.phoneNumber ?? '';
    _accountNumberController.text = widget.beneficiary.accountNumber ?? '';
    _walletNumberController.text = widget.beneficiary.walletNumber ?? '';
  }

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _addressController.dispose();
    _phoneController.dispose();
    _accountNumberController.dispose();
    _walletNumberController.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    if (!(_formKey.currentState?.validate() ?? false)) {
      return;
    }

    dynamic transactionType;
    if (widget.transactionId != 0) {
      transactionType = widget.transactionId;
    } else {
      transactionType = int.tryParse(widget.beneficiary.transactionType);
    }

    final isWallet = int.tryParse(widget.beneficiary.transactionType) == 6;

    final request = UpdateBeneficiaryRequest(
      id: widget.beneficiary.id,
      firstName: _firstNameController.text,
      lastName: _lastNameController.text,
      phoneNumber: _phoneController.text,
      address: _addressController.text,
      countryId: widget.beneficiary.countryId.toString(),
      relationshipToSender: widget.beneficiary.relationshipToSender.toString(),
      transactionType: transactionType,
      bankId: widget.beneficiary.bnkInfoId,
      branchId: widget.beneficiary.bnkBrInfoId,
      accountNumber: !isWallet ? _accountNumberController.text : '',
      walletProvider: widget.beneficiary.walletProvider,
      walletNumber: isWallet ? _walletNumberController.text : '',
      accountType: widget.beneficiary.accountType,
      email: '',
      city: '',
    );

    DialogHelper.showLoadingDialog(context);

    try {
      final UpdateBeneficiaryResponse response = await ref.read(updateBeneficiaryProvider(request).future);
      if (!mounted) return;
      Navigator.of(context, rootNavigator: true).pop();

      DialogHelper.showSnackBar(context, context.tr(response.message), isError: !response.status);

      if (response.status) {
        Navigator.of(context).pop(true);
      }
    } catch (e) {
      if (!mounted) return;
      Navigator.of(context, rootNavigator: true).pop();
      DialogHelper.showSnackBar(context, context.tr(ErrorHandler.getErrorMessage(e)), isError: true);
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('Update Beneficiary')),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 16.0),
          child: Card(
            elevation: 0,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(20.0),
            ),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 32.0),
              child: Form(
                key: _formKey,
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text(
                      context.tr('Edit Beneficiary Information'),
                      textAlign: TextAlign.center,
                      style: theme.textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 32),
                    _buildTextField(
                      _firstNameController,
                      context.tr('First Name'),
                      prefixIcon: const Icon(Icons.person_outline),
                      validator: (value) => Validators.validateName(value, localizations),
                      textCapitalization: TextCapitalization.words,
                      autofillHints: const [AutofillHints.givenName],
                    ),
                    const SizedBox(height: 16),
                    _buildTextField(
                      _lastNameController,
                      context.tr('Last Name'),
                      prefixIcon: const Icon(Icons.person_outline),
                      validator: (value) => Validators.validateName(value, localizations),
                      textCapitalization: TextCapitalization.words,
                      autofillHints: const [AutofillHints.familyName],
                    ),
                    const SizedBox(height: 16),
                    _buildTextField(
                      _addressController,
                      context.tr('Address'),
                      prefixIcon: const Icon(Icons.location_on_outlined),
                      textCapitalization: TextCapitalization.sentences,
                      autocorrect: true,
                      autofillHints: const [AutofillHints.fullStreetAddress],
                    ),
                    const SizedBox(height: 16),
                    _buildTextField(
                      _phoneController,
                      context.tr('Phone Number'),
                      prefixIcon: const Icon(Icons.phone_outlined),
                      keyboardType: TextInputType.phone,
                      validator: (value) => Validators.validatePhoneNumber(value, localizations),
                      autofillHints: const [AutofillHints.telephoneNumber],
                      inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    ),
                    const SizedBox(height: 16),
                    if (int.tryParse(widget.beneficiary.transactionType) == 6)
                      _buildTextField(
                        _walletNumberController,
                        context.tr('Wallet Number'),
                        prefixIcon: const Icon(Icons.account_balance_wallet_outlined),
                        keyboardType: TextInputType.number,
                      )
                    else
                      _buildTextField(
                        _accountNumberController,
                        context.tr('Account Number'),
                        prefixIcon: const Icon(Icons.account_balance_wallet_outlined),
                        keyboardType: TextInputType.number,
                      ),
                    const SizedBox(height: 32),
                    ElevatedButton(
                      onPressed: _submit,
                      child: Text(context.tr('Update Beneficiary')),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildTextField(
    TextEditingController controller,
    String label, {
    TextInputType keyboardType = TextInputType.text,
    String? Function(String?)? validator,
    Widget? prefixIcon,
    Iterable<String>? autofillHints,
    TextCapitalization textCapitalization = TextCapitalization.none,
    bool autocorrect = false,
    List<TextInputFormatter>? inputFormatters,
  }) {
    return CustomTextField(
      controller: controller,
      labelText: label,
      prefixIcon: prefixIcon,
      keyboardType: keyboardType,
      validator: validator,
      inputFormatters: inputFormatters,
      textCapitalization: textCapitalization,
      autocorrect: autocorrect,
      autofillHints: autofillHints,
    );
  }
}
