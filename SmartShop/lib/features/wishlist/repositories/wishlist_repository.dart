import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/constants/constants.dart';
import '../../product/models/product_model.dart';

class WishlistRepository {
  final SupabaseClient _supabase;

  WishlistRepository(this._supabase);

  Future<List<ProductModel>> getWishlist(String userId) async {
    final response = await _supabase
        .from(AppConstants.wishlistTable)
        .select('product_id, products(*)')
        .eq('user_id', userId);
    
    return (response as List).map((json) => ProductModel.fromJson(json['products'])).toList();
  }

  Future<void> addToWishlist(String userId, String productId) async {
    await _supabase.from(AppConstants.wishlistTable).insert({
      'user_id': userId,
      'product_id': productId,
    });
  }

  Future<void> removeFromWishlist(String userId, String productId) async {
    await _supabase.from(AppConstants.wishlistTable).delete().match({
      'user_id': userId,
      'product_id': productId,
    });
  }

  Future<bool> isInWishlist(String userId, String productId) async {
    final response = await _supabase
        .from(AppConstants.wishlistTable)
        .select()
        .match({
          'user_id': userId,
          'product_id': productId,
        })
        .maybeSingle();
    return response != null;
  }
}
