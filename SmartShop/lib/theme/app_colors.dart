import 'package:flutter/material.dart';

class AppColors {
  // Brand Identity (Premium Teal/Mint from new UI design)
  static const Color primary = Color(0xFF54B599); // Main Teal
  static const Color primaryLight = Color(0xFF75CDB3); // Lighter Teal for gradients
  static const Color primaryDark = Color(0xFF38977C); // Darker Teal
  
  static const Color secondary = Color(0xFF2A3A35); // Elegant Dark Green/Grey for text
  static const Color accent = Color(0xFF65C4A6); // Accent light teal

  // Modern Backgrounds
  static const List<Color> bgGradient = [Color(0xFFE5F5EF), Color(0xFFFFFFFF)];
  static const List<Color> logoGradient = [Color(0xFF75CDB3), Color(0xFF38977C)];
  static const List<Color> cardGradient = [Color(0xFF54B599), Color(0xFF38977C)]; // For wallet cards

  // Semantic Colors
  static const Color success = Color(0xFF00C853);
  static const Color error = Color(0xFFE53935);
  static const Color warning = Color(0xFFF59E0B);
  static const Color info = Color(0xFF3B82F6);

  // Neutrals
  static const Color slate50 = Color(0xFFF8FAFC);
  static const Color slate100 = Color(0xFFF1F5F9);
  static const Color slate200 = Color(0xFFE2E8F0);
  static const Color slate300 = Color(0xFFCBD5E1);
  static const Color slate400 = Color(0xFF94A3B8);
  static const Color slate500 = Color(0xFF64748B);
  static const Color slate600 = Color(0xFF475569);
  static const Color slate700 = Color(0xFF334155);
  static const Color slate800 = Color(0xFF1E293B);
  static const Color slate900 = Color(0xFF0F172A);

  // Surface Colors
  static const Color backgroundLight = Color(0xFFFDFDFD); // Clean white
  static const Color backgroundDark = Color(0xFF020617);
  
  static const Color surfaceLight = Colors.white;
  static const Color surfaceDark = Color(0xFF0F172A);

  // Theme Selection Palette
  static const List<Color> themePalette = [
    Color(0xFF54B599), // Teal
    Color(0xFF1A237E), // Indigo
    Color(0xFFD32F2F), // Red
    Color(0xFFFFA000), // Amber
    Color(0xFF512DA8), // Deep Purple
    Color(0xFFE64A19), // Deep Orange
    Color(0xFFC2185B), // Pink
  ];
}
