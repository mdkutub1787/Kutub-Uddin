<?php
include_once "models/Fee.php";
class FeeController {
    private $fee;
    public function __construct($conn) { $this->fee = new Fee($conn); }
    public function index() {
        $fees = $this->fee->all();
        include "views/fees/view.php";
    }
    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = [
                'student_id' => $_POST['student_id'],
                'amount' => $_POST['amount'],
                'due_date' => $_POST['due_date'],
                'paid_date' => $_POST['paid_date'],
                'status' => $_POST['status']
            ];
            if ($this->fee->create($data)) {
                echo json_encode(['success'=>true,'redirect'=>'router.php?action=fee_index']); exit;
            } else { $error = 'ফি যোগ হয়নি!'; }
        }
        include "views/fees/create.php";
    }
}
