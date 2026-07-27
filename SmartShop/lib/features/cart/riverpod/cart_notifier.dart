import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/cart_model.dart';
import '../../product/models/product_model.dart';

final cartNotifierProvider = NotifierProvider<CartNotifier, List<CartItem>>(() {
  return CartNotifier();
});

class CartNotifier extends Notifier<List<CartItem>> {
  @override
  List<CartItem> build() {
    return [];
  }

  void addToCart(ProductModel product, {int quantity = 1}) {
    var currentState = state;
    
    // Check if cart has items from a different shop
    if (currentState.isNotEmpty && currentState.first.product.shopId != product.shopId) {
      // Clear the cart if adding product from a different shop
      currentState = [];
    }

    final index = currentState.indexWhere((item) => item.product.id == product.id);

    if (index != -1) {
      // Product already in cart, increment quantity
      final updatedCart = List<CartItem>.from(currentState);
      updatedCart[index].quantity += quantity;
      state = updatedCart;
    } else {
      // Add new product
      state = [...currentState, CartItem(product: product, quantity: quantity)];
    }
  }

  void removeFromCart(String productId) {
    state = state.where((item) => item.product.id != productId).toList();
  }

  void updateQuantity(String productId, int newQuantity) {
    if (newQuantity <= 0) {
      removeFromCart(productId);
      return;
    }

    final currentState = state;
    final index = currentState.indexWhere((item) => item.product.id == productId);

    if (index != -1) {
      final updatedCart = List<CartItem>.from(currentState);
      updatedCart[index].quantity = newQuantity;
      state = updatedCart;
    }
  }

  void clearCart() {
    state = [];
  }

  double get totalAmount {
    return state.fold(0, (sum, item) => sum + item.totalPrice);
  }
}
