class SendMoneyPaymentDetailsResponse {
  final bool success;
  final String message;
  final PaymentDetailsData data;
  final String timestamp;

  SendMoneyPaymentDetailsResponse({
    required this.success,
    required this.message,
    required this.data,
    required this.timestamp,
  });

  factory SendMoneyPaymentDetailsResponse.fromJson(Map<String, dynamic> json) =>
      SendMoneyPaymentDetailsResponse(
        success: json["success"],
        message: json["message"],
        data: PaymentDetailsData.fromJson(json["data"]),
        timestamp: json["timestamp"],
      );

  SendMoneyPaymentDetailsResponse copyWith({
    bool? success,
    String? message,
    PaymentDetailsData? data,
    String? timestamp,
  }) {
    return SendMoneyPaymentDetailsResponse(
      success: success ?? this.success,
      message: message ?? this.message,
      data: data ?? this.data,
      timestamp: timestamp ?? this.timestamp,
    );
  }
}

class PaymentDetailsData {
  final String sessionToken;
  final BeneficiaryDetails beneficiary;
  final CountryDetails senderCountry;
  final CountryDetails receiverCountry;
  final int sessionExpiresIn;

  PaymentDetailsData({
    required this.sessionToken,
    required this.beneficiary,
    required this.senderCountry,
    required this.receiverCountry,
    required this.sessionExpiresIn,
  });

  factory PaymentDetailsData.fromJson(Map<String, dynamic> json) => PaymentDetailsData(
        sessionToken: json["session_token"],
        beneficiary: BeneficiaryDetails.fromJson(json["beneficiary"]),
        senderCountry: CountryDetails.fromJson(json["sender_country"]),
        receiverCountry: CountryDetails.fromJson(json["receiver_country"]),
        sessionExpiresIn: json["session_expires_in"],
      );
  PaymentDetailsData copyWith({
    String? sessionToken,
    BeneficiaryDetails? beneficiary,
    CountryDetails? senderCountry,
    CountryDetails? receiverCountry,
    int? sessionExpiresIn,
  }) {
    return PaymentDetailsData(
      sessionToken: sessionToken ?? this.sessionToken,
      beneficiary: beneficiary ?? this.beneficiary,
      senderCountry: senderCountry ?? this.senderCountry,
      receiverCountry: receiverCountry ?? this.receiverCountry,
      sessionExpiresIn: sessionExpiresIn ?? this.sessionExpiresIn,
    );
  }
}

class BeneficiaryDetails {
  final int id;
  final String fullName;
  final String email;
  final String phone;
  final String? accountNumber;
  final String? walletNumber;
  final String transactionType;
  final String transactionTypeName;
  final String? bankName;
  final String? branchName;
  final String? walletProvider;

  BeneficiaryDetails({
    required this.id,
    required this.fullName,
    required this.email,
    required this.phone,
    this.accountNumber,
    this.walletNumber,
    required this.transactionType,
    required this.transactionTypeName,
    this.bankName,
    this.branchName,
    this.walletProvider,
  });

  factory BeneficiaryDetails.fromJson(Map<String, dynamic> json) =>
      BeneficiaryDetails(
        id: json["id"],
        fullName: json["full_name"],
        email: json["email"],
        phone: json["phone"],
        accountNumber: json["account_number"],
        walletNumber: json["wallet_number"],
        transactionType: json["transaction_type"].toString(),
        transactionTypeName: json["transaction_type_name"],
        bankName: json["bank_name"],
        branchName: json["branch_name"],
        walletProvider: json["wallet_provider"],
      );
}

class CountryDetails {
  final int id;
  final String name;
  final String code;
  final String rate;
  final String image;
  final String flag;
  final int? minimumAmount;
  final int? maximumAmount;

  CountryDetails({
    required this.id,
    required this.name,
    required this.code,
    required this.rate,
    required this.image,
    required this.flag,
    this.minimumAmount,
    this.maximumAmount,
  });

  factory CountryDetails.fromJson(Map<String, dynamic> json) {
    int? parseInt(dynamic value) {
      if (value == null) return null;
      if (value is int) return value;
      if (value is String) {
        final doubleValue = double.tryParse(value);
        return doubleValue?.toInt();
      }
      return null;
    }

    return CountryDetails(
      id: json["id"],
      name: json["name"],
      code: json["code"],
      rate: json["rate"].toString(),
      image: json["image"],
      flag: json["flag"],
      minimumAmount: parseInt(json["minimum_amount"]),
      maximumAmount: parseInt(json["maximum_amount"]),
    );
  }
}