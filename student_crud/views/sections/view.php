<!DOCTYPE html>
<html lang="bn">
<head>
    <meta charset="UTF-8">
    <title>সেকশন তালিকা</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <h2 class="mb-4 text-primary">সেকশন তালিকা</h2>
    <a href="router.php?action=section_create" class="btn btn-success mb-3">+ নতুন সেকশন</a>
    <table class="table table-bordered">
        <thead><tr><th>আইডি</th><th>ক্লাস আইডি</th><th>নাম</th></tr></thead>
        <tbody>
        <?php if ($sections && $sections->num_rows > 0): foreach($sections as $row): ?>
            <tr><td><?= $row['id'] ?></td><td><?= $row['class_id'] ?></td><td><?= htmlspecialchars($row['name']) ?></td></tr>
        <?php endforeach; else: ?>
            <tr><td colspan="3">কোনো সেকশন নেই</td></tr>
        <?php endif; ?>
        </tbody>
    </table>
</body>
</html>
