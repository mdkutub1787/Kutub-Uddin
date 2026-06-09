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
import 'screens/auth/login_screen.dart';
import 'services/auth_service.dart';
import 'routes/app_routes.dart';
import 'utils/theme/app_theme.dart';
import 'screens/home/main_screen.dart';

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
      home: const AuthWrapper(),
    );
  }
}

class AuthWrapper extends StatelessWidget {
  const AuthWrapper({super.key});

  @override
  Widget build(BuildContext context) {
    final authService = AuthService();
    return StreamBuilder(
      stream: authService.userStream,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Scaffold(body: Center(child: CircularProgressIndicator()));
        }
        if (snapshot.hasData) {
          WidgetsBinding.instance.addPostFrameCallback((_) {
            context.read<WishlistViewModel>().init(snapshot.data!.uid);
          });
          return const MainScreen();
        } else {
          return const LoginScreen();
        }
      },
    );
  }
}
