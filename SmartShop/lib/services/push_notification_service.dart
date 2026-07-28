import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter_callkit_incoming/entities/call_event.dart';
import 'package:flutter_callkit_incoming/flutter_callkit_incoming.dart';
import 'package:flutter_callkit_incoming/entities/call_kit_params.dart';
import 'package:flutter_callkit_incoming/entities/android_params.dart';
import 'package:flutter_callkit_incoming/entities/ios_params.dart';
import 'package:flutter_callkit_incoming/entities/notification_params.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:uuid/uuid.dart';
import 'package:flutter/material.dart';
import 'package:audioplayers/audioplayers.dart';
import '../main.dart';
import '../core/constants/constants.dart';

@pragma('vm:entry-point')
Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  await Firebase.initializeApp();
  debugPrint("Handling a background message: ${message.messageId}");
  PushNotificationService.handleIncomingMessage(message, isForeground: false);
}

class PushNotificationService {
  static final PushNotificationService _instance = PushNotificationService._internal();
  factory PushNotificationService() => _instance;
  PushNotificationService._internal();

  final FirebaseMessaging _firebaseMessaging = FirebaseMessaging.instance;

  Future<void> init() async {
    NotificationSettings settings = await _firebaseMessaging.requestPermission(
      alert: true,
      badge: true,
      sound: true,
    );
    debugPrint('User granted permission: ${settings.authorizationStatus}');

    FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);

    FirebaseMessaging.onMessage.listen((RemoteMessage message) {
      debugPrint('Got a message whilst in the foreground!');
      handleIncomingMessage(message, isForeground: true);
    });

    FlutterCallkitIncoming.onEvent.listen((CallEvent? event) {
      if (event == null) return;
      if (event is CallEventActionCallAccept) {
        debugPrint('Call Accepted: ${event.callKitParams.extra}');
      } else if (event is CallEventActionCallDecline) {
        debugPrint('Call Declined: ${event.callKitParams.extra}');
      }
    });
  }

  static Future<void> handleIncomingMessage(RemoteMessage message, {bool isForeground = false}) async {
    debugPrint("Message data: ${message.data}");
    
    // Safety check: Only show delivery dialog to users with 'delivery_man' role
    final currentUser = Supabase.instance.client.auth.currentUser;
    if (currentUser == null) return;

    // Fetch user role from DB to verify if they should see rider alerts
    final userRes = await Supabase.instance.client
        .from(AppConstants.usersTable)
        .select('role')
        .eq('id', currentUser.id)
        .maybeSingle();

    if (userRes == null || userRes['role'] != 'delivery_man') {
      debugPrint("Skipping rider dialog: User is not a delivery man.");
      return;
    }

    if (message.data['type'] == 'new_order') {
      final orderId = message.data['orderId'] ?? 'Unknown Order';
      final shopName = message.data['shopName'] ?? 'Smart Shop';
      final orderType = message.data['orderType'] ?? 'online';

      // Strictly skip if order type is POS (it should be delivered instantly by admin)
      if (orderType == 'pos') return;
      
      if (isForeground) {
        _showInAppIncomingOrderDialog(orderId: orderId, shopName: shopName);
      } else {
        await showIncomingCall(orderId: orderId, shopName: shopName);
      }
    }
  }

  static void _showInAppIncomingOrderDialog({required String orderId, required String shopName}) async {
    final context = navigatorKey.currentContext;
    if (context == null) return;

    final player = AudioPlayer();
    
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        backgroundColor: Colors.white,
        title: Column(
          children: [
            const Icon(Icons.delivery_dining, size: 60, color: Colors.green),
            const SizedBox(height: 10),
            const Text(
              "New Delivery Request!",
              style: TextStyle(fontWeight: FontWeight.w900, color: Colors.green),
              textAlign: TextAlign.center,
            ),
          ],
        ),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text("Shop: $shopName", style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Text("Order ID: #$orderId", style: const TextStyle(fontSize: 14)),
          ],
        ),
        actionsAlignment: MainAxisAlignment.spaceEvenly,
        actions: [
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.red,
              foregroundColor: Colors.white,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            ),
            onPressed: () {
              player.stop();
              Navigator.pop(ctx);
            },
            child: const Text("Decline"),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.green,
              foregroundColor: Colors.white,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            ),
            onPressed: () {
              player.stop();
              Navigator.pop(ctx);
              // Logic to accept order via repository
            },
            child: const Text("Accept"),
          ),
        ],
      ),
    );
  }

  static Future<void> showIncomingCall({required String orderId, required String shopName}) async {
    final uuid = const Uuid().v4();
    final params = CallKitParams(
      id: uuid,
      nameCaller: shopName,
      appName: 'Smart Shop Rider',
      avatar: 'https://i.pravatar.cc/100',
      handle: 'New Order: #$orderId',
      type: 0,
      duration: 30000,
      missedCallNotification: const NotificationParams(
        showNotification: true,
        isShowCallback: false,
        subtitle: 'Missed Order Request',
        callbackText: 'Call back',
      ),
      extra: <String, dynamic>{'orderId': orderId},
      headers: <String, dynamic>{'apiKey': 'abc@123!'},
      android: const AndroidParams(
        isCustomNotification: true,
        isShowLogo: false,
        ringtonePath: 'system_ringtone_default',
        backgroundColor: '#0955fa',
        backgroundUrl: 'https://i.pravatar.cc/500',
        actionColor: '#4CAF50',
        textAccept: 'Accept',
        textDecline: 'Decline',
      ),
      ios: const IOSParams(
        iconName: 'CallKitIcon',
        handleType: 'generic',
        supportsVideo: false,
        maximumCallGroups: 1,
        maximumCallsPerCallGroup: 1,
        audioSessionMode: 'default',
        audioSessionActive: true,
        audioSessionPreferredSampleRate: 44100.0,
        audioSessionPreferredIOBufferDuration: 0.005,
        supportsDTMF: true,
        supportsHolding: false,
        supportsGrouping: false,
        supportsUngrouping: false,
        ringtonePath: 'system_ringtone_default',
      ),
    );

    await FlutterCallkitIncoming.showCallkitIncoming(params);
  }
}
