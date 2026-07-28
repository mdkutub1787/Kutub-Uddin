import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:fl_chart/fl_chart.dart';
import '../../order/models/order_model.dart';
import '../../order/riverpod/order_notifier.dart';
import '../../../core/app_strings.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';

class AdminAnalyticsScreen extends ConsumerWidget { 
  const AdminAnalyticsScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final authState = ref.watch(authNotifierProvider);
    final shopId = authState.value?.shopId;
    final settings = ref.watch(settingsProvider);
    final currency = settings.currencySymbol;

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: CustomAppBar(
        title: AppStrings.analyticsTitle.tr(),
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          ref.read(orderNotifierProvider.notifier).loadOrders();
        },
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: const EdgeInsets.symmetric(vertical: 20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildDetailedSummary(context, ref, shopId, currency),
              const SizedBox(height: 24),
              _buildSalesChart(context, ref),
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 30, 20, 15),
                child: Text(
                  AppStrings.recentPerformance.tr(),
                  style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
              _buildDetailedList(context, ref, shopId, currency),
              const SizedBox(height: 50),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildSalesChart(BuildContext context, WidgetRef ref) {
    final orderState = ref.watch(orderNotifierProvider);
    final orders = orderState.value ?? [];
    
    Map<String, double> dailySales = {};
    final now = DateTime.now();
    for (int i = 6; i >= 0; i--) {
      final date = now.subtract(Duration(days: i));
      final key = DateFormat('dd/MM').format(date);
      dailySales[key] = 0;
    }

    for (var order in orders) {
      if (order.status == 'Delivered') {
        final key = DateFormat('dd/MM').format(order.date);
        if (dailySales.containsKey(key)) {
          dailySales[key] = dailySales[key]! + order.totalAmount;
        }
      }
    }

    List<BarChartGroupData> barGroups = [];
    int index = 0;
    dailySales.forEach((key, value) {
      barGroups.add(
        BarChartGroupData(
          x: index,
          barRods: [
            BarChartRodData(
              toY: value,
              color: Theme.of(context).primaryColor,
              width: 16,
              borderRadius: BorderRadius.circular(4),
            ),
          ],
        ),
      );
      index++;
    });

    return Container(
      height: 250,
      margin: const EdgeInsets.symmetric(horizontal: 20),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 20)],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text("Last 7 Days Sales", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
          const SizedBox(height: 20),
          Expanded(
            child: BarChart(
              BarChartData(
                barGroups: barGroups,
                borderData: FlBorderData(show: false),
                gridData: const FlGridData(show: false),
                titlesData: FlTitlesData(
                  leftTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
                  topTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
                  rightTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
                  bottomTitles: AxisTitles(
                    sideTitles: SideTitles(
                      showTitles: true,
                      getTitlesWidget: (value, meta) {
                        if (value.toInt() < 0 || value.toInt() >= dailySales.length) return const SizedBox();
                        return Padding(
                          padding: const EdgeInsets.only(top: 8.0),
                          child: Text(dailySales.keys.elementAt(value.toInt()), style: const TextStyle(fontSize: 10, color: Colors.grey)),
                        );
                      },
                    ),
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDetailedSummary(BuildContext context, WidgetRef ref, String? shopId, String currency) {
    final orderState = ref.watch(orderNotifierProvider);
    
    if (orderState.isLoading) {
      return const Center(child: CircularProgressIndicator());
    }
    
    final allOrders = orderState.value ?? [];
    final orders = allOrders;

    double totalRev = 0;
    double onlineRev = 0;
    double posRev = 0;
    int onlineCount = 0;
    int posCount = 0;
    
    for (var order in orders) {
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

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Column(
        children: [
          _statCard(context, "Total Sales", "$currency${totalRev.toStringAsFixed(0)}", Icons.account_balance_wallet_rounded, Colors.indigo, isLarge: true),
          const SizedBox(height: 16),
          Row(
            children: [
              Expanded(child: _statCard(context, "Online Sales", "$currency${onlineRev.toStringAsFixed(0)}", Icons.language_rounded, Colors.blue)),
              const SizedBox(width: 16),
              Expanded(child: _statCard(context, "POS Sales", "$currency${posRev.toStringAsFixed(0)}", Icons.point_of_sale_rounded, Colors.teal)),
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
  }

  Widget _buildDetailedList(BuildContext context, WidgetRef ref, String? shopId, String currency) {
    final orderState = ref.watch(orderNotifierProvider);
    
    if (orderState.isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    final allOrders = orderState.value ?? [];
    if (allOrders.isEmpty) return const Center(child: Text("No transactions yet"));

    final recentOrders = allOrders.take(10).toList();

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
                Text("$currency${order.totalAmount.toInt()}", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15)),
                Text(isPos ? "POS" : "ONLINE", style: TextStyle(fontSize: 9, fontWeight: FontWeight.w900, color: isPos ? Colors.teal : Colors.blue)),
              ],
            ),
          ),
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
