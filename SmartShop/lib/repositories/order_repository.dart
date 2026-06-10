import 'package:firebase_database/firebase_database.dart';
import '../models/order_model.dart';

class OrderRepository {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref();

  Future<bool> placeOrder(OrderModel order) async {
    try {
      // Generate numeric ID for Order
      String orderId = DateTime.now().millisecondsSinceEpoch.toString();
      
      // Set order with timeout
      await _dbRef.child('orders').child(orderId).set(order.toMap()).timeout(
        const Duration(seconds: 15),
        onTimeout: () => throw Exception("Order placement timed out"),
      );

      // Adjust stock
      for (var item in order.items) {
        final productRef = _dbRef.child('products').child(item.product.id);
        await productRef.runTransaction((Object? post) {
          if (post == null) return Transaction.abort();
          Map<String, dynamic> product = Map<String, dynamic>.from(post as Map);
          int currentStock = product['stock'] ?? 0;
          if (currentStock < item.quantity) return Transaction.abort();
          product['stock'] = currentStock - item.quantity;
          return Transaction.success(product);
        }).timeout(
          const Duration(seconds: 10),
          onTimeout: () => throw Exception("Stock adjustment timed out"),
        );
      }
      return true;
    } catch (e) {
      print("Error placing order: $e");
      return false;
    }
  }

  Stream<List<OrderModel>> getUserOrders(String userId) {
    return _dbRef.child('orders').onValue.map((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data == null) return [];

      return data.entries
          .map((entry) => OrderModel.fromSnapshot(event.snapshot.child(entry.key)))
          .where((order) => order.userId == userId)
          .toList()
        ..sort((a, b) => b.date.compareTo(a.date));
    });
  }

  Stream<List<OrderModel>> getAllOrders() {
    return _dbRef.child('orders').onValue.map((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data == null) return [];

      return data.entries
          .map((entry) => OrderModel.fromSnapshot(event.snapshot.child(entry.key)))
          .toList()
        ..sort((a, b) => b.date.compareTo(a.date));
    });
  }

  Future<void> updateOrderStatus(String orderId, String status) async {
    await _dbRef.child('orders').child(orderId).update({'status': status});
  }

  Future<void> deleteOrder(String orderId) async {
    await _dbRef.child('orders').child(orderId).remove();
  }

  Future<bool> cancelOrder(OrderModel order) async {
    try {
      // 1. Double check status is still Pending in DB
      final snapshot = await _dbRef.child('orders').child(order.id).get();
      if (!snapshot.exists) return false;
      
      final data = snapshot.value as Map<dynamic, dynamic>;
      if (data['status'] != 'Pending') return false;

      // 2. Update status to Cancelled
      await _dbRef.child('orders').child(order.id).update({'status': 'Cancelled'});

      // 3. Restore stock
      for (var item in order.items) {
        final productRef = _dbRef.child('products').child(item.product.id);
        await productRef.runTransaction((Object? post) {
          if (post == null) return Transaction.abort();
          Map<String, dynamic> product = Map<String, dynamic>.from(post as Map);
          int currentStock = product['stock'] ?? 0;
          product['stock'] = currentStock + item.quantity;
          return Transaction.success(product);
        });
      }
      return true;
    } catch (e) {
      print("Error cancelling order: $e");
      return false;
    }
  }
}
