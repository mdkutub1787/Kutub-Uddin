import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:fflipy/core/errors/exceptions.dart';
import 'package:fflipy/models/auth/login_model.dart';
import 'package:fflipy/models/auth/mail_verify_model.dart';
import 'package:fflipy/models/auth/registration_model.dart';
import 'package:fflipy/models/auth/update_password_model.dart';
import 'package:fflipy/models/auth/forgot_password_model.dart';
import 'package:flutter/foundation.dart';
import 'package:fflipy/core/constants/api_config.dart';
import '../models/auth/resend_code_model.dart';
import '../models/profile/user_model.dart';
import '../security_helper/secure_storage_service.dart';

class AuthService {
  final Dio _dio;
  final SecureStorageService _secureStorage = SecureStorageService();

  AuthService(this._dio);

  Future<RegistrationResponse> register(
      RegistrationRequest registrationRequest) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.registerMobileUrl}');
      print('Payload: ${registrationRequest.toJson()}');
    }
    try {
      final formData = FormData.fromMap(registrationRequest.toJson());

      final response = await _dio.post(
        ApiConfig.registerMobileUrl,
        data: formData,
      );
      if (kDebugMode) {
        print('Success: ${response.data}');
      }
      return RegistrationResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      final data = e.response?.data;
      if (data != null && data is Map<String, dynamic>) {
        return RegistrationResponse.fromJson(data);
      } else {
        return RegistrationResponse(success: false, message: e.message ?? 'An unknown error occurred');
      }
    }
  }

  Future<LoginResponse> login(LoginRequest loginRequest) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.loginUrl}');
      print('Payload: ${loginRequest.toJson()}');
    }
    try {
      final formData = FormData.fromMap(loginRequest.toJson());

      final response = await _dio.post(
        ApiConfig.loginUrl,
        data: formData,
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }

      final loginResponse = LoginResponse.fromJson(response.data);

      if (loginResponse.status == false) {
        if (loginResponse.user?.emailVerification == '0') {
          throw EmailNotVerifiedException(
            token: loginResponse.token ?? '',
            email: loginResponse.user?.email ?? loginRequest.username ?? '',
          );
        }
        throw Exception(loginResponse.message ?? 'Login failed');
      }

      if (loginResponse.token != null && loginResponse.user != null) {
        if (loginResponse.user?.emailVerification == '0') {
             throw EmailNotVerifiedException(
                token: loginResponse.token!,
                email: loginResponse.user!.email!,
              );
        }
        await saveSession(loginResponse.token!, loginResponse.user!);
      }

      return loginResponse;
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      final data = e.response?.data;
      if (data != null && data is Map<String, dynamic>) {
        final loginResponse = LoginResponse.fromJson(data);
        if (loginResponse.user?.emailVerification == '0') {
             throw EmailNotVerifiedException(
                token: loginResponse.token ?? '',
                email: loginResponse.user?.email ?? loginRequest.username ?? '',
              );
        }
        throw Exception(loginResponse.message ?? 'Login failed');
      }
      throw Exception('Login failed');
    }
  }

  Future<void> mailVerify(MailVerifyModel mailVerifyModel) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.mailVerifyUrl}');
      print('Payload: ${mailVerifyModel.toJson()}');
    }
    try {
      final formData = FormData.fromMap(mailVerifyModel.toJson());
      final authToken = mailVerifyModel.token;
       if (authToken == null) {
        throw Exception('Auth token not found for mail verification');
      }

      final response = await _dio.post(
        ApiConfig.mailVerifyUrl,
        data: formData,
        options: Options(headers: {'Authorization': 'Bearer $authToken'}),
      );
      
      if (response.data is Map<String, dynamic>) {
        final responseData = response.data as Map<String, dynamic>;
        if (responseData['status'] != 'success') {
          throw Exception(responseData['message'] ?? 'OTP verification failed');
        }
      } else {
        if (kDebugMode) print('Unexpected response format: ${response.data}');
      }
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      final data = e.response?.data;
      if (data != null && data is Map<String, dynamic>) {
         throw Exception(data['message'] ?? 'OTP verification failed');
      }
      throw Exception('OTP verification failed');
    }
  }

  Future<ResendCodeResponse> resendCode(ResendCodeRequest resendCodeRequest) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.resendCodeUrl}');
      print('Payload: ${resendCodeRequest.toJson()}');
    }
    try {
      final formData = FormData.fromMap(resendCodeRequest.toJson());
      final authToken = resendCodeRequest.token ?? await _secureStorage.getToken();
       if (authToken == null) {
        throw Exception('Auth token not found to resend code');
      }

      final response = await _dio.post(
        ApiConfig.resendCodeUrl,
        data: formData,
        options: Options(headers: {'Authorization': 'Bearer $authToken'}),
      );
      return ResendCodeResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      final data = e.response?.data;
      if (data != null && data is Map<String, dynamic>) {
         throw Exception(data['message'] ?? 'Failed to resend code');
      }
      throw Exception('Failed to resend code');
    }
  }

  Future<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.forgotPasswordUrl}');
      print('Payload: ${request.toJson()}');
    }
    try {
      final formData = FormData.fromMap(request.toJson());

      final response = await _dio.post(
        ApiConfig.forgotPasswordUrl,
        data: formData,
      );

      if (kDebugMode) {
        print('Success: ${response.data}');
      }
      return ForgotPasswordResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      final data = e.response?.data;
      if (data != null && data is Map<String, dynamic>) {
        return ForgotPasswordResponse.fromJson(data);
      } else {
         throw Exception('Failed to send reset link');
      }
    }
  }

  Future<UpdatePasswordResponse> updatePassword(
      UpdatePasswordRequest updatePasswordRequest) async {
    if (kDebugMode) {
      print('Requesting: ${ApiConfig.baseUrl}${ApiConfig.updatePasswordUrl}');
      print('Payload: ${updatePasswordRequest.toJson()}');
    }
    try {
      final token = await _secureStorage.getToken();
      if (token == null) {
        throw Exception('Authentication token not found');
      }

      final formData = FormData.fromMap(updatePasswordRequest.toJson());

      final response = await _dio.post(
        ApiConfig.updatePasswordUrl,
        data: formData,
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );
      if (kDebugMode) {
        print('Success: ${response.data}');
      }
      return UpdatePasswordResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (kDebugMode) {
        print('Error: ${e.response?.data}');
      }
      final data = e.response?.data;
      if (data != null && data is Map<String, dynamic>) {
        return UpdatePasswordResponse.fromJson(data);
      } else {
         throw Exception('Failed to update password');
      }
    }
  }

  Future<String?> logout() async {
    try {
      final token = await _secureStorage.getToken();
      if (token != null) {
        final response = await _dio.post(
          ApiConfig.logoutUrl,
          options: Options(headers: {'Authorization': 'Bearer $token'}),
        );
        if (kDebugMode) print('Logout API call successful');
        return response.data['message'] as String?;
      }
      return null;
    } catch (e) {
      if (kDebugMode) print('Logout API failed: $e');
      return null;
    } finally {
      await clearSession();
    }
  }

  Future<void> saveSession(String token, UserModel user) async {
    try {
      await _secureStorage.saveToken(token);
      await _secureStorage.saveUserData(json.encode(user.toJson()));
      if (kDebugMode) print('Saved session token and user data to secure storage');
    } catch (e) {
      if (kDebugMode) print('Failed to save session: $e');
    }
  }

  Future<LoginResponse?> loadSavedSession() async {
    try {
      final token = await _secureStorage.getToken();
      final userJson = await _secureStorage.getUserData();
      if (token == null || userJson == null) return null;

      final user = UserModel.fromJson(json.decode(userJson));
      return LoginResponse(status: true, token: token, user: user);
    } catch (e) {
      if (kDebugMode) {
        print('Failed to load saved session: $e');
      }
      return null;
    }
  }

  Future<void> clearSession() async {
    try {
      await _secureStorage.clearSession();
      if (kDebugMode) print('Cleared saved session from secure storage');
    } catch (e) {
      if (kDebugMode) print('Failed to clear session: $e');
    }
  }
}
