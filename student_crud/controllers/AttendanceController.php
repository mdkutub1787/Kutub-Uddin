<?php
include_once "models/Attendance.php";
class AttendanceController {
    private $attendance;
    public function __construct($conn) { $this->attendance = new Attendance($conn); }
    public function index() {
        $attendances = $this->attendance->all();
        include "views/attendance/view.php";
    }
    public function create() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = [
                'student_id' => $_POST['student_id'],
                'date' => $_POST['date'],
                'status' => $_POST['status']
            ];
            if ($this->attendance->create($data)) {
                echo json_encode(['success'=>true,'redirect'=>'router.php?action=attendance_index']); exit;
            } else { $error = 'উপস্থিতি যোগ হয়নি!'; }
        }
        include "views/attendance/create.php";
    }
}
