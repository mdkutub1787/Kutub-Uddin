import 'package:fflipy/models/beneficiary/beneficiary_model.dart';

class AddBeneficiaryResponse {
  final bool status;
  final String message;
  final BeneficiaryModel? data;

  AddBeneficiaryResponse({
    required this.status,
    required this.message,
    this.data,
  });

  factory AddBeneficiaryResponse.fromJson(Map<String, dynamic> json) =>
      AddBeneficiaryResponse(
        status: json["success"],
        message: json["message"],
        data: json["data"] == null ? null : BeneficiaryModel.fromJson(json["data"]),
      );
}
