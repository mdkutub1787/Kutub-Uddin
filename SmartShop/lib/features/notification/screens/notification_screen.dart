import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:intl/intl.dart';
import '../riverpod/notification_notifier.dart';
import '../../support/riverpod/support_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../utils/constants/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../support/screens/support_screen.dart';

class NotificationScreen extends ConsumerStatefulWidget {
  const NotificationScreen({super.key});

  @override
  ConsumerState<NotificationScreen> createState() => _NotificationScreenState();
}

class _NotificationScreenState extends ConsumerState<NotificationScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
    
    // Listen to tab changes to mark as read
    _tabController.addListener(() {
      if (_tabController.index == 0) {
      } else {
      }
    });

    // Mark first tab as read initially
    WidgetsBinding.instance.addPostFrameCallback((_) {
    });
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final noticeState = ref.watch(notificationNotifierProvider);
    final supportState = ref.watch(supportNotifierProvider);
    final auth = ref.watch(authNotifierProvider).value;
    
    // dummy unread counts
    final noticeUnreadCount = 0; 
    final supportUnreadCount = 0; 

    final isAdmin = auth?.role == 'admin' || auth?.role == 'super_admin';

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
                  if (noticeUnreadCount > 0)
                    _buildTabBadge(noticeUnreadCount),
                ],
              ),
            ),
            Tab(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text("Support"),
                  if (supportUnreadCount > 0)
                    _buildTabBadge(supportUnreadCount),
                ],
              ),
            ),
          ],
        ),
        actions: [
          if (isAdmin && _tabController.index == 0)
            IconButton(
              icon: const Icon(Icons.add_circle_outline),
              onPressed: () => _showAddEditDialog(context, ref),
            ),
        ],
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          _buildNoticeTab(noticeState, isAdmin, ref),
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

  Widget _buildNoticeTab(AsyncValue<List<dynamic>> noticeState, bool isAdmin, WidgetRef ref) {
    if (noticeState.isLoading) {
      return const Center(child: CircularProgressIndicator());
    }
    
    final notifications = noticeState.value ?? [];

    if (notifications.isEmpty) {
      return _buildEmptyState();
    }
    
    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: notifications.length,
      itemBuilder: (context, index) {
        final notification = notifications[index];
        return _buildNotificationCard(context, notification, isAdmin, ref);
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

  Widget _buildNotificationCard(BuildContext context, dynamic notification, bool isAdmin, WidgetRef ref) {
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
            IconButton(icon: const Icon(Icons.edit, color: Colors.blue), onPressed: () => _showAddEditDialog(context, ref, notification: notification)),
            IconButton(icon: const Icon(Icons.delete, color: Colors.red), onPressed: () => ref.read(notificationNotifierProvider.notifier).deleteNotification(notification.id)),
          ],
        ) : const Icon(Icons.arrow_forward_ios, size: 14, color: Colors.grey),
      ),
    );
  }

  void _showAddEditDialog(BuildContext context, WidgetRef ref, {dynamic notification}) {
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
                // ref.read(notificationNotifierProvider.notifier).addNotification(titleController.text, messageController.text);
              } else {
                // ref.read(notificationNotifierProvider.notifier).updateNotification(notification.id, titleController.text, messageController.text);
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
