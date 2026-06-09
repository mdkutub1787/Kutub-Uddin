import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/product_model.dart';

class ProductRepository {
  final CollectionReference _productsRef =
      FirebaseFirestore.instance.collection('products');

  Stream<List<ProductModel>> getFeaturedProducts() {
    return _productsRef.limit(10).snapshots().map((snapshot) {
      return snapshot.docs.map((doc) => ProductModel.fromFirestore(doc)).toList();
    });
  }

  Stream<List<ProductModel>> getProductsByCategory(String categoryId) {
    return _productsRef
        .where('categoryId', isEqualTo: categoryId)
        .snapshots()
        .map((snapshot) {
      return snapshot.docs.map((doc) => ProductModel.fromFirestore(doc)).toList();
    });
  }

  Future<void> addProduct(ProductModel product) async {
    await _productsRef.add({
      'name': product.name,
      'description': product.description,
      'price': product.price,
      'imageUrl': product.imageUrl,
      'categoryId': product.categoryId,
      'rating': product.rating,
    });
  }

  Future<void> updateProduct(ProductModel product) async {
    await _productsRef.doc(product.id).update({
      'name': product.name,
      'description': product.description,
      'price': product.price,
      'imageUrl': product.imageUrl,
      'categoryId': product.categoryId,
      'rating': product.rating,
    });
  }

  Future<void> deleteProduct(String productId) async {
    await _productsRef.doc(productId).delete();
  }
}
