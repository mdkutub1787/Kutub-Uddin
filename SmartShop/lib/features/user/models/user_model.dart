class UserModel {
  final String uid;
  final String email;
  final String name; 
  final String phoneNumber;
  final String address;
  final String role;
  final String? shopId;
  final String? shopName;
  final String? imageUrl;
  final bool isActive;
  
  final bool? isAvailable;
  
  // Delivery Zone
  final String? deliveryZoneId;
  final String? deliveryZoneName;

  UserModel({
    required this.uid,
    required this.email,
    required this.name,
    required this.phoneNumber,
    required this.address,
    required this.role,
    this.shopId,
    this.shopName,
    this.imageUrl,
    this.isActive = true,
    this.isAvailable,
    this.deliveryZoneId,
    this.deliveryZoneName,
  });

  factory UserModel.fromJson(Map<String, dynamic> data) {
    return UserModel(
      uid: data['id']?.toString() ?? data['uid']?.toString() ?? '',
      email: data['email'] ?? '',
      name: data['name'] ?? data['full_name'] ?? '',
      phoneNumber: data['phoneNumber'] ?? data['phone_number'] ?? '',
      address: data['address'] ?? '',
      role: data['role'] ?? 'user',
      shopId: data['shopId']?.toString() ?? data['shop_id']?.toString(),
      shopName: data['shop_name'] ?? data['shopName'],
      imageUrl: data['imageUrl'] ?? data['image_url'] ?? data['avatar_url'],
      isActive: data['isActive'] ?? true,
      isAvailable: data['isAvailable'],
      deliveryZoneId: data['deliveryZoneId'] ?? data['delivery_zone_id'],
      deliveryZoneName: data['deliveryZoneName'] ?? data['delivery_zone_name'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {
      'name': name,
      'phoneNumber': phoneNumber,
      'address': address,
      'role': role,
      'shopName': shopName,
      'imageUrl': imageUrl,
      'isActive': isActive,
      'isAvailable': isAvailable,
      'deliveryZoneId': deliveryZoneId,
      'deliveryZoneName': deliveryZoneName,
    };
    return data;
  }

  UserModel copyWith({
    String? uid,
    String? email,
    String? name,
    String? phoneNumber,
    String? address,
    String? role,
    String? shopId,
    String? shopName,
    String? imageUrl,
    bool? isActive,
    bool? isAvailable,
    String? deliveryZoneId,
    String? deliveryZoneName,
  }) {
    return UserModel(
      uid: uid ?? this.uid,
      email: email ?? this.email,
      name: name ?? this.name,
      phoneNumber: phoneNumber ?? this.phoneNumber,
      address: address ?? this.address,
      role: role ?? this.role,
      shopId: shopId ?? this.shopId,
      shopName: shopName ?? this.shopName,
      imageUrl: imageUrl ?? this.imageUrl,
      isActive: isActive ?? this.isActive,
      isAvailable: isAvailable ?? this.isAvailable,
      deliveryZoneId: deliveryZoneId ?? this.deliveryZoneId,
      deliveryZoneName: deliveryZoneName ?? this.deliveryZoneName,
    );
  }
}
