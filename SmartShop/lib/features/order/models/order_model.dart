import '../../cart/models/cart_model.dart';
import '../../product/models/product_model.dart';

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

  Map<String, dynamic> toJson() {
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
      'delivery_lat': deliveryLatitude,
      'delivery_lng': deliveryLongitude,
    };
  }

  factory OrderModel.fromJson(Map<String, dynamic> data) {
    List<CartItem> orderItems = [];
    try {
      if (data['items'] != null) {
        if (data['items'] is List) {
          final itemsList = data['items'] as List<dynamic>;
          orderItems = itemsList.map((item) {
            final itemMap = Map<String, dynamic>.from(item as Map);
            return CartItem(
              product: ProductModel(
                id: itemMap['productId']?.toString() ?? '',
                shopId: data['shopId']?.toString() ?? '',
                name: itemMap['productName'] ?? 'Product',
                description: '',
                price: double.tryParse(itemMap['price']?.toString() ?? '0') ?? 0.0,
                originalPrice: double.tryParse(itemMap['price']?.toString() ?? '0') ?? 0.0,
                imageUrl: itemMap['imageUrl'] ?? '',
                categoryId: '',
                rating: 0,
                stock: 0,
              ),
              quantity: int.tryParse(itemMap['quantity']?.toString() ?? '1') ?? 1,
            );
          }).toList();
        }
      }
    } catch (e) {
      print('Error parsing order items: $e');
    }

    return OrderModel(
      id: data['id']?.toString() ?? '',
      shopId: data['shopId']?.toString() ?? '',
      userId: data['userId']?.toString() ?? '',
      userName: data['userName'] ?? 'User',
      userPhone: data['userPhone'] ?? '',
      userAddress: data['userAddress'] ?? '',
      items: orderItems,
      totalAmount: double.tryParse(data['totalAmount']?.toString() ?? '0') ?? 0.0,
      deliveryFee: double.tryParse(data['deliveryFee']?.toString() ?? '0') ?? 0.0,
      date: data['date'] != null ? DateTime.parse(data['date']) : (data['created_at'] != null ? DateTime.parse(data['created_at']) : DateTime.now()),
      status: data['status'] ?? 'Pending',
      orderType: data['orderType'] ?? 'online',
      deliveryManId: data['deliveryManId']?.toString(),
      deliveryManName: data['deliveryManName'],
      deliveryManPhone: data['deliveryManPhone'],
      deliveryLatitude: data['delivery_lat'] != null ? (data['delivery_lat'] as num).toDouble() : null,
      deliveryLongitude: data['delivery_lng'] != null ? (data['delivery_lng'] as num).toDouble() : null,
    );
  }
}
