class DeliveryZoneModel {
  final String id;
  final String zoneName; // e.g. "Dhanmondi", "Mirpur"
  final double baseDeliveryCharge;
  final String? description;
  final bool isActive;

  DeliveryZoneModel({
    required this.id,
    required this.zoneName,
    required this.baseDeliveryCharge,
    this.description,
    this.isActive = true,
  });

  factory DeliveryZoneModel.fromJson(Map<String, dynamic> data) {
    return DeliveryZoneModel(
      id: data['id']?.toString() ?? '',
      zoneName: data['zoneName'] ?? data['zone_name'] ?? data['name'] ?? '',
      baseDeliveryCharge: (data['baseDeliveryCharge'] ?? data['base_delivery_charge'] ?? 0.0).toDouble(),
      description: data['description'],
      isActive: data['isActive'] ?? data['is_active'] ?? true,
    );
  }

  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{
      'zone_name': zoneName,
      'base_delivery_charge': baseDeliveryCharge,
      'description': description,
      'is_active': isActive,
    };
    if (id.isNotEmpty) map['id'] = id;
    return map;
  }

  DeliveryZoneModel copyWith({
    String? id,
    String? zoneName,
    double? baseDeliveryCharge,
    String? description,
    bool? isActive,
  }) {
    return DeliveryZoneModel(
      id: id ?? this.id,
      zoneName: zoneName ?? this.zoneName,
      baseDeliveryCharge: baseDeliveryCharge ?? this.baseDeliveryCharge,
      description: description ?? this.description,
      isActive: isActive ?? this.isActive,
    );
  }
}
