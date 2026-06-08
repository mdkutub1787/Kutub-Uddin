<?php
include_once "db/db.php";
$dept_result = $conn->query("SELECT department, COUNT(*) as total FROM students GROUP BY department");
$total_result = $conn->query("SELECT COUNT(*) as total FROM students");
$total_students = $total_result->fetch_assoc()['total'];
$dept_count_result = $conn->query("SELECT COUNT(DISTINCT department) as dept_count FROM students");
$total_departments = $dept_count_result->fetch_assoc()['dept_count'];
?>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
<style>
.dashboard-card {
    background: linear-gradient(135deg, #0d6efd 60%, #6dd5ed 100%);
    color: #fff;
    border-radius: 18px;
    box-shadow: 0 6px 24px rgba(13,110,253,0.12);
    padding: 2.5rem 1.5rem;
    text-align: center;
    position: relative;
    overflow: hidden;
}
.dashboard-card .icon {
    font-size: 2.8rem;
    margin-bottom: 0.5rem;
    color: #fff;
    opacity: 0.8;
}
.dashboard-card .card-stats {
    font-size: 2.5rem;
    font-weight: bold;
    color: #fff;
    margin-top: 0.5rem;
    text-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.dashboard-card .label {
    font-size: 1.1rem;
    font-weight: 500;
    letter-spacing: 0.5px;
    margin-bottom: 0.2rem;
    color: #e3f2fd;
}
@media (max-width: 600px) {
    .dashboard-card { padding: 1.2rem 0.5rem; font-size: 1rem; }
    .dashboard-card .card-stats { font-size: 1.5rem; }
}
</style>
<div class="container mt-5">
    <h2 class="mb-4 text-success text-center">Department Wise Student Count</h2>
    <div class="mb-3 text-center">
        <span class="badge bg-primary fs-5">Total Department: <?= $total_departments ?></span>
    </div>
    <table class="table table-bordered table-striped shadow">
        <thead class="table-primary">
            <tr>
                <th>Department</th>
                <th>Student Count</th>
            </tr>
        </thead>
        <tbody>
            <?php while ($row = $dept_result->fetch_assoc()): ?>
                <tr>
                    <td><?= htmlspecialchars($row['department']) ?></td>
                    <td><?= $row['total'] ?></td>
                </tr>
            <?php endwhile; ?>
        </tbody>
        <tfoot>
            <tr class="table-success">
                <th>Total Students</th>
                <th><?= $total_students ?></th>
            </tr>
        </tfoot>
    </table>
</div>
