import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:intl/intl.dart';
import '../riverpod/support_notifier.dart';
import '../../auth/riverpod/auth_notifier.dart';
import '../../../core/riverpod/settings_notifier.dart';
import '../../../models/support_ticket_model.dart';
import '../../../utils/constants/app_strings.dart';

class SupportScreen extends ConsumerStatefulWidget {
  final bool isEmbedded;
  const SupportScreen({super.key, this.isEmbedded = false});

  @override
  ConsumerState<SupportScreen> createState() => _SupportScreenState();
}

class _SupportScreenState extends ConsumerState<SupportScreen> {
  final TextEditingController _messageController = TextEditingController();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final auth = ref.read(authNotifierProvider).value;
      if (auth?.role == 'admin' || auth?.role == 'super_admin') {
      } else if (auth != null) {
        // Mark as read when opening support
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authNotifierProvider);
    final supportState = ref.watch(supportNotifierProvider);
    final settings = ref.watch(settingsProvider);
    
    final isAdmin = authState.value?.role == 'admin' || authState.value?.role == 'super_admin';

    return Scaffold(
      appBar: widget.isEmbedded ? null : AppBar(
        title: const Text("Help & Support"),
      ),
      body: supportState.when(
        data: (data) => isAdmin 
          ? _buildAdminTicketList(data)
          : _buildChatInterface(data, authState.value, settings),
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (e, st) => Center(child: Text(e.toString())),
      ),
    );
  }

  Widget _buildAdminTicketList(List<dynamic> state) {
    if (state.isEmpty) return const Center(child: CircularProgressIndicator());
    final tickets = []; // Access tickets from state in actual implementation
    if (tickets.isEmpty) return Center(child: Text(AppStrings.noSupportTickets.tr()));

    return ListView.builder(
      itemCount: tickets.length,
      itemBuilder: (context, index) {
        final ticket = tickets[index];
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

  Widget _buildChatInterface(List<dynamic> state, dynamic user, dynamic settings) {
    final messages = []; // Access messages from state in actual implementation
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
                    Text(settings.shopName ?? 'Shop', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                    Text(AppStrings.onlineSupport.tr(), style: const TextStyle(color: Colors.green, fontSize: 13, fontWeight: FontWeight.bold)),
                  ],
                ),
              ),
            ],
          ),
        ),
        Expanded(
          child: ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: messages.length,
            itemBuilder: (context, index) {
              final msg = messages[index];
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
        _buildInputArea(user),
      ],
    );
  }

  Widget _buildInputArea(dynamic user) {
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
                  hintText: AppStrings.typeMessage.tr(),
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
                  if (_messageController.text.isNotEmpty && user != null) {
                    // ref.read(supportNotifierProvider.notifier).sendMessage(
                    //  user.uid, 
                    //  user.name, 
                    //  _messageController.text,
                    //  userPhone: user.phoneNumber,
                    // );
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

class AdminChatDetail extends ConsumerStatefulWidget {
  final SupportTicket ticket;
  const AdminChatDetail({super.key, required this.ticket});

  @override
  ConsumerState<AdminChatDetail> createState() => _AdminChatDetailState();
}

class _AdminChatDetailState extends ConsumerState<AdminChatDetail> {
  final TextEditingController _msgController = TextEditingController();
  String? _userPhone;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _userPhone = widget.ticket.userPhone;
      if (_userPhone == null || _userPhone!.isEmpty) {
        _fetchUserPhone();
      }
    });
  }

  void _fetchUserPhone() async {
    final phone = widget.ticket.userPhone;
    if (mounted && phone.isNotEmpty) {
      setState(() {
        _userPhone = phone;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final supportState = ref.watch(supportNotifierProvider);
    final settings = ref.watch(settingsProvider);

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
              Navigator.pop(context);
            },
          ),
        ],
      ),
      body: supportState.when(
        data: (messages) => Column(
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
                        Text("${AppStrings.customerLabel.tr()}: ${widget.ticket.userName}", style: const TextStyle(fontWeight: FontWeight.bold)),
                        Text("${AppStrings.phoneLabel.tr()}: ${_userPhone ?? '...'}", style: const TextStyle(fontSize: 12)),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            Expanded(
              child: ListView.builder(
                padding: const EdgeInsets.all(16),
                itemCount: messages.length,
                itemBuilder: (context, index) {
                  final msg = messages[index];
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
            _buildAdminInput(settings),
          ],
        ),
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (e, st) => Center(child: Text(e.toString())),
      ),
    );
  }

  Widget _buildAdminInput(dynamic settings) {
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
                  hintText: AppStrings.replyToCustomer.tr(),
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
                    // ref.read(supportNotifierProvider.notifier).sendMessage(
                    //  widget.ticket.userId, 
                    //  widget.ticket.userName, 
                    //  _msgController.text, 
                    //  isAdmin: true, 
                    //  ticketId: widget.ticket.id
                    // );
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
