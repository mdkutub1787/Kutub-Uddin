class DocumentInfoResponse {
    final bool success;
    final int status;
    final String message;
    final DocumentInfoData data;

    DocumentInfoResponse({
        required this.success,
        required this.status,
        required this.message,
        required this.data,
    });

    factory DocumentInfoResponse.fromJson(Map<String, dynamic> json) => DocumentInfoResponse(
        success: json["success"],
        status: json["status"],
        message: json["message"],
        data: DocumentInfoData.fromJson(json["data"]),
    );
}

class DocumentInfoData {
    final int id;
    final String userId;
    final String documentType;
    final String documentFile;
    final String documentIdNumber;
    final String issueCountryCode;
    final DateTime documentIssueDate;
    final DateTime documentExpiryDate;
    final dynamic confidenceScore;
    final String faceMatch;
    final DateTime createdAt;
    final DateTime updatedAt;

    DocumentInfoData({
        required this.id,
        required this.userId,
        required this.documentType,
        required this.documentFile,
        required this.documentIdNumber,
        required this.issueCountryCode,
        required this.documentIssueDate,
        required this.documentExpiryDate,
        this.confidenceScore,
        required this.faceMatch,
        required this.createdAt,
        required this.updatedAt,
    });

    factory DocumentInfoData.fromJson(Map<String, dynamic> json) => DocumentInfoData(
        id: json["id"],
        userId: json["user_id"],
        documentType: json["document_type"],
        documentFile: json["document_file"],
        documentIdNumber: json["document_id_number"],
        issueCountryCode: json["issue_country_code"],
        documentIssueDate: DateTime.parse(json["document_issue_date"]),
        documentExpiryDate: DateTime.parse(json["document_expiry_date"]),
        confidenceScore: json["confidence_score"],
        faceMatch: json["face_match"],
        createdAt: DateTime.parse(json["created_at"]),
        updatedAt: DateTime.parse(json["updated_at"]),
    );
}
