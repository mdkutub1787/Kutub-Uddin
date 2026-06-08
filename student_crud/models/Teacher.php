<?php
class Teacher {
    private $conn;

    public function __construct($db) {
        $this->conn = $db;
    }

    // Create teacher
    public function create($name, $department) {
    $stmt = $this->conn->prepare("INSERT INTO teachers (name, department) VALUES (?, ?)");
        $stmt->bind_param("ss", $name, $department);
        return $stmt->execute();
    }

    // Get all teachers
    public function all() {
    return $this->conn->query("SELECT * FROM teachers");
    }

    // Find teacher by id
    public function find($id) {
    $stmt = $this->conn->prepare("SELECT * FROM teachers WHERE id = ? LIMIT 1");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $result = $stmt->get_result();
        return $result->fetch_assoc();
    }

    // Update teacher
    public function update($id, $name, $department) {
    $stmt = $this->conn->prepare("UPDATE teachers SET name = ?, department = ? WHERE id = ?");
        $stmt->bind_param("ssi", $name, $department, $id);
        return $stmt->execute();
    }

    // Delete teacher
    public function delete($id) {
    $stmt = $this->conn->prepare("DELETE FROM teachers WHERE id = ?");
        $stmt->bind_param("i", $id);
        return $stmt->execute();
    }
}
