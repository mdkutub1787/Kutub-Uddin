import 'package:flutter/foundation.dart';
import 'package:supabase_flutter/supabase_flutter.dart';

class ErrorLogger {
  static Future<void> logError(dynamic error, StackTrace? stackTrace, {String? hint}) async {
    debugPrint('❌ ERROR LOGGED: $error');
    if (stackTrace != null) debugPrint(stackTrace.toString());

    try {
      final supabase = Supabase.instance.client;
      final userId = supabase.auth.currentUser?.id;
      
      await supabase.from('error_logs').insert({
        'exception': error.toString(),
        'stack_trace': stackTrace?.toString() ?? 'No stack trace',
        'hint': hint ?? 'General Error',
        'device_info': kIsWeb ? 'Web' : 'Mobile Device',
        'user_id': userId,
        'timestamp': DateTime.now().toIso8601String(),
      });
    } catch (e) {
      debugPrint('Failed to log error to Supabase: $e');
    }
  }
}
