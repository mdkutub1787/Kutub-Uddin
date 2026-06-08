class Booking {
  final String id;
  final String trainId;
  final String trainName;
  final String route;
  final String departureTime;
  final String arrivalTime;
  final String date; 
  final String bogieName;
  final String seatType;
  final List<String> seatNumbers;
  final double totalPrice;
  final String userEmail;
  final DateTime bookingTime;

  Booking({
    required this.id,
    required this.trainId,
    required this.trainName,
    required this.route,
    required this.departureTime,
    required this.arrivalTime,
    required this.date,
    required this.bogieName,
    required this.seatType,
    required this.seatNumbers,
    required this.totalPrice,
    required this.userEmail,
    required this.bookingTime,
  });

  factory Booking.fromFirestore(Map<String, dynamic> data, String id) {
    return Booking(
      id: id,
      trainId: data['trainId'] ?? '',
      trainName: data['trainName'] ?? '',
      route: data['route'] ?? '',
      departureTime: data['departureTime'] ?? '',
      arrivalTime: data['arrivalTime'] ?? '',
      date: data['date'] ?? '',
      bogieName: data['bogieName'] ?? '',
      seatType: data['seatType'] ?? '',
      seatNumbers: List<String>.from(data['seatNumbers'] ?? []),
      totalPrice: (data['totalPrice'] ?? 0).toDouble(),
      userEmail: data['userEmail'] ?? '',
      bookingTime: DateTime.fromMillisecondsSinceEpoch(data['bookingTime'] ?? 0),
    );
  }

  Map<String, dynamic> toFirestore() {
    return {
      'trainId': trainId,
      'trainName': trainName,
      'route': route,
      'departureTime': departureTime,
      'arrivalTime': arrivalTime,
      'date': date,
      'bogieName': bogieName,
      'seatType': seatType,
      'seatNumbers': seatNumbers,
      'totalPrice': totalPrice,
      'userEmail': userEmail,
      'bookingTime': bookingTime.millisecondsSinceEpoch,
    };
  }
}
