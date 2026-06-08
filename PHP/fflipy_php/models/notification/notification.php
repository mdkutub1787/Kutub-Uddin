<?php

class Notification {
    public $id;
    public $message;
    public $read_at;
    public $created_at;
    public $isRead;

    public function __construct($data) {
        $this->id = $data['id'] ?? null;
        $this->message = $data['message'] ?? $data['data']['message'] ?? '';
        $this->read_at = $data['read_at'] ?? null;
        $this->created_at = $data['created_at'] ?? '';
        $this->isRead = !empty($this->read_at);
    }
}
