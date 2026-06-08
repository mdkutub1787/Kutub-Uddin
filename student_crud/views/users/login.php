<!DOCTYPE html>
<html lang="bn">
<head>
    <meta charset="UTF-8">
    <title>লগইন</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h2 class="mb-4 text-primary text-center">লগইন করুন</h2>
                    <?php if (!empty($error)): ?><div class="alert alert-danger"> <?= $error ?> </div><?php endif; ?>
                    <form method="post">
                        <div class="mb-3">
                            <label class="form-label">ইউজারনেম</label>
                            <input type="text" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">পাসওয়ার্ড</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-success w-100">লগইন</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
