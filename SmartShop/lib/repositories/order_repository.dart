import 'package:firebase_database/firebase_database.dart';
import '../models/order_model.dart';

class OrderRepository {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref().child('orders');

  Future<void> placeOrder(OrderModel order) async {
    await _dbRef.push().set(order.toMap());
  }

  Stream<List<OrderModel>> getUserOrders(String userId) {
    return _dbRef.onValue.map((event) {
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
    return _dbRef.onValue.map((event) {
      final Map<dynamic, dynamic>? data = event.snapshot.value as Map<dynamic, dynamic>?;
      if (data == null) return [];

      return data.entries
          .map((entry) => OrderModel.fromSnapshot(event.snapshot.child(entry.key)))
          .toList()
        ..sort((a, b) => b.date.compareTo(a.date));
    });
  }

  Future<void> updateOrderStatus(String orderId, String status) async {
    await _dbRef.child(orderId).update({'status': status});
  }
}
