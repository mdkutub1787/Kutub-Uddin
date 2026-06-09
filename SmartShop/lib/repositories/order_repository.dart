import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/order_model.dart';

class OrderRepository {
  final CollectionReference _ordersRef =
      FirebaseFirestore.instance.collection('orders');

  Future<void> placeOrder(OrderModel order) async {
    await _ordersRef.add(order.toMap());
  }

  Stream<List<OrderModel>> getUserOrders(String userId) {
    return _ordersRef
        .where('userId', isEqualTo: userId)
        .orderBy('date', descending: true)
        .snapshots()
        .map((snapshot) {
      return snapshot.docs.map((doc) => OrderModel.fromFirestore(doc)).toList();
    });
  }

  // Admin: Get all orders
  Stream<List<OrderModel>> getAllOrders() {
    return _ordersRef.orderBy('date', descending: true).snapshots().map((snapshot) {
      return snapshot.docs.map((doc) => OrderModel.fromFirestore(doc)).toList();
    });
  }

  Future<void> updateOrderStatus(String orderId, String status) async {
    await _ordersRef.doc(orderId).update({'status': status});
  }
}
