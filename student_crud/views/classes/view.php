<!DOCTYPE html>
<html lang="bn">
<head>
    <meta charset="UTF-8">
    <title>ক্লাস তালিকা</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <h2 class="mb-4 text-primary">ক্লাস তালিকা</h2>
    <a href="router.php?action=class_create" class="btn btn-success mb-3">+ নতুন ক্লাস</a>
    <table class="table table-bordered">
        <thead><tr><th>আইডি</th><th>নাম</th></tr></thead>
        <tbody>
        <?php if ($classes && $classes->num_rows > 0): foreach($classes as $row): ?>
            <tr><td><?= $row['id'] ?></td><td><?= htmlspecialchars($row['name']) ?></td></tr>
        <?php endforeach; else: ?>
            <tr><td colspan="2">কোনো ক্লাস নেই</td></tr>
        <?php endif; ?>
        </tbody>
    </table>
</body>
</html>
