import 'package:supabase_flutter/supabase_flutter.dart';
import '../models/order_model.dart';
import '../../user/models/user_model.dart'; // We haven't migrated UserModel fully yet, but we will soon

class OrderRepository {
  final SupabaseClient _supabase;

  OrderRepository(this._supabase);

  Future<bool> placeOrder(OrderModel order) async {
    try {
      // 1. Insert order
      await _supabase.from('orders').insert(order.toJson());

      // 2. Adjust stock (This ideally should be a database function/RPC for transactions)
      // Since we don't have an RPC setup here yet, we'll do sequential updates
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
      print("Error placing order: $e");
      return false;
    }
  }

  Future<List<OrderModel>> getUserOrders(String userId) async {
    final response = await _supabase
        .from('orders')
        .select()
        .eq('userId', userId)
        .order('date', ascending: false);

    return (response as List).map((json) => OrderModel.fromJson(json)).toList();
  }

  Future<List<OrderModel>> getOrdersByShop(String shopId) async {
    final response = await _supabase
        .from('orders')
        .select()
        .eq('shopId', shopId)
        .order('date', ascending: false);

    return (response as List).map((json) => OrderModel.fromJson(json)).toList();
  }

  Future<List<OrderModel>> getAllOrders() async {
    final response = await _supabase
        .from('orders')
        .select()
        .order('date', ascending: false);

    return (response as List).map((json) => OrderModel.fromJson(json)).toList();
  }

  Future<void> updateOrderStatus(String orderId, String status) async {
    await _supabase
        .from('orders')
        .update({'status': status})
        .eq('id', orderId);
  }

  // Future<void> assignDeliveryMan(String orderId, UserModel deliveryMan) async {
  //   await _supabase.from('orders').update({
  //     'deliveryManId': deliveryMan.uid,
  //     'deliveryManName': deliveryMan.name,
  //     'deliveryManPhone': deliveryMan.phoneNumber,
  //     'status': 'Assigned',
  //   }).eq('id', orderId);
  // }

  Future<void> updateDeliveryLocation(String orderId, double lat, double lng) async {
    await _supabase.from('orders').update({
      'deliveryLatitude': lat,
      'deliveryLongitude': lng,
    }).eq('id', orderId);
  }

  Future<void> deleteOrder(String orderId) async {
    await _supabase.from('orders').delete().eq('id', orderId);
  }

  Future<bool> cancelOrder(OrderModel order) async {
    try {
      // 1. Double check status
      final response = await _supabase.from('orders').select('status').eq('id', order.id).single();
      if (response['status'] != 'Pending') return false;

      // 2. Update status
      await updateOrderStatus(order.id, 'Cancelled');

      // 3. Restore stock
      for (var item in order.items) {
        final productId = item.product.id;
        final quantity = item.quantity;
        
        final productData = await _supabase.from('products').select('stock').eq('id', productId).single();
        final currentStock = productData['stock'] as int;
        
        await _supabase.from('products').update({'stock': currentStock + quantity}).eq('id', productId);
      }
      return true;
    } catch (e) {
      print("Error cancelling order: $e");
      return false;
    }
  }
}
