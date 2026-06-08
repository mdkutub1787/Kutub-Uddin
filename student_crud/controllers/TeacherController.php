<?php
include_once "models/Teacher.php";

class TeacherController {
    private $teacher;
    public function __construct($conn) {
        $this->teacher = new Teacher($conn);
    }

    // List all teachers
    public function index() {
        $teachers = $this->teacher->all();
        include "views/teachers/view.php";
    }

    // Create teacher
    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $name = trim($_POST['name']);
            $department = trim($_POST['department']);
            if (!$name || !$department) {
                $error = "নাম এবং বিভাগ দিতে হবে!";
            } else {
                $this->teacher->create($name, $department);
                echo '<script>window.location.href="router.php?action=teacher_index";</script>';
                exit;
            }
        }
        include "views/teachers/create.php";
    }

    // Edit teacher
    public function edit($id) {
        $teacher = $this->teacher->find($id);
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $name = trim($_POST['name']);
            $department = trim($_POST['department']);
            if (!$name || !$department) {
                $error = "নাম এবং বিভাগ দিতে হবে!";
            } else {
                $this->teacher->update($id, $name, $department);
                echo '<script>window.location.href="router.php?action=teacher_index";</script>';
                exit;
            }
        }
        include "views/teachers/edit.php";
    }

    // Delete teacher
    public function delete($id) {
        $this->teacher->delete($id);
        echo '<script>window.location.href="router.php?action=teacher_index";</script>';
        exit;
    }
    
    // Additional CRUD methods can be added here if needed
}
