<?php
class Fee {
    private $conn;
    public function __construct($db) { $this->conn = $db; }
    public function all() { return $this->conn->query("SELECT * FROM fees"); }
    public function create($data) {
        $stmt = $this->conn->prepare("INSERT INTO fees (student_id, amount, due_date, paid_date, status) VALUES (?, ?, ?, ?, ?)");
        $stmt->bind_param("idsss", $data['student_id'], $data['amount'], $data['due_date'], $data['paid_date'], $data['status']);
        return $stmt->execute();
    }
}
