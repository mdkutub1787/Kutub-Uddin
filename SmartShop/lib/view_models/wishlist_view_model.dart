import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/product_model.dart';

class WishlistViewModel extends ChangeNotifier {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;
  List<String> _wishlistProductIds = [];
  bool _isLoading = false;

  List<String> get wishlistProductIds => _wishlistProductIds;
  bool get isLoading => _isLoading;

  void init(String userId) {
    _isLoading = true;
    _firestore.collection('users').doc(userId).snapshots().listen((doc) {
      if (doc.exists && doc.data()!.containsKey('wishlist')) {
        _wishlistProductIds = List<String>.from(doc.data()!['wishlist']);
      } else {
        _wishlistProductIds = [];
      }
      _isLoading = false;
      notifyListeners();
    });
  }

  Future<void> toggleWishlist(String userId, String productId) async {
    if (_wishlistProductIds.contains(productId)) {
      _wishlistProductIds.remove(productId);
    } else {
      _wishlistProductIds.add(productId);
    }
    notifyListeners();

    await _firestore.collection('users').doc(userId).update({
      'wishlist': _wishlistProductIds,
    });
  }

  bool isFavorite(String productId) {
    return _wishlistProductIds.contains(productId);
  }
}
