import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../../view_models/support_view_model.dart';
import '../../view_models/auth_view_model.dart';
import '../../view_models/settings_view_model.dart';

class SupportScreen extends StatefulWidget {
  const SupportScreen({super.key});

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
    }
  }

  @override
  Widget build(BuildContext context) {
    final authVM = context.watch<AuthViewModel>();
    final supportVM = context.watch<SupportViewModel>();
    final settings = context.watch<SettingsViewModel>();

    return Scaffold(
      appBar: AppBar(title: const Text("Help & Support")),
      body: authVM.isAdmin 
        ? _buildAdminTicketList(supportVM)
        : _buildChatInterface(supportVM, authVM),
    );
  }

  Widget _buildAdminTicketList(SupportViewModel vm) {
    return ListView.builder(
      itemCount: vm.tickets.length,
      itemBuilder: (context, index) {
        final ticket = vm.tickets[index];
        return ListTile(
          leading: const CircleAvatar(child: Icon(Icons.person)),
          title: Text(ticket.userName, style: const TextStyle(fontWeight: FontWeight.bold)),
          subtitle: Text(ticket.lastMessage, maxLines: 1, overflow: TextOverflow.ellipsis),
          trailing: Text(DateFormat('hh:mm a').format(ticket.lastUpdate), style: const TextStyle(fontSize: 10)),
          onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => AdminChatDetail(ticket: ticket))),
        );
      },
    );
  }

  Widget _buildChatInterface(SupportViewModel vm, AuthViewModel auth) {
    return Column(
      children: [
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
                  decoration: BoxDecoration(
                    color: isMe ? Theme.of(context).primaryColor : Colors.grey[200],
                    borderRadius: BorderRadius.circular(15),
                  ),
                  child: Text(
                    msg.message,
                    style: TextStyle(color: isMe ? Colors.white : Colors.black87),
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
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(color: Colors.white, boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 4)]),
      child: Row(
        children: [
          Expanded(
            child: TextField(
              controller: _messageController,
              decoration: const InputDecoration(hintText: "Type a message...", border: InputBorder.none),
            ),
          ),
          IconButton(
            icon: const Icon(Icons.send),
            onPressed: () {
              if (_messageController.text.isNotEmpty) {
                vm.sendMessage(auth.user!.uid, auth.user!.name, _messageController.text);
                _messageController.clear();
              }
            },
          ),
        ],
      ),
    );
  }
}

class AdminChatDetail extends StatefulWidget {
  final dynamic ticket;
  const AdminChatDetail({super.key, required this.ticket});

  @override
  State<AdminChatDetail> createState() => _AdminChatDetailState();
}

class _AdminChatDetailState extends State<AdminChatDetail> {
  final TextEditingController _msgController = TextEditingController();

  @override
  void initState() {
    super.initState();
    context.read<SupportViewModel>().listenToMessages(widget.ticket.id);
  }

  @override
  Widget build(BuildContext context) {
    final vm = context.watch<SupportViewModel>();
    return Scaffold(
      appBar: AppBar(title: Text(widget.ticket.userName)),
      body: Column(
        children: [
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
                    decoration: BoxDecoration(
                      color: isMe ? Colors.green : Colors.grey[200],
                      borderRadius: BorderRadius.circular(15),
                    ),
                    child: Text(msg.message, style: TextStyle(color: isMe ? Colors.white : Colors.black87)),
                  ),
                );
              },
            ),
          ),
          Container(
            padding: const EdgeInsets.all(16),
            child: Row(
              children: [
                Expanded(child: TextField(controller: _msgController)),
                IconButton(
                  icon: const Icon(Icons.send),
                  onPressed: () {
                    vm.sendMessage(widget.ticket.userId, widget.ticket.userName, _msgController.text, isAdmin: true, ticketId: widget.ticket.id);
                    _msgController.clear();
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
