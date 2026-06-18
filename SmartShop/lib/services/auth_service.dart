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

  Future<UserCredential> register(String email, String password, {String name = "", String phoneNumber = "", String address = "", String? shopName}) async {
    UserCredential credential = await _auth.createUserWithEmailAndPassword(email: email, password: password);
    
    if (credential.user != null) {
      String? shopId;
      
      // If shopName is provided, create a new shop and make this user an admin
      if (shopName != null && shopName.isNotEmpty) {
        shopId = DateTime.now().millisecondsSinceEpoch.toString();
        await _dbRef.child('shops').child(shopId).set({
          'name': shopName,
          'ownerId': credential.user!.uid,
          'address': address,
          'phone': phoneNumber,
          'isOnlineOrderEnabled': true,
          'isPosEnabled': true,
          'createdAt': ServerValue.timestamp,
        });
      }

      await _dbRef.child('users').child(credential.user!.uid).set({
        'email': email,
        'name': name,
        'phoneNumber': phoneNumber,
        'address': address,
        'role': shopName != null ? 'owner' : 'user',
        'shopId': shopId,
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
        List<dynamic> validCodes = [];
        if (data['codes'] is List) {
          validCodes = data['codes'];
        } else if (data['codes'] is Map) {
          validCodes = (data['codes'] as Map).values.toList();
        }

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

  Future<bool> reauthenticate(String email, String oldPassword) async {
    try {
      User? user = _auth.currentUser;
      if (user != null && user.email != null) {
        AuthCredential credential = EmailAuthProvider.credential(email: email, password: oldPassword);
        await user.reauthenticateWithCredential(credential);
        return true;
      }
      return false;
    } catch (e) {
      print("Re-authentication error: $e");
      return false;
    }
  }

  Future<void> sendPasswordResetEmail(String email) async {
    await _auth.sendPasswordResetEmail(email: email);
  }

  Future<void> updatePassword(String newPassword) async {
    await _auth.currentUser?.updatePassword(newPassword);
  }

  Future<void> signOut() async {
    await _auth.signOut();
  }

  // --- Delivery System Methods ---

  Future<void> updateLocation(String uid, double lat, double lng) async {
    await _dbRef.child('users').child(uid).update({
      'latitude': lat,
      'longitude': lng,
      'lastLocationUpdate': ServerValue.timestamp,
    });
  }

  Future<void> updateAvailability(String uid, bool available) async {
    await _dbRef.child('users').child(uid).update({
      'isAvailable': available,
    });
  }

  Future<List<UserModel>> getAvailableDeliveryMen() async {
    try {
      DataSnapshot snapshot = await _dbRef.child('users').orderByChild('role').equalTo('delivery_man').get();
      if (snapshot.exists) {
        Map<dynamic, dynamic> usersMap = snapshot.value as Map<dynamic, dynamic>;
        return usersMap.entries.map((e) {
          return UserModel.fromMap(Map<String, dynamic>.from(e.value), e.key);
        }).where((user) => user.isAvailable == true).toList();
      }
    } catch (e) {
      print("Error getting delivery men: $e");
    }
    return [];
  }
}
