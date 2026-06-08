<?php

class BaseService {
    protected $token;

    public function __construct($token = null) {
        $this->token = $token ?: ($_SESSION['token'] ?? null);
    }

    protected function request($endpoint, $method = 'GET', $data = []) {
        return api_request($endpoint, $method, $data, $this->token);
    }
}
