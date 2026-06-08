import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../providers/auth_providers.dart';

class SessionService {
  final Ref _ref;
  Timer? _heartbeatTimer;
  Timer? _inactivityTimer;
  DateTime? _lastPausedTime;
  
  static const int _backgroundTimeoutMinutes = 3;
  static const String _lastActiveTimeKey = 'last_active_time';

  SessionService(this._ref);

  Future<void> initializeSession() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final lastActiveTime = prefs.getInt(_lastActiveTimeKey);

      if (lastActiveTime != null) {
        final lastActiveDateTime = DateTime.fromMillisecondsSinceEpoch(lastActiveTime);
        final diff = DateTime.now().difference(lastActiveDateTime);
        
        if (diff.inMinutes >= _backgroundTimeoutMinutes) {
          _ref.read(authViewModelProvider.notifier).logout();
          await prefs.remove(_lastActiveTimeKey);
          return;
        }
      }

      await _ref.read(authViewModelProvider.notifier).loadSession();
      
      await _updateLastActiveTime();
      _startHeartbeat();
      _handleUserInteraction();
      
    } catch (e) {
      if (kDebugMode) print('Session initialization error: $e');
       await _ref.read(authViewModelProvider.notifier).loadSession();
       _startHeartbeat();
    }
  }

  void _startHeartbeat() {
    _heartbeatTimer?.cancel();
    _heartbeatTimer = Timer.periodic(const Duration(minutes: 1), (timer) {
      _updateLastActiveTime();
    });
  }

  Future<void> _updateLastActiveTime() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setInt(_lastActiveTimeKey, DateTime.now().millisecondsSinceEpoch);
    } catch (e) {
      if (kDebugMode) print('Failed to update heartbeat: $e');
    }
  }

  void handleUserInteraction() {
    _handleUserInteraction();
  }

  void _handleUserInteraction() {
    _inactivityTimer?.cancel();

    final isLoggedIn = _ref.read(isLoggedInProvider);
    if (isLoggedIn) {
      _inactivityTimer = Timer(const Duration(minutes: _backgroundTimeoutMinutes), () {
        _ref.read(authViewModelProvider.notifier).logout();
      });
    }
  }

  void onAppPaused() {
    _lastPausedTime = DateTime.now();
    _inactivityTimer?.cancel();
    _updateLastActiveTime();
  }

  void onAppResumed() {
    if (_lastPausedTime != null) {
      final timeDiff = DateTime.now().difference(_lastPausedTime!);
      if (timeDiff.inMinutes >= _backgroundTimeoutMinutes) {
        _ref.read(authViewModelProvider.notifier).logout();
      } else {
        _updateLastActiveTime();
        _handleUserInteraction();
      }
      _lastPausedTime = null;
    } else {
      _updateLastActiveTime();
      _handleUserInteraction();
    }
  }

  void dispose() {
    _heartbeatTimer?.cancel();
    _inactivityTimer?.cancel();
  }
}

final sessionServiceProvider = Provider<SessionService>((ref) {
  return SessionService(ref);
});
