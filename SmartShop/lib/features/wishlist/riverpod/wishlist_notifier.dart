import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../product/models/product_model.dart';
import '../repositories/wishlist_repository.dart';
import '../../auth/riverpod/auth_notifier.dart';

final wishlistNotifierProvider = AsyncNotifierProvider<WishlistNotifier, List<ProductModel>>(() {
  return WishlistNotifier();
});

class WishlistNotifier extends AsyncNotifier<List<ProductModel>> {
  late WishlistRepository _repository;

  @override
  Future<List<ProductModel>> build() async {
    _repository = ref.watch(wishlistRepositoryProvider);
    final user = ref.watch(authNotifierProvider).value;
    if (user == null) return [];
    return _repository.getWishlist(user.uid);
  }

  Future<void> loadWishlist() async {
    state = const AsyncLoading();
    final user = ref.read(authNotifierProvider).value;
    if (user != null) {
      state = await AsyncValue.guard(() => _repository.getWishlist(user.uid));
    }
  }

  bool isInWishlist(String productId) {
    final list = state.value ?? [];
    return list.any((p) => p.id == productId);
  }

  Future<void> toggleWishlist(ProductModel product) async {
    final user = ref.read(authNotifierProvider).value;
    if (user == null) return;

    final currentList = state.value ?? [];
    bool exists = currentList.any((p) => p.id == product.id);

    // Optimistic UI Update: change state immediately
    if (exists) {
      state = AsyncData(currentList.where((p) => p.id != product.id).toList());
    } else {
      state = AsyncData([...currentList, product]);
    }

    try {
      if (exists) {
        await _repository.removeFromWishlist(user.uid, product.id);
      } else {
        await _repository.addToWishlist(user.uid, product.id);
      }
    } catch (e) {
      // Revert on error
      state = AsyncData(currentList);
    }
  }
}
