import 'package:flutter/material.dart';
import '../models/cart_model.dart';
import '../models/product_model.dart';
import '../models/coupon_model.dart';

class CartViewModel extends ChangeNotifier {
  final Map<String, CartItem> _items = {};
  double _discountAmount = 0.0;
  CouponModel? _appliedCoupon;
  
  // Delivery related
  bool _isInsideDhaka = true;
  final double _deliveryFeeDhaka = 60.0;
  final double _deliveryFeeOutside = 150.0;

  Map<String, CartItem> get items => {..._items};
  int get itemCount => _items.length;
  bool get isInsideDhaka => _isInsideDhaka;
  CouponModel? get appliedCoupon => _appliedCoupon;

  // Mock available coupons
  final List<CouponModel> _availableCoupons = [
    CouponModel(
      id: '1',
      code: 'SMART20',
      title: '20% Mega Discount',
      description: 'Get 20% off on all products. Min purchase ৳1000',
      discountValue: 20,
      type: CouponType.percentage,
      minPurchase: 1000,
      expiryDate: DateTime.now().add(const Duration(days: 30)),
    ),
    CouponModel(
      id: '2',
      code: 'FIRST500',
      title: 'First Purchase Offer',
      description: 'Flat ৳500 off on your first order. Min purchase ৳5000',
      discountValue: 500,
      type: CouponType.fixedAmount,
      minPurchase: 5000,
      expiryDate: DateTime.now().add(const Duration(days: 60)),
    ),
    CouponModel(
      id: '3',
      code: 'FREESHIP',
      title: 'Free Delivery',
      description: 'Free delivery on orders above ৳2000',
      discountValue: 0,
      type: CouponType.freeDelivery,
      minPurchase: 2000,
      expiryDate: DateTime.now().add(const Duration(days: 15)),
    ),
  ];

  List<CouponModel> get availableCoupons => _availableCoupons;

  void setInsideDhaka(bool value) {
    _isInsideDhaka = value;
    // Re-validate free delivery coupon if applied
    if (_appliedCoupon?.type == CouponType.freeDelivery) {
      _calculateDiscount();
    }
    notifyListeners();
  }

  /// Sum of all products' original prices (before any discounts)
  double get totalOriginalPrice {
    var total = 0.0;
    _items.forEach((key, cartItem) {
      total += cartItem.product.originalPrice * cartItem.quantity;
    });
    return total;
  }

  /// Sum of all savings from product-specific offers
  double get totalProductDiscount {
    var total = 0.0;
    _items.forEach((key, cartItem) {
      if (cartItem.product.hasDiscount) {
        total += (cartItem.product.originalPrice - cartItem.product.price) * cartItem.quantity;
      }
    });
    return total;
  }

  /// Subtotal after product discounts, but before coupon
  double get subtotal {
    var total = 0.0;
    _items.forEach((key, cartItem) {
      total += cartItem.totalPrice;
    });
    return total;
  }

  double get deliveryFee {
    if (_items.isEmpty) return 0;
    if (_appliedCoupon?.type == CouponType.freeDelivery) return 0;
    return _isInsideDhaka ? _deliveryFeeDhaka : _deliveryFeeOutside;
  }
  
  /// Discount amount from the applied coupon ONLY
  double get couponDiscount => _discountAmount;
  
  // Keep this for compatibility if needed elsewhere
  double get discountAmount => _discountAmount;

  String get appliedCouponDetails {
    if (_appliedCoupon == null) return "";
    if (_appliedCoupon!.type == CouponType.percentage) {
      return "${_appliedCoupon!.discountValue.toInt()}% OFF";
    } else if (_appliedCoupon!.type == CouponType.fixedAmount) {
      return "৳${_appliedCoupon!.discountValue.toInt()} OFF";
    } else {
      return "Free Delivery";
    }
  }

  /// Final amount user has to pay
  double get totalAmount {
    return subtotal + deliveryFee - _discountAmount;
  }

  String applyCoupon(String code) {
    final coupon = _availableCoupons.firstWhere(
      (c) => c.code.toUpperCase() == code.toUpperCase(),
      orElse: () => throw 'Invalid Coupon',
    );

    if (!coupon.isActive || coupon.isExpired) {
      return 'Coupon expired or inactive';
    }

    if (subtotal < coupon.minPurchase) {
      return 'Minimum purchase ৳${coupon.minPurchase} required';
    }

    _appliedCoupon = coupon;
    _calculateDiscount();
    notifyListeners();
    return 'Success';
  }

  void _calculateDiscount() {
    if (_appliedCoupon == null) {
      _discountAmount = 0;
      return;
    }

    switch (_appliedCoupon!.type) {
      case CouponType.percentage:
        _discountAmount = subtotal * (_appliedCoupon!.discountValue / 100);
        break;
      case CouponType.fixedAmount:
        _discountAmount = _appliedCoupon!.discountValue;
        break;
      case CouponType.freeDelivery:
        _discountAmount = 0; // Handled in deliveryFee getter
        break;
    }
  }

  void removeCoupon() {
    _appliedCoupon = null;
    _discountAmount = 0;
    notifyListeners();
  }

  void resetDiscount() {
    _appliedCoupon = null;
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
    _appliedCoupon = null;
    _discountAmount = 0.0;
    notifyListeners();
  }
}
