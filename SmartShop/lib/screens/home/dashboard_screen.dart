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
    
    return Scaffold(
      drawer: _buildDrawer(context),
      body: RefreshIndicator(
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
              expandedHeight: 60,
              titleWidget: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    AppStrings.appName.tr(),
                    style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w500, color: Colors.white70),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  Text(
                    "Hello, ${auth.user?.name ?? 'Guest'}!",
                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.white),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
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
                  _buildFeaturedProducts(context, settings),
                  _buildCategorySections(context, settings),
                  const SizedBox(height: 100),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSearchBar(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 20, 16, 16),
      child: AppCard(
        elevation: 6,
        borderRadius: 20,
        border: Border.all(
          color: Theme.of(context).primaryColor.withValues(alpha: 0.3), 
          width: 2
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
            contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 15),
            fillColor: Theme.of(context).cardColor,
            filled: true,
          ),
        ),
      ),
    );
  }

  Widget _buildPromoBanner(BuildContext context) {
    return SizedBox(
      height: 180,
      child: PageView(
        children: [
          _promoItem(
            context,
            "SUMMER SALE",
            "Get up to 50% OFF",
            "On all electronics",
            const Color(0xFF1A237E),
            const Color(0xFF3949AB),
            Icons.flash_on_rounded,
          ),
          _promoItem(
            context,
            "NEW ARRIVAL",
            "Exclusive Collection",
            "Fresh stock available",
            const Color(0xFFD32F2F),
            const Color(0xFFEF5350),
            Icons.new_releases_rounded,
          ),
          _promoItem(
            context,
            "FREE SHIPPING",
            "On orders over ৳1000",
            "Limited time offer",
            const Color(0xFF388E3C),
            const Color(0xFF66BB6A),
            Icons.local_shipping_rounded,
          ),
        ],
      ),
    );
  }

  Widget _promoItem(
    BuildContext context,
    String tag,
    String title,
    String sub,
    Color color1,
    Color color2,
    IconData icon,
  ) {
    return AppCard(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      borderRadius: 24,
      elevation: 8,
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(24),
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [color1, color2],
          ),
        ),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(24),
          child: Stack(
            children: [
              Positioned(
                right: -20,
                top: -20,
                child: Icon(icon, size: 150, color: Colors.white.withValues(alpha: 0.1)),
              ),
              Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                      decoration: BoxDecoration(
                        color: Colors.white.withValues(alpha: 0.2),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Text(
                        tag,
                        style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
                      ),
                    ),
                    const SizedBox(height: 12),
                    Text(
                      title,
                      style: const TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.w900),
                    ),
                    Text(
                      sub,
                      style: const TextStyle(color: Colors.white70, fontSize: 14),
                    ),
                  ],
                ),
              )
            ],
          ),
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
          Expanded(
            child: Text(
              title, 
              style: const TextStyle(fontSize: 20, fontWeight: FontWeight.w900),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ),
          AppCard(
            borderRadius: 12,
            elevation: 2,
            onTap: onSeeAll,
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
              child: Text(
                AppStrings.seeAll.tr(), 
                style: TextStyle(
                  fontWeight: FontWeight.bold, 
                  color: Theme.of(context).primaryColor,
                  fontSize: 12,
                ),
              ),
            ),
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
                          AppCard(
                            width: 70,
                            height: 70,
                            borderRadius: 20,
                            elevation: isSelected ? 8 : 2,
                            color: isSelected ? cat.color : Theme.of(context).cardColor,
                            border: isSelected 
                                ? Border.all(color: Colors.white, width: 2) 
                                : Border.all(color: cat.color.withValues(alpha: 0.1), width: 1),
                            onTap: () => productVM.filterByCategory(cat.id),
                            child: Icon(
                              cat.icon, 
                              color: isSelected ? Colors.white : cat.color, 
                              size: 30
                            ),
                          ),
                          const SizedBox(height: 8),
                          Text(
                            cat.name,
                            style: TextStyle(
                              fontSize: 13,
                              fontWeight: isSelected ? FontWeight.w900 : FontWeight.bold,
                              color: isSelected ? cat.color : null,
                            ),
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
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
        if (viewModel.isLoading && viewModel.featuredProducts.isEmpty) {
          return const SizedBox(height: 280, child: Center(child: CircularProgressIndicator()));
        }
        if (viewModel.featuredProducts.isEmpty) {
          return const EmptyStateWidget(
            icon: Icons.inventory_2_outlined,
            title: "No Products",
            subtitle: "Check back later for new arrivals!",
          );
        }
        return SizedBox(
          height: 310,
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            scrollDirection: Axis.horizontal,
            itemCount: viewModel.featuredProducts.length,
            itemBuilder: (context, index) {
              return ProductCard(product: viewModel.featuredProducts[index]);
            },
          ),
        );
      },
    );
  }

  Widget _buildCategorySections(BuildContext context, SettingsViewModel settings) {
    return Consumer2<CategoryViewModel, ProductViewModel>(
      builder: (context, catVM, prodVM, child) {
        if (catVM.categories.isEmpty) return const SizedBox.shrink();

        return Column(
          children: catVM.categories.take(5).map((category) {
            final categoryProducts = prodVM.featuredProducts.where((p) => p.categoryId == category.id).toList();
            
            if (categoryProducts.isEmpty) return const SizedBox.shrink();

            return Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _buildSectionHeader(context, category.name, () {
                  prodVM.filterByCategory(category.id);
                }),
                SizedBox(
                  height: 310,
                  child: ListView.builder(
                    padding: const EdgeInsets.symmetric(horizontal: 12),
                    scrollDirection: Axis.horizontal,
                    itemCount: categoryProducts.length,
                    itemBuilder: (context, index) {
                      return ProductCard(product: categoryProducts[index]);
                    },
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
                            authViewModel.user?.name ?? "User",
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
                _buildDrawerItem(context, Icons.support_agent_rounded, AppStrings.help.tr(), () {
                  Navigator.pop(context);
                  // Add help functionality here if needed
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
                Padding(
                  padding: const EdgeInsets.only(left: 16, top: 10),
                  child: Row(
                    children: [
                      const Icon(Icons.color_lens_rounded, color: Colors.grey),
                      const SizedBox(width: 32),
                      Text(AppStrings.themeColor.tr(), style: const TextStyle(fontSize: 16)),
                    ],
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(left: 64, bottom: 10),
                  child: SingleChildScrollView(
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
