import 'package:equatable/equatable.dart';

class InventoryItem extends Equatable {
  final String id;
  final String name;
  final String category;
  final int quantity;
  final double price;
  final DateTime lastUpdated;

  const InventoryItem({
    required this.id,
    required this.name,
    required this.category,
    required this.quantity,
    required this.price,
    required this.lastUpdated,
  });

  @override
  List<Object?> get props => [id, name, category, quantity, price, lastUpdated];
}
