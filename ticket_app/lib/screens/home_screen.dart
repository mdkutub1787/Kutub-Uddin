import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:ticket_app/models/train.dart';
import 'package:ticket_app/services/firebase_service.dart';
import 'package:ticket_app/services/station_service.dart';
import 'package:ticket_app/screens/bookings_screen.dart';
import 'package:ticket_app/screens/user/train_results_screen.dart';
import 'package:ticket_app/screens/user/profile_screen.dart';
import 'package:ticket_app/widgets/app_drawer.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final FirebaseService _firebaseService = FirebaseService();
  final _fromController = TextEditingController();
  final _toController = TextEditingController();
  DateTime? _selectedDate;
  SeatType? _selectedClass;
  bool _isSearching = false;
  int _currentIndex = 0;

  void _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _selectedDate ?? DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 30)),
    );
    if (picked != null) setState(() => _selectedDate = picked);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF5F5F5),
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        leading: Builder(
          builder: (context) => IconButton(
            icon: const Icon(Icons.menu, color: Color(0xFF00695C)),
            onPressed: () => Scaffold.of(context).openDrawer(),
          ),
        ),
        title: const Text('Rail Sheba', style: TextStyle(color: Color(0xFF8D6E63), fontWeight: FontWeight.bold)),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh, color: Colors.teal),
            onPressed: () => setState(() {}),
          )
        ],
      ),
      drawer: const AppDrawer(),
      body: _buildBody(),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _currentIndex,
        selectedItemColor: const Color(0xFF00695C),
        unselectedItemColor: Colors.grey,
        onTap: (index) => setState(() => _currentIndex = index),
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.train_outlined), label: 'Buy Tickets'),
          BottomNavigationBarItem(icon: Icon(Icons.confirmation_number_outlined), label: 'My Tickets'),
          BottomNavigationBarItem(icon: Icon(Icons.person_outline), label: 'My Account'),
        ],
      ),
    );
  }

  Widget _buildBody() {
    if (_currentIndex == 1) return BookingsScreen();
    if (_currentIndex == 2) return ProfileScreen();

    return SingleChildScrollView(
      child: Column(
        children: [
          _buildSearchCard(),
          _buildNoticeSection(),
          if (_isSearching) _buildSearchResultsLogic(),
          const SizedBox(height: 20),
        ],
      ),
    );
  }

  Widget _buildSearchCard() {
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(15), boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 10)]),
      child: Column(
        children: [
          _buildStationField('FROM', _fromController, Icons.location_on_outlined, Colors.teal),
          IconButton(icon: const Icon(Icons.swap_vert, color: Colors.orange), onPressed: () {
            setState(() {
              String temp = _fromController.text;
              _fromController.text = _toController.text;
              _toController.text = temp;
            });
          }),
          _buildStationField('TO', _toController, Icons.location_on, Colors.redAccent),
          const SizedBox(height: 15),
          _buildClassDropdown(),
          const SizedBox(height: 15),
          _buildDateField(),
          const SizedBox(height: 25),
          SizedBox(
            width: double.infinity,
            height: 50,
            child: ElevatedButton(
              onPressed: () {
                if (_fromController.text.isEmpty || _toController.text.isEmpty || _selectedDate == null) {
                  ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Please select From, To and Date')));
                  return;
                }
                setState(() => _isSearching = true);
              },
              style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFF00695C), foregroundColor: Colors.white, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12))),
              child: const Text('SEARCH TRAINS', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStationField(String label, TextEditingController controller, IconData icon, Color color) {
    return Autocomplete<String>(
      optionsBuilder: (value) => StationService.getSuggestions(value.text),
      onSelected: (selection) => controller.text = selection,
      fieldViewBuilder: (context, fieldController, focusNode, onFieldSubmitted) {
        if (fieldController.text != controller.text) fieldController.text = controller.text;
        return TextField(
          controller: fieldController,
          focusNode: focusNode,
          decoration: InputDecoration(
            labelText: label,
            prefixIcon: Icon(icon, color: color),
            border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
          ),
          onChanged: (val) => controller.text = val,
        );
      },
    );
  }

  Widget _buildClassDropdown() {
    return DropdownButtonFormField<SeatType>(
      decoration: InputDecoration(labelText: 'CLASS', border: OutlineInputBorder(borderRadius: BorderRadius.circular(12))),
      value: _selectedClass,
      items: SeatType.values.map((t) => DropdownMenuItem(value: t, child: Text(t.toString().split('.').last.toUpperCase()))).toList(),
      onChanged: (val) => setState(() => _selectedClass = val),
    );
  }

  Widget _buildDateField() {
    return InkWell(
      onTap: () => _selectDate(context),
      child: InputDecorator(
        decoration: InputDecoration(labelText: 'JOURNEY DATE', border: OutlineInputBorder(borderRadius: BorderRadius.circular(12))),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(_selectedDate == null ? 'Select Date' : DateFormat('yyyy-MM-dd').format(_selectedDate!)),
            const Icon(Icons.calendar_month, color: Color(0xFF00695C)),
          ],
        ),
      ),
    );
  }

  Widget _buildNoticeSection() {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16),
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(color: Colors.yellow[50], borderRadius: BorderRadius.circular(10), border: Border.all(color: Colors.orange[100]!)),
      child: const Text('• সর্বোচ্চ ৪টি টিকেট কাটা যাবে।\n• এনআইডি সাথে রাখা বাধ্যতামূলক।', style: TextStyle(fontSize: 12, color: Colors.brown)),
    );
  }

  Widget _buildSearchResultsLogic() {
    String searchDate = DateFormat('yyyy-MM-dd').format(_selectedDate!);
    return StreamBuilder<List<Train>>(
      stream: _firebaseService.searchTrains(_fromController.text.trim(), _toController.text.trim(), searchDate),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) return const Padding(padding: EdgeInsets.all(20), child: CircularProgressIndicator());
        
        final trains = snapshot.data ?? [];
        if (trains.isNotEmpty) {
          WidgetsBinding.instance.addPostFrameCallback((_) {
            if (mounted && _isSearching) {
              setState(() => _isSearching = false);
              Navigator.push(context, MaterialPageRoute(builder: (c) => TrainResultsScreen(
                trains: trains, from: _fromController.text, to: _toController.text, date: searchDate,
              )));
            }
          });
        } else if (snapshot.connectionState == ConnectionState.active) {
          WidgetsBinding.instance.addPostFrameCallback((_) {
            if (mounted && _isSearching) {
              setState(() => _isSearching = false);
              ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('No trains found! Please check Admin Panel if trains are added for this route.')));
            }
          });
        }
        return const SizedBox.shrink();
      },
    );
  }
}
