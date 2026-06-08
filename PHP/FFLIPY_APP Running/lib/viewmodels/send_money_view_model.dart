import 'package:fflipy/models/beneficiary/beneficiary_list_response.dart';
import 'package:fflipy/models/beneficiary/beneficiary_model.dart';
import 'package:fflipy/models/send_money/send_money_cal_service_crg.dart';
import 'package:fflipy/models/send_money/send_money_payment_details.dart';
import 'package:fflipy/models/send_money/send_money_selected_beneficiary.dart';
import 'package:fflipy/models/send_money/send_money_step2_store.dart';
import 'package:fflipy/models/send_money/send_money_step3_store.dart';
import 'package:fflipy/models/send_money/send_money_otp_generate.dart';
import 'package:fflipy/models/send_money/send_money_otp_resend.dart';
import 'package:fflipy/models/send_money/send_money_verify_otp.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../repositories/send_money_repository.dart';

class SendMoneyState {
  final AsyncValue<BeneficiaryListResponse?> beneficiaryListResponse;
  final AsyncValue<SelectSendMoneyBeneficiaryResponse?> selectBeneficiaryResponse;
  final AsyncValue<SendMoneyPaymentDetailsResponse?> paymentDetailsResponse;
  final AsyncValue<SendMoneyCalServiceCrgResponse?> calculationResponse;
  final AsyncValue<SendMoneyStep2StoreResponse?> step2StoreResponse;
  final AsyncValue<SendMoneyStep3StoreResponse?> step3StoreResponse;
  final AsyncValue<SendMoneyOtpGenerateResponse?> otpGenerateResponse;
  final AsyncValue<SendMoneyVerifyOtpResponse?> verifyOtpResponse;
  final AsyncValue<SendMoneyOtpResendResponse?> resendOtpResponse;
  final BeneficiaryModel? selectedBeneficiary;
  final String? sessionToken;
  final bool isSelectingBeneficiary;

  const SendMoneyState({
    this.beneficiaryListResponse = const AsyncValue.data(null),
    this.selectBeneficiaryResponse = const AsyncValue.data(null),
    this.paymentDetailsResponse = const AsyncValue.data(null),
    this.calculationResponse = const AsyncValue.data(null),
    this.step2StoreResponse = const AsyncValue.data(null),
    this.step3StoreResponse = const AsyncValue.data(null),
    this.otpGenerateResponse = const AsyncValue.data(null),
    this.verifyOtpResponse = const AsyncValue.data(null),
    this.resendOtpResponse = const AsyncValue.data(null),
    this.selectedBeneficiary,
    this.sessionToken,
    this.isSelectingBeneficiary = false,
  });

  bool get isLoading =>
      isSelectingBeneficiary ||
          beneficiaryListResponse is AsyncLoading ||
          selectBeneficiaryResponse is AsyncLoading ||
          paymentDetailsResponse is AsyncLoading ||
          calculationResponse is AsyncLoading ||
          step2StoreResponse is AsyncLoading ||
          step3StoreResponse is AsyncLoading ||
          otpGenerateResponse is AsyncLoading ||
          verifyOtpResponse is AsyncLoading ||
          resendOtpResponse is AsyncLoading;


  SendMoneyState copyWith({
    AsyncValue<BeneficiaryListResponse?>? beneficiaryListResponse,
    AsyncValue<SelectSendMoneyBeneficiaryResponse?>? selectBeneficiaryResponse,
    AsyncValue<SendMoneyPaymentDetailsResponse?>? paymentDetailsResponse,
    AsyncValue<SendMoneyCalServiceCrgResponse?>? calculationResponse,
    AsyncValue<SendMoneyStep2StoreResponse?>? step2StoreResponse,
    AsyncValue<SendMoneyStep3StoreResponse?>? step3StoreResponse,
    AsyncValue<SendMoneyOtpGenerateResponse?>? otpGenerateResponse,
    AsyncValue<SendMoneyVerifyOtpResponse?>? verifyOtpResponse,
    AsyncValue<SendMoneyOtpResendResponse?>? resendOtpResponse,
    BeneficiaryModel? selectedBeneficiary,
    String? sessionToken,
    bool? isSelectingBeneficiary,
    bool clearSelectedBeneficiary = false,
  }) {
    return SendMoneyState(
      beneficiaryListResponse: beneficiaryListResponse ?? this.beneficiaryListResponse,
      selectBeneficiaryResponse: selectBeneficiaryResponse ?? this.selectBeneficiaryResponse,
      paymentDetailsResponse: paymentDetailsResponse ?? this.paymentDetailsResponse,
      calculationResponse: calculationResponse ?? this.calculationResponse,
      step2StoreResponse: step2StoreResponse ?? this.step2StoreResponse,
      step3StoreResponse: step3StoreResponse ?? this.step3StoreResponse,
      otpGenerateResponse: otpGenerateResponse ?? this.otpGenerateResponse,
      verifyOtpResponse: verifyOtpResponse ?? this.verifyOtpResponse,
      resendOtpResponse: resendOtpResponse ?? this.resendOtpResponse,
      selectedBeneficiary: clearSelectedBeneficiary ? null : selectedBeneficiary ?? this.selectedBeneficiary,
      sessionToken: clearSelectedBeneficiary ? null : sessionToken ?? this.sessionToken,
      isSelectingBeneficiary: isSelectingBeneficiary ?? this.isSelectingBeneficiary,
    );
  }
}

class SendMoneyViewModel extends StateNotifier<SendMoneyState> {
  final SendMoneyRepository _sendMoneyRepository;

  SendMoneyViewModel(this._sendMoneyRepository) : super(const SendMoneyState()) {
    getBeneficiaries();
  }

  Future<void> getBeneficiaries({int page = 1}) async {
    state = state.copyWith(beneficiaryListResponse: const AsyncValue.loading());
    try {
      final data = await _sendMoneyRepository.getBeneficiaries(page: page);
      state = state.copyWith(beneficiaryListResponse: AsyncValue.data(data));
    } catch (e, stackTrace) {
      state = state.copyWith(beneficiaryListResponse: AsyncValue.error(e, stackTrace));
    }
  }

  Future<bool> proceedToConfirmation() async {
    final transactionToken = await storeStep2Details();
    if (transactionToken != null) {
      final step3Success = await storeStep3Details(transactionToken: transactionToken);
      return step3Success;
    } else {
      return false;
    }
  }

  Future<void> selectBeneficiary(BeneficiaryModel beneficiary) async {
    resetBeneficiarySelection();
    await Future.delayed(const Duration(milliseconds: 10));
    state = state.copyWith(isSelectingBeneficiary: true, selectBeneficiaryResponse: const AsyncValue.loading());
    try {
      final response = await _sendMoneyRepository.selectBeneficiary(beneficiary.id);
      state = state.copyWith(
        isSelectingBeneficiary: false,
        selectBeneficiaryResponse: AsyncValue.data(response),
        selectedBeneficiary: beneficiary,
        sessionToken: response.data.sessionToken,
      );
      await getPaymentDetails();
    } catch (e) {
      state = state.copyWith(
        isSelectingBeneficiary: false,
        selectBeneficiaryResponse: AsyncValue.error(e, StackTrace.current),
        selectedBeneficiary: null,
      );
    }
  }

  void unselectBeneficiary() {
    resetBeneficiarySelection();
  }

  Future<void> getPaymentDetails() async {
    if (state.sessionToken == null) return;
    state = state.copyWith(paymentDetailsResponse: const AsyncValue.loading());
    try {
      final response = await _sendMoneyRepository.getPaymentDetails(state.sessionToken!);
      state = state.copyWith(paymentDetailsResponse: AsyncValue.data(response));
    } catch (e) {
      state = state.copyWith(paymentDetailsResponse: AsyncValue.error(e, StackTrace.current));
    }
  }

  Future<void> calculateServiceCharge({
    required double amount,
    required int fromCountryId,
    required int toCountryId,
  }) async {
    if (state.sessionToken == null || state.selectedBeneficiary == null) return;
    state = state.copyWith(calculationResponse: const AsyncValue.loading());
    try {
      final response = await _sendMoneyRepository.calculateServiceCharge(
        sessionToken: state.sessionToken!,
        beneficiaryId: state.selectedBeneficiary!.id,
        amount: amount,
        fromCountryId: fromCountryId,
        toCountryId: toCountryId,
      );
      state = state.copyWith(calculationResponse: AsyncValue.data(response));
    } catch (e) {
      state = state.copyWith(calculationResponse: AsyncValue.error(e, StackTrace.current));
    }
  }

  Future<String?> storeStep2Details() async {
    final sessionToken = state.sessionToken;
    final selectedBeneficiary = state.selectedBeneficiary;
    final calcData = state.calculationResponse.asData?.value?.data;
    final paymentData = state.paymentDetailsResponse.asData?.value?.data;

    if (sessionToken == null || selectedBeneficiary == null || calcData == null || paymentData == null) {
      return null;
    }

    state = state.copyWith(step2StoreResponse: const AsyncValue.loading());
    try {
      final response = await _sendMoneyRepository.storeStep2Details(
        sessionToken: sessionToken,
        beneficiaryId: selectedBeneficiary.id,
        sendAmount: double.parse(calcData.sendAmount),
        senderCurrencyId: paymentData.senderCountry.id,
        receiverCurrencyId: paymentData.receiverCountry.id,
        fee: double.parse(calcData.fee),
        exchangeRate: double.parse(calcData.exchangeRate),
        receivedAmount: double.parse(calcData.receivedAmount),
      );
      state = state.copyWith(step2StoreResponse: AsyncValue.data(response));
      return response.data?.transactionToken;
    } catch (e) {
      state = state.copyWith(step2StoreResponse: AsyncValue.error(e, StackTrace.current));
      return null;
    }
  }

  Future<bool> storeStep3Details({required String transactionToken}) async {
    state = state.copyWith(step3StoreResponse: const AsyncValue.loading());
    try {
      final response = await _sendMoneyRepository.storeStep3Details(transactionToken: transactionToken);
      state = state.copyWith(step3StoreResponse: AsyncValue.data(response));
      return response.success;
    } catch (e) {
      state = state.copyWith(step3StoreResponse: AsyncValue.error(e, StackTrace.current));
      return false;
    }
  }

  Future<void> generateOtp({required String transactionToken}) async {
    state = state.copyWith(otpGenerateResponse: const AsyncValue.loading());
    try {
      final response = await _sendMoneyRepository.generateOtp(transactionToken: transactionToken);
      state = state.copyWith(otpGenerateResponse: AsyncValue.data(response));
    } catch (e) {
      state = state.copyWith(otpGenerateResponse: AsyncValue.error(e, StackTrace.current));
    }
  }

  Future<SendMoneyVerifyOtpResponse?> verifyOtp({
    required String transactionToken,
    required String otp,
    required int purposeOfTransfer,
    String? remarks,
  }) async {
    state = state.copyWith(verifyOtpResponse: const AsyncValue.loading());
    try {
      final response = await _sendMoneyRepository.verifyOtp(
        transactionToken: transactionToken,
        otp: otp,
        purposeOfTransfer: purposeOfTransfer,
        remarks: remarks ?? '',
      );
      state = state.copyWith(verifyOtpResponse: AsyncValue.data(response));
      return response;
    } catch (e) {
      state = state.copyWith(verifyOtpResponse: AsyncValue.error(e, StackTrace.current));
      return null;
    }
  }

  Future<SendMoneyOtpResendResponse?> resendOtp({required String transactionToken}) async {
    state = state.copyWith(resendOtpResponse: const AsyncValue.loading());
    try {
      final response = await _sendMoneyRepository.resendOtp(transactionToken: transactionToken);
      state = state.copyWith(resendOtpResponse: AsyncValue.data(response));
      return response;
    } catch (e) {
      state = state.copyWith(resendOtpResponse: AsyncValue.error(e, StackTrace.current));
      return null;
    }
  }

  void resetBeneficiarySelection() {
    state = state.copyWith(
      clearSelectedBeneficiary: true,
      selectBeneficiaryResponse: const AsyncValue.data(null),
      paymentDetailsResponse: const AsyncValue.data(null),
      calculationResponse: const AsyncValue.data(null),
      step2StoreResponse: const AsyncValue.data(null),
      step3StoreResponse: const AsyncValue.data(null),
      otpGenerateResponse: const AsyncValue.data(null),
      verifyOtpResponse: const AsyncValue.data(null),
      resendOtpResponse: const AsyncValue.data(null),
    );
  }

  void changeFromCountry(Country newCountry) {
    final currentPaymentDetails = state.paymentDetailsResponse.asData?.value;
    if (currentPaymentDetails == null) return;
    final newCountryDetails = CountryDetails(
      id: newCountry.id,
      name: newCountry.name,
      code: newCountry.code,
      rate: newCountry.rate,
      image: newCountry.image,
      flag: newCountry.flag,
      minimumAmount: int.tryParse(newCountry.minimumAmount.split('.').first),
      maximumAmount: int.tryParse(newCountry.maximumAmount.split('.').first),
    );
    final updatedPaymentData = currentPaymentDetails.data.copyWith(senderCountry: newCountryDetails);
    state = state.copyWith(paymentDetailsResponse: AsyncValue.data(currentPaymentDetails.copyWith(data: updatedPaymentData)));
    resetCalculation();
  }

  void changeToCountry(Country newCountry) {
    final currentPaymentDetails = state.paymentDetailsResponse.asData?.value;
    if (currentPaymentDetails == null) return;
    final newCountryDetails = CountryDetails(
      id: newCountry.id,
      name: newCountry.name,
      code: newCountry.code,
      rate: newCountry.rate,
      image: newCountry.image,
      flag: newCountry.flag,
      minimumAmount: int.tryParse(newCountry.minimumAmount.split('.').first),
      maximumAmount: int.tryParse(newCountry.maximumAmount.split('.').first),
    );
    final updatedPaymentData = currentPaymentDetails.data.copyWith(receiverCountry: newCountryDetails);
    state = state.copyWith(paymentDetailsResponse: AsyncValue.data(currentPaymentDetails.copyWith(data: updatedPaymentData)));
    resetCalculation();
  }

  void resetCalculation() {
    state = state.copyWith(calculationResponse: const AsyncValue.data(null));
  }

  void resetAmountStep() {
    state = state.copyWith(
      selectBeneficiaryResponse: const AsyncValue.data(null),
      calculationResponse: const AsyncValue.data(null),
      step2StoreResponse: const AsyncValue.data(null),
    );
  }
}
