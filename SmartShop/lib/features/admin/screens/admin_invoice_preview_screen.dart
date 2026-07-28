import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:printing/printing.dart';
import '../../order/models/order_model.dart';
import '../../order/services/pdf_invoice_service.dart';
import '../../../core/riverpod/settings_notifier.dart';

class AdminInvoicePreviewScreen extends ConsumerWidget {
  final OrderModel order;
  final String shopName;

  const AdminInvoicePreviewScreen({super.key, required this.order, required this.shopName});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final settings = ref.watch(settingsProvider);
    final currency = settings.currencySymbol == '৳' ? 'BDT' : settings.currencySymbol;

    return Scaffold(
      appBar: AppBar(
        title: Text('Invoice #${order.id.length > 8 ? order.id.substring(order.id.length - 8) : order.id}'),
        elevation: 0,
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: PdfPreview(
        build: (format) => PdfInvoiceService.generateInvoicePdf(order, shopName, currency: currency),
        canChangeOrientation: false,
        canChangePageFormat: false,
        canDebug: false,
        pdfFileName: 'Invoice_${order.id}.pdf',
      ),
    );
  }
}
