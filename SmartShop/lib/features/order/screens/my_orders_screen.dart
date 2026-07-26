import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../../core/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/app_card.dart';
import '../../../widgets/empty_state_widget.dart';
import 'package:intl/intl.dart';
import 'order_tracking_screen.dart';

class MyOrdersScreen extends ConsumerStatefulWidget {
  const MyOrdersScreen({super.key});

  @override
  ConsumerState<MyOrdersScreen> createState() => _MyOrdersScreenState();
}

class _MyOrdersScreenState extends ConsumerState<MyOrdersScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final auth = ref.read(authNotifierProvider).value;
      if (auth != null) {
        ref.read(orderNotifierProvider.notifier).loadOrders();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final orderState = ref.watch(orderNotifierProvider);
    final primaryColor = Theme.of(context).primaryColor;
    final size = MediaQuery.of(context).size;

    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: CustomAppBar(
        title: AppStrings.myOrdersMenu.tr(),
      ),
      body: Stack(
        children: [
          // Decorative Background Elements
          Positioned(
            top: -size.height * 0.1,
            right: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.4,
              backgroundColor: primaryColor.withValues(alpha: 0.05),
            ),
          ),
          
          RefreshIndicator(
            onRefresh: () async {
              final auth = ref.read(authNotifierProvider).value;
              if (auth != null) {
                await ref.read(orderNotifierProvider.notifier).loadOrders();
              }
            },
            child: orderState.isLoading && (orderState.value?.isEmpty ?? true)
                ? const Center(child: CircularProgressIndicator())
                : (orderState.value?.isEmpty ?? true)
                    ? EmptyStateWidget(
                        icon: Icons.receipt_long_outlined,
                        title: AppStrings.noOrders.tr(),
                        subtitle: AppStrings.noOrdersMsg.tr(),
                        actionText: AppStrings.browseProducts.tr(),
                        onAction: () => ref.read(navigationNotifierProvider.notifier).setIndex(0),
                      )
                    : ListView.builder(
                        padding: const EdgeInsets.symmetric(vertical: 10),
                        physics: const AlwaysScrollableScrollPhysics(),
                        itemCount: orderState.value!.length,
                        itemBuilder: (context, index) {
                          final order = orderState.value![index];
                          return _buildPremiumOrderCard(context, order);
                        },
                      ),
          ),
        ],
      ),
    );
  }

  Widget _buildPremiumOrderCard(BuildContext context, dynamic order) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.04),
            blurRadius: 20,
            offset: const Offset(0, 10),
          ),
        ],
      ),
      child: InkWell(
        onTap: () => Navigator.pushNamed(context, AppRoutes.orderDetails, arguments: order),
        borderRadius: BorderRadius.circular(24),
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Row(
                    children: [
                      Container(
                        padding: const EdgeInsets.all(8),
                        decoration: BoxDecoration(
                          color: Theme.of(context).primaryColor.withValues(alpha: 0.1),
                          shape: BoxShape.circle,
                        ),
                        child: Icon(Icons.shopping_bag_outlined, color: Theme.of(context).primaryColor, size: 20),
                      ),
                      const SizedBox(width: 12),
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "${AppStrings.orderId.tr()} #${order.id.toString().substring(0, 8)}",
                            style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 14),
                          ),
                          Text(
                            DateFormat('dd MMM yyyy').format(order.date),
                            style: const TextStyle(color: Colors.grey, fontSize: 12, fontWeight: FontWeight.w500),
                          ),
                        ],
                      ),
                    ],
                  ),
                  _buildStatusBadge(order.status),
                ],
              ),
              const Padding(
                padding: EdgeInsets.symmetric(vertical: 16),
                child: Divider(height: 1, thickness: 1, color: Color(0xFFF0F0F0)),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(AppStrings.itemsCount.tr(args: [order.items.length.toString()]), style: const TextStyle(color: Colors.grey, fontSize: 12, fontWeight: FontWeight.bold)),
                      const SizedBox(height: 4),
                      Text(
                        "${AppStrings.currency.tr()} ${NumberFormat('#,##,###').format(order.totalAmount.toInt())}",
                        style: TextStyle(fontWeight: FontWeight.w900, fontSize: 18, color: Theme.of(context).primaryColor),
                      ),
                    ],
                  ),
                  GestureDetector(
                    onTap: () {
                      if (order.status == 'Assigned' || order.status == 'PickedUp' || order.status == 'OnTheWay') {
                        Navigator.push(context, MaterialPageRoute(builder: (_) => OrderTrackingScreen(order: order)));
                      } else {
                        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Tracking not available for this order yet.')));
                      }
                    },
                    child: Container(
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      decoration: BoxDecoration(
                        color: (order.status == 'Assigned' || order.status == 'PickedUp' || order.status == 'OnTheWay') 
                            ? Colors.black 
                            : Colors.grey[300],
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: Row(
                        children: [
                          Text("Track Order", style: TextStyle(color: (order.status == 'Assigned' || order.status == 'PickedUp' || order.status == 'OnTheWay') ? Colors.white : Colors.grey[600], fontSize: 12, fontWeight: FontWeight.bold)),
                          const SizedBox(width: 4),
                          Icon(Icons.arrow_forward_rounded, color: (order.status == 'Assigned' || order.status == 'PickedUp' || order.status == 'OnTheWay') ? Colors.white : Colors.grey[600], size: 14),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildStatusBadge(String status) {
    Color color = Colors.blue;
    String statusText = status;
    if (status == 'Pending') {
      color = Colors.orange;
      statusText = AppStrings.statusPending.tr();
    } else if (status == 'Delivered') {
      color = Colors.green;
      statusText = AppStrings.statusDelivered.tr();
    } else if (status == 'Cancelled') {
      color = Colors.red;
      statusText = AppStrings.statusCancelled.tr();
    } else if (status == 'Shipped') {
      color = Colors.purple;
      statusText = AppStrings.statusShipped.tr();
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Text(
        statusText,
        style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 11),
      ),
    );
  }
}
