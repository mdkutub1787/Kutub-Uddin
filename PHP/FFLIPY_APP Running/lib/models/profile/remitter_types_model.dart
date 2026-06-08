class RemitterTypesResponse {
    final bool success;
    final int status;
    final String message;
    final List<RemitterType> data;

    RemitterTypesResponse({
        required this.success,
        required this.status,
        required this.message,
        required this.data,
    });

    factory RemitterTypesResponse.fromJson(Map<String, dynamic> json) => RemitterTypesResponse(
        success: json["success"],
        status: json["status"],
        message: json["message"],
        data: List<RemitterType>.from(json["data"].map((x) => RemitterType.fromJson(x))),
    );
}

class RemitterType {
    final int id;
    final String name;

    RemitterType({
        required this.id,
        required this.name,
    });

    factory RemitterType.fromJson(Map<String, dynamic> json) => RemitterType(
        id: json["id"],
        name: json["name"],
    );
}
