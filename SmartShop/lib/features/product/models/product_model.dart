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
  final List<String> images;
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
    this.images = const [],
    required this.categoryId,
    required this.rating,
    required this.stock,
  });

  factory ProductModel.fromJson(Map<String, dynamic> json) {
    List<String> imageList = [];
    if (json['images'] != null && json['images'] is List) {
      imageList = List<String>.from(json['images']);
    }
    
    // If images list is empty, put primary imageUrl in it
    if (imageList.isEmpty && json['imageUrl'] != null) {
      imageList = [json['imageUrl']];
    }

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
      images: imageList,
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
