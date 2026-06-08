import 'package:fflipy/models/transaction_model/transaction_report_model.dart';
import 'package:fflipy/services/transaction_service.dart';

class TransactionRepository {
  final TransactionService _transactionService;

  TransactionRepository(this._transactionService);

  Future<TransactionReportResponse> getTransactionReport({
    int page = 1,
    String? search,
    String? startDate,
    String? endDate,
    String? status,
  }) async {
    return await _transactionService.getTransactionReport(
      page: page,
      search: search,
      startDate: startDate,
      endDate: endDate,
      status: status,
    );
  }

  Future<String> cancelTransaction(int transactionId) async {
    return await _transactionService.cancelTransaction(transactionId);
  }
}
