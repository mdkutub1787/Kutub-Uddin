<?php 
$page_title = 'My QR Code | FFLIPY';
$breadcrumb = 'Profile / QR Code';
$header_title = 'Scan to Pay';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card" style="max-width: 440px; margin: 0 auto; text-align: center; padding: 48px 24px;">
    
    <div style="margin-bottom: 24px;">
        <img src="<?php echo $userProfile->getDisplayImage(); ?>" alt="Profile" style="width: 80px; height: 80px; border-radius: 50%; object-fit: cover; border: 4px solid #eff6ff;">
        <h2 style="font-size: 20px; font-weight: 700; margin-top: 16px;"><?php echo htmlspecialchars($userProfile->getFullName()); ?></h2>
        <p style="color: var(--text-muted); font-size: 14px; margin-top: 4px;">@<?php echo htmlspecialchars($userProfile->username); ?></p>
    </div>

    <!-- Fallback QR using API -->
    <div style="background: white; padding: 16px; border-radius: 16px; display: inline-block; margin: 24px 0; border: 1px solid #f3f4f6;">
        <!-- In a real app we'd generate this properly or use an endpoint, fallback to google charts api for now using username -->
        <img src="https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=<?php echo urlencode($userProfile->username); ?>" alt="QR Code">
    </div>
    
    <p style="color: var(--text-muted); font-size: 14px; margin-bottom: 32px; line-height: 1.5;">Scan this QR code with the FFLIPY app to send money directly to this account.</p>

    <div style="display: flex; gap: 16px; justify-content: center;">
        <button class="primary-btn" onclick="window.print()" style="padding: 12px 24px;">
            <i data-lucide="printer" style="width: 18px; height: 18px; margin-right: 8px;"></i> Print
        </button>
        <a href="profile.php" class="nav-item" style="padding: 12px 24px; text-decoration: none; border: 1px solid var(--border-color);">
            Back to Profile
        </a>
    </div>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
