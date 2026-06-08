import 'package:fflipy/core/widgets/preloader.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/widgets/brand_app_bar.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import '../../providers/localization_provider.dart';

class LanguageSettingsScreen extends ConsumerStatefulWidget {
  const LanguageSettingsScreen({super.key});

  @override
  ConsumerState<LanguageSettingsScreen> createState() => _LanguageSettingsScreenState();
}

class _LanguageSettingsScreenState extends ConsumerState<LanguageSettingsScreen> {
  bool _isLoading = false;

  Future<void> _changeLanguage(Future<void> Function() languageSetter) async {
    setState(() => _isLoading = true);
    try {
      await languageSetter();
    } finally {
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context);

    return Scaffold(
      appBar: BrandAppBar(
        title: Text(loc.translate('Language')),
      ),
      body: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  loc.translate('Preferred language'),
                  style: Theme.of(context).textTheme.titleLarge,
                ),
                const SizedBox(height: 16),
                ListTile(
                  title: const Text('English'),
                  onTap: () => _changeLanguage(ref.read(localeProvider.notifier).setEnglish),
                  trailing: ref.watch(localeProvider).languageCode == 'en'
                      ? const Icon(Icons.check, color: Colors.green)
                      : null,
                ),
                ListTile(
                  title: const Text('Español'),
                  onTap: () => _changeLanguage(ref.read(localeProvider.notifier).setSpanish),
                  trailing: ref.watch(localeProvider).languageCode == 'es'
                      ? const Icon(Icons.check, color: Colors.green)
                      : null,
                ),
                ListTile(
                  title: const Text('বাংলা'),
                  onTap: () => _changeLanguage(ref.read(localeProvider.notifier).setBangla),
                  trailing: ref.watch(localeProvider).languageCode == 'bn'
                      ? const Icon(Icons.check, color: Colors.green)
                      : null,
                ),
              ],
            ),
          ),
          if (_isLoading)
            const Preloader(),
        ],
      ),
    );
  }
}
