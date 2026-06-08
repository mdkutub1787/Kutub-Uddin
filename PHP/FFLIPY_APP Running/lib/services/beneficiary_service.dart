import 'package:dio/dio.dart';
import 'package:fflipy/core/constants/api_config.dart';
import 'package:fflipy/core/constants/app_constants.dart';
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
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class BeneficiaryService {
  final Dio _dio;
  final FlutterSecureStorage _secureStorage;

  BeneficiaryService(this._dio, this._secureStorage);

  Future<Options> _getAuthOptions() async {
    final token = await _secureStorage.read(key: AppConstants.userTokenKey);
    if (token == null) {
      throw Exception('Auth token not found!');
    }
    return Options(headers: {'Authorization': 'Bearer $token'});
  }

  Future<Data> getBeneficiaryInfo({int page = 1}) async {
    try {
      final response = await _dio.get(
        ApiConfig.getBeneficiariesUrl,
        queryParameters: {'page': page},
        options: await _getAuthOptions(),
      );
      final beneficiaryResponse = BeneficiaryListResponse.fromJson(response.data);
      return beneficiaryResponse.data;
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to load beneficiary info: ${e.message}');
    }
  }

  Future<AddBeneficiaryResponse> addBeneficiary(AddBeneficiaryRequest request) async {
    try {
      final Map<String, dynamic> jsonMap = request.toJson();
      jsonMap.remove('beneficiary_id');
      final formData = FormData.fromMap(jsonMap.map((k, v) => MapEntry(k, v.toString())));

      final response = await _dio.post(
        ApiConfig.addBeneficiaryUrl,
        data: formData,
        options: await _getAuthOptions(),
      );
      return AddBeneficiaryResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to add beneficiary: ${e.message}');
    }
  }

  Future<UpdateBeneficiaryResponse> updateBeneficiary(UpdateBeneficiaryRequest request) async {
    try {
      final Map<String, dynamic> jsonMap = request.toJson();
      jsonMap.remove('id');
      jsonMap.removeWhere((key, value) => value == null || (value is String && value.isEmpty));

      final response = await _dio.post(
        ApiConfig.updateBeneficiaryUrl(request.id!),
        data: jsonMap,
        options: await _getAuthOptions(),
      );
      return UpdateBeneficiaryResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to update beneficiary: ${e.message}');
    }
  }

  Future<void> deleteBeneficiary(int id) async {
    try {
      await _dio.delete(ApiConfig.deleteBeneficiaryUrl(id), options: await _getAuthOptions());
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to delete beneficiary: ${e.message}');
    }
  }

  Future<List<Bank>> getBanks(String countryId) async {
    try {
      final response = await _dio.get(ApiConfig.banksUrl(countryId), options: await _getAuthOptions());
      final bankResponse = BankResponse.fromJson(response.data);
      return bankResponse.data;
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to load banks: ${e.message}');
    }
  }

  Future<List<Branch>> getBranches(String bankId) async {
    try {
      final response = await _dio.get(ApiConfig.branchesUrl(bankId), options: await _getAuthOptions());
      final branchResponse = BranchResponse.fromJson(response.data);
      return branchResponse.data;
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to load branches: ${e.message}');
    }
  }

  Future<List<Facility>> getFacilities(String countryId) async {
    try {
      final response = await _dio.get(ApiConfig.facilitiesUrl(countryId), options: await _getAuthOptions());
      final facilityResponse = FacilityResponse.fromJson(response.data);
      return facilityResponse.data;
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to load facilities: ${e.message}');
    }
  }

  Future<List<WalletProvider>> getWalletProviders(String countryId) async {
    try {
      final response = await _dio.get(ApiConfig.walletProvidersUrl(countryId), options: await _getAuthOptions());
      final walletResponse = WalletProviderResponse.fromJson(response.data);
      return walletResponse.data;
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to load wallet providers: ${e.message}');
    }
  }

  Future<List<AccountType>> getAccountTypes() async {
    try {
      final response = await _dio.get(ApiConfig.accountTypesUrl, options: await _getAuthOptions());
      final accountTypeResponse = AccountTypeResponse.fromJson(response.data);
      return accountTypeResponse.data;
    } on DioException catch (e) {
      if (e.response?.data != null && e.response?.data is Map<String, dynamic> && e.response!.data['message'] is String) {
        throw Exception(e.response!.data['message']);
      }
      throw Exception('Failed to load account types: ${e.message}');
    }
  }
}
