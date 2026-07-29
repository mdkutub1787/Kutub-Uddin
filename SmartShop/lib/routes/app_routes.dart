import 'package:flutter/material.dart';
import '../features/auth/screens/login_screen.dart';
import '../features/auth/screens/register_screen.dart';
import '../features/home/screens/main_screen.dart';
import '../features/home/screens/dashboard_screen.dart';
import '../features/category/screens/all_categories_screen.dart';
import '../features/shop/screens/all_shops_screen.dart';
import '../features/shop/screens/shop_details_screen.dart';
import '../features/product/screens/product_details_screen.dart';
import '../features/product/screens/all_products_screen.dart';
import '../features/cart/screens/cart_screen.dart';
import '../features/profile/screens/profile_screen.dart';
import '../features/order/screens/my_orders_screen.dart';
import '../features/order/screens/order_details_screen.dart';
import '../features/wishlist/screens/wishlist_screen.dart';
import '../features/profile/screens/edit_profile_screen.dart';
import '../features/admin/screens/admin_dashboard_screen.dart';
import '../features/delivery/screens/delivery_dashboard_screen.dart';
import '../features/delivery/screens/delivery_main_screen.dart';
import '../features/offers/screens/offers_screen.dart';
import '../features/notification/screens/notification_screen.dart';
import '../features/notification/screens/notification_details_screen.dart';
import '../features/support/screens/support_screen.dart';
import '../models/shop_model.dart';
import '../features/product/models/product_model.dart';
import '../features/order/models/order_model.dart';
import '../models/notification_model.dart';

import '../features/splash/screens/splash_screen.dart';
import '../features/splash/screens/onboarding_screen.dart';

class AppRoutes {
  static const String splash = '/';
  static const String onboarding = '/onboarding';
  static const String login = '/login';
  static const String register = '/register';
  static const String main = '/main';
  static const String dashboard = '/dashboard';
  static const String productDetails = '/product-details';
  static const String allProducts = '/all-products';
  static const String allCategories = '/all-categories';
  static const String allShops = '/all-shops';
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
  static const String shopDetails = '/shop-details';

  static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case splash:
        return MaterialPageRoute(builder: (_) => const SplashScreen());
      case onboarding:
        return MaterialPageRoute(builder: (_) => const OnboardingScreen());
      case login:
        return MaterialPageRoute(builder: (_) => const LoginScreen());
      case register:
        return MaterialPageRoute(builder: (_) => const RegisterScreen());
      case main:
        return MaterialPageRoute(builder: (_) => const MainScreen());
      case dashboard:
        return MaterialPageRoute(builder: (_) => const DashboardScreen());
      case shopDetails:
        if (settings.arguments is ShopModel) {
          return MaterialPageRoute(
              builder: (_) => ShopDetailsScreen(shop: settings.arguments as ShopModel));
        }
        return MaterialPageRoute(builder: (_) => const Scaffold(body: Center(child: Text('Error: Invalid arguments'))));
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
        return MaterialPageRoute(builder: (_) => const DeliveryMainScreen());
      case offers:
        return MaterialPageRoute(builder: (_) => const OffersScreen());
      case notifications:
        return MaterialPageRoute(builder: (_) => const NotificationScreen());
      case notificationDetails:
        final notification = settings.arguments as NotificationModel;
        return MaterialPageRoute(
          builder: (_) => NotificationDetailsScreen(notification: notification),
        );
      case allCategories:
        return MaterialPageRoute(builder: (_) => const AllCategoriesScreen());
      case allShops:
        return MaterialPageRoute(builder: (_) => const AllShopsScreen());
      case support:
        return MaterialPageRoute(builder: (_) => const SupportScreen());
      case allProducts:
        final args = settings.arguments as Map<String, dynamic>;
        return MaterialPageRoute(
          builder: (_) => AllProductsScreen(
            title: args['title'] as String,
            categoryId: args['categoryId'] as String?,
          ),
        );
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
