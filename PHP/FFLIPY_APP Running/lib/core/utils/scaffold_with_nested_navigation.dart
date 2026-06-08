import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:go_router/go_router.dart';
import 'dialog_helper.dart';

class ScaffoldWithNestedNavigation extends StatelessWidget {
  const ScaffoldWithNestedNavigation({super.key, required this.navigationShell});

  final StatefulNavigationShell navigationShell;

  void _goBranch(int index) {
    navigationShell.goBranch(
      index,
      initialLocation: index == navigationShell.currentIndex,
    );
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvoked: (bool didPop) async {
        if (didPop) return;

        if (GoRouter.of(context).canPop()) {
          GoRouter.of(context).pop();
        } else {
          final bool shouldExit = await DialogHelper.showAppExitConfirmation(context);
          if (shouldExit && context.mounted) {
            SystemNavigator.pop();
          }
        }
      },
      child: Scaffold(
        body: navigationShell,
        bottomNavigationBar: BottomNavigationBar(
          currentIndex: navigationShell.currentIndex,
          onTap: _goBranch,
          type: BottomNavigationBarType.fixed,
          items: const [
            BottomNavigationBarItem(
              icon: Icon(Icons.home_outlined),
              activeIcon: Icon(Icons.home),
              label: 'Home',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.history_outlined),
              activeIcon: Icon(Icons.history),
              label: 'Activity',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.payments),
              activeIcon: Icon(Icons.payments_outlined),
              label: 'Send Money',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.location_searching),
              activeIcon: Icon(Icons.location_searching_outlined),
              label: 'Tracking',
            ),
          ],
        ),
      ),
    );
  }
}
