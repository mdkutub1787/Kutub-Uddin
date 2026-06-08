import 'package:fflipy/providers/dio_provider.dart';
import 'package:fflipy/repositories/support_ticket_repository.dart';
import 'package:fflipy/services/support_ticket_service.dart';
import 'package:fflipy/viewmodels/support_ticket_viewmodel.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final supportTicketServiceProvider = Provider<SupportTicketService>((ref) {
  final dio = ref.watch(dioProvider);
  return SupportTicketService(dio);
});

final supportTicketRepositoryProvider = Provider<SupportTicketRepository>((ref) {
  final supportTicketService = ref.watch(supportTicketServiceProvider);
  return SupportTicketRepository(supportTicketService);
});

final supportTicketViewModelProvider = StateNotifierProvider.autoDispose<SupportTicketViewModel, SupportTicketState>((ref) {
  final supportTicketRepository = ref.watch(supportTicketRepositoryProvider);
  return SupportTicketViewModel(supportTicketRepository);
});
