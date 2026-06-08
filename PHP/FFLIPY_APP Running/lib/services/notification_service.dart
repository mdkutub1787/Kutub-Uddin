import 'package:dio/dio.dart';
import 'package:fflipy/core/constants/api_config.dart';
import 'package:fflipy/models/notification_model/notification_model.dart';
import 'package:flutter/foundation.dart';
import '../security_helper/secure_storage_service.dart';

class NotificationService {
  final Dio _dio;
  final SecureStorageService _secureStorage = SecureStorageService();

  NotificationService(this._dio);

  Future<NotificationResponse> getNotifications() async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.notificationUrl}');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final response = await _dio.get(
        ApiConfig.notificationUrl,
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      return NotificationResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      throw Exception(e.response?.data?['message'] ?? 'Failed to fetch notifications');
    }
  }

  Future<void> markAsRead(int id) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.notificationReadUrl(id)}');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final response = await _dio.get(
        ApiConfig.notificationReadUrl(id),
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      if (response.data['success'] == false && response.data['message'] != 'Notification delete successfully') {
         throw Exception(response.data['message']);
      }

    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      throw Exception(e.response?.data?['message'] ?? 'Failed to mark notification as read');
    }
  }
}
