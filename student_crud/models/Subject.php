<?php
class Subject {
    private $conn;
    public function __construct($db) { $this->conn = $db; }
    public function all() { return $this->conn->query("SELECT * FROM subjects"); }
    public function create($data) {
        $stmt = $this->conn->prepare("INSERT INTO subjects (name, class_id) VALUES (?, ?)");
        $stmt->bind_param("si", $data['name'], $data['class_id']);
        return $stmt->execute();
    }
}
