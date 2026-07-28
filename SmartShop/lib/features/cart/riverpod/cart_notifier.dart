import 'dart:convert';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/cart_model.dart';
import '../../product/models/product_model.dart';
import '../../../models/coupon_model.dart';
import '../../offers/riverpod/coupon_notifier.dart';

class CartState {
  final List<CartItem> items;
  final CouponModel? appliedCoupon;
  final bool isInsideDhaka;
  final String deliveryMethod;

  CartState({
    this.items = const [],
    this.appliedCoupon,
    this.isInsideDhaka = true,
    this.deliveryMethod = 'standard',
  });

  CartState copyWith({
    List<CartItem>? items,
    CouponModel? appliedCoupon,
    bool? isInsideDhaka,
    String? deliveryMethod,
  }) {
    return CartState(
      items: items ?? this.items,
      appliedCoupon: appliedCoupon ?? this.appliedCoupon,
      isInsideDhaka: isInsideDhaka ?? this.isInsideDhaka,
      deliveryMethod: deliveryMethod ?? this.deliveryMethod,
    );
  }

  double get subtotal => items.fold(0, (sum, item) => sum + item.totalPrice);
  
  double get deliveryFee {
    if (appliedCoupon?.type == CouponType.freeDelivery) return 0;
    if (isInsideDhaka) {
      return deliveryMethod == 'express' ? 100 : 60;
    } else {
      return deliveryMethod == 'express' ? 250 : 150;
    }
  }

  double get couponDiscount {
    if (appliedCoupon == null) return 0;
    if (appliedCoupon!.type == CouponType.freeDelivery) return 0;
    if (appliedCoupon!.type == CouponType.percentage) {
      return (subtotal * appliedCoupon!.discountValue) / 100;
    } else {
      return appliedCoupon!.discountValue;
    }
  }

  double get totalAmount => subtotal + deliveryFee - couponDiscount;

  // Serialization for persistence
  Map<String, dynamic> toMap() {
    return {
      'items': items.map((x) => {
        'product': x.product.toJson(),
        'quantity': x.quantity,
      }).toList(),
      'isInsideDhaka': isInsideDhaka,
      'deliveryMethod': deliveryMethod,
    };
  }

  factory CartState.fromMap(Map<String, dynamic> map) {
    return CartState(
      items: (map['items'] as List).map((x) => CartItem(
        product: ProductModel.fromJson(x['product']),
        quantity: x['quantity'],
      )).toList(),
      isInsideDhaka: map['isInsideDhaka'] ?? true,
      deliveryMethod: map['deliveryMethod'] ?? 'standard',
    );
  }
}

class CartNotifier extends Notifier<CartState> {
  static const String _storageKey = 'cached_cart';

  @override
  CartState build() {
    _loadCart();
    return CartState();
  }

  Future<void> _saveCart() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_storageKey, json.encode(state.toMap()));
  }

  Future<void> _loadCart() async {
    final prefs = await SharedPreferences.getInstance();
    final data = prefs.getString(_storageKey);
    if (data != null) {
      try {
        state = CartState.fromMap(json.decode(data));
      } catch (e) {
        // Corrupted data
      }
    }
  }

  void addToCart(ProductModel product, {int quantity = 1}) {
    final currentState = state.items;
    if (currentState.isNotEmpty && currentState.first.product.shopId != product.shopId) {
      state = state.copyWith(items: [CartItem(product: product, quantity: quantity)]);
    } else {
      final index = currentState.indexWhere((item) => item.product.id == product.id);
      if (index != -1) {
        final updatedItems = List<CartItem>.from(currentState);
        updatedItems[index].quantity += quantity;
        state = state.copyWith(items: updatedItems);
      } else {
        state = state.copyWith(items: [...currentState, CartItem(product: product, quantity: quantity)]);
      }
    }
    _saveCart();
  }

  void removeFromCart(String productId) {
    state = state.copyWith(
      items: state.items.where((item) => item.product.id != productId).toList()
    );
    _saveCart();
  }

  void updateQuantity(String productId, int newQuantity) {
    if (newQuantity <= 0) {
      removeFromCart(productId);
      return;
    }
    final index = state.items.indexWhere((item) => item.product.id == productId);
    if (index != -1) {
      final updatedItems = List<CartItem>.from(state.items);
      updatedItems[index].quantity = newQuantity;
      state = state.copyWith(items: updatedItems);
      _saveCart();
    }
  }

  Future<String> applyCoupon(String code) async {
    final repository = ref.read(couponRepositoryProvider);
    final coupon = await repository.getCouponByCode(code);
    if (coupon == null) return "Invalid Coupon Code";
    if (coupon.isExpired) return "Coupon has expired";
    if (state.subtotal < coupon.minPurchase) {
      return "Minimum purchase of ৳${coupon.minPurchase.toInt()} required";
    }
    state = state.copyWith(appliedCoupon: coupon);
    return "Success";
  }

  void removeCoupon() {
    state = state.copyWith(appliedCoupon: null);
  }

  void setInsideDhaka(bool value) {
    state = state.copyWith(isInsideDhaka: value);
    _saveCart();
  }

  void setDeliveryMethod(String method) {
    state = state.copyWith(deliveryMethod: method);
    _saveCart();
  }

  void clearCart() {
    state = CartState();
    _saveCart();
  }
}

final cartNotifierProvider = NotifierProvider<CartNotifier, CartState>(() {
  return CartNotifier();
});
