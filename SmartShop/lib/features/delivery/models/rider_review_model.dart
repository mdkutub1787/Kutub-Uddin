class RiderReviewModel {
  final String id;
  final String riderId;
  final String orderId;
  final String userId;
  final String userName;
  final String? userImageUrl;
  final double rating;
  final String comment;
  final DateTime createdAt;

  RiderReviewModel({
    required this.id,
    required this.riderId,
    required this.orderId,
    required this.userId,
    required this.userName,
    this.userImageUrl,
    required this.rating,
    required this.comment,
    required this.createdAt,
  });

  factory RiderReviewModel.fromJson(Map<String, dynamic> json) {
    return RiderReviewModel(
      id: json['id'].toString(),
      riderId: json['riderId'],
      orderId: json['orderId'],
      userId: json['userId'],
      userName: json['userName'] ?? 'User',
      userImageUrl: json['userImageUrl'],
      rating: (json['rating'] ?? 0).toDouble(),
      comment: json['comment'] ?? '',
      createdAt: DateTime.parse(json['created_at'] ?? DateTime.now().toIso8601String()),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'riderId': riderId,
      'orderId': orderId,
      'userId': userId,
      'userName': userName,
      'userImageUrl': userImageUrl,
      'rating': rating,
      'comment': comment,
    };
  }
}
