import 'dart:convert';
import 'package:encrypt/encrypt.dart' as encrypt;
import 'secure_storage_service.dart';

class EncryptionHelper {
  static final SecureStorageService _secureStorage = SecureStorageService();

  static Future<String> encryptData(Map<String, dynamic> data) async {
    try {
      String? encryptionKey = await _secureStorage.getEncryptionKeyFromNative();

      if (encryptionKey.isEmpty) {
        throw Exception('Encryption key not found. Please login again.');
      }

      if (encryptionKey.length != 32) {
        throw Exception('Invalid encryption key length');
      }

      String jsonData = jsonEncode(data);

      final key = encrypt.Key.fromUtf8(encryptionKey);
      final iv = encrypt.IV.fromSecureRandom(16);

      final encrypter = encrypt.Encrypter(
          encrypt.AES(key, mode: encrypt.AESMode.cbc)
      );

      final encrypted = encrypter.encrypt(jsonData, iv: iv);

      List<int> combined = [];
      combined.addAll(iv.bytes);
      combined.addAll(encrypted.bytes);

      String encryptedData = base64Encode(combined);

      return encryptedData;

    } catch (e) {
      print('Encryption Error: $e');
      rethrow;
    }
  }
}