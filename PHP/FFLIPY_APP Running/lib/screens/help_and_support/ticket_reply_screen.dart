import 'dart:io';

import 'package:fflipy/core/constants/api_config.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/utils/dialog_helper.dart';
import 'package:fflipy/core/widgets/empty_state_widget.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/models/support_ticket_model/support_ticket_model.dart';
import 'package:fflipy/models/support_ticket_model/ticket_details_model.dart';
import 'package:fflipy/providers/profile_providers.dart';
import 'package:fflipy/providers/support_ticket_providers.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:intl/intl.dart';
import '../../core/widgets/brand_app_bar.dart';

import '../../core/theme/app_theme.dart';

class TicketReplyScreen extends ConsumerStatefulWidget {
  final SupportTicketItem ticket;

  const TicketReplyScreen({super.key, required this.ticket});

  @override
  ConsumerState<TicketReplyScreen> createState() => _TicketReplyScreenState();
}

class _TicketReplyScreenState extends ConsumerState<TicketReplyScreen> {
  final _replyController = TextEditingController();
  PlatformFile? _selectedFile;
  final ScrollController _scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    Future.microtask(() => _fetchTicketDetails());
  }

  @override
  void dispose() {
    _replyController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  void _fetchTicketDetails() {
    ref.read(supportTicketViewModelProvider.notifier).getTicketDetails(widget.ticket.ticket);
  }

  void _scrollToBottom() {
    if (_scrollController.hasClients) {
      _scrollController.animateTo(
        _scrollController.position.maxScrollExtent,
        duration: const Duration(milliseconds: 300),
        curve: Curves.easeOut,
      );
    }
  }

  Future<void> _pickFile() async {
    FilePickerResult? result = await FilePicker.platform.pickFiles();
    if (result != null) {
      final file = result.files.first;
      final fileSizeInMB = file.size / (1024 * 1024);

      if (fileSizeInMB > 1.0) {
        if (mounted) {
          showDialog(
            context: context,
            builder: (context) => AlertDialog(
              title: Text(context.tr('File Too Large')),
              content: Text(context.tr('The selected file is larger than 1MB. Please upload a smaller file.')),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: Text(context.tr('OK')),
                ),
              ],
            ),
          );
        }
        return;
      }

      setState(() {
        _selectedFile = file;
      });
    }
  }

  void _sendReply() {
    if (ref.read(supportTicketViewModelProvider).isLoading) return;

    if (_replyController.text.trim().isEmpty && _selectedFile == null) {
      DialogHelper.showSnackBar(context, context.tr('Please enter a message or attach a file'), isError: true);
      return;
    }

    FocusScope.of(context).unfocus();
    ref.read(supportTicketViewModelProvider.notifier).replySupportTicket(
        id: widget.ticket.id,
        ticketId: widget.ticket.ticket,
        message: _replyController.text,
        attachments: _selectedFile?.path,
        replyType: 1,
        onSuccess: (message) {
          DialogHelper.showSnackBar(context, message);
          _replyController.clear();
          setState(() {
            _selectedFile = null;
          });
          _fetchTicketDetails();
          Future.delayed(const Duration(milliseconds: 100), _scrollToBottom);
        });
  }

  void _closeTicket() async {
    final confirmed = await DialogHelper.showConfirmationDialog(
      context: context,
      title: context.tr('Close Ticket'),
      message: context.tr('Are you sure you want to close this ticket?'),
      confirmText: context.tr('Close'),
      cancelText: context.tr('Cancel'),
    );

    if (confirmed == true && mounted) {
      ref.read(supportTicketViewModelProvider.notifier).replySupportTicket(
          id: widget.ticket.id,
          ticketId: widget.ticket.ticket,
          message: context.tr('Ticket closed by user'),
          replyType: 2,
          onSuccess: (message) {
            DialogHelper.showSnackBar(context, message);
            Navigator.of(context).pop();
          });
    }
  }

  @override
  Widget build(BuildContext context) {
    final ticketState = ref.watch(supportTicketViewModelProvider);
    final ticketDetail = ticketState.ticketDetailsResponse?.data?.ticket;

    ref.listen(supportTicketViewModelProvider, (previous, next) {
      if (next.error != null && next.error != previous?.error) {
        DialogHelper.showSnackBar(context, context.tr(ErrorHandler.getErrorMessage(next.error!)), isError: true);
      }
      if (previous?.isLoading == true && next.isLoading == false && next.error == null) {
        WidgetsBinding.instance.addPostFrameCallback((_) => _scrollToBottom());
      }
    });

    final isTicketClosed = ticketDetail?.status == '3' || widget.ticket.status == '3';

    return Scaffold(
      appBar: BrandAppBar(
        title: Text('${context.tr("Ticket")} #${widget.ticket.ticket}'),
        actions: [
          if (!isTicketClosed)
            TextButton.icon(
              onPressed: _closeTicket,
              icon: const Icon(Icons.close, size: 20),
              label: Text(context.tr('Close')),
            ),
        ],
      ),
      body: Column(
        children: [
          _TicketInfoHeader(
            subject: ticketDetail?.subject ?? widget.ticket.subject,
            createdAt: ticketDetail?.createdAt ?? widget.ticket.createdAt,
            status: ticketDetail?.status ?? widget.ticket.status,
          ),
          Expanded(
            child: (ticketState.isLoading && ticketDetail == null)
                ? const Center(child: Preloader())
                : (ticketState.error != null && ticketDetail == null)
                    ? _ErrorView(error: ticketState.error, onRetry: _fetchTicketDetails)
                    : (ticketDetail == null || ticketDetail.messages.isEmpty)
                        ? Center(child: EmptyStateWidget(message: context.tr('No messages found')))
                        : _MessageList(
                            scrollController: _scrollController,
                            messages: ticketDetail.messages,
                            widget: widget,
                          ),
          ),
          if (!isTicketClosed)
            _ReplyInputSection(
              replyController: _replyController,
              selectedFile: _selectedFile,
              onPickFile: _pickFile,
              onSendReply: _sendReply,
              onRemoveFile: () => setState(() => _selectedFile = null),
            ),
        ],
      ),
    );
  }
}

class _ErrorView extends StatelessWidget {
  const _ErrorView({this.error, required this.onRetry});

  final String? error;
  final VoidCallback onRetry;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(context.tr(ErrorHandler.getErrorMessage(error ?? 'Could not load ticket details.')), textAlign: TextAlign.center),
            const SizedBox(height: 16),
            ElevatedButton(onPressed: onRetry, child: Text(context.tr('Retry'))),
          ],
        ),
      ),
    );
  }
}

class _TicketInfoHeader extends StatelessWidget {
  const _TicketInfoHeader({
    required this.subject,
    required this.createdAt,
    required this.status,
  });

  final String subject;
  final String createdAt;
  final String status;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: theme.cardTheme.color,
        border: Border(bottom: BorderSide(color: theme.dividerColor)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            subject,
            style: theme.textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 8),
          Row(
            children: [
              _TicketStatusBadge(status: status),
              const SizedBox(width: 8),
              Text(
                '${context.tr('Created')}: ${_formatDate(context, createdAt, forHeader: true)}',
                style: theme.textTheme.bodySmall?.copyWith(color: theme.hintColor),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _MessageList extends ConsumerWidget {
  const _MessageList({
    required this.scrollController,
    required this.messages,
    required this.widget,
  });

  final ScrollController scrollController;
  final List<TicketMessage> messages;
  final TicketReplyScreen widget;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final userProfile = ref.watch(profileViewModelProvider).value?.userProfile;
    final fullName = userProfile != null && userProfile.firstname != null
        ? '${userProfile.firstname} ${userProfile.lastname ?? ''}'.trim()
        : widget.ticket.name;

    final reversedMessages = messages.reversed.toList();

    return ListView.builder(
      controller: scrollController,
      padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 12),
      itemCount: reversedMessages.length,
      itemBuilder: (context, index) {
        final message = reversedMessages[index];
        final isMe = message.adminId == null;
        return _MessageBubble(
          sender: isMe ? fullName : context.tr('Support Team'),
          message: message,
          isMe: isMe,
          userImage: userProfile?.image,
        );
      },
    );
  }
}

class _MessageBubble extends StatelessWidget {
  const _MessageBubble({
    required this.sender,
    required this.message,
    required this.isMe,
    this.userImage,
  });

  final String sender;
  final TicketMessage message;
  final bool isMe;
  final String? userImage;

  Future<void> _launchUrl(String url) async {
    if (!await launchUrl(Uri.parse(url), mode: LaunchMode.externalApplication)) {
      throw Exception('Could not launch $url');
    }
  }

  bool _isImageFile(String fileName) {
    final lowercasedFileName = fileName.toLowerCase();
    return lowercasedFileName.endsWith('.png') ||
        lowercasedFileName.endsWith('.jpg') ||
        lowercasedFileName.endsWith('.jpeg') ||
        lowercasedFileName.endsWith('.gif') ||
        lowercasedFileName.endsWith('.webp');
  }

  void _showFullImage(BuildContext context, String imageUrl) {
    showGeneralDialog(
      context: context,
      barrierDismissible: true,
      barrierLabel: '',
      barrierColor: Colors.black.withOpacity(0.9),
      pageBuilder: (context, anim1, anim2) => Scaffold(
        backgroundColor: Colors.transparent,
        body: Stack(
          alignment: Alignment.center,
          children: [
            InteractiveViewer(
              minScale: 0.5,
              maxScale: 4.0,
              child: Hero(
                tag: imageUrl,
                child: Center(
                  child: CachedNetworkImage(
                    imageUrl: imageUrl,
                    fit: BoxFit.contain,
                    placeholder: (context, url) => const Center(child: CircularProgressIndicator(color: Colors.white)),
                    errorWidget: (context, url, error) => const Icon(Icons.error, color: Colors.white),
                  ),
                ),
              ),
            ),
            Positioned(
              top: MediaQuery.of(context).padding.top + 10,
              left: 10,
              child: IconButton(
                icon: const Icon(Icons.arrow_back, color: Colors.white),
                onPressed: () => Navigator.pop(context),
              ),
            ),
            Positioned(
              top: MediaQuery.of(context).padding.top + 10,
              right: 10,
              child: Row(
                children: [
                  IconButton(
                    icon: const Icon(Icons.download, color: Colors.white),
                    onPressed: () => _launchUrl(imageUrl),
                  ),
                  IconButton(
                    icon: const Icon(Icons.close, color: Colors.white),
                    onPressed: () => Navigator.pop(context),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final attachments = message.attachments;

    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      child: Row(
        mainAxisAlignment: isMe ? MainAxisAlignment.end : MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          if (!isMe) ...[senderAvatar(context, theme), const SizedBox(width: 8)],
          Flexible(
            child: Column(
              crossAxisAlignment: isMe ? CrossAxisAlignment.end : CrossAxisAlignment.start,
              children: [
                Text(
                  sender,
                  style: theme.textTheme.bodySmall?.copyWith(fontWeight: FontWeight.bold, color: theme.hintColor),
                ),
                const SizedBox(height: 4),
                if (message.message.isNotEmpty)
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
                    decoration: BoxDecoration(
                      color: isMe ? theme.colorScheme.primary : theme.colorScheme.surfaceVariant,
                      borderRadius: BorderRadius.circular(16).copyWith(
                        bottomLeft: isMe ? const Radius.circular(16) : const Radius.circular(4),
                        bottomRight: isMe ? const Radius.circular(4) : const Radius.circular(16),
                      ),
                      boxShadow: [BoxShadow(color: theme.shadowColor.withOpacity(0.05), blurRadius: 4, offset: const Offset(0, 2))],
                    ),
                    child: Text(message.message, style: TextStyle(color: isMe ? theme.colorScheme.onPrimary : theme.colorScheme.onSurfaceVariant, height: 1.4)),
                  ),
                if (attachments.isNotEmpty)
                  ...attachments.map((attachment) {
                    if (attachment.image.isEmpty) return const SizedBox.shrink();
                    final isImage = _isImageFile(attachment.image);
                    final fileName = attachment.image.split('/').last;

                    String imageUrl = attachment.image;
                    if (!imageUrl.startsWith('http')) {
                      imageUrl = '${ApiConfig.siteUrl}assets/uploads/ticket/${attachment.image}';
                    }

                    if (isImage) {
                      return GestureDetector(
                        onTap: () => _showFullImage(context, imageUrl),
                        child: Container(
                          margin: const EdgeInsets.only(top: 8),
                          width: 240,
                          height: 180,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(color: theme.dividerColor),
                            color: theme.colorScheme.surface,
                          ),
                          child: ClipRRect(
                          borderRadius: BorderRadius.circular(11),
                          child: Stack(
                            alignment: Alignment.center,
                            children: [
                              Hero(
                                tag: imageUrl,
                                child: CachedNetworkImage(
                                  imageUrl: imageUrl,
                                  width: double.infinity,
                                  height: double.infinity,
                                  fit: BoxFit.contain,
                                  placeholder: (context, url) => Container(
                                    color: theme.colorScheme.surfaceVariant,
                                    alignment: Alignment.center,
                                    child: const CircularProgressIndicator(strokeWidth: 2),
                                  ),
                                  errorWidget: (context, url, error) => Container(
                                    color: theme.colorScheme.errorContainer,
                                    alignment: Alignment.center,
                                    child: Icon(Icons.broken_image_outlined, color: theme.colorScheme.onErrorContainer, size: 40),
                                  ),
                                ),
                              ),
                                Positioned(
                                  top: 4,
                                  right: 4,
                                  child: Container(
                                    padding: const EdgeInsets.all(4),
                                    decoration: BoxDecoration(
                                      color: theme.colorScheme.scrim.withOpacity(0.3),
                                      shape: BoxShape.circle,
                                    ),
                                    child: Icon(
                                      Icons.fullscreen,
                                      color: theme.colorScheme.onPrimary,
                                      size: 18,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      );
                    } else {
                      return GestureDetector(
                        onTap: () => _launchUrl(imageUrl),
                        child: Container(
                          margin: const EdgeInsets.only(top: 8),
                          padding: const EdgeInsets.all(8),
                          decoration: BoxDecoration(
                            color: theme.cardTheme.color,
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(color: theme.dividerColor),
                          ),
                          child: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Icon(Icons.insert_drive_file, color: theme.colorScheme.primary, size: 20),
                              const SizedBox(width: 8),
                              Flexible(
                                child: Text(
                                  fileName,
                                  style: theme.textTheme.bodySmall?.copyWith(color: theme.colorScheme.primary),
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    }
                  }).toList(),
                const SizedBox(height: 4),
                Text(
                  _formatDate(context, message.createdAt),
                  style: theme.textTheme.labelSmall?.copyWith(color: theme.disabledColor, fontSize: 10),
                ),
              ],
            ),
          ),
          if (isMe) ...[const SizedBox(width: 8), senderAvatar(context, theme)],
        ],
      ),
    );
  }

  Widget senderAvatar(BuildContext context, ThemeData theme) {
    return CircleAvatar(
      radius: 16,
      backgroundColor: isMe ? theme.colorScheme.primaryContainer : theme.colorScheme.secondaryContainer,
      backgroundImage: isMe && userImage != null ? NetworkImage(userImage!) : null,
      child: (isMe && userImage != null)
          ? null
          : Icon(
              isMe ? Icons.person_outline : Icons.support_agent_outlined,
              size: 18,
              color: isMe ? theme.colorScheme.onPrimaryContainer : theme.colorScheme.onSecondaryContainer,
            ),
    );
  }

}

class _TicketStatusBadge extends StatelessWidget {
  final String status;

  const _TicketStatusBadge({required this.status});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    Color color;
    String text;

    switch (status) {
      case '0':
        color = theme.colorScheme.info;
        text = context.tr('Open');
        break;
      case '1':
        color = theme.colorScheme.success;
        text = context.tr('Answered');
        break;
      case '2':
        color = theme.colorScheme.warning;
        text = context.tr('Replied');
        break;
      case '3':
        color = theme.colorScheme.error;
        text = context.tr('Closed');
        break;
      default:
        color = theme.colorScheme.outline;
        text = context.tr('Unknown');
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
      decoration: BoxDecoration(
        color: color.withOpacity(0.1),
        borderRadius: BorderRadius.circular(4),
        border: Border.all(color: color.withOpacity(0.5)),
      ),
      child: Text(
        text,
        style: TextStyle(color: color, fontSize: 10, fontWeight: FontWeight.bold),
      ),
    );
  }
}

class _ReplyInputSection extends StatelessWidget {
  const _ReplyInputSection({
    required this.replyController,
    required this.selectedFile,
    required this.onPickFile,
    required this.onSendReply,
    required this.onRemoveFile,
  });

  final TextEditingController replyController;
  final PlatformFile? selectedFile;
  final VoidCallback onPickFile;
  final VoidCallback onSendReply;
  final VoidCallback onRemoveFile;

  bool _isImage(String path) {
    final lower = path.toLowerCase();
    return lower.endsWith('.jpg') || lower.endsWith('.jpeg') || lower.endsWith('.png') || lower.endsWith('.gif') || lower.endsWith('.webp');
  }

  void _showFullImage(BuildContext context, String path) {
    showDialog(
      context: context,
      builder: (context) => Dialog(
        backgroundColor: Colors.transparent,
        insetPadding: EdgeInsets.zero,
        child: Stack(
          alignment: Alignment.center,
          children: [
            InteractiveViewer(
              child: Image.file(
                File(path),
                fit: BoxFit.contain,
              ),
            ),
            Positioned(
              top: 40,
              right: 20,
              child: IconButton(
                icon: const Icon(Icons.close, color: Colors.white, size: 30),
                onPressed: () => Navigator.pop(context),
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: theme.cardTheme.color,
        border: Border(top: BorderSide(color: theme.dividerColor)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (selectedFile != null)
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                if (selectedFile!.path != null && _isImage(selectedFile!.path!))
                  GestureDetector(
                    onTap: () => _showFullImage(context, selectedFile!.path!),
                    child: Stack(
                      children: [
                        Container(
                          height: 100,
                          width: 100,
                          margin: const EdgeInsets.only(bottom: 8),
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(8),
                            border: Border.all(color: theme.dividerColor),
                            color: theme.colorScheme.surfaceVariant,
                          ),
                          child: ClipRRect(
                            borderRadius: BorderRadius.circular(8),
                            child: Image.file(
                              File(selectedFile!.path!),
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                        Positioned(
                          right: 4,
                          bottom: 12,
                          child: Container(
                            padding: const EdgeInsets.all(2),
                            decoration: BoxDecoration(
                              color: Colors.black.withOpacity(0.5),
                              borderRadius: BorderRadius.circular(4),
                            ),
                            child: const Icon(Icons.fullscreen, color: Colors.white, size: 16),
                          ),
                        ),
                      ],
                    ),
                  ),
                Container(
                  margin: const EdgeInsets.only(bottom: 8),
                  padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                  decoration: BoxDecoration(
                    color: theme.colorScheme.surfaceVariant,
                    borderRadius: BorderRadius.circular(8),
                    border: Border.all(color: theme.dividerColor),
                  ),
                  child: Row(
                    children: [
                      const Icon(Icons.attach_file, size: 16),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          selectedFile!.name,
                          style: theme.textTheme.bodySmall,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                      InkWell(
                        onTap: onRemoveFile,
                        child: Icon(Icons.close, size: 16, color: theme.colorScheme.error),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          Row(
            children: [
              IconButton(
                onPressed: onPickFile,
                icon: Icon(Icons.attach_file, color: theme.hintColor),
                tooltip: context.tr('Attach File (Max 1MB)'),
              ),
              Expanded(
                child: TextField(
                  controller: replyController,
                  decoration: InputDecoration(
                    hintText: context.tr('Type your message...'),
                    border: InputBorder.none,
                    contentPadding: const EdgeInsets.symmetric(horizontal: 8),
                  ),
                  minLines: 1,
                  maxLines: 4,
                ),
              ),
              IconButton(
                onPressed: onSendReply,
                icon: Icon(Icons.send, color: theme.colorScheme.secondary),
                tooltip: context.tr('Send Reply'),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

String _formatDate(BuildContext context, String dateString, {bool forHeader = false}) {
  try {
    if (dateString.isEmpty) return '';
    final DateTime date = DateTime.parse(dateString);
    if (forHeader) {
       return DateFormat('dd MMM yyyy, hh:mm a').format(date.toLocal());
    }
    return DateFormat('dd MMM, hh:mm a').format(date.toLocal());
  } catch (e) {
    return dateString;
  }
}
