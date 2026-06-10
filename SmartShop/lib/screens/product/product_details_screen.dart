import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/models/product_model.dart';
import 'package:smart_shop/view_models/category_view_model.dart';
import 'package:smart_shop/view_models/cart_view_model.dart';
import 'package:smart_shop/view_models/wishlist_view_model.dart';
import 'package:smart_shop/view_models/auth_view_model.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:intl/intl.dart';

class ProductDetailsScreen extends StatelessWidget {
  final ProductModel product;
  final String? heroTag;

  const ProductDetailsScreen({super.key, required this.product, this.heroTag});

  @override
  Widget build(BuildContext context) {
    final primaryColor = Theme.of(context).primaryColor;

    return Scaffold(
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: 420,
            pinned: true,
            stretch: true,
            leading: Padding(
              padding: const EdgeInsets.all(8.0),
              child: CircleAvatar(
                backgroundColor: Colors.white.withValues(alpha: 0.9),
                child: IconButton(icon: const Icon(Icons.arrow_back_ios_new_rounded, color: Colors.black, size: 20), onPressed: () => Navigator.pop(context)),
              ),
            ),
            actions: [
              Consumer<WishlistViewModel>(
                builder: (context, wishlistVM, child) {
                  final isFav = wishlistVM.isFavorite(product.id);
                  return Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: CircleAvatar(
                      backgroundColor: Colors.white.withValues(alpha: 0.9),
                      child: IconButton(
                        icon: Icon(isFav ? Icons.favorite : Icons.favorite_border, color: isFav ? Colors.red : Colors.black),
                        onPressed: () {
                          final auth = context.read<AuthViewModel>();
                          if (auth.user != null) wishlistVM.toggleWishlist(auth.user!.uid, product.id);
                        },
                      ),
                    ),
                  );
                },
              ),
            ],
            flexibleSpace: FlexibleSpaceBar(
              background: Hero(
                tag: heroTag ?? product.id,
                child: Stack(
                  fit: StackFit.expand,
                  children: [
                    Image.network(product.imageUrl, fit: BoxFit.cover),
                    Container(
                      decoration: BoxDecoration(
                        gradient: LinearGradient(
                          begin: Alignment.topCenter,
                          end: Alignment.bottomCenter,
                          colors: [Colors.black.withValues(alpha: 0.3), Colors.transparent, Colors.black.withValues(alpha: 0.4)],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
          SliverToBoxAdapter(
            child: Container(
              decoration: BoxDecoration(
                color: Theme.of(context).scaffoldBackgroundColor,
                borderRadius: const BorderRadius.vertical(top: Radius.circular(40)),
              ),
              child: Padding(
                padding: const EdgeInsets.all(28.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                          decoration: BoxDecoration(color: primaryColor.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(10)),
                          child: Consumer<CategoryViewModel>(
                            builder: (context, catVM, child) {
                              final catName = catVM.categories.any((c) => c.id == product.categoryId) ? catVM.categories.firstWhere((c) => c.id == product.categoryId).name : 'General';
                              return Text(catName, style: TextStyle(color: primaryColor, fontWeight: FontWeight.bold, fontSize: 12));
                            },
                          ),
                        ),
                        Row(
                          children: [
                            const Icon(Icons.star_rounded, color: Colors.amber, size: 22),
                            const SizedBox(width: 4),
                            Text("${product.rating}", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                            Text(" (120+ Reviews)", style: TextStyle(color: Colors.grey[600], fontSize: 12)),
                          ],
                        ),
                      ],
                    ),
                    const SizedBox(height: 20),
                    Text(product.name, style: const TextStyle(fontSize: 30, fontWeight: FontWeight.w900, letterSpacing: -0.5)),
                    const SizedBox(height: 15),
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: [
                        Text("৳${NumberFormat('#,##,###').format(product.price.toInt())}", style: TextStyle(fontSize: 32, fontWeight: FontWeight.w900, color: primaryColor)),
                        const SizedBox(width: 12),
                        if (product.hasDiscount) ...[
                          Text("৳${NumberFormat('#,##,###').format(product.originalPrice.toInt())}", style: const TextStyle(fontSize: 18, color: Colors.grey, decoration: TextDecoration.lineThrough)),
                          const SizedBox(width: 10),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                            decoration: BoxDecoration(color: Colors.red, borderRadius: BorderRadius.circular(8)),
                            child: Text(
                              AppStrings.offLabel.tr(args: [
                                product.discountType == 'percentage' ? "${product.discountValue.toInt()}%" : "৳${product.discountValue.toInt()}"
                              ]),
                              style: const TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold),
                            ),
                          ),
                        ],
                      ],
                    ),
                    const SizedBox(height: 15),
                    Row(
                      children: [
                        Icon(Icons.inventory_2_outlined, size: 18, color: Colors.grey[600]),
                        const SizedBox(width: 8),
                        Text(
                          "${AppStrings.stock.tr()}: ${product.stock} ${AppStrings.pieces.tr()}",
                          style: TextStyle(
                            color: product.stock <= 5 ? Colors.orange[800] : Colors.grey[800],
                            fontWeight: FontWeight.bold,
                            fontSize: 14,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 30),
                    Text(AppStrings.description.tr(), style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                    const SizedBox(height: 12),
                    Text(product.description, style: TextStyle(fontSize: 16, color: Colors.grey[700], height: 1.6)),
                    const SizedBox(height: 120),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
      bottomSheet: _buildBottomBar(context),
    );
  }

  Widget _buildBottomBar(BuildContext context) {
    final primaryColor = Theme.of(context).primaryColor;
    return Container(
      padding: const EdgeInsets.fromLTRB(25, 15, 25, 35),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: const BorderRadius.vertical(top: Radius.circular(40)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.08), 
            blurRadius: 30, 
            offset: const Offset(0, -10)
          )
        ],
      ),
      child: SafeArea(
        child: Row(
          children: [
            Container(
              height: 60,
              width: 60,
              decoration: BoxDecoration(
                color: Colors.grey[100], 
                borderRadius: BorderRadius.circular(20),
                border: Border.all(color: Colors.grey[200]!)
              ),
              child: IconButton(
                icon: Icon(Icons.share_rounded, color: Colors.grey[700]), 
                onPressed: () {}
              ),
            ),
            const SizedBox(width: 20),
            Expanded(
              child: SizedBox(
                height: 60,
                child: ElevatedButton(
                  onPressed: product.stock > 0 ? () {
                    bool added = context.read<CartViewModel>().addItem(product);
                    if (added) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Row(
                            children: [
                              const Icon(Icons.check_circle_rounded, color: Colors.white),
                              const SizedBox(width: 10),
                              const Text("Added to your cart!", style: TextStyle(fontWeight: FontWeight.bold)),
                            ],
                          ),
                          behavior: SnackBarBehavior.floating,
                          backgroundColor: primaryColor,
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                          margin: const EdgeInsets.all(20),
                        )
                      );
                    }
                  } : null,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: primaryColor,
                    foregroundColor: Colors.white,
                    elevation: 10,
                    shadowColor: primaryColor.withValues(alpha: 0.4),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(22)),
                  ),
                  child: Text(
                    product.stock > 0 ? AppStrings.addToCart.tr().toUpperCase() : AppStrings.outOfStock.tr().toUpperCase(),
                    style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900, letterSpacing: 1.2)
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
