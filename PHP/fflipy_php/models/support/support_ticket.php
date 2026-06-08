<?php

class SupportTicket {
    public $id;
    public $ticket_id;
    public $subject;
    public $status;
    public $last_reply;
    public $created_at;
    public $messages = [];

    public function __construct($data) {
        $this->id = $data['id'] ?? null;
        $this->ticket_id = $data['ticket'] ?? $data['ticket_id'] ?? '';
        $this->subject = $data['subject'] ?? '';
        $this->status = (string)($data['status'] ?? '1');
        $this->last_reply = $data['last_reply'] ?? '';
        $this->created_at = $data['created_at'] ?? '';
        
        if (isset($data['messages'])) {
            $this->messages = $data['messages'];
        }
    }

    public function getStatusBadge() {
        switch ($this->status) {
            case '1': return 'status-open'; // Open
            case '2': return 'status-pending'; // Answered/Pending
            case '3': return 'status-closed'; // Closed
            default: return 'status-pending';
        }
    }

    public function getStatusText() {
        $is_bn = ($_SESSION['lang'] ?? 'en') == 'bn';
        switch ($this->status) {
            case '0': return $is_bn ? 'ওপেন' : 'Open';
            case '1': return $is_bn ? 'অপেক্ষমাণ' : 'Answered';
            case '2': return $is_bn ? 'replied' : 'Replied';
            case '3': return $is_bn ? 'বন্ধ' : 'Closed';
            default: return $is_bn ? 'অজ্ঞাত' : 'Unknown';
        }
    }
}
