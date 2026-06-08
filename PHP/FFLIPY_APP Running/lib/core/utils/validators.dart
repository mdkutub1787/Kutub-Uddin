import 'package:fflipy/core/constants/app_constants.dart';
import 'package:fflipy/core/localization/app_localizations.dart';

class Validators {
  static String? validateEmail(String? value, AppLocalizations localizations) {
    if (value == null || value.isEmpty) {
      return localizations.translate("The email field is required.");
    }

    final emailRegex = RegExp(AppConstants.emailPattern);
    if (!emailRegex.hasMatch(value)) {
      return localizations.translate("Invalid email address");
    }

    return null;
  }

  static String? validatePhoneNumber(String? value,
      AppLocalizations localizations) {
    if (value == null || value.isEmpty) {
      return localizations.translate("Phone number is required");
    }

    final phoneRegex = RegExp(AppConstants.phoneNumberPattern);
    if (!phoneRegex.hasMatch(value)) {
      return localizations.translate("Invalid phone number");
    }

    return null;
  }

  static String? validatePassword(String? value,
      AppLocalizations localizations) {
    if (value == null || value.isEmpty) {
      return localizations.translate("Password is required");
    }

    if (value.length < AppConstants.minPasswordLength) {
      return localizations.translate("Password must be at least 8 characters");
    }

    final passwordRegex = RegExp(AppConstants.passwordPattern);
    if (!passwordRegex.hasMatch(value)) {
      return localizations.translate("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
    }

    return null;
  }

  static String? validateConfirmPassword(String? value,
      String? password,
      AppLocalizations localizations,) {
    if (value == null || value.isEmpty) {
      return localizations.translate("Confirm Password is required");
    }

    if (value != password) {
      return localizations.translate("Passwords do not match");
    }

    return null;
  }

  static String? validateName(String? value, AppLocalizations localizations) {
    if (value == null || value.isEmpty) {
      return localizations.translate("Name is required");
    }

    if (value.length < AppConstants.minNameLength) {
      return localizations.translate("Name is too short");
    }

    if (!RegExp(r'^[a-zA-Z\s]+$').hasMatch(value)) {
      return localizations.translate("Name contains invalid characters");
    }

    return null;
  }

  static String? validateUsername(String? value,
      AppLocalizations localizations) {
    if (value == null || value.isEmpty) {
      return localizations.translate("Username is required");
    }

    if (value.length < 5) {
      return localizations.translate(
          "The username must be at least 5 characters");
    }

    return null;
  }
}