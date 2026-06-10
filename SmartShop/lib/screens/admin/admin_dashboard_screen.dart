import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../view_models/settings_view_model.dart';
import '../../view_models/product_view_model.dart';
import '../../view_models/category_view_model.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/notification_view_model.dart';
import '../../view_models/support_view_model.dart';
import '../../view_models/user_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../routes/app_routes.dart';
import '../../utils/constants/app_strings.dart';
import '../../widgets/custom_app_bar.dart';
import 'admin_product_list_screen.dart';
import 'admin_category_list_screen.dart';
import 'admin_order_list_screen.dart';
import 'admin_analytics_screen.dart';
import 'admin_user_list_screen.dart';
import 'admin_activity_log_screen.dart';

class AdminDashboardScreen extends StatefulWidget {
  const AdminDashboardScreen({super.key});

  @override
  State<AdminDashboardScreen> createState() => _AdminDashboardScreenState();
}

class _AdminDashboardScreenState extends State<AdminDashboardScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final user = context.read<AuthViewModel>().user;
      final shopId = user?.shopId;
      
      context.read<OrderViewModel>().fetchAllOrders(shopId: shopId);
      context.read<SupportViewModel>().fetchAllTickets(); // Consider shopId for support too
      context.read<UserViewModel>().fetchAllUsers();
      context.read<ProductViewModel>().initStream(shopId: shopId);
      context.read<CategoryViewModel>().fetchCategories(shopId: shopId);
    });
  }

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final productVM = context.watch<ProductViewModel>();
    final categoryVM = context.watch<CategoryViewModel>();
    final orderVM = context.watch<OrderViewModel>();
    final noticeVM = context.watch<NotificationViewModel>();
    final supportVM = context.watch<SupportViewModel>();
    final userVM = context.watch<UserViewModel>();
    final authVM = context.watch<AuthViewModel>();
    final currentUser = authVM.user;
    final isSuperAdmin = currentUser?.role == 'super_admin';
    
    // Dynamic configurations
    final bool isOnlineOrderEnabled = true; 
    final bool isPosEnabled = true; 
    
    int pendingOrders = orderVM.allOrders.where((o) => o.status == 'Pending').length;

    return Scaffold(
      appBar: CustomAppBar(title: AppStrings.adminControlPanel.tr()),
      body: RefreshIndicator(
        onRefresh: () async {
          final shopId = currentUser?.shopId;
          context.read<OrderViewModel>().fetchAllOrders(shopId: shopId);
          context.read<UserViewModel>().fetchAllUsers();
          context.read<ProductViewModel>().fetchFeaturedProducts(shopId: shopId);
          context.read<CategoryViewModel>().refreshCategories(shopId: shopId);
        },
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: const EdgeInsets.symmetric(vertical: 12),
          child: Column(
            children: [
              _buildAdminHero(context, settings.primaryColor, currentUser?.name ?? "Admin", isSuperAdmin),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text("Quick Management", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.grey[800])),
                    const SizedBox(height: 12),
                    GridView.count(
                      shrinkWrap: true,
                      physics: const NeverScrollableScrollPhysics(),
                      crossAxisCount: 2,
                      crossAxisSpacing: 12,
                      mainAxisSpacing: 12,
                      childAspectRatio: 1.25,
                      children: [
                        _buildAdminCard(context, AppStrings.products.tr(), Icons.inventory_2_rounded, Colors.blue, "${productVM.featuredProducts.length} ${AppStrings.pieces.tr()}", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminProductListScreen()))),
                        _buildAdminCard(context, AppStrings.categoriesTitle.tr(), Icons.category_rounded, Colors.orange, "${categoryVM.categories.length} ${AppStrings.items.tr()}", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminCategoryListScreen()))),
                        
                        if (isPosEnabled)
                          _buildAdminCard(context, "POS Sell", Icons.point_of_sale_rounded, Colors.teal, "Direct Store Sale", () {
                            // TODO: Navigate to POS Screen
                            ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("POS System Coming Soon!")));
                          }),
                        
                        if (isOnlineOrderEnabled)
                          _buildAdminCard(context, AppStrings.orders.tr(), Icons.receipt_long_rounded, Colors.green, AppStrings.trackSales.tr(), () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminOrderListScreen())), badgeCount: pendingOrders),
                        
                        _buildAdminCard(context, AppStrings.analytics.tr(), Icons.analytics_rounded, Colors.purple, AppStrings.viewReports.tr(), () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminAnalyticsScreen()))),
                        _buildAdminCard(context, AppStrings.notices.tr(), Icons.notification_add_rounded, Colors.red, "${noticeVM.notifications.length} ${AppStrings.active.tr()}", () => Navigator.pushNamed(context, AppRoutes.notifications)),
                        _buildAdminCard(context, AppStrings.support.tr(), Icons.support_agent_rounded, Colors.cyan, AppStrings.customerChat.tr(), () => Navigator.pushNamed(context, AppRoutes.support), badgeCount: supportVM.adminUnreadCount),
                        
                        if (isSuperAdmin) ...[
                          _buildAdminCard(context, AppStrings.users.tr(), Icons.people_alt_rounded, Colors.indigo, "${userVM.users.length} Users", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminUserListScreen()))),
                          _buildAdminCard(context, "Activity Logs", Icons.history_rounded, Colors.blueGrey, "System audit", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminActivityLogScreen()))),
                        ],
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

  Widget _buildAdminHero(BuildContext context, Color color, String userName, bool isSuperAdmin) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(20, 15, 20, 25),
      decoration: BoxDecoration(color: color, borderRadius: const BorderRadius.vertical(bottom: Radius.circular(30))),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const CircleAvatar(radius: 24, backgroundColor: Colors.white24, child: Icon(Icons.admin_panel_settings_rounded, size: 30, color: Colors.white)),
          const SizedBox(height: 12),
          Text(
            isSuperAdmin ? "Hello, $userName (Super Admin)" : "Hello, $userName",
            style: const TextStyle(color: Colors.white, fontSize: 20, fontWeight: FontWeight.bold)
          ),
          Text(AppStrings.adminWelcomeMsg.tr(), style: TextStyle(color: Colors.white.withValues(alpha: 0.8), fontSize: 12)),
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
