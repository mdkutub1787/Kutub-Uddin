import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/providers/profile_providers.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/widgets/brand_app_bar.dart';
import 'package:go_router/go_router.dart';
import 'package:share_plus/share_plus.dart';
import 'package:qr_flutter/qr_flutter.dart';

import '../../core/theme/app_theme.dart';

class SecurityPrivacyScreen extends ConsumerWidget {
  const SecurityPrivacyScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final profileState = ref.watch(profileViewModelProvider);
    final theme = Theme.of(context);

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('Security & Privacy')),
      ),
      body: profileState.when(
        loading: () => const Preloader(),
        error: (error, stackTrace) =>
            Center(child: Text(context.tr(ErrorHandler.getErrorMessage(error)))),
        data: (profileData) => ListView(
          padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 24.0),
          children: [
            _buildSectionHeader(
              context,
              title: context.tr('Security Settings'),
              icon: Icons.security_outlined,
            ),
            _buildSettingCard(
              context,
              icon: Icons.visibility_outlined,
              title: context.tr('Profile Visible'),
              widget: _buildStatusBadge(
                  context, profileData.userProfile.status == '1'),
            ),
            _buildSettingCard(
              context,
              icon: Icons.phonelink_lock_outlined,
              title: context.tr('Two Factor Authenticator'),
              widget: _buildStatusBadge(
                  context, profileData.userProfile.twoFaVerify == '1'),
            ),
            _buildSettingCard(
              context,
              icon: Icons.lock_outline,
              title: context.tr('Password Settings'),
              onTap: () => context.push(AppRouter.updatePassword),
            ),
            const SizedBox(height: 24),
            _buildSectionHeader(
              context,
              title: context.tr('Profile Actions'),
              icon: Icons.person_outline,
            ),
            _buildSettingCard(
              context,
              icon: Icons.share_outlined,
              title: context.tr('Referral Link'),
              onTap: () {
                if (profileData.userProfile.referralLink != null &&
                    profileData.userProfile.referralLink!.isNotEmpty) {
                  Share.share('${context.tr('Check out my referral link: ')}${profileData.userProfile.referralLink!}');
                }
              },
            ),
            _buildSettingCard(
              context,
              icon: Icons.qr_code_2,
              title: context.tr('Referral QR'),
              onTap: () {
                final referralLink = profileData.userProfile.referralLink ?? '';
                if (referralLink.isNotEmpty) {
                  showDialog(
                    context: context,
                    builder: (context) => AlertDialog(
                      title: Text(context.tr('Referral QR')),
                      content: SizedBox(
                        width: 220,
                        height: 220,
                        child: Center(
                          child: QrImageView(
                            data: referralLink,
                            size: 200.0,
                          ),
                        ),
                      ),
                      actions: [
                        TextButton(
                          onPressed: () => Navigator.of(context).pop(),
                          child: Text(context.tr('Close')),
                        ),
                        TextButton(
                          onPressed: () {
                            Share.share('${context.tr('Check out my referral link: ')}$referralLink');
                          },
                          child: Text(context.tr('Share')),
                        ),
                      ],
                    ),
                  );
                }
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSectionHeader(BuildContext context,
      {required String title, required IconData icon}) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.only(left: 4.0, bottom: 16.0),
      child: Row(
        children: [
          Icon(icon, color: theme.colorScheme.primary, size: 24),
          const SizedBox(width: 16),
          Text(
            title,
            style: theme.textTheme.titleLarge?.copyWith(
              fontWeight: FontWeight.bold,
              fontSize: 20,
              letterSpacing: 0.5,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSettingCard(BuildContext context,
      {required IconData icon,
      required String title,
      Widget? widget,
      VoidCallback? onTap}) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Card(
        elevation: 0,
        shape: RoundedRectangleBorder(
          side: BorderSide(
            color: theme.colorScheme.outline.withOpacity(0.3),
            width: 1.5,
          ),
          borderRadius: BorderRadius.circular(16),
        ),
        clipBehavior: Clip.antiAlias,
        child: ListTile(
          onTap: onTap,
          contentPadding:
              const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
          leading: Icon(icon, color: theme.colorScheme.secondary),
          title: Text(title,
              style:
                  const TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
          trailing: widget ??
              (onTap != null
                  ? Icon(Icons.arrow_forward_ios,
                      size: 16, color: theme.textTheme.bodySmall?.color)
                  : null),
          splashColor: theme.colorScheme.primary.withOpacity(0.1),
        ),
      ),
    );
  }

  Widget _buildStatusBadge(BuildContext context, bool isActive) {
    final theme = Theme.of(context);
    final Color color = isActive ? theme.colorScheme.success : theme.colorScheme.error;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: color.withOpacity(0.15),
        borderRadius: BorderRadius.circular(30),
      ),
      child: Text(
        isActive ? context.tr('Active') : context.tr('Inactive'),
        style: theme.textTheme.labelLarge?.copyWith(
          color: color,
          fontWeight: FontWeight.bold,
        ),
      ),
    );
  }
}
