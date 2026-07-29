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
import '../../../core/riverpod/navigation_notifier.dart';

class ProfileScreen extends ConsumerWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final authState = ref.watch(authNotifierProvider);
    final settings = ref.watch(settingsProvider);
    final cart = ref.watch(cartNotifierProvider);
    final cartCount = cart.items.fold(0, (sum, item) => sum + item.quantity);
    
    final wishlistItems = ref.watch(wishlistNotifierProvider).value ?? [];
    final orderItems = ref.watch(orderNotifierProvider).value ?? [];
    
    final unreadNotifCount = ref.watch(notificationNotifierProvider).value?.length ?? 0;
    final unreadSupportCount = ref.watch(supportNotifierProvider).value?.length ?? 0;
    
    final user = authState.value;
    final isAdmin = user?.role == 'admin' || user?.role == 'super_admin' || user?.role == 'owner';
    final isDeliveryMan = user?.role == 'delivery_man';

    return Scaffold(
      appBar: CustomAppBar(
        title: AppStrings.profileMenu.tr(),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout_rounded, color: Colors.white),
            onPressed: () => _showLogoutConfirmation(context, ref),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(vertical: 12),
        child: Column(
          children: [
            _buildProfileHeader(context, user, settings, isAdmin),
            const SizedBox(height: 16),

            // Stats Row
            if (!isDeliveryMan) ...[
              _buildStatsRow(context, cartCount, wishlistItems.length, orderItems.length),
              const SizedBox(height: 16),
            ],
            
            if (isAdmin) ...[
              _buildAdminCard(context, settings),
              const SizedBox(height: 16),
            ],

            if (isDeliveryMan) ...[
              _buildDeliveryCard(context, ref, settings),
              const SizedBox(height: 16),
            ],

            // User Info Section
            _buildSectionHeader(AppStrings.personalInfo.tr()),
            _buildUserInfoSection(context, user, settings),

            const SizedBox(height: 20),

            // App Preferences
            _buildSectionHeader(AppStrings.appPreferences.tr()),
            _buildSettingsCard(context, ref, settings),

            const SizedBox(height: 20),

            // Main Menu (Hidden for riders)
            if (!isDeliveryMan) ...[
              _buildSectionHeader(AppStrings.shoppingActivity.tr()),
              _buildMenuCard(context, [
                _menuItem(Icons.shopping_bag_outlined, AppStrings.myOrdersMenu.tr(), () => Navigator.pushNamed(context, AppRoutes.myOrders)),
                _menuItem(Icons.favorite_outline, AppStrings.wishlistMenu.tr(), () => Navigator.pushNamed(context, AppRoutes.wishlist)),
                _menuItem(Icons.notifications_outlined, AppStrings.notices.tr(), () => Navigator.pushNamed(context, AppRoutes.notifications), badge: unreadNotifCount),
                _menuItem(Icons.help_outline_rounded, AppStrings.support.tr(), () => Navigator.pushNamed(context, AppRoutes.support), badge: unreadSupportCount),
              ]),
              const SizedBox(height: 20),
            ],

            _buildSectionHeader(AppStrings.accountSecurity.tr()),
            _buildMenuCard(context, [
              _menuItem(Icons.lock_reset_rounded, AppStrings.changePassword.tr(), () => _showChangePasswordDialog(context, ref)),
              _menuItem(Icons.mail_lock_rounded, AppStrings.resetViaEmail.tr(), () async {
                if (user?.email != null) {
                }
              }),
            ]),

            const SizedBox(height: 24),

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
      builder: (ctx) => Dialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
        elevation: 0,
        backgroundColor: Colors.transparent,
        child: Container(
          padding: const EdgeInsets.all(24),
          decoration: BoxDecoration(
            color: Colors.white,
            shape: BoxShape.rectangle,
            borderRadius: BorderRadius.circular(24),
            boxShadow: const [
              BoxShadow(color: Colors.black26, blurRadius: 10.0, offset: Offset(0.0, 10.0)),
            ],
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.red.withValues(alpha: 0.1),
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.logout_rounded, color: Colors.red, size: 40),
              ),
              const SizedBox(height: 24),
              Text(
                AppStrings.logout.tr(),
                style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900, letterSpacing: -0.5),
              ),
              const SizedBox(height: 8),
              Text(
                AppStrings.logoutConfirm.tr(),
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 14, color: Colors.grey[600]),
              ),
              const SizedBox(height: 32),
              Row(
                children: [
                  Expanded(
                    child: OutlinedButton(
                      onPressed: () => Navigator.pop(ctx),
                      style: OutlinedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        side: BorderSide(color: Colors.grey[300]!),
                      ),
                      child: Text(AppStrings.cancel.tr(), style: const TextStyle(color: Colors.black87, fontWeight: FontWeight.bold)),
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: ElevatedButton(
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
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        elevation: 0,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      child: Text(AppStrings.logout.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildProfileHeader(BuildContext context, dynamic user, dynamic settings, bool isAdmin) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 20),
      padding: const EdgeInsets.symmetric(vertical: 24, horizontal: 20),
      width: double.infinity,
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [
            settings.primaryColor.withValues(alpha: 0.8),
            settings.primaryColor,
            settings.primaryColor.withValues(alpha: 0.95),
          ],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(32),
        boxShadow: [
          BoxShadow(
            color: settings.primaryColor.withValues(alpha: 0.35),
            blurRadius: 25,
            offset: const Offset(0, 12),
            spreadRadius: 2,
          )
        ],
      ),
      child: Stack(
        clipBehavior: Clip.none,
        alignment: Alignment.center,
        children: [
          Positioned(
            top: -40,
            right: -20,
            child: Container(
              width: 100,
              height: 100,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: Colors.white.withValues(alpha: 0.06),
              ),
            ),
          ),
          Positioned(
            bottom: -30,
            left: -30,
            child: Container(
              width: 80,
              height: 80,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: Colors.white.withValues(alpha: 0.06),
              ),
            ),
          ),
          Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Stack(
                clipBehavior: Clip.none,
                children: [
                  Container(
                    padding: const EdgeInsets.all(5),
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: Colors.white.withValues(alpha: 0.15),
                      boxShadow: [
                        BoxShadow(color: Colors.white.withValues(alpha: 0.15), blurRadius: 15, spreadRadius: 5)
                      ],
                    ),
                    child: Container(
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        border: Border.all(color: Colors.white, width: 3),
                        boxShadow: [
                          BoxShadow(color: Colors.black.withValues(alpha: 0.15), blurRadius: 10, offset: const Offset(0, 5))
                        ],
                      ),
                      child: CircleAvatar(
                        radius: 45,
                        backgroundColor: Colors.white,
                        backgroundImage: user?.imageUrl != null && user!.imageUrl!.isNotEmpty 
                            ? NetworkImage(user.imageUrl!) 
                            : null,
                        child: user?.imageUrl == null || user!.imageUrl!.isEmpty
                            ? const Icon(Icons.person_rounded, size: 45, color: Colors.grey)
                            : null,
                      ),
                    ),
                  ),
                  Positioned(
                    bottom: 0,
                    right: 0,
                    child: GestureDetector(
                      onTap: () => Navigator.pushNamed(context, AppRoutes.editProfile),
                      child: Container(
                        padding: const EdgeInsets.all(8),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          shape: BoxShape.circle,
                          boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.2), blurRadius: 8, offset: const Offset(0, 3))],
                        ),
                        child: Icon(Icons.camera_alt_rounded, color: settings.primaryColor, size: 14),
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              Text(
                user?.name ?? "User Name", 
                textAlign: TextAlign.center,
                style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Colors.white, letterSpacing: -0.5)
              ),
              const SizedBox(height: 4),
              Text(
                user?.email ?? "", 
                textAlign: TextAlign.center,
                style: TextStyle(color: Colors.white.withValues(alpha: 0.9), fontSize: 13, fontWeight: FontWeight.w500)
              ),
              const SizedBox(height: 16),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    colors: [Colors.black.withValues(alpha: 0.2), Colors.black.withValues(alpha: 0.1)],
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                  ),
                  borderRadius: BorderRadius.circular(24),
                  border: Border.all(color: Colors.white.withValues(alpha: 0.3), width: 1),
                  boxShadow: [
                    BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 8, offset: const Offset(0, 2))
                  ],
                ),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(
                      user?.role == 'admin' || user?.role == 'super_admin' ? Icons.verified_user_rounded : 
                      user?.role == 'delivery_man' ? Icons.local_shipping_rounded : 
                      Icons.person_rounded, 
                      size: 14, color: Colors.white
                    ),
                    const SizedBox(width: 6),
                    Text(
                      (user?.role == 'user' ? 'Customer' : user?.role ?? 'Customer').toUpperCase().replaceAll('_', ' '), 
                      style: const TextStyle(fontSize: 10, fontWeight: FontWeight.w900, color: Colors.white, letterSpacing: 1.5)
                    ),
                  ],
                ),
              ),
            ],
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
    String currencyLabel = "Currency";
    if (settings.currencySymbol == '৳') {
      currencyLabel = "BDT (৳)";
    } else if (settings.currencySymbol == '\$') {
      currencyLabel = "USD (\$)";
    } else {
      currencyLabel = settings.currencySymbol;
    }

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: AppCard(
        elevation: 2,
        borderRadius: 20,
        child: Column(
          children: [
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
            ListTile(
              leading: const Icon(Icons.monetization_on_outlined),
              title: const Text("Currency", style: TextStyle(fontSize: 15, fontWeight: FontWeight.w500)),
              trailing: Text(
                currencyLabel,
                style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold)
              ),
              onTap: () => _showCurrencyPicker(context, ref, settings),
            ),
            const Divider(height: 1, indent: 55),
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
      child: Container(
        decoration: BoxDecoration(
          gradient: const LinearGradient(
            colors: [Color(0xFF1E1E1E), Color(0xFF2C2C2C)],
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          ),
          borderRadius: BorderRadius.circular(28),
          boxShadow: [
            BoxShadow(color: Colors.black.withValues(alpha: 0.25), blurRadius: 25, offset: const Offset(0, 10)),
          ],
        ),
        child: Material(
          color: Colors.transparent,
          child: InkWell(
            onTap: () => Navigator.pushNamed(context, AppRoutes.adminDashboard),
            borderRadius: BorderRadius.circular(28),
            child: Padding(
              padding: const EdgeInsets.all(24),
              child: Row(
                children: [
                  Container(
                    padding: const EdgeInsets.all(14),
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        colors: [Colors.white.withValues(alpha: 0.15), Colors.white.withValues(alpha: 0.05)],
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                      ),
                      shape: BoxShape.circle,
                      border: Border.all(color: Colors.white.withValues(alpha: 0.2), width: 1)
                    ),
                    child: const Icon(Icons.admin_panel_settings_rounded, color: Colors.white, size: 30),
                  ),
                  const SizedBox(width: 18),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          AppStrings.adminPanel.tr(),
                          style: const TextStyle(color: Colors.white, fontSize: 19, fontWeight: FontWeight.w900, letterSpacing: 0.5),
                        ),
                        const SizedBox(height: 6),
                        Text(
                          AppStrings.adminPanelDesc.tr(),
                          style: TextStyle(color: Colors.white.withValues(alpha: 0.6), fontSize: 12, fontWeight: FontWeight.w600),
                        ),
                      ],
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: Colors.white.withValues(alpha: 0.1),
                      shape: BoxShape.circle,
                    ),
                    child: const Icon(Icons.arrow_forward_ios_rounded, color: Colors.white, size: 14),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildDeliveryCard(BuildContext context, WidgetRef ref, dynamic settings) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: AppCard(
        color: Colors.blueAccent,
        borderRadius: 20,
        onTap: () {
          ref.read(navigationNotifierProvider.notifier).setIndex(0);
        },
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
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(28),
          boxShadow: [
            BoxShadow(color: Colors.black.withValues(alpha: 0.04), blurRadius: 24, offset: const Offset(0, 8)),
          ],
          border: Border.all(color: Colors.grey.withValues(alpha: 0.1), width: 1.5),
        ),
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(AppStrings.personalInfo.tr(), style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w800, color: Colors.black87)),
                  GestureDetector(
                    onTap: () => Navigator.pushNamed(context, AppRoutes.editProfile),
                    child: Text(AppStrings.update.tr(), style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.w900, fontSize: 14)),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              const Divider(height: 1, color: Colors.black12),
              const SizedBox(height: 20),
              _buildInfoRow(Icons.phone_android_rounded, AppStrings.phoneLabel.tr(), user?.phoneNumber ?? "Not provided"),
              const SizedBox(height: 16),
              _buildInfoRow(Icons.email_outlined, AppStrings.emailLabel.tr(), user?.email ?? ""),
              const SizedBox(height: 16),
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
          padding: const EdgeInsets.all(10),
          decoration: BoxDecoration(
            color: Colors.grey.withValues(alpha: 0.1), 
            borderRadius: BorderRadius.circular(12)
          ),
          child: Icon(icon, size: 20, color: Colors.black54),
        ),
        const SizedBox(width: 16),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(label, style: const TextStyle(fontSize: 12, color: Colors.black54, fontWeight: FontWeight.bold)),
              const SizedBox(height: 4),
              Text(value, style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w700, color: Colors.black87)),
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
              try {
                await ref.read(authNotifierProvider.notifier).changePassword(newController.text.trim());
                if (context.mounted) {
                  Navigator.pop(ctx);
                  ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Password updated successfully!"), backgroundColor: Colors.green));
                }
              } catch (e) {
                if (context.mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Error: $e"), backgroundColor: Colors.red));
              }
            },
            child: const Text("Update"),
          )
        ],
      ),
    );
  }

  void _showCurrencyPicker(BuildContext context, WidgetRef ref, dynamic settings) {
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(25))),
      builder: (context) => Container(
        padding: const EdgeInsets.symmetric(vertical: 20),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Text("Select Currency", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            const SizedBox(height: 10),
            ListTile(
              title: const Text("BDT - Bangladeshi Taka"),
              trailing: settings.currencySymbol == '৳' ? Icon(Icons.check_circle, color: settings.primaryColor) : null,
              onTap: () {
                ref.read(settingsProvider.notifier).setCurrency('৳');
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text("USD - US Dollar"),
              trailing: settings.currencySymbol == '\$' ? Icon(Icons.check_circle, color: settings.primaryColor) : null,
              onTap: () {
                ref.read(settingsProvider.notifier).setCurrency('\$');
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text("INR - Indian Rupee"),
              trailing: settings.currencySymbol == '₹' ? Icon(Icons.check_circle, color: settings.primaryColor) : null,
              onTap: () {
                ref.read(settingsProvider.notifier).setCurrency('₹');
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text("AED - Dirham"),
              trailing: settings.currencySymbol == 'د.إ' ? Icon(Icons.check_circle, color: settings.primaryColor) : null,
              onTap: () {
                ref.read(settingsProvider.notifier).setCurrency('د.إ');
                Navigator.pop(context);
              },
            ),
          ],
        ),
      ),
    );
  }
}
