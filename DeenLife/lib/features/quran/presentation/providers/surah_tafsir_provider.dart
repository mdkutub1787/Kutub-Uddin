import 'dart:convert';
import 'dart:io';

import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';

final selectedTafsirIdProvider = StateProvider<int?>(
  (ref) => null,
); // null means default Translation (AlQuran Cloud)

final onlineTafsirsProvider = FutureProvider<List<dynamic>>((ref) async {
  final url = 'https://api.quran.com/api/v4/resources/tafsirs';
  final response = await http.get(Uri.parse(url));

  if (response.statusCode == 200) {
    final data = jsonDecode(response.body);
    return data['tafsirs'] as List<dynamic>;
  } else {
    throw Exception('Failed to load Tafsir list');
  }
});

final surahTafsirProvider = FutureProvider.family<Map<int, String>, int>((
  ref,
  surahNumber,
) async {
  final tafsirId = ref.watch(selectedTafsirIdProvider);

  if (tafsirId == null) {
    // Fetch Default Translation from AlQuran Cloud
    final directory = await getApplicationDocumentsDirectory();
    final file = File('${directory.path}/surah_bn_$surahNumber.json');

    if (await file.exists()) {
      final String jsonString = await file.readAsString();
      final data = jsonDecode(jsonString);
      final ayahs = data['data']['ayahs'] as List<dynamic>;
      return {
        for (int i = 0; i < ayahs.length; i++)
          (i + 1): ayahs[i]['text'].toString(),
      };
    }

    final url = 'https://api.alquran.cloud/v1/surah/$surahNumber/bn.bengali';
    final response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      await file.writeAsString(response.body);
      final data = jsonDecode(response.body);
      final ayahs = data['data']['ayahs'] as List<dynamic>;
      return {
        for (int i = 0; i < ayahs.length; i++)
          (i + 1): ayahs[i]['text'].toString(),
      };
    } else {
      throw Exception('Failed to load Translation');
    }
  } else {
    // Fetch Tafsir from Quran.com API v4
    final directory = await getApplicationDocumentsDirectory();
    final file = File(
      '${directory.path}/tafsir_${tafsirId}_surah_$surahNumber.json',
    );

    if (await file.exists()) {
      final String jsonString = await file.readAsString();
      final data = jsonDecode(jsonString);
      final tafsirs = data['tafsirs'] as List<dynamic>;
      // verse_id in quran.com api is absolute verse ID or sometimes just the verse number?
      // Actually, in the /by_chapter/ endpoint, they have 'verse_id' which is absolute, BUT they also return them in order.
      // So we can just map them sequentially since it returns all verses for the chapter.
      return {
        for (int i = 0; i < tafsirs.length; i++)
          (i + 1): tafsirs[i]['text'].toString(),
      };
    }

    // per_page=300 to ensure we get all verses of even Al-Baqarah (286)
    final url =
        'https://api.quran.com/api/v4/tafsirs/$tafsirId/by_chapter/$surahNumber?per_page=300';
    final response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      await file.writeAsString(response.body);
      final data = jsonDecode(response.body);
      final tafsirs = data['tafsirs'] as List<dynamic>;
      return {
        for (int i = 0; i < tafsirs.length; i++)
          (i + 1): tafsirs[i]['text'].toString(),
      };
    } else {
      throw Exception('Failed to load Tafsir');
    }
  }
});
