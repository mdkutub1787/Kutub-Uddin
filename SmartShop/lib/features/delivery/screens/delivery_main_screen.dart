import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'delivery_dashboard_screen.dart';
import 'rider_global_map_screen.dart';
import '../../profile/screens/profile_screen.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../../core/utils/exit_dialog_helper.dart';

class DeliveryMainScreen extends ConsumerStatefulWidget {
  const DeliveryMainScreen({super.key});

  @override
  ConsumerState<DeliveryMainScreen> createState() => _DeliveryMainScreenState();
}

class _DeliveryMainScreenState extends ConsumerState<DeliveryMainScreen> {
  final List<Widget> _screens = [
    const DeliveryDashboardScreen(),
    const RiderGlobalMapScreen(),
    const ProfileScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    final selectedIndex = ref.watch(navigationNotifierProvider);

    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (didPop) return;
        if (selectedIndex != 0) {
          ref.read(navigationNotifierProvider.notifier).setIndex(0);
          return;
        }
        await ExitDialogHelper.showExitDialog(context);
      },
      child: Scaffold(
        backgroundColor: const Color(0xFFF5F7FA),
        body: IndexedStack(
          index: selectedIndex > 2 ? 0 : selectedIndex,
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
          child: ClipRRect(
            borderRadius: const BorderRadius.only(
              topLeft: Radius.circular(30),
              topRight: Radius.circular(30),
            ),
            child: BackdropFilter(
              filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
              child: BottomNavigationBar(
                currentIndex: selectedIndex > 2 ? 0 : selectedIndex,
                onTap: (index) {
                  HapticFeedback.lightImpact();
                  ref.read(navigationNotifierProvider.notifier).setIndex(index);
                },
                backgroundColor: Colors.white.withValues(alpha: 0.9),
                selectedItemColor: settings.primaryColor,
                unselectedItemColor: Colors.grey[400],
                selectedLabelStyle: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                unselectedLabelStyle: const TextStyle(fontWeight: FontWeight.w600, fontSize: 12),
                type: BottomNavigationBarType.fixed,
                elevation: 0,
                items: [
                  _buildNavItem(Icons.dashboard_rounded, Icons.dashboard_outlined, "Dashboard", 0, selectedIndex, settings.primaryColor),
                  _buildNavItem(Icons.map_rounded, Icons.map_outlined, "Map", 1, selectedIndex, settings.primaryColor),
                  _buildNavItem(Icons.person_rounded, Icons.person_outline_rounded, "Profile", 2, selectedIndex, settings.primaryColor),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  BottomNavigationBarItem _buildNavItem(
      IconData activeIcon, IconData inactiveIcon, String label, int index, int selectedIndex, Color primaryColor) {
    final isSelected = index == selectedIndex;
    return BottomNavigationBarItem(
      icon: Container(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        decoration: BoxDecoration(
          color: isSelected ? primaryColor.withValues(alpha: 0.1) : Colors.transparent,
          borderRadius: BorderRadius.circular(20),
        ),
        child: Icon(
          isSelected ? activeIcon : inactiveIcon,
          size: 24,
          color: isSelected ? primaryColor : Colors.grey[400],
        ),
      ),
      label: label,
    );
  }
}
