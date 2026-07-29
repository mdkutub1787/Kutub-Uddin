import 'package:flutter/foundation.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/constants/constants.dart';
import '../models/product_model.dart';
import 'package:uuid/uuid.dart';

class ProductRepository {
  final SupabaseClient _supabase;

  ProductRepository(this._supabase);

  Stream<List<ProductModel>> getProductsByShop(String shopId) {
    return _supabase
        .from(AppConstants.productsTable)
        .stream(primaryKey: ['id'])
        .eq('shopId', shopId)
        .map((data) => data.map((json) => ProductModel.fromJson(json)).toList());
  }

  Stream<List<ProductModel>> getAllProducts() {
    return _supabase
        .from(AppConstants.productsTable)
        .stream(primaryKey: ['id'])
        .map((data) => data.map((json) => ProductModel.fromJson(json)).toList());
  }

  Future<ProductModel> addProduct(ProductModel product) async {
    final uuid = const Uuid().v4();
    final json = product.toJson();
    json['id'] = uuid; 

    await _supabase.from(AppConstants.productsTable).insert(json);
    return ProductModel.fromJson(json);
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
