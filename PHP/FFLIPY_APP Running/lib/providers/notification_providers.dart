import 'package:fflipy/providers/dio_provider.dart';
import 'package:fflipy/repositories/notification_repository.dart';
import 'package:fflipy/services/notification_service.dart';
import 'package:fflipy/viewmodels/notification_viewmodel.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final notificationServiceProvider = Provider<NotificationService>((ref) {
  final dio = ref.watch(dioProvider);
  return NotificationService(dio);
});

final notificationRepositoryProvider = Provider<NotificationRepository>((ref) {
  final notificationService = ref.watch(notificationServiceProvider);
  return NotificationRepository(notificationService);
});

final notificationViewModelProvider = StateNotifierProvider<NotificationViewModel, NotificationState>((ref) {
  final notificationRepository = ref.watch(notificationRepositoryProvider);
  return NotificationViewModel(notificationRepository);
});
