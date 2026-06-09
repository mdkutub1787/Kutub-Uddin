import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../models/order_model.dart';
import '../../models/product_model.dart';
import '../../repositories/order_repository.dart';
import '../../repositories/product_repository.dart';
import '../../utils/constants/app_strings.dart';

class AdminAnalyticsScreen extends StatelessWidget {
  const AdminAnalyticsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: Text(AppStrings.analyticsTitle.tr(), style: const TextStyle(fontWeight: FontWeight.bold)),
        centerTitle: true,
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
              _buildSummarySection(context),
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 30, 20, 15),
                child: Text(
                  AppStrings.recentPerformance.tr(),
                  style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
              _buildDetailedList(context),
              const SizedBox(height: 50),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildSummarySection(BuildContext context) {
    return StreamBuilder<List<OrderModel>>(
      stream: OrderRepository().getAllOrders(),
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
                      stream: ProductRepository().getFeaturedProducts(),
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

  Widget _buildDetailedList(BuildContext context) {
    return StreamBuilder<List<OrderModel>>(
      stream: OrderRepository().getAllOrders(),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        }
        
        // Group sales by date
        Map<String, double> dateWise = {};
        if (snapshot.hasData) {
          for (var order in snapshot.data!) {
            if (order.status != 'Cancelled') {
              String d = DateFormat('dd MMM yyyy').format(order.date);
              dateWise[d] = (dateWise[d] ?? 0) + order.totalAmount;
            }
          }
        }

        if (dateWise.isEmpty) {
          return Center(
            child: Padding(
              padding: const EdgeInsets.all(40),
              child: Text(AppStrings.noProducts.tr()),
            ),
          );
        }

        return ListView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          padding: const EdgeInsets.symmetric(horizontal: 20),
          itemCount: dateWise.length,
          itemBuilder: (context, index) {
            String date = dateWise.keys.elementAt(index);
            double amount = dateWise[date]!;
            
            return Container(
              margin: const EdgeInsets.only(bottom: 12),
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(15),
                border: Border.all(color: Colors.grey.withValues(alpha: 0.1)),
              ),
              child: Row(
                children: [
                  Container(
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      color: Colors.green.withValues(alpha: 0.1),
                      shape: BoxShape.circle,
                    ),
                    child: const Icon(Icons.calendar_today_rounded, color: Colors.green, size: 20),
                  ),
                  const SizedBox(width: 15),
                  Expanded(
                    child: Text(
                      date,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                    ),
                  ),
                  Text(
                    "৳${amount.toStringAsFixed(2)}",
                    style: const TextStyle(
                      fontWeight: FontWeight.w900,
                      fontSize: 16,
                      color: Colors.green,
                    ),
                  ),
                ],
              ),
            );
          },
        );
      },
    );
  }

  Widget _bigStatCard(BuildContext context, String title, String value, IconData icon, Color color) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(24),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: [
          BoxShadow(
            color: color.withValues(alpha: 0.08),
            blurRadius: 20,
            offset: const Offset(0, 8),
          )
        ],
      ),
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: color.withValues(alpha: 0.1),
              shape: BoxShape.circle,
            ),
            child: Icon(icon, color: color, size: 32),
          ),
          const SizedBox(height: 16),
          Text(
            title,
            style: TextStyle(color: Colors.grey[600], fontSize: 14, fontWeight: FontWeight.w500),
          ),
          const SizedBox(height: 8),
          Text(
            value,
            style: const TextStyle(fontSize: 28, fontWeight: FontWeight.w900),
          ),
        ],
      ),
    );
  }

  Widget _smallStatCard(BuildContext context, String title, String value, IconData icon, Color color) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: [
          BoxShadow(
            color: color.withValues(alpha: 0.08),
            blurRadius: 20,
            offset: const Offset(0, 8),
          )
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, color: color, size: 28),
          const SizedBox(height: 16),
          Text(
            title,
            style: TextStyle(color: Colors.grey[600], fontSize: 12, fontWeight: FontWeight.w500),
          ),
          const SizedBox(height: 4),
          Text(
            value,
            style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900),
          ),
        ],
      ),
    );
  }
}
