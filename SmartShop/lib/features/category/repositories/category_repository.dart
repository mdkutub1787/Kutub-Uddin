import 'package:supabase_flutter/supabase_flutter.dart';
import '../models/category_model.dart';

class CategoryRepository {
  final SupabaseClient _supabase;

  CategoryRepository(this._supabase);

  Future<List<CategoryModel>> getCategoriesByShop(String shopId) async {
    final response = await _supabase
        .from('categories')
        .select()
        .eq('shopId', shopId);

    return (response as List).map((json) => CategoryModel.fromJson(json)).toList();
  }

  Future<List<CategoryModel>> getAllCategories() async {
    final response = await _supabase
        .from('categories')
        .select();

    return (response as List).map((json) => CategoryModel.fromJson(json)).toList();
  }

  Future<void> addCategory(CategoryModel category) async {
    await _supabase.from('categories').insert(category.toJson());
  }

  Future<void> updateCategory(CategoryModel category) async {
    await _supabase
        .from('categories')
        .update(category.toJson())
        .eq('id', category.id);
  }

  Future<void> deleteCategory(String categoryId) async {
    await _supabase
        .from('categories')
        .delete()
        .eq('id', categoryId);
  }
}
