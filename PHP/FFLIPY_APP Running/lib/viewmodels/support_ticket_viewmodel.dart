import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/models/support_ticket_model/support_ticket_model.dart';
import 'package:fflipy/models/support_ticket_model/ticket_details_model.dart';
import 'package:fflipy/repositories/support_ticket_repository.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class SupportTicketState {
  final bool isLoading;
  final String? error;
  final SupportTicketResponse? supportTicketResponse;
  final TicketDetailsResponse? ticketDetailsResponse;

  SupportTicketState({
    this.isLoading = false,
    this.error,
    this.supportTicketResponse,
    this.ticketDetailsResponse,
  });

  SupportTicketState copyWith({
    bool? isLoading,
    String? error,
    SupportTicketResponse? supportTicketResponse,
    TicketDetailsResponse? ticketDetailsResponse,
    bool clearError = false,
    bool? clearTicketDetails,
  }) {
    return SupportTicketState(
      isLoading: isLoading ?? this.isLoading,
      error: clearError ? null : error ?? this.error,
      supportTicketResponse: supportTicketResponse ?? this.supportTicketResponse,
      ticketDetailsResponse: clearTicketDetails == true ? null : ticketDetailsResponse ?? this.ticketDetailsResponse,
    );
  }
}

class SupportTicketViewModel extends StateNotifier<SupportTicketState> {
  final SupportTicketRepository _supportTicketRepository;

  SupportTicketViewModel(this._supportTicketRepository) : super(SupportTicketState());

  Future<void> getTicketDetails(String ticketId) async {
    state = state.copyWith(isLoading: true, clearError: true);
    try {
      final result = await _supportTicketRepository.getTicketDetails(ticketId);
      state = state.copyWith(isLoading: false, ticketDetailsResponse: result);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  void clearTicketDetails() {
    state = state.copyWith(clearTicketDetails: true);
  }

  Future<void> getSupportTickets({int page = 1, bool showLoading = true}) async {
    if (showLoading) {
      state = state.copyWith(isLoading: true, clearError: true);
    }
    try {
      final result = await _supportTicketRepository.getSupportTickets(page: page);
      state = state.copyWith(
        isLoading: false,
        supportTicketResponse: result,
      );
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  Future<void> createSupportTicket({
    required String subject,
    required String message,
    String? attachments,
    Function? onSuccess,
  }) async {
    state = state.copyWith(isLoading: true, clearError: true);
    try {
      await _supportTicketRepository.createSupportTicket(
        subject: subject,
        message: message,
        attachments: attachments,
      );
      if (onSuccess != null) onSuccess();
      await getSupportTickets();
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  Future<void> replySupportTicket({
    required int id,
    required String ticketId,
    required String message,
    String? attachments,
    int replyType = 1,
    Function(String message)? onSuccess,
  }) async {
    state = state.copyWith(isLoading: true, clearError: true);
    try {
      final response = await _supportTicketRepository.replySupportTicket(
        id: id,
        message: message,
        attachments: attachments,
        replyType: replyType,
      );

      if (onSuccess != null) {
        onSuccess(response.message);
      }

      if (replyType == 2) {
        await getSupportTickets();
      } else {
        final newDetails = await _supportTicketRepository.getTicketDetails(ticketId);
        state = state.copyWith(isLoading: false, ticketDetailsResponse: newDetails);

        getSupportTickets(showLoading: false);
      }
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }
}
