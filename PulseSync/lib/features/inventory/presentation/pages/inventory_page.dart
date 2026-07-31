import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../../../../core/di/injection.dart';
import '../bloc/inventory_bloc.dart';
import '../bloc/inventory_event.dart';
import '../bloc/inventory_state.dart';

class InventoryPage extends StatelessWidget {
  const InventoryPage({super.key});

  @override
  Widget build(BuildContext context) {
    // Scoped BlocProvider that auto-disposes when the route pops
    return BlocProvider(
      create: (_) => getIt<InventoryBloc>(),
      child: const _InventoryView(),
    );
  }
}

class _InventoryView extends StatelessWidget {
  const _InventoryView();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Industrial Asset Inventory'),
        actions: [
          BlocBuilder<InventoryBloc, InventoryState>(
            builder: (context, state) {
              final isSyncing = state is InventoryLoaded && state.isSyncing;
              return IconButton(
                icon: isSyncing
                    ? const SizedBox(
                        width: 20,
                        height: 20,
                        child: CircularProgressIndicator(strokeWidth: 2),
                      )
                    : const Icon(Icons.sync),
                onPressed: isSyncing
                    ? null
                    : () {
                        context.read<InventoryBloc>().add(const SyncRequested());
                      },
              );
            },
          ),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: TextField(
              decoration: const InputDecoration(
                labelText: 'Scan or Search Asset',
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.search),
              ),
              onChanged: (value) {
                // RxDart in Bloc will debounce this rapid input
                context.read<InventoryBloc>().add(SearchInputChanged(value));
              },
            ),
          ),
          Expanded(
            child: BlocConsumer<InventoryBloc, InventoryState>(
              listener: (context, state) {
                if (state is InventoryError) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text(state.message), backgroundColor: Colors.red),
                  );
                }
              },
              builder: (context, state) {
                if (state is InventoryInitial || state is InventoryLoading) {
                  return const Center(child: CircularProgressIndicator());
                } else if (state is InventoryLoaded) {
                  if (state.items.isEmpty) {
                    return const Center(child: Text('No assets found.'));
                  }
                  return ListView.builder(
                    itemCount: state.items.length,
                    itemBuilder: (context, index) {
                      final item = state.items[index];
                      return ListTile(
                        leading: CircleAvatar(
                          child: Text(item.id),
                        ),
                        title: Text(item.name),
                        subtitle: Text('\${item.category} - Qty: \${item.quantity}'),
                        trailing: Text('\$\${item.price.toStringAsFixed(2)}'),
                      );
                    },
                  );
                } else if (state is InventoryError) {
                  return Center(child: Text('Error: \${state.message}'));
                }
                return const SizedBox.shrink();
              },
            ),
          ),
        ],
      ),
    );
  }
}
