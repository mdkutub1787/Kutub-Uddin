import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/view_models/auth_view_model.dart';
import 'package:smart_shop/view_models/settings_view_model.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:smart_shop/routes/app_routes.dart';
import 'package:smart_shop/view_models/cart_view_model.dart';
import 'package:smart_shop/view_models/wishlist_view_model.dart';
import 'package:smart_shop/view_models/order_view_model.dart';
import 'package:smart_shop/view_models/notification_view_model.dart';
import 'package:smart_shop/view_models/support_view_model.dart';
import 'package:smart_shop/widgets/custom_app_bar.dart';
import 'package:smart_shop/widgets/app_card.dart';
import 'package:smart_shop/utils/constants/app_colors.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final authViewModel = context.watch<AuthViewModel>();
    final settings = context.watch<SettingsViewModel>();
    final cart = context.watch<CartViewModel>();
    final wishlist = context.watch<WishlistViewModel>();
    final orderVM = context.watch<OrderViewModel>();
    final user = authViewModel.user;

    return Scaffold(
      appBar: CustomAppBar(
        title: AppStrings.profileMenu.tr(),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(vertical: 12),
        child: Column(
          children: [
            _buildProfileHeader(context, user, settings, authViewModel.isAdmin),
            const SizedBox(height: 16),

            // Stats Row
            _buildStatsRow(context, cart, wishlist, orderVM),
            
            if (authViewModel.isAdmin) ...[
              const SizedBox(height: 16),
              _buildAdminCard(context, settings),
            ],

            const SizedBox(height: 20),

            // User Info Section
            _buildSectionHeader(AppStrings.personalInfo.tr()),
            _buildUserInfoSection(context, user, settings),

            const SizedBox(height: 20),

            // App Preferences
            _buildSectionHeader(AppStrings.appPreferences.tr()),
            _buildSettingsCard(context, settings),

            const SizedBox(height: 20),

            // Main Menu
            _buildSectionHeader(AppStrings.shoppingActivity.tr()),
            _buildMenuCard(context, [
              _menuItem(Icons.shopping_bag_outlined, AppStrings.myOrdersMenu.tr(), () => Navigator.pushNamed(context, AppRoutes.myOrders)),
              _menuItem(Icons.favorite_outline, AppStrings.wishlistMenu.tr(), () => Navigator.pushNamed(context, AppRoutes.wishlist)),
              _menuItem(Icons.notifications_outlined, AppStrings.notices.tr(), () => Navigator.pushNamed(context, AppRoutes.notifications), badge: context.watch<NotificationViewModel>().unreadCount),
              _menuItem(Icons.help_outline_rounded, AppStrings.support.tr(), () => Navigator.pushNamed(context, AppRoutes.support), badge: context.watch<SupportViewModel>().unreadCount),
            ]),

            const SizedBox(height: 20),

            _buildSectionHeader(AppStrings.accountSecurity.tr()),
            _buildMenuCard(context, [
              _menuItem(Icons.lock_reset_rounded, AppStrings.changePassword.tr(), () => _showChangePasswordDialog(context)),
              _menuItem(Icons.mail_lock_rounded, AppStrings.resetViaEmail.tr(), () async {
                if (user?.email != null) {
                  await authViewModel.forgotPassword(user!.email);
                  if (context.mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.resetEmailSent.tr())));
                }
              }),
            ]),

            const SizedBox(height: 24),

            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: SizedBox(
                width: double.infinity,
                child: OutlinedButton.icon(
                  onPressed: () => _showLogoutConfirmation(context, authViewModel),
                  icon: const Icon(Icons.logout, color: Colors.red, size: 20),
                  label: Text(AppStrings.logout.tr(), style: const TextStyle(color: Colors.red, fontWeight: FontWeight.bold, fontSize: 14)),
                  style: OutlinedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 12),
                    side: const BorderSide(color: Colors.red, width: 1.2),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                  ),
                ),
              ),
            ),

            const SizedBox(height: 12),
            Text("${AppStrings.appVersion.tr()} 1.0.0", style: TextStyle(color: Colors.grey[400], fontSize: 11)),
            const SizedBox(height: 30),
          ],
        ),
      ),
    );
  }

  void _showLogoutConfirmation(BuildContext context, AuthViewModel authViewModel) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: Text(AppStrings.logout.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
        content: Text(AppStrings.logoutConfirm.tr()),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: Text(AppStrings.cancel.tr()),
          ),
          ElevatedButton(
            onPressed: () async {
              Navigator.pop(ctx);
              context.read<CartViewModel>().clearCart();
              context.read<WishlistViewModel>().clear();
              await authViewModel.logout();
              if (context.mounted) {
                Navigator.pushNamedAndRemoveUntil(context, AppRoutes.login, (route) => false);
              }
            },
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.red,
              foregroundColor: Colors.white,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            ),
            child: Text(AppStrings.logout.tr()),
          ),
        ],
      ),
    );
  }

  Widget _buildSectionHeader(String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(25, 0, 25, 8),
      child: Align(
        alignment: Alignment.centerLeft,
        child: Text(title.toUpperCase(), style: const TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: Colors.grey, letterSpacing: 1.1)),
      ),
    );
  }

  Widget _buildStatsRow(BuildContext context, CartViewModel cart, WishlistViewModel wishlist, OrderViewModel orderVM) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Row(
        children: [
          _statItem(context, "${orderVM.userOrders.length}", AppStrings.ordersCountLabel.tr(), Colors.blue, () => Navigator.pushNamed(context, AppRoutes.myOrders)),
          const SizedBox(width: 10),
          _statItem(context, "${wishlist.wishlistProductIds.length}", AppStrings.wishlistCountLabel.tr(), Colors.pink, () => Navigator.pushNamed(context, AppRoutes.wishlist)),
          const SizedBox(width: 10),
          _statItem(context, "${cart.itemCount}", AppStrings.cartCountLabel.tr(), Colors.orange, () => Navigator.pushNamed(context, AppRoutes.cart)),
        ],
      ),
    );
  }

  Widget _statItem(BuildContext context, String value, String label, Color color, VoidCallback onTap) {
    return Expanded(
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(15),
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 12),
          decoration: BoxDecoration(
            color: color.withValues(alpha: 0.1),
            borderRadius: BorderRadius.circular(15),
            border: Border.all(color: color.withValues(alpha: 0.2)),
          ),
          child: Column(
            children: [
              Text(value, style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: color)),
              Text(label, style: TextStyle(fontSize: 11, color: color.withValues(alpha: 0.8), fontWeight: FontWeight.w600)),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildMenuCard(BuildContext context, List<Widget> items) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: AppCard(
        elevation: 2,
        borderRadius: 20,
        child: Column(children: items),
      ),
    );
  }

  Widget _menuItem(IconData icon, String title, VoidCallback onTap, {int badge = 0}) {
    return ListTile(
      dense: true,
      leading: Icon(icon, size: 20),
      title: Text(title, style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500)),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          if (badge > 0)
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
              decoration: BoxDecoration(color: Colors.red, borderRadius: BorderRadius.circular(10)),
              child: Text('$badge', style: const TextStyle(color: Colors.white, fontSize: 9, fontWeight: FontWeight.bold)),
            ),
          const SizedBox(width: 4),
          const Icon(Icons.arrow_forward_ios, size: 12, color: Colors.grey),
        ],
      ),
      onTap: onTap,
    );
  }

  Widget _buildSettingsCard(BuildContext context, SettingsViewModel settings) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: AppCard(
        elevation: 2,
        borderRadius: 20,
        child: Column(
          children: [
            // Dark Mode
            ListTile(
              leading: Icon(settings.themeMode == ThemeMode.dark ? Icons.dark_mode_rounded : Icons.light_mode_rounded),
              title: Text(AppStrings.appearance.tr(), style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w500)),
              trailing: Switch.adaptive(
                value: settings.themeMode == ThemeMode.dark,
                activeColor: settings.primaryColor,
                onChanged: (val) => settings.setThemeMode(val ? ThemeMode.dark : ThemeMode.light),
              ),
            ),
            const Divider(height: 1, indent: 55),
            // Language
            ListTile(
              leading: const Icon(Icons.translate_rounded),
              title: Text(AppStrings.language.tr(), style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w500)),
              trailing: Text(
                context.locale.languageCode == 'en' ? AppStrings.english.tr() : AppStrings.bengali.tr(),
                style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold)
              ),
              onTap: () {
                if (context.locale.languageCode == 'en') {
                  context.setLocale(const Locale('bn', 'BD'));
                } else {
                  context.setLocale(const Locale('en', 'US'));
                }
              },
            ),
            const Divider(height: 1, indent: 55),
            // Theme Color
            ListTile(
              leading: const Icon(Icons.palette_outlined),
              title: Text(AppStrings.themeColor.tr(), style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w500)),
              subtitle: Padding(
                padding: const EdgeInsets.only(top: 8),
                child: Wrap(
                  spacing: 10,
                  runSpacing: 10,
                  children: AppColors.themePalette.map((color) => _colorDot(settings, color)).toList(),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _colorDot(SettingsViewModel settings, Color color) {
    bool isSelected = settings.primaryColor.value == color.value;
    return GestureDetector(
      onTap: () => settings.setPrimaryColor(color),
      child: Container(
        width: 22,
        height: 22,
        decoration: BoxDecoration(
          color: color,
          shape: BoxShape.circle,
          border: isSelected ? Border.all(color: Colors.black54, width: 2) : null,
        ),
        child: isSelected ? const Icon(Icons.check, size: 12, color: Colors.white) : null,
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
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      AppStrings.adminPanel.tr(),
                      style: const TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    Text(
                      AppStrings.adminPanelDesc.tr(),
                      style: const TextStyle(color: Colors.white70, fontSize: 12),
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

  Widget _buildUserInfoSection(BuildContext context, dynamic user, SettingsViewModel settings) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: AppCard(
        elevation: 1,
        borderRadius: 16,
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(AppStrings.personalInfo.tr(), style: const TextStyle(fontSize: 15, fontWeight: FontWeight.bold)),
                  GestureDetector(
                    onTap: () => Navigator.pushNamed(context, AppRoutes.editProfile),
                    child: Text(AppStrings.update.tr(), style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold, fontSize: 13)),
                  ),
                ],
              ),
              const Divider(height: 20),
              _buildInfoRow(Icons.phone_android_rounded, AppStrings.phoneLabel.tr(), user?.phoneNumber ?? "Not provided"),
              const SizedBox(height: 10),
              _buildInfoRow(Icons.email_outlined, AppStrings.emailLabel.tr(), user?.email ?? ""),
              const SizedBox(height: 10),
              _buildInfoRow(Icons.location_on_outlined, AppStrings.addressLabel.tr(), user?.address ?? "No address saved"),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildInfoRow(IconData icon, String label, String value) {
    return Row(
      children: [
        Container(
          padding: const EdgeInsets.all(8),
          decoration: BoxDecoration(color: Colors.grey[100], borderRadius: BorderRadius.circular(10)),
          child: Icon(icon, size: 18, color: Colors.grey[700]),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(label, style: const TextStyle(fontSize: 11, color: Colors.grey)),
              Text(value, style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w600)),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildProfileHeader(BuildContext context, dynamic user, SettingsViewModel settings, bool isAdmin) {
    return Column(
      children: [
        Stack(
          children: [
            Container(
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                border: Border.all(color: settings.primaryColor.withValues(alpha: 0.2), width: 4),
              ),
              child: CircleAvatar(
                radius: 55,
                backgroundColor: settings.primaryColor.withValues(alpha: 0.1),
                child: Icon(Icons.person_rounded, size: 70, color: settings.primaryColor),
              ),
            ),
            Positioned(
              bottom: 0,
              right: 0,
              child: GestureDetector(
                onTap: () => Navigator.pushNamed(context, AppRoutes.editProfile),
                child: CircleAvatar(
                  backgroundColor: settings.primaryColor,
                  radius: 18,
                  child: const Icon(Icons.edit_rounded, color: Colors.white, size: 18),
                ),
              ),
            ),
          ],
        ),
        const SizedBox(height: 15),
        Text(user?.name ?? "User Name", style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900, letterSpacing: -0.5)),
        Text(user?.email ?? "", style: TextStyle(color: Colors.grey[600], fontSize: 14)),
        if (isAdmin)
          Container(
            margin: const EdgeInsets.only(top: 10),
            padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 5),
            decoration: BoxDecoration(
              color: Colors.amber[700]!.withValues(alpha: 0.15),
              borderRadius: BorderRadius.circular(20),
              border: Border.all(color: Colors.amber[700]!.withValues(alpha: 0.3)),
            ),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                Icon(Icons.verified_user_rounded, size: 14, color: Colors.amber[900]),
                const SizedBox(width: 6),
                Text(AppStrings.storeAdmin.tr().toUpperCase(), style: TextStyle(fontSize: 10, fontWeight: FontWeight.w900, color: Colors.amber[900], letterSpacing: 1)),
              ],
            ),
          ),
      ],
    );
  }

  Widget _buildProfileItem(IconData icon, String title, VoidCallback onTap, {bool isExit = false, int badgeCount = 0}) {
    return ListTile(
      leading: Icon(icon, color: isExit ? Colors.red : null),
      title: Text(title, style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500, color: isExit ? Colors.red : null)),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          if (badgeCount > 0)
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
              decoration: BoxDecoration(color: Colors.red, borderRadius: BorderRadius.circular(10)),
              child: Text(
                '$badgeCount',
                style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
              ),
            ),
          const SizedBox(width: 5),
          if (!isExit) const Icon(Icons.arrow_forward_ios, size: 16),
        ],
      ),
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
              autofillHints: const [AutofillHints.password],
              decoration: const InputDecoration(labelText: "Old Password", prefixIcon: Icon(Icons.lock_outline)),
            ),
            const SizedBox(height: 10),
            TextField(
              controller: newController,
              obscureText: true,
              autofillHints: const [AutofillHints.newPassword],
              decoration: const InputDecoration(labelText: "New Password", prefixIcon: Icon(Icons.password)),
            ),
            const SizedBox(height: 10),
            TextField(
              controller: confirmController,
              obscureText: true,
              autofillHints: const [AutofillHints.password],
              decoration: const InputDecoration(labelText: "Confirm New Password", prefixIcon: Icon(Icons.check_circle_outline)),
            ),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("Cancel")),
          ElevatedButton(
            onPressed: () async {
              FocusManager.instance.primaryFocus?.unfocus();
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
