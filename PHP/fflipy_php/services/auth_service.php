<?php
require_once __DIR__ . '/base_service.php';

class AuthService extends BaseService {
    
    public function login($username, $password) {
        $data = [
            'username' => $username,
            'password' => $password
        ];
        $response = $this->request(ApiEndpoints::LOGIN, 'POST', $data);
        return map_login_response($response);
    }

    public function register($formData) {
        $response = $this->request(ApiEndpoints::REGISTER, 'POST', $formData);
        return map_register_response($response);
    }

    public function forgotPassword($email) {
        return $this->request(ApiEndpoints::FORGOT_PASSWORD, 'POST', ['email' => $email]);
    }

    public function updatePassword($currentPassword, $newPassword, $confirmPassword) {
        $data = [
            'current_password' => $currentPassword,
            'password' => $newPassword,
            'password_confirmation' => $confirmPassword
        ];
        return $this->request(ApiEndpoints::UPDATE_PASSWORD, 'POST', $data);
    }

    public function verifyEmail($code) {
        return $this->request(ApiEndpoints::MAIL_VERIFY, 'POST', ['code' => $code]);
    }

    public function resendCode($type = 'email') {
        return $this->request(ApiEndpoints::RESEND_CODE, 'POST', ['type' => $type]);
    }

    public function logout() {
        return $this->request(ApiEndpoints::LOGOUT, 'POST');
    }
}
