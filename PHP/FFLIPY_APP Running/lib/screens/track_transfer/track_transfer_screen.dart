import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/core/widgets/empty_state_widget.dart';
import 'package:fflipy/models/transaction_model/transaction_report_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../core/widgets/brand_app_bar.dart';
import '../../providers/transaction_providers.dart';

class TrackTransferScreen extends ConsumerStatefulWidget {
  const TrackTransferScreen({super.key});

  @override
  ConsumerState<TrackTransferScreen> createState() =>
      _TrackTransferScreenState();
}

class _TrackTransferScreenState extends ConsumerState<TrackTransferScreen> {
  final TextEditingController _searchController = TextEditingController();
  String _searchQuery = '';
  final ScrollController _scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(transactionViewModelProvider.notifier).getTransactionReport();
    });
    _searchController.addListener(() {
      setState(() {
        _searchQuery = _searchController.text.trim();
      });
    });

    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
          _scrollController.position.maxScrollExtent) {
        final viewModel = ref.read(transactionViewModelProvider.notifier);
        final currentState = ref.read(transactionViewModelProvider);
        final currentPage =
            currentState.transactionReport?.data.transactions.currentPage ?? 0;
        final lastPage =
            currentState.transactionReport?.data.transactions.lastPage ?? 1;

        if (currentPage < lastPage && !currentState.isLoadingMore) {
          viewModel.getTransactionReport(page: currentPage + 1);
        }
      }
    });
  }

  @override
  void dispose() {
    _searchController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final transactionState = ref.watch(transactionViewModelProvider);
    final allTransactions =
        transactionState.transactionReport?.data.transactions.data ?? [];

    final pendingTransactions = allTransactions.where((tx) => 
      tx.status != '2' && tx.status != '3'
    ).toList();
    
    final transactions = _searchQuery.isEmpty
        ? pendingTransactions
        : pendingTransactions.where((tx) =>
            tx.refNo.toLowerCase().contains(_searchQuery.toLowerCase()) ||
            tx.recipientName.toLowerCase().contains(_searchQuery.toLowerCase())
          ).toList();

    return PopScope(
      canPop: false,
      onPopInvoked: (bool didPop) {
        if (didPop) return;
        context.pop();
      },
      child: Scaffold(
        appBar: BrandAppBar(
          title: Text(context.tr('Track a Transfer')),
        ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 16, 8),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                hintText: context.tr('Search by Ref No or Recipient Name'),
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                isDense: true,
                contentPadding: const EdgeInsets.symmetric(vertical: 12, horizontal: 12),
              ),
            ),
          ),
          Expanded(
            child: transactionState.isLoading && transactions.isEmpty
                ? const Preloader()
                : transactions.isEmpty
                    ? EmptyStateWidget(message: context.tr('No transactions found.'))
                    : RefreshIndicator(
                        onRefresh: () => ref
                            .read(transactionViewModelProvider.notifier)
                            .getTransactionReport(),
                        child: ListView.builder(
                          controller: _scrollController,
                          padding: const EdgeInsets.all(8.0),
                          itemCount: transactions.length + (transactionState.isLoadingMore ? 1 : 0),
                          itemBuilder: (context, index) {
                            if (index == transactions.length) {
                              return const Center(child: CircularProgressIndicator());
                            }
                            final transaction = transactions[index];
                            return TransactionCard(transaction: transaction);
                          },
                        ),
                      ),
          ),
        ],
      ),
    ));
  }
}

class TransactionCard extends StatelessWidget {
  final TransactionModel transaction;

  const TransactionCard({super.key, required this.transaction});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        color: theme.cardColor,
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
        child: InkWell(
          onTap: () {
            context.push('/tracking-details', extra: transaction);
          },
          borderRadius: BorderRadius.circular(16),
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    CircleAvatar(
                      backgroundColor:
                          theme.colorScheme.secondary.withAlpha((0.1 * 255).toInt()),
                      child: Text(
                        transaction.recipientName.isNotEmpty
                            ? transaction.recipientName[0].toUpperCase()
                            : '?',
                        style: TextStyle(
                            color: theme.colorScheme.secondary,
                            fontWeight: FontWeight.bold),
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            transaction.recipientName,
                            style: theme.textTheme.titleMedium
                                ?.copyWith(fontWeight: FontWeight.bold),
                            overflow: TextOverflow.ellipsis,
                          ),
                          const SizedBox(height: 2),
                          Text(
                            "${context.tr('Ref')}: ${transaction.refNo}",
                            style: theme.textTheme.bodySmall
                                ?.copyWith(color: Colors.grey.shade600),
                            overflow: TextOverflow.ellipsis,
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 16),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          '${transaction.sendAmount} ${transaction.sendCurr}',
                          style: theme.textTheme.titleSmall?.copyWith(
                              fontWeight: FontWeight.bold,
                              color: theme.colorScheme.primary),
                        ),
                        const SizedBox(height: 2),
                        Text(
                          '${transaction.recipientGetAmount} ${transaction.receiveCurr}',
                          style: theme.textTheme.bodyMedium
                              ?.copyWith(color: Colors.grey.shade700),
                        )
                      ],
                    ),
                    const SizedBox(width: 16),
                    Icon(Icons.double_arrow_outlined,
                        color: Colors.green.withAlpha((0.9 * 255).toInt()),
                        size: 28),
                  ],
                ),
                const SizedBox(height: 6),
                const Divider(height: 1, thickness: 1),
                const SizedBox(height: 6),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    StatusBadge(status: transaction.status),
                    Text(
                      transaction.txPaidDate != null
                          ? '${context.tr('Paid on')} ${transaction.txPaidDate}'
                          : '',
                      style: theme.textTheme.bodySmall
                          ?.copyWith(color: Colors.grey.shade600),
                    ),
                  ],
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class StatusBadge extends StatelessWidget {
  const StatusBadge({required this.status, super.key});

  final String status;

  String _getStatusText(BuildContext context, String status) {
    switch (status) {
      case '1':
        return context.tr('Pending');
      case '2':
        return context.tr('Cancelled');
      case '3':
        return context.tr('Paid');
      default:
        return context.tr('Unknown');
    }
  }

  Color _getStatusColor(String status) {
    switch (status) {
      case '1':
        return Colors.orange.shade700;
      case '2':
        return Colors.red.shade700;
      case '3':
        return Colors.green.shade700;
      default:
        return Colors.grey.shade600;
    }
  }

  IconData _getStatusIcon(String status) {
    switch (status) {
      case '1':
        return Icons.hourglass_bottom_rounded;
      case '2':
        return Icons.cancel_rounded;
      case '3':
        return Icons.check_circle_rounded;
      default:
        return Icons.help_outline_rounded;
    }
  }

  @override
  Widget build(BuildContext context) {
    final color = _getStatusColor(status);
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 5.0),
      decoration: BoxDecoration(
        color: color.withAlpha((0.1 * 255).toInt()),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(_getStatusIcon(status), color: color, size: 14),
          const SizedBox(width: 6),
          Text(
            _getStatusText(context, status),
            style: TextStyle(
              color: color,
              fontWeight: FontWeight.w600,
              fontSize: 12,
            ),
          ),
        ],
      ),
    );
  }
}
