<?php 
$page_title = 'Update Profile | FFLIPY';
$breadcrumb = 'Profile / Update';
$header_title = 'Edit Personal Details';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card" style="max-width: 900px; margin: 0 auto;">
    <div class="card-header">
        <h2 class="card-title">Comprehensive Profile Update</h2>
    </div>

    <?php if (isset($error)): ?>
        <div class="error-msg" style="margin: 0 24px 24px; padding: 12px; background: rgba(239, 68, 68, 0.1); color: #ef4444; border-radius: 8px;">
            <?php echo htmlspecialchars($error); ?>
        </div>
    <?php endif; ?>

    <form method="POST" enctype="multipart/form-data" style="padding: 24px;">
        
        <h3 style="margin-bottom: 16px; font-size: 18px; border-bottom: 1px solid var(--border-color); padding-bottom: 8px;">Basic Information</h3>
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">First Name</label>
                <input type="text" name="firstname" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->firstname ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Last Name</label>
                <input type="text" name="lastname" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->lastname ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Username</label>
                <input type="text" name="username" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->username ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Date of Birth</label>
                <input type="date" name="date_of_birth" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->date_of_birth ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Place of Birth</label>
                <input type="text" name="place_of_birth" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->place_of_birth ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Occupation</label>
                <input type="text" name="occupation" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->occupation ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Gender</label>
                <?php if (!empty($genderTypes)): ?>
                    <select name="gender_type" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
                        <option value="">Select Gender</option>
                        <?php foreach($genderTypes as $gender): ?>
                            <option value="<?php echo htmlspecialchars($gender['id'] ?? $gender['code'] ?? ''); ?>" <?php echo ($userProfile->gender_type == ($gender['id'] ?? $gender['code']) ? 'selected' : ''); ?>><?php echo htmlspecialchars($gender['name'] ?? $gender['title'] ?? ''); ?></option>
                        <?php endforeach; ?>
                    </select>
                <?php else: ?>
                    <input type="text" name="gender_type" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" placeholder="e.g. 1 or 2" value="<?php echo htmlspecialchars($userProfile->gender_type ?? ''); ?>">
                <?php endif; ?>
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Language ID</label>
                <input type="text" name="language_id" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" placeholder="e.g. 1" value="<?php echo htmlspecialchars($userProfile->language_id ?? '1'); ?>">
            </div>
        </div>

        <h3 style="margin-bottom: 16px; font-size: 18px; border-bottom: 1px solid var(--border-color); padding-bottom: 8px;">Address Information</h3>
        <div class="form-group" style="margin-bottom: 24px;">
            <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Full Address</label>
            <input type="text" name="address" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->address ?? ''); ?>">
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">City</label>
                <input type="text" name="city" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->city ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">State</label>
                <input type="text" name="state" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->state ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Post Code</label>
                <input type="text" name="post_code" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->post_code ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Country</label>
                <?php if (!empty($countries)): ?>
                    <select name="country_id" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
                        <option value="">Select Country</option>
                        <?php foreach($countries as $country): ?>
                            <option value="<?php echo htmlspecialchars($country['id']); ?>" <?php echo ($userProfile->country_id == $country['id'] ? 'selected' : ''); ?>><?php echo htmlspecialchars($country['name']); ?></option>
                        <?php endforeach; ?>
                    </select>
                <?php else: ?>
                    <input type="text" name="country_id" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->country_id ?? ''); ?>" placeholder="e.g. 2">
                <?php endif; ?>
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Nationality</label>
                <input type="text" name="nationality" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->nationality ?? ''); ?>">
            </div>
        </div>

        <h3 style="margin-bottom: 16px; font-size: 18px; border-bottom: 1px solid var(--border-color); padding-bottom: 8px;">Financial Details</h3>
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Source of Fund</label>
                <input type="text" name="source_of_fund" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->source_of_fund ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Yearly Income</label>
                <input type="text" name="yearly_income" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->yearly_income ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Daily Limit</label>
                <input type="text" name="daily_limit" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->daily_limit ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Monthly Limit</label>
                <input type="text" name="monthly_limit" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->monthly_limit ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Yearly Limit</label>
                <input type="text" name="yearly_limit" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->yearly_limit ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Remitter Type</label>
                <?php if (!empty($remitterTypes)): ?>
                    <select name="remitter_type" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
                        <option value="">Select Remitter Type</option>
                        <?php foreach($remitterTypes as $rem): ?>
                            <option value="<?php echo htmlspecialchars($rem['id'] ?? $rem['code'] ?? ''); ?>" <?php echo ($userProfile->remitter_type == ($rem['id'] ?? $rem['code']) ? 'selected' : ''); ?>><?php echo htmlspecialchars($rem['name'] ?? $rem['title'] ?? ''); ?></option>
                        <?php endforeach; ?>
                    </select>
                <?php else: ?>
                    <input type="text" name="remitter_type" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" placeholder="e.g. 1 or 2" value="<?php echo htmlspecialchars($userProfile->remitter_type ?? ''); ?>">
                <?php endif; ?>
            </div>
        </div>

        <h3 style="margin-bottom: 16px; font-size: 18px; border-bottom: 1px solid var(--border-color); padding-bottom: 8px;">Declarations (Optional)</h3>
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Declaration Amount</label>
                <input type="text" name="declaration_amount" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->declaration_amount ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Remarks</label>
                <input type="text" name="remarks" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->remarks ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Declaration Start Date</label>
                <input type="date" name="declaration_start_date" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->declaration_start_date ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Declaration End Date</label>
                <input type="date" name="declaration_end_date" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->declaration_end_date ?? ''); ?>">
            </div>
        </div>

        <h3 style="margin-bottom: 16px; font-size: 18px; border-bottom: 1px solid var(--border-color); padding-bottom: 8px;">Documents & Uploads</h3>
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Document Type</label>
                <?php if (!empty($docTypes)): ?>
                    <select name="document_type" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
                        <option value="">Select Document Type</option>
                        <?php foreach($docTypes as $doc): ?>
                            <option value="<?php echo htmlspecialchars($doc['document_code'] ?? $doc['id'] ?? ''); ?>" <?php echo ($userProfile->document_type == ($doc['document_code'] ?? $doc['id']) ? 'selected' : ''); ?>><?php echo htmlspecialchars($doc['document_type'] ?? $doc['name'] ?? ''); ?></option>
                        <?php endforeach; ?>
                    </select>
                <?php else: ?>
                    <input type="text" name="document_type" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" placeholder="e.g. 4" value="<?php echo htmlspecialchars($userProfile->document_type ?? ''); ?>">
                <?php endif; ?>
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Document ID Number</label>
                <input type="text" name="document_id_number" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->document_id_number ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Issue Country Code</label>
                <input type="text" name="issue_country_code" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" placeholder="e.g. BD" value="<?php echo htmlspecialchars($userProfile->issue_country_code ?? ''); ?>">
            </div>
            <div class="form-group"></div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Document Issue Date</label>
                <input type="date" name="document_issue_date" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->document_issue_date ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Document Expiry Date</label>
                <input type="date" name="document_expiry_date" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" value="<?php echo htmlspecialchars($userProfile->document_expiry_date ?? ''); ?>">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Upload Document (Image/PDF)</label>
                <input type="file" name="document_upload" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Upload Profile Image</label>
                <input type="file" name="image" accept="image/*" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
            </div>
        </div>

        <div style="display: flex; gap: 16px; margin-top: 32px;">
            <button type="submit" class="primary-btn" style="flex: 1; padding: 14px;">Save Detailed Profile</button>
            <a href="profile.php" class="nav-item" style="flex: 1; justify-content: center; text-decoration: none; border: 1px solid var(--border-color);">Cancel</a>
        </div>
    </form>
</div>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
