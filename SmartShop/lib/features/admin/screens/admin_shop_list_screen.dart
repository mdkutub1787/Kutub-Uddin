import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../shop/riverpod/shop_notifier.dart';
import '../../../core/riverpod/admin_shop_filter_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import 'package:smart_shop/widgets/custom_loading.dart';

class AdminShopListScreen extends ConsumerWidget {
  const AdminShopListScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final settings = ref.watch(settingsProvider);
    final shopsState = ref.watch(shopNotifierProvider);

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: const Text("Manage Shops", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20)),
        centerTitle: true,
        backgroundColor: Colors.white,
        elevation: 0,
        surfaceTintColor: Colors.white,
      ),
      body: shopsState.when(
        data: (shops) {
          if (shops.isEmpty) {
            return const Center(
              child: Text("No shops found", style: TextStyle(color: Colors.grey, fontSize: 16)),
            );
          }
          return RefreshIndicator(
            onRefresh: () async => ref.read(shopNotifierProvider.notifier).loadShops(),
            child: ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: shops.length,
              itemBuilder: (context, index) {
                final shop = shops[index];
                return Card(
                  elevation: 2,
                  margin: const EdgeInsets.only(bottom: 16),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                  child: ListTile(
                    contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                    leading: CircleAvatar(
                      backgroundColor: settings.primaryColor.withValues(alpha: 0.1),
                      radius: 25,
                      backgroundImage: (shop.imageUrl != null && shop.imageUrl!.isNotEmpty) ? NetworkImage(shop.imageUrl!) : null,
                      child: (shop.imageUrl == null || shop.imageUrl!.isEmpty) ? Icon(Icons.storefront_rounded, color: settings.primaryColor) : null,
                    ),
                    title: Text(shop.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                    subtitle: Padding(
                      padding: const EdgeInsets.only(top: 4.0),
                      child: Text(
                        shop.address, 
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: const TextStyle(color: Colors.grey, fontSize: 13),
                      ),
                    ),
                    trailing: const Icon(Icons.arrow_forward_ios_rounded, size: 16, color: Colors.grey),
                    onTap: () {
                      ref.read(adminShopFilterProvider.notifier).state = shop.id;
                      ref.read(adminShopFilterNameProvider.notifier).state = shop.name;
                      Navigator.pop(context);
                    },
                  ),
                );
              },
            ),
          );
        },
        loading: () => const Center(child: CustomLoading()),
        error: (err, stack) => Center(child: Text("Failed to load shops", style: TextStyle(color: Colors.red[300]))),
      ),
    );
  }
}
