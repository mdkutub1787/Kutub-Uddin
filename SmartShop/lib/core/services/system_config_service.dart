import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../constants/constants.dart';
import '../providers.dart';

class SystemConfig {
  final bool isMaintenanceMode;
  final String maintenanceMessage;
  final double globalDeliveryFee;
  final String latestAppVersion;

  SystemConfig({
    this.isMaintenanceMode = false,
    this.maintenanceMessage = 'App is under maintenance. Please check back later.',
    this.globalDeliveryFee = 60.0,
    this.latestAppVersion = '1.0.0',
  });
}

final systemConfigProvider = AsyncNotifierProvider<SystemConfigNotifier, SystemConfig>(() {
  return SystemConfigNotifier();
});

class SystemConfigNotifier extends AsyncNotifier<SystemConfig> {
  @override
  Future<SystemConfig> build() async {
    return await _fetchConfig();
  }

  Future<SystemConfig> _fetchConfig() async {
    try {
      final supabase = ref.read(supabaseClientProvider);
      final response = await supabase
          .from(AppConstants.systemConfigTable)
          .select();
      
      final data = response as List;
      bool isMaintenance = false;
      String message = '';
      double fee = 60.0;
      String version = '1.0.0';

      for (var item in data) {
        if (item['key'] == 'maintenance_mode') isMaintenance = item['value'] == 'true';
        if (item['key'] == 'maintenance_message') message = item['value'];
        if (item['key'] == 'delivery_fee') fee = double.tryParse(item['value']) ?? 60.0;
        if (item['key'] == 'latest_version') version = item['value'];
      }

      return SystemConfig(
        isMaintenanceMode: isMaintenance,
        maintenanceMessage: message,
        globalDeliveryFee: fee,
        latestAppVersion: version,
      );
    } catch (e) {
      return SystemConfig(); // Default config on error
    }
  }
}
