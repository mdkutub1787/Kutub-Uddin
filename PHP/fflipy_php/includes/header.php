<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $page_title ?? 'FFLIPY'; ?></title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../../assets/css/style.css">
    <script src="https://unpkg.com/lucide@latest"></script>
</head>
<body>
    <div class="app-container">
        <?php include __DIR__ . '/sidebar.php'; ?>
        <main class="main-content">
            <header class="top-header">
                <div class="breadcrumb">Pages / <?php echo $breadcrumb ?? 'Dashboard'; ?></div>
                <h1 class="header-title"><?php echo $header_title ?? 'Dashboard'; ?></h1>
            </header>
