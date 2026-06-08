import 'package:fflipy/models/profile/user_model.dart';

class LoginRequest {
  final String? username;
  final String? password;

  LoginRequest({this.username, this.password});

  Map<String, dynamic> toJson() {
    return {
      'username': username,
      'password': password,
    };
  }
}

class LoginResponse {
  final bool? status;
  final String? message;
  final String? token;
  final UserModel? user;
  final String? encryptionKey;

  LoginResponse({
    this.status,
    this.message,
    this.token,
    this.user,
    this.encryptionKey,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    bool? isSuccess;
    final statusValue = json['status'];
    if (statusValue is bool) {
      isSuccess = statusValue;
    } else if (statusValue is String) {
      isSuccess = statusValue.toLowerCase() != 'error';
    }

    return LoginResponse(
      status: isSuccess,
      message: json['message'],
      token: json['token'],
      user: json['user'] != null ? UserModel.fromJson(json['user']) : null,
      encryptionKey: json['encryption_key'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'status': status,
      'message': message,
      'token': token,
      'user': user?.toJson(),
      'encryption_key': encryptionKey,
    };
  }
}
