<?php

class Beneficiary {
    public $id;
    public $firstname;
    public $lastname;
    public $email;
    public $phone;
    public $country_id;
    public $country_name;
    public $address;
    public $bank_name;
    public $bank_id;
    public $branch_name;
    public $account_number;
    public $account_type;
    public $wallet_name;
    public $wallet_number;
    public $wallet_provider;
    public $payout_type;
    public $transaction_type_name;
    public $relationship_to_sender;
    public $status;
    public $created_at;

    public function __construct($data) {
        $this->id = $data['id'] ?? null;
        $this->firstname = $data['first_name'] ?? $data['firstname'] ?? $data['FirstName'] ?? '';
        $this->lastname = $data['last_name'] ?? $data['lastname'] ?? $data['LastName'] ?? '';
        $this->email = $data['email'] ?? '';
        $this->phone = $data['phone_number'] ?? $data['phone'] ?? $data['Phone'] ?? '';
        $this->country_id = $data['country_id'] ?? $data['CountryCode'] ?? '';
        $this->country_name = $data['country']['name'] ?? $data['country_name'] ?? '';
        $this->address = $data['address'] ?? $data['Address1'] ?? '';
        
        $this->bank_name = $data['bnk_info']['bank_name'] ?? $data['bank_name'] ?? '';
        $this->bank_id = $data['bnk_info_id'] ?? $data['bank_id'] ?? '';
        $this->branch_name = $data['bnk_br_info']['branch_name'] ?? $data['branch_name'] ?? '';
        
        $this->account_number = $data['account_number'] ?? '';
        $this->account_type = $data['account_type'] ?? '';
        
        $this->wallet_name = $data['wallet_name'] ?? '';
        $this->wallet_number = $data['wallet_number'] ?? '';
        $this->wallet_provider = $data['wallet_provider'] ?? '';
        
        $this->payout_type = $data['transaction_type'] ?? $data['payout_type'] ?? '';
        $this->transaction_type_name = $data['transaction_type_name'] ?? '';
        $this->relationship_to_sender = $data['relationship_to_sender'] ?? '';
        $this->status = $data['status'] ?? '';
        $this->created_at = $data['created_at'] ?? '';
    }

    public function getFullName() {
        return trim($this->firstname . ' ' . $this->lastname);
    }
}
