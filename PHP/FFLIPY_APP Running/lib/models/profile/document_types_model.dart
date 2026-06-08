class DocumentTypesResponse {
    final bool success;
    final int status;
    final String message;
    final List<DocumentType> data;

    DocumentTypesResponse({
        required this.success,
        required this.status,
        required this.message,
        required this.data,
    });

    factory DocumentTypesResponse.fromJson(Map<String, dynamic> json) => DocumentTypesResponse(
        success: json["success"],
        status: json["status"],
        message: json["message"],
        data: List<DocumentType>.from(json["data"].map((x) => DocumentType.fromJson(x))),
    );
}

class DocumentType {
    final int id;
    final String documentCode;
    final String documentType;
    final DateTime? createdAt;
    final DateTime? updatedAt;

    DocumentType({
        required this.id,
        required this.documentCode,
        required this.documentType,
        this.createdAt,
        this.updatedAt,
    });

    factory DocumentType.fromJson(Map<String, dynamic> json) => DocumentType(
        id: json["id"],
        documentCode: json["document_code"],
        documentType: json["document_type"],
        createdAt: json["created_at"] == null ? null : DateTime.parse(json["created_at"]),
        updatedAt: json["updated_at"] == null ? null : DateTime.parse(json["updated_at"]),
    );
}
