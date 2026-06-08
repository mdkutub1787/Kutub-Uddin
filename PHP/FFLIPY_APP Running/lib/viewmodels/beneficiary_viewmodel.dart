import 'package:fflipy/models/beneficiary/account_type_model.dart';
import 'package:fflipy/models/beneficiary/add_beneficiary_request.dart';
import 'package:fflipy/models/beneficiary/add_beneficiary_response.dart';
import 'package:fflipy/models/beneficiary/beneficiary_list_response.dart';
import 'package:fflipy/models/beneficiary/update_beneficiary_request.dart';
import 'package:fflipy/models/beneficiary/update_beneficiary_response.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../repositories/beneficiary_repository.dart';

class BeneficiaryViewModel extends StateNotifier<AsyncValue<Data>> {
  final BeneficiaryRepository _beneficiaryRepository;
  int _currentPage = 1;
  bool _isFetching = false;

  BeneficiaryViewModel(this._beneficiaryRepository)
      : super(const AsyncValue.loading()) {
    loadBeneficiaries();
  }

  Future<void> loadBeneficiaries({bool isLoadMore = false}) async {
    if (_isFetching) return;
    _isFetching = true;

    if (!isLoadMore) {
      _currentPage = 1;
      state = const AsyncValue.loading();
    }

    try {
      var newInfo = await _beneficiaryRepository.getBeneficiaryInfo(page: _currentPage);

      if (newInfo.accountTypes.isEmpty) {
        final currentState = state.valueOrNull;
        if (currentState != null && currentState.accountTypes.isNotEmpty) {
          newInfo = newInfo.copyWith(accountTypes: currentState.accountTypes);
        } else {
          try {
            final types = await _beneficiaryRepository.getAccountTypes();
            if (types.isNotEmpty) {
              newInfo = newInfo.copyWith(accountTypes: types);
            }
          } catch (_) {
            // Ignore error, will fallback to IDs
          }
        }
      }

      final currentState = state.valueOrNull;

      if (isLoadMore && currentState != null) {
        final allBeneficiaries = [
          ...currentState.beneficiaries.data,
          ...newInfo.beneficiaries.data
        ];
        final newBeneficiaryData = newInfo.beneficiaries.copyWith(
            data: allBeneficiaries,
            currentPage: newInfo.beneficiaries.currentPage,
            lastPage: newInfo.beneficiaries.lastPage
        );
        
        state = AsyncValue.data(newInfo.copyWith(
          beneficiaries: newBeneficiaryData,
        ));
      } else {
        state = AsyncValue.data(newInfo);
      }
      _currentPage++;
    } catch (e, s) {
      if (!isLoadMore) {
        state = AsyncValue.error(e, s);
      }
    } finally {
      _isFetching = false;
    }
  }

  Future<AddBeneficiaryResponse> addBeneficiary(AddBeneficiaryRequest request) async {
    final response = await _beneficiaryRepository.addBeneficiary(request);
    if (response.status) {
      await loadBeneficiaries();
    }
    return response;
  }

  Future<UpdateBeneficiaryResponse> updateBeneficiary(UpdateBeneficiaryRequest request) async {
    final response = await _beneficiaryRepository.updateBeneficiary(request);
    if (response.status) {
      await loadBeneficiaries();
    }
    return response;
  }

  Future<void> deleteBeneficiary(int id) async {
    try {
      await _beneficiaryRepository.deleteBeneficiary(id);
      state.whenData((info) {
        final updatedBeneficiaries =
            info.beneficiaries.data.where((b) => b.id != id).toList();
        final newBeneficiaries =
            info.beneficiaries.copyWith(data: updatedBeneficiaries);
        state = AsyncValue.data(info.copyWith(beneficiaries: newBeneficiaries));
      });
    } catch (e, s) {
      state = AsyncValue.error(e, s);
    }
  }

  Future<List<AccountType>> getAccountTypes() {
    return _beneficiaryRepository.getAccountTypes();
  }
}
