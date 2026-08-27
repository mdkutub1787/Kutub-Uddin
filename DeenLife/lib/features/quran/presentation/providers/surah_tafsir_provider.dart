import 'dart:convert';
import 'dart:io';

import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';
import '../../../../core/constants/api_constants.dart';

final selectedTafsirIdProvider = StateProvider<int?>(
  (ref) => null,
); // null means default Translation (AlQuran Cloud)

final onlineTafsirsProvider = FutureProvider<List<dynamic>>((ref) async {
  final response = await http.get(Uri.parse(ApiConstants.tafsirResources));

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
      Map<int, String> tafsirMap = {};
      for (var a in ayahs) {
        tafsirMap[a['numberInSurah'] as int] = a['text'].toString();
      }
      return tafsirMap;
    }

    final response = await http.get(Uri.parse(ApiConstants.surahTranslation(surahNumber)));

    if (response.statusCode == 200) {
      await file.writeAsString(response.body);
      final data = jsonDecode(response.body);
      final ayahs = data['data']['ayahs'] as List<dynamic>;
      Map<int, String> tafsirMap = {};
      for (var a in ayahs) {
        tafsirMap[a['numberInSurah'] as int] = a['text'].toString();
      }
      return tafsirMap;
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
      Map<int, String> tafsirMap = {};
      for (var t in tafsirs) {
        if (t['verse_key'] != null) {
          final parts = t['verse_key'].toString().split(':');
          if (parts.length == 2) {
            tafsirMap[int.parse(parts[1])] = t['text'].toString();
          }
        }
      }
      return tafsirMap;
    }

    final response = await http.get(Uri.parse(ApiConstants.surahTafsir(tafsirId, surahNumber)));

    if (response.statusCode == 200) {
      await file.writeAsString(response.body);
      final data = jsonDecode(response.body);
      final tafsirs = data['tafsirs'] as List<dynamic>;
      Map<int, String> tafsirMap = {};
      for (var t in tafsirs) {
        if (t['verse_key'] != null) {
          final parts = t['verse_key'].toString().split(':');
          if (parts.length == 2) {
            tafsirMap[int.parse(parts[1])] = t['text'].toString();
          }
        }
      }
      return tafsirMap;
    } else {
      throw Exception('Failed to load Tafsir');
    }
  }
});

