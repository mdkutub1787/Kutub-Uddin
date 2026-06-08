<?php
class Result {
    private $conn;
    public function __construct($db) { $this->conn = $db; }
    public function all() { return $this->conn->query("SELECT * FROM results"); }
    public function create($data) {
        $stmt = $this->conn->prepare("INSERT INTO results (student_id, exam_id, subject_id, marks, grade) VALUES (?, ?, ?, ?, ?)");
        $stmt->bind_param("iiids", $data['student_id'], $data['exam_id'], $data['subject_id'], $data['marks'], $data['grade']);
        return $stmt->execute();
    }
}
