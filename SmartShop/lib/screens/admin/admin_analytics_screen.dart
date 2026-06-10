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
          // StreamBuilder handles updates, but this allows manual trigger if needed
        },
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: const EdgeInsets.symmetric(vertical: 20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildSummarySection(context, shopId),
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

  Widget _buildSummarySection(BuildContext context, String? shopId) {
    return StreamBuilder<List<OrderModel>>(
      stream: shopId != null 
          ? OrderRepository().getOrdersByShop(shopId)
          : OrderRepository().getAllOrders(),
      builder: (context, snapshot) {
        double totalRevenue = 0;
        int successfulOrders = 0;
        
        if (snapshot.hasData) {
          for (var order in snapshot.data!) {
            if (order.status == 'Delivered' || order.status == 'Shipped') {
              totalRevenue += order.totalAmount;
              successfulOrders++;
            }
          }
        }

        return Column(
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: _bigStatCard(
                context,
                AppStrings.totalRevenue.tr(),
                "৳${totalRevenue.toStringAsFixed(2)}",
                Icons.account_balance_wallet_rounded,
                Colors.green,
              ),
            ),
            const SizedBox(height: 16),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: Row(
                children: [
                  Expanded(
                    child: _smallStatCard(
                      context,
                      AppStrings.successfulOrders.tr(),
                      successfulOrders.toString(),
                      Icons.check_circle_rounded,
                      Colors.blue,
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: StreamBuilder<List<ProductModel>>(
                      stream: shopId != null 
                          ? ProductRepository().getProductsByShop(shopId)
                          : ProductRepository().getAllProducts(),
                      builder: (context, pSnapshot) {
                        int lowStock = pSnapshot.hasData 
                            ? pSnapshot.data!.where((p) => p.stock < 10).length 
                            : 0;
                        return _smallStatCard(
                          context,
                          AppStrings.lowStockAlert.tr(),
                          lowStock.toString(),
                          Icons.report_problem_rounded,
                          Colors.orange,
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
          ],
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
        if (snapshot.data!.isEmpty) {
          return const Center(child: Padding(padding: EdgeInsets.all(20), child: Text("No data found")));
        }

        return ListView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          itemCount: snapshot.data!.length > 5 ? 5 : snapshot.data!.length,
          itemBuilder: (context, index) {
            final order = snapshot.data![index];
            return ListTile(
              title: Text(order.userName),
              subtitle: Text(DateFormat('dd MMM yyyy').format(order.date)),
              trailing: Text("৳${order.totalAmount}"),
            );
          },
        );
      },
    );
  }

  Widget _bigStatCard(BuildContext context, String title, String value, IconData icon, Color color) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withValues(alpha: 0.2)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, color: color, size: 30),
          const SizedBox(height: 15),
          Text(title, style: TextStyle(color: Colors.grey[600], fontSize: 14)),
          const SizedBox(height: 5),
          Text(value, style: TextStyle(color: color, fontSize: 28, fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }

  Widget _smallStatCard(BuildContext context, String title, String value, IconData icon, Color color) {
    return Container(
      padding: const EdgeInsets.all(15),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 10)],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, color: color, size: 24),
          const SizedBox(height: 10),
          Text(title, style: TextStyle(color: Colors.grey[600], fontSize: 12), maxLines: 1, overflow: TextOverflow.ellipsis),
          const SizedBox(height: 5),
          Text(value, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }
}
