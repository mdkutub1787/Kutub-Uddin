import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:provider/provider.dart';
import '../../models/order_model.dart';
import '../../models/product_model.dart';
import '../../repositories/order_repository.dart';
import '../../repositories/product_repository.dart';
import '../../utils/constants/app_strings.dart';
import '../../widgets/custom_app_bar.dart';
import '../../view_models/auth_view_model.dart';

class AdminAnalyticsScreen extends StatelessWidget { 
  const AdminAnalyticsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final authVM = context.watch<AuthViewModel>();
    final shopId = authVM.user?.shopId;

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: CustomAppBar(
        title: AppStrings.analyticsTitle.tr(),
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          // StreamBuilder handles updates
        },
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: const EdgeInsets.symmetric(vertical: 20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildDetailedSummary(context, shopId),
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 30, 20, 15),
                child: Text(
                  AppStrings.recentPerformance.tr(),
                  style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
              _buildDetailedList(context, shopId),
              const SizedBox(height: 50),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildDetailedSummary(BuildContext context, String? shopId) {
    return StreamBuilder<List<OrderModel>>(
      stream: shopId != null 
          ? OrderRepository().getOrdersByShop(shopId)
          : OrderRepository().getAllOrders(),
      builder: (context, snapshot) {
        double totalRev = 0;
        double onlineRev = 0;
        double posRev = 0;
        int onlineCount = 0;
        int posCount = 0;
        
        if (snapshot.hasData) {
          for (var order in snapshot.data!) {
            if (order.status == 'Delivered' || order.status == 'Shipped') {
              totalRev += order.totalAmount;
              if (order.orderType == 'pos') {
                posRev += order.totalAmount;
                posCount++;
              } else {
                onlineRev += order.totalAmount;
                onlineCount++;
              }
            }
          }
        }

        return Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20),
          child: Column(
            children: [
              _statCard(context, "Total Sales", "৳${totalRev.toStringAsFixed(0)}", Icons.account_balance_wallet_rounded, Colors.indigo, isLarge: true),
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(child: _statCard(context, "Online Sales", "৳${onlineRev.toStringAsFixed(0)}", Icons.language_rounded, Colors.blue)),
                  const SizedBox(width: 16),
                  Expanded(child: _statCard(context, "POS Sales", "৳${posRev.toStringAsFixed(0)}", Icons.point_of_sale_rounded, Colors.teal)),
                ],
              ),
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(child: _statCard(context, "Orders ($onlineCount)", "Online", Icons.shopping_bag_outlined, Colors.orange)),
                  const SizedBox(width: 16),
                  Expanded(child: _statCard(context, "Sales ($posCount)", "Store", Icons.storefront_rounded, Colors.purple)),
                ],
              ),
            ],
          ),
        );
      },
    );
  }

  Widget _buildDetailedList(BuildContext context, String? shopId) {
    return StreamBuilder<List<OrderModel>>(
      stream: shopId != null 
          ? OrderRepository().getOrdersByShop(shopId)
          : OrderRepository().getAllOrders(),
      builder: (context, snapshot) {
        if (!snapshot.hasData) return const Center(child: CircularProgressIndicator());
        if (snapshot.data!.isEmpty) return const Center(child: Text("No transactions yet"));

        final recentOrders = snapshot.data!.take(10).toList();

        return ListView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          itemCount: recentOrders.length,
          itemBuilder: (context, index) {
            final order = recentOrders[index];
            bool isPos = order.orderType == 'pos';
            return Container(
              margin: const EdgeInsets.symmetric(horizontal: 20, vertical: 6),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(15),
                border: Border.all(color: Colors.grey[200]!),
              ),
              child: ListTile(
                leading: CircleAvatar(
                  backgroundColor: isPos ? Colors.teal.withValues(alpha: 0.1) : Colors.blue.withValues(alpha: 0.1),
                  child: Icon(isPos ? Icons.storefront_rounded : Icons.language_rounded, size: 18, color: isPos ? Colors.teal : Colors.blue),
                ),
                title: Text(order.userName, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                subtitle: Text(DateFormat('dd MMM, hh:mm a').format(order.date), style: const TextStyle(fontSize: 11)),
                trailing: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Text("৳${order.totalAmount.toInt()}", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15)),
                    Text(isPos ? "POS" : "ONLINE", style: TextStyle(fontSize: 9, fontWeight: FontWeight.w900, color: isPos ? Colors.teal : Colors.blue)),
                  ],
                ),
              ),
            );
          },
        );
      },
    );
  }

  Widget _statCard(BuildContext context, String title, String value, IconData icon, Color color, {bool isLarge = false}) {
    return Container(
      padding: EdgeInsets.all(isLarge ? 24 : 16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.03), blurRadius: 10, offset: const Offset(0, 4))],
        border: Border.all(color: color.withValues(alpha: 0.1), width: 2),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(color: color.withValues(alpha: 0.1), shape: BoxShape.circle),
                child: Icon(icon, size: isLarge ? 28 : 20, color: color),
              ),
              if (isLarge) Icon(Icons.trending_up_rounded, color: Colors.green[400], size: 20),
            ],
          ),
          SizedBox(height: isLarge ? 20 : 12),
          Text(title, style: TextStyle(color: Colors.grey[600], fontSize: isLarge ? 14 : 11, fontWeight: FontWeight.bold)),
          const SizedBox(height: 4),
          Text(value, style: TextStyle(color: color, fontSize: isLarge ? 32 : 18, fontWeight: FontWeight.w900)),
        ],
      ),
    );
  }
}
