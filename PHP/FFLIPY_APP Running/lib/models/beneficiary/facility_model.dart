import 'package:equatable/equatable.dart';

class FacilityResponse {
  final bool success;
  final String message;
  final List<Facility> data;

  FacilityResponse({
    required this.success,
    required this.message,
    required this.data,
  });

  factory FacilityResponse.fromJson(Map<String, dynamic> json) => FacilityResponse(
        success: json["success"],
        message: json["message"],
        data: List<Facility>.from(json["data"].map((x) => Facility.fromJson(x))),
      );
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
