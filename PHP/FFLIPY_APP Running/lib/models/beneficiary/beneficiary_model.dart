import 'package:equatable/equatable.dart';

class BeneficiaryModel {
  final int id;
  final String userId;
  final String firstName;
  final String? middleName;
  final String lastName;
  final String? secondLastName;
  final String? address;
  final String countryId;
  final String? cityCode;
  final String? city;
  final String? zipCode;
  final String? phoneNumber;
  final String? cellPhoneNumber;
  final String? primaryPhoneNumber;
  final String? email;
  final String relationshipToSender;
  final String transactionType;
  final String? bnkInfoId;
  final String? bnkBrInfoId;
  final String? accountNumber;
  final String? accountType;
  final String? iban;
  final String? swiftCode;
  final String? walletProvider;
  final String? walletNumber;
  final String? verifySanction;
  final String? sanctionListSource;
  final String? verifiedAt;
  final String? verifiedBy;
  final String? sanctionMatchDetails;
  final String? status;
  final DateTime createdAt;
  final DateTime updatedAt;
  final DateTime? deletedAt;
  final String? transactionTypeName;
  final Country? country;
  final BnkInfo? bnkInfo;
  final BnkBrInfo? bnkBrInfo;
  final CountryService? countryService;

  BeneficiaryModel({
    required this.id,
    required this.userId,
    required this.firstName,
    this.middleName,
    required this.lastName,
    this.secondLastName,
    this.address,
    required this.countryId,
    this.cityCode,
    this.city,
    this.zipCode,
    this.phoneNumber,
    this.cellPhoneNumber,
    this.primaryPhoneNumber,
    this.email,
    required this.relationshipToSender,
    required this.transactionType,
    this.bnkInfoId,
    this.bnkBrInfoId,
    this.accountNumber,
    this.accountType,
    this.iban,
    this.swiftCode,
    this.walletProvider,
    this.walletNumber,
    this.verifySanction,
    this.sanctionListSource,
    this.verifiedAt,
    this.verifiedBy,
    this.sanctionMatchDetails,
    this.status,
    required this.createdAt,
    required this.updatedAt,
    this.deletedAt,
    this.transactionTypeName,
    this.country,
    this.bnkInfo,
    this.bnkBrInfo,
    this.countryService,
  });

  factory BeneficiaryModel.fromJson(Map<String, dynamic> json) =>
      BeneficiaryModel(
        id: json["id"],
        userId: json["user_id"].toString(),
        firstName: json["first_name"],
        middleName: json["middle_name"],
        lastName: json["last_name"],
        secondLastName: json["second_last_name"],
        address: json["address"],
        countryId: json["country_id"].toString(),
        cityCode: json["city_code"],
        city: json["city"],
        zipCode: json["zip_code"],
        phoneNumber: json["phone_number"],
        cellPhoneNumber: json["cell_phone_number"],
        primaryPhoneNumber: json["primary_phone_number"],
        email: json["email"],
        relationshipToSender: json["relationship_to_sender"].toString(),
        transactionType: json["transaction_type"].toString(),
        bnkInfoId: json["bnk_info_id"]?.toString(),
        bnkBrInfoId: json["bnk_br_info_id"]?.toString(),
        accountNumber: json["account_number"],
        accountType: json["account_type"]?.toString(),
        iban: json["iban"],
        swiftCode: json["swift_code"],
        walletProvider: json["wallet_provider"]?.toString(),
        walletNumber: json["wallet_number"],
        verifySanction: json["verify_sanction"],
        sanctionListSource: json["sanction_list_source"],
        verifiedAt: json["verified_at"],
        verifiedBy: json["verified_by"],
        sanctionMatchDetails: json["sanction_match_details"],
        status: json["status"],
        createdAt: DateTime.parse(json["created_at"]),
        updatedAt: DateTime.parse(json["updated_at"]),
        deletedAt: json["deleted_at"] == null
            ? null
            : DateTime.parse(json["deleted_at"]),
        transactionTypeName: json["transaction_type_name"],
        country:
            json["country"] == null ? null : Country.fromJson(json["country"]),
        bnkInfo:
            json["bnk_info"] == null ? null : BnkInfo.fromJson(json["bnk_info"]),
        bnkBrInfo: json["bnk_br_info"] == null
            ? null
            : BnkBrInfo.fromJson(json["bnk_br_info"]),
        countryService: json["country_service"] == null
            ? null
            : CountryService.fromJson(json["country_service"]),
      );
}

class BnkInfo {
  final int id;
  final String? bicNo;
  final String? bankCode;
  final String bankName;
  final String? bbFiCodeRemit;
  final String? countryId;
  final String? usr;
  final String? dt;
  final String? tm;
  final String? npsb;

  BnkInfo({
    required this.id,
    this.bicNo,
    this.bankCode,
    required this.bankName,
    this.bbFiCodeRemit,
    this.countryId,
    this.usr,
    this.dt,
    this.tm,
    this.npsb,
  });

  factory BnkInfo.fromJson(Map<String, dynamic> json) => BnkInfo(
        id: json["id"],
        bicNo: json["bic_no"],
        bankCode: json["bankCode"],
        bankName: json["bank_name"],
        bbFiCodeRemit: json["bbFICodeRemit"],
        countryId: json["country_id"]?.toString(),
        usr: json["usr"]?.toString(),
        dt: json["dt"],
        tm: json["tm"],
        npsb: json["npsb"]?.toString(),
      );
}

class BnkBrInfo {
  final int id;
  final String? bnkInfoId;
  final String branchName;
  final String? branchCode;
  final String? adBrCode;
  final String? routingNumber;
  final String? usr;
  final String? dt;
  final String? tm;

  BnkBrInfo({
    required this.id,
    this.bnkInfoId,
    required this.branchName,
    this.branchCode,
    this.adBrCode,
    this.routingNumber,
    this.usr,
    this.dt,
    this.tm,
  });

  factory BnkBrInfo.fromJson(Map<String, dynamic> json) => BnkBrInfo(
        id: json["id"],
        bnkInfoId: json["bnk_info_id"]?.toString(),
        branchName: json["branch_name"],
        branchCode: json["branchCode"],
        adBrCode: json["adBrCode"],
        routingNumber: json["routingNumber"],
        usr: json["usr"],
        dt: json["dt"],
        tm: json["tm"],
      );
}

class CountryService {
  final int id;
  final String name;
  final String? bankCode;
  final dynamic operatorId;
  final dynamic localMinAmount;
  final dynamic localMaxAmount;
  final String? countryId;
  final String? serviceId;
  final List<dynamic>? servicesForm;
  final String? status;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  CountryService({
    required this.id,
    required this.name,
    this.bankCode,
    this.operatorId,
    this.localMinAmount,
    this.localMaxAmount,
    this.countryId,
    this.serviceId,
    this.servicesForm,
    this.status,
    this.createdAt,
    this.updatedAt,
  });

  factory CountryService.fromJson(Map<String, dynamic> json) => CountryService(
        id: json["id"],
        name: json["name"],
        bankCode: json["bank_code"],
        operatorId: json["operatorId"],
        localMinAmount: json["localMinAmount"],
        localMaxAmount: json["localMaxAmount"],
        countryId: json["country_id"]?.toString(),
        serviceId: json["service_id"]?.toString(),
        servicesForm: json["services_form"] == null
            ? []
            : List<dynamic>.from(json["services_form"]!.map((x) => x)),
        status: json["status"]?.toString(),
        createdAt: json["created_at"] == null
            ? null
            : DateTime.parse(json["created_at"]),
        updatedAt: json["updated_at"] == null
            ? null
            : DateTime.parse(json["updated_at"]),
      );
}

class Country extends Equatable {
    final int id;
    final String name;
    final String isoCode;
    final String slug;
    final String code;
    final String minimumAmount;
    final String maximumAmount;
    final String image;
    final String continentId;
    final List<Facility> facilities;
    final String rate;
    final String status;
    final String sendFrom;
    final String sendTo;
    final String details;
    final DateTime createdAt;
    final DateTime updatedAt;
    final String flag;

    const Country({
        required this.id,
        required this.name,
        required this.isoCode,
        required this.slug,
        required this.code,
        required this.minimumAmount,
        required this.maximumAmount,
        required this.image,
        required this.continentId,
        required this.facilities,
        required this.rate,
        required this.status,
        required this.sendFrom,
        required this.sendTo,
        required this.details,
        required this.createdAt,
        required this.updatedAt,
        required this.flag,
    });

    factory Country.fromJson(Map<String, dynamic> json) => Country(
        id: json["id"],
        name: json["name"],
        isoCode: json["iso_code"],
        slug: json["slug"],
        code: json["code"],
        minimumAmount: json["minimum_amount"],
        maximumAmount: json["maximum_amount"],
        image: json["image"],
        continentId: json["continent_id"].toString(),
        facilities: List<Facility>.from(json["facilities"].map((x) => Facility.fromJson(x))),
        rate: json["rate"],
        status: json["status"].toString(),
        sendFrom: json["send_from"].toString(),
        sendTo: json["send_to"].toString(),
        details: json["details"],
        createdAt: DateTime.parse(json["created_at"]),
        updatedAt: DateTime.parse(json["updated_at"]),
        flag: json["flag"],
    );

    @override
    List<Object?> get props => [id, name, isoCode, slug, code, minimumAmount, maximumAmount, image, continentId, facilities, rate, status, sendFrom, sendTo, details, createdAt, updatedAt, flag];
}

class Facility extends Equatable {
    final int id;
    final String name;

    const Facility({
        required this.id,
        required this.name,
    });

    factory Facility.fromJson(Map<String, dynamic> json) => Facility(
        id: json["id"],
        name: json["name"],
    );

    @override
    List<Object?> get props => [id, name];
}
