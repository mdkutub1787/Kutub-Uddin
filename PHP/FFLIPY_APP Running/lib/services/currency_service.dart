import 'dart:convert';
import 'package:http/http.dart' as http;
import '../core/constants/api_config.dart';
import '../models/currency_rate.dart';

class CurrencyService {
  Future<Map<String, List<CurrencyRate>>> fetchCurrencyRates() async {
    final url = Uri.parse(ApiConfig.baseUrl + ApiConfig.currencyListUrl);
    final response = await http.get(url);
    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      if (data['status'] == true && data['data'] != null) {
        final senderList = (data['data']['sender_currencies'] as List)
            .map((e) => CurrencyRate.fromJson(e)).toList();
        final receiverList = (data['data']['receiver_currencies'] as List)
            .map((e) => CurrencyRate.fromJson(e)).toList();
        return {
          'sender': senderList,
          'receiver': receiverList,
        };
      } else {
        throw Exception(data['message'] ?? 'Failed to fetch currency list');
      }
    } else {
      throw Exception('Failed to fetch currency list');
    }
  }
}
