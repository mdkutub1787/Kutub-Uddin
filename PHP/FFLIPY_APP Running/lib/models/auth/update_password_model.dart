class UpdatePasswordRequest {
  final String currentPassword;
  final String password;

  UpdatePasswordRequest({required this.currentPassword, required this.password});

  Map<String, dynamic> toJson() => {
        'current_password': currentPassword,
        'password': password,
      };
}

class UpdatePasswordResponse {
  final bool success;
  final String message;

  UpdatePasswordResponse({required this.success, required this.message});

  factory UpdatePasswordResponse.fromJson(Map<String, dynamic> json) {
    return UpdatePasswordResponse(
      success: json['success'],
      message: json['message'],
    );
  }
}
