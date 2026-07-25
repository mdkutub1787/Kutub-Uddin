import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../../../models/notification_model.dart';

class NotificationDetailsScreen extends StatelessWidget {
  final NotificationModel notification;

  const NotificationDetailsScreen({super.key, required this.notification});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF5F7FA),
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: notification.imageUrl != null && notification.imageUrl!.isNotEmpty ? 250.0 : 120.0,
            floating: false,
            pinned: true,
            backgroundColor: const Color(0xFF1B3128),
            iconTheme: const IconThemeData(color: Colors.white),
            flexibleSpace: FlexibleSpaceBar(
              title: const Text("Notice Details", style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
              background: notification.imageUrl != null && notification.imageUrl!.isNotEmpty
                  ? Stack(
                      fit: StackFit.expand,
                      children: [
                        Image.network(notification.imageUrl!, fit: BoxFit.cover),
                        Container(
                          decoration: BoxDecoration(
                            gradient: LinearGradient(
                              begin: Alignment.topCenter,
                              end: Alignment.bottomCenter,
                              colors: [Colors.transparent, const Color(0xFF1B3128).withValues(alpha: 0.8)],
                            ),
                          ),
                        ),
                      ],
                    )
                  : Container(
                      decoration: const BoxDecoration(
                        gradient: LinearGradient(
                          colors: [Color(0xFF1B3128), Color(0xFF2A4D40)],
                          begin: Alignment.topLeft,
                          end: Alignment.bottomRight,
                        ),
                      ),
                    ),
            ),
          ),
          SliverToBoxAdapter(
            child: Container(
              transform: Matrix4.translationValues(0, -20, 0),
              padding: const EdgeInsets.all(24),
              decoration: const BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.only(topLeft: Radius.circular(30), topRight: Radius.circular(30)),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                    decoration: BoxDecoration(
                      color: const Color(0xFFE2F3ED),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(Icons.access_time_rounded, size: 14, color: Color(0xFF1B3128)),
                        const SizedBox(width: 6),
                        Text(
                          DateFormat('dd MMM yyyy, hh:mm a').format(notification.timestamp),
                          style: const TextStyle(color: Color(0xFF1B3128), fontSize: 12, fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 20),
                  Text(
                    notification.title,
                    style: const TextStyle(fontSize: 26, fontWeight: FontWeight.w900, color: Color(0xFF1B3128), height: 1.2),
                  ),
                  const SizedBox(height: 20),
                  const Divider(color: Colors.black12),
                  const SizedBox(height: 20),
                  Text(
                    notification.message,
                    style: TextStyle(fontSize: 16, height: 1.7, color: Colors.grey[800]),
                  ),
                  const SizedBox(height: 100), // padding for scrolling
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
