import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../models/delivery_zone_model.dart';
import '../repositories/zone_repository.dart';

final zoneRepositoryProvider = Provider<ZoneRepository>((ref) {
  return ZoneRepository(ref.watch(supabaseClientProvider));
});

final activeZonesProvider = FutureProvider<List<DeliveryZoneModel>>((ref) {
  return ref.watch(zoneRepositoryProvider).getZones();
});

final allZonesAdminProvider = FutureProvider<List<DeliveryZoneModel>>((ref) {
  return ref.watch(zoneRepositoryProvider).getAllZonesAdmin();
});

class ZoneNotifier extends StateNotifier<AsyncValue<List<DeliveryZoneModel>>> {
  final ZoneRepository _repo;

  ZoneNotifier(this._repo) : super(const AsyncValue.loading()) {
    loadZones();
  }

  Future<void> loadZones() async {
    state = const AsyncValue.loading();
    try {
      final zones = await _repo.getAllZonesAdmin();
      state = AsyncValue.data(zones);
    } catch (e, st) {
      state = AsyncValue.error(e, st);
    }
  }

  Future<void> createZone(DeliveryZoneModel zone) async {
    await _repo.createZone(zone);
    await loadZones();
  }

  Future<void> updateZone(DeliveryZoneModel zone) async {
    await _repo.updateZone(zone);
    await loadZones();
  }

  Future<void> deleteZone(String id) async {
    await _repo.deleteZone(id);
    await loadZones();
  }
}

final zoneNotifierProvider = StateNotifierProvider<ZoneNotifier, AsyncValue<List<DeliveryZoneModel>>>((ref) {
  return ZoneNotifier(ref.watch(zoneRepositoryProvider));
});
