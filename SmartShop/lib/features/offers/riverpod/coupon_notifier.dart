import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../../../models/coupon_model.dart';
import '../repositories/coupon_repository.dart';

final couponRepositoryProvider = Provider<CouponRepository>((ref) {
  return CouponRepository(ref.watch(supabaseClientProvider));
});

final couponNotifierProvider = AsyncNotifierProvider<CouponNotifier, List<CouponModel>>(() {
  return CouponNotifier();
});

class CouponNotifier extends AsyncNotifier<List<CouponModel>> {
  late CouponRepository _repository;

  @override
  FutureOr<List<CouponModel>> build() async {
    _repository = ref.watch(couponRepositoryProvider);
    return await _fetchCoupons();
  }

  Future<List<CouponModel>> _fetchCoupons() async {
    try {
      return await _repository.getActiveCoupons();
    } catch (e) {
      return [];
    }
  }

  Future<void> loadCoupons() async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() => _fetchCoupons());
  }
}
