import 'package:flutter/foundation.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/providers.dart';
import '../models/category_model.dart';

class CategoryRepository {
  final SupabaseClient _supabase;
  static const String _table = 'categories';

  CategoryRepository(this._supabase);

  Future<List<CategoryModel>> getCategoriesByShop(String shopId) async {
    try {
      final response = await _supabase
          .from(_table)
          .select()
          .eq('shopId', shopId);
      return (response as List).map((json) => CategoryModel.fromJson(json)).toList();
    } catch (e) {
      return [];
    }
  }

  Future<List<CategoryModel>> getAllCategories() async {
    try {
      debugPrint('📡 INFO: Fetching all categories');
      final response = await _supabase.from(_table).select();
      debugPrint('✅ SUCCESS: Loaded ${(response as List).length} categories');
      return response.map((json) => CategoryModel.fromJson(json)).toList();
    } catch (e) {
      debugPrint('❌ ERROR: Failed to load categories -> $e');
      rethrow;
    }
  }

  Future<void> addCategory(CategoryModel category) async {
    await _supabase.from(_table).insert(category.toJson());
  }

  Future<void> updateCategory(CategoryModel category) async {
    await _supabase.from(_table).update(category.toJson()).eq('id', category.id);
  }

  Future<void> deleteCategory(String categoryId) async {
    await _supabase.from(_table).delete().eq('id', categoryId);
  }
}

final categoryRepositoryProvider = Provider<CategoryRepository>((ref) {
  return CategoryRepository(ref.watch(supabaseClientProvider));
});
