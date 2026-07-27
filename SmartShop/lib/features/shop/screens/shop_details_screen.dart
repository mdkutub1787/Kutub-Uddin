import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../../models/shop_model.dart';
import '../../category/riverpod/category_notifier.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../widgets/product_card.dart';

class ShopDetailsScreen extends ConsumerStatefulWidget {
  final ShopModel shop;

  const ShopDetailsScreen({super.key, required this.shop});

  @override
  ConsumerState<ShopDetailsScreen> createState() => _ShopDetailsScreenState();
}

class _ShopDetailsScreenState extends ConsumerState<ShopDetailsScreen> {
  String? _selectedCategoryId;

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;
    final shop = widget.shop;

    // Use product list to filter categories available in this shop
    final productState = ref.watch(productNotifierProvider);
    final allShopProducts = productState.featuredProducts.where((p) => p.shopId == shop.id).toList();
    
    // We only want categories that have products in this shop
    final categoryIdsInShop = allShopProducts.map((p) => p.categoryId).toSet();
    final categories = ref.watch(categoryNotifierProvider).value?.where((c) => categoryIdsInShop.contains(c.id)).toList() ?? [];

    final displayProducts = _selectedCategoryId == null 
        ? allShopProducts 
        : allShopProducts.where((p) => p.categoryId == _selectedCategoryId).toList();

    return Scaffold(
      body: CustomScrollView(
        slivers: [
          // Shop Cover Header
          SliverAppBar(
            expandedHeight: 250.0,
            pinned: true,
            flexibleSpace: FlexibleSpaceBar(
              title: Text(
                shop.name,
                style: const TextStyle(fontWeight: FontWeight.w900, shadows: [Shadow(color: Colors.black54, blurRadius: 10)]),
              ),
              background: Hero(
                tag: 'shop_image_${shop.id}',
                child: Container(
                  decoration: BoxDecoration(
                    image: shop.imageUrl != null && shop.imageUrl!.isNotEmpty && shop.imageUrl!.startsWith('http')
                        ? DecorationImage(
                            image: NetworkImage(shop.imageUrl!),
                            fit: BoxFit.cover,
                          )
                        : null,
                    color: Colors.grey[300],
                  ),
                  child: Stack(
                    children: [
                      Container(
                        decoration: BoxDecoration(
                          gradient: LinearGradient(
                            colors: [Colors.black.withValues(alpha: 0.7), Colors.transparent, Colors.black.withValues(alpha: 0.7)],
                            begin: Alignment.topCenter,
                            end: Alignment.bottomCenter,
                          ),
                        ),
                      ),
                      if (shop.imageUrl == null || shop.imageUrl!.isEmpty || !shop.imageUrl!.startsWith('http'))
                        Center(child: Icon(Icons.restaurant_rounded, size: 80, color: Colors.grey[500])),
                    ],
                  ),
                ),
              ),
            ),
          ),

          // Shop Info Details
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      Icon(Icons.star_rounded, color: Colors.orange, size: 20),
                      const SizedBox(width: 4),
                      const Text("4.8 (120+ ratings)", style: TextStyle(fontWeight: FontWeight.bold)),
                      const Spacer(),
                      if (shop.isOnlineOrderEnabled)
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                          decoration: BoxDecoration(color: Colors.green.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(10)),
                          child: const Text("Accepting Orders", style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold, fontSize: 12)),
                        )
                    ],
                  ),
                  const SizedBox(height: 12),
                  Row(
                    children: [
                      Icon(Icons.location_on, color: Colors.grey[600], size: 16),
                      const SizedBox(width: 4),
                      Expanded(child: Text(shop.address, style: TextStyle(color: Colors.grey[600]))),
                    ],
                  ),
                  const SizedBox(height: 8),
                  Row(
                    children: [
                      Icon(Icons.delivery_dining, color: primaryColor, size: 16),
                      const SizedBox(width: 4),
                      Text("Delivery inside Dhaka", style: TextStyle(color: primaryColor, fontWeight: FontWeight.bold)),
                    ],
                  ),
                  const SizedBox(height: 16),
                  const Divider(),
                ],
              ),
            ),
          ),

          // Menu Categories
          if (categories.isNotEmpty)
            SliverToBoxAdapter(
              child: SizedBox(
                height: 40,
                child: ListView.builder(
                  scrollDirection: Axis.horizontal,
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  itemCount: categories.length + 1,
                  itemBuilder: (context, index) {
                    if (index == 0) {
                      return _buildCategoryTab("All", null, primaryColor);
                    }
                    final cat = categories[index - 1];
                    return _buildCategoryTab(cat.name, cat.id, primaryColor);
                  },
                ),
              ),
            ),
          
          if (categories.isNotEmpty)
            const SliverToBoxAdapter(child: SizedBox(height: 16)),

          // Products Grid
          SliverPadding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            sliver: displayProducts.isEmpty
                ? SliverToBoxAdapter(
                    child: Center(
                      child: Padding(
                        padding: const EdgeInsets.all(32.0),
                        child: Column(
                          children: [
                            Icon(Icons.fastfood_outlined, size: 64, color: Colors.grey[300]),
                            const SizedBox(height: 16),
                            Text("No items found in this category", style: TextStyle(color: Colors.grey[600])),
                          ],
                        ),
                      ),
                    ),
                  )
                : SliverGrid(
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      childAspectRatio: 0.68, // Matched from previous ProductGrid
                      crossAxisSpacing: 10,
                      mainAxisSpacing: 10,
                    ),
                    delegate: SliverChildBuilderDelegate(
                      (context, index) {
                        return ProductCard(product: displayProducts[index]);
                      },
                      childCount: displayProducts.length,
                    ),
                  ),
          ),
          
          const SliverToBoxAdapter(child: SizedBox(height: 100)), // Bottom padding
        ],
      ),
    );
  }

  Widget _buildCategoryTab(String title, String? id, Color primaryColor) {
    final isSelected = _selectedCategoryId == id;
    return GestureDetector(
      onTap: () {
        setState(() {
          _selectedCategoryId = id;
        });
      },
      child: Container(
        margin: const EdgeInsets.only(right: 8),
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        decoration: BoxDecoration(
          color: isSelected ? primaryColor : Colors.grey[200],
          borderRadius: BorderRadius.circular(20),
        ),
        child: Center(
          child: Text(
            title,
            style: TextStyle(
              color: isSelected ? Colors.white : Colors.black87,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ),
    );
  }
}
