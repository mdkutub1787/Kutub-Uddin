import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'view_models/category_view_model.dart';
import 'view_models/auth_view_model.dart';
import 'view_models/product_view_model.dart';
import 'view_models/settings_view_model.dart';
import 'view_models/cart_view_model.dart';
import 'view_models/order_view_model.dart';
import 'view_models/wishlist_view_model.dart';
import 'view_models/loading_view_model.dart';
import 'view_models/navigation_view_model.dart';
import 'view_models/notification_view_model.dart';
import 'view_models/support_view_model.dart';
import 'view_models/user_view_model.dart';
import 'view_models/activity_log_view_model.dart';
import 'screens/splash/splash_screen.dart';
import 'routes/app_routes.dart';
import 'utils/theme/app_theme.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  await EasyLocalization.ensureInitialized();
  
  runApp(
    EasyLocalization(
      supportedLocales: const [Locale('en', 'US'), Locale('bn', 'BD')],
      path: 'assets/translations',
      fallbackLocale: const Locale('en', 'US'),
      child: MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => AuthViewModel()),
          ChangeNotifierProvider(create: (_) => CategoryViewModel()),
          ChangeNotifierProvider(create: (_) => ProductViewModel()),
          ChangeNotifierProvider(create: (_) => SettingsViewModel()),
          ChangeNotifierProvider(create: (_) => CartViewModel()),
          ChangeNotifierProvider(create: (_) => OrderViewModel()),
          ChangeNotifierProvider(create: (_) => WishlistViewModel()),
          ChangeNotifierProvider(create: (_) => LoadingViewModel()),
          ChangeNotifierProvider(create: (_) => NavigationViewModel()),
          ChangeNotifierProvider(create: (_) => NotificationViewModel()),
          ChangeNotifierProvider(create: (_) => SupportViewModel()),
          ChangeNotifierProvider(create: (_) => UserViewModel()),
          ChangeNotifierProvider(create: (_) => ActivityLogViewModel()),
        ],
        child: const MyApp(),
      ),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final loading = context.watch<LoadingViewModel>();

    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Smart Shop',
      
      localizationsDelegates: context.localizationDelegates,
      supportedLocales: context.supportedLocales,
      locale: context.locale,

      themeMode: settings.themeMode,
      theme: AppTheme.lightTheme(settings.primaryColor),
      darkTheme: AppTheme.darkTheme(settings.primaryColor),

      onGenerateRoute: AppRoutes.generateRoute,
      home: const SplashScreen(),
      builder: (context, child) {
        return Stack(
          children: [
            child!,
            if (loading.isLoading)
              BackdropFilter(
                filter: ImageFilter.blur(sigmaX: 4, sigmaY: 4),
                child: Container(
                  color: Colors.black26,
                  child: Center(
                    child: Container(
                      padding: const EdgeInsets.all(32),
                      decoration: BoxDecoration(
                        color: Theme.of(context).cardColor,
                        borderRadius: BorderRadius.circular(20),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withValues(alpha: 0.1),
                            blurRadius: 20,
                          )
                        ],
                      ),
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          CircularProgressIndicator(
                            color: settings.primaryColor,
                            strokeWidth: 4,
                          ),
                          if (loading.message.isNotEmpty) ...[
                            const SizedBox(height: 20),
                            Material(
                              color: Colors.transparent,
                              child: Text(
                                loading.message,
                                style: TextStyle(
                                  color: Theme.of(context).textTheme.bodyLarge?.color,
                                  fontWeight: FontWeight.bold,
                                  fontSize: 16,
                                ),
                              ),
                            ),
                          ],
                        ],
                      ),
                    ),
                  ),
                ),
              ),
          ],
        );
      },
    );
  }
}

