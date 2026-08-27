import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:deen_life/core/localization/app_localizations.dart';
import 'package:deen_life/core/utils/dialog_helper.dart';

import '../../features/home/presentation/screens/home_screen.dart';
import '../../features/quran/presentation/screens/quran_screen.dart';
import '../../features/masjid/presentation/screens/masjid_list_screen.dart';
import '../../features/explore/presentation/screens/explore_screen.dart';
import '../../features/settings/presentation/screens/settings_screen.dart';

final selectedIndexProvider = StateProvider<int>((ref) => 0);
final lastPopTimeProvider = StateProvider<DateTime>(
  (ref) => DateTime.now().subtract(const Duration(seconds: 2)),
);

class MainLayout extends ConsumerWidget {
  const MainLayout({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedIndex = ref.watch(selectedIndexProvider);

    final pages = [
      const HomeScreen(),
      const QuranScreen(),
      const MasjidListScreen(),
      const ExploreScreen(),
      const SettingsScreen(),
    ];

    return PopScope(
      canPop: false,
      onPopInvoked: (didPop) async {
        if (didPop) return;

        if (selectedIndex != 0) {
          ref.read(selectedIndexProvider.notifier).state = 0;
          return;
        }

        final bool shouldExit = await DialogHelper.showAppExitConfirmation(
          context,
        );
        if (shouldExit && context.mounted) {
          SystemNavigator.pop();
        }
      },
      child: Scaffold(
        body: pages[selectedIndex],
        bottomNavigationBar: NavigationBar(
          selectedIndex: selectedIndex,
          onDestinationSelected: (index) {
            ref.read(selectedIndexProvider.notifier).state = index;
          },
          destinations: [
            NavigationDestination(
              icon: const Icon(Icons.home_outlined),
              selectedIcon: const Icon(Icons.home),
              label: context.tr('Home'),
            ),
            NavigationDestination(
              icon: const Icon(Icons.menu_book_outlined),
              selectedIcon: const Icon(Icons.menu_book),
              label: context.tr('Quran'),
            ),
            NavigationDestination(
              icon: const Icon(Icons.mosque_outlined),
              selectedIcon: const Icon(Icons.mosque),
              label: context.tr('Masjid'),
            ),
            NavigationDestination(
              icon: const Icon(Icons.explore_outlined),
              selectedIcon: const Icon(Icons.explore),
              label: context.tr('Explore'),
            ),
            NavigationDestination(
              icon: const Icon(Icons.settings_outlined),
              selectedIcon: const Icon(Icons.settings),
              label: context.tr('Settings'),
            ),
          ],
        ),
      ),
    );
  }
}

