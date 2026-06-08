import 'package:dio/dio.dart';
import 'package:fflipy/core/constants/api_config.dart';
import 'package:fflipy/core/constants/app_constants.dart';
import 'package:fflipy/models/profile/active_countries_model.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../models/profile/document_types_model.dart';
import '../models/profile/profile_update_model.dart';
import '../models/profile/remitter_types_model.dart';
import '../models/profile/user_profile_model.dart';

class ProfileService {
  final Dio dio;
  final FlutterSecureStorage secureStorage;

  ProfileService({required this.dio, required this.secureStorage});

  Future<String> _getToken() async {
    final token = await secureStorage.read(key: AppConstants.userTokenKey);
    if (token == null) {
      throw Exception('Auth token not found!');
    }
    return token;
  }

  Future<Options> _getAuthOptions() async {
    final token = await _getToken();
    return Options(headers: {'Authorization': 'Bearer $token'});
  }

  Future<Map<String, dynamic>> updateProfile(ProfileUpdateModel profileUpdateModel) async {
    try {
      final data = await profileUpdateModel.toJson();
      final formData = FormData.fromMap(data);
      final response = await dio.post(ApiConfig.updateProfileUrl, data: formData, options: await _getAuthOptions());
      return response.data;
    } catch (e) {
      print('Error updating profile: $e');
      rethrow;
    }
  }

  Future<ProfileData> getUserProfile() async {
    try {
      final response = await dio.get(ApiConfig.getProfileUrl, options: await _getAuthOptions());
      final userProfile = UserProfileModel.fromJson(response.data['data']);
      final languages = (response.data['data']['languages'] as List)
          .map((lang) => Language.fromJson(lang))
          .toList();
      return ProfileData(userProfile: userProfile, languages: languages);
    } catch (e) {
      print('Error getting user profile: $e');
      rethrow;
    }
  }

  Future<List<RemitterType>> getRemitterTypes() async {
    try {
      final response = await dio.get(ApiConfig.remitterTypesUrl, options: await _getAuthOptions());
      final remitterResponse = RemitterTypesResponse.fromJson(response.data);
      return remitterResponse.data;
    } catch (e) {
      print('Error fetching remitter types: $e');
      rethrow;
    }
  }

  Future<List<DocumentType>> getDocumentTypes() async {
    try {
      final response = await dio.get(ApiConfig.documentTypesUrl, options: await _getAuthOptions());
      final documentResponse = DocumentTypesResponse.fromJson(response.data);
      return documentResponse.data;
    } catch (e) {
      print('Error fetching document types: $e');
      rethrow;
    }
  }

  Future<List<Country>> getActiveCountries() async {
    try {
      final response = await dio.get(ApiConfig.activeCountriesUrl, options: await _getAuthOptions());
      final countryResponse = ActiveCountriesResponse.fromJson(response.data);
      return countryResponse.data;
    } catch (e) {
      print('Error fetching active countries: $e');
      rethrow;
    }
  }
}
