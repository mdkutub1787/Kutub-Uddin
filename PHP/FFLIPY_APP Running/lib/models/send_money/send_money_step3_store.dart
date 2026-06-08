class SendMoneyStep3StoreResponse {
  final bool success;
  final String message;
  final Step3StoreData? data;
  final Map<String, List<String>>? errors;
  final String? errorCode;

  SendMoneyStep3StoreResponse({
    required this.success,
    required this.message,
    this.data,
    this.errors,
    this.errorCode,
  });

  factory SendMoneyStep3StoreResponse.fromJson(Map<String, dynamic> json) =>
      SendMoneyStep3StoreResponse(
        success: json["success"],
        message: json["message"],
        data: json["data"] == null ? null : Step3StoreData.fromJson(json["data"]),
        errors: json["errors"] == null
            ? null
            : Map.from(json["errors"]).map((k, v) =>
                MapEntry<String, List<String>>(k, List<String>.from(v.map((x) => x)))),
        errorCode: json["error_code"],
      );
}

class Step3StoreData {
  final String transactionToken;
  final int expiresIn;
  final String expiresAt;
  final Beneficiary beneficiary;
  final TransactionSummary transactionSummary;
  final SenderInfo senderInfo;
  final List<SendingPurpose> sendingPurposes;
  final List<PaymentMethod> paymentMethods;

  Step3StoreData({
    required this.transactionToken,
    required this.expiresIn,
    required this.expiresAt,
    required this.beneficiary,
    required this.transactionSummary,
    required this.senderInfo,
    required this.sendingPurposes,
    required this.paymentMethods,
  });

  factory Step3StoreData.fromJson(Map<String, dynamic> json) =>
      Step3StoreData(
        transactionToken: json["transaction_token"],
        expiresIn: json["expires_in"],
        expiresAt: json["expires_at"],
        beneficiary: Beneficiary.fromJson(json["beneficiary"]),
        transactionSummary: TransactionSummary.fromJson(json["transaction_summary"]),
        senderInfo: SenderInfo.fromJson(json["sender_info"]),
        sendingPurposes: List<SendingPurpose>.from(
            json["sending_purposes"].map((x) => SendingPurpose.fromJson(x))),
        paymentMethods: List<PaymentMethod>.from(
            json["payment_methods"].map((x) => PaymentMethod.fromJson(x))),
      );
}

class Beneficiary {
  final int id;
  final String fullName;
  final String email;
  final String phone;
  final String country;
  final String transactionType;
  final String? accountNumber;
  final String? walletNumber;
  final String? bankName;
  final String? branchName;

  Beneficiary({
    required this.id,
    required this.fullName,
    required this.email,
    required this.phone,
    required this.country,
    required this.transactionType,
    this.accountNumber,
    this.walletNumber,
    this.bankName,
    this.branchName,
  });

  factory Beneficiary.fromJson(Map<String, dynamic> json) => Beneficiary(
        id: json["id"],
        fullName: json["full_name"],
        email: json["email"],
        phone: json["phone"],
        country: json["country"],
        transactionType: json["transaction_type"],
        accountNumber: json["account_number"],
        walletNumber: json["wallet_number"],
        bankName: json["bank_name"],
        branchName: json["branch_name"],
      );
}

class PaymentMethod {
  final int id;
  final String name;
  final String code;

  PaymentMethod({
    required this.id,
    required this.name,
    required this.code,
  });

  factory PaymentMethod.fromJson(Map<String, dynamic> json) => PaymentMethod(
        id: json["id"],
        name: json["name"],
        code: json["code"],
      );
}

class SenderInfo {
  final String name;
  final String email;
  final String phone;

  SenderInfo({
    required this.name,
    required this.email,
    required this.phone,
  });

  factory SenderInfo.fromJson(Map<String, dynamic> json) => SenderInfo(
        name: json["name"],
        email: json["email"],
        phone: json["phone"],
      );
}

class SendingPurpose {
  final int id;
  final String title;

  SendingPurpose({
    required this.id,
    required this.title,
  });

  factory SendingPurpose.fromJson(Map<String, dynamic> json) => SendingPurpose(
        id: json["id"],
        title: json["title"],
      );
}

class TransactionSummary {
  final String sendAmount;
  final int fee;
  final String totalPayable;
  final double exchangeRate;
  final double receivedAmount;
  final Currency senderCurrency;
  final Currency receiverCurrency;

  TransactionSummary({
    required this.sendAmount,
    required this.fee,
    required this.totalPayable,
    required this.exchangeRate,
    required this.receivedAmount,
    required this.senderCurrency,
    required this.receiverCurrency,
  });

  factory TransactionSummary.fromJson(Map<String, dynamic> json) {
    num feeNum = json["fee"] ?? 0;

    return TransactionSummary(
      sendAmount: json["send_amount"],
      fee: feeNum.toInt(),
      totalPayable: json["total_payable"],
      exchangeRate: (json["exchange_rate"] as num).toDouble(),
      receivedAmount: (json["received_amount"] as num).toDouble(),
      senderCurrency: Currency.fromJson(json["sender_currency"]),
      receiverCurrency: Currency.fromJson(json["receiver_currency"]),
    );
  }
}


class Currency {
  final String code;
  final String symbol;
  final String name;

  Currency({
    required this.code,
    required this.symbol,
    required this.name,
  });

  factory Currency.fromJson(Map<String, dynamic> json) => Currency(
        code: json["code"],
        symbol: json["symbol"],
        name: json["name"],
      );
}
