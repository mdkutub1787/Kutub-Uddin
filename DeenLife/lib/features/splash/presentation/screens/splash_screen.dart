import 'package:flutter/material.dart';

import 'dart:async';

import 'package:go_router/go_router.dart';
import '../../../../shared/widgets/main_layout.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _fadeAnimation;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1500),
    );

    _fadeAnimation = Tween<double>(
      begin: 0.0,
      end: 1.0,
    ).animate(CurvedAnimation(parent: _controller, curve: Curves.easeIn));

    _scaleAnimation = Tween<double>(
      begin: 0.8,
      end: 1.0,
    ).animate(CurvedAnimation(parent: _controller, curve: Curves.easeOutBack));

    _controller.forward();

    Timer(const Duration(seconds: 3), () {
      if (mounted) context.go('/main');
    });
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
        fit: StackFit.expand,
        children: [
          // Subtle background pattern (reusing the faint quran pattern for elegance)
          Opacity(
            opacity: 0.05,
            child: Image.asset('assets/quran_pattern.jpg', fit: BoxFit.cover),
          ),
          Center(
            child: FadeTransition(
              opacity: _fadeAnimation,
              child: ScaleTransition(
                scale: _scaleAnimation,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                      width: 180,
                      height: 180,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withOpacity(0.1),
                            blurRadius: 20,
                            offset: const Offset(0, 10),
                          ),
                        ],
                      ),
                      child: ClipOval(
                        child: Image.asset(
                          'assets/splash_logo.jpg',
                          fit: BoxFit.cover,
                        ),
                      ),
                    ),
                    const SizedBox(height: 24),
                    const Text(
                      'DeenLife',
                      style: TextStyle(
                        fontSize: 32,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF1E3A5F), // Deep teal/navy
                        letterSpacing: 2,
                      ),
                    ),
                    const SizedBox(height: 8),
                    const Text(
                      'Your Daily Islamic Companion',
                      style: TextStyle(
                        fontSize: 14,
                        color: Colors.grey,
                        letterSpacing: 1,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

