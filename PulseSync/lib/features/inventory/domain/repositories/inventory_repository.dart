import '../entities/inventory_item.dart';

abstract class InventoryRepository {
  Future<List<InventoryItem>> searchInventory(String query);
  Future<void> syncInventory();
}
