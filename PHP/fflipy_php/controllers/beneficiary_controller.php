<?php
/**
 * BeneficiaryController - Standard PHP MVC
 */
session_start();
require_once __DIR__ . '/../includes/functions.php';

class BeneficiaryController {
    private $service;

    public function __construct() {
        $this->service = new BeneficiaryService();
    }

    public function index() {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        try {
            if (isset($_POST['delete_id'])) {
                $this->service->deleteBeneficiary($_POST['delete_id']);
                header('Location: beneficiaries.php?success=deleted'); exit;
            }
            $beneficiaries = $this->service->getBeneficiaries();
            include __DIR__ . '/../views/beneficiaries/beneficiary_list_view.php';
        } catch (Exception $e) { $beneficiaries = []; include __DIR__ . '/../views/beneficiaries/beneficiary_list_view.php'; }
    }

    public function add() {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            try {
                $this->service->addBeneficiary($_POST);
                header('Location: beneficiaries.php?success=added'); exit;
            } catch (Exception $e) { $error = $e->getMessage(); }
        }
        
        $countries = [];
        $relationships = [];
        $sendingPurposes = [];
        $accountTypes = [];
        try {
            $benData = $this->service->getAllBeneficiaryData();
            if(isset($benData['data'])) {
                $countries = $benData['data']['countries'] ?? [];
                $relationships = $benData['data']['relationships'] ?? [];
                $sendingPurposes = $benData['data']['sendingPurposes'] ?? [];
            }
            
            $accData = $this->service->getAccountTypes();
            $accountTypes = $accData['data'] ?? [];
        } catch (Exception $e) {}
        include __DIR__ . '/../views/beneficiaries/add_beneficiary_view.php';
    }
    public function ajaxGetFacilities() {
        if (!isset($_SESSION['token'])) { http_response_code(401); exit; }
        header('Content-Type: application/json');
        echo json_encode($this->service->getFacilities($_GET['country_id'] ?? 0));
        exit;
    }

    public function ajaxGetBanks() {
        if (!isset($_SESSION['token'])) { http_response_code(401); exit; }
        header('Content-Type: application/json');
        echo json_encode($this->service->getBanks($_GET['country_id'] ?? 0));
        exit;
    }

    public function ajaxGetBranches() {
        if (!isset($_SESSION['token'])) { http_response_code(401); exit; }
        header('Content-Type: application/json');
        echo json_encode($this->service->getBranches($_GET['bank_id'] ?? 0));
        exit;
    }

    public function ajaxGetWallets() {
        if (!isset($_SESSION['token'])) { http_response_code(401); exit; }
        header('Content-Type: application/json');
        echo json_encode($this->service->getWallets($_GET['country_id'] ?? 0));
        exit;
    }
}

$controller = new BeneficiaryController();
$action = $_GET['action'] ?? 'index';

if ($action === 'add') $controller->add();
elseif ($action === 'get_facilities') $controller->ajaxGetFacilities();
elseif ($action === 'get_banks') $controller->ajaxGetBanks();
elseif ($action === 'get_branches') $controller->ajaxGetBranches();
elseif ($action === 'get_wallets') $controller->ajaxGetWallets();
else $controller->index();
?>
