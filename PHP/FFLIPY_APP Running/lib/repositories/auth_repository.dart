import 'package:fflipy/models/auth/login_model.dart';
import 'package:fflipy/models/auth/mail_verify_model.dart';
import 'package:fflipy/models/auth/registration_model.dart';
import 'package:fflipy/models/auth/update_password_model.dart';
import 'package:fflipy/models/auth/forgot_password_model.dart';
import 'package:fflipy/services/auth_service.dart';
import 'package:fflipy/models/profile/user_model.dart';
import '../models/auth/resend_code_model.dart';

class AuthRepository {
  final AuthService _authService;

  AuthRepository(this._authService);

  Future<LoginResponse> login(LoginRequest loginRequest) async {
    try {
      return await _authService.login(loginRequest);
    } catch (e) {
      rethrow;
    }
  }

  Future<RegistrationResponse> register(RegistrationRequest registrationRequest) {
    return _authService.register(registrationRequest);
  }

  Future<void> mailVerify(MailVerifyModel mailVerifyModel) {
    return _authService.mailVerify(mailVerifyModel);
  }

  Future<ResendCodeResponse> resendCode(ResendCodeRequest resendCodeRequest) {
    return _authService.resendCode(resendCodeRequest);
  }

  Future<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request) {
    return _authService.forgotPassword(request);
  }

  Future<UpdatePasswordResponse> updatePassword(
      UpdatePasswordRequest updatePasswordRequest) {
    return _authService.updatePassword(updatePasswordRequest);
  }

  Future<String?> logout() async {
    return await _authService.logout();
  }

  Future<LoginResponse?> loadSavedSession() {
    return _authService.loadSavedSession();
  }

  Future<void> saveSession(String token, UserModel user) {
    return _authService.saveSession(token, user);
  }
}
