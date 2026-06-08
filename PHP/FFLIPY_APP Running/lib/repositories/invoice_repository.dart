import 'package:fflipy/models/invoice/invoice_model.dart';
import 'package:fflipy/services/invoice_service.dart';

class InvoiceRepository {
    final InvoiceService _invoiceService = InvoiceService();

    Future<InvoiceModel> getInvoice(String transactionId) async {
        return await _invoiceService.getInvoice(transactionId);
    }
}
