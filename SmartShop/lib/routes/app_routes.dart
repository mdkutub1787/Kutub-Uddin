import 'package:flutter/material.dart';
import '../screens/auth/login_screen.dart';
import '../screens/auth/register_screen.dart';
import '../screens/main_screen.dart';
import '../screens/dashboard_screen.dart';
import '../screens/product_details_screen.dart';
import '../screens/cart_screen.dart';
import '../screens/profile_screen.dart';
import '../screens/my_orders_screen.dart';
import '../screens/wishlist_screen.dart';
import '../screens/edit_profile_screen.dart';
import '../screens/admin_verification_screen.dart';
import '../models/product_model.dart';

class AppRoutes {
  static const String login = '/login';
  static const String register = '/register';
  static const String main = '/main';
  static const String dashboard = '/dashboard';
  static const String productDetails = '/product-details';
  static const String cart = '/cart';
  static const String profile = '/profile';
  static const String myOrders = '/my-orders';
  static const String wishlist = '/wishlist';
  static const String editProfile = '/edit-profile';
  static const String adminVerification = '/admin-verification';

  static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case login:
        return MaterialPageRoute(builder: (_) => const LoginScreen());
      case register:
        return MaterialPageRoute(builder: (_) => const RegisterScreen());
      case main:
        return MaterialPageRoute(builder: (_) => const MainScreen());
      case dashboard:
        return MaterialPageRoute(builder: (_) => const DashboardScreen());
      case cart:
        return MaterialPageRoute(builder: (_) => const CartScreen());
      case profile:
        return MaterialPageRoute(builder: (_) => const ProfileScreen());
      case myOrders:
        return MaterialPageRoute(builder: (_) => const MyOrdersScreen());
      case wishlist:
        return MaterialPageRoute(builder: (_) => const WishlistScreen());
      case editProfile:
        return MaterialPageRoute(builder: (_) => const EditProfileScreen());
      case adminVerification:
        return MaterialPageRoute(builder: (_) => const AdminVerificationScreen());
      case productDetails:
        final product = settings.arguments as ProductModel;
        return MaterialPageRoute(
          builder: (_) => ProductDetailsScreen(product: product),
        );
      default:
        return MaterialPageRoute(
          builder: (_) => Scaffold(
            body: Center(child: Text('No route defined for ${settings.name}')),
          ),
        );
    }
  }
}
