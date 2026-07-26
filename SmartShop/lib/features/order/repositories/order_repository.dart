import 'package:supabase_flutter/supabase_flutter.dart';
import '../models/order_model.dart';
import '../../user/models/user_model.dart'; // We haven't migrated UserModel fully yet, but we will soon

class OrderRepository {
  final SupabaseClient _supabase;
  static const String _table = 'orders';

  OrderRepository(this._supabase);

  Future<bool> placeOrder(OrderModel order) async {
    try {
      // 1. Insert order
      await _supabase.from(_table).insert(order.toJson());

      // 2. Adjust stock
      for (var item in order.items) {
        final productId = item.product.id;
        final quantity = item.quantity;
        
        final productData = await _supabase.from('products').select('stock').eq('id', productId).single();
        final currentStock = productData['stock'] as int;
        
        if (currentStock >= quantity) {
          await _supabase.from('products').update({'stock': currentStock - quantity}).eq('id', productId);
        }
      }
      return true;
    } catch (e) {
      return false;
    }
  }

  Future<List<OrderModel>> getUserOrders(String userId) async {
    try {
      final response = await _supabase
          .from(_table)
          .select()
          .eq('userId', userId)
          .order('date', ascending: false);
      return (response as List).map((json) => OrderModel.fromJson(json)).toList();
    } catch (e) {
      return [];
    }
  }

  Future<List<OrderModel>> getOrdersByShop(String shopId) async {
    try {
      final response = await _supabase
          .from(_table)
          .select()
          .eq('shopId', shopId)
          .order('date', ascending: false);
      return (response as List).map((json) => OrderModel.fromJson(json)).toList();
    } catch (e) {
      return [];
    }
  }

  Future<List<OrderModel>> getAllOrders() async {
    try {
      final response = await _supabase
          .from(_table)
          .select()
          .order('date', ascending: false);
      return (response as List).map((json) => OrderModel.fromJson(json)).toList();
    } catch (e) {
      return [];
    }
  }

  Future<void> updateOrderStatus(String orderId, String status) async {
    await _supabase.from(_table).update({'status': status}).eq('id', orderId);
  }

  Future<void> updateDeliveryLocation(String orderId, double lat, double lng) async {
    await _supabase.from(_table).update({
      'delivery_lat': lat,
      'delivery_lng': lng,
    }).eq('id', orderId);
  }

  Future<void> deleteOrder(String orderId) async {
    await _supabase.from(_table).delete().eq('id', orderId);
  }

  Future<bool> cancelOrder(OrderModel order) async {
    try {
      final response = await _supabase.from(_table).select('status').eq('id', order.id).single();
      if (response['status'] != 'Pending') return false;

      await updateOrderStatus(order.id, 'Cancelled');

      for (var item in order.items) {
        final productId = item.product.id;
        final quantity = item.quantity;
        final productData = await _supabase.from('products').select('stock').eq('id', productId).single();
        final currentStock = productData['stock'] as int;
        await _supabase.from('products').update({'stock': currentStock + quantity}).eq('id', productId);
      }
      return true;
    } catch (e) {
      return false;
    }
  }

  Stream<List<OrderModel>> streamAvailableOrders() {
    return _supabase
        .from(_table)
        .stream(primaryKey: ['id'])
        .map((maps) {
          return maps
              .map((map) => OrderModel.fromJson(map))
              .where((o) => (o.status == 'Pending' || o.status == 'Confirmed') && o.deliveryManId == null && o.orderType != 'pos')
              .toList()
            ..sort((a, b) => b.date.compareTo(a.date));
        });
  }

  Stream<List<OrderModel>> streamMyDeliveries(String deliveryManId) {
    return _supabase
        .from(_table)
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
    await _supabase.from(_table).update({
      'deliveryManId': deliveryMan.uid,
      'deliveryManName': deliveryMan.name,
      'deliveryManPhone': deliveryMan.phoneNumber,
      'status': 'Assigned',
    }).eq('id', orderId);
  }
}
