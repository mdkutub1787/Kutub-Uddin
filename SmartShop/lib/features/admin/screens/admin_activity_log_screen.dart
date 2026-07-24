import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../admin/riverpod/activity_log_notifier.dart';
import '../../../widgets/custom_app_bar.dart';

class ActivityLogModel {
  final String adminName;
  final String action;
  final String targetId;
  final String details;
  final DateTime timestamp;

  ActivityLogModel({
    required this.adminName,
    required this.action,
    required this.targetId,
    required this.details,
    required this.timestamp,
  });
}

class AdminActivityLogScreen extends ConsumerStatefulWidget {
  const AdminActivityLogScreen({super.key});

  @override
  ConsumerState<AdminActivityLogScreen> createState() => _AdminActivityLogScreenState();
}

class _AdminActivityLogScreenState extends ConsumerState<AdminActivityLogScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      // ref.read(activityLogNotifierProvider.notifier).loadLogs();
    });
  }

  @override
  Widget build(BuildContext context) {
    // For now returning an empty list as we don't have a real implementation
    // final logs = ref.watch(activityLogNotifierProvider);
    final List<ActivityLogModel> logs = [];

    return Scaffold(
      appBar: const CustomAppBar(title: "Activity Logs"),
      body: logs.isEmpty
          ? const Center(child: Text("No logs found"))
          : ListView.builder(
              itemCount: logs.length,
              padding: const EdgeInsets.all(12),
              itemBuilder: (context, index) {
                final log = logs[index];
                return Card(
                  margin: const EdgeInsets.only(bottom: 8),
                  elevation: 0,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                    side: BorderSide(color: Colors.grey.withValues(alpha: 0.1)),
                  ),
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: _getActionColor(log.action).withValues(alpha: 0.1),
                      child: Icon(_getActionIcon(log.action), color: _getActionColor(log.action), size: 20),
                    ),
                    title: RichText(
                      text: TextSpan(
                        style: const TextStyle(color: Colors.black, fontSize: 14),
                        children: [
                          TextSpan(text: log.adminName, style: const TextStyle(fontWeight: FontWeight.bold)),
                          TextSpan(text: " ${log.action} "),
                          TextSpan(text: log.targetId, style: const TextStyle(fontWeight: FontWeight.w500, color: Colors.blue)),
                        ],
                      ),
                    ),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const SizedBox(height: 4),
                        Text(log.details, style: const TextStyle(fontSize: 12)),
                        Text(
                          DateFormat('dd MMM yyyy, hh:mm a').format(log.timestamp),
                          style: TextStyle(color: Colors.grey[400], fontSize: 10),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
    );
  }

  IconData _getActionIcon(String action) {
    if (action.contains('Blocked')) return Icons.block_flipped;
    if (action.contains('Product')) return Icons.shopping_bag;
    if (action.contains('Order')) return Icons.receipt;
    return Icons.info_outline;
  }

  Color _getActionColor(String action) {
    if (action.contains('Added')) return Colors.green;
    if (action.contains('Deleted')) return Colors.red;
    if (action.contains('Blocked')) return Colors.orange;
    return Colors.blue;
  }
}
