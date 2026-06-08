<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Students</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <style>
    body {
        background: linear-gradient(120deg, #e3f2fd 60%, #f8fafc 100%);
        font-family: 'Segoe UI', 'Roboto', Arial, sans-serif;
    }
    .card, .table, .dashboard-card {
        border-radius: 16px;
        box-shadow: 0 4px 18px rgba(13,110,253,0.10);
    }
    .table th {
        background: linear-gradient(135deg, #0d6efd 80%, #6dd5ed 100%);
        color: #fff;
        font-size: 1.08rem;
        font-weight: 600;
    }
    .table td {
        text-align: center;
        vertical-align: middle;
        font-size: 1.05rem;
    }
    .btn-success, .btn-warning, .btn-danger, .btn-primary {
        border-radius: 8px;
        font-weight: 500;
    }
    /* Custom SPA Buttons & Table Styles */
    .btn-icon.edit i {
        color: #ffc107;
        font-size: 20px;
    }
    .btn-icon.edit:hover {
        background: #fffbe6;
        box-shadow: 0 2px 8px rgba(255,193,7,0.10);
    }
    .btn-icon.delete i {
        color: #dc3545;
        font-size: 20px;
    }
    .btn-icon.delete:hover {
        background: #ffe6e9;
        box-shadow: 0 2px 8px rgba(220,53,69,0.10);
    }
    .header-title {
        font-size: 2.2rem;
        font-weight: 700;
        text-align: center;
        margin-bottom: 30px;
        color: #0d6efd;
        text-shadow: 0 2px 8px rgba(13,110,253,0.08);
    }
    .btn-success {
        font-weight: 500;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(25,135,84,0.10);
        padding: 0.5rem 1.2rem;
    }
    .btn-success:hover {
        background: linear-gradient(135deg, #198754 80%, #6dd5ed 100%);
        color: #fff;
    }
    @media (max-width: 700px) {
        .header-title { font-size: 1.3rem; }
        .table th, .table td { font-size: 0.95rem; }
    }
    </style>
</head>
<body class="container py-5">

    <?php
    // Ensure required variables are set to avoid undefined warnings
    global $conn;
    if (!isset($conn)) {
        include_once __DIR__ . '/../../db/db.php';
    }
    // Teachers
    if (!isset($teacherModel)) {
        include_once __DIR__ . '/../../models/Teacher.php';
        $teacherModel = new Teacher($conn);
    }
    if (!isset($teachers)) {
        $teachers = $teacherModel->all();
    }
    // Total students
    if (!isset($total_students)) {
        $result = $conn->query("SELECT COUNT(*) as total FROM students");
        $total_students = $result ? $result->fetch_assoc()['total'] : 0;
    }
    // Students paged
    if (!isset($students_paged)) {
        $limit = 10;
        $page = isset($_GET['page']) ? max(1, intval($_GET['page'])) : 1;
        $offset = ($page - 1) * $limit;
    $sql = "SELECT s.*, GROUP_CONCAT(t.name SEPARATOR ', ') AS teacher_names FROM students s LEFT JOIN teachers t ON s.department = t.department GROUP BY s.id LIMIT $limit OFFSET $offset";
        $students_paged = $conn->query($sql);
        $result = $conn->query("SELECT COUNT(*) as total FROM students");
        $total_students = $result ? $result->fetch_assoc()['total'] : 0;
        $total_pages = ceil($total_students / $limit);
    }
    // Helper functions to get names from IDs
    function getClassName($conn, $class_id) {
        $result = $conn->query("SELECT name FROM classes WHERE id = " . intval($class_id));
        if ($row = $result->fetch_assoc()) return $row['name'];
        return '';
    }
    function getSectionName($conn, $section_id) {
        $result = $conn->query("SELECT name FROM sections WHERE id = " . intval($section_id));
        if ($row = $result->fetch_assoc()) return $row['name'];
        return '';
    }
    function getGuardianName($conn, $guardian_id) {
        $result = $conn->query("SELECT name FROM guardians WHERE id = " . intval($guardian_id));
        if ($row = $result->fetch_assoc()) return $row['name'];
        return '';
    }
    ?>
    <!-- Only show student list -->

    <div class="card mb-4 border-0 shadow-sm">
        <div class="card-header bg-success text-white d-flex align-items-center">
            <i class="fa fa-users me-2"></i>
            <span class="fs-5 fw-bold">স্টুডেন্ট তালিকা</span>
        </div>
        <div class="card-body p-0">
            <div class="mb-3 text-end">
            </div>
            <table class="table table-bordered table-hover shadow-sm bg-white">
                <thead class="table-dark">
                    <tr>
                        <th>SL</th>
                        <th>নাম</th>
                        <th>রোল</th>
                        <th>ক্লাস</th>
                        <th>সেকশন</th>
                        <th>জন্ম তারিখ</th>
                        <th>লিঙ্গ</th>
                        <th>গার্ডিয়ান</th>
                        <th>ঠিকানা</th>
                        <th>ফোন</th>
                        <th>ছবি</th>
                        <th>ভর্তি তারিখ</th>
                        <th>স্ট্যাটাস</th>
                        <th>একশন</th>
                    </tr>
                </thead>
                <tbody>
                    <?php $sl = 1; foreach ($students as $student): ?>
                    <tr>
                        <td><?= $sl++ ?></td>
                        <td><?= htmlspecialchars($student['name']) ?></td>
                        <td><?= htmlspecialchars($student['roll']) ?></td>
                        <td><?= htmlspecialchars(getClassName($conn, $student['class_id'])) ?></td>
                        <td><?= htmlspecialchars(getSectionName($conn, $student['section_id'])) ?></td>
                        <td><?= htmlspecialchars($student['dob']) ?></td>
                        <td><?= htmlspecialchars($student['gender']) ?></td>
                        <td><?= htmlspecialchars(getGuardianName($conn, $student['guardian_id'])) ?></td>
                        <td><?= htmlspecialchars($student['address']) ?></td>
                        <td><?= htmlspecialchars($student['phone']) ?></td>
                        <td><?php if ($student['photo']): ?><img src="<?= htmlspecialchars($student['photo']) ?>" alt="ছবি" style="width:40px;height:40px;border-radius:50%;object-fit:cover;" /><?php endif; ?></td>
                        <td><?= htmlspecialchars($student['admission_date']) ?></td>
                        <td><?= htmlspecialchars($student['status']) ?></td>
                        <td>
                            <a href="router.php?action=edit&id=<?= $student['id'] ?>" class="btn btn-icon edit" title="Edit"><i class="fa fa-edit"></i></a>
                            <a href="router.php?action=delete&id=<?= $student['id'] ?>" class="btn btn-icon delete" title="Delete"><i class="fa fa-trash"></i></a>
                        </td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        </div>
    </div>
    <!-- Pagination -->
    <nav aria-label="Student pagination">
        <ul class="pagination justify-content-center">
            <?php if ($total_pages > 1): ?>
                <li class="page-item <?= $page == 1 ? 'disabled' : '' ?>">
                    <a class="page-link" href="router.php?action=index&page=<?= $page - 1 ?>">আগে</a>
                </li>
                <?php for ($i = 1; $i <= $total_pages; $i++): ?>
                    <li class="page-item <?= $page == $i ? 'active' : '' ?>">
                        <a class="page-link" href="router.php?action=index&page=<?= $i ?>"> <?= $i ?> </a>
                    </li>
                <?php endfor; ?>
                <li class="page-item <?= $page == $total_pages ? 'disabled' : '' ?>">
                    <a class="page-link" href="router.php?action=index&page=<?= $page + 1 ?>">পরবর্তী</a>
                </li>
            <?php endif; ?>
        </ul>
    </nav>

</body>
</html>
