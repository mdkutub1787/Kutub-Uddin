
class UpdateBeneficiaryResponse {
  final bool status;
  final int statusCode;
  final String message;
  final UpdatedBeneficiaryData? data;

  UpdateBeneficiaryResponse({
    required this.status,
    required this.statusCode,
    required this.message,
    this.data,
  });

  factory UpdateBeneficiaryResponse.fromJson(Map<String, dynamic> json) {
    return UpdateBeneficiaryResponse(
      status: json['status'],
      statusCode: json['status_code'],
      message: json['message'],
      data: json['data'] != null ? UpdatedBeneficiaryData.fromJson(json['data']) : null,
    );
  }
}

class UpdatedBeneficiaryData {
  final int beneficiaryId;
  final String name;

  UpdatedBeneficiaryData({
    required this.beneficiaryId,
    required this.name,
  });

  factory UpdatedBeneficiaryData.fromJson(Map<String, dynamic> json) {
    return UpdatedBeneficiaryData(
      beneficiaryId: json['beneficiary_id'],
      name: json['name'],
    );
  }
}
