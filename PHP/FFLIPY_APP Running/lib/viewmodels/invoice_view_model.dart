import 'package:flutter/material.dart';
import 'package:fflipy/models/invoice/invoice_model.dart';
import 'package:fflipy/repositories/invoice_repository.dart';

class InvoiceViewModel extends ChangeNotifier {
    final InvoiceRepository _invoiceRepository = InvoiceRepository();

    InvoiceModel? _invoiceModel;
    InvoiceModel? get invoiceModel => _invoiceModel;

    bool _isLoading = false;
    bool get isLoading => _isLoading;

    Future<void> getInvoice(String transactionId) async {
        _isLoading = true;
        notifyListeners();

        try {
            _invoiceModel = await _invoiceRepository.getInvoice(transactionId);
        } catch (e) {
            // Handle error
        }

        _isLoading = false;
        notifyListeners();
    }
}
