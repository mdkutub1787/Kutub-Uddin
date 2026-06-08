import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/errors/exceptions.dart';
import 'package:fflipy/models/auth/login_model.dart';
import 'package:fflipy/models/auth/mail_verify_model.dart';
import 'package:fflipy/models/auth/registration_model.dart';
import 'package:fflipy/models/auth/update_password_model.dart';
import 'package:fflipy/models/auth/forgot_password_model.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:fflipy/repositories/auth_repository.dart';
import '../models/auth/resend_code_model.dart';

class AuthState {
  final bool isLoading;
  final String? error;
  final String? successMessage;
  final LoginResponse? responseModelUser;
  final RegistrationResponse? registrationResponse;
  final UpdatePasswordResponse? updatePasswordResponse;
  final ForgotPasswordResponse? forgotPasswordResponse;
  final String? tempToken;
  final String? emailForVerification;
  final Map<String, dynamic>? validationErrors;

  AuthState({
    this.isLoading = false,
    this.error,
    this.successMessage,
    this.responseModelUser,
    this.registrationResponse,
    this.updatePasswordResponse,
    this.forgotPasswordResponse,
    this.tempToken,
    this.emailForVerification,
    this.validationErrors,
  });

  AuthState copyWith({
    bool? isLoading,
    String? error,
    String? successMessage,
    LoginResponse? responseModelUser,
    RegistrationResponse? registrationResponse,
    UpdatePasswordResponse? updatePasswordResponse,
    ForgotPasswordResponse? forgotPasswordResponse,
    String? tempToken,
    String? emailForVerification,
    Map<String, dynamic>? validationErrors,
    bool clearError = false,
    bool clearSuccessMessage = false,
    bool clearValidationErrors = false,
    bool clearTempToken = false,
    bool clearResponseModelUser = false,
    bool clearRegistrationResponse = false,
    bool clearForgotPasswordResponse = false,
  }) {
    return AuthState(
      isLoading: isLoading ?? this.isLoading,
      error: clearError ? null : error ?? this.error,
      successMessage:
          clearSuccessMessage ? null : successMessage ?? this.successMessage,
      responseModelUser:
          clearResponseModelUser ? null : responseModelUser ?? this.responseModelUser,
      registrationResponse: clearRegistrationResponse
          ? null
          : registrationResponse ?? this.registrationResponse,
      updatePasswordResponse: updatePasswordResponse ?? this.updatePasswordResponse,
      forgotPasswordResponse: clearForgotPasswordResponse
          ? null
          : forgotPasswordResponse ?? this.forgotPasswordResponse,
      tempToken: clearTempToken ? null : tempToken ?? this.tempToken,
      emailForVerification:
          clearTempToken ? null : emailForVerification ?? this.emailForVerification,
      validationErrors:
          clearValidationErrors ? null : validationErrors ?? this.validationErrors,
    );
  }
}

class AuthViewModel extends StateNotifier<AuthState> {
  final AuthRepository _authRepository;

  AuthViewModel(this._authRepository) : super(AuthState()) {
  }

  Future<void> loadSession() async {
    state = state.copyWith(isLoading: true);
    final user = await _authRepository.loadSavedSession();
    state = AuthState(responseModelUser: user, isLoading: false);
  }

  Future<void> register(RegistrationRequest registrationRequest) async {
    state = state.copyWith(isLoading: true, clearError: true, clearValidationErrors: true);
    try {
      final result = await _authRepository.register(registrationRequest);
      if (result.success == true) {
        state = state.copyWith(
          isLoading: false,
          registrationResponse: result,
          tempToken: result.token,
          emailForVerification: registrationRequest.email,
          clearError: true,
          clearValidationErrors: true,
        );
      } else {
        state = state.copyWith(
          isLoading: false,
          error: result.message,
          validationErrors: result.errors,
        );
      }
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  Future<void> login(LoginRequest loginRequest) async {
    state = AuthState(isLoading: true);
    try {
      final result = await _authRepository.login(loginRequest);
      state = state.copyWith(
        isLoading: false,
        responseModelUser: result,
        clearTempToken: true,
        clearRegistrationResponse: true,
      );
    } on EmailNotVerifiedException catch (e) {
      try {
        await _authRepository.resendCode(ResendCodeRequest(
          type: 'email',
          email: e.email,
          token: e.token,
        ));
      } catch (_) {
      }
      
      state = state.copyWith(
        isLoading: false,
        tempToken: e.token,
        emailForVerification: e.email,
      );
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  Future<bool> mailVerify(MailVerifyModel mailVerifyModel) async {
    state = state.copyWith(isLoading: true, clearError: true, clearValidationErrors: true);
    try {
      final modelWithToken = MailVerifyModel(
        code: mailVerifyModel.code,
        token: state.tempToken,
      );

      await _authRepository.mailVerify(modelWithToken);

      final token = state.tempToken;
      final user = state.registrationResponse?.user;

      if (token != null && user != null) {
        await _authRepository.saveSession(token, user);

        await loadSession();

        state = state.copyWith(
          clearTempToken: true,
          clearRegistrationResponse: true,
        );
        return true;
      } else {
        throw Exception('Verification successful, but session could not be created.');
      }
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
      return false;
    }
  }

  Future<void> resendCode(ResendCodeRequest resendCodeRequest) async {
    state = state.copyWith(isLoading: true, clearError: true);
    try {
      final requestWithToken = ResendCodeRequest(
        type: resendCodeRequest.type,
        email: resendCodeRequest.email,
        token: state.tempToken,
      );

      final response = await _authRepository.resendCode(requestWithToken);
      if (response.status != 'success') {
        state = state.copyWith(isLoading: false, error: response.message);
      } else {
        state = state.copyWith(isLoading: false, clearError: true);
      }
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  Future<void> forgotPassword(String email) async {
    state = state.copyWith(isLoading: true, clearError: true, clearSuccessMessage: true, clearForgotPasswordResponse: true);
    try {
      final request = ForgotPasswordRequest(email: email);
      final response = await _authRepository.forgotPassword(request);

      if (response.success) {
        state = state.copyWith(
          isLoading: false,
          forgotPasswordResponse: response,
          successMessage: response.message,
        );
      } else {
        state = state.copyWith(
          isLoading: false,
          error: response.message,
          forgotPasswordResponse: response,
        );
      }
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  Future<void> updatePassword(String currentPassword, String newPassword) async {
    state = state.copyWith(isLoading: true, clearError: true, clearSuccessMessage: true);
    try {
      final request = UpdatePasswordRequest(
          currentPassword: currentPassword, password: newPassword);
      final response = await _authRepository.updatePassword(request);

      if (response.success) {
        state = state.copyWith(
          isLoading: false,
          updatePasswordResponse: response,
          successMessage: response.message,
        );
      } else {
        state = state.copyWith(
          isLoading: false,
          error: response.message,
          updatePasswordResponse: response,
        );
      }
    } catch (e) {
      state = state.copyWith(isLoading: false, error: ErrorHandler.getErrorMessage(e));
    }
  }

  Future<String?> logout() async {
    final message = await _authRepository.logout();
    state = AuthState();
    return message;
  }
}
