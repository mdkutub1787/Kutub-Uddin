<!DOCTYPE html>
<html lang="bn">
<head>
    <meta charset="UTF-8">
    <title>উপস্থিতি তালিকা</title>
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
    </style>
</head>
<body class="container py-5">
    <h2 class="mb-4 text-primary">উপস্থিতি তালিকা</h2>
    <a href="router.php?action=attendance_create" class="btn btn-success mb-3">+ নতুন উপস্থিতি</a>
    <table class="table table-bordered">
        <thead><tr><th>আইডি</th><th>স্টুডেন্ট আইডি</th><th>তারিখ</th><th>স্ট্যাটাস</th></tr></thead>
        <tbody>
        <?php if ($attendances && $attendances->num_rows > 0): foreach($attendances as $row): ?>
            <tr><td><?= $row['id'] ?></td><td><?= $row['student_id'] ?></td><td><?= $row['date'] ?></td><td><?= $row['status'] ?></td></tr>
        <?php endforeach; else: ?>
            <tr><td colspan="4">কোনো উপস্থিতি নেই</td></tr>
        <?php endif; ?>
        </tbody>
    </table>
</body>
</html>
