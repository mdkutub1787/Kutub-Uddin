
import 'dart:io';
import 'package:dio/dio.dart';

class ErrorHandler {
  /// Returns a translation key for the given error.
  /// Use with context.tr() to get the localized message.
  static String getErrorMessage(dynamic error) {
    if (error is DioException) {
      if (error.type == DioExceptionType.badResponse) {
        if (error.response?.data != null && error.response!.data is Map) {
          final data = error.response!.data as Map;
          
          String? message = data['message']?.toString();
          String? detail = data['error']?.toString();
          
          if (message != null && message.isNotEmpty) {
            if (detail != null && detail.isNotEmpty) {
              return "$message: $detail";
            }
            return message;
          }

          if (data['errors'] != null && data['errors'] is Map) {
            final errors = data['errors'] as Map;
            if (errors.isNotEmpty) {
              final firstErrorValue = errors.values.first;
              if (firstErrorValue is List && firstErrorValue.isNotEmpty) {
                return firstErrorValue.first.toString();
              } else {
                return firstErrorValue.toString();
              }
            }
          }
        }
        return 'error.server_error';
      }

      switch (error.type) {
        case DioExceptionType.connectionTimeout:
        case DioExceptionType.sendTimeout:
        case DioExceptionType.receiveTimeout:
          return 'error.connection_timeout';
        case DioExceptionType.cancel:
          return 'error.request_cancelled';
        case DioExceptionType.connectionError:
          return 'error.no_internet';
        case DioExceptionType.unknown:
          if (error.error is SocketException) {
            return 'error.no_internet';
          }
          return getUserFriendlyMessage(error);
        default:
          return getUserFriendlyMessage(error);
      }
    }
    
    return getUserFriendlyMessage(error);
  }

  /// Converts any raw error/exception string into a user-friendly message.
  /// This is the universal sanitizer — call from any screen or viewmodel.
  static String getUserFriendlyMessage(dynamic error) {
    final raw = error.toString();

    // Network / connectivity errors
    if (_isNetworkError(raw)) {
      return 'error.no_internet';
    }

    // Timeout errors
    if (_isTimeoutError(raw)) {
      return 'error.connection_timeout';
    }

    // Server errors
    if (raw.contains('500') || raw.contains('Internal Server Error')) {
      return 'error.server_error';
    }

    // Session / auth errors
    if (raw.contains('Encryption key not found') ||
        raw.contains('Unauthenticated') ||
        raw.contains('401')) {
      return 'error.session_expired';
    }

    // Clean up the message
    return _sanitizeMessage(raw);
  }

  /// Checks if the raw error string is a network-related error.
  static bool _isNetworkError(String raw) {
    return raw.contains('SocketException') ||
        raw.contains('Connection refused') ||
        raw.contains('Network is unreachable') ||
        raw.contains('Failed host lookup') ||
        raw.contains('No route to host') ||
        raw.contains('No address associated') ||
        raw.contains('connection error') ||
        raw.contains('DioException [connection error]') ||
        raw.contains('HandshakeException') ||
        raw.contains('Connection reset by peer') ||
        raw.contains('Software caused connection abort') ||
        raw.contains('Connection closed');
  }

  /// Checks if the raw error string is a timeout-related error.
  static bool _isTimeoutError(String raw) {
    return raw.contains('TimeoutException') ||
        raw.contains('Connection timed out') ||
        raw.contains('connectionTimeout') ||
        raw.contains('sendTimeout') ||
        raw.contains('receiveTimeout');
  }

  /// Strips raw exception prefixes and returns a clean message.
  static String _sanitizeMessage(String raw) {
    return raw
        .replaceFirst(RegExp(r'^Exception:\s*'), '')
        .replaceFirst(RegExp(r'^FormatException:\s*'), '')
        .replaceFirst(RegExp(r'^Error:\s*'), '')
        .replaceFirst(RegExp(r'^DioException.*?:\s*'), '')
        .replaceFirst(RegExp(r'^SocketException.*?:\s*'), '')
        .trim();
  }
}
