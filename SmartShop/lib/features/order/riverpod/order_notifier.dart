import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/order_model.dart';
import '../repositories/order_repository.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';
import '../../admin/riverpod/activity_log_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../user/models/user_model.dart';
import '../../../core/riverpod/admin_shop_filter_notifier.dart';

final orderNotifierProvider = AsyncNotifierProvider<OrderNotifier, List<OrderModel>>(() {
  return OrderNotifier();
});

final availableOrdersProvider = StreamProvider<List<OrderModel>>((ref) {
  final user = ref.watch(authNotifierProvider).value;
  return ref.watch(orderRepositoryProvider).streamAvailableOrders(zoneId: user?.deliveryZoneId);
});

final myDeliveriesProvider = StreamProvider.family<List<OrderModel>, String>((ref, deliveryManId) {
  return ref.watch(orderRepositoryProvider).streamMyDeliveries(deliveryManId);
});

final myCompletedDeliveriesProvider = StreamProvider.family<List<OrderModel>, String>((ref, deliveryManId) {
  return ref.watch(orderRepositoryProvider).streamMyCompletedDeliveries(deliveryManId);
});

final userOrdersStreamProvider = StreamProvider.family<List<OrderModel>, String>((ref, userId) {
  return ref.watch(orderRepositoryProvider).streamUserOrders(userId);
});



class OrderNotifier extends AsyncNotifier<List<OrderModel>> {
  late OrderRepository _repository;

  @override
  Future<List<OrderModel>> build() async {
    _repository = ref.read(orderRepositoryProvider);
    final user = ref.read(authNotifierProvider).value;
    final adminShopId = ref.read(adminShopFilterProvider);

    final isAdmin = (user?.role == 'super_admin' || user?.role == 'admin');

    if (isAdmin && adminShopId != null) {
      return _repository.getOrdersByShop(adminShopId);
    }
    if (user != null && user.role == 'owner') {
      if (user.shopId != null && user.shopId!.isNotEmpty) {
        return _repository.getOrdersByShop(user.shopId!);
      }
      return [];
    }
    return _repository.fetchOrders();
  }

  Future<void> loadOrders() async {
    state = const AsyncLoading();
    final user = ref.read(authNotifierProvider).value;
    final adminShopId = ref.read(adminShopFilterProvider);
    
    final isAdmin = (user?.role == 'super_admin' || user?.role == 'admin');

    if (isAdmin && adminShopId != null) {
      state = await AsyncValue.guard(() => _repository.getOrdersByShop(adminShopId));
    } else if (user != null && user.role == 'owner') {
      if (user.shopId != null && user.shopId!.isNotEmpty) {
        state = await AsyncValue.guard(() => _repository.getOrdersByShop(user.shopId!));
      } else {
        state = const AsyncData([]); // No shop, no orders
      }
    } else {
      // Super Admin sees everything
      state = await AsyncValue.guard(() => _repository.fetchOrders());
    }
  }

  Future<bool> placeOrder(OrderModel order) async {
    final client = ref.read(supabaseClientProvider);
    
    try {
      final orderId = await _repository.createOrder(order);
      if (orderId == null) return false;

      for (var item in order.items) {
        final currentProduct = await client
            .from(AppConstants.productsTable)
            .select('stock')
            .eq('id', item.product.id)
            .maybeSingle();
        
        if (currentProduct != null) {
          int newStock = (currentProduct['stock'] as int) - item.quantity;
          await client
              .from(AppConstants.productsTable)
              .update({'stock': newStock})
              .eq('id', item.product.id);
        }
      }

      if (order.orderType == 'pos') {
        final currentUser = ref.read(authNotifierProvider).value;
        ref.read(activityLogNotifierProvider.notifier).logAction(
          adminId: currentUser?.uid ?? 'unknown',
          adminName: currentUser?.name ?? 'Admin',
          action: 'POS Sale Created',
          targetId: orderId,
          details: 'Walk-in sale of ${order.items.length} items. Total: ${order.totalAmount}',
        );
      }

      await loadOrders();
      return true;
    } catch (e) {
      return false;
    }
  }

  Future<void> updateOrderStatus(String orderId, String status) async {
    await _repository.updateOrderStatus(orderId, status);
    
    final currentUser = ref.read(authNotifierProvider).value;
    ref.read(activityLogNotifierProvider.notifier).logAction(
      adminId: currentUser?.uid ?? 'unknown',
      adminName: currentUser?.name ?? 'Admin',
      action: 'Order Status Updated',
      targetId: orderId,
      details: 'Status changed to $status',
    );
    
    await loadOrders();
  }

  Future<void> updateOrderLocation(String orderId, double lat, double lng) async {
    await _repository.updateDeliveryLocation(orderId, lat, lng);
  }

  Future<bool> acceptOrder(OrderModel order, UserModel deliveryMan) async {
    try {
      await _repository.acceptOrder(order.id, deliveryMan);
      await loadOrders();
      return true;
    } catch (e) {
      return false;
    }
  }

  Future<void> cancelOrder(OrderModel order) async {
    await _repository.updateOrderStatus(order.id, 'Cancelled');
    
    final client = ref.read(supabaseClientProvider);
    for (var item in order.items) {
       final res = await client.from(AppConstants.productsTable).select('stock').eq('id', item.product.id).maybeSingle();
       if (res != null) {
         int restoredStock = (res['stock'] as int) + item.quantity;
         await client.from(AppConstants.productsTable).update({'stock': restoredStock}).eq('id', item.product.id);
       }
    }
    
    await loadOrders();
  }

  Future<void> deleteOrder(String orderId) async {
    await _repository.deleteOrder(orderId);
    await loadOrders();
  }
}
