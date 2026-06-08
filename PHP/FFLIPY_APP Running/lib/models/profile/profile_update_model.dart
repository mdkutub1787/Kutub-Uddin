import 'package:dio/dio.dart';

class ProfileUpdateModel {
  final String? firstname;
  final String? lastname;
  final String? username;
  final String? dateOfBirth;
  final String? placeOfBirth;
  final String? occupation;
  final String? languageId;
  final String? address;
  final String? genderType;
  final String? remitterType;
  final String? postCode;
  final String? countryId;
  final String? city;
  final String? state;
  final String? nationality;
  final String? sourceOfFund;
  final String? declarationAmount;
  final String? declarationStartDate;
  final String? declarationEndDate;
  final String? yearlyIncome;
  final String? dailyLimit;
  final String? monthlyLimit;
  final String? yearlyLimit;
  final String? remarks;
  final String? image;
  final String? documentType;
  final String? documentUpload;
  final String? documentIdNumber;
  final String? issue_country_code;
  final String? documentIssueDate;
  final String? documentExpiryDate;

  ProfileUpdateModel({
    this.firstname,
    this.lastname,
    this.username,
    this.dateOfBirth,
    this.placeOfBirth,
    this.occupation,
    this.languageId,
    this.address,
    this.genderType,
    this.remitterType,
    this.postCode,
    this.countryId,
    this.city,
    this.state,
    this.nationality,
    this.sourceOfFund,
    this.declarationAmount,
    this.declarationStartDate,
    this.declarationEndDate,
    this.yearlyIncome,
    this.dailyLimit,
    this.monthlyLimit,
    this.yearlyLimit,
    this.remarks,
    this.image,
    this.documentType,
    this.documentUpload,
    this.documentIdNumber,
    this.issue_country_code,
    this.documentIssueDate,
    this.documentExpiryDate,
  });

  Future<Map<String, dynamic>> toJson() async {
    final Map<String, dynamic> data = {
      'firstname': firstname,
      'lastname': lastname,
      'username': username,
      'date_of_birth': dateOfBirth,
      'place_of_birth': placeOfBirth,
      'occupation': occupation,
      'language_id': languageId,
      'address': address,
      'gender_type': genderType,
      'remitter_type': remitterType,
      'post_code': postCode,
      'country_id': countryId,
      'city': city,
      'state': state,
      'nationality': nationality,
      'source_of_fund': sourceOfFund,
      'declaration_amount': declarationAmount,
      'declaration_start_date': declarationStartDate,
      'declaration_end_date': declarationEndDate,
      'yearly_income': yearlyIncome,
      'daily_limit': dailyLimit,
      'monthly_limit': monthlyLimit,
      'yearly_limit': yearlyLimit,
      'remarks': remarks,
      'document_type': documentType,
      'document_id_number': documentIdNumber,
      'issue_country_code': issue_country_code,
      'document_issue_date': documentIssueDate,
      'document_expiry_date': documentExpiryDate,
    }..removeWhere((key, value) => value == null || value.toString().isEmpty);

    if (image != null && image!.isNotEmpty) {
      data['image'] = await MultipartFile.fromFile(image!);
    }
    if (documentUpload != null && documentUpload!.isNotEmpty) {
      data['document_upload'] =
          await MultipartFile.fromFile(documentUpload!);
    }

    return data;
  }
}

class ProfileUpdateResponse {
  final bool success;
  final String message;

  ProfileUpdateResponse({
    required this.success,
    required this.message,
  });

  factory ProfileUpdateResponse.fromJson(Map<String, dynamic> json) =>
      ProfileUpdateResponse(
        success: json["success"],
        message: json["message"],
      );
}
