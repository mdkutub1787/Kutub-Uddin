import 'package:equatable/equatable.dart';
import '../../domain/entities/inventory_item.dart';

sealed class InventoryState extends Equatable {
  const InventoryState();
  
  @override
  List<Object?> get props => [];
}

class InventoryInitial extends InventoryState {}

class InventoryLoading extends InventoryState {}

class InventoryLoaded extends InventoryState {
  final List<InventoryItem> items;
  final String currentQuery;
  final bool isSyncing;

  const InventoryLoaded({
    required this.items,
    this.currentQuery = '',
    this.isSyncing = false,
  });

  @override
  List<Object?> get props => [items, currentQuery, isSyncing];

  InventoryLoaded copyWith({
    List<InventoryItem>? items,
    String? currentQuery,
    bool? isSyncing,
  }) {
    return InventoryLoaded(
      items: items ?? this.items,
      currentQuery: currentQuery ?? this.currentQuery,
      isSyncing: isSyncing ?? this.isSyncing,
    );
  }
}

class InventoryError extends InventoryState {
  final String message;

  const InventoryError(this.message);

  @override
  List<Object?> get props => [message];
}
