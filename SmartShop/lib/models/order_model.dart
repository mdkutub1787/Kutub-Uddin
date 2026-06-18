import 'package:firebase_database/firebase_database.dart';
import 'cart_model.dart';
import 'product_model.dart';

class OrderModel {
  final String id;
  final String shopId;
  final String userId;
  final String userName;
  final String userPhone;
  final String userAddress;
  final List<CartItem> items;
  final double totalAmount;
  final double deliveryFee;
  final DateTime date;
  final String status; // 'Pending', 'Confirmed', 'Assigned', 'PickedUp', 'OnTheWay', 'Delivered', 'Cancelled'
  final String orderType; // 'online' or 'pos'
  
  // Delivery related fields
  final String? deliveryManId;
  final String? deliveryManName;
  final String? deliveryManPhone;
  final double? deliveryLatitude;
  final double? deliveryLongitude;

  OrderModel({
    required this.id,
    required this.shopId,
    required this.userId,
    required this.userName,
    required this.userPhone,
    required this.userAddress,
    required this.items,
    required this.totalAmount,
    this.deliveryFee = 0.0,
    required this.date,
    required this.status,
    this.orderType = 'online',
    this.deliveryManId,
    this.deliveryManName,
    this.deliveryManPhone,
    this.deliveryLatitude,
    this.deliveryLongitude,
  });

  Map<String, dynamic> toMap() {
    return {
      'shopId': shopId,
      'userId': userId,
      'userName': userName,
      'userPhone': userPhone,
      'userAddress': userAddress,
      'items': items.map((item) => {
        'productId': item.product.id,
        'productName': item.product.name,
        'quantity': item.quantity,
        'price': item.product.price,
        'imageUrl': item.product.imageUrl,
      }).toList(),
      'totalAmount': totalAmount,
      'deliveryFee': deliveryFee,
      'date': date.toIso8601String(),
      'status': status,
      'orderType': orderType,
      'deliveryManId': deliveryManId,
      'deliveryManName': deliveryManName,
      'deliveryManPhone': deliveryManPhone,
      'deliveryLatitude': deliveryLatitude,
      'deliveryLongitude': deliveryLongitude,
    };
  }

  factory OrderModel.fromSnapshot(DataSnapshot snapshot) {
    final Map<dynamic, dynamic> data = snapshot.value as Map<dynamic, dynamic>;
    
    List<CartItem> orderItems = [];
    if (data['items'] != null) {
      final itemsList = data['items'] as List<dynamic>;
      orderItems = itemsList.map((item) {
        final itemMap = Map<String, dynamic>.from(item as Map);
        return CartItem(
          product: ProductModel(
            id: itemMap['productId'] ?? '',
            shopId: data['shopId'] ?? '',
            name: itemMap['productName'] ?? 'Product',
            description: '',
            price: (itemMap['price'] ?? 0).toDouble(),
            originalPrice: (itemMap['price'] ?? 0).toDouble(),
            imageUrl: itemMap['imageUrl'] ?? '',
            categoryId: '',
            rating: 0,
            stock: 0,
          ),
          quantity: itemMap['quantity'] ?? 1,
        );
      }).toList();
    }

    return OrderModel(
      id: snapshot.key ?? '',
      shopId: data['shopId'] ?? '',
      userId: data['userId'] ?? '',
      userName: data['userName'] ?? 'User',
      userPhone: data['userPhone'] ?? '',
      userAddress: data['userAddress'] ?? '',
      items: orderItems,
      totalAmount: (data['totalAmount'] ?? 0).toDouble(),
      deliveryFee: (data['deliveryFee'] ?? 0).toDouble(),
      date: data['date'] != null ? DateTime.parse(data['date']) : DateTime.now(),
      status: data['status'] ?? 'Pending',
      orderType: data['orderType'] ?? 'online',
      deliveryManId: data['deliveryManId'],
      deliveryManName: data['deliveryManName'],
      deliveryManPhone: data['deliveryManPhone'],
      deliveryLatitude: data['deliveryLatitude']?.toDouble(),
      deliveryLongitude: data['deliveryLongitude']?.toDouble(),
    );
  }
}
