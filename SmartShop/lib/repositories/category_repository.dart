import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';
import '../models/category_model.dart';

class CategoryRepository {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref().child('categories');

  Stream<List<CategoryModel>> getCategories() {
    return _dbRef.onValue.map((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data == null) return [];

      return data.entries.map((entry) {
        final value = Map<String, dynamic>.from(entry.value);
        return CategoryModel(
          id: entry.key.toString(), // ID will be numeric string
          name: value['name'] ?? '',
          icon: Icons.category,
          color: Color(value['color'] ?? 0xFF1A237E),
        );
      }).toList();
    });
  }

  Future<void> addCategory(CategoryModel category) async {
    // Generate numeric ID based on timestamp
    String numericId = DateTime.now().millisecondsSinceEpoch.toString();
    await _dbRef.child(numericId).set({
      'name': category.name,
      'icon': 'category',
      'color': category.color.value,
    });
  }

  Future<void> updateCategory(CategoryModel category) async {
    await _dbRef.child(category.id).update({
      'name': category.name,
      'icon': 'category',
      'color': category.color.value,
    });
  }

  Future<void> deleteCategory(String categoryId) async {
    await _dbRef.child(categoryId).remove();
  }
}
