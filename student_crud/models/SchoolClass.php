<?php
class SchoolClass {
    private $conn;
    public function __construct($db) { $this->conn = $db; }
    public function all() { return $this->conn->query("SELECT * FROM classes"); }
    public function create($name) {
        $stmt = $this->conn->prepare("INSERT INTO classes (name) VALUES (?)");
        $stmt->bind_param("s", $name);
        return $stmt->execute();
    }

    public function find($id) {
        $stmt = $this->conn->prepare("SELECT * FROM classes WHERE id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $result = $stmt->get_result();
        return $result->fetch_assoc();
    }

    public function update($id, $name) {
        $stmt = $this->conn->prepare("UPDATE classes SET name = ? WHERE id = ?");
        $stmt->bind_param("si", $name, $id);
        return $stmt->execute();
    }

    public function delete($id) {
        $stmt = $this->conn->prepare("DELETE FROM classes WHERE id = ?");
        $stmt->bind_param("i", $id);
        return $stmt->execute();
    }
}
