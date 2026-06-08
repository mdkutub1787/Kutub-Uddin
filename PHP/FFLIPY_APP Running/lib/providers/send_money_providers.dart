
import 'package:fflipy/models/send_money/send_money_payment_details.dart';
import 'package:fflipy/repositories/send_money_repository.dart';
import 'package:fflipy/services/send_money_service.dart';
import 'package:fflipy/viewmodels/send_money_view_model.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import 'dio_provider.dart';

final sendMoneyServiceProvider = Provider.autoDispose<SendMoneyService>((ref) {
  final dio = ref.watch(dioProvider);
  const secureStorage = FlutterSecureStorage();
  return SendMoneyService(dio, secureStorage);
});

final sendMoneyRepositoryProvider = Provider.autoDispose<SendMoneyRepository>(
    (ref) => SendMoneyRepository(ref.watch(sendMoneyServiceProvider)));

final sendMoneyViewModelProvider =
    StateNotifierProvider.autoDispose<SendMoneyViewModel, SendMoneyState>((ref) {
  return SendMoneyViewModel(ref.watch(sendMoneyRepositoryProvider));
});

final amountProvider = StateProvider.autoDispose<double>((ref) => 0.0);
final selectedFromCountryProvider =
    StateProvider.autoDispose<CountryDetails?>((ref) => null);
final selectedToCountryProvider =
    StateProvider.autoDispose<CountryDetails?>((ref) => null);
final selectedPurposeIdProvider = StateProvider.autoDispose<int?>((ref) => null);
final remarksProvider = StateProvider.autoDispose<String>((ref) => '');
final confirmDetailsProvider = StateProvider.autoDispose<bool>((ref) => false);
