<?php
include_once "models/Subject.php";
class SubjectController {
    private $subject;
    public function __construct($conn) { $this->subject = new Subject($conn); }
    public function index() {
        $subjects = $this->subject->all();
        include "views/subjects/view.php";
    }
    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = [
                'name' => $_POST['name'],
                'class_id' => $_POST['class_id']
            ];
            if ($this->subject->create($data)) {
                echo json_encode(['success'=>true,'redirect'=>'router.php?action=subject_index']); exit;
            } else { $error = 'বিষয় যোগ হয়নি!'; }
        }
        include "views/subjects/create.php";
    }
}
