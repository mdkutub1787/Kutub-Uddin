import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/providers.dart';
import '../models/order_model.dart';
import '../repositories/order_repository.dart';

final orderNotifierProvider = AsyncNotifierProvider<OrderNotifier, List<OrderModel>>(() {
  return OrderNotifier();
});

class OrderNotifier extends AsyncNotifier<List<OrderModel>> {
  late final OrderRepository _repository;

  @override
  FutureOr<List<OrderModel>> build() async {
    _repository = OrderRepository(ref.watch(supabaseClientProvider));
    return await _fetchOrders();
  }

  Future<List<OrderModel>> _fetchOrders() async {
    final user = ref.read(authNotifierProvider).value;
    if (user != null) {
      if (user.role == 'admin') {
        return await _repository.getAllOrders();
      } else {
        return await _repository.getUserOrders(user.uid);
      }
    }
    return [];
  }

  Future<void> loadOrders() async {
    state = const AsyncValue.loading();
    try {
      final orders = await _fetchOrders();
      state = AsyncValue.data(orders);
    } catch (e, stackTrace) {
      state = AsyncValue.error(e, stackTrace);
    }
  }

  Future<bool> placeOrder(OrderModel order) async {
    try {
      final success = await _repository.placeOrder(order);
      if (success) {
        await loadOrders(); // Refresh the list
      }
      return success;
    } catch (e) {
      return false;
    }
  }

  Future<void> updateOrderStatus(String orderId, String status) async {
    try {
      await _repository.updateOrderStatus(orderId, status);
      await loadOrders(); // Refresh the list
    } catch (e) {
      rethrow;
    }
  }

  Future<bool> cancelOrder(OrderModel order) async {
    try {
      final success = await _repository.cancelOrder(order);
      if (success) {
        await loadOrders(); // Refresh the list
      }
      return success;
    } catch (e) {
      return false;
    }
  }

  Future<void> deleteOrder(String orderId) async {
    try {
      await _repository.deleteOrder(orderId);
      await loadOrders(); // Refresh the list
    } catch (e) {
      rethrow;
    }
  }
}
