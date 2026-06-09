import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../models/product_model.dart';
import '../view_models/settings_view_model.dart';
import '../utils/constants/app_strings.dart';
import '../routes/app_routes.dart';
import 'package:intl/intl.dart';
import 'app_card.dart';

class ProductListItem extends StatelessWidget {
  final ProductModel product;
  final Widget? trailing;
  final VoidCallback? onTap;
  final String? heroTag;

  const ProductListItem({
    super.key,
    required this.product,
    this.trailing,
    this.onTap,
    this.heroTag,
  });

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();

    return AppCard(
      margin: const EdgeInsets.only(bottom: 12),
      onTap: onTap ?? () => Navigator.pushNamed(
        context,
        AppRoutes.productDetails,
        arguments: {
          'product': product,
          'heroTag': heroTag ?? product.id,
        },
      ),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          children: [
            Hero(
              tag: heroTag ?? product.id,
              child: ClipRRect(
                borderRadius: BorderRadius.circular(15),
                child: Image.network(
                  product.imageUrl,
                  width: 80,
                  height: 80,
                  fit: BoxFit.cover,
                  errorBuilder: (_, __, ___) => Container(
                    width: 80,
                    height: 80,
                    color: Colors.grey[100],
                    child: const Icon(Icons.image_not_supported, color: Colors.grey),
                  ),
                ),
              ),
            ),
            const SizedBox(width: 15),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    product.name,
                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 4),
                  if (product.hasDiscount)
                    Row(
                      children: [
                        Text(
                          "৳${NumberFormat('#,##,###').format(product.originalPrice.toInt())}",
                          style: const TextStyle(
                            color: Colors.grey,
                            decoration: TextDecoration.lineThrough,
                            fontSize: 13,
                          ),
                        ),
                        const SizedBox(width: 8),
                        Text(
                          product.discountType == 'percentage' 
                            ? "${product.discountValue.toInt()}% OFF" 
                            : "৳${product.discountValue.toInt()} OFF",
                          style: const TextStyle(color: Colors.red, fontSize: 11, fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                  Text(
                    "৳${NumberFormat('#,##,###').format(product.price.toInt())}",
                    style: TextStyle(
                      color: settings.primaryColor,
                      fontWeight: FontWeight.w900,
                      fontSize: 18,
                    ),
                  ),
                  const SizedBox(height: 6),
                  Row(
                    children: [
                      Icon(
                        Icons.inventory_2_outlined,
                        size: 12,
                        color: product.stock > 0 ? Colors.green : Colors.red,
                      ),
                      const SizedBox(width: 4),
                      Expanded(
                        child: Text(
                          product.stock > 0
                              ? "${AppStrings.stock.tr()}: ${product.stock} ${AppStrings.pieces.tr()}"
                              : AppStrings.outOfStock.tr(),
                          style: TextStyle(
                            fontSize: 11,
                            color: product.stock > 0 ? Colors.green : Colors.red,
                            fontWeight: FontWeight.bold,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            if (trailing != null) trailing!,
          ],
        ),
      ),
    );
  }
}
