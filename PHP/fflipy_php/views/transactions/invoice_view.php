<?php 
$page_title = 'Invoice | FFLIPY';
$breadcrumb = 'Transactions / Invoice';
$header_title = 'Transaction Invoice';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card" style="max-width: 800px; margin: 0 auto; padding: 48px;">
    <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 48px;">
        <div>
            <h1 style="color: var(--primary); font-size: 32px; font-weight: 800;">FFLIPY</h1>
            <p style="color: var(--text-muted);">Secure Global Money Transfer</p>
        </div>
        <div style="text-align: right;">
            <h2 style="font-size: 18px; font-weight: 700;">INVOICE</h2>
            <p style="color: var(--text-muted);">#<?php echo htmlspecialchars($transaction->ref_no); ?></p>
            <p style="color: var(--text-muted);"><?php echo format_date($transaction->created_at); ?></p>
        </div>
    </div>

    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 48px; margin-bottom: 48px; padding-bottom: 32px; border-bottom: 2px solid #f3f4f6;">
        <div>
            <h3 style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 12px;">Recipient</h3>
            <p style="font-weight: 700; font-size: 18px;"><?php echo htmlspecialchars($transaction->recipient_name); ?></p>
        </div>
        <div style="text-align: right;">
            <h3 style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 12px;">Status</h3>
            <span class="badge <?php echo $transaction->getStatusBadge(); ?>" style="padding: 8px 16px; font-size: 14px;"><?php echo $transaction->getStatusText(); ?></span>
        </div>
    </div>

    <div style="margin-bottom: 48px;">
        <table style="width: 100%; border-collapse: collapse;">
            <tr style="border-bottom: 1px solid #f3f4f6;">
                <td style="padding: 16px 0; color: var(--text-muted);">Sending Amount</td>
                <td style="padding: 16px 0; text-align: right; font-weight: 600;"><?php echo format_currency($transaction->send_amount, $transaction->send_currency); ?></td>
            </tr>
            <tr style="border-bottom: 1px solid #f3f4f6;">
                <td style="padding: 16px 0; color: var(--text-muted);">Total Fees</td>
                <td style="padding: 16px 0; text-align: right; font-weight: 600;"><?php echo format_currency($transaction->fees, $transaction->send_currency); ?></td>
            </tr>
            <tr>
                <td style="padding: 24px 0; font-size: 20px; font-weight: 700;">Recipient Receives</td>
                <td style="padding: 24px 0; text-align: right; font-size: 24px; font-weight: 800; color: var(--primary);"><?php echo format_currency($transaction->receive_amount, $transaction->receive_currency); ?></td>
            </tr>
        </table>
    </div>

    <div style="text-align: center; color: var(--text-muted); font-size: 12px; border-top: 1px solid #f3f4f6; padding-top: 24px;">
        <p>Thank you for using FFLIPY for your international money transfer.</p>
        <p>If you have any questions, please contact our support team.</p>
    </div>

    <div style="margin-top: 32px; display: flex; justify-content: center; gap: 16px;">
        <button onclick="window.print()" class="nav-item" style="padding: 12px 24px; border: 1px solid var(--border-color);">
            <i data-lucide="printer"></i>
            <span>Print Invoice</span>
        </button>
        <a href="transactions.php" class="primary-btn">Back to History</a>
    </div>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
