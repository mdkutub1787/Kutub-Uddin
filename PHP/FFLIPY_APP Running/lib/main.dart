import 'dart:async';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/providers/auth_providers.dart';
import 'package:fflipy/providers/localization_provider.dart';
import 'package:fflipy/providers/theme_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/theme/app_theme.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const ProviderScope(child: MyApp()));
}

class MyApp extends ConsumerStatefulWidget {
  const MyApp({super.key});

  @override
  ConsumerState<MyApp> createState() => _MyAppState();
}

class _MyAppState extends ConsumerState<MyApp> with WidgetsBindingObserver {
  DateTime? _lastPausedTime;
  Timer? _heartbeatTimer;
  Timer? _inactivityTimer;
  static const int _backgroundTimeoutMinutes = 3;
  static const String _lastActiveTimeKey = 'last_active_time';

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
  }

  void _startHeartbeat() {
    _heartbeatTimer?.cancel();
    _heartbeatTimer = Timer.periodic(const Duration(minutes: 1), (timer) {
      _updateLastActiveTime();
    });
  }

  void _stopHeartbeat() {
    _heartbeatTimer?.cancel();
  }

  Future<void> _updateLastActiveTime() async {
    if (!ref.read(isLoggedInProvider)) return;
    
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt(_lastActiveTimeKey, DateTime.now().millisecondsSinceEpoch);
  }

  void _handleUserInteraction([dynamic _]) {
    _inactivityTimer?.cancel();

    if (ref.read(isLoggedInProvider)) {
      _inactivityTimer = Timer(const Duration(minutes: _backgroundTimeoutMinutes), () {
        ref.read(authViewModelProvider.notifier).logout();
      });
    }
  }

  @override
  void dispose() {
    _heartbeatTimer?.cancel();
    _inactivityTimer?.cancel();
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.paused) {
      _lastPausedTime = DateTime.now();
      _inactivityTimer?.cancel();

      if (ref.read(isLoggedInProvider)) {
         _updateLastActiveTime();
      }
      
    } else if (state == AppLifecycleState.resumed) {
      if (_lastPausedTime != null) {
        final timeDiff = DateTime.now().difference(_lastPausedTime!);

        if (timeDiff.inMinutes >= _backgroundTimeoutMinutes) {
          ref.read(authViewModelProvider.notifier).logout();
        } else {
           if (ref.read(isLoggedInProvider)) {
             _updateLastActiveTime();
             _handleUserInteraction();
           }
        }
        _lastPausedTime = null;
      } else {
        if (ref.read(isLoggedInProvider)) {
             _updateLastActiveTime();
             _handleUserInteraction();
        }
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final router = ref.watch(routerProvider);
    final locale = ref.watch(localeProvider);
    final supportedLocales = ref.watch(supportedLocalesProvider);
    final themeMode = ref.watch(themeModeProvider);
    final authState = ref.watch(authViewModelProvider);

    ref.listen<bool>(sessionExpiredProvider, (previous, next) {
      if (next) {
        ref.read(authViewModelProvider.notifier).logout();
        ref.read(sessionExpiredProvider.notifier).state = false;
      }
    });

    ref.listen<bool>(isLoggedInProvider, (previous, isLoggedIn) {
      if (isLoggedIn) {
        _handleUserInteraction();
        _startHeartbeat();
        _updateLastActiveTime();
      } else {
        _inactivityTimer?.cancel();
        _stopHeartbeat();
      }
    });

    return MaterialApp.router(
      onGenerateTitle: (context) => AppLocalizations.of(context).translate('AppName'),
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme(),
      darkTheme: AppTheme.darkTheme(),
      themeMode: themeMode,

      locale: locale,
      supportedLocales: supportedLocales,
      localizationsDelegates: const [
        AppLocalizationsDelegate(),
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      localeResolutionCallback: (deviceLocale, supportedLocales) {
        for (var supportedLocale in supportedLocales) {
          if (supportedLocale.languageCode == deviceLocale?.languageCode) {
            return supportedLocale;
          }
        }
        return supportedLocales.first;
      },

      routerConfig: router,
      builder: (context, child) {
        return Listener(
          behavior: HitTestBehavior.translucent,
          onPointerDown: _handleUserInteraction,
          onPointerUp: _handleUserInteraction,
          child: Stack(
            children: [
              child!,
              if (authState.isLoading) const Preloader(),
            ],
          ),
        );
      },
    );
  }
}
