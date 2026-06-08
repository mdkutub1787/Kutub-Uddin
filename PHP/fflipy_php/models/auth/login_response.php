<?php

class LoginResponse {
    public $status;
    public $token;
    public $user;

    public function __construct($data) {
        $this->status = $data['status'] ?? false;
        $this->token = $data['token'] ?? '';
        
        if (isset($data['user'])) {
            // Re-using UserProfile for the nested user object 
            // as it has all the matching fields from your API response
            $this->user = new UserProfile($data['user']);
        } else {
            $this->user = null;
        }
    }

    public function isSuccessful() {
        return $this->status === true && !empty($this->token);
    }
}
