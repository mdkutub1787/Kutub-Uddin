import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/view_models/category_view_model.dart';
import 'package:smart_shop/view_models/auth_view_model.dart';
import 'package:smart_shop/view_models/product_view_model.dart';
import 'package:smart_shop/view_models/settings_view_model.dart';
import 'package:smart_shop/view_models/cart_view_model.dart';
import 'package:smart_shop/view_models/wishlist_view_model.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:smart_shop/routes/app_routes.dart';
import 'package:smart_shop/screens/admin/admin_dashboard_screen.dart';
import 'package:smart_shop/widgets/custom_app_bar.dart';

class DashboardScreen extends StatelessWidget {
  const DashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final auth = context.watch<AuthViewModel>();
    
    return Scaffold(
      drawer: _buildDrawer(context),
      body: CustomScrollView(
        physics: const BouncingScrollPhysics(),
        slivers: [
          CustomSliverAppBar(
            expandedHeight: 60,
            titleWidget: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  AppStrings.appName.tr(),
                  style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w500, color: Colors.white70),
                ),
                Text(
                  "Hello, ${auth.user?.displayName ?? 'Guest'}!",
                  style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.white),
                ),
              ],
            ),
            showCart: true,
            backgroundColor: settings.primaryColor,
            centerTitle: false,
            leading: Builder(
              builder: (context) => IconButton(
                icon: const Icon(Icons.menu_open_rounded, size: 28),
                onPressed: () => Scaffold.of(context).openDrawer(),
              ),
            ),
            actions: [
              IconButton(
                icon: const Icon(Icons.notifications_none_rounded, size: 28, color: Colors.white),
                onPressed: () {},
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
                _buildFeaturedProducts(context),
                const SizedBox(height: 100),
              ],
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {},
        backgroundColor: settings.primaryColor,
        icon: const Icon(Icons.support_agent_rounded, color: Colors.white),
        label: Text(
          AppStrings.help.tr(),
          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
      ),
    );
  }

  Widget _buildSearchBar(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 20, 16, 16),
      child: Container(
        height: 60,
        decoration: BoxDecoration(
          color: Theme.of(context).cardColor,
          borderRadius: BorderRadius.circular(18),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.04),
              blurRadius: 20,
              offset: const Offset(0, 4),
            )
          ],
        ),
        child: TextField(
          textAlignVertical: TextAlignVertical.center,
          onChanged: (query) => context.read<ProductViewModel>().searchProducts(query),
          decoration: InputDecoration(
            hintText: AppStrings.searchHint.tr(),
            border: InputBorder.none,
            enabledBorder: InputBorder.none,
            focusedBorder: InputBorder.none,
            prefixIcon: Icon(Icons.search_rounded, color: Theme.of(context).primaryColor, size: 28),
            contentPadding: EdgeInsets.zero,
          ),
        ),
      ),
    );
  }

  Widget _buildPromoBanner(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      height: 160,
      width: double.infinity,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(24),
        gradient: const LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [Color(0xFFFF8F00), Color(0xFFFF6D00)],
        ),
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(24),
        child: Stack(
          children: [
            Positioned(
              right: -30,
              top: -30,
              child: Icon(Icons.local_mall_rounded, size: 200, color: Colors.white.withValues(alpha: 0.1)),
            ),
            Padding(
              padding: const EdgeInsets.all(24.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    AppStrings.limitedOffer.tr().toUpperCase(),
                    style: const TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    AppStrings.offerBannerTitle.tr(),
                    style: const TextStyle(color: Colors.white, fontSize: 26, fontWeight: FontWeight.w900),
                  ),
                  Text(
                    AppStrings.offerBannerSubtitle.tr(),
                    style: const TextStyle(color: Colors.white70, fontSize: 16),
                  ),
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
      padding: const EdgeInsets.fromLTRB(16, 24, 16, 16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(title, style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900)),
          TextButton(
            onPressed: onSeeAll,
            child: Text(AppStrings.seeAll.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
          )
        ],
      ),
    );
  }

  Widget _buildCategoryList(BuildContext context) {
    return Consumer<CategoryViewModel>(
      builder: (context, viewModel, child) {
        if (viewModel.categories.isEmpty) {
          return SizedBox(height: 130, child: Center(child: Text(AppStrings.loading.tr())));
        }
        return SizedBox(
          height: 130,
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            scrollDirection: Axis.horizontal,
            itemCount: viewModel.categories.length,
            itemBuilder: (context, index) {
              final cat = viewModel.categories[index];
              return Consumer<ProductViewModel>(
                builder: (context, productVM, child) {
                  bool isSelected = productVM.selectedCategoryId == cat.id;
                  return GestureDetector(
                    onTap: () => productVM.filterByCategory(cat.id),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 10),
                      child: Column(
                        children: [
                          Container(
                            height: 70,
                            width: 70,
                            decoration: BoxDecoration(
                              color: isSelected ? cat.color : cat.color.withValues(alpha: 0.1),
                              borderRadius: BorderRadius.circular(20),
                              border: isSelected ? Border.all(color: Colors.white, width: 2) : null,
                            ),
                            child: Icon(cat.icon, color: isSelected ? Colors.white : cat.color, size: 30),
                          ),
                          const SizedBox(height: 8),
                          Text(
                            cat.name,
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: isSelected ? FontWeight.w900 : FontWeight.bold,
                              color: isSelected ? cat.color : null,
                            ),
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

  Widget _buildFeaturedProducts(BuildContext context) {
    return Consumer<ProductViewModel>(
      builder: (context, viewModel, child) {
        if (viewModel.isLoading && viewModel.featuredProducts.isEmpty) {
          return SizedBox(height: 280, child: Center(child: Text(AppStrings.loading.tr())));
        }
        if (viewModel.featuredProducts.isEmpty) {
          return SizedBox(height: 280, child: Center(child: Text(AppStrings.noProducts.tr())));
        }
        return SizedBox(
          height: 300,
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            scrollDirection: Axis.horizontal,
            itemCount: viewModel.featuredProducts.length,
            itemBuilder: (context, index) {
              final product = viewModel.featuredProducts[index];
              return GestureDetector(
                onTap: () => Navigator.pushNamed(
                  context,
                  AppRoutes.productDetails,
                  arguments: product,
                ),
                child: Container(
                  width: 190,
                  margin: const EdgeInsets.symmetric(horizontal: 10, vertical: 10),
                  decoration: BoxDecoration(
                    color: Theme.of(context).cardColor,
                    borderRadius: BorderRadius.circular(24),
                    boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 10)],
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Expanded(
                        child: Stack(
                          children: [
                            ClipRRect(
                              borderRadius: const BorderRadius.vertical(top: Radius.circular(24)),
                              child: Hero(
                                tag: product.id,
                                child: Image.network(product.imageUrl, width: double.infinity, height: double.infinity, fit: BoxFit.cover),
                              ),
                            ),
                            Positioned(
                              top: 10,
                              right: 10,
                              child: Consumer<WishlistViewModel>(
                                builder: (context, wishlistVM, child) {
                                  final isFav = wishlistVM.isFavorite(product.id);
                                  return GestureDetector(
                                    onTap: () {
                                      final auth = context.read<AuthViewModel>();
                                      if (auth.user != null) {
                                        wishlistVM.toggleWishlist(auth.user!.uid, product.id);
                                      }
                                    },
                                    child: CircleAvatar(
                                      backgroundColor: Colors.white.withValues(alpha: 0.8),
                                      radius: 15,
                                      child: Icon(
                                        isFav ? Icons.favorite : Icons.favorite_border,
                                        color: isFav ? Colors.red : Colors.grey,
                                        size: 18,
                                      ),
                                    ),
                                  );
                                },
                              ),
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(product.name, maxLines: 1, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                            const SizedBox(height: 8),
                            Text(AppStrings.bestQuality.tr(), style: const TextStyle(color: Colors.grey, fontSize: 12)),
                            const SizedBox(height: 8),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text("৳${product.price}", style: TextStyle(color: Theme.of(context).primaryColor, fontWeight: FontWeight.w900, fontSize: 18)),
                                Consumer<CartViewModel>(
                                  builder: (context, cart, child) => GestureDetector(
                                    onTap: () {
                                      cart.addItem(product);
                                      ScaffoldMessenger.of(context).showSnackBar(
                                        SnackBar(content: Text(AppStrings.addedToCart.tr()), duration: const Duration(seconds: 1)),
                                      );
                                    },
                                    child: Container(
                                      padding: const EdgeInsets.all(8),
                                      decoration: BoxDecoration(color: Theme.of(context).primaryColor, borderRadius: BorderRadius.circular(12)),
                                      child: const Icon(Icons.add, color: Colors.white, size: 20),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        );
      },
    );
  }

  Widget _buildDrawer(BuildContext context) {
    final authViewModel = context.watch<AuthViewModel>();
    final settings = context.watch<SettingsViewModel>();

    return Drawer(
      child: Column(
        children: [
          Expanded(
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                DrawerHeader(
                  margin: EdgeInsets.zero,
                  padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                  decoration: BoxDecoration(color: settings.primaryColor),
                  child: Center(
                    child: SingleChildScrollView(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          const CircleAvatar(
                            radius: 35,
                            backgroundColor: Colors.white,
                            child: Icon(Icons.person, size: 45, color: Colors.grey),
                          ),
                          const SizedBox(height: 8),
                          Text(
                            authViewModel.user?.displayName ?? "User",
                            style: const TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                            textAlign: TextAlign.center,
                          ),
                          if (authViewModel.user?.phoneNumber != null && authViewModel.user!.phoneNumber.isNotEmpty)
                            Text(
                              authViewModel.user!.phoneNumber,
                              style: const TextStyle(color: Colors.white70, fontSize: 13),
                              textAlign: TextAlign.center,
                            ),
                          Text(
                            authViewModel.user?.email ?? "",
                            style: const TextStyle(color: Colors.white70, fontSize: 13),
                            textAlign: TextAlign.center,
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
                _buildDrawerItem(context, Icons.dashboard_rounded, AppStrings.homeMenu.tr(), () => Navigator.pop(context), isSelected: true),
                _buildDrawerItem(context, Icons.shopping_bag_rounded, AppStrings.myOrdersMenu.tr(), () {
                  Navigator.pop(context);
                  Navigator.pushNamed(context, AppRoutes.myOrders);
                }),
                _buildDrawerItem(context, Icons.favorite_rounded, AppStrings.wishlistMenu.tr(), () {
                  Navigator.pop(context);
                  Navigator.pushNamed(context, AppRoutes.wishlist);
                }),
                _buildDrawerItem(context, Icons.person_rounded, AppStrings.profileMenu.tr(), () {
                  Navigator.pop(context);
                  Navigator.pushNamed(context, AppRoutes.profile);
                }),
                if (authViewModel.isAdmin)
                  _buildDrawerItem(context, Icons.admin_panel_settings, "Admin Panel", () {
                    Navigator.pop(context);
                    Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminDashboardScreen()));
                  }),
                const Divider(),
                // Language Switcher
                ListTile(
                  leading: const Icon(Icons.language_rounded),
                  title: Text(AppStrings.language.tr()),
                  trailing: DropdownButton<Locale>(
                    value: context.locale,
                    onChanged: (Locale? locale) {
                      if (locale != null) context.setLocale(locale);
                    },
                    items: const [
                      DropdownMenuItem(value: Locale('en', 'US'), child: Text("English")),
                      DropdownMenuItem(value: Locale('bn', 'BD'), child: Text("বাংলা")),
                    ],
                  ),
                ),
                // Dark Mode Toggle
                SwitchListTile(
                  secondary: const Icon(Icons.dark_mode_rounded),
                  title: Text(AppStrings.darkMode.tr()),
                  value: settings.themeMode == ThemeMode.dark,
                  onChanged: (val) => settings.setThemeMode(val ? ThemeMode.dark : ThemeMode.light),
                ),
                // Theme Color Picker
                ListTile(
                  leading: const Icon(Icons.color_lens_rounded),
                  title: Text(AppStrings.themeColor.tr()),
                  subtitle: SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: Row(
                      children: [
                        _colorOption(context, settings, const Color(0xFF1A237E)),
                        _colorOption(context, settings, const Color(0xFFD32F2F)),
                        _colorOption(context, settings, const Color(0xFF388E3C)),
                        _colorOption(context, settings, const Color(0xFFF57C00)),
                        _colorOption(context, settings, const Color(0xFF7B1FA2)),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
          const Divider(),
          ListTile(
            leading: const Icon(Icons.logout, color: Colors.red),
            title: Text(AppStrings.logout.tr(), style: const TextStyle(color: Colors.red)),
            onTap: () => authViewModel.logout(),
          ),
          const SizedBox(height: 10),
        ],
      ),
    );
  }

  Widget _buildDrawerItem(BuildContext context, IconData icon, String title, VoidCallback onTap, {bool isSelected = false}) {
    return ListTile(
      leading: Icon(icon, color: isSelected ? Theme.of(context).primaryColor : null),
      title: Text(title, style: TextStyle(fontWeight: isSelected ? FontWeight.bold : null, color: isSelected ? Theme.of(context).primaryColor : null)),
      onTap: onTap,
      selected: isSelected,
    );
  }

  Widget _colorOption(BuildContext context, SettingsViewModel settings, Color color) {
    bool isSelected = settings.primaryColor.value == color.value;
    return GestureDetector(
      onTap: () => settings.setPrimaryColor(color),
      child: Container(
        margin: const EdgeInsets.only(right: 10, top: 10),
        width: 35,
        height: 35,
        decoration: BoxDecoration(
          color: color,
          shape: BoxShape.circle,
          border: isSelected ? Border.all(color: Theme.of(context).textTheme.bodyLarge?.color ?? Colors.black, width: 2) : null,
        ),
        child: isSelected ? const Icon(Icons.check, color: Colors.white, size: 20) : null,
      ),
    );
  }
}
