import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../models/banner_model.dart';
import '../repositories/banner_repository.dart';

final bannerNotifierProvider = AsyncNotifierProvider<BannerNotifier, List<BannerModel>>(() {
  return BannerNotifier();
});

class BannerNotifier extends AsyncNotifier<List<BannerModel>> {
  late BannerRepository _repository;

  @override
  FutureOr<List<BannerModel>> build() async {
    _repository = BannerRepository(ref.watch(supabaseClientProvider));
    return await _fetchBanners();
  }

  Future<List<BannerModel>> _fetchBanners() async {
    try {
      return await _repository.getAllBanners();
    } catch (e) {
      if (e.toString().contains('JWT issued at future')) {
        await Future.delayed(const Duration(seconds: 2));
        return await _repository.getAllBanners();
      }
      // If table doesn't exist yet, we will just return empty instead of crashing
      return [];
    }
  }

  Future<void> loadBanners() async {
    state = const AsyncValue.loading();
    try {
      final banners = await _fetchBanners();
      state = AsyncValue.data(banners);
    } catch (e, stackTrace) {
      state = AsyncValue.error(e, stackTrace);
    }
  }
}
