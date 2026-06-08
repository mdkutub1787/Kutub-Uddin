import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class AppTheme {
  // Light Theme Colors
  static const Color lightPrimary = Color(0xFF007AFF);
  static const Color lightSecondary = Color(0xFF0EA5E9);
  static const Color lightAccent = Color(0xFFEC4899);
  static const Color lightBackground = Color(0xFFFAFAFA);
  static const Color lightError = Color(0xFFDC2626);
  static const Color lightErrorContainer = Color(0xFFFFE5E0);
  static const Color lightSuccess = Color(0xFF6BA180);
  static const Color lightWarning = Color(0xFFF59E0B);
  static const Color lightInfo = Color(0xFF0EA5E9);
  static const Color lightSurface = Color(0xFFFFFFFF);
  static const Color topBar = Color(0x4827A18D);    // TopBar Color
  static const Color actionBar = Color(0xFFE91E63);    // TopBar Color
  static const Color topBarGradientLeft = Color(0xFFF3BED9);    // TopBar Color Left
  static const Color topBarGradientRight = Color(0xFF39D1B8);    // TopBar Color Right

  // Dark Theme Colors
  static const Color darkPrimary = Color(0xFF007AFF);
  static const Color darkSecondary = Color(0xFF06B6D4);
  static const Color darkAccent = Color(0xFFF472B6);
  static const Color darkBackground = Color(0xFF0F172A);
  static const Color darkSurface = Color(0xFF1E293B);
  static const Color darkError = Color(0xFFFCA5A5);
  static const Color darkErrorContainer = Color(0xFF5A1D1D);
  static const Color darkSuccess = Color(0xFF86EFAC);
  static const Color darkWarning = Color(0xFFFCD34D);
  static const Color darkInfo = Color(0xFF67E8F9);
  static const Color darkTopBarGradientLeft = Color(0xFFF3BED9);    // TopBar Color Gradient Left
  static const Color darkTopBarGradientRight = Color(0xFF39D1B8);    // TopBar Color Gradient Right
  static const Color darkActionBar = Color(0xFFE91E63);    // TopBar Color Gradient
  static const Color darkTopBar = Color(0xFFC2E4DE);    // Opaque Mint to match light theme look
  static const Color darkNeutralGrey50 = Color(0xFFFAFAFA);
  static const Color darkNeutralGrey100 = Color(0xFFF3F4F6);

  // Virtual Card Colors
  static const Color virtualCardPrimary = Color(0xFF007AFF);
  static const Color virtualCardSecondary = Color(0xFF0EA5E9);
  static const Color virtualCardAccent = Color(0xFFFFB703);
  static const Color virtualCardBackground = Color(0xFFF8F9FA);
  static const Color virtualCardChipGold = Color(0xFFD4AF37); // Professional metallic look
  static const Color virtualCardChipLightGold = Color(0xFFF5F3CE); // Subtle highlight
  static const Color virtualCardChipDeeperGold = Color(0xFFBFA76F); // Subtle highlight
  static const Color virtualCardShadow = Color(0x1F000000);
  static const Color virtualCardBorder = Color(0x78FFFFFF);
  static const Color virtualCardChipHighlight = Color(0xB3FFFFFF);
  static const Color virtualCardLogoBg = Color(0xDCFFFFFF);
  static const Color virtualCardLogoShadow = Color(0x1E000000);
  static const Color virtualCardLogoBorder = Color(0x3C9E9E9E);
  static const Color virtualCardErrorIcon = Color(0xFF757575);
  static const Color virtualCardChipInternalGold = Color(0x78D4AF37);


  // Neutral Colors (Common for both themes)
  static const Color neutralGrey50 = Color(0xFFFAFAFA);
  static const Color neutralGrey100 = Color(0xFFF3F4F6);
  static const Color neutralGrey200 = Color(0xFFE5E7EB);
  static const Color neutralGrey300 = Color(0xFFD1D5DB);
  static const Color neutralGrey400 = Color(0xFF9CA3AF);
  static const Color neutralGrey500 = Color(0xFF6B7280);
  static const Color neutralGrey600 = Color(0xFF4B5563);
  static const Color neutralGrey700 = Color(0xFF374151);
  static const Color neutralGrey800 = Color(0xFF1F2937);
  static const Color neutralGrey900 = Color(0xFF111827);

  /// Light Theme
  static ThemeData lightTheme() {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.light,
      colorScheme: const ColorScheme.light(
        primary: lightPrimary,
        secondary: lightSecondary,
        tertiary: lightAccent,
        surface: lightSurface,
        background: lightBackground,
        error: lightError,
        errorContainer: lightErrorContainer,
        onErrorContainer: darkError,
      ),
      scaffoldBackgroundColor: lightBackground,
      appBarTheme: AppBarTheme(
        backgroundColor: lightSurface,
        foregroundColor: neutralGrey900,
        elevation: 0,
        centerTitle: true,
        titleTextStyle: GoogleFonts.poppins(
          fontSize: 18,
          fontWeight: FontWeight.w600,
          color: Colors.black,
        ),
      ),
      textTheme: TextTheme(
        displayLarge: GoogleFonts.poppins(
          fontSize: 32,
          fontWeight: FontWeight.w700,
          color: Colors.black,
        ),
        displayMedium: GoogleFonts.poppins(
          fontSize: 28,
          fontWeight: FontWeight.w700,
          color: Colors.black,
        ),
        displaySmall: GoogleFonts.poppins(
          fontSize: 24,
          fontWeight: FontWeight.w700,
          color: Colors.black,
        ),
        headlineMedium: GoogleFonts.poppins(
          fontSize: 20,
          fontWeight: FontWeight.w600,
          color: Colors.black,
        ),
        headlineSmall: GoogleFonts.poppins(
          fontSize: 18,
          fontWeight: FontWeight.w600,
          color: Colors.black,
        ),
        titleLarge: GoogleFonts.poppins(
          fontSize: 16,
          fontWeight: FontWeight.w600,
          color: Colors.black,
        ),
        titleMedium: GoogleFonts.poppins(
          fontSize: 14,
          fontWeight: FontWeight.w500,
          color: Colors.black,
        ),
        bodyLarge: GoogleFonts.poppins(
          fontSize: 16,
          fontWeight: FontWeight.w400,
          color: neutralGrey700,
        ),
        bodyMedium: GoogleFonts.poppins(
          fontSize: 14,
          fontWeight: FontWeight.w400,
          color: neutralGrey600,
        ),
        bodySmall: GoogleFonts.poppins(
          fontSize: 12,
          fontWeight: FontWeight.w400,
          color: neutralGrey500,
        ),
        labelLarge: GoogleFonts.poppins(
          fontSize: 14,
          fontWeight: FontWeight.w600,
          color: lightPrimary,
        ),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: lightPrimary,
          foregroundColor: Colors.white,
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          textStyle: GoogleFonts.poppins(
            fontSize: 16,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
      outlinedButtonTheme: OutlinedButtonThemeData(
        style: OutlinedButton.styleFrom(
          foregroundColor: lightPrimary,
          side: const BorderSide(color: lightPrimary),
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
        ),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: neutralGrey50,
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: neutralGrey200),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: neutralGrey200),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: lightPrimary, width: 2),
        ),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: lightError),
        ),
        focusedErrorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: lightError, width: 2),
        ),
        hintStyle: GoogleFonts.poppins(
          fontSize: 14,
          color: neutralGrey400,
        ),
        labelStyle: GoogleFonts.poppins(
          fontSize: 14,
          color: neutralGrey600,
        ),
      ),
      cardTheme: CardThemeData(
        color: lightSurface,
        elevation: 1,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
          side: const BorderSide(color: neutralGrey100),
        ),
      ),
      dividerTheme: const DividerThemeData(
        color: neutralGrey200,
        thickness: 1,
        space: 16,
      ),
    );
  }

  /// Dark Theme
  static ThemeData darkTheme() {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.dark,
      colorScheme: const ColorScheme.dark(
        primary: darkPrimary,
        secondary: darkSecondary,
        tertiary: darkAccent,
        surface: darkSurface,
        background: darkBackground,
        error: darkError,
        errorContainer: darkErrorContainer,
        onErrorContainer: lightError,
      ),
      scaffoldBackgroundColor: darkBackground,
      appBarTheme: AppBarTheme(
        backgroundColor: darkSurface,
        foregroundColor: Colors.white,
        elevation: 0,
        centerTitle: true,
        titleTextStyle: GoogleFonts.poppins(
          fontSize: 18,
          fontWeight: FontWeight.w600,
          color: Colors.white,
        ),
      ),
      textTheme: TextTheme(
        displayLarge: GoogleFonts.poppins(
          fontSize: 32,
          fontWeight: FontWeight.w700,
          color: Colors.white,
        ),
        displayMedium: GoogleFonts.poppins(
          fontSize: 28,
          fontWeight: FontWeight.w700,
          color: Colors.white,
        ),
        displaySmall: GoogleFonts.poppins(
          fontSize: 24,
          fontWeight: FontWeight.w700,
          color: Colors.white,
        ),
        headlineMedium: GoogleFonts.poppins(
          fontSize: 20,
          fontWeight: FontWeight.w600,
          color: Colors.white,
        ),
        headlineSmall: GoogleFonts.poppins(
          fontSize: 18,
          fontWeight: FontWeight.w600,
          color: Colors.white,
        ),
        titleLarge: GoogleFonts.poppins(
          fontSize: 16,
          fontWeight: FontWeight.w600,
          color: Colors.white,
        ),
        titleMedium: GoogleFonts.poppins(
          fontSize: 14,
          fontWeight: FontWeight.w500,
          color: Colors.white,
        ),
        bodyLarge: GoogleFonts.poppins(
          fontSize: 16,
          fontWeight: FontWeight.w400,
          color: neutralGrey200,
        ),
        bodyMedium: GoogleFonts.poppins(
          fontSize: 14,
          fontWeight: FontWeight.w400,
          color: neutralGrey300,
        ),
        bodySmall: GoogleFonts.poppins(
          fontSize: 12,
          fontWeight: FontWeight.w400,
          color: neutralGrey400,
        ),
        labelLarge: GoogleFonts.poppins(
          fontSize: 14,
          fontWeight: FontWeight.w600,
          color: darkPrimary,
        ),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: darkPrimary,
          foregroundColor: Colors.white,
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          textStyle: GoogleFonts.poppins(
            fontSize: 16,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
      outlinedButtonTheme: OutlinedButtonThemeData(
        style: OutlinedButton.styleFrom(
          foregroundColor: darkPrimary,
          side: const BorderSide(color: darkPrimary),
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
        ),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: const Color(0xFF334155),
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: Color(0xFF475569)),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: Color(0xFF475569)),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: darkPrimary, width: 2),
        ),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: darkError),
        ),
        focusedErrorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: darkError, width: 2),
        ),
        hintStyle: GoogleFonts.poppins(
          fontSize: 14,
          color: neutralGrey400,
        ),
        labelStyle: GoogleFonts.poppins(
          fontSize: 14,
          color: neutralGrey300,
        ),
      ),
      cardTheme: CardThemeData(
        color: darkSurface,
        elevation: 1,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
          side: const BorderSide(color: Color(0xFF334155)),
        ),
      ),
      dividerTheme: const DividerThemeData(
        color: Color(0xFF334155),
        thickness: 1,
        space: 16,
      ),
    );
  }
}

extension ColorSchemeExtension on ColorScheme {
  Color get success => brightness == Brightness.light ? AppTheme.lightSuccess : AppTheme.darkSuccess;
  Color get onSuccess => brightness == Brightness.light ? Colors.white : AppTheme.neutralGrey800;
  Color get warning => brightness == Brightness.light ? AppTheme.lightWarning : AppTheme.darkWarning;
  Color get onWarning => brightness == Brightness.light ? Colors.white : AppTheme.neutralGrey800;
  Color get info => brightness == Brightness.light ? AppTheme.lightInfo : AppTheme.darkInfo;
  Color get onInfo => brightness == Brightness.light ? Colors.white : AppTheme.neutralGrey800;
  Color get error => brightness == Brightness.light ? AppTheme.lightError : AppTheme.darkError;
  Color get onError => brightness == Brightness.light ? Colors.white : AppTheme.neutralGrey800;
  Color get errorContainer => brightness == Brightness.light ? AppTheme.lightErrorContainer : AppTheme.darkErrorContainer;
  Color get onErrorContainer => brightness == Brightness.light ? AppTheme.darkError : AppTheme.lightError;
  Color get surfaceVariant => brightness == Brightness.light ? AppTheme.lightSurface : AppTheme.darkSurface;
  Color get onSurfaceVariant => brightness == Brightness.light ? AppTheme.neutralGrey600 : AppTheme.neutralGrey300;
  Color get topBarGradientLeft => brightness == Brightness.light ? AppTheme.topBarGradientLeft : AppTheme.darkTopBarGradientLeft;
  Color get topBarGradientRight => brightness == Brightness.light ? AppTheme.topBarGradientRight : AppTheme.darkTopBarGradientRight;
  Color get topBar => brightness == Brightness.light ? AppTheme.topBar : AppTheme.darkTopBar;
  Color get actionBar => brightness == Brightness.light ? AppTheme.actionBar : AppTheme.darkActionBar;
  Color get onPrimary => brightness == Brightness.light ? Colors.white : AppTheme.neutralGrey800;
  Color get onSecondary => brightness == Brightness.light ? Colors.white : AppTheme.neutralGrey800;
  Color get onTertiary => brightness == Brightness.light ? Colors.white : AppTheme.neutralGrey800;
  Color get onBackground => brightness == Brightness.light ? AppTheme.neutralGrey900 : AppTheme.neutralGrey50;
  Color get onSurface => brightness == Brightness.light ? AppTheme.neutralGrey900 : AppTheme.neutralGrey50;

  // Virtual Card Colors
  Color get virtualCardPrimary => AppTheme.virtualCardPrimary;
  Color get virtualCardSecondary => AppTheme.virtualCardSecondary;
  Color get virtualCardAccent => AppTheme.virtualCardAccent;
  Color get virtualCardBackground => AppTheme.virtualCardBackground;
  Color get virtualCardChipGold => AppTheme.virtualCardChipGold;
  Color get virtualCardChipLightGold => AppTheme.virtualCardChipLightGold;
  Color get virtualCardChipDeeperGold => AppTheme.virtualCardChipDeeperGold;
  Color get virtualCardShadow => AppTheme.virtualCardShadow;
  Color get virtualCardBorder => AppTheme.virtualCardBorder;
  Color get virtualCardChipHighlight => AppTheme.virtualCardChipHighlight;
  Color get virtualCardLogoBg => AppTheme.virtualCardLogoBg;
  Color get virtualCardLogoShadow => AppTheme.virtualCardLogoShadow;
  Color get virtualCardLogoBorder => AppTheme.virtualCardLogoBorder;
  Color get virtualCardErrorIcon => AppTheme.virtualCardErrorIcon;
  Color get virtualCardChipInternalGold => AppTheme.virtualCardChipInternalGold;
}
