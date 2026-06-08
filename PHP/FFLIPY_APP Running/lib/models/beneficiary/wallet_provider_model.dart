import 'package:equatable/equatable.dart';

class WalletProviderResponse {
  final bool success;
  final String message;
  final List<WalletProvider> data;

  WalletProviderResponse({
    required this.success,
    required this.message,
    required this.data,
  });

  factory WalletProviderResponse.fromJson(Map<String, dynamic> json) =>
      WalletProviderResponse(
        success: json["success"],
        message: json["message"],
        data: List<WalletProvider>.from(
            json["data"].map((x) => WalletProvider.fromJson(x))),
      );
}

class WalletProvider extends Equatable {
  final int id;
  final String name;

  const WalletProvider({
    required this.id,
    required this.name,
  });

  factory WalletProvider.fromJson(Map<String, dynamic> json) => WalletProvider(
        id: json["id"],
        name: json["name"],
      );

  @override
  List<Object?> get props => [id, name];
}
