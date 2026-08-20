import 'package:adhan/adhan.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:geolocator/geolocator.dart';
import '../../domain/models/prayer_data.dart';

final locationProvider = FutureProvider<Position>((ref) async {
  bool serviceEnabled;
  LocationPermission permission;

  serviceEnabled = await Geolocator.isLocationServiceEnabled();
  if (!serviceEnabled) {
    throw Exception('Location services are disabled.');
  }

  permission = await Geolocator.checkPermission();
  if (permission == LocationPermission.denied) {
    permission = await Geolocator.requestPermission();
    if (permission == LocationPermission.denied) {
      throw Exception('Location permissions are denied');
    }
  }

  if (permission == LocationPermission.deniedForever) {
    throw Exception('Location permissions are permanently denied, we cannot request permissions.');
  }

  return await Geolocator.getCurrentPosition();
});

final prayerTimesProvider = FutureProvider<PrayerData>((ref) async {
  final position = await ref.watch(locationProvider.future);
  
  final coordinates = Coordinates(position.latitude, position.longitude);
  final date = DateComponents.from(DateTime.now());
  final params = CalculationMethod.karachi.getParameters();
  params.madhab = Madhab.hanafi; // Defaulting to Hanafi for Bangladesh/South Asia

  final prayerTimes = PrayerTimes.today(coordinates, params);

  String nextName = '';
  DateTime nextTime = DateTime.now();

  switch (prayerTimes.nextPrayer()) {
    case Prayer.fajr:
      nextName = 'Fajr';
      nextTime = prayerTimes.fajr;
      break;
    case Prayer.sunrise:
      nextName = 'Sunrise';
      nextTime = prayerTimes.sunrise;
      break;
    case Prayer.dhuhr:
      nextName = 'Dhuhr';
      nextTime = prayerTimes.dhuhr;
      break;
    case Prayer.asr:
      nextName = 'Asr';
      nextTime = prayerTimes.asr;
      break;
    case Prayer.maghrib:
      nextName = 'Maghrib';
      nextTime = prayerTimes.maghrib;
      break;
    case Prayer.isha:
      nextName = 'Isha';
      nextTime = prayerTimes.isha;
      break;
    case Prayer.none:
      // If after Isha, the next prayer is Fajr tomorrow
      final tomorrow = DateComponents.from(DateTime.now().add(const Duration(days: 1)));
      final tomorrowPrayerTimes = PrayerTimes(coordinates, tomorrow, params);
      nextName = 'Fajr';
      nextTime = tomorrowPrayerTimes.fajr;
      break;
  }

  return PrayerData(
    fajr: prayerTimes.fajr,
    dhuhr: prayerTimes.dhuhr,
    asr: prayerTimes.asr,
    maghrib: prayerTimes.maghrib,
    isha: prayerTimes.isha,
    sunrise: prayerTimes.sunrise,
    nextPrayerName: nextName,
    nextPrayerTime: nextTime,
  );
});
