import 'dart:convert';
import 'dart:io';

import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';
import '../../../../core/constants/api_constants.dart';

final onlineHadithProvider = FutureProvider.family<Map<String, dynamic>, String>((
  ref,
  bookId,
) async {
  final directory = await getApplicationDocumentsDirectory();
  final file = File('${directory.path}/ben_$bookId.json');

  // Check if we already have it cached offline
  if (await file.exists()) {
    final String jsonString = await file.readAsString();
    final data = jsonDecode(jsonString);
    return data as Map<String, dynamic>;
  }

  // If not cached, fetch from the internet (Live API)
  final response = await http.get(Uri.parse(ApiConstants.hadithBook(bookId)));

  if (response.statusCode == 200) {
    // Cache the entire JSON string to the local file for future offline use
    await file.writeAsString(response.body);

    // Parse and return
    final data = jsonDecode(response.body);
    return data as Map<String, dynamic>;
  } else {
    throw Exception('Failed to load Hadiths from internet');
  }
});
