<?php 
$page_title = 'Dashboard | FFLIPY';
$breadcrumb = 'Dashboard';
$header_title = __($greet_key) . ', ' . htmlspecialchars($user->firstname) . '!';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="greet-card">
    <div class="greet-text">
        <h2>Manage your money with ease</h2>
        <p>Track your transfers and manage beneficiaries in one place.</p>
    </div>
    <div class="balance-info">
        <div class="balance-label">Available Balance</div>
        <div class="balance-amount">€ 1,250.00</div>
    </div>
</div>

<div class="stat-grid">
    <div class="stat-card">
        <div class="stat-icon" style="background: #eff6ff; color: #3b82f6;">
            <i data-lucide="send"></i>
        </div>
        <div class="stat-data">
            <h3>Total Sent</h3>
            <p>€ 4,500.00</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-icon" style="background: #ecfdf5; color: #10b981;">
            <i data-lucide="users"></i>
        </div>
        <div class="stat-data">
            <h3>Beneficiaries</h3>
            <p>12 Active</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-icon" style="background: #fff7ed; color: #f59e0b;">
            <i data-lucide="clock"></i>
        </div>
        <div class="stat-data">
            <h3>Pending</h3>
            <p>2 Transfers</p>
        </div>
    </div>
</div>

<div class="dashboard-layout">
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">Recent Transactions</h2>
            <a href="../transactions/transactions.php" style="color: var(--primary); font-size: 14px; font-weight: 600; text-decoration: none;">View All</a>
        </div>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Recipient</th>
                    <th>Date</th>
                    <th>Amount</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <?php if (empty($transactions)): ?>
                    <tr><td colspan="4" style="text-align: center; color: var(--text-muted); padding: 40px;">No transactions found</td></tr>
                <?php else: foreach ($transactions as $tx): ?>
                    <tr>
                        <td>
                            <div style="font-weight: 600;"><?php echo htmlspecialchars($tx->recipient_name); ?></div>
                            <div style="font-size: 12px; color: var(--text-muted);">Ref: <?php echo htmlspecialchars($tx->ref_no); ?></div>
                        </td>
                        <td><?php echo format_date($tx->created_at); ?></td>
                        <td style="font-weight: 700;"><?php echo format_currency($tx->send_amount, $tx->send_currency); ?></td>
                        <td><span class="badge <?php echo $tx->getStatusBadge(); ?>"><?php echo $tx->getStatusText(); ?></span></td>
                    </tr>
                <?php endforeach; endif; ?>
            </tbody>
        </table>
    </div>

    <div class="right-panel">
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Quick Transfer</h2>
            </div>
            <p style="color: var(--text-muted); font-size: 14px; margin-bottom: 24px;">Send money to your frequent recipients instantly.</p>
            <div class="recipient-item">
                <div class="recipient-avatar">JD</div>
                <div class="recipient-info">
                    <span class="recipient-name">John Doe</span>
                    <span class="recipient-time">Last sent 2 days ago</span>
                </div>
                <i data-lucide="chevron-right" style="color: var(--text-muted);"></i>
            </div>
            <div class="recipient-item">
                <div class="recipient-avatar" style="background: #fdf2f2; color: #ef4444;">AS</div>
                <div class="recipient-info">
                    <span class="recipient-name">Alice Smith</span>
                    <span class="recipient-time">Last sent 5 days ago</span>
                </div>
                <i data-lucide="chevron-right" style="color: var(--text-muted);"></i>
            </div>
            <a href="../send_money/send_money.php" class="primary-btn" style="width: 100%; margin-top: 24px;">New Transfer</a>
        </div>
    </div>
</div>

<style>
    .greet-card {
        background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
        color: white;
        padding: 32px;
        border-radius: var(--radius-lg);
        margin-bottom: 32px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .greet-text h2 { font-size: 24px; margin-bottom: 8px; }
    .balance-info { text-align: right; }
    .balance-label { font-size: 14px; opacity: 0.8; }
    .balance-amount { font-size: 32px; font-weight: 800; }
    
    .stat-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
        gap: 24px;
        margin-bottom: 32px;
    }
    .stat-card {
        background: white;
        padding: 24px;
        border-radius: var(--radius-lg);
        border: 1px solid var(--border-color);
        display: flex;
        align-items: center;
        gap: 16px;
    }
    .stat-icon {
        width: 48px;
        height: 48px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .stat-data h3 { font-size: 14px; color: var(--text-muted); margin-bottom: 4px; }
    .stat-data p { font-size: 20px; font-weight: 700; }
</style>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
