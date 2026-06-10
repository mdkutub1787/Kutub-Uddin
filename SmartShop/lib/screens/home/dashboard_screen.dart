import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/view_models/category_view_model.dart';
import 'package:smart_shop/view_models/auth_view_model.dart';
import 'package:smart_shop/view_models/product_view_model.dart';
import 'package:smart_shop/view_models/settings_view_model.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:smart_shop/routes/app_routes.dart';
import 'package:smart_shop/view_models/navigation_view_model.dart';
import '../../view_models/cart_view_model.dart';
import '../../view_models/wishlist_view_model.dart';
import '../../view_models/notification_view_model.dart';
import '../../view_models/support_view_model.dart';
import 'package:firebase_database/firebase_database.dart';
import '../../widgets/product_card.dart';
import '../../widgets/custom_app_bar.dart';
import '../../widgets/app_card.dart';
import '../../utils/constants/app_colors.dart';

class DashboardScreen extends StatelessWidget {
  const DashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final auth = context.watch<AuthViewModel>();
    final size = MediaQuery.of(context).size;
    
    return Scaffold(
      drawer: _buildDrawer(context),
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
              await context.read<ProductViewModel>().fetchFeaturedProducts();
              if (context.mounted) {
                await context.read<CategoryViewModel>().refreshCategories();
              }
            },
            child: CustomScrollView(
              physics: const BouncingScrollPhysics(parent: AlwaysScrollableScrollPhysics()),
              slivers: [
                CustomSliverAppBar(
                  expandedHeight: 110,
                  titleWidget: Consumer<AuthViewModel>(
                    builder: (context, authVM, _) {
                      final shopId = authVM.user?.shopId;
                      final userName = authVM.user?.name ?? AppStrings.guest.tr();
                      
                      if (shopId == null) {
                        return Row(
                          children: [
                            CircleAvatar(
                              radius: 22,
                              backgroundColor: Colors.white24,
                              child: Text(userName[0].toUpperCase(), style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                            ),
                            const SizedBox(width: 15),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  Text(
                                    "Hello, $userName",
                                    style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: Colors.white, letterSpacing: -0.5),
                                    maxLines: 1, overflow: TextOverflow.ellipsis,
                                  ),
                                  Text(
                                    "Ready to shop today?",
                                    style: TextStyle(fontSize: 12, color: Colors.white.withValues(alpha: 0.7), fontWeight: FontWeight.w500),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        );
                      }

                      return StreamBuilder<DatabaseEvent>(
                        stream: FirebaseDatabase.instance.ref().child('shops').child(shopId).onValue,
                        builder: (context, snapshot) {
                          String shopName = "Smart Shop";
                          if (snapshot.hasData && snapshot.data!.snapshot.value != null) {
                            final data = Map<dynamic, dynamic>.from(snapshot.data!.snapshot.value as Map);
                            shopName = data['name'] ?? "Smart Shop";
                          }
                          return Row(
                            children: [
                              Container(
                                padding: const EdgeInsets.all(2),
                                decoration: BoxDecoration(shape: BoxShape.circle, border: Border.all(color: Colors.white30, width: 2)),
                                child: const CircleAvatar(
                                  radius: 22,
                                  backgroundColor: Colors.white,
                                  child: Icon(Icons.storefront_rounded, color: Colors.indigo, size: 24),
                                ),
                              ),
                              const SizedBox(width: 15),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    Text(
                                      shopName,
                                      style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900, color: Colors.white, letterSpacing: -0.8),
                                      maxLines: 1, overflow: TextOverflow.ellipsis,
                                    ),
                                    Row(
                                      children: [
                                        Container(
                                          padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                                          decoration: BoxDecoration(color: Colors.greenAccent.withValues(alpha: 0.2), borderRadius: BorderRadius.circular(4)),
                                          child: const Row(
                                            children: [
                                              Icon(Icons.circle, size: 6, color: Colors.greenAccent),
                                              SizedBox(width: 4),
                                              Text("LIVE", style: TextStyle(color: Colors.greenAccent, fontSize: 8, fontWeight: FontWeight.w900, letterSpacing: 0.5)),
                                            ],
                                          ),
                                        ),
                                        const SizedBox(width: 8),
                                        Expanded(
                                          child: Text(
                                            "Manager: $userName",
                                            style: TextStyle(fontSize: 11, color: Colors.white.withValues(alpha: 0.6), fontWeight: FontWeight.w500),
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
                    Consumer2<NotificationViewModel, SupportViewModel>(
                      builder: (context, noticeVM, supportVM, _) {
                        int totalUnread = noticeVM.unreadCount + supportVM.unreadCount;
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
                      _buildSearchBar(context),
                      _buildSectionHeader(context, AppStrings.categoriesTitle.tr(), () {}),
                      _buildCategoryList(context),
                      _buildSectionHeader(context, AppStrings.featuredProductsTitle.tr(), () {}),
                      _buildFeaturedProducts(context, settings),
                      const SizedBox(height: 12),
                      _buildCategorySections(context, settings),
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

  Widget _buildSearchBar(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(12, 12, 12, 12),
      child: Hero(
        tag: 'search_bar',
        child: AppCard(
          elevation: 5,
          borderRadius: 20,
          child: TextField(
            onChanged: (query) => context.read<ProductViewModel>().searchProducts(query),
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

  Widget _buildCategoryList(BuildContext context) {
    return Consumer<CategoryViewModel>(
      builder: (context, viewModel, child) {
        return SizedBox(
          height: 115,
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 8),
            scrollDirection: Axis.horizontal,
            itemCount: viewModel.categories.length,
            itemBuilder: (context, index) {
              final cat = viewModel.categories[index];
              return Consumer<ProductViewModel>(
                builder: (context, productVM, child) {
                  bool isSelected = productVM.selectedCategoryId == cat.id;
                  return GestureDetector(
                    onTap: () => productVM.filterByCategory(cat.id),
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
                              color: isSelected ? cat.color : Colors.white,
                              borderRadius: BorderRadius.circular(18),
                              boxShadow: [
                                BoxShadow(
                                  color: isSelected 
                                    ? cat.color.withValues(alpha: 0.2) 
                                    : Colors.black.withValues(alpha: 0.03),
                                  blurRadius: 10,
                                  offset: const Offset(0, 5),
                                )
                              ],
                              border: isSelected ? null : Border.all(color: Colors.grey[100]!),
                            ),
                            child: Icon(
                              cat.icon, 
                              color: isSelected ? Colors.white : cat.color, 
                              size: 28
                            ),
                          ),
                          const SizedBox(height: 6),
                          Text(
                            cat.name.tr(), 
                            style: TextStyle(
                              fontSize: 11, 
                              fontWeight: isSelected ? FontWeight.w900 : FontWeight.bold, 
                              color: isSelected ? cat.color : Colors.grey[600],
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
              );
            },
          ),
        );
      },
    );
  }

  Widget _buildFeaturedProducts(BuildContext context, SettingsViewModel settings) {
    return Consumer<ProductViewModel>(
      builder: (context, viewModel, child) {
        if (viewModel.isLoading && viewModel.featuredProducts.isEmpty) return const SizedBox(height: 250, child: Center(child: CircularProgressIndicator()));
        return SizedBox(
          height: 290,
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 8),
            scrollDirection: Axis.horizontal,
            itemCount: viewModel.featuredProducts.length,
            itemBuilder: (context, index) {
              final product = viewModel.featuredProducts[index];
              return ProductCard(product: product, heroTag: 'featured-${product.id}', width: 160);
            },
          ),
        );
      },
    );
  }

  Widget _buildCategorySections(BuildContext context, SettingsViewModel settings) {
    return Consumer2<CategoryViewModel, ProductViewModel>(
      builder: (context, catVM, prodVM, child) {
        return Column(
          children: catVM.categories.take(3).map((category) {
            final categoryProducts = prodVM.featuredProducts.where((p) => p.categoryId == category.id).toList();
            if (categoryProducts.isEmpty) return const SizedBox.shrink();
            return Column(
              children: [
                _buildSectionHeader(context, category.name, () => prodVM.filterByCategory(category.id)),
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
      },
    );
  }

  Widget _buildDrawer(BuildContext context) {
    final auth = context.watch<AuthViewModel>();
    final settings = context.watch<SettingsViewModel>();
    final primaryColor = settings.primaryColor;

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
              auth.user?.name ?? AppStrings.guest.tr(),
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
            ),
            accountEmail: Text(auth.user?.email ?? ""),
          ),
          Expanded(
            child: ListView(
              padding: const EdgeInsets.symmetric(horizontal: 10),
              children: [
                _drawerItem(context, Icons.home_rounded, AppStrings.homeMenu.tr(), () => Navigator.pop(context), true),
                _drawerItem(context, Icons.shopping_bag_rounded, AppStrings.myOrdersMenu.tr(), () {
                  Navigator.pop(context);
                  context.read<NavigationViewModel>().setIndex(1);
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
                  context.watch<NotificationViewModel>().unreadCount
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
                  context.watch<SupportViewModel>().unreadCount
                ),
                if (auth.isAdmin)
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
                      settings.setThemeMode(val ? ThemeMode.dark : ThemeMode.light);
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
                          children: AppColors.themePalette.map((color) => _colorDot(settings, color)).toList(),
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
              _showLogoutConfirmation(context, auth);
            },
          ),
          const SizedBox(height: 20),
        ],
      ),
    );
  }

  void _showLogoutConfirmation(BuildContext context, AuthViewModel authViewModel) {
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
              context.read<CartViewModel>().clearCart();
              context.read<WishlistViewModel>().clear();
              await authViewModel.logout();
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

  Widget _colorDot(SettingsViewModel settings, Color color) {
    bool isSelected = settings.primaryColor.value == color.value;
    return GestureDetector(
      onTap: () => settings.setPrimaryColor(color),
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
