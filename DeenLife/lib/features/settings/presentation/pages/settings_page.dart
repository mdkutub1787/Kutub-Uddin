import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:geolocator/geolocator.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:adhan/adhan.dart';
import '../../../../core/providers/language_provider.dart';
import '../../../prayer_times/presentation/providers/prayer_times_provider.dart';

class SettingsPage extends ConsumerWidget {
  const SettingsPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final calcMethod = ref.watch(calculationMethodProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Settings'),
        centerTitle: true,
      ),
      body: ListView(
        children: [
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: Text(
              'App Configuration',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Colors.grey,
              ),
            ),
          ),
          ListTile(
            leading: const Icon(Icons.location_on),
            title: const Text('Location Settings'),
            subtitle: const Text('Manage location for prayer times'),
            trailing: const Icon(Icons.arrow_forward_ios, size: 16),
            onTap: () async {
              await Geolocator.openLocationSettings();
            },
          ),
          ListTile(
            leading: const Icon(Icons.calculate),
            title: const Text('Prayer Calculation Method'),
            subtitle: Text(_getCalcMethodName(calcMethod)),
            trailing: const Icon(Icons.arrow_forward_ios, size: 16),
            onTap: () {
              _showCalcMethodDialog(context, ref, calcMethod);
            },
          ),
          ListTile(
            leading: const Icon(Icons.notifications),
            title: const Text('Azan Notifications'),
            subtitle: const Text('Configure prayer alerts'),
            trailing: Switch(
              value: true,
              onChanged: (val) {
                 ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Notifications enabled.')),
                );
              },
            ),
          ),
          const Divider(),
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: Text(
              'App Language',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Colors.grey,
              ),
            ),
          ),
          Consumer(
            builder: (context, ref, child) {
              final currentLang = ref.watch(languageProvider);
              return Column(
                children: [
                  RadioListTile<AppLanguage>(
                    title: const Text('English'),
                    value: AppLanguage.english,
                    groupValue: currentLang,
                    onChanged: (val) {
                      if (val != null) ref.read(languageProvider.notifier).setLanguage(val);
                    },
                  ),
                  RadioListTile<AppLanguage>(
                    title: const Text('বাংলা (Bengali)'),
                    subtitle: const Text('Foundation added. Translations pending.'),
                    value: AppLanguage.bengali,
                    groupValue: currentLang,
                    onChanged: (val) {
                      if (val != null) ref.read(languageProvider.notifier).setLanguage(val);
                    },
                  ),
                ],
              );
            },
          ),
          const Divider(),
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: Text(
              'About',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Colors.grey,
              ),
            ),
          ),
          ListTile(
            leading: const Icon(Icons.info),
            title: const Text('DeenLife App'),
            subtitle: const Text('Version 1.0.0'),
          ),
          ListTile(
            leading: const Icon(Icons.star),
            title: const Text('Rate Us'),
            trailing: const Icon(Icons.open_in_new, size: 16),
            onTap: () async {
              final url = Uri.parse('https://play.google.com/store/apps');
              if (await canLaunchUrl(url)) {
                await launchUrl(url, mode: LaunchMode.externalApplication);
              }
            },
          ),
          ListTile(
            leading: const Icon(Icons.privacy_tip),
            title: const Text('Privacy Policy'),
            trailing: const Icon(Icons.open_in_new, size: 16),
            onTap: () async {
               final url = Uri.parse('https://google.com');
              if (await canLaunchUrl(url)) {
                await launchUrl(url, mode: LaunchMode.externalApplication);
              }
            },
          ),
        ],
      ),
    );
  }

  String _getCalcMethodName(CalculationMethod method) {
    switch (method) {
      case CalculationMethod.karachi:
        return 'University of Islamic Sciences, Karachi';
      case CalculationMethod.muslim_world_league:
        return 'Muslim World League';
      case CalculationMethod.umm_al_qura:
        return 'Umm al-Qura University, Makkah';
      case CalculationMethod.egyptian:
        return 'Egyptian General Authority of Survey';
      case CalculationMethod.north_america:
        return 'ISNA (North America)';
      default:
        return 'Other';
    }
  }

  void _showCalcMethodDialog(BuildContext context, WidgetRef ref, CalculationMethod currentMethod) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Calculation Method'),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                CalculationMethod.karachi,
                CalculationMethod.muslim_world_league,
                CalculationMethod.umm_al_qura,
                CalculationMethod.egyptian,
                CalculationMethod.north_america,
              ].map((method) {
                return RadioListTile<CalculationMethod>(
                  title: Text(_getCalcMethodName(method)),
                  value: method,
                  groupValue: currentMethod,
                  onChanged: (val) {
                    if (val != null) {
                      ref.read(calculationMethodProvider.notifier).state = val;
                      Navigator.pop(context);
                    }
                  },
                );
              }).toList(),
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
          ],
        );
      },
    );
  }
}
