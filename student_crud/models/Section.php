<?php
class Section {
    private $conn;
    public function __construct($db) { $this->conn = $db; }
    public function all() { return $this->conn->query("SELECT * FROM sections"); }
    public function create($class_id, $name) {
        $stmt = $this->conn->prepare("INSERT INTO sections (class_id, name) VALUES (?, ?)");
        $stmt->bind_param("is", $class_id, $name);
        return $stmt->execute();
    }
}
