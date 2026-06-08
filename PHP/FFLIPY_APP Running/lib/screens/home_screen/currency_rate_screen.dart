import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import '../../core/localization/app_localizations.dart';
import '../../models/currency_rate.dart';
import '../../services/currency_service.dart';

class CurrentRateScreen extends StatefulWidget {
  const CurrentRateScreen({Key? key}) : super(key: key);

  @override
  State<CurrentRateScreen> createState() => _CurrentRateScreenState();
}

class _CurrentRateScreenState extends State<CurrentRateScreen> {
  List<CurrencyRate> _senderRates = [];
  List<CurrencyRate> _receiverRates = [];
  String? _fromCurrencyCode;
  String? _toCurrencyCode;
  double _sendAmount = 1.0;
  late TextEditingController _sendAmountController;

  @override
  void initState() {
    super.initState();
    _sendAmountController = TextEditingController(); // No default value
    _fetchRates();
  }

  @override
  void dispose() {
    _sendAmountController.dispose();
    super.dispose();
  }

  Future<void> _fetchRates() async {
    setState(() { });
    try {
      final data = await CurrencyService().fetchCurrencyRates();
      setState(() {
        _senderRates = data['sender'] ?? [];
        _receiverRates = data['receiver'] ?? [];
        // Set default sender and receiver currencies by id: sender id 5, receiver id 2
        CurrencyRate? senderDefault = _senderRates.firstWhereOrNull(
          (c) => c.id == 5,
        );
        CurrencyRate? receiverDefault = _receiverRates.firstWhereOrNull(
          (c) => c.id == 2,
        );
        _fromCurrencyCode = (senderDefault ?? _senderRates.firstOrNull)?.code;
        _toCurrencyCode = (receiverDefault ?? _receiverRates.firstOrNull)?.code;
      });
    } catch (e) {
      setState(() { });
    }
  }

  CurrencyRate? get _fromRate {
    return _senderRates.firstWhereOrNull((r) => r.code == _fromCurrencyCode);
  }
  CurrencyRate? get _toRate {
    return _receiverRates.firstWhereOrNull((r) => r.code == _toCurrencyCode);
  }

  @override
  Widget build(BuildContext context) {
    final fromCurrency = _fromRate?.code ?? '';
    final toCurrency = _toRate?.code ?? '';
    final rate = (_fromRate != null && _toRate != null && _fromRate!.rate != 0)
        ? _toRate!.rate / _fromRate!.rate
        : 0.0;
    final receiveAmount = (_sendAmountController.text.isEmpty)
        ? 0.0
        : _sendAmount * rate;

    return LayoutBuilder(
      builder: (context, constraints) {
        final theme = Theme.of(context);
        final isDark = theme.brightness == Brightness.dark;
        
        double maxWidth = constraints.maxWidth;
        double cardWidth = maxWidth * 0.94; // Dynamic width based on screen size
        double horizontalPadding = cardWidth * 0.05;
        double verticalPadding = cardWidth * 0.04;
        double fontSizeTitle = 14.0; 
        double fontSizeValue = 18.0; 
        double fontSizeFx = 12.0; 
        double flagWidth = 24.0; 
        double iconSize = 20.0; 

        return Center(
          child: Container(
            width: cardWidth,
            margin: const EdgeInsets.symmetric(vertical: 8),
            decoration: BoxDecoration(
              color: isDark ? theme.cardColor : Colors.white,
              borderRadius: BorderRadius.circular(24),
              border: Border.all(
                color: isDark 
                    ? Colors.white.withOpacity(0.2) 
                    : theme.dividerColor.withOpacity(0.5)
              ),
              boxShadow: [
                BoxShadow(
                  color: isDark 
                      ? Colors.black.withOpacity(0.4) 
                      : theme.primaryColor.withOpacity(0.08),
                  blurRadius: 24,
                  offset: const Offset(0, 8),
                ),
              ],
            ),
            child: Padding(
              padding: EdgeInsets.all(horizontalPadding),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        context.tr('Currency Calculator'),
                        style: TextStyle(
                          fontSize: fontSizeValue * 0.85,
                          fontWeight: FontWeight.w800,
                          color: isDark ? Colors.white : theme.primaryColor,
                          letterSpacing: 0.3,
                        ),
                      ),
                      Icon(
                        Icons.calculate_rounded, 
                        color: isDark ? Colors.white70 : theme.primaryColor.withOpacity(0.5)
                      ),
                    ],
                  ),
                  SizedBox(height: verticalPadding),
                  
                  Stack(
                    alignment: Alignment.center,
                    children: [
                      Column(
                        children: [
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                            decoration: BoxDecoration(
                              color: isDark 
                                  ? theme.colorScheme.surface.withOpacity(0.5) 
                                  : const Color(0xFFF8FAFC), 
                              borderRadius: BorderRadius.circular(16),
                              border: Border.all(
                                color: isDark 
                                    ? Colors.white.withOpacity(0.15) 
                                    : const Color(0xFFE2E8F0)
                              ),
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(context.tr('Send Amount'), 
                                  style: TextStyle(
                                    fontSize: fontSizeTitle, 
                                    color: isDark ? Colors.white60 : const Color(0xFF64748B), 
                                    fontWeight: FontWeight.w600
                                  )
                                ),
                                const SizedBox(height: 8),
                                Row(
                                  children: [
                                    Expanded(
                                      child: TextField(
                                        controller: _sendAmountController,
                                        keyboardType: const TextInputType.numberWithOptions(decimal: true),
                                        style: TextStyle(
                                          fontWeight: FontWeight.w800, 
                                          fontSize: fontSizeValue, 
                                          color: theme.textTheme.bodyLarge?.color
                                        ),
                                        decoration: InputDecoration(
                                          border: InputBorder.none,
                                          isDense: true,
                                          contentPadding: EdgeInsets.zero,
                                          hintText: '1000',
                                          hintStyle: TextStyle(color: theme.hintColor.withOpacity(0.3)),
                                        ),
                                        onTap: () {
                                          if (_sendAmountController.text == '0' || _sendAmountController.text == '0.0' || _sendAmountController.text == '0.00') {
                                            _sendAmountController.clear();
                                          }
                                        },
                                        onChanged: (val) {
                                          final v = double.tryParse(val);
                                          setState(() {
                                            _sendAmount = v ?? 0.0;
                                          });
                                        },
                                      ),
                                    ),
                                    _buildCurrencySelector(
                                      context: context,
                                      value: _fromCurrencyCode,
                                      rates: _senderRates,
                                      onChanged: (val) => setState(() => _fromCurrencyCode = val),
                                      flagWidth: flagWidth,
                                      fontSize: fontSizeValue * 0.75,
                                      iconSize: iconSize,
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 12),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                            decoration: BoxDecoration(
                              color: isDark 
                                  ? theme.colorScheme.surface.withOpacity(0.5) 
                                  : const Color(0xFFF8FAFC),
                              borderRadius: BorderRadius.circular(16),
                              border: Border.all(
                                color: isDark 
                                    ? Colors.white.withOpacity(0.15) 
                                    : const Color(0xFFE2E8F0)
                              ),
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(context.tr('Recipient gets'), style: TextStyle(
                                  fontSize: fontSizeTitle, 
                                  color: isDark ? Colors.white60 : const Color(0xFF64748B), 
                                  fontWeight: FontWeight.w600
                                )),
                                const SizedBox(height: 8),
                                Row(
                                  children: [
                                    Expanded(
                                      child: Text(
                                        receiveAmount.toStringAsFixed(2),
                                        style: TextStyle(
                                          fontWeight: FontWeight.w800, 
                                          fontSize: fontSizeValue, 
                                          color: theme.textTheme.bodyLarge?.color
                                        ),
                                      ),
                                    ),
                                    _buildCurrencySelector(
                                      context: context,
                                      value: _toCurrencyCode,
                                      rates: _receiverRates,
                                      onChanged: (val) => setState(() => _toCurrencyCode = val),
                                      flagWidth: flagWidth,
                                      fontSize: fontSizeValue * 0.75,
                                      iconSize: iconSize,
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                      GestureDetector(
                        onTap: () {
                          setState(() {
                            final tempCode = _fromCurrencyCode;
                            _fromCurrencyCode = _toCurrencyCode;
                            _toCurrencyCode = tempCode;
                          });
                        },
                        child: AnimatedRotation(
                          turns: 1,
                          duration: const Duration(milliseconds: 350),
                          child: Container(
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                              color: theme.primaryColor,
                              shape: BoxShape.circle,
                              border: Border.all(
                                color: isDark ? const Color(0xFF1E293B) : Colors.white, 
                                width: 4
                              ),
                              boxShadow: [
                                BoxShadow(
                                  color: theme.primaryColor.withOpacity(0.3),
                                  blurRadius: 8,
                                  offset: const Offset(0, 4),
                                )
                              ],
                            ),
                            child: Icon(Icons.swap_vert_rounded, size: iconSize * 0.9, color: Colors.white),
                          ),
                        ),
                      ),
                    ],
                  ),
                  SizedBox(height: verticalPadding * 1.2),
                  
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
                    decoration: BoxDecoration(
                      color: isDark 
                          ? theme.primaryColor.withOpacity(0.15) 
                          : theme.primaryColor.withOpacity(0.1),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.trending_up_rounded, 
                          size: fontSizeFx * 1.4, 
                          color: theme.primaryColor
                        ),
                        const SizedBox(width: 8),
                        Flexible(
                          child: Text(
                            "1.00 $fromCurrency = ${rate.toStringAsFixed(4)} $toCurrency",
                            style: TextStyle(
                              fontSize: fontSizeFx,
                              fontWeight: FontWeight.w700,
                              color: theme.primaryColor,
                            ),
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }

  Widget _buildCurrencySelector({
    required BuildContext context,
    required String? value,
    required List<CurrencyRate> rates,
    required ValueChanged<String?> onChanged,
    required double flagWidth,
    required double fontSize,
    required double iconSize,
  }) {
    final theme = Theme.of(context);
    final isDark = theme.brightness == Brightness.dark;
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: isDark ? const Color(0xFF334155) : Colors.white,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: isDark ? Colors.white.withOpacity(0.15) : const Color(0xFFE2E8F0)
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.02),
            blurRadius: 4,
            offset: const Offset(0, 2),
          )
        ],
      ),
      child: DropdownButtonHideUnderline(
        child: DropdownButton<String>(
          value: value,
          isDense: true,
          icon: Icon(Icons.keyboard_arrow_down_rounded, size: iconSize, color: theme.hintColor),
          borderRadius: BorderRadius.circular(16),
          dropdownColor: theme.cardColor,
          items: rates.map((rate) {
            return DropdownMenuItem<String>(
              value: rate.code,
              child: Row(
                children: [
                  rate.flag.isNotEmpty
                      ? ClipRRect(
                          borderRadius: BorderRadius.circular(4),
                          child: Image.network(
                            rate.flag, 
                            width: 24, 
                            height: 16, 
                            fit: BoxFit.cover, 
                            errorBuilder: (c, e, s) => const SizedBox(width: 24, height: 16)
                          ),
                        )
                      : const SizedBox(width: 24, height: 16),
                  const SizedBox(width: 8),
                  Text(rate.code, style: TextStyle(
                    fontWeight: FontWeight.w700, 
                    fontSize: 14.0, 
                    color: theme.textTheme.bodyLarge?.color
                  )),
                ],
              ),
            );
          }).toList(),
          onChanged: onChanged,
        ),
      ),
    );
  }
}

