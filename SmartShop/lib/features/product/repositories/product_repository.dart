import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/product_model.dart';

class ProductRepository {
  final SupabaseClient _supabase;
  final String _table = 'products';

  ProductRepository(this._supabase);

  Stream<List<ProductModel>> getProductsByShop(String shopId) {
    return _supabase
        .from(_table)
        .stream(primaryKey: ['id'])
        .eq('shopId', shopId)
        .map((data) => data.map((json) => ProductModel.fromJson(json)).toList());
  }

  Stream<List<ProductModel>> getAllProducts() {
    return _supabase
        .from(_table)
        .stream(primaryKey: ['id'])
        .map((data) => data.map((json) => ProductModel.fromJson(json)).toList());
  }

  Future<void> addProduct(ProductModel product) async {
    await _supabase.from(_table).insert(product.toJson());
  }

  Future<void> updateProduct(ProductModel product) async {
    await _supabase.from(_table).update(product.toJson()).eq('id', product.id);
  }

  Future<void> deleteProduct(String productId) async {
    await _supabase.from(_table).delete().eq('id', productId);
  }
}

final productRepositoryProvider = Provider<ProductRepository>((ref) {
  return ProductRepository(Supabase.instance.client);
});
