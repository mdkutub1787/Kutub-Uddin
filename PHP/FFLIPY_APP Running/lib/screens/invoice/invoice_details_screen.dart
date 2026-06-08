import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/models/invoice/invoice_model.dart';
import 'package:fflipy/providers/invoice_provider.dart';
import 'package:fflipy/viewmodels/invoice_view_model.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:printing/printing.dart';
import 'package:provider/provider.dart';
import '../../core/widgets/brand_app_bar.dart';

class InvoiceDetailsScreen extends StatelessWidget {
  final String transactionId;

  const InvoiceDetailsScreen({Key? key, required this.transactionId})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return InvoiceProvider(
      child: _InvoiceDetailsScreenContent(transactionId: transactionId),
    );
  }
}

class _InvoiceDetailsScreenContent extends StatefulWidget {
  final String transactionId;

  const _InvoiceDetailsScreenContent({Key? key, required this.transactionId})
      : super(key: key);

  @override
  State<_InvoiceDetailsScreenContent> createState() =>
      __InvoiceDetailsScreenContentState();
}

class __InvoiceDetailsScreenContentState
    extends State<_InvoiceDetailsScreenContent> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (mounted) {
        context.read<InvoiceViewModel>().getInvoice(widget.transactionId);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final primaryColor = theme.colorScheme.primary;
    final secondaryColor = theme.colorScheme.secondary;
    final backgroundColor = theme.colorScheme.surface;
    final cardColor = theme.cardColor;
    final borderRadius = BorderRadius.circular(16.0);

    return Scaffold(
      backgroundColor: backgroundColor,
      appBar: BrandAppBar(
        title: Text(context.tr('Payment Receipt')),
        actions: [
          IconButton(
            icon: const Icon(Icons.home),
            onPressed: () => context.go(AppRouter.home),
          ),
        ],
      ),
      body: Consumer<InvoiceViewModel>(
        builder: (context, viewModel, child) {
          if (viewModel.isLoading) {
            return const Center(
              child: Preloader(),
            );
          }

          final invoiceData = viewModel.invoiceModel?.data;
          if (invoiceData?.invoice == null) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.error_outline,
                    color: theme.colorScheme.error,
                    size: 64,
                  ),
                  const SizedBox(height: 16),
                  Text(
                    context.tr('No invoice data found.'),
                    style: TextStyle(
                        color: theme.colorScheme.error,
                        fontSize: 18,
                        fontWeight: FontWeight.bold),
                    textAlign: TextAlign.center,
                  ),
                ],
              ),
            );
          }

          final invoice = invoiceData!.invoice!;
          final contact = invoiceData.contact;
          final sender = invoice.sender;
          final recipient = invoice.recipient;

          // Utility function to filter out empty/null fields from a list of map rows
          List<Map<String, dynamic>> filterEmptyRows(List<Map<String, dynamic>> rows) {
            return rows.where((row) {
              final value = row['value'];
              if (value == null) return false;
              if (value is String && value.trim().isEmpty) return false;
              return true;
            }).toList();
          }

          // Recipient Information Section (PDF & UI)
          final recipientRows = filterEmptyRows([
            {'label': context.tr('Receiver Name'), 'value': recipient?.name},
            {'label': context.tr('Email'), 'value': recipient?.email},
            {'label': context.tr('Phone'), 'value': recipient?.phone},
          ]);

          // Sender Information Section (PDF & UI)
          final senderRows = filterEmptyRows([
            {'label': context.tr('Name'), 'value': sender?.name},
            {'label': context.tr('Phone'), 'value': sender?.phone},
            {'label': context.tr('Address'), 'value': sender?.address},
            {'label': context.tr('City'), 'value': sender?.city},
            {'label': context.tr('Post Code'), 'value': sender?.postCode},
            {'label': context.tr('Country'), 'value': sender?.country},
            {'label': context.tr('Funding Source'), 'value': invoice.fundingSource},
            {'label': context.tr('Sending Purpose'), 'value': getSendingPurposeTitle(invoice.sendingPurpose)},
          ]);

          return Column(
            children: [
              Expanded(
                child: SingleChildScrollView(
                  padding: const EdgeInsets.symmetric(
                      horizontal: 8.0, vertical: 16.0),
                  child: Container(
                    padding: const EdgeInsets.all(20.0),
                    decoration: BoxDecoration(
                      color: cardColor,
                      borderRadius: borderRadius,
                      boxShadow: [
                        BoxShadow(
                          color: Colors.grey.withAlpha(20),
                          spreadRadius: 2,
                          blurRadius: 16,
                          offset: const Offset(0, 8),
                        ),
                      ],
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        _buildHeader(context, contact, primaryColor),
                        const SizedBox(height: 28),
                        _buildSectionTable(context, [
                          {
                            'label': context.tr('Transaction'),
                            'value': invoice.transaction
                          },
                          {
                            'label': context.tr('Status'),
                            'value': invoice.status
                          },
                          {
                            'label': context.tr('Transaction Date'),
                            'value': formatLocalDateTime(invoice.transactionDate)
                          },
                          {
                            'label': context.tr('Service'),
                            'value': getServiceName(invoice.service)
                          },
                          {
                            'label': context.tr('Bank Name'),
                            'value': invoice.serviceProvider
                          },
                          {
                            'label': context.tr('Account Number'),
                            'value': recipient?.accountNumber
                          },
                          {
                            'label': context.tr('Send Amount'),
                            'value': invoice.sendAmount
                          },
                          {'label': context.tr('Fees'), 'value': invoice.fees},
                          {
                            'label': context.tr('Total Send Amount'),
                            'value': invoice.totalSendAmount,
                            'isHighlighted': true
                          },
                          {
                            'label': context.tr('Recipient Amount'),
                            'value': invoice.recipientAmount,
                            'isHighlighted': true
                          },
                          {'label': context.tr('Rate'), 'value': invoice.rate},
                        ], primaryColor),
                        const SizedBox(height: 28),
                        _buildTitledSection(context, context.tr(
                            'Sender Information'), senderRows, secondaryColor),
                        const SizedBox(height: 28),
                        _buildTitledSection(context, context.tr(
                            'Recipient Information'), recipientRows, secondaryColor),
                      ],
                    ),
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(
                    horizontal: 16.0, vertical: 12.0),
                child: SizedBox(
                  width: double.infinity,
                  child: ElevatedButton.icon(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: primaryColor,
                      padding: const EdgeInsets.symmetric(vertical: 16.0),
                      shape: RoundedRectangleBorder(borderRadius: borderRadius),
                      elevation: 4,
                    ),
                    icon: const Icon(Icons.print, color: Colors.white),
                    label: Text(
                      context.tr('Print'),
                      style: const TextStyle(fontWeight: FontWeight.bold,
                          color: Colors.white,
                          fontSize: 18),
                    ),
                    onPressed: () =>
                        _printInvoice(
                            context, invoice, sender, recipient, contact),
                  ),
                ),
              ),
            ],
          );
        },
      ),
    );
  }

  void _printInvoice(BuildContext context, dynamic invoice, dynamic sender,
      dynamic recipient, dynamic contact) async {
    final pdf = pw.Document();
    final theme = Theme.of(context);
    final primaryColor = PdfColor.fromInt(theme.colorScheme.primary.value);
    final secondaryColor = PdfColor.fromInt(theme.colorScheme.secondary.value);

    List<Map<String, dynamic>> filterEmptyRows(List<Map<String, dynamic>> rows) {
      return rows.where((row) {
        final value = row['value'];
        if (value == null) return false;
        if (value is String && value.trim().isEmpty) return false;
        return true;
      }).toList();
    }

    // Prepare translated labels and values
    final sectionRows = filterEmptyRows([
      {'label': context.tr('Transaction'), 'value': invoice.transaction},
      {'label': context.tr('Status'), 'value': invoice.status},
      {
        'label': context.tr('Transaction Date'),
        'value': formatLocalDateTime(invoice.transactionDate)
      },
      {'label': context.tr('Service'), 'value': getServiceName(invoice.service)},
      {'label': context.tr('Bank Name'), 'value': invoice.serviceProvider},
      {
        'label': context.tr('Account Number'),
        'value': recipient?.accountNumber
      },
      {'label': context.tr('Send Amount'), 'value': invoice.sendAmount},
      {'label': context.tr('Fees'), 'value': invoice.fees},
      {
        'label': context.tr('Total Send Amount'),
        'value': invoice.totalSendAmount,
      },
      {
        'label': context.tr('Recipient Amount'),
        'value': invoice.recipientAmount,
      },
      {'label': context.tr('Rate'), 'value': invoice.rate},
    ]);

    final senderRows = filterEmptyRows([
      {'label': context.tr('Name'), 'value': sender?.name},
      {'label': context.tr('Phone'), 'value': sender?.phone},
      {'label': context.tr('Address'), 'value': sender?.address},
      {'label': context.tr('City'), 'value': sender?.city},
      {'label': context.tr('Post Code'), 'value': sender?.postCode},
      {'label': context.tr('Country'), 'value': sender?.country},
      {'label': context.tr('Funding Source'), 'value': invoice.fundingSource},
      {'label': context.tr('Sending Purpose'), 'value': getSendingPurposeTitle(invoice.sendingPurpose)},
    ]);

    final recipientRows = filterEmptyRows([
      {'label': context.tr('Receiver Name'), 'value': recipient?.name},
      {'label': context.tr('Email'), 'value': recipient?.email},
      {'label': context.tr('Phone'), 'value': recipient?.phone},
    ]);

    pdf.addPage(
      pw.Page(
        pageFormat: PdfPageFormat.a4,
        margin: const pw.EdgeInsets.symmetric(horizontal: 24, vertical: 16), // Reduce vertical margin
        build: (pw.Context pdfContext) {
          return pw.Column(
            crossAxisAlignment: pw.CrossAxisAlignment.start,
            children: [
              // Header Section (reduce vertical space)
              pw.Row(
                mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                crossAxisAlignment: pw.CrossAxisAlignment.start,
                children: [
                  pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.start,
                      children: [
                        pw.Text(context.tr('Fflipy'), style: pw.TextStyle(
                            fontSize: 20, // slightly smaller
                            fontWeight: pw.FontWeight.bold,
                            color: primaryColor)),
                        if (contact != null) ...[
                          if (contact.address != null &&
                              contact.address!.isNotEmpty)
                            pw.Text(contact.address!,
                                style: const pw.TextStyle(fontSize: 9)),
                          if (contact.email != null && contact.email!.isNotEmpty)
                            pw.Text('${context.tr('Email')}: ${contact.email!}',
                                style: const pw.TextStyle(fontSize: 9)),
                          if (contact.phone != null && contact.phone!.isNotEmpty)
                            pw.Text('${context.tr('Phone')}: ${contact.phone!}',
                                style: const pw.TextStyle(fontSize: 9)),
                        ]
                      ]
                  ),
                  pw.Text(context.tr('dev.fflipy.com'), style: pw.TextStyle(
                      fontSize: 9, color: PdfColor.fromHex('#888888'))),
                ],
              ),
              pw.SizedBox(height: 10), // less space
              pw.Center(
                child: pw.Text(context.tr('Payment Receipt'), style: pw.TextStyle(
                    fontSize: 16,
                    fontWeight: pw.FontWeight.bold,
                    color: primaryColor)),
              ),
              pw.Divider(height: 16), // less space

              // Transaction Details Table
              _pdfSectionTable(sectionRows, primaryColor),
              pw.SizedBox(height: 14),

              // Sender Information Section
              pw.Text(context.tr('Sender Information'), style: pw.TextStyle(
                  fontWeight: pw.FontWeight.bold,
                  fontSize: 13,
                  color: secondaryColor)),
              pw.SizedBox(height: 2),
              _pdfSectionTable(senderRows, secondaryColor),
              pw.SizedBox(height: 14),

              // Recipient Information Section
              pw.Text(context.tr('Recipient Information'), style: pw.TextStyle(
                  fontWeight: pw.FontWeight.bold,
                  fontSize: 13,
                  color: secondaryColor)),
              pw.SizedBox(height: 1),
              _pdfSectionTable(
                recipientRows,
                secondaryColor,
              ),
            ],
          );
        },
      ),
    );
    await Printing.layoutPdf(onLayout: (format) async => pdf.save());
  }

  pw.Widget _pdfSectionTable(List<Map<String, dynamic>> rows,
      PdfColor accentColor) {
    return pw.Table(
      border: pw.TableBorder.all(color: PdfColor.fromHex('#eeeeee')),
      columnWidths: {
        0: const pw.FlexColumnWidth(2),
        1: const pw.FlexColumnWidth(3),
      },
      children: rows.asMap().entries.map((entry) {
        final index = entry.key;
        final row = entry.value;
        final isHighlighted = row['isHighlighted'] ?? false;
        final fontWeight = isHighlighted ? pw.FontWeight.bold : pw.FontWeight.normal;
        final color = isHighlighted ? accentColor : PdfColor.fromHex('#222222');

        final bgColor = isHighlighted
            ? PdfColor(accentColor.red, accentColor.green, accentColor.blue, 0.08)
            : (index % 2 == 0 ? PdfColors.white : PdfColor.fromHex('#fafafa'));

        return pw.TableRow(
          decoration: pw.BoxDecoration(color: bgColor),
          children: [
            pw.Padding(
              padding: const pw.EdgeInsets.all(8.0),
              child: pw.Text(row['label']?.toString() ?? '',
                  style: pw.TextStyle(fontWeight: pw.FontWeight.normal,
                      color: PdfColor.fromHex('#444444'))),
            ),
            pw.Padding(
              padding: const pw.EdgeInsets.all(8.0),
              child: pw.Text(row['value']?.toString() ?? context.tr('N/A'),
                  style: pw.TextStyle(fontWeight: fontWeight, color: color),
                  textAlign: pw.TextAlign.right),
            ),
          ],
        );
      }).toList(),
    );
  }


  Widget _buildHeader(BuildContext context, Contact? contact,
      Color accentColor) {
    final textTheme = Theme
        .of(context)
        .textTheme;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Row(
              children: [
                CircleAvatar(
                  backgroundColor: accentColor,
                  child: const Icon(Icons.receipt_long, color: Colors.white),
                ),
                const SizedBox(width: 8),
                Text(context.tr('Fflipy'),
                    style: textTheme.headlineSmall?.copyWith(
                        fontWeight: FontWeight.bold, color: Colors.black)),
              ],
            ),
            Text(('dev.fflipy.com'),
                style: textTheme.bodySmall?.copyWith(color: Colors.grey[600])),
          ],
        ),
        const SizedBox(height: 16),
        if (contact != null)
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              if (contact.address != null && contact.address!.isNotEmpty)
                Padding(
                  padding: const EdgeInsets.only(bottom: 2.0),
                  child: Text(contact.address!,
                      style: textTheme.bodyMedium?.copyWith(
                          color: Colors.grey[800])),
                ),
              if (contact.email != null && contact.email!.isNotEmpty)
                Padding(
                  padding: const EdgeInsets.only(bottom: 2.0),
                  child: Text('${context.tr('Email')}: ${contact.email!}',
                      style: textTheme.bodyMedium?.copyWith(
                          color: Colors.grey[800])),
                ),
              if (contact.phone != null && contact.phone!.isNotEmpty)
                Padding(
                  padding: const EdgeInsets.only(bottom: 2.0),
                  child: Text('${context.tr('Phone')}: ${contact.phone!}',
                      style: textTheme.bodyMedium?.copyWith(
                          color: Colors.grey[800])),
                ),
            ],
          ),
        const SizedBox(height: 16),
        Center(
          child: Text(
            context.tr('Payment Receipt'),
            style: textTheme.titleLarge?.copyWith(
                fontWeight: FontWeight.bold, color: accentColor),
          ),
        ),
      ],
    );
  }

  Widget _buildTitledSection(BuildContext context, String title,
      List<Map<String, dynamic>> rows, Color accentColor) {
    final highlightColor = accentColor.withAlpha(
        (accentColor.alpha * 0.08).round());
    final borderColor = accentColor.withAlpha(
        (accentColor.alpha * 0.2).round());
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          width: double.infinity,
          padding: const EdgeInsets.all(10.0),
          decoration: BoxDecoration(
            color: highlightColor,
            border: Border.all(color: borderColor),
            borderRadius: BorderRadius.circular(8.0),
          ),
          child: Center(
            child: Text(
              title,
              style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 16,
                  color: accentColor),
            ),
          ),
        ),
        _buildSectionTable(context, rows, accentColor),
      ],
    );
  }

  Widget _buildSectionTable(BuildContext context,
      List<Map<String, dynamic>> rows, Color accentColor) {
    return Table(
      border: TableBorder.all(color: Colors.grey.shade200),
      columnWidths: const {
        0: FlexColumnWidth(2),
        1: FlexColumnWidth(3),
      },
      children: rows.asMap().entries.map((entry) {
        final index = entry.key;
        final row = entry.value;
        final isHighlighted = row['isHighlighted'] ?? false;
        final fontWeight = isHighlighted ? FontWeight.bold : FontWeight.normal;
        final color = isHighlighted ? accentColor : Colors.black87;
        final bgColor = isHighlighted
            ? accentColor.withAlpha((accentColor.alpha * 0.08).round())
            : (index % 2 == 0 ? Colors.white : Colors.grey.shade50);

        return TableRow(
          decoration: BoxDecoration(color: bgColor),
          children: [
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Text(row['label'] as String,
                  style: TextStyle(
                      fontWeight: FontWeight.normal, color: Colors.grey[800])),
            ),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Text(
                row['value']?.toString() ?? context.tr('N/A'),
                textAlign: TextAlign.end,
                style: TextStyle(fontWeight: fontWeight, color: color),
              ),
            ),
          ],
        );
      }).toList(),
    );
  }
}

// Service & Sending Purpose mapping
const Map<int, String> serviceIdToName = {
  1: "Account Deposit (Or Bank Account)",
  2: "Office Pick-Up",
  3: "Bank Pick-Up",
  4: "Home Delivery",
  5: "Card Top Up",
  6: "Wallet Top Up",
  7: "Pix",
};
const Map<int, String> sendingPurposeIdToTitle = {
  1: "Remit to Business",
  2: "Remit to Family",
  3: "Other",
  5: "Savings Account",
  7: "Bills",
  9: "Buy Property",
  11: "Church Donation [DIZIMO]",
  15: "Pay Vacations",
  17: "School Payment",
  23: "Pay Wedding",
  25: "Remit to Friend",
  27: "Pay Credit Card",
  29: "Pay Debt",
  35: "Gift",
};

String? getServiceName(dynamic id) {
  if (id == null) return null;
  if (id is int) return serviceIdToName[id];
  if (id is String) return serviceIdToName[int.tryParse(id) ?? -1];
  return null;
}
String? getSendingPurposeTitle(dynamic id) {
  if (id == null) return null;
  if (id is int) return sendingPurposeIdToTitle[id];
  if (id is String) return sendingPurposeIdToTitle[int.tryParse(id) ?? -1];
  return null;
}

String? formatLocalDateTime(String? dateString) {
  if (dateString == null || dateString.isEmpty) return null;
  try {
    final inputFormat = DateFormat('dd MMM, yyyy hh:mm a');
    final dt = inputFormat.parse(dateString, true).toLocal();
    return inputFormat.format(dt);
  } catch (_) {
    try {
      final dt = DateTime.parse(dateString).toLocal();
      return DateFormat('dd MMM, yyyy hh:mm a').format(dt);
    } catch (_) {
      return dateString;
    }
  }
}
