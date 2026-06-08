<?php
class Attendance {
    private $conn;
    public function __construct($db) { $this->conn = $db; }
    public function all() { return $this->conn->query("SELECT * FROM attendance"); }
    public function create($data) {
        $stmt = $this->conn->prepare("INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)");
        $stmt->bind_param("iss", $data['student_id'], $data['date'], $data['status']);
        return $stmt->execute();
    }
}
