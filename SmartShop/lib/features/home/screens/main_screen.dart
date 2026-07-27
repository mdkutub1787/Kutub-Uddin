import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import 'dashboard_screen.dart';
import '../../cart/screens/cart_screen.dart';
import '../../order/screens/my_orders_screen.dart';
import '../../profile/screens/profile_screen.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../support/riverpod/support_notifier.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../order/models/order_model.dart';
import '../../../core/app_strings.dart';
import '../../../core/utils/exit_dialog_helper.dart';
import '../../../services/notification_service.dart';

class MainScreen extends ConsumerStatefulWidget {
  const MainScreen({super.key});

  @override
  ConsumerState<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends ConsumerState<MainScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final auth = ref.read(authNotifierProvider).value;
      if (auth != null) {
      }
    });
  }
  
  final List<Widget> _screens = [
    const DashboardScreen(),
    const MyOrdersScreen(),
    const CartScreen(),
    const ProfileScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    final user = ref.watch(authNotifierProvider).value;
    
    // Listen to user orders for status change notifications
    if (user != null) {
      ref.listen<AsyncValue<List<OrderModel>>>(userOrdersStreamProvider(user.uid), (previous, next) {
        if (previous != null && previous.hasValue && next.hasValue) {
          final previousOrders = previous.value!;
          final nextOrders = next.value!;
          
          for (final nextOrder in nextOrders) {
            final prevOrderIndex = previousOrders.indexWhere((o) => o.id == nextOrder.id);
            if (prevOrderIndex != -1) {
              final prevOrder = previousOrders[prevOrderIndex];
              if (prevOrder.status != nextOrder.status) {
                // Status changed!
                NotificationService().showNotification(
                  id: nextOrder.id.hashCode,
                  title: 'Order Status Update',
                  body: 'Your order #${nextOrder.id.substring(nextOrder.id.length - 8)} is now ${nextOrder.status}',
                );
              }
            }
          }
        }
      });
    }

    final settings = ref.watch(settingsProvider);
    final cartItems = ref.watch(cartNotifierProvider) ?? [];
    final cartItemCount = cartItems.fold(0, (sum, item) => sum + item.quantity);
    final selectedIndex = ref.watch(navigationNotifierProvider);

    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (didPop) return;
        await ExitDialogHelper.showExitDialog(context);
      },
      child: Scaffold(
        backgroundColor: const Color(0xFFF5F7FA), // Light background for the scaffold
        body: IndexedStack(
          index: selectedIndex,
          children: _screens,
        ),
        bottomNavigationBar: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: const BorderRadius.only(
              topLeft: Radius.circular(30),
              topRight: Radius.circular(30),
            ),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.05),
                blurRadius: 20,
                offset: const Offset(0, -5),
              ),
            ],
          ),
          child: SafeArea(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 12),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  _buildNavItem(0, Icons.home_rounded, Icons.home_outlined, AppStrings.homeMenu.tr(), selectedIndex, settings),
                  _buildNavItem(1, Icons.receipt_long_rounded, Icons.receipt_long_outlined, AppStrings.myOrdersMenu.tr(), selectedIndex, settings),
                  _buildNavItem(2, Icons.shopping_bag_rounded, Icons.shopping_bag_outlined, AppStrings.myCart.tr(), selectedIndex, settings, badgeCount: cartItemCount),
                  _buildNavItem(3, Icons.person_rounded, Icons.person_outline_rounded, AppStrings.accountMenu.tr(), selectedIndex, settings),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildNavItem(int index, IconData activeIcon, IconData inactiveIcon, String label, int selectedIndex, dynamic settings, {int badgeCount = 0}) {
    bool isSelected = selectedIndex == index;
    return GestureDetector(
      onTap: () => ref.read(navigationNotifierProvider.notifier).setIndex(index),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 300),
        curve: Curves.easeOutCubic,
        padding: EdgeInsets.symmetric(horizontal: isSelected ? 20 : 16, vertical: 12),
        decoration: BoxDecoration(
          color: isSelected ? settings.primaryColor.withValues(alpha: 0.1) : Colors.transparent,
          borderRadius: BorderRadius.circular(30),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Stack(
              clipBehavior: Clip.none,
              children: [
                Icon(isSelected ? activeIcon : inactiveIcon, color: isSelected ? settings.primaryColor : Colors.grey[400], size: 24),
                if (badgeCount > 0)
                  Positioned(
                    right: -6,
                    top: -6,
                    child: Container(
                      padding: const EdgeInsets.all(4),
                      decoration: const BoxDecoration(color: Colors.red, shape: BoxShape.circle),
                      constraints: const BoxConstraints(minWidth: 16, minHeight: 16),
                      child: Text(
                        '$badgeCount',
                        style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
                        textAlign: TextAlign.center,
                      ),
                    ),
                  )
              ],
            ),
            if (isSelected) ...[
              const SizedBox(width: 8),
              Text(
                label,
                style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.w800, fontSize: 13),
              ),
            ]
          ],
        ),
      ),
    );
  }
}
