class GenderTypesResponse {
    final bool success;
    final int status;
    final String message;
    final List<GenderType> data;

    GenderTypesResponse({
        required this.success,
        required this.status,
        required this.message,
        required this.data,
    });

    factory GenderTypesResponse.fromJson(Map<String, dynamic> json) => GenderTypesResponse(
        success: json["success"],
        status: json["status"],
        message: json["message"],
        data: List<GenderType>.from(json["data"].map((x) => GenderType.fromJson(x))),
    );
}

class GenderType {
    final int id;
    final String name;

    GenderType({
        required this.id,
        required this.name,
    });

    factory GenderType.fromJson(Map<String, dynamic> json) => GenderType(
        id: json["id"],
        name: json["name"],
    );
}
