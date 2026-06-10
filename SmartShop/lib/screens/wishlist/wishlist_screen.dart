import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/view_models/wishlist_view_model.dart';
import 'package:smart_shop/view_models/product_view_model.dart';
import 'package:smart_shop/view_models/cart_view_model.dart';
import 'package:smart_shop/view_models/auth_view_model.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:smart_shop/widgets/custom_app_bar.dart';
import 'package:smart_shop/widgets/empty_state_widget.dart';
import 'package:smart_shop/widgets/product_list_item.dart';

class WishlistScreen extends StatelessWidget {
  const WishlistScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final wishlistVM = context.watch<WishlistViewModel>();
    final productVM = context.watch<ProductViewModel>();
    final cartVM = context.read<CartViewModel>();
    final primaryColor = Theme.of(context).primaryColor;
    final size = MediaQuery.of(context).size;

    final favoriteProducts = productVM.featuredProducts
        .where((p) => wishlistVM.wishlistProductIds.contains(p.id))
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
              final auth = context.read<AuthViewModel>();
              if (auth.user != null) {
                context.read<WishlistViewModel>().init(auth.user!.uid);
              }
              await context.read<ProductViewModel>().fetchFeaturedProducts();
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
                                bool added = cartVM.addItem(product);
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
                                final auth = context.read<AuthViewModel>();
                                if (auth.user != null) {
                                  wishlistVM.toggleWishlist(auth.user!.uid, product.id);
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
