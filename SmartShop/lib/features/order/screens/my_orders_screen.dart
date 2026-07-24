import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/navigation_notifier.dart';
import '../../../utils/constants/app_strings.dart';
import '../../../routes/app_routes.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/app_card.dart';
import '../../../widgets/empty_state_widget.dart';
import 'package:intl/intl.dart';

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
                          return AppCard(
                            margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                            borderRadius: 16,
                            onTap: () {
                              Navigator.pushNamed(
                                context,
                                AppRoutes.orderDetails,
                                arguments: order,
                              );
                            },
                            child: Padding(
                              padding: const EdgeInsets.all(12.0),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    children: [
                                      Expanded(
                                        child: Column(
                                          crossAxisAlignment: CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              "${AppStrings.orderId.tr()}: ${order.id}",
                                              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                                              maxLines: 1,
                                              overflow: TextOverflow.ellipsis,
                                            ),
                                            Text(
                                              DateFormat('dd MMM yyyy, hh:mm a').format(order.date),
                                              style: const TextStyle(color: Colors.grey, fontSize: 11),
                                            ),
                                          ],
                                        ),
                                      ),
                                      _buildStatusBadge(order.status),
                                    ],
                                  ),
                                  const Divider(height: 20),
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    children: [
                                      Text(
                                        AppStrings.itemsCount.tr(args: [order.items.length.toString()]),
                                        style: const TextStyle(fontWeight: FontWeight.w500, fontSize: 14),
                                      ),
                                      Text(
                                        "${AppStrings.total.tr()}: ${AppStrings.currency.tr()}${order.totalAmount}",
                                        style: TextStyle(
                                          fontWeight: FontWeight.bold,
                                          fontSize: 15,
                                          color: Theme.of(context).primaryColor,
                                        ),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                          );
                        },
                      ),
          ),
        ],
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
