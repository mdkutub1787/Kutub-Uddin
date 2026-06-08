import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/models/track_transfer/track_transfer_model.dart';
import 'package:fflipy/models/transaction_model/transaction_report_model.dart';
import 'package:fflipy/providers/track_transfer_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import '../../core/widgets/brand_app_bar.dart';

class TrackingDetailsScreen extends ConsumerStatefulWidget {
  final TransactionModel transaction;
  const TrackingDetailsScreen({super.key, required this.transaction});

  @override
  ConsumerState<TrackingDetailsScreen> createState() =>
      _TrackingDetailsScreenState();
}

class _TrackingDetailsScreenState extends ConsumerState<TrackingDetailsScreen> {
  bool _isExpanded = false;
  bool _canCollapse = false;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        context.go('/track-transfer');
        return false;
      },
      child: _buildTrackingDetailsContent(context),
    );
  }

  Widget _buildTrackingDetailsContent(BuildContext context) {
    final trackTransferAsyncValue =
        ref.watch(trackTransferProvider(widget.transaction.refNo));

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('Tracking Details')),
      ),
      backgroundColor: Theme.of(context).colorScheme.surface,
      body: trackTransferAsyncValue.when(
        data: (trackData) {
          final logs = trackData?.data?.trackerLogs ?? [];
          WidgetsBinding.instance.addPostFrameCallback((_) {
            if (mounted) {
              setState(() {
                _canCollapse = logs.length > 6;
              });
            }
          });
          return SingleChildScrollView(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16.0),
                  child: _buildTransactionSummary(
                      widget.transaction, trackData?.data),
                ),
                const SizedBox(height: 24),
                if (logs.isNotEmpty)
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16.0),
                    child: _buildLatestStatus(logs.first),
                  )
                else
                  Center(child: Text(context.tr('No tracking data available'))),
                const SizedBox(height: 16),
                if (logs.length > 1)
                  _buildTrackingHistory(logs.skip(1).toList()),
                if (_canCollapse) const SizedBox(height: 72),
              ],
            ),
          );
        },
        loading: () {
          return Column(
            children: [
              Padding(
                padding: const EdgeInsets.symmetric(
                    vertical: 16.0, horizontal: 16.0),
                child: _buildTransactionSummary(widget.transaction, null),
              ),
              const Expanded(
                child: Center(child: Preloader()),
              ),
            ],
          );
        },
        error: (error, stack) {
          return Column(
            children: [
              Padding(
                padding: const EdgeInsets.symmetric(
                    vertical: 16.0, horizontal: 16.0),
                child: _buildTransactionSummary(widget.transaction, null),
              ),
              Expanded(
                child: Center(child: Text(context.tr(ErrorHandler.getErrorMessage(error)))),
              ),
            ],
          );
        },
      ),
      floatingActionButton: _canCollapse
          ? FloatingActionButton.extended(
              onPressed: () {
                setState(() {
                  _isExpanded = !_isExpanded;
                });
              },
              label: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(_isExpanded ? context.tr('See Less') : context.tr('See More')),
                  const SizedBox(width: 8.0),
                  Icon(_isExpanded
                      ? Icons.keyboard_arrow_up
                      : Icons.keyboard_arrow_down),
                ],
              ),
              backgroundColor: Colors.white,
              foregroundColor: Theme.of(context).colorScheme.primary,
              elevation: 4.0,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(24.0),
                side: const BorderSide(color: Colors.pink, width: 2),
              ),
            )
          : null,
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
    );
  }

  Widget _buildLatestStatus(TrackerLogs latestLog) {
    final theme = Theme.of(context);
    final style = _getOperationStyle(latestLog.operationNameEn ?? '');
    String formattedDate = '';
    if (latestLog.createdAt != null) {
      try {
        final dateTime = DateTime.parse(latestLog.createdAt!).toLocal();
        formattedDate = DateFormat('dd MMM yyyy, hh:mm a').format(dateTime);
      } catch (_) {}
    }

    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12),
        color: theme.cardColor,
        boxShadow: [
          BoxShadow(
            color: Colors.red.withOpacity(0.1),
            blurRadius: 8,
            offset: const Offset(-4, -4),
          ),
          BoxShadow(
            color: Colors.green.withOpacity(0.1),
            blurRadius: 8,
            offset: const Offset(4, -4),
          ),
          BoxShadow(
            color: Colors.amber.withOpacity(0.1),
            blurRadius: 8,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Card(
        elevation: 0,
        color: style.color.withOpacity(0.1),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Row(
            children: [
              CircleAvatar(
                radius: 22,
                backgroundColor: style.color,
                child: Icon(style.icon, color: Colors.white, size: 24),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      context.tr('Latest Status'),
                      style: theme.textTheme.labelMedium?.copyWith(
                        color: style.color.withOpacity(0.9),
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      context.tr(latestLog.operationNameEn ?? 'Unknown Status'),
                      style: theme.textTheme.titleMedium?.copyWith(
                        fontWeight: FontWeight.bold,
                        color: style.color,
                        height: 1.2,
                      ),
                    ),
                    const SizedBox(height: 2),
                    Text(
                      formattedDate,
                      style: theme.textTheme.bodySmall
                          ?.copyWith(color: style.color.withOpacity(0.8)),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildTrackingHistory(List<TrackerLogs> logs) {
    if (logs.isEmpty) {
      return const SizedBox.shrink();
    }

    final List<TrackerLogs> displayedLogs =
        _isExpanded ? logs : (logs.length > 5 ? logs.take(5).toList() : logs);

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: const EdgeInsets.only(left: 8.0, bottom: 16),
            child: Text(
              context.tr('Full History'),
              style: Theme.of(context)
                  .textTheme
                  .titleMedium
                  ?.copyWith(fontWeight: FontWeight.bold),
            ),
          ),
          ...List.generate(displayedLogs.length, (index) {
            return _buildTrackerLog(
              displayedLogs[index],
              isFirst: false,
              isLast: index == displayedLogs.length - 1 && !_isExpanded,
            );
          }),
        ],
      ),
    );
  }

  Widget _buildTransactionSummary(
      TransactionModel details, Data? trackDetails) {
    final theme = Theme.of(context);
    final senderName = trackDetails?.senderName;

    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        color: theme.cardColor,
        boxShadow: [
          BoxShadow(
            color: Colors.red.withOpacity(0.1),
            blurRadius: 8,
            offset: const Offset(-4, -4),
          ),
          BoxShadow(
            color: Colors.green.withOpacity(0.1),
            blurRadius: 8,
            offset: const Offset(4, -4),
          ),
          BoxShadow(
            color: Colors.amber.withOpacity(0.1),
            blurRadius: 8,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Card(
        elevation: 0,
        margin: EdgeInsets.zero,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "${context.tr('From')}: ${senderName ?? context.tr('You')}",
                      style: theme.textTheme.titleSmall
                          ?.copyWith(fontWeight: FontWeight.w600),
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 4),
                    Text(
                      "${context.tr('To')}: ${details.recipientName}",
                      style: theme.textTheme.titleSmall
                          ?.copyWith(fontWeight: FontWeight.w600),
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      '${context.tr('Ref')}: ${details.refNo}',
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
                    '${details.sendAmount} ${details.sendCurr}',
                    style: theme.textTheme.titleMedium?.copyWith(
                        fontWeight: FontWeight.bold,
                        color: theme.colorScheme.primary),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    '${details.recipientGetAmount} ${details.receiveCurr}',
                    style: theme.textTheme.bodyMedium
                        ?.copyWith(color: Colors.grey.shade700),
                  )
                ],
              )
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildTrackerLog(TrackerLogs log,
      {bool isFirst = false, bool isLast = false}) {
    String formattedDate = log.createdAt ?? '';
    if (log.createdAt != null) {
      try {
        final dateTime = DateTime.parse(log.createdAt!).toLocal();
        final now = DateTime.now();
        final today = DateTime(now.year, now.month, now.day);
        final logDay = DateTime(dateTime.year, dateTime.month, dateTime.day);
        final difference = now.difference(dateTime);

        if (difference.inMinutes < 1) {
          formattedDate = context.tr('just now');
        } else if (logDay == today) {
          formattedDate = DateFormat('hh:mm a').format(dateTime);
        } else if (logDay == today.subtract(const Duration(days: 1))) {
          formattedDate =
          '${context.tr('Yesterday')} at ${DateFormat('hh:mm a').format(dateTime)}';
        } else {
          formattedDate = DateFormat('dd MMM yyyy, hh:mm a').format(dateTime);
        }
      } catch (_) {
        formattedDate = log.createdAt ?? '';
      }
    }

    _getOperationStyle(log.operationNameEn ?? '');
    final theme = Theme.of(context);

    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                width: 2,
                height: 4,
                color: isFirst ? Colors.transparent : Colors.grey.shade300,
              ),
              _getOperationIcon(log.operationNameEn ?? '', isHighlighted: false),
              Expanded(
                child: Container(
                  width: 2,
                  color: isLast ? Colors.transparent : Colors.grey.shade300,
                ),
              ),
            ],
          ),
          const SizedBox(width: 16),
          Expanded(
            child: Container(
              margin: EdgeInsets.only(bottom: isLast ? 0 : 12),
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(12),
                color: theme.cardColor,
                boxShadow: [
                  BoxShadow(
                    color: Colors.red.withOpacity(0.05),
                    blurRadius: 6,
                    offset: const Offset(-3, -3),
                  ),
                  BoxShadow(
                    color: Colors.green.withOpacity(0.05),
                    blurRadius: 6,
                    offset: const Offset(3, -3),
                  ),
                  BoxShadow(
                    color: Colors.amber.withOpacity(0.05),
                    blurRadius: 6,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),
              child: Card(
                margin: EdgeInsets.zero,
                elevation: 0,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                  side: BorderSide(color: Colors.grey.shade200, width: 1),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        context.tr(log.operationNameEn ?? 'Unknown Status'),
                        style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                            fontWeight: FontWeight.w600, height: 1.2),
                      ),
                      const SizedBox(height: 6),
                      Text(
                        formattedDate,
                        style: Theme.of(context)
                            .textTheme
                            .bodyMedium
                            ?.copyWith(color: Colors.grey.shade600),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _getOperationIcon(String operationName, {bool isHighlighted = false}) {
    final style = _getOperationStyle(operationName);

    return CircleAvatar(
      radius: isHighlighted ? 22 : 14,
      backgroundColor:
          isHighlighted ? style.color : style.color.withOpacity(0.8),
      child: Icon(
        style.icon,
        color: Colors.white,
        size: isHighlighted ? 24 : 16,
      ),
    );
  }

  ({IconData icon, Color color}) _getOperationStyle(String operationName) {
    final name = operationName.toLowerCase();

    final Map<String, ({IconData icon, Color color})> operationStyles = {
      'request received': (
        icon: Icons.check_circle_outline,
        color: Colors.blue.shade600
      ),
      'transfer submitted': (icon: Icons.send, color: Colors.blue.shade600),
      'transfer is being processed':
          (icon: Icons.sync, color: Colors.blue.shade600),
      'payment is being processed':
          (icon: Icons.sync, color: Colors.blue.shade600),
      'providing estimate':
          (icon: Icons.calculate_outlined, color: Colors.blue.shade400),
      'transfer sent for correction':
          (icon: Icons.edit_note_outlined, color: Colors.blue.shade600),
      'review completed, processing continues': (
        icon: Icons.playlist_add_check_circle_outlined,
        color: Colors.blue.shade600
      ),

      'transfer is being prepared for payout':
          (icon: Icons.hourglass_full, color: Colors.purple.shade600),
      'payment transmitted to recipient':
          (icon: Icons.receipt_long_outlined, color: Colors.purple.shade600),

      'money is on its way':
          (icon: Icons.local_shipping_outlined, color: Colors.teal.shade600),
      'money is out for delivery':
          (icon: Icons.delivery_dining_outlined, color: Colors.teal.shade600),

      'transfer is on hold':
          (icon: Icons.pause_circle_outline, color: Colors.orange.shade600),
      'transfer updated, awaiting confirmation': (
        icon: Icons.edit_notifications_outlined,
        color: Colors.amber.shade700
      ),
      'transfer sent for repair':
          (icon: Icons.build_outlined, color: Colors.orange.shade700),
      'transfer under review by support':
          (icon: Icons.support_agent_outlined, color: Colors.orange.shade800),
      'waiting for payer confirmation':
          (icon: Icons.timer_outlined, color: Colors.amber.shade800),
      'incoming payment placed on hold':
          (icon: Icons.pan_tool_outlined, color: Colors.orange.shade600),
      'information required':
          (icon: Icons.info_outline, color: Colors.amber.shade700),

      'money is ready for pick up': (
        icon: Icons.store_mall_directory_outlined,
        color: Colors.green.shade600
      ),
      'money has been picked up by the recipient': (
        icon: Icons.person_pin_circle_outlined,
        color: Colors.green.shade700
      ),
      'money has reached the beneficiary':
          (icon: Icons.verified_user_outlined, color: Colors.green.shade800),
      'transfer completed successfully':
          (icon: Icons.done_all, color: Colors.green.shade700),
      'transfer repaired successfully':
          (icon: Icons.check_circle_outline, color: Colors.green.shade600),
      'transfer corrected and processed':
          (icon: Icons.check_circle_outline, color: Colors.green.shade600),
      'transfer repaired and completed':
          (icon: Icons.check_circle, color: Colors.green.shade800),
      'transfer cleared for payout':
          (icon: Icons.done_all_outlined, color: Colors.green.shade600),
      'recipient has received the money':
          (icon: Icons.paid_outlined, color: Colors.green.shade700),

      'transfer has been cancelled':
          (icon: Icons.cancel_outlined, color: Colors.red.shade600),
      'transfer voided':
          (icon: Icons.do_not_disturb_on_outlined, color: Colors.red.shade700),
      'transfer canceled': (icon: Icons.cancel, color: Colors.red.shade600),
      'transfer canceled by agent':
          (icon: Icons.person_off_outlined, color: Colors.red.shade700),
      'refund issued': (icon: Icons.undo_outlined, color: Colors.red.shade400),
      'refund sent to agent': (icon: Icons.undo, color: Colors.red.shade500),

      'processing': (icon: Icons.sync, color: Colors.grey.shade600),
    };

    for (final entry in operationStyles.entries) {
      if (name.contains(entry.key)) {
        return entry.value;
      }
    }

    return (icon: Icons.info_outline, color: Colors.grey.shade500);
  }
}
