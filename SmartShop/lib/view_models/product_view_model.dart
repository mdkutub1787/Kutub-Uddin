import 'package:flutter/material.dart';
import '../models/product_model.dart';
import '../repositories/product_repository.dart';

class ProductViewModel extends ChangeNotifier {
  final ProductRepository _repository = ProductRepository();
  
  List<ProductModel> _featuredProducts = [];
  List<ProductModel> _filteredProducts = [];
  String _selectedCategoryId = '';
  bool _isLoading = false;

  List<ProductModel> get featuredProducts {
    List<ProductModel> products = _selectedCategoryId.isEmpty 
        ? _featuredProducts 
        : _featuredProducts.where((p) => p.categoryId == _selectedCategoryId).toList();
    
    return _filteredProducts.isEmpty ? products : _filteredProducts;
  }
  
  bool get isLoading => _isLoading;
  String get selectedCategoryId => _selectedCategoryId;

  ProductViewModel() {
    initStream();
  }

  void filterByCategory(String categoryId) {
    if (_selectedCategoryId == categoryId) {
      _selectedCategoryId = '';
    } else {
      _selectedCategoryId = categoryId;
    }
    notifyListeners();
  }

  void searchProducts(String query) {
    if (query.isEmpty) {
      _filteredProducts = [];
    } else {
      _filteredProducts = _featuredProducts
          .where((product) => product.name.toLowerCase().contains(query.toLowerCase()))
          .toList();
    }
    notifyListeners();
  }

  Future<void> fetchFeaturedProducts() async {
    _isLoading = true;
    notifyListeners();

    // In a real app with streams, the first value might take a moment
    // For manual refresh, we can convert the stream logic or just wait for the first emission
    final stream = _repository.getFeaturedProducts();
    await for (final products in stream) {
      _featuredProducts = products;
      _isLoading = false;
      notifyListeners();
      break; // For manual refresh/future, we just need the latest once
    }
  }

  void initStream() {
    _repository.getFeaturedProducts().listen((products) {
      _featuredProducts = products;
      _isLoading = false;
      notifyListeners();
    });
  }

  Future<void> addProduct(ProductModel product) async {
    await _repository.addProduct(product);
  }

  Future<void> updateProduct(ProductModel product) async {
    await _repository.updateProduct(product);
  }

  Future<void> deleteProduct(String productId) async {
    await _repository.deleteProduct(productId);
  }
}
