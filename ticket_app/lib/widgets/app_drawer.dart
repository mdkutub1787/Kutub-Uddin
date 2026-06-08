import 'package:flutter/material.dart';
import 'package:ticket_app/services/firebase_service.dart';

class AppDrawer extends StatelessWidget {
  const AppDrawer({super.key});

  @override
  Widget build(BuildContext context) {
    final FirebaseService firebaseService = FirebaseService();

    return Drawer(
      child: Column(
        children: [
          Expanded(
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                DrawerHeader(
                  decoration: const BoxDecoration(color: Colors.white),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Image.asset('assets/Bangladesh-Railway-Ticket.jpg', height: 60),
                      const SizedBox(height: 10),
                      const Text(
                        'Rail Sheba',
                        style: TextStyle(color: Colors.orange, fontSize: 20, fontWeight: FontWeight.bold),
                      ),
                    ],
                  ),
                ),
                _buildDrawerItem(Icons.train, 'Train Information', Colors.teal),
                _buildDrawerItem(Icons.verified_user, 'Verify Ticket', Colors.teal),
                _buildDrawerItem(Icons.fastfood, 'Food', Colors.teal),
                _buildDrawerItem(Icons.star, 'Ratings & Reviews', Colors.teal),
                _buildDrawerItem(Icons.campaign, 'Announcement', Colors.teal),
                const Divider(),
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildFooterLink('Terms & Conditions'),
                      const SizedBox(height: 12),
                      _buildFooterLink('Privacy Policy'),
                    ],
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text('For any queries, please contact us', style: TextStyle(color: Colors.grey, fontSize: 12)),
                const SizedBox(height: 4),
                const Text(
                  'support@eticket.railway.gov.bd',
                  style: TextStyle(color: Colors.teal, fontWeight: FontWeight.bold, fontSize: 12),
                ),
                const SizedBox(height: 20),
                ListTile(
                  leading: const Icon(Icons.logout, color: Colors.red),
                  title: const Text('Logout', style: TextStyle(color: Colors.red)),
                  onTap: () => firebaseService.signOut(),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDrawerItem(IconData icon, String title, Color iconColor) {
    return ListTile(
      leading: Icon(icon, color: iconColor),
      title: Text(title, style: const TextStyle(color: Colors.orange, fontWeight: FontWeight.w500)),
      onTap: () {},
    );
  }

  Widget _buildFooterLink(String text) {
    return Text(
      text,
      style: const TextStyle(color: Colors.teal, fontWeight: FontWeight.bold, fontSize: 14),
    );
  }
}
