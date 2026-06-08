import 'package:fflipy/models/track_transfer/track_transfer_model.dart';
import 'package:fflipy/providers/dio_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../repositories/track_transfer_repository.dart';
import '../services/track_transfer_service.dart';

final trackTransferServiceProvider =
    Provider.autoDispose<TrackTransferService>((ref) {
  final dio = ref.watch(dioProvider);
  return TrackTransferService(dio);
});

final trackTransferRepositoryProvider =
    Provider.autoDispose<TrackTransferRepository>((ref) {
  final trackTransferService = ref.watch(trackTransferServiceProvider);
  return TrackTransferRepository(trackTransferService);
});

final trackTransferProvider = FutureProvider.family
    .autoDispose<TrackTransferResponse?, String>((ref, refNo) async {
  final trackTransferRepository = ref.watch(trackTransferRepositoryProvider);
  return await trackTransferRepository.trackTransfer(refNo);
});
