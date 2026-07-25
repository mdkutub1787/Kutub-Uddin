import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:intl/intl.dart';
import '../riverpod/notification_notifier.dart';
import '../../support/riverpod/support_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/app_strings.dart';
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
    final auth = ref.watch(authNotifierProvider).value;
    
    // dummy unread counts
    final noticeUnreadCount = 0; 
    final supportUnreadCount = 0; 
    final isAdmin = auth?.role == 'admin' || auth?.role == 'super_admin';

    return Scaffold(
      backgroundColor: const Color(0xFFF5F7FA), // Light premium background
      body: Column(
        children: [
          _buildPremiumHeader(noticeUnreadCount, supportUnreadCount, isAdmin),
          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: [
                _buildNoticeTab(noticeState, isAdmin, ref),
                const SupportScreen(isEmbedded: true),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildPremiumHeader(int noticeCount, int supportCount, bool isAdmin) {
    return Container(
      padding: EdgeInsets.only(top: MediaQuery.of(context).padding.top, bottom: 20),
      decoration: const BoxDecoration(
        color: Color(0xFF1B3128),
        borderRadius: BorderRadius.only(
          bottomLeft: Radius.circular(30),
          bottomRight: Radius.circular(30),
        ),
        boxShadow: [
          BoxShadow(color: Colors.black26, blurRadius: 10, offset: Offset(0, 5))
        ],
      ),
      child: Column(
        children: [
          // Header Bar
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                GestureDetector(
                  onTap: () => Navigator.pop(context),
                  child: Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.1), shape: BoxShape.circle),
                    child: const Icon(Icons.arrow_back_ios_new_rounded, color: Colors.white, size: 18),
                  ),
                ),
                const Text(
                  "Notification Center",
                  style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                ),
                if (isAdmin)
                  GestureDetector(
                    onTap: () => _showAddEditDialog(context, ref),
                    child: Container(
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.1), shape: BoxShape.circle),
                      child: const Icon(Icons.add_rounded, color: Colors.white, size: 20),
                    ),
                  )
                else
                  const SizedBox(width: 36),
              ],
            ),
          ),
          const SizedBox(height: 15),
          // Custom TabBar
          Container(
            height: 45,
            margin: const EdgeInsets.symmetric(horizontal: 20),
            decoration: BoxDecoration(
              color: Colors.white.withValues(alpha: 0.1),
              borderRadius: BorderRadius.circular(25),
            ),
            child: TabBar(
              controller: _tabController,
              dividerColor: Colors.transparent,
              indicatorSize: TabBarIndicatorSize.tab,
              indicator: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(25),
                boxShadow: [
                  BoxShadow(color: Colors.black.withValues(alpha: 0.1), blurRadius: 4, offset: const Offset(0, 2))
                ]
              ),
              labelColor: const Color(0xFF1B3128),
              unselectedLabelColor: Colors.white70,
              labelStyle: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
              tabs: [
                Tab(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text("Notices"),
                      if (noticeCount > 0) _buildTabBadge(noticeCount),
                    ],
                  ),
                ),
                Tab(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text("Support"),
                      if (supportCount > 0) _buildTabBadge(supportCount),
                    ],
                  ),
                ),
              ],
            ),
          ),
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
      margin: const EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.03), blurRadius: 10, offset: const Offset(0, 4))],
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          borderRadius: BorderRadius.circular(20),
          onTap: () => Navigator.pushNamed(context, AppRoutes.notificationDetails, arguments: notification),
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Container(
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: const Color(0xFFE2F3ED),
                    borderRadius: BorderRadius.circular(16),
                  ),
                  child: const Icon(Icons.notifications_active_rounded, color: Color(0xFF1B3128), size: 24),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Text(notification.title, style: const TextStyle(fontWeight: FontWeight.w800, fontSize: 16, color: Color(0xFF1B3128)), maxLines: 1, overflow: TextOverflow.ellipsis),
                          ),
                          Text(
                            DateFormat('dd MMM').format(notification.timestamp),
                            style: TextStyle(color: Colors.grey[400], fontSize: 11, fontWeight: FontWeight.bold),
                          ),
                        ],
                      ),
                      const SizedBox(height: 6),
                      Text(notification.message, maxLines: 2, overflow: TextOverflow.ellipsis, style: TextStyle(color: Colors.grey[600], fontSize: 13, height: 1.4)),
                      if (isAdmin) ...[
                        const SizedBox(height: 10),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.end,
                          children: [
                            GestureDetector(
                              onTap: () => _showAddEditDialog(context, ref, notification: notification),
                              child: const Icon(Icons.edit_rounded, color: Colors.blue, size: 18),
                            ),
                            const SizedBox(width: 16),
                            GestureDetector(
                              onTap: () => ref.read(notificationNotifierProvider.notifier).deleteNotification(notification.id),
                              child: const Icon(Icons.delete_rounded, color: Colors.red, size: 18),
                            ),
                          ],
                        )
                      ]
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
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
