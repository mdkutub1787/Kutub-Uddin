import 'package:flutter/foundation.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class AuthRepository {
  final SupabaseClient _supabase;

  AuthRepository(this._supabase);

  // Get current user stream
  Stream<AuthState> get authStateChanges => _supabase.auth.onAuthStateChange;

  // Get current user
  User? get currentUser => _supabase.auth.currentUser;

  // Sign In
  Future<AuthResponse> signIn(String email, String password) async {
    try {
      final response = await _supabase.auth.signInWithPassword(
        email: email,
        password: password,
      );
      debugPrint('✅ SUCCESS: Login successful for $email');
      return response;
    } catch (e) {
      debugPrint('❌ ERROR: Login failed for $email -> $e');
      rethrow;
    }
  }

  // Sign Up
  Future<AuthResponse> signUp(String email, String password, {required Map<String, dynamic> metadata}) async {
    try {
      final response = await _supabase.auth.signUp(
        email: email,
        password: password,
        data: metadata,
      );
      debugPrint('✅ SUCCESS: Registration successful for $email');
      return response;
    } catch (e) {
      debugPrint('❌ ERROR: Registration failed for $email -> $e');
      rethrow;
    }
  }

  // Sign Out
  Future<void> signOut() async {
    try {
      await _supabase.auth.signOut();
      debugPrint('✅ SUCCESS: User signed out');
    } catch (e) {
      debugPrint('❌ ERROR: Sign out failed -> $e');
    }
  }
}

// Provider for AuthRepository
final authRepositoryProvider = Provider<AuthRepository>((ref) {
  return AuthRepository(Supabase.instance.client);
});
