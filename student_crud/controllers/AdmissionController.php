<?php
include_once "models/Admission.php";
class AdmissionController {
    private $admission;
    public function __construct($conn) {
        $this->admission = new Admission($conn);
    }
    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = $_POST;
            // Handle photo upload
            if (isset($_FILES['photo']) && $_FILES['photo']['error'] === UPLOAD_ERR_OK) {
                $targetDir = 'uploads/students/';
                if (!is_dir($targetDir)) {
                    mkdir($targetDir, 0777, true);
                }
                $fileName = uniqid('student_') . '_' . basename($_FILES['photo']['name']);
                $targetFile = $targetDir . $fileName;
                if (move_uploaded_file($_FILES['photo']['tmp_name'], $targetFile)) {
                    $data['photo'] = $targetFile;
                } else {
                    $error = 'ছবি আপলোড হয়নি!';
                }
            } else if (!empty($_POST['photo'])) {
                $data['photo'] = $_POST['photo']; // If photo is given as link
            } else {
                $data['photo'] = '';
            }
            include_once 'models/Admission.php';
            $admissionModel = new Admission($this->conn);
            $result = $admissionModel->create($data);
            if ($result) {
                echo '<script>window.location.href="router.php?action=index";</script>';
                exit;
            } else {
                $error = 'স্টুডেন্ট যোগ হয়নি!';
            }
        }
        include 'views/admission/create.php';
    }
}
