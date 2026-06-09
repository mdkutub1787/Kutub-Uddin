import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/view_models/wishlist_view_model.dart';
import 'package:smart_shop/view_models/product_view_model.dart';
import 'package:smart_shop/view_models/cart_view_model.dart';
import 'package:smart_shop/view_models/auth_view_model.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:smart_shop/routes/app_routes.dart';
import 'package:smart_shop/widgets/custom_app_bar.dart';
import 'package:smart_shop/widgets/product_list_item.dart';
import 'package:smart_shop/widgets/app_card.dart';

class WishlistScreen extends StatelessWidget {
  const WishlistScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final wishlistVM = context.watch<WishlistViewModel>();
    final productVM = context.watch<ProductViewModel>();
    final cartVM = context.read<CartViewModel>();

    final favoriteProducts = productVM.featuredProducts
        .where((p) => wishlistVM.wishlistProductIds.contains(p.id))
        .toList();

    return Scaffold(
      appBar: CustomAppBar(
        title: AppStrings.wishlistMenu.tr(),
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          final auth = context.read<AuthViewModel>();
          if (auth.user != null) {
            context.read<WishlistViewModel>().init(auth.user!.uid);
          }
          await context.read<ProductViewModel>().fetchFeaturedProducts();
        },
        child: favoriteProducts.isEmpty
            ? Center(
                child: ListView(
                  physics: const AlwaysScrollableScrollPhysics(),
                  children: const [
                    SizedBox(height: 200),
                    Center(child: Text("Your wishlist is empty!")),
                  ],
                ),
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
                          icon: const Icon(Icons.add_shopping_cart, color: Colors.blue),
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
                          icon: const Icon(Icons.delete_outline, color: Colors.red),
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
    );
  }
}
