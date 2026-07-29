import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/product_model.dart';
import '../repositories/product_repository.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../admin/riverpod/activity_log_notifier.dart';
import '../../../core/riverpod/admin_shop_filter_notifier.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/providers.dart';

final productRepositoryProvider = Provider<ProductRepository>((ref) {
  return ProductRepository(ref.watch(supabaseClientProvider));
});

class ProductState {
  final List<ProductModel> allProducts;
  final String selectedCategoryId;
  final String searchQuery;
  final double minPrice;
  final double maxPrice;
  final bool isLoading;

  ProductState({
    this.allProducts = const [],
    this.selectedCategoryId = '',
    this.searchQuery = '',
    this.minPrice = 0,
    this.maxPrice = 1000000,
    this.isLoading = false,
  });

  ProductState copyWith({
    List<ProductModel>? allProducts,
    String? selectedCategoryId,
    String? searchQuery,
    double? minPrice,
    double? maxPrice,
    bool? isLoading,
  }) {
    return ProductState(
      allProducts: allProducts ?? this.allProducts,
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
    _initStream();
    return ProductState(isLoading: true);
  }

  void _initStream() {
    final user = ref.watch(authNotifierProvider).value;
    final adminShopId = ref.watch(adminShopFilterProvider);
    
    // PROFESSIONAL ROLE LOGIC
    // Super Admin/Admin: Sees EVERYTHING (unless adminShopId is set)
    // Customer/User: Sees EVERYTHING
    // Owner/Manager/Staff: Sees ONLY their shop products. If they have no shopId, they see nothing.
    final isAdmin = (user?.role == 'super_admin' || user?.role == 'admin');
    final isCustomer = (user?.role == 'user' || user?.role == 'customer' || user == null);
    
    final stream = (isAdmin && adminShopId != null) 
        ? _repository.getProductsByShop(adminShopId)
        : (isAdmin || isCustomer)
            ? _repository.getAllProducts()
            : _repository.getProductsByShop(user?.shopId ?? '');
            
    final subscription = stream.listen(
      (products) {
        print('✅ Product Stream: Loaded ${products.length} products for shop ${user?.shopId}');
        state = state.copyWith(allProducts: products, isLoading: false);
      },
      onError: (error) {
        print('❌ Product Stream Error: $error');
        state = state.copyWith(isLoading: false);
      },
    );

    ref.onDispose(() => subscription.cancel());
  }

  void filterByCategory(String categoryId) {
    state = state.copyWith(selectedCategoryId: state.selectedCategoryId == categoryId ? '' : categoryId);
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
    final createdProduct = await _repository.addProduct(product);
    state = state.copyWith(allProducts: [...state.allProducts, createdProduct]);
  }

  Future<void> updateProduct(ProductModel product) async {
    await _repository.updateProduct(product);
  }

  Future<void> deleteProduct(String productId) async {
    await _repository.deleteProduct(productId);
  }
}

final productNotifierProvider = NotifierProvider<ProductNotifier, ProductState>(() {
  return ProductNotifier();
});
