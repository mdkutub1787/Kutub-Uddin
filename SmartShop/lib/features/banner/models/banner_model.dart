class BannerModel {
  final String id;
  final String shopId;
  final String imageUrl;
  final String tag;
  final String title;
  final String subtitle;
  final String actionText;

  BannerModel({
    required this.id,
    required this.shopId,
    required this.imageUrl,
    required this.tag,
    required this.title,
    required this.subtitle,
    required this.actionText,
  });

  factory BannerModel.fromJson(Map<String, dynamic> json) {
    return BannerModel(
      id: json['id']?.toString() ?? '',
      shopId: json['shopId']?.toString() ?? '',
      imageUrl: json['image_url'] ?? '',
      tag: json['tag'] ?? '',
      title: json['title'] ?? '',
      subtitle: json['subtitle'] ?? '',
      actionText: json['action_text'] ?? 'Shop Now',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'shopId': shopId,
      'image_url': imageUrl,
      'tag': tag,
      'title': title,
      'subtitle': subtitle,
      'action_text': actionText,
    };
  }
}
