import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/product_model.dart';
import '../repositories/product_repository.dart';

class ProductState {
  final List<ProductModel> featuredProducts;
  final List<ProductModel> filteredProducts;
  final String selectedCategoryId;
  final bool isLoading;

  ProductState({
    this.featuredProducts = const [],
    this.filteredProducts = const [],
    this.selectedCategoryId = '',
    this.isLoading = false,
  });

  ProductState copyWith({
    List<ProductModel>? featuredProducts,
    List<ProductModel>? filteredProducts,
    String? selectedCategoryId,
    bool? isLoading,
  }) {
    return ProductState(
      featuredProducts: featuredProducts ?? this.featuredProducts,
      filteredProducts: filteredProducts ?? this.filteredProducts,
      selectedCategoryId: selectedCategoryId ?? this.selectedCategoryId,
      isLoading: isLoading ?? this.isLoading,
    );
  }

  List<ProductModel> get displayProducts {
    List<ProductModel> products = selectedCategoryId.isEmpty
        ? featuredProducts
        : featuredProducts.where((p) => p.categoryId == selectedCategoryId).toList();
    
    return filteredProducts.isEmpty ? products : filteredProducts;
  }
}

class ProductNotifier extends Notifier<ProductState> {
  late final ProductRepository _repository;

  @override
  ProductState build() {
    _repository = ref.watch(productRepositoryProvider);
    
    // Set up real-time listener
    _initStream();
    
    return ProductState(isLoading: true);
  }

  void _initStream({String? shopId}) {
    final stream = shopId != null 
        ? _repository.getProductsByShop(shopId)
        : _repository.getAllProducts();
        
    final subscription = stream.listen(
      (products) {
        state = state.copyWith(
          featuredProducts: products,
          isLoading: false,
        );
      },
      onError: (error) {
        // Handle stream errors (Realtime disabled, JWT issues, etc.)
        state = state.copyWith(isLoading: false);
        if (error.toString().contains('JWT issued at future')) {
          Future.delayed(const Duration(seconds: 3), () => _initStream(shopId: shopId));
        }
      },
    );

    ref.onDispose(() => subscription.cancel());
  }

  void filterByCategory(String categoryId) {
    if (state.selectedCategoryId == categoryId) {
      state = state.copyWith(selectedCategoryId: '');
    } else {
      state = state.copyWith(selectedCategoryId: categoryId);
    }
  }

  void searchProducts(String query) {
    if (query.isEmpty) {
      state = state.copyWith(filteredProducts: []);
    } else {
      final filtered = state.featuredProducts
          .where((product) => product.name.toLowerCase().contains(query.toLowerCase()))
          .toList();
      state = state.copyWith(filteredProducts: filtered);
    }
  }

  void clearSearch() {
    state = state.copyWith(
      filteredProducts: [],
      selectedCategoryId: '',
    );
  }

  Future<void> addProduct(ProductModel product) async {
    await _repository.addProduct(product);
    _initStream(); // Refresh the list
  }

  Future<void> updateProduct(ProductModel product) async {
    await _repository.updateProduct(product);
    _initStream(); // Refresh the list
  }

  Future<void> deleteProduct(String productId) async {
    await _repository.deleteProduct(productId);
    _initStream(); // Refresh the list
  }
}

final productNotifierProvider = NotifierProvider<ProductNotifier, ProductState>(() {
  return ProductNotifier();
});
