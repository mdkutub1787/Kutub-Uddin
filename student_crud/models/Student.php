<?php
class Student {
    private $conn;
    public function __construct($db) {
        $this->conn = $db;
    }

    public function findByRoll($roll) {
        $stmt = $this->conn->prepare("SELECT * FROM students WHERE roll = ? LIMIT 1");
        $stmt->bind_param("s", $roll);
        $stmt->execute();
        $result = $stmt->get_result();
        return $result->fetch_assoc();
    }

    public function all() {
           return $this->conn->query("SELECT * FROM students");
        }

        // Join students with teachers by department
        public function studentsWithTeachers() {
            $sql = "SELECT s.*, GROUP_CONCAT(t.name SEPARATOR ', ') AS teacher_names, t.department FROM students s LEFT JOIN teachers t ON s.class_id = t.id GROUP BY s.id";
            return $this->conn->query($sql);
    }

    public function find($id) {
        return $this->conn->query("SELECT * FROM students WHERE id=$id")->fetch_assoc();
    }

    public function create($data) {
        $stmt = $this->conn->prepare("SELECT id FROM students WHERE roll = ?");
        $stmt->bind_param("s", $data['roll']);
        $stmt->execute();
        $stmt->store_result();
        if ($stmt->num_rows > 0) {
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

    public function update($id, $data) {
        $name = $data['name'];
        $roll = $data['roll'];
        $department = $data['department'];
        return $this->conn->query("UPDATE students SET name='$name', roll='$roll', department='$department' WHERE id=$id");
    }

    public function delete($id) {
        return $this->conn->query("DELETE FROM students WHERE id=$id");
    }
}
?>
