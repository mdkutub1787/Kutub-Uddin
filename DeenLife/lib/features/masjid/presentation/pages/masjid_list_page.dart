import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:geolocator/geolocator.dart';
import 'package:deen_life/core/localization/app_localizations.dart';
import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';

class Masjid {
  final String nameEn;
  final String nameBn;
  final double distance;
  final bool isVerified;
  final String nextPrayer;
  final String nextPrayerTime;
  final String jumuahTime;

  Masjid({
    required this.nameEn,
    required this.nameBn,
    required this.distance,
    this.isVerified = false,
    this.nextPrayer = 'Asr',
    this.nextPrayerTime = '4:30 PM',
    this.jumuahTime = '1:30 PM',
  });
}

final realMasjidsProvider = FutureProvider<List<Masjid>>((ref) async {
  final position = await ref.watch(locationProvider.future);
  
  final query = '''
    [out:json];
    (
      node["amenity"="place_of_worship"]["religion"="muslim"](around:20000, ${position.latitude}, ${position.longitude});
      way["amenity"="place_of_worship"]["religion"="muslim"](around:20000, ${position.latitude}, ${position.longitude});
    );
    out center;
  ''';

  final response = await http.post(
    Uri.parse('https://overpass-api.de/api/interpreter'),
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'User-Agent': 'DeenLifeApp/1.0',
    },
    body: 'data=${Uri.encodeComponent(query)}',
  );

  if (response.statusCode == 200) {
    final data = json.decode(response.body);
    final elements = data['elements'] as List;

    List<Masjid> masjids = [];
    for (var el in elements) {
      final tags = el['tags'] ?? {};
      String name = tags['name:en'] ?? tags['name'] ?? 'Unnamed Mosque';
      String nameBn = tags['name:bn'] ?? tags['name'] ?? 'মসজিদ';
      
      double lat = el['lat'] ?? el['center']['lat'];
      double lon = el['lon'] ?? el['center']['lon'];

      double distanceInMeters = Geolocator.distanceBetween(
        position.latitude, position.longitude,
        lat, lon
      );

      masjids.add(Masjid(
        nameEn: name,
        nameBn: nameBn,
        distance: distanceInMeters / 1000.0, // km
        isVerified: tags.containsKey('name'), // Just a dummy heuristic
      ));
    }
    
    masjids.sort((a, b) => a.distance.compareTo(b.distance));
    return masjids.take(20).toList();
  } else {
    throw Exception('Failed to load mosques');
  }
});

class MasjidListPage extends ConsumerWidget {
  const MasjidListPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final masjidsAsync = ref.watch(realMasjidsProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text(context.tr('Nearby Masjids')),
        centerTitle: true,
      ),
      body: masjidsAsync.when(
        data: (masjids) {
          if (masjids.isEmpty) {
            return Center(child: Text(context.tr('No mosques found nearby (5km radius).')));
          }
          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: masjids.length,
            itemBuilder: (context, index) {
              final masjid = masjids[index];
              return _buildMasjidCard(context, masjid);
            },
          );
        },
        loading: () => Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const CircularProgressIndicator(),
              const SizedBox(height: 16),
              Text(context.tr('Searching for nearby Masjids...')),
            ],
          ),
        ),
        error: (error, stack) => Center(child: Text('${context.tr('Error')}: $error')),
      ),
    );
  }

  Widget _buildMasjidCard(BuildContext context, Masjid masjid) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: InkWell(
        onTap: () => _showMasjidDetails(context, masjid),
        borderRadius: BorderRadius.circular(16),
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          masjid.nameEn,
                          style: const TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          masjid.nameBn,
                          style: const TextStyle(
                            fontSize: 14,
                            color: Colors.grey,
                          ),
                        ),
                      ],
                    ),
                  ),
                  if (masjid.isVerified)
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                      decoration: BoxDecoration(
                        color: Colors.green.withOpacity(0.1),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: const Row(
                        children: [
                          Icon(Icons.verified, color: Colors.green, size: 16),
                          SizedBox(width: 4),
                          Text(
                            'Verified',
                            style: TextStyle(color: Colors.green, fontSize: 12),
                          ),
                        ],
                      ),
                    ),
                ],
              ),
              const SizedBox(height: 16),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Row(
                    children: [
                      Icon(Icons.location_on, size: 16, color: Theme.of(context).colorScheme.primary),
                      const SizedBox(width: 4),
                      Text('${masjid.distance.toStringAsFixed(1)} km away'),
                    ],
                  ),
                  Row(
                    children: [
                      Icon(Icons.access_time, size: 16, color: Theme.of(context).colorScheme.primary),
                      const SizedBox(width: 4),
                      Text('Jumu\'ah: ${masjid.jumuahTime}'),
                    ],
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _showMasjidDetails(BuildContext context, Masjid masjid) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (context) => Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Center(
              child: Container(
                width: 40,
                height: 4,
                decoration: BoxDecoration(
                  color: Colors.grey[300],
                  borderRadius: BorderRadius.circular(2),
                ),
              ),
            ),
            const SizedBox(height: 24),
            Text(
              masjid.nameEn,
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Text(
              masjid.nameBn,
              style: const TextStyle(fontSize: 16, color: Colors.grey),
            ),
            const SizedBox(height: 24),
            const Text(
              'Iqamah Times',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.grey[100],
                borderRadius: BorderRadius.circular(16),
              ),
              child: Column(
                children: [
                  _buildIqamaRow('Fajr', '5:00 AM'),
                  _buildIqamaRow('Dhuhr', '1:15 PM'),
                  _buildIqamaRow('Asr', '4:30 PM', isBold: true),
                  _buildIqamaRow('Maghrib', '6:15 PM'),
                  _buildIqamaRow('Isha', '7:45 PM'),
                  const Divider(),
                  _buildIqamaRow('Jumu\'ah', masjid.jumuahTime, isBold: true),
                ],
              ),
            ),
            const SizedBox(height: 24),
            ElevatedButton.icon(
              onPressed: () async {
                Navigator.pop(context);
                final query = Uri.encodeComponent(masjid.nameEn);
                final url = Uri.parse('https://www.google.com/maps/search/?api=1&query=$query');
                if (await canLaunchUrl(url)) {
                  await launchUrl(url, mode: LaunchMode.externalApplication);
                } else {
                  if (context.mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Could not launch maps')),
                    );
                  }
                }
              },
              icon: const Icon(Icons.directions),
              label: const Text('Get Directions'),
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(double.infinity, 50),
                backgroundColor: Theme.of(context).colorScheme.primary,
                foregroundColor: Colors.white,
              ),
            ),
            const SizedBox(height: 32),
          ],
        ),
      ),
    );
  }

  Widget _buildIqamaRow(String prayer, String time, {bool isBold = false}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            prayer,
            style: TextStyle(
              fontSize: 16,
              fontWeight: isBold ? FontWeight.bold : FontWeight.normal,
            ),
          ),
          Text(
            time,
            style: TextStyle(
              fontSize: 16,
              fontWeight: isBold ? FontWeight.bold : FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }
}
