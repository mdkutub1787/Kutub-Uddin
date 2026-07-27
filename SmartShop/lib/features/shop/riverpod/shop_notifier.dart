import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../models/shop_model.dart';
import '../repositories/shop_repository.dart';

final shopNotifierProvider = AsyncNotifierProvider<ShopNotifier, List<ShopModel>>(() {
  return ShopNotifier();
});

class ShopNotifier extends AsyncNotifier<List<ShopModel>> {
  @override
  FutureOr<List<ShopModel>> build() async {
    return await _fetchShops();
  }

  Future<List<ShopModel>> _fetchShops() async {
    final repository = ref.watch(shopRepositoryProvider);
    return await repository.getAllShops();
  }

  Future<void> loadShops() async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(() => _fetchShops());
  }
}
