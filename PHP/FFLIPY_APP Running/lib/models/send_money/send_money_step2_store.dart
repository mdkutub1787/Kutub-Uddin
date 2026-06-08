class SendMoneyStep2StoreResponse {
  final bool success;
  final String message;
  final Step2StoreData? data;
  final Map<String, List<String>>? errors;
  final String? errorCode;
  final String? warning;

  SendMoneyStep2StoreResponse({
    required this.success,
    required this.message,
    this.data,
    this.errors,
    this.errorCode,
    this.warning,
  });

  factory SendMoneyStep2StoreResponse.fromJson(Map<String, dynamic> json) =>
      SendMoneyStep2StoreResponse(
        success: json["success"],
        message: json["message"],
        data: json["data"] == null ? null : Step2StoreData.fromJson(json["data"]),
        errors: json["errors"] == null
            ? null
            : Map.from(json["errors"]).map((k, v) =>
            MapEntry<String, List<String>>(k, List<String>.from(v.map((x) => x)))),
        errorCode: json["error_code"],
        warning: json["warning"],
      );
}

class Step2StoreData {
  final String transactionToken;
  final String? expiresAt;
  final int? expiresIn;
  final String? beneficiaryName;
  final String? sendAmount;
  final String? senderCurrency;
  final String? receiverCurrency;

  Step2StoreData({
    required this.transactionToken,
    this.expiresAt,
    this.expiresIn,
    this.beneficiaryName,
    this.sendAmount,
    this.senderCurrency,
    this.receiverCurrency,
  });

  factory Step2StoreData.fromJson(Map<String, dynamic> json) =>
      Step2StoreData(
        transactionToken: json["transaction_token"],
        expiresAt: json["expires_at"],
        expiresIn: json["expires_in"],
        beneficiaryName: json["beneficiary_name"],
        sendAmount: json["send_amount"],
        senderCurrency: json["sender_currency"],
        receiverCurrency: json["receiver_currency"],
      );
}
