import 'package:fflipy/core/widgets/preloader.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:fflipy/providers/localization_provider.dart';

class OnboardingScreen extends ConsumerStatefulWidget {
  const OnboardingScreen({Key? key}) : super(key: key);

  @override
  ConsumerState<OnboardingScreen> createState() => _OnboardingScreenState();
}

class _OnboardingScreenState extends ConsumerState<OnboardingScreen> {
  late PageController _pageController;
  int _currentPage = 0;
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _pageController = PageController();
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  Future<void> _completeOnboarding() async {
    setState(() => _isLoading = true);
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setBool('hasSeenOnboarding', true);
      if (mounted) {
        context.go(AppRouter.login);
      }
    } finally {
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final localizations = AppLocalizations.of(context);
    final theme = Theme.of(context);

    final onboardingSlides = [
      _OnboardingSlide(
        title: localizations.translate("Easy Transfers"),
        description: localizations.translate(
            "Send money to anyone, anywhere instantly with just a few taps."),
        icon: Icons.touch_app,
      ),
      _OnboardingSlide(
        title: localizations.translate("Secure & Safe"),
        description: localizations.translate(
            "Your transactions are protected by industry-leading security protocols."),
        icon: Icons.security,
      ),
      _OnboardingSlide(
        title: localizations.translate("Global Access"),
        description: localizations.translate(
            "Access your account and manage finances from anywhere in the world."),
        icon: Icons.rocket_launch,
      ),
    ];

    return Scaffold(
      body: Stack(
        children: [
          SafeArea(
            child: Column(
              children: [
                Padding(
                  padding: const EdgeInsets.symmetric(
                      horizontal: 16.0, vertical: 8.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      _buildLanguageSelector(),
                      TextButton(
                        onPressed: _isLoading ? null : _completeOnboarding,
                        child: Text(localizations.translate("Skip")),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: PageView.builder(
                    controller: _pageController,
                    onPageChanged: (index) {
                      setState(() {
                        _currentPage = index;
                      });
                    },
                    itemCount: onboardingSlides.length,
                    itemBuilder: (context, index) {
                      return _buildSlide(onboardingSlides[index]);
                    },
                  ),
                ),
                _buildBottomControls(
                    localizations, theme, onboardingSlides.length),
              ],
            ),
          ),
          if (_isLoading) const Preloader(),
        ],
      ),
    );
  }

  Widget _buildLanguageSelector() {
    final theme = Theme.of(context);
    final locale = ref.watch(localeProvider);
    final langOptions = [
      {'flag': '🇬🇧', 'label': context.tr('English'), 'code': 'en'},
      {'flag': '🇧🇩', 'label': context.tr('Bangla'), 'code': 'bn'},
      {'flag': '🇪🇸', 'label': context.tr('Spanish'), 'code': 'es'},
    ];
    int selectedIndex = langOptions.indexWhere((l) => l['code'] == locale.languageCode);
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: theme.dividerColor),
      ),
      child: DropdownButton<int>(
        value: selectedIndex >= 0 ? selectedIndex : 0,
        items: List.generate(langOptions.length, (i) =>
            DropdownMenuItem<int>(
              value: i,
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(langOptions[i]['flag']!, style: const TextStyle(fontSize: 20)),
                  const SizedBox(width: 8),
                  Text(langOptions[i]['label']!, style: theme.textTheme.bodyMedium),
                ],
              ),
            )),
        onChanged: (int? newIndex) {
          if (newIndex != null) {
            final code = langOptions[newIndex]['code'];
            if (code == 'en') {
              ref.read(localeProvider.notifier).setEnglish();
            } else if (code == 'es') {
              ref.read(localeProvider.notifier).setSpanish();
            } else if (code == 'bn') {
              ref.read(localeProvider.notifier).setLocale(const Locale('bn'));
            }
          }
        },
        underline: Container(),
        isExpanded: false,
        borderRadius: BorderRadius.circular(12),
        dropdownColor: theme.colorScheme.surface,
        style: theme.textTheme.bodyMedium,
      ),
    );
  }

  Widget _buildBottomControls(
      AppLocalizations localizations, ThemeData theme, int slideCount) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 32),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Row(
            children: List.generate(
              slideCount,
              (index) => AnimatedContainer(
                duration: const Duration(milliseconds: 300),
                margin: const EdgeInsets.only(right: 8),
                width: _currentPage == index ? 30 : 10,
                height: 10,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(5),
                  color: _currentPage == index
                      ? theme.colorScheme.primary
                      : theme.colorScheme.primary.withOpacity(0.3),
                ),
              ),
            ),
          ),
          FloatingActionButton(
            onPressed: () {
              if (_currentPage == slideCount - 1) {
                _completeOnboarding();
              } else {
                _pageController.nextPage(
                  duration: const Duration(milliseconds: 400),
                  curve: Curves.easeInOut,
                );
              }
            },
            elevation: 2,
            child: Icon(
              _currentPage == slideCount - 1
                  ? Icons.check
                  : Icons.arrow_forward,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSlide(_OnboardingSlide slide) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 40),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(slide.icon, size: 120, color: theme.colorScheme.primary),
          const SizedBox(height: 48),
          Text(
            slide.title,
            textAlign: TextAlign.center,
            style: theme.textTheme.headlineLarge?.copyWith(
              fontWeight: FontWeight.bold,
              fontSize: 28,
            ),
          ),
          const SizedBox(height: 16),
          Text(
            slide.description,
            textAlign: TextAlign.center,
            style: theme.textTheme.titleMedium?.copyWith(
              color: theme.textTheme.bodyMedium?.color?.withOpacity(0.7),
            ),
          ),
        ],
      ),
    );
  }
}

class _OnboardingSlide {
  final String title;
  final String description;
  final IconData icon;

  _OnboardingSlide({
    required this.title,
    required this.description,
    required this.icon,
  });
}
