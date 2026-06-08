import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/widgets/empty_state_widget.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/models/notification_model/notification_model.dart';
import 'package:fflipy/providers/notification_providers.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/widgets/brand_app_bar.dart';

class NotificationsScreen extends ConsumerStatefulWidget {
  const NotificationsScreen({super.key});

  @override
  ConsumerState<NotificationsScreen> createState() => _NotificationsScreenState();
}

class _NotificationsScreenState extends ConsumerState<NotificationsScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(notificationViewModelProvider.notifier).getNotifications();
    });
  }

  Future<void> _refreshNotifications() async {
    await ref.read(notificationViewModelProvider.notifier).getNotifications();
  }
  
  void _deleteNotification(int id) {
      ref.read(notificationViewModelProvider.notifier).markAsRead(id);
  }

  @override
  Widget build(BuildContext context) {
    final notificationState = ref.watch(notificationViewModelProvider);

    ref.listen(notificationViewModelProvider, (previous, next) {
      if (next.error != null && next.error != previous?.error) {
         ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(context.tr(ErrorHandler.getErrorMessage(next.error!)))),
        );
      }
    });

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('Notifications')),
      ),
      body: Stack(
        children: [
          if (notificationState.error != null && (notificationState.notificationResponse?.data.isEmpty ?? true))
             Center(child: Padding(
               padding: const EdgeInsets.all(20.0),
               child: Text(context.tr(ErrorHandler.getErrorMessage(notificationState.error))),
             ))
          else if (notificationState.notificationResponse?.data.isEmpty ?? true)
            if (!notificationState.isLoading) Center(child: EmptyStateWidget(message: context.tr('No Notifications Yet'))),

          if (notificationState.notificationResponse?.data.isNotEmpty ?? false)
            RefreshIndicator(
              onRefresh: _refreshNotifications,
              child: ListView.separated(
                itemCount: notificationState.notificationResponse!.data.length,
                separatorBuilder: (context, index) => const Divider(height: 1, indent: 80),
                itemBuilder: (context, index) {
                  final notification = notificationState.notificationResponse!.data[index];
                  return Dismissible(
                    key: Key(notification.id.toString()),
                    direction: DismissDirection.endToStart,
                    background: Container(
                      color: Colors.red,
                      alignment: Alignment.centerRight,
                      padding: const EdgeInsets.only(right: 20.0),
                      child: const Icon(Icons.delete, color: Colors.white),
                    ),
                    onDismissed: (direction) {
                       _deleteNotification(notification.id);
                    },
                    child: _buildNotificationItem(context, notification),
                  );
                },
              ),
            ),
            
          if (notificationState.isLoading && (notificationState.notificationResponse == null))
            const Preloader(),
        ],
      ),
    );
  }

  Widget _buildNotificationItem(BuildContext context, NotificationItem notification) {
    final IconData iconData = Icons.notifications; 
    final Color iconColor = Theme.of(context).primaryColor;

    return InkWell(
      onTap: () {
      },
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 16.0, horizontal: 16.0),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: iconColor.withOpacity(0.1),
                shape: BoxShape.circle,
              ),
              child: Icon(iconData, color: iconColor),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    notification.description.text,
                    style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    notification.formattedDate,
                    style: Theme.of(context).textTheme.bodySmall?.copyWith(
                      color: Colors.grey.shade500,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  // Removed manual empty state build in favor of EmptyStateWidget
}
