import 'package:equatable/equatable.dart';

sealed class InventoryEvent extends Equatable {
  const InventoryEvent();

  @override
  List<Object?> get props => [];
}

class SearchInputChanged extends InventoryEvent {
  final String query;

  const SearchInputChanged(this.query);

  @override
  List<Object?> get props => [query];
}

class SyncRequested extends InventoryEvent {
  const SyncRequested();
}
