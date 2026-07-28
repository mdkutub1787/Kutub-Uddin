import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../../../models/coupon_model.dart';
import '../../../../core/constants/constants.dart';
import '../../../../core/providers.dart';
import '../../../offers/riverpod/coupon_notifier.dart';
import '../../../../widgets/custom_app_bar.dart';
import '../../../../core/riverpod/settings_notifier.dart';

class AdminCouponScreen extends ConsumerStatefulWidget {
  const AdminCouponScreen({super.key});

  @override
  ConsumerState<AdminCouponScreen> createState() => _AdminCouponScreenState();
}

class _AdminCouponScreenState extends ConsumerState<AdminCouponScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(couponNotifierProvider.notifier).loadCoupons();
    });
  }

  @override
  Widget build(BuildContext context) {
    final couponState = ref.watch(couponNotifierProvider);
    final settings = ref.watch(settingsProvider);
    final currency = settings.currencySymbol;

    return Scaffold(
      appBar: const CustomAppBar(title: "Manage Coupons"),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddDialog(),
        child: const Icon(Icons.add),
      ),
      body: couponState.when(
        data: (coupons) => ListView.builder(
          itemCount: coupons.length,
          padding: const EdgeInsets.all(16),
          itemBuilder: (context, index) {
            final coupon = coupons[index];
            return Card(
              margin: const EdgeInsets.only(bottom: 12),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
              child: ListTile(
                title: Text(coupon.code, style: const TextStyle(fontWeight: FontWeight.bold)),
                subtitle: Text("$currency${coupon.discountValue.toInt()} - ${coupon.type.toString().split('.').last}"),
                trailing: IconButton(
                  icon: const Icon(Icons.delete_outline, color: Colors.red),
                  onPressed: () => _deleteCoupon(coupon.id),
                ),
              ),
            );
          },
        ),
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (e, st) => Center(child: Text("Error: $e")),
      ),
    );
  }

  void _showAddDialog() {
    final codeCtrl = TextEditingController();
    final discCtrl = TextEditingController();
    
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Add New Coupon"),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(controller: codeCtrl, decoration: const InputDecoration(labelText: "Code")),
            TextField(controller: discCtrl, decoration: const InputDecoration(labelText: "Discount Value"), keyboardType: TextInputType.number),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text("CANCEL")),
          ElevatedButton(onPressed: () async {
            final supabase = ref.read(supabaseClientProvider);
            await supabase.from(AppConstants.couponsTable).insert({
              'code': codeCtrl.text.toUpperCase(),
              'title': 'Special Offer',
              'description': 'Enjoy a special discount',
              'discountValue': double.parse(discCtrl.text),
              'type': 'percentage',
              'minPurchase': 500,
              'expiryDate': DateTime.now().add(const Duration(days: 30)).toIso8601String(),
              'isActive': true,
            });
            ref.read(couponNotifierProvider.notifier).loadCoupons();
            if (mounted) Navigator.pop(ctx);
          }, child: const Text("SAVE")),
        ],
      ),
    );
  }

  void _deleteCoupon(String id) async {
    final supabase = ref.read(supabaseClientProvider);
    await supabase.from(AppConstants.couponsTable).delete().eq('id', id);
    ref.read(couponNotifierProvider.notifier).loadCoupons();
  }
}
