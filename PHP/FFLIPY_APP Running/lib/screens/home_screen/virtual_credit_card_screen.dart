import 'package:fflipy/core/theme/app_theme.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:flutter/material.dart';
import 'package:fflipy/core/localization/app_localizations.dart';

class VirtualCreditCardScreen extends StatefulWidget {
  final String cardHolderName;
  final String balance;
  final String currency;

  const VirtualCreditCardScreen({
    super.key,
    required this.cardHolderName,
    required this.balance,
    required this.currency,
  });

  @override
  State<VirtualCreditCardScreen> createState() =>
      _VirtualCreditCardScreenState();
}

class _VirtualCreditCardScreenState extends State<VirtualCreditCardScreen> {
  bool _isLoading = false;

  void _loadCardDetails() async {
    setState(() => _isLoading = true);
    await Future.delayed(const Duration(seconds: 2));
    if (mounted) {
      setState(() => _isLoading = false);
    }
  }

  @override
  void initState() {
    super.initState();
    _loadCardDetails();
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context);
    final theme = Theme.of(context);
    final colorScheme = theme.colorScheme;

    return Stack(
      children: [
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: Center(
            child: Card(
              elevation: 12,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(24),
              ),
              clipBehavior: Clip.antiAlias,
              child: Container(
                width: double.infinity,
                height: 230,
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    colors: [
                      colorScheme.virtualCardPrimary.withAlpha((255 * 0.95).toInt()),
                      colorScheme.virtualCardSecondary.withAlpha((255 * 0.85).toInt()),
                      colorScheme.virtualCardPrimary.withAlpha((255 * 0.7).toInt()),
                    ],
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: colorScheme.virtualCardShadow,
                      blurRadius: 24,
                      offset: const Offset(0, 8),
                    ),
                  ],
                ),
                child: Stack(
                  children: [
                    // Glass effect overlay
                    Positioned.fill(
                      child: Container(
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(24),
                          gradient: LinearGradient(
                            colors: [
                              colorScheme.surfaceVariant.withAlpha((255 * 0.08).toInt()),
                              Colors.transparent
                            ],
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                          ),
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(24.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                'Fflipy',
                                style: TextStyle(
                                  color: colorScheme.onPrimary,
                                  fontSize: 22,
                                  fontWeight: FontWeight.bold,
                                  letterSpacing: 1.2,
                                ),
                              ),
                              Text(
                                '${widget.currency} ${widget.balance}',
                                style: TextStyle(
                                  color: colorScheme.onPrimary,
                                  fontSize: 22,
                                  fontWeight: FontWeight.bold,
                                  letterSpacing: 1.1,
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 18),
                          // Card chip (professional metallic look)
                          Container(
                            width: 48,
                            height: 32,
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8),
                              gradient: LinearGradient(
                                colors: [
                                  colorScheme.virtualCardChipGold,
                                  colorScheme.virtualCardChipLightGold,
                                  colorScheme.virtualCardChipDeeperGold,
                                ],
                                begin: Alignment.topLeft,
                                end: Alignment.bottomRight,
                              ),
                              boxShadow: [
                                BoxShadow(
                                  color: colorScheme.virtualCardShadow,
                                  blurRadius: 6,
                                  offset: const Offset(0, 2),
                                ),
                              ],
                              border: Border.all(
                                color: colorScheme.virtualCardBorder,
                                width: 1.2,
                              ),
                            ),
                            child: Stack(
                              children: [
                                Align(
                                  alignment: Alignment.center,
                                  child: Container(
                                    width: 24,
                                    height: 16,
                                    decoration: BoxDecoration(
                                      gradient: LinearGradient(
                                        colors: [
                                          colorScheme.virtualCardChipHighlight,
                                          colorScheme.virtualCardChipInternalGold,
                                        ],
                                        begin: Alignment.topLeft,
                                        end: Alignment.bottomRight,
                                      ),
                                      borderRadius: BorderRadius.circular(4),
                                    ),
                                  ),
                                ),
                                // Subtle highlight
                                Positioned(
                                  top: 4,
                                  left: 8,
                                  right: 8,
                                  child: Container(
                                    height: 4,
                                    decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(2),
                                      gradient: LinearGradient(
                                        colors: [
                                          colorScheme.virtualCardChipHighlight,
                                          Colors.transparent
                                        ],
                                        begin: Alignment.topCenter,
                                        end: Alignment.bottomCenter,
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const Spacer(),
                          // Card number
                          Text(
                            '**** **** **** 1234',
                            style: TextStyle(
                              color: colorScheme.onPrimary,
                              fontSize: 26,
                              letterSpacing: 3,
                              fontFamily: 'RobotoMono',
                              fontWeight: FontWeight.w600,
                              shadows: [
                                Shadow(
                                  color: colorScheme.virtualCardShadow,
                                  blurRadius: 2,
                                  offset: const Offset(1, 1),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 12),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    loc.translate('CARD HOLDER'),
                                    style: TextStyle(
                                      color: colorScheme.onPrimary.withAlpha((255 * 0.7).toInt()),
                                      fontSize: 12,
                                      fontWeight: FontWeight.w500,
                                      letterSpacing: 1.1,
                                    ),
                                  ),
                                  Text(
                                    widget.cardHolderName,
                                    style: TextStyle(
                                      color: colorScheme.onPrimary,
                                      fontSize: 16,
                                      fontWeight: FontWeight.bold,
                                      letterSpacing: 1.1,
                                    ),
                                  ),
                                ],
                              ),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    loc.translate('EXPIRES'),
                                    style: TextStyle(
                                      color: colorScheme.onPrimary.withAlpha((255 * 0.7).toInt()),
                                      fontSize: 12,
                                      fontWeight: FontWeight.w500,
                                      letterSpacing: 1.1,
                                    ),
                                  ),
                                  Text(
                                    '12/28',
                                    style: TextStyle(
                                      color: colorScheme.onPrimary,
                                      fontSize: 16,
                                      fontWeight: FontWeight.bold,
                                      letterSpacing: 1.1,
                                    ),
                                  ),
                                ],
                              ),
                              // Card network logo (Visa)
                              Container(
                                margin: const EdgeInsets.only(left: 8),
                                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                                decoration: BoxDecoration(
                                  color: colorScheme.virtualCardLogoBg,
                                  borderRadius: BorderRadius.circular(12),
                                  boxShadow: [
                                    BoxShadow(
                                      color: colorScheme.virtualCardLogoShadow,
                                      blurRadius: 6,
                                      offset: const Offset(0, 2),
                                    ),
                                  ],
                                  border: Border.all(
                                    color: colorScheme.virtualCardLogoBorder,
                                    width: 1,
                                  ),
                                ),
                                child: Image.network(
                                  'https://upload.wikimedia.org/wikipedia/commons/4/41/Visa_Logo.png',
                                  width: 44,
                                  height: 18,
                                  fit: BoxFit.contain,
                                  errorBuilder: (context, error, stackTrace) => Center(
                                    child: Icon(
                                      Icons.credit_card,
                                      color: colorScheme.virtualCardErrorIcon,
                                      size: 24,
                                    ),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
        if (_isLoading)
          const Preloader(),
      ],
    );
  }
}
