class PrayerData {
  final DateTime fajr;
  final DateTime dhuhr;
  final DateTime asr;
  final DateTime maghrib;
  final DateTime isha;
  final DateTime sunrise;
  
  final String nextPrayerName;
  final DateTime nextPrayerTime;

  PrayerData({
    required this.fajr,
    required this.dhuhr,
    required this.asr,
    required this.maghrib,
    required this.isha,
    required this.sunrise,
    required this.nextPrayerName,
    required this.nextPrayerTime,
  });
}
