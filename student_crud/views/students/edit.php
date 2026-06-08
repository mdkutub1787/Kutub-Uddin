<?php // ...existing code... ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Student</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(120deg, #e3f2fd 60%, #f8fafc 100%); }
        .card {
            max-width: 500px;
            margin: auto;
            border-radius: 18px;
            box-shadow: 0 6px 24px rgba(13,110,253,0.12);
            border: none;
        }
        .btn-lg-custom {
            width: 48%;
            font-size: 18px;
            border-radius: 8px;
            font-weight: 500;
            box-shadow: 0 2px 8px rgba(255,193,7,0.10);
            transition: background 0.2s, color 0.2s;
        }
        .btn-warning.btn-lg-custom:hover {
            background: linear-gradient(135deg, #ffc107 80%, #6dd5ed 100%);
            color: #fff;
        }
        .btn-secondary.btn-lg-custom:hover {
            background: #e3f2fd;
            color: #0d6efd;
        }
        .btn-container { display: flex; justify-content: space-between; }
        .form-label {
            font-weight: 500;
            color: #0d6efd;
        }
        .form-control {
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(13,110,253,0.06);
            border: 1px solid #b6d4fe;
        }
        h2 {
            font-size: 2rem;
            font-weight: 700;
            color: #0d6efd;
            text-shadow: 0 2px 8px rgba(13,110,253,0.08);
        }
        @media (max-width: 700px) {
            h2 { font-size: 1.2rem; }
            .card { padding: 1rem; }
        }
    </style>
</head>
<body class="container py-5">

    <h2 class="mb-4 text-center text-primary">✏️ Edit Student</h2>
    <form method="POST" class="card p-4 shadow">
        <div class="mb-3">
            <label class="form-label">Name:</label>
            <input type="text" name="name" value="<?= $student['name'] ?>" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Roll:</label>
            <input type="text" name="roll" value="<?= $student['roll'] ?>" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Department:</label>
            <input type="text" name="department" value="<?= $student['department'] ?>" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="photo" class="form-label">ছবি</label>
            <input type="file" name="photo" id="photo" class="form-control" accept="image/*">
            <small class="text-muted">ছবি ফাইল সিলেক্ট করুন অথবা লিংক দিন</small>
        </div>
        <div class="btn-container mt-3">
            <button type="submit" class="btn btn-warning btn-lg btn-lg-custom">Update</button>
            <a href="router.php?action=index" class="btn btn-secondary btn-lg btn-lg-custom">Back</a>
        </div>
    </form>

</body>
</html>
