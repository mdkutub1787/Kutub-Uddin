<?php
// Home page for SPA dashboard
include_once __DIR__ . '/db/db.php';
include_once "models/Teacher.php";
$teacherModel = new Teacher($conn);
$teachers = $teacherModel->all();
$total_teachers = $teachers ? $teachers->num_rows : 0;
$result = $conn->query("SELECT COUNT(*) as total FROM students");
$total_students = $result->fetch_assoc()['total'];
?>
<div class="container py-4">

    <div class="row mb-4">
        <div class="col-md-6 mb-2">
            <div class="card shadow-sm border-0">
                <div class="card-body d-flex align-items-center">
                    <i class="fa fa-users fa-2x text-success me-3"></i>
                    <div>
                        <h5 class="mb-0">মোট স্টুডেন্ট</h5>
                        <span class="fs-4 fw-bold text-success"><?= $total_students ?></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6 mb-2">
            <div class="card shadow-sm border-0">
                <div class="card-body d-flex align-items-center">
                    <i class="fa fa-chalkboard-teacher fa-2x text-primary me-3"></i>
                    <div>
                        <h5 class="mb-0">মোট টিচার</h5>
                        <span class="fs-4 fw-bold text-primary"><?= $total_teachers ?></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body">
            <h4 class="mb-3 text-primary">ড্যাশবোর্ড ব্যবহারের নির্দেশনা</h4>
            <ul class="list-group list-group-flush">
                <li class="list-group-item">স্টুডেন্ট ও টিচার যোগ/সম্পাদনা/ডিলিট করতে সাইডবারের অপশন ব্যবহার করুন।</li>
                <li class="list-group-item">সব তথ্য বাংলায় ও সহজভাবে দেখানো হয়েছে।</li>
                <li class="list-group-item">ড্যাশবোর্ডটি সম্পূর্ণ SPA, তাই কোনো রিলোড ছাড়াই সব কাজ করুন।</li>
                <li class="list-group-item">যেকোনো সমস্যা হলে রিফ্রেশ দিন অথবা লগআউট করুন।</li>
            </ul>
        </div>
    </div>
</div>
