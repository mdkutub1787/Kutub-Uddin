class SendMoneySelectedBeneficiaryRequest {
  final String beneficiaryId;

  SendMoneySelectedBeneficiaryRequest({required this.beneficiaryId});

  Map<String, dynamic> toJson() => {
        "beneficiary_id": beneficiaryId,
      };
}

class SelectSendMoneyBeneficiaryResponse {
  final bool success;
  final String message;
  final Data data;
  final String timestamp;

  SelectSendMoneyBeneficiaryResponse({
    required this.success,
    required this.message,
    required this.data,
    required this.timestamp,
  });

  factory SelectSendMoneyBeneficiaryResponse.fromJson(Map<String, dynamic> json) =>
      SelectSendMoneyBeneficiaryResponse(
        success: json["success"],
        message: json["message"],
        data: Data.fromJson(json["data"]),
        timestamp: json["timestamp"],
      );
}

class Data {
  final String sessionToken;
  final int expiresIn;
  final SelectedBeneficiary beneficiary;

  Data({
    required this.sessionToken,
    required this.expiresIn,
    required this.beneficiary,
  });

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        sessionToken: json["session_token"],
        expiresIn: json["expires_in"],
        beneficiary: SelectedBeneficiary.fromJson(json["beneficiary"]),
      );
}

class SelectedBeneficiary {
  final int id;
  final String name;
  final String countryId;
  final String transactionType;

  SelectedBeneficiary({
    required this.id,
    required this.name,
    required this.countryId,
    required this.transactionType,
  });

  factory SelectedBeneficiary.fromJson(Map<String, dynamic> json) =>
      SelectedBeneficiary(
        id: json["id"],
        name: json["name"],
        countryId: json["country_id"].toString(),
        transactionType: json["transaction_type"].toString(),
      );
}
