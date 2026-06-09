import 'package:firebase_database/firebase_database.dart';

class ProductModel {
  final String id;
  final String name;
  final String description;
  final double originalPrice; // Regular price
  final double price; // Discounted price (what user pays)
  final double discountValue;
  final String discountType; // 'flat', 'percentage', 'none'
  final String imageUrl;
  final String categoryId;
  final double rating;
  final int stock;

  ProductModel({
    required this.id,
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

  factory ProductModel.fromSnapshot(DataSnapshot snapshot) {
    Map<dynamic, dynamic> data = snapshot.value as Map<dynamic, dynamic>;
    return ProductModel(
      id: snapshot.key ?? '',
      name: data['name'] ?? '',
      description: data['description'] ?? '',
      originalPrice: (data['originalPrice'] ?? (data['price'] ?? 0)).toDouble(),
      price: (data['price'] ?? 0).toDouble(),
      discountValue: (data['discountValue'] ?? 0).toDouble(),
      discountType: data['discountType'] ?? 'none',
      imageUrl: data['imageUrl'] ?? 'https://via.placeholder.com/150',
      categoryId: data['categoryId'] ?? '',
      rating: (data['rating'] ?? 0).toDouble(),
      stock: data['stock'] ?? 0,
    );
  }

  Map<String, dynamic> toMap() {
    return {
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
