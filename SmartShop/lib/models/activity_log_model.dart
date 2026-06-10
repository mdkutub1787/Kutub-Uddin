class ActivityLogModel {
  final String id;
  final String adminId;
  final String adminName;
  final String action; // e.g., 'Product Added', 'Order Updated', 'User Blocked'
  final String targetId; // ID of the product/order/user
  final String details;
  final DateTime timestamp;

  ActivityLogModel({
    required this.id,
    required this.adminId,
    required this.adminName,
    required this.action,
    required this.targetId,
    required this.details,
    required this.timestamp,
  });

  Map<String, dynamic> toMap() {
    return {
      'adminId': adminId,
      'adminName': adminName,
      'action': action,
      'targetId': targetId,
      'details': details,
      'timestamp': timestamp.millisecondsSinceEpoch,
    };
  }

  factory ActivityLogModel.fromMap(Map<dynamic, dynamic> map, String id) {
    return ActivityLogModel(
      id: id,
      adminId: map['adminId'] ?? '',
      adminName: map['adminName'] ?? 'Unknown Admin',
      action: map['action'] ?? '',
      targetId: map['targetId'] ?? '',
      details: map['details'] ?? '',
      timestamp: DateTime.fromMillisecondsSinceEpoch(map['timestamp'] ?? DateTime.now().millisecondsSinceEpoch),
    );
  }
}
