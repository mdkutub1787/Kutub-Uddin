class UpdateBeneficiaryRequest {
  final int? id;
  final String firstName;
  final String lastName;
  final String email;
  final String phoneNumber;
  final String address;
  final String countryId;
  final String city;
  final String relationshipToSender;
  final dynamic transactionType;
  final String? bankId;
  final String? branchId;
  final String? accountNumber;
  final String? walletProvider;
  final String? walletNumber;
  final String? accountType;

  UpdateBeneficiaryRequest({
    this.id,
    required this.firstName,
    required this.lastName,
    required this.email,
    required this.phoneNumber,
    required this.address,
    required this.countryId,
    required this.city,
    required this.relationshipToSender,
    required this.transactionType,
    this.bankId,
    this.branchId,
    this.accountNumber,
    this.walletProvider,
    this.walletNumber,
    this.accountType,
  });

  Map<String, dynamic> toJson() => {
        'beneficiary_id': id,
        'FirstName': firstName,
        'LastName': lastName,
        'Email': email,
        'PhoneNumber': phoneNumber,
        'Address1': address,
        'CountryCode': countryId,
        'CityCode': city,
        'RelationshipToSender': relationshipToSender,
        'TransactionType': transactionType,
        'bank_name': bankId,
        'branch_name': branchId,
        'AccountNumber': accountNumber,
        'WalletProvider': walletProvider,
        'WalletNumber': walletNumber,
        'account_type': accountType,
      }..removeWhere((key, value) => value == null);
}
