class UserModel {
  final String uid;
  final String email;
  final String name; // Renamed from displayName
  final String phoneNumber;
  final String address;
  final String role;
  final String? shopId;
  final bool isActive;

  UserModel({
    required this.uid,
    required this.email,
    required this.name,
    required this.phoneNumber,
    required this.address,
    required this.role,
    this.shopId,
    this.isActive = true,
  });

  factory UserModel.fromMap(Map<String, dynamic> data, String uid) {
    return UserModel(
      uid: uid,
      email: data['email'] ?? '',
      name: data['name'] ?? (data['displayName'] ?? ''), // Support both for migration
      phoneNumber: data['phoneNumber'] ?? '',
      address: data['address'] ?? '',
      role: data['role'] ?? 'user',
      shopId: data['shopId'],
      isActive: data['isActive'] ?? true,
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
    };
  }
}
