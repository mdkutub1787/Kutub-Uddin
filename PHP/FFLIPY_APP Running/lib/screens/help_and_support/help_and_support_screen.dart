import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/theme/app_theme.dart';
import 'package:fflipy/core/widgets/empty_state_widget.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/models/support_ticket_model/support_ticket_model.dart';
import 'package:fflipy/providers/support_ticket_providers.dart';
import 'package:fflipy/screens/help_and_support/create_ticket_screen.dart';
import 'package:fflipy/screens/help_and_support/ticket_reply_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../core/widgets/brand_app_bar.dart';

class HelpAndSupportScreen extends ConsumerStatefulWidget {
  const HelpAndSupportScreen({super.key});

  @override
  ConsumerState<HelpAndSupportScreen> createState() =>
      _HelpAndSupportScreenState();
}

class _HelpAndSupportScreenState extends ConsumerState<HelpAndSupportScreen> {
  final TextEditingController _searchController = TextEditingController();
  String _searchQuery = '';

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(supportTicketViewModelProvider.notifier).getSupportTickets();
    });
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _refreshTickets() async {
    await ref.read(supportTicketViewModelProvider.notifier).getSupportTickets();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final ticketState = ref.watch(supportTicketViewModelProvider);

    final allTickets = ticketState.supportTicketResponse?.data?.data ?? [];
    final filteredTickets = allTickets.where((ticket) {
      if (_searchQuery.isEmpty) return true;
      final query = _searchQuery.toLowerCase();
      return ticket.ticket.toLowerCase().contains(query) ||
          ticket.subject.toLowerCase().contains(query);
    }).toList();

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('Help & Support')),
      ),
      body: Stack(
        children: [
          RefreshIndicator(
            onRefresh: _refreshTickets,
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(16.0),
              physics: const AlwaysScrollableScrollPhysics(),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _buildSearchBar(theme),
                  const SizedBox(height: 24),
                  if (allTickets.isNotEmpty) ...[
                    Text(context.tr('My Tickets'),
                        style: theme.textTheme.titleMedium
                            ?.copyWith(fontWeight: FontWeight.bold)),
                    const SizedBox(height: 12),
                  ],
                  if (ticketState.error != null && allTickets.isEmpty)
                    Center(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 40.0),
                        child: Text(context.tr(ErrorHandler.getErrorMessage(ticketState.error))),
                      ),
                    )
                  else if (allTickets.isEmpty && !ticketState.isLoading)
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 40.0),
                      child: EmptyStateWidget(message: context.tr('No support tickets found')),
                    )
                  else if (filteredTickets.isEmpty && !ticketState.isLoading)
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 40.0),
                      child: EmptyStateWidget(message: context.tr('No matching tickets found')),
                    )
                  else
                    _buildTicketList(context, theme, filteredTickets),
                ],
              ),
            ),
          ),
          if (ticketState.isLoading && allTickets.isEmpty)
            const Preloader(),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => const CreateTicketScreen()),
          );
        },
        label: Text(context.tr('Create Ticket')),
        icon: const Icon(Icons.add),
      ),
    );
  }

  Widget _buildSearchBar(ThemeData theme) {
    return TextField(
      controller: _searchController,
      onChanged: (value) {
        setState(() {
          _searchQuery = value;
        });
      },
      decoration: InputDecoration(
        hintText: context.tr('Search Ticket # or Subject'),
        hintStyle: TextStyle(color: theme.hintColor),
        prefixIcon: Icon(Icons.search, color: theme.hintColor),
        filled: true,
        contentPadding: const EdgeInsets.symmetric(vertical: 14.0),
      ),
    );
  }

  Widget _buildTicketList(BuildContext context, ThemeData theme,
      List<SupportTicketItem> tickets) {
    return ListView.separated(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      itemCount: tickets.length,
      separatorBuilder: (context, index) => const SizedBox(height: 10),
      itemBuilder: (context, index) {
        final ticket = tickets[index];
        return Card(
          elevation: 0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
            side: BorderSide(color: theme.dividerColor),
          ),
          child: Padding(
            padding: const EdgeInsets.all(12.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      '#${ticket.ticket}',
                      style: theme.textTheme.bodyMedium?.copyWith(
                          fontWeight: FontWeight.bold,
                          color: theme.colorScheme.primary),
                    ),
                    Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 8, vertical: 4),
                      decoration: BoxDecoration(
                        color: _getStatusColor(context, ticket.status).withOpacity(0.1),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Text(
                        _getStatusText(ticket.status),
                        style: TextStyle(
                            color: _getStatusColor(context, ticket.status),
                            fontSize: 10,
                            fontWeight: FontWeight.bold),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                Text(
                  ticket.subject,
                  style: theme.textTheme.titleSmall
                      ?.copyWith(fontWeight: FontWeight.w600),
                ),
                const SizedBox(height: 8),
                Row(
                  children: [
                    Text(
                      _formatDate(ticket.createdAt),
                      style: theme.textTheme.bodySmall
                          ?.copyWith(color: theme.hintColor),
                    ),
                    const Spacer(),
                    if (ticket.lastReply.isNotEmpty) ...[
                      Text(
                        _formatLastReplyDate(ticket.lastReply),
                        style: theme.textTheme.bodySmall
                            ?.copyWith(fontWeight: FontWeight.bold),
                      ),
                    ],
                    const Spacer(),
                    InkWell(
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) =>
                                  TicketReplyScreen(ticket: ticket)),
                        );
                      },
                      child: Container(
                        padding: const EdgeInsets.symmetric(
                            horizontal: 20, vertical: 8),
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(8),
                          border: 
                              Border.all(color: theme.dividerColor),
                        ),
                        child: Text(
                          context.tr('View'),
                          style: TextStyle(
                            color: theme.colorScheme.secondary,
                            fontSize: 12,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      ),
                    )
                  ],
                )
              ],
            ),
          ),
        );
      },
    );
  }

  String _formatDate(String dateString) {
    try {
      if (dateString.isEmpty) return '';
      final DateTime date = DateTime.parse(dateString);
      return DateFormat('dd MMM yyyy').format(date.toLocal());
    } catch (e) {
      return dateString.split('T')[0];
    }
  }

  String _formatLastReplyDate(String dateString) {
    if (dateString.isEmpty) return '';
    try {
      final date = DateTime.parse('${dateString.replaceFirst(' ', 'T')}Z').toLocal();
      final now = DateTime.now();
      final difference = now.difference(date);

      if (difference.inDays >= 1) {
        return '${difference.inDays}d ${context.tr('ago')}';
      }

      final hours = difference.inHours;
      final minutes = difference.inMinutes % 60;

      if (hours > 0) {
        return '${hours}h ${minutes}m ${context.tr('ago')}';
      }

      if (minutes > 0) {
        return '${minutes}m ${context.tr('ago')}';
      }

      return context.tr('just now');
    } catch (e) {
      return dateString;
    }
  }

  Color _getStatusColor(BuildContext context, String status) {
    final theme = Theme.of(context);
    switch (status) {
      case '0':
        return theme.colorScheme.info;
      case '1':
        return theme.colorScheme.success;
      case '2':
        return theme.colorScheme.warning;
      case '3':
        return theme.colorScheme.error;
      default:
        return theme.colorScheme.outline;
    }
  }

  String _getStatusText(String status) {
    switch (status) {
      case '0':
        return context.tr('Open');
      case '1':
        return context.tr('Answered');
      case '2':
        return context.tr('Replied');
      case '3':
        return context.tr('Closed');
      default:
        return context.tr('Unknown');
    }
  }
}
