class ApiConstants {
  static const String quranComBaseUrl = 'https://api.quran.com/api/v4';
  static const String alQuranCloudBaseUrl = 'https://api.alquran.cloud/v1';
  static const String overpassApiUrl = 'https://overpass-api.de/api/interpreter';
  static const String hadithApiBaseUrl = 'https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions';

  // Tafsir Resources
  static const String tafsirResources = '$quranComBaseUrl/resources/tafsirs';

  // Surah Translation (AlQuran Cloud)
  static String surahTranslation(int surahNumber) =>
      '$alQuranCloudBaseUrl/surah/$surahNumber/bn.bengali';

  // Surah Tafsir (Quran.com)
  static String surahTafsir(int tafsirId, int surahNumber) =>
      '$quranComBaseUrl/tafsirs/$tafsirId/by_chapter/$surahNumber?per_page=400';

  // Hadith API
  static String hadithBook(String bookId) =>
      '$hadithApiBaseUrl/ben-$bookId.json';

  // Radio Streams
  static const String radioMakkah = 'https://qurango.net/radio/makkah';
  static const String radioMadinah = 'https://qurango.net/radio/madinah';
  static const String radioMishary = 'https://stream.radiojar.com/8s5u8tp48vduv';
  static const String radioEnglish = 'https://stream.zeno.fm/3r77vwa8mreuv';
  static const String radioBangla = 'https://qurango.net/radio/tarjumat_bangla';

  // External Links
  static String googleMapsSearch(String query) =>
      'https://www.google.com/maps/search/?api=1&query=${Uri.encodeComponent(query)}';
  static const String playStoreUrl = 'https://play.google.com/store/apps';
  static const String privacyPolicyUrl = 'https://google.com';
}
