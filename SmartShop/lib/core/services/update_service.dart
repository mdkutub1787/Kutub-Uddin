import 'package:flutter/material.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../config/app_config.dart';

class UpdateService {
  static Future<void> checkForUpdates(BuildContext context) async {
    try {
      final packageInfo = await PackageInfo.fromPlatform();
      final currentVersion = packageInfo.version;

      // Fetch latest version from Supabase (assuming a 'system_config' table exists)
      final response = await Supabase.instance.client
          .from('system_config')
          .select('value')
          .eq('key', 'latest_version')
          .maybeSingle();

      if (response != null) {
        final latestVersion = response['value'] as String;
        if (_isUpdateAvailable(currentVersion, latestVersion)) {
          if (context.mounted) {
            _showUpdateDialog(context, latestVersion);
          }
        }
      }
    } catch (e) {
      debugPrint("Update check failed: \$e");
    }
  }

  static bool _isUpdateAvailable(String current, String latest) {
    List<int> curItems = current.split('.').map(int.parse).toList();
    List<int> latItems = latest.split('.').map(int.parse).toList();
    for (int i = 0; i < curItems.length; i++) {
      if (latItems[i] > curItems[i]) return true;
      if (latItems[i] < curItems[i]) return false;
    }
    return false;
  }

  static void _showUpdateDialog(BuildContext context, String version) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: const Text("Update Available"),
        content: Text("A new version (\$version) of Smart Shop is available. Please update to enjoy the latest features and security patches."),
        actions: [
          ElevatedButton(
            onPressed: () {
              // Redirect to Play Store / App Store
            },
            child: const Text("UPDATE NOW"),
          ),
        ],
      ),
    );
  }
}
