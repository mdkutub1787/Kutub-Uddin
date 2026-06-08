<?php
/**
 * DashboardController - Standard PHP MVC
 */
session_start();
require_once __DIR__ . '/../includes/functions.php';

class DashboardController {
    public function index() {
        // Auth check
        if (!isset($_SESSION['token'])) {
            header('Location: ../auth/login.php');
            exit;
        }

        $transactionService = new TransactionService();
        $userProfile = unserialize($_SESSION['user']);

        try {
            $recent_transactions = $transactionService->getRecentTransactions();
            
            // UI Helpers
            $hour = date('H');
            $greet_key = ($hour < 12) ? 'good_morning' : (($hour < 18) ? 'good_afternoon' : 'good_evening');
            
            // Send data to View
            $transactions = $recent_transactions;
            $user = $userProfile;
            
            include __DIR__ . '/../views/dashboard/dashboard_view.php';
        } catch (Exception $e) {
            // Log error and redirect
            header('Location: ../auth/login.php?error=expired');
        }
    }
}

$controller = new DashboardController();
$controller->index();
?>
