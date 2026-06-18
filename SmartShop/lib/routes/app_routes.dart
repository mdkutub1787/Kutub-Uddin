import 'package:flutter/material.dart';
import '../screens/auth/login_screen.dart';
import '../screens/auth/register_screen.dart';
import '../screens/home/main_screen.dart';
import '../screens/home/dashboard_screen.dart';
import '../screens/product/product_details_screen.dart';
import '../screens/cart/cart_screen.dart';
import '../screens/profile/profile_screen.dart';
import '../screens/order/my_orders_screen.dart';
import '../screens/order/order_details_screen.dart';
import '../screens/wishlist/wishlist_screen.dart';
import '../screens/profile/edit_profile_screen.dart';
import '../screens/admin/admin_dashboard_screen.dart';
import '../screens/delivery/delivery_dashboard_screen.dart';
import '../screens/offers/offers_screen.dart';
import '../screens/notification/notification_screen.dart';
import '../screens/notification/notification_details_screen.dart';
import '../screens/support/support_screen.dart';
import '../models/product_model.dart';
import '../models/order_model.dart';
import '../models/notification_model.dart';

import '../screens/splash/splash_screen.dart';

class AppRoutes {
  static const String splash = '/';
  static const String login = '/login';
  static const String register = '/register';
  static const String main = '/main';
  static const String dashboard = '/dashboard';
  static const String productDetails = '/product-details';
  static const String orderDetails = '/order-details';
  static const String cart = '/cart';
  static const String profile = '/profile';
  static const String myOrders = '/my-orders';
  static const String wishlist = '/wishlist';
  static const String editProfile = '/edit-profile';
  static const String adminDashboard = '/admin-dashboard';
  static const String deliveryDashboard = '/delivery-dashboard';
  static const String offers = '/offers';
  static const String notifications = '/notifications';
  static const String notificationDetails = '/notification-details';
  static const String support = '/support';

  static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case splash:
        return MaterialPageRoute(builder: (_) => const SplashScreen());
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
      case adminDashboard:
        return MaterialPageRoute(builder: (_) => const AdminDashboardScreen());
      case deliveryDashboard:
        return MaterialPageRoute(builder: (_) => const DeliveryDashboardScreen());
      case offers:
        return MaterialPageRoute(builder: (_) => const OffersScreen());
      case notifications:
        return MaterialPageRoute(builder: (_) => const NotificationScreen());
      case notificationDetails:
        final notification = settings.arguments as NotificationModel;
        return MaterialPageRoute(
          builder: (_) => NotificationDetailsScreen(notification: notification),
        );
      case support:
        return MaterialPageRoute(builder: (_) => const SupportScreen());
      case productDetails:
        if (settings.arguments is ProductModel) {
          final product = settings.arguments as ProductModel;
          return MaterialPageRoute(
            builder: (_) => ProductDetailsScreen(product: product),
          );
        } else if (settings.arguments is Map) {
          final args = settings.arguments as Map<String, dynamic>;
          return MaterialPageRoute(
            builder: (_) => ProductDetailsScreen(
              product: args['product'] as ProductModel,
              heroTag: args['heroTag'] as String?,
            ),
          );
        }
        return MaterialPageRoute(
          builder: (_) => const Scaffold(
            body: Center(child: Text('Invalid product details arguments')),
          ),
        );
      case orderDetails:
        final order = settings.arguments as OrderModel;
        return MaterialPageRoute(
          builder: (_) => OrderDetailsScreen(order: order),
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
