class TicketDetailsResponse {
  final bool success;
  final String message;
  final TicketDetailsData? data;

  TicketDetailsResponse({
    required this.success,
    required this.message,
    this.data,
  });

  factory TicketDetailsResponse.fromJson(Map<String, dynamic> json) {
    return TicketDetailsResponse(
      success: json['success'] ?? false,
      message: json['message'] ?? '',
      data: json['data'] != null ? TicketDetailsData.fromJson(json['data']) : null,
    );
  }
}

class TicketDetailsData {
  final TicketDetail ticket;

  TicketDetailsData({required this.ticket});

  factory TicketDetailsData.fromJson(Map<String, dynamic> json) {
    return TicketDetailsData(
      ticket: TicketDetail.fromJson(json['ticket']),
    );
  }
}

class TicketDetail {
  final int id;
  final String userId;
  final String name;
  final String email;
  final String ticket;
  final String subject;
  final String status;
  final String lastReply;
  final String createdAt;
  final String updatedAt;
  final List<TicketMessage> messages;

  TicketDetail({
    required this.id,
    required this.userId,
    required this.name,
    required this.email,
    required this.ticket,
    required this.subject,
    required this.status,
    required this.lastReply,
    required this.createdAt,
    required this.updatedAt,
    required this.messages,
  });

  factory TicketDetail.fromJson(Map<String, dynamic> json) {
    return TicketDetail(
      id: json['id'] ?? 0,
      userId: json['user_id']?.toString() ?? '',
      name: json['name'] ?? '',
      email: json['email'] ?? '',
      ticket: json['ticket']?.toString() ?? '',
      subject: json['subject'] ?? '',
      status: json['status']?.toString() ?? '',
      lastReply: json['last_reply'] ?? '',
      createdAt: json['created_at'] ?? '',
      updatedAt: json['updated_at'] ?? '',
      messages: (json['messages'] as List<dynamic>? ?? [])
          .map((e) => TicketMessage.fromJson(e))
          .toList(),
    );
  }
}

class TicketMessage {
  final int id;
  final String ticketId;
  final String? adminId;
  final String message;
  final String createdAt;
  final String updatedAt;
  final List<TicketAttachment> attachments;

  TicketMessage({
    required this.id,
    required this.ticketId,
    this.adminId,
    required this.message,
    required this.createdAt,
    required this.updatedAt,
    required this.attachments,
  });

  factory TicketMessage.fromJson(Map<String, dynamic> json) {
    return TicketMessage(
      id: json['id'] ?? 0,
      ticketId: json['ticket_id']?.toString() ?? '',
      adminId: json['admin_id']?.toString(),
      message: json['message'] ?? '',
      createdAt: json['created_at'] ?? '',
      updatedAt: json['updated_at'] ?? '',
      attachments: (json['attachments'] as List<dynamic>? ?? [])
          .map((e) => TicketAttachment.fromJson(e))
          .toList(),
    );
  }
}

class TicketAttachment {
  final int id;
  final String ticketMessageId;
  final String image;
  final String createdAt;
  final String updatedAt;

  TicketAttachment({
    required this.id,
    required this.ticketMessageId,
    required this.image,
    required this.createdAt,
    required this.updatedAt,
  });

  factory TicketAttachment.fromJson(Map<String, dynamic> json) {
    return TicketAttachment(
      id: json['id'] ?? 0,
      ticketMessageId: json['ticket_message_id']?.toString() ?? '',
      image: json['image'] ?? '',
      createdAt: json['created_at'] ?? '',
      updatedAt: json['updated_at'] ?? '',
    );
  }
}
