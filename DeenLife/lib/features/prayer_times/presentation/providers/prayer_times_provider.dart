import 'package:adhan/adhan.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:geolocator/geolocator.dart';
import 'package:geocoding/geocoding.dart';
import 'package:hijri/hijri_calendar.dart';

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
    throw Exception(
      'Location permissions are permanently denied, we cannot request permissions.',
    );
  }

  return await Geolocator.getCurrentPosition();
});

final calculationMethodProvider = StateProvider<CalculationMethod>((ref) {
  return CalculationMethod.karachi;
});

final prayerTimesProvider = FutureProvider<PrayerData>((ref) async {
  final position = await ref.watch(locationProvider.future);
  final calcMethod = ref.watch(calculationMethodProvider);

  final coordinates = Coordinates(position.latitude, position.longitude);
  final params = calcMethod.getParameters();
  params.madhab = Madhab.hanafi;

  final prayerTimes = PrayerTimes.today(coordinates, params);

  // Get Hijri Date using hijri package
  final hijri = HijriCalendar.now();
  final hijriFormatted = "${hijri.hDay} ${hijri.longMonthName} ${hijri.hYear}";

  // Get City Name
  String city = "Unknown";
  try {
    List<Placemark> placemarks = await placemarkFromCoordinates(
      position.latitude,
      position.longitude,
    );
    if (placemarks.isNotEmpty) {
      city =
          placemarks.first.locality ??
          placemarks.first.subAdministrativeArea ??
          "Unknown";
    }
  } catch (e) {
    city = "Location Error";
  }

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
      final tomorrow = DateComponents.from(
        DateTime.now().add(const Duration(days: 1)),
      );
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
    city: city,
    hijriDate: hijriFormatted,
  );
});
