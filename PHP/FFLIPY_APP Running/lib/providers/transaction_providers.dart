import 'package:fflipy/providers/dio_provider.dart';
import 'package:fflipy/repositories/transaction_repository.dart';
import 'package:fflipy/services/transaction_service.dart';
import 'package:fflipy/viewmodels/transaction_viewmodel.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final transactionServiceProvider = Provider.autoDispose<TransactionService>((ref) {
  final dio = ref.watch(dioProvider);
  return TransactionService(dio);
});

final transactionRepositoryProvider = Provider.autoDispose<TransactionRepository>((ref) {
  final transactionService = ref.watch(transactionServiceProvider);
  return TransactionRepository(transactionService);
});

// ViewModel Provider
final transactionViewModelProvider =
    StateNotifierProvider.autoDispose<TransactionViewModel, TransactionState>((ref) {

  final transactionRepository = ref.watch(transactionRepositoryProvider);
  return TransactionViewModel(transactionRepository);
});
