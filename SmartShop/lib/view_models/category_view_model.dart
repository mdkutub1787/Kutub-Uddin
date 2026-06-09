import 'package:flutter/material.dart';
import '../models/category_model.dart';
import '../repositories/category_repository.dart';

class CategoryViewModel extends ChangeNotifier {
  final CategoryRepository _repository = CategoryRepository();
  
  List<CategoryModel> _categories = [];
  bool _isLoading = false;
  String? _errorMessage;

  List<CategoryModel> get categories => _categories;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  CategoryViewModel() {
    _fetchCategories();
  }

  void _fetchCategories() {
    _isLoading = true;
    notifyListeners();

    _repository.getCategories().listen((categoryList) {
      _categories = categoryList;
      _isLoading = false;
      _errorMessage = null;
      notifyListeners();
    }, onError: (error) {
      _isLoading = false;
      _errorMessage = error.toString();
      notifyListeners();
    });
  }

  Future<void> addCategory(CategoryModel category) async {
    await _repository.addCategory(category);
  }

  Future<void> updateCategory(CategoryModel category) async {
    await _repository.updateCategory(category);
  }

  Future<void> deleteCategory(String categoryId) async {
    await _repository.deleteCategory(categoryId);
  }
}
