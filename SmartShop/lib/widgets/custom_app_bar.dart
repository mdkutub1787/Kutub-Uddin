import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../features/cart/riverpod/cart_notifier.dart';
import '../routes/app_routes.dart';

class CustomAppBar extends ConsumerWidget implements PreferredSizeWidget {
  final String? title;
  final Widget? titleWidget;
  final List<Widget>? actions;
  final Widget? leading;
  final bool centerTitle;
  final Color? backgroundColor;
  final double elevation;
  final bool showCart;
  final bool showNotification;
  final PreferredSizeWidget? bottom;

  const CustomAppBar({
    super.key,
    this.title,
    this.titleWidget,
    this.actions,
    this.leading,
    this.centerTitle = true,
    this.backgroundColor,
    this.elevation = 0,
    this.showCart = false,
    this.showNotification = false,
    this.bottom,
  });

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return AppBar(
      title: titleWidget ??
          (title != null
              ? Text(
                  title!,
                  style: const TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 22,
                  ),
                )
              : null),
      centerTitle: centerTitle,
      backgroundColor: backgroundColor ?? Theme.of(context).primaryColor,
      elevation: elevation,
      leading: leading,
      actions: _buildActions(context, ref),
      bottom: bottom,
      iconTheme: const IconThemeData(color: Colors.white),
      titleTextStyle: const TextStyle(color: Colors.white, fontSize: 20, fontWeight: FontWeight.bold),
    );
  }

  List<Widget> _buildActions(BuildContext context, WidgetRef ref) {
    List<Widget> actionList = actions ?? [];

    if (showCart) {
      actionList.add(
        Stack(
          children: [
            IconButton(
              icon: const Icon(Icons.shopping_cart_outlined, size: 26),
              onPressed: () => Navigator.pushNamed(context, AppRoutes.cart),
            ),
            Positioned(
              right: 8,
              top: 8,
              child: Consumer(
                builder: (context, ref, child) {
                  final cartItems = ref.watch(cartNotifierProvider) ?? [];
                  final itemCount = cartItems.fold(0, (sum, item) => sum + item.quantity);
                  
                  return itemCount > 0
                      ? Container(
                          padding: const EdgeInsets.all(2),
                          decoration: BoxDecoration(
                            color: Colors.red,
                            borderRadius: BorderRadius.circular(10),
                          ),
                          constraints: const BoxConstraints(
                            minWidth: 16,
                            minHeight: 16,
                          ),
                          child: Text(
                            '$itemCount',
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 10,
                            ),
                            textAlign: TextAlign.center,
                          ),
                        )
                      : const SizedBox.shrink();
                },
              ),
            ),
          ],
        ),
      );
    }

    if (showNotification) {
      actionList.add(
        IconButton(
          icon: const Icon(Icons.notifications_none_rounded, size: 26),
          onPressed: () {},
        ),
      );
    }

    return actionList;
  }

  @override
  Size get preferredSize => Size.fromHeight(kToolbarHeight + (bottom?.preferredSize.height ?? 0.0));
}

class CustomSliverAppBar extends ConsumerWidget {
  final String? title;
  final Widget? titleWidget;
  final List<Widget>? actions;
  final Widget? leading;
  final bool centerTitle;
  final Color? backgroundColor;
  final bool floating;
  final bool pinned;
  final double expandedHeight;
  final Widget? flexibleSpace;
  final bool showCart;

  const CustomSliverAppBar({
    super.key,
    this.title,
    this.titleWidget,
    this.actions,
    this.leading,
    this.centerTitle = true,
    this.backgroundColor,
    this.floating = true,
    this.pinned = true,
    this.expandedHeight = 100.0,
    this.flexibleSpace,
    this.showCart = false,
  });

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return SliverAppBar(
      expandedHeight: expandedHeight,
      floating: floating,
      pinned: pinned,
      elevation: 0,
      backgroundColor: backgroundColor ?? Theme.of(context).primaryColor,
      centerTitle: centerTitle,
      leading: leading,
      flexibleSpace: flexibleSpace,
      title: titleWidget ??
          (title != null
              ? Text(
                  title!,
                  style: const TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 22,
                    color: Colors.white,
                  ),
                )
              : null),
      actions: _buildActions(context, ref),
      iconTheme: const IconThemeData(color: Colors.white),
    );
  }

  List<Widget> _buildActions(BuildContext context, WidgetRef ref) {
    List<Widget> actionList = actions ?? [];

    if (showCart) {
      actionList.add(
        Stack(
          children: [
            IconButton(
              icon: const Icon(Icons.shopping_cart_outlined, size: 26, color: Colors.white),
              onPressed: () => Navigator.pushNamed(context, AppRoutes.cart),
            ),
            Positioned(
              right: 8,
              top: 8,
              child: Consumer(
                builder: (context, ref, child) {
                  final cartItems = ref.watch(cartNotifierProvider) ?? [];
                  final itemCount = cartItems.fold(0, (sum, item) => sum + item.quantity);
                  
                  return itemCount > 0
                      ? Container(
                          padding: const EdgeInsets.all(2),
                          decoration: BoxDecoration(
                            color: Colors.red,
                            borderRadius: BorderRadius.circular(10),
                          ),
                          constraints: const BoxConstraints(
                            minWidth: 16,
                            minHeight: 16,
                          ),
                          child: Text(
                            '$itemCount',
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 10,
                            ),
                            textAlign: TextAlign.center,
                          ),
                        )
                      : const SizedBox.shrink();
                },
              ),
            ),
          ],
        ),
      );
    }
    return actionList;
  }
}
