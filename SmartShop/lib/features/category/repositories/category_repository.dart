import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/providers.dart';
import '../../../core/constants/constants.dart';
import '../models/category_model.dart';

class CategoryRepository {
  final SupabaseClient _supabase;
  static const String _cacheKey = 'cached_categories';

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
      // 1. Try to fetch from Supabase
      final response = await _supabase.from(AppConstants.categoriesTable).select();
      final categories = (response as List).map((json) => CategoryModel.fromJson(json)).toList();
      
      // 2. Cache the result locally
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(_cacheKey, json.encode(response));
      
      return categories;
    } catch (e) {
      debugPrint('❌ ERROR (Supabase): $e. Trying local cache...');
      
      // 3. Fallback to local cache if network fails
      try {
        final prefs = await SharedPreferences.getInstance();
        final cachedData = prefs.getString(_cacheKey);
        if (cachedData != null) {
          final List decoded = json.decode(cachedData);
          return decoded.map((json) => CategoryModel.fromJson(json)).toList();
        }
      } catch (cacheError) {
        debugPrint('❌ ERROR (Cache): $cacheError');
      }
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
