class ProductModel {
  final String id;
  final String shopId;
  final String name;
  final String description;
  final double originalPrice;
  final double price;
  final double discountValue;
  final String discountType;
  final String imageUrl;
  final String categoryId;
  final double rating;
  final int stock;

  ProductModel({
    required this.id,
    required this.shopId,
    required this.name,
    required this.description,
    required this.originalPrice,
    required this.price,
    this.discountValue = 0,
    this.discountType = 'none',
    required this.imageUrl,
    required this.categoryId,
    required this.rating,
    required this.stock,
  });

  factory ProductModel.fromJson(Map<String, dynamic> json) {
    return ProductModel(
      id: json['id']?.toString() ?? '',
      shopId: json['shopId']?.toString() ?? '',
      name: json['name'] ?? '',
      description: json['description'] ?? '',
      originalPrice: (json['originalPrice'] ?? (json['price'] ?? 0)).toDouble(),
      price: (json['price'] ?? 0).toDouble(),
      discountValue: (json['discountValue'] ?? 0).toDouble(),
      discountType: json['discountType'] ?? 'none',
      imageUrl: json['imageUrl'] ?? 'https://via.placeholder.com/150',
      categoryId: json['categoryId']?.toString() ?? '',
      rating: (json['rating'] ?? 0).toDouble(),
      stock: json['stock'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'shopId': shopId,
      'name': name,
      'description': description,
      'originalPrice': originalPrice,
      'price': price,
      'discountValue': discountValue,
      'discountType': discountType,
      'imageUrl': imageUrl,
      'categoryId': categoryId,
      'rating': rating,
      'stock': stock,
    };
  }

  bool get hasDiscount => discountType != 'none' && discountValue > 0;
}
