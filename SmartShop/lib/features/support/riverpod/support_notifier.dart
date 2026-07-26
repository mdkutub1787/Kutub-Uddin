import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../models/support_ticket_model.dart';
import '../../auth/riverpod/auth_notifier.dart';

final supportNotifierProvider = AsyncNotifierProvider<SupportNotifier, List<dynamic>>(() {
  return SupportNotifier();
});

class SupportNotifier extends AsyncNotifier<List<dynamic>> {
  StreamSubscription? _subscription;

  @override
  FutureOr<List<dynamic>> build() async {
    final authState = ref.watch(authNotifierProvider);
    final user = authState.value;
    if (user == null) return [];

    ref.onDispose(() {
      _subscription?.cancel();
    });

    final isAdmin = user.role == 'admin' || user.role == 'super_admin';
    if (isAdmin) {
      _loadAdminTickets();
    } else {
      _loadUserMessages(user.uid);
    }
    return [];
  }

  void _loadAdminTickets() {
    _subscription?.cancel();
    _subscription = Supabase.instance.client
        .from('support_tickets')
        .stream(primaryKey: ['id'])
        .order('lastUpdate', ascending: false)
        .listen((data) {
      final tickets = data.map((json) => SupportTicket.fromMap(json, json['id'])).toList();
      state = AsyncData(tickets);
    }, onError: (e, st) {
      // state = AsyncError(e, st);
    });
  }

  void _loadUserMessages(String userId) {
    _subscription?.cancel();
    _subscription = Supabase.instance.client
        .from('support_messages')
        .stream(primaryKey: ['id'])
        .eq('ticket_id', userId) // Using userId as ticketId for 1-on-1 support
        .order('timestamp', ascending: true)
        .listen((data) {
      final messages = data.map((json) => SupportMessage.fromMap(json)).toList();
      state = AsyncData(messages);
    }, onError: (e, st) {
      // state = AsyncError(e, st);
    });
  }

  Future<void> sendMessage(String userId, String userName, String message, {bool isAdmin = false, String? ticketId, String? userPhone}) async {
    final actualTicketId = ticketId ?? userId; // Using user's ID as the unique ticket ID between them and admin
    
    try {
      // 1. Insert message
      await Supabase.instance.client.from('support_messages').insert({
        'ticket_id': actualTicketId,
        'senderId': userId,
        'message': message,
        'timestamp': DateTime.now().millisecondsSinceEpoch,
        'isAdmin': isAdmin,
      });

      // 2. Upsert ticket
      final ticketData = {
        'id': actualTicketId,
        'userId': actualTicketId,
        'userName': userName,
        'userPhone': userPhone ?? '',
        'lastMessage': message,
        'lastUpdate': DateTime.now().millisecondsSinceEpoch,
        'status': 'open',
        'adminRead': isAdmin,
      };
      await Supabase.instance.client.from('support_tickets').upsert(ticketData);
      
    } catch (e) {
      print('Error sending support message: $e');
    }
  }
}
