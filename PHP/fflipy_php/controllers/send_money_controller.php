<?php
/**
 * SendMoneyController - Standard PHP MVC
 */
session_start();
require_once __DIR__ . '/../includes/functions.php';

class SendMoneyController {
    public function index() {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        
        $breadcrumb = 'Send Money';
        $header_title = 'Transfer Money';
        
        // This is a complex multi-step process, for now just show a placeholder view or the start of the process
        include __DIR__ . '/../views/send_money/send_money_view.php';
    }
}

(new SendMoneyController())->index();
?>
