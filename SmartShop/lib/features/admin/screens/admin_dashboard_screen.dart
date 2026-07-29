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
import '../riverpod/activity_log_notifier.dart';
import '../../../routes/app_routes.dart';
import '../../../core/app_strings.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../core/utils/exit_dialog_helper.dart';
import 'admin_product_list_screen.dart';
import 'admin_category_list_screen.dart';
import 'admin_order_list_screen.dart';
import 'admin_analytics_screen.dart';
import 'admin_user_list_screen.dart';
import 'admin_activity_log_screen.dart';
import 'coupons/admin_coupon_screen.dart';
import 'admin_pos_screen.dart';
import 'admin_shop_list_screen.dart';
import '../../../core/riverpod/admin_shop_filter_notifier.dart';
import 'package:fl_chart/fl_chart.dart';
import '../../order/models/order_model.dart';
import '../../shop/riverpod/shop_notifier.dart';

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
      ref.read(activityLogNotifierProvider.notifier).loadLogs();
      ref.read(notificationNotifierProvider.notifier).loadNotifications();
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
    
    // Inventory Health Check
    final lowStockItems = products.where((p) => p.stock <= 5).length;

    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (didPop) return;
        await ExitDialogHelper.showExitDialog(context);
      },
      child: Scaffold(
        appBar: CustomAppBar(
          title: AppStrings.adminControlPanel.tr(),
          actions: [
            GestureDetector(
              onTap: () => Navigator.pushNamed(context, AppRoutes.profile),
              child: Padding(
                padding: const EdgeInsets.only(right: 16.0),
                child: CircleAvatar(
                  radius: 16,
                  backgroundColor: Colors.white24,
                  backgroundImage: currentUser?.imageUrl != null && currentUser!.imageUrl!.isNotEmpty
                      ? NetworkImage(currentUser.imageUrl!)
                      : null,
                  child: currentUser?.imageUrl == null || currentUser!.imageUrl!.isEmpty
                      ? const Icon(Icons.person_rounded, size: 20, color: Colors.white)
                      : null,
                ),
              ),
            ),
          ],
        ),
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
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text("Management Console", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.grey[800])),
                        if (ref.watch(adminShopFilterProvider) != null)
                          TextButton.icon(
                            icon: const Icon(Icons.clear_rounded, size: 16),
                            label: const Text("Clear Shop Filter", style: TextStyle(fontSize: 12)),
                            style: TextButton.styleFrom(foregroundColor: Colors.red, padding: EdgeInsets.zero, minimumSize: Size.zero),
                            onPressed: () {
                              ref.read(adminShopFilterProvider.notifier).state = null;
                              ref.read(adminShopFilterNameProvider.notifier).state = null;
                            },
                          ),
                      ],
                    ),
                    if (ref.watch(adminShopFilterNameProvider) != null)
                      Padding(
                        padding: const EdgeInsets.only(top: 4.0),
                        child: Text("Viewing: ${ref.watch(adminShopFilterNameProvider)}", style: const TextStyle(color: Colors.blue, fontWeight: FontWeight.bold)),
                      ),
                    const SizedBox(height: 12),
                    if (orders.isNotEmpty) _buildOrderPieChart(orders, settings.primaryColor),
                    const SizedBox(height: 16),
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
                          _buildAdminCard(context, AppStrings.products.tr(), Icons.inventory_2_rounded, Colors.blue, "${products.length} ${AppStrings.pieces.tr()}", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminProductListScreen())), badgeCount: lowStockItems),
                        
                        if (isManager)
                          _buildAdminCard(context, AppStrings.categoriesTitle.tr(), Icons.category_rounded, Colors.orange, "${categories.length} ${AppStrings.items.tr()}", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminCategoryListScreen()))),
                        
                        if (isStaff)
                          _buildAdminCard(context, AppStrings.orders.tr(), Icons.receipt_long_rounded, Colors.green, AppStrings.trackSales.tr(), () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminOrderListScreen())), badgeCount: pendingOrders),
                        
                        if (isManager)
                          _buildAdminCard(context, AppStrings.analytics.tr(), Icons.analytics_rounded, Colors.purple, AppStrings.viewReports.tr(), () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminAnalyticsScreen()))),
                        
                        if (isSuperAdmin)
                          _buildAdminCard(context, AppStrings.notices.tr(), Icons.notification_add_rounded, Colors.red, "${noticeState.value?.length ?? 0} ${AppStrings.active.tr()}", () => Navigator.pushNamed(context, AppRoutes.notifications)),
                        
                        if (isManager)
                          _buildAdminCard(context, AppStrings.support.tr(), Icons.support_agent_rounded, Colors.cyan, AppStrings.customerChat.tr(), () => Navigator.pushNamed(context, AppRoutes.support), badgeCount: supportState.value?.length ?? 0),
                        
                        if (isSuperAdmin)
                          _buildAdminCard(context, AppStrings.users.tr(), Icons.people_alt_rounded, Colors.indigo, "${users.length} Team Members", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminUserListScreen()))),
                        
                        if (isOwner)
                          _buildAdminCard(context, "Coupons", Icons.confirmation_num_rounded, Colors.pink, "Manage discounts", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminCouponScreen()))),
                        
                        if (isSuperAdmin)
                          _buildAdminCard(context, "Manage Shops", Icons.storefront_rounded, Colors.deepPurple, "View & Manage Shops", () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminShopListScreen()))),
                          
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
    ));
  }

  Widget _buildAdminHero(BuildContext context, Color color, String userName, String role) {
    final user = ref.watch(authNotifierProvider).value;
    final shopId = user?.shopId;
    
    String shopName = "My Shop";
    if (shopId != null) {
      final shopsState = ref.watch(shopNotifierProvider);
      if (shopsState.value != null) {
        try {
          final shop = shopsState.value!.firstWhere((s) => s.id == shopId);
          shopName = shop.name;
        } catch (_) {}
      }
    }

    return Container(
      width: double.infinity,
      margin: const EdgeInsets.symmetric(horizontal: 16),
      padding: const EdgeInsets.fromLTRB(25, 25, 25, 30),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [color, color.withValues(alpha: 0.8)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(36),
        boxShadow: [
          BoxShadow(color: color.withValues(alpha: 0.3), blurRadius: 25, offset: const Offset(0, 15))
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Colors.white.withValues(alpha: 0.15),
                  shape: BoxShape.circle,
                  border: Border.all(color: Colors.white.withValues(alpha: 0.3), width: 2),
                ),
                child: const Icon(Icons.admin_panel_settings_rounded, size: 30, color: Colors.white),
              ),
              if (shopId != null)
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(20),
                    boxShadow: [
                      BoxShadow(color: Colors.black.withValues(alpha: 0.1), blurRadius: 10)
                    ],
                  ),
                  child: Row(
                    children: [
                      Icon(Icons.storefront_rounded, size: 16, color: color),
                      const SizedBox(width: 8),
                      Text(shopName, style: TextStyle(color: color, fontWeight: FontWeight.w900, fontSize: 13, letterSpacing: 0.5)),
                    ],
                  ),
                ),
            ],
          ),
          const SizedBox(height: 30),
          Text(
            "Hello, $userName",
            style: const TextStyle(color: Colors.white, fontSize: 26, fontWeight: FontWeight.w900, letterSpacing: -0.5)
          ),
          Container(
            margin: const EdgeInsets.only(top: 8),
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            decoration: BoxDecoration(
              color: Colors.white24,
              borderRadius: BorderRadius.circular(20),
            ),
            child: Text(
              role.replaceAll('_', ' ').toUpperCase(), 
              style: const TextStyle(color: Colors.white, fontSize: 11, fontWeight: FontWeight.w900, letterSpacing: 2)
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildAdminCard(BuildContext context, String title, IconData icon, Color color, String sub, VoidCallback onTap, {int badgeCount = 0}) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(24),
      child: Stack(
        clipBehavior: Clip.none,
        children: [
          Container(
            width: double.infinity,
            padding: const EdgeInsets.symmetric(horizontal: 12),
            decoration: BoxDecoration(
              color: Colors.white, 
              borderRadius: BorderRadius.circular(24), 
              boxShadow: [
                BoxShadow(color: color.withValues(alpha: 0.1), blurRadius: 20, offset: const Offset(0, 10))
              ], 
              border: Border.all(color: color.withValues(alpha: 0.05), width: 2)
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Container(
                  padding: const EdgeInsets.all(10), 
                  decoration: BoxDecoration(
                    color: color.withValues(alpha: 0.1), 
                    shape: BoxShape.circle,
                  ), 
                  child: Icon(icon, size: 26, color: color)
                ),
                const SizedBox(height: 12),
                Text(title, textAlign: TextAlign.center, style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w900, color: Colors.black87), maxLines: 1),
                const SizedBox(height: 4),
                Text(sub, textAlign: TextAlign.center, style: TextStyle(fontSize: 10, color: Colors.grey[600], fontWeight: FontWeight.bold), maxLines: 1),
              ],
            ),
          ),
          if (badgeCount > 0)
            Positioned(
              top: -6,
              right: -6,
              child: Container(
                padding: const EdgeInsets.all(6), 
                decoration: BoxDecoration(color: Colors.red, shape: BoxShape.circle, border: Border.all(color: Colors.white, width: 3)), 
                constraints: const BoxConstraints(minWidth: 26, minHeight: 26), 
                child: Text('$badgeCount', style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold), textAlign: TextAlign.center)
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildOrderPieChart(List<OrderModel> orders, Color primaryColor) {
    int pending = 0;
    int processing = 0;
    int outForDelivery = 0;
    int delivered = 0;
    int cancelled = 0;

    for (var o in orders) {
      if (o.status == 'Pending') pending++;
      else if (o.status == 'Processing') processing++;
      else if (o.status == 'Out for Delivery') outForDelivery++;
      else if (o.status == 'Delivered') delivered++;
      else if (o.status == 'Cancelled') cancelled++;
    }

    final total = orders.length;

    List<PieChartSectionData> sections = [];
    if (pending > 0) {
      sections.add(PieChartSectionData(
        value: pending.toDouble(),
        title: '${(pending / total * 100).toStringAsFixed(0)}%',
        color: Colors.orange,
        radius: 40,
        titleStyle: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Colors.white),
      ));
    }
    if (processing > 0) {
      sections.add(PieChartSectionData(
        value: processing.toDouble(),
        title: '${(processing / total * 100).toStringAsFixed(0)}%',
        color: Colors.blue,
        radius: 40,
        titleStyle: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Colors.white),
      ));
    }
    if (outForDelivery > 0) {
      sections.add(PieChartSectionData(
        value: outForDelivery.toDouble(),
        title: '${(outForDelivery / total * 100).toStringAsFixed(0)}%',
        color: Colors.purple,
        radius: 40,
        titleStyle: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Colors.white),
      ));
    }
    if (delivered > 0) {
      sections.add(PieChartSectionData(
        value: delivered.toDouble(),
        title: '${(delivered / total * 100).toStringAsFixed(0)}%',
        color: Colors.green,
        radius: 45,
        titleStyle: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.white),
      ));
    }
    if (cancelled > 0) {
      sections.add(PieChartSectionData(
        value: cancelled.toDouble(),
        title: '${(cancelled / total * 100).toStringAsFixed(0)}%',
        color: Colors.red,
        radius: 35,
        titleStyle: const TextStyle(fontSize: 9, fontWeight: FontWeight.bold, color: Colors.white),
      ));
    }

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.04),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
        border: Border.all(color: Colors.grey.withValues(alpha: 0.1)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text("Order Analytics", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.grey[800])),
          const SizedBox(height: 16),
          SizedBox(
            height: 150,
            child: Row(
              children: [
                Expanded(
                  flex: 1,
                  child: PieChart(
                    PieChartData(
                      sectionsSpace: 2,
                      centerSpaceRadius: 30,
                      sections: sections.isEmpty ? [PieChartSectionData(value: 1, color: Colors.grey[200]!, title: '')] : sections,
                    ),
                  ),
                ),
                const SizedBox(width: 16),
                Expanded(
                  flex: 1,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildLegend("Pending", Colors.orange, pending),
                      _buildLegend("Processing", Colors.blue, processing),
                      _buildLegend("Out for Delivery", Colors.purple, outForDelivery),
                      _buildLegend("Delivered", Colors.green, delivered),
                      _buildLegend("Cancelled", Colors.red, cancelled),
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

  Widget _buildLegend(String title, Color color, int count) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 4.0),
      child: Row(
        children: [
          Container(width: 10, height: 10, decoration: BoxDecoration(color: color, shape: BoxShape.circle)),
          const SizedBox(width: 6),
          Expanded(child: Text(title, style: TextStyle(fontSize: 11, color: Colors.grey[700]), overflow: TextOverflow.ellipsis)),
          Text(count.toString(), style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }
}
