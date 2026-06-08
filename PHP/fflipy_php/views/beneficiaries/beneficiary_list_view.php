<?php 
$page_title = 'Beneficiaries | FFLIPY';
$breadcrumb = 'Beneficiaries';
$header_title = 'My Beneficiaries';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h2 class="card-title">Saved Recipients</h2>
        <a href="add_beneficiary.php" class="primary-btn">Add New</a>
    </div>

    <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; padding: 10px;">
        <?php if (empty($beneficiaries)): ?>
            <div style="grid-column: 1/-1; text-align: center; padding: 60px; color: var(--text-muted);">No beneficiaries saved yet.</div>
        <?php else: foreach ($beneficiaries as $b): ?>
            <div class="stat-card" style="flex-direction: column; align-items: flex-start; gap: 12px; position: relative;">
                <div style="display: flex; gap: 12px; align-items: center; width: 100%;">
                    <div class="recipient-avatar"><?php echo strtoupper(substr($b->firstname, 0, 1) . substr($b->lastname, 0, 1)); ?></div>
                    <div style="flex: 1;">
                        <h3 style="font-size: 16px; font-weight: 700;"><?php echo htmlspecialchars($b->getFullName()); ?></h3>
                        <p style="font-size: 13px; color: var(--text-muted);"><?php echo htmlspecialchars($b->transaction_type_name ?: 'Transfer'); ?></p>
                    </div>
                    <form method="POST" onsubmit="return confirm('Are you sure?');" style="position: absolute; top: 15px; right: 15px;">
                        <input type="hidden" name="delete_id" value="<?php echo $b->id; ?>">
                        <button type="submit" style="background:none; border:none; color: var(--danger); cursor:pointer;">
                            <i data-lucide="trash-2" style="width: 18px; height: 18px;"></i>
                        </button>
                    </form>
                </div>
                <div style="width: 100%; font-size: 14px; color: var(--text-main); background: #f9fafb; padding: 12px; border-radius: 8px;">
                    <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
                        <span style="color: var(--text-muted);">Account/Wallet:</span>
                        <span style="font-weight: 600;"><?php echo htmlspecialchars($b->account_number ?: $b->wallet_number ?: 'N/A'); ?></span>
                    </div>
                    <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
                        <span style="color: var(--text-muted);">Bank/Provider:</span>
                        <span style="font-weight: 600;"><?php echo htmlspecialchars($b->bank_name ?: $b->wallet_provider ?: $b->wallet_name ?: 'N/A'); ?></span>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <span style="color: var(--text-muted);">Country:</span>
                        <span style="font-weight: 600;"><?php echo htmlspecialchars($b->country_name ?: 'N/A'); ?></span>
                    </div>
                </div>
                <a href="../send_money/send_money.php?beneficiary_id=<?php echo $b->id; ?>" class="nav-item active" style="width: 100%; justify-content: center; margin-top: 8px;">
                    <i data-lucide="send"></i>
                    <span>Send Money</span>
                </a>
            </div>
        <?php endforeach; endif; ?>
    </div>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
