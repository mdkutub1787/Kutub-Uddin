class NotificationResponse {
  final bool success;
  final String message;
  final List<NotificationItem> data;

  NotificationResponse({
    required this.success,
    required this.message,
    required this.data,
  });

  factory NotificationResponse.fromJson(Map<String, dynamic> json) {
    return NotificationResponse(
      success: json['success'] ?? false,
      message: json['message'] ?? '',
      data: (json['data'] as List<dynamic>?)
              ?.map((e) => NotificationItem.fromJson(e))
              .toList() ??
          [],
    );
  }
}

class NotificationItem {
  final int id;
  final String siteNotificationalId;
  final String siteNotificationalType;
  final NotificationDescription description;
  final String createdAt;
  final String updatedAt;
  final String formattedDate;

  NotificationItem({
    required this.id,
    required this.siteNotificationalId,
    required this.siteNotificationalType,
    required this.description,
    required this.createdAt,
    required this.updatedAt,
    required this.formattedDate,
  });

  factory NotificationItem.fromJson(Map<String, dynamic> json) {
    return NotificationItem(
      id: json['id'] ?? 0,
      siteNotificationalId: safeString(json['site_notificational_id']),
      siteNotificationalType: json['site_notificational_type'] ?? '',
      description: NotificationDescription.fromJson(json['description'] ?? {}),
      createdAt: json['created_at'] ?? '',
      updatedAt: json['updated_at'] ?? '',
      formattedDate: json['formatted_date'] ?? '',
    );
  }

  static String safeString(dynamic value) {
    if (value == null) return '';
    return value.toString();
  }
}

class NotificationDescription {
  final String link;
  final String icon;
  final String text;

  NotificationDescription({
    required this.link,
    required this.icon,
    required this.text,
  });

  factory NotificationDescription.fromJson(Map<String, dynamic> json) {
    return NotificationDescription(
      link: json['link'] ?? '',
      icon: json['icon'] ?? '',
      text: (json['text'] as String?)?.trim() ?? '',
    );
  }
}
