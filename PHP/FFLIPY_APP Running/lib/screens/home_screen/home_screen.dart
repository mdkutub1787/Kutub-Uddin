import 'dart:async';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/theme/app_theme.dart';
import 'package:fflipy/core/widgets/brand_app_bar.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/core/widgets/empty_state_widget.dart';
import 'package:fflipy/core/widgets/wave_clipper.dart';
import 'package:fflipy/providers/app_info_provider.dart';
import 'package:fflipy/providers/auth_providers.dart';
import 'package:fflipy/providers/localization_provider.dart';
import 'package:fflipy/providers/notification_providers.dart';
import 'package:fflipy/providers/theme_provider.dart';
import 'package:fflipy/providers/transaction_providers.dart';
import 'package:fflipy/screens/home_screen/currency_rate_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:badges/badges.dart' as badges;
import 'package:share_plus/share_plus.dart';
import '../../core/utils/dialog_helper.dart';
import '../../models/profile/active_countries_model.dart' as active_country;
import '../../models/profile/user_profile_model.dart';
import '../../models/transaction_model/transaction_report_model.dart';
import '../../providers/profile_providers.dart';

class HomeScreen extends ConsumerStatefulWidget {
  const HomeScreen({super.key});

  @override
  ConsumerState<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends ConsumerState<HomeScreen> {
  bool _isBalanceVisible = false;
  bool _isCalculatorExpanded = false;
  Timer? _timer;
  final ScrollController _scrollController = ScrollController();

  @override
  void dispose() {
    _timer?.cancel();
    _scrollController.dispose();
    super.dispose();
  }

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(transactionViewModelProvider.notifier).getTransactionReport();
      ref.read(notificationViewModelProvider.notifier).getNotifications();
    });
    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
          _scrollController.position.maxScrollExtent) {
        final viewModel = ref.read(transactionViewModelProvider.notifier);
        final currentState = ref.read(transactionViewModelProvider);
        final currentPage =
            currentState.transactionReport?.data.transactions.currentPage ?? 0;
        final lastPage =
            currentState.transactionReport?.data.transactions.lastPage ?? 1;

        if (currentPage < lastPage && !currentState.isLoadingMore) {
          viewModel.getTransactionReport(page: currentPage + 1);
        }
      }
    });
  }

  void _toggleBalance() {
    setState(() {
      _isBalanceVisible = !_isBalanceVisible;
    });

    if (_isBalanceVisible) {
      _timer?.cancel();
      _timer = Timer(const Duration(seconds: 3), () {
        if (mounted) {
          setState(() {
            _isBalanceVisible = false;
          });
        }
      });
    } else {
      _timer?.cancel();
    }
  }

  Future<void> _handleLogout(BuildContext context, WidgetRef ref) async {
    final shouldLogout = await DialogHelper.showConfirmationDialog(
      context: context,
      title: context.tr('Log Out'),
      message: context.tr('Are you sure you want to log out?'),
      confirmText: context.tr('Yes'),
      cancelText: context.tr('Cancel'),
    );

    if (shouldLogout == true) {
      final message = await ref.read(authViewModelProvider.notifier).logout();
      ref.invalidate(profileViewModelProvider);
      if (context.mounted) {
        if (message != null) {
          DialogHelper.showSnackBar(context, context.tr(message));
        }
        context.go(AppRouter.login);
      }
    }
  }

  String _getStatusText(String status) {
    switch (status) {
      case '1':
        return context.tr('Pending');
      case '2':
        return context.tr('Cancelled');
      case '3':
        return context.tr('Paid');
      default:
        return context.tr('Unknown');
    }
  }

  Color _getStatusColor(BuildContext context, String status) {
    final theme = Theme.of(context);
    switch (status) {
      case '1':
        return theme.colorScheme.warning;
      case '2':
        return theme.colorScheme.error;
      case '3':
        return theme.colorScheme.success;
      default:
        return theme.colorScheme.outline;
    }
  }

  Widget _getBankIcon(BuildContext context, String bankName) {
    final theme = Theme.of(context);
    String name = bankName.toLowerCase();
    if (name.contains('bkash')) {
      return Image.asset('assets/preloader/bkash.png',
          width: 30, height: 30, fit: BoxFit.contain);
    } else if (name.contains('rocket')) {
      return Image.asset('assets/preloader/rocket.png',
          width: 30, height: 30, fit: BoxFit.contain);
    } else if (name.contains('nagad')) {
      return Image.asset('assets/preloader/nagad.png',
          width: 30, height: 30, fit: BoxFit.contain);
    } else if (name.contains('bank')) {
      return Image.asset('assets/preloader/bank.png',
          width: 30, height: 30, fit: BoxFit.contain);
    } else if (name.contains('card')) {
      return Icon(Icons.credit_card, color: theme.colorScheme.tertiary, size: 30);
    }
    return Icon(Icons.account_balance_wallet,
        color: theme.colorScheme.onSurface.withValues(alpha: 0.5), size: 30);
  }

  @override
  Widget build(BuildContext context) {
    ref.listen(profileViewModelProvider, (previous, next) {
      final wasLoading = previous == null || previous.isLoading;
      if (wasLoading && !next.isLoading && next.hasValue) {
        final userProfile = next.value!.userProfile;
        if (userProfile.firstname == null ||
            userProfile.lastname == null ||
            userProfile.username == null ||
            userProfile.email == null ||
            userProfile.phone == null ||
            userProfile.dateOfBirth == null ||
            userProfile.placeOfBirth == null ||
            userProfile.nationality == null ||
            userProfile.genderType == null ||
            userProfile.documentType == null ||
            userProfile.documentUpload == null ||
            userProfile.documentIdNumber == null ||
            userProfile.issueCountryCode == null ||
            userProfile.documentIssueDate == null ||
            userProfile.documentExpiryDate == null ||
            userProfile.address == null ||
            userProfile.countryId == null ||
            userProfile.city == null ||
            userProfile.state == null ||
            userProfile.postCode == null ||
            userProfile.image == null) {
          DialogHelper.showSnackBar(
              context, context.tr('Please Complete Your Profile'));
          context.go(AppRouter.updateProfile, extra: userProfile);
        }
      }
    });

    final theme = Theme.of(context);
    final profileState = ref.watch(profileViewModelProvider);
    final countriesState = ref.watch(activeCountriesProvider);
    final transactionState = ref.watch(transactionViewModelProvider);
    final notificationState = ref.watch(notificationViewModelProvider);
    final unreadCount = notificationState.notificationResponse?.data.length ?? 0;
    final authState = ref.watch(authViewModelProvider);
    
    final allTransactions = transactionState.transactionReport?.data.transactions.data ?? [];
    final transactions = allTransactions.where((tx) => tx.status != '1').toList();
    final pendingTransactions = allTransactions.where((tx) => tx.status == '1').toList();

    final bool isProfileLoading = profileState.isLoading;
    final bool isCountriesLoading = countriesState.isLoading;
    final bool isTransactionsLoading =
        transactionState.isLoading && allTransactions.isEmpty;

    if (isProfileLoading || isCountriesLoading || isTransactionsLoading) {
      return const Scaffold(body: Center(child: Preloader()));
    }

    if (profileState.hasError) {
      return Scaffold(body: Center(child: Text(context.tr(ErrorHandler.getErrorMessage(profileState.error)))));
    }

    if (!profileState.hasValue) {
      return const Scaffold(body: Center(child: Preloader()));
    }

    final profileData = profileState.value!;
    final userProfile = profileData.userProfile;
    final fullName = '${userProfile.firstname} ${userProfile.lastname}'.trim();
    final imageUrl = userProfile.image;

    final userBalance = authState.responseModelUser?.user?.balance ?? '0.00';

    String currencyCode = '€';
    if (countriesState.hasValue) {
      final countries = countriesState.value!;
      final userCountryId = userProfile.countryId;
      if (userCountryId != null) {
        try {
          final country = countries.firstWhere(
                  (c) => c.id.toString() == userCountryId,
              orElse: () => countries.first);
          currencyCode = country.code;
        } catch (e) {
          debugPrint(
              "Could not find country to get currency code for '$userCountryId'");
        }
      }
    }

    final isDark = ref.watch(isDarkModeProvider);
    final currentLocale = ref.watch(localeProvider);

    return Scaffold(
        appBar: BrandAppBar(
          height: 100.0,
          leadingWidth: 100,
          leading: Builder(
            builder: (context) => Padding(
              padding: const EdgeInsets.only(left: 10.0),
              child: GestureDetector(
                onTap: () => Scaffold.of(context).openDrawer(),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Stack(
                      alignment: Alignment.bottomLeft,
                      children: [
                        CircleAvatar(
                          radius: 20,
                          backgroundColor: theme.colorScheme.onPrimary,
                          child: ClipOval(
                            child: (imageUrl != null && imageUrl.isNotEmpty)
                                ? CachedNetworkImage(
                              imageUrl: imageUrl,
                              fit: BoxFit.cover,
                              width: 70,
                              height: 70,
                              placeholder: (context, url) => const Center(
                                  child: CircularProgressIndicator(
                                      strokeWidth: 2.0)),
                              errorWidget: (context, url, error) =>
                              const Icon(Icons.person, size: 20),
                            )
                                : const Icon(Icons.person, size: 20),
                          ),
                        ),
                        Container(
                          padding: const EdgeInsets.all(2),
                          decoration: BoxDecoration(
                            color: theme.colorScheme.surface,
                            shape: BoxShape.circle,
                          ),
                          child: Icon(Icons.menu,
                              color: theme.colorScheme.onSurface, size: 10),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
          title: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
              GestureDetector(
                onTap: _toggleBalance,
                child: Container(
                  padding: const EdgeInsets.symmetric(
                      horizontal: 12,
                      vertical: 6),
                  decoration: BoxDecoration(
                      color: theme.colorScheme.surface,
                      borderRadius: BorderRadius.circular(50),
                      boxShadow: [BoxShadow(color: theme.shadowColor.withValues(alpha: 0.1), blurRadius: 5, offset: const Offset(0, 2))]
                  ),
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Image.asset('assets/logo/logo.png', height: 20),
                      const SizedBox(width: 8),
                      AnimatedCrossFade(
                        firstChild: Text(
                          context.tr('Balance'),
                          style: theme.textTheme.titleMedium?.copyWith(
                            color: theme.colorScheme.secondary,
                            fontWeight: FontWeight.bold,
                            letterSpacing: 2
                          ),
                        ),
                        secondChild: Text(
                          '$currencyCode$userBalance',
                          style: theme.textTheme.titleMedium
                              ?.copyWith(fontWeight: FontWeight.bold),
                        ),
                        crossFadeState: _isBalanceVisible
                            ? CrossFadeState.showSecond
                            : CrossFadeState.showFirst,
                        duration: const Duration(milliseconds: 300),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
          actions: [
            Padding(
              padding: const EdgeInsets.only(right: 16.0, top: 8.0),
              child: badges.Badge(
                position: badges.BadgePosition.topEnd(top: -12, end: -12),
                badgeContent: Text(
                  unreadCount.toString(),
                  style: TextStyle(color: theme.colorScheme.onError, fontSize: 10),
                ),
                showBadge: unreadCount > 0,
                badgeStyle: badges.BadgeStyle(
                  badgeColor: theme.colorScheme.error,
                ),
                child: IconButton(
                  onPressed: () async {
                    await context.push(AppRouter.notifications);
                    ref.read(notificationViewModelProvider.notifier).getNotifications();
                  },
                  icon: Icon(Icons.notifications,
                      size: 30, color: theme.colorScheme.onSurface),
                  padding: EdgeInsets.zero,
                  constraints: const BoxConstraints(),
                ),
              ),
            ),
          ],
        ),
        drawer: _buildDrawer(context, userProfile, fullName, imageUrl, countriesState.asData?.value ?? [], isDark, currentLocale),
        floatingActionButton: Container(
          width: 56,
          height: 56,
          decoration: BoxDecoration(
            gradient: LinearGradient(
              colors: [
                theme.colorScheme.primary.withValues(alpha: 0.85),
                theme.colorScheme.primary,
              ],
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
            ),
            shape: BoxShape.circle,
            boxShadow: [
              BoxShadow(
                color: theme.colorScheme.primary.withValues(alpha: 0.2),
                blurRadius: 15,
                offset: const Offset(0, 8),
              ),
            ],
          ),
          child: FloatingActionButton(
            heroTag: 'helpFAB',
            onPressed: () => context.push(AppRouter.helpSupport),
            backgroundColor: Colors.transparent,
            elevation: 0,
            highlightElevation: 0,
            child: const Icon(Icons.headset_mic_rounded, color: Colors.white, size: 26),
          ),
        ),
        body: RefreshIndicator(
          onRefresh: () async {
            await ref.read(transactionViewModelProvider.notifier).getTransactionReport(page: 1);
            await ref.read(profileViewModelProvider.notifier).loadUserProfile();
          },
          child: SingleChildScrollView(
            controller: _scrollController,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // 1. Welcome Section (Centered & Enhanced)
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 8, 20, 1),
                  child: Center(
                    child: Column(
                      children: [
                        RichText(
                          textAlign: TextAlign.center,
                          text: TextSpan(
                            style: theme.textTheme.headlineSmall?.copyWith(
                              fontWeight: FontWeight.bold,
                              color: theme.colorScheme.onSurface,
                            ),
                            children: [
                              TextSpan(
                                text: '${_getGreeting(context)}, ',
                                style: TextStyle(color: theme.colorScheme.primary),
                              ),
                              TextSpan(text: fullName),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                  child: Container(
                    padding: const EdgeInsets.symmetric(vertical: 24, horizontal: 8),
                    decoration: BoxDecoration(
                      color: theme.colorScheme.surface,
                      borderRadius: BorderRadius.circular(24),
                      boxShadow: [
                        BoxShadow(
                          color: theme.colorScheme.primary.withValues(alpha: 0.05),
                          blurRadius: 20,
                          offset: const Offset(0, 8),
                        ),
                      ],
                      border: Border.all(
                        color: theme.colorScheme.primary.withValues(alpha: 0.05),
                        width: 1,
                      ),
                    ),
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: [
                            _buildCircleAction(
                              context,
                              theme,
                              icon: Icons.send_rounded,
                              label: context.tr('Send Money'),
                              onTap: () => context.push(AppRouter.sendMoney),
                              color: const Color(0xFFFDE8E8),
                              iconColor: const Color(0xFFE54B4B),
                            ),
                            _buildCircleAction(
                              context,
                              theme,
                              icon: Icons.qr_code_scanner_rounded,
                              label: context.tr('Receive'),
                              onTap: () => context.push(AppRouter.qrCode),
                              color: const Color(0xFFE8F5E9),
                              iconColor: const Color(0xFF2E7D32),
                            ),
                            _buildCircleAction(
                              context,
                              theme,
                              icon: Icons.person_add_alt_1_rounded,
                              label: context.tr('Add Beneficiary'),
                              onTap: () => context.push(AppRouter.addBeneficiary),
                              color: const Color(0xFFE3F2FD),
                              iconColor: const Color(0xFF1976D2),
                            ),
                          ],
                        ),
                        const SizedBox(height: 24),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: [
                            _buildCircleAction(
                              context,
                              theme,
                              icon: Icons.people_alt_rounded,
                              label: context.tr('Beneficiaries'),
                              onTap: () => context.push(AppRouter.beneficiary),
                              color: const Color(0xFFF3E5F5),
                              iconColor: const Color(0xFF7B1FA2),
                            ),
                            _buildCircleAction(
                              context,
                              theme,
                              icon: Icons.receipt_long_rounded,
                              label: context.tr('Invoices'),
                              onTap: () => context.push(AppRouter.invoice),
                              color: const Color(0xFFFFF3E0),
                              iconColor: const Color(0xFFEF6C00),
                            ),
                            _buildCircleAction(
                              context,
                              theme,
                              icon: Icons.insert_chart_rounded,
                              label: context.tr('Analytics'),
                              onTap: () => context.push(AppRouter.analytics),
                              color: const Color(0xFFE0F7FA),
                              iconColor: const Color(0xFF00838F),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
                // 2. Pending Transactions (Short View)
                if (pendingTransactions.isNotEmpty) ...[
                  Padding(
                    padding: const EdgeInsets.fromLTRB(20, 8, 20, 4),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          context.tr('Pending Transactions'),
                          style: theme.textTheme.titleSmall?.copyWith(fontWeight: FontWeight.bold),
                        ),
                        GestureDetector(
                          onTap: () => context.push(AppRouter.trackTransfer),
                          child: Text(
                            context.tr('Track All'),
                            style: TextStyle(color: theme.colorScheme.primary, fontSize: 12, fontWeight: FontWeight.bold),
                          ),
                        ),
                      ],
                    ),
                  ),
                  SizedBox(
                    height: 90,
                    child: ListView.builder(
                      scrollDirection: Axis.horizontal,
                      padding: const EdgeInsets.symmetric(horizontal: 20),
                      itemCount: pendingTransactions.length,
                      itemBuilder: (context, index) => _buildPendingCardShort(context, theme, pendingTransactions[index]),
                    ),
                  ),
                ],

                // 4. Peeking Calculator Card (Expandable)
                Padding(
                  padding: const EdgeInsets.symmetric(vertical: 6),
                  child: Container(
                    margin: const EdgeInsets.symmetric(horizontal: 20),
                    decoration: BoxDecoration(
                      color: theme.colorScheme.surface,
                      borderRadius: BorderRadius.circular(24),
                      border: Border.all(
                        color: theme.colorScheme.primary.withValues(alpha: 0.3),
                        width: 1.5,
                      ),
                      boxShadow: [
                        BoxShadow(
                          color: theme.colorScheme.primary.withValues(alpha: 0.06),
                          blurRadius: 20,
                          offset: const Offset(0, 10),
                        ),
                      ],
                    ),
                    clipBehavior: Clip.antiAlias,
                    child: Column(
                      children: [
                        InkWell(
                          onTap: () {
                            setState(() {
                              _isCalculatorExpanded = !_isCalculatorExpanded;
                            });
                          },
                          child: Container(
                            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                            decoration: BoxDecoration(
                              gradient: LinearGradient(
                                colors: [
                                  theme.colorScheme.primary.withValues(alpha: 0.05),
                                  theme.colorScheme.surface,
                                ],
                                begin: Alignment.topLeft,
                                end: Alignment.bottomRight,
                              ),
                            ),
                            child: Row(
                              children: [
                                Container(
                                  padding: const EdgeInsets.all(12),
                                  decoration: BoxDecoration(
                                    color: theme.colorScheme.primary.withValues(alpha: 0.15),
                                    borderRadius: BorderRadius.circular(16),
                                  ),
                                  child: Icon(
                                    Icons.calculate_rounded,
                                    color: theme.colorScheme.primary,
                                    size: 28,
                                  ),
                                ),
                                const SizedBox(width: 16),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                        Row(
                                          children: [
                                            const _PulsingLiveBadge(),
                                            const SizedBox(width: 10),
                                            Text(
                                              context.tr('Calculate Rate'),
                                              style: theme.textTheme.titleMedium?.copyWith(
                                                fontWeight: FontWeight.bold,
                                                letterSpacing: 0.2,
                                              ),
                                            ),
                                          ],
                                        ),
                                      const SizedBox(height: 2),
                                      Text(
                                        context.tr('Check exchange rates instantly'),
                                        style: theme.textTheme.bodySmall?.copyWith(
                                          color: theme.colorScheme.onSurface.withValues(alpha: 0.5),
                                          fontWeight: FontWeight.w500,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                                AnimatedRotation(
                                  turns: _isCalculatorExpanded ? 0.5 : 0,
                                  duration: const Duration(milliseconds: 300),
                                  child: Container(
                                    padding: const EdgeInsets.all(4),
                                    decoration: BoxDecoration(
                                      color: theme.colorScheme.primary.withValues(alpha: 0.1),
                                      shape: BoxShape.circle,
                                    ),
                                    child: Icon(
                                      Icons.keyboard_arrow_down_rounded,
                                      color: theme.colorScheme.primary,
                                      size: 20,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                        if (_isCalculatorExpanded)
                          Divider(
                            height: 1,
                            thickness: 1,
                            color: theme.colorScheme.primary.withValues(alpha: 0.05),
                          ),
                        AnimatedCrossFade(
                          firstChild: const SizedBox.shrink(),
                          secondChild: const Padding(
                            padding: EdgeInsets.all(16),
                            child: CurrentRateScreen(),
                          ),
                          crossFadeState: _isCalculatorExpanded
                            ? CrossFadeState.showSecond
                            : CrossFadeState.showFirst,
                          duration: const Duration(milliseconds: 300),
                        ),
                      ],
                    ),
                  ),
                ),

                // 5. Last 5 Activities
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 12, 20, 4),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        context.tr('Recent Activity'),
                        style: theme.textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold),
                      ),
                      TextButton(
                        onPressed: () => context.push(AppRouter.activity),
                        child: Text(context.tr('View All')),
                      ),
                    ],
                  ),
                ),

                transactions.isEmpty
                    ? Padding(
                  padding: const EdgeInsets.symmetric(vertical: 32.0),
                  child: EmptyStateWidget(
                    message: context.tr('No recent transactions'),
                    lottieSize: 100,
                  ),
                )
                    : ListView.builder(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  padding: const EdgeInsets.symmetric(horizontal: 20.0),
                  itemCount: transactions.take(5).length,
                  itemBuilder: (context, index) {
                    return _buildTransactionTile(context, theme, transactions[index]);
                  },
                ),

                const SizedBox(height: 20),
              ],
            ),
          ),
        )
    );
  }

  Widget _buildCircleAction(BuildContext context, ThemeData theme,
      {required IconData icon, required String label, required VoidCallback onTap, required Color color, required Color iconColor}) {
    return Column(
      children: [
        InkWell(
          onTap: onTap,
          borderRadius: BorderRadius.circular(32),
          child: Container(
            width: 64,
            height: 64,
            decoration: BoxDecoration(
              color: color,
              shape: BoxShape.circle,
              boxShadow: [
                BoxShadow(
                  color: iconColor.withValues(alpha: 0.12),
                  blurRadius: 12,
                  offset: const Offset(0, 4),
                ),
              ],
            ),
            child: Icon(icon, color: iconColor, size: 24),
          ),
        ),
        const SizedBox(height: 8),
        SizedBox(
          width: 84,
          child: Text(
            label,
            style: theme.textTheme.labelMedium?.copyWith(
              fontWeight: FontWeight.w600,
              fontSize: 10,
              letterSpacing: 0.1,
              color: theme.colorScheme.onSurface.withValues(alpha: 0.8),
            ),
            textAlign: TextAlign.center,
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    );
  }

  Widget _buildPendingCardShort(BuildContext context, ThemeData theme, TransactionModel tx) {
    final screenWidth = MediaQuery.of(context).size.width;
    return GestureDetector(
      onTap: () => context.pushNamed('trackingDetails', extra: tx),
      child: Container(
        width: screenWidth * 0.44, // Sets width to approx 44% of screen to show ~2 cards
        margin: const EdgeInsets.only(right: 12, bottom: 8, top: 4),
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
        decoration: BoxDecoration(
          color: theme.colorScheme.surface,
          borderRadius: BorderRadius.circular(16),
          border: Border.all(color: theme.dividerColor.withValues(alpha: 0.1)),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.02),
              blurRadius: 4,
              offset: const Offset(0, 2),
            )
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              tx.recipientName,
              style: theme.textTheme.bodySmall?.copyWith(
                fontWeight: FontWeight.bold,
                fontSize: 13,
                color: theme.colorScheme.onSurface,
              ),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
            const SizedBox(height: 2),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  context.tr('Pending'),
                  style: TextStyle(
                    color: Colors.orange.shade700,
                    fontSize: 10,
                    fontWeight: FontWeight.w600,
                  ),
                ),
                Text(
                  '${tx.sendAmount} ${tx.sendCurr}',
                  style: theme.textTheme.bodySmall?.copyWith(
                    fontWeight: FontWeight.bold,
                    fontSize: 11,
                    color: theme.colorScheme.primary,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildTransactionTile(BuildContext context, ThemeData theme, TransactionModel transfer) {
    final statusText = _getStatusText(transfer.status);
    final statusColor = _getStatusColor(context, transfer.status);

    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: theme.colorScheme.surface,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: theme.dividerColor.withValues(alpha: 0.05)),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
        leading: Container(
          width: 48,
          height: 48,
          decoration: BoxDecoration(
            color: theme.colorScheme.surfaceVariant.withValues(alpha: 0.3),
            shape: BoxShape.circle,
          ),
          child: Center(child: _getBankIcon(context, transfer.recipientBank)),
        ),
        title: Text(
          transfer.recipientName,
          style: theme.textTheme.titleSmall?.copyWith(fontWeight: FontWeight.bold),
        ),
        subtitle: Text(
          '${transfer.sendAmount} ${transfer.sendCurr}',
          style: theme.textTheme.bodySmall,
        ),
        trailing: Column(
          crossAxisAlignment: CrossAxisAlignment.end,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              statusText,
              style: TextStyle(color: statusColor, fontWeight: FontWeight.bold, fontSize: 12),
            ),
            const SizedBox(height: 4),
            Text(
              transfer.createdAt?.split(' ').first ?? '',
              style: theme.textTheme.bodySmall?.copyWith(fontSize: 10),
            ),
          ],
        ),
        onTap: () {
          context.pushNamed('trackingDetails', extra: transfer);
        },
      ),
    );
  }

  String _getGreeting(BuildContext context) {
    final hour = DateTime.now().hour;
    if (hour < 12) return context.tr('Good Morning');
    if (hour < 17) return context.tr('Good Afternoon');
    if (hour < 21) return context.tr('Good Evening');
    return context.tr('Good Night');
  }

  IconData _getGreetingIcon() {
    final hour = DateTime.now().hour;
    if (hour < 12) return Icons.wb_sunny_rounded; // Morning
    if (hour < 17) return Icons.wb_cloudy_rounded; // Afternoon
    if (hour < 21) return Icons.wb_twilight_rounded; // Evening
    return Icons.nights_stay_rounded; // Night
  }

  Widget _buildLanguageSelector(BuildContext context, ThemeData theme, Locale currentLocale) {
    return PopupMenuButton<String>(
      onSelected: (String languageCode) {
        if (languageCode == 'en') {
          ref.read(localeProvider.notifier).setEnglish();
        } else if (languageCode == 'es') {
          ref.read(localeProvider.notifier).setSpanish();
        } else {
          ref.read(localeProvider.notifier).setBangla();
        }
      },
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(12),
      ),
      offset: const Offset(0, 40),
      itemBuilder: (BuildContext context) => <PopupMenuEntry<String>>[
        PopupMenuItem<String>(
          value: 'en',
          child: Row(
            children: [
              const Text('🇺🇸', style: TextStyle(fontSize: 20)),
              const SizedBox(width: 10),
              Text(context.tr('English')),
            ],
          ),
        ),
        PopupMenuItem<String>(
          value: 'es',
          child: Row(
            children: [
              const Text('🇪🇸', style: TextStyle(fontSize: 20)),
              const SizedBox(width: 10),
              Text(context.tr('Spanish')),
            ],
          ),
        ),
        PopupMenuItem<String>(
          value: 'bn',
          child: Row(
            children: [
              const Text('🇧🇩', style: TextStyle(fontSize: 20)),
              const SizedBox(width: 10),
              Text(context.tr('Bangla')),
            ],
          ),
        ),
      ],
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(8),
          border: Border.all(
            color: theme.colorScheme.outline.withValues(alpha: 0.1),
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.05),
              blurRadius: 4,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              currentLocale.languageCode == 'en' ? '🇺🇸' : currentLocale.languageCode == 'es' ? '🇪🇸' : '🇧🇩',
              style: const TextStyle(fontSize: 16),
            ),
            const SizedBox(width: 8),
            Text(
              currentLocale.languageCode == 'en'
                  ? 'EN'
                  : currentLocale.languageCode == 'es'
                      ? 'ES'
                      : 'BN',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: theme.colorScheme.onSurface,
                fontSize: 13,
              ),
            ),
            const SizedBox(width: 4),
            Icon(Icons.arrow_drop_down, size: 20, color: theme.colorScheme.onSurface),
          ],
        ),
      ),
    );
  }

  Widget _buildDrawer(BuildContext context, UserProfileModel userProfile, String fullName, String? imageUrl, List<active_country.Country> countries, bool isDark, Locale currentLocale) {
    final theme = Theme.of(context);
    final currentLanguage = currentLocale.languageCode == 'en' ? 'Eng' : currentLocale.languageCode == 'es' ? 'Esp' : 'Bn';
    
    return Drawer(
      width: MediaQuery.of(context).size.width * 0.75,
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          _buildDrawerHeader(context, userProfile, fullName, imageUrl, countries),
          _buildDrawerItem(
            context,
            icon: Icons.person_pin_rounded,
            title: context.tr('My Account'),
            onTap: () => context.push(AppRouter.myAccount),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: Icons.analytics_outlined,
            title: context.tr('Analytics'),
            onTap: () => context.push(AppRouter.analytics),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: Icons.send_outlined,
            title: context.tr('Send Money'),
            onTap: () => context.push(AppRouter.sendMoney),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: Icons.people_outline_rounded,
            title: context.tr('Beneficiaries'),
            onTap: () => context.push(AppRouter.beneficiary),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: Icons.track_changes_rounded,
            title: context.tr('Track Transfer'),
            onTap: () => context.push(AppRouter.trackTransfer),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: Icons.receipt_long_outlined,
            title: context.tr('Payment Invoice'),
            onTap: () => context.push(AppRouter.invoice),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: Icons.history_rounded,
            title: context.tr('Reports'),
            onTap: () => context.push(AppRouter.activity),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: Icons.support_agent_rounded,
            title: context.tr('Help & Support'),
            onTap: () => context.push(AppRouter.helpSupport),
            isHighlighted: true,
          ),
          const Divider(thickness: 1, height: 24, indent: 20, endIndent: 20),
          _buildDrawerItem(
            context,
            icon: Icons.language_rounded,
            title: context.tr('Language'),
            trailing: _buildLanguageSelector(context, theme, currentLocale),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: isDark ? Icons.light_mode_rounded : Icons.dark_mode_rounded,
            title: context.tr('Dark Mode'),
            trailing: Switch(
              value: isDark,
              onChanged: (val) => ref.read(themeModeProvider.notifier).toggleTheme(),
              activeColor: theme.colorScheme.primary,
            ),
            isHighlighted: true,
          ),
          _buildDrawerItem(
            context,
            icon: Icons.logout_rounded,
            title: context.tr('Log Out'),
            color: Colors.red,
            onTap: () => _handleLogout(context, ref),
            isHighlighted: true,
          ),
          const SizedBox(height: 10),
          _buildInviteSection(context, userProfile),
          _buildFooter(context, ref),
        ],
      ),
    );
  }

  Widget _buildDrawerHeader(BuildContext context, UserProfileModel userProfile, String fullName, String? imageUrl, List<active_country.Country> countries) {
    final theme = Theme.of(context);
    final country = countries.firstWhere((c) => c.id.toString() == userProfile.countryId, orElse: () => countries.first);
    final phoneNumber = userProfile.phone ?? 'N/A';

    return UserAccountsDrawerHeader(
      accountName: Text(
        fullName,
        style: TextStyle(
          fontSize: 18,
          fontWeight: FontWeight.bold,
          color: theme.colorScheme.onPrimary,
        ),
      ),
      accountEmail: Row(
        children: [
          if (country.flag.isNotEmpty)
            CachedNetworkImage(
              imageUrl: country.flag,
              width: 24,
              height: 18,
              fit: BoxFit.cover,
              placeholder: (context, url) =>
                  Image.asset('assets/logo/logo.png', height: 16, width: 24),
              errorWidget: (context, url, error) =>
                  Image.asset('assets/logo/logo.png', height: 16, width: 24),
            )
          else
            Image.asset('assets/logo/logo.png', height: 16, width: 24),
          const SizedBox(width: 8),
          Text(
            phoneNumber,
            style: TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.bold,
              color: theme.colorScheme.onPrimary,
            ),
          ),
        ],
      ),
      currentAccountPicture: GestureDetector(
        onTap: () {
          context.push(AppRouter.profile);
        },
        child: CircleAvatar(
          backgroundColor: theme.colorScheme.onPrimary,
          child: ClipOval(
            child: (imageUrl != null && imageUrl.isNotEmpty)
                ? CachedNetworkImage(
                    imageUrl: imageUrl,
                    fit: BoxFit.cover,
                    width: 70,
                    height: 70,
                    placeholder: (context, url) => const Center(
                        child: CircularProgressIndicator(strokeWidth: 2.0)),
                    errorWidget: (context, url, error) =>
                        const Icon(Icons.person, size: 40),
                  )
                : const Icon(Icons.person, size: 40),
          ),
        ),
      ),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [theme.colorScheme.topBarGradientLeft, theme.colorScheme.topBarGradientRight],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
      ),
    );
  }

  Widget _buildDrawerItem(BuildContext context, {required IconData icon, required String title, VoidCallback? onTap, Color? color, Widget? trailing, bool isHighlighted = false}) {
    final theme = Theme.of(context);
    final effectiveColor = color ?? theme.colorScheme.primary;
    
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
      child: ListTile(
        tileColor: isHighlighted ? effectiveColor.withValues(alpha: 0.08) : null,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        leading: Container(
          padding: const EdgeInsets.all(8),
          decoration: BoxDecoration(
            color: isHighlighted ? Colors.white : effectiveColor.withValues(alpha: 0.1),
            borderRadius: BorderRadius.circular(10),
          ),
          child: Icon(icon, color: effectiveColor, size: 22),
        ),
        title: Text(title, style: TextStyle(color: effectiveColor, fontWeight: FontWeight.bold, fontSize: 14)),
        trailing: trailing ?? Icon(Icons.chevron_right_rounded, size: 20, color: effectiveColor.withValues(alpha: 0.5)),
        onTap: onTap,
        dense: true,
      ),
    );
  }

  Widget _buildInviteSection(BuildContext context, UserProfileModel userProfile) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
      child: Column(
        children: [
          Image.asset(
            'assets/logo/invite-now-illus.png',
            height: 80,
            fit: BoxFit.contain,
          ),
          const SizedBox(height: 6),
          Text(
            context.tr('Invite your friend and get \$25'),
            textAlign: TextAlign.center,
            style: theme.textTheme.titleSmall?.copyWith(
              fontWeight: FontWeight.bold,
              fontSize: 13,
            ),
          ),
          const SizedBox(height: 10),
          SizedBox(
            width: 120,
            child: ElevatedButton(
              onPressed: () {
                final referralLink = userProfile.referralLink ?? '';
                if (referralLink.isNotEmpty) {
                  Share.share('${context.tr('Check out my referral link: ')}$referralLink');
                } else {
                  DialogHelper.showSnackBar(context, context.tr('No referral link found.'));
                }
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFFE91E63),
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20),
                ),
                padding: const EdgeInsets.symmetric(vertical: 8),
                elevation: 0,
              ),
              child: Text(
                context.tr('Invite Now'),
                style: const TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 13,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildFooter(BuildContext context, WidgetRef ref) {
    final theme = Theme.of(context);
    final packageInfoAsync = ref.watch(packageInfoProvider);
    return Padding(
      padding: const EdgeInsets.only(bottom: 20, top: 10),
      child: packageInfoAsync.when(
        data: (info) => Column(
          children: [
            Text('${context.tr("Version")} ${info.version}', style: theme.textTheme.bodySmall),
            Text('© ${DateTime.now().year} FFlipy. ${context.tr("All rights reserved.")}', style: const TextStyle(fontSize: 10, color: Colors.grey)),
          ],
        ),
        loading: () => const SizedBox(),
        error: (_, __) => const SizedBox(),
      ),
    );
  }
}

class _PulsingLiveBadge extends StatefulWidget {
  const _PulsingLiveBadge();

  @override
  State<_PulsingLiveBadge> createState() => _PulsingLiveBadgeState();
}

class _PulsingLiveBadgeState extends State<_PulsingLiveBadge> with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _animation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1000),
    )..repeat(reverse: true);
    _animation = Tween<double>(begin: 0.4, end: 1.0).animate(CurvedAnimation(
      parent: _controller,
      curve: Curves.easeInOut,
    ));
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FadeTransition(
      opacity: _animation,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
        decoration: BoxDecoration(
          color: Colors.red.withOpacity(0.1),
          borderRadius: BorderRadius.circular(6),
          border: Border.all(
            color: Colors.red.withOpacity(0.3),
            width: 1,
          ),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 6,
              height: 6,
              decoration: BoxDecoration(
                color: Colors.red,
                shape: BoxShape.circle,
                boxShadow: [
                  BoxShadow(
                    color: Colors.red.withOpacity(0.5),
                    blurRadius: 4,
                    spreadRadius: 1,
                  ),
                ],
              ),
            ),
            const SizedBox(width: 5),
            Text(
              context.tr('LIVE'),
              style: const TextStyle(
                color: Colors.red,
                fontSize: 9,
                fontWeight: FontWeight.w900,
                letterSpacing: 0.8,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

