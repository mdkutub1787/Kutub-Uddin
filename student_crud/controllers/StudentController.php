<?php
include "models/Student.php";

class StudentController {
    private $student;

    public function __construct($conn) {
        $this->student = new Student($conn);
    }

    public function index() {
        $students = $this->student->studentsWithTeachers();
        include "views/students/view.php";
    }

    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = [
                'name' => trim($_POST['name']),
                'roll' => trim($_POST['roll']),
                'class_id' => $_POST['class_id'],
                'section_id' => $_POST['section_id'],
                'dob' => $_POST['dob'],
                'gender' => $_POST['gender'],
                'guardian_id' => $_POST['guardian_id'],
                'address' => $_POST['address'],
                'phone' => $_POST['phone'],
                'photo' => $_POST['photo'],
                'admission_date' => $_POST['admission_date'],
                'status' => $_POST['status']
            ];
            $exists = $this->student->findByRoll($data['roll']);
            if ($exists) {
                $error = "⚠️ A student with this roll number already exists!";
            } else {
                $this->student->create($data);
                if (!empty($_SERVER['HTTP_X_REQUESTED_WITH']) && strtolower($_SERVER['HTTP_X_REQUESTED_WITH']) == 'xmlhttprequest') {
                    echo json_encode(['success' => true, 'redirect' => 'router.php?action=index']);
                    exit;
                } else {
                    echo '<script>window.location.href="router.php?action=index";</script>';
                    exit;
                }
            }
        }
        include "views/students/create.php";
    }

    public function edit($id) {
        $student = $this->student->find($id);
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $name = $_POST['name'];
            $roll = $_POST['roll'];
            $department = $_POST['department'];
            $this->student->update($id, [
                'name' => $name,
                'roll' => $roll,
                'department' => $department
            ]);
            echo '<script>window.location.href="router.php?action=index";</script>';
            exit;
        }
        include "views/students/edit.php";
    }

    public function delete($id) {
        $this->student->delete($id);
        echo '<script>window.location.href="router.php?action=index";</script>';
        exit;
    }
}
?>
