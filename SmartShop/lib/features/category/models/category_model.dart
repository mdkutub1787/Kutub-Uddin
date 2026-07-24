class CategoryModel {
  final String id;
  final String shopId;
  final String name;
  final String imageUrl;

  CategoryModel({
    required this.id,
    required this.shopId,
    required this.name,
    required this.imageUrl,
  });

  factory CategoryModel.fromJson(Map<String, dynamic> json) {
    return CategoryModel(
      id: json['id']?.toString() ?? '',
      shopId: json['shopId']?.toString() ?? '',
      name: json['name'] ?? '',
      imageUrl: json['imageUrl'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'shopId': shopId,
      'name': name,
      'imageUrl': imageUrl,
    };
  }
}
