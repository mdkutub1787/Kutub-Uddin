<?php
class Guardian {
    private $conn;
    public function __construct($db) { $this->conn = $db; }
    public function all() { return $this->conn->query("SELECT * FROM guardians"); }
    public function create($data) {
        $stmt = $this->conn->prepare("INSERT INTO guardians (name, relation, phone, email, address) VALUES (?, ?, ?, ?, ?)");
        $stmt->bind_param("sssss", $data['name'], $data['relation'], $data['phone'], $data['email'], $data['address']);
        return $stmt->execute();
    }
}
