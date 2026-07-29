import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/constants/constants.dart';
import '../../../models/coupon_model.dart';

class CouponRepository {
  final SupabaseClient _supabase;

  CouponRepository(this._supabase);

  Future<List<CouponModel>> getActiveCoupons() async {
    final response = await _supabase
        .from(AppConstants.couponsTable)
        .select()
        .eq('isActive', true)
        .order('expiryDate', ascending: true);
    
    return (response as List).map((json) {
      final data = Map<String, dynamic>.from(json);
      if (data['expiryDate'] is String) {
        data['expiryDate'] = DateTime.parse(data['expiryDate']);
      }
      return CouponModel.fromMap(data, data['id'].toString());
    }).toList();
  }

  Future<List<CouponModel>> getCouponsByShop(String shopId) async {
    final response = await _supabase
        .from(AppConstants.couponsTable)
        .select()
        .eq('shopId', shopId)
        .order('expiryDate', ascending: true);
    
    return (response as List).map((json) {
      final data = Map<String, dynamic>.from(json);
      if (data['expiryDate'] is String) {
        data['expiryDate'] = DateTime.parse(data['expiryDate']);
      }
      return CouponModel.fromMap(data, data['id'].toString());
    }).toList();
  }

  Future<CouponModel?> getCouponByCode(String code) async {
    try {
      final response = await _supabase
          .from(AppConstants.couponsTable)
          .select()
          .eq('code', code)
          .eq('isActive', true)
          .maybeSingle();
      
      if (response == null) return null;
      
      final data = Map<String, dynamic>.from(response);
      if (data['expiryDate'] is String) {
        data['expiryDate'] = DateTime.parse(data['expiryDate']);
      }
      return CouponModel.fromMap(data, data['id'].toString());
    } catch (e) {
      return null;
    }
  }
}
