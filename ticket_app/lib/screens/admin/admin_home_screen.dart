import 'package:flutter/material.dart';
import 'package:ticket_app/services/firebase_service.dart';
import 'package:ticket_app/models/train.dart';
import 'package:ticket_app/screens/admin/add_train_screen.dart';

class AdminHomeScreen extends StatelessWidget {
  AdminHomeScreen({super.key});

  final FirebaseService _firebaseService = FirebaseService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: const Text('Admin Dashboard', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () => _firebaseService.signOut(),
          ),
        ],
      ),
      body: StreamBuilder<List<Train>>(
        stream: _firebaseService.getTrains(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          final trains = snapshot.data ?? [];
          
          if (trains.isEmpty) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.train_outlined, size: 80, color: Colors.grey[300]),
                  const SizedBox(height: 16),
                  const Text('No trains added yet', style: TextStyle(color: Colors.grey)),
                ],
              ),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(12),
            itemCount: trains.length,
            itemBuilder: (context, index) {
              final train = trains[index];
              return Card(
                elevation: 2,
                margin: const EdgeInsets.only(bottom: 12),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
                child: ListTile(
                  contentPadding: const EdgeInsets.all(16),
                  leading: CircleAvatar(
                    backgroundColor: Colors.teal[50],
                    child: const Icon(Icons.train, color: Colors.teal),
                  ),
                  title: Text(train.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                  subtitle: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const SizedBox(height: 4),
                      Text('${train.from} ➔ ${train.to}', style: TextStyle(color: Colors.teal[700], fontWeight: FontWeight.w500)),
                      Text('Time: ${train.departureTime} | ${train.isDaily ? "Daily" : "Date: ${train.date}"}'),
                    ],
                  ),
                  trailing: PopupMenuButton(
                    onSelected: (value) {
                      if (value == 'edit') {
                        Navigator.push(context, MaterialPageRoute(builder: (context) => AddTrainScreen(train: train)));
                      } else if (value == 'delete') {
                        _showDeleteConfirm(context, train.id);
                      }
                    },
                    itemBuilder: (context) => [
                      const PopupMenuItem(value: 'edit', child: Row(children: [Icon(Icons.edit, color: Colors.blue), SizedBox(width: 8), Text('Edit')])),
                      const PopupMenuItem(value: 'delete', child: Row(children: [Icon(Icons.delete, color: Colors.red), SizedBox(width: 8), Text('Delete')])),
                    ],
                  ),
                ),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (context) => const AddTrainScreen())),
        label: const Text('ADD TRAIN'),
        icon: const Icon(Icons.add),
        backgroundColor: Colors.teal[800],
        foregroundColor: Colors.white,
      ),
    );
  }

  void _showDeleteConfirm(BuildContext context, String id) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Delete Train?'),
        content: const Text('Are you sure you want to remove this train and all its schedules?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
          TextButton(
            onPressed: () {
              _firebaseService.deleteTrain(id);
              Navigator.pop(context);
            },
            child: const Text('Delete', style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }
}
