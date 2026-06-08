class SupportTicketResponse {
  final bool success;
  final String message;
  final SupportTicketData? data;

  SupportTicketResponse({
    required this.success,
    required this.message,
    this.data,
  });

  factory SupportTicketResponse.fromJson(Map<String, dynamic> json) {
    return SupportTicketResponse(
      success: json['success'] ?? false,
      message: json['message'] ?? '',
      data: json['data'] != null ? SupportTicketData.fromJson(json['data']) : null,
    );
  }
}

class SupportTicketData {
  final int currentPage;
  final List<SupportTicketItem> data;
  final String firstPageUrl;
  final int from;
  final int lastPage;
  final String lastPageUrl;
  final String? nextPageUrl;
  final String path;
  final int perPage;
  final String? prevPageUrl;
  final int to;
  final int total;

  SupportTicketData({
    required this.currentPage,
    required this.data,
    required this.firstPageUrl,
    required this.from,
    required this.lastPage,
    required this.lastPageUrl,
    this.nextPageUrl,
    required this.path,
    required this.perPage,
    this.prevPageUrl,
    required this.to,
    required this.total,
  });

  factory SupportTicketData.fromJson(Map<String, dynamic> json) {
    return SupportTicketData(
      currentPage: json['current_page'] ?? 1,
      data: (json['data'] as List<dynamic>?)
              ?.map((e) => SupportTicketItem.fromJson(e))
              .toList() ??
          [],
      firstPageUrl: json['first_page_url'] ?? '',
      from: json['from'] ?? 0,
      lastPage: json['last_page'] ?? 1,
      lastPageUrl: json['last_page_url'] ?? '',
      nextPageUrl: json['next_page_url'],
      path: json['path'] ?? '',
      perPage: json['per_page'] ?? 20,
      prevPageUrl: json['prev_page_url'],
      to: json['to'] ?? 0,
      total: json['total'] ?? 0,
    );
  }
}

class SupportTicketItem {
  final int id;
  final String userId;
  final String name;
  final String email;
  final String ticket;
  final String subject;
  final String message;
  final String status;
  final String lastReply;
  final String createdAt;
  final String updatedAt;

  SupportTicketItem({
    required this.id,
    required this.userId,
    required this.name,
    required this.email,
    required this.ticket,
    required this.subject,
    required this.message,
    required this.status,
    required this.lastReply,
    required this.createdAt,
    required this.updatedAt,
  });

  factory SupportTicketItem.fromJson(Map<String, dynamic> json) {
    return SupportTicketItem(
      id: json['id'] ?? 0,
      userId: json['user_id']?.toString() ?? '',
      name: json['name'] ?? '',
      email: json['email'] ?? '',
      ticket: json['ticket']?.toString() ?? '',
      subject: json['subject'] ?? '',
      message: json['message'] ?? '',
      status: json['status']?.toString() ?? '',
      lastReply: json['last_reply'] ?? '',
      createdAt: json['created_at'] ?? '',
      updatedAt: json['updated_at'] ?? '',
    );
  }
}
