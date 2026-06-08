<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Teachers List</title>
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
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1 class="header-title mb-1">👨‍🏫 Teachers List</h1>
        <a href="router.php?action=teacher_create" class="btn btn-success">Add Teacher</a>
    </div>
    <table class="table table-bordered table-hover shadow-sm bg-white">
        <thead class="table-dark">
            <tr>
                <th>SL</th>
                <th>Name</th>
                <th>Department</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <?php if ($teachers && $teachers->num_rows > 0): ?>
            <?php $sl = 1; ?>
            <?php while ($row = $teachers->fetch_assoc()): ?>
                <tr>
                    <td><?= $sl++; ?></td>
                    <td><?= htmlspecialchars($row['name']); ?></td>
                    <td><?= htmlspecialchars($row['department']); ?></td>
                    <td>
                        <a href="router.php?action=teacher_edit&id=<?= $row['id']; ?>" class="btn btn-warning btn-sm">Edit</a>
                        <a href="router.php?action=teacher_delete&id=<?= $row['id']; ?>" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this teacher?')">Delete</a>
                    </td>
                </tr>
            <?php endwhile; ?>
        <?php else: ?>
            <tr>
                <td colspan="4" class="text-center text-muted">No teachers found.</td>
            </tr>
        <?php endif; ?>
        </tbody>
    </table>
</body>
</html>
