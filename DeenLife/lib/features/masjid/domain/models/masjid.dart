class Masjid {
  final String nameEn;
  final String nameBn;
  final String location;
  final double distance; // in km
  final bool isVerified;
  final String iqamahTime;
  final String nextPrayer;
  final String jumuahTime;
  final bool isFavorite;

  Masjid({
    required this.nameEn,
    this.nameBn = '',
    required this.location,
    required this.distance,
    this.isVerified = false,
    required this.iqamahTime,
    required this.nextPrayer,
    this.jumuahTime = '1:30 PM',
    this.isFavorite = false,
  });
}
