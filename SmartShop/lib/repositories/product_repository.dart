import 'package:firebase_database/firebase_database.dart';
import '../models/product_model.dart';

class ProductRepository {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref().child('products');

  Stream<List<ProductModel>> getFeaturedProducts() {
    return _dbRef.onValue.map((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data == null) return [];
      
      return data.entries.map((entry) {
        return ProductModel.fromSnapshot(event.snapshot.child(entry.key));
      }).toList();
    });
  }

  Future<void> addProduct(ProductModel product) async {
    // Generate numeric ID based on timestamp
    String numericId = DateTime.now().millisecondsSinceEpoch.toString();
    await _dbRef.child(numericId).set(product.toMap());
  }

  Future<void> updateProduct(ProductModel product) async {
    await _dbRef.child(product.id).update(product.toMap());
  }

  Future<void> deleteProduct(String productId) async {
    await _dbRef.child(productId).remove();
  }
}
