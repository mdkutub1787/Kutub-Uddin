import 'package:flutter/material.dart';

class AppConstants {
  static const Duration connectionTimeout = Duration(seconds: 30);
  static const Duration receiveTimeout = Duration(seconds: 30);

  static const String appName = 'FFLIPY';
  static const String appVersion = 'V-1.2.34.1+';
  static const String appDescription = 'Professional and Trusted Money Transfer App';
  static const String appUrl = 'https://dev.fflipy.com';

  static const String userTokenKey = 'user_token';
  static const String userDataKey = 'user_data';
  static const String themeKey = 'theme_mode';
  static const String languageKey = 'language';
  static const String onboardingCompletedKey = 'onboarding_completed';
  static const String userLocationKey = 'user_location';
  static const String tokenKey = 'auth_token';
  static const String userSettingsKey = 'user_settings';

  static const Duration splashDuration = Duration(milliseconds: 4000);
  static const Duration animationDuration = Duration(milliseconds: 300);
  static const Duration shortAnimationDuration = Duration(milliseconds: 150);

  static const int pageSize = 20;
  static const int initialPage = 1;

  static const String networkError = 'Network error occurred';
  static const String serverError = 'Server error occurred';
  static const String unauthorizedError = 'Unauthorized access';
  static const String notFoundError = 'Resource not found';
  static const String validationError = 'Validation error';
  static const String unknownError = 'Unknown error occurred';

  static const String loginSuccess = 'Login successful';
  static const String signupSuccess = 'Account created successfully';
  static const String logoutSuccess = 'Logged out successfully';
  static const String passwordResetSuccess = 'Password reset successful';

  static const int minPasswordLength = 8;
  static const int maxPasswordLength = 128;
  static const int minNameLength = 2;
  static const int maxNameLength = 50;
  static const String passwordPattern = r'^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$';
  static const String phoneNumberPattern = r'^\+?[0-9]{10,15}$';
  static const String mobileNumberPattern = r'^\+?[0-9]{10,15}$';
  static const String otpPattern = r'^[0-9]{4,6}$';
  static const String emailPattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$';

  static const String sharedPreferencesBox = 'fflipy_box';

  static const double borderRadius = 12.0;
  static const double cardElevation = 4.0;
  static const EdgeInsets pagePadding = EdgeInsets.all(16.0);

  static const String facebookUrl = 'https://facebook.com/fflipy';
  static const String twitterUrl = 'https://twitter.com/fflipy';
  static const String instagramUrl = 'https://instagram.com/fflipy';
}
