
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Teacher</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
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
        }
        .alert {
            margin-bottom: 20px;
            border-radius: 8px;
            font-size: 1.08rem;
            font-weight: 500;
        }
    </style>
</head>
<body class="container py-5">
    <div class="card">
        <div class="card-body">
            <h2 class="mb-4 text-warning text-center"><i class="fa fa-chalkboard-teacher"></i> Edit Teacher</h2>
            <?php if (!empty($error)): ?>
                <div class="alert alert-danger"> <?= $error ?> </div>
            <?php endif; ?>
            <form method="post">
                <div class="mb-3">
                    <label for="name" class="form-label">Name</label>
                    <input type="text" name="name" id="name" class="form-control" value="<?= htmlspecialchars($teacher['name']) ?>" required>
                </div>
                <div class="mb-3">
                    <label for="department" class="form-label">Department</label>
                    <input type="text" name="department" id="department" class="form-control" value="<?= htmlspecialchars($teacher['department']) ?>" required>
                </div>
                <div class="btn-container">
                    <button type="submit" class="btn btn-warning btn-lg-custom">Update</button>
                    <a href="router.php?action=teacher_index" class="btn btn-secondary btn-lg-custom">Back</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
