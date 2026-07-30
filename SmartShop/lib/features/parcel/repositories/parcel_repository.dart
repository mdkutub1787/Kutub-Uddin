import 'package:supabase_flutter/supabase_flutter.dart';
import '../models/parcel_model.dart';

class ParcelRepository {
  final SupabaseClient _supabase;

  ParcelRepository(this._supabase);

  Stream<List<ParcelModel>> getUserParcels(String userId) {
    return _supabase.from('parcels').stream(primaryKey: ['id']).eq('sender_id', userId).map(
      (list) => list.map((e) => ParcelModel.fromJson(e)).toList()..sort((a, b) => b.createdAt.compareTo(a.createdAt))
    );
  }

  Stream<List<ParcelModel>> getAvailableParcelsForZone(String zoneId) {
    if (zoneId.isEmpty) return Stream.value([]);
    return _supabase.from('parcels').stream(primaryKey: ['id']).eq('delivery_zone_id', zoneId).map(
      (list) => list.map((e) => ParcelModel.fromJson(e)).where((p) => p.status == 'Pending' || p.status == 'Confirmed').toList()..sort((a, b) => b.createdAt.compareTo(a.createdAt))
    );
  }
  
  Stream<List<ParcelModel>> getRiderParcels(String riderId) {
    return _supabase.from('parcels').stream(primaryKey: ['id']).eq('delivery_man_id', riderId).map(
      (list) => list.map((e) => ParcelModel.fromJson(e)).where((p) => p.status != 'Delivered' && p.status != 'Cancelled').toList()..sort((a, b) => b.createdAt.compareTo(a.createdAt))
    );
  }

  Future<void> createParcel(ParcelModel parcel) async {
    await _supabase.from('parcels').insert(parcel.toJson());
  }

  Future<void> updateParcelStatus(String parcelId, String status, {String? riderId, String? riderName, String? riderPhone}) async {
    final data = {'status': status, 'updated_at': DateTime.now().toIso8601String()};
    if (riderId != null) data['delivery_man_id'] = riderId;
    if (riderName != null) data['delivery_man_name'] = riderName;
    if (riderPhone != null) data['delivery_man_phone'] = riderPhone;
    
    await _supabase.from('parcels').update(data).eq('id', parcelId);
  }
}
