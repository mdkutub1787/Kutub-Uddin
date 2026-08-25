import 'package:adhan/adhan.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';

enum QiblaStatus { loading, success, error }

class QiblaData {
  final double qiblaDirection;
  final QiblaStatus status;
  final String? errorMsg;

  QiblaData({
    required this.qiblaDirection,
    required this.status,
    this.errorMsg,
  });
}

final qiblaProvider = Provider<QiblaData>((ref) {
  final locationAsync = ref.watch(locationProvider);

  return locationAsync.when(
    data: (position) {
      final coordinates = Coordinates(position.latitude, position.longitude);
      final qibla = Qibla(coordinates);
      return QiblaData(
        qiblaDirection: qibla.direction,
        status: QiblaStatus.success,
      );
    },
    loading: () => QiblaData(qiblaDirection: 0, status: QiblaStatus.loading),
    error: (err, stack) => QiblaData(
      qiblaDirection: 0,
      status: QiblaStatus.error,
      errorMsg: err.toString(),
    ),
  );
});
