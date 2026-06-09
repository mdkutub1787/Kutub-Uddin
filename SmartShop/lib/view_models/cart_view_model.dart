import 'package:flutter/material.dart';
import '../models/cart_model.dart';
import '../models/product_model.dart';

class CartViewModel extends ChangeNotifier {
  final Map<String, CartItem> _items = {};
  double _discountAmount = 0.0;
  
  // Delivery related
  bool _isInsideDhaka = true;
  final double _deliveryFeeDhaka = 60.0;
  final double _deliveryFeeOutside = 150.0;

  Map<String, CartItem> get items => {..._items};
  int get itemCount => _items.length;
  bool get isInsideDhaka => _isInsideDhaka;

  void setInsideDhaka(bool value) {
    _isInsideDhaka = value;
    notifyListeners();
  }

  double get subtotal {
    var total = 0.0;
    _items.forEach((key, cartItem) {
      total += cartItem.totalPrice;
    });
    return total;
  }

  double get deliveryFee {
    if (_items.isEmpty) return 0;
    return _isInsideDhaka ? _deliveryFeeDhaka : _deliveryFeeOutside;
  }
  double get discountAmount => _discountAmount;

  double get totalAmount {
    return subtotal + deliveryFee - _discountAmount;
  }

  bool applyCoupon(String code) {
    if (code.toUpperCase() == 'SMART20') {
      _discountAmount = subtotal * 0.2; // 20% discount
      notifyListeners();
      return true;
    } else if (code.toUpperCase() == 'FREESHIP') {
      _discountAmount = 0; // Just an example, maybe set delivery fee to 0
      notifyListeners();
      return true;
    }
    return false;
  }

  void resetDiscount() {
    _discountAmount = 0;
    notifyListeners();
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
