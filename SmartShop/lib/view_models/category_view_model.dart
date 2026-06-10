import 'package:flutter/material.dart';
import '../models/category_model.dart';
import '../repositories/category_repository.dart';

class CategoryViewModel extends ChangeNotifier {
  final CategoryRepository _repository = CategoryRepository();
  
  List<CategoryModel> _categories = [];
  bool _isLoading = false;

  List<CategoryModel> get categories => _categories;
  bool get isLoading => _isLoading;

  CategoryViewModel() {
    // Categories will be fetched when user login is detected or manually
  }

  Future<void> refreshCategories({String? shopId}) async {
    _isLoading = true;
    notifyListeners();
    final stream = shopId != null 
        ? _repository.getCategoriesByShop(shopId)
        : _repository.getAllCategories();
        
    await for (final list in stream) {
      _categories = list;
      _isLoading = false;
      notifyListeners();
      break;
    }
  }

  void fetchCategories({String? shopId}) {
    _isLoading = true;
    final stream = shopId != null 
        ? _repository.getCategoriesByShop(shopId)
        : _repository.getAllCategories();
        
    stream.listen((categoryList) {
      _categories = categoryList;
      _isLoading = false;
      notifyListeners();
    });
  }

  Future<void> addCategory(CategoryModel category) async {
    _isLoading = true;
    notifyListeners();
    await _repository.addCategory(category);
    _isLoading = false;
    notifyListeners();
  }

  Future<void> updateCategory(CategoryModel category) async {
    _isLoading = true;
    notifyListeners();
    await _repository.updateCategory(category);
    _isLoading = false;
    notifyListeners();
  }

  Future<void> deleteCategory(String categoryId) async {
    await _repository.deleteCategory(categoryId);
    notifyListeners();
  }
}
