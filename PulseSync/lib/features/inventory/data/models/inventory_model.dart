import '../../domain/entities/inventory_item.dart';

class InventoryModel extends InventoryItem {
  const InventoryModel({
    required super.id,
    required super.name,
    required super.category,
    required super.quantity,
    required super.price,
    required super.lastUpdated,
  });

  factory InventoryModel.fromJson(Map<String, dynamic> json) {
    return InventoryModel(
      id: json['id'] as String,
      name: json['name'] as String,
      category: json['category'] as String,
      quantity: json['quantity'] as int,
      price: (json['price'] as num).toDouble(),
      lastUpdated: DateTime.parse(json['lastUpdated'] as String),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'category': category,
      'quantity': quantity,
      'price': price,
      'lastUpdated': lastUpdated.toIso8601String(),
    };
  }
}
