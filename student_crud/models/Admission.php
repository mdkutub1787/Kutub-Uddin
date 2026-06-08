<?php
class Admission {
    private $conn;
    public function __construct($db) {
        $this->conn = $db;
    }
    public function create($data) {
        // রোল নম্বর ডুপ্লিকেট চেক
        $stmt = $this->conn->prepare("SELECT id FROM students WHERE roll = ?");
        $stmt->bind_param("s", $data['roll']);
        $stmt->execute();
        $stmt->store_result();
        if ($stmt->num_rows > 0) {
            // রোল নম্বর ডুপ্লিকেট, ইনসার্ট করবেন না
            return false;
        }
        $stmt->close();
        $sql = "INSERT INTO students (name, roll, class_id, section_id, dob, gender, guardian_id, address, phone, photo, admission_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        $stmt = $this->conn->prepare($sql);
        $stmt->bind_param("ssiississsss",
            $data['name'], $data['roll'], $data['class_id'], $data['section_id'], $data['dob'], $data['gender'], $data['guardian_id'], $data['address'], $data['phone'], $data['photo'], $data['admission_date'], $data['status']
        );
        return $stmt->execute();
    }
    // অন্যান্য প্রয়োজনীয় মেথড এখানে যোগ করা যাবে
}
