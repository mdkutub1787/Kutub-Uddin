import 'package:country_picker/country_picker.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/models/profile/active_countries_model.dart' as model;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import '../../core/widgets/brand_sliver_app_bar.dart';
import '../../models/profile/document_types_model.dart';
import '../../models/profile/remitter_types_model.dart';
import '../../models/profile/user_profile_model.dart';
import '../../providers/profile_providers.dart';

class ProfileScreen extends ConsumerStatefulWidget {
  const ProfileScreen({super.key});

  @override
  ConsumerState<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends ConsumerState<ProfileScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _refreshProfile();
    });
  }

  Future<void> _refreshProfile() async {
    ref.invalidate(profileViewModelProvider);
    ref.invalidate(activeCountriesProvider);
    ref.invalidate(remitterTypesProvider);
    ref.invalidate(documentTypesProvider);
    await ref.read(profileViewModelProvider.notifier).loadUserProfile();
  }

  @override
  Widget build(BuildContext context) {
    final profileState = ref.watch(profileViewModelProvider);
    final countriesState = ref.watch(activeCountriesProvider);
    final remitterState = ref.watch(remitterTypesProvider);
    final docTypesState = ref.watch(documentTypesProvider);

    final isLoading = profileState.isLoading ||
        countriesState.isLoading ||
        remitterState.isLoading ||
        docTypesState.isLoading;

    final hasError = profileState.hasError ||
        countriesState.hasError ||
        remitterState.hasError ||
        docTypesState.hasError;

    Object? error;
    if (profileState.hasError) {
      error = profileState.error;
    } else if (countriesState.hasError) {
      error = countriesState.error;
    } else if (remitterState.hasError) {
      error = remitterState.error;
    } else if (docTypesState.hasError) {
      error = docTypesState.error;
    }

    return Scaffold(
      body: RefreshIndicator(
        onRefresh: () async {
          ref.invalidate(profileViewModelProvider);
          ref.invalidate(activeCountriesProvider);
          ref.invalidate(remitterTypesProvider);
          ref.invalidate(documentTypesProvider);
          await ref.read(profileViewModelProvider.notifier).loadUserProfile();
        },
        child: isLoading
            ? const Preloader()
            : hasError
                ? Center(child: Text(context.tr(ErrorHandler.getErrorMessage(error))))
                : _buildProfileContent(
                    context,
                    profileState.value!.userProfile,
                    profileState.value!.languages,
                    countriesState.value!,
                    remitterState.value!,
                    docTypesState.value!,
                    ref,
                  ),
      ),
    );
  }

  Widget _buildProfileContent(
    BuildContext context,
    UserProfileModel userProfile,
    List<Language> languages,
    List<model.Country> countries,
    List<RemitterType> remitters,
    List<DocumentType> docTypes,
    WidgetRef ref,
  ) {
    final theme = Theme.of(context);
    final countryMap = {for (var c in countries) c.id.toString(): c};
    final genderMap = {'1': 'Male', '2': 'Female', '3': 'Others'};
    final remitterMap = {for (var r in remitters) r.id.toString(): r.name};
    final docTypeMap = {for (var d in docTypes) d.id.toString(): d.documentType};
    final languageMap = {for (var l in languages) l.id.toString(): l.name};

    final country = countryMap[userProfile.countryId];
    final issueCountry = countryMap[userProfile.issueCountryCode];
    final preferredLanguage = languageMap[userProfile.languageId];
    String formattedDate = 'N/A';
    if (userProfile.updatedAt != null) {
      try {
        final dateTime = DateTime.parse(userProfile.updatedAt!).toLocal();
        final locale = Localizations.localeOf(context).toString();
        formattedDate = DateFormat.yMMMd(locale).add_jm().format(dateTime);
      } catch (e) {
        formattedDate = userProfile.updatedAt!;
      }
    }

    return CustomScrollView(
      slivers: [
        BrandSliverAppBar(
          leading: IconButton(
            icon: const Icon(Icons.arrow_back, color: Colors.white),
            onPressed: () => context.go(AppRouter.home),
          ),
          expandedHeight: 300.0,
          title: Text(context.tr('My Profile'), style: const TextStyle(color: Colors.white)),
          actions: [
            Padding(
              padding: const EdgeInsets.only(right: 16.0),
              child: Center(
                child: FilledButton.icon(
                  onPressed: () async {
                    await context.push(AppRouter.updateProfile, extra: userProfile);
                    _refreshProfile();
                  },
                  icon: const Icon(Icons.edit, size: 16),
                  label: Text(context.tr('Edit Profile')),
                  style: FilledButton.styleFrom(
                    backgroundColor: theme.colorScheme.onPrimary,
                    foregroundColor: theme.colorScheme.primary,
                    shape: const StadiumBorder(),
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                  ),
                ),
              ),
            ),
          ],
          flexibleSpace: FlexibleSpaceBar(
            background: _buildProfileHeader(context, userProfile, theme),
          ),
        ),
        SliverPadding(
          padding: const EdgeInsets.all(6.0),
          sliver: SliverList(
            delegate: SliverChildListDelegate([
             _buildInfoCard(
                context,
                title: context.tr('Personal Information'),
                icon: Icons.person_outline,
                customRows: [
                  _buildInfoRow(context, context.tr('Full Name'), '${userProfile.firstname ?? ''} ${userProfile.lastname ?? ''}'.trim(), icon: Icons.person),
                  _buildInfoRow(context, context.tr('Username'), userProfile.username ?? context.tr('N/A'), icon: Icons.account_circle),
                  _buildInfoRow(context, context.tr('Email'), userProfile.email ?? context.tr('N/A'), icon: Icons.email),
                  _buildInfoRow(context, context.tr('Phone'), userProfile.phone ?? context.tr('N/A'), icon: Icons.phone),
                  _buildInfoRow(context, context.tr('Date of Birth'), userProfile.dateOfBirth ?? context.tr('N/A'), icon: Icons.cake),
                  _buildInfoRow(context, context.tr('Place of Birth'), userProfile.placeOfBirth ?? context.tr('N/A'), icon: Icons.location_city),
                  _buildInfoRow(context, context.tr('Gender'), genderMap[userProfile.genderType] ?? context.tr('N/A'), icon: Icons.wc),
                  _buildNationalityRow(context, context.tr('Nationality'), userProfile.nationality ?? context.tr('N/A'), icon: Icons.flag),
                ],
              ),
              const SizedBox(height: 6),
              _buildInfoCard(
                context,
                title: context.tr('Address Information'),
                icon: Icons.location_on_outlined,
                customRows: [
                  _buildInfoRow(context, context.tr('Address'), userProfile.address ?? context.tr('N/A'), icon: Icons.home),
                  _buildInfoRow(context, context.tr('City'), userProfile.city ?? context.tr('N/A'), icon: Icons.location_city),
                  _buildInfoRow(context, context.tr('State/Division'), userProfile.state ?? context.tr('N/A'), icon: Icons.map),
                  if (country != null)
                    _buildCountryRow(context, context.tr('Country'), country, icon: Icons.public)
                  else
                    _buildInfoRow(context, context.tr('Country'), userProfile.countryId ?? context.tr('N/A'), icon: Icons.public),
                  _buildInfoRow(context, context.tr('Post Code'), userProfile.postCode ?? context.tr('N/A'), icon: Icons.local_post_office),
                ],
              ),
              const SizedBox(height: 6),
              _buildInfoCard(
                context,
                title: context.tr('Financial Information'),
                icon: Icons.attach_money,
                customRows: [
                  _buildInfoRow(context, context.tr('Occupation'), userProfile.occupation ?? context.tr('N/A'), icon: Icons.work),
                  _buildInfoRow(context, context.tr('Source of Fund'), userProfile.sourceOfFund ?? context.tr('N/A'), icon: Icons.account_balance),
                  _buildInfoRow(context, context.tr('Monthly Income'), userProfile.yearlyIncome ?? context.tr('N/A'), icon: Icons.monetization_on),
                  _buildInfoRow(context, context.tr('Remitter Type'), remitterMap[userProfile.remitterType] ?? context.tr('N/A'), icon: Icons.supervised_user_circle),
                  _buildInfoRow(context, context.tr('Declaration Amount'), userProfile.declarationAmount ?? context.tr('N/A'), icon: Icons.attach_money),
                  _buildInfoRow(context, context.tr('Declaration Period'), (userProfile.declarationStartDate?.isNotEmpty ?? false)
                      ? '${userProfile.declarationStartDate} ${context.tr('to')} ${userProfile.declarationEndDate}'
                      : context.tr('N/A'), icon: Icons.date_range),
                  _buildInfoRow(context, context.tr('Daily Limit'), userProfile.dailyLimit ?? context.tr('N/A'), icon: Icons.timelapse),
                  _buildInfoRow(context, context.tr('Monthly Limit'), userProfile.monthlyLimit ?? context.tr('N/A'), icon: Icons.calendar_month),
                  _buildInfoRow(context, context.tr('Yearly Limit'), userProfile.yearlyLimit ?? context.tr('N/A'), icon: Icons.calendar_today),
                ]
              ),
              const SizedBox(height: 6),
              _buildInfoCard(
                context,
                title: context.tr('Document Information'),
                icon: Icons.description_outlined,
                customRows: [
                  _buildInfoRow(context, context.tr('Document Type'), docTypeMap[userProfile.documentType] ?? context.tr('N/A'), icon: Icons.description),
                  _buildInfoRow(context, context.tr('ID Number'), userProfile.documentIdNumber ?? context.tr('N/A'), icon: Icons.credit_card),
                  if (issueCountry != null)
                    _buildCountryRow(context, context.tr('Issue Country'), issueCountry, icon: Icons.public)
                  else
                    _buildInfoRow(context, context.tr('Issue Country'), userProfile.issueCountryCode ?? context.tr('N/A'), icon: Icons.public),
                  _buildInfoRow(context, context.tr('Expiry Date'), userProfile.documentExpiryDate ?? context.tr('N/A'), icon: Icons.date_range),
                ],
              ),
              const SizedBox(height: 6),
              _buildInfoCard(
                  context,
                  title: context.tr('Preferences'),
                  icon: Icons.settings_suggest_outlined,
                  customRows: [
                    _buildInfoRow(context, context.tr('Preferred Language'), preferredLanguage ?? context.tr('N/A'), icon: Icons.language),
                    _buildInfoRow(context, context.tr('Remarks'), userProfile.remarks ?? context.tr('N/A'), icon: Icons.notes),
                  ]
              ),
              const SizedBox(height: 16),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.history, size: 18, color: theme.hintColor),
                  const SizedBox(width: 8),
                  Text(
                    '${context.tr('Last Updated')}: $formattedDate',
                    style: TextStyle(color: theme.hintColor),
                  ),
                ],
              ),
              const SizedBox(height: 32),
            ]),
          ),
        ),
      ],
    );
  }

  Widget _buildProfileHeader(BuildContext context, UserProfileModel userProfile, ThemeData theme) {
    final imageUrl = userProfile.image;
    final fullName = '${userProfile.firstname ?? ''} ${userProfile.lastname ?? ''}'.trim();

    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [
            theme.colorScheme.primary,
            theme.colorScheme.primary,
          ],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
      ),
      child: SafeArea(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircleAvatar(
                radius: 55,
                backgroundColor: Colors.white.withOpacity(0.9),
                child: CircleAvatar(
                  radius: 52,
                  backgroundColor: theme.disabledColor,
                  backgroundImage: (imageUrl != null && imageUrl.isNotEmpty) ? NetworkImage(imageUrl) : null,
                  child: (imageUrl == null || imageUrl.isEmpty) ? Icon(Icons.person, size: 60, color: theme.hintColor) : null,
                ),
              ),
              const SizedBox(height: 12),
              Text(
                fullName.isNotEmpty ? fullName : context.tr('user_name_placeholder'),
                style: theme.textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.bold, color: Colors.white),
              ),
              const SizedBox(height: 6),
              Text(
                userProfile.occupation ?? 'N/A',
                style: theme.textTheme.bodyLarge?.copyWith(color: Colors.white.withOpacity(0.8)),
              ),
            ],
          )),
    );
  }

  Widget _buildInfoCard(
    BuildContext context,
    {
    required String title,
    required IconData icon,
    List<Widget>? customRows,
  }) {
    final theme = Theme.of(context);
    return Card(
      elevation: 2,
      shadowColor: theme.shadowColor.withOpacity(0.15),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, color: theme.primaryColor, size: 24),
                const SizedBox(width: 12),
                Text(
                  title,
                  style: theme.textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold, fontSize: 18),
                ),
              ],
            ),
            const Divider(height: 24, thickness: 1),
            if (customRows != null)
              ...customRows,
          ],
        ),
      ),
    );
  }

  Widget _buildInfoRow(BuildContext context, String label, String value, {IconData? icon}) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (icon != null)
            Icon(icon, size: 20, color: theme.hintColor)
          else
            const SizedBox(width: 20),
          const SizedBox(width: 16),
          Expanded(child: Text(label, style: TextStyle(color: theme.hintColor))),
          const SizedBox(width: 16),
          Expanded(
            child: Text(
              value,
              textAlign: TextAlign.right,
              style: const TextStyle(fontWeight: FontWeight.w500),
            ),
          ),
        ],
      ),
    );
}

  Widget _buildCountryRow(BuildContext context, String label, model.Country country, {IconData? icon}) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
           if (icon != null)
            Icon(icon, size: 20, color: theme.hintColor)
          else
            const SizedBox(width: 20),
          const SizedBox(width: 16),
          Expanded(child: Text(label, style: TextStyle(color: theme.hintColor))),
          const SizedBox(width: 16),
          Row(
            children: [
              Image.network(country.flag, width: 24, height: 18, fit: BoxFit.cover),
              const SizedBox(width: 8),
              Text(
                country.name,
                style: const TextStyle(fontWeight: FontWeight.w500),
              ),
            ],
          ),
        ],
      ),
    );
  }


  Widget _buildNationalityRow(BuildContext context, String label, String value, {IconData? icon}) {
    final theme = Theme.of(context);
    String? flagEmoji;
    
    if (value != 'N/A') {
      try {
        final country = CountryService().getAll().firstWhere(
            (element) => element.name.toLowerCase() == value.toLowerCase() || 
                         element.displayName.toLowerCase() == value.toLowerCase(),
        );
        flagEmoji = country.flagEmoji;
      } catch (_) {}
    }

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
           if (icon != null)
            Icon(icon, size: 20, color: theme.hintColor)
          else
            const SizedBox(width: 20),
          const SizedBox(width: 16),
          Expanded(child: Text(label, style: TextStyle(color: theme.hintColor))),
          const SizedBox(width: 16),
          if (flagEmoji != null) ...[
             Text(flagEmoji, style: const TextStyle(fontSize: 20)),
             const SizedBox(width: 8),
          ],
          Align(
            alignment: Alignment.centerRight,
            child: Text(
              value,
              textAlign: TextAlign.right,
              style: const TextStyle(fontWeight: FontWeight.w500),
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}
