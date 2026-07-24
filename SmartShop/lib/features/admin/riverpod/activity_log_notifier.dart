import 'package:flutter_riverpod/flutter_riverpod.dart';

final activityLogNotifierProvider = NotifierProvider<ActivityLogNotifier, List<dynamic>>(() {
  return ActivityLogNotifier();
});

class ActivityLogNotifier extends Notifier<List<dynamic>> {
  @override
  List<dynamic> build() {
    return [];
  }
}
