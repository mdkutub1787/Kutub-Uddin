import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:intl/intl.dart';
import '../../view_models/notification_view_model.dart';
import '../../view_models/support_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../utils/constants/app_strings.dart';
import '../../routes/app_routes.dart';
import '../support/support_screen.dart';

class NotificationScreen extends StatefulWidget {
  const NotificationScreen({super.key});

  @override
  State<NotificationScreen> createState() => _NotificationScreenState();
}

class _NotificationScreenState extends State<NotificationScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
    
    // Listen to tab changes to mark as read
    _tabController.addListener(() {
      if (_tabController.index == 0) {
        context.read<NotificationViewModel>().markAsRead();
      } else {
        context.read<SupportViewModel>().markAsRead();
      }
    });

    // Mark first tab as read initially
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<NotificationViewModel>().markAsRead();
    });
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final noticeVM = context.watch<NotificationViewModel>();
    final supportVM = context.watch<SupportViewModel>();
    final authVM = context.watch<AuthViewModel>();

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).primaryColor,
        foregroundColor: Colors.white,
        title: const Text("Notification Center", style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white)),
        centerTitle: true,
        bottom: TabBar(
          controller: _tabController,
          indicatorColor: Colors.white,
          indicatorWeight: 3,
          labelColor: Colors.white,
          unselectedLabelColor: Colors.white70,
          tabs: [
            Tab(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text("Notices"),
                  if (noticeVM.unreadCount > 0)
                    _buildTabBadge(noticeVM.unreadCount),
                ],
              ),
            ),
            Tab(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text("Support"),
                  if (supportVM.unreadCount > 0)
                    _buildTabBadge(supportVM.unreadCount),
                ],
              ),
            ),
          ],
        ),
        actions: [
          if (authVM.isAdmin && _tabController.index == 0)
            IconButton(
              icon: const Icon(Icons.add_circle_outline),
              onPressed: () => _showAddEditDialog(context, noticeVM),
            ),
        ],
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          _buildNoticeTab(noticeVM, authVM),
          const SupportScreen(isEmbedded: true), // Updated with isEmbedded
        ],
      ),
    );
  }

  Widget _buildTabBadge(int count) {
    return Container(
      margin: const EdgeInsets.only(left: 8),
      padding: const EdgeInsets.all(4),
      decoration: const BoxDecoration(color: Colors.red, shape: BoxShape.circle),
      constraints: const BoxConstraints(minWidth: 18, minHeight: 18),
      child: Text(
        '$count',
        style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
        textAlign: TextAlign.center,
      ),
    );
  }

  Widget _buildNoticeTab(NotificationViewModel notificationVM, AuthViewModel authVM) {
    return notificationVM.isLoading 
        ? const Center(child: CircularProgressIndicator())
        : notificationVM.notifications.isEmpty
          ? _buildEmptyState()
          : ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: notificationVM.notifications.length,
              itemBuilder: (context, index) {
                final notification = notificationVM.notifications[index];
                return _buildNotificationCard(context, notification, authVM.isAdmin, notificationVM);
              },
            );
  }

  Widget _buildEmptyState() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.notifications_off_outlined, size: 80, color: Colors.grey[300]),
          const SizedBox(height: 16),
          Text(AppStrings.noNotifications.tr(), style: TextStyle(color: Colors.grey[600], fontSize: 16)),
        ],
      ),
    );
  }

  Widget _buildNotificationCard(BuildContext context, dynamic notification, bool isAdmin, NotificationViewModel vm) {
    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(15),
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 10, offset: const Offset(0, 4))],
      ),
      child: ListTile(
        onTap: () => Navigator.pushNamed(context, AppRoutes.notificationDetails, arguments: notification),
        contentPadding: const EdgeInsets.all(16),
        title: Text(notification.title, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 8),
            Text(notification.message, maxLines: 2, overflow: TextOverflow.ellipsis),
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
        ) : const Icon(Icons.arrow_forward_ios, size: 14, color: Colors.grey),
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
