import 'package:fflipy/models/beneficiary/beneficiary_list_response.dart';
import 'package:fflipy/models/send_money/send_money_cal_service_crg.dart';
import 'package:fflipy/models/send_money/send_money_payment_details.dart';
import 'package:fflipy/models/send_money/send_money_selected_beneficiary.dart';
import 'package:fflipy/models/send_money/send_money_step2_store.dart';
import 'package:fflipy/models/send_money/send_money_step3_store.dart';
import 'package:fflipy/models/send_money/send_money_otp_generate.dart';
import 'package:fflipy/models/send_money/send_money_otp_resend.dart';
import 'package:fflipy/models/send_money/send_money_verify_otp.dart';
import 'package:fflipy/services/send_money_service.dart';

class SendMoneyRepository {
  final SendMoneyService _sendMoneyService;

  SendMoneyRepository(this._sendMoneyService);

  Future<BeneficiaryListResponse> getBeneficiaries({int page = 1}) async {
    return await _sendMoneyService.getBeneficiaries(page: page);
  }

  Future<SelectSendMoneyBeneficiaryResponse> selectBeneficiary(int beneficiaryId) async {
    return await _sendMoneyService.selectBeneficiary(beneficiaryId);
  }

  Future<SendMoneyPaymentDetailsResponse> getPaymentDetails(String sessionToken) async {
    return await _sendMoneyService.getPaymentDetails(sessionToken);
  }

  Future<SendMoneyCalServiceCrgResponse> calculateServiceCharge(
      {required String sessionToken,
      required int beneficiaryId,
      required double amount,
      required int fromCountryId,
      required int toCountryId}) async {
    return await _sendMoneyService.calculateServiceCharge(
      sessionToken: sessionToken,
      beneficiaryId: beneficiaryId,
      amount: amount,
      fromCountryId: fromCountryId,
      toCountryId: toCountryId,
    );
  }

  Future<SendMoneyStep2StoreResponse> storeStep2Details(
      {required String sessionToken,
      required int beneficiaryId,
      required double sendAmount,
      required int senderCurrencyId,
      required int receiverCurrencyId,
      required double fee,
      required double exchangeRate,
      required double receivedAmount}) async {
    return await _sendMoneyService.storeStep2Details(
      sessionToken: sessionToken,
      beneficiaryId: beneficiaryId,
      sendAmount: sendAmount,
      senderCurrencyId: senderCurrencyId,
      receiverCurrencyId: receiverCurrencyId,
      fee: fee,
      exchangeRate: exchangeRate,
      receivedAmount: receivedAmount,
    );
  }

  Future<SendMoneyStep3StoreResponse> storeStep3Details({required String transactionToken}) async {
    return await _sendMoneyService.storeStep3Details(transactionToken: transactionToken);
  }

  Future<SendMoneyOtpGenerateResponse> generateOtp({required String transactionToken}) async {
    return await _sendMoneyService.generateOtp(transactionToken: transactionToken);
  }

  Future<SendMoneyVerifyOtpResponse> verifyOtp(
      {required String transactionToken, required String otp, required int purposeOfTransfer, required String remarks}) async {
    return await _sendMoneyService.verifyOtp(
      transactionToken: transactionToken,
      otp: otp,
      purposeOfTransfer: purposeOfTransfer,
      remarks: remarks,
    );
  }

  Future<SendMoneyOtpResendResponse> resendOtp({required String transactionToken}) async {
    return await _sendMoneyService.resendOtp(transactionToken: transactionToken);
  }
}
