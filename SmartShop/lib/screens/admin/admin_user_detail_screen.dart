import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../../models/user_model.dart';
import '../../view_models/order_view_model.dart';
import '../../view_models/user_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/activity_log_view_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../widgets/custom_app_bar.dart';
import '../order/order_details_screen.dart';

class AdminUserDetailScreen extends StatefulWidget {
  final UserModel user;
  const AdminUserDetailScreen({super.key, required this.user});

  @override
  State<AdminUserDetailScreen> createState() => _AdminUserDetailScreenState();
}

class _AdminUserDetailScreenState extends State<AdminUserDetailScreen> {
  late TextEditingController _nameController, _phoneController, _addressController;

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.user.name);
    _phoneController = TextEditingController(text: widget.user.phoneNumber);
    _addressController = TextEditingController(text: widget.user.address);
  }

  @override
  void dispose() {
    _nameController.dispose();
    _phoneController.dispose();
    _addressController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsViewModel>();
    final orderVM = context.watch<OrderViewModel>();
    final authVM = context.read<AuthViewModel>();
    
    final isSuperAdmin = authVM.user?.role == 'super_admin';
    final canEdit = isSuperAdmin || (widget.user.role == 'user');
    
    final userOrders = orderVM.allOrders.where((o) => o.userId == widget.user.uid).toList();

    return Scaffold(
      appBar: CustomAppBar(
        title: widget.user.name,
        actions: [
          if (canEdit)
            IconButton(
              icon: const Icon(Icons.save_rounded, color: Colors.white),
              onPressed: () => _updateUserData(context, authVM),
            ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // User Edit Section
            _buildEditCard(context, settings, isSuperAdmin, canEdit),
            
            const SizedBox(height: 24),
            
            Text(
              "Order History (${userOrders.length})",
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900),
            ),
            const SizedBox(height: 12),
            
            if (userOrders.isEmpty)
              const Center(child: Padding(
                padding: EdgeInsets.all(20.0),
                child: Text("No orders placed by this user"),
              ))
            else
              ListView.builder(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                itemCount: userOrders.length,
                itemBuilder: (context, index) {
                  final order = userOrders[index];
                  return Card(
                    margin: const EdgeInsets.only(bottom: 10),
                    elevation: 0,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(15),
                      side: BorderSide(color: Colors.grey.withValues(alpha: 0.1)),
                    ),
                    child: ListTile(
                      title: Text("Order ID: ${order.id}", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                      subtitle: Text(DateFormat('dd MMM yyyy').format(order.date), style: const TextStyle(fontSize: 12)),
                      trailing: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          Text("৳${order.totalAmount.toInt()}", style: TextStyle(fontWeight: FontWeight.bold, color: settings.primaryColor)),
                          Text(order.status, style: TextStyle(fontSize: 10, color: _getStatusColor(order.status))),
                        ],
                      ),
                      onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => OrderDetailsScreen(order: order))),
                    ),
                  );
                },
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildEditCard(BuildContext context, SettingsViewModel settings, bool isSuperAdmin, bool canEdit) {
    return Card(
      elevation: 0,
      margin: EdgeInsets.zero,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(16),
        side: BorderSide(color: Colors.grey.withValues(alpha: 0.1)),
      ),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Center(
              child: Column(
                children: [
                  CircleAvatar(
                    radius: 40,
                    backgroundColor: settings.primaryColor.withValues(alpha: 0.1),
                    child: Text(widget.user.name[0].toUpperCase(), style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold, color: settings.primaryColor)),
                  ),
                  const SizedBox(height: 12),
                  Text(widget.user.role.toUpperCase(), style: TextStyle(fontSize: 12, fontWeight: FontWeight.w900, color: settings.primaryColor, letterSpacing: 1.5)),
                ],
              ),
            ),
            const Divider(height: 32),
            _editField(_nameController, "Full Name", Icons.person_outline, !canEdit),
            const SizedBox(height: 16),
            _editField(_phoneController, "Phone Number", Icons.phone_android, !canEdit),
            const SizedBox(height: 16),
            _editField(_addressController, "Address", Icons.location_on_outlined, !canEdit, lines: 2),
            const SizedBox(height: 24),
            if (isSuperAdmin && widget.user.uid != context.read<AuthViewModel>().user?.uid)
              _buildRoleSwitcher(context, settings),
          ],
        ),
      ),
    );
  }

  Widget _editField(TextEditingController controller, String label, IconData icon, bool readOnly, {int lines = 1}) {
    return TextField(
      controller: controller,
      readOnly: readOnly,
      maxLines: lines,
      decoration: InputDecoration(
        labelText: label,
        prefixIcon: Icon(icon, size: 20),
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
        contentPadding: const EdgeInsets.all(12),
      ),
    );
  }

  Widget _buildRoleSwitcher(BuildContext context, SettingsViewModel settings) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text("Change Account Role", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
        const SizedBox(height: 12),
        Row(
          children: [
            _roleBtn(context, "user", "Customer", settings),
            const SizedBox(width: 10),
            _roleBtn(context, "admin", "Admin", settings),
          ],
        ),
      ],
    );
  }

  Widget _roleBtn(BuildContext context, String role, String label, SettingsViewModel settings) {
    bool isSelected = widget.user.role == role;
    return Expanded(
      child: OutlinedButton(
        onPressed: () => _updateRole(context, role),
        style: OutlinedButton.styleFrom(
          backgroundColor: isSelected ? settings.primaryColor : null,
          foregroundColor: isSelected ? Colors.white : settings.primaryColor,
          side: BorderSide(color: settings.primaryColor),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
        ),
        child: Text(label),
      ),
    );
  }

  Future<void> _updateUserData(BuildContext context, AuthViewModel authVM) async {
    final userVM = context.read<UserViewModel>();
    final logVM = context.read<ActivityLogViewModel>();
    
    await userVM.updateUserProfile(widget.user.uid, {
      'name': _nameController.text.trim(),
      'phoneNumber': _phoneController.text.trim(),
      'address': _addressController.text.trim(),
    });

    await logVM.logAction(
      adminId: authVM.user!.uid,
      adminName: authVM.user!.name,
      action: "Updated Profile",
      targetId: widget.user.name,
      details: "Changed info for ${widget.user.email}",
    );

    if (mounted) ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Profile updated successfully!")));
  }

  Future<void> _updateRole(BuildContext context, String newRole) async {
    if (widget.user.role == newRole) return;
    
    final userVM = context.read<UserViewModel>();
    final authVM = context.read<AuthViewModel>();
    final logVM = context.read<ActivityLogViewModel>();

    await userVM.updateUserRole(widget.user.uid, newRole);

    await logVM.logAction(
      adminId: authVM.user!.uid,
      adminName: authVM.user!.name,
      action: "Changed Role",
      targetId: widget.user.name,
      details: "Promoted/Demoted to $newRole",
    );

    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("User role changed to $newRole")));
      Navigator.pop(context);
    }
  }

  Color _getStatusColor(String status) {
    if (status == 'Pending') return Colors.orange;
    if (status == 'Delivered') return Colors.green;
    if (status == 'Cancelled') return Colors.red;
    return Colors.blue;
  }
}
