import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../admin/riverpod/activity_log_notifier.dart';
import '../../../core/providers.dart';
import '../models/category_model.dart';
import '../repositories/category_repository.dart';

final categoryNotifierProvider = AsyncNotifierProvider<CategoryNotifier, List<CategoryModel>>(() {
  return CategoryNotifier();
});

class CategoryNotifier extends AsyncNotifier<List<CategoryModel>> {
  @override
  FutureOr<List<CategoryModel>> build() async {
    return await _fetchCategories();
  }

  Future<List<CategoryModel>> _fetchCategories() async {
    final user = ref.read(authNotifierProvider).value;
    final repository = ref.watch(categoryRepositoryProvider);
    
    if (user != null && (user.role == 'owner' || user.role == 'manager') && user.shopId != null && user.shopId!.isNotEmpty) {
      try {
        return await repository.getCategoriesByShop(user.shopId!);
      } catch (e) {
        if (e.toString().contains('JWT issued at future')) {
          await Future.delayed(const Duration(seconds: 2));
          return await repository.getCategoriesByShop(user.shopId!);
        }
        rethrow;
      }
    } else {
      try {
        return await repository.getAllCategories();
      } catch (e) {
        if (e.toString().contains('JWT issued at future')) {
          await Future.delayed(const Duration(seconds: 2));
          return await repository.getAllCategories();
        }
        rethrow;
      }
    }
  }

  Future<void> loadCategories() async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(() => _fetchCategories());
  }

  Future<void> addCategory(CategoryModel category) async {
    try {
      await ref.read(categoryRepositoryProvider).addCategory(category);
      await loadCategories();
      
      // Log Activity
      final admin = ref.read(authNotifierProvider).value;
      if (admin != null) {
        await ref.read(activityLogNotifierProvider.notifier).logAction(
          adminId: admin.uid,
          adminName: admin.name,
          action: 'Category Added',
          targetId: category.name,
          details: 'New category "${category.name}" was created.',
        );
      }
    } catch (e) {
      rethrow;
    }
  }

  Future<void> updateCategory(CategoryModel category) async {
    try {
      await ref.read(categoryRepositoryProvider).updateCategory(category);
      await loadCategories();

      // Log Activity
      final admin = ref.read(authNotifierProvider).value;
      if (admin != null) {
        await ref.read(activityLogNotifierProvider.notifier).logAction(
          adminId: admin.uid,
          adminName: admin.name,
          action: 'Category Updated',
          targetId: category.name,
          details: 'Category "${category.name}" details were updated.',
        );
      }
    } catch (e) {
      rethrow;
    }
  }

  Future<void> deleteCategory(String categoryId) async {
    try {
      await ref.read(categoryRepositoryProvider).deleteCategory(categoryId);
      await loadCategories();

      // Log Activity
      final admin = ref.read(authNotifierProvider).value;
      if (admin != null) {
        await ref.read(activityLogNotifierProvider.notifier).logAction(
          adminId: admin.uid,
          adminName: admin.name,
          action: 'Category Deleted',
          targetId: categoryId,
          details: 'Category ID: $categoryId was removed.',
        );
      }
    } catch (e) {
      rethrow;
    }
  }
}
