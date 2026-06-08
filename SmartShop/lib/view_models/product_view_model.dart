import 'package:flutter/material.dart';
import '../models/product_model.dart';
import '../repositories/product_repository.dart';

class ProductViewModel extends ChangeNotifier {
  final ProductRepository _repository = ProductRepository();
  
  List<ProductModel> _featuredProducts = [];
  bool _isLoading = false;

  List<ProductModel> get featuredProducts => _featuredProducts;
  bool get isLoading => _isLoading;

  ProductViewModel() {
    fetchFeaturedProducts();
  }

  void fetchFeaturedProducts() {
    _isLoading = true;
    notifyListeners();

    _repository.getFeaturedProducts().listen((products) {
      _featuredProducts = products;
      _isLoading = false;
      notifyListeners();
    });
  }
}
