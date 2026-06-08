class ApiConfig {
    static const String baseUrl = 'https://dev.fflipy.com/api/';
    static const String siteUrl = 'https://dev.fflipy.com/';

    /// Currency API endpoints
    static const String currencyListUrl = 'mobile/currencyList';

    /// Auth API endpoints Start from here
    static const String registerMobileUrl = 'register_mobile';
    static const String mailVerifyUrl = 'user/mail-verify';
    static const String resendCodeUrl = 'user/resend_code';
    static const String loginUrl = 'apilogin';
    static const String logoutUrl = 'user/logoutmobile';
    static const String updatePasswordUrl = 'user/mobile_profile/update-password';
    static const String forgotPasswordUrl = 'mobile/forgot-password';

    /// Profile API endpoints Start from here
    static const String getProfileUrl = 'user/mobile_profile';
    static const String updateProfileUrl = 'user/mobile_profile/update-info';
    static const String documentInfoUrl = 'user/mobile/document-info';
    static const String documentTypesUrl = 'user/mobile/document-types';
    static const String activeCountriesUrl = 'user/mobile/active-countries';
    static const String remitterTypesUrl = 'user/mobile/remitter-types';

    /// Beneficiary API endpoints Start from here
    static const String getBeneficiariesUrl = 'user/mobile_beneficiary_info';
    static const String addBeneficiaryUrl = 'user/mobile_beneficiary/store';
    static String deleteBeneficiaryUrl(int id) => 'user/beneficiary/delete/$id';
    static String updateBeneficiaryUrl(int id) => 'user/mobile-beneficiary-update/$id';
    static String banksUrl(String countryId) => 'user/country/$countryId/banks';
    static String branchesUrl(String bankId) => 'user/bank/$bankId/branches';
    static String facilitiesUrl(String countryId) => 'user/country/$countryId/facilities';
    static String walletProvidersUrl(String countryId) => 'user/country/$countryId/wallet-providers';
    static const String accountTypesUrl = 'user/account-types';

    /// Send Money API endpoints Start from here
    static const String selectSendMoneyBeneficiaryUrl = 'user/select-send-money-beneficiary';
    static const String sendMoneyPaymentUrl = 'user/send-money-payment';
    static const String sendMoneyCalServiceCrgUrl = 'user/send-money-cal-service-crg';
    static const String sendMoneyStep2StoreUrl = 'user/send-money-step2-store';
    static const String sendMoneyStep3StoreUrl = 'user/send-money-step3-store';
    static const String sendMoneyOtpGenerateUrl = 'user/send-money-otp-generate';
    static const String sendMoneyVerifyOtpUrl = 'user/send-money-verify-otp';
    static const String sendMoneyOtpResendUrl = 'user/send-money-otp-resend';

    /// Transaction API endpoints
    static const String transactionReportUrl = 'user/transaction_report';
    static const String trackTransferUrl = 'user/track-transfer';
    static String cancelTransactionUrl(int id) => 'user/mobile-transaction-cancel/$id';

    /// Invoice API Endpoints
    static String getInvoiceUrl(String transactionId) => 'user/payment/invoice/$transactionId';

    /// Notification API Endpoints
    static const String notificationUrl = 'user/notification-show';
    static String notificationReadUrl(int id) => 'user/read-at/$id';

    /// Support Ticket API Endpoints
    static const String supportTicketListUrl = 'user/mobile-ticket-list';
    static const String storeTicketUrl = 'user/mobile-ticket-store';
    static String replyTicketUrl(String id) => 'user/mobile-ticket-reply/$id';
    static String viewTicketUrl(String ticketId) => 'user/mobile-view/$ticketId';

}
