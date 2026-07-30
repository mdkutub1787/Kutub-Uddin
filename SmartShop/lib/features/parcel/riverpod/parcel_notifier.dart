import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../models/parcel_model.dart';
import '../repositories/parcel_repository.dart';

final parcelRepositoryProvider = Provider<ParcelRepository>((ref) {
  return ParcelRepository(ref.watch(supabaseClientProvider));
});

final userParcelsStreamProvider = StreamProvider.family<List<ParcelModel>, String>((ref, userId) {
  return ref.watch(parcelRepositoryProvider).getUserParcels(userId);
});

final availableZoneParcelsProvider = StreamProvider.family<List<ParcelModel>, String>((ref, zoneId) {
  return ref.watch(parcelRepositoryProvider).getAvailableParcelsForZone(zoneId);
});

final riderParcelsProvider = StreamProvider.family<List<ParcelModel>, String>((ref, riderId) {
  return ref.watch(parcelRepositoryProvider).getRiderParcels(riderId);
});

class ParcelNotifier extends StateNotifier<AsyncValue<void>> {
  final ParcelRepository _repo;

  ParcelNotifier(this._repo) : super(const AsyncValue.data(null));

  Future<void> createParcel(ParcelModel parcel) async {
    state = const AsyncValue.loading();
    try {
      await _repo.createParcel(parcel);
      state = const AsyncValue.data(null);
    } catch (e, st) {
      state = AsyncValue.error(e, st);
      rethrow;
    }
  }

  Future<void> acceptParcel(String parcelId, String riderId, String riderName, String riderPhone) async {
    state = const AsyncValue.loading();
    try {
      await _repo.updateParcelStatus(parcelId, 'Accepted', riderId: riderId, riderName: riderName, riderPhone: riderPhone);
      state = const AsyncValue.data(null);
    } catch (e, st) {
      state = AsyncValue.error(e, st);
      rethrow;
    }
  }
  
  Future<void> updateParcelStatus(String parcelId, String status) async {
    state = const AsyncValue.loading();
    try {
      await _repo.updateParcelStatus(parcelId, status);
      state = const AsyncValue.data(null);
    } catch (e, st) {
      state = AsyncValue.error(e, st);
      rethrow;
    }
  }
}

final parcelNotifierProvider = StateNotifierProvider<ParcelNotifier, AsyncValue<void>>((ref) {
  return ParcelNotifier(ref.watch(parcelRepositoryProvider));
});
