class SessionManager {
  static final SessionManager _instance = SessionManager._internal();

  SessionManager._internal();

  factory SessionManager() {
    return _instance;
  }

  String? _sessionToken;

  void setSessionToken(String token) {
    _sessionToken = token;
  }

  String? getSessionToken() {
    return _sessionToken;
  }

  void clearSession() {
    _sessionToken = null;
  }
}