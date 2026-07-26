import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/providers.dart';
import '../models/order_model.dart';
import '../repositories/order_repository.dart';

final orderRepositoryProvider = Provider<OrderRepository>((ref) {
  return OrderRepository(ref.watch(supabaseClientProvider));
});

final orderNotifierProvider = AsyncNotifierProvider<OrderNotifier, List<OrderModel>>(() {
  return OrderNotifier();
});

final availableOrdersProvider = StreamProvider<List<OrderModel>>((ref) {
  return ref.watch(orderRepositoryProvider).streamAvailableOrders();
});

final myDeliveriesProvider = StreamProvider.family<List<OrderModel>, String>((ref, deliveryManId) {
  return ref.watch(orderRepositoryProvider).streamMyDeliveries(deliveryManId);
});

class OrderNotifier extends AsyncNotifier<List<OrderModel>> {
  late final OrderRepository _repository;

  @override
  FutureOr<List<OrderModel>> build() async {
    _repository = ref.watch(orderRepositoryProvider);
    return await _fetchOrders();
  }

  Future<List<OrderModel>> _fetchOrders() async {
    final user = ref.read(authNotifierProvider).value;
    if (user != null) {
      if (user.role == 'admin' || user.role == 'super_admin' || user.role == 'owner' || user.role == 'manager') {
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

  Future<bool> acceptOrder(OrderModel order, dynamic deliveryMan) async {
    try {
      await _repository.acceptOrder(order.id, deliveryMan);
      // No need to loadOrders() if we are using streams in the UI
      return true;
    } catch (e) {
      return false;
    }
  }

  Future<void> updateOrderLocation(String orderId, double lat, double lng) async {
    try {
      await _repository.updateDeliveryLocation(orderId, lat, lng);
    } catch (e) {
      // Ignore if it fails
    }
  }
}
