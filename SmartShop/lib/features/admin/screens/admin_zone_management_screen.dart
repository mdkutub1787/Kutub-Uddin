import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../delivery/riverpod/zone_notifier.dart';
import '../../delivery/models/delivery_zone_model.dart';
import 'package:smart_shop/widgets/custom_loading.dart';
import '../../../core/riverpod/settings_notifier.dart';

class AdminZoneManagementScreen extends ConsumerStatefulWidget {
  const AdminZoneManagementScreen({super.key});

  @override
  ConsumerState<AdminZoneManagementScreen> createState() => _AdminZoneManagementScreenState();
}

class _AdminZoneManagementScreenState extends ConsumerState<AdminZoneManagementScreen> {
  void _showAddEditZoneDialog({DeliveryZoneModel? zone}) {
    final isEditing = zone != null;
    final nameController = TextEditingController(text: zone?.zoneName ?? '');
    final chargeController = TextEditingController(text: zone?.baseDeliveryCharge.toString() ?? '');
    final descriptionController = TextEditingController(text: zone?.description ?? '');
    bool isActive = zone?.isActive ?? true;

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (context) {
        return StatefulBuilder(
          builder: (context, setState) {
            return Container(
              padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom, left: 20, right: 20, top: 20),
              decoration: const BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.vertical(top: Radius.circular(30)),
              ),
              child: SingleChildScrollView(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Container(
                      width: 50,
                      height: 5,
                      decoration: BoxDecoration(color: Colors.grey[300], borderRadius: BorderRadius.circular(10)),
                    ),
                    const SizedBox(height: 20),
                    Text(isEditing ? 'Edit Delivery Zone' : 'Create New Zone', style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900)),
                    const SizedBox(height: 25),
                    TextField(
                      controller: nameController,
                      decoration: InputDecoration(
                        labelText: 'Zone Name (e.g. Dhanmondi)', 
                        prefixIcon: const Icon(Icons.location_city_rounded),
                        filled: true,
                        fillColor: Colors.grey[50],
                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(16), borderSide: BorderSide.none),
                      ),
                    ),
                    const SizedBox(height: 15),
                    TextField(
                      controller: chargeController,
                      keyboardType: TextInputType.number,
                      decoration: InputDecoration(
                        labelText: 'Base Delivery Charge', 
                        prefixIcon: const Icon(Icons.monetization_on_rounded),
                        filled: true,
                        fillColor: Colors.grey[50],
                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(16), borderSide: BorderSide.none),
                      ),
                    ),
                    const SizedBox(height: 15),
                    TextField(
                      controller: descriptionController,
                      decoration: InputDecoration(
                        labelText: 'Description (Optional)', 
                        prefixIcon: const Icon(Icons.description_rounded),
                        filled: true,
                        fillColor: Colors.grey[50],
                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(16), borderSide: BorderSide.none),
                      ),
                    ),
                    const SizedBox(height: 15),
                    Container(
                      decoration: BoxDecoration(
                        color: isActive ? Colors.green.withOpacity(0.1) : Colors.red.withOpacity(0.1),
                        borderRadius: BorderRadius.circular(16),
                      ),
                      child: Material(
                        color: Colors.transparent,
                        child: SwitchListTile(
                          title: Text(isActive ? 'Status: Active' : 'Status: Inactive', style: TextStyle(fontWeight: FontWeight.bold, color: isActive ? Colors.green : Colors.red)),
                          value: isActive,
                          activeColor: Colors.green,
                          onChanged: (val) {
                            setState(() => isActive = val);
                          },
                        ),
                      ),
                    ),
                    const SizedBox(height: 30),
                    SizedBox(
                      width: double.infinity,
                      height: 55,
                      child: ElevatedButton(
                        onPressed: () async {
                          if (nameController.text.isEmpty || chargeController.text.isEmpty) return;
                          final newZone = DeliveryZoneModel(
                            id: isEditing ? zone!.id : '',
                            zoneName: nameController.text,
                            baseDeliveryCharge: double.tryParse(chargeController.text) ?? 0,
                            description: descriptionController.text,
                            isActive: isActive,
                          );
                          final notifier = ref.read(zoneNotifierProvider.notifier);
                          if (isEditing) {
                            await notifier.updateZone(newZone);
                          } else {
                            await notifier.createZone(newZone);
                          }
                          if (context.mounted) Navigator.pop(context);
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.blue[700],
                          foregroundColor: Colors.white,
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                          elevation: 4,
                        ),
                        child: Text(isEditing ? 'UPDATE ZONE' : 'SAVE ZONE', style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16)),
                      ),
                    ),
                    const SizedBox(height: 30),
                  ],
                ),
              ),
            );
          },
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final settings = ref.watch(settingsProvider);
    final zonesState = ref.watch(zoneNotifierProvider);

    return Scaffold(
      backgroundColor: Colors.grey[50],
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: 120,
            floating: true,
            pinned: true,
            flexibleSpace: FlexibleSpaceBar(
              titlePadding: const EdgeInsets.only(left: 20, bottom: 16),
              title: const Text("Manage Zones", style: TextStyle(fontWeight: FontWeight.w900, color: Colors.black87, fontSize: 20)),
              background: Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    colors: [Colors.blue[50]!, Colors.white],
                    begin: Alignment.topCenter,
                    end: Alignment.bottomCenter,
                  ),
                ),
              ),
            ),
          ),
          zonesState.when(
            data: (zones) {
              if (zones.isEmpty) {
                return SliverFillRemaining(
                  child: Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.map_rounded, size: 80, color: Colors.grey[300]),
                        const SizedBox(height: 16),
                        const Text("No Delivery Zones Yet", style: TextStyle(color: Colors.grey, fontSize: 18, fontWeight: FontWeight.bold)),
                        const SizedBox(height: 8),
                        const Text("Tap the + button to create a new zone.", style: TextStyle(color: Colors.grey)),
                      ],
                    ),
                  ),
                );
              }
              return SliverPadding(
                padding: const EdgeInsets.all(16),
                sliver: SliverList(
                  delegate: SliverChildBuilderDelegate(
                    (context, index) {
                      final zone = zones[index];
                      return Container(
                        margin: const EdgeInsets.only(bottom: 16),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(20),
                          boxShadow: [
                            BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 15, offset: const Offset(0, 5))
                          ],
                        ),
                        child: Material(
                          color: Colors.transparent,
                          child: InkWell(
                            borderRadius: BorderRadius.circular(20),
                            onTap: () => _showAddEditZoneDialog(zone: zone),
                            child: Padding(
                              padding: const EdgeInsets.all(20),
                              child: Row(
                                children: [
                                  Container(
                                    padding: const EdgeInsets.all(12),
                                    decoration: BoxDecoration(
                                      color: zone.isActive ? Colors.blue[50] : Colors.red[50],
                                      shape: BoxShape.circle,
                                    ),
                                    child: Icon(Icons.location_on_rounded, color: zone.isActive ? Colors.blue[700] : Colors.red[400]),
                                  ),
                                  const SizedBox(width: 16),
                                  Expanded(
                                    child: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        Text(zone.zoneName, style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 18)),
                                        const SizedBox(height: 4),
                                        Text('Base Charge: ৳${zone.baseDeliveryCharge}', style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold)),
                                        if (zone.description != null && zone.description!.isNotEmpty) ...[
                                          const SizedBox(height: 4),
                                          Text(zone.description!, style: const TextStyle(color: Colors.grey, fontSize: 13), maxLines: 1, overflow: TextOverflow.ellipsis),
                                        ],
                                      ],
                                    ),
                                  ),
                                  Container(
                                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                                    decoration: BoxDecoration(
                                      color: zone.isActive ? Colors.green[50] : Colors.red[50],
                                      borderRadius: BorderRadius.circular(20),
                                    ),
                                    child: Text(zone.isActive ? 'Active' : 'Inactive', style: TextStyle(color: zone.isActive ? Colors.green[700] : Colors.red[700], fontSize: 12, fontWeight: FontWeight.bold)),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                      );
                    },
                    childCount: zones.length,
                  ),
                ),
              );
            },
            loading: () => const SliverFillRemaining(child: Center(child: CustomLoading())),
            error: (err, stack) => SliverFillRemaining(child: Center(child: Text("Failed to load zones\n$err", style: TextStyle(color: Colors.red[300])))),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _showAddEditZoneDialog(),
        icon: const Icon(Icons.add_location_alt_rounded),
        label: const Text('Add Zone', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.blue[700],
        foregroundColor: Colors.white,
        elevation: 4,
      ),
    );
  }
}
