import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';
import '../models/category_model.dart';

class CategoryRepository {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref().child('categories');

  Stream<List<CategoryModel>> getCategoriesByShop(String shopId) {
    return _dbRef.orderByChild('shopId').equalTo(shopId).onValue.map((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data == null) return [];

      return data.entries.map((entry) {
        return CategoryModel.fromSnapshot(event.snapshot.child(entry.key));
      }).toList();
    });
  }

  Stream<List<CategoryModel>> getAllCategories() {
    return _dbRef.onValue.map((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data == null) return [];

      return data.entries.map((entry) {
        return CategoryModel.fromSnapshot(event.snapshot.child(entry.key));
      }).toList();
    });
  }

  Future<void> addCategory(CategoryModel category) async {
    String numericId = DateTime.now().millisecondsSinceEpoch.toString();
    await _dbRef.child(numericId).set({
      'shopId': category.shopId,
      'name': category.name,
      'icon': CategoryModel.getIconName(category.icon),
      'color': category.color.value,
    });
  }

  Future<void> updateCategory(CategoryModel category) async {
    await _dbRef.child(category.id).update({
      'shopId': category.shopId,
      'name': category.name,
      'icon': CategoryModel.getIconName(category.icon),
      'color': category.color.value,
    });
  }

  Future<void> deleteCategory(String categoryId) async {
    await _dbRef.child(categoryId).remove();
  }
}
