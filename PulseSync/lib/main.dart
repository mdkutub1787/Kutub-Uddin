import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:path_provider/path_provider.dart';
import 'core/di/injection.dart';
import 'features/inventory/presentation/pages/inventory_page.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Initialize dependency injection
  configureDependencies();

  // Setup HydratedBloc for offline-first state persistence
  HydratedBloc.storage = await HydratedStorage.build(
    storageDirectory: kIsWeb 
        ? HydratedStorageDirectory.web
        : HydratedStorageDirectory((await getApplicationDocumentsDirectory()).path),
  );

  runApp(const PulseSyncApp());
}

class PulseSyncApp extends StatelessWidget {
  const PulseSyncApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'PulseSync Enterprise',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.teal,
          brightness: Brightness.dark,
        ),
        useMaterial3: true,
      ),
      home: const InventoryPage(),
    );
  }
}
