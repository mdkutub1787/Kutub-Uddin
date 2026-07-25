import 'package:flutter/material.dart';

class AuthBackground extends StatelessWidget {
  final Widget child;
  
  const AuthBackground({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFE2F3ED), // Very light mint background
      body: Stack(
        children: [
          // Top Left Blob
          Positioned(
            top: -100,
            left: -100,
            child: Container(
              width: 300,
              height: 300,
              decoration: BoxDecoration(
                color: Theme.of(context).primaryColorLight,
                shape: BoxShape.circle,
              ),
            ),
          ),
          // Bottom Right Blob
          Positioned(
            bottom: -150,
            right: -100,
            child: Container(
              width: 350,
              height: 350,
              decoration: BoxDecoration(
                color: Theme.of(context).primaryColor,
                shape: BoxShape.circle,
              ),
            ),
          ),
          
          SafeArea(child: child),
        ],
      ),
    );
  }
}
