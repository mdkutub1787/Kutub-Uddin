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
import '../../view_models/activity_log_view_model.dart';
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
      context.read<OrderViewModel>().fetchAllOrders();
      context.read<SupportViewModel>().fetchAllTickets();
      context.read<UserViewModel>().fetchAllUsers();
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
    final currentUser = context.read<AuthViewModel>().user;
    final isSuperAdmin = currentUser?.role == 'super_admin';
    
    int pendingOrders = orderVM.allOrders.where((o) => o.status == 'Pending').length;

    return Scaffold(
      appBar: CustomAppBar(title: AppStrings.adminControlPanel.tr()),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(vertical: 12),
        child: Column(
          children: [
            _buildAdminHero(context, settings.primaryColor),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 12),
              child: GridView.count(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                crossAxisCount: 2,
                crossAxisSpacing: 12,
                mainAxisSpacing: 12,
                childAspectRatio: 1.1,
                children: [
                  _buildAdminCard(context, AppStrings.products.tr(), Icons.shopping_bag_rounded, Colors.blue, "${productVM.featuredProducts.length} ${AppStrings.pieces.tr()}", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminProductListScreen()))),
                  _buildAdminCard(context, AppStrings.categoriesTitle.tr(), Icons.category_rounded, Colors.orange, "${categoryVM.categories.length} ${AppStrings.items.tr()}", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminCategoryListScreen()))),
                  _buildAdminCard(context, AppStrings.orders.tr(), Icons.receipt_long_rounded, Colors.green, AppStrings.trackSales.tr(), () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminOrderListScreen())), badgeCount: pendingOrders),
                  _buildAdminCard(context, AppStrings.analytics.tr(), Icons.analytics_rounded, Colors.purple, AppStrings.viewReports.tr(), () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminAnalyticsScreen()))),
                  _buildAdminCard(context, AppStrings.notices.tr(), Icons.notification_add_rounded, Colors.red, "${noticeVM.notifications.length} ${AppStrings.active.tr()}", () => Navigator.pushNamed(context, AppRoutes.notifications)),
                  _buildAdminCard(context, AppStrings.support.tr(), Icons.support_agent_rounded, Colors.teal, AppStrings.customerChat.tr(), () => Navigator.pushNamed(context, AppRoutes.support), badgeCount: supportVM.tickets.length),
                  _buildAdminCard(context, AppStrings.users.tr(), Icons.people_alt_rounded, Colors.indigo, "${userVM.users.length} Registered", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminUserListScreen()))),
                  if (isSuperAdmin)
                    _buildAdminCard(context, "Logs", Icons.history_rounded, Colors.blueGrey, "Admin activity", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminActivityLogScreen()))),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildAdminHero(BuildContext context, Color color) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 30),
      decoration: BoxDecoration(color: color, borderRadius: const BorderRadius.vertical(bottom: Radius.circular(32))),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const CircleAvatar(radius: 28, backgroundColor: Colors.white24, child: Icon(Icons.admin_panel_settings_rounded, size: 35, color: Colors.white)),
          const SizedBox(height: 16),
          Text(AppStrings.helloAdmin.tr(), style: const TextStyle(color: Colors.white, fontSize: 22, fontWeight: FontWeight.bold)),
          Text(AppStrings.adminWelcomeMsg.tr(), style: TextStyle(color: Colors.white.withValues(alpha: 0.8), fontSize: 13)),
        ],
      ),
    );
  }

  Widget _buildAdminCard(BuildContext context, String title, IconData icon, Color color, String sub, VoidCallback onTap, {int badgeCount = 0}) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(20),
      child: Stack(
        children: [
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(color: Theme.of(context).cardColor, borderRadius: BorderRadius.circular(20), boxShadow: [BoxShadow(color: color.withValues(alpha: 0.05), blurRadius: 10, offset: const Offset(0, 4))], border: Border.all(color: color.withValues(alpha: 0.08))),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Container(padding: const EdgeInsets.all(8), decoration: BoxDecoration(color: color.withValues(alpha: 0.1), shape: BoxShape.circle), child: Icon(icon, size: 28, color: color)),
                const SizedBox(height: 10),
                Text(title, textAlign: TextAlign.center, style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold), maxLines: 1),
                const SizedBox(height: 2),
                Text(sub, textAlign: TextAlign.center, style: const TextStyle(fontSize: 10, color: Colors.grey), maxLines: 1),
              ],
            ),
          ),
          if (badgeCount > 0)
            Positioned(
              top: 10,
              right: 10,
              child: Container(
                padding: const EdgeInsets.all(5),
                decoration: const BoxDecoration(color: Colors.red, shape: BoxShape.circle),
                constraints: const BoxConstraints(minWidth: 20, minHeight: 20),
                child: Text('$badgeCount', style: const TextStyle(color: Colors.white, fontSize: 9, fontWeight: FontWeight.bold), textAlign: TextAlign.center),
              ),
            ),
        ],
      ),
    );
  }
}
