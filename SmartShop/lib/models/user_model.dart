class UserModel {
  final String uid;
  final String email;
  final String name; // Renamed from displayName
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

  factory UserModel.fromMap(Map<String, dynamic> data, String uid) {
    return UserModel(
      uid: uid,
      email: data['email'] ?? '',
      name: data['name'] ?? (data['displayName'] ?? ''),
      phoneNumber: data['phoneNumber'] ?? '',
      address: data['address'] ?? '',
      role: data['role'] ?? 'user',
      shopId: data['shopId'],
      isActive: data['isActive'] ?? true,
      isAvailable: data['isAvailable'],
      latitude: data['latitude']?.toDouble(),
      longitude: data['longitude']?.toDouble(),
      vehicleType: data['vehicleType'],
    );
  }

  Map<String, dynamic> toMap() {
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
}
