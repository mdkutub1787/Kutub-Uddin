
import 'package:fflipy/models/track_transfer/track_transfer_model.dart';
import 'package:fflipy/services/track_transfer_service.dart';

class TrackTransferRepository {
  final TrackTransferService _trackTransferService;

  TrackTransferRepository(this._trackTransferService);

  Future<TrackTransferResponse> trackTransfer(String refNo) async {
    return await _trackTransferService.trackTransfer(refNo);
  }
}
