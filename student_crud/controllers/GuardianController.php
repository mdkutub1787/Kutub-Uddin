<?php
include_once "models/Guardian.php";
class GuardianController {
    private $guardian;
    public function __construct($conn) { $this->guardian = new Guardian($conn); }
    public function index() {
        $guardians = $this->guardian->all();
        include "views/guardians/view.php";
    }
    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = [
                'name' => $_POST['name'],
                'relation' => $_POST['relation'],
                'phone' => $_POST['phone'],
                'email' => $_POST['email'],
                'address' => $_POST['address']
            ];
            if ($this->guardian->create($data)) {
                echo json_encode(['success'=>true,'redirect'=>'router.php?action=guardian_index']); exit;
            } else { $error = 'গার্ডিয়ান যোগ হয়নি!'; }
        }
        include "views/guardians/create.php";
    }
}
