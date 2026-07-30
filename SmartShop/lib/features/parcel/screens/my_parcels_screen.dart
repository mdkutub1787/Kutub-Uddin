import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../riverpod/parcel_notifier.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/custom_loading.dart';
import '../../../widgets/empty_state_widget.dart';

class MyParcelsScreen extends ConsumerWidget {
  const MyParcelsScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final settings = ref.watch(settingsProvider);
    final user = ref.watch(authNotifierProvider).value;
    
    if (user == null) {
      return const Scaffold(body: Center(child: Text("Please login to view parcels")));
    }

    final parcelsStream = ref.watch(userParcelsStreamProvider(user.uid));

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: const CustomAppBar(title: "My Parcels"),
      body: parcelsStream.when(
        data: (parcels) {
          if (parcels.isEmpty) {
            return const EmptyStateWidget(
              icon: Icons.local_shipping_outlined,
              title: "No Parcels Yet",
              subtitle: "You haven't requested any parcel deliveries.",
            );
          }
          
          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: parcels.length,
            itemBuilder: (context, index) {
              final parcel = parcels[index];
              return Card(
                elevation: 2,
                margin: const EdgeInsets.only(bottom: 16),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text("Tracking ID: ${parcel.id.substring(0, 8).toUpperCase()}", style: const TextStyle(fontWeight: FontWeight.bold)),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                            decoration: BoxDecoration(
                              color: _getStatusColor(parcel.status).withValues(alpha: 0.1),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text(
                              parcel.status,
                              style: TextStyle(color: _getStatusColor(parcel.status), fontWeight: FontWeight.bold, fontSize: 12),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 12),
                      Row(
                        children: [
                          const Icon(Icons.inventory_2_outlined, size: 20, color: Colors.grey),
                          const SizedBox(width: 8),
                          Expanded(child: Text(parcel.parcelType, style: const TextStyle(fontSize: 16))),
                          Text("৳${parcel.deliveryCharge}", style: TextStyle(fontWeight: FontWeight.w900, color: settings.primaryColor, fontSize: 16)),
                        ],
                      ),
                      const SizedBox(height: 12),
                      const Divider(),
                      const SizedBox(height: 8),
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Column(
                            children: [
                              const Icon(Icons.circle, size: 12, color: Colors.blue),
                              Container(height: 20, width: 2, color: Colors.grey[300]),
                              const Icon(Icons.location_on, size: 12, color: Colors.red),
                            ],
                          ),
                          const SizedBox(width: 12),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(parcel.pickupAddress, maxLines: 1, overflow: TextOverflow.ellipsis, style: const TextStyle(fontSize: 13)),
                                const SizedBox(height: 14),
                                Text(parcel.dropoffAddress, maxLines: 1, overflow: TextOverflow.ellipsis, style: const TextStyle(fontSize: 13)),
                              ],
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 12),
                      Row(
                        children: [
                          const Icon(Icons.calendar_month, size: 14, color: Colors.grey),
                          const SizedBox(width: 4),
                          Text(DateFormat('dd MMM yyyy, hh:mm a').format(parcel.createdAt), style: const TextStyle(color: Colors.grey, fontSize: 12)),
                        ],
                      ),
                    ],
                  ),
                ),
              );
            },
          );
        },
        loading: () => const Center(child: CustomLoading()),
        error: (err, st) => Center(child: Text("Error: $err", style: const TextStyle(color: Colors.red))),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => Navigator.pushNamed(context, '/create-parcel'),
        backgroundColor: settings.primaryColor,
        foregroundColor: Colors.white,
        icon: const Icon(Icons.add),
        label: const Text("Send Parcel", style: TextStyle(fontWeight: FontWeight.bold)),
      ),
    );
  }

  Color _getStatusColor(String status) {
    switch (status) {
      case 'Pending': return Colors.orange;
      case 'Confirmed': return Colors.blue;
      case 'Picked_Up': return Colors.purple;
      case 'In_Transit': return Colors.indigo;
      case 'Delivered': return Colors.green;
      case 'Cancelled': return Colors.red;
      default: return Colors.grey;
    }
  }
}
