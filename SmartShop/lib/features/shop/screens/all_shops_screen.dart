import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../riverpod/shop_notifier.dart';
import '../../../widgets/shop_card.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../core/riverpod/settings_notifier.dart';

class AllShopsScreen extends ConsumerWidget {
  const AllShopsScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final shopState = ref.watch(shopNotifierProvider);
    final settings = ref.watch(settingsProvider);

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: const CustomAppBar(title: "All Partner Shops"),
      body: shopState.when(
        data: (shops) {
          if (shops.isEmpty) {
            return const Center(child: Text("No shops found at the moment."));
          }

          return ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 20),
            itemCount: shops.length,
            itemBuilder: (context, index) {
              return ShopCard(
                shop: shops[index],
                width: double.infinity,
              );
            },
          );
        },
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (e, st) => Center(child: Text("Error: $e")),
      ),
    );
  }
}
