<?php 
$page_title = 'My Profile | FFLIPY';
$breadcrumb = 'Profile';
$header_title = 'User Profile';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="dashboard-layout">
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">Profile Information</h2>
            <a href="update_profile.php" class="primary-btn" style="padding: 8px 16px; font-size: 14px;">Update</a>
        </div>

        <div style="display: flex; align-items: center; gap: 24px; margin-bottom: 32px; padding-bottom: 32px; border-bottom: 1px solid var(--border-color);">
            <img src="<?php echo $userProfile->getDisplayImage(); ?>" style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover; border: 4px solid #eff6ff;">
            <div>
                <h3 style="font-size: 24px; font-weight: 700;"><?php echo htmlspecialchars($userProfile->getFullName()); ?></h3>
                <p style="color: var(--text-muted);"><?php echo htmlspecialchars($userProfile->email); ?></p>
                <div style="margin-top: 8px;">
                    <span class="badge badge-success">Verified User</span>
                </div>
            </div>
        </div>

        <style>
            .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; }
            .info-item label { display: block; font-size: 12px; color: var(--text-muted); margin-bottom: 4px; font-weight: 600; text-transform: uppercase; }
            .info-item p { font-size: 16px; font-weight: 500; }
        </style>

        <div class="info-grid">
            <div class="info-item">
                <label>Username</label>
                <p><?php echo htmlspecialchars($userProfile->username); ?></p>
            </div>
            <div class="info-item">
                <label>Phone Number</label>
                <p><?php echo htmlspecialchars($userProfile->phone ?: 'N/A'); ?></p>
            </div>
            <div class="info-item">
                <label>Country</label>
                <p><?php echo htmlspecialchars($userProfile->country ?: 'N/A'); ?></p>
            </div>
            <div class="info-item">
                <label>Address</label>
                <p><?php echo htmlspecialchars($userProfile->address ?: 'N/A'); ?></p>
            </div>
            <div class="info-item">
                <label>Referral Code</label>
                <p style="color: var(--primary); font-weight: 700;"><?php echo htmlspecialchars($userProfile->referral_code ?: 'N/A'); ?></p>
            </div>
        </div>
    </div>

    <div class="right-panel">
        <div class="card" style="text-align: center;">
            <h2 class="card-title" style="margin-bottom: 24px;">Quick Actions</h2>
            <a href="qr_code.php" class="nav-item" style="justify-content: center; background: #eff6ff; color: var(--primary); margin-bottom: 12px; border: 1px solid #dbeafe;">
                <i data-lucide="qr-code"></i>
                <span>Show My QR</span>
            </a>
            <a href="../auth/login.php?action=logout" class="nav-item" style="justify-content: center; background: #fef2f2; color: var(--danger); border: 1px solid #fee2e2;">
                <i data-lucide="log-out"></i>
                <span>Logout</span>
            </a>
        </div>
    </div>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
