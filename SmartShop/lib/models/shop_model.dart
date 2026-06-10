class ShopModel {
  final String id;
  final String name;
  final String ownerId;
  final String address;
  final String phone;
  final bool isOnlineOrderEnabled;
  final bool isPosEnabled;
  final DateTime createdAt;

  ShopModel({
    required this.id,
    required this.name,
    required this.ownerId,
    required this.address,
    required this.phone,
    this.isOnlineOrderEnabled = true,
    this.isPosEnabled = true,
    required this.createdAt,
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
    };
  }
}
