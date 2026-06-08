import 'package:fflipy/models/profile/user_model.dart';

class RegistrationRequest {
  final String? firstname;
  final String? lastname;
  final String? username;
  final String? email;
  final String? phone;
  final String? password;

  RegistrationRequest({
    this.firstname,
    this.lastname,
    this.username,
    this.email,
    this.phone,
    this.password,
  });

  Map<String, dynamic> toJson() {
    return {
      'firstname': firstname,
      'lastname': lastname,
      'username': username,
      'email': email,
      'phone': phone,
      'password': password,
    };
  }
}

class RegistrationResponse {
  final bool? success;
  final String? message;
  final String? token;
  final UserModel? user;
  final Map<String, dynamic>? errors;

  RegistrationResponse({
    this.success,
    this.message,
    this.token,
    this.user,
    this.errors,
  });

  factory RegistrationResponse.fromJson(Map<String, dynamic> json) {
    return RegistrationResponse(
      success: json['success'],
      message: json['message'],
      token: json['data']?['token'],
      user: json['data']?['user'] != null
          ? UserModel.fromJson(json['data']['user'])
          : null,
      errors:
          json['errors'] != null ? Map<String, dynamic>.from(json['errors']) : null,
    );
  }
}
