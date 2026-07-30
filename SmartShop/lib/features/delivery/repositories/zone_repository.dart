import 'package:supabase_flutter/supabase_flutter.dart';
import '../models/delivery_zone_model.dart';

class ZoneRepository {
  final SupabaseClient _supabase;

  ZoneRepository(this._supabase);

  Future<List<DeliveryZoneModel>> getZones() async {
    final response = await _supabase.from('delivery_zones').select().eq('is_active', true);
    return (response as List).map((e) => DeliveryZoneModel.fromJson(e)).toList();
  }

  Future<List<DeliveryZoneModel>> getAllZonesAdmin() async {
    final response = await _supabase.from('delivery_zones').select().order('zone_name');
    return (response as List).map((e) => DeliveryZoneModel.fromJson(e)).toList();
  }

  Future<void> createZone(DeliveryZoneModel zone) async {
    await _supabase.from('delivery_zones').insert(zone.toJson());
  }

  Future<void> updateZone(DeliveryZoneModel zone) async {
    await _supabase.from('delivery_zones').update(zone.toJson()).eq('id', zone.id);
  }

  Future<void> deleteZone(String id) async {
    await _supabase.from('delivery_zones').delete().eq('id', id);
  }
}
