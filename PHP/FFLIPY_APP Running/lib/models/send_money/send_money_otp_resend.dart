class SendMoneyOtpResendResponse {
  final bool success;
  final String message;
  final SendMoneyOtpResendData? data;
  final Map<String, List<String>>? errors;
  final String? errorCode;

  SendMoneyOtpResendResponse({
    required this.success,
    required this.message,
    this.data,
    this.errors,
    this.errorCode,
  });

  factory SendMoneyOtpResendResponse.fromJson(Map<String, dynamic> json) =>
      SendMoneyOtpResendResponse(
        success: json["success"],
        message: json["message"],
        data: json["data"] == null ? null : SendMoneyOtpResendData.fromJson(json["data"]),
        errors: json["errors"] == null
            ? null
            : Map.from(json["errors"]).map((k, v) =>
            MapEntry<String, List<String>>(k, List<String>.from(v.map((x) => x)))),
        errorCode: json["error_code"],
      );
}

class SendMoneyOtpResendData {
  final int? expiryTime;
  final int? expiresIn;
  final String? expiresAt;
  final int? canResendAfter;

  SendMoneyOtpResendData({
    this.expiryTime,
    this.expiresIn,
    this.expiresAt,
    this.canResendAfter,
  });

  factory SendMoneyOtpResendData.fromJson(Map<String, dynamic> json) => SendMoneyOtpResendData(
    expiryTime: json["expiry_time"],
    expiresIn: json["expires_in"],
    expiresAt: json["expires_at"],
    canResendAfter: json["can_resend_after"],
  );
}
