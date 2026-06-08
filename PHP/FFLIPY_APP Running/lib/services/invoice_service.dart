import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';

import '../core/constants/api_config.dart';
import '../models/invoice/invoice_model.dart';
import '../security_helper/secure_storage_service.dart';

class InvoiceService {
  final Dio _dio = Dio(BaseOptions(baseUrl: ApiConfig.baseUrl));
  final SecureStorageService _secureStorage = SecureStorageService();

  Future<InvoiceModel> getInvoice(String transactionId) async {
    final url = ApiConfig.getInvoiceUrl(transactionId);
    if (kDebugMode) {
      print('Requesting GET: $url');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final response = await _dio.get(
        url,
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      return InvoiceModel.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      throw Exception(e.response?.data?['message'] ?? 'Failed to get invoice');
    }
  }
}
