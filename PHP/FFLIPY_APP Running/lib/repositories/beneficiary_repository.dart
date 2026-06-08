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
import 'package:fflipy/services/beneficiary_service.dart';

class BeneficiaryRepository {
  final BeneficiaryService _beneficiaryService;

  BeneficiaryRepository(this._beneficiaryService);

  Future<Data> getBeneficiaryInfo({int page = 1}) {
    return _beneficiaryService.getBeneficiaryInfo(page: page);
  }

  Future<AddBeneficiaryResponse> addBeneficiary(AddBeneficiaryRequest request) {
    return _beneficiaryService.addBeneficiary(request);
  }

  Future<UpdateBeneficiaryResponse> updateBeneficiary(
      UpdateBeneficiaryRequest request) {
    return _beneficiaryService.updateBeneficiary(request);
  }

  Future<void> deleteBeneficiary(int id) {
    return _beneficiaryService.deleteBeneficiary(id);
  }

  Future<List<Bank>> getBanks(String countryId) {
    return _beneficiaryService.getBanks(countryId);
  }

  Future<List<Branch>> getBranches(String bankId) {
    return _beneficiaryService.getBranches(bankId);
  }

  Future<List<Facility>> getFacilities(String countryId) {
    return _beneficiaryService.getFacilities(countryId);
  }

  Future<List<WalletProvider>> getWalletProviders(String countryId) {
    return _beneficiaryService.getWalletProviders(countryId);
  }

  Future<List<AccountType>> getAccountTypes() {
    return _beneficiaryService.getAccountTypes();
  }
}