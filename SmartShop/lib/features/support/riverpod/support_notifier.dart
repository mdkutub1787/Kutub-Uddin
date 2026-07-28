import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';
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
    final supabase = ref.read(supabaseClientProvider);
    _subscription = supabase
        .from(AppConstants.supportTicketsTable)
        .stream(primaryKey: ['id'])
        .order('lastUpdate', ascending: false)
        .listen((data) {
      final tickets = data.map((json) => SupportTicket.fromMap(json, json['id'].toString())).toList();
      state = AsyncData(tickets);
    }, onError: (e, st) {
      // Error in stream
    });
  }

  void _loadUserMessages(String userId) {
    _subscription?.cancel();
    final supabase = ref.read(supabaseClientProvider);
    _subscription = supabase
        .from(AppConstants.supportMessagesTable)
        .stream(primaryKey: ['id'])
        .eq('ticket_id', userId) 
        .order('timestamp', ascending: true)
        .listen((data) {
      final messages = data.map((json) => SupportMessage.fromMap(json)).toList();
      state = AsyncData(messages);
    }, onError: (e, st) {
      // Error in stream
    });
  }

  Future<void> sendMessage(String userId, String userName, String message, {bool isAdmin = false, String? ticketId, String? userPhone}) async {
    final actualTicketId = ticketId ?? userId; 
    final supabase = ref.read(supabaseClientProvider);
    
    try {
      await supabase.from(AppConstants.supportMessagesTable).insert({
        'ticket_id': actualTicketId,
        'senderId': userId,
        'message': message,
        'timestamp': DateTime.now().millisecondsSinceEpoch,
        'isAdmin': isAdmin,
      });

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
      await supabase.from(AppConstants.supportTicketsTable).upsert(ticketData);
      
    } catch (e) {
      // Error sending message
    }
  }
}
