import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/providers.dart';
import '../models/category_model.dart';
import '../repositories/category_repository.dart';

final categoryNotifierProvider = AsyncNotifierProvider<CategoryNotifier, List<CategoryModel>>(() {
  return CategoryNotifier();
});

class CategoryNotifier extends AsyncNotifier<List<CategoryModel>> {
  late final CategoryRepository _repository;

  @override
  FutureOr<List<CategoryModel>> build() async {
    _repository = CategoryRepository(ref.watch(supabaseClientProvider));
    return await _fetchCategories();
  }

  Future<List<CategoryModel>> _fetchCategories() async {
    final user = ref.read(authNotifierProvider).value;
    if (user != null) {
      try {
        return await _repository.getAllCategories();
      } catch (e) {
        if (e.toString().contains('JWT issued at future')) {
          // Wait 2 seconds and retry once if it's a clock skew issue
          await Future.delayed(const Duration(seconds: 2));
          return await _repository.getAllCategories();
        }
        rethrow;
      }
    }
    return [];
  }

  Future<void> loadCategories() async {
    state = const AsyncValue.loading();
    try {
      final categories = await _fetchCategories();
      state = AsyncValue.data(categories);
    } catch (e, stackTrace) {
      state = AsyncValue.error(e, stackTrace);
    }
  }

  Future<void> addCategory(CategoryModel category) async {
    try {
      await _repository.addCategory(category);
      await loadCategories(); // Refresh the list
    } catch (e) {
      rethrow;
    }
  }

  Future<void> updateCategory(CategoryModel category) async {
    try {
      await _repository.updateCategory(category);
      await loadCategories(); // Refresh the list
    } catch (e) {
      rethrow;
    }
  }

  Future<void> deleteCategory(String categoryId) async {
    try {
      await _repository.deleteCategory(categoryId);
      await loadCategories(); // Refresh the list
    } catch (e) {
      rethrow;
    }
  }
}
