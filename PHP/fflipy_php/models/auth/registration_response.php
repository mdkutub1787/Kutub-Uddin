<?php
/**
 * Registration Response Model
 */
class RegistrationResponse {
    public $success;
    public $message;
    public $token;
    public $user;

    public function __construct($data) {
        $this->success = $data['success'] ?? false;
        $this->message = $data['message'] ?? '';
        $this->token = $data['data']['token'] ?? '';
        
        if (isset($data['data']['user'])) {
            $this->user = new UserProfile($data['data']['user']);
        } else {
            $this->user = null;
        }
    }

    public function isSuccessful() {
        return $this->success === true && !empty($this->token);
    }
}
?>
