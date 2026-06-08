import 'package:equatable/equatable.dart';
import 'package:fflipy/models/beneficiary/account_type_model.dart';
import 'beneficiary_model.dart';

class BeneficiaryListResponse {
  final bool status;
  final String message;
  final Data data;

  BeneficiaryListResponse(
      {required this.status, required this.message, required this.data});

  factory BeneficiaryListResponse.fromJson(Map<String, dynamic> json) =>
      BeneficiaryListResponse(
        status: json["status"],
        message: json["message"],
        data: Data.fromJson(json["data"]),
      );
}

class Data {
  final Beneficiaries beneficiaries;
  final List<Country> countries;
  final List<SendingPurpose> sendingPurposes;
  final List<Relationship> relationships;
  final List<AccountType> accountTypes;

  Data({
    required this.beneficiaries,
    required this.countries,
    required this.sendingPurposes,
    required this.relationships,
    required this.accountTypes,
  });

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        beneficiaries: Beneficiaries.fromJson(json["beneficiaries"]),
        countries: List<Country>.from(
            json["countries"].map((x) => Country.fromJson(x))),
        sendingPurposes: List<SendingPurpose>.from(
            json["sendingPurposes"].map((x) => SendingPurpose.fromJson(x))),
        relationships: List<Relationship>.from(
            json["relationships"].map((x) => Relationship.fromJson(x))),
        accountTypes: (json["account_types"] ?? json["accountTypes"]) != null
            ? List<AccountType>.from(
                (json["account_types"] ?? json["accountTypes"])
                    .map((x) => AccountType.fromJson(x)))
            : [],
      );

  Data copyWith({
    Beneficiaries? beneficiaries,
    List<Country>? countries,
    List<SendingPurpose>? sendingPurposes,
    List<Relationship>? relationships,
    List<AccountType>? accountTypes,
  }) =>
      Data(
        beneficiaries: beneficiaries ?? this.beneficiaries,
        countries: countries ?? this.countries,
        sendingPurposes: sendingPurposes ?? this.sendingPurposes,
        relationships: relationships ?? this.relationships,
        accountTypes: accountTypes ?? this.accountTypes,
      );
}

class Beneficiaries {
  final int currentPage;
  final int lastPage;
  final List<BeneficiaryModel> data;

  Beneficiaries(
      {required this.currentPage, required this.lastPage, required this.data});

  factory Beneficiaries.fromJson(Map<String, dynamic> json) => Beneficiaries(
        currentPage: json["current_page"],
        lastPage: json["last_page"],
        data: List<BeneficiaryModel>.from(
            json["data"].map((x) => BeneficiaryModel.fromJson(x))),
      );

  Beneficiaries copyWith({
    int? currentPage,
    int? lastPage,
    List<BeneficiaryModel>? data,
  }) =>
      Beneficiaries(
        currentPage: currentPage ?? this.currentPage,
        lastPage: lastPage ?? this.lastPage,
        data: data ?? this.data,
      );
}

class SendingPurpose extends Equatable {
  final String title;

  const SendingPurpose({required this.title});

  factory SendingPurpose.fromJson(Map<String, dynamic> json) =>
      SendingPurpose(title: json["title"]);

  @override
  List<Object?> get props => [title];
}

class Relationship extends Equatable {
  final int id;
  final String title;

  const Relationship({required this.id, required this.title});

  factory Relationship.fromJson(Map<String, dynamic> json) => Relationship(
        id: json["id"],
        title: json["title"],
      );

  @override
  List<Object?> get props => [id, title];
}
