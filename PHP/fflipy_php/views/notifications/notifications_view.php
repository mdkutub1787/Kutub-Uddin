<?php 
$page_title = 'Notifications | FFLIPY';
$breadcrumb = 'Notifications';
$header_title = 'My Notifications';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card" style="max-width: 800px; margin: 0 auto;">
    <div class="card-header">
        <h2 class="card-title">Recent Updates</h2>
        <button class="primary-btn" style="background:none; color: var(--primary); font-size: 14px; padding:0;">Mark all as read</button>
    </div>

    <div class="notification-list">
        <?php if (empty($notifications)): ?>
            <div style="text-align: center; padding: 60px;">
                <i data-lucide="bell-off" style="width: 48px; height: 48px; color: var(--text-muted); margin-bottom: 16px;"></i>
                <p style="color: var(--text-muted);">No notifications yet.</p>
            </div>
        <?php else: foreach ($notifications as $n): ?>
            <div style="padding: 20px; border-bottom: 1px solid var(--border-color); display: flex; gap: 16px; align-items: flex-start; <?php echo !$n->isRead ? 'background: #f8faff;' : ''; ?>">
                <div style="width: 10px; height: 10px; border-radius: 50%; background: <?php echo $n->isRead ? 'transparent' : 'var(--primary-light)'; ?>; margin-top: 6px; flex-shrink: 0;"></div>
                <div style="flex: 1;">
                    <p style="font-size: 15px; color: var(--text-main); margin-bottom: 4px; <?php echo !$n->isRead ? 'font-weight: 600;' : ''; ?>">
                        <?php echo htmlspecialchars($n->message); ?>
                    </p>
                    <span style="font-size: 12px; color: var(--text-muted);"><?php echo format_date($n->created_at); ?></span>
                </div>
            </div>
        <?php endforeach; endif; ?>
    </div>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
