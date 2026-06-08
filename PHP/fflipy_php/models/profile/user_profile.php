<?php

class UserProfile {
    public $id;
    public $firstname;
    public $lastname;
    public $username;
    public $email;
    public $phone;
    public $date_of_birth;
    public $occupation;
    public $address;
    public $city;
    public $state;
    public $post_code;
    public $country;
    public $nationality;
    public $source_of_fund;
    public $yearly_income;
    public $daily_limit;
    public $monthly_limit;
    public $yearly_limit;
    public $remarks;
    public $gender_type;
    public $remitter_type;
    public $country_id;
    public $language_id;
    public $place_of_birth;
    public $declaration_amount;
    public $declaration_start_date;
    public $declaration_end_date;
    
    public $document_type;
    public $document_id_number;
    public $document_expiry_date;
    public $document_issue_date;
    public $issue_country_code;
    
    public $image;
    public $referral_code;
    public $email_verification;
    public $sms_verification;

    public function __construct($data) {
        $user = $data['user'] ?? $data;
        $profile = $data['user_profile'] ?? $data['otherInfo'] ?? $data;
        $doc = $data['document_info'] ?? $data;

        $this->id = $user['id'] ?? null;
        $this->firstname = $user['firstname'] ?? '';
        $this->lastname = $user['lastname'] ?? '';
        $this->username = $user['username'] ?? '';
        $this->email = $user['email'] ?? '';
        $this->phone = $profile['phone'] ?? $user['phone'] ?? '';
        
        $this->date_of_birth = $profile['date_of_birth'] ?? $user['date_of_birth'] ?? '';
        $this->occupation = $profile['occupation'] ?? $user['occupation'] ?? '';
        $this->address = $profile['address'] ?? $user['address'] ?? '';
        $this->city = $profile['city'] ?? $user['city'] ?? '';
        $this->state = $profile['state'] ?? $user['state'] ?? '';
        $this->post_code = $profile['post_code'] ?? $profile['zip_code'] ?? '';
        $this->country = $profile['country'] ?? $profile['country_id'] ?? '';
        $this->nationality = $profile['nationality'] ?? '';
        $this->source_of_fund = $profile['source_of_fund'] ?? '';
        $this->yearly_income = $profile['yearly_income'] ?? '';
        $this->daily_limit = $profile['daily_limit'] ?? '0';
        $this->monthly_limit = $profile['monthly_limit'] ?? '0';
        $this->yearly_limit = $profile['yearly_limit'] ?? '0';
        $this->remarks = $profile['remarks'] ?? '';
        $this->gender_type = $profile['gender_type'] ?? '';
        $this->remitter_type = $profile['remitter_type'] ?? '';
        $this->country_id = $profile['country_id'] ?? '';
        $this->language_id = $user['language_id'] ?? '1';
        $this->place_of_birth = $user['place_of_birth'] ?? '';
        $this->declaration_amount = $profile['declaration_amount'] ?? '';
        $this->declaration_start_date = $profile['declaration_start_date'] ?? '';
        $this->declaration_end_date = $profile['declaration_end_date'] ?? '';
        
        $this->document_type = $doc['document_type'] ?? '';
        $this->document_id_number = $doc['document_id_number'] ?? '';
        $this->document_expiry_date = $doc['document_expiry_date'] ?? '';
        $this->document_issue_date = $doc['document_issue_date'] ?? '';
        $this->issue_country_code = $doc['issue_country_code'] ?? '';
        
        $this->image = $user['photo'] ?? $user['image'] ?? $profile['image'] ?? null;
        $this->referral_code = $profile['referral_code'] ?? $data['referralCode'] ?? '';
        $this->email_verification = $user['email_verification'] ?? $user['email_verified'] ?? 1;
        $this->sms_verification = $user['sms_verification'] ?? $user['sms_verified'] ?? 1;
    }

    public function getFullName() {
        return trim($this->firstname . ' ' . $this->lastname);
    }

    public function getDisplayImage() {
        if ($this->image) {
            return $this->image;
        }
        return 'https://ui-avatars.com/api/?name=' . urlencode($this->getFullName()) . '&background=eff6ff&color=3b82f6';
    }

    public function toArray() {
        return get_object_vars($this);
    }
}
