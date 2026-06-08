import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:qr_flutter/qr_flutter.dart';
import '../../providers/auth_providers.dart';
import '../../core/widgets/brand_app_bar.dart';

class QRCodeScreen extends ConsumerWidget {
  const QRCodeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final user = ref.watch(authViewModelProvider).responseModelUser;
    final phone = user?.user?.phone ?? user?.user?.mobile ?? '';
    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('My QR')),
      ),
      body: phone.isEmpty
          ? Center(child: Text(context.tr('No phone number found.')))
          : Center(
        child: QrImageView(
          data: phone,
          size: 220.0,
        ),
      ),
    );
  }
}
