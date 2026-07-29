import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:printing/printing.dart';
import '../../features/order/models/order_model.dart';
import 'package:intl/intl.dart';

class PdfService {
  static Future<void> generateOrderInvoice(OrderModel order, {String currency = r'৳'}) async {
    final pdf = pw.Document();

    // Map currency for PDF fonts
    String pdfCurrency = currency;
    if (currency == r'৳') pdfCurrency = 'BDT';
    if (currency == r'$') pdfCurrency = 'USD';

    pdf.addPage(
      pw.Page(
        pageFormat: PdfPageFormat.a4,
        build: (pw.Context context) {
          return pw.Padding(
            padding: const pw.EdgeInsets.all(40),
            child: pw.Column(
              crossAxisAlignment: pw.CrossAxisAlignment.start,
              children: [
                pw.Row(
                  mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                  children: [
                    pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.start,
                      children: [
                        pw.Text('SMART SHOP', style: pw.TextStyle(fontSize: 28, fontWeight: pw.FontWeight.bold, color: PdfColors.teal)),
                        pw.Text('Your Premium Shopping Partner'),
                      ],
                    ),
                    pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.end,
                      children: [
                        pw.Text('OFFICIAL INVOICE', style: pw.TextStyle(fontSize: 20, fontWeight: pw.FontWeight.bold, color: PdfColors.grey700)),
                        pw.Text('#${order.id.length > 8 ? order.id.substring(order.id.length - 8).toUpperCase() : order.id.toUpperCase()}'),
                      ],
                    ),
                  ],
                ),
                pw.Divider(thickness: 2, color: PdfColors.teal, height: 40),

                pw.Row(
                  mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                  crossAxisAlignment: pw.CrossAxisAlignment.start,
                  children: [
                    pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.start,
                      children: [
                        pw.Text('CUSTOMER DETAILS:', style: pw.TextStyle(fontWeight: pw.FontWeight.bold, color: PdfColors.teal)),
                        pw.Text(order.userName, style: pw.TextStyle(fontSize: 16, fontWeight: pw.FontWeight.bold)),
                        pw.Text('Phone: ${order.userPhone}'),
                        pw.SizedBox(height: 5),
                        pw.SizedBox(width: 180, child: pw.Text('Address: ${order.userAddress}')),
                      ],
                    ),
                    pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.end,
                      children: [
                        pw.Text('ORDER INFO:', style: pw.TextStyle(fontWeight: pw.FontWeight.bold, color: PdfColors.teal)),
                        pw.Text('Date: ${DateFormat('dd MMM yyyy, hh:mm a').format(order.date)}'),
                        pw.Text('Method: ${order.paymentMethod}'),
                        pw.Text('Status: ${order.status.toUpperCase()}'),
                        if (order.isPaid) pw.Text('PAYMENT: SUCCESSFUL', style: pw.TextStyle(color: PdfColors.green, fontWeight: pw.FontWeight.bold)),
                      ],
                    ),
                  ],
                ),
                pw.SizedBox(height: 40),

                pw.TableHelper.fromTextArray(
                  headerDecoration: const pw.BoxDecoration(color: PdfColors.teal50),
                  headerStyle: pw.TextStyle(fontWeight: pw.FontWeight.bold),
                  cellHeight: 35,
                  cellAlignments: {
                    0: pw.Alignment.centerLeft,
                    1: pw.Alignment.center,
                    2: pw.Alignment.centerRight,
                    3: pw.Alignment.centerRight,
                  },
                  data: [
                    ['Product Item', 'Qty', 'Unit Price', 'Total'],
                    ...order.items.map((item) => [
                      item.product.name,
                      item.quantity.toString(),
                      '$pdfCurrency ${item.product.price.toInt()}',
                      '$pdfCurrency ${(item.product.price * item.quantity).toInt()}',
                    ]),
                  ],
                ),

                pw.SizedBox(height: 30),

                pw.Align(
                  alignment: pw.Alignment.centerRight,
                  child: pw.Container(
                    width: 220,
                    padding: const pw.EdgeInsets.all(12),
                    decoration: pw.BoxDecoration(
                      color: PdfColors.grey50,
                      borderRadius: pw.BorderRadius.circular(8),
                    ),
                    child: pw.Column(
                      children: [
                        _summaryRow('Subtotal', '$pdfCurrency ${(order.totalAmount - order.deliveryFee).toInt()}'),
                        _summaryRow('Delivery Fee', '$pdfCurrency ${order.deliveryFee.toInt()}'),
                        pw.Divider(),
                        pw.Row(
                          mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                          children: [
                            pw.Text('GRAND TOTAL', style: pw.TextStyle(fontWeight: pw.FontWeight.bold, fontSize: 16)),
                            pw.Text('$pdfCurrency ${order.totalAmount.toInt()}', style: pw.TextStyle(fontWeight: pw.FontWeight.bold, fontSize: 16, color: PdfColors.teal)),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),

                pw.Spacer(),
                pw.Center(
                  child: pw.Column(
                    children: [
                      pw.Text('Scan to verify this invoice', style: pw.TextStyle(fontSize: 10, color: PdfColors.grey600)),
                      pw.SizedBox(height: 10),
                      pw.Text('Authorized Electronic Signature', style: pw.TextStyle(fontStyle: pw.FontStyle.italic, color: PdfColors.grey500)),
                      pw.SizedBox(height: 20),
                      pw.Text('Thank you for shopping at Smart Shop Bangladesh!', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                      pw.Text('Contact: support@smartshop.com | +880 1XXX-XXXXXX'),
                    ],
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );

    await Printing.layoutPdf(
      onLayout: (PdfPageFormat format) async => pdf.save(),
      name: 'SmartShop_Invoice_${order.id.length > 8 ? order.id.substring(order.id.length - 8) : order.id}.pdf',
    );
  }

  static pw.Widget _summaryRow(String label, String value) {
    return pw.Padding(
      padding: const pw.EdgeInsets.symmetric(vertical: 4),
      child: pw.Row(
        mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
        children: [
          pw.Text(label),
          pw.Text(value, style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
        ],
      ),
    );
  }
}
