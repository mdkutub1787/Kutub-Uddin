class SendMoneyCalServiceCrgResponse {
  final bool success;
  final String message;
  final CalculationData? data;
  final Map<String, List<String>>? errors;
  final String? errorCode;

  SendMoneyCalServiceCrgResponse({
    required this.success,
    required this.message,
    this.data,
    this.errors,
    this.errorCode,
  });

  factory SendMoneyCalServiceCrgResponse.fromJson(Map<String, dynamic> json) =>
      SendMoneyCalServiceCrgResponse(
        success: json["success"],
        message: json["message"],
        data: json["data"] == null ? null : CalculationData.fromJson(json["data"]),
        errors: json["errors"] == null
            ? null
            : Map.from(json["errors"]).map((k, v) =>
                MapEntry<String, List<String>>(k, List<String>.from(v.map((x) => x)))),
        errorCode: json["error_code"],
      );
}

class CalculationData {
  final String sendAmount;
  final String fee;
  final String feeType;
  final String? feePercentage;
  final String totalPayable;
  final String exchangeRate;
  final String? amountAfterFee;
  final String receivedAmount;
  final String senderCurrencyCode;
  final String senderCurrencySymbol;
  final String receiverCurrencyCode;
  final String receiverCurrencySymbol;

  CalculationData({
    required this.sendAmount,
    required this.fee,
    required this.feeType,
    this.feePercentage,
    required this.totalPayable,
    required this.exchangeRate,
    this.amountAfterFee,
    required this.receivedAmount,
    required this.senderCurrencyCode,
    required this.senderCurrencySymbol,
    required this.receiverCurrencyCode,
    required this.receiverCurrencySymbol,
  });

  factory CalculationData.fromJson(Map<String, dynamic> json) =>
      CalculationData(
        sendAmount: json["send_amount"],
        fee: json["fee"],
        feeType: json["fee_type"],
        feePercentage: json["fee_percentage"],
        totalPayable: json["total_payable"],
        exchangeRate: json["exchange_rate"],
        amountAfterFee: json["amount_after_fee"],
        receivedAmount: json["received_amount"],
        senderCurrencyCode: json["sender_currency_code"],
        senderCurrencySymbol: json["sender_currency_symbol"],
        receiverCurrencyCode: json["receiver_currency_code"],
        receiverCurrencySymbol: json["receiver_currency_symbol"],
      );
}
