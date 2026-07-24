import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final supportNotifierProvider = AsyncNotifierProvider<SupportNotifier, List<dynamic>>(() {
  return SupportNotifier();
});

class SupportNotifier extends AsyncNotifier<List<dynamic>> {
  @override
  FutureOr<List<dynamic>> build() async {
    return [];
  }

  Future<void> loadTickets() async {
    state = const AsyncLoading();
    try {
      // Fetch from Supabase
      state = const AsyncData([]);
    } catch (e, st) {
      state = AsyncError(e, st);
    }
  }

  Future<void> sendMessage(String userId, String userName, String message, {bool isAdmin = false, String? ticketId, String? userPhone}) async {
    // Send message to Supabase
  }
}
