import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/product_model.dart';
import '../repositories/product_repository.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../admin/riverpod/activity_log_notifier.dart';

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
  late ProductRepository _repository;

  @override
  ProductState build() {
    _repository = ref.watch(productRepositoryProvider);
    
    // Set up real-time listener based on user role
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
    
    // Log Activity
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
    _initStream(); 
  }

  Future<void> updateProduct(ProductModel product) async {
    await _repository.updateProduct(product);

    // Log Activity
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
    _initStream();
  }

  Future<void> deleteProduct(String productId) async {
    await _repository.deleteProduct(productId);

    // Log Activity
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
    _initStream();
  }
}

final productNotifierProvider = NotifierProvider<ProductNotifier, ProductState>(() {
  return ProductNotifier();
});
