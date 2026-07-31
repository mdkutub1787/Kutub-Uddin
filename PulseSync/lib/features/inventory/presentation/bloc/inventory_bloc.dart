import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:injectable/injectable.dart';
import 'package:rxdart/rxdart.dart';
import '../../domain/usecases/search_inventory.dart';
import 'inventory_event.dart';
import 'inventory_state.dart';

EventTransformer<T> debounce<T>(Duration duration) {
  return (events, mapper) => events.debounceTime(duration).flatMap(mapper);
}

@injectable
class InventoryBloc extends HydratedBloc<InventoryEvent, InventoryState> {
  final SearchInventoryUseCase searchUseCase;

  InventoryBloc(this.searchUseCase) : super(InventoryInitial()) {
    on<SearchInputChanged>(
      _onSearchInputChanged,
      transformer: debounce(const Duration(milliseconds: 300)),
    );
    
    on<SyncRequested>(_onSyncRequested);
    
    // Initial load
    add(const SearchInputChanged(''));
  }

  Future<void> _onSearchInputChanged(
    SearchInputChanged event,
    Emitter<InventoryState> emit,
  ) async {
    // If not already loaded, emit loading
    if (state is! InventoryLoaded) {
      emit(InventoryLoading());
    } else {
      // Retain old items while searching
      final currentState = state as InventoryLoaded;
      emit(currentState.copyWith(isSyncing: true)); // Or a separate search loading indicator
    }

    try {
      final items = await searchUseCase(event.query);
      emit(InventoryLoaded(
        items: items,
        currentQuery: event.query,
        isSyncing: false,
      ));
    } catch (e) {
      emit(InventoryError(e.toString()));
    }
  }

  Future<void> _onSyncRequested(
    SyncRequested event,
    Emitter<InventoryState> emit,
  ) async {
    if (state is InventoryLoaded) {
      final currentState = state as InventoryLoaded;
      emit(currentState.copyWith(isSyncing: true));
      
      try {
        await searchUseCase.repository.syncInventory();
        // Reload current query
        add(SearchInputChanged(currentState.currentQuery));
      } catch (e) {
        emit(InventoryError("Sync failed: \${e.toString()}"));
      }
    }
  }

  // HydratedBloc Persistence
  @override
  InventoryState? fromJson(Map<String, dynamic> json) {
    // Basic implementation for caching the last known good state
    // For a full implementation, we'd serialize/deserialize the InventoryItem list
    // This is just a placeholder demonstrating the offline capability setup.
    return null;
  }

  @override
  Map<String, dynamic>? toJson(InventoryState state) {
    // Serialize state to cache
    return null;
  }
}
