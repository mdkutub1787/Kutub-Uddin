import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/theme/app_theme.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../models/profile/user_profile_model.dart';
import '../../providers/app_info_provider.dart';
import '../../providers/auth_providers.dart';
import '../../providers/localization_provider.dart';
import '../../providers/profile_providers.dart';
import '../../providers/theme_provider.dart';
import '../../viewmodels/auth_viewmodel.dart';
import '../../core/widgets/brand_app_bar.dart';

class MyAccountScreen extends ConsumerWidget {
  const MyAccountScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    ref.listen<AuthState>(authViewModelProvider, (previous, next) {
      if (previous?.responseModelUser != next.responseModelUser && next.responseModelUser != null) {
        ref.read(profileViewModelProvider.notifier).loadUserProfile();
      }
    });

    final profileState = ref.watch(profileViewModelProvider);
    Theme.of(context);
    ref.watch(packageInfoProvider);
    ref.watch(localeProvider);
    ref.watch(isDarkModeProvider);

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('My Account')),
      ),
      body: SafeArea(
        child: profileState.when(
          loading: () => const Preloader(),
          error: (error, stackTrace) => SingleChildScrollView(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                const SizedBox(height: 24),
                _buildVerificationCard(context),
                const SizedBox(height: 24),
                _buildMenuCards(context, ref),
                const SizedBox(height: 24),
              ],
            ),
          ),
          data: (profileData) => SingleChildScrollView(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                _buildUserHeader(context, profileData.userProfile),
                const SizedBox(height: 10),
                _buildVerificationCard(context),
                const SizedBox(height: 20),
                _buildMenuCards(context, ref),
                const SizedBox(height: 24),
              ],
            ),
          ),
        ),
      ),
    );
  }

  String _getGreeting(BuildContext context) {
    final hour = DateTime.now().hour;
    if (hour < 12) {
      return context.tr('Good Morning');
    }
    if (hour < 17) {
      return context.tr('Good Afternoon');
    }
    if (hour < 21) {
      return context.tr('Good Evening');
    }
    return context.tr('Good Night');
  }

  Widget _buildUserHeader(BuildContext context, UserProfileModel userProfile) {
    final theme = Theme.of(context);
    final imageUrl = userProfile.image;
    final fullName = '${userProfile.firstname} ${userProfile.lastname}'.trim();

    // Calculate profile completion
    int totalPoints = 0;
    int earnedPoints = 0;

    void check(String? value) {
      totalPoints++;
      if (value != null &&
          value.trim().isNotEmpty &&
          value.trim() != '0' &&
          value.trim() != '0.00' &&
          value.trim().toLowerCase() != 'n/a') {
        earnedPoints++;
      }
    }

    check(userProfile.firstname);
    check(userProfile.lastname);
    check(userProfile.username);
    check(userProfile.email);
    check(userProfile.phone);
    check(userProfile.dateOfBirth);
    check(userProfile.placeOfBirth);
    check(userProfile.occupation);
    check(userProfile.languageId);
    check(userProfile.address);
    check(userProfile.genderType);
    check(userProfile.remitterType);
    check(userProfile.postCode);
    check(userProfile.countryId);
    check(userProfile.city);
    check(userProfile.state);
    check(userProfile.nationality);
    check(userProfile.sourceOfFund);
    check(userProfile.declarationAmount);
    check(userProfile.declarationStartDate);
    check(userProfile.declarationEndDate);
    check(userProfile.yearlyIncome);
    check(userProfile.dailyLimit);
    check(userProfile.monthlyLimit);
    check(userProfile.yearlyLimit);
    check(userProfile.remarks);
    check(userProfile.image);
    check(userProfile.documentType);
    check(userProfile.documentUpload);
    check(userProfile.documentIdNumber);
    check(userProfile.issueCountryCode);
    check(userProfile.documentIssueDate);
    check(userProfile.documentExpiryDate);

    double completionRatio = totalPoints == 0 ? 0 : earnedPoints / totalPoints;
    int percentage = (completionRatio * 100).toInt();
    final progressColor = percentage < 50
        ? theme.colorScheme.warning
        : theme.colorScheme.success;

    return GestureDetector(
      onTap: () => context.push(AppRouter.profile),
      child: Card(
        elevation: 0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16),
          side: BorderSide(color: theme.dividerColor),
        ),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 18.0),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Stack(
                  children: [
                    CircleAvatar(
                      radius: 28,
                      backgroundColor: theme.colorScheme.surfaceVariant,
                      backgroundImage: (imageUrl != null && imageUrl.isNotEmpty)
                          ? NetworkImage(imageUrl)
                          : null,
                      child: (imageUrl == null || imageUrl.isEmpty)
                          ? Icon(Icons.person, size: 28, color: theme.colorScheme.onSurfaceVariant)
                          : null,
                    ),
                    Positioned(
                      bottom: 0,
                      right: 0,
                      child: Container(
                        padding: const EdgeInsets.all(2),
                        decoration: BoxDecoration(
                          color: theme.colorScheme.primary,
                          shape: BoxShape.circle,
                          border: Border.all(color: theme.cardTheme.color!, width: 1.5),
                        ),
                        child: Icon(
                          Icons.edit,
                          size: 10,
                          color: theme.colorScheme.onPrimary,
                        ),
                      ),
                    ),
                  ],
                ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      '${_getGreeting(context)}, ${fullName.isNotEmpty ? fullName : context.tr('User')}',
                      style: theme.textTheme.titleMedium
                          ?.copyWith(fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    if (percentage < 100) ...[
                      Row(
                        children: [
                          Expanded(
                            child: Container(
                              height: 8,
                              clipBehavior: Clip.antiAlias,
                              decoration: BoxDecoration(
                                color: theme.dividerColor,
                                borderRadius: BorderRadius.circular(4),
                              ),
                              child: FractionallySizedBox(
                                widthFactor: completionRatio,
                                alignment: Alignment.centerLeft,
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: progressColor,
                                    borderRadius: BorderRadius.circular(4),
                                  ),
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(width: 12),
                          Text(
                            '$percentage%',
                            style: theme.textTheme.bodySmall?.copyWith(
                              fontWeight: FontWeight.bold,
                              color: progressColor,
                            ),
                          ),
                        ],
                      ),
                    ] else
                      Row(
                        children: [
                          Icon(Icons.check_circle,
                              color: progressColor, size: 16),
                          const SizedBox(width: 4),
                          Text(
                            context.tr('Profile Completed'),
                            style: theme.textTheme.bodySmall?.copyWith(
                              color: progressColor,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ],
                      ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildVerificationCard(BuildContext context) {
    final theme = Theme.of(context);

    return Card(
      color: theme.colorScheme.primaryContainer,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(12),
      ),
      elevation: 0,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
        child: Row(
          children: [
            Icon(Icons.shield_moon_outlined,
                color: theme.colorScheme.onPrimaryContainer),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(context.tr('Verification Level: Basic'),
                      style: TextStyle(
                          fontWeight: FontWeight.bold,
                          color: theme.colorScheme.onPrimaryContainer)),
                  Text(context.tr('Remove all restrictions'),
                      style: TextStyle(
                          color: theme.colorScheme.onPrimaryContainer.withOpacity(0.8),
                          fontSize: 12)),
                ],
              ),
            ),
            TextButton(
              onPressed: () {},
              style: TextButton.styleFrom(
                backgroundColor: theme.colorScheme.primary,
                foregroundColor: theme.colorScheme.onPrimary,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              ),
              child: Text(
                context.tr('Increase Limits'),
                textAlign: TextAlign.center,
                style: const TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 12,
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  Widget _buildMenuCards(BuildContext context, WidgetRef ref) {
    final theme = Theme.of(context);
    final menuItems = [
      {
        'icon': Icons.person_outline,
        'title': 'Personal Details',
        'onTap': () => context.push(AppRouter.profile),
      },
      {
        'icon': Icons.lock_outline,
        'title': 'Security & Privacy',
        'onTap': () => context.push(AppRouter.securityAndPrivacy),
      },
      {
        'icon': Icons.credit_card_outlined,
        'title': 'Payment Methods',
        'onTap': () => context.push(AppRouter.virtualCreditCard),
      },
      {
        'icon': Icons.qr_code_2,
        'title': 'My QR',
        'onTap': () => context.push(AppRouter.qrCode),
      },
      {
        'icon': Icons.help_outline,
        'title': 'How it Works',
        'onTap': () => context.push(AppRouter.howItWorks),
      },
      {
        'icon': Icons.info_outline_rounded,
        'title': 'About Fflipy',
        'onTap': () => context.push(AppRouter.aboutUs),
      },

    ];

    return Column(
      children: menuItems.map((item) {
        return Card(
          margin: const EdgeInsets.symmetric(vertical: 8),
          elevation: 2,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
            side: BorderSide(color: theme.dividerColor.withOpacity(0.08)),
          ),
          child: ListTile(
            leading: Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: theme.colorScheme.primaryContainer,
                borderRadius: BorderRadius.circular(8),
              ),
              child: Icon(item['icon'] as IconData,
                  color: theme.colorScheme.onPrimaryContainer),
            ),
            title: Text(context.tr(item['title'] as String),
                style: const TextStyle(fontWeight: FontWeight.w600)),
            trailing: item['trailing'] as Widget? ??
                Icon(Icons.arrow_forward_ios,
                    size: 16, color: theme.colorScheme.outline),
            onTap: item['onTap'] as VoidCallback?,
          ),
        );
      }).toList(),
    );
  }
}
