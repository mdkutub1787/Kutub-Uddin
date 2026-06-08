<?php

class Transaction {
    public $id;
    public $ref_no;
    public $recipient_name;
    public $send_amount;
    public $send_currency;
    public $receive_amount;
    public $receive_currency;
    public $fees;
    public $status;
    public $status_text;
    public $created_at;

    public function __construct($data) {
        $this->id = $data['id'] ?? null;
        $this->ref_no = $data['ref_no'] ?? $data['ref'] ?? '';
        $this->recipient_name = $data['recipient_name'] ?? 'N/A';
        $this->send_amount = $data['send_amount'] ?? '0';
        $this->send_currency = $data['send_curr'] ?? 'EUR';
        $this->receive_amount = $data['receive_amount'] ?? '0';
        $this->receive_currency = $data['receive_curr'] ?? '';
        $this->fees = $data['fees'] ?? '0';
        $this->status = (string)($data['status'] ?? '1');
        $this->created_at = $data['created_at'] ?? $data['tx_paid_date'] ?? '';
    }

    public function getStatusBadge() {
        switch ($this->status) {
            case '1': return 'badge-pending';
            case '2': return 'badge-success';
            case '3': return 'badge-danger';
            default: return 'badge-secondary';
        }
    }

    public function getStatusText() {
        switch ($this->status) {
            case '1': return 'Pending';
            case '2': return 'Completed';
            case '3': return 'Cancelled';
            default: return 'Processing';
        }
    }
}
