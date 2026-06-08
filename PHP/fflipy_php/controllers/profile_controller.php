<?php
/**
 * ProfileController - Standard PHP MVC
 */
session_start();
require_once __DIR__ . '/../includes/functions.php';

class ProfileController {
    private $service;

    public function __construct() {
        $this->service = new ProfileService();
    }

    public function index() {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        try {
            $userProfile = $this->service->getProfile();
            $_SESSION['user'] = serialize($userProfile);
            include __DIR__ . '/../views/profile/profile_view.php';
        } catch (Exception $e) { $userProfile = unserialize($_SESSION['user']); include __DIR__ . '/../views/profile/profile_view.php'; }
    }

    public function update() {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            try {
                $postData = $_POST;
                
                // Handle file uploads
                if (isset($_FILES['image']) && $_FILES['image']['error'] === UPLOAD_ERR_OK) {
                    $postData['image'] = new CURLFile($_FILES['image']['tmp_name'], $_FILES['image']['type'], $_FILES['image']['name']);
                }
                if (isset($_FILES['document_upload']) && $_FILES['document_upload']['error'] === UPLOAD_ERR_OK) {
                    $postData['document_upload'] = new CURLFile($_FILES['document_upload']['tmp_name'], $_FILES['document_upload']['type'], $_FILES['document_upload']['name']);
                }

                $this->service->updateProfile($postData);
                header('Location: profile.php?success=1'); exit;
            } catch (Exception $e) { $error = $e->getMessage(); }
        }
        $userProfile = unserialize($_SESSION['user']);
        
        $countries = [];
        $docTypes = [];
        $genderTypes = [];
        $remitterTypes = [];

        try {
            $res = $this->service->getActiveCountries();
            $countries = $res['data']['country'] ?? $res['data'] ?? [];
            
            $resDoc = $this->service->getDocumentTypes();
            $docTypes = $resDoc['data']['document_type'] ?? $resDoc['data'] ?? [];
            
            $resGen = $this->service->getGenderTypes();
            $genderTypes = $resGen['data']['gender_type'] ?? $resGen['data'] ?? [];
            
            $resRem = $this->service->getRemitterTypes();
            $remitterTypes = $resRem['data']['remitter_type'] ?? $resRem['data'] ?? [];
        } catch (Exception $e) { }

        include __DIR__ . '/../views/profile/update_profile_view.php';
    }

    public function qrCode() {
        if (!isset($_SESSION['token'])) { header('Location: ../auth/login.php'); exit; }
        $userProfile = unserialize($_SESSION['user']);
        include __DIR__ . '/../views/profile/qr_code_view.php';
    }
}

$controller = new ProfileController();
$action = $_GET['action'] ?? 'index';
if ($action === 'update') $controller->update();
elseif ($action === 'qr') $controller->qrCode();
else $controller->index();
?>
