<?php 
$page_title = 'Support | FFLIPY';
$breadcrumb = 'Support';
$header_title = 'Help & Support';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h2 class="card-title">My Support Tickets</h2>
        <a href="create_ticket.php" class="primary-btn">Create New Ticket</a>
    </div>

    <table class="data-table">
        <thead>
            <tr>
                <th>Ticket ID</th>
                <th>Subject</th>
                <th>Status</th>
                <th>Last Reply</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <?php if (empty($tickets)): ?>
                <tr><td colspan="5" style="text-align: center; padding: 60px; color: var(--text-muted);">No support tickets found.</td></tr>
            <?php else: foreach ($tickets as $t): ?>
                <tr>
                    <td style="font-weight: 600;">#<?php echo htmlspecialchars($t->ticket_id); ?></td>
                    <td><?php echo htmlspecialchars($t->subject); ?></td>
                    <td><span class="badge <?php echo $t->getStatusBadge(); ?>"><?php echo $t->getStatusText(); ?></span></td>
                    <td style="font-size: 13px; color: var(--text-muted);"><?php echo format_date($t->last_reply ?: $t->created_at); ?></td>
                    <td>
                        <a href="view_ticket.php?id=<?php echo $t->id; ?>" class="nav-item" style="display:inline-flex; padding: 4px; border: 1px solid var(--border-color);">
                            <i data-lucide="message-square" style="width:16px; height:16px;"></i>
                        </a>
                    </td>
                </tr>
            <?php endforeach; endif; ?>
        </tbody>
    </table>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
