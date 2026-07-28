import 'package:flutter/foundation.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/providers.dart';
import '../../../core/constants/constants.dart';
import '../models/category_model.dart';

class CategoryRepository {
  final SupabaseClient _supabase;

  CategoryRepository(this._supabase);

  Future<List<CategoryModel>> getCategoriesByShop(String shopId) async {
    try {
      final response = await _supabase
          .from(AppConstants.categoriesTable)
          .select()
          .eq('shopId', shopId);
      return (response as List).map((json) => CategoryModel.fromJson(json)).toList();
    } catch (e) {
      debugPrint('❌ ERROR: $e');
      return [];
    }
  }

  Future<List<CategoryModel>> getAllCategories() async {
    try {
      final response = await _supabase.from(AppConstants.categoriesTable).select();
      return (response as List).map((json) => CategoryModel.fromJson(json)).toList();
    } catch (e) {
      debugPrint('❌ ERROR: $e');
      rethrow;
    }
  }

  Future<void> addCategory(CategoryModel category) async {
    await _supabase.from(AppConstants.categoriesTable).insert(category.toJson());
  }

  Future<void> updateCategory(CategoryModel category) async {
    await _supabase
        .from(AppConstants.categoriesTable)
        .update(category.toJson())
        .eq('id', category.id);
  }

  Future<void> deleteCategory(String categoryId) async {
    await _supabase.from(AppConstants.categoriesTable).delete().eq('id', categoryId);
  }
}

final categoryRepositoryProvider = Provider<CategoryRepository>((ref) {
  return CategoryRepository(ref.watch(supabaseClientProvider));
});
