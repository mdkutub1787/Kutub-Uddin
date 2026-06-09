import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../view_models/settings_view_model.dart';
import '../../widgets/custom_app_bar.dart';
import 'admin_product_list_screen.dart';
import 'admin_category_list_screen.dart';
import 'admin_order_list_screen.dart';
import 'admin_analytics_screen.dart';

class AdminDashboardScreen extends StatelessWidget {
  const AdminDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    
    return Scaffold(
      appBar: const CustomAppBar(
        title: "Control Panel",
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            _buildAdminHero(context, settings.primaryColor),
            Padding(
              padding: const EdgeInsets.all(20.0),
              child: GridView.count(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                crossAxisCount: 2,
                crossAxisSpacing: 20,
                mainAxisSpacing: 20,
                children: [
                  _buildAdminCard(
                    context,
                    "Products",
                    Icons.shopping_bag_rounded,
                    Colors.blue,
                    "Manage inventory",
                    () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminProductListScreen())),
                  ),
                  _buildAdminCard(
                    context,
                    "Categories",
                    Icons.category_rounded,
                    Colors.orange,
                    "Organize items",
                    () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminCategoryListScreen())),
                  ),
                  _buildAdminCard(
                    context,
                    "Orders",
                    Icons.receipt_long_rounded,
                    Colors.green,
                    "Track sales",
                    () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminOrderListScreen())),
                  ),
                  _buildAdminCard(
                    context,
                    "Analytics",
                    Icons.analytics_rounded,
                    Colors.purple,
                    "View reports",
                    () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminAnalyticsScreen())),
                  ),
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
      padding: const EdgeInsets.symmetric(vertical: 40, horizontal: 25),
      decoration: BoxDecoration(
        color: color,
        borderRadius: const BorderRadius.vertical(bottom: Radius.circular(40)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const CircleAvatar(
            radius: 35,
            backgroundColor: Colors.white24,
            child: Icon(Icons.admin_panel_settings_rounded, size: 45, color: Colors.white),
          ),
          const SizedBox(height: 20),
          const Text(
            "Hello, Admin!",
            style: TextStyle(color: Colors.white, fontSize: 28, fontWeight: FontWeight.bold),
          ),
          Text(
            "Welcome back to your shop manager.",
            style: TextStyle(color: Colors.white.withValues(alpha: 0.8), fontSize: 16),
          ),
        ],
      ),
    );
  }

  Widget _buildAdminCard(BuildContext context, String title, IconData icon, Color color, String subtitle, VoidCallback onTap) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(25),
      child: Container(
        decoration: BoxDecoration(
          color: Theme.of(context).cardColor,
          borderRadius: BorderRadius.circular(25),
          boxShadow: [
            BoxShadow(
              color: color.withValues(alpha: 0.1),
              blurRadius: 10,
              offset: const Offset(0, 5),
            )
          ],
          border: Border.all(color: color.withValues(alpha: 0.1)),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: color.withValues(alpha: 0.1),
                shape: BoxShape.circle,
              ),
              child: Icon(icon, size: 35, color: color),
            ),
            const SizedBox(height: 12),
            Text(
              title,
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 4),
            Text(
              subtitle,
              style: const TextStyle(fontSize: 12, color: Colors.grey),
            ),
          ],
        ),
      ),
    );
  }
}
