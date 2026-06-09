import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../view_models/auth_view_model.dart';
import '../view_models/settings_view_model.dart';
import 'package:easy_localization/easy_localization.dart';
import '../utils/constants/app_strings.dart';
import '../routes/app_routes.dart';

import '../widgets/custom_app_bar.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final authViewModel = context.watch<AuthViewModel>();
    final settings = context.watch<SettingsViewModel>();
    final user = authViewModel.user;

    return Scaffold(
      appBar: CustomAppBar(
        title: AppStrings.profileMenu.tr(),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            const SizedBox(height: 20),
            Center(
              child: Stack(
                children: [
                  CircleAvatar(
                    radius: 60,
                    backgroundColor: settings.primaryColor.withValues(alpha: 0.1),
                    child: Icon(Icons.person, size: 80, color: settings.primaryColor),
                  ),
                  Positioned(
                    bottom: 0,
                    right: 0,
                    child: GestureDetector(
                      onTap: () => Navigator.pushNamed(context, AppRoutes.editProfile),
                      child: CircleAvatar(
                        backgroundColor: settings.primaryColor,
                        radius: 20,
                        child: const Icon(Icons.edit, color: Colors.white, size: 20),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 20),
            Text(
              user?.displayName ?? "User Name",
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            Text(
              user?.email ?? "email@example.com",
              style: const TextStyle(fontSize: 16, color: Colors.grey),
            ),
            // Show admin badge if user is admin
            if (authViewModel.isAdmin)
              Padding(
                padding: const EdgeInsets.only(top: 10),
                child: Container(
                  padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                  decoration: BoxDecoration(
                    color: Colors.amber.withOpacity(0.2),
                    border: Border.all(color: Colors.amber),
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: const Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(Icons.admin_panel_settings, size: 16, color: Colors.amber),
                      SizedBox(width: 4),
                      Text(
                        'Admin',
                        style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.amber),
                      ),
                    ],
                  ),
                ),
              ),
            const SizedBox(height: 30),
            _buildProfileItem(Icons.shopping_bag_outlined, AppStrings.myOrdersMenu.tr(), () {
              Navigator.pushNamed(context, AppRoutes.myOrders);
            }),
            _buildProfileItem(Icons.favorite_outline, AppStrings.wishlistMenu.tr(), () {
              Navigator.pushNamed(context, AppRoutes.wishlist);
            }),
            _buildProfileItem(Icons.location_on_outlined, AppStrings.shippingAddress.tr(), () {}),
            _buildProfileItem(Icons.payment_outlined, AppStrings.paymentMethods.tr(), () {}),
            _buildProfileItem(Icons.settings_outlined, AppStrings.settings.tr(), () {}),
            // Show admin access request only if not admin
            if (!authViewModel.isAdmin)
              _buildProfileItem(Icons.admin_panel_settings, 'Request Admin Access', () {
                Navigator.pushNamed(context, AppRoutes.adminVerification);
              }),
            const Divider(height: 40),
            _buildProfileItem(Icons.logout, AppStrings.logout.tr(), () => authViewModel.logout(), isExit: true),
          ],
        ),
      ),
    );
  }

  Widget _buildProfileItem(IconData icon, String title, VoidCallback onTap, {bool isExit = false}) {
    return ListTile(
      leading: Icon(icon, color: isExit ? Colors.red : null),
      title: Text(
        title,
        style: TextStyle(
          fontSize: 18,
          fontWeight: FontWeight.w500,
          color: isExit ? Colors.red : null,
        ),
      ),
      trailing: isExit ? null : const Icon(Icons.arrow_forward_ios, size: 18),
      onTap: onTap,
    );
  }
}
