<?php
require_once __DIR__ . '/base_service.php';

class ProfileService extends BaseService {

    public function getProfile() {
        $response = $this->request(ApiEndpoints::GET_PROFILE, 'GET');
        return map_profile($response['data'] ?? []);
    }

    public function updateProfile($data) {
        return $this->request(ApiEndpoints::UPDATE_PROFILE, 'POST', $data);
    }

    public function getDocumentInfo() {
        return $this->request(ApiEndpoints::DOCUMENT_INFO, 'GET');
    }

    public function getActiveCountries() {
        return $this->request(ApiEndpoints::ACTIVE_COUNTRIES, 'GET');
    }

    public function getRemitterTypes() {
        return $this->request(ApiEndpoints::REMITTER_TYPES, 'GET');
    }

    public function getGenderTypes($code = '200669') {
        // Form body for GET might not be standard, passing as query param or as POST if server allows
        return $this->request(ApiEndpoints::GENDER_TYPES . '?code=' . urlencode($code), 'GET');
    }

    public function getDocumentTypes() {
        return $this->request(ApiEndpoints::DOCUMENT_TYPES, 'GET');
    }
}
