import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../riverpod/wishlist_notifier.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../utils/constants/app_strings.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/empty_state_widget.dart';
import '../../../widgets/product_list_item.dart';

class WishlistScreen extends ConsumerWidget {
  const WishlistScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final wishlistState = ref.watch(wishlistNotifierProvider);
    final productState = ref.watch(productNotifierProvider);
    final primaryColor = Theme.of(context).primaryColor;
    final size = MediaQuery.of(context).size;

    final featuredProducts = productState.featuredProducts ?? [];
    final wishlistProductIds = wishlistState.value ?? [];
    
    final favoriteProducts = featuredProducts
        .where((p) => wishlistProductIds.contains(p.id))
        .toList();

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: CustomAppBar(
        title: AppStrings.wishlistMenu.tr(),
      ),
      body: Stack(
        children: [
          // Decorative Background Elements
          Positioned(
            top: -size.height * 0.1,
            right: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.4,
              backgroundColor: primaryColor.withValues(alpha: 0.05),
            ),
          ),
          Positioned(
            bottom: -size.height * 0.1,
            left: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.3,
              backgroundColor: primaryColor.withValues(alpha: 0.05),
            ),
          ),
          
          RefreshIndicator(
            onRefresh: () async {
              final auth = ref.read(authNotifierProvider).value;
              if (auth != null) {
                ref.read(wishlistNotifierProvider.notifier).fetchWishlist(auth.uid);
              }
            },
            child: favoriteProducts.isEmpty
                ? EmptyStateWidget(
                    icon: Icons.favorite_border_rounded,
                    title: "Your wishlist is empty!",
                    subtitle: "Tap the heart on any product to save it here for later.",
                    actionText: "Go Shopping",
                    onAction: () => Navigator.pop(context),
                  )
                : ListView.builder(
                    physics: const AlwaysScrollableScrollPhysics(),
                    padding: const EdgeInsets.all(16),
                    itemCount: favoriteProducts.length,
                    itemBuilder: (context, index) {
                      final product = favoriteProducts[index];
                      return ProductListItem(
                        product: product,
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            IconButton(
                              icon: const Icon(Icons.add_shopping_cart_rounded, color: Colors.blue),
                              onPressed: () {
                                // Dummy true for added to cart
                                bool added = true; // ref.read(cartNotifierProvider.notifier).addItem(product);
                                if (added) {
                                  ScaffoldMessenger.of(context).showSnackBar(
                                    SnackBar(content: Text(AppStrings.addedToCart.tr()), duration: const Duration(seconds: 1), behavior: SnackBarBehavior.floating),
                                  );
                                } else {
                                  ScaffoldMessenger.of(context).showSnackBar(
                                    const SnackBar(content: Text("Out of stock!"), backgroundColor: Colors.red, behavior: SnackBarBehavior.floating),
                                  );
                                }
                              },
                            ),
                            IconButton(
                              icon: const Icon(Icons.delete_outline_rounded, color: Colors.red),
                              onPressed: () {
                                final auth = ref.read(authNotifierProvider).value;
                                if (auth != null) {
                                  ref.read(wishlistNotifierProvider.notifier).toggleWishlist(auth.uid, product.id);
                                }
                              },
                            ),
                          ],
                        ),
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }
}
