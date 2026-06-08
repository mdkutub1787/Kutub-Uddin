import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:ticket_app/models/train.dart';
import 'package:ticket_app/models/booking.dart';
import 'package:ticket_app/models/app_user.dart';

class FirebaseService {
  final FirebaseDatabase _db = FirebaseDatabase.instanceFor(
    app: Firebase.app(),
    databaseURL: 'https://ticketapp-76594-default-rtdb.firebaseio.com/',
  );
  final FirebaseAuth _auth = FirebaseAuth.instance;

  Stream<User?> get authStateChanges => _auth.authStateChanges();

  Future<AppUser?> getCurrentUserData() async {
    final user = _auth.currentUser;
    if (user == null) return null;
    final snapshot = await _db.ref('users/${user.uid}').get();
    if (!snapshot.exists) return null;
    return AppUser.fromFirestore(Map<String, dynamic>.from(snapshot.value as Map), user.uid);
  }

  Future<String?> signUp({
    required String email,
    required String password,
    required String name,
    required String phone,
    required String nid,
    UserRole role = UserRole.user,
  }) async {
    try {
      final credential = await _auth.createUserWithEmailAndPassword(email: email, password: password);
      final user = AppUser(uid: credential.user!.uid, email: email, name: name, phone: phone, nid: nid, role: role);
      await _db.ref('users/${user.uid}').set(user.toFirestore());
      return null;
    } on FirebaseAuthException catch (e) {
      return e.message;
    }
  }

  Future<String?> signIn({required String email, required String password}) async {
    try {
      await _auth.signInWithEmailAndPassword(email: email, password: password);
      return null;
    } on FirebaseAuthException catch (e) {
      return e.message;
    }
  }

  Future<void> signOut() => _auth.signOut();

  // --- Flexible Search Logic ---
  Stream<List<Train>> searchTrains(String from, String to, String date) {
    return _db.ref('trains').onValue.map((event) {
      final data = event.snapshot.value as Map?;
      if (data == null) return [];
      
      return data.entries.map((e) {
        return Train.fromFirestore(Map<String, dynamic>.from(e.value as Map), e.key);
      }).where((train) {
        // Clean input strings
        String searchFrom = from.trim().toLowerCase();
        String searchTo = to.trim().toLowerCase();
        
        bool matchFrom = train.from.toLowerCase().contains(searchFrom);
        bool matchTo = train.to.toLowerCase().contains(searchTo);
        bool matchDate = train.isDaily || train.date == date;
        
        return matchFrom && matchTo && matchDate;
      }).toList();
    });
  }

  Stream<List<Train>> getTrains() {
    return _db.ref('trains').onValue.map((event) {
      final data = event.snapshot.value as Map?;
      if (data == null) return [];
      return data.entries.map((e) {
        return Train.fromFirestore(Map<String, dynamic>.from(e.value as Map), e.key);
      }).toList();
    });
  }

  Stream<List<String>> getBookedSeats(String trainId, String date, String bogieName) {
    return _db.ref('bookings')
        .orderByChild('trainId')
        .equalTo(trainId)
        .onValue.map((event) {
      final data = event.snapshot.value as Map?;
      if (data == null) return [];
      
      List<String> bookedSeats = [];
      data.forEach((key, value) {
        final booking = Map<String, dynamic>.from(value as Map);
        if (booking['date'] == date && booking['bogieName'] == bogieName) {
          bookedSeats.addAll(List<String>.from(booking['seatNumbers'] ?? []));
        }
      });
      return bookedSeats;
    });
  }

  Future<bool> bookSeats({
    required Train train, 
    required Bogie bogie, 
    required List<String> selectedSeats,
    required String travelDate,
  }) async {
    final user = _auth.currentUser;
    if (user == null) return false;
    final userData = await getCurrentUserData();
    if (userData == null) return false;

    final bookingRef = _db.ref('bookings').push();
    
    // Check if seats are already booked
    final bookedSnapshot = await _db.ref('bookings').orderByChild('trainId').equalTo(train.id).get();
    if (bookedSnapshot.exists) {
      final bookingsMap = bookedSnapshot.value as Map;
      for (var b in bookingsMap.values) {
        final bookingData = Map<String, dynamic>.from(b as Map);
        if (bookingData['date'] == travelDate && bookingData['bogieName'] == bogie.name) {
          List existingSeats = bookingData['seatNumbers'] as List;
          for (var s in selectedSeats) {
            if (existingSeats.contains(s)) return false;
          }
        }
      }
    }

    final booking = Booking(
      id: bookingRef.key!,
      trainId: train.id,
      trainName: train.name,
      route: train.route,
      departureTime: train.departureTime,
      arrivalTime: train.arrivalTime,
      date: travelDate,
      bogieName: bogie.name,
      seatType: bogie.type.toString().split('.').last,
      seatNumbers: selectedSeats,
      totalPrice: bogie.price * selectedSeats.length,
      userEmail: userData.email,
      bookingTime: DateTime.now(),
    );

    await bookingRef.set(booking.toFirestore());
    return true;
  }

  Stream<List<Booking>> getUserBookings() {
    final user = _auth.currentUser;
    if (user == null) return Stream.value([]);
    return _db.ref('bookings').orderByChild('userEmail').equalTo(user.email).onValue.map((event) {
      final data = event.snapshot.value as Map?;
      if (data == null) return [];
      var list = data.entries.map((e) => Booking.fromFirestore(Map<String, dynamic>.from(e.value as Map), e.key)).toList();
      list.sort((a, b) => b.bookingTime.compareTo(a.bookingTime));
      return list;
    });
  }

  Future<void> addTrain(Train train) async {
    await _db.ref('trains').push().set(train.toFirestore());
  }

  Future<void> updateTrain(Train train) async {
    await _db.ref('trains/${train.id}').update(train.toFirestore());
  }

  Future<void> deleteTrain(String trainId) async {
    await _db.ref('trains/$trainId').remove();
  }

  // --- Profile Update ---
  Future<String?> updateUserProfile({
    required String name,
    required String phone,
    required String nid,
  }) async {
    final user = _auth.currentUser;
    if (user == null) return "User not logged in";

    try {
      await _db.ref('users/${user.uid}').update({
        'name': name,
        'phone': phone,
        'nid': nid,
      });
      return null; // Success
    } catch (e) {
      return e.toString();
    }
  }
}
