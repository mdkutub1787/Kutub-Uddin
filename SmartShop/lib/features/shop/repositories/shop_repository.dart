import 'package:flutter/foundation.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/providers.dart';
import '../../../core/constants/constants.dart';
import '../../../models/shop_model.dart';

class ShopRepository {
  final SupabaseClient _supabase;

  ShopRepository(this._supabase);

  Future<List<ShopModel>> getAllShops() async {
    try {
      debugPrint('📡 INFO: Fetching all shops');
      final response = await _supabase.from(AppConstants.shopsTable).select();
      debugPrint('✅ SUCCESS: Loaded ${(response as List).length} shops');
      return response.map((json) => ShopModel.fromMap(json, json['id'])).toList();
    } catch (e) {
      debugPrint('❌ ERROR: Failed to load shops -> $e');
      rethrow;
    }
  }

  Future<ShopModel> getShopById(String id) async {
    final response = await _supabase
        .from(AppConstants.shopsTable)
        .select()
        .eq('id', id)
        .single();
    return ShopModel.fromMap(response, response['id']);
  }
}

final shopRepositoryProvider = Provider<ShopRepository>((ref) {
  return ShopRepository(ref.watch(supabaseClientProvider));
});
