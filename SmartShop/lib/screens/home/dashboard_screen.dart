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
import '../../widgets/product_card.dart';
import '../../widgets/empty_state_widget.dart';
import '../../widgets/custom_app_bar.dart';
import '../../widgets/app_card.dart';

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
                  expandedHeight: 70,
                  titleWidget: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        "Hello, ${auth.user?.name ?? 'Guest'}!",
                        style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: Colors.white),
                      ),
                      Text(
                        AppStrings.welcomeMessage.tr(),
                        style: const TextStyle(fontSize: 12, color: Colors.white70),
                      ),
                    ],
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
                    Stack(
                      children: [
                        IconButton(
                          icon: const Icon(Icons.notifications_none_rounded, size: 28),
                          onPressed: () {},
                        ),
                        Positioned(
                          right: 12,
                          top: 12,
                          child: Container(width: 8, height: 8, decoration: const BoxDecoration(color: Colors.red, shape: BoxShape.circle)),
                        )
                      ],
                    ),
                  ],
                ),
                SliverToBoxAdapter(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildSearchBar(context),
                      _buildPromoBanner(context),
                      _buildSectionHeader(context, AppStrings.categoriesTitle.tr(), () {}),
                      _buildCategoryList(context),
                      _buildSectionHeader(context, AppStrings.featuredProductsTitle.tr(), () {}),
                      _buildFeaturedProducts(context, settings),
                      const SizedBox(height: 20),
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
      padding: const EdgeInsets.fromLTRB(16, 20, 16, 16),
      child: Hero(
        tag: 'search_bar',
        child: AppCard(
          elevation: 10,
          borderRadius: 25,
          child: TextField(
            onChanged: (query) => context.read<ProductViewModel>().searchProducts(query),
            decoration: InputDecoration(
              hintText: AppStrings.searchHint.tr(),
              prefixIcon: Icon(Icons.search_rounded, color: Theme.of(context).primaryColor),
              suffixIcon: Container(
                margin: const EdgeInsets.all(8),
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(color: Theme.of(context).primaryColor, borderRadius: BorderRadius.circular(15)),
                child: const Icon(Icons.tune_rounded, color: Colors.white, size: 20),
              ),
              border: InputBorder.none,
              contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 15),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildPromoBanner(BuildContext context) {
    return SizedBox(
      height: 190,
      child: PageView(
        children: [
          _promoItem(context, "UP TO 50% OFF", "Summer Electronics Sale", "Shop now for best deals", const Color(0xFF0F2027), const Color(0xFF203A43), Icons.flash_on_rounded),
          _promoItem(context, "NEW ARRIVAL", "Exclusive Fashion", "Fresh styles for you", const Color(0xFFED213A), const Color(0xFF93291E), Icons.shopping_bag_rounded),
          _promoItem(context, "FREE DELIVERY", "Orders over ৳2000", "Use code: FREE2024", const Color(0xFF1D976C), const Color(0xFF93F9B9), Icons.local_shipping_rounded),
        ],
      ),
    );
  }

  Widget _promoItem(BuildContext context, String tag, String title, String sub, Color c1, Color c2, IconData icon) {
    return AppCard(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
      borderRadius: 25,
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(25),
          gradient: LinearGradient(colors: [c1, c2], begin: Alignment.topLeft, end: Alignment.bottomRight),
        ),
        child: Stack(
          children: [
            Positioned(right: -30, bottom: -30, child: Icon(icon, size: 160, color: Colors.white.withValues(alpha: 0.1))),
            Padding(
              padding: const EdgeInsets.all(24.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                    decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.2), borderRadius: BorderRadius.circular(10)),
                    child: Text(tag, style: const TextStyle(color: Colors.white, fontSize: 11, fontWeight: FontWeight.w900)),
                  ),
                  const SizedBox(height: 12),
                  Text(title, style: const TextStyle(color: Colors.white, fontSize: 22, fontWeight: FontWeight.w900)),
                  Text(sub, style: const TextStyle(color: Colors.white70, fontSize: 14)),
                ],
              ),
            )
          ],
        ),
      ),
    );
  }

  Widget _buildSectionHeader(BuildContext context, String title, VoidCallback onSeeAll) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 25, 20, 15),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(title, style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900)),
          TextButton(onPressed: onSeeAll, child: Text(AppStrings.seeAll.tr(), style: const TextStyle(fontWeight: FontWeight.bold))),
        ],
      ),
    );
  }

  Widget _buildCategoryList(BuildContext context) {
    return Consumer<CategoryViewModel>(
      builder: (context, viewModel, child) {
        return SizedBox(
          height: 120,
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 10),
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
                      width: 80,
                      margin: const EdgeInsets.symmetric(horizontal: 10),
                      child: Column(
                        children: [
                          Container(
                            height: 75,
                            width: 75,
                            decoration: BoxDecoration(
                              color: isSelected ? cat.color : cat.color.withValues(alpha: 0.1),
                              borderRadius: BorderRadius.circular(22),
                              boxShadow: isSelected ? [BoxShadow(color: cat.color.withValues(alpha: 0.3), blurRadius: 10, offset: const Offset(0, 5))] : null,
                            ),
                            child: Icon(cat.icon, color: isSelected ? Colors.white : cat.color, size: 32),
                          ),
                          const SizedBox(height: 8),
                          Text(cat.name, style: TextStyle(fontSize: 12, fontWeight: isSelected ? FontWeight.w900 : FontWeight.bold, color: isSelected ? cat.color : Colors.grey[700]), maxLines: 1, overflow: TextOverflow.ellipsis),
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
        if (viewModel.isLoading && viewModel.featuredProducts.isEmpty) return const SizedBox(height: 280, child: Center(child: CircularProgressIndicator()));
        return SizedBox(
          height: 320,
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            scrollDirection: Axis.horizontal,
            itemCount: viewModel.featuredProducts.length,
            itemBuilder: (context, index) {
              final product = viewModel.featuredProducts[index];
              return ProductCard(product: product, heroTag: 'featured-${product.id}');
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
                  height: 320,
                  child: ListView.builder(
                    padding: const EdgeInsets.symmetric(horizontal: 12),
                    scrollDirection: Axis.horizontal,
                    itemCount: categoryProducts.length,
                    itemBuilder: (context, index) => ProductCard(product: categoryProducts[index], heroTag: 'cat-${category.id}-${categoryProducts[index].id}'),
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
              auth.user?.name ?? "Guest User",
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
                _drawerItem(context, Icons.favorite_rounded, AppStrings.wishlistMenu.tr(), () {
                  Navigator.pop(context);
                  Navigator.pushNamed(context, AppRoutes.wishlist);
                }),
                if (auth.isAdmin)
                  _drawerItem(context, Icons.admin_panel_settings_rounded, "Admin Panel", () {
                    Navigator.pop(context);
                    Navigator.pushNamed(context, AppRoutes.adminDashboard);
                  }),
                
                const Padding(
                  padding: EdgeInsets.fromLTRB(20, 20, 20, 10),
                  child: Text("App Settings", style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.grey)),
                ),

                // Language Toggle
                ListTile(
                  leading: const Icon(Icons.translate_rounded),
                  title: const Text("Language", style: TextStyle(fontWeight: FontWeight.w500)),
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
                  title: const Text("Dark Mode", style: TextStyle(fontWeight: FontWeight.w500)),
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
                  child: Row(
                    children: [
                      const Icon(Icons.palette_outlined, color: Colors.grey),
                      const SizedBox(width: 32),
                      const Text("Theme Color", style: TextStyle(fontWeight: FontWeight.w500)),
                      const Spacer(),
                      Wrap(
                        spacing: 8,
                        children: [
                          _colorDot(settings, const Color(0xFF1A237E)),
                          _colorDot(settings, const Color(0xFFD32F2F)),
                          _colorDot(settings, const Color(0xFF388E3C)),
                        ],
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
            title: const Text("Logout", style: TextStyle(color: Colors.red, fontWeight: FontWeight.bold)),
            onTap: () => auth.logout(),
          ),
          const SizedBox(height: 20),
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

  Widget _drawerItem(BuildContext context, IconData icon, String title, VoidCallback onTap, [bool selected = false]) {
    return ListTile(
      leading: Icon(icon, color: selected ? Theme.of(context).primaryColor : Colors.grey[600]),
      title: Text(title, style: TextStyle(fontWeight: selected ? FontWeight.bold : FontWeight.w500, color: selected ? Theme.of(context).primaryColor : null)),
      onTap: onTap,
      selected: selected,
      selectedTileColor: Theme.of(context).primaryColor.withValues(alpha: 0.1),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      contentPadding: const EdgeInsets.symmetric(horizontal: 20),
    );
  }
}
