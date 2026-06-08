import 'package:flutter/material.dart';
import 'package:ticket_app/models/train.dart';
import 'package:ticket_app/services/firebase_service.dart';
import 'package:ticket_app/screens/user/payment_screen.dart';

class PassengerDetailsScreen extends StatefulWidget {
  final Train train;
  final Bogie bogie;
  final List<String> selectedSeats;
  final String travelDate;

  const PassengerDetailsScreen({
    super.key,
    required this.train,
    required this.bogie,
    required this.selectedSeats,
    required this.travelDate,
  });

  @override
  State<PassengerDetailsScreen> createState() => _PassengerDetailsScreenState();
}

class _PassengerDetailsScreenState extends State<PassengerDetailsScreen> {
  final _emailController = TextEditingController();
  final _phoneController = TextEditingController();
  final _nameController = TextEditingController();
  final FirebaseService _firebaseService = FirebaseService();

  @override
  void initState() {
    super.initState();
    _loadUserData();
  }

  void _loadUserData() async {
    final user = await _firebaseService.getCurrentUserData();
    if (user != null) {
      setState(() {
        _nameController.text = user.name.toUpperCase();
        _emailController.text = user.email;
        _phoneController.text = user.phone;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0.5,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.teal),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('Passenger Details', style: TextStyle(color: Colors.black87, fontWeight: FontWeight.bold, fontSize: 18)),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            _buildNote('Co-passengers\' names are mandatory to complete the ticket purchase process.'),
            const SizedBox(height: 20),
            _buildTimer(),
            const SizedBox(height: 24),
            _buildPassengerForm(),
            const SizedBox(height: 24),
            _buildContactInfo(),
            const SizedBox(height: 40),
            SizedBox(
              width: double.infinity,
              height: 55,
              child: ElevatedButton(
                onPressed: _goToPayment,
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF00695C),
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(25)),
                ),
                child: const Text('PROCEED TO PAYMENT', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildNote(String text) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(color: Colors.orange[50], borderRadius: BorderRadius.circular(8), border: Border.all(color: Colors.orange[100]!)),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Icon(Icons.info, color: Colors.orange, size: 20),
          const SizedBox(width: 8),
          Expanded(child: Text(text, style: TextStyle(color: Colors.orange[900], fontSize: 11))),
        ],
      ),
    );
  }

  Widget _buildTimer() {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(12), border: Border.all(color: Colors.orange[200]!)),
      child: const Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.access_time, color: Colors.orange),
          SizedBox(width: 8),
          Text('04:15', style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold, color: Colors.orange)),
          SizedBox(width: 12),
          Text('Remaining to initiate\nyour payment', style: TextStyle(color: Colors.grey, fontSize: 12)),
        ],
      ),
    );
  }

  Widget _buildPassengerForm() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(12), border: Border.all(color: Colors.grey[200]!)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text('Seats: ${widget.selectedSeats.join(", ")}', style: TextStyle(color: Colors.teal[800], fontWeight: FontWeight.bold)),
          const SizedBox(height: 10),
          Text('Passenger: ${_nameController.text}', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
          const SizedBox(height: 10),
          const Text('Category: Adult', style: TextStyle(color: Colors.grey)),
        ],
      ),
    );
  }

  Widget _buildContactInfo() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(12), border: Border.all(color: Colors.grey[200]!)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('Contact Information', style: TextStyle(fontWeight: FontWeight.bold)),
          const SizedBox(height: 20),
          _buildTextField(_emailController, 'Email Address'),
          const SizedBox(height: 12),
          _buildTextField(_phoneController, 'Phone Number'),
        ],
      ),
    );
  }

  Widget _buildTextField(TextEditingController controller, String hint) {
    return TextField(
      controller: controller,
      decoration: InputDecoration(
        hintText: hint,
        filled: true,
        fillColor: Colors.blueGrey[50],
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(8), borderSide: BorderSide.none),
      ),
    );
  }

  void _goToPayment() {
    if (_emailController.text.isEmpty || _phoneController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Please provide contact details')));
      return;
    }
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => PaymentScreen(
          train: widget.train,
          bogie: widget.bogie,
          selectedSeats: widget.selectedSeats,
          travelDate: widget.travelDate,
          passengerInfo: {
            'name': _nameController.text,
            'email': _emailController.text,
            'phone': _phoneController.text,
          },
        ),
      ),
    );
  }
}
