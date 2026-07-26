import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../category/riverpod/category_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../../core/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../../wishlist/riverpod/wishlist_notifier.dart';
import '../../notification/riverpod/notification_notifier.dart';
import '../../support/riverpod/support_notifier.dart';
import '../../banner/riverpod/banner_notifier.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../widgets/product_card.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/app_card.dart';
import '../../../theme/app_colors.dart';

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
                              backgroundImage: auth?.imageUrl != null && auth!.imageUrl!.isNotEmpty 
                                  ? NetworkImage(auth.imageUrl!) 
                                  : null,
                              child: auth?.imageUrl == null || auth!.imageUrl!.isEmpty
                                  ? Text(userName.isNotEmpty ? userName[0].toUpperCase() : 'U', style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14))
                                  : null,
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
                      Container(
                        decoration: const BoxDecoration(
                          color: Colors.white,
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const SizedBox(height: 10),
                            _buildPromoBanner(context, ref), // Dynamic promo banners
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
          ),
        ],
      ),
    );
  }

  Widget _buildSearchBar(BuildContext context, WidgetRef ref) {
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).primaryColor,
      ),
      padding: const EdgeInsets.fromLTRB(20, 10, 20, 20),
      child: Hero(
          tag: 'search_bar',
          child: Container(
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(20),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withValues(alpha: 0.05),
                  blurRadius: 15,
                  offset: const Offset(0, 5),
                )
              ],
            ),
            child: TextField(
              onChanged: (query) {
                 // ref.read(productNotifierProvider.notifier).searchProducts(query);
              },
              decoration: InputDecoration(
                hintText: AppStrings.searchHint.tr(),
                hintStyle: TextStyle(color: Colors.grey[400], fontSize: 14),
                prefixIcon: Icon(Icons.search_rounded, color: Theme.of(context).primaryColor, size: 22),
                suffixIcon: Container(
                  margin: const EdgeInsets.all(6),
                  padding: const EdgeInsets.all(8),
                  decoration: BoxDecoration(color: Theme.of(context).primaryColor, borderRadius: BorderRadius.circular(12)),
                  child: const Icon(Icons.tune_rounded, color: Colors.white, size: 18),
                ),
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(20), borderSide: BorderSide.none),
                filled: true,
                fillColor: Colors.white,
                contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
              ),
            ),
          ),
        ),
    );
  }


  Widget _buildPromoBanner(BuildContext context, WidgetRef ref) {
    final bannerState = ref.watch(bannerNotifierProvider);

    return bannerState.when(
      data: (banners) {
        if (banners.isEmpty) {
          // Fallback to static if no dynamic banners available
          return _buildStaticBanner(context);
        }

        return SizedBox(
          height: 180,
          child: PageView.builder(
            itemCount: banners.length,
            itemBuilder: (context, index) {
              final banner = banners[index];
              return Container(
                margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(24),
                  image: DecorationImage(
                    image: NetworkImage(banner.imageUrl),
                    fit: BoxFit.cover,
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: Theme.of(context).primaryColor.withValues(alpha: 0.3),
                      blurRadius: 20,
                      offset: const Offset(0, 10),
                    ),
                  ],
                ),
                child: Stack(
                  children: [
                    Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(24),
                        gradient: LinearGradient(
                          colors: [Colors.black.withValues(alpha: 0.7), Colors.transparent],
                          begin: Alignment.centerLeft,
                          end: Alignment.centerRight,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(20.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          if (banner.tag.isNotEmpty)
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                              decoration: BoxDecoration(
                                color: Colors.white24,
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Text(
                                banner.tag,
                                style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold, letterSpacing: 1.5),
                              ),
                            ),
                          const SizedBox(height: 8),
                          Text(
                            banner.title,
                            style: const TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.w900),
                          ),
                          const SizedBox(height: 4),
                          Text(
                            banner.subtitle,
                            style: const TextStyle(color: Colors.white70, fontSize: 12),
                          ),
                          const SizedBox(height: 12),
                          ElevatedButton(
                            onPressed: () {},
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.white,
                              foregroundColor: Theme.of(context).primaryColor,
                              minimumSize: const Size(100, 32),
                              padding: const EdgeInsets.symmetric(horizontal: 16),
                              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                            ),
                            child: Text(banner.actionText, style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              );
            },
          ),
        );
      },
      loading: () => const SizedBox(height: 160, child: Center(child: CircularProgressIndicator())),
      error: (e, st) => _buildStaticBanner(context), // Fallback on error
    );
  }

  Widget _buildStaticBanner(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      height: 180,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(24),
        image: const DecorationImage(
          image: AssetImage('assets/images/promo_banner.png'),
          fit: BoxFit.cover,
        ),
        boxShadow: [
          BoxShadow(
            color: Theme.of(context).primaryColor.withValues(alpha: 0.3),
            blurRadius: 20,
            offset: const Offset(0, 10),
          ),
        ],
      ),
      child: Stack(
        children: [
          Container(
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(24),
              gradient: LinearGradient(
                colors: [Colors.black.withValues(alpha: 0.7), Colors.transparent],
                begin: Alignment.centerLeft,
                end: Alignment.centerRight,
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: Colors.white24,
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: const Text(
                    "MEGA SALE",
                    style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold, letterSpacing: 1.5),
                  ),
                ),
                const SizedBox(height: 8),
                const Text(
                  "Up to 50% OFF",
                  style: TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.w900),
                ),
                const SizedBox(height: 4),
                const Text(
                  "On all premium electronics",
                  style: TextStyle(color: Colors.white70, fontSize: 12),
                ),
                const SizedBox(height: 12),
                ElevatedButton(
                  onPressed: () {},
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.white,
                    foregroundColor: Theme.of(context).primaryColor,
                    minimumSize: const Size(100, 32),
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                  ),
                  child: const Text("Shop Now", style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
                ),
              ],
            ),
          ),
        ],
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
              
              // We'll use the category imageUrl if it exists, otherwise fallback to an icon.
              bool hasImage = cat.imageUrl.isNotEmpty && cat.imageUrl.startsWith('http');
              
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
                          color: isSelected ? Theme.of(context).primaryColor : Colors.white,
                          borderRadius: BorderRadius.circular(18),
                          image: hasImage 
                            ? DecorationImage(image: NetworkImage(cat.imageUrl), fit: BoxFit.cover)
                            : null,
                          boxShadow: [
                            BoxShadow(
                              color: isSelected 
                                ? Theme.of(context).primaryColor.withValues(alpha: 0.2) 
                                : Colors.black.withValues(alpha: 0.03),
                              blurRadius: 10,
                              offset: const Offset(0, 5),
                            )
                          ],
                          border: isSelected ? null : Border.all(color: Colors.grey[100]!),
                        ),
                        child: !hasImage 
                          ? Icon(
                              Icons.category,
                              color: isSelected ? Colors.white : Theme.of(context).primaryColor, 
                              size: 28
                            )
                          : null,
                      ),
                      const SizedBox(height: 6),
                      Text(
                        cat.name.tr(), 
                        style: TextStyle(
                          fontSize: 11, 
                          fontWeight: isSelected ? FontWeight.w900 : FontWeight.bold, 
                          color: isSelected ? Theme.of(context).primaryColor : Colors.grey[600],
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
            currentAccountPicture: CircleAvatar(
              backgroundColor: Colors.white,
              backgroundImage: auth?.imageUrl != null && auth!.imageUrl!.isNotEmpty 
                  ? NetworkImage(auth.imageUrl!) 
                  : null,
              child: auth?.imageUrl == null || auth!.imageUrl!.isEmpty
                  ? const Icon(Icons.person, size: 40, color: Colors.grey)
                  : null,
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
      builder: (ctx) => Dialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
        elevation: 0,
        backgroundColor: Colors.transparent,
        child: Container(
          padding: const EdgeInsets.all(24),
          decoration: BoxDecoration(
            color: Colors.white,
            shape: BoxShape.rectangle,
            borderRadius: BorderRadius.circular(24),
            boxShadow: const [
              BoxShadow(color: Colors.black26, blurRadius: 10.0, offset: Offset(0.0, 10.0)),
            ],
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.red.withValues(alpha: 0.1),
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.logout_rounded, color: Colors.red, size: 40),
              ),
              const SizedBox(height: 24),
              Text(
                AppStrings.logout.tr(),
                style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900, letterSpacing: -0.5),
              ),
              const SizedBox(height: 8),
              Text(
                AppStrings.logoutConfirm.tr(),
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 14, color: Colors.grey[600]),
              ),
              const SizedBox(height: 32),
              Row(
                children: [
                  Expanded(
                    child: OutlinedButton(
                      onPressed: () => Navigator.pop(ctx),
                      style: OutlinedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        side: BorderSide(color: Colors.grey[300]!),
                      ),
                      child: Text(AppStrings.cancel.tr(), style: const TextStyle(color: Colors.black87, fontWeight: FontWeight.bold)),
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: ElevatedButton(
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
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        elevation: 0,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      child: Text(AppStrings.logout.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
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
