import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../../view_models/support_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/settings_view_model.dart';
import '../../models/support_ticket_model.dart';
import '../../routes/app_routes.dart';

class SupportScreen extends StatefulWidget {
  final bool isEmbedded;
  const SupportScreen({super.key, this.isEmbedded = false});

  @override
  State<SupportScreen> createState() => _SupportScreenState();
}

class _SupportScreenState extends State<SupportScreen> {
  final TextEditingController _messageController = TextEditingController();

  @override
  void initState() {
    super.initState();
    final authVM = context.read<AuthViewModel>();
    final supportVM = context.read<SupportViewModel>();
    if (authVM.isAdmin) {
      supportVM.fetchAllTickets();
    } else if (authVM.user != null) {
      supportVM.listenToMessages(authVM.user!.uid);
      // Mark as read when opening support
      WidgetsBinding.instance.addPostFrameCallback((_) {
        supportVM.markAsRead();
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final authVM = context.watch<AuthViewModel>();
    final supportVM = context.watch<SupportViewModel>();
    final settings = context.watch<SettingsViewModel>();

    return Scaffold(
      appBar: widget.isEmbedded ? null : AppBar(
        title: const Text("Help & Support"),
      ),
      body: authVM.isAdmin 
        ? _buildAdminTicketList(supportVM)
        : _buildChatInterface(supportVM, authVM, settings),
    );
  }

  Widget _buildAdminTicketList(SupportViewModel vm) {
    if (vm.isLoading && vm.tickets.isEmpty) return const Center(child: CircularProgressIndicator());
    if (vm.tickets.isEmpty) return const Center(child: Text("No support tickets yet."));

    return ListView.builder(
      itemCount: vm.tickets.length,
      itemBuilder: (context, index) {
        final ticket = vm.tickets[index];
        // Clean display of phone number
        String displayPhone = (ticket.userPhone.isEmpty || ticket.userPhone == "null") 
            ? "No phone number" 
            : ticket.userPhone;
        
        return Card(
          margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
          elevation: 0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(15),
            side: BorderSide(color: Colors.grey.withValues(alpha: 0.1)),
          ),
          child: ListTile(
            contentPadding: const EdgeInsets.all(12),
            leading: CircleAvatar(
              backgroundColor: Theme.of(context).primaryColor.withValues(alpha: 0.1),
              child: Icon(Icons.person, color: Theme.of(context).primaryColor),
            ),
            title: Text(ticket.userName, style: const TextStyle(fontWeight: FontWeight.bold)),
            subtitle: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const SizedBox(height: 4),
                Text(displayPhone, style: TextStyle(color: Colors.grey[600], fontSize: 13)),
                const SizedBox(height: 2),
                Text(ticket.lastMessage, maxLines: 1, overflow: TextOverflow.ellipsis),
              ],
            ),
            trailing: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(DateFormat('hh:mm a').format(ticket.lastUpdate), style: const TextStyle(fontSize: 10)),
                const SizedBox(height: 8),
                Icon(Icons.circle, size: 10, color: ticket.status == 'open' ? Colors.green : Colors.grey),
              ],
            ),
            onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => AdminChatDetail(ticket: ticket))),
          ),
        );
      },
    );
  }

  Widget _buildChatInterface(SupportViewModel vm, AuthViewModel auth, SettingsViewModel settings) {
    return Column(
      children: [
        // Professional Shop Info Header
        Container(
          padding: const EdgeInsets.all(16),
          color: settings.primaryColor.withValues(alpha: 0.1),
          child: Row(
            children: [
              CircleAvatar(
                backgroundColor: settings.primaryColor,
                child: const Icon(Icons.storefront_rounded, color: Colors.white),
              ),
              const SizedBox(width: 15),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(settings.shopName, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                    const Text("Online Support", style: TextStyle(color: Colors.green, fontSize: 13, fontWeight: FontWeight.bold)),
                  ],
                ),
              ),
            ],
          ),
        ),
        Expanded(
          child: ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: vm.messages.length,
            itemBuilder: (context, index) {
              final msg = vm.messages[index];
              bool isMe = !msg.isAdmin;
              return Align(
                alignment: isMe ? Alignment.centerRight : Alignment.centerLeft,
                child: Container(
                  margin: const EdgeInsets.only(bottom: 8),
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                  constraints: BoxConstraints(maxWidth: MediaQuery.of(context).size.width * 0.75),
                  decoration: BoxDecoration(
                    color: isMe ? settings.primaryColor : Colors.grey[200],
                    borderRadius: BorderRadius.only(
                      topLeft: const Radius.circular(15),
                      topRight: const Radius.circular(15),
                      bottomLeft: Radius.circular(isMe ? 15 : 0),
                      bottomRight: Radius.circular(isMe ? 0 : 15),
                    ),
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        msg.message,
                        style: TextStyle(color: isMe ? Colors.white : Colors.black87),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        DateFormat('hh:mm a').format(msg.timestamp),
                        style: TextStyle(color: isMe ? Colors.white70 : Colors.black54, fontSize: 10),
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
        _buildInputArea(vm, auth),
      ],
    );
  }

  Widget _buildInputArea(SupportViewModel vm, AuthViewModel auth) {
    return Container(
      padding: const EdgeInsets.fromLTRB(16, 8, 8, 16),
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 10, offset: const Offset(0, -5))],
      ),
      child: SafeArea(
        child: Row(
          children: [
            Expanded(
              child: TextField(
                controller: _messageController,
                decoration: InputDecoration(
                  hintText: "Type a message...",
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(25), borderSide: BorderSide.none),
                  filled: true,
                  fillColor: Colors.grey[100],
                  contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                ),
              ),
            ),
            const SizedBox(width: 8),
            CircleAvatar(
              backgroundColor: Theme.of(context).primaryColor,
              child: IconButton(
                icon: const Icon(Icons.send_rounded, color: Colors.white),
                onPressed: () {
                  if (_messageController.text.isNotEmpty && auth.user != null) {
                    vm.sendMessage(
                      auth.user!.uid, 
                      auth.user!.name, 
                      _messageController.text,
                      userPhone: auth.user!.phoneNumber,
                    );
                    _messageController.clear();
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class AdminChatDetail extends StatefulWidget {
  final SupportTicket ticket;
  const AdminChatDetail({super.key, required this.ticket});

  @override
  State<AdminChatDetail> createState() => _AdminChatDetailState();
}

class _AdminChatDetailState extends State<AdminChatDetail> {
  final TextEditingController _msgController = TextEditingController();
  String? _userPhone;

  @override
  void initState() {
    super.initState();
    context.read<SupportViewModel>().listenToMessages(widget.ticket.id);
    _userPhone = widget.ticket.userPhone;
    if (_userPhone == null || _userPhone!.isEmpty) {
      _fetchUserPhone();
    }
  }

  void _fetchUserPhone() async {
    final phone = await context.read<SupportViewModel>().getUserPhone(widget.ticket.userId);
    if (mounted && phone != null) {
      setState(() {
        _userPhone = phone;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final vm = context.watch<SupportViewModel>();
    final settings = context.watch<SettingsViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(widget.ticket.userName),
            if (_userPhone != null && _userPhone!.isNotEmpty)
              Text(_userPhone!, style: const TextStyle(fontSize: 12, fontWeight: FontWeight.normal)),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.check_circle_outline_rounded),
            onPressed: () {
              vm.closeTicket(widget.ticket.id);
              Navigator.pop(context);
            },
          ),
        ],
      ),
      body: Column(
        children: [
          // Professional User Info Header
          Container(
            padding: const EdgeInsets.all(12),
            color: Colors.orange.withValues(alpha: 0.1),
            child: Row(
              children: [
                const CircleAvatar(child: Icon(Icons.person_outline_rounded)),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text("Customer: ${widget.ticket.userName}", style: const TextStyle(fontWeight: FontWeight.bold)),
                      Text("Phone: ${_userPhone ?? 'Loading...'}", style: const TextStyle(fontSize: 12)),
                    ],
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: vm.messages.length,
              itemBuilder: (context, index) {
                final msg = vm.messages[index];
                bool isMe = msg.isAdmin;
                return Align(
                  alignment: isMe ? Alignment.centerRight : Alignment.centerLeft,
                  child: Container(
                    margin: const EdgeInsets.only(bottom: 8),
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                    constraints: BoxConstraints(maxWidth: MediaQuery.of(context).size.width * 0.75),
                    decoration: BoxDecoration(
                      color: isMe ? Colors.green[600] : Colors.grey[200],
                      borderRadius: BorderRadius.only(
                        topLeft: const Radius.circular(15),
                        topRight: const Radius.circular(15),
                        bottomLeft: Radius.circular(isMe ? 15 : 0),
                        bottomRight: Radius.circular(isMe ? 0 : 15),
                      ),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(msg.message, style: TextStyle(color: isMe ? Colors.white : Colors.black87)),
                        const SizedBox(height: 4),
                        Text(
                          DateFormat('hh:mm a').format(msg.timestamp),
                          style: TextStyle(color: isMe ? Colors.white70 : Colors.black54, fontSize: 10),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          ),
          _buildAdminInput(vm, settings),
        ],
      ),
    );
  }

  Widget _buildAdminInput(SupportViewModel vm, SettingsViewModel settings) {
    return Container(
      padding: const EdgeInsets.fromLTRB(16, 8, 8, 16),
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 10, offset: const Offset(0, -5))],
      ),
      child: SafeArea(
        child: Row(
          children: [
            Expanded(
              child: TextField(
                controller: _msgController,
                decoration: InputDecoration(
                  hintText: "Reply to customer...",
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(25), borderSide: BorderSide.none),
                  filled: true,
                  fillColor: Colors.grey[100],
                  contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                ),
              ),
            ),
            const SizedBox(width: 8),
            CircleAvatar(
              backgroundColor: Colors.green,
              child: IconButton(
                icon: const Icon(Icons.send_rounded, color: Colors.white),
                onPressed: () {
                  if (_msgController.text.isNotEmpty) {
                    vm.sendMessage(
                      widget.ticket.userId, 
                      widget.ticket.userName, 
                      _msgController.text, 
                      isAdmin: true, 
                      ticketId: widget.ticket.id
                    );
                    _msgController.clear();
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
