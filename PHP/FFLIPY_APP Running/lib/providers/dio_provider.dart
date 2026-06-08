import 'package:dio/dio.dart';
import 'package:fflipy/core/constants/app_constants.dart';
import 'package:fflipy/providers/auth_providers.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:fflipy/core/constants/api_config.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

final dioProvider = Provider.autoDispose<Dio>((ref) {
  final dio = Dio(
    BaseOptions(
      baseUrl: ApiConfig.baseUrl,
      connectTimeout: const Duration(seconds: 15),
      receiveTimeout: const Duration(seconds: 15),
      headers: {
        'Accept': 'application/json',
      },
    ),
  );

  dio.interceptors.add(
    QueuedInterceptorsWrapper(
      onRequest: (options, handler) async {
        if (options.headers.containsKey('Authorization')) {
          return handler.next(options);
        }

        const secureStorage = FlutterSecureStorage();
        String? token;

        try {
          token = await secureStorage.read(key: AppConstants.userTokenKey);
        } on PlatformException catch (e) {
          if (e.message?.contains('BadPaddingException') == true ||
              e.message?.contains('BAD_DECRYPT') == true) {
            await secureStorage.deleteAll();
            ref.read(sessionExpiredProvider.notifier).state = true;
            return handler.resolve(Response(requestOptions: options, statusCode: 200, data: {}));
          }
          return handler.reject(DioException(requestOptions: options, error: e));
        }

        if (token != null && token.isNotEmpty) {
          options.headers['Authorization'] = 'Bearer $token';
        }

        return handler.next(options);
      },
      onResponse: (response, handler) {
        return handler.next(response);
      },
      onError: (DioException e, handler) {
        return handler.next(e);
      },
    ),
  );

  if (kDebugMode) {
    dio.interceptors.add(
      LogInterceptor(
        requestBody: true,
        responseBody: true,
        requestHeader: true,
        responseHeader: false,
        error: true,
      ),
    );
  }

  return dio;
});
