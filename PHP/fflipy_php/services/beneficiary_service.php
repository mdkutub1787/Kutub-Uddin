<?php
require_once __DIR__ . '/base_service.php';

class BeneficiaryService extends BaseService {

    public function getBeneficiaries() {
        $response = $this->request(ApiEndpoints::GET_BENEFICIARIES, 'GET');
        $list = $response['data']['beneficiaries']['data'] ?? $response['data']['beneficiaries'] ?? [];
        return map_beneficiaries($list);
    }
    
    public function getAllBeneficiaryData() {
        return $this->request(ApiEndpoints::GET_BENEFICIARIES, 'GET');
    }

    public function addBeneficiary($data) {
        return $this->request(ApiEndpoints::ADD_BENEFICIARY, 'POST', $data);
    }

    public function updateBeneficiary($id, $data) {
        return $this->request(ApiEndpoints::UPDATE_BENEFICIARY($id), 'POST', $data);
    }

    public function deleteBeneficiary($id) {
        return $this->request(ApiEndpoints::DELETE_BENEFICIARY($id), 'DELETE');
    }

    public function getBanks($countryId) {
        return $this->request(ApiEndpoints::GET_BANKS($countryId), 'GET');
    }

    public function getBranches($bankId) {
        return $this->request(ApiEndpoints::GET_BRANCHES($bankId), 'GET');
    }

    public function getFacilities($countryId) {
        return $this->request(ApiEndpoints::GET_FACILITIES($countryId), 'GET');
    }

    public function getWallets($countryId) {
        return $this->request(ApiEndpoints::GET_WALLETS($countryId), 'GET');
    }

    public function getAccountTypes() {
        return $this->request(ApiEndpoints::ACCOUNT_TYPES, 'GET');
    }
}
