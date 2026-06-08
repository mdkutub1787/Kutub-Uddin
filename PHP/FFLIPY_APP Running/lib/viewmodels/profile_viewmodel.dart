import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../repositories/profile_repository.dart';
import '../models/profile/profile_update_model.dart';
import '../models/profile/user_profile_model.dart';

class ProfileViewModel extends StateNotifier<AsyncValue<ProfileData>> {
  final ProfileRepository _profileRepository;

  ProfileViewModel(this._profileRepository) : super(const AsyncValue.loading()) {
    loadUserProfile();
  }

  Future<void> loadUserProfile() async {
    if (!mounted) return;
    state = const AsyncValue.loading();
    try {
      final profileData = await _profileRepository.getUserProfile();
      if (mounted) {
        state = AsyncValue.data(profileData);
      }
    } catch (e, s) {
      if (mounted) {
        state = AsyncValue.error(e, s);
      }
    }
  }

  Future<String> updateUserProfile(ProfileUpdateModel profileUpdateModel) async {
    final previousState = state.asData?.value;
    try {
      final response = await _profileRepository.updateProfile(profileUpdateModel);
      final message = response['message'] as String;

      if (mounted) {
        final currentData = state.value;
        if (currentData != null) {
          // Instead of manually updating with potentially partial data,
          // we reload the full profile to ensure consistency.
          await loadUserProfile();
        } else {
          await loadUserProfile();
        }
      }
      return message;
    } catch (e) {
      if (mounted && previousState != null) {
        state = AsyncValue.data(previousState);
      }
      rethrow;
    }
  }
}
