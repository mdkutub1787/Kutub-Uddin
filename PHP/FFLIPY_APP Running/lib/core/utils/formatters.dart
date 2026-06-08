import 'package:intl/intl.dart';
import 'dart:convert';
import 'package:fflipy/core/localization/app_localizations.dart';

class Formatters {
  static String formatDate(DateTime date, {String format = 'MMM dd, yyyy', String? locale}) {
    try {
      final formatter = DateFormat(format, locale);
      return formatter.format(date);
    } catch (e) {
      return date.toString();
    }
  }

  static String formatDateTime(DateTime dateTime,
      {String format = 'MMM dd, yyyy \'at\' h:mm a', String? locale}) {
    try {
      final formatter = DateFormat(format, locale);
      return formatter.format(dateTime);
    } catch (e) {
      return dateTime.toString();
    }
  }

  static String formatTime(DateTime dateTime, {String format = 'h:mm a', String? locale}) {
    try {
      final formatter = DateFormat(format, locale);
      return formatter.format(dateTime);
    } catch (e) {
      return dateTime.toString();
    }
  }

  static String formatCurrency(
      double amount, {
        String currencySymbol = '\$',
        int decimalPlaces = 2,
      }) {
    try {
      return '$currencySymbol${amount.toStringAsFixed(decimalPlaces)}';
    } catch (e) {
      return '$currencySymbol${0.toStringAsFixed(decimalPlaces)}';
    }
  }

  static String formatNumber(
      double number, {
        int decimalPlaces = 2,
      }) {
    try {
      final formatter = NumberFormat('#,##0.##');
      return formatter.format(number);
    } catch (e) {
      return number.toString();
    }
  }

  static String formatPhoneNumber(String phoneNumber) {
    try {
      final cleaned = phoneNumber.replaceAll(RegExp(r'\D'), '');

      if (cleaned.length == 10) {
        return '(${cleaned.substring(0, 3)}) ${cleaned.substring(3, 6)}-${cleaned.substring(6)}';
      } else if (cleaned.length == 11) {
        return '+${cleaned.substring(0, 1)} (${cleaned.substring(1, 4)}) ${cleaned.substring(4, 7)}-${cleaned.substring(7)}';
      }

      return phoneNumber;
    } catch (e) {
      return phoneNumber;
    }
  }

  static String formatFileSize(int bytes) {
    try {
      if (bytes <= 0) return '0 B';

      const suffixes = ['B', 'KB', 'MB', 'GB', 'TB'];
      int index = 0;
      double size = bytes.toDouble();

      while (size >= 1024 && index < suffixes.length - 1) {
        size /= 1024;
        index++;
      }

      return '${size.toStringAsFixed(2)} ${suffixes[index]}';
    } catch (e) {
      return '0 B';
    }
  }

  static String formatDuration(Duration duration) {
    try {
      final hours = duration.inHours;
      final minutes = duration.inMinutes.remainder(60);
      final seconds = duration.inSeconds.remainder(60);

      if (hours > 0) {
        return '${hours}h ${minutes}m';
      } else if (minutes > 0) {
        return '${minutes}m ${seconds}s';
      } else {
        return '${seconds}s';
      }
    } catch (e) {
      return '0s';
    }
  }

  static String formatPercentage(double value, {int decimalPlaces = 1}) {
    try {
      return '${(value * 100).toStringAsFixed(decimalPlaces)}%';
    } catch (e) {
      return '0%';
    }
  }

  static String formatRating(double rating, {int maxRating = 5}) {
    try {
      return '${rating.toStringAsFixed(1)}/$maxRating';
    } catch (e) {
      return '0/$maxRating';
    }
  }

  static String capitalize(String text) {
    if (text.isEmpty) return text;
    return text[0].toUpperCase() + text.substring(1).toLowerCase();
  }

  static String toTitleCase(String text) {
    try {
      return text
          .split(' ')
          .map((word) => capitalize(word))
          .join(' ');
    } catch (e) {
      return text;
    }
  }

  static String truncate(
      String text, {
        int maxLength = 50,
        String ellipsis = '...',
      }) {
    if (text.length <= maxLength) {
      return text;
    }
    return '${text.substring(0, maxLength - ellipsis.length)}$ellipsis';
  }

 static String formatRelativeTime(DateTime dateTime, AppLocalizations loc) {
    try {
      final now = DateTime.now();
      final difference = now.difference(dateTime);

     String ago = loc.translate('ago');

      if (difference.inSeconds < 60) {
        return loc.translate('just now');
      } else if (difference.inMinutes < 60) {
        final minutes = difference.inMinutes;
        String unit = loc.translate(minutes > 1 ? 'minutes' : 'minute');
        return '$minutes $unit $ago';
      } else if (difference.inHours < 24) {
        final hours = difference.inHours;
        String unit = loc.translate(hours > 1 ? 'hours' : 'hour');
        return '$hours $unit $ago';
      } else if (difference.inDays < 7) {
        final days = difference.inDays;
        String unit = loc.translate(days > 1 ? 'days' : 'day');
        return '$days $unit $ago';
      } else if (difference.inDays < 30) {
        final weeks = (difference.inDays / 7).floor();
        String unit = loc.translate(weeks > 1 ? 'weeks' : 'week');
        return '$weeks $unit $ago';
      } else if (difference.inDays < 365) {
        final months = (difference.inDays / 30).floor();
        String unit = loc.translate(months > 1 ? 'months' : 'month');
        return '$months $unit $ago';
      } else {
        final years = (difference.inDays / 365).floor();
        String unit = loc.translate(years > 1 ? 'years' : 'year');
        return '$years $unit $ago';
      }
    } catch (e) {
      return loc.translate('unknown time');
    }
  }

  static String formatAddress({
    required String street,
    required String city,
    required String state,
    required String zipCode,
  }) {
    try {
      return '$street, $city, $state $zipCode';
    } catch (e) {
      return '';
    }
  }

  static String removeSpecialCharacters(String text) {
    try {
      return text.replaceAll(RegExp(r'[^a-zA-Z0-9\s]'), '');
    } catch (e) {
      return text;
    }
  }

  static String camelCaseToSnakeCase(String text) {
    try {
      return text
          .replaceAllMapped(
        RegExp(r'[A-Z]'),
            (match) => '_${match.group(0)!.toLowerCase()}',
      )
          .replaceFirst('_', '');
    } catch (e) {
      return text;
    }
  }

  static String snakeCaseToCamelCase(String text) {
    try {
      final parts = text.split('_');
      return parts.first +
          parts.skip(1).map((part) => capitalize(part)).join();
    } catch (e) {
      return text;
    }
  }

  static String formatJson(String jsonString, {int indent = 2}) {
    try {
      final dynamic json = jsonDecode(jsonString);
      final encoder = JsonEncoder.withIndent(' ' * indent);
      return encoder.convert(json);
    } catch (e) {
      return jsonString;
    }
  }
}
