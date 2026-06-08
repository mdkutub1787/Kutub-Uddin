import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/routing/app_router.dart';
import 'package:fflipy/core/widgets/preloader.dart';
import 'package:fflipy/models/beneficiary/beneficiary_list_response.dart';
import 'package:fflipy/models/beneficiary/beneficiary_model.dart';
import 'package:fflipy/core/widgets/empty_state_widget.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../core/utils/dialog_helper.dart';
import '../../core/widgets/brand_app_bar.dart';
import '../../providers/beneficiary_providers.dart';
import '../../providers/auth_providers.dart';

class BeneficiaryScreen extends ConsumerStatefulWidget {
  const BeneficiaryScreen({super.key});

  @override
  ConsumerState<BeneficiaryScreen> createState() => _BeneficiaryScreenState();
}

class _BeneficiaryScreenState extends ConsumerState<BeneficiaryScreen> {
  final _scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(beneficiaryViewModelProvider.notifier).loadBeneficiaries());
    _scrollController.addListener(_scrollListener);
  }

  @override
  void dispose() {
    _scrollController.removeListener(_scrollListener);
    _scrollController.dispose();
    super.dispose();
  }

  void _scrollListener() {
    if (_scrollController.position.pixels >=
        _scrollController.position.maxScrollExtent * 0.9) {
      final viewModel = ref.read(beneficiaryViewModelProvider.notifier);
      final state = ref.read(beneficiaryViewModelProvider).value;
      if (state != null && state.beneficiaries.currentPage < state.beneficiaries.lastPage) {
        viewModel.loadBeneficiaries(isLoadMore: true);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final beneficiaryState = ref.watch(beneficiaryViewModelProvider);

    return beneficiaryState.when(
      loading: () => Scaffold(
        appBar: BrandAppBar(
          leading: IconButton(
            icon: const Icon(Icons.arrow_back),
            onPressed: () => context.go(AppRouter.home),
          ),
          title: Text(context.tr('Beneficiaries')),
        ),
        body: const Preloader(),
      ),
      error: (error, stackTrace) {
        if (kDebugMode) {
          print('Beneficiary Screen Error: $error\n$stackTrace');
        }
        return Scaffold(
          appBar: BrandAppBar(
            leading: IconButton(
              icon: const Icon(Icons.arrow_back),
              onPressed: () => context.go(AppRouter.home),
            ),
            title: Text(context.tr('Beneficiaries')),
          ),
          body: _buildErrorWidget(context, '${context.tr('Failed to load beneficiaries')}: $error'),
        );
      },
      data: (beneficiaryInfo) {
        return Scaffold(
          appBar: BrandAppBar(
            leading: IconButton(
              icon: const Icon(Icons.arrow_back),
              onPressed: () => context.go(AppRouter.home),
            ),
            title: Text(context.tr('Beneficiaries')),
          ),
          body: Column(
            children: [
              Padding(
                padding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
                child: TextField(
                  onChanged: (query) =>
                  ref.read(searchQueryProvider.notifier).state = query,
                  decoration: InputDecoration(
                    hintText: context.tr('Search by name, bank, or account'),
                    prefixIcon: const Icon(Icons.search),
                  ),
                ),
              ),
              Expanded(
                child: _buildBeneficiaryList(context, ref, beneficiaryInfo),
              ),
            ],
          ),
          floatingActionButton: FloatingActionButton.extended(
            onPressed: () => context.push(AppRouter.addBeneficiary),
            label: Text(context.tr('Add Beneficiary')),
            icon: const Icon(Icons.add),
          ),
        );
      },
    );
  }

  Widget _buildBeneficiaryList(
      BuildContext context, WidgetRef ref, Data beneficiaryInfo) {
    final searchQuery = ref.watch(searchQueryProvider).toLowerCase();
    final filteredData =
    beneficiaryInfo.beneficiaries.data.where((beneficiary) {
      if (searchQuery.isEmpty) return true;
      final fullName =
          '${beneficiary.firstName} ${beneficiary.lastName}'.toLowerCase();
      final bankName = (beneficiary.bnkInfo?.bankName ?? '').toLowerCase();
      final accountNumber = (beneficiary.accountNumber ?? '').toLowerCase();
      return fullName.contains(searchQuery) ||
          bankName.contains(searchQuery) ||
          accountNumber.contains(searchQuery);
    }).toList();

    if (filteredData.isEmpty) {
      return _buildEmptyState(context, isSearch: searchQuery.isNotEmpty);
    }

    return RefreshIndicator(
      onRefresh: () => ref
          .read(beneficiaryViewModelProvider.notifier)
          .loadBeneficiaries(),
      child: ListView.builder(
        controller: _scrollController,
        itemCount: filteredData.length + (beneficiaryInfo.beneficiaries.currentPage < beneficiaryInfo.beneficiaries.lastPage ? 1 : 0),
        itemBuilder: (context, index) {
          if (index == filteredData.length) {
            return const Preloader();
          }
          final beneficiary = filteredData[index];
          return BeneficiaryListItem(
            beneficiary: beneficiary,
            beneficiaryInfo: beneficiaryInfo,
          );
        },
      ),
    );
  }

  Widget _buildEmptyState(BuildContext context, {bool isSearch = false}) {
    return EmptyStateWidget(
      message: isSearch ? context.tr('No Results Found') : context.tr('No Beneficiaries'),
      subtitle: isSearch
          ? context.tr('Your search did not match any beneficiaries.')
          : context.tr("Click 'Add Beneficiary' to get started."),
    );
  }

  Widget _buildErrorWidget(BuildContext context, String message) {
    final theme = Theme.of(context);
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Text(
          message,
          textAlign: TextAlign.center,
          style: theme.textTheme.bodyMedium?.copyWith(
            color: theme.colorScheme.error,
          ),
        ),
      ),
    );
  }
}

class BeneficiaryListItem extends ConsumerWidget {
  final BeneficiaryModel beneficiary;
  final Data beneficiaryInfo;

  const BeneficiaryListItem(
      {super.key, required this.beneficiary, required this.beneficiaryInfo});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final theme = Theme.of(context);
    final initial = (beneficiary.firstName.isNotEmpty)
        ? beneficiary.firstName[0].toUpperCase()
        : '?';
    final fullName = '${beneficiary.firstName} ${beneficiary.lastName}';

    String subtitleText;

    if (beneficiary.transactionType == '1' || beneficiary.bnkInfo != null) {
      subtitleText = '${beneficiary.bnkInfo?.bankName ?? context.tr('Bank')} • ${beneficiary.accountNumber ?? ''}';
    }
    else if (beneficiary.transactionType == '6') {
      final walletProviderName = beneficiary.countryService?.name;
      subtitleText = '${walletProviderName ?? context.tr('Wallet Top Up')} • ${beneficiary.walletNumber ?? ''}';
    } else {
      subtitleText = beneficiary.transactionTypeName ?? context.tr('Receiving Method');
    }

    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        color: theme.cardColor,
        boxShadow: [
          BoxShadow(
            color: Colors.red.withAlpha((0.1 * 255).toInt()),
            blurRadius: 10,
            offset: const Offset(-4, -4),
          ),
          BoxShadow(
            color: Colors.green.withAlpha((0.1 * 255).toInt()),
            blurRadius: 10,
            offset: const Offset(4, -4),
          ),
          BoxShadow(
            color: Colors.amber.withAlpha((0.1 * 255).toInt()),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          onTap: () => _showBeneficiaryDetails(context, beneficiary, beneficiaryInfo, ref),
          borderRadius: BorderRadius.circular(16),
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Row(
              children: [
                CircleAvatar(
                  radius: 26,
                  backgroundColor: theme.colorScheme.primary.withAlpha((0.1 * 255).toInt()),
                  child: Text(initial,
                      style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 20,
                          color: theme.colorScheme.primary)),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        fullName,
                        style: theme.textTheme.titleMedium
                            ?.copyWith(fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        subtitleText,
                        style: theme.textTheme.bodySmall?.copyWith(
                            color: theme.colorScheme.onSurface.withAlpha((255 * 0.6).toInt()),
                            fontWeight: FontWeight.w500),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ],
                  ),
                ),
                Icon(Icons.arrow_forward_ios_rounded, size: 16, color: theme.colorScheme.primary.withAlpha(150)),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

void _showBeneficiaryDetails(BuildContext context, BeneficiaryModel beneficiary,
    Data beneficiaryInfo, WidgetRef ref) {
  final theme = Theme.of(context);
  showModalBottomSheet(
    context: context,
    isScrollControlled: true,
    backgroundColor: Colors.transparent,
    builder: (context) {
      return DraggableScrollableSheet(
        initialChildSize: 0.7,
        minChildSize: 0.4,
        maxChildSize: 0.9,
        builder: (_, scrollController) {
          return Container(
            decoration: BoxDecoration(
              color: theme.colorScheme.surface,
              borderRadius: const BorderRadius.vertical(top: Radius.circular(24)),
            ),
            child: _BeneficiaryDetailsSheet(
              beneficiary: beneficiary,
              beneficiaryInfo: beneficiaryInfo,
              scrollController: scrollController,
              ref: ref,
            ),
          );
        },
      );
    },
  );
}

class _BeneficiaryDetailsSheet extends StatelessWidget {
  final BeneficiaryModel beneficiary;
  final Data beneficiaryInfo;
  final ScrollController scrollController;
  final WidgetRef ref;

  const _BeneficiaryDetailsSheet({
    required this.beneficiary,
    required this.beneficiaryInfo,
    required this.scrollController,
    required this.ref,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final address = [
      beneficiary.address,
      beneficiary.city,
      beneficiary.country?.name
    ].where((s) => s != null && s.isNotEmpty).join(', ');
    final fullName = '${beneficiary.firstName} ${beneficiary.lastName}'.trim();
    final relationshipMap = {
      for (var r in beneficiaryInfo.relationships) r.id.toString(): r.title
    };
    final relationship = 
        relationshipMap[beneficiary.relationshipToSender] ??
            beneficiary.relationshipToSender;

    final accountTypeMap = {
      for (var at in beneficiaryInfo.accountTypes) at.id: at.name
    };
    final accountType = 
        accountTypeMap[beneficiary.accountType] ?? beneficiary.accountType;

    return Stack(
      children: [
        Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 16.0),
              child: Container(
                width: 40,
                height: 5,
                decoration: BoxDecoration(
                  color: theme.dividerColor,
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
            ),
            Expanded(
              child: ListView(
                padding: const EdgeInsets.all(24),
                controller: scrollController,
                children: [
                  Center(
                    child: CircleAvatar(
                      radius: 40,
                      backgroundColor: theme.colorScheme.primaryContainer,
                      child: Text(
                        (fullName.isNotEmpty)
                            ? fullName[0].toUpperCase()
                            : '?'.toUpperCase(),
                        style: TextStyle(
                            fontSize: 32, fontWeight: FontWeight.bold, color: theme.colorScheme.onPrimaryContainer),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Center(
                    child: Text(
                      fullName,
                      style: theme.textTheme.headlineSmall
                          ?.copyWith(fontWeight: FontWeight.bold),
                    ),
                  ),
                  if (beneficiary.email?.isNotEmpty ?? false)
                    Center(
                      child: Text(
                        beneficiary.email!,
                        style: theme.textTheme.bodyMedium?.copyWith(color: theme.colorScheme.onSurfaceVariant),
                      ),
                    ),
                  const SizedBox(height: 24),
                  _buildInfoCard(
                    context,
                    title: 'Personal Information',
                    children: [
                      _buildDetailRow(context, Icons.phone_outlined, 'Phone',
                          beneficiary.phoneNumber ?? ''),
                      _buildDetailRow(
                          context, Icons.location_on_outlined, 'Address', address),
                      _buildDetailRow(context, Icons.person_outline,
                          'Relationship', relationship),
                    ],
                  ),
                  const SizedBox(height: 16),
                  _buildInfoCard(
                    context,
                    title: 'Receiving Details',
                    children: [
                      if (beneficiary.transactionType == '1' ||
                          beneficiary.bnkInfo != null) ...[
                        _buildDetailRow(
                            context,
                            Icons.account_balance_outlined,
                            'Bank',
                            beneficiary.bnkInfo?.bankName ?? 'N/A'),
                        _buildDetailRow(
                            context,
                            Icons.location_city,
                            'Branch',
                            beneficiary.bnkBrInfo?.branchName ?? 'N/A'),
                        if (accountType != null)
                          _buildDetailRow(context, Icons.receipt_long_outlined,
                              'Account Type', accountType),
                        _buildDetailRow(context, Icons.pin_outlined, 'Account No.',
                            beneficiary.accountNumber ?? 'N/A'),
                      ] else if (beneficiary.transactionType == '6') ...[
                        _buildDetailRow(
                            context,
                            Icons.wallet_outlined,
                            'Wallet Provider',
                            beneficiary.countryService?.name ?? 'N/A'),
                        _buildDetailRow(
                            context,
                            Icons.phone_android_outlined,
                            'Wallet Number',
                            beneficiary.walletNumber ?? 'N/A'),
                      ] else ...[
                        _buildDetailRow(
                            context,
                            Icons.business_center_outlined,
                            'Method',
                            beneficiary.transactionTypeName ?? 'N/A'),
                      ],
                    ],
                  ),
                  const SizedBox(height: 32),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton.icon(
                      onPressed: () => _showDeleteConfirmationDialog(context, beneficiary, ref),
                      icon: const Icon(Icons.delete_outline_rounded, size: 22),
                      label: Text(context.tr('Delete Beneficiary')),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.red.shade50,
                        foregroundColor: Colors.red.shade700,
                        elevation: 0,
                        padding: const EdgeInsets.symmetric(vertical: 16),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(16),
                          side: BorderSide(color: Colors.red.shade100, width: 1),
                        ),
                        textStyle: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
        Positioned(
          top: 0,
          right: 0,
          child: IconButton(
            icon: const Icon(Icons.close),
            onPressed: () => Navigator.of(context).pop(),
          ),
        ),
      ],
    );
  }

  Widget _buildInfoCard(BuildContext context,
      {required String title, required List<Widget> children}) {
    final theme = Theme.of(context);
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        color: theme.cardColor,
        boxShadow: [
          BoxShadow(
            color: Colors.red.withAlpha((0.08 * 255).toInt()),
            blurRadius: 10,
            offset: const Offset(-4, -4),
          ),
          BoxShadow(
            color: Colors.green.withAlpha((0.08 * 255).toInt()),
            blurRadius: 10,
            offset: const Offset(4, -4),
          ),
          BoxShadow(
            color: Colors.amber.withAlpha((0.08 * 255).toInt()),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Container(
                  width: 4,
                  height: 18,
                  decoration: BoxDecoration(
                    color: theme.colorScheme.primary,
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),
                const SizedBox(width: 8),
                Text(
                  title,
                  style: theme.textTheme.titleMedium?.copyWith(
                    fontWeight: FontWeight.bold,
                    color: theme.colorScheme.onSurface,
                  ),
                ),
              ],
            ),
            const Padding(
              padding: EdgeInsets.symmetric(vertical: 12),
              child: Divider(height: 1, thickness: 0.5),
            ),
            ...children,
          ],
        ),
      ),
    );
  }

  Widget _buildDetailRow(
      BuildContext context, IconData icon, String label, String value) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, size: 20, color: theme.colorScheme.primary),
          const SizedBox(width: 16),
          Text(label, style: theme.textTheme.bodyLarge),
          const SizedBox(width: 16),
          Expanded(
            child: Text(
              value,
              textAlign: TextAlign.end,
              style: theme.textTheme.bodyMedium?.copyWith(color: theme.colorScheme.onSurfaceVariant),
            ),
          ),
        ],
      ),
    );
  }
}

void _showDeleteConfirmationDialog(BuildContext context, BeneficiaryModel beneficiary, WidgetRef ref) async {
  final confirmed = await DialogHelper.showDeleteConfirmation(
    context: context,
    title: context.tr('Delete Beneficiary'),
    message: '${context.tr('Are you sure you want to delete')} ${beneficiary.firstName} ${beneficiary.lastName}?',
    confirmText: context.tr('Delete'),
    cancelText: context.tr('Cancel'),
  );

  if (confirmed == true && context.mounted) {
    DialogHelper.showLoadingDialog(context);

    try {
      await ref.read(deleteBeneficiaryProvider(beneficiary.id).future);

      if (context.mounted) {
        Navigator.of(context).pop();
        Navigator.of(context).pop();
      }

      if (context.mounted) {
        DialogHelper.showSnackBar(context, context.tr('Beneficiary deleted successfully'));
      }
      
      ref.read(beneficiaryViewModelProvider.notifier).loadBeneficiaries();

    } catch (e, s) {
      if (kDebugMode) {
        print('Error deleting beneficiary: $e\n$s');
      }
      if (context.mounted) {
        Navigator.of(context).pop();
      }
      if (context.mounted) {
        DialogHelper.showSnackBar(context, context.tr(ErrorHandler.getErrorMessage(e)), isError: true);
      }
    }
  }
}
