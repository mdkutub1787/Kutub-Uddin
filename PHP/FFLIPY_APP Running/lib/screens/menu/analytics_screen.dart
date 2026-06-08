import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/theme/app_theme.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/providers/auth_providers.dart';
import 'package:fflipy/providers/profile_providers.dart';
import 'package:fflipy/providers/transaction_providers.dart';
import 'package:fflipy/models/transaction_model/transaction_report_model.dart';
import 'package:fflipy/core/widgets/empty_state_widget.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:intl/intl.dart';
import '../../core/widgets/brand_app_bar.dart';

class AnalyticsScreen extends ConsumerStatefulWidget {
  const AnalyticsScreen({Key? key}) : super(key: key);

  @override
  ConsumerState<AnalyticsScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends ConsumerState<AnalyticsScreen> {
  String _selectedFilter = '';
  List<String> _timeFilters = [];
  bool _filtersInitialized = false;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(transactionViewModelProvider.notifier).fetchAllTransactionPages();
    });
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (!_filtersInitialized) {
      _timeFilters = [
        context.tr('All Time'),
        context.tr('Last 7 Days'),
        context.tr('Last 30 Days'),
        ..._generateLast12Months()
      ];
      _selectedFilter = _timeFilters.first;
      _filtersInitialized = true;
    }
  }

  List<String> _generateLast12Months() {
    final now = DateTime.now();
    final format = DateFormat('MMMM yyyy');
    return List.generate(12, (index) {
      final date = DateTime(now.year, now.month - index, 1);
      return format.format(date);
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final transactionState = ref.watch(transactionViewModelProvider);
    ref.watch(authViewModelProvider);
    final profileState = ref.watch(profileViewModelProvider);
    final countriesState = ref.watch(activeCountriesProvider);

    String currencyCode = '€';

    if (profileState.hasValue &&
        countriesState.hasValue &&
        profileState.value != null &&
        countriesState.value != null) {
      final userProfile = profileState.value!.userProfile;
      final countries = countriesState.value!;
      final userCountryId = userProfile.countryId;
      if (userCountryId != null) {
        try {
          final country = countries
              .firstWhere((c) => c.id.toString() == userCountryId.toString());
          currencyCode = country.code;
        } catch (e) {
          // In case country is not found, fallback to default.
        }
      }
    }

    final allTransactions = transactionState.transactionReport?.data.transactions.data ?? [];
    List<TransactionModel> filteredTransactions;

    // Client-side filtering logic
    final now = DateTime.now();
    DateTime? startDate;
    DateTime? endDate;

    if (_selectedFilter == context.tr('Last 7 Days')) {
      startDate = now.subtract(const Duration(days: 6));
      endDate = now;
    } else if (_selectedFilter == context.tr('Last 30 Days')) {
      startDate = now.subtract(const Duration(days: 29));
      endDate = now;
    } else if (_selectedFilter != context.tr('All Time')) {
      try {
        final monthFormat = DateFormat('MMMM yyyy');
        final selectedDate = monthFormat.parse(_selectedFilter);
        startDate = DateTime(selectedDate.year, selectedDate.month, 1);
        endDate = DateTime(selectedDate.year, selectedDate.month + 1, 0);
      } catch (e) {
        startDate = null;
        endDate = null;
      }
    }

    if (startDate != null && endDate != null) {
      final normalizedEndDate = DateTime(endDate.year, endDate.month, endDate.day, 23, 59, 59);
      filteredTransactions = allTransactions.where((t) {
        if (t.txPaidDate == null) return false;
        try {
          final txDate = DateTime.parse(t.txPaidDate!);
          final normalizedStartDate = DateTime(startDate!.year, startDate.month, startDate.day);
          return txDate.isAfter(normalizedStartDate.subtract(const Duration(microseconds: 1))) && txDate.isBefore(normalizedEndDate.add(const Duration(microseconds: 1)));
        } catch (e) {
          return false;
        }
      }).toList();
    } else {
      filteredTransactions = allTransactions;
    }

    final pendingTransactions = filteredTransactions.where((t) => t.status == '1').toList();
    final cancelledTransactions = filteredTransactions.where((t) => t.status == '2').toList();
    final paidTransactions = filteredTransactions.where((t) => t.status == '3').toList();

    final pendingAmount = pendingTransactions.fold<double>(0.0, (sum, item) => sum + item.totalPay);
    final cancelledAmount = cancelledTransactions.fold<double>(0.0, (sum, item) => sum + item.totalPay);
    final paidAmount = paidTransactions.fold<double>(0.0, (sum, item) => sum + item.totalPay);

    final providerAnalytics = <String, Map<String, dynamic>>{};
    for (var transaction in filteredTransactions) {
      final providerName = transaction.recipientBank;
      if (providerAnalytics.containsKey(providerName)) {
        providerAnalytics[providerName]!['count'] += 1;
        providerAnalytics[providerName]!['amount'] += transaction.totalPay;
      } else {
        providerAnalytics[providerName] = {
          'count': 1,
          'amount': transaction.totalPay.toDouble(),
        };
      }
    }

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('Analytics')),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Align(
                alignment: Alignment.centerRight,
                child: DropdownButton<String>(
                  value: _selectedFilter,
                  items: _timeFilters.map<DropdownMenuItem<String>>((String value) {
                    return DropdownMenuItem<String>(
                      value: value,
                      child: Text(value),
                    );
                  }).toList(),
                  onChanged: (String? newValue) {
                    if (newValue != null) {
                      setState(() {
                        _selectedFilter = newValue;
                      });
                    }
                  },
                ),
              ),
              const SizedBox(height: 20),
              if (transactionState.isLoading)
                const Center(child: Preloader())
              else if (transactionState.error != null && allTransactions.isEmpty)
                Center(child: Text(context.tr(ErrorHandler.getErrorMessage(transactionState.error))))
              else
                Column(
                  children: [
                    Card(
                      elevation: 4,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18)),
                      child: Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          children: [
                            AspectRatio(
                              aspectRatio: 1.5,
                              child: PieChartWidget(
                                pending: pendingTransactions.length,
                                cancelled: cancelledTransactions.length,
                                paid: paidTransactions.length,
                              ),
                            ),
                            const Divider(height: 30),
                            _buildBalanceRow(context.tr('Pending'), pendingTransactions.length, pendingAmount, currencyCode, theme.colorScheme.warning),
                            const SizedBox(height: 12),
                            _buildBalanceRow(context.tr('Paid'), paidTransactions.length, paidAmount, currencyCode, theme.colorScheme.success),
                            const SizedBox(height: 12),
                            _buildBalanceRow(context.tr('Cancelled'), cancelledTransactions.length, cancelledAmount, currencyCode, theme.colorScheme.error),
                          ],
                        ),
                      ),
                    ),
                    const SizedBox(height: 20),
                    Text(
                      context.tr('Provider Analytics'),
                      style: theme.textTheme.headlineSmall,
                    ),
                    const SizedBox(height: 20),
                    Card(
                      elevation: 4,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18)),
                      child: providerAnalytics.isEmpty
                          ? Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: Center(
                                child: EmptyStateWidget(
                                  message: context.tr('No provider data for this period'),
                                  lottieSize: 120,
                                ),
                              ),
                            )
                          : ListView.separated(
                              shrinkWrap: true,
                              physics: const NeverScrollableScrollPhysics(),
                              padding: const EdgeInsets.all(16),
                              itemCount: providerAnalytics.length,
                              separatorBuilder: (context, index) => const Divider(),
                              itemBuilder: (context, index) {
                                final entry = providerAnalytics.entries.elementAt(index);
                                final providerName = entry.key;
                                final count = entry.value['count'];
                                final amount = entry.value['amount'];
                                return _buildProviderBalanceRow(providerName, count, amount, currencyCode);
                              },
                            ),
                    ),
                  ],
                ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildBalanceRow(String title, int count, double amount, String currencyCode, Color color) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Row(
          children: [
            Icon(Icons.circle, color: color, size: 12),
            const SizedBox(width: 8),
            Text('$title ($count)', style: Theme.of(context).textTheme.titleMedium),
          ],
        ),
        Text('$currencyCode ${amount.toStringAsFixed(2)}', style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold)),
      ],
    );
  }

  Widget _getBankIcon(String bankName) {
    final theme = Theme.of(context);
    String name = bankName.toLowerCase();
    if (name.contains('bkash')) {
      return Image.asset('assets/preloader/bkash.png', width: 30, height: 30, fit: BoxFit.contain);
    } else if (name.contains('rocket')) {
      return Image.asset('assets/preloader/rocket.png', width: 30, height: 30, fit: BoxFit.contain);
    } else if (name.contains('nagad')) {
      return Image.asset('assets/preloader/nagad.png', width: 30, height: 30, fit: BoxFit.contain);
    } else if (name.contains('bank')) {
      return Image.asset('assets/preloader/bank.png', width: 30, height: 30, fit: BoxFit.contain);
    } else if (name.contains('card')) {
      return Icon(Icons.credit_card, color: theme.colorScheme.tertiary, size: 30);
    }
    return Icon(Icons.account_balance_wallet, color: theme.colorScheme.outline, size: 30);
  }

  Widget _buildProviderBalanceRow(String providerName, int count, double amount, String currencyCode) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Expanded(
          child: Row(
            children: [
              _getBankIcon(providerName),
              const SizedBox(width: 12),
              Expanded(
                child: Text(
                  '$providerName ($count)',
                  style: Theme.of(context).textTheme.titleMedium,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
        ),
        Text('$currencyCode ${amount.toStringAsFixed(2)}', style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold)),
      ],
    );
  }
}

class PieChartWidget extends StatefulWidget {
  final int pending;
  final int cancelled;
  final int paid;

  const PieChartWidget({
    Key? key,
    required this.pending,
    required this.cancelled,
    required this.paid,
  }) : super(key: key);

  @override
  State<PieChartWidget> createState() => _PieChartWidgetState();
}

class _PieChartWidgetState extends State<PieChartWidget> {
  int touchedIndex = -1;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final total = widget.pending + widget.cancelled + widget.paid;
    if (total == 0) {
      return Center(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.info_outline, color: theme.colorScheme.outline, size: 22),
            const SizedBox(width: 8),
            Text(
              context.tr('No transactions found for this period'),
              style: theme.textTheme.bodyMedium,
            ),
          ],
        ),
      );
    }
    return Row(
      children: <Widget>[
        Expanded(
          child: Stack(
            alignment: Alignment.center,
            children: [
              PieChart(
                PieChartData(
                  pieTouchData: PieTouchData(
                    touchCallback: (FlTouchEvent event, pieTouchResponse) {
                      setState(() {
                        if (!event.isInterestedForInteractions ||
                            pieTouchResponse == null ||
                            pieTouchResponse.touchedSection == null) {
                          touchedIndex = -1;
                          return;
                        }
                        touchedIndex = pieTouchResponse.touchedSection!.touchedSectionIndex;
                      });
                    },
                  ),
                  borderData: FlBorderData(
                    show: false,
                  ),
                  sectionsSpace: 0,
                  centerSpaceRadius: 40,
                  sections: showingSections(),
                ),
              ),
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    context.tr('Total'),
                    style: theme.textTheme.titleMedium,
                  ),
                  Text(
                    total.toString(),
                    style: theme.textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.bold),
                  ),
                ],
              )
            ],
          ),
        ),
        Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Indicator(
              color: theme.colorScheme.warning,
              text: context.tr('Pending'),
              isSquare: true,
            ),
            const SizedBox(
              height: 4,
            ),
            Indicator(
              color: theme.colorScheme.error,
              text: context.tr('Cancelled'),
              isSquare: true,
            ),
            const SizedBox(
              height: 4,
            ),
            Indicator(
              color: theme.colorScheme.success,
              text: context.tr('Paid'),
              isSquare: true,
            ),
            const SizedBox(
              height: 18,
            ),
          ],
        ),
      ],
    );
  }

  List<PieChartSectionData> showingSections() {
    final theme = Theme.of(context);
    final total = widget.pending + widget.cancelled + widget.paid;

    final sections = <PieChartSectionData>[];
    int sectionIndex = 0;

    if (widget.pending > 0) {
      final isTouched = sectionIndex == touchedIndex;
      final fontSize = isTouched ? 18.0 : 14.0;
      final radius = isTouched ? 60.0 : 50.0;
      final title = '${(widget.pending / total * 100).toStringAsFixed(1)}%';

      sections.add(PieChartSectionData(
        color: theme.colorScheme.warning,
        value: widget.pending.toDouble(),
        title: title,
        radius: radius,
        titleStyle: TextStyle(
            fontSize: fontSize,
            fontWeight: FontWeight.bold,
            color: theme.colorScheme.onWarning),
        badgeWidget: _buildBadge('${context.tr('Pending')}: ${widget.pending}', theme.colorScheme.warning, isTouched),
        badgePositionPercentageOffset: .98,
      ));
      sectionIndex++;
    }

    if (widget.cancelled > 0) {
      final isTouched = sectionIndex == touchedIndex;
      final fontSize = isTouched ? 18.0 : 14.0;
      final radius = isTouched ? 60.0 : 50.0;
      final title = '${(widget.cancelled / total * 100).toStringAsFixed(1)}%';

      sections.add(PieChartSectionData(
        color: theme.colorScheme.error,
        value: widget.cancelled.toDouble(),
        title: title,
        radius: radius,
        titleStyle: TextStyle(
            fontSize: fontSize,
            fontWeight: FontWeight.bold,
            color: theme.colorScheme.onError),
        badgeWidget: _buildBadge('${context.tr('Cancelled')}: ${widget.cancelled}', theme.colorScheme.error, isTouched),
        badgePositionPercentageOffset: .98,
      ));
      sectionIndex++;
    }

    if (widget.paid > 0) {
      final isTouched = sectionIndex == touchedIndex;
      final fontSize = isTouched ? 18.0 : 14.0;
      final radius = isTouched ? 60.0 : 50.0;
      final title = '${(widget.paid / total * 100).toStringAsFixed(1)}%';

      sections.add(PieChartSectionData(
        color: theme.colorScheme.success,
        value: widget.paid.toDouble(),
        title: title,
        radius: radius,
        titleStyle: TextStyle(
            fontSize: fontSize,
            fontWeight: FontWeight.bold,
            color: theme.colorScheme.onSuccess),
        badgeWidget: _buildBadge('${context.tr('Paid')}: ${widget.paid}', theme.colorScheme.success, isTouched),
        badgePositionPercentageOffset: .98,
      ));
    }

    return sections;
  }

  Widget _buildBadge(String text, Color color, bool isTouched) {
    if (!isTouched) {
      return Container();
    }
    return Container(
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        color: color,
        borderRadius: BorderRadius.circular(4),
        boxShadow: <BoxShadow>[
          BoxShadow(
            color: Colors.black.withOpacity(.5),
            offset: const Offset(3, 3),
            blurRadius: 3,
          ),
        ],
      ),
      child: Text(
        text,
        style: const TextStyle(
            color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
      ),
    );
  }
}

class Indicator extends StatelessWidget {
  const Indicator({
    Key? key,
    required this.color,
    required this.text,
    required this.isSquare,
    this.size = 16,
    this.textColor,
  }) : super(key: key);
  final Color color;
  final String text;
  final bool isSquare;
  final double size;
  final Color? textColor;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Row(
      children: <Widget>[
        Container(
          width: size,
          height: size,
          decoration: BoxDecoration(
            shape: isSquare ? BoxShape.rectangle : BoxShape.circle,
            color: color,
          ),
        ),
        const SizedBox(
          width: 8,
        ),
        Text(
          text,
          style: theme.textTheme.bodyMedium?.copyWith(color: textColor),
        )
      ],
    );
  }
}
