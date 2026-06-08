import 'package:fflipy/models/profile/active_countries_model.dart';
import 'package:fflipy/repositories/profile_repository.dart';
import 'package:fflipy/services/profile_service.dart';
import 'package:fflipy/viewmodels/profile_viewmodel.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../models/profile/document_types_model.dart';
import '../models/profile/remitter_types_model.dart';
import '../models/profile/user_profile_model.dart';
import 'dio_provider.dart';

final profileServiceProvider = Provider.autoDispose<ProfileService>((ref) {
  final dio = ref.watch(dioProvider);
  const secureStorage = FlutterSecureStorage();
  return ProfileService(dio: dio, secureStorage: secureStorage);
});

final profileRepositoryProvider = Provider.autoDispose<ProfileRepository>((ref) {
  final profileService = ref.watch(profileServiceProvider);
  return ProfileRepository(profileService);
});

final profileViewModelProvider =
    StateNotifierProvider.autoDispose<ProfileViewModel, AsyncValue<ProfileData>>((ref) {
  final profileRepository = ref.watch(profileRepositoryProvider);
  return ProfileViewModel(profileRepository);
});

final remitterTypesProvider = FutureProvider.autoDispose<List<RemitterType>>((ref) {
  final repository = ref.watch(profileRepositoryProvider);
  return repository.getRemitterTypes();
});

final documentTypesProvider = FutureProvider.autoDispose<List<DocumentType>>((ref) {
  final repository = ref.watch(profileRepositoryProvider);
  return repository.getDocumentTypes();
});

final activeCountriesProvider = FutureProvider.autoDispose<List<Country>>((ref) {
  final repository = ref.watch(profileRepositoryProvider);
  return repository.getActiveCountries();
});
