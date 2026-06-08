<?php 
$page_title = 'Send Money | FFLIPY';
$breadcrumb = 'Send Money';
$header_title = 'International Money Transfer';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card" style="max-width: 600px; margin: 0 auto; text-align: center; padding: 60px;">
    <div style="width: 80px; height: 80px; background: #eff6ff; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 24px; color: var(--primary);">
        <i data-lucide="send" style="width: 40px; height: 40px;"></i>
    </div>
    <h2 style="margin-bottom: 16px;">Send Money Feature</h2>
    <p style="color: var(--text-muted); margin-bottom: 32px;">The multi-step international money transfer process is being refactored to the new MVC architecture. This will include recipient selection, amount calculation with real-time exchange rates, and secure OTP verification.</p>
    
    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
        <a href="../dashboard/home.php" class="nav-item" style="justify-content: center; border: 1px solid var(--border-color);">Back to Dashboard</a>
        <a href="../beneficiaries/beneficiaries.php" class="primary-btn">Select Beneficiary</a>
    </div>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
