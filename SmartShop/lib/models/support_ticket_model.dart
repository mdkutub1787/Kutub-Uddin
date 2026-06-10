class SupportMessage {
  final String senderId;
  final String message;
  final DateTime timestamp;
  final bool isAdmin;

  SupportMessage({
    required this.senderId,
    required this.message,
    required this.timestamp,
    required this.isAdmin,
  });

  factory SupportMessage.fromMap(Map<dynamic, dynamic> map) {
    return SupportMessage(
      senderId: map['senderId'] ?? '',
      message: map['message'] ?? '',
      timestamp: DateTime.fromMillisecondsSinceEpoch(map['timestamp'] ?? DateTime.now().millisecondsSinceEpoch),
      isAdmin: map['isAdmin'] ?? false,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'senderId': senderId,
      'message': message,
      'timestamp': timestamp.millisecondsSinceEpoch,
      'isAdmin': isAdmin,
    };
  }
}

class SupportTicket {
  final String id;
  final String userId;
  final String userName;
  final String userPhone;
  final String lastMessage;
  final DateTime lastUpdate;
  final String status; // 'open', 'closed'

  SupportTicket({
    required this.id,
    required this.userId,
    required this.userName,
    required this.userPhone,
    required this.lastMessage,
    required this.lastUpdate,
    this.status = 'open',
  });

  factory SupportTicket.fromMap(Map<dynamic, dynamic> map, String id) {
    return SupportTicket(
      id: id,
      userId: map['userId'] ?? '',
      userName: map['userName'] ?? '',
      userPhone: map['userPhone'] ?? '',
      lastMessage: map['lastMessage'] ?? '',
      lastUpdate: DateTime.fromMillisecondsSinceEpoch(map['lastUpdate'] ?? DateTime.now().millisecondsSinceEpoch),
      status: map['status'] ?? 'open',
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'userId': userId,
      'userName': userName,
      'userPhone': userPhone,
      'lastMessage': lastMessage,
      'lastUpdate': lastUpdate.millisecondsSinceEpoch,
      'status': status,
    };
  }
}
