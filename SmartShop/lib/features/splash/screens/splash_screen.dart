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

class _SplashScreenState extends ConsumerState<SplashScreen> {
  @override
  void initState() {
    super.initState();
    _initializeApp();
  }

  Future<void> _initializeApp() async {
    await Future.delayed(const Duration(seconds: 2));
    if (!mounted) return;

    final client = ref.read(supabaseClientProvider);

    // 1. Check Maintenance Mode
    try {
      final configRes = await client
          .from(AppConstants.systemConfigTable)
          .select()
          .eq('key', 'maintenance_mode')
          .maybeSingle();
      
      if (configRes != null && configRes['value'] == 'true') {
        _showMaintenanceDialog();
        return;
      }
    } catch (e) {
      debugPrint("Config check failed: $e");
    }

    // 2. Check Auth and Active Status
    final session = client.auth.currentSession;
    if (session != null) {
      try {
        final userRes = await client
            .from(AppConstants.usersTable)
            .select('isActive')
            .eq('id', session.user.id)
            .maybeSingle();

        if (userRes != null && userRes['isActive'] == false) {
          await client.auth.signOut();
          _showAccountBlockedDialog();
          return;
        }

        await ref.read(authNotifierProvider.notifier).loadUser();
        if (mounted) Navigator.pushReplacementNamed(context, AppRoutes.main);
      } catch (e) {
        if (mounted) Navigator.pushReplacementNamed(context, AppRoutes.login);
      }
    } else {
      if (mounted) Navigator.pushReplacementNamed(context, AppRoutes.login);
    }
  }

  void _showMaintenanceDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) => AlertDialog(
        title: const Text("Under Maintenance"),
        content: const Text("Smart Shop is currently undergoing maintenance. Please try again later."),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("Exit"))
        ],
      ),
    );
  }

  void _showAccountBlockedDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) => AlertDialog(
        title: const Text("Account Deactivated"),
        content: const Text("Your account has been deactivated by the administrator. Please contact support."),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("OK"))
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image.asset('assets/images/app_icon.png', width: 120, errorBuilder: (_, __, ___) => const Icon(Icons.shopping_cart_rounded, size: 80, color: Colors.teal)),
            const SizedBox(height: 24),
            const CircularProgressIndicator(),
          ],
        ),
      ),
    );
  }
}
