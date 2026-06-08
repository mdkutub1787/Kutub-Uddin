import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/models/transaction_model/transaction_report_model.dart';
import 'package:fflipy/repositories/transaction_repository.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class TransactionState {
  final bool isLoading;
  final String? error;
  final TransactionReportResponse? transactionReport;
  final String? search;
  final String? startDate;
  final String? endDate;
  final String? status;
  final bool isLoadingMore;

  TransactionState({
    this.isLoading = false,
    this.error,
    this.transactionReport,
    this.search,
    this.startDate,
    this.endDate,
    this.status,
    this.isLoadingMore = false,
  });

  TransactionState copyWith({
    bool? isLoading,
    String? error,
    TransactionReportResponse? transactionReport,
    String? search,
    String? startDate,
    String? endDate,
    String? status,
    bool? isLoadingMore,
    bool clearError = false,
    bool clearSearch = false,
    bool clearStartDate = false,
    bool clearEndDate = false,
    bool clearStatus = false,
  }) {
    return TransactionState(
      isLoading: isLoading ?? this.isLoading,
      error: clearError ? null : error ?? this.error,
      transactionReport: transactionReport ?? this.transactionReport,
      search: clearSearch ? null : search ?? this.search,
      startDate: clearStartDate ? null : startDate ?? this.startDate,
      endDate: clearEndDate ? null : endDate ?? this.endDate,
      status: clearStatus ? null : status ?? this.status,
      isLoadingMore: isLoadingMore ?? this.isLoadingMore,
    );
  }
}

class TransactionViewModel extends StateNotifier<TransactionState> {
  final TransactionRepository _transactionRepository;
  bool _isFetching = false;

  TransactionViewModel(this._transactionRepository) : super(TransactionState());

  Future<void> getTransactionReport({int page = 1, bool showLoading = true}) async {
    if (_isFetching) return;

    _isFetching = true;

    try {
      if (page == 1 && showLoading) {
        state = state.copyWith(isLoading: true, clearError: true);
      } else if (page > 1) {
        state = state.copyWith(isLoadingMore: true);
      }

      final result = await _transactionRepository.getTransactionReport(
        page: page,
        search: state.search,
        startDate: state.startDate,
        endDate: state.endDate,
        status: state.status,
      );

      if (!mounted) return;

      final newTransactions = result.data.transactions.data;
      final currentReport = state.transactionReport;

      if (page > 1 && currentReport != null) {
        final currentTransactions = currentReport.data.transactions.data;
        final Set<int> existingIds = currentTransactions.map((t) => t.id).toSet();
        final uniqueNewTransactions = newTransactions.where((t) => !existingIds.contains(t.id)).toList();
        currentTransactions.addAll(uniqueNewTransactions);

        final updatedPagination = currentReport.data.transactions.copyWith(
          currentPage: result.data.transactions.currentPage,
          data: currentTransactions,
          lastPage: result.data.transactions.lastPage, 
        );
        
        final updatedData = currentReport.data.copyWith(
          transactions: updatedPagination
        );

        state = state.copyWith(
          transactionReport: currentReport.copyWith(
            data: updatedData,
          ),
        );
      } else {
        state = state.copyWith(transactionReport: result);
      }
    } catch (e) {
      if (!mounted) return;
      state = state.copyWith(error: ErrorHandler.getErrorMessage(e));
    } finally {
      if (!mounted) return;
      state = state.copyWith(isLoading: false, isLoadingMore: false);
      _isFetching = false;
    }
  }

  Future<void> fetchAllTransactionPages() async {
    if (_isFetching) return;
    _isFetching = true;
    if (!mounted) return;
    state = state.copyWith(isLoading: true, clearError: true);

    try {
      TransactionReportResponse? finalReport;
      int currentPage = 1;
      int lastPage = 1;

      do {
        final result = await _transactionRepository.getTransactionReport(
          page: currentPage,
          search: state.search,
          startDate: state.startDate,
          endDate: state.endDate,
          status: state.status,
        );

        if (!mounted) return;

        if (finalReport == null) {
          finalReport = result;
        } else {
          finalReport.data.transactions.data.addAll(result.data.transactions.data);
        }

        lastPage = result.data.transactions.lastPage;
        currentPage++;

      } while (currentPage <= lastPage);

      state = state.copyWith(transactionReport: finalReport);

    } catch (e) {
      if (!mounted) return;
      state = state.copyWith(error: ErrorHandler.getErrorMessage(e));
    } finally {
      if (!mounted) return;
      state = state.copyWith(isLoading: false);
      _isFetching = false;
    }
  }


  void updateFilters({
    String? search,
    String? startDate,
    String? endDate,
    String? status,
    bool fetchAll = false,
  }) {
    state = state.copyWith(
      search: search,
      startDate: startDate,
      endDate: endDate,
      status: status,
      clearSearch: search == null && state.search != null,
      clearStartDate: startDate == null && state.startDate != null,
      clearEndDate: endDate == null && state.endDate != null,
      clearStatus: status == null && state.status != null,
    );
     if (fetchAll) {
      fetchAllTransactionPages();
    } else {
      getTransactionReport(page: 1);
    }
  }

  Future<void> clearFilters({bool fetchAll = false}) async {
    state = state.copyWith(
      clearSearch: true,
      clearStartDate: true,
      clearEndDate: true,
      clearStatus: true,
    );
    if (fetchAll) {
      await fetchAllTransactionPages();
    } else {
      await getTransactionReport(page: 1);
    }
  }

    Future<String> cancelTransaction(int transactionId) async {
    try {
      final message =
          await _transactionRepository.cancelTransaction(transactionId);
      return message;
    } catch (e) {
      rethrow;
    }
  }
}
