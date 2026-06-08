<?php
include_once "models/Result.php";
class ResultController {
    private $result;
    public function __construct($conn) { $this->result = new Result($conn); }
    public function index() {
        $results = $this->result->all();
        include "views/results/view.php";
    }
    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = [
                'student_id' => $_POST['student_id'],
                'exam_id' => $_POST['exam_id'],
                'subject_id' => $_POST['subject_id'],
                'marks' => $_POST['marks'],
                'grade' => $_POST['grade']
            ];
            if ($this->result->create($data)) {
                echo json_encode(['success'=>true,'redirect'=>'router.php?action=result_index']); exit;
            } else { $error = 'রেজাল্ট যোগ হয়নি!'; }
        }
        include "views/results/create.php";
    }
}
