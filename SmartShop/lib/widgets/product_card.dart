import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../features/product/models/product_model.dart';
import '../features/cart/riverpod/cart_notifier.dart';
import '../features/wishlist/riverpod/wishlist_notifier.dart';
import '../features/auth/riverpod/auth_notifier.dart';
import '../../core/riverpod/settings_notifier.dart';
import '../utils/constants/app_strings.dart';
import '../routes/app_routes.dart';

class ProductCard extends ConsumerWidget {
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
  Widget build(BuildContext context, WidgetRef ref) {
    final settings = ref.watch(settingsProvider);
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
                          AppStrings.offLabel.tr(args: [
                            product.discountType == 'percentage' 
                                ? "${product.discountValue.toInt()}%" 
                                : "৳${product.discountValue.toInt()}"
                          ]),
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
                    child: Consumer(
                      builder: (context, ref, child) {
                        final wishlistState = ref.watch(wishlistNotifierProvider);
                        final isFav = wishlistState.value?.contains(product.id) ?? false;
                        return GestureDetector(
                          onTap: () {
                            final auth = ref.read(authNotifierProvider).value;
                            if (auth != null) {
                              ref.read(wishlistNotifierProvider.notifier).toggleWishlist(auth.uid, product.id);
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
              flex: 13, // Increased flex to give more space
              child: Padding(
                padding: const EdgeInsets.fromLTRB(12, 6, 12, 10), // Slightly reduced padding
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
                          style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 14, letterSpacing: -0.5),
                        ),
                        const SizedBox(height: 2),
                        Row(
                          children: [
                            Icon(Icons.star_rounded, size: 12, color: Colors.amber[700]),
                            const SizedBox(width: 4),
                            Text(
                              "${product.rating}",
                              style: TextStyle(fontSize: 10, fontWeight: FontWeight.w900, color: Colors.grey[700]),
                            ),
                            const Spacer(),
                            if (product.stock > 0)
                              Container(
                                padding: const EdgeInsets.symmetric(horizontal: 5, vertical: 2),
                                decoration: BoxDecoration(
                                  color: (product.stock <= 5) ? Colors.orange.withValues(alpha: 0.1) : Colors.blue.withValues(alpha: 0.1),
                                  borderRadius: BorderRadius.circular(4),
                                ),
                                child: Text(
                                  "${product.stock} ${AppStrings.pieces.tr()}",
                                  style: TextStyle(
                                    color: (product.stock <= 5) ? Colors.orange : Colors.blue,
                                    fontSize: 8, 
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
                                "৳${product.originalPrice?.toInt() ?? product.price.toInt()}",
                                style: TextStyle(
                                  color: Colors.grey[400],
                                  decoration: TextDecoration.lineThrough,
                                  fontSize: 10,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              const SizedBox(width: 4),
                              Text(
                                product.discountType == 'percentage' 
                                    ? "-${product.discountValue.toInt()}%" 
                                    : "-৳${product.discountValue.toInt()}",
                                style: const TextStyle(color: Colors.red, fontSize: 9, fontWeight: FontWeight.w900),
                              ),
                            ],
                          ),
                        Text(
                          "৳${product.price.toInt()}",
                          style: TextStyle(
                            color: primaryColor,
                            fontWeight: FontWeight.w900,
                            fontSize: 18,
                          ),
                        ),
                      ],
                    ),
                    
                    // ADD TO CART BUTTON
                    SizedBox(
                      width: double.infinity,
                      height: 38, // Slightly reduced height
                      child: ElevatedButton(
                        onPressed: product.stock > 0 ? () {
                          // Dummy true
                          bool added = true; // ref.read(cartNotifierProvider.notifier).addToCart(product, 1);
                          if (added) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(
                                content: Text(AppStrings.addedToCart.tr()),
                                duration: const Duration(seconds: 1),
                                behavior: SnackBarBehavior.floating,
                                margin: const EdgeInsets.all(10),
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                              ),
                            );
                          }
                        } : null,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: product.stock > 0 ? primaryColor : Colors.grey[100],
                          foregroundColor: Colors.white,
                          elevation: 0,
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                          padding: EdgeInsets.zero,
                        ),
                        child: Text(
                          product.stock > 0 ? AppStrings.addToCart.tr().toUpperCase() : AppStrings.outOfStock.tr().toUpperCase(), 
                          style: const TextStyle(fontSize: 10, fontWeight: FontWeight.w900),
                        ),
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
