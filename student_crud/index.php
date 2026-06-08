<a href="#" onclick="loadPage('router.php?action=report'); return false;"><i class="fa fa-chart-bar me-2"></i> রিপোর্ট</a>
    <a href="#" onclick="loadPage('router.php?action=login'); return false;"><i class="fa fa-sign-in-alt me-2"></i> লগইন</a>
    <a href="#" onclick="loadPage('router.php?action=fee_index'); return false;"><i class="fa fa-money-bill me-2"></i> ফি</a>
    <a href="#" onclick="loadPage('router.php?action=result_index'); return false;"><i class="fa fa-award me-2"></i> রেজাল্ট</a>
    <a href="#" onclick="loadPage('router.php?action=attendance_index'); return false;"><i class="fa fa-calendar-check me-2"></i> উপস্থিতি</a>
    <a href="#" onclick="loadPage('router.php?action=teacher_index'); return false;"><i class="fa fa-chalkboard me-2"></i> শিক্ষক</a>
    <a href="#" onclick="loadPage('router.php?action=teacher_create'); return false;"><i class="fa fa-chalkboard-teacher me-2"></i> নতুন শিক্ষক</a>
    <a href="#" onclick="loadPage('router.php?action=subject_index'); return false;"><i class="fa fa-book me-2"></i> বিষয়</a>
    <a href="#" onclick="loadPage('router.php?action=guardian_index'); return false;"><i class="fa fa-user-friends me-2"></i> গার্ডিয়ান</a>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
   <style>
body {
    margin: 0;
    font-family: 'Segoe UI', 'Roboto', Arial, sans-serif;
    background: linear-gradient(120deg, #e3f2fd 60%, #f8fafc 100%);
}

/* Sidebar */
.sidebar {
    height: 100vh;
    width: 230px;
    background: linear-gradient(135deg, #0d6efd 80%, #6dd5ed 100%);
    color: #fff;
    position: fixed;
    top: 0;
    left: 0;
    padding-top: 28px;
    box-shadow: 2px 0 18px rgba(13,110,253,0.08);
    overflow-y: auto;
    scrollbar-width: thin;
    /* border-radius বাদ */
}
.sidebar h3 {
    text-align: center;
    margin-bottom: 28px;
    font-size: 1.7rem;
    font-weight: 600;
    letter-spacing: 1px;
    color: #fff;
    text-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.sidebar a {
    display: flex;
    align-items: center;
    color: #fff;
    padding: 15px 24px;
    text-decoration: none;
    font-size: 1.08rem;
    font-weight: 500;
    border-radius: 8px;
    margin: 0 12px 8px 12px;
    transition: background 0.2s, color 0.2s, box-shadow 0.2s;
    box-shadow: 0 2px 8px rgba(13,110,253,0.04);
}
.sidebar a:hover {
    background: rgba(255,255,255,0.18);
    color: #e3f2fd;
    box-shadow: 0 4px 16px rgba(13,110,253,0.10);
}

/* Main content */
.main-content {
    margin-left: 230px; /* sidebar width */
    background: linear-gradient(120deg, #e3f2fd 60%, #f8fafc 100%);
    min-height: 100vh;
    padding-bottom: 32px;
}

/* Top navbar */
.topnav {
    background: linear-gradient(135deg, #0d6efd 80%, #6dd5ed 100%);
    color: #fff;
    padding: 14px 32px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: sticky;
    top: 0;
    z-index: 100;
    border-bottom: 1px solid #0b5ed7;
    box-shadow: 0 2px 12px rgba(13,110,253,0.08);
    /* border-radius বাদ */
}
.topnav a.btn {
    color: #0d6efd;
    background: #fff;
    border-radius: 6px;
    font-weight: 500;
    box-shadow: 0 2px 8px rgba(13,110,253,0.08);
    transition: color 0.2s, background 0.2s;
}
.topnav a.btn:hover {
    background: #e3f2fd;
    color: #0b5ed7;
}

.header-title {
    font-size: 2.2rem;
    font-weight: 700;
    color: #0d6efd;
    margin-bottom: 18px;
    text-shadow: 0 2px 8px rgba(13,110,253,0.08);
}
.main-content p {
    font-size: 1.15rem;
    color: #333;
    margin-bottom: 0;
}
@media (max-width: 700px) {
    .sidebar { width: 100vw; height: auto; position: static; border-radius: 0; box-shadow: none; }
    .main-content { margin-left: 0; }
    .topnav { border-radius: 0; }
}
</style>

</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h3>🎓 স্টুডেন্ট ম্যানেজমেন্ট</h3>
    <a href="#" onclick="loadPage('home.php'); return false;" id="home-link"><i class="fa fa-home me-2"></i> হোম</a>
    <a href="#" onclick="loadPage('router.php?action=report'); return false;"><i class="fa fa-chart-bar me-2"></i> রিপোর্ট</a>
    <a href="#" onclick="loadPage('router.php?action=login'); return false;"><i class="fa fa-sign-in-alt me-2"></i> লগইন</a>
    <a href="#" onclick="loadPage('router.php?action=fee_index'); return false;"><i class="fa fa-money-bill me-2"></i> ফি</a>
    <a href="#" onclick="loadPage('router.php?action=result_index'); return false;"><i class="fa fa-award me-2"></i> রেজাল্ট</a>
    <a href="#" onclick="loadPage('router.php?action=attendance_index'); return false;"><i class="fa fa-calendar-check me-2"></i> উপস্থিতি</a>
    <a href="#" onclick="loadPage('router.php?action=teacher_index'); return false;"><i class="fa fa-chalkboard me-2"></i> শিক্ষক</a>
    <a href="#" onclick="loadPage('router.php?action=teacher_create'); return false;"><i class="fa fa-chalkboard-teacher me-2"></i> নতুন শিক্ষক</a>
    <a href="#" onclick="loadPage('router.php?action=subject_index'); return false;"><i class="fa fa-book me-2"></i> বিষয়</a>
    <a href="#" onclick="loadPage('router.php?action=guardian_index'); return false;"><i class="fa fa-user-friends me-2"></i> গার্ডিয়ান</a>
    <a href="#" onclick="loadPage('router.php?action=admission'); return false;"><i class="fa fa-user-plus me-2"></i> ছাত্র ভর্তি</a>
    <a href="#" onclick="loadPage('router.php?action=class_index'); return false;"><i class="fa fa-layer-group me-2"></i> ক্লাস</a>
    <a href="#" onclick="loadPage('router.php?action=section_index'); return false;"><i class="fa fa-th-large me-2"></i> সেকশন</a>
    <a href="#" onclick="loadPage('router.php?action=index'); return false;"><i class="fa fa-users me-2"></i> স্টুডেন্ট তালিকা</a>
</div>

<!-- Main content -->
<div class="main-content">
    <!-- Top navbar -->
    <div class="topnav">
        <div>Welcome, Admin</div>
        <div><a href="#" class="btn btn-light btn-sm">Logout</a></div>
    </div>

    <!-- Content area -->
    <div id="content"></div>
</div>

<script>
// AJAX page load
function loadPage(page) {
    $("#content").html('<p>লোড হচ্ছে...</p>');
    $.ajax({
        url: page,
        method: 'GET',
        success: function(data) {
            $("#content").html(data);
            // Always handle all forms in #content via AJAX
            $("#content form").off('submit').on('submit', function(e) {
                e.preventDefault();
                var $form = $(this);
                var formData = $form.serialize();
                $.ajax({
                    url: $form.attr('action') || page,
                    method: $form.attr('method') || 'POST',
                    data: formData,
                    headers: { 'X-Requested-With': 'XMLHttpRequest' },
                    success: function(resp) {
                        try {
                            var result = JSON.parse(resp);
                            if (result.success && result.redirect) {
                                loadPage(result.redirect);
                            } else {
                                $("#content").html(resp);
                            }
                        } catch (e) {
                            $("#content").html(resp);
                        }
                        // নতুন শিক্ষক যোগ হলে শিক্ষক তালিকা দেখাও
                        if (page.includes('teacher_create') || page.includes('teacher_edit')) {
                            loadPage('router.php?action=teacher_index');
                        }
                    },
                    error: function() {
                        $("#content").html('<p class="text-danger">ডাটা সংরক্ষণে সমস্যা হয়েছে।</p>');
                    }
                });
            });
            // Make all router.php links AJAX (SPA)
            $("a[href*='router.php']").off('click').on('click', function(e) {
                e.preventDefault();
                var href = $(this).attr('href');
                loadPage(href);
                return false;
            });
            // Make all edit links AJAX
            $("#content a[href*='action=edit']").off('click').on('click', function(e) {
                e.preventDefault();
                var href = $(this).attr('href');
                loadPage(href);
                return false;
            });
            $("#content a[href*='action=teacher_edit']").off('click').on('click', function(e) {
                e.preventDefault();
                var href = $(this).attr('href');
                loadPage(href);
                return false;
            });
            // Prevent any direct navigation to edit pages (student/teacher)
            if (window.location.search.indexOf('action=edit') !== -1 || window.location.search.indexOf('action=teacher_edit') !== -1) {
                var url = window.location.pathname + window.location.search;
                loadPage(url.replace(/^\//, ''));
                history.replaceState(null, '', 'index.php');
            }
            // Make all pagination links AJAX
            $("#content .pagination a").off('click').on('click', function(e) {
                e.preventDefault();
                var href = $(this).attr('href');
                if (href) {
                    loadPage(href);
                }
                return false;
            });
            // Highlight active pagination link
            var urlParams = new URLSearchParams(window.location.search);
            var currentPage = urlParams.get('page') || 1;
            $("#content .pagination li").removeClass('active');
            $("#content .pagination a").each(function() {
                var href = $(this).attr('href');
                if (href && href.indexOf('page='+currentPage) !== -1) {
                    $(this).parent().addClass('active');
                }
            });
        },
        error: function() {
            $("#content").html('<p class="text-danger">কন্টেন্ট লোড হয়নি।</p>');
        }
    });
}

// Load home page by default
$(document).ready(function() {
    loadPage('home.php');
    // SPA: All router.php pages always load inside index.php
    $(document).on('click', "a[href*='router.php']", function(e) {
        e.preventDefault();
        var href = $(this).attr('href');
        loadPage(href);
        return false;
    });
    $(document).on('click', "a[href*='action=edit']", function(e) {
        e.preventDefault();
        var href = $(this).attr('href');
        loadPage(href);
        return false;
    });
    $(document).on('click', "a[href*='action=delete']", function(e) {
        e.preventDefault();
        var href = $(this).attr('href');
        loadPage(href);
        return false;
    });
    $(document).on('click', "a[href*='action=create']", function(e) {
        e.preventDefault();
        var href = $(this).attr('href');
        loadPage(href);
        return false;
    });
    // SPA: Prevent direct navigation to router.php?action=index and always load inside index.php
    if (window.location.pathname.endsWith('/router.php') && window.location.search.includes('action=index')) {
        window.location.href = 'index.php';
        // Optionally, you can use history.replaceState for smoother SPA experience
        // history.replaceState(null, '', 'index.php');
    }
    // SPA: Prevent direct navigation to any router.php?action=... page, always load inside index.php
    if (window.location.pathname.endsWith('/router.php') && window.location.search.includes('action=')) {
        window.location.href = 'index.php';
    }
});
</script>

</body>
</html>
