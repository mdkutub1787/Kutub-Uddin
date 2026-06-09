import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import '../models/category_model.dart';

class CategoryRepository {
  final CollectionReference _categoriesRef =
      FirebaseFirestore.instance.collection('categories');

  Stream<List<CategoryModel>> getCategories() {
    return _categoriesRef.snapshots().map((snapshot) {
      return snapshot.docs.map((doc) => CategoryModel.fromFirestore(doc)).toList();
    });
  }

  Future<void> addCategory(CategoryModel category) async {
    await _categoriesRef.add({
      'name': category.name,
      'icon': _getIconName(category.icon),
      'color': category.color.value,
    });
  }

  Future<void> updateCategory(CategoryModel category) async {
    await _categoriesRef.doc(category.id).update({
      'name': category.name,
      'icon': _getIconName(category.icon),
      'color': category.color.value,
    });
  }

  Future<void> deleteCategory(String categoryId) async {
    await _categoriesRef.doc(categoryId).delete();
  }

  String _getIconName(IconData icon) {
    if (icon == Icons.medical_services) return 'medical_services';
    if (icon == Icons.shopping_basket) return 'shopping_basket';
    if (icon == Icons.face) return 'face';
    if (icon == Icons.handyman) return 'handyman';
    if (icon == Icons.checkroom) return 'checkroom';
    return 'help_outline';
  }
}
