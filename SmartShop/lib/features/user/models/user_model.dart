class UserModel {
  final String uid;
  final String email;
  final String name; 
  final String phoneNumber;
  final String address;
  final String role; // 'user', 'owner', 'admin', 'delivery_man'
  final String? shopId;
  final bool isActive;
  
  // Delivery man specific fields
  final bool? isAvailable;
  final double? latitude;
  final double? longitude;
  final String? vehicleType;

  UserModel({
    required this.uid,
    required this.email,
    required this.name,
    required this.phoneNumber,
    required this.address,
    required this.role,
    this.shopId,
    this.isActive = true,
    this.isAvailable,
    this.latitude,
    this.longitude,
    this.vehicleType,
  });

  factory UserModel.fromJson(Map<String, dynamic> data) {
    return UserModel(
      uid: data['id']?.toString() ?? '',
      email: data['email'] ?? '',
      name: data['name'] ?? '',
      phoneNumber: data['phoneNumber'] ?? '',
      address: data['address'] ?? '',
      role: data['role'] ?? 'user',
      shopId: data['shopId']?.toString(),
      isActive: data['isActive'] ?? true,
      isAvailable: data['isAvailable'],
      latitude: data['latitude']?.toDouble(),
      longitude: data['longitude']?.toDouble(),
      vehicleType: data['vehicleType'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'email': email,
      'name': name,
      'phoneNumber': phoneNumber,
      'address': address,
      'role': role,
      'shopId': shopId,
      'isActive': isActive,
      'isAvailable': isAvailable,
      'latitude': latitude,
      'longitude': longitude,
      'vehicleType': vehicleType,
    };
  }

  UserModel copyWith({
    String? uid,
    String? email,
    String? name,
    String? phoneNumber,
    String? address,
    String? role,
    String? shopId,
    bool? isActive,
    bool? isAvailable,
    double? latitude,
    double? longitude,
    String? vehicleType,
  }) {
    return UserModel(
      uid: uid ?? this.uid,
      email: email ?? this.email,
      name: name ?? this.name,
      phoneNumber: phoneNumber ?? this.phoneNumber,
      address: address ?? this.address,
      role: role ?? this.role,
      shopId: shopId ?? this.shopId,
      isActive: isActive ?? this.isActive,
      isAvailable: isAvailable ?? this.isAvailable,
      latitude: latitude ?? this.latitude,
      longitude: longitude ?? this.longitude,
      vehicleType: vehicleType ?? this.vehicleType,
    );
  }
}
