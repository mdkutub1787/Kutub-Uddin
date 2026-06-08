import 'package:equatable/equatable.dart';

class AccountType extends Equatable {
  final String id;
  final String name;

  const AccountType({required this.id, required this.name});

  factory AccountType.fromJson(Map<String, dynamic> json) {
    return AccountType(
      id: json['id'].toString(),
      name: json['name'] as String,
    );
  }

  @override
  List<Object?> get props => [id, name];
}

class AccountTypeResponse {
  final bool success;
  final String message;
  final List<AccountType> data;

  AccountTypeResponse({
    required this.success,
    required this.message,
    required this.data,
  });

  factory AccountTypeResponse.fromJson(Map<String, dynamic> json) {
    var dataList = json['data'] as List;
    List<AccountType> accountTypes =
        dataList.map((i) => AccountType.fromJson(i)).toList();
    return AccountTypeResponse(
      success: json['success'],
      message: json['message'],
      data: accountTypes,
    );
  }
}
