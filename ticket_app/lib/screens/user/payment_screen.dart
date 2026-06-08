import 'package:flutter/material.dart';
import 'package:ticket_app/models/train.dart';
import 'package:ticket_app/services/firebase_service.dart';

class PaymentScreen extends StatefulWidget {
  final Train train;
  final Bogie bogie;
  final List<String> selectedSeats;
  final String travelDate;
  final Map<String, String> passengerInfo;

  const PaymentScreen({
    super.key,
    required this.train,
    required this.bogie,
    required this.selectedSeats,
    required this.travelDate,
    required this.passengerInfo,
  });

  @override
  State<PaymentScreen> createState() => _PaymentScreenState();
}

class _PaymentScreenState extends State<PaymentScreen> {
  final FirebaseService _firebaseService = FirebaseService();
  String? _selectedMethod;
  bool _isProcessing = false;

  @override
  Widget build(BuildContext context) {
    double totalAmount = widget.bogie.price * widget.selectedSeats.length;

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: const Text('Payment Selection', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.white,
        foregroundColor: Colors.teal[800],
        elevation: 0.5,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildSummaryCard(totalAmount),
            const SizedBox(height: 24),
            const Text('Select Payment Method', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.blueGrey)),
            const SizedBox(height: 16),
            _buildPaymentMethod('bKash', 'assets/bkash.png'),
            _buildPaymentMethod('Nagad', 'assets/nagad.png'),
            _buildPaymentMethod('Rocket', 'assets/rocket.png'),
            _buildPaymentMethod('Debit/Credit Card', 'assets/bank.png'),
            const SizedBox(height: 40),
            SizedBox(
              width: double.infinity,
              height: 55,
              child: ElevatedButton(
                onPressed: (_selectedMethod == null || _isProcessing) ? null : _processPayment,
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF00695C),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: _isProcessing
                    ? Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Image.asset('assets/preloader.gif', height: 25),
                          const SizedBox(width: 10),
                          const Text('PROCESSING...', style: TextStyle(color: Colors.white)),
                        ],
                      )
                    : Text('PAY ৳${totalAmount.toInt()}', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18, color: Colors.white)),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSummaryCard(double total) {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [const Text('Train:'), Text(widget.train.name, style: const TextStyle(fontWeight: FontWeight.bold))]),
            const SizedBox(height: 8),
            Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [const Text('Date:'), Text(widget.travelDate, style: const TextStyle(fontWeight: FontWeight.bold))]),
            const SizedBox(height: 8),
            Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [const Text('Seats:'), Text(widget.selectedSeats.join(', '), style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.teal))]),
            const Divider(height: 30),
            Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
              const Text('Total Amount', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
              Text('৳${total.toInt()}', style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Colors.orange)),
            ]),
          ],
        ),
      ),
    );
  }

  Widget _buildPaymentMethod(String name, String iconPath) {
    bool isSelected = _selectedMethod == name;
    return GestureDetector(
      onTap: () => setState(() => _selectedMethod = name),
      child: Container(
        margin: const EdgeInsets.only(bottom: 12),
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: isSelected ? Colors.teal : Colors.grey[200]!, width: isSelected ? 2 : 1),
          boxShadow: isSelected ? [BoxShadow(color: Colors.teal.withOpacity(0.1), blurRadius: 8)] : null,
        ),
        child: Row(
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(8),
              child: Image.asset(iconPath, height: 40, width: 40, fit: BoxFit.contain, errorBuilder: (c, e, s) => const Icon(Icons.payment, color: Colors.teal, size: 40)),
            ),
            const SizedBox(width: 16),
            Text(name, style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
            const Spacer(),
            Radio<String>(
              value: name,
              groupValue: _selectedMethod,
              activeColor: Colors.teal,
              onChanged: (val) => setState(() => _selectedMethod = val),
            ),
          ],
        ),
      ),
    );
  }

  void _processPayment() async {
    setState(() => _isProcessing = true);
    
    // Simulate payment delay
    await Future.delayed(const Duration(seconds: 3));

    final success = await _firebaseService.bookSeats(
      train: widget.train,
      bogie: widget.bogie,
      selectedSeats: widget.selectedSeats,
      travelDate: widget.travelDate,
    );

    if (mounted) setState(() => _isProcessing = false);

    if (success) {
      _showSuccessDialog();
    } else {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Booking failed! Seat might be taken.'), backgroundColor: Colors.red));
    }
  }

  void _showSuccessDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: const Icon(Icons.check_circle, color: Colors.green, size: 80),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Text('Payment Successful!', style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
            const SizedBox(height: 10),
            Text('Your seats (${widget.selectedSeats.join(", ")}) for ${widget.train.name} have been confirmed.', textAlign: TextAlign.center, style: const TextStyle(color: Colors.grey)),
          ],
        ),
        actions: [
          Column(
            children: [
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.pop(context); // Close Dialog
                    Navigator.popUntil(context, (route) => route.isFirst);
                  },
                  style: ElevatedButton.styleFrom(backgroundColor: Colors.teal, foregroundColor: Colors.white),
                  child: const Text('GO TO HOME'),
                ),
              ),
              const SizedBox(height: 8),
              TextButton(
                onPressed: () {
                  Navigator.pop(context); // Close Dialog
                  Navigator.popUntil(context, (route) => route.isFirst);
                  // Since we are at home, we can trigger the tab change if needed, 
                  // but for simplicity, the user can just click 'My Tickets'.
                },
                child: const Text('VIEW ALL TICKETS', style: TextStyle(color: Colors.teal, fontWeight: FontWeight.bold)),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
