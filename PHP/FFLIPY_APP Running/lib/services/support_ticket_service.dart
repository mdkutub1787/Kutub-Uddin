import 'dart:io';

import 'package:dio/dio.dart';
import 'package:http_parser/http_parser.dart';
import 'package:mime/mime.dart';
import 'package:fflipy/core/constants/api_config.dart';
import 'package:fflipy/models/support_ticket_model/support_ticket_model.dart';
import 'package:fflipy/models/support_ticket_model/ticket_details_model.dart';
import 'package:fflipy/models/support_ticket_model/ticket_reply_model.dart';
import 'package:flutter/foundation.dart';
import '../security_helper/secure_storage_service.dart';

class SupportTicketService {
  final Dio _dio;
  final SecureStorageService _secureStorage = SecureStorageService();

  SupportTicketService(this._dio);

  Future<SupportTicketResponse> getSupportTickets({int page = 1}) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.supportTicketListUrl}?page=$page');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final response = await _dio.get(
        ApiConfig.supportTicketListUrl,
        queryParameters: {'page': page},
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      return SupportTicketResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      throw Exception(e.response?.data?['message'] ?? 'Failed to fetch support tickets');
    }
  }

  Future<TicketDetailsResponse> getTicketDetails(String ticketId) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.viewTicketUrl(ticketId)}');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final response = await _dio.get(
        ApiConfig.viewTicketUrl(ticketId),
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      return TicketDetailsResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      throw Exception(e.response?.data?['message'] ?? 'Failed to fetch ticket details');
    }
  }

  Future<void> createSupportTicket({
    required String subject,
    required String message,
    String? attachments,
  }) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.storeTicketUrl}');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final formData = FormData();
      formData.fields.addAll([
        MapEntry('subject', subject),
        MapEntry('message', message),
      ]);

      if (attachments != null && attachments.isNotEmpty) {
        final file = File(attachments);
        if (file.existsSync()) {
          final mimeType = lookupMimeType(attachments) ?? 'application/octet-stream';
          formData.files.add(
            MapEntry(
              'attachments[]',
              await MultipartFile.fromFile(
                attachments,
                filename: attachments.split('/').last,
                contentType: MediaType.parse(mimeType),
              ),
            ),
          );
        }
      }

      final response = await _dio.post(
        ApiConfig.storeTicketUrl,
        data: formData,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
            'Accept': 'application/json',
          },
        ),
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }
      
      if (response.data['success'] == false) {
         throw Exception(response.data['message']);
      }

    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      throw Exception(e.response?.data?['message'] ?? 'Failed to create ticket');
    }
  }
  

  Future<TicketReplyResponse> replySupportTicket({
    required int id,
    required String message,
    String? attachments,
    int replyType = 1, // 1 for reply, 2 for close
  }) async {
       if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.replyTicketUrl(id.toString())}');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Auth token not found');
      }

      final formData = FormData();
      formData.fields.addAll([
        MapEntry('replayTicket', replyType.toString()),
        MapEntry('message', message),
      ]);

      if (attachments != null && attachments.isNotEmpty) {
        final file = File(attachments);
        if (file.existsSync()) {
          final mimeType = lookupMimeType(attachments) ?? 'application/octet-stream';
          formData.files.add(
            MapEntry(
              'attachments[]',
              await MultipartFile.fromFile(
                attachments,
                filename: attachments.split('/').last,
                contentType: MediaType.parse(mimeType),
              ),
            ),
          );
        }
      }

      final response = await _dio.post(
        ApiConfig.replyTicketUrl(id.toString()),
        data: formData,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
            'Accept': 'application/json',
          },
        ),
      );
      
      if (kDebugMode) {
        print('Success: ${response.data}');
      }
       if (response.data['success'] == false) {
         throw Exception(response.data['message']);
      }

      return TicketReplyResponse.fromJson(response.data);

    } on DioException catch (e) {
       if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      String errorMsg = e.response?.data?['message'] ?? 'Failed to reply to ticket';
      if (e.response?.data?['error'] != null) {
         errorMsg += ": ${e.response?.data?['error']}";
      }
      throw Exception(errorMsg);
    }
  }
}
