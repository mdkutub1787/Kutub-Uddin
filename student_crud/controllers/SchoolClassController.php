<?php
include_once "models/SchoolClass.php";
class SchoolClassController {
    private $classModel;
    public function __construct($conn) { $this->classModel = new SchoolClass($conn); }

    public function index() {
        $classes = $this->classModel->all();
        include "views/classes/view.php";
    }

    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $name = $_POST['name'];
            if ($this->classModel->create($name)) {
                echo json_encode(['success'=>true,'redirect'=>'router.php?action=class_index']); exit;
            } else { $error = 'ক্লাস যোগ হয়নি!'; }
        }
        include "views/classes/create.php";
    }

    public function edit() {
        $error = '';
        $id = $_GET['id'] ?? null;
        if (!$id) { echo "Invalid class id!"; exit; }
        $class = $this->classModel->find($id);
        if (!$class) { echo "Class not found!"; exit; }
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $name = $_POST['name'];
            if ($this->classModel->update($id, $name)) {
                echo json_encode(['success'=>true,'redirect'=>'router.php?action=class_index']); exit;
            } else { $error = 'ক্লাস আপডেট হয়নি!'; }
        }
        include "views/classes/edit.php";
    }

    public function delete() {
        $id = $_GET['id'] ?? null;
        if (!$id) { echo "Invalid class id!"; exit; }
        if ($this->classModel->delete($id)) {
            echo json_encode(['success'=>true,'redirect'=>'router.php?action=class_index']); exit;
        } else {
            echo json_encode(['success'=>false,'error'=>'ক্লাস ডিলিট হয়নি!']); exit;
        }
    }
}
