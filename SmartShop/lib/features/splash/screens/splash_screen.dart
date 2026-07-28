import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../wishlist/riverpod/wishlist_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../../routes/app_routes.dart';
import '../../user/models/user_model.dart';

class SplashScreen extends ConsumerStatefulWidget {
  const SplashScreen({super.key});

  @override
  ConsumerState<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends ConsumerState<SplashScreen> with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _animation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1500),
    );
    _animation = CurvedAnimation(parent: _controller, curve: Curves.easeIn);
    _controller.forward();

    _navigateToNext();
  }

  Future<void> _navigateToNext() async {
    await Future.delayed(const Duration(seconds: 2));
    if (!mounted) return;

    UserModel? user;
    try {
      user = await ref.read(authNotifierProvider.future);
    } catch (e) {
      user = null;
    }

    if (!mounted) return;

    if (user != null) {
      if (!user.isActive) {
        await ref.read(authNotifierProvider.notifier).signOut();
        if (!mounted) return;
        Navigator.pushReplacementNamed(context, AppRoutes.login);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Your account has been deactivated by the Admin.')));
        return;
      }

      ref.read(wishlistNotifierProvider.notifier).loadWishlist();
      ref.read(navigationNotifierProvider.notifier).setIndex(0);
      
      if (user.role == 'admin' || user.role == 'super_admin' || user.role == 'owner') {
        Navigator.pushReplacementNamed(context, AppRoutes.adminDashboard);
      } else if (user.role == 'delivery_man') {
        Navigator.pushReplacementNamed(context, AppRoutes.deliveryDashboard);
      } else {
        Navigator.pushReplacementNamed(context, AppRoutes.main);
      }
    } else {
      final prefs = await SharedPreferences.getInstance();
      final hasSeenOnboarding = prefs.getBool('has_seen_onboarding') ?? false;
      
      if (hasSeenOnboarding) {
        Navigator.pushReplacementNamed(context, AppRoutes.login);
      } else {
        Navigator.pushReplacementNamed(context, AppRoutes.onboarding);
      }
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: Stack(
        children: [
          Positioned(
            top: -100,
            right: -50,
            child: CircleAvatar(
              radius: 180,
              backgroundColor: Theme.of(context).primaryColor.withValues(alpha: 0.1),
            ),
          ),
          Positioned(
            bottom: -80,
            left: -80,
            child: CircleAvatar(
              radius: 150,
              backgroundColor: Theme.of(context).primaryColor.withValues(alpha: 0.1),
            ),
          ),
          FadeTransition(
            opacity: _animation,
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    padding: const EdgeInsets.all(25),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      shape: BoxShape.circle,
                      boxShadow: [
                        BoxShadow(
                          color: Theme.of(context).primaryColor.withValues(alpha: 0.2),
                          blurRadius: 40,
                          offset: const Offset(0, 20),
                        )
                      ],
                    ),
                    child: Hero(
                      tag: 'app_logo',
                      child: Image.asset(
                        'assets/images/app_icon.png',
                        width: 100,
                        height: 100,
                      ),
                    ),
                  ),
                  const SizedBox(height: 40),
                  Text(
                    "Smart Shop",
                    style: Theme.of(context).textTheme.displayLarge?.copyWith(
                      fontSize: 42,
                      color: Theme.of(context).primaryColor,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "Elegance in Every Purchase",
                    style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                      color: Colors.black45,
                      letterSpacing: 0.5,
                    ),
                  ),
                  const SizedBox(height: 80),
                  SizedBox(
                    width: 40,
                    height: 40,
                    child: CircularProgressIndicator(
                      color: Theme.of(context).primaryColor,
                      strokeWidth: 3,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
