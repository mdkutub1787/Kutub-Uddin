import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../admin/riverpod/activity_log_notifier.dart';
import '../../../widgets/custom_app_bar.dart';

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
      ref.read(activityLogNotifierProvider.notifier).loadLogs();
    });
  }

  @override
  Widget build(BuildContext context) {
    final logState = ref.watch(activityLogNotifierProvider);

    return Scaffold(
      appBar: const CustomAppBar(title: "Activity Logs"),
      body: logState.when(
        data: (logs) => logs.isEmpty
            ? const Center(child: Text("No logs found"))
            : RefreshIndicator(
                onRefresh: () => ref.read(activityLogNotifierProvider.notifier).loadLogs(),
                child: ListView.builder(
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
              ),
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (e, st) => Center(child: Text("Error: $e")),
      ),
    );
  }

  IconData _getActionIcon(String action) {
    String a = action.toLowerCase();
    if (a.contains('block') || a.contains('inactive')) return Icons.block_flipped;
    if (a.contains('product')) return Icons.shopping_bag;
    if (a.contains('order')) return Icons.receipt;
    if (a.contains('category')) return Icons.category;
    if (a.contains('added')) return Icons.add_circle_outline;
    if (a.contains('deleted')) return Icons.delete_outline;
    if (a.contains('updated')) return Icons.edit_note;
    return Icons.info_outline;
  }

  Color _getActionColor(String action) {
    String a = action.toLowerCase();
    if (a.contains('added') || a.contains('active')) return Colors.green;
    if (a.contains('deleted') || a.contains('block')) return Colors.red;
    if (a.contains('updated')) return Colors.orange;
    return Colors.blue;
  }
}
