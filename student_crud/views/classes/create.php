<!DOCTYPE html>
<html lang="bn">
<head>
    <meta charset="UTF-8">
    <title>নতুন ক্লাস যোগ করুন</title>
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
    </style>
</head>
<body class="container py-5">
    <h2 class="mb-4 text-primary">নতুন ক্লাস যোগ করুন</h2>
    <?php if (!empty($error)): ?><div class="alert alert-danger"> <?= $error ?> </div><?php endif; ?>
    <form method="post">
        <div class="mb-3">
            <label class="form-label">ক্লাসের নাম</label>
            <input type="text" name="name" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-success">যোগ করুন</button>
    </form>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
    $(function() {
        $('form').on('submit', function(e) {
            e.preventDefault();
            var $form = $(this);
            $.ajax({
                url: '',
                method: 'POST',
                data: $form.serialize(),
                dataType: 'json',
                success: function(res) {
                    if(res.success && res.redirect) {
                        if(window.loadPage) {
                            loadPage(res.redirect);
                        } else {
                            window.location.href = res.redirect;
                        }
                    } else if(res.error) {
                        $form.prepend('<div class="alert alert-danger">'+res.error+'</div>');
                    }
                },
                error: function(xhr) {
                    $form.prepend('<div class="alert alert-danger">সার্ভার সমস্যা!</div>');
                }
            });
        });
    });
    </script>
</body>
</html>
