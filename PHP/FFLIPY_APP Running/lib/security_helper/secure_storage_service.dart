import 'package:flutter/services.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../core/constants/app_constants.dart';

class SecureStorageService {
  final _storage = const FlutterSecureStorage();

  static const _platformChannel = MethodChannel('com.logicsoftbd.fflipy/security');

  Future<String> getEncryptionKeyFromNative() async {
    try {
      final String key = await _platformChannel.invokeMethod('getEncKey');
      return key;
    } on PlatformException catch (e) {
      throw Exception("Failed to get Native Key: '${e.message}'.");
    }
  }

  Future<void> saveToken(String token) async {
    await _storage.write(key: AppConstants.userTokenKey, value: token);
  }

  Future<String?> getToken() async {
    return await _storage.read(key: AppConstants.userTokenKey);
  }

  Future<void> saveUserData(String userData) async {
    await _storage.write(key: AppConstants.userDataKey, value: userData);
  }

  Future<String?> getUserData() async {
    return await _storage.read(key: AppConstants.userDataKey);
  }

  Future<void> clearSession() async {
    await _storage.delete(key: AppConstants.userTokenKey);
    await _storage.delete(key: AppConstants.userDataKey);
  }
}
