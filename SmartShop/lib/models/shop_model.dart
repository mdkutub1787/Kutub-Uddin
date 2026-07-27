class ShopModel {
  final String id;
  final String name;
  final String ownerId;
  final String address;
  final String phone;
  final bool isOnlineOrderEnabled;
  final bool isPosEnabled;
  final DateTime createdAt;
  final double? latitude;
  final double? longitude;
  final String? imageUrl;

  ShopModel({
    required this.id,
    required this.name,
    required this.ownerId,
    required this.address,
    required this.phone,
    this.isOnlineOrderEnabled = true,
    this.isPosEnabled = true,
    required this.createdAt,
    this.latitude,
    this.longitude,
    this.imageUrl,
  });

  factory ShopModel.fromMap(Map<String, dynamic> data, String id) {
    return ShopModel(
      id: id,
      name: data['name'] ?? '',
      ownerId: data['ownerId'] ?? '',
      address: data['address'] ?? '',
      phone: data['phone'] ?? '',
      isOnlineOrderEnabled: data['isOnlineOrderEnabled'] ?? true,
      isPosEnabled: data['isPosEnabled'] ?? true,
      createdAt: data['createdAt'] != null 
          ? DateTime.parse(data['createdAt']) 
          : DateTime.now(),
      latitude: data['latitude'] != null ? (data['latitude'] as num).toDouble() : null,
      longitude: data['longitude'] != null ? (data['longitude'] as num).toDouble() : null,
      imageUrl: data['imageUrl'],
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'name': name,
      'ownerId': ownerId,
      'address': address,
      'phone': phone,
      'isOnlineOrderEnabled': isOnlineOrderEnabled,
      'isPosEnabled': isPosEnabled,
      'createdAt': createdAt.toIso8601String(),
      'latitude': latitude,
      'longitude': longitude,
      'imageUrl': imageUrl,
    };
  }
}
