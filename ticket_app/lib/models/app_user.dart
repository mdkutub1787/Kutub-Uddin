enum UserRole {
  admin,
  user,
}

class AppUser {
  final String uid;
  final String email;
  final String name;
  final String phone;
  final String nid;
  final UserRole role;

  AppUser({
    required this.uid,
    required this.email,
    required this.name,
    required this.phone,
    required this.nid,
    required this.role,
  });

  factory AppUser.fromFirestore(Map<String, dynamic> data, String uid) {
    return AppUser(
      uid: uid,
      email: data['email'] ?? '',
      name: data['name'] ?? '',
      phone: data['phone'] ?? '',
      nid: data['nid'] ?? '',
      role: data['role'] == 'admin' ? UserRole.admin : UserRole.user,
    );
  }

  Map<String, dynamic> toFirestore() {
    return {
      'email': email,
      'name': name,
      'phone': phone,
      'nid': nid,
      'role': role.toString().split('.').last,
    };
  }
}
