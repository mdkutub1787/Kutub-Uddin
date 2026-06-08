import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/screens/beneficiary/update_beneficiary_screen.dart';
import 'package:fflipy/screens/home_screen/currency_rate_screen.dart';
import 'package:fflipy/screens/home_screen/qr_code_screen.dart';
import 'package:fflipy/screens/menu/analytics_screen.dart';
import 'package:fflipy/screens/home_screen/home_screen.dart';
import 'package:fflipy/screens/invoice/invoice_details_screen.dart';
import 'package:fflipy/screens/invoice/invoice_screen.dart';
import 'package:fflipy/screens/send_money/send_money_screen.dart';
import 'package:fflipy/screens/send_money/send_money_success_screen.dart';
import 'package:fflipy/screens/transaction_screen/transaction_report_screen.dart';
import 'package:fflipy/screens/auth_screen/update_password_screen.dart';
import 'package:fflipy/screens/track_transfer/track_transfer_screen.dart';
import 'package:fflipy/screens/track_transfer/tracking_details_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../models/profile/user_profile_model.dart';
import '../../models/transaction_model/transaction_report_model.dart';
import '../../screens/auth_screen/forgot_password_screen.dart';
import '../../screens/auth_screen/login_screen.dart';
import '../../screens/auth_screen/otp_verification_screen.dart';
import '../../screens/profile_screen/profile_screen.dart';
import '../../screens/profile_screen/update_profile_screen.dart';
import '../../screens/beneficiary/add_beneficiary_screen.dart';
import '../../screens/beneficiary/beneficiary_screen.dart';
import '../../screens/help_and_support/help_and_support_screen.dart';
import '../../screens/home_screen/notifications_screen.dart';
import '../../screens/home_screen/search_screen.dart';
import '../../screens/home_screen/virtual_credit_card_screen.dart';
import '../../screens/menu/about_us_screen.dart';
import '../../screens/menu/language_settings_screen.dart';
import '../../screens/menu/my_account_screen.dart';
import '../../screens/menu/security_privacy_screen.dart';
import '../../screens/menu/how_it_works_screen.dart';
import '../../screens/splash_screen/onboarding_screen.dart';
import '../utils/scaffold_with_nested_navigation.dart';
import '../../screens/splash_screen/splash_screen.dart';
import '../../screens/auth_screen/reset_password_confirm_screen.dart';
import '../../providers/auth_providers.dart';

final _rootNavigatorKey = GlobalKey<NavigatorState>();

class RouterNotifier extends ChangeNotifier {
  final Ref _ref;

  RouterNotifier(this._ref) {
    _ref.listen<bool>(
      isLoggedInProvider,
      (_, __) => notifyListeners(),
    );
  }
}

final routerNotifierProvider = Provider<RouterNotifier>((ref) {
  return RouterNotifier(ref);
});

final routerProvider = Provider<GoRouter>((ref) {
  final notifier = ref.watch(routerNotifierProvider);
  return GoRouter(
    navigatorKey: _rootNavigatorKey,
    refreshListenable: notifier,
    initialLocation: AppRouter.splash,
    errorBuilder: (context, state) => Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.error_outline, size: 64, color: Colors.red),
            const SizedBox(height: 16),
            Text(
              'Page not found',
              style: Theme.of(context).textTheme.headlineSmall,
            ),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () => context.go(AppRouter.home),
              child: const Text('Go Home'),
            ),
          ],
        ),
      ),
    ),
    redirect: (context, state) {
      final isLoggedIn = ref.read(isLoggedInProvider);
      final location = state.uri.path;

      final isSplash = location == AppRouter.splash;
      final isOnboarding = location == AppRouter.onboarding;
      final isLogin = location == AppRouter.login;
      final isForgotPassword = location == AppRouter.forgotPassword;
      final isResetPassword = location.startsWith('/password/reset');
      final isOtpVerification = location.startsWith('/otp-verification');
      final isAboutUs = location == AppRouter.aboutUs;
      final isHowItWorks = location == AppRouter.howItWorks;
      final isLanguageSettings = location == AppRouter.languageSettings;
      
      final isPublicRoute = isSplash || isOnboarding || isLogin || isForgotPassword || isResetPassword || isOtpVerification || isAboutUs || isHowItWorks || isLanguageSettings;

      if (!isLoggedIn && !isPublicRoute) {
        return AppRouter.login;
      }

      if (isLoggedIn && (isSplash || isOnboarding || isLogin)) {
        return AppRouter.home;
      }

      return null;
    },
    routes: <RouteBase>[
      GoRoute(
        path: AppRouter.splash,
        name: 'splash',
        builder: (context, state) => const SplashScreen(),
      ),

       GoRoute(
        path: AppRouter.invoice,
        name: 'invoice',
        builder: (context, state) => const InvoiceScreen(),
      ),
       GoRoute(
        path: AppRouter.invoiceDetails,
        name: 'invoiceDetails',
        builder: (context, state) => InvoiceDetailsScreen(transactionId: state.pathParameters['transactionId']!),
      ),
      GoRoute(
        path: AppRouter.onboarding,
        name: 'onboarding',
        builder: (context, state) => const OnboardingScreen(),
      ),
      GoRoute(
        path: AppRouter.login,
        name: 'login',
        builder: (context, state) => const LoginScreen(),
      ),
       GoRoute(
        path: AppRouter.updatePassword,
        name: 'updatePassword',
        builder: (context, state) => const UpdatePasswordScreen(),
      ),
      GoRoute(
        path: AppRouter.forgotPassword,
        name: 'forgotPassword',
        builder: (context, state) => const ForgotPasswordScreen(),
      ),
      // GoRoute(
      //   path: '/password/reset',
      //   builder: (context, state) {
      //      final token = state.uri.queryParameters['token'];
      //      final email = state.uri.queryParameters['email'];
      //      return ResetPasswordConfirmScreen(token: token, email: email); 
      //   },
      // ),
      // GoRoute(
      //   path: '/password/reset/:token',
      //   builder: (context, state) {
      //      final token = state.pathParameters['token'];
      //      final email = state.uri.queryParameters['email'];
      //      return ResetPasswordConfirmScreen(token: token, email: email); 
      //   },
      // ),
      GoRoute(
        path: AppRouter.otpVerification,
        name: AppRouter.otpVerification,
        builder: (context, state) => OtpVerificationScreen(email: state.pathParameters['email']!),
      ),
       GoRoute(
         path: AppRouter.analytics,
         name: 'analytics',
         builder: (context, state) => const AnalyticsScreen(),
      ),
      GoRoute(
        path: AppRouter.aboutUs,
        name: 'aboutUs',
        builder: (context, state) => const AboutUsScreen(),
      ),
       GoRoute(
        path: AppRouter.languageSettings,
        name: 'languageSettings',
        builder: (context, state) => const LanguageSettingsScreen(),
      ),
      GoRoute(
        path: AppRouter.howItWorks,
        name: 'howItWorks',
        builder: (context, state) => const HowItWorksScreen(),
      ),
      GoRoute(
        path: '/help-support',
        name: 'helpSupport',
        builder: (context, state) => const HelpAndSupportScreen(),
      ),

       GoRoute(
        path: AppRouter.securityAndPrivacy,
        name: 'securityAndPrivacy',
        builder: (context, state) => const SecurityPrivacyScreen(),
      ),
      StatefulShellRoute.indexedStack(
          builder: (context, state, navigationShell) =>
              ScaffoldWithNestedNavigation(navigationShell: navigationShell),
        branches: <StatefulShellBranch>[
          StatefulShellBranch(
            routes: <RouteBase>[
              GoRoute(
                path: AppRouter.home,
                name: 'home',
                builder: (BuildContext context, GoRouterState state) =>
                    const HomeScreen(),
                routes: <RouteBase>[
                  GoRoute(
                    path: AppRouter.serviceDetails,
                    name: AppRouter.serviceDetails,
                    builder: (context, state) {
                      final serviceId = state.pathParameters['id'] ?? '';
                      return Scaffold(
                        appBar: AppBar(
                          title: const Text('Service Details'),
                        ),
                        body: Center(
                          child: Text(
                              'Service Details: $serviceId - Coming Soon'),
                        ),
                      );
                    },
                  ),
                ],
              ),
            ],
          ),
          StatefulShellBranch(
            routes: <RouteBase>[
              GoRoute(
                path: AppRouter.activity,
                name: AppRouter.activity,
                builder: (BuildContext context, GoRouterState state) =>
                    const TransactionReportScreen(),
              ),
            ],
          ),
          StatefulShellBranch(
            routes: <RouteBase>[
              GoRoute(
                path: AppRouter.sendMoney,
                name: AppRouter.sendMoney,
                builder: (BuildContext context, GoRouterState state) =>
                    const SendMoneyScreen(),
              ),
            ],
          ),
          StatefulShellBranch(
            routes: <RouteBase>[
              GoRoute(
                path: AppRouter.trackTransfer,
                name: 'trackTransferTab',
                builder: (BuildContext context, GoRouterState state) =>
                    const TrackTransferScreen(),
              ),
            ],
          ),
        ],
      ),
      GoRoute(
        path: AppRouter.myAccount,
        name: 'myAccount',
        builder: (context, state) => const MyAccountScreen(),
      ),
      GoRoute(
        path: AppRouter.search,
        name: 'search',
        builder: (context, state) => const SearchScreen(),
      ),
      GoRoute(
        path: AppRouter.virtualCreditCard,
        name: 'virtualCreditCard',
        builder: (context, state) => const VirtualCreditCardScreen(cardHolderName: '', balance: '', currency: '',),
      ),
      GoRoute(
        path: AppRouter.notifications,
        name: 'notifications',
        builder: (context, state) => const NotificationsScreen(),
      ),
      GoRoute(
        path: AppRouter.profile,
        name: 'profile',
        builder: (context, state) => const ProfileScreen(),
      ),
      GoRoute(
        path: AppRouter.sendMoneySuccess,
        name: 'sendMoneySuccess',
        builder: (context, state) {
          final transactionData = state.extra as Map<String, dynamic>?;
          final referenceNumber = transactionData?['reference_number'] as String?;
          final message = transactionData?['message'] as String?;
          return SendMoneySuccessScreen(referenceNumber: referenceNumber, message: message);
        },
      ),
      GoRoute(
        path: AppRouter.updateProfile,
        name: 'updateProfile',
        builder: (context, state) {
           final extra = state.extra;

           UserProfileModel userProfile;
           if (extra is ProfileData) {
             userProfile = extra.userProfile;
           } else if (extra is UserProfileModel) {
             userProfile = extra;
           } else {
             userProfile = const UserProfileModel();
           }
           return UpdateProfileScreen(userProfile: userProfile);
        }
      ),
      GoRoute(
        path: AppRouter.qrCode,
        name: 'qrCode',
        builder: (context, state) => const QRCodeScreen(),
      ),
      GoRoute(
        path: AppRouter.beneficiary,
        name: 'beneficiary',
        builder: (context, state) => const BeneficiaryScreen(),
      ),
      GoRoute(
        path: AppRouter.addBeneficiary,
        name: 'addBeneficiary',
        builder: (context, state) => const AddBeneficiaryScreen(),
      ),
      GoRoute(
        path: AppRouter.updateBeneficiary,
        name: 'updateBeneficiary',
        builder: UpdateBeneficiaryScreen.fromGoRouterState,
      ),
      GoRoute(
        path: AppRouter.sendMoney,
        name: 'send-money',
        builder: (context, state) => const SendMoneyScreen(),
      ),
      GoRoute(
        path: '/tracking-details',
        name: 'trackingDetails',
        builder: (BuildContext context, GoRouterState state) {
          final transaction = state.extra as TransactionModel?;
          if (transaction == null) {
            return Scaffold(body: Center(child: Text(context.tr('No transactions found'))));
          }
          return TrackingDetailsScreen(transaction: transaction);
        },
      ),
      GoRoute(
        path: AppRouter.exchangeRate,
        name: 'exchangeRate',
        builder: (context, state) => const CurrentRateScreen(),
      ),
      // GoRoute(
      //   path: AppRouter.testDecrypt,
      //   name: 'testDecrypt',
      //   builder: (context, state) => const TestDecryptionScreen(),
      // ),
    ],
  );
});

class AppRouter {
  static const String splash = '/';
  static const String onboarding = '/onboarding';
  static const String login = '/login';
  static const String logout = '/logout';
  static const String trackTransfer = '/track-transfer';
  static const String signup = '/signup';
  static const String updatePassword = '/update-password';
  static const String otpVerification = '/otp-verification/:email';
  static const String forgotPassword = '/forgot-password';
  static const String resetPasswordConfirm = '/password/reset';
  static const String home = '/home';
  static const String serviceDetails = 'service-details';
  static const String bookings = '/bookings';
  static const String profile = '/profile';
  static const String updateProfile = '/update-profile';
  static const String providerDashboard = '/provider-dashboard';
  static const String clientDashboard = '/client-dashboard';
  static const String notifications = '/notifications';
  static const String messages = '/messages';
  static const String search = '/search';
  static const String userTypeSelect = '/user-type-select';
  static const String cart = '/cart';
  static const String offers = '/offers';
  static const String orders = '/orders';
  static const String myAccount = '/my-account';
  static const String activity = '/activity';
  static const String recipients = '/recipients';
  static const String chats = '/chats';
  static const String chatDetails = '/chat-details';
  static const String services = '/services';
  static const String virtualCreditCard = '/virtual-credit-card';
  static const String qrCode = '/promo-code';
  static const String userTypeSelectionRoute = '/user-type';
  static const String providerProfileRoute = '/provider/profile';
  static const String providerJobDetailsRoute = '/provider/jobs/:jobId';
  static const String customerProfileRoute = '/customer/profile';
  static const String sendMoneySuccess = '/send-money-success';
  static const String invoice = '/invoices';
  static const String invoiceDetails = '/invoice-details/:transactionId';

  static const String aboutUs = '/about-us';
  static const String howItWorks = '/how-it-works';
  static const String helpSupport = '/help-support';

  static const String languageSettings = '/language-settings';
  static const String securityAndPrivacy = '/security-and-privacy';
  static const String beneficiary = '/beneficiary';
  static const String addBeneficiary = '/add-beneficiary';
  static const String updateBeneficiary = '/update-beneficiary';
  static const String sendMoney = '/send-money';
  static const String analytics = '/analytics';
  static const String exchangeRate = '/exchange-rate';
  // static const String testDecrypt = '/test-decrypt';
}
