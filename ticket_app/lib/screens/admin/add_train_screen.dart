import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:ticket_app/models/train.dart';
import 'package:ticket_app/services/firebase_service.dart';
import 'package:ticket_app/services/station_service.dart';

class AddTrainScreen extends StatefulWidget {
  final Train? train;

  const AddTrainScreen({super.key, this.train});

  @override
  State<AddTrainScreen> createState() => _AddTrainScreenState();
}

class _AddTrainScreenState extends State<AddTrainScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _fromController = TextEditingController();
  final _toController = TextEditingController();
  final _routeController = TextEditingController();
  final _timeController = TextEditingController();
  final _arrivalTimeController = TextEditingController();
  final _dateController = TextEditingController();
  final _firebaseService = FirebaseService();
  final List<Bogie> _bogies = [];
  bool _isDaily = true;
  bool _isSaving = false;

  @override
  void initState() {
    super.initState();
    if (widget.train != null) {
      _nameController.text = widget.train!.name;
      _fromController.text = widget.train!.from;
      _toController.text = widget.train!.to;
      _routeController.text = widget.train!.route;
      _timeController.text = widget.train!.departureTime;
      _arrivalTimeController.text = widget.train!.arrivalTime;
      _dateController.text = widget.train!.date;
      _isDaily = widget.train!.isDaily;
      _bogies.addAll(widget.train!.bogies);
    }
  }

  Future<void> _selectDate() async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 365)),
    );
    if (picked != null) {
      setState(() {
        _dateController.text = DateFormat('yyyy-MM-dd').format(picked);
      });
    }
  }

  Future<void> _selectTime(TextEditingController controller) async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: TimeOfDay.now(),
    );
    if (picked != null) {
      setState(() {
        controller.text = picked.format(context);
      });
    }
  }

  void _addBogie() {
    showDialog(
      context: context,
      builder: (context) {
        String bogieName = "";
        SeatType selectedType = SeatType.nonAc;
        double price = 0;
        int totalSeats = 40;

        return StatefulBuilder(builder: (context, setDialogState) {
          return AlertDialog(
            title: const Text('Add New Bogie'),
            content: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextField(
                    onChanged: (val) => bogieName = val,
                    decoration: const InputDecoration(labelText: 'Bogie Name (e.g. UMA, CHA)'),
                  ),
                  DropdownButtonFormField<SeatType>(
                    value: selectedType,
                    items: SeatType.values.map((type) => DropdownMenuItem(value: type, child: Text(type.toString().split('.').last.toUpperCase()))).toList(),
                    onChanged: (val) => setDialogState(() => selectedType = val!),
                    decoration: const InputDecoration(labelText: 'Seat Type'),
                  ),
                  TextField(
                    onChanged: (val) => price = double.tryParse(val) ?? 0,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(labelText: 'Price Per Seat'),
                  ),
                  TextField(
                    onChanged: (val) => totalSeats = int.tryParse(val) ?? 40,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(labelText: 'Total Seats'),
                  ),
                ],
              ),
            ),
            actions: [
              TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
              ElevatedButton(
                onPressed: () {
                  if (bogieName.isEmpty || price <= 0) return;
                  setState(() {
                    _bogies.add(Bogie(
                      name: bogieName.toUpperCase(),
                      type: selectedType,
                      price: price,
                      totalSeats: totalSeats,
                    ));
                  });
                  Navigator.pop(context);
                },
                child: const Text('Add'),
              ),
            ],
          );
        });
      },
    );
  }

  void _saveTrain() async {
    if (!_formKey.currentState!.validate()) return;
    if (_bogies.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Please add at least one bogie')));
      return;
    }

    setState(() => _isSaving = true);

    final train = Train(
      id: widget.train?.id ?? '',
      name: _nameController.text.trim(),
      from: _fromController.text.trim(),
      to: _toController.text.trim(),
      route: _routeController.text.trim(),
      departureTime: _timeController.text.trim(),
      arrivalTime: _arrivalTimeController.text.trim(),
      date: _dateController.text.trim(),
      isDaily: _isDaily,
      bogies: _bogies,
    );

    try {
      if (widget.train == null) {
        await _firebaseService.addTrain(train);
      } else {
        await _firebaseService.updateTrain(train);
      }
      if (mounted) Navigator.pop(context);
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Error: $e')));
        setState(() => _isSaving = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: Text(widget.train == null ? 'Add New Train' : 'Edit Train', style: const TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: Form(
        key: _formKey,
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildSectionTitle('General Information'),
              _buildTextField(_nameController, 'Train Name', Icons.train),
              Row(
                children: [
                  Expanded(child: _buildStationAutocomplete(_fromController, 'From Station', Icons.location_on_outlined)),
                  const SizedBox(width: 10),
                  Expanded(child: _buildStationAutocomplete(_toController, 'To Station', Icons.location_on)),
                ],
              ),
              _buildTextField(_routeController, 'Full Route Description', Icons.map),
              
              const SizedBox(height: 20),
              _buildSectionTitle('Schedule'),
              Row(
                children: [
                  Expanded(
                    child: InkWell(
                      onTap: () => _selectTime(_timeController),
                      child: _buildTextField(_timeController, 'Departure Time', Icons.access_time, enabled: false),
                    ),
                  ),
                  const SizedBox(width: 10),
                  Expanded(
                    child: InkWell(
                      onTap: () => _selectTime(_arrivalTimeController),
                      child: _buildTextField(_arrivalTimeController, 'Arrival Time', Icons.access_time_filled, enabled: false),
                    ),
                  ),
                ],
              ),
              InkWell(
                onTap: _selectDate,
                child: _buildTextField(_dateController, 'Base Date', Icons.calendar_today, enabled: false),
              ),
              SwitchListTile(
                title: const Text('Daily Train', style: TextStyle(fontWeight: FontWeight.bold)),
                subtitle: const Text('Train will be available every day'),
                value: _isDaily,
                activeColor: Colors.teal,
                onChanged: (val) => setState(() => _isDaily = val),
              ),

              const SizedBox(height: 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  _buildSectionTitle('Bogies / Coaches'),
                  TextButton.icon(
                    onPressed: _addBogie,
                    icon: const Icon(Icons.add_circle_outline, color: Colors.teal),
                    label: const Text('Add Bogie', style: TextStyle(color: Colors.teal, fontWeight: FontWeight.bold)),
                  ),
                ],
              ),
              if (_bogies.isEmpty)
                const Center(child: Padding(padding: EdgeInsets.all(20), child: Text('No bogies added yet', style: TextStyle(color: Colors.grey)))),
              ..._bogies.map((bogie) => _buildBogieCard(bogie)).toList(),

              const SizedBox(height: 40),
              SizedBox(
                width: double.infinity,
                height: 55,
                child: ElevatedButton(
                  onPressed: _isSaving ? null : _saveTrain,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.teal[800],
                    foregroundColor: Colors.white,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    elevation: 4,
                  ),
                  child: _isSaving
                      ? const CircularProgressIndicator(color: Colors.white)
                      : Text(widget.train == null ? 'CREATE TRAIN' : 'UPDATE TRAIN', style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, letterSpacing: 1.2)),
                ),
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildSectionTitle(String title) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Text(title, style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.teal[900])),
    );
  }

  Widget _buildTextField(TextEditingController controller, String label, IconData icon, {bool enabled = true}) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 15),
      child: TextFormField(
        controller: controller,
        enabled: enabled,
        validator: (val) => val == null || val.isEmpty ? 'Required' : null,
        decoration: InputDecoration(
          labelText: label,
          prefixIcon: Icon(icon, color: Colors.teal),
          filled: true,
          fillColor: Colors.white,
          border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide(color: Colors.grey[300]!)),
          enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide(color: Colors.grey[200]!)),
        ),
      ),
    );
  }

  Widget _buildStationAutocomplete(TextEditingController controller, String label, IconData icon) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 15),
      child: Autocomplete<String>(
        optionsBuilder: (value) => StationService.getSuggestions(value.text),
        onSelected: (selection) => controller.text = selection,
        fieldViewBuilder: (context, fieldController, focusNode, onFieldSubmitted) {
          if (fieldController.text != controller.text) fieldController.text = controller.text;
          return TextFormField(
            controller: fieldController,
            focusNode: focusNode,
            validator: (val) => val == null || val.isEmpty ? 'Required' : null,
            decoration: InputDecoration(
              labelText: label,
              prefixIcon: Icon(icon, color: Colors.teal),
              filled: true,
              fillColor: Colors.white,
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
            ),
            onChanged: (val) => controller.text = val,
          );
        },
      ),
    );
  }

  Widget _buildBogieCard(Bogie bogie) {
    return Card(
      elevation: 0,
      margin: const EdgeInsets.only(bottom: 10),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12), side: BorderSide(color: Colors.grey[200]!)),
      child: ListTile(
        leading: CircleAvatar(backgroundColor: Colors.orange[50], child: Icon(Icons.airline_seat_recline_extra, color: Colors.orange[800])),
        title: Text('${bogie.name} (${bogie.type.toString().split('.').last.toUpperCase()})', style: const TextStyle(fontWeight: FontWeight.bold)),
        subtitle: Text('Price: ৳${bogie.price} | Total Seats: ${bogie.totalSeats}'),
        trailing: IconButton(
          icon: const Icon(Icons.remove_circle_outline, color: Colors.red),
          onPressed: () => setState(() => _bogies.remove(bogie)),
        ),
      ),
    );
  }
}
