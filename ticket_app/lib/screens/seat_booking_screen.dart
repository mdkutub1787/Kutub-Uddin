import 'package:flutter/material.dart';
import 'package:ticket_app/models/train.dart';
import 'package:ticket_app/services/firebase_service.dart';
import 'package:ticket_app/screens/user/passenger_details_screen.dart';

class SeatBookingScreen extends StatefulWidget {
  final Train train;
  final String travelDate; // Need to pass the date from search

  const SeatBookingScreen({super.key, required this.train, required this.travelDate});

  @override
  _SeatBookingScreenState createState() => _SeatBookingScreenState();
}

class _SeatBookingScreenState extends State<SeatBookingScreen> {
  final Set<String> _selectedSeats = {};
  late Bogie _selectedBogie;
  final FirebaseService _firebaseService = FirebaseService();

  @override
  void initState() {
    super.initState();
    _selectedBogie = widget.train.bogies.first;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0.5,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.teal),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text(
          widget.train.name.toUpperCase(),
          style: const TextStyle(color: Colors.black87, fontSize: 16, fontWeight: FontWeight.bold),
        ),
      ),
      body: StreamBuilder<List<String>>(
        stream: _firebaseService.getBookedSeats(widget.train.id, widget.travelDate, _selectedBogie.name),
        builder: (context, snapshot) {
          final bookedSeats = snapshot.data ?? [];

          return SingleChildScrollView(
            child: Column(
              children: [
                _buildTrainHeader(),
                _buildBogieSelector(),
                _buildLegend(),
                _buildSeatLayout(bookedSeats),
                const SizedBox(height: 100),
              ],
            ),
          );
        },
      ),
      bottomNavigationBar: _selectedSeats.isEmpty
          ? null
          : Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(color: Colors.white, boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 10)]),
              child: ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => PassengerDetailsScreen(
                        train: widget.train,
                        bogie: _selectedBogie,
                        selectedSeats: _selectedSeats.toList(),
                        travelDate: widget.travelDate,
                      ),
                    ),
                  );
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.teal[800],
                  foregroundColor: Colors.white,
                  minimumSize: const Size(double.infinity, 50),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(25)),
                ),
                child: Text('PROCEED WITH ${_selectedSeats.length} SEATS', style: const TextStyle(fontWeight: FontWeight.bold)),
              ),
            ),
    );
  }

  Widget _buildTrainHeader() {
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.teal[50]!),
      ),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(widget.train.from, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18, color: Colors.teal)),
              const Icon(Icons.arrow_forward, color: Colors.teal),
              Text(widget.train.to, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18, color: Colors.teal)),
            ],
          ),
          const SizedBox(height: 8),
          Text('Journey Date: ${widget.travelDate}', style: const TextStyle(color: Colors.grey)),
        ],
      ),
    );
  }

  Widget _buildBogieSelector() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Padding(
          padding: EdgeInsets.symmetric(horizontal: 16),
          child: Text('Select Coach', style: TextStyle(fontWeight: FontWeight.bold)),
        ),
        const SizedBox(height: 8),
        SizedBox(
          height: 50,
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 16),
            itemCount: widget.train.bogies.length,
            itemBuilder: (context, index) {
              final bogie = widget.train.bogies[index];
              bool isSelected = _selectedBogie.name == bogie.name;
              return GestureDetector(
                onTap: () => setState(() {
                  _selectedBogie = bogie;
                  _selectedSeats.clear();
                }),
                child: Container(
                  margin: const EdgeInsets.only(right: 10),
                  padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                  decoration: BoxDecoration(
                    color: isSelected ? Colors.teal[800] : Colors.white,
                    borderRadius: BorderRadius.circular(10),
                    border: Border.all(color: Colors.teal[800]!),
                  ),
                  child: Text(
                    bogie.name,
                    style: TextStyle(color: isSelected ? Colors.white : Colors.teal[800], fontWeight: FontWeight.bold),
                  ),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  Widget _buildLegend() {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          _legendItem('Available', Colors.white, Colors.grey),
          _legendItem('Selected', Colors.teal[800]!, Colors.teal[800]!),
          _legendItem('Booked', Colors.orange, Colors.orange),
        ],
      ),
    );
  }

  Widget _legendItem(String label, Color color, Color border) {
    return Row(
      children: [
        Container(width: 12, height: 12, decoration: BoxDecoration(color: color, border: Border.all(color: border), borderRadius: BorderRadius.circular(2))),
        const SizedBox(width: 4),
        Text(label, style: const TextStyle(fontSize: 10)),
      ],
    );
  }

  Widget _buildSeatLayout(List<String> bookedSeats) {
    return GridView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      padding: const EdgeInsets.all(24),
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 5,
        mainAxisSpacing: 15,
        crossAxisSpacing: 15,
      ),
      itemCount: _selectedBogie.totalSeats,
      itemBuilder: (context, index) {
        if (index % 5 == 2) return const SizedBox(); // Aisle
        
        final seatNum = '${_selectedBogie.name}-${index + 1}';
        final isBooked = bookedSeats.contains(seatNum);
        final isSelected = _selectedSeats.contains(seatNum);

        return GestureDetector(
          onTap: isBooked ? null : () {
            setState(() {
              if (isSelected) {
                _selectedSeats.remove(seatNum);
              } else if (_selectedSeats.length < 4) {
                _selectedSeats.add(seatNum);
              } else {
                ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Max 4 seats allowed')));
              }
            });
          },
          child: Container(
            decoration: BoxDecoration(
              color: isBooked ? Colors.orange : (isSelected ? Colors.teal[800] : Colors.white),
              border: Border.all(color: isBooked ? Colors.orange : (isSelected ? Colors.teal[800]! : Colors.grey)),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Center(
              child: Text(
                seatNum,
                style: TextStyle(
                  color: (isSelected || isBooked) ? Colors.white : Colors.black87,
                  fontSize: 10,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}
