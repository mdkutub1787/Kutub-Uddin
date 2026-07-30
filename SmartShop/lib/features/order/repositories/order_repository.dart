import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/providers.dart';
import '../../../core/constants/constants.dart';
import '../models/order_model.dart';
import '../../user/models/user_model.dart';

class OrderRepository {
  final SupabaseClient _supabase;

  OrderRepository(this._supabase);

  Future<List<OrderModel>> getOrdersByShop(String shopId) async {
    try {
      final response = await _supabase
          .from(AppConstants.ordersTable)
          .select()
          .eq('shopId', shopId)
          .order('date', ascending: false);
      return (response as List).map((json) => OrderModel.fromJson(json)).toList();
    } catch (e) {
      return [];
    }
  }

  Future<List<OrderModel>> fetchOrders() async {
    try {
      final response = await _supabase
          .from(AppConstants.ordersTable)
          .select()
          .order('date', ascending: false);
      return (response as List).map((json) => OrderModel.fromJson(json)).toList();
    } catch (e) {
      return [];
    }
  }

  Future<String?> createOrder(OrderModel order) async {
    try {
      final response = await _supabase
          .from(AppConstants.ordersTable)
          .insert(order.toJson())
          .select('id')
          .single();
      return response['id']?.toString();
    } catch (e) {
      print('Error creating order: $e');
      return null;
    }
  }

  Future<void> updateOrderStatus(String orderId, String status) async {
    await _supabase
        .from(AppConstants.ordersTable)
        .update({'status': status})
        .eq('id', orderId);
  }

  Future<void> updateDeliveryLocation(String orderId, double lat, double lng) async {
    await _supabase.from(AppConstants.ordersTable).update({
      'deliveryLatitude': lat,
      'deliveryLongitude': lng,
    }).eq('id', orderId);
  }

  Future<void> deleteOrder(String orderId) async {
    await _supabase.from(AppConstants.ordersTable).delete().eq('id', orderId);
  }

  Stream<List<OrderModel>> streamAvailableOrders({String? zoneId}) {
    return _supabase
        .from(AppConstants.ordersTable)
        .stream(primaryKey: ['id'])
        .map((maps) {
          return maps
              .map((map) => OrderModel.fromJson(map))
              .where((o) => (o.status == 'Pending' || o.status == 'Confirmed') && o.deliveryManId == null && o.orderType != 'pos' && (zoneId == null || o.deliveryZoneId == zoneId))
              .toList()
            ..sort((a, b) => b.date.compareTo(a.date));
        });
  }

  Stream<List<OrderModel>> streamMyDeliveries(String deliveryManId) {
    return _supabase
        .from(AppConstants.ordersTable)
        .stream(primaryKey: ['id'])
        .map((maps) {
          return maps
              .map((map) => OrderModel.fromJson(map))
              .where((o) => o.deliveryManId == deliveryManId && o.status != 'Delivered' && o.status != 'Cancelled')
              .toList()
            ..sort((a, b) => b.date.compareTo(a.date));
        });
  }

  Future<void> acceptOrder(String orderId, UserModel deliveryMan) async {
    await _supabase.from(AppConstants.ordersTable).update({
      'deliveryManId': deliveryMan.uid,
      'deliveryManName': deliveryMan.name,
      'deliveryManPhone': deliveryMan.phoneNumber,
      'deliveryManImage': deliveryMan.imageUrl,
      'status': 'Assigned',
    }).eq('id', orderId);
  }

  Stream<List<OrderModel>> streamMyCompletedDeliveries(String deliveryManId) {
    return _supabase
        .from(AppConstants.ordersTable)
        .stream(primaryKey: ['id'])
        .map((maps) {
          return maps
              .map((map) => OrderModel.fromJson(map))
              .where((o) => o.deliveryManId == deliveryManId && o.status == 'Delivered')
              .toList()
            ..sort((a, b) => b.date.compareTo(a.date));
        });
  }

  Stream<List<OrderModel>> streamUserOrders(String userId) {
    return _supabase
        .from(AppConstants.ordersTable)
        .stream(primaryKey: ['id'])
        .map((maps) {
          return maps
              .map((map) => OrderModel.fromJson(map))
              .where((o) => o.userId == userId)
              .toList()
            ..sort((a, b) => b.date.compareTo(a.date));
        });
  }
}

final orderRepositoryProvider = Provider<OrderRepository>((ref) {
  return OrderRepository(ref.watch(supabaseClientProvider));
});
