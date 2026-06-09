import 'package:flutter/material.dart';
import '../models/cart_model.dart';
import '../models/product_model.dart';

class CartViewModel extends ChangeNotifier {
  final Map<String, CartItem> _items = {};

  Map<String, CartItem> get items => {..._items};

  int get itemCount => _items.length;

  double get totalAmount {
    var total = 0.0;
    _items.forEach((key, cartItem) {
      total += cartItem.totalPrice;
    });
    return total;
  }

  /// Adds an item to the cart if stock allows.
  /// Returns [true] if successfully added, [false] if out of stock.
  bool addItem(ProductModel product) {
    if (_items.containsKey(product.id)) {
      int currentQty = _items[product.id]!.quantity;
      if (currentQty + 1 > product.stock) {
        return false; // Not enough stock
      }
      _items.update(
        product.id,
        (existingItem) => CartItem(
          product: existingItem.product,
          quantity: existingItem.quantity + 1,
        ),
      );
    } else {
      if (product.stock < 1) {
        return false; // Out of stock
      }
      _items.putIfAbsent(
        product.id,
        () => CartItem(product: product),
      );
    }
    notifyListeners();
    return true;
  }

  void removeItem(String productId) {
    _items.remove(productId);
    notifyListeners();
  }

  void removeSingleItem(String productId) {
    if (!_items.containsKey(productId)) return;
    if (_items[productId]!.quantity > 1) {
      _items.update(
        productId,
        (existingItem) => CartItem(
          product: existingItem.product,
          quantity: existingItem.quantity - 1,
        ),
      );
    } else {
      _items.remove(productId);
    }
    notifyListeners();
  }

  void clearCart() {
    _items.clear();
    notifyListeners();
  }
}
