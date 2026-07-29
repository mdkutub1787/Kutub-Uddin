import 'dart:math' as math;
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../routes/app_routes.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/constants/constants.dart';
import '../../../core/providers.dart';

class SplashScreen extends ConsumerStatefulWidget {
  const SplashScreen({super.key});

  @override
  ConsumerState<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends ConsumerState<SplashScreen> with SingleTickerProviderStateMixin {
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(seconds: 2),
      vsync: this,
    )..repeat(); // Icon will keep spinning professionally
    
    _initializeApp();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  Future<void> _initializeApp() async {
    await Future.delayed(const Duration(seconds: 3));
    if (!mounted) return;

    final client = ref.read(supabaseClientProvider);

    // 1. Check Maintenance Mode
    try {
      final configRes = await client.from(AppConstants.systemConfigTable).select().eq('key', 'maintenance_mode').maybeSingle();
      if (configRes != null && configRes['value'] == 'true') {
        _showMaintenanceDialog();
        return;
      }
    } catch (e) {
      debugPrint("Config check failed: $e");
    }

    // 2. Check Auth and User Role
    final session = client.auth.currentSession;
    if (session != null) {
      try {
        final userRes = await client.from(AppConstants.usersTable).select('isActive, role').eq('id', session.user.id).maybeSingle();

        if (userRes != null && userRes['isActive'] == false) {
          await client.auth.signOut();
          _showAccountBlockedDialog();
          return;
        }

        await ref.read(authNotifierProvider.notifier).loadUser();
        final user = ref.read(authNotifierProvider).value;
        if (mounted) {
          if (user != null) {
            if (user.role == 'admin' || user.role == 'super_admin' || user.role == 'owner') {
              Navigator.pushReplacementNamed(context, AppRoutes.adminDashboard);
            } else if (user.role == 'delivery_man') {
              Navigator.pushReplacementNamed(context, AppRoutes.deliveryDashboard);
            } else {
              Navigator.pushReplacementNamed(context, AppRoutes.main);
            }
          } else {
            Navigator.pushReplacementNamed(context, AppRoutes.main);
          }
        }
      } catch (e) {
        if (mounted) Navigator.pushReplacementNamed(context, AppRoutes.login);
      }
    } else {
      if (mounted) Navigator.pushReplacementNamed(context, AppRoutes.login);
    }
  }

  void _showMaintenanceDialog() {
    showDialog(context: context, barrierDismissible: false, builder: (ctx) => AlertDialog(title: const Text("Under Maintenance"), content: const Text("Smart Shop is currently undergoing maintenance. Please try again later."), actions: [TextButton(onPressed: () => {}, child: const Text("Exit"))]));
  }

  void _showAccountBlockedDialog() {
    showDialog(context: context, barrierDismissible: false, builder: (ctx) => AlertDialog(title: const Text("Account Deactivated"), content: const Text("Your account has been deactivated. Please contact support."), actions: [TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("OK"))]));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // Premium 3D Flipping App Icon Loading
            AnimatedBuilder(
              animation: _controller,
              builder: (_, child) {
                final transform = Matrix4.identity()
                  ..setEntry(3, 2, 0.001)
                  ..rotateY(_controller.value * 2 * math.pi);
                return Transform(
                  transform: transform,
                  alignment: Alignment.center,
                  child: child,
                );
              },
              child: ClipRRect(
                borderRadius: BorderRadius.circular(20),
                child: Image.asset(
                  'assets/images/app_icon.png', 
                  width: 100, 
                  height: 100,
                  fit: BoxFit.cover,
                  errorBuilder: (_, __, ___) => const Icon(Icons.shopping_cart_rounded, size: 80, color: Colors.teal)
                ),
              ),
            ),
            const SizedBox(height: 40),
            const Text(
              "SMART SHOP",
              style: TextStyle(fontSize: 22, fontWeight: FontWeight.w900, letterSpacing: 2, color: Colors.black87),
            ),
            const SizedBox(height: 8),
            Text(
              "Your Premium Marketplace",
              style: TextStyle(fontSize: 12, color: Colors.grey[500], fontWeight: FontWeight.w500),
            ),
          ],
        ),
      ),
    );
  }
}
