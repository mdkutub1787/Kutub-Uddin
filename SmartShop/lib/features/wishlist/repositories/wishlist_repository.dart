import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';
import '../../product/models/product_model.dart';

class WishlistRepository {
  final SupabaseClient _supabase;

  WishlistRepository(this._supabase);

  Future<List<ProductModel>> getWishlist(String userId) async {
    try {
      final response = await _supabase
          .from(AppConstants.wishlistTable)
          .select('product_id, products(*)')
          .eq('user_id', userId);
      
      return (response as List).map((json) {
        final productJson = json['products'];
        // Ensure ID is included in the product json
        productJson['id'] = json['product_id'];
        return ProductModel.fromJson(productJson);
      }).toList();
    } catch (e) {
      return [];
    }
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

final wishlistRepositoryProvider = Provider<WishlistRepository>((ref) {
  return WishlistRepository(ref.watch(supabaseClientProvider));
});
