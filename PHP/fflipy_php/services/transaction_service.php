<?php
require_once __DIR__ . '/base_service.php';

class TransactionService extends BaseService {

    /**
     * Get all transactions with pagination
     */
    public function getTransactions($page = 1) {
        $endpoint = ApiEndpoints::TRANSACTION_REPORT . "?page=" . $page;
        $response = $this->request($endpoint, 'GET');
        $data = $response['data']['transactions']['data'] ?? [];
        return map_transactions($data);
    }

    /**
     * Alias for getTransactions for semantic use in dashboard
     */
    public function getRecentTransactions($limit = 5) {
        $transactions = $this->getTransactions(1);
        return array_slice($transactions, 0, $limit);
    }

    /**
     * Alias for TransactionController
     */
    public function getAllTransactions() {
        return $this->getTransactions(1);
    }

    public function trackTransfer($refNo) {
        return $this->request(ApiEndpoints::TRACK_TRANSFER, 'POST', ['ref_no' => $refNo]);
    }

    public function getInvoice($txId) {
        $response = $this->request(ApiEndpoints::GET_INVOICE($txId), 'GET');
        $data = $response['data']['transaction'] ?? $response['data'] ?? [];
        return new Transaction($data);
    }

    public function cancelTransaction($id) {
        return $this->request(ApiEndpoints::CANCEL_TRANSACTION($id), 'POST');
    }
}
?>
