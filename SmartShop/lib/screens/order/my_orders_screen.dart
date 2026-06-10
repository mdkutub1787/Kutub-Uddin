import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:smart_shop/view_models/order_view_model.dart';
import 'package:smart_shop/view_models/auth_view_model.dart';
import 'package:smart_shop/view_models/navigation_view_model.dart';
import 'package:smart_shop/utils/constants/app_strings.dart';
import 'package:smart_shop/repositories/order_repository.dart';
import 'package:smart_shop/routes/app_routes.dart';
import 'package:smart_shop/widgets/custom_app_bar.dart';
import 'package:smart_shop/widgets/app_card.dart';
import 'package:smart_shop/widgets/empty_state_widget.dart';

class MyOrdersScreen extends StatefulWidget {
  const MyOrdersScreen({super.key});

  @override
  State<MyOrdersScreen> createState() => _MyOrdersScreenState();
}

class _MyOrdersScreenState extends State<MyOrdersScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final auth = context.read<AuthViewModel>();
      if (auth.user != null) {
        context.read<OrderViewModel>().fetchUserOrders(auth.user!.uid);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final orderViewModel = context.watch<OrderViewModel>();
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
          Positioned(
            bottom: -size.height * 0.1,
            left: -size.width * 0.2,
            child: CircleAvatar(
              radius: size.width * 0.3,
              backgroundColor: primaryColor.withValues(alpha: 0.05),
            ),
          ),
          
          RefreshIndicator(
            onRefresh: () async {
              final auth = context.read<AuthViewModel>();
              if (auth.user != null) {
                await context.read<OrderViewModel>().refreshUserOrders(auth.user!.uid);
              }
            },
            child: orderViewModel.isLoading && orderViewModel.userOrders.isEmpty
                ? const Center(child: CircularProgressIndicator())
                : orderViewModel.userOrders.isEmpty
                    ? EmptyStateWidget(
                        icon: Icons.receipt_long_outlined,
                        title: AppStrings.noOrders.tr(),
                        subtitle: AppStrings.noOrdersMsg.tr(),
                        actionText: AppStrings.browseProducts.tr(),
                        onAction: () => context.read<NavigationViewModel>().setIndex(0),
                      )
                    : ListView.builder(
                        padding: const EdgeInsets.only(top: 8, bottom: 20),
                        physics: const AlwaysScrollableScrollPhysics(),
                        itemCount: orderViewModel.userOrders.length,
                        itemBuilder: (context, index) {
                          final order = orderViewModel.userOrders[index];
                          return AppCard(
                            margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                            onTap: () {
                              Navigator.pushNamed(
                                context,
                                AppRoutes.orderDetails,
                                arguments: order,
                              );
                            },
                            child: Padding(
                              padding: const EdgeInsets.all(16.0),
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
                                              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                                              maxLines: 1,
                                              overflow: TextOverflow.ellipsis,
                                            ),
                                            const SizedBox(height: 4),
                                            Text(
                                              DateFormat('dd MMM yyyy, hh:mm a').format(order.date),
                                              style: const TextStyle(color: Colors.grey, fontSize: 12),
                                            ),
                                          ],
                                        ),
                                      ),
                                      _buildStatusBadge(order.status),
                                    ],
                                  ),
                                  const Divider(height: 24),
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    children: [
                                      Text(
                                        AppStrings.itemsCount.tr(args: [order.items.length.toString()]),
                                        style: const TextStyle(fontWeight: FontWeight.w500),
                                      ),
                                      Text(
                                        "${AppStrings.total.tr()}: ${AppStrings.currency.tr()}${order.totalAmount}",
                                        style: TextStyle(
                                          fontWeight: FontWeight.bold,
                                          fontSize: 16,
                                          color: Theme.of(context).primaryColor,
                                        ),
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: 12),
                                  Row(
                                    children: [
                                      const Icon(Icons.info_outline, size: 16, color: Colors.blueGrey),
                                      const SizedBox(width: 4),
                                      Text(
                                        AppStrings.tapToViewDetails.tr(),
                                        style: const TextStyle(fontSize: 12, color: Colors.blueGrey),
                                      ),
                                      const Spacer(),
                                      Icon(Icons.arrow_forward_ios, size: 14, color: Colors.grey[400]),
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
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Text(
        statusText,
        style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 12),
      ),
    );
  }
}
