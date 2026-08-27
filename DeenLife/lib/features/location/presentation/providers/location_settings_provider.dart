import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../domain/models/bd_districts.dart';

class LocationSettings {
  final bool isLiveLocationEnabled;
  final BdDistrict selectedDistrict;

  LocationSettings({
    required this.isLiveLocationEnabled,
    required this.selectedDistrict,
  });

  LocationSettings copyWith({
    bool? isLiveLocationEnabled,
    BdDistrict? selectedDistrict,
  }) {
    return LocationSettings(
      isLiveLocationEnabled: isLiveLocationEnabled ?? this.isLiveLocationEnabled,
      selectedDistrict: selectedDistrict ?? this.selectedDistrict,
    );
  }
}

class LocationSettingsNotifier extends StateNotifier<LocationSettings> {
  LocationSettingsNotifier()
      : super(LocationSettings(
          isLiveLocationEnabled: true,
          selectedDistrict: bdDistricts.firstWhere((d) => d.name == 'Dhaka'),
        )) {
    _loadSettings();
  }

  Future<void> _loadSettings() async {
    final prefs = await SharedPreferences.getInstance();
    final isLive = prefs.getBool('isLiveLocationEnabled') ?? true;
    final districtName = prefs.getString('selectedDistrictName') ?? 'Dhaka';
    
    final district = bdDistricts.firstWhere(
      (d) => d.name == districtName,
      orElse: () => bdDistricts.firstWhere((d) => d.name == 'Dhaka'),
    );

    state = LocationSettings(
      isLiveLocationEnabled: isLive,
      selectedDistrict: district,
    );
  }

  Future<void> setLiveLocation(bool isLive) async {
    state = state.copyWith(isLiveLocationEnabled: isLive);
    final prefs = await SharedPreferences.getInstance();
    await prefs.setBool('isLiveLocationEnabled', isLive);
  }

  Future<void> setSelectedDistrict(BdDistrict district) async {
    state = state.copyWith(selectedDistrict: district, isLiveLocationEnabled: false);
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('selectedDistrictName', district.name);
    await prefs.setBool('isLiveLocationEnabled', false);
  }
}

final locationSettingsProvider =
    StateNotifierProvider<LocationSettingsNotifier, LocationSettings>((ref) {
  return LocationSettingsNotifier();
});
