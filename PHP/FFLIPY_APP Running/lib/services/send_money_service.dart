import 'package:dio/dio.dart';
import 'package:fflipy/models/beneficiary/beneficiary_list_response.dart';
import 'package:flutter/foundation.dart';
import 'package:fflipy/models/send_money/send_money_cal_service_crg.dart';
import 'package:fflipy/models/send_money/send_money_payment_details.dart';
import 'package:fflipy/models/send_money/send_money_selected_beneficiary.dart';
import 'package:fflipy/models/send_money/send_money_step2_store.dart';
import 'package:fflipy/models/send_money/send_money_step3_store.dart';
import 'package:fflipy/models/send_money/send_money_otp_generate.dart';
import 'package:fflipy/models/send_money/send_money_otp_resend.dart';
import 'package:fflipy/models/send_money/send_money_verify_otp.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../core/constants/api_config.dart';
import '../core/constants/app_constants.dart';
import '../../security_helper/EncryptionHelper.dart';

class OtpRateLimitException implements Exception {
  final int retryAfter;
  final String message;

  OtpRateLimitException({required this.retryAfter, required this.message});
}

class SendMoneyService {
  final Dio _dio;
  final FlutterSecureStorage _secureStorage;

  static const bool _isEncryptionEnabled = true;

  SendMoneyService(this._dio, this._secureStorage) {
    _dio.options.baseUrl = ApiConfig.baseUrl;
    if (kDebugMode) {
      _dio.interceptors.add(LogInterceptor(
        requestHeader: true,
        requestBody: true,
        responseBody: true,
        responseHeader: false,
        error: true,
        logPrint: (object) => print(object.toString()),
      ));
    }
  }

  Future<Options> _getAuthOptions({bool isFormData = false}) async {
    final token = await _secureStorage.read(key: AppConstants.userTokenKey);
    if (token == null) throw Exception('Auth token not found!');

    return Options(
      headers: {
        'Authorization': 'Bearer $token',
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
    );
  }

  Future<Response> _submitRequest({
    required String url,
    required Map<String, dynamic> data,
    bool useFormData = false,
  }) async {
    if (_isEncryptionEnabled) {
      try {
        if (kDebugMode) {
          print("\n🔹 [ENCRYPTION] Encrypting payload for: $url");
          print("📝 Raw Data: $data");
        }

        final encryptedData = await EncryptionHelper.encryptData(data);

        final body = {"encrypted_data": encryptedData};

        if (kDebugMode) {
          print("🔒 Payload sent to server: $body");
        }

        return await _dio.post(
          url,
          data: body,
          options: await _getAuthOptions(isFormData: false),
        );
      } on DioException {
        rethrow;
      }
    }

    else {
      try {
        if (kDebugMode) print("📤 Sending Plain (${useFormData ? 'Form' : 'JSON'}) to $url");

        if (useFormData) {
          final formData = FormData.fromMap(data);
          final token = await _secureStorage.read(key: AppConstants.userTokenKey);
          final options = Options(headers: {
            'Authorization': 'Bearer $token',
            'Accept': 'application/json',
          });

          return await _dio.post(
            url,
            data: formData,
            options: options,
          );
        } else {
          return await _dio.post(
            url,
            data: data,
            options: await _getAuthOptions(isFormData: false),
          );
        }
      } on DioException {
        rethrow;
      }
    }
  }

  Future<BeneficiaryListResponse> getBeneficiaries({int page = 1}) async {
    try {
      final response = await _dio.get(
          ApiConfig.getBeneficiariesUrl,
          queryParameters: {'page': page},
          options: await _getAuthOptions()
      );
      return BeneficiaryListResponse.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }

  Future<SelectSendMoneyBeneficiaryResponse> selectBeneficiary(int beneficiaryId) async {
    try {
      final response = await _submitRequest(
        url: ApiConfig.selectSendMoneyBeneficiaryUrl,
        data: {'beneficiary_id': beneficiaryId},
        useFormData: true,
      );
      return SelectSendMoneyBeneficiaryResponse.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }

  Future<SendMoneyPaymentDetailsResponse> getPaymentDetails(String sessionToken) async {
    try {
      final response = await _submitRequest(
        url: ApiConfig.sendMoneyPaymentUrl,
        data: {'session_token': sessionToken},
        useFormData: true,
      );
      return SendMoneyPaymentDetailsResponse.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }

  Future<SendMoneyCalServiceCrgResponse> calculateServiceCharge({
    required String sessionToken,
    required int beneficiaryId,
    required double amount,
    required int fromCountryId,
    required int toCountryId,
  }) async {
    try {
      final response = await _submitRequest(
        url: ApiConfig.sendMoneyCalServiceCrgUrl,
        data: {
          'session_token': sessionToken,
          'beneficiary_id': beneficiaryId.toString(),
          'send_amount': amount.toString(),
          'sender_currency_id': fromCountryId,
          'receiver_currency_id': toCountryId,
        },
        useFormData: true,
      );
      return SendMoneyCalServiceCrgResponse.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }

  Future<SendMoneyStep2StoreResponse> storeStep2Details({
    required String sessionToken,
    required int beneficiaryId,
    required double sendAmount,
    required int senderCurrencyId,
    required int receiverCurrencyId,
    required double fee,
    required double exchangeRate,
    required double receivedAmount,
  }) async {
    try {
      final response = await _submitRequest(
        url: ApiConfig.sendMoneyStep2StoreUrl,
        data: {
          'session_token': sessionToken,
          'beneficiary_id': beneficiaryId,
          'send_amount': sendAmount,
          'sender_currency_id': senderCurrencyId,
          'receiver_currency_id': receiverCurrencyId,
          'calculated_data': {
            'fee': fee,
            'exchange_rate': exchangeRate,
            'received_amount': receivedAmount,
          }
        },
        useFormData: false,
      );
      return SendMoneyStep2StoreResponse.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }

  Future<SendMoneyStep3StoreResponse> storeStep3Details({required String transactionToken}) async {
    try {
      final response = await _submitRequest(
        url: ApiConfig.sendMoneyStep3StoreUrl,
        data: {'transaction_token': transactionToken},
        useFormData: false,
      );
      return SendMoneyStep3StoreResponse.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }

  Future<SendMoneyOtpGenerateResponse> generateOtp({required String transactionToken}) async {
    try {
      final response = await _submitRequest(
        url: ApiConfig.sendMoneyOtpGenerateUrl,
        data: {'transaction_token': transactionToken},
        useFormData: false,
      );
      return SendMoneyOtpGenerateResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (e.response?.statusCode == 429) {
        final retryAfter = e.response?.data?['retry_after'] ?? 60;
        throw OtpRateLimitException(
            retryAfter: retryAfter,
            message: e.response?.data?['message'] ?? 'Too many requests'
        );
      }
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }

  Future<SendMoneyVerifyOtpResponse> verifyOtp({
    required String transactionToken,
    required String otp,
    required int purposeOfTransfer,
    required String remarks,
  }) async {
    try {
      final response = await _submitRequest(
        url: ApiConfig.sendMoneyVerifyOtpUrl,
        data: {
          'transaction_token': transactionToken,
          'otp': otp,
          'purpose_of_transfer': purposeOfTransfer.toString(),
          'remarks': remarks,
        },
        useFormData: true,
      );
      return SendMoneyVerifyOtpResponse.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }

  Future<SendMoneyOtpResendResponse> resendOtp({required String transactionToken}) async {
    try {
      final response = await _submitRequest(
        url: ApiConfig.sendMoneyOtpResendUrl,
        data: {'transaction_token': transactionToken},
        useFormData: false,
      );
      return SendMoneyOtpResendResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (e.response?.statusCode == 429) {
        final retryAfter = e.response?.data?['retry_after'] ?? 60;
        throw OtpRateLimitException(
            retryAfter: retryAfter,
            message: e.response?.data?['message'] ?? 'Too many requests'
        );
      }
      throw Exception(e.response?.data?['message'] ?? e.message);
    }
  }
}
