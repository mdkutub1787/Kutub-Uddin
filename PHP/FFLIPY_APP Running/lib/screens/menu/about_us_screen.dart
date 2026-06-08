import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:flutter/material.dart';
import '../../core/widgets/brand_app_bar.dart';

class AboutUsScreen extends StatelessWidget {
  const AboutUsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isDark = theme.brightness == Brightness.dark;

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(context.tr('About Fflipy')),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            const SizedBox(height: 40),
            // Header Section with Logo
            Center(
              child: Column(
                children: [
                  Container(
                    width: 100,
                    height: 100,
                    padding: const EdgeInsets.all(20),
                    decoration: BoxDecoration(
                      color: theme.colorScheme.primary.withOpacity(0.1),
                      shape: BoxShape.circle,
                    ),
                    child: Image.asset(
                      'assets/logo/logo.png',
                      fit: BoxFit.contain,
                    ),
                  ),
                  const SizedBox(height: 16),
                  Text(
                    'FFLIPY',
                    style: theme.textTheme.headlineMedium?.copyWith(
                      fontWeight: FontWeight.bold,
                      letterSpacing: 4,
                      color: theme.colorScheme.primary,
                    ),
                  ),
                  Text(
                    context.tr('Fast, Flexible, Intelligent Payments'),
                    style: theme.textTheme.bodySmall?.copyWith(
                      color: theme.hintColor,
                      letterSpacing: 1.2,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 40),

            // Content Section
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _buildSectionTitle(context, context.tr('Who We Are')),
                  Text(
                    context.tr('Fflipy is a next-generation neo-banking platform designed for modern consumers and businesses. We represent the "Triple-F" philosophy: Fast, Flexible, and Financial Freedom. Our platform bridges the gap between traditional banking and the future of digital finance.'),
                    style: theme.textTheme.bodyMedium?.copyWith(height: 1.6),
                  ),
                  const SizedBox(height: 24),

                  _buildSectionTitle(context, context.tr('Our Mission')),
                  Text(
                    context.tr('To empower people across the globe with borderless, secure, and instant financial services. We aim to simplify cross-border remittances, providing transparency and competitive rates for everyone.'),
                    style: theme.textTheme.bodyMedium?.copyWith(height: 1.6),
                  ),
                  const SizedBox(height: 24),

                  _buildSectionTitle(context, context.tr('Key Highlights')),
                  _buildHighlightItem(context, Icons.security_rounded, context.tr('Bank-Grade Security'), context.tr('Your funds are protected by multi-layer encryption and robust security protocols.')),
                  _buildHighlightItem(context, Icons.speed_rounded, context.tr('Instant Transfers'), context.tr('Send money across continents in seconds, not days.')),
                  _buildHighlightItem(context, Icons.language_rounded, context.tr('Global Reach'), context.tr('Connecting you to over 100+ countries with localized payout options.')),
                  
                  const SizedBox(height: 40),

                  // Contact/Footer
                  Center(
                    child: Column(
                      children: [
                        Text(
                          '© ${DateTime.now().year} Fflipy Technologies Ltd.',
                          style: theme.textTheme.bodySmall,
                        ),
                        const SizedBox(height: 4),
                        Text(
                          context.tr('Made with ❤️ for Global Stability'),
                          style: theme.textTheme.bodySmall?.copyWith(color: theme.hintColor),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 40),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSectionTitle(BuildContext context, String title) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Text(
        title,
        style: Theme.of(context).textTheme.titleLarge?.copyWith(
              fontWeight: FontWeight.bold,
              color: Theme.of(context).colorScheme.primary,
            ),
      ),
    );
  }

  Widget _buildHighlightItem(BuildContext context, IconData icon, String title, String desc) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.only(bottom: 20),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.all(10),
            decoration: BoxDecoration(
              color: theme.colorScheme.primary.withOpacity(0.1),
              borderRadius: BorderRadius.circular(12),
            ),
            child: Icon(icon, color: theme.colorScheme.primary, size: 24),
          ),
          const SizedBox(width: 16),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  title,
                  style: theme.textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold),
                ),
                Text(
                  desc,
                  style: theme.textTheme.bodySmall?.copyWith(color: theme.hintColor),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
