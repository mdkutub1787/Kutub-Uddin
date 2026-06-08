<?php
require_once __DIR__ . '/base_service.php';

class SendMoneyService extends BaseService {

    public function calculate($data) {
        return $this->request(ApiEndpoints::SEND_MONEY_CALC, 'POST', $data);
    }

    public function step2($data) {
        return $this->request(ApiEndpoints::SEND_MONEY_STEP2, 'POST', $data);
    }

    public function step3($data) {
        return $this->request(ApiEndpoints::SEND_MONEY_STEP3, 'POST', $data);
    }

    public function generateOtp($transactionToken) {
        return $this->request(ApiEndpoints::OTP_GENERATE, 'POST', ['transaction_token' => $transactionToken]);
    }

    public function verifyOtp($data) {
        return $this->request(ApiEndpoints::OTP_VERIFY, 'POST', $data);
    }

    public function resendOtp($transactionToken) {
        return $this->request(ApiEndpoints::OTP_RESEND, 'POST', ['transaction_token' => $transactionToken]);
    }
}
