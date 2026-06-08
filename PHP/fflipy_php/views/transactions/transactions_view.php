<?php 
$page_title = 'Transactions | FFLIPY';
$breadcrumb = 'Transactions';
$header_title = 'Transaction History';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h2 class="card-title">All Transactions</h2>
        <div class="card-actions">
            <!-- Filter or search can go here -->
        </div>
    </div>
    
    <table class="data-table">
        <thead>
            <tr>
                <th>Reference</th>
                <th>Recipient</th>
                <th>Date</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <?php if (empty($transactions)): ?>
                <tr><td colspan="6" style="text-align: center; padding: 60px; color: var(--text-muted);">No transactions found.</td></tr>
            <?php else: foreach ($transactions as $tx): ?>
                <tr>
                    <td style="font-weight: 500;">#<?php echo htmlspecialchars($tx->ref_no); ?></td>
                    <td><?php echo htmlspecialchars($tx->recipient_name); ?></td>
                    <td><?php echo format_date($tx->created_at); ?></td>
                    <td style="font-weight: 700;"><?php echo format_currency($tx->send_amount, $tx->send_currency); ?></td>
                    <td><span class="badge <?php echo $tx->getStatusBadge(); ?>"><?php echo $tx->getStatusText(); ?></span></td>
                    <td>
                        <a href="invoice.php?id=<?php echo $tx->id; ?>" class="nav-item" style="display:inline-flex; padding: 4px; border: 1px solid var(--border-color);">
                            <i data-lucide="file-text" style="width:16px; height:16px;"></i>
                        </a>
                    </td>
                </tr>
            <?php endforeach; endif; ?>
        </tbody>
    </table>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
