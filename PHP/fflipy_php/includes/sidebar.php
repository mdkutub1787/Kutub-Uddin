<?php
/**
 * Sidebar Component - Part of the Shared Layout
 */
$user = isset($_SESSION['user']) ? unserialize($_SESSION['user']) : null;
?>
<aside class="sidebar">
    <div class="sidebar-header">
        <h2 class="logo-text">FFLIPY</h2>
    </div>

    <nav class="sidebar-nav">
        <?php
        $current_page = basename($_SERVER['PHP_SELF']);
        $menu_items = [
            ['path' => '../dashboard/home.php', 'label' => 'dashboard', 'icon' => 'layout-dashboard'],
            ['path' => '../send_money/send_money.php', 'label' => 'send_money', 'icon' => 'send'],
            ['path' => '../transactions/transactions.php', 'label' => 'transactions', 'icon' => 'history'],
            ['path' => '../transactions/track_transfer.php', 'label' => 'track_transfer', 'icon' => 'search'],
            ['path' => '../beneficiaries/beneficiaries.php', 'label' => 'beneficiaries', 'icon' => 'users'],
            ['path' => '../notifications/notifications.php', 'label' => 'notifications', 'icon' => 'bell'],
            ['path' => '../profile/qr_code.php', 'label' => 'my_qr', 'icon' => 'qr-code'],
            ['path' => '../profile/profile.php', 'label' => 'profile', 'icon' => 'user'],
            ['path' => '../support/support.php', 'label' => 'support', 'icon' => 'help-circle'],
        ];

        foreach ($menu_items as $item):
            $is_active = ($current_page == basename($item['path']));
        ?>
            <a href="<?php echo $item['path']; ?>" class="nav-item <?php echo $is_active ? 'active' : ''; ?>">
                <i data-lucide="<?php echo $item['icon']; ?>"></i>
                <span><?php echo __($item['label']); ?></span>
            </a>
        <?php endforeach; ?>
    </nav>

    <div style="padding: 16px;">
        <a href="../auth/login.php?action=logout" class="nav-item" style="color: var(--danger);">
            <i data-lucide="log-out"></i>
            <span><?php echo __('logout'); ?></span>
        </a>
    </div>
</aside>
