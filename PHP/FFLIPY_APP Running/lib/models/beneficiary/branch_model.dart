import 'package:equatable/equatable.dart';

class BranchResponse {
  final bool success;
  final String message;
  final List<Branch> data;

  BranchResponse({
    required this.success,
    required this.message,
    required this.data,
  });

  factory BranchResponse.fromJson(Map<String, dynamic> json) => BranchResponse(
        success: json["success"],
        message: json["message"],
        data: List<Branch>.from(json["data"].map((x) => Branch.fromJson(x))),
      );
}

class Branch extends Equatable {
  final int id;
  final String branchName;

  const Branch({
    required this.id,
    required this.branchName,
  });

  factory Branch.fromJson(Map<String, dynamic> json) => Branch(
        id: json["id"],
        branchName: json["branch_name"],
      );

  @override
  List<Object?> get props => [id, branchName];
}
