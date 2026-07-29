import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../features/product/models/product_model.dart';
import '../features/cart/riverpod/cart_notifier.dart';
import '../features/wishlist/riverpod/wishlist_notifier.dart';
import '../features/auth/riverpod/auth_notifier.dart';
import '../../core/riverpod/settings_notifier.dart';
import '../core/app_strings.dart';
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
    final currency = settings.currencySymbol;
    
    return Container(
      width: width,
      margin: const EdgeInsets.symmetric(horizontal: 10, vertical: 12),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(32),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.03),
            blurRadius: 20,
            offset: const Offset(0, 10),
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
        borderRadius: BorderRadius.circular(32),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              flex: 12,
              child: Stack(
                children: [
                  Hero(
                    tag: heroTag ?? product.id,
                    child: Container(
                      width: double.infinity,
                      margin: const EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        color: const Color(0xFFF8F9FA),
                        borderRadius: BorderRadius.circular(24),
                      ),
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(24),
                        child: Image.network(
                          product.imageUrl,
                          fit: BoxFit.contain,
                          errorBuilder: (context, error, stackTrace) => Center(
                            child: Icon(Icons.image_not_supported_outlined, color: Colors.grey[300], size: 40),
                          ),
                        ),
                      ),
                    ),
                  ),
                  
                  if (product.hasDiscount)
                    Positioned(
                      top: 15,
                      left: 15,
                      child: Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: const Color(0xFF2D958E),
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Text(
                          "-${product.discountValue.toInt()}%",
                          style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
                        ),
                      ),
                    ),
                    
                  Positioned(
                    top: 15,
                    right: 15,
                    child: Consumer(
                      builder: (context, ref, child) {
                        final wishlist = ref.watch(wishlistNotifierProvider).value ?? [];
                        final isFav = wishlist.any((p) => p.id == product.id);
                        return GestureDetector(
                          onTap: () {
                            ref.read(wishlistNotifierProvider.notifier).toggleWishlist(product);
                          },
                          child: Icon(
                            isFav ? Icons.favorite_rounded : Icons.favorite_border_rounded,
                            color: isFav ? Colors.red : Colors.black26,
                            size: 22,
                          ),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
            
            Padding(
              padding: const EdgeInsets.fromLTRB(16, 4, 16, 16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    product.name,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: const TextStyle(fontWeight: FontWeight.w800, fontSize: 15, color: Colors.black87),
                  ),
                  const SizedBox(height: 4),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        "$currency${product.price.toInt()}",
                        style: const TextStyle(
                          color: Color(0xFF2D958E),
                          fontWeight: FontWeight.w900,
                          fontSize: 18,
                        ),
                      ),
                      GestureDetector(
                        onTap: () {
                          ref.read(cartNotifierProvider.notifier).addToCart(product);
                        },
                        child: Container(
                          padding: const EdgeInsets.all(6),
                          decoration: BoxDecoration(
                            color: Colors.black87,
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: const Icon(Icons.add_rounded, color: Colors.white, size: 18),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
