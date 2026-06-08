import 'package:country_picker/country_picker.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/utils/custom_text_field.dart';
import 'package:fflipy/core/utils/validators.dart';
import 'package:fflipy/models/beneficiary/account_type_model.dart';
import 'package:fflipy/models/beneficiary/add_beneficiary_request.dart';
import 'package:fflipy/models/beneficiary/bank_model.dart';
import 'package:fflipy/models/beneficiary/branch_model.dart';
import 'package:fflipy/models/beneficiary/facility_model.dart' as beneficiary_facility;
import '../../core/theme/primary_button.dart';
import '../../core/utils/dialog_helper.dart';
import '../../core/widgets/brand_app_bar.dart';
import '../../core/widgets/preloader.dart';
import '../../models/beneficiary/add_beneficiary_response.dart';
import '../../models/beneficiary/wallet_provider_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../models/beneficiary/beneficiary_list_response.dart';
import '../../models/beneficiary/beneficiary_model.dart' as app_country;
import '../../providers/beneficiary_providers.dart';

import '../../core/theme/app_theme.dart';
import 'dart:ui';


class AddBeneficiaryScreen extends ConsumerStatefulWidget {
  const AddBeneficiaryScreen({super.key});

  @override
  ConsumerState<AddBeneficiaryScreen> createState() => _AddBeneficiaryScreenState();
}

class _AddBeneficiaryScreenState extends ConsumerState<AddBeneficiaryScreen> {
  final _formKey = GlobalKey<FormState>();

  final _firstNameController = TextEditingController();
  final _lastNameController = TextEditingController();
  final _emailController = TextEditingController();
  final _phoneController = TextEditingController();
  final _addressController = TextEditingController();
  final _cityController = TextEditingController();
  final _accountNumberController = TextEditingController();
  final _walletNumberController = TextEditingController();

  app_country.Country? _selectedCountry;
  Relationship? _selectedRelationship;
  beneficiary_facility.Facility? _selectedTransactionType;
  Bank? _selectedBank;
  Branch? _selectedBranch;
  WalletProvider? _selectedWalletProvider;
  AccountType? _selectedAccountType;
  Country? _pickedCountry;

  final TextEditingController _searchController = TextEditingController();

  // State to prevent multiple submissions
  bool _isSubmitting = false;

  bool get _isWalletTopUp => _selectedTransactionType?.id.toString() == '6';
  bool get _isAccountDeposit => _selectedTransactionType?.id.toString() == '1';

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(beneficiaryViewModelProvider.notifier).loadBeneficiaries();
    });
  }

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _emailController.dispose();
    _phoneController.dispose();
    _addressController.dispose();
    _cityController.dispose();
    _accountNumberController.dispose();
    _walletNumberController.dispose();
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    if (_isSubmitting) return;

    setState(() {
      _isSubmitting = true;
    });

    if (!(_formKey.currentState?.validate() ?? false)) {
      DialogHelper.showSnackBar(context, context.tr('Please fix the errors above.'), isError: true);
      setState(() {
        _isSubmitting = false;
      });
      return;
    }

    final phoneCode = _pickedCountry?.phoneCode ?? '';
    final fullPhoneNumber = '+$phoneCode${_phoneController.text}';

    final request = AddBeneficiaryRequest(
        firstName: _firstNameController.text,
        lastName: _lastNameController.text,
        email: _emailController.text,
        phoneNumber: fullPhoneNumber,
        address: _addressController.text,
        countryId: _selectedCountry!.id.toString(),
        city: _cityController.text,
        relationshipToSender: _selectedRelationship!.id.toString(),
        transactionType: _selectedTransactionType!.id.toString(),
        bankId: _isWalletTopUp ? null : _selectedBank?.id.toString(),
        branchId: _isWalletTopUp ? null : _selectedBranch?.id.toString(),
        accountNumber: _isWalletTopUp ? null : _accountNumberController.text,
        walletProvider: _isWalletTopUp ? _selectedWalletProvider?.id.toString() : null,
        walletNumber: _isWalletTopUp ? _walletNumberController.text : null,
        accountType: _isAccountDeposit ? _selectedAccountType?.id : null);

    try {
      final AddBeneficiaryResponse response = await ref.read(addBeneficiaryProvider(request).future);
      if (!mounted) return;
      DialogHelper.showSnackBar(context, context.tr(response.message), isError: false);
      Navigator.of(context).pop(); // Go back to list
    } catch (e) {
      if (!mounted) return;
      DialogHelper.showSnackBar(context, context.tr(ErrorHandler.getErrorMessage(e)), isError: true);
      setState(() {
        _isSubmitting = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final beneficiaryInfo = ref.watch(beneficiaryViewModelProvider);

    return beneficiaryInfo.when(
      loading: () => const Scaffold(body: Preloader()),
      error: (err, stack) => Scaffold(
        appBar: BrandAppBar(title: Text(context.tr('Add Beneficiary'))),
        body: Center(child: Text(context.tr(ErrorHandler.getErrorMessage(err)))),
      ),
      data: (data) {
        final localizations = AppLocalizations.of(context);

        return Scaffold(
          appBar: BrandAppBar(title: Text(context.tr('Add Beneficiary'))),
          body: Form(
            key: _formKey,
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  _buildSectionCard(
                    context.tr('Personal Information'),
                    [
                      _buildTextField(
                        _firstNameController,
                        context.tr('First Name'),
                        prefixIcon: const Icon(Icons.person),
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
                        _emailController,
                        context.tr('Email'),
                        prefixIcon: const Icon(Icons.email),
                        keyboardType: TextInputType.emailAddress,
                        validator: (value) => Validators.validateEmail(value, localizations),
                        autofillHints: const [AutofillHints.email],
                      ),
                      const SizedBox(height: 16),
                      _buildTextField(
                        _addressController,
                        context.tr('Address'),
                        prefixIcon: const Icon(Icons.location_on),
                        textCapitalization: TextCapitalization.sentences,
                        autocorrect: true,
                        autofillHints: const [AutofillHints.fullStreetAddress],
                      ),
                      const SizedBox(height: 16),
                      _buildTextField(
                        _cityController,
                        context.tr('City'),
                        prefixIcon: const Icon(Icons.location_city),
                        textCapitalization: TextCapitalization.words,
                        autocorrect: true,
                        autofillHints: const [AutofillHints.addressCity],
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  _buildSectionCard(
                    context.tr('Contact & Relationship'),
                    [
                      _buildDropdown(
                        label: context.tr('Country'),
                        items: data.countries,
                        selectedValue: _selectedCountry,
                        onChanged: (country) {
                          setState(() {
                            _selectedCountry = country;
                            if (country != null) {
                              try {
                                _pickedCountry = CountryParser.parseCountryCode(country.isoCode);
                              } catch (e) {
                                _pickedCountry = null;
                              }
                            } else {
                              _pickedCountry = null;
                            }
                            _selectedTransactionType = null;
                            _selectedBank = null;
                            _selectedBranch = null;
                            _selectedWalletProvider = null;
                            _selectedAccountType = null;
                            _phoneController.clear();
                          });
                        },
                        validator: (value) => value == null ? context.tr('Please select a country') : null,
                        itemMap: (country) => {
                          'id': country.id.toString(),
                          'name': country.name,
                          'flag': country.flag,
                        },
                      ),
                      const SizedBox(height: 16),
                      CustomTextField(
                        controller: _phoneController,
                        labelText: context.tr('Phone Number'),
                        keyboardType: TextInputType.phone,
                        inputFormatters: [
                          FilteringTextInputFormatter.digitsOnly,
                          if (_selectedCountry?.isoCode == 'BD')
                            FilteringTextInputFormatter.deny(RegExp(r'^0')),
                          LengthLimitingTextInputFormatter(_selectedCountry?.isoCode == 'BD' ? 10 : 15),
                        ],
                        prefixIcon: InkWell(
                          onTap: () {
                            showCountryPicker(
                              context: context,
                              showPhoneCode: true,
                              countryFilter: data.countries.map((c) => c.isoCode).toList(),
                              onSelect: (Country country) {
                                try {
                                  final selectedAppCountry = data.countries.firstWhere(
                                          (c) => c.isoCode == country.countryCode);
                                  setState(() {
                                    _selectedCountry = selectedAppCountry;
                                    _pickedCountry = country;

                                    _selectedTransactionType = null;
                                    _selectedBank = null;
                                    _selectedBranch = null;
                                    _selectedWalletProvider = null;
                                    _selectedAccountType = null;
                                    _phoneController.clear();
                                    _accountNumberController.clear();
                                    _walletNumberController.clear();
                                  });
                                } catch (_) {}
                              },
                            );
                          },
                          child: Padding(
                            padding: const EdgeInsets.only(left: 12.0, right: 8.0),
                            child: Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Text(_pickedCountry?.flagEmoji ?? '🏳️', style: const TextStyle(fontSize: 24)),
                                const SizedBox(width: 8),
                                Text('+${_pickedCountry?.phoneCode ?? ""}', style: Theme.of(context).textTheme.titleMedium),
                                const SizedBox(width: 4),
                                const Icon(Icons.arrow_drop_down, size: 16),
                              ],
                            ),
                          ),
                        ),
                        validator: (value) => Validators.validatePhoneNumber(value, localizations),
                        autofillHints: const [AutofillHints.telephoneNumber],
                      ),
                      const SizedBox(height: 16),
                      _buildDropdown(
                        label: context.tr('Relationship'),
                        items: data.relationships,
                        selectedValue: _selectedRelationship,
                        onChanged: (relationship) => setState(() => _selectedRelationship = relationship),
                        validator: (value) => value == null ? context.tr('Please select a relationship') : null,
                        itemMap: (relationship) => {
                          'id': relationship.id.toString(),
                          'name': relationship.title,
                        },
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  _buildSectionCard(
                    context.tr('Transaction Details'),
                    [
                      if (_selectedCountry != null)
                        _buildAsyncDropdown<beneficiary_facility.Facility>(
                          label: context.tr('Transaction Type'),
                          provider: facilitiesProvider(_selectedCountry!.id.toString()),
                          selectedValue: _selectedTransactionType,
                          onChanged: (facility) {
                            setState(() {
                              _selectedTransactionType = facility;
                              _selectedBank = null;
                              _selectedBranch = null;
                              _selectedWalletProvider = null;
                              _selectedAccountType = null;
                              _accountNumberController.clear();
                              _walletNumberController.clear();
                            });
                          },
                          validator: (value) => value == null ? context.tr('Please select a transaction type') : null,
                          itemMap: (facility) => {
                            'id': facility.id.toString(),
                            'name': facility.name,
                          },
                        ),
                      if (_selectedTransactionType != null) ...[
                        const SizedBox(height: 16),
                        if (_isWalletTopUp)
                          ..._buildWalletFields()
                        else
                          ..._buildBankFields(),
                      ],
                    ],
                  ),
                  const SizedBox(height: 24),
                  PrimaryButton(
                    onPressed: _isSubmitting ? null : _submit,
                    text: context.tr('Add Beneficiary'),
                    isLoading: _isSubmitting,
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }

  List<Widget> _buildWalletFields() {
    return [
      _buildAsyncDropdown<WalletProvider>(
        label: context.tr('Wallet Provider'),
        provider: walletProvidersProvider(_selectedCountry!.id.toString()),
        selectedValue: _selectedWalletProvider,
        onChanged: (provider) => setState(() => _selectedWalletProvider = provider),
        validator: (value) => value == null ? context.tr('Please select a wallet provider') : null,
        itemMap: (provider) => {
          'id': provider.id.toString(),
          'name': provider.name,
        },
      ),
      const SizedBox(height: 16),
      _buildTextField(
        _walletNumberController,
        context.tr('Wallet Number'),
        prefixIcon: const Icon(Icons.account_balance_wallet),
        keyboardType: TextInputType.number,
        validator: (value) {
          if (value == null || value.isEmpty) {
            return context.tr('Wallet Number is required');
          }
          if (_selectedWalletProvider?.name.toLowerCase() == 'bkash') {
            if (value.length != 11) {
              return context.tr('bKash number must be 11 digits');
            }
          } else if (_selectedWalletProvider?.name.toLowerCase() == 'rocket') {
            if (value.length != 12) {
              return context.tr('Rocket number must be 12 digits');
            }
          }

          final beneficiaries = ref.read(beneficiaryViewModelProvider).value?.beneficiaries.data ?? [];
          final isDuplicate = beneficiaries.any((b) =>
              b.walletProvider == _selectedWalletProvider?.id.toString() &&
              b.walletNumber == value);

          if (isDuplicate) {
            return context.tr('This wallet beneficiary already exists.');
          }

          return null;
        },
        inputFormatters: [
          FilteringTextInputFormatter.digitsOnly,
          LengthLimitingTextInputFormatter(
            _selectedWalletProvider?.name.toLowerCase() == 'bkash' ? 11 : 12,
          ),
        ],
      ),
    ];
  }

  List<Widget> _buildBankFields() {
    return [
      if (_isAccountDeposit) ...[
        _buildAsyncDropdown<AccountType>(
          label: context.tr('Account Type'),
          provider: accountTypesProvider,
          selectedValue: _selectedAccountType,
          onChanged: (type) => setState(() => _selectedAccountType = type),
          validator: (value) => value == null ? context.tr('Please select an account type') : null,
          itemMap: (type) => {
            'id': type.id.toString(),
            'name': type.name,
          },
        ),
        const SizedBox(height: 16),
      ],
      _buildAsyncDropdown<Bank>(
        label: context.tr('Bank'),
        provider: banksProvider(_selectedCountry!.id.toString()),
        selectedValue: _selectedBank,
        onChanged: (bank) {
          setState(() {
            _selectedBank = bank;
            _selectedBranch = null;
          });
        },
        validator: (value) => value == null ? context.tr('Please select a bank') : null,
        itemMap: (bank) => {
          'id': bank.id.toString(),
          'name': bank.bankName,
        },
      ),
      const SizedBox(height: 16),
      if (_selectedBank != null) ...[
        _buildAsyncDropdown<Branch>(
          label: context.tr('Branch'),
          provider: branchesProvider(_selectedBank!.id.toString()),
          selectedValue: _selectedBranch,
          onChanged: (branch) => setState(() => _selectedBranch = branch),
          validator: (value) => value == null ? context.tr('Please select a branch') : null,
          itemMap: (branch) => {
            'id': branch.id.toString(),
            'name': branch.branchName,
          },
        ),
        const SizedBox(height: 16),
      ],
      _buildTextField(
        _accountNumberController,
        context.tr('Account Number'),
        prefixIcon: const Icon(Icons.account_balance),
        keyboardType: TextInputType.number,
        inputFormatters: [FilteringTextInputFormatter.digitsOnly],
        validator: (value) {
            if (value == null || value.isEmpty) {
              return 'Account Number is required';
            }

            final beneficiaries = ref.read(beneficiaryViewModelProvider).value?.beneficiaries.data ?? [];
            final isDuplicate = beneficiaries.any((b) =>
              b.bnkInfoId == _selectedBank?.id.toString() &&
              b.bnkBrInfoId == _selectedBranch?.id.toString() &&
              b.accountNumber == value);

            if (isDuplicate) {
              return context.tr('This bank account beneficiary already exists.');
            }

            return null;
        },
      ),
    ];
  }

  Widget _buildSectionCard(String title, List<Widget> children) {
    final theme = Theme.of(context);
    return Card(
      elevation: 0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12.0)),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(title, style: theme.textTheme.titleLarge),
            const SizedBox(height: 20),
            ...children,
          ],
        ),
      ),
    );
  }

  Widget _buildTextField(
      TextEditingController controller,
      String label, {
        Widget? prefixIcon,
        TextInputType keyboardType = TextInputType.text,
        String? Function(String?)? validator,
        List<TextInputFormatter>? inputFormatters,
        TextCapitalization textCapitalization = TextCapitalization.none,
        bool autocorrect = false,
        Iterable<String>? autofillHints,
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

  Widget _buildDropdown<T>({required String label, required List<T> items, T? selectedValue, required void Function(T?) onChanged, required Map<String, String> Function(T) itemMap, String? Function(T?)? validator,}) {
    final mappedItems = items.map((item) {
        final map = itemMap(item);
        if (!map.containsKey('id')) map['id'] = '';
        if (!map.containsKey('name')) map['name'] = '';
        return map;
    }).toList();

    final selectedId = selectedValue != null ? itemMap(selectedValue)['id'] : null;

    return CustomPopupDropdown(
      value: selectedId,
      items: mappedItems,
      label: label,
      onChanged: (newId) {
        if (newId == null) {
          onChanged(null);
        } else {
          try {
            final selectedItem = items.firstWhere((item) => itemMap(item)['id'] == newId);
            onChanged(selectedItem);
          } catch (_) {
            onChanged(null);
          }
        }
      },
      validator: (val) {
        // We need to resolve T from ID to pass to validator, OR just pass selectedValue from scope (which is stale?)
        // Better: Find T again.
        T? item;
        if (val != null) {
           try {
             item = items.firstWhere((t) => itemMap(t)['id'] == val);
           } catch (_) {}
        }
        return validator?.call(item);
      },
    );
  }

  Widget _buildAsyncDropdown<T>({required String label, required ProviderBase<AsyncValue<List<T>>> provider, T? selectedValue, required void Function(T?) onChanged, required Map<String, String> Function(T) itemMap, String? Function(T?)? validator}) {
    final theme = Theme.of(context);
    return Consumer(
      builder: (context, ref, child) {
        final asyncValue = ref.watch(provider);
        return asyncValue.when(
          loading: () => InputDecorator(
            decoration: InputDecoration(
              labelText: label,
              suffixIcon: const SizedBox(
                width: 24,
                height: 24,
                child: Preloader(),
              ),
            ),
            child: const Text(''),
          ),
          error: (err, stack) => InputDecorator(
            decoration: InputDecoration(
              labelText: label,
              suffixIcon: Icon(Icons.error, color: theme.colorScheme.error),
            ),
            child: Text(context.tr('Error'), style: TextStyle(color: theme.colorScheme.error)),
          ),
          data: (items) => _buildDropdown<T>(
            label: label,
            items: items,
            selectedValue: selectedValue,
            onChanged: onChanged,
            itemMap: itemMap,
            validator: validator,
          ),
        );
      },
    );
  }
}

class CustomPopupDropdown extends StatelessWidget {
  final String? value;
  final List<Map<String, String>> items;
  final String label;
  final bool showAsterisk;
  final void Function(String?) onChanged;
  final String? Function(String?)? validator;

  const CustomPopupDropdown({
    Key? key,
    required this.value,
    required this.items,
    required this.label,
    required this.onChanged,
    this.validator,
    this.showAsterisk = false,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final colorScheme = theme.colorScheme;
    final selectedName = items.firstWhere((item) => item['id'] == value, orElse: () => {'name': ''})['name'] ?? '';
    
    return FormField<String>(
      initialValue: value,
      validator: validator,
      builder: (FormFieldState<String> state) {
        return Padding(
          padding: const EdgeInsets.symmetric(vertical: 6.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Text(label, style: theme.textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold, fontSize: 14, color: colorScheme.onSurface)),
                  if (showAsterisk)
                    Text(' *', style: TextStyle(color: colorScheme.error, fontSize: 13)),
                ],
              ),
              const SizedBox(height: 4),
              GestureDetector(
                onTap: () async {
                  final selected = await showGeneralDialog<String>(
                    context: context,
                    barrierDismissible: true,
                    barrierLabel: '',
                    barrierColor: colorScheme.onSurface.withAlpha((0.18 * 255).toInt()),
                    transitionDuration: const Duration(milliseconds: 220),
                    pageBuilder: (context, anim1, anim2) {
                      return const SizedBox.shrink();
                    },
                    transitionBuilder: (context, anim1, anim2, child) {
                      return _DropdownSearchDialog(
                        label: label,
                        items: items,
                        value: value,
                      );
                    },
                  );
                  if (selected != null) {
                    onChanged(selected);
                    state.didChange(selected);
                  }
                },
                child: AnimatedContainer(
                  duration: const Duration(milliseconds: 180),
                  curve: Curves.easeInOut,
                  padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
                  decoration: BoxDecoration(
                    color: colorScheme.surface,
                    borderRadius: BorderRadius.circular(12),
                    boxShadow: [
                      BoxShadow(
                        color: colorScheme.onSurface.withAlpha((0.08 * 255).toInt()),
                        blurRadius: 10,
                        offset: const Offset(0, 2),
                      ),
                    ],
                    border: Border.all(
                      color: state.hasError ? colorScheme.error : ((value?.isEmpty ?? true) ? theme.dividerColor : colorScheme.primary),
                      width: 1.2,
                    ),
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Expanded(
                        child: AnimatedSwitcher(
                          duration: const Duration(milliseconds: 200),
                          child: Text(
                            selectedName.isNotEmpty ? context.tr(selectedName) : context.tr('Select...'),
                            key: ValueKey(selectedName),
                            style: TextStyle(
                              color: (value?.isEmpty ?? true) ? colorScheme.onSurfaceVariant : colorScheme.onSurface,
                              fontWeight: FontWeight.w600,
                              fontSize: 13,
                              letterSpacing: 0.1,
                            ),
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                      ),
                      Icon(Icons.expand_more, color: colorScheme.primary, size: 20),
                    ],
                  ),
                ),
              ),
              if (state.hasError)
                Padding(
                  padding: const EdgeInsets.only(top: 2.0, left: 2.0),
                  child: Text(state.errorText!, style: TextStyle(color: colorScheme.error, fontSize: 11)),
                ),
            ],
          ),
        );
      }
    );
  }
}

class _DropdownSearchDialog extends StatefulWidget {
  final String label;
  final List<Map<String, String>> items;
  final String? value;
  const _DropdownSearchDialog({required this.label, required this.items, required this.value});
  @override
  State<_DropdownSearchDialog> createState() => _DropdownSearchDialogState();
}

class _DropdownSearchDialogState extends State<_DropdownSearchDialog> {
  String searchText = '';
  final FocusNode _searchFocus = FocusNode();
  @override
  void dispose() {
    _searchFocus.dispose();
    super.dispose();
  }
  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final colorScheme = theme.colorScheme;
    List<Map<String, String>> filteredItems = widget.items;
    if (searchText.isNotEmpty) {
      filteredItems = widget.items.where((item) => (context.tr(item['name'] ?? '')).toLowerCase().contains(searchText.toLowerCase())).toList();
    }
    return Opacity(
      opacity: 1.0,
      child: Transform.scale(
        scale: 1.0,
        child: Center(
          child: Material(
            color: Colors.transparent,
            child: ClipRRect(
              borderRadius: BorderRadius.circular(20),
              child: BackdropFilter(
                filter: ImageFilter.blur(sigmaX: 18, sigmaY: 18),
                child: Container(
                  width: MediaQuery.of(context).size.width * 0.90,
                  constraints: const BoxConstraints(maxHeight: 340),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(20),
                    color: colorScheme.surface,
                    boxShadow: [
                      BoxShadow(
                        color: colorScheme.onSurface.withAlpha((0.10 * 255).toInt()),
                        blurRadius: 24,
                        offset: const Offset(0, 8),
                      ),
                    ],
                    border: Border.all(color: theme.dividerColor, width: 1.0),
                  ),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        decoration: const BoxDecoration(
                          gradient: LinearGradient(
                            colors: [AppTheme.topBarGradientLeft, AppTheme.topBarGradientRight],
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                          ),
                          borderRadius: BorderRadius.only(
                            topLeft: Radius.circular(20),
                            topRight: Radius.circular(20),
                          ),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
                          child: Row(
                            children: [
                              Expanded(
                                child: Text(
                                  widget.label,
                                  style: theme.textTheme.titleMedium?.copyWith(
                                    fontWeight: FontWeight.bold,
                                    color: colorScheme.onSurface,
                                    fontSize: 14,
                                  ),
                                ),
                              ),
                              InkWell(
                                borderRadius: BorderRadius.circular(16),
                                onTap: () => Navigator.pop(context),
                                child: Padding(
                                  padding: const EdgeInsets.all(2.0),
                                  child: Icon(Icons.close_rounded, color: colorScheme.onSurfaceVariant, size: 22),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        child: TextField(
                          focusNode: _searchFocus,
                          autofocus: false,
                          onTap: () => _searchFocus.requestFocus(),
                          onChanged: (val) => setState(() => searchText = val),
                          decoration: InputDecoration(
                            hintText: context.tr('Search...'),
                            prefixIcon: Icon(Icons.search, size: 18, color: colorScheme.primary),
                            isDense: true,
                            contentPadding: const EdgeInsets.symmetric(vertical: 6, horizontal: 8),
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(10),
                              borderSide: BorderSide(color: theme.dividerColor),
                            ),
                          ),
                          style: theme.textTheme.bodySmall?.copyWith(fontSize: 12, color: colorScheme.onSurface),
                        ),
                      ),
                      Divider(height: 1, thickness: 1, color: theme.dividerColor),
                      Flexible(
                        child: filteredItems.isEmpty
                            ? Padding(
                                padding: const EdgeInsets.all(16.0),
                                child: Text(context.tr('No data found'), style: theme.textTheme.bodyMedium?.copyWith(fontSize: 12, color: colorScheme.onSurfaceVariant)),
                              )
                            : ListView.separated(
                                padding: EdgeInsets.zero,
                                shrinkWrap: true,
                                itemCount: filteredItems.length,
                                separatorBuilder: (_, __) => Divider(height: 1, thickness: 0.5, color: theme.dividerColor),
                                itemBuilder: (context, idx) {
                                  final item = filteredItems[idx];
                                  final isSelected = item['id'] == widget.value;
                                  return InkWell(
                                    borderRadius: BorderRadius.circular(10),
                                    splashColor: colorScheme.primary.withAlpha((0.08 * 255).toInt()),
                                    highlightColor: colorScheme.primary.withAlpha((0.04 * 255).toInt()),
                                    onTap: () => Navigator.pop(context, item['id']),
                                    child: AnimatedContainer(
                                      duration: const Duration(milliseconds: 120),
                                      curve: Curves.easeInOut,
                                      decoration: BoxDecoration(
                                        color: isSelected ? colorScheme.primary.withAlpha((0.18 * 255).toInt()) : Colors.transparent,
                                        borderRadius: BorderRadius.circular(10),
                                      ),
                                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                                      child: Row(
                                        children: [
                                          if (item['flag'] != null && item['flag']!.isNotEmpty)
                                            Padding(
                                              padding: const EdgeInsets.only(right: 6.0),
                                              child: Image.network(
                                                item['flag']!,
                                                width: 18,
                                                height: 14,
                                                fit: BoxFit.cover,
                                                errorBuilder: (context, error, stackTrace) => SizedBox(width: 18, height: 14, child: Icon(Icons.flag, color: colorScheme.onSurfaceVariant, size: 12)),
                                              ),
                                            ),
                                          Expanded(
                                            child: Text(
                                              context.tr(item['name'] ?? ''),
                                              style: theme.textTheme.bodySmall?.copyWith(
                                                fontSize: 12,
                                                fontWeight: isSelected ? FontWeight.bold : FontWeight.w500,
                                                color: isSelected ? colorScheme.onSurface : colorScheme.onSurfaceVariant,
                                                letterSpacing: 0.1,
                                              ),
                                            ),
                                          ),
                                          if (isSelected)
                                            Icon(Icons.check_circle_rounded, color: colorScheme.primary, size: 16),
                                        ],
                                      ),
                                    ),
                                  );
                                },
                              ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

