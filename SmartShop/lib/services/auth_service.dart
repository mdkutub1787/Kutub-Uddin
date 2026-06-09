import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/user_model.dart';

class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  // Get current user
  User? get currentUser => _auth.currentUser;

  // Auth change user stream
  Stream<User?> get userStream => _auth.authStateChanges();

  // Get user data from Firestore
  Future<UserModel?> getUserData(String uid) async {
    try {
      DocumentSnapshot doc = await _firestore.collection('users').doc(uid).get();
      if (doc.exists) {
        return UserModel.fromMap(doc.data() as Map<String, dynamic>, uid);
      }
    } catch (e) {
      print("Error getting user data: $e");
    }
    return null;
  }

  // Sign in with email and password
  Future<UserCredential> signIn(String email, String password) async {
    return await _auth.signInWithEmailAndPassword(email: email, password: password);
  }

  // Register with email and password
  Future<UserCredential> register(String email, String password, {String displayName = "", String phoneNumber = "", String role = 'user'}) async {
    UserCredential credential = await _auth.createUserWithEmailAndPassword(email: email, password: password);
    
    // Create user document in Firestore - always create as 'user' role
    if (credential.user != null) {
      await _firestore.collection('users').doc(credential.user!.uid).set({
        'email': email,
        'displayName': displayName,
        'phoneNumber': phoneNumber,
        'role': 'user', // Always start as regular user
        'createdAt': FieldValue.serverTimestamp(),
      });
    }
    return credential;
  }

  // Verify admin credentials and update user role to admin
  Future<bool> verifyAdminCredentials(String uid, String adminCode) async {
    try {
      // Get admin codes from Firestore settings
      DocumentSnapshot adminDoc = await _firestore.collection('settings').doc('admin_codes').get();

      if (adminDoc.exists) {
        List<dynamic> validCodes = adminDoc['codes'] ?? [];

        if (validCodes.contains(adminCode)) {
          // Update user role to admin
          await _firestore.collection('users').doc(uid).update({
            'role': 'admin',
            'adminVerifiedAt': FieldValue.serverTimestamp(),
          });
          return true;
        }
      }
      return false;
    } catch (e) {
      print("Error verifying admin credentials: $e");
      return false;
    }
  }

  // Update user role
  Future<void> updateUserRole(String uid, String role) async {
    try {
      await _firestore.collection('users').doc(uid).update({
        'role': role,
      });
    } catch (e) {
      print("Error updating user role: $e");
    }
  }

  // Sign out
  Future<void> signOut() async {
    await _auth.signOut();
  }
}
