import 'package:fflipy/models/beneficiary/account_type_model.dart';
import 'package:fflipy/models/beneficiary/add_beneficiary_request.dart';
import 'package:fflipy/models/beneficiary/add_beneficiary_response.dart';
import 'package:fflipy/models/beneficiary/bank_model.dart';
import 'package:fflipy/models/beneficiary/beneficiary_list_response.dart';
import 'package:fflipy/models/beneficiary/branch_model.dart';
import 'package:fflipy/models/beneficiary/facility_model.dart';
import 'package:fflipy/models/beneficiary/update_beneficiary_request.dart';
import 'package:fflipy/models/beneficiary/update_beneficiary_response.dart';
import 'package:fflipy/models/beneficiary/wallet_provider_model.dart';
import 'package:fflipy/repositories/beneficiary_repository.dart';
import 'package:fflipy/services/beneficiary_service.dart';
import 'package:fflipy/viewmodels/beneficiary_viewmodel.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import 'dio_provider.dart';

// Service Provider
final beneficiaryServiceProvider =
    Provider.autoDispose<BeneficiaryService>((ref) {
  final dio = ref.watch(dioProvider);
  const secureStorage = FlutterSecureStorage();
  return BeneficiaryService(dio, secureStorage);
});

// Repository Provider
final beneficiaryRepositoryProvider =
    Provider.autoDispose<BeneficiaryRepository>((ref) {
  final beneficiaryService = ref.watch(beneficiaryServiceProvider);
  return BeneficiaryRepository(beneficiaryService);
});

// ViewModel Provider
final beneficiaryViewModelProvider =
    StateNotifierProvider.autoDispose<BeneficiaryViewModel, AsyncValue<Data>>(
        (ref) {
  final beneficiaryRepository = ref.watch(beneficiaryRepositoryProvider);
  return BeneficiaryViewModel(beneficiaryRepository);
});

// UI-specific provider
final searchQueryProvider = StateProvider.autoDispose<String>((ref) => '');

final addBeneficiaryProvider = FutureProvider.family
    .autoDispose<AddBeneficiaryResponse, AddBeneficiaryRequest>(
        (ref, request) async {
  return await ref
      .read(beneficiaryViewModelProvider.notifier)
      .addBeneficiary(request);
});

final updateBeneficiaryProvider = FutureProvider.family
    .autoDispose<UpdateBeneficiaryResponse, UpdateBeneficiaryRequest>(
        (ref, request) async {
  return await ref
      .read(beneficiaryViewModelProvider.notifier)
      .updateBeneficiary(request);
});

final deleteBeneficiaryProvider =
    FutureProvider.family.autoDispose<void, int>((ref, beneficiaryId) async {
  await ref
      .read(beneficiaryViewModelProvider.notifier)
      .deleteBeneficiary(beneficiaryId);
});

final banksProvider =
FutureProvider.family.autoDispose<List<Bank>, String>((ref, countryId) {
  return ref.watch(beneficiaryRepositoryProvider).getBanks(countryId);
});

final branchesProvider =
FutureProvider.family.autoDispose<List<Branch>, String>((ref, bankId) {
  return ref.watch(beneficiaryRepositoryProvider).getBranches(bankId);
});

final facilitiesProvider =
FutureProvider.family.autoDispose<List<Facility>, String>((ref, countryId) {
  return ref.watch(beneficiaryRepositoryProvider).getFacilities(countryId);
});

final walletProvidersProvider =
FutureProvider.family.autoDispose<List<WalletProvider>, String>(
        (ref, countryId) {
      return ref.watch(beneficiaryRepositoryProvider).getWalletProviders(countryId);
    });

final accountTypesProvider = FutureProvider.autoDispose<List<AccountType>>((ref) {
  return ref.watch(beneficiaryRepositoryProvider).getAccountTypes();
});
