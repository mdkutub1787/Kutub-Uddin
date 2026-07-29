import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../models/product_model.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../../wishlist/riverpod/wishlist_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../routes/app_routes.dart';
import 'package:share_plus/share_plus.dart';
import 'package:intl/intl.dart';
import '../riverpod/review_provider.dart';

class ProductDetailsScreen extends ConsumerStatefulWidget {
  final ProductModel product;
  final String? heroTag;

  const ProductDetailsScreen({super.key, required this.product, this.heroTag});

  @override
  ConsumerState<ProductDetailsScreen> createState() => _ProductDetailsScreenState();
}

class _ProductDetailsScreenState extends ConsumerState<ProductDetailsScreen> {
  int _currentPage = 0;
  String _selectedSize = 'M';
  Color _selectedColor = Colors.black;
  final PageController _pageController = PageController();

  final List<String> _sizes = ['S', 'M', 'L', 'XL', 'XXL'];
  final List<Color> _colors = [Colors.black, Colors.blue, Colors.red, Colors.teal, Colors.grey];

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    final currency = settings.currencySymbol;
    final wishlist = ref.watch(wishlistNotifierProvider).value ?? [];
    bool isWishlisted = wishlist.any((item) => item.id == widget.product.id);

    final List<String> productImages = [
      widget.product.imageUrl,
      "https://picsum.photos/id/20/800/800",
      "https://picsum.photos/id/1/800/800",
      "https://picsum.photos/id/26/800/800",
    ];

    return Scaffold(
      backgroundColor: Colors.white,
      body: Stack(
        children: [
          CustomScrollView(
            slivers: [
              SliverAppBar(
                expandedHeight: 420,
                pinned: true,
                elevation: 0,
                backgroundColor: Colors.white,
                leading: IconButton(
                  icon: const Icon(Icons.arrow_back_ios_new_rounded, color: Colors.black),
                  onPressed: () => Navigator.pop(context),
                ),
                actions: [
                  IconButton(
                    icon: Icon(isWishlisted ? Icons.favorite_rounded : Icons.favorite_border_rounded, 
                    color: isWishlisted ? Colors.red : Colors.black),
                    onPressed: () => ref.read(wishlistNotifierProvider.notifier).toggleWishlist(widget.product),
                  ),
                ],
                flexibleSpace: FlexibleSpaceBar(
                  background: Stack(
                    children: [
                      PageView.builder(
                        controller: _pageController,
                        itemCount: productImages.length,
                        onPageChanged: (index) => setState(() => _currentPage = index),
                        itemBuilder: (context, index) {
                          return Hero(
                            tag: index == 0 ? (widget.heroTag ?? widget.product.id) : "img-$index",
                            child: Image.network(productImages[index], fit: BoxFit.cover),
                          );
                        },
                      ),
                      Positioned(
                        bottom: 30,
                        left: 0,
                        right: 0,
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: List.generate(
                            productImages.length,
                            (index) => AnimatedContainer(
                              duration: const Duration(milliseconds: 300),
                              margin: const EdgeInsets.symmetric(horizontal: 4),
                              height: 8,
                              width: _currentPage == index ? 24 : 8,
                              decoration: BoxDecoration(
                                color: _currentPage == index ? settings.primaryColor : Colors.white.withValues(alpha: 0.5),
                                borderRadius: BorderRadius.circular(4),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SliverToBoxAdapter(
                child: Container(
                  padding: const EdgeInsets.all(24),
                  decoration: const BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.vertical(top: Radius.circular(30)),
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(
                            widget.product.name,
                            style: const TextStyle(fontSize: 26, fontWeight: FontWeight.w900, letterSpacing: -0.5),
                          ),
                          Container(
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(color: Colors.amber.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(10)),
                            child: Row(children: [const Icon(Icons.star_rounded, color: Colors.amber, size: 18), const SizedBox(width: 4), Text(widget.product.rating.toString(), style: const TextStyle(fontWeight: FontWeight.bold))]),
                          ),
                        ],
                      ),
                      const SizedBox(height: 12),
                      Text(
                        "$currency${NumberFormat('#,##,###').format(widget.product.price.toInt())}",
                        style: TextStyle(fontSize: 24, fontWeight: FontWeight.w900, color: settings.primaryColor),
                      ),
                      
                      const SizedBox(height: 24),
                      const Text("Select Color", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                      const SizedBox(height: 12),
                      Row(
                        children: _colors.map((color) => GestureDetector(
                          onTap: () => setState(() => _selectedColor = color),
                          child: Container(
                            margin: const EdgeInsets.only(right: 12),
                            padding: const EdgeInsets.all(3),
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              border: Border.all(color: _selectedColor == color ? settings.primaryColor : Colors.transparent, width: 2),
                            ),
                            child: CircleAvatar(radius: 12, backgroundColor: color),
                          ),
                        )).toList(),
                      ),

                      const SizedBox(height: 24),
                      const Text("Select Size", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                      const SizedBox(height: 12),
                      Row(
                        children: _sizes.map((size) => GestureDetector(
                          onTap: () => setState(() => _selectedSize = size),
                          child: Container(
                            margin: const EdgeInsets.only(right: 12),
                            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                            decoration: BoxDecoration(
                              color: _selectedSize == size ? settings.primaryColor : Colors.grey[100],
                              borderRadius: BorderRadius.circular(10),
                            ),
                            child: Text(size, style: TextStyle(color: _selectedSize == size ? Colors.white : Colors.black, fontWeight: FontWeight.bold)),
                          ),
                        )).toList(),
                      ),

                      const SizedBox(height: 24),
                      const Text("About Product", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                      const SizedBox(height: 8),
                      Text(
                        widget.product.description.isEmpty ? "Premium quality product from Smart Shop. Best in class design and durability." : widget.product.description,
                        style: TextStyle(color: Colors.grey[600], height: 1.6, fontSize: 15),
                      ),
                      const SizedBox(height: 32),
                      _buildReviewHeader(),
                      const SizedBox(height: 16),
                      Consumer(
                        builder: (context, ref, _) {
                          final reviewsState = ref.watch(productReviewsProvider(widget.product.id));
                          return reviewsState.when(
                            data: (reviews) {
                              if (reviews.isEmpty) {
                                return Padding(
                                  padding: const EdgeInsets.only(bottom: 24.0),
                                  child: Text("No reviews yet. Be the first to review!", style: TextStyle(color: Colors.grey[600])),
                                );
                              }
                              return Column(
                                children: reviews.map((review) => _reviewCard(
                                  review.userName, 
                                  review.comment, 
                                  review.rating
                                )).toList(),
                              );
                            },
                            loading: () => const Center(child: CircularProgressIndicator()),
                            error: (e, _) => Text("Failed to load reviews", style: TextStyle(color: Colors.red[300])),
                          );
                        },
                      ),
                      const SizedBox(height: 120),
                    ],
                  ),
                ),
              ),
            ],
          ),
          Positioned(
            bottom: 0, left: 0, right: 0,
            child: _buildBottomBar(settings, currency),
          ),
        ],
      ),
    );
  }

  Widget _buildReviewHeader() {
    return const Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text("Reviews", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
        Text("See All", style: TextStyle(color: Colors.blue, fontWeight: FontWeight.bold, fontSize: 13)),
      ],
    );
  }

  Widget _reviewCard(String name, String comment, double rating) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(color: Colors.grey[50], borderRadius: BorderRadius.circular(20), border: Border.all(color: Colors.grey[100]!)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(name, style: const TextStyle(fontWeight: FontWeight.bold)),
              Row(children: List.generate(5, (i) => Icon(Icons.star_rounded, color: i < rating ? Colors.amber : Colors.grey[300], size: 14))),
            ],
          ),
          const SizedBox(height: 8),
          Text(comment, style: TextStyle(color: Colors.grey[600], fontSize: 13)),
        ],
      ),
    );
  }

  Widget _buildBottomBar(dynamic settings, String currency) {
    return Container(
      padding: const EdgeInsets.fromLTRB(24, 16, 24, 32),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: const BorderRadius.vertical(top: Radius.circular(30)),
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 20, offset: const Offset(0, -5))],
      ),
      child: Row(
        children: [
          GestureDetector(
            onTap: () {
              Share.share(
                "Check out this amazing product: ${widget.product.name}\nPrice: ${currency}${widget.product.price}\nDownload Smart Shop App now!",
                subject: widget.product.name,
              );
            },
            child: Container(
              height: 55, width: 55,
              decoration: BoxDecoration(color: Colors.grey[100], borderRadius: BorderRadius.circular(15)),
              child: const Icon(Icons.share_outlined, color: Colors.black),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: SizedBox(
              height: 55,
              child: OutlinedButton(
                onPressed: () {
                  ref.read(cartNotifierProvider.notifier).addToCart(widget.product);
                  ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Added to Cart"), duration: Duration(milliseconds: 500)));
                },
                style: OutlinedButton.styleFrom(
                  side: BorderSide(color: settings.primaryColor),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                ),
                child: Text("ADD TO CART", style: TextStyle(fontWeight: FontWeight.w900, fontSize: 14, color: settings.primaryColor)),
              ),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: SizedBox(
              height: 55,
              child: ElevatedButton(
                onPressed: () {
                  ref.read(cartNotifierProvider.notifier).addToCart(widget.product);
                  Navigator.pushNamed(context, AppRoutes.cart);
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: settings.primaryColor,
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                  elevation: 0,
                ),
                child: const Text("BUY NOW", style: TextStyle(fontWeight: FontWeight.w900, fontSize: 14)),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
