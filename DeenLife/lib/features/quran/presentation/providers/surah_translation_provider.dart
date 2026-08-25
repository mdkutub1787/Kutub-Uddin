import 'dart:convert';
import 'dart:io';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';

final surahTranslationProvider = FutureProvider.family<List<dynamic>, int>((ref, surahNumber) async {
  final directory = await getApplicationDocumentsDirectory();
  final file = File('${directory.path}/surah_bn_$surahNumber.json');

  if (await file.exists()) {
    final String jsonString = await file.readAsString();
    final data = jsonDecode(jsonString);
    return data['data']['ayahs'] as List<dynamic>;
  }

  final url = 'https://api.alquran.cloud/v1/surah/$surahNumber/bn.bengali';
  final response = await http.get(Uri.parse(url));

  if (response.statusCode == 200) {
    await file.writeAsString(response.body);
    final data = jsonDecode(response.body);
    return data['data']['ayahs'] as List<dynamic>;
  } else {
    throw Exception('Failed to load Surah translation');
  }
});
