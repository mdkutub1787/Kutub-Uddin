class ParcelModel {
  final String id;
  final String senderId;
  final String senderName;
  final String senderPhone;
  final String pickupAddress;
  final double? pickupLatitude;
  final double? pickupLongitude;

  final String receiverName;
  final String receiverPhone;
  final String dropoffAddress;
  final double? dropoffLatitude;
  final double? dropoffLongitude;
  
  final String deliveryZoneId; // Zone for assignment

  final String parcelType; // e.g., "Document", "Box", "Fragile"
  final double weightKg;
  final double deliveryCharge;

  final String status; // 'Pending', 'Accepted', 'Picked Up', 'Delivered', 'Cancelled'
  
  final String? deliveryManId;
  final String? deliveryManName;
  final String? deliveryManPhone;

  final DateTime createdAt;
  final DateTime? updatedAt;

  ParcelModel({
    required this.id,
    required this.senderId,
    required this.senderName,
    required this.senderPhone,
    required this.pickupAddress,
    this.pickupLatitude,
    this.pickupLongitude,
    required this.receiverName,
    required this.receiverPhone,
    required this.dropoffAddress,
    this.dropoffLatitude,
    this.dropoffLongitude,
    required this.deliveryZoneId,
    required this.parcelType,
    required this.weightKg,
    required this.deliveryCharge,
    this.status = 'Pending',
    this.deliveryManId,
    this.deliveryManName,
    this.deliveryManPhone,
    required this.createdAt,
    this.updatedAt,
  });

  factory ParcelModel.fromJson(Map<String, dynamic> data) {
    return ParcelModel(
      id: data['id']?.toString() ?? '',
      senderId: data['senderId'] ?? data['sender_id'] ?? '',
      senderName: data['senderName'] ?? data['sender_name'] ?? '',
      senderPhone: data['senderPhone'] ?? data['sender_phone'] ?? '',
      pickupAddress: data['pickupAddress'] ?? data['pickup_address'] ?? '',
      pickupLatitude: (data['pickupLatitude'] ?? data['pickup_latitude'])?.toDouble(),
      pickupLongitude: (data['pickupLongitude'] ?? data['pickup_longitude'])?.toDouble(),
      receiverName: data['receiverName'] ?? data['receiver_name'] ?? '',
      receiverPhone: data['receiverPhone'] ?? data['receiver_phone'] ?? '',
      dropoffAddress: data['dropoffAddress'] ?? data['delivery_address'] ?? '',
      dropoffLatitude: (data['dropoffLatitude'] ?? data['delivery_latitude'])?.toDouble(),
      dropoffLongitude: (data['dropoffLongitude'] ?? data['delivery_longitude'])?.toDouble(),
      deliveryZoneId: data['deliveryZoneId'] ?? data['delivery_zone_id'] ?? '',
      parcelType: data['parcelType'] ?? data['item_description'] ?? 'Standard',
      weightKg: (data['weightKg'] ?? data['weight_kg'] ?? 0.0).toDouble(),
      deliveryCharge: (data['deliveryCharge'] ?? data['delivery_charge'] ?? 0.0).toDouble(),
      status: data['status'] ?? 'Pending',
      deliveryManId: data['deliveryManId'] ?? data['delivery_man_id'],
      deliveryManName: data['deliveryManName'] ?? data['delivery_man_name'],
      deliveryManPhone: data['deliveryManPhone'] ?? data['delivery_man_phone'],
      createdAt: data['createdAt'] != null ? DateTime.parse(data['createdAt']) : (data['created_at'] != null ? DateTime.parse(data['created_at']) : DateTime.now()),
      updatedAt: data['updatedAt'] != null ? DateTime.parse(data['updatedAt']) : (data['updated_at'] != null ? DateTime.parse(data['updated_at']) : null),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'sender_id': senderId,
      'sender_name': senderName,
      'sender_phone': senderPhone,
      'pickup_address': pickupAddress,
      'pickup_latitude': pickupLatitude,
      'pickup_longitude': pickupLongitude,
      'receiver_name': receiverName,
      'receiver_phone': receiverPhone,
      'delivery_address': dropoffAddress,
      'delivery_latitude': dropoffLatitude,
      'delivery_longitude': dropoffLongitude,
      'delivery_zone_id': deliveryZoneId.isEmpty ? null : deliveryZoneId,
      'item_description': parcelType,
      'weight_kg': weightKg,
      'delivery_charge': deliveryCharge,
      'status': status,
      'delivery_man_id': (deliveryManId == null || deliveryManId!.isEmpty) ? null : deliveryManId,
      'delivery_man_name': deliveryManName,
      'delivery_man_phone': deliveryManPhone,
      'created_at': createdAt.toIso8601String(),
      'updated_at': updatedAt != null ? updatedAt!.toIso8601String() : DateTime.now().toIso8601String(),
    };
  }
}
