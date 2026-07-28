import 'package:flutter/foundation.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../constants/constants.dart';

class ErrorLogger {
  static Future<void> logError(dynamic error, StackTrace? stackTrace, {String? hint}) async {
    debugPrint('❌ ERROR LOGGED: $error');
    if (stackTrace != null) debugPrint(stackTrace.toString());

    try {
      final supabase = Supabase.instance.client;
      await supabase.from('error_logs').insert({
        'error': error.toString(),
        'stack_trace': stackTrace?.toString() ?? 'No stack trace',
        'hint': hint ?? 'General Error',
        'device_info': kIsWeb ? 'Web' : 'Mobile/Desktop',
        'timestamp': DateTime.now().toIso8601String(),
      });
    } catch (e) {
      // Avoid infinite loop if logging fails
      debugPrint('Failed to log error to Supabase: $e');
    }
  }
}
