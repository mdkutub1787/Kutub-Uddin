
import 'package:fflipy/models/track_transfer/track_transfer_model.dart';
import 'package:fflipy/repositories/track_transfer_repository.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class TrackTransferState {
  final AsyncValue<TrackTransferResponse?> trackTransferResponse;

  const TrackTransferState({
    this.trackTransferResponse = const AsyncValue.data(null),
  });

  bool get isLoading => trackTransferResponse is AsyncLoading;

  TrackTransferState copyWith({
    AsyncValue<TrackTransferResponse?>? trackTransferResponse,
  }) {
    return TrackTransferState(
      trackTransferResponse: trackTransferResponse ?? this.trackTransferResponse,
    );
  }
}

class TrackTransferViewModel extends StateNotifier<TrackTransferState> {
  final TrackTransferRepository _trackTransferRepository;

  TrackTransferViewModel(this._trackTransferRepository) : super(const TrackTransferState());

  Future<void> trackTransfer(String refNo) async {
    state = state.copyWith(trackTransferResponse: const AsyncValue.loading());
    try {
      final data = await _trackTransferRepository.trackTransfer(refNo);
      state = state.copyWith(trackTransferResponse: AsyncValue.data(data));
    } catch (e, stackTrace) {
      state = state.copyWith(trackTransferResponse: AsyncValue.error(e, stackTrace));
    }
  }

   void clear() {
    state = const TrackTransferState();
  }
}
