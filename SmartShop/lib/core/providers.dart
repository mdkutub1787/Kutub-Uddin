import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../features/order/models/order_model.dart';
import '../features/order/repositories/order_repository.dart';

// Global providers
final supabaseClientProvider = Provider<SupabaseClient>((ref) {
  return Supabase.instance.client;
});

final userOrdersStreamProvider = StreamProvider.family<List<OrderModel>, String>((ref, userId) {
  return ref.watch(orderRepositoryProvider).streamUserOrders(userId);
});
