<?php

class TicketMessage {
    public $message;
    public $admin_id;
    public $created_at;
    public $isAdmin;

    public function __construct($data) {
        $this->message = $data['message'] ?? '';
        $this->admin_id = $data['admin_id'] ?? null;
        $this->created_at = $data['created_at'] ?? '';
        $this->isAdmin = !empty($this->admin_id);
    }
}
