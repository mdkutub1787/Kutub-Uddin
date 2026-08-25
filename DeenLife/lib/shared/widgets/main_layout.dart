import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:deen_life/core/localization/app_localizations.dart';
import 'package:deen_life/core/utils/dialog_helper.dart';
import '../../features/home/presentation/pages/home_page.dart';
import '../../features/tasbeeh/presentation/pages/tasbeeh_page.dart';
import '../../features/quran/presentation/pages/quran_page.dart';
import '../../features/qibla/presentation/pages/qibla_page.dart';
import '../../features/duas/presentation/pages/dua_page.dart';

final selectedIndexProvider = StateProvider<int>((ref) => 0);
final lastPopTimeProvider = StateProvider<DateTime>((ref) => DateTime.now().subtract(const Duration(seconds: 2)));

class MainLayout extends ConsumerWidget {
  const MainLayout({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedIndex = ref.watch(selectedIndexProvider);

    final pages = [
      const HomePage(),
      const QuranPage(),
      const QiblaPage(),
      DuaPage(),
      const TasbeehPage(),
    ];

    return PopScope(
      canPop: false,
      onPopInvoked: (didPop) async {
        if (didPop) return;
        
        if (selectedIndex != 0) {
          ref.read(selectedIndexProvider.notifier).state = 0;
          return;
        }

        final bool shouldExit = await DialogHelper.showAppExitConfirmation(context);
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
              icon: const Icon(Icons.explore_outlined),
              selectedIcon: const Icon(Icons.explore),
              label: context.tr('Qibla'),
            ),
            NavigationDestination(
              icon: const Icon(Icons.favorite_outline),
              selectedIcon: const Icon(Icons.favorite),
              label: context.tr('Duas'),
            ),
            NavigationDestination(
              icon: const Icon(Icons.touch_app_outlined),
              selectedIcon: const Icon(Icons.touch_app),
              label: context.tr('Tasbeeh'),
            ),
          ],
        ),
      ),
    );
  }
}


