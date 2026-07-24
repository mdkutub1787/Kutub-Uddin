import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../category/riverpod/category_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../../utils/constants/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../../wishlist/riverpod/wishlist_notifier.dart';
import '../../notification/riverpod/notification_notifier.dart';
import '../../support/riverpod/support_notifier.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../widgets/product_card.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/app_card.dart';
import '../../../utils/constants/app_colors.dart';

class DashboardScreen extends ConsumerWidget {
  const DashboardScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final settings = ref.watch(settingsProvider);
    final authState = ref.watch(authNotifierProvider);
    final size = MediaQuery.of(context).size;
    
    return Scaffold(
      drawer: _buildDrawer(context, ref),
      body: Stack(
        children: [
          // Decorative Background Elements
          Positioned(
            top: -size.height * 0.1,
            right: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.4,
              backgroundColor: settings.primaryColor.withValues(alpha: 0.05),
            ),
          ),
          Positioned(
            bottom: -size.height * 0.1,
            left: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.3,
              backgroundColor: settings.primaryColor.withValues(alpha: 0.05),
            ),
          ),
          
          RefreshIndicator(
            onRefresh: () async {
              ref.read(categoryNotifierProvider.notifier).loadCategories();
            },
            child: CustomScrollView(
              physics: const BouncingScrollPhysics(parent: AlwaysScrollableScrollPhysics()),
              slivers: [
                CustomSliverAppBar(
                  expandedHeight: 95,
                  titleWidget: Consumer(
                    builder: (context, ref, _) {
                      final auth = ref.watch(authNotifierProvider).value;
                      final shopId = auth?.shopId;
                      final userName = auth?.name ?? AppStrings.guest.tr();
                      
                      if (shopId == null) {
                        return Row(
                          children: [
                            CircleAvatar(
                              radius: 20,
                              backgroundColor: Colors.white24,
                              child: Text(userName.isNotEmpty ? userName[0].toUpperCase() : 'U', style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14)),
                            ),
                            const SizedBox(width: 12),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  Text(
                                    "Hello, $userName",
                                    style: const TextStyle(fontSize: 17, fontWeight: FontWeight.w900, color: Colors.white, letterSpacing: -0.5),
                                    maxLines: 1, overflow: TextOverflow.ellipsis,
                                  ),
                                  Text(
                                    "Ready to shop today?",
                                    style: TextStyle(fontSize: 11, color: Colors.white.withValues(alpha: 0.7), fontWeight: FontWeight.w500),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        );
                      }

                      return StreamBuilder<List<Map<String, dynamic>>>(
                        stream: Supabase.instance.client.from('shops').stream(primaryKey: ['id']).eq('id', shopId),
                        builder: (context, snapshot) {
                          String shopName = "Smart Shop";
                          if (snapshot.hasData && snapshot.data!.isNotEmpty) {
                            final data = snapshot.data!.first;
                            shopName = data['name'] ?? "Smart Shop";
                          }
                          return Row(
                            children: [
                              Container(
                                padding: const EdgeInsets.all(1.5),
                                decoration: BoxDecoration(shape: BoxShape.circle, border: Border.all(color: Colors.white24, width: 1.5)),
                                child: const CircleAvatar(
                                  radius: 20,
                                  backgroundColor: Colors.white,
                                  child: Icon(Icons.storefront_rounded, color: Colors.indigo, size: 20),
                                ),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    Text(
                                      shopName,
                                      style: const TextStyle(fontSize: 20, fontWeight: FontWeight.w900, color: Colors.white, letterSpacing: -0.7),
                                      maxLines: 1, overflow: TextOverflow.ellipsis,
                                    ),
                                    Row(
                                      children: [
                                        Container(
                                          padding: const EdgeInsets.symmetric(horizontal: 5, vertical: 1.5),
                                          decoration: BoxDecoration(color: Colors.greenAccent.withValues(alpha: 0.2), borderRadius: BorderRadius.circular(4)),
                                          child: const Row(
                                            children: [
                                              Icon(Icons.circle, size: 5, color: Colors.greenAccent),
                                              SizedBox(width: 3),
                                              Text("LIVE", style: TextStyle(color: Colors.greenAccent, fontSize: 7, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
                                            ],
                                          ),
                                        ),
                                        const SizedBox(width: 6),
                                        Expanded(
                                          child: Text(
                                            "Manager: $userName",
                                            style: TextStyle(fontSize: 10, color: Colors.white.withValues(alpha: 0.6), fontWeight: FontWeight.w500),
                                            maxLines: 1,
                                            overflow: TextOverflow.ellipsis,
                                          ),
                                        ),
                                      ],
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          );
                        },
                      );
                    },
                  ),
                  showCart: true,
                  backgroundColor: settings.primaryColor,
                  centerTitle: false,
                  leading: Builder(
                    builder: (context) => IconButton(
                      icon: const Icon(Icons.grid_view_rounded, size: 28),
                      onPressed: () => Scaffold.of(context).openDrawer(),
                    ),
                  ),
                  actions: [
                    Consumer(
                      builder: (context, ref, _) {
                        // Dummy count for now
                        int totalUnread = 0; 
                        return Stack(
                          children: [
                            IconButton(
                              icon: const Icon(Icons.notifications_none_rounded, size: 28),
                              onPressed: () => Navigator.pushNamed(context, AppRoutes.notifications),
                            ),
                            if (totalUnread > 0)
                              Positioned(
                                right: 8,
                                top: 8,
                                child: Container(
                                  padding: const EdgeInsets.all(4),
                                  decoration: const BoxDecoration(color: Colors.red, shape: BoxShape.circle),
                                  constraints: const BoxConstraints(minWidth: 16, minHeight: 16),
                                  child: Text(
                                    '$totalUnread',
                                    style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
                                    textAlign: TextAlign.center,
                                  ),
                                ),
                              ),
                          ],
                        );
                      },
                    ),
                  ],
                ),
                SliverToBoxAdapter(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildSearchBar(context, ref),
                      _buildSectionHeader(context, AppStrings.categoriesTitle.tr(), () {}),
                      _buildCategoryList(context, ref),
                      _buildSectionHeader(context, AppStrings.featuredProductsTitle.tr(), () {}),
                      _buildFeaturedProducts(context, ref),
                      const SizedBox(height: 12),
                      _buildCategorySections(context, ref),
                      const SizedBox(height: 100),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSearchBar(BuildContext context, WidgetRef ref) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(12, 12, 12, 12),
      child: Hero(
        tag: 'search_bar',
        child: AppCard(
          elevation: 5,
          borderRadius: 20,
          child: TextField(
            onChanged: (query) {
               // ref.read(productNotifierProvider.notifier).searchProducts(query);
            },
            decoration: InputDecoration(
              hintText: AppStrings.searchHint.tr(),
              prefixIcon: Icon(Icons.search_rounded, color: Theme.of(context).primaryColor, size: 22),
              suffixIcon: Container(
                margin: const EdgeInsets.all(6),
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(color: Theme.of(context).primaryColor, borderRadius: BorderRadius.circular(12)),
                child: const Icon(Icons.tune_rounded, color: Colors.white, size: 18),
              ),
              border: InputBorder.none,
              contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildSectionHeader(BuildContext context, String title, VoidCallback onSeeAll) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 12, 12, 8),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(title, style: const TextStyle(fontSize: 19, fontWeight: FontWeight.w900)),
          TextButton(
            onPressed: onSeeAll, 
            style: TextButton.styleFrom(padding: EdgeInsets.zero, minimumSize: const Size(50, 30), tapTargetSize: MaterialTapTargetSize.shrinkWrap),
            child: Text(AppStrings.seeAll.tr(), style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13))
          ),
        ],
      ),
    );
  }

  Widget _buildCategoryList(BuildContext context, WidgetRef ref) {
    final categoryState = ref.watch(categoryNotifierProvider);
    
    return categoryState.when(
      data: (categories) {
        if (categories.isEmpty) return const SizedBox.shrink();
        
        // For now we don't have selectedCategoryId in state, assume dummy
        const String? selectedCategoryId = null; 

        return SizedBox(
          height: 115,
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 8),
            scrollDirection: Axis.horizontal,
            itemCount: categories.length,
            itemBuilder: (context, index) {
              final cat = categories[index];
              bool isSelected = selectedCategoryId == cat.id;
              return GestureDetector(
                onTap: () {
                  // ref.read(productNotifierProvider.notifier).filterByCategory(cat.id);
                },
                child: Container(
                  width: 75,
                  margin: const EdgeInsets.symmetric(horizontal: 6, vertical: 4),
                  child: Column(
                    children: [
                      AnimatedContainer(
                        duration: const Duration(milliseconds: 300),
                        height: 65,
                        width: 65,
                        decoration: BoxDecoration(
                          color: isSelected ? Colors.blue : Colors.white, // Dummy color
                          borderRadius: BorderRadius.circular(18),
                          boxShadow: [
                            BoxShadow(
                              color: isSelected 
                                ? Colors.blue.withValues(alpha: 0.2) 
                                : Colors.black.withValues(alpha: 0.03),
                              blurRadius: 10,
                              offset: const Offset(0, 5),
                            )
                          ],
                          border: isSelected ? null : Border.all(color: Colors.grey[100]!),
                        ),
                        child: Icon(
                          Icons.category, // Dummy icon
                          color: isSelected ? Colors.white : Colors.blue, 
                          size: 28
                        ),
                      ),
                      const SizedBox(height: 6),
                      Text(
                        cat.name.tr(), 
                        style: TextStyle(
                          fontSize: 11, 
                          fontWeight: isSelected ? FontWeight.w900 : FontWeight.bold, 
                          color: isSelected ? Colors.blue : Colors.grey[600],
                          letterSpacing: -0.2,
                        ), 
                        maxLines: 1, 
                        overflow: TextOverflow.ellipsis
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        );
      },
      loading: () => const SizedBox(height: 115, child: Center(child: CircularProgressIndicator())),
      error: (error, stack) => SizedBox(
        height: 115,
        child: Center(
          child: Text(
            error.toString().contains('JWT issued at future') 
                ? "Time mismatch. Please fix device clock." 
                : "Error loading categories",
            style: const TextStyle(fontSize: 10, color: Colors.red),
          ),
        ),
      ),
    );
  }

  Widget _buildFeaturedProducts(BuildContext context, WidgetRef ref) {
    final productState = ref.watch(productNotifierProvider);
    final products = productState.featuredProducts ?? [];
    
    if (productState.isLoading && products.isEmpty) {
      return const SizedBox(height: 250, child: Center(child: CircularProgressIndicator()));
    }
    
    return SizedBox(
      height: 290,
      child: ListView.builder(
        padding: const EdgeInsets.symmetric(horizontal: 8),
        scrollDirection: Axis.horizontal,
        itemCount: products.length,
        itemBuilder: (context, index) {
          final product = products[index];
          return ProductCard(product: product, heroTag: 'featured-${product.id}', width: 160);
        },
      ),
    );
  }

  Widget _buildCategorySections(BuildContext context, WidgetRef ref) {
    final categories = ref.watch(categoryNotifierProvider).value ?? [];
    final products = ref.watch(productNotifierProvider).featuredProducts ?? [];

    return Column(
      children: categories.take(3).map((category) {
        final categoryProducts = products.where((p) => p.categoryId == category.id).toList();
        if (categoryProducts.isEmpty) return const SizedBox.shrink();
        return Column(
          children: [
            _buildSectionHeader(context, category.name, () {
               // ref.read(productNotifierProvider.notifier).filterByCategory(category.id);
            }),
            SizedBox(
              height: 290,
              child: ListView.builder(
                padding: const EdgeInsets.symmetric(horizontal: 8),
                scrollDirection: Axis.horizontal,
                itemCount: categoryProducts.length,
                itemBuilder: (context, index) => ProductCard(product: categoryProducts[index], heroTag: 'cat-${category.id}-${categoryProducts[index].id}', width: 160),
              ),
            ),
          ],
        );
      }).toList(),
    );
  }

  Widget _buildDrawer(BuildContext context, WidgetRef ref) {
    final auth = ref.watch(authNotifierProvider).value;
    final settings = ref.watch(settingsProvider);
    final primaryColor = settings.primaryColor;
    
    final isAdmin = auth?.role == 'admin' || auth?.role == 'super_admin';

    return Drawer(
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.horizontal(right: Radius.circular(30))),
      child: Column(
        children: [
          UserAccountsDrawerHeader(
            decoration: BoxDecoration(
              color: primaryColor,
              borderRadius: const BorderRadius.only(bottomRight: Radius.circular(30)),
            ),
            currentAccountPicture: const CircleAvatar(
              backgroundColor: Colors.white,
              child: Icon(Icons.person, size: 40, color: Colors.grey),
            ),
            accountName: Text(
              auth?.name ?? AppStrings.guest.tr(),
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
            ),
            accountEmail: Text(auth?.email ?? ""),
          ),
          Expanded(
            child: ListView(
              padding: const EdgeInsets.symmetric(horizontal: 10),
              children: [
                _drawerItem(context, Icons.home_rounded, AppStrings.homeMenu.tr(), () => Navigator.pop(context), true),
                _drawerItem(context, Icons.shopping_bag_rounded, AppStrings.myOrdersMenu.tr(), () {
                  Navigator.pop(context);
                  ref.read(navigationNotifierProvider.notifier).setIndex(1);
                }),
                _drawerItem(context, Icons.local_offer_rounded, AppStrings.specialOffers.tr(), () {
                  Navigator.pop(context);
                  Navigator.pushNamed(context, AppRoutes.offers);
                }),
                _drawerItem(context, Icons.favorite_rounded, AppStrings.wishlistMenu.tr(), () {
                  Navigator.pop(context);
                  Navigator.pushNamed(context, AppRoutes.wishlist);
                }),
                _drawerItem(
                  context, 
                  Icons.notifications_active_rounded, 
                  AppStrings.notices.tr(), 
                  () {
                    Navigator.pop(context);
                    Navigator.pushNamed(context, AppRoutes.notifications);
                  },
                  false,
                  0 // Dummy count
                ),
                _drawerItem(
                  context, 
                  Icons.support_agent_rounded, 
                  AppStrings.support.tr(), 
                  () {
                    Navigator.pop(context);
                    Navigator.pushNamed(context, AppRoutes.support);
                  },
                  false,
                  0 // Dummy count
                ),
                if (isAdmin)
                  _drawerItem(context, Icons.admin_panel_settings_rounded, AppStrings.adminPanel.tr(), () {
                    Navigator.pop(context);
                    Navigator.pushNamed(context, AppRoutes.adminDashboard);
                  }),
                
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 20, 20, 10),
                  child: Text(AppStrings.appSettings.tr(), style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.grey)),
                ),

                // Language Toggle
                ListTile(
                  leading: const Icon(Icons.translate_rounded),
                  title: Text(AppStrings.language.tr(), style: const TextStyle(fontWeight: FontWeight.w500)),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      _langChip(context, "EN", context.locale.languageCode == 'en'),
                      const SizedBox(width: 5),
                      _langChip(context, "BN", context.locale.languageCode == 'bn'),
                    ],
                  ),
                ),

                // Dark Mode Toggle
                ListTile(
                  leading: Icon(settings.themeMode == ThemeMode.dark ? Icons.dark_mode_rounded : Icons.light_mode_rounded),
                  title: Text(AppStrings.darkMode.tr(), style: const TextStyle(fontWeight: FontWeight.w500)),
                  trailing: Switch.adaptive(
                    value: settings.themeMode == ThemeMode.dark,
                    activeColor: primaryColor,
                    onChanged: (val) {
                      ref.read(settingsProvider.notifier).setThemeMode(val ? ThemeMode.dark : ThemeMode.light);
                    },
                  ),
                ),

                // Primary Color Picker
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        children: [
                          const Icon(Icons.palette_outlined, color: Colors.grey),
                          const SizedBox(width: 32),
                          Text(AppStrings.themeColor.tr(), style: const TextStyle(fontWeight: FontWeight.w500)),
                        ],
                      ),
                      const SizedBox(height: 12),
                      Padding(
                        padding: const EdgeInsets.only(left: 56),
                        child: Wrap(
                          spacing: 10,
                          runSpacing: 10,
                          children: AppColors.themePalette.map((color) => _colorDot(ref, settings, color)).toList(),
                        ),
                      )
                    ],
                  ),
                ),
              ],
            ),
          ),
          const Divider(indent: 20, endIndent: 20),
          ListTile(
            leading: const Icon(Icons.logout_rounded, color: Colors.red),
            title: Text(AppStrings.logout.tr(), style: const TextStyle(color: Colors.red, fontWeight: FontWeight.bold)),
            onTap: () {
              Navigator.pop(context); // Close drawer
              _showLogoutConfirmation(context, ref);
            },
          ),
          const SizedBox(height: 20),
        ],
      ),
    );
  }

  void _showLogoutConfirmation(BuildContext context, WidgetRef ref) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: Text(AppStrings.logout.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
        content: Text(AppStrings.logoutConfirm.tr()),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: Text(AppStrings.cancel.tr()),
          ),
          ElevatedButton(
            onPressed: () async {
              Navigator.pop(ctx);
              await ref.read(authNotifierProvider.notifier).signOut();
              if (context.mounted) {
                Navigator.pushNamedAndRemoveUntil(context, AppRoutes.login, (route) => false);
              }
            },
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.red,
              foregroundColor: Colors.white,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            ),
            child: Text(AppStrings.logout.tr()),
          ),
        ],
      ),
    );
  }

  Widget _langChip(BuildContext context, String label, bool isSelected) {
    return GestureDetector(
      onTap: () {
        if (label == "EN") context.setLocale(const Locale('en', 'US'));
        else context.setLocale(const Locale('bn', 'BD'));
      },
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
        decoration: BoxDecoration(
          color: isSelected ? Theme.of(context).primaryColor : Colors.grey[200],
          borderRadius: BorderRadius.circular(10),
        ),
        child: Text(label, style: TextStyle(color: isSelected ? Colors.white : Colors.black, fontSize: 10, fontWeight: FontWeight.bold)),
      ),
    );
  }

  Widget _colorDot(WidgetRef ref, dynamic settings, Color color) {
    bool isSelected = settings.primaryColor.value == color.value;
    return GestureDetector(
      onTap: () => ref.read(settingsProvider.notifier).setPrimaryColor(color),
      child: Container(
        width: 24,
        height: 24,
        decoration: BoxDecoration(
          color: color,
          shape: BoxShape.circle,
          border: isSelected ? Border.all(color: Colors.black54, width: 2) : null,
        ),
        child: isSelected ? const Icon(Icons.check, size: 14, color: Colors.white) : null,
      ),
    );
  }

  Widget _drawerItem(BuildContext context, IconData icon, String title, VoidCallback onTap, [bool selected = false, int badgeCount = 0]) {
    return ListTile(
      leading: Icon(icon, color: selected ? Theme.of(context).primaryColor : Colors.grey[600]),
      title: Text(title, style: TextStyle(fontWeight: selected ? FontWeight.bold : FontWeight.w500, color: selected ? Theme.of(context).primaryColor : null)),
      trailing: badgeCount > 0 
        ? Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
            decoration: BoxDecoration(color: Colors.red, borderRadius: BorderRadius.circular(10)),
            child: Text('$badgeCount', style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
          )
        : null,
      onTap: onTap,
      selected: selected,
      selectedTileColor: Theme.of(context).primaryColor.withValues(alpha: 0.1),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      contentPadding: const EdgeInsets.symmetric(horizontal: 20),
    );
  }
}
