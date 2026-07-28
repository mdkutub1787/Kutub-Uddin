import 'dart:typed_data';
import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:printing/printing.dart';
import 'package:intl/intl.dart';
import '../models/order_model.dart';

class PdfInvoiceService {
  static Future<Uint8List> generateInvoicePdf(OrderModel order, String shopName, {String currency = 'BDT'}) async {
    final pdf = pw.Document();

    pdf.addPage(
      pw.Page(
        pageFormat: PdfPageFormat.a4,
        build: (pw.Context context) {
          return pw.Column(
            crossAxisAlignment: pw.CrossAxisAlignment.start,
            children: [
              pw.Row(
                mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                children: [
                  pw.Text(shopName, style: pw.TextStyle(fontSize: 28, fontWeight: pw.FontWeight.bold, color: PdfColors.teal)),
                  pw.Text('INVOICE', style: pw.TextStyle(fontSize: 24, fontWeight: pw.FontWeight.bold, color: PdfColors.grey700)),
                ],
              ),
              pw.SizedBox(height: 20),
              
              pw.Row(
                mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                crossAxisAlignment: pw.CrossAxisAlignment.start,
                children: [
                  pw.Column(
                    crossAxisAlignment: pw.CrossAxisAlignment.start,
                    children: [
                      pw.Text('Billed To:', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                      pw.Text(order.userName),
                      pw.Text(order.userPhone),
                      pw.Text(order.userAddress),
                    ],
                  ),
                  pw.Column(
                    crossAxisAlignment: pw.CrossAxisAlignment.end,
                    children: [
                      pw.Text('Invoice ID: #${order.id.length > 8 ? order.id.substring(order.id.length - 8) : order.id}', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                      pw.Text('Date: ${DateFormat('dd MMM yyyy, hh:mm a').format(order.date)}'),
                      pw.Text('Status: ${order.status.toUpperCase()}'),
                    ],
                  ),
                ],
              ),
              pw.SizedBox(height: 30),
              
              pw.TableHelper.fromTextArray(
                context: context,
                border: const pw.TableBorder(
                  bottom: pw.BorderSide(color: PdfColors.grey300),
                  horizontalInside: pw.BorderSide(color: PdfColors.grey300),
                ),
                headerDecoration: const pw.BoxDecoration(color: PdfColors.teal100),
                headerHeight: 25,
                cellHeight: 30,
                cellAlignments: {
                  0: pw.Alignment.centerLeft,
                  1: pw.Alignment.centerRight,
                  2: pw.Alignment.centerRight,
                  3: pw.Alignment.centerRight,
                },
                headers: ['Item Description', 'Qty', 'Unit Price', 'Total'],
                data: [
                  ...order.items.map((item) => [
                        item.product.name,
                        item.quantity.toString(),
                        '$currency ${item.product.price.toStringAsFixed(2)}',
                        '$currency ${(item.product.price * item.quantity).toStringAsFixed(2)}',
                      ]),
                ],
              ),
              pw.SizedBox(height: 20),
              
              pw.Row(
                mainAxisAlignment: pw.MainAxisAlignment.end,
                children: [
                  pw.Column(
                    crossAxisAlignment: pw.CrossAxisAlignment.end,
                    children: [
                      if (order.deliveryFee > 0)
                        pw.Text('Delivery Fee: $currency ${order.deliveryFee.toStringAsFixed(2)}'),
                      pw.SizedBox(height: 5),
                      pw.Text('Total Amount: $currency ${order.totalAmount.toStringAsFixed(2)}', style: pw.TextStyle(fontSize: 18, fontWeight: pw.FontWeight.bold, color: PdfColors.teal)),
                    ],
                  ),
                ],
              ),
              pw.SizedBox(height: 40),
              
              pw.Center(
                child: pw.Text('Thank you for shopping with $shopName!', style: pw.TextStyle(color: PdfColors.grey600, fontStyle: pw.FontStyle.italic)),
              ),
            ],
          );
        },
      ),
    );

    return pdf.save();
  }
}
