import 'package:flutter/material.dart';
import 'package:ticket_app/models/train.dart';
import 'package:ticket_app/screens/seat_booking_screen.dart';
import 'package:ticket_app/services/firebase_service.dart';
import 'package:ticket_app/widgets/app_drawer.dart';

class TrainResultsScreen extends StatelessWidget {
  final List<Train> trains;
  final String from;
  final String to;
  final String date;
  final FirebaseService _firebaseService = FirebaseService();

  TrainResultsScreen({
    super.key,
    required this.trains,
    required this.from,
    required this.to,
    required this.date,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[100],
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0.5,
        leading: Builder(
          builder: (context) => IconButton(
            icon: const Icon(Icons.menu, color: Color(0xFF00695C)),
            onPressed: () => Scaffold.of(context).openDrawer(),
          ),
        ),
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('$from to $to', style: const TextStyle(color: Colors.black87, fontSize: 14, fontWeight: FontWeight.bold)),
            Text(date, style: const TextStyle(color: Colors.grey, fontSize: 11)),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit, color: Colors.teal, size: 20),
            onPressed: () => Navigator.pop(context),
          ),
        ],
      ),
      drawer: const AppDrawer(),
      body: ListView.builder(
        padding: const EdgeInsets.all(12),
        itemCount: trains.length,
        itemBuilder: (context, index) => _buildTrainResultCard(context, trains[index]),
      ),
    );
  }

  Widget _buildTrainResultCard(BuildContext context, Train train) {
    return Card(
      elevation: 0,
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12), side: BorderSide(color: Colors.grey[300]!)),
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(train.name.toUpperCase(), style: const TextStyle(color: Colors.orange, fontWeight: FontWeight.bold, fontSize: 16)),
                const SizedBox(height: 12),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    _buildStationTime('Departure', train.from, train.departureTime),
                    const Icon(Icons.arrow_forward, color: Color(0xFF00695C), size: 20),
                    _buildStationTime('Arrival', train.to, train.arrivalTime),
                  ],
                ),
              ],
            ),
          ),
          const Divider(height: 1),
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.all(12),
            child: Row(
              children: train.bogies.map((b) => _buildClassCard(context, train, b)).toList(),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStationTime(String label, String station, String time) {
    return Column(
      crossAxisAlignment: label == 'Departure' ? CrossAxisAlignment.start : CrossAxisAlignment.end,
      children: [
        Text(label, style: const TextStyle(color: Colors.grey, fontSize: 10)),
        Text(station.toUpperCase(), style: const TextStyle(color: Color(0xFF00695C), fontWeight: FontWeight.bold, fontSize: 13)),
        Text(time, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
      ],
    );
  }

  Widget _buildClassCard(BuildContext context, Train train, Bogie bogie) {
    return StreamBuilder<List<String>>(
      stream: _firebaseService.getBookedSeats(train.id, date, bogie.name),
      builder: (context, snapshot) {
        int bookedCount = snapshot.data?.length ?? 0;
        int availableCount = bogie.totalSeats - bookedCount;

        return GestureDetector(
          onTap: () => Navigator.push(context, MaterialPageRoute(builder: (c) => SeatBookingScreen(train: train, travelDate: date))),
          child: Container(
            width: 110,
            margin: const EdgeInsets.only(right: 8),
            padding: const EdgeInsets.all(10),
            decoration: BoxDecoration(
              color: availableCount > 0 ? const Color(0xFFE0F2F1) : Colors.grey[200],
              borderRadius: BorderRadius.circular(8),
              border: Border.all(color: Colors.teal.withOpacity(0.2)),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(bogie.type.toString().split('.').last.toUpperCase(), style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
                const SizedBox(height: 4),
                Text('৳${bogie.price.toInt()}', style: const TextStyle(color: Color(0xFF00695C), fontWeight: FontWeight.bold, fontSize: 15)),
                const SizedBox(height: 4),
                Text(
                  'Available: $availableCount',
                  style: TextStyle(fontSize: 10, color: availableCount > 0 ? Colors.green[700] : Colors.red, fontWeight: FontWeight.bold),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
