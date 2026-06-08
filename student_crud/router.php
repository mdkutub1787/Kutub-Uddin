<?php
// router.php - Manual Router for MVC

include __DIR__ . '/db/db.php';
$action = $_GET['action'] ?? 'dashboard';

switch ($action) {
    case 'report':
        include __DIR__ . '/controllers/ReportController.php';
        $controller = new ReportController($conn);
        $controller->index();
        break;
    case 'login':
        include __DIR__ . '/controllers/UserController.php';
        $controller = new UserController($conn);
        $controller->login();
        break;
    case 'logout':
        include __DIR__ . '/controllers/UserController.php';
        $controller = new UserController($conn);
        $controller->logout();
        break;
    case 'fee_index':
        include __DIR__ . '/controllers/FeeController.php';
        $controller = new FeeController($conn);
        $controller->index();
        break;
    case 'fee_create':
        include __DIR__ . '/controllers/FeeController.php';
        $controller = new FeeController($conn);
        $controller->create();
        break;
    case 'result_index':
        include __DIR__ . '/controllers/ResultController.php';
        $controller = new ResultController($conn);
        $controller->index();
        break;
    case 'result_create':
        include __DIR__ . '/controllers/ResultController.php';
        $controller = new ResultController($conn);
        $controller->create();
        break;
    case 'attendance_index':
        include __DIR__ . '/controllers/AttendanceController.php';
        $controller = new AttendanceController($conn);
        $controller->index();
        break;
    case 'attendance_create':
        include __DIR__ . '/controllers/AttendanceController.php';
        $controller = new AttendanceController($conn);
        $controller->create();
        break;
    case 'teacher_index':
        include __DIR__ . '/controllers/TeacherController.php';
        $controller = new TeacherController($conn);
        $controller->index();
        break;
    case 'teacher_create':
        include __DIR__ . '/controllers/TeacherController.php';
        $controller = new TeacherController($conn);
        $controller->create();
        break;
    case 'teacher_edit':
        include __DIR__ . '/controllers/TeacherController.php';
        $controller = new TeacherController($conn);
        $id = $_GET['id'] ?? null;
        if ($id) {
            $controller->edit($id);
        } else {
            echo '<p class="text-danger">No teacher ID provided.</p>';
        }
        break;
    case 'teacher_delete':
        include __DIR__ . '/controllers/TeacherController.php';
        $controller = new TeacherController($conn);
        $id = $_GET['id'] ?? null;
        if ($id) {
            $controller->delete($id);
        } else {
            echo '<p class="text-danger">No teacher ID provided.</p>';
        }
        break;
    case 'subject_index':
        include __DIR__ . '/controllers/SubjectController.php';
        $controller = new SubjectController($conn);
        $controller->index();
        break;
    case 'subject_create':
        include __DIR__ . '/controllers/SubjectController.php';
        $controller = new SubjectController($conn);
        $controller->create();
        break;
    case 'guardian_index':
        include __DIR__ . '/controllers/GuardianController.php';
        $controller = new GuardianController($conn);
        $controller->index();
        break;
    case 'guardian_create':
        include __DIR__ . '/controllers/GuardianController.php';
        $controller = new GuardianController($conn);
        $controller->create();
        break;
    case 'class_index':
        include __DIR__ . '/controllers/SchoolClassController.php';
        $controller = new SchoolClassController($conn);
        $controller->index();
        break;
    case 'class_create':
        include __DIR__ . '/controllers/SchoolClassController.php';
        $controller = new SchoolClassController($conn);
        $controller->create();
        break;
    case 'section_index':
        include __DIR__ . '/controllers/SectionController.php';
        $controller = new SectionController($conn);
        $controller->index();
        break;
    case 'section_create':
        include __DIR__ . '/controllers/SectionController.php';
        $controller = new SectionController($conn);
        $controller->create();
        break;
    case 'admission':
        include __DIR__ . '/controllers/AdmissionController.php';
        $controller = new AdmissionController($conn);
        $controller->create();
        break;
    case 'admission_success':
        include __DIR__ . '/views/admission/success.php';
        break;
    case 'dashboard':
        include __DIR__ . '/views/dashboard/dashboard.php';
        break;
    case 'index':
        include __DIR__ . '/controllers/StudentController.php';
        $controller = new StudentController($conn);
        $controller->index();
        break;
    case 'create':
        include __DIR__ . '/controllers/StudentController.php';
        $controller = new StudentController($conn);
        $controller->create();
        break;
    case 'edit':
        include __DIR__ . '/controllers/StudentController.php';
        $controller = new StudentController($conn);
        $id = $_GET['id'] ?? null;
        if ($id) {
            $controller->edit($id);
        } else {
            echo '<p class="text-danger">No student ID provided.</p>';
        }
        break;
    case 'delete':
        include __DIR__ . '/controllers/StudentController.php';
        $controller = new StudentController($conn);
        $id = $_GET['id'] ?? null;
        if ($id) {
            $controller->delete($id);
        } else {
            echo '<p class="text-danger">No student ID provided.</p>';
        }
        break;
    default:
        echo '<p class="text-danger">404 - Page not found</p>';
        break;
}
