import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../product/models/product_model.dart';
import '../repositories/wishlist_repository.dart';

final wishlistRepositoryProvider = Provider<WishlistRepository>((ref) {
  return WishlistRepository(ref.watch(supabaseClientProvider));
});

final wishlistNotifierProvider = AsyncNotifierProvider<WishlistNotifier, List<ProductModel>>(() {
  return WishlistNotifier();
});

class WishlistNotifier extends AsyncNotifier<List<ProductModel>> {
  late WishlistRepository _repository;

  @override
  FutureOr<List<ProductModel>> build() async {
    _repository = ref.watch(wishlistRepositoryProvider);
    return await _fetchWishlist();
  }

  Future<List<ProductModel>> _fetchWishlist() async {
    final user = ref.read(authNotifierProvider).value;
    if (user == null) return [];
    try {
      return await _repository.getWishlist(user.uid);
    } catch (e) {
      return [];
    }
  }

  Future<void> loadWishlist() async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() => _fetchWishlist());
  }

  Future<void> toggleWishlist(String productId) async {
    final user = ref.read(authNotifierProvider).value;
    if (user == null) return;

    final isExist = state.value?.any((p) => p.id == productId) ?? false;

    try {
      if (isExist) {
        await _repository.removeFromWishlist(user.uid, productId);
      } else {
        await _repository.addToWishlist(user.uid, productId);
      }
      await loadWishlist();
    } catch (e) {
      // Toggle failed
    }
  }

  bool isInWishlist(String productId) {
    return state.value?.any((p) => p.id == productId) ?? false;
  }
}
