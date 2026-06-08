<?php // ...existing code... ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Student</title>
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
            box-shadow: 0 2px 8px rgba(25,135,84,0.10);
            transition: background 0.2s, color 0.2s;
        }
        .btn-success.btn-lg-custom:hover {
            background: linear-gradient(135deg, #198754 80%, #6dd5ed 100%);
            color: #fff;
        }
        .btn-secondary.btn-lg-custom:hover {
            background: #e3f2fd;
            color: #0d6efd;
        }
        .btn-container { display: flex; justify-content: space-between; }
        .alert {
            margin-bottom: 20px;
            border-radius: 8px;
            font-size: 1.08rem;
            font-weight: 500;
        }
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
            color: #198754;
            text-shadow: 0 2px 8px rgba(25,135,84,0.08);
        }
        @media (max-width: 700px) {
            h2 { font-size: 1.2rem; }
            .card { padding: 1rem; }
        }
    </style>
</head>
<body class="container py-5">

    <!-- Professional Student Add Form Title -->
    <h2 class="mb-4 text-center text-success">নতুন ছাত্র ভর্তি করুন</h2>

    <?php if (!empty($error)): ?>
        <div class="alert alert-warning text-center"><?= htmlspecialchars($error) ?></div>
    <?php endif; ?>

    <form method="POST" class="card p-4 shadow" enctype="multipart/form-data">
        <div class="mb-3">
            <label class="form-label">Name:</label>
            <input type="text" name="name" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Roll:</label>
            <input type="text" name="roll" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Class:</label>
            <select name="class_id" class="form-control" required>
                <option value="">-- Select Class --</option>
                <?php include_once 'models/SchoolClass.php'; $classModel=new SchoolClass($conn); $classes=$classModel->all(); if($classes && $classes->num_rows>0): foreach($classes as $row): ?>
                <option value="<?= $row['id'] ?>"><?= htmlspecialchars($row['name']) ?></option>
                <?php endforeach; endif; ?>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Section:</label>
            <select name="section_id" class="form-control">
                <option value="">-- Select Section --</option>
                <?php include_once 'models/Section.php'; $sectionModel=new Section($conn); $sections=$sectionModel->all(); if($sections && $sections->num_rows>0): foreach($sections as $row): ?>
                <option value="<?= $row['id'] ?>"><?= htmlspecialchars($row['name']) ?></option>
                <?php endforeach; endif; ?>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Date of Birth:</label>
            <input type="date" name="dob" class="form-control">
        </div>
        <div class="mb-3">
            <label class="form-label">Gender:</label>
            <select name="gender" class="form-control">
                <option value="">-- Select Gender --</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Guardian:</label>
            <select name="guardian_id" class="form-control">
                <option value="">-- Select Guardian --</option>
                <?php include_once 'models/Guardian.php'; $guardianModel=new Guardian($conn); $guardians=$guardianModel->all(); if($guardians && $guardians->num_rows>0): foreach($guardians as $row): ?>
                <option value="<?= $row['id'] ?>"><?= htmlspecialchars($row['name']) ?></option>
                <?php endforeach; endif; ?>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Address:</label>
            <input type="text" name="address" class="form-control">
        </div>
        <div class="mb-3">
            <label class="form-label">Phone:</label>
            <input type="text" name="phone" class="form-control">
        </div>
        <div class="mb-3">
            <label for="photo" class="form-label">ছবি</label>
            <input type="file" name="photo" id="photo" class="form-control" accept="image/*">
            <small class="text-muted">ছবি ফাইল সিলেক্ট করুন অথবা লিংক দিন</small>
        </div>
        <div class="mb-3">
            <label class="form-label">Admission Date:</label>
            <input type="date" name="admission_date" class="form-control" value="<?= date('Y-m-d') ?>">
        </div>
        <div class="mb-3">
            <label class="form-label">Status:</label>
            <select name="status" class="form-control">
                <option value="active">Active</option>
                <option value="inactive">Inactive</option>
            </select>
        </div>
        <div class="btn-container mt-3">
            <button type="submit" class="btn btn-success btn-lg btn-lg-custom">Save</button>
            <a href="router.php?action=index" class="btn btn-secondary btn-lg btn-lg-custom">Back</a>
        </div>
        <div class="d-grid gap-2 mt-4">
            <button type="submit" class="btn btn-success btn-lg">স্টুডেন্ট যোগ করুন</button>
        </div>
    </form>

</body>
</html>
