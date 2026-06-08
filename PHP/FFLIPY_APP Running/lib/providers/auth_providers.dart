import 'package:fflipy/repositories/auth_repository.dart';
import 'package:fflipy/services/auth_service.dart';
import 'package:fflipy/viewmodels/auth_viewmodel.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'dio_provider.dart';

final authServiceProvider = Provider.autoDispose<AuthService>((ref) {
  final dio = ref.watch(dioProvider);
  return AuthService(dio);
});

final authRepositoryProvider = Provider.autoDispose<AuthRepository>((ref) {
  final authService = ref.watch(authServiceProvider);
  return AuthRepository(authService);
});

final authViewModelProvider =
    StateNotifierProvider.autoDispose<AuthViewModel, AuthState>((ref) {
  final authRepository = ref.watch(authRepositoryProvider);
  return AuthViewModel(authRepository);
});

final isLoggedInProvider = Provider.autoDispose<bool>((ref) {
  final authState = ref.watch(authViewModelProvider);
  return authState.responseModelUser?.user != null &&
      authState.responseModelUser?.user?.emailVerification == '1';
});

final sessionExpiredProvider = StateProvider.autoDispose<bool>((ref) => false);
