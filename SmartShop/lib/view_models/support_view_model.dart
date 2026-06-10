import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/support_ticket_model.dart';

class SupportViewModel extends ChangeNotifier {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref();
  List<SupportTicket> _tickets = [];
  List<SupportMessage> _messages = [];
  bool _isLoading = false;
  int _lastReadTimestamp = 0;

  List<SupportTicket> get tickets => _tickets;
  List<SupportMessage> get messages => _messages;
  bool get isLoading => _isLoading;
  int get unreadCount => _messages.where((m) => m.isAdmin && m.timestamp.millisecondsSinceEpoch > _lastReadTimestamp).length;

  SupportViewModel() {
    _loadLastRead();
  }

  Future<void> _loadLastRead() async {
    final prefs = await SharedPreferences.getInstance();
    _lastReadTimestamp = prefs.getInt('last_read_support') ?? 0;
    notifyListeners();
  }

  Future<void> markAsRead() async {
    _lastReadTimestamp = DateTime.now().millisecondsSinceEpoch;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt('last_read_support', _lastReadTimestamp);
    notifyListeners();
  }

  // For Admin: Listen to all tickets
  void fetchAllTickets() {
    _isLoading = true;
    _dbRef.child('support_tickets').onValue.listen((event) async {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data != null) {
        List<SupportTicket> tempTickets = [];
        for (var e in data.entries) {
          var ticket = SupportTicket.fromMap(e.value, e.key);
          
          // If phone is missing in ticket, try to fetch from user profile
          if (ticket.userPhone.isEmpty) {
            final phone = await getUserPhone(ticket.userId);
            if (phone != null) {
              ticket = SupportTicket(
                id: ticket.id,
                userId: ticket.userId,
                userName: ticket.userName,
                userPhone: phone,
                lastMessage: ticket.lastMessage,
                lastUpdate: ticket.lastUpdate,
                status: ticket.status,
              );
            }
          }
          tempTickets.add(ticket);
        }
        _tickets = tempTickets;
        _tickets.sort((a, b) => b.lastUpdate.compareTo(a.lastUpdate));
      } else {
        _tickets = [];
      }
      _isLoading = false;
      notifyListeners();
    });
  }

  // Listen to messages for a specific ticket
  void listenToMessages(String ticketId) {
    _messages = [];
    _dbRef.child('support_messages').child(ticketId).onValue.listen((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data != null) {
        _messages = data.entries.map((e) => SupportMessage.fromMap(e.value)).toList();
        _messages.sort((a, b) => a.timestamp.compareTo(b.timestamp));
      }
      notifyListeners();
    });
  }

  Future<void> sendMessage(String userId, String userName, String message, {bool isAdmin = false, String? ticketId, String? userPhone}) async {
    final tId = ticketId ?? userId; // For simplicity, userId is the ticketId for users
    
    final messageData = SupportMessage(
      senderId: isAdmin ? 'admin' : userId,
      message: message,
      timestamp: DateTime.now(),
      isAdmin: isAdmin,
    );

    // 1. Add Message
    await _dbRef.child('support_messages').child(tId).push().set({
      ...messageData.toMap(),
      'timestamp': ServerValue.timestamp,
    });

    // 2. Update/Create Ticket Info
    final updates = {
      'userId': userId,
      'userName': userName,
      'lastMessage': message,
      'lastUpdate': ServerValue.timestamp,
      'status': 'open',
    };
    
    // Ensure userPhone is updated if provided
    if (userPhone != null && userPhone.isNotEmpty) {
      updates['userPhone'] = userPhone;
    }
    
    await _dbRef.child('support_tickets').child(tId).update(updates);
  }

  Future<String?> getUserPhone(String userId) async {
    try {
      final snapshot = await _dbRef.child('users').child(userId).child('phoneNumber').get();
      return snapshot.value as String?;
    } catch (e) {
      return null;
    }
  }

  Future<void> closeTicket(String ticketId) async {
    await _dbRef.child('support_tickets').child(ticketId).update({'status': 'closed'});
  }
}
