import 'package:flutter/material.dart';
import 'package:printing/printing.dart';
import '../../order/models/order_model.dart';
import '../../order/services/pdf_invoice_service.dart';

class AdminInvoicePreviewScreen extends StatelessWidget {
  final OrderModel order;
  final String shopName;

  const AdminInvoicePreviewScreen({super.key, required this.order, required this.shopName});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Invoice #${order.id.substring(order.id.length > 8 ? order.id.length - 8 : 0)}'),
        elevation: 0,
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: PdfPreview(
        build: (format) => PdfInvoiceService.generateInvoicePdf(order, shopName),
        canChangeOrientation: false,
        canChangePageFormat: false,
        canDebug: false,
        pdfFileName: 'Invoice_${order.id}.pdf',
      ),
    );
  }
}
