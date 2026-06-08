<?php
include_once "models/Section.php";
class SectionController {
    private $sectionModel;
    public function __construct($conn) { $this->sectionModel = new Section($conn); }
    public function index() {
        $sections = $this->sectionModel->all();
        include "views/sections/view.php";
    }
    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $class_id = $_POST['class_id'];
            $name = $_POST['name'];
            if ($this->sectionModel->create($class_id, $name)) {
                echo json_encode(['success'=>true,'redirect'=>'router.php?action=section_index']); exit;
            } else { $error = 'সেকশন যোগ হয়নি!'; }
        }
        include "views/sections/create.php";
    }
}
