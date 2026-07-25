import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';
import 'app_colors.dart';

class AppTheme {
  // Premium Design Constants
  static const double _borderRadius = 28.0; // More rounded for pill shapes
  static const double _cardElevation = 6.0; // Soft shadow for floating elements

  static ThemeData lightTheme(Color primaryColor) {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.light,
      primaryColor: primaryColor,
      scaffoldBackgroundColor: AppColors.backgroundLight,
      
      textTheme: GoogleFonts.outfitTextTheme().copyWith(
        displayLarge: GoogleFonts.playfairDisplay(fontWeight: FontWeight.w900, color: AppColors.secondary),
        displayMedium: GoogleFonts.playfairDisplay(fontWeight: FontWeight.w800, color: AppColors.secondary),
        titleLarge: GoogleFonts.playfairDisplay(fontWeight: FontWeight.w700, fontSize: 24, color: AppColors.secondary),
        titleMedium: GoogleFonts.outfit(fontWeight: FontWeight.bold, fontSize: 18, color: AppColors.secondary),
        bodyLarge: GoogleFonts.outfit(fontSize: 16, color: Colors.black87),
        bodyMedium: GoogleFonts.outfit(fontSize: 14, color: Colors.black54),
      ),

      colorScheme: ColorScheme.fromSeed(
        seedColor: primaryColor,
        primary: primaryColor,
        secondary: AppColors.secondary,
        surface: Colors.white,
        outlineVariant: Colors.grey[200],
      ),

      cardTheme: CardThemeData(
        elevation: _cardElevation,
        shadowColor: primaryColor.withValues(alpha: 0.15),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(_borderRadius),
          side: BorderSide(color: Colors.transparent),
        ),
        color: Colors.white,
      ),

      appBarTheme: AppBarTheme(
        backgroundColor: Colors.transparent, // Header handles background
        foregroundColor: AppColors.secondary,
        elevation: 0,
        centerTitle: true,
        systemOverlayStyle: SystemUiOverlayStyle.dark,
        iconTheme: const IconThemeData(color: AppColors.secondary),
        titleTextStyle: GoogleFonts.outfit(
          fontSize: 20,
          fontWeight: FontWeight.w800,
          color: AppColors.secondary,
        ),
      ),

      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: primaryColor,
          foregroundColor: Colors.white,
          elevation: _cardElevation,
          shadowColor: primaryColor.withValues(alpha: 0.4),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)), // Pill shape
          padding: const EdgeInsets.symmetric(vertical: 18, horizontal: 24),
          textStyle: GoogleFonts.outfit(fontWeight: FontWeight.w700, fontSize: 16),
        ),
      ),

      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: Colors.white,
        contentPadding: const EdgeInsets.symmetric(vertical: 20, horizontal: 24),
        hintStyle: GoogleFonts.outfit(color: Colors.grey[400], fontSize: 14),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(30), // Pill shape for inputs
          borderSide: BorderSide.none,
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(30),
          borderSide: BorderSide(color: Colors.grey[100]!, width: 1.5),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(30),
          borderSide: BorderSide(color: primaryColor, width: 2),
        ),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(30),
          borderSide: BorderSide(color: AppColors.error, width: 1.5),
        ),
      ),
    );
  }

  static ThemeData darkTheme(Color primaryColor) {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.dark,
      primaryColor: primaryColor,
      scaffoldBackgroundColor: AppColors.backgroundDark,
      textTheme: GoogleFonts.outfitTextTheme(ThemeData.dark().textTheme).copyWith(
        displayLarge: GoogleFonts.playfairDisplay(fontWeight: FontWeight.w900, color: Colors.white),
        titleLarge: GoogleFonts.playfairDisplay(fontWeight: FontWeight.w700, fontSize: 24, color: Colors.white),
      ),
      colorScheme: ColorScheme.fromSeed(
        seedColor: primaryColor,
        primary: primaryColor,
        brightness: Brightness.dark,
        surface: AppColors.surfaceDark,
      ),
    );
  }
}
