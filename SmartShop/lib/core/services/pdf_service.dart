import 'dart:io';
import 'package:flutter/services.dart';
import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:printing/printing.dart';
import '../../features/order/models/order_model.dart';
import 'package:intl/intl.dart';

class PdfService {
  static Future<void> generateOrderInvoice(OrderModel order, {String currency = '৳'}) async {
    final pdf = pw.Document();

    pdf.addPage(
      pw.Page(
        pageFormat: PdfPageFormat.a4,
        build: (pw.Context context) {
          return pw.Padding(
            padding: const pw.EdgeInsets.all(30),
            child: pw.Column(
              crossAxisAlignment: pw.CrossAxisAlignment.start,
              children: [
                pw.Row(
                  mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                  children: [
                    pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.start,
                      children: [
                        pw.Text('Smart Shop', style: pw.TextStyle(fontSize: 24, fontWeight: pw.FontWeight.bold)),
                        pw.Text('Quality you can trust'),
                      ],
                    ),
                    pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.end,
                      children: [
                        pw.Text('INVOICE', style: pw.TextStyle(fontSize: 20, fontWeight: pw.FontWeight.bold, color: PdfColors.blueAccent)),
                        pw.Text('#${order.id.length > 8 ? order.id.substring(order.id.length - 8).toUpperCase() : order.id.toUpperCase()}'),
                      ],
                    ),
                  ],
                ),
                pw.SizedBox(height: 30),

                pw.Row(
                  mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                  children: [
                    pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.start,
                      children: [
                        pw.Text('Bill To:', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                        pw.Text(order.userName),
                        pw.Text(order.userPhone),
                        pw.SizedBox(width: 150, child: pw.Text(order.userAddress)),
                      ],
                    ),
                    pw.Column(
                      crossAxisAlignment: pw.CrossAxisAlignment.end,
                      children: [
                        pw.Text('Order Date:', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                        pw.Text(DateFormat('dd MMM yyyy, hh:mm a').format(order.date)),
                        pw.SizedBox(height: 10),
                        pw.Text('Payment:', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                        pw.Text('Cash on Delivery'),
                      ],
                    ),
                  ],
                ),
                pw.SizedBox(height: 40),

                pw.TableHelper.fromTextArray(
                  headerDecoration: const pw.BoxDecoration(color: PdfColors.grey300),
                  headerStyle: pw.TextStyle(fontWeight: pw.FontWeight.bold),
                  cellHeight: 30,
                  cellAlignments: {
                    0: pw.Alignment.centerLeft,
                    1: pw.Alignment.center,
                    2: pw.Alignment.centerRight,
                    3: pw.Alignment.centerRight,
                  },
                  headerAlignment: pw.Alignment.centerLeft,
                  data: [
                    ['Product', 'Qty', 'Unit Price', 'Total'],
                    ...order.items.map((item) => [
                      item.product.name,
                      item.quantity.toString(),
                      '$currency${item.product.price.toInt()}',
                      '$currency${(item.product.price * item.quantity).toInt()}',
                    ]),
                  ],
                ),

                pw.SizedBox(height: 30),

                pw.Align(
                  alignment: pw.Alignment.centerRight,
                  child: pw.SizedBox(
                    width: 200,
                    child: pw.Column(
                      children: [
                        pw.Row(
                          mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                          children: [
                            pw.Text('Subtotal:'),
                            pw.Text('$currency${(order.totalAmount - order.deliveryFee).toInt()}'),
                          ],
                        ),
                        pw.Row(
                          mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                          children: [
                            pw.Text('Delivery Fee:'),
                            pw.Text('$currency${order.deliveryFee.toInt()}'),
                          ],
                        ),
                        pw.Divider(),
                        pw.Row(
                          mainAxisAlignment: pw.MainAxisAlignment.spaceBetween,
                          children: [
                            pw.Text('Grand Total:', style: pw.TextStyle(fontWeight: pw.FontWeight.bold, fontSize: 16)),
                            pw.Text('$currency${order.totalAmount.toInt()}', style: pw.TextStyle(fontWeight: pw.FontWeight.bold, fontSize: 16, color: PdfColors.blueAccent)),
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
                      pw.Text('Thank you for shopping with us!', style: pw.TextStyle(fontStyle: pw.FontStyle.italic)),
                      pw.Text('www.smartshop.com'),
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
      name: 'Invoice_${order.id.length > 8 ? order.id.substring(order.id.length - 8) : order.id}.pdf',
    );
  }
}
