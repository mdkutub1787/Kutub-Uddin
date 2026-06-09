import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../../view_models/notification_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/settings_view_model.dart';

class NotificationScreen extends StatelessWidget {
  const NotificationScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final notificationVM = context.watch<NotificationViewModel>();
    final authVM = context.watch<AuthViewModel>();
    final settings = context.watch<SettingsViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: const Text("Notifications", style: TextStyle(fontWeight: FontWeight.bold)),
        actions: [
          if (authVM.isAdmin)
            IconButton(
              icon: const Icon(Icons.add_circle_outline),
              onPressed: () => _showAddEditDialog(context, notificationVM),
            ),
        ],
      ),
      body: notificationVM.isLoading 
        ? const Center(child: CircularProgressIndicator())
        : notificationVM.notifications.isEmpty
          ? _buildEmptyState()
          : ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: notificationVM.notifications.length,
              itemBuilder: (context, index) {
                final notification = notificationVM.notifications[index];
                return _buildNotificationCard(context, notification, authVM.isAdmin, notificationVM, settings);
              },
            ),
    );
  }

  Widget _buildEmptyState() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.notifications_off_outlined, size: 80, color: Colors.grey[300]),
          const SizedBox(height: 16),
          Text("No notifications yet", style: TextStyle(color: Colors.grey[600], fontSize: 16)),
        ],
      ),
    );
  }

  Widget _buildNotificationCard(BuildContext context, dynamic notification, bool isAdmin, NotificationViewModel vm, SettingsViewModel settings) {
    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(15),
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 10, offset: const Offset(0, 4))],
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.all(16),
        title: Text(notification.title, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 8),
            Text(notification.message),
            const SizedBox(height: 8),
            Text(
              DateFormat('dd MMM yyyy, hh:mm a').format(notification.timestamp),
              style: TextStyle(color: Colors.grey[400], fontSize: 12),
            ),
          ],
        ),
        trailing: isAdmin ? Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            IconButton(icon: const Icon(Icons.edit, color: Colors.blue), onPressed: () => _showAddEditDialog(context, vm, notification: notification)),
            IconButton(icon: const Icon(Icons.delete, color: Colors.red), onPressed: () => vm.deleteNotification(notification.id)),
          ],
        ) : null,
      ),
    );
  }

  void _showAddEditDialog(BuildContext context, NotificationViewModel vm, {dynamic notification}) {
    final titleController = TextEditingController(text: notification?.title ?? "");
    final messageController = TextEditingController(text: notification?.message ?? "");

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text(notification == null ? "Add Notification" : "Edit Notification"),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(controller: titleController, decoration: const InputDecoration(labelText: "Title")),
            TextField(controller: messageController, decoration: const InputDecoration(labelText: "Message"), maxLines: 3),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("Cancel")),
          ElevatedButton(
            onPressed: () {
              if (notification == null) {
                vm.addNotification(titleController.text, messageController.text);
              } else {
                vm.updateNotification(notification.id, titleController.text, messageController.text);
              }
              Navigator.pop(ctx);
            },
            child: const Text("Save"),
          ),
        ],
      ),
    );
  }
}
