<?php
// API Configuration
define('API_BASE_URL', 'https://dev.fflipy.com/api/');
define('SITE_URL', 'https://dev.fflipy.com/');

/**
 * Main API Request Wrapper based on Flutter ApiConfig
 */
function api_request($endpoint, $method = 'GET', $data = [], $token = null) {
    if (strpos($endpoint, 'https') === 0) {
        $url = $endpoint;
    } else {
        $url = API_BASE_URL . $endpoint;
    }
    
    $ch = curl_init();
    
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    
    $hasFile = false;
    foreach ($data as $value) {
        if ($value instanceof CURLFile) {
            $hasFile = true;
            break;
        }
    }

    $headers = [
        'Accept: application/json'
    ];
    if (!$hasFile) {
        $headers[] = 'Content-Type: application/json';
    }
    
    if ($token) {
        $headers[] = 'Authorization: Bearer ' . $token;
    }
    
    $method = strtoupper($method);
    if ($method === 'POST') {
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $hasFile ? $data : json_encode($data));
    } elseif ($method === 'DELETE' || $method === 'PUT' || $method === 'PATCH') {
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $method);
        if (!empty($data)) {
            curl_setopt($ch, CURLOPT_POSTFIELDS, $hasFile ? $data : json_encode($data));
        }
    }
    
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    
    $response = curl_exec($ch);
    $error = curl_error($ch);
    curl_close($ch);
    
    if ($error) return ['status' => 'error', 'message' => $error];
    $decoded = json_decode($response, true);
    if (json_last_error() !== JSON_ERROR_NONE) {
        return ['status' => 'error', 'message' => 'Invalid JSON response from API'];
    }
    return $decoded;
}

/**
 * API Endpoints mapping from api_config.dart
 */
class ApiEndpoints {
    // Auth
    const LOGIN = 'apilogin';
    const LOGOUT = 'user/logoutmobile';
    const REGISTER = 'register_mobile';
    const MAIL_VERIFY = 'user/mail-verify';
    const RESEND_CODE = 'user/resend_code';
    const FORGOT_PASSWORD = 'user/password/reset';
    const UPDATE_PASSWORD = 'user/mobile_profile/update-password';
    
    // Profile
    const GET_PROFILE = 'user/mobile_profile';
    const UPDATE_PROFILE = 'user/mobile_profile/update-info';
    const DOCUMENT_INFO = 'user/mobile/document-info';
    const DOCUMENT_TYPES = 'user/mobile/document-types';
    const ACTIVE_COUNTRIES = 'user/mobile/active-countries';
    const REMITTER_TYPES = 'user/mobile/remitter-types';
    const GENDER_TYPES = 'user/mobile/gender-types';
    
    // Beneficiary
    const GET_BENEFICIARIES = 'user/mobile_beneficiary_info';
    const ADD_BENEFICIARY = 'user/mobile_beneficiary/store';
    const ACCOUNT_TYPES = 'user/account-types';
    static function UPDATE_BENEFICIARY($id) { return "user/mobile-beneficiary-update/$id"; }
    static function DELETE_BENEFICIARY($id) { return "user/beneficiary/delete/$id"; }
    static function GET_BANKS($countryId) { return "user/country/$countryId/banks"; }
    static function GET_BRANCHES($bankId) { return "user/bank/$bankId/branches"; }
    static function GET_FACILITIES($countryId) { return "user/country/$countryId/facilities"; }
    static function GET_WALLETS($countryId) { return "user/country/$countryId/wallet-providers"; }
    
    // Transactions
    const TRANSACTION_REPORT = 'user/transaction_report';
    const TRACK_TRANSFER = 'user/track-transfer';
    static function CANCEL_TRANSACTION($id) { return "user/mobile-transaction-cancel/$id"; }
    static function GET_INVOICE($id) { return "user/payment/invoice/$id"; }
    
    // Send Money
    const SEND_MONEY_CALC = 'user/send-money-cal-service-crg';
    const SEND_MONEY_STEP2 = 'user/send-money-step2-store';
    const SEND_MONEY_STEP3 = 'user/send-money-step3-store';
    const OTP_GENERATE = 'user/send-money-otp-generate';
    const OTP_VERIFY = 'user/send-money-verify-otp';
    const OTP_RESEND = 'user/send-money-otp-resend';
    
    // Notifications
    const NOTIFICATIONS = 'user/notification-show';
    static function READ_NOTIFICATION($id) { return "user/read-at/$id"; }
    
    // Support
    const SUPPORT_TICKETS = 'user/mobile-ticket-list';
    const STORE_TICKET = 'user/mobile-ticket-store';
    static function REPLY_TICKET($id) { return "user/mobile-ticket-reply/$id"; }
    static function VIEW_TICKET($id) { return "user/mobile-view/$id"; }
}

// Session management & Language
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

if (!isset($_SESSION['lang'])) {
    $_SESSION['lang'] = 'bn'; // Default to Bengali as requested
}

if (isset($_GET['lang'])) {
    $_SESSION['lang'] = $_GET['lang'] == 'en' ? 'en' : 'bn';
}
?>
