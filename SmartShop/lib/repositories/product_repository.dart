import 'package:firebase_database/firebase_database.dart';
import '../models/product_model.dart';

class ProductRepository {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref().child('products');

  Stream<List<ProductModel>> getFeaturedProducts() {
    return _dbRef.onValue.map((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data == null) return [];
      
      return data.entries.map((entry) {
        // Since we don't have entry as DataSnapshot directly here in map
        // We'll simulate it for our model factory or use a different approach
        final value = Map<String, dynamic>.from(entry.value);
        return ProductModel(
          id: entry.key,
          name: value['name'] ?? '',
          description: value['description'] ?? '',
          price: (value['price'] ?? 0).toDouble(),
          imageUrl: value['imageUrl'] ?? '',
          categoryId: value['categoryId'] ?? '',
          rating: (value['rating'] ?? 0).toDouble(),
        );
      }).toList();
    });
  }

  Future<void> addProduct(ProductModel product) async {
    await _dbRef.push().set(product.toMap());
  }

  Future<void> updateProduct(ProductModel product) async {
    await _dbRef.child(product.id).update(product.toMap());
  }

  Future<void> deleteProduct(String productId) async {
    await _dbRef.child(productId).remove();
  }
}
