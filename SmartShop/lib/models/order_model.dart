import 'package:cloud_firestore/cloud_firestore.dart';
import 'cart_model.dart';

class OrderModel {
  final String id;
  final String userId;
  final List<CartItem> items;
  final double totalAmount;
  final DateTime date;
  final String status; // 'Pending', 'Processing', 'Shipped', 'Delivered', 'Cancelled'
  final String shippingAddress;

  OrderModel({
    required this.id,
    required this.userId,
    required this.items,
    required this.totalAmount,
    required this.date,
    required this.status,
    required this.shippingAddress,
  });

  Map<String, dynamic> toMap() {
    return {
      'userId': userId,
      'items': items.map((item) => {
        'productId': item.product.id,
        'productName': item.product.name,
        'quantity': item.quantity,
        'price': item.product.price,
        'imageUrl': item.product.imageUrl,
      }).toList(),
      'totalAmount': totalAmount,
      'date': Timestamp.fromDate(date),
      'status': status,
      'shippingAddress': shippingAddress,
    };
  }

  factory OrderModel.fromFirestore(DocumentSnapshot doc) {
    Map<String, dynamic> data = doc.data() as Map<String, dynamic>;
    return OrderModel(
      id: doc.id,
      userId: data['userId'] ?? '',
      items: [], // Simplified for now as we usually don't need full ProductModel in history
      totalAmount: (data['totalAmount'] ?? 0).toDouble(),
      date: (data['date'] as Timestamp).toDate(),
      status: data['status'] ?? 'Pending',
      shippingAddress: data['shippingAddress'] ?? '',
    );
  }
}
