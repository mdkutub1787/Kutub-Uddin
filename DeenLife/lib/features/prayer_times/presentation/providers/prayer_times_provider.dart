import 'package:adhan/adhan.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:geolocator/geolocator.dart';
import 'package:geocoding/geocoding.dart';
import 'package:hijri/hijri_calendar.dart';
import 'package:flutter/foundation.dart';
import 'package:intl/intl.dart';

import '../../../../core/services/notification_service.dart';
import '../../../../features/masjid/presentation/screens/set_masjid_times_screen.dart';
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

  final userIqamahTimes = ref.watch(iqamahTimesProvider);

  String nextName = '';
  DateTime nextTime = DateTime.now();
  DateTime nextCountdownTime = DateTime.now();

  Prayer currentPrayer = prayerTimes.currentPrayer();
  String currentName = '';
  DateTime currentAdhanTime = DateTime.now();
  switch (currentPrayer) {
    case Prayer.fajr: currentName = 'Fajr'; currentAdhanTime = prayerTimes.fajr; break;
    case Prayer.sunrise: currentName = 'Sunrise'; currentAdhanTime = prayerTimes.sunrise; break;
    case Prayer.dhuhr: currentName = 'Dhuhr'; currentAdhanTime = prayerTimes.dhuhr; break;
    case Prayer.asr: currentName = 'Asr'; currentAdhanTime = prayerTimes.asr; break;
    case Prayer.maghrib: currentName = 'Maghrib'; currentAdhanTime = prayerTimes.maghrib; break;
    case Prayer.isha: currentName = 'Isha'; currentAdhanTime = prayerTimes.isha; break;
    case Prayer.none: break;
  }

  bool isBeforeIqamah = false;
  if (currentPrayer != Prayer.none && currentPrayer != Prayer.sunrise) {
    String? iqamahStr = userIqamahTimes[currentName];
    if (iqamahStr != null) {
      try {
        DateTime parsedTime = DateFormat("h:mm a").parse(iqamahStr);
        DateTime iqamahDateTime = DateTime(
          DateTime.now().year,
          DateTime.now().month,
          DateTime.now().day,
          parsedTime.hour,
          parsedTime.minute,
        );
        if (DateTime.now().isBefore(iqamahDateTime)) {
          isBeforeIqamah = true;
          nextName = currentName;
          nextTime = currentAdhanTime; // Hexagon shows Adhan time
          nextCountdownTime = iqamahDateTime; // Countdown tracks Iqamah time
        }
      } catch (e) {
        debugPrint("Error parsing iqamah time: $e");
      }
    }
  }

  if (!isBeforeIqamah) {
    switch (prayerTimes.nextPrayer()) {
      case Prayer.fajr:
        nextName = 'Fajr';
        nextTime = prayerTimes.fajr;
        nextCountdownTime = prayerTimes.fajr;
        break;
      case Prayer.sunrise:
        nextName = 'Sunrise';
        nextTime = prayerTimes.sunrise;
        nextCountdownTime = prayerTimes.sunrise;
        break;
      case Prayer.dhuhr:
        nextName = 'Dhuhr';
        nextTime = prayerTimes.dhuhr;
        nextCountdownTime = prayerTimes.dhuhr;
        break;
      case Prayer.asr:
        nextName = 'Asr';
        nextTime = prayerTimes.asr;
        nextCountdownTime = prayerTimes.asr;
        break;
      case Prayer.maghrib:
        nextName = 'Maghrib';
        nextTime = prayerTimes.maghrib;
        nextCountdownTime = prayerTimes.maghrib;
        break;
      case Prayer.isha:
        nextName = 'Isha';
        nextTime = prayerTimes.isha;
        nextCountdownTime = prayerTimes.isha;
        break;
      case Prayer.none:
        final tomorrow = DateComponents.from(
          DateTime.now().add(const Duration(days: 1)),
        );
        final tomorrowPrayerTimes = PrayerTimes(coordinates, tomorrow, params);
        nextName = 'Fajr';
        nextTime = tomorrowPrayerTimes.fajr;
        nextCountdownTime = tomorrowPrayerTimes.fajr;
        break;
    }
  }

  // Schedule background notifications for today's prayers
  try {
    final ns = NotificationService();
    ns.scheduleAdhan(id: 1, title: 'Fajr Adhan', body: 'It is time for Fajr prayer.', scheduledTime: prayerTimes.fajr);
    ns.scheduleAdhan(id: 2, title: 'Dhuhr Adhan', body: 'It is time for Dhuhr prayer.', scheduledTime: prayerTimes.dhuhr);
    ns.scheduleAdhan(id: 3, title: 'Asr Adhan', body: 'It is time for Asr prayer.', scheduledTime: prayerTimes.asr);
    ns.scheduleAdhan(id: 4, title: 'Maghrib Adhan', body: 'It is time for Maghrib prayer.', scheduledTime: prayerTimes.maghrib);
    ns.scheduleAdhan(id: 5, title: 'Isha Adhan', body: 'It is time for Isha prayer.', scheduledTime: prayerTimes.isha);
  } catch (e) {
    debugPrint('Error scheduling notifications: $e');
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
    nextPrayerCountdownTime: nextCountdownTime,
    city: city,
    hijriDate: hijriFormatted,
  );
});

final sevenDaysPrayerProvider = FutureProvider<List<PrayerTimes>>((ref) async {
  final position = await ref.watch(locationProvider.future);
  final calcMethod = ref.watch(calculationMethodProvider);

  final coordinates = Coordinates(position.latitude, position.longitude);
  final params = calcMethod.getParameters();
  params.madhab = Madhab.hanafi;

  List<PrayerTimes> sevenDays = [];
  for (int i = 0; i < 7; i++) {
    final date = DateComponents.from(DateTime.now().add(Duration(days: i)));
    sevenDays.add(PrayerTimes(coordinates, date, params));
  }
  return sevenDays;
});

