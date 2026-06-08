import 'package:equatable/equatable.dart';

class BankResponse {
  final bool success;
  final String message;
  final List<Bank> data;

  BankResponse({
    required this.success,
    required this.message,
    required this.data,
  });

  factory BankResponse.fromJson(Map<String, dynamic> json) => BankResponse(
        success: json["success"],
        message: json["message"],
        data: List<Bank>.from(json["data"].map((x) => Bank.fromJson(x))),
      );
}

class Bank extends Equatable {
  final int id;
  final String bankName;

  const Bank({
    required this.id,
    required this.bankName,
  });

  factory Bank.fromJson(Map<String, dynamic> json) => Bank(
        id: json["id"],
        bankName: json["bank_name"],
      );

  @override
  List<Object?> get props => [id, bankName];
}
