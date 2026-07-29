import 'dart:math' as math;
import 'package:flutter/material.dart';

class CustomLoading extends StatefulWidget {
  final double size;
  const CustomLoading({super.key, this.size = 50});

  @override
  State<CustomLoading> createState() => _CustomLoadingState();
}

class _CustomLoadingState extends State<CustomLoading> with SingleTickerProviderStateMixin {
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(seconds: 2),
      vsync: this,
    )..repeat();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: AnimatedBuilder(
        animation: _controller,
        builder: (_, child) {
          // 3D Flip animation on the Y axis
          final transform = Matrix4.identity()
            ..setEntry(3, 2, 0.001) // perspective
            ..rotateY(_controller.value * 2 * math.pi);
            
          return Transform(
            transform: transform,
            alignment: Alignment.center,
            child: child,
          );
        },
        child: ClipRRect(
          borderRadius: BorderRadius.circular(12), // Rounded corners
          child: Image.asset(
            'assets/images/app_icon.png', 
            width: widget.size, 
            height: widget.size,
            fit: BoxFit.cover,
            errorBuilder: (_, __, ___) => Icon(Icons.shopping_cart_rounded, size: widget.size, color: Colors.teal),
          ),
        ),
      ),
    );
  }
}
