<?php
/**
 * Global Configuration and Helper Functions for Pure PHP MVC
 */
// Core Config
include_once __DIR__ . '/config.php';

// Models
include_once __DIR__ . '/../models/profile/user_profile.php';
include_once __DIR__ . '/../models/beneficiary/beneficiary.php';
include_once __DIR__ . '/../models/transaction/transaction.php';
include_once __DIR__ . '/../models/auth/login_response.php';
include_once __DIR__ . '/../models/auth/registration_response.php';
include_once __DIR__ . '/../models/support/support_ticket.php';
include_once __DIR__ . '/../models/support/ticket_message.php';
include_once __DIR__ . '/../models/notification/notification.php';

// Services (API Layer)
include_once __DIR__ . '/../services/base_service.php';
include_once __DIR__ . '/../services/auth_service.php';
include_once __DIR__ . '/../services/profile_service.php';
include_once __DIR__ . '/../services/transaction_service.php';
include_once __DIR__ . '/../services/support_service.php';
include_once __DIR__ . '/../services/notification_service.php';
include_once __DIR__ . '/../services/beneficiary_service.php';
include_once __DIR__ . '/../services/send_money_service.php';

/**
 * Localization Helper
 */
function __($key) {
    static $translations = null;
    $lang = $_SESSION['lang'] ?? 'en';
    if ($translations === null) {
        $filePath = __DIR__ . "/../assets/lang/{$lang}.json";
        $json = file_exists($filePath) ? file_get_contents($filePath) : '{}';
        $translations = json_decode($json, true);
    }
    return $translations[$key] ?? $key;
}

function format_currency($amount, $currency = 'EUR') {
    return number_format((float)$amount, 2, '.', ',') . ' ' . $currency;
}

function format_date($date_str) {
    return $date_str ? date('d M, Y', strtotime($date_str)) : 'N/A';
}

/**
 * Model Mappers
 */
function map_profile($data) { return new UserProfile($data); }
function map_beneficiaries($list) { return array_map(fn($item) => new Beneficiary($item), is_array($list) ? $list : []); }
function map_transactions($list) { return array_map(fn($item) => new Transaction($item), is_array($list) ? $list : []); }
function map_login_response($data) { return new LoginResponse($data); }
function map_register_response($data) { return new RegistrationResponse($data); }
function map_support_tickets($list) { return array_map(fn($item) => new SupportTicket($item), is_array($list) ? $list : []); }
function map_ticket_messages($list) { return array_map(fn($item) => new TicketMessage($item), is_array($list) ? $list : []); }
function map_notifications($list) { return array_map(fn($item) => new Notification($item), is_array($list) ? $list : []); }
?>
