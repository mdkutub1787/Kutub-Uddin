import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/providers.dart';
import '../../../core/constants/constants.dart';
import '../models/banner_model.dart';

class BannerRepository {
  final SupabaseClient _supabase;

  BannerRepository(this._supabase);

  Future<List<BannerModel>> getAllBanners() async {
    final response = await _supabase
        .from(AppConstants.bannersTable)
        .select();
        
    return (response as List).map((json) => BannerModel.fromJson(json)).toList();
  }
}

final bannerRepositoryProvider = Provider<BannerRepository>((ref) {
  return BannerRepository(ref.watch(supabaseClientProvider));
});
