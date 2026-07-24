import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../product/riverpod/product_notifier.dart';
import '../../category/riverpod/category_notifier.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../notification/riverpod/notification_notifier.dart';
import '../../support/riverpod/support_notifier.dart';
import '../../user/riverpod/user_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../routes/app_routes.dart';
import '../../../utils/constants/app_strings.dart';
import '../../../widgets/custom_app_bar.dart';
import 'admin_product_list_screen.dart';
import 'admin_category_list_screen.dart';
import 'admin_order_list_screen.dart';
import 'admin_analytics_screen.dart';
import 'admin_user_list_screen.dart';
import 'admin_activity_log_screen.dart';
import 'admin_pos_screen.dart';

class AdminDashboardScreen extends ConsumerStatefulWidget {
  const AdminDashboardScreen({super.key});

  @override
  ConsumerState<AdminDashboardScreen> createState() => _AdminDashboardScreenState();
}

class _AdminDashboardScreenState extends ConsumerState<AdminDashboardScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(orderNotifierProvider.notifier).loadOrders();
      ref.read(userNotifierProvider.notifier).loadUsers();
      ref.read(categoryNotifierProvider.notifier).loadCategories();
    });
  }

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    final productState = ref.watch(productNotifierProvider);
    final categoryState = ref.watch(categoryNotifierProvider);
    final orderState = ref.watch(orderNotifierProvider);
    final noticeState = ref.watch(notificationNotifierProvider);
    final supportState = ref.watch(supportNotifierProvider);
    final userState = ref.watch(userNotifierProvider);
    final authState = ref.watch(authNotifierProvider);
    
    final currentUser = authState.value;
    final isSuperAdmin = currentUser?.role == 'super_admin';
    final isOwner = currentUser?.role == 'owner' || isSuperAdmin;
    final isManager = currentUser?.role == 'manager' || isOwner;
    final isStaff = currentUser?.role == 'staff' || isManager;
    
    final orders = orderState.value ?? [];
    final pendingOrders = orders.where((o) => o.status == 'Pending').length;

    final products = productState.featuredProducts ?? [];
    final categories = categoryState.value ?? [];
    final users = userState.value ?? [];

    return Scaffold(
      appBar: CustomAppBar(title: AppStrings.adminControlPanel.tr()),
      body: RefreshIndicator(
        onRefresh: () async {
          await ref.read(orderNotifierProvider.notifier).loadOrders();
          await ref.read(userNotifierProvider.notifier).loadUsers();
          await ref.read(categoryNotifierProvider.notifier).loadCategories();
        },
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: const EdgeInsets.symmetric(vertical: 12),
          child: Column(
            children: [
              _buildAdminHero(context, settings.primaryColor, currentUser?.name ?? "Admin", currentUser?.role ?? "Staff"),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text("Management Console", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.grey[800])),
                    const SizedBox(height: 12),
                    GridView.count(
                      shrinkWrap: true,
                      physics: const NeverScrollableScrollPhysics(),
                      crossAxisCount: 2,
                      crossAxisSpacing: 12,
                      mainAxisSpacing: 12,
                      childAspectRatio: 1.25,
                      children: [
                        if (isStaff)
                          _buildAdminCard(context, "POS Sell", Icons.point_of_sale_rounded, Colors.teal, "Direct Store Sale", () {
                            Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminPosScreen()));
                          }),
                        
                        if (isStaff)
                          _buildAdminCard(context, AppStrings.products.tr(), Icons.inventory_2_rounded, Colors.blue, "${products.length} ${AppStrings.pieces.tr()}", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminProductListScreen()))),
                        
                        if (isManager)
                          _buildAdminCard(context, AppStrings.categoriesTitle.tr(), Icons.category_rounded, Colors.orange, "${categories.length} ${AppStrings.items.tr()}", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminCategoryListScreen()))),
                        
                        if (isStaff)
                          _buildAdminCard(context, AppStrings.orders.tr(), Icons.receipt_long_rounded, Colors.green, AppStrings.trackSales.tr(), () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminOrderListScreen())), badgeCount: pendingOrders),
                        
                        if (isManager)
                          _buildAdminCard(context, AppStrings.analytics.tr(), Icons.analytics_rounded, Colors.purple, AppStrings.viewReports.tr(), () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminAnalyticsScreen()))),
                        
                        if (isOwner)
                          _buildAdminCard(context, AppStrings.notices.tr(), Icons.notification_add_rounded, Colors.red, "${noticeState.value?.length ?? 0} ${AppStrings.active.tr()}", () => Navigator.pushNamed(context, AppRoutes.notifications)),
                        
                        if (isManager)
                          _buildAdminCard(context, AppStrings.support.tr(), Icons.support_agent_rounded, Colors.cyan, AppStrings.customerChat.tr(), () => Navigator.pushNamed(context, AppRoutes.support), badgeCount: supportState.value?.length ?? 0),
                        
                        if (isOwner)
                          _buildAdminCard(context, AppStrings.users.tr(), Icons.people_alt_rounded, Colors.indigo, "${users.length} Team Members", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminUserListScreen()))),
                        
                        if (isSuperAdmin)
                          _buildAdminCard(context, "Activity Logs", Icons.history_rounded, Colors.blueGrey, "System audit", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminActivityLogScreen()))),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildAdminHero(BuildContext context, Color color, String userName, String role) {
    final user = ref.read(authNotifierProvider).value;
    final shopId = user?.shopId;

    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(20, 15, 20, 25),
      decoration: BoxDecoration(
        color: color, 
        borderRadius: const BorderRadius.vertical(bottom: Radius.circular(35)),
        boxShadow: [BoxShadow(color: color.withValues(alpha: 0.3), blurRadius: 15, offset: const Offset(0, 8))],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const CircleAvatar(radius: 24, backgroundColor: Colors.white24, child: Icon(Icons.admin_panel_settings_rounded, size: 30, color: Colors.white)),
              if (shopId != null)
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
                  decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(15)),
                  child: Row(
                    children: [
                      Icon(Icons.storefront_rounded, size: 16, color: color),
                      const SizedBox(width: 8),
                      Text("My Shop", style: TextStyle(color: color, fontWeight: FontWeight.w900, fontSize: 13, letterSpacing: 0.5)),
                    ],
                  ),
                ),
            ],
          ),
          const SizedBox(height: 20),
          Text(
            "Hello, $userName",
            style: const TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.w900, letterSpacing: -0.5)
          ),
          Container(
            margin: const EdgeInsets.only(top: 4),
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
            decoration: BoxDecoration(color: Colors.white24, borderRadius: BorderRadius.circular(20)),
            child: Text(
              role.replaceAll('_', ' ').toUpperCase(), 
              style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.w900, letterSpacing: 1.5)
            ),
          ),
          const SizedBox(height: 8),
          Text(AppStrings.adminWelcomeMsg.tr(), style: TextStyle(color: Colors.white.withValues(alpha: 0.8), fontSize: 13)),
        ],
      ),
    );
  }

  Widget _buildAdminCard(BuildContext context, String title, IconData icon, Color color, String sub, VoidCallback onTap, {int badgeCount = 0}) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(16),
      child: Stack(
        clipBehavior: Clip.none,
        children: [
          Container(
            width: double.infinity,
            padding: const EdgeInsets.symmetric(horizontal: 12),
            decoration: BoxDecoration(
              color: Theme.of(context).cardColor, 
              borderRadius: BorderRadius.circular(16), 
              boxShadow: [
                BoxShadow(color: Colors.black.withValues(alpha: 0.02), blurRadius: 15, offset: const Offset(0, 8))
              ], 
              border: Border.all(color: color.withValues(alpha: 0.05), width: 1.5)
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Container(
                  padding: const EdgeInsets.all(6), 
                  decoration: BoxDecoration(color: color.withValues(alpha: 0.08), shape: BoxShape.circle), 
                  child: Icon(icon, size: 22, color: color)
                ),
                const SizedBox(height: 8),
                Text(title, textAlign: TextAlign.center, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold), maxLines: 1),
                const SizedBox(height: 2),
                Text(sub, textAlign: TextAlign.center, style: const TextStyle(fontSize: 9, color: Colors.grey), maxLines: 1),
              ],
            ),
          ),
          if (badgeCount > 0)
            Positioned(
              top: -4,
              right: -4,
              child: Container(
                padding: const EdgeInsets.all(4), 
                decoration: BoxDecoration(color: Colors.red, shape: BoxShape.circle, border: Border.all(color: Colors.white, width: 2)), 
                constraints: const BoxConstraints(minWidth: 20, minHeight: 20), 
                child: Text('$badgeCount', style: const TextStyle(color: Colors.white, fontSize: 8, fontWeight: FontWeight.bold), textAlign: TextAlign.center)
              ),
            ),
        ],
      ),
    );
  }
}
