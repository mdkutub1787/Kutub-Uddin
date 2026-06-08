import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/theme/app_theme.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../core/routing/app_router.dart';
import '../../core/widgets/brand_app_bar.dart';

class SendMoneySuccessScreen extends StatelessWidget {
  final String? referenceNumber;
  final String? message;

  const SendMoneySuccessScreen({super.key, this.referenceNumber, this.message});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isDarkMode = theme.brightness == Brightness.dark;
    final successColor = isDarkMode ? AppTheme.darkSuccess : AppTheme.lightSuccess;
    final warningColor = isDarkMode ? AppTheme.darkWarning : AppTheme.lightWarning;

    return Scaffold(
      backgroundColor: theme.scaffoldBackgroundColor,
      appBar: BrandAppBar(
        title: Text(context.tr('Transaction Successful')),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Card(
                elevation: 4,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(16)),
                color: theme.cardTheme.color,
                child: Padding(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 32, vertical: 40),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        padding: const EdgeInsets.all(4),
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          border: Border.all(
                            color: successColor,
                            width: 2.5,
                          ),
                        ),
                        child: Icon(
                          Icons.check,
                          color: successColor,
                          size: 50,
                        ),
                      ),
                      const SizedBox(height: 24),
                      if (message?.isNotEmpty == true)
                        Text(
                          context.tr(message!),
                          style: TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                            color: warningColor,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      const SizedBox(height: 24),
                      if (referenceNumber != null) ...[
                        const Divider(),
                        const SizedBox(height: 24),
                        Text(
                          context.tr('Reference Number'),
                          style: theme.textTheme.bodyLarge
                              ?.copyWith(color: theme.colorScheme.onSurfaceVariant),
                        ),
                        const SizedBox(height: 8),
                        SelectableText(
                          referenceNumber!,
                          style: theme.textTheme.headlineSmall?.copyWith(
                              fontWeight: FontWeight.bold,
                              color: theme.colorScheme.onSurface),
                        ),
                      ]
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 40),
              if (referenceNumber != null)
                FilledButton(
                  style: FilledButton.styleFrom(
                    backgroundColor: successColor,
                    foregroundColor: isDarkMode ? AppTheme.darkBackground : AppTheme.lightSurface,
                    padding: const EdgeInsets.symmetric(vertical: 16),
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(30)),
                  ),
                  onPressed: () {
                    context.goNamed(
                      'invoiceDetails',
                      pathParameters: {'transactionId': referenceNumber!},
                    );
                  },
                  child: Text(
                    context.tr('View Receipt'),
                    style: const TextStyle(
                        fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                ),
              const SizedBox(height: 16),
              TextButton(
                onPressed: () => context.go(AppRouter.home),
                child: Text(
                  context.tr('Back to Home'),
                  style: TextStyle(
                    color: successColor,
                    fontWeight: FontWeight.bold,
                    fontSize: 16,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}