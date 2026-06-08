import 'dart:io';

import 'package:dotted_border/dotted_border.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/utils/dialog_helper.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/providers/support_ticket_providers.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/widgets/brand_app_bar.dart';

class CreateTicketScreen extends ConsumerStatefulWidget {
  const CreateTicketScreen({super.key});

  @override
  ConsumerState<CreateTicketScreen> createState() => _CreateTicketScreenState();
}

class _CreateTicketScreenState extends ConsumerState<CreateTicketScreen> {
  final _formKey = GlobalKey<FormState>();
  final _subjectController = TextEditingController();
  final _messageController = TextEditingController();
  String? _selectedFilePath;
  String? _selectedFileName;

  @override
  void dispose() {
    _subjectController.dispose();
    _messageController.dispose();
    super.dispose();
  }

  Future<void> _pickFile() async {
    FilePickerResult? result = await FilePicker.platform.pickFiles(
      type: FileType.custom,
      allowedExtensions: ['jpg', 'jpeg', 'png', 'pdf', 'doc', 'docx'],
    );

    if (result != null) {
      final file = result.files.single;
      final fileSizeInBytes = file.size;
      final fileSizeInMB = fileSizeInBytes / (1024 * 1024);

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
        _selectedFilePath = file.path;
        _selectedFileName = file.name;
      });
    }
  }

  void _submitTicket() {
    if (_formKey.currentState!.validate()) {
      FocusScope.of(context).unfocus();

      ref.read(supportTicketViewModelProvider.notifier).createSupportTicket(
            subject: _subjectController.text,
            message: _messageController.text,
            attachments: _selectedFilePath,
            onSuccess: () {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(context.tr('Ticket created successfully')),
                  behavior: SnackBarBehavior.floating,
                ),
              );
              Navigator.pop(context);
            },
          );
    }
  }

  void _removeFile() {
    setState(() {
      _selectedFilePath = null;
      _selectedFileName = null;
    });
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
    final ticketState = ref.watch(supportTicketViewModelProvider);
    final theme = Theme.of(context);

    ref.listen(supportTicketViewModelProvider, (previous, next) {
      if (next.error != null && next.error != previous?.error) {
        DialogHelper.showSnackBar(context, context.tr(ErrorHandler.getErrorMessage(next.error!)), isError: true);
      }
    });

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('Create New Ticket')),
      ),
      body: Stack(
        children: [
          Form(
            key: _formKey,
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(24.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    context.tr('We are here to help!'),
                    style: theme.textTheme.headlineSmall?.copyWith(
                      fontWeight: FontWeight.bold,
                      color: theme.colorScheme.onSurface,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    context.tr('Please fill out the form below and we will get back to you as soon as possible.'),
                    style: theme.textTheme.bodyMedium
                        ?.copyWith(color: theme.colorScheme.onSurface.withOpacity(0.6)),
                  ),
                  const SizedBox(height: 32),
                  _buildSectionTitle(theme, context.tr('Subject')),
                  const SizedBox(height: 12),
                  _buildSubjectFormField(),
                  const SizedBox(height: 24),
                  _buildSectionTitle(theme, context.tr('Message')),
                  const SizedBox(height: 12),
                  _buildMessageFormField(),
                  const SizedBox(height: 24),
                  _buildSectionTitle(theme, context.tr('Attachments (Optional)')),
                  const SizedBox(height: 12),
                  _buildAttachmentPicker(theme),
                  const SizedBox(height: 40),
                  _buildSubmitButton(theme),
                ],
              ),
            ),
          ),
          if (ticketState.isLoading) const Preloader(),
        ],
      ),
    );
  }

  Widget _buildSectionTitle(ThemeData theme, String title) {
    return Text(
      title,
      style: theme.textTheme.titleMedium
          ?.copyWith(fontWeight: FontWeight.bold, color: theme.colorScheme.onSurface),
    );
  }

  Widget _buildSubjectFormField() {
    return TextFormField(
      controller: _subjectController,
      decoration: InputDecoration(
        hintText: context.tr('e.g., Trouble with my recent transaction'),
      ),
      validator: (value) =>
          value == null || value.isEmpty ? context.tr('Please enter a subject') : null,
      textInputAction: TextInputAction.next,
    );
  }

  Widget _buildMessageFormField() {
    return TextFormField(
      controller: _messageController,
      maxLines: 6,
      decoration: InputDecoration(
        hintText: context.tr('Describe your issue in detail...'),
        alignLabelWithHint: true,
      ),
      validator: (value) =>
          value == null || value.isEmpty ? context.tr('Please enter a message') : null,
      textInputAction: TextInputAction.done,
    );
  }

  Widget _buildAttachmentPicker(ThemeData theme) {
    if (_selectedFileName != null) {
      final isImage = _selectedFileName!.toLowerCase().endsWith('.jpg') ||
          _selectedFileName!.toLowerCase().endsWith('.jpeg') ||
          _selectedFileName!.toLowerCase().endsWith('.png');

      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            decoration: BoxDecoration(
              color: theme.colorScheme.primary.withOpacity(0.05),
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: theme.colorScheme.primary.withOpacity(0.2)),
            ),
            child: Row(
              children: [
                Icon(Icons.attach_file_rounded, color: theme.colorScheme.primary, size: 22),
                const SizedBox(width: 12),
                Expanded(
                  child: Text(
                    _selectedFileName!,
                    style: theme.textTheme.bodyMedium?.copyWith(fontWeight: FontWeight.w500),
                    overflow: TextOverflow.ellipsis,
                  ),
                ),
                IconButton(
                  icon: Icon(Icons.close_rounded, color: theme.colorScheme.error, size: 22),
                  onPressed: _removeFile,
                  constraints: const BoxConstraints(),
                  padding: EdgeInsets.zero,
                  splashRadius: 20,
                ),
              ],
            ),
          ),
          if (isImage && _selectedFilePath != null) ...[
            const SizedBox(height: 12),
            GestureDetector(
              onTap: () => _showFullImage(context, _selectedFilePath!),
              child: Stack(
                children: [
                  Container(
                    width: double.infinity,
                    height: 200,
                    decoration: BoxDecoration(
                      color: theme.colorScheme.onSurface.withOpacity(0.05),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(
                          color: theme.dividerColor.withOpacity(0.1)),
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(12),
                      child: Image.file(
                        File(_selectedFilePath!),
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                  Positioned(
                    right: 8,
                    bottom: 8,
                    child: Container(
                      padding: const EdgeInsets.all(4),
                      decoration: BoxDecoration(
                        color: Colors.black.withOpacity(0.5),
                        borderRadius: BorderRadius.circular(4),
                      ),
                      child: const Icon(Icons.fullscreen, color: Colors.white, size: 20),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ],
      );
    } else {
      return GestureDetector(
        onTap: _pickFile,
        child: DottedBorder(
          color: theme.hintColor.withOpacity(0.5),
          strokeWidth: 1.5,
          dashPattern: const [8, 6],
          borderType: BorderType.RRect,
          radius: const Radius.circular(12),
          child: Container(
            height: 140,
            width: double.infinity,
            decoration: BoxDecoration(
              color: theme.scaffoldBackgroundColor,
              borderRadius: BorderRadius.circular(12),
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  Icons.cloud_upload_outlined,
                  color: theme.colorScheme.primary,
                  size: 48,
                ),
                const SizedBox(height: 12),
                Text(
                  context.tr('Tap to upload a file'),
                  style: theme.textTheme.bodyMedium?.copyWith(
                      color: theme.hintColor, fontWeight: FontWeight.w500),
                ),
                 const SizedBox(height: 4),
                Text(
                  context.tr('PDF, DOC, JPG, PNG (Max 1MB)'),
                  style: theme.textTheme.bodySmall?.copyWith(
                      color: theme.hintColor.withOpacity(0.8)),
                ),
              ],
            ),
          ),
        ),
      );
    }
  }

  Widget _buildSubmitButton(ThemeData theme) {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: _submitTicket,
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
          textStyle:
              const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
        ),
        child: Text(context.tr('Submit Ticket')),
      ),
    );
  }
}
