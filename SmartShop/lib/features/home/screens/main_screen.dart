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
import '../../../core/app_strings.dart';
import '../../../core/utils/exit_dialog_helper.dart';

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
        extendBody: true, // Needed for floating nav bar
        body: IndexedStack(
          index: selectedIndex,
          children: _screens,
        ),
        bottomNavigationBar: Container(
          margin: const EdgeInsets.only(left: 20, right: 20, bottom: 20),
          decoration: BoxDecoration(
            color: Colors.white.withValues(alpha: 0.9),
            borderRadius: BorderRadius.circular(40),
            boxShadow: [
              BoxShadow(
                color: settings.primaryColor.withValues(alpha: 0.15),
                blurRadius: 30,
                offset: const Offset(0, 10),
              ),
            ],
          ),
          child: ClipRRect(
            borderRadius: BorderRadius.circular(40),
            child: BackdropFilter(
              filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
              child: NavigationBarTheme(
                data: NavigationBarThemeData(
                  indicatorColor: settings.primaryColor.withValues(alpha: 0.15),
                  labelTextStyle: WidgetStateProperty.resolveWith((states) {
                    if (states.contains(WidgetState.selected)) {
                      return TextStyle(
                        fontSize: 12,
                        fontWeight: FontWeight.w800,
                        color: settings.primaryColor,
                      );
                    }
                    return const TextStyle(fontSize: 12, color: Colors.grey, fontWeight: FontWeight.w500);
                  }),
                  iconTheme: WidgetStateProperty.resolveWith((states) {
                    if (states.contains(WidgetState.selected)) {
                      return IconThemeData(color: settings.primaryColor, size: 26);
                    }
                    return const IconThemeData(color: Colors.grey, size: 24);
                  }),
                ),
                child: NavigationBar(
                  selectedIndex: selectedIndex,
                  onDestinationSelected: (index) => ref.read(navigationNotifierProvider.notifier).setIndex(index),
                  backgroundColor: Colors.transparent, // Let Container handle background
                  elevation: 0,
                  height: 75,
                  labelBehavior: NavigationDestinationLabelBehavior.onlyShowSelected,
                  destinations: [
                    NavigationDestination(
                      icon: const Icon(Icons.home_outlined),
                      selectedIcon: const Icon(Icons.home_rounded),
                      label: AppStrings.homeMenu.tr(),
                    ),
                    NavigationDestination(
                      icon: const Icon(Icons.receipt_long_outlined),
                      selectedIcon: const Icon(Icons.receipt_long_rounded),
                      label: AppStrings.myOrdersMenu.tr(),
                    ),
                    NavigationDestination(
                      icon: Badge(
                        label: Text('$cartItemCount'),
                        isLabelVisible: cartItemCount > 0,
                        child: const Icon(Icons.shopping_bag_outlined),
                      ),
                      selectedIcon: Badge(
                        label: Text('$cartItemCount'),
                        isLabelVisible: cartItemCount > 0,
                        child: const Icon(Icons.shopping_bag_rounded),
                      ),
                      label: AppStrings.myCart.tr(),
                    ),
                    NavigationDestination(
                      icon: const Icon(Icons.person_outline_rounded),
                      selectedIcon: const Icon(Icons.person_rounded),
                      label: AppStrings.accountMenu.tr(),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
