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
import '../../../utils/constants/app_strings.dart';

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
        final shouldExit = await _showExitConfirmation(context, settings.primaryColor);
        if (shouldExit && context.mounted) {
          SystemNavigator.pop();
        }
      },
      child: Scaffold(
        body: IndexedStack(
          index: selectedIndex,
          children: _screens,
        ),
        bottomNavigationBar: NavigationBarTheme(
          data: NavigationBarThemeData(
            indicatorColor: settings.primaryColor.withValues(alpha: 0.15),
            labelTextStyle: WidgetStateProperty.resolveWith((states) {
              if (states.contains(WidgetState.selected)) {
                return TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.bold,
                  color: settings.primaryColor,
                );
              }
              return const TextStyle(fontSize: 12, color: Colors.grey);
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
            backgroundColor: Theme.of(context).cardColor,
            elevation: 10,
            height: 70,
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
                  child: const Icon(Icons.shopping_cart_outlined),
                ),
                selectedIcon: Badge(
                  label: Text('$cartItemCount'),
                  isLabelVisible: cartItemCount > 0,
                  child: const Icon(Icons.shopping_cart_rounded),
                ),
                label: AppStrings.myCart.tr(),
              ),
              NavigationDestination(
                icon: const Icon(Icons.account_circle_outlined),
                selectedIcon: const Icon(Icons.account_circle_rounded),
                label: AppStrings.accountMenu.tr(),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<bool> _showExitConfirmation(BuildContext context, Color primaryColor) async {
    return await showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
            title: Text(AppStrings.exitApp.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
            content: Text(AppStrings.exitConfirm.tr()),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(ctx, false),
                child: Text(AppStrings.stay.tr()),
              ),
              ElevatedButton(
                onPressed: () => Navigator.pop(ctx, true),
                style: ElevatedButton.styleFrom(
                  backgroundColor: primaryColor,
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: Text(AppStrings.exit.tr()),
              ),
            ],
          ),
        ) ??
        false;
  }
}
