class UserModel {
  final int? id;
  final String? firstname;
  final String? lastname;
  final String? dateOfBirth;
  final String? placeOfBirth;
  final String? occupation;
  final String? username;
  final dynamic referralId;
  final String? languageId;
  final String? email;
  final String? phone;
  final String? balance;
  final String? merchant;
  final String? image;
  final String? address;
  final dynamic provider;
  final dynamic providerId;
  final String? status;
  final String? identityVerify;
  final String? addressVerify;
  final String? twoFa;
  final String? twoFaVerify;
  final dynamic twoFaCode;
  final String? emailVerification;
  final String? smsVerification;
  final String? verifyCode;
  final String? otpExpiry;
  final dynamic sentAt;
  final String? lastLogin;
  final String? lastLoginIp;
  final dynamic emailVerifiedAt;
  final String? referralCode;
  final dynamic referredBy;
  final String? referralLink;
  final String? createdAt;
  final String? updatedAt;
  final String? fullname;
  final String? mobile;
  final String? profileName;
  final String? photo;
  final bool? requiresVerification;
  final String? verificationType;

  UserModel({
    this.id,
    this.firstname,
    this.lastname,
    this.dateOfBirth,
    this.placeOfBirth,
    this.occupation,
    this.username,
    this.referralId,
    this.languageId,
    this.email,
    this.phone,
    this.balance,
    this.merchant,
    this.image,
    this.address,
    this.provider,
    this.providerId,
    this.status,
    this.identityVerify,
    this.addressVerify,
    this.twoFa,
    this.twoFaVerify,
    this.twoFaCode,
    this.emailVerification,
    this.smsVerification,
    this.verifyCode,
    this.otpExpiry,
    this.sentAt,
    this.lastLogin,
    this.lastLoginIp,
    this.emailVerifiedAt,
    this.referralCode,
    this.referredBy,
    this.referralLink,
    this.createdAt,
    this.updatedAt,
    this.fullname,
    this.mobile,
    this.profileName,
    this.photo,
    this.requiresVerification,
    this.verificationType,
  });

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      id: json['id'],
      firstname: json['firstname'],
      lastname: json['lastname'],
      dateOfBirth: json['date_of_birth'],
      placeOfBirth: json['place_of_birth'],
      occupation: json['occupation'],
      username: json['username'],
      referralId: json['referral_id'],
      languageId: json['language_id'],
      email: json['email'],
      phone: json['phone'],
      balance: json['balance'],
      merchant: json['merchant'],
      image: json['image'],
      address: json['address'],
      provider: json['provider'],
      providerId: json['provider_id'],
      status: json['status']?.toString(),
      identityVerify: json['identity_verify']?.toString(),
      addressVerify: json['address_verify']?.toString(),
      twoFa: json['two_fa']?.toString(),
      twoFaVerify: json['two_fa_verify']?.toString(),
      twoFaCode: json['two_fa_code'],
      emailVerification: json['email_verification']?.toString() ?? json['email_verified']?.toString(),
      smsVerification: json['sms_verification']?.toString() ?? json['sms_verified']?.toString(),
      verifyCode: json['verify_code'],
      otpExpiry: json['otp_expiry'],
      sentAt: json['sent_at'],
      lastLogin: json['last_login'],
      lastLoginIp: json['last_login_ip'],
      emailVerifiedAt: json['email_verified_at'],
      referralCode: json['referral_code'],
      referredBy: json['referred_by'],
      referralLink: json['referral_link'],
      createdAt: json['created_at'],
      updatedAt: json['updated_at'],
      fullname: json['fullname'],
      mobile: json['mobile'],
      profileName: json['profileName'],
      photo: json['photo'],
      requiresVerification: json['requires_verification'],
      verificationType: json['verification_type'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'firstname': firstname,
      'lastname': lastname,
      'date_of_birth': dateOfBirth,
      'place_of_birth': placeOfBirth,
      'occupation': occupation,
      'username': username,
      'referral_id': referralId,
      'language_id': languageId,
      'email': email,
      'phone': phone,
      'balance': balance,
      'merchant': merchant,
      'image': image,
      'address': address,
      'provider': provider,
      'provider_id': providerId,
      'status': status,
      'identity_verify': identityVerify,
      'address_verify': addressVerify,
      'two_fa': twoFa,
      'two_fa_verify': twoFaVerify,
      'two_fa_code': twoFaCode,
      'email_verification': emailVerification,
      'sms_verification': smsVerification,
      'verify_code': verifyCode,
      'otp_expiry': otpExpiry,
      'sent_at': sentAt,
      'last_login': lastLogin,
      'last_login_ip': lastLoginIp,
      'email_verified_at': emailVerifiedAt,
      'referral_code': referralCode,
      'referred_by': referredBy,
      'referral_link': referralLink,
      'created_at': createdAt,
      'updated_at': updatedAt,
      'fullname': fullname,
      'mobile': mobile,
      'profileName': profileName,
      'photo': photo,
      'requires_verification': requiresVerification,
      'verification_type': verificationType,
    };
  }

   UserModel copyWith({
    int? id,
    String? firstname,
    String? lastname,
    String? dateOfBirth,
    String? placeOfBirth,
    String? occupation,
    String? username,
    dynamic referralId,
    String? languageId,
    String? email,
    String? phone,
    String? balance,
    String? merchant,
    String? image,
    String? address,
    dynamic provider,
    dynamic providerId,
    String? status,
    String? identityVerify,
    String? addressVerify,
    String? twoFa,
    String? twoFaVerify,
    dynamic twoFaCode,
    String? emailVerification,
    String? smsVerification,
    String? verifyCode,
    String? otpExpiry,
    dynamic sentAt,
    String? lastLogin,
    String? lastLoginIp,
    dynamic emailVerifiedAt,
    String? referralCode,
    dynamic referredBy,
    String? referralLink,
    String? createdAt,
    String? updatedAt,
    String? fullname,
    String? mobile,
    String? profileName,
    String? photo,
    bool? requiresVerification,
    String? verificationType,
  }) {
    return UserModel(
      id: id ?? this.id,
      firstname: firstname ?? this.firstname,
      lastname: lastname ?? this.lastname,
      dateOfBirth: dateOfBirth ?? this.dateOfBirth,
      placeOfBirth: placeOfBirth ?? this.placeOfBirth,
      occupation: occupation ?? this.occupation,
      username: username ?? this.username,
      referralId: referralId ?? this.referralId,
      languageId: languageId ?? this.languageId,
      email: email ?? this.email,
      phone: phone ?? this.phone,
      balance: balance ?? this.balance,
      merchant: merchant ?? this.merchant,
      image: image ?? this.image,
      address: address ?? this.address,
      provider: provider ?? this.provider,
      providerId: providerId ?? this.providerId,
      status: status ?? this.status,
      identityVerify: identityVerify ?? this.identityVerify,
      addressVerify: addressVerify ?? this.addressVerify,
      twoFa: twoFa ?? this.twoFa,
      twoFaVerify: twoFaVerify ?? this.twoFaVerify,
      twoFaCode: twoFaCode ?? this.twoFaCode,
      emailVerification: emailVerification ?? this.emailVerification,
      smsVerification: smsVerification ?? this.smsVerification,
      verifyCode: verifyCode ?? this.verifyCode,
      otpExpiry: otpExpiry ?? this.otpExpiry,
      sentAt: sentAt ?? this.sentAt,
      lastLogin: lastLogin ?? this.lastLogin,
      lastLoginIp: lastLoginIp ?? this.lastLoginIp,
      emailVerifiedAt: emailVerifiedAt ?? this.emailVerifiedAt,
      referralCode: referralCode ?? this.referralCode,
      referredBy: referredBy ?? this.referredBy,
      referralLink: referralLink ?? this.referralLink,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      fullname: fullname ?? this.fullname,
      mobile: mobile ?? this.mobile,
      profileName: profileName ?? this.profileName,
      photo: photo ?? this.photo,
      requiresVerification: requiresVerification ?? this.requiresVerification,
      verificationType: verificationType ?? this.verificationType,
    );
  }
}
