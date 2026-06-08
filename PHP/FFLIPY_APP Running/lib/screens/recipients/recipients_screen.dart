import 'package:fflipy/core/widgets/preloader.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../core/widgets/brand_app_bar.dart';

class RecipientScreen extends StatefulWidget {
  const RecipientScreen({super.key});

  @override
  State<RecipientScreen> createState() => _RecipientScreenState();
}

class _RecipientScreenState extends State<RecipientScreen> {
  String _selectedWallet = 'bkash';
  late final TextEditingController _accountNumberController;
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _accountNumberController = TextEditingController();
  }

  @override
  void dispose() {
    _accountNumberController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: BrandAppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            GoRouter.of(context).go('/');
          },
          tooltip: 'Back',
        ),
        title: const Text(
          'Step 3 of 5: Details',
          style: TextStyle(fontSize: 16),
        ),
      ),
      body: Stack(
        children: [
          SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Form(
                key: _formKey,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Recipient Details & \nPayment Setup',
                      style: theme.textTheme.headlineMedium?.copyWith(fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Step 3 of: Details',
                      style: theme.textTheme.titleMedium,
                    ),
                    const SizedBox(height: 24),
                    _buildMobileWalletDetails(theme),
                    const SizedBox(height: 24),
                    _buildMobileAccountNumber(theme),
                    const SizedBox(height: 24),
                    _buildPaymentMethod(theme),
                    const SizedBox(height: 40),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: _isLoading ? null : _onReviewPressed,
                        style: ElevatedButton.styleFrom(
                          padding: const EdgeInsets.symmetric(vertical: 16),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8),
                          ),
                        ),
                        child: const Text(
                          'Review Transfer',
                          style: TextStyle(fontSize: 18),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
          if (_isLoading)
            const Preloader(),
        ],
      ),
    );
  }

  Widget _buildMobileWalletDetails(ThemeData theme) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Mobile Wallet Details',
          style: theme.textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 8),
        Card(
          elevation: 1,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
            child: DropdownButtonFormField<String>(
              value: _selectedWallet,
              decoration: const InputDecoration(border: InputBorder.none),
              isExpanded: true,
              items: const [
                DropdownMenuItem(value: 'bkash', child: Text('bKash')),
                DropdownMenuItem(value: 'nagad', child: Text('Nagad')),
                DropdownMenuItem(value: 'rocket', child: Text('Rocket')),
                DropdownMenuItem(value: 'mcash', child: Text('mCash')),
                DropdownMenuItem(value: 'taptap', child: Text('TapTap')),
              ],
              onChanged: (value) {
                if (value == null) return;
                setState(() => _selectedWallet = value);
              },
              hint: const Text('Select Mobile Wallet'),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Please select a mobile wallet';
                }
                return null;
              },
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildMobileAccountNumber(ThemeData theme) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Mobile Account Number',
          style: theme.textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 8),
        TextFormField(
          controller: _accountNumberController,
          decoration: InputDecoration(
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: BorderSide.none,
            ),
            filled: true,
            hintText: '01XXX XXX XXX',
          ),
          keyboardType: TextInputType.phone,
          textInputAction: TextInputAction.done,
          validator: (value) {
            if (value == null || value.trim().isEmpty) {
              return 'Please enter account number';
            }
            final digits = value.replaceAll(RegExp(r'\D'), '');
            if (digits.length < 10) {
              return 'Enter a valid phone number';
            }
            return null;
          },
        ),
      ],
    );
  }

  Widget _buildPaymentMethod(ThemeData theme) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          "Sender's Payment Method",
          style: theme.textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 8),
        Card(
          elevation: 1,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Pay From', style: TextStyle(color: theme.hintColor)),
                const SizedBox(height: 8),
                Row(
                  children: [
                    Icon(Icons.payment, color: theme.colorScheme.primary),
                    const SizedBox(width: 8),
                    const Expanded(
                      child: Text('VISA Ending in 4567'),
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                Text('Change/Add New Card', style: TextStyle(color: theme.colorScheme.primary)),
              ],
            ),
          ),
        ),
      ],
    );
  }

  Future<void> _onReviewPressed() async {
    if (_formKey.currentState?.validate() ?? false) {
      setState(() => _isLoading = true);
      await Future.delayed(const Duration(seconds: 2));
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Reviewing transfer from $_selectedWallet to ${_accountNumberController.text}'),
          ),
        );
        setState(() => _isLoading = false);
      }
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please fix the errors above')),
      );
    }
  }
}
