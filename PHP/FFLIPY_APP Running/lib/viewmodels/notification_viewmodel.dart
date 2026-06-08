import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/models/notification_model/notification_model.dart';
import 'package:fflipy/repositories/notification_repository.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class NotificationState {
  final bool isLoading;
  final String? error;
  final NotificationResponse? notificationResponse;

  NotificationState({
    this.isLoading = false,
    this.error,
    this.notificationResponse,
  });

  NotificationState copyWith({
    bool? isLoading,
    String? error,
    NotificationResponse? notificationResponse,
    bool clearError = false,
  }) {
    return NotificationState(
      isLoading: isLoading ?? this.isLoading,
      error: clearError ? null : error ?? this.error,
      notificationResponse: notificationResponse ?? this.notificationResponse,
    );
  }
}

class NotificationViewModel extends StateNotifier<NotificationState> {
  final NotificationRepository _notificationRepository;

  NotificationViewModel(this._notificationRepository) : super(NotificationState());

  Future<void> getNotifications() async {
    state = state.copyWith(isLoading: true, clearError: true);
    try {
      final result = await _notificationRepository.getNotifications();
      state = state.copyWith(
        isLoading: false,
        notificationResponse: result,
      );
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  Future<void> markAsRead(int id) async {
    try {
      await _notificationRepository.markAsRead(id);

      if (state.notificationResponse != null) {
        final updatedList = state.notificationResponse!.data.where((n) => n.id != id).toList();
        final updatedResponse = NotificationResponse(
          success: state.notificationResponse!.success, 
          message: state.notificationResponse!.message, 
          data: updatedList
        );
         state = state.copyWith(notificationResponse: updatedResponse);
      }

    } catch (e) {
       state = state.copyWith(error: ErrorHandler.getErrorMessage(e));
    }
  }
}
