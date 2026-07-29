import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../../../models/coupon_model.dart';
import '../repositories/coupon_repository.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/admin_shop_filter_notifier.dart';

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
      final user = ref.read(authNotifierProvider).value;
      final adminShopId = ref.read(adminShopFilterProvider);
      
      final isAdmin = (user?.role == 'super_admin' || user?.role == 'admin');
      
      if (isAdmin && adminShopId != null) {
        return await _repository.getCouponsByShop(adminShopId);
      }
      
      if (user != null && (user.role == 'owner' || user.role == 'manager') && user.shopId != null && user.shopId!.isNotEmpty) {
        return await _repository.getCouponsByShop(user.shopId!);
      }
      
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
