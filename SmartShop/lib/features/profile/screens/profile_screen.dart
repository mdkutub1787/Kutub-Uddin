import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../core/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../cart/riverpod/cart_notifier.dart';
import '../../wishlist/riverpod/wishlist_notifier.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../notification/riverpod/notification_notifier.dart';
import '../../support/riverpod/support_notifier.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/app_card.dart';
import '../../../theme/app_colors.dart';

class ProfileScreen extends ConsumerWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final authState = ref.watch(authNotifierProvider);
    final settings = ref.watch(settingsProvider);
    final cartItems = ref.watch(cartNotifierProvider) ?? [];
    final cartCount = cartItems.fold(0, (sum, item) => sum + item.quantity);
    
    final wishlistItems = ref.watch(wishlistNotifierProvider).value ?? [] ?? [];
    final orderItems = ref.watch(orderNotifierProvider).value ?? [];
    
    final unreadNotifCount = ref.watch(notificationNotifierProvider).value?.length ?? 0; // Dummy
    final unreadSupportCount = ref.watch(supportNotifierProvider).value?.length ?? 0; // Dummy
    
    final user = authState.value;
    final isAdmin = user?.role == 'admin' || user?.role == 'super_admin';
    final isDeliveryMan = user?.role == 'delivery_man';

    return Scaffold(
      appBar: CustomAppBar(
        title: AppStrings.profileMenu.tr(),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(vertical: 12),
        child: Column(
          children: [
            _buildProfileHeader(context, user, settings, isAdmin),
            const SizedBox(height: 16),

            // Stats Row
            _buildStatsRow(context, cartCount, wishlistItems.length, orderItems.length),
            
            if (isAdmin) ...[
              const SizedBox(height: 16),
              _buildAdminCard(context, settings),
            ],

            if (isDeliveryMan) ...[
              const SizedBox(height: 16),
              _buildDeliveryCard(context, settings),
            ],

            const SizedBox(height: 20),

            // User Info Section
            _buildSectionHeader(AppStrings.personalInfo.tr()),
            _buildUserInfoSection(context, user, settings),

            const SizedBox(height: 20),

            // App Preferences
            _buildSectionHeader(AppStrings.appPreferences.tr()),
            _buildSettingsCard(context, ref, settings),

            const SizedBox(height: 20),

            // Main Menu
            _buildSectionHeader(AppStrings.shoppingActivity.tr()),
            _buildMenuCard(context, [
              _menuItem(Icons.shopping_bag_outlined, AppStrings.myOrdersMenu.tr(), () => Navigator.pushNamed(context, AppRoutes.myOrders)),
              _menuItem(Icons.favorite_outline, AppStrings.wishlistMenu.tr(), () => Navigator.pushNamed(context, AppRoutes.wishlist)),
              _menuItem(Icons.notifications_outlined, AppStrings.notices.tr(), () => Navigator.pushNamed(context, AppRoutes.notifications), badge: unreadNotifCount),
              _menuItem(Icons.help_outline_rounded, AppStrings.support.tr(), () => Navigator.pushNamed(context, AppRoutes.support), badge: unreadSupportCount),
            ]),

            const SizedBox(height: 20),

            _buildSectionHeader(AppStrings.accountSecurity.tr()),
            _buildMenuCard(context, [
              _menuItem(Icons.lock_reset_rounded, AppStrings.changePassword.tr(), () => _showChangePasswordDialog(context, ref)),
              _menuItem(Icons.mail_lock_rounded, AppStrings.resetViaEmail.tr(), () async {
                if (user?.email != null) {
                  // await authViewModel.forgotPassword(user!.email);
                  // if (context.mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(AppStrings.resetEmailSent.tr())));
                }
              }),
            ]),

            const SizedBox(height: 24),

            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: SizedBox(
                width: double.infinity,
                child: OutlinedButton.icon(
                  onPressed: () => _showLogoutConfirmation(context, ref),
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

  void _showLogoutConfirmation(BuildContext context, WidgetRef ref) {
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
              await ref.read(authNotifierProvider.notifier).signOut();
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

  Widget _buildProfileHeader(BuildContext context, dynamic user, dynamic settings, bool isAdmin) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 20),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [settings.primaryColor, settings.primaryColor.withValues(alpha: 0.7)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(30),
        boxShadow: [
          BoxShadow(
            color: settings.primaryColor.withValues(alpha: 0.3),
            blurRadius: 20,
            offset: const Offset(0, 10),
          )
        ],
      ),
      child: Column(
        children: [
          Stack(
            children: [
              Container(
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  border: Border.all(color: Colors.white.withValues(alpha: 0.4), width: 4),
                  boxShadow: [
                    BoxShadow(color: Colors.black.withValues(alpha: 0.1), blurRadius: 10)
                  ],
                ),
                child: const CircleAvatar(
                  radius: 45,
                  backgroundColor: Colors.white,
                  child: Icon(Icons.person_rounded, size: 50, color: Colors.grey),
                ),
              ),
              Positioned(
                bottom: 0,
                right: 0,
                child: GestureDetector(
                  onTap: () => Navigator.pushNamed(context, AppRoutes.editProfile),
                  child: Container(
                    padding: const EdgeInsets.all(6),
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      shape: BoxShape.circle,
                    ),
                    child: Icon(Icons.edit_rounded, color: settings.primaryColor, size: 14),
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 15),
          Text(user?.name ?? "User Name", style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900, color: Colors.white)),
          Text(user?.email ?? "", style: TextStyle(color: Colors.white.withValues(alpha: 0.9), fontSize: 13, fontWeight: FontWeight.w500)),
          if (isAdmin)
            Container(
              margin: const EdgeInsets.only(top: 10),
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
              decoration: BoxDecoration(
                color: Colors.white24,
                borderRadius: BorderRadius.circular(20),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Icon(Icons.admin_panel_settings_rounded, size: 14, color: Colors.white),
                  const SizedBox(width: 6),
                  Text(
                    (user?.role == 'super_admin' ? AppStrings.superAdmin.tr() : AppStrings.storeAdmin.tr()).toUpperCase(), 
                    style: const TextStyle(fontSize: 10, fontWeight: FontWeight.w900, color: Colors.white, letterSpacing: 1)
                  ),
                ],
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildSectionHeader(String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(25, 10, 25, 10),
      child: Align(
        alignment: Alignment.centerLeft,
        child: Text(title.toUpperCase(), style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w900, color: Colors.grey, letterSpacing: 1.2)),
      ),
    );
  }

  Widget _buildStatsRow(BuildContext context, int cartCount, int wishlistCount, int orderCount) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Row(
        children: [
          _statItem(context, "$orderCount", AppStrings.ordersCountLabel.tr(), Colors.blueAccent, () => Navigator.pushNamed(context, AppRoutes.myOrders)),
          const SizedBox(width: 15),
          _statItem(context, "$wishlistCount", AppStrings.wishlistCountLabel.tr(), Colors.pinkAccent, () => Navigator.pushNamed(context, AppRoutes.wishlist)),
          const SizedBox(width: 15),
          _statItem(context, "$cartCount", AppStrings.cartCountLabel.tr(), Colors.orangeAccent, () => Navigator.pushNamed(context, AppRoutes.cart)),
        ],
      ),
    );
  }

  Widget _statItem(BuildContext context, String value, String label, Color color, VoidCallback onTap) {
    return Expanded(
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(24),
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 16),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(24),
            boxShadow: [
              BoxShadow(color: color.withValues(alpha: 0.1), blurRadius: 15, offset: const Offset(0, 5)),
            ],
            border: Border.all(color: color.withValues(alpha: 0.1)),
          ),
          child: Column(
            children: [
              Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(color: color.withValues(alpha: 0.15), shape: BoxShape.circle),
                child: Text(value, style: TextStyle(fontSize: 16, fontWeight: FontWeight.w900, color: color)),
              ),
              const SizedBox(height: 8),
              Text(label, style: const TextStyle(fontSize: 10, color: Colors.black54, fontWeight: FontWeight.bold)),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildMenuCard(BuildContext context, List<Widget> items) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(24),
          boxShadow: [
            BoxShadow(color: Colors.black.withValues(alpha: 0.03), blurRadius: 20, offset: const Offset(0, 5)),
          ],
        ),
        child: Material(
          color: Colors.transparent,
          borderRadius: BorderRadius.circular(24),
          clipBehavior: Clip.antiAlias,
          child: Column(children: items),
        ),
      ),
    );
  }

  Widget _menuItem(IconData icon, String title, VoidCallback onTap, {int badge = 0}) {
    return ListTile(
      contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 4),
      leading: Container(
        padding: const EdgeInsets.all(8),
        decoration: BoxDecoration(color: Colors.grey[100], borderRadius: BorderRadius.circular(12)),
        child: Icon(icon, size: 20, color: Colors.grey[800]),
      ),
      title: Text(title, style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w600, color: Colors.black87)),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          if (badge > 0)
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
              decoration: BoxDecoration(color: Colors.red, borderRadius: BorderRadius.circular(12)),
              child: Text('$badge', style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
            ),
          const SizedBox(width: 8),
          const Icon(Icons.arrow_forward_ios_rounded, size: 14, color: Colors.grey),
        ],
      ),
      onTap: onTap,
    );
  }

  Widget _buildSettingsCard(BuildContext context, WidgetRef ref, dynamic settings) {
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
                onChanged: (val) {
                   ref.read(settingsProvider.notifier).setThemeMode(val ? ThemeMode.dark : ThemeMode.light);
                },
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
                  children: AppColors.themePalette.map((color) => _colorDot(ref, settings, color)).toList(),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _colorDot(WidgetRef ref, dynamic settings, Color color) {
    bool isSelected = settings.primaryColor.value == color.value;
    return GestureDetector(
      onTap: () => ref.read(settingsProvider.notifier).setPrimaryColor(color),
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

  Widget _buildAdminCard(BuildContext context, dynamic settings) {
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

  Widget _buildDeliveryCard(BuildContext context, dynamic settings) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: AppCard(
        color: Colors.blueAccent,
        borderRadius: 20,
        onTap: () => Navigator.pushNamed(context, AppRoutes.deliveryDashboard),
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Row(
            children: [
              const CircleAvatar(
                backgroundColor: Colors.white24,
                child: Icon(Icons.delivery_dining_rounded, color: Colors.white),
              ),
              const SizedBox(width: 15),
              const Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Delivery Dashboard",
                      style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    Text(
                      "Manage your deliveries and status",
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

  Widget _buildUserInfoSection(BuildContext context, dynamic user, dynamic settings) {
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



  void _showChangePasswordDialog(BuildContext context, WidgetRef ref) {
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
              // TODO: Add Change password
            },
            child: const Text("Update"),
          )
        ],
      ),
    );
  }
}
