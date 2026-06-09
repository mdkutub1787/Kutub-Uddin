import 'package:firebase_database/firebase_database.dart';
import 'cart_model.dart';
import 'product_model.dart';

class OrderModel {
  final String id;
  final String userId;
  final List<CartItem> items;
  final double totalAmount;
  final DateTime date;
  final String status;
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
      'date': date.toIso8601String(),
      'status': status,
      'shippingAddress': shippingAddress,
    };
  }

  factory OrderModel.fromSnapshot(DataSnapshot snapshot) {
    Map<dynamic, dynamic> data = snapshot.value as Map<dynamic, dynamic>;
    return OrderModel(
      id: snapshot.key ?? '',
      userId: data['userId'] ?? '',
      items: [], // Simplified
      totalAmount: (data['totalAmount'] ?? 0).toDouble(),
      date: DateTime.parse(data['date']),
      status: data['status'] ?? 'Pending',
      shippingAddress: data['shippingAddress'] ?? '',
    );
  }
}
