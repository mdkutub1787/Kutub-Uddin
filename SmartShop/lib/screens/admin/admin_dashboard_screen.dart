import 'package:flutter/material.dart';
import 'admin_product_list_screen.dart';
import 'admin_category_list_screen.dart';
import 'admin_order_list_screen.dart';

class AdminDashboardScreen extends StatelessWidget {
  const AdminDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Admin Dashboard"),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: GridView.count(
          crossAxisCount: 2,
          crossAxisSpacing: 20,
          mainAxisSpacing: 20,
          children: [
            _buildAdminCard(
              context,
              "Manage Products",
              Icons.shopping_bag,
              Colors.blue,
              () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminProductListScreen())),
            ),
            _buildAdminCard(
              context,
              "Manage Categories",
              Icons.category,
              Colors.orange,
              () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminCategoryListScreen())),
            ),
            _buildAdminCard(
              context,
              "Orders",
              Icons.list_alt,
              Colors.green,
              () => Navigator.push(context, MaterialPageRoute(builder: (_) => const AdminOrderListScreen())),
            ),
            _buildAdminCard(
              context,
              "Users",
              Icons.people,
              Colors.purple,
              () {},
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildAdminCard(BuildContext context, String title, IconData icon, Color color, VoidCallback onTap) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(20),
      child: Container(
        decoration: BoxDecoration(
          color: color.withOpacity(0.1),
          borderRadius: BorderRadius.circular(20),
          border: Border.all(color: color.withOpacity(0.5)),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 50, color: color),
            const SizedBox(height: 10),
            Text(
              title,
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: color),
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }
}
