import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import '../models/user_model.dart';

class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref();

  User? get currentUser => _auth.currentUser;
  Stream<User?> get userStream => _auth.authStateChanges();

  Future<UserModel?> getUserData(String uid) async {
    try {
      DataSnapshot snapshot = await _dbRef.child('users').child(uid).get();
      if (snapshot.exists) {
        return UserModel.fromMap(
          Map<String, dynamic>.from(snapshot.value as Map),
          uid,
        );
      }
    } catch (e) {
      print("Error getting user data: $e");
    }
    return null;
  }

  Future<UserCredential> signIn(String email, String password) async {
    return await _auth.signInWithEmailAndPassword(email: email, password: password);
  }

  Future<UserCredential> register(String email, String password, {String displayName = "", String phoneNumber = "", String role = 'user'}) async {
    UserCredential credential = await _auth.createUserWithEmailAndPassword(email: email, password: password);
    
    if (credential.user != null) {
      await _dbRef.child('users').child(credential.user!.uid).set({
        'email': email,
        'displayName': displayName,
        'phoneNumber': phoneNumber,
        'role': 'user',
        'createdAt': ServerValue.timestamp,
      });
    }
    return credential;
  }

  Future<bool> verifyAdminCredentials(String uid, String adminCode) async {
    try {
      DataSnapshot adminDoc = await _dbRef.child('settings').child('admin_codes').get();

      if (adminDoc.exists) {
        final data = Map<dynamic, dynamic>.from(adminDoc.value as Map);
        List<dynamic> validCodes = data['codes'] ?? [];

        if (validCodes.contains(adminCode)) {
          await _dbRef.child('users').child(uid).update({
            'role': 'admin',
            'adminVerifiedAt': ServerValue.timestamp,
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

  Future<void> signOut() async {
    await _auth.signOut();
  }
}
