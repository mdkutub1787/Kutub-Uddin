class TicketReplyResponse {
  final bool success;
  final String message;
  final List<TicketConversation>? data;

  TicketReplyResponse({
    required this.success,
    required this.message,
    this.data,
  });

  factory TicketReplyResponse.fromJson(Map<String, dynamic> json) {
    return TicketReplyResponse(
      success: json['success'] ?? false,
      message: json['message'] ?? '',
      data: json['data'] != null 
          ? (json['data'] as List<dynamic>)
              .map((e) => TicketConversation.fromJson(e))
              .toList() 
          : null,
    );
  }
}

class TicketConversation {
  final int id;
  final String replayTicket;
  final String userId;
  final String? adminId;
  final String message;
  final String? attachments;
  final String createdAt;
  final String updatedAt;

  TicketConversation({
    required this.id,
    required this.replayTicket,
    required this.userId,
    this.adminId,
    required this.message,
    this.attachments,
    required this.createdAt,
    required this.updatedAt,
  });

  factory TicketConversation.fromJson(Map<String, dynamic> json) {
    return TicketConversation(
      id: json['id'] ?? 0,
      replayTicket: json['replayTicket']?.toString() ?? '',
      userId: json['user_id']?.toString() ?? '',
      adminId: json['admin_id']?.toString(),
      message: json['message'] ?? '',
      attachments: json['attachments'],
      createdAt: json['created_at'] ?? '',
      updatedAt: json['updated_at'] ?? '',
    );
  }
}
