import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../riverpod/wishlist_notifier.dart';
import '../../../widgets/product_list_item.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/empty_state_widget.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import 'package:smart_shop/widgets/custom_loading.dart';

class WishlistScreen extends ConsumerWidget {
  const WishlistScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final wishlistState = ref.watch(wishlistNotifierProvider);

    return Scaffold(
      appBar: CustomAppBar(title: "My Favorites"),
      body: wishlistState.when(
        data: (products) {
          if (products.isEmpty) {
            return EmptyStateWidget(
              icon: Icons.favorite_border_rounded,
              title: "Wishlist is Empty",
              subtitle: "Save products you like to buy them later!",
              actionText: "Go Shopping",
              onAction: () => ref.read(navigationNotifierProvider.notifier).setIndex(0),
            );
          }
          
          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: products.length,
            itemBuilder: (context, index) {
              final product = products[index];
              return ProductListItem(
                product: product,
                trailing: IconButton(
                  icon: const Icon(Icons.delete_outline_rounded, color: Colors.red),
                  onPressed: () => ref.read(wishlistNotifierProvider.notifier).toggleWishlist(product),
                ),
              );
            },
          );
        },
        loading: () => const Center(child: CustomLoading()),
        error: (e, st) => Center(child: Text("Error: $e")),
      ),
    );
  }
}
