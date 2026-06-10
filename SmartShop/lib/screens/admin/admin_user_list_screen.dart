import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:easy_localization/easy_localization.dart';
import '../../utils/constants/app_strings.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/user_view_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../widgets/custom_app_bar.dart';
import 'admin_user_detail_screen.dart';

class AdminUserListScreen extends StatefulWidget {
  const AdminUserListScreen({super.key});

  @override
  State<AdminUserListScreen> createState() => _AdminUserListScreenState();
}

class _AdminUserListScreenState extends State<AdminUserListScreen> {
  final TextEditingController _searchController = TextEditingController();
  String _searchQuery = "";

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final user = context.read<AuthViewModel>().user;
      context.read<UserViewModel>().fetchUsers(
        shopId: user?.shopId, 
        isSuperAdmin: user?.role == 'super_admin'
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    final userVM = context.watch<UserViewModel>();
    final settings = context.watch<SettingsViewModel>();
    final currentUser = context.read<AuthViewModel>().user;
    final isSuperAdmin = currentUser?.role == 'super_admin';

    final filteredUsers = userVM.users.where((user) {
      if (!isSuperAdmin && user.role == 'super_admin') return false;
      return user.name.toLowerCase().contains(_searchQuery.toLowerCase()) ||
             user.email.toLowerCase().contains(_searchQuery.toLowerCase()) ||
             user.phoneNumber.contains(_searchQuery);
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
            child: userVM.isLoading
                ? const Center(child: CircularProgressIndicator())
                : filteredUsers.isEmpty
                    ? const Center(child: Text("No users found"))
                    : ListView.builder(
                        itemCount: filteredUsers.length,
                        padding: const EdgeInsets.symmetric(horizontal: 12),
                        itemBuilder: (context, index) {
                          final user = filteredUsers[index];
                          return Card(
                            margin: const EdgeInsets.only(bottom: 8),
                            elevation: 0,
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12), side: BorderSide(color: Colors.grey.withValues(alpha: 0.1))),
                            child: ListTile(
                              leading: CircleAvatar(backgroundColor: settings.primaryColor.withValues(alpha: 0.1), child: Text(user.name.isNotEmpty ? user.name[0].toUpperCase() : "?", style: TextStyle(color: settings.primaryColor, fontWeight: FontWeight.bold))),
                              title: Row(
                                children: [
                                  Expanded(child: Text(user.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14))),
                                  if (user.role != 'user')
                                    Container(padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2), decoration: BoxDecoration(color: Colors.amber.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(5)), child: Text(user.role.toUpperCase(), style: const TextStyle(color: Colors.amber, fontSize: 8, fontWeight: FontWeight.bold))),
                                ],
                              ),
                              subtitle: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(user.email, style: const TextStyle(fontSize: 11)),
                                  Text(user.phoneNumber, style: const TextStyle(fontSize: 11)),
                                  const SizedBox(height: 4),
                                  Container(padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2), decoration: BoxDecoration(color: user.isActive ? Colors.green.withValues(alpha: 0.1) : Colors.red.withValues(alpha: 0.1), borderRadius: BorderRadius.circular(10)), child: Text(user.isActive ? "Active" : "Inactive", style: TextStyle(color: user.isActive ? Colors.green : Colors.red, fontSize: 9, fontWeight: FontWeight.bold))),
                                ],
                              ),
                              trailing: (isSuperAdmin && user.uid != currentUser?.uid) || (user.role == 'user')
                                ? Switch.adaptive(value: user.isActive, activeColor: Colors.green, onChanged: (val) => userVM.toggleUserStatus(user.uid, user.isActive))
                                : null,
                              onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => AdminUserDetailScreen(user: user))),
                            ),
                          );
                        },
                      ),
          ),
        ],
      ),
    );
  }
}
