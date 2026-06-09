import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../models/product_model.dart';
import '../view_models/cart_view_model.dart';
import '../view_models/wishlist_view_model.dart';
import '../view_models/auth_view_model.dart';
import '../view_models/settings_view_model.dart';
import '../utils/constants/app_strings.dart';
import '../routes/app_routes.dart';
import 'app_card.dart';

class ProductCard extends StatelessWidget {
  final ProductModel product;
  final double width;

  const ProductCard({
    super.key,
    required this.product,
    this.width = 175,
  });

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    
    return AppCard(
      width: width,
      margin: const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      onTap: () => Navigator.pushNamed(
        context,
        AppRoutes.productDetails,
        arguments: product,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Expanded(
            child: Stack(
              children: [
                Hero(
                  tag: 'product-${product.id}',
                  child: Container(
                    decoration: const BoxDecoration(
                      borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
                    ),
                    child: ClipRRect(
                      borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
                      child: Image.network(
                        product.imageUrl,
                        width: double.infinity,
                        height: double.infinity,
                        fit: BoxFit.cover,
                        errorBuilder: (context, error, stackTrace) => Container(
                          color: Colors.grey[100],
                          child: const Center(
                            child: Icon(Icons.image_not_supported_outlined, color: Colors.grey, size: 40),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                if (product.hasDiscount)
                  Positioned(
                    top: 12,
                    left: 0,
                    child: Container(
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                      decoration: const BoxDecoration(
                        color: Colors.red,
                        borderRadius: BorderRadius.only(topRight: Radius.circular(12), bottomRight: Radius.circular(12)),
                        boxShadow: [BoxShadow(color: Colors.black26, blurRadius: 4, offset: Offset(2, 2))],
                      ),
                      child: Text(
                        product.discountType == 'percentage' 
                            ? "${product.discountValue.toInt()}% OFF" 
                            : "${AppStrings.currency.tr()} ${product.discountValue.toInt()} OFF",
                        style: const TextStyle(color: Colors.white, fontSize: 11, fontWeight: FontWeight.w900),
                      ),
                    ),
                  ),
                Positioned(
                  top: 10,
                  right: 10,
                  child: Consumer<WishlistViewModel>(
                    builder: (context, wishlistVM, child) {
                      final isFav = wishlistVM.isFavorite(product.id);
                      return GestureDetector(
                        onTap: () {
                          final auth = context.read<AuthViewModel>();
                          if (auth.user != null) {
                            wishlistVM.toggleWishlist(auth.user!.uid, product.id);
                          } else {
                             ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Please login first!")));
                          }
                        },
                        child: Container(
                          padding: const EdgeInsets.all(6),
                          decoration: BoxDecoration(
                            color: Colors.white.withValues(alpha: 0.9),
                            shape: BoxShape.circle,
                            boxShadow: const [BoxShadow(color: Colors.black12, blurRadius: 4)],
                          ),
                          child: Icon(
                            isFav ? Icons.favorite : Icons.favorite_border,
                            color: isFav ? Colors.red : Colors.grey,
                            size: 18,
                          ),
                        ),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(12.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  product.name,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                ),
                const SizedBox(height: 6),
                Row(
                  children: [
                    Text.rich(
                      TextSpan(
                        children: [
                          TextSpan(
                            text: "${AppStrings.currency.tr()} ",
                            style: TextStyle(
                              color: settings.primaryColor,
                              fontWeight: FontWeight.bold,
                              fontSize: 14,
                            ),
                          ),
                          TextSpan(
                            text: NumberFormat('#,##,###').format(product.price.toInt()),
                            style: TextStyle(
                              color: settings.primaryColor,
                              fontWeight: FontWeight.w900,
                              fontSize: 18,
                            ),
                          ),
                        ],
                      ),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    if (product.hasDiscount) ...[
                      const SizedBox(width: 4),
                      Expanded(
                        child: Text(
                          "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(product.originalPrice.toInt())}",
                          style: const TextStyle(
                            color: Colors.grey,
                            decoration: TextDecoration.lineThrough,
                            fontSize: 11,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                    ]
                  ],
                ),
                const SizedBox(height: 8),
                Row(
                  children: [
                    Icon(
                      Icons.inventory_2_outlined, 
                      size: 11, 
                      color: product.stock > 0 ? Colors.green : Colors.red
                    ),
                    const SizedBox(width: 4),
                    Expanded(
                      child: Text(
                        product.stock > 0 
                          ? "${AppStrings.stock.tr()}: ${product.stock}"
                          : AppStrings.outOfStock.tr(),
                        style: TextStyle(
                          fontSize: 10, 
                          color: product.stock > 0 ? Colors.green : Colors.red,
                          fontWeight: FontWeight.bold
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 10),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: product.stock > 0 ? () {
                      bool added = context.read<CartViewModel>().addItem(product);
                      if (added) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Row(
                              children: [
                                const Icon(Icons.check_circle, color: Colors.white, size: 20),
                                const SizedBox(width: 8),
                                Expanded(child: Text(AppStrings.addedToCart.tr())),
                              ],
                            ),
                            duration: const Duration(seconds: 1), 
                            behavior: SnackBarBehavior.floating,
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                          ),
                        );
                      }
                    } : null,
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(vertical: 4),
                      backgroundColor: product.stock > 0 ? settings.primaryColor : Colors.grey[400],
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      elevation: 0,
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Icon(Icons.add_shopping_cart, size: 14, color: Colors.white),
                        const SizedBox(width: 4),
                        Text(
                          product.stock > 0 ? AppStrings.addToCart.tr() : AppStrings.outOfStock.tr(),
                          style: const TextStyle(fontSize: 10, color: Colors.white, fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// Add width support to AppCard or update AppCard to accept width
extension on AppCard {
  Widget withWidth(double? width) {
    return SizedBox(width: width, child: this);
  }
}
