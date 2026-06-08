import 'package:fflipy/models/support_ticket_model/support_ticket_model.dart';
import 'package:fflipy/models/support_ticket_model/ticket_details_model.dart';
import 'package:fflipy/models/support_ticket_model/ticket_reply_model.dart';
import 'package:fflipy/services/support_ticket_service.dart';

class SupportTicketRepository {
  final SupportTicketService _supportTicketService;

  SupportTicketRepository(this._supportTicketService);

  Future<SupportTicketResponse> getSupportTickets({int page = 1}) async {
    return await _supportTicketService.getSupportTickets(page: page);
  }

  Future<TicketDetailsResponse> getTicketDetails(String ticketId) async {
    return await _supportTicketService.getTicketDetails(ticketId);
  }

  Future<void> createSupportTicket({
    required String subject,
    required String message,
    String? attachments,
  }) async {
    return await _supportTicketService.createSupportTicket(
      subject: subject,
      message: message,
      attachments: attachments,
    );
  }

  Future<TicketReplyResponse> replySupportTicket({
    required int id,
    required String message,
    String? attachments,
    int replyType = 1, // 1 for reply, 2 for close
  }) async {
    return await _supportTicketService.replySupportTicket(
      id: id,
      message: message,
      attachments: attachments,
      replyType: replyType,
    );
  }
}
