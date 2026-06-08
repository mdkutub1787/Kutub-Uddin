import 'package:equatable/equatable.dart';

class Language {
  final int id;
  final String name;
  final String shortName;

  Language({
    required this.id,
    required this.name,
    required this.shortName,
  });

  factory Language.fromJson(Map<String, dynamic> json) => Language(
        id: json["id"],
        name: json["name"],
        shortName: json["short_name"],
      );
}

class ProfileData {
  final UserProfileModel userProfile;
  final List<Language> languages;

  ProfileData({required this.userProfile, required this.languages});

  ProfileData copyWith({
    UserProfileModel? userProfile,
    List<Language>? languages,
  }) {
    return ProfileData(
      userProfile: userProfile ?? this.userProfile,
      languages: languages ?? this.languages,
    );
  }
}

class UserProfileModel extends Equatable {
  final int? id;
  final String? firstname;
  final String? lastname;
  final String? username;
  final String? email;
  final String? phone;
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
  final String? issueCountryCode;
  final String? documentIssueDate;
  final String? documentExpiryDate;
  final String? status;
  final String? referralCode;
  final String? balance;
  final int? rewardPoint;
  final String? updatedAt;
  final String? lastLogin;
  final String? referralLink;
  final int? referredUsers;
  final String? identityVerify;
  final String? addressVerify;
  final String? twoFa;
  final String? twoFaVerify;

  const UserProfileModel({
    this.id,
    this.firstname,
    this.lastname,
    this.username,
    this.email,
    this.phone,
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
    this.issueCountryCode,
    this.documentIssueDate,
    this.documentExpiryDate,
    this.status,
    this.referralCode,
    this.balance,
    this.rewardPoint,
    this.updatedAt,
    this.lastLogin,
    this.referralLink,
    this.referredUsers,
    this.identityVerify,
    this.addressVerify,
    this.twoFa,
    this.twoFaVerify,
  });

  factory UserProfileModel.fromJson(Map<String, dynamic> json) {
    Map<String, dynamic> user;
    Map<String, dynamic> otherInfo;
    Map<String, dynamic> documentInfo;
    Map<String, dynamic> root;

    if (json.containsKey('user') && json['user'] is Map) {
      user = json['user'] as Map<String, dynamic>;
      otherInfo = json['otherInfo'] as Map<String, dynamic>? ?? {};
      documentInfo = json['document_info'] as Map<String, dynamic>? ?? {};
      root = json; 
    } else {
      user = json;
      otherInfo = json;
      documentInfo = json;
      root = json;
    }

    return UserProfileModel(
      id: user['id'] as int?,
      firstname: user['firstname']?.toString(),
      lastname: user['lastname']?.toString(),
      username: user['username']?.toString(),
      email: user['email']?.toString(),
      phone: user['phone']?.toString(),
      dateOfBirth: user['date_of_birth']?.toString(),
      placeOfBirth: user['place_of_birth']?.toString(),
      occupation: user['occupation']?.toString(),
      languageId: user['language_id']?.toString(),
      address: user['address']?.toString(),
      image: user['photo']?.toString() ?? user['image']?.toString(),
      balance: user['balance']?.toString(),
      status: user['status']?.toString(),
      identityVerify: user['identity_verify']?.toString(),
      addressVerify: user['address_verify']?.toString(),
      twoFa: user['two_fa']?.toString(),
      twoFaVerify: user['two_fa_verify']?.toString(),
      updatedAt: user['updated_at']?.toString(),
      lastLogin: user['last_login']?.toString(),
      
      genderType: otherInfo['gender_type']?.toString(),
      remitterType: otherInfo['remitter_type']?.toString(),
      postCode: otherInfo['post_code']?.toString(),
      countryId: otherInfo['country_id']?.toString(),
      city: otherInfo['city']?.toString(),
      state: otherInfo['state']?.toString(),
      nationality: otherInfo['nationality']?.toString(),
      sourceOfFund: otherInfo['source_of_fund']?.toString(),
      declarationAmount: otherInfo['declaration_amount']?.toString(),
      declarationStartDate: otherInfo['declaration_start_date']?.toString(),
      declarationEndDate: otherInfo['declaration_end_date']?.toString(),
      yearlyIncome: otherInfo['yearly_income']?.toString(),
      dailyLimit: otherInfo['daily_limit']?.toString(),
      monthlyLimit: otherInfo['monthly_limit']?.toString(),
      yearlyLimit: otherInfo['yearly_limit']?.toString(),
      remarks: otherInfo['remarks']?.toString(),

      documentType: documentInfo['document_type']?.toString(),
      documentUpload: documentInfo['document_file']?.toString() ?? documentInfo['document_upload']?.toString(),
      documentIdNumber: documentInfo['document_id_number']?.toString(),
      issueCountryCode: documentInfo['issue_country_code']?.toString(),
      documentIssueDate: documentInfo['document_issue_date']?.toString(),
      documentExpiryDate: documentInfo['document_expiry_date']?.toString(),
      
      rewardPoint: root['rewardPoint'] as int?,
      referralCode: root['referralCode']?.toString(),
      referralLink: root['referralLink']?.toString(),
      referredUsers: root['referredUsers'] as int?,
    );
  }

  UserProfileModel copyWith({
    int? id,
    String? firstname,
    String? lastname,
    String? username,
    String? email,
    String? phone,
    String? dateOfBirth,
    String? placeOfBirth,
    String? occupation,
    String? languageId,
    String? address,
    String? genderType,
    String? remitterType,
    String? postCode,
    String? countryId,
    String? city,
    String? state,
    String? nationality,
    String? sourceOfFund,
    String? declarationAmount,
    String? declarationStartDate,
    String? declarationEndDate,
    String? yearlyIncome,
    String? dailyLimit,
    String? monthlyLimit,
    String? yearlyLimit,
    String? remarks,
    String? image,
    String? documentType,
    String? documentUpload,
    String? documentIdNumber,
    String? issueCountryCode,
    String? documentIssueDate,
    String? documentExpiryDate,
    String? status,
    String? referralCode,
    String? balance,
    int? rewardPoint,
    String? updatedAt,
    String? lastLogin,
    String? referralLink,
    int? referredUsers,
    String? identityVerify,
    String? addressVerify,
    String? twoFa,
    String? twoFaVerify,
  }) {
    return UserProfileModel(
      id: id ?? this.id,
      firstname: firstname ?? this.firstname,
      lastname: lastname ?? this.lastname,
      username: username ?? this.username,
      email: email ?? this.email,
      phone: phone ?? this.phone,
      dateOfBirth: dateOfBirth ?? this.dateOfBirth,
      placeOfBirth: placeOfBirth ?? this.placeOfBirth,
      occupation: occupation ?? this.occupation,
      languageId: languageId ?? this.languageId,
      address: address ?? this.address,
      genderType: genderType ?? this.genderType,
      remitterType: remitterType ?? this.remitterType,
      postCode: postCode ?? this.postCode,
      countryId: countryId ?? this.countryId,
      city: city ?? this.city,
      state: state ?? this.state,
      nationality: nationality ?? this.nationality,
      sourceOfFund: sourceOfFund ?? this.sourceOfFund,
      declarationAmount: declarationAmount ?? this.declarationAmount,
      declarationStartDate: declarationStartDate ?? this.declarationStartDate,
      declarationEndDate: declarationEndDate ?? this.declarationEndDate,
      yearlyIncome: yearlyIncome ?? this.yearlyIncome,
      dailyLimit: dailyLimit ?? this.dailyLimit,
      monthlyLimit: monthlyLimit ?? this.monthlyLimit,
      yearlyLimit: yearlyLimit ?? this.yearlyLimit,
      remarks: remarks ?? this.remarks,
      image: image ?? this.image,
      documentType: documentType ?? this.documentType,
      documentUpload: documentUpload ?? this.documentUpload,
      documentIdNumber: documentIdNumber ?? this.documentIdNumber,
      issueCountryCode: issueCountryCode ?? this.issueCountryCode,
      documentIssueDate: documentIssueDate ?? this.documentIssueDate,
      documentExpiryDate: documentExpiryDate ?? this.documentExpiryDate,
      status: status ?? this.status,
      referralCode: referralCode ?? this.referralCode,
      balance: balance ?? this.balance,
      rewardPoint: rewardPoint ?? this.rewardPoint,
      updatedAt: updatedAt ?? this.updatedAt,
      lastLogin: lastLogin ?? this.lastLogin,
      referralLink: referralLink ?? this.referralLink,
      referredUsers: referredUsers ?? this.referredUsers,
      identityVerify: identityVerify ?? this.identityVerify,
      addressVerify: addressVerify ?? this.addressVerify,
      twoFa: twoFa ?? this.twoFa,
      twoFaVerify: twoFaVerify ?? this.twoFaVerify,
    );
  }

  @override
  List<Object?> get props => [
        id,
        firstname,
        lastname,
        username,
        email,
        phone,
        dateOfBirth,
        placeOfBirth,
        occupation,
        languageId,
        address,
        genderType,
        remitterType,
        postCode,
        countryId,
        city,
        state,
        nationality,
        sourceOfFund,
        declarationAmount,
        declarationStartDate,
        declarationEndDate,
        yearlyIncome,
        dailyLimit,
        monthlyLimit,
        yearlyLimit,
        remarks,
        image,
        documentType,
        documentUpload,
        documentIdNumber,
        issueCountryCode,
        documentIssueDate,
        documentExpiryDate,
        status,
        referralCode,
        balance,
        rewardPoint,
        updatedAt,
        lastLogin,
        referralLink,
        referredUsers,
        identityVerify,
        addressVerify,
        twoFa,
        twoFaVerify,
      ];
}
