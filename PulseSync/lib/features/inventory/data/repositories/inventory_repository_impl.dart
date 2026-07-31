import 'dart:convert';
import 'dart:isolate';
import 'package:flutter/foundation.dart';
import 'package:injectable/injectable.dart';
import '../../domain/entities/inventory_item.dart';
import '../../domain/repositories/inventory_repository.dart';
import '../models/inventory_model.dart';

@LazySingleton(as: InventoryRepository)
class InventoryRepositoryImpl implements InventoryRepository {
  
  // Simulated large mock data (JSON string)
  final String _mockJsonData = '''
  [
    {"id": "1", "name": "Industrial Widget A", "category": "Parts", "quantity": 100, "price": 10.5, "lastUpdated": "2026-07-31T10:00:00Z"},
    {"id": "2", "name": "Industrial Widget B", "category": "Parts", "quantity": 50, "price": 25.0, "lastUpdated": "2026-07-31T10:05:00Z"},
    {"id": "3", "name": "Heavy Machinery X", "category": "Equipment", "quantity": 2, "price": 15000.0, "lastUpdated": "2026-07-30T14:30:00Z"},
    {"id": "4", "name": "Lubricant Oil 50L", "category": "Consumables", "quantity": 20, "price": 120.0, "lastUpdated": "2026-07-31T08:15:00Z"},
    {"id": "5", "name": "Safety Harness", "category": "Safety", "quantity": 75, "price": 45.0, "lastUpdated": "2026-07-29T11:00:00Z"}
  ]
  ''';

  @override
  Future<List<InventoryItem>> searchInventory(String query) async {
    // We use Isolate.run to parse and filter the data in a background isolate
    // to prevent blocking the main thread when dealing with massive datasets.
    // However, Isolate is not supported on the web.
    if (kIsWeb) {
      return _parseAndFilter(_mockJsonData, query);
    } else {
      return await Isolate.run(() => _parseAndFilter(_mockJsonData, query));
    }
  }

  static List<InventoryItem> _parseAndFilter(String jsonStr, String query) {
    // Simulate heavy computation/parsing delay
    // ignore: avoid_print
    print('Isolate: Parsing JSON and filtering for "$query"...');
    
    // Simulating delay for large dataset
    int i = 0;
    while(i < 50000000) { i++; } // artificial delay

    final List<dynamic> parsed = jsonDecode(jsonStr);
    final allItems = parsed.map((json) => InventoryModel.fromJson(json)).toList();

    if (query.isEmpty) {
      return allItems;
    }

    final lowerQuery = query.toLowerCase();
    return allItems.where((item) => 
      item.name.toLowerCase().contains(lowerQuery) || 
      item.category.toLowerCase().contains(lowerQuery) ||
      item.id.toLowerCase().contains(lowerQuery)
    ).toList();
  }

  @override
  Future<void> syncInventory() async {
    // Simulate a network sync request
    await Future.delayed(const Duration(seconds: 2));
  }
}
