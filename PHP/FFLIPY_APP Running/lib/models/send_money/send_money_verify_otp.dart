class SendMoneyVerifyOtpResponse {
  final bool success;
  final String message;
  final VerifyOtpData? data;
  final Map<String, List<String>>? errors;
  final String? errorCode;

  SendMoneyVerifyOtpResponse({
    required this.success,
    required this.message,
    this.data,
    this.errors,
    this.errorCode,
  });

  factory SendMoneyVerifyOtpResponse.fromJson(Map<String, dynamic> json) =>
      SendMoneyVerifyOtpResponse(
        success: json["success"],
        message: json["message"],
        data: json["data"] == null ? null : VerifyOtpData.fromJson(json["data"]),
        errors: json["errors"] == null
            ? null
            : Map.from(json["errors"]).map((k, v) =>
                MapEntry<String, List<String>>(k, List<String>.from(v.map((x) => x)))),
        errorCode: json["error_code"],
      );
}

class VerifyOtpData {
  final int transactionId;
  final String referenceNumber;
  final String amountSent;
  final String fee;
  final String totalPaid;
  final String recipientReceives;
  final String exchangeRate;
  final String senderCurrency;
  final String receiverCurrency;
  final String recipientName;
  final String status;
  final String createdAt;

  VerifyOtpData({
    required this.transactionId,
    required this.referenceNumber,
    required this.amountSent,
    required this.fee,
    required this.totalPaid,
    required this.recipientReceives,
    required this.exchangeRate,
    required this.senderCurrency,
    required this.receiverCurrency,
    required this.recipientName,
    required this.status,
    required this.createdAt,
  });

  factory VerifyOtpData.fromJson(Map<String, dynamic> json) =>
      VerifyOtpData(
        transactionId: json["transaction_id"],
        referenceNumber: json["reference_number"],
        amountSent: json["amount_sent"],
        fee: json["fee"],
        totalPaid: json["total_paid"],
        recipientReceives: json["recipient_receives"],
        exchangeRate: json["exchange_rate"],
        senderCurrency: json["sender_currency"],
        receiverCurrency: json["receiver_currency"],
        recipientName: json["recipient_name"],
        status: json["status"],
        createdAt: json["created_at"],
      );
}
