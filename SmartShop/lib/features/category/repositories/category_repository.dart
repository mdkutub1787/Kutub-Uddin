import 'package:supabase_flutter/supabase_flutter.dart';
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
      final response = await _supabase.from(_table).select();
      return (response as List).map((json) => CategoryModel.fromJson(json)).toList();
    } catch (e) {
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
