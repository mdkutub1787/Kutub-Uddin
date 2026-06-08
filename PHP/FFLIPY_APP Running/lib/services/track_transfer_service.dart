
import 'package:dio/dio.dart';
import 'package:fflipy/core/constants/api_config.dart';
import 'package:fflipy/models/track_transfer/track_transfer_model.dart';
import 'package:flutter/foundation.dart';
import '../security_helper/secure_storage_service.dart';

class TrackTransferService {
  final Dio _dio;
  final SecureStorageService _secureStorage = SecureStorageService();

  TrackTransferService(this._dio);

  Future<TrackTransferResponse> trackTransfer(String refNo) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.trackTransferUrl} with ref_no: $refNo');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final response = await _dio.post(
        ApiConfig.trackTransferUrl,
        data: {'ref_no': refNo},
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      return TrackTransferResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      throw Exception(e.response?.data?['message'] ?? 'Failed to track transfer');
    }
  }
}
