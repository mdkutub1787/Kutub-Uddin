import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../../core/app_strings.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../user/riverpod/user_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../widgets/custom_app_bar.dart';
import '../../../widgets/custom_shimmer.dart';
import 'admin_user_detail_screen.dart';

class AdminUserListScreen extends ConsumerStatefulWidget {
  const AdminUserListScreen({super.key});

  @override
  ConsumerState<AdminUserListScreen> createState() => _AdminUserListScreenState();
}

class _AdminUserListScreenState extends ConsumerState<AdminUserListScreen> {
  final TextEditingController _searchController = TextEditingController();
  String _searchQuery = "";

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ref.read(userNotifierProvider.notifier).loadUsers();
    });
  }

  @override
  Widget build(BuildContext context) {
    final userState = ref.watch(userNotifierProvider);
    final settings = ref.watch(settingsProvider);
    final currentUser = ref.watch(authNotifierProvider).value;
    final isSuperAdmin = currentUser?.role == 'super_admin';

    final users = userState.value ?? [];
    
    final filteredUsers = users.where((user) {
      if (!isSuperAdmin && user.role == 'super_admin') return false;
      return user.name.toLowerCase().contains(_searchQuery.toLowerCase()) ||
             user.email.toLowerCase().contains(_searchQuery.toLowerCase()) ||
             user.phoneNumber.contains(_searchQuery) ||
             (user.shopName?.toLowerCase().contains(_searchQuery.toLowerCase()) ?? false);
    }).toList();

    return Scaffold(
      appBar: CustomAppBar(title: AppStrings.users.tr()),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
            child: TextField(
              controller: _searchController,
              onChanged: (v) => setState(() => _searchQuery = v),
              decoration: InputDecoration(
                hintText: "Search name, email or phone...",
                prefixIcon: const Icon(Icons.search, size: 20),
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
                filled: true,
                fillColor: Colors.grey[100],
              ),
            ),
          ),
          Expanded(
            child: userState.isLoading
                ? const ListShimmer()
                : filteredUsers.isEmpty
                    ? Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(Icons.people_outline, size: 64, color: Colors.grey[300]),
                            const SizedBox(height: 16),
                            Text("No users found", style: TextStyle(color: Colors.grey[500], fontSize: 16)),
                          ],
                        ),
                      )
                    : RefreshIndicator(
                        onRefresh: () async => ref.read(userNotifierProvider.notifier).loadUsers(),
                        color: settings.primaryColor,
                        child: ListView.builder(
                          itemCount: filteredUsers.length,
                          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                          itemBuilder: (context, index) {
                            final user = filteredUsers[index];
                            return Container(
                              margin: const EdgeInsets.only(bottom: 12),
                              decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.circular(16),
                                boxShadow: [
                                  BoxShadow(
                                    color: Colors.black.withValues(alpha: 0.04),
                                    blurRadius: 10,
                                    offset: const Offset(0, 4),
                                  ),
                                ],
                                border: Border.all(color: Colors.grey.withValues(alpha: 0.1)),
                              ),
                              child: Material(
                                color: Colors.transparent,
                                child: InkWell(
                                  borderRadius: BorderRadius.circular(16),
                                  onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => AdminUserDetailScreen(user: user))),
                                  child: Padding(
                                    padding: const EdgeInsets.all(16),
                                    child: Row(
                                      children: [
                                        // Avatar
                                        Container(
                                          width: 50,
                                          height: 50,
                                          decoration: BoxDecoration(
                                            color: settings.primaryColor.withValues(alpha: 0.1),
                                            shape: BoxShape.circle,
                                          ),
                                          child: Center(
                                            child: Text(
                                              user.name.isNotEmpty ? user.name[0].toUpperCase() : "?",
                                              style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold, fontSize: 20),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 16),
                                        
                                        // Details
                                        Expanded(
                                          child: Column(
                                            crossAxisAlignment: CrossAxisAlignment.start,
                                            children: [
                                              Row(
                                                children: [
                                                  Expanded(
                                                    child: Text(
                                                      user.name.isNotEmpty ? user.name : "Unknown User",
                                                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                                                      maxLines: 1,
                                                      overflow: TextOverflow.ellipsis,
                                                    ),
                                                  ),
                                                  const SizedBox(width: 8),
                                                  _buildRoleBadge(user.role),
                                                ],
                                              ),
                                              const SizedBox(height: 4),
                                              if (user.email.isNotEmpty)
                                                Text(user.email, style: TextStyle(color: Colors.grey[600], fontSize: 12)),
                                              if (user.phoneNumber.isNotEmpty)
                                                Text(user.phoneNumber, style: TextStyle(color: Colors.grey[600], fontSize: 12)),
                                              if (user.role == 'owner' && user.shopName != null && user.shopName!.isNotEmpty)
                                                Padding(
                                                  padding: const EdgeInsets.only(top: 4.0),
                                                  child: Row(
                                                    children: [
                                                      Icon(Icons.storefront, size: 14, color: settings.primaryColor),
                                                      const SizedBox(width: 4),
                                                      Expanded(
                                                        child: Text(
                                                          user.shopName!, 
                                                          style: TextStyle(color: settings.primaryColor, fontSize: 12, fontWeight: FontWeight.bold),
                                                          maxLines: 1,
                                                          overflow: TextOverflow.ellipsis,
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                                ),
                                              const SizedBox(height: 8),
                                              Container(
                                                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                                                decoration: BoxDecoration(
                                                  color: user.isActive ? Colors.green.withValues(alpha: 0.1) : Colors.red.withValues(alpha: 0.1),
                                                  borderRadius: BorderRadius.circular(8),
                                                ),
                                                child: Text(
                                                  user.isActive ? "Active Account" : "Inactive Account",
                                                  style: TextStyle(
                                                    color: user.isActive ? Colors.green[700] : Colors.red[700],
                                                    fontSize: 10,
                                                    fontWeight: FontWeight.bold,
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),
                                        ),
                                        
                                        // Trailing Switch
                                        if ((isSuperAdmin && user.uid != currentUser?.uid) || (user.role == 'user' || user.role == 'owner' || user.role == 'delivery_man'))
                                          Padding(
                                            padding: const EdgeInsets.only(left: 12),
                                            child: Switch.adaptive(
                                              value: user.isActive,
                                              activeColor: Colors.green,
                                              onChanged: (val) async {
                                                try {
                                                  final updatedUser = user.copyWith(isActive: val);
                                                  await ref.read(userNotifierProvider.notifier).updateUser(updatedUser);
                                                } catch (e) {
                                                  if (context.mounted) {
                                                    ScaffoldMessenger.of(context).showSnackBar(
                                                      SnackBar(content: Text('Failed to update user: $e'), backgroundColor: Colors.red),
                                                    );
                                                  }
                                                }
                                              },
                                            ),
                                          ),
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                            );
                          },
                        ),
                      ),
          ),
        ],
      ),
    );
  }

  Widget _buildRoleBadge(String role) {
    Color bgColor;
    Color textColor;
    String label;

    switch (role) {
      case 'super_admin':
        bgColor = Colors.amber.withValues(alpha: 0.2);
        textColor = Colors.amber[800]!;
        label = "SUPER ADMIN";
        break;
      case 'admin':
        bgColor = Colors.red.withValues(alpha: 0.1);
        textColor = Colors.red;
        label = "ADMIN";
        break;
      case 'owner':
        bgColor = Colors.deepPurple.withValues(alpha: 0.1);
        textColor = Colors.deepPurple;
        label = "SHOP OWNER";
        break;
      case 'delivery_man':
        bgColor = Colors.teal.withValues(alpha: 0.1);
        textColor = Colors.teal[700]!;
        label = "RIDER";
        break;
      case 'user':
      default:
        bgColor = Colors.blue.withValues(alpha: 0.1);
        textColor = Colors.blue[700]!;
        label = "CUSTOMER";
        break;
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: bgColor,
        borderRadius: BorderRadius.circular(6),
      ),
      child: Text(
        label,
        style: TextStyle(
          color: textColor,
          fontSize: 9,
          fontWeight: FontWeight.w900,
          letterSpacing: 0.5,
        ),
      ),
    );
  }
}
