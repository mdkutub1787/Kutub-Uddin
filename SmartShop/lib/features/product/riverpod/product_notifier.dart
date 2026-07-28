import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/product_model.dart';
import '../repositories/product_repository.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../admin/riverpod/activity_log_notifier.dart';

class ProductState {
  final List<ProductModel> allProducts;
  final List<ProductModel> filteredProducts;
  final String selectedCategoryId;
  final String searchQuery;
  final double minPrice;
  final double maxPrice;
  final bool isLoading;

  ProductState({
    this.allProducts = const [],
    this.filteredProducts = const [],
    this.selectedCategoryId = '',
    this.searchQuery = '',
    this.minPrice = 0,
    this.maxPrice = 1000000,
    this.isLoading = false,
  });

  ProductState copyWith({
    List<ProductModel>? allProducts,
    List<ProductModel>? filteredProducts,
    String? selectedCategoryId,
    String? searchQuery,
    double? minPrice,
    double? maxPrice,
    bool? isLoading,
  }) {
    return ProductState(
      allProducts: allProducts ?? this.allProducts,
      filteredProducts: filteredProducts ?? this.filteredProducts,
      selectedCategoryId: selectedCategoryId ?? this.selectedCategoryId,
      searchQuery: searchQuery ?? this.searchQuery,
      minPrice: minPrice ?? this.minPrice,
      maxPrice: maxPrice ?? this.maxPrice,
      isLoading: isLoading ?? this.isLoading,
    );
  }

  List<ProductModel> get featuredProducts => allProducts;

  List<ProductModel> get displayProducts {
    return allProducts.where((p) {
      final matchesCategory = selectedCategoryId.isEmpty || p.categoryId == selectedCategoryId;
      final matchesSearch = searchQuery.isEmpty || p.name.toLowerCase().contains(searchQuery.toLowerCase());
      final matchesPrice = p.price >= minPrice && p.price <= maxPrice;
      return matchesCategory && matchesSearch && matchesPrice;
    }).toList();
  }
}

class ProductNotifier extends Notifier<ProductState> {
  late ProductRepository _repository;

  @override
  ProductState build() {
    _repository = ref.watch(productRepositoryProvider);
    
    final user = ref.watch(authNotifierProvider).value;
    String? filterShopId;
    
    if (user != null && (user.role == 'owner' || user.role == 'manager')) {
      filterShopId = user.shopId;
    }
    
    _initStream(shopId: filterShopId);
    
    return ProductState(isLoading: true);
  }

  void _initStream({String? shopId}) {
    final stream = shopId != null 
        ? _repository.getProductsByShop(shopId)
        : _repository.getAllProducts();
        
    final subscription = stream.listen(
      (products) {
        state = state.copyWith(
          allProducts: products,
          isLoading: false,
        );
      },
      onError: (error) {
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
    state = state.copyWith(searchQuery: query);
  }

  void setPriceRange(double min, double max) {
    state = state.copyWith(minPrice: min, maxPrice: max);
  }

  void clearFilters() {
    state = state.copyWith(
      searchQuery: '',
      selectedCategoryId: '',
      minPrice: 0,
      maxPrice: 1000000,
    );
  }

  Future<void> addProduct(ProductModel product) async {
    await _repository.addProduct(product);
    final admin = ref.read(authNotifierProvider).value;
    if (admin != null) {
      await ref.read(activityLogNotifierProvider.notifier).logAction(
        adminId: admin.uid,
        adminName: admin.name,
        action: 'Product Added',
        targetId: product.name,
        details: 'Product "${product.name}" was added to inventory.',
      );
    }
  }

  Future<void> updateProduct(ProductModel product) async {
    await _repository.updateProduct(product);
    final admin = ref.read(authNotifierProvider).value;
    if (admin != null) {
      await ref.read(activityLogNotifierProvider.notifier).logAction(
        adminId: admin.uid,
        adminName: admin.name,
        action: 'Product Updated',
        targetId: product.name,
        details: 'Product "${product.name}" details were updated.',
      );
    }
  }

  Future<void> deleteProduct(String productId) async {
    await _repository.deleteProduct(productId);
    final admin = ref.read(authNotifierProvider).value;
    if (admin != null) {
      await ref.read(activityLogNotifierProvider.notifier).logAction(
        adminId: admin.uid,
        adminName: admin.name,
        action: 'Product Deleted',
        targetId: productId,
        details: 'Product ID: $productId was removed from inventory.',
      );
    }
  }
}

final productNotifierProvider = NotifierProvider<ProductNotifier, ProductState>(() {
  return ProductNotifier();
});
