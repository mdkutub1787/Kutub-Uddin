class SendMoneyOtpGenerateResponse {
  final bool success;
  final String message;
  final OtpGenerateData? data;
  final Map<String, List<String>>? errors;
  final String? errorCode;

  SendMoneyOtpGenerateResponse({
    required this.success,
    required this.message,
    this.data,
    this.errors,
    this.errorCode,
  });

  factory SendMoneyOtpGenerateResponse.fromJson(Map<String, dynamic> json) =>
      SendMoneyOtpGenerateResponse(
        success: json["success"],
        message: json["message"],
        data: json["data"] == null ? null : OtpGenerateData.fromJson(json["data"]),
        errors: json["errors"] == null
            ? null
            : Map.from(json["errors"]).map((k, v) =>
                MapEntry<String, List<String>>(k, List<String>.from(v.map((x) => x)))),
        errorCode: json["error_code"],
      );
}

class OtpGenerateData {
  final int expiryTime;
  final int expiresIn;
  final String expiresAt;
  final int canResendAfter;

  OtpGenerateData({
    required this.expiryTime,
    required this.expiresIn,
    required this.expiresAt,
    required this.canResendAfter,
  });

  factory OtpGenerateData.fromJson(Map<String, dynamic> json) =>
      OtpGenerateData(
        expiryTime: json["expiry_time"],
        expiresIn: json["expires_in"],
        expiresAt: json["expires_at"],
        canResendAfter: json["can_resend_after"],
      );
}
