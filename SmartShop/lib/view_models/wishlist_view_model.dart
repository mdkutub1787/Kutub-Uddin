import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';

class WishlistViewModel extends ChangeNotifier {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref();
  List<String> _wishlistProductIds = [];
  bool _isLoading = false;

  List<String> get wishlistProductIds => _wishlistProductIds;
  bool get isLoading => _isLoading;

  void init(String userId) {
    _isLoading = true;
    _dbRef.child('users').child(userId).onValue.listen((event) {
      final data = event.snapshot.value as Map?;
      if (data != null && data.containsKey('wishlist')) {
        _wishlistProductIds = List<String>.from(data['wishlist']);
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

    await _dbRef.child('users').child(userId).update({
      'wishlist': _wishlistProductIds,
    });
  }

  bool isFavorite(String productId) {
    return _wishlistProductIds.contains(productId);
  }
}
