import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/location_settings_provider.dart';
import '../../domain/models/bd_districts.dart';

class LocationSettingsScreen extends ConsumerWidget {
  const LocationSettingsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final settings = ref.watch(locationSettingsProvider);

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: const Text('Location', style: TextStyle(color: Colors.black, fontWeight: FontWeight.bold)),
        backgroundColor: Colors.white,
        elevation: 0,
        centerTitle: true,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back_ios, color: Colors.black),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: Text(
              'LOCATION',
              style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: Colors.black),
            ),
          ),
          SwitchListTile(
            title: const Text('Current Location', style: TextStyle(fontSize: 16)),
            value: settings.isLiveLocationEnabled,
            activeColor: Colors.blue,
            onChanged: (bool value) {
              ref.read(locationSettingsProvider.notifier).setLiveLocation(value);
            },
          ),
          if (!settings.isLiveLocationEnabled)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
              child: DropdownButtonFormField<BdDistrict>(
                decoration: const InputDecoration(
                  labelText: 'Select District',
                  border: OutlineInputBorder(),
                ),
                value: settings.selectedDistrict,
                isExpanded: true,
                items: bdDistricts.map((BdDistrict district) {
                  return DropdownMenuItem<BdDistrict>(
                    value: district,
                    child: Text('${district.name} (${district.bnName})'),
                  );
                }).toList(),
                onChanged: (BdDistrict? newValue) {
                  if (newValue != null) {
                    ref.read(locationSettingsProvider.notifier).setSelectedDistrict(newValue);
                  }
                },
              ),
            ),
        ],
      ),
    );
  }
}
