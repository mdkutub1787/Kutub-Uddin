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
  final String? heroTag;

  const ProductCard({
    super.key,
    required this.product,
    this.width = 175,
    this.heroTag,
  });

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final primaryColor = settings.primaryColor;
    
    return Container(
      width: width,
      margin: const EdgeInsets.symmetric(horizontal: 10, vertical: 12),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(28),
        border: Border.all(color: Colors.grey.withValues(alpha: 0.1)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.04),
            blurRadius: 15,
            offset: const Offset(0, 8),
          )
        ],
      ),
      child: InkWell(
        onTap: () => Navigator.pushNamed(
          context,
          AppRoutes.productDetails,
          arguments: {
            'product': product,
            'heroTag': heroTag ?? product.id,
          },
        ),
        borderRadius: BorderRadius.circular(28),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // IMAGE SECTION
            Expanded(
              flex: 10,
              child: Stack(
                children: [
                  Hero(
                    tag: heroTag ?? product.id,
                    child: Container(
                      width: double.infinity,
                      decoration: BoxDecoration(
                        color: Colors.grey[50],
                        borderRadius: const BorderRadius.vertical(top: Radius.circular(28)),
                      ),
                      child: ClipRRect(
                        borderRadius: const BorderRadius.vertical(top: Radius.circular(28)),
                        child: Image.network(
                          product.imageUrl,
                          fit: BoxFit.cover,
                          errorBuilder: (context, error, stackTrace) => Center(
                            child: Icon(Icons.image_not_supported_outlined, color: Colors.grey[300], size: 50),
                          ),
                        ),
                      ),
                    ),
                  ),
                  
                  // OFFER TAG
                  if (product.hasDiscount)
                    Positioned(
                      top: 15,
                      left: 15,
                      child: Container(
                        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                        decoration: BoxDecoration(
                          gradient: LinearGradient(
                            colors: [Colors.red[700]!, Colors.red[400]!],
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                          ),
                          borderRadius: BorderRadius.circular(12),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.red.withValues(alpha: 0.3),
                              blurRadius: 8,
                              offset: const Offset(0, 4),
                            )
                          ],
                        ),
                        child: Text(
                          product.discountType == 'percentage' 
                              ? "${product.discountValue.toInt()}% OFF" 
                              : "৳${product.discountValue.toInt()} OFF",
                          style: const TextStyle(
                            color: Colors.white, 
                            fontSize: 10, 
                            fontWeight: FontWeight.w900,
                            letterSpacing: 0.5,
                          ),
                        ),
                      ),
                    ),
                    
                  // WISHLIST BUTTON
                  Positioned(
                    top: 12,
                    right: 12,
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
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                              color: Colors.white.withValues(alpha: 0.9),
                              shape: BoxShape.circle,
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withValues(alpha: 0.1),
                                  blurRadius: 10,
                                )
                              ],
                            ),
                            child: Icon(
                              isFav ? Icons.favorite_rounded : Icons.favorite_border_rounded,
                              color: isFav ? Colors.red : Colors.grey[400],
                              size: 20,
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
            
            // DETAILS SECTION
            Expanded(
              flex: 12,
              child: Padding(
                padding: const EdgeInsets.fromLTRB(16, 8, 16, 12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          product.name,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 15, letterSpacing: -0.5),
                        ),
                        const SizedBox(height: 2),
                        Row(
                          children: [
                            Icon(Icons.star_rounded, size: 14, color: Colors.amber[700]),
                            const SizedBox(width: 4),
                            Text(
                              "${product.rating}",
                              style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: Colors.grey[700]),
                            ),
                            const Spacer(),
                            if (product.stock <= 5 && product.stock > 0)
                              Container(
                                padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                                decoration: BoxDecoration(
                                  color: Colors.orange.withValues(alpha: 0.1),
                                  borderRadius: BorderRadius.circular(6),
                                ),
                                child: Text(
                                  "Only ${product.stock} left",
                                  style: const TextStyle(
                                    color: Colors.orange, 
                                    fontSize: 9, 
                                    fontWeight: FontWeight.w900
                                  ),
                                ),
                              ),
                          ],
                        ),
                      ],
                    ),
                    
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        if (product.hasDiscount)
                          Row(
                            children: [
                              Text(
                                "৳${NumberFormat('#,##,###').format(product.originalPrice.toInt())}",
                                style: TextStyle(
                                  color: Colors.grey[400],
                                  decoration: TextDecoration.lineThrough,
                                  fontSize: 12,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              const SizedBox(width: 6),
                              Container(
                                padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                                decoration: BoxDecoration(
                                  color: Colors.red.withValues(alpha: 0.1),
                                  borderRadius: BorderRadius.circular(6),
                                ),
                                child: Text(
                                  product.discountType == 'percentage' 
                                    ? "-${product.discountValue.toInt()}%" 
                                    : "-৳${product.discountValue.toInt()}",
                                  style: const TextStyle(color: Colors.red, fontSize: 10, fontWeight: FontWeight.w900),
                                ),
                              ),
                            ],
                          ),
                        Text(
                          "৳${NumberFormat('#,##,###').format(product.price.toInt())}",
                          style: TextStyle(
                            color: primaryColor,
                            fontWeight: FontWeight.w900,
                            fontSize: 20,
                          ),
                        ),
                      ],
                    ),
                    
                    // ADD TO CART BUTTON
                    SizedBox(
                      width: double.infinity,
                      height: 42,
                      child: ElevatedButton(
                        onPressed: product.stock > 0 ? () {
                          bool added = context.read<CartViewModel>().addItem(product);
                          if (added) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(
                                content: Row(
                                  children: [
                                    const Icon(Icons.check_circle_rounded, color: Colors.white, size: 20),
                                    const SizedBox(width: 10),
                                    Expanded(child: Text(AppStrings.addedToCart.tr(), style: const TextStyle(fontWeight: FontWeight.bold))),
                                  ],
                                ),
                                duration: const Duration(seconds: 1), 
                                behavior: SnackBarBehavior.floating,
                                backgroundColor: primaryColor,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                                margin: const EdgeInsets.all(20),
                              ),
                            );
                          }
                        } : null,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: product.stock > 0 ? primaryColor : Colors.grey[100],
                          foregroundColor: Colors.white,
                          elevation: 0,
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                          padding: EdgeInsets.zero,
                        ),
                        child: product.stock > 0 
                          ? const Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Icon(Icons.add_shopping_cart_rounded, size: 16),
                                SizedBox(width: 8),
                                Text("ADD TO CART", style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
                              ],
                            )
                          : Text("OUT OF STOCK", style: TextStyle(fontSize: 10, color: Colors.grey[400], fontWeight: FontWeight.w900)),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
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
