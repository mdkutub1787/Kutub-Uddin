import 'package:dio/dio.dart';
import 'package:fflipy/core/constants/api_config.dart';
import 'package:fflipy/models/transaction_model/transaction_report_model.dart';
import 'package:flutter/foundation.dart';
import '../security_helper/secure_storage_service.dart';

class TransactionService {
  final Dio _dio;
  final SecureStorageService _secureStorage = SecureStorageService();

  TransactionService(this._dio);

  Future<TransactionReportResponse> getTransactionReport({
    int page = 1,
    String? search,
    String? startDate,
    String? endDate,
    String? status,
  }) async {
    if (kDebugMode) {
      print(
          'Requesting: ${ApiConfig.baseUrl}${ApiConfig.transactionReportUrl}?page=$page&search=$search&start_date=$startDate&end_date=$endDate&status=$status');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final queryParameters = <String, dynamic>{'page': page};
      if (search != null && search.isNotEmpty) queryParameters['search'] = search;
      if (startDate != null && startDate.isNotEmpty) queryParameters['start_date'] = startDate;
      if (endDate != null && endDate.isNotEmpty) queryParameters['end_date'] = endDate;
      if (status != null && status.isNotEmpty) queryParameters['status'] = status;


      final response = await _dio.get(
        ApiConfig.transactionReportUrl,
        queryParameters: queryParameters,
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      return TransactionReportResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      throw Exception(e.response?.data?['message'] ?? 'Failed to fetch transaction report');
    }
  }

  Future<String> cancelTransaction(int transactionId) async {
    if (kDebugMode) {
      print('Cancelling transaction: ${ApiConfig.baseUrl}${ApiConfig.cancelTransactionUrl(transactionId)}');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final response = await _dio.post(
        ApiConfig.cancelTransactionUrl(transactionId),
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      if (response.data is Map<String, dynamic>) {
        final responseData = response.data as Map<String, dynamic>;
        final message = responseData['message'] as String?;
        final status = responseData['status'] as bool?;

        if (status == true) {
          return message ?? '';
        } else {
          throw Exception(message);
        }
      }
      throw Exception('An unexpected response was received.');
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      if (e.response?.data is Map<String, dynamic>) {
        final responseData = e.response!.data as Map<String, dynamic>;
        throw Exception(responseData['message']);
      }
      throw Exception('A network error occurred.');
    }
  }
}
