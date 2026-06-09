import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/view_models/auth_view_model.dart';
import 'package:smart_shop/view_models/settings_view_model.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:smart_shop/routes/app_routes.dart';
import 'package:smart_shop/widgets/custom_app_bar.dart';
import 'package:smart_shop/widgets/app_card.dart';

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
            _buildProfileHeader(context, user, settings, authViewModel.isAdmin),
            const SizedBox(height: 30),

            if (authViewModel.isAdmin) ...[
              _buildAdminCard(context, settings),
              const SizedBox(height: 20),
              const Padding(
                padding: EdgeInsets.symmetric(horizontal: 20),
                child: Align(
                  alignment: Alignment.centerLeft,
                  child: Text("User Menu", style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: Colors.grey)),
                ),
              ),
            ],
            
            _buildProfileItem(Icons.shopping_bag_outlined, AppStrings.myOrdersMenu.tr(), () {
              Navigator.pushNamed(context, AppRoutes.myOrders);
            }),
            _buildProfileItem(Icons.favorite_outline, AppStrings.wishlistMenu.tr(), () {
              Navigator.pushNamed(context, AppRoutes.wishlist);
            }),
            
            const Divider(indent: 20, endIndent: 20),
            
            // Password Management
            _buildProfileItem(Icons.lock_reset_rounded, "Change Password", () => _showChangePasswordDialog(context)),
            _buildProfileItem(Icons.mail_lock_rounded, "Reset via Email", () async {
              if (user?.email != null) {
                await authViewModel.forgotPassword(user!.email);
                if (context.mounted) ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Reset email sent!")));
              }
            }),

            const Divider(indent: 20, endIndent: 20),
            
            if (!authViewModel.isAdmin)
              _buildProfileItem(Icons.admin_panel_settings, 'Request Admin Access', () {
                Navigator.pushNamed(context, AppRoutes.adminVerification);
              }),
              
            _buildProfileItem(Icons.logout, AppStrings.logout.tr(), () => authViewModel.logout(), isExit: true),
            const SizedBox(height: 40),
          ],
        ),
      ),
    );
  }

  Widget _buildAdminCard(BuildContext context, SettingsViewModel settings) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: AppCard(
        color: settings.primaryColor,
        borderRadius: 20,
        onTap: () => Navigator.pushNamed(context, AppRoutes.adminDashboard),
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Row(
            children: [
              const CircleAvatar(
                backgroundColor: Colors.white24,
                child: Icon(Icons.admin_panel_settings, color: Colors.white),
              ),
              const SizedBox(width: 15),
              const Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Admin Control Panel",
                      style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    Text(
                      "Manage products, orders & analytics",
                      style: TextStyle(color: Colors.white70, fontSize: 12),
                    ),
                  ],
                ),
              ),
              const Icon(Icons.arrow_forward_ios, color: Colors.white, size: 18),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildProfileHeader(BuildContext context, dynamic user, SettingsViewModel settings, bool isAdmin) {
    return Column(
      children: [
        Stack(
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
        const SizedBox(height: 15),
        Text(user?.name ?? "User Name", style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
        Text(user?.email ?? "", style: const TextStyle(color: Colors.grey)),
        if (isAdmin)
          Container(
            margin: const EdgeInsets.only(top: 8),
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
            decoration: BoxDecoration(color: Colors.amber.withValues(alpha: 0.2), borderRadius: BorderRadius.circular(20)),
            child: const Text("ADMIN", style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Colors.orange)),
          ),
      ],
    );
  }

  Widget _buildProfileItem(IconData icon, String title, VoidCallback onTap, {bool isExit = false}) {
    return ListTile(
      leading: Icon(icon, color: isExit ? Colors.red : null),
      title: Text(title, style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500, color: isExit ? Colors.red : null)),
      trailing: isExit ? null : const Icon(Icons.arrow_forward_ios, size: 16),
      onTap: onTap,
    );
  }

  void _showChangePasswordDialog(BuildContext context) {
    final oldController = TextEditingController();
    final newController = TextEditingController();
    final confirmController = TextEditingController();
    
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: const Text("Change Password", style: TextStyle(fontWeight: FontWeight.bold)),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: oldController,
              obscureText: true,
              decoration: const InputDecoration(labelText: "Old Password", prefixIcon: Icon(Icons.lock_outline)),
            ),
            const SizedBox(height: 10),
            TextField(
              controller: newController,
              obscureText: true,
              decoration: const InputDecoration(labelText: "New Password", prefixIcon: Icon(Icons.password)),
            ),
            const SizedBox(height: 10),
            TextField(
              controller: confirmController,
              obscureText: true,
              decoration: const InputDecoration(labelText: "Confirm New Password", prefixIcon: Icon(Icons.check_circle_outline)),
            ),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("Cancel")),
          ElevatedButton(
            onPressed: () async {
              if (newController.text.length < 6) {
                ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Password must be at least 6 chars")));
                return;
              }
              if (newController.text != confirmController.text) {
                ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Passwords do not match")));
                return;
              }
              
              final success = await context.read<AuthViewModel>().changePassword(
                oldController.text, 
                newController.text
              );
              
              if (context.mounted) {
                if (success) {
                  Navigator.pop(ctx);
                  ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Password updated successfully!")));
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                    content: Text(context.read<AuthViewModel>().error ?? "Failed to update password"),
                    backgroundColor: Colors.red,
                  ));
                }
              }
            },
            child: const Text("Update"),
          )
        ],
      ),
    );
  }
}
