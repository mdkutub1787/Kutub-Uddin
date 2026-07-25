import 'package:supabase_flutter/supabase_flutter.dart';
import '../models/banner_model.dart';

class BannerRepository {
  final SupabaseClient _supabase;

  BannerRepository(this._supabase);

  Future<List<BannerModel>> getAllBanners() async {
    final response = await _supabase
        .from('banners')
        .select();
        
    return (response as List).map((json) => BannerModel.fromJson(json)).toList();
  }
}
