import 'package:flutter/foundation.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../../../core/constants/constants.dart';
import '../models/product_model.dart';

class ProductRepository {
  final SupabaseClient _supabase;

  ProductRepository(this._supabase);

  Stream<List<ProductModel>> getProductsByShop(String shopId) {
    debugPrint('📡 INFO: Fetching products for shop: $shopId');
    return _supabase
        .from(AppConstants.productsTable)
        .stream(primaryKey: ['id'])
        .eq('shopId', shopId)
        .map((data) {
          debugPrint('✅ SUCCESS: Loaded ${data.length} products for shop $shopId');
          return data.map((json) => ProductModel.fromJson(json)).toList();
        });
  }

  Stream<List<ProductModel>> getAllProducts() {
    debugPrint('📡 INFO: Fetching all products');
    return _supabase
        .from(AppConstants.productsTable)
        .stream(primaryKey: ['id'])
        .map((data) {
          debugPrint('✅ SUCCESS: Loaded ${data.length} products globally');
          return data.map((json) => ProductModel.fromJson(json)).toList();
        });
  }

  Future<void> addProduct(ProductModel product) async {
    await _supabase.from(AppConstants.productsTable).insert(product.toJson());
  }

  Future<void> updateProduct(ProductModel product) async {
    await _supabase
        .from(AppConstants.productsTable)
        .update(product.toJson())
        .eq('id', product.id);
  }

  Future<void> deleteProduct(String productId) async {
    await _supabase.from(AppConstants.productsTable).delete().eq('id', productId);
  }
}

final productRepositoryProvider = Provider<ProductRepository>((ref) {
  return ProductRepository(ref.watch(supabaseClientProvider));
});
