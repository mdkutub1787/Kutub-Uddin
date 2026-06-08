enum SeatType {
  acSleeper,
  acChair,
  nonAc,
  cabin,
}

class Bogie {
  final String name;
  final SeatType type;
  final double price;
  final int totalSeats;

  Bogie({
    required this.name,
    required this.type,
    required this.price,
    required this.totalSeats,
  });

  factory Bogie.fromMap(Map<String, dynamic> data) {
    return Bogie(
      name: data['name'] ?? '',
      type: SeatType.values.firstWhere(
        (e) => e.toString().split('.').last == data['type'],
        orElse: () => SeatType.nonAc,
      ),
      price: (data['price'] ?? 0).toDouble(),
      totalSeats: data['totalSeats'] ?? 0,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'name': name,
      'type': type.toString().split('.').last,
      'price': price,
      'totalSeats': totalSeats,
    };
  }
}

class Train {
  final String id;
  final String name;
  final String from;
  final String to;
  final String route;
  final String departureTime;
  final String arrivalTime;
  final String date; 
  final bool isDaily;
  final List<Bogie> bogies;

  Train({
    required this.id,
    required this.name,
    required this.from,
    required this.to,
    required this.route,
    required this.departureTime,
    required this.arrivalTime,
    required this.date,
    this.isDaily = true,
    required this.bogies,
  });

  factory Train.fromFirestore(Map<String, dynamic> data, String id) {
    return Train(
      id: id,
      name: data['name'] ?? '',
      from: data['from'] ?? '',
      to: data['to'] ?? '',
      route: data['route'] ?? '',
      departureTime: data['departureTime'] ?? '',
      arrivalTime: data['arrivalTime'] ?? '',
      date: data['date'] ?? '',
      isDaily: data['isDaily'] ?? true,
      bogies: (data['bogies'] as List? ?? [])
          .map((b) => Bogie.fromMap(Map<String, dynamic>.from(b)))
          .toList(),
    );
  }

  Map<String, dynamic> toFirestore() {
    return {
      'name': name,
      'from': from,
      'to': to,
      'route': route,
      'departureTime': departureTime,
      'arrivalTime': arrivalTime,
      'date': date,
      'isDaily': isDaily,
      'bogies': bogies.map((b) => b.toMap()).toList(),
    };
  }
}
