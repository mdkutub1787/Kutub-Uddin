<!DOCTYPE html>
<html lang="bn">
<head>
    <meta charset="UTF-8">
    <title>রিপোর্ট</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <h2 class="mb-4 text-primary">রিপোর্ট</h2>
    <div class="row g-4">
        <div class="col-md-3">
            <div class="card shadow-sm border-0">
                <div class="card-body text-center">
                    <h5>মোট ছাত্র</h5>
                    <span class="fs-3 fw-bold text-success"><?= $summary['total_students'] ?></span>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card shadow-sm border-0">
                <div class="card-body text-center">
                    <h5>মোট শিক্ষক</h5>
                    <span class="fs-3 fw-bold text-primary"><?= $summary['total_teachers'] ?></span>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card shadow-sm border-0">
                <div class="card-body text-center">
                    <h5>মোট গার্ডিয়ান</h5>
                    <span class="fs-3 fw-bold text-info"><?= $summary['total_guardians'] ?></span>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card shadow-sm border-0">
                <div class="card-body text-center">
                    <h5>মোট ফি</h5>
                    <span class="fs-3 fw-bold text-danger"><?= $summary['total_fees'] ?></span>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
