<?php
class Report {
    private $conn;
    public function __construct($db) { $this->conn = $db; }
    public function summary() {
        // এখানে বিভিন্ন রিপোর্টের জন্য SQL query যোগ করুন
        return [
            'total_students' => $this->conn->query("SELECT COUNT(*) as total FROM students")->fetch_assoc()['total'],
            'total_teachers' => $this->conn->query("SELECT COUNT(*) as total FROM teachers")->fetch_assoc()['total'],
            'total_guardians' => $this->conn->query("SELECT COUNT(*) as total FROM guardians")->fetch_assoc()['total'],
            'total_fees' => $this->conn->query("SELECT SUM(amount) as total FROM fees")->fetch_assoc()['total']
        ];
    }
}
