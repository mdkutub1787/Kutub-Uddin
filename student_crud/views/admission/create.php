<!DOCTYPE html>
<html lang="bn">
<head>
    <meta charset="UTF-8">
    <title>নতুন ছাত্র ভর্তি</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(120deg, #e3f2fd 60%, #f8fafc 100%); }
        .card { border-radius: 18px; box-shadow: 0 4px 18px rgba(13,110,253,0.10); border: none; }
        .card-header { background: linear-gradient(135deg, #0d6efd 80%, #6dd5ed 100%); color: #fff; font-size: 1.3rem; font-weight: 600; border-radius: 18px 18px 0 0; }
        .form-label { font-weight: 500; color: #0d6efd; }
        .btn-success { font-weight: 500; border-radius: 8px; box-shadow: 0 2px 8px rgba(25,135,84,0.10); padding: 0.5rem 1.2rem; }
        .btn-success:hover { background: linear-gradient(135deg, #198754 80%, #6dd5ed 100%); color: #fff; }
        .btn-secondary { border-radius: 8px; }
        .fa-user-plus { color: #fff; margin-right: 10px; }
    </style>
</head>
<body class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-7">
            <div class="card">
                <div class="card-header d-flex align-items-center">
                    <i class="fa fa-user-plus fa-2x"></i>
                    <span>নতুন ছাত্র ভর্তি</span>
                </div>
                <div class="card-body">
                    <?php if (!empty($error)): ?>
                        <div class="alert alert-danger mb-3"> <?= $error ?> </div>
                    <?php endif; ?>
                    <form method="post">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label">নাম</label>
                                <input type="text" name="name" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">রোল</label>
                                <input type="text" name="roll" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">ক্লাস</label>
                                <select name="class_id" class="form-control" required>
                                    <option value="">-- ক্লাস নির্বাচন করুন --</option>
                                    <?php if ($classes && $classes->num_rows > 0): foreach($classes as $row): ?>
                                        <option value="<?= $row['id'] ?>"><?= htmlspecialchars($row['name']) ?></option>
                                    <?php endforeach; endif; ?>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">সেকশন</label>
                                <select name="section_id" class="form-control">
                                    <option value="">-- সেকশন নির্বাচন করুন --</option>
                                    <?php if ($sections && $sections->num_rows > 0): foreach($sections as $row): ?>
                                        <option value="<?= $row['id'] ?>"><?= htmlspecialchars($row['name']) ?></option>
                                    <?php endforeach; endif; ?>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">জন্ম তারিখ</label>
                                <input type="date" name="dob" class="form-control">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">লিঙ্গ</label>
                                <select name="gender" class="form-control">
                                    <option value="">নির্বাচন করুন</option>
                                    <option value="male">ছেলে</option>
                                    <option value="female">মেয়ে</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">গার্ডিয়ান আইডি</label>
                                <select name="guardian_id" class="form-control">
                                    <option value="">-- গার্ডিয়ান নির্বাচন করুন --</option>
                                    <?php if ($guardians && $guardians->num_rows > 0): foreach($guardians as $row): ?>
                                        <option value="<?= $row['id'] ?>"><?= htmlspecialchars($row['name']) ?></option>
                                    <?php endforeach; endif; ?>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">মোবাইল</label>
                                <input type="text" name="phone" class="form-control">
                            </div>
                            <div class="col-12">
                                <label class="form-label">ঠিকানা</label>
                                <input type="text" name="address" class="form-control">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">ছবি</label>
                                <input type="file" name="photo" class="form-control" accept="image/*">
                                <small class="text-muted">ছবি ফাইল সিলেক্ট করুন অথবা লিংক দিন</small>
                            </div>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mt-4">
                            <button type="submit" class="btn btn-success px-4"><i class="fa fa-plus me-2"></i> ভর্তি করুন</button>
                            <a href="#" onclick="window.history.back();return false;" class="btn btn-secondary"> <i class="fa fa-arrow-left me-1"></i> ফিরে যান</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
