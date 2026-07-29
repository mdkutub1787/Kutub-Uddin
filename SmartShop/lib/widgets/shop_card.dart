import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/shop_model.dart';
import '../routes/app_routes.dart';
import '../core/riverpod/settings_notifier.dart';

class ShopCard extends ConsumerWidget {
  final ShopModel shop;
  final double width;

  const ShopCard({
    super.key,
    required this.shop,
    this.width = double.infinity,
  });

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final primaryColor = ref.watch(settingsProvider).primaryColor;
    bool hasImage = shop.imageUrl != null && shop.imageUrl!.isNotEmpty && shop.imageUrl!.startsWith('http');

    return GestureDetector(
      onTap: () => Navigator.pushNamed(context, AppRoutes.shopDetails, arguments: shop),
      child: Container(
        width: width,
        margin: const EdgeInsets.only(bottom: 12),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(24),
          boxShadow: [
            BoxShadow(
              color: primaryColor.withValues(alpha: 0.04),
              blurRadius: 16,
              offset: const Offset(0, 8),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Shop Image
            Hero(
              tag: 'shop_image_${shop.id}',
              child: Container(
                height: 100,
                width: double.infinity,
                decoration: BoxDecoration(
                  color: Colors.grey[100],
                  borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(24),
                    topRight: Radius.circular(24),
                  ),
                  image: hasImage
                      ? DecorationImage(
                          image: NetworkImage(shop.imageUrl!),
                          fit: BoxFit.cover,
                        )
                      : null,
                ),
                child: !hasImage
                    ? Center(
                        child: Icon(
                          Icons.storefront_rounded,
                          size: 40,
                          color: primaryColor.withValues(alpha: 0.3),
                        ),
                      )
                    : null,
              ),
            ),
            
            // Shop Details
            Padding(
              padding: const EdgeInsets.all(12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Expanded(
                        child: Text(
                          shop.name,
                          style: const TextStyle(
                            fontSize: 15,
                            fontWeight: FontWeight.w900,
                            letterSpacing: -0.3,
                            color: Colors.black87,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 3),
                        decoration: BoxDecoration(
                          color: shop.isOnlineOrderEnabled ? Colors.green.withValues(alpha: 0.1) : Colors.red.withValues(alpha: 0.1),
                          borderRadius: BorderRadius.circular(6),
                        ),
                        child: Text(
                          shop.isOnlineOrderEnabled ? "OPEN" : "CLOSED",
                          style: TextStyle(
                            color: shop.isOnlineOrderEnabled ? Colors.green : Colors.red,
                            fontSize: 9,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 6),
                  Row(
                    children: [
                      Icon(Icons.location_on_rounded, size: 12, color: Colors.grey[400]),
                      const SizedBox(width: 4),
                      Expanded(
                        child: Text(
                          shop.address.isNotEmpty ? shop.address : "Location unavailable",
                          style: TextStyle(
                            fontSize: 11,
                            color: Colors.grey[500],
                            fontWeight: FontWeight.w500,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Row(
                        children: [
                          Icon(Icons.delivery_dining_rounded, size: 14, color: primaryColor),
                          const SizedBox(width: 4),
                          Text(
                            "Delivery",
                            style: TextStyle(
                              fontSize: 11,
                              color: primaryColor,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ],
                      ),
                      Row(
                        children: [
                          const Icon(Icons.star_rounded, size: 14, color: Colors.orange),
                          const SizedBox(width: 4),
                          const Text(
                            "4.8",
                            style: TextStyle(
                              fontSize: 11,
                              fontWeight: FontWeight.w800,
                              color: Colors.black87,
                            ),
                          ),
                        ],
                      )
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
