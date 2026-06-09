import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';
import '../models/support_ticket_model.dart';

class SupportViewModel extends ChangeNotifier {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref();
  List<SupportTicket> _tickets = [];
  List<SupportMessage> _messages = [];
  bool _isLoading = false;

  List<SupportTicket> get tickets => _tickets;
  List<SupportMessage> get messages => _messages;
  bool get isLoading => _isLoading;

  // For Admin: Listen to all tickets
  void fetchAllTickets() {
    _isLoading = true;
    _dbRef.child('support_tickets').onValue.listen((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data != null) {
        _tickets = data.entries.map((e) => SupportTicket.fromMap(e.value, e.key)).toList();
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

  Future<void> sendMessage(String userId, String userName, String message, {bool isAdmin = false, String? ticketId}) async {
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
    await _dbRef.child('support_tickets').child(tId).update({
      'userId': userId,
      'userName': userName,
      'lastMessage': message,
      'lastUpdate': ServerValue.timestamp,
      'status': 'open',
    });
  }

  Future<void> closeTicket(String ticketId) async {
    await _dbRef.child('support_tickets').child(ticketId).update({'status': 'closed'});
  }
}
