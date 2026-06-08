<?php
/**
 * TransactionController - Standard PHP MVC
 */
session_start();
require_once __DIR__ . '/../includes/functions.php';

class TransactionController {
    private $service;

    public function __construct() {
        $this->service = new TransactionService();
    }

    public function index() {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        try {
            $transactions = $this->service->getAllTransactions();
            include __DIR__ . '/../views/transactions/transactions_view.php';
        } catch (Exception $e) { $error = $e->getMessage(); include __DIR__ . '/../views/transactions/transactions_view.php'; }
    }

    public function invoice($id) {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        try {
            $transaction = $this->service->getInvoice($id);
            include __DIR__ . '/../views/transactions/invoice_view.php';
        } catch (Exception $e) { header('Location: transactions.php?error=not_found'); }
    }

    public function track($ref_no = null) {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        $trackData = null;
        if ($ref_no) {
            try { $trackData = $this->service->trackTransfer($ref_no); } 
            catch (Exception $e) { $error = $e->getMessage(); }
        }
        include __DIR__ . '/../views/transactions/track_transfer_view.php';
    }
}

$controller = new TransactionController();
$action = $_GET['action'] ?? 'index';
if ($action === 'invoice') {
    $controller->invoice($_GET['id'] ?? '');
} elseif ($action === 'track') {
    $controller->track($_GET['ref_no'] ?? null);
} else {
    $controller->index();
}
?>
