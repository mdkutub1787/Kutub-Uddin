import 'package:fflipy/models/profile/active_countries_model.dart';
import 'package:fflipy/services/profile_service.dart';
import '../models/profile/document_types_model.dart';
import '../models/profile/profile_update_model.dart';
import '../models/profile/remitter_types_model.dart';
import '../models/profile/user_profile_model.dart';

class ProfileRepository {
  final ProfileService _profileService;

  ProfileRepository(this._profileService);

  Future<ProfileData> getUserProfile() {
    return _profileService.getUserProfile();
  }

  Future<Map<String, dynamic>> updateProfile(ProfileUpdateModel profileUpdateModel) async {
    final response = await _profileService.updateProfile(profileUpdateModel);

    final userProfile = UserProfileModel.fromJson(response['data']);
    final message = response['message'] as String? ?? 'Profile updated successfully';

    return {
      'userProfile': userProfile,
      'message': message,
    };
  }

  Future<List<RemitterType>> getRemitterTypes() {
    return _profileService.getRemitterTypes();
  }

  Future<List<DocumentType>> getDocumentTypes() {
    return _profileService.getDocumentTypes();
  }

  Future<List<Country>> getActiveCountries() {
    return _profileService.getActiveCountries();
  }
}
