<?php 
$page_title = 'Add Beneficiary | FFLIPY';
$breadcrumb = 'Beneficiaries / Add';
$header_title = 'Add New Recipient';
include __DIR__ . '/../../includes/header.php'; 
?>

<div class="card" style="max-width: 800px; margin: 0 auto;">
    <div class="card-header">
        <h2 class="card-title">Recipient Details</h2>
    </div>

    <?php if (isset($error)): ?>
        <div class="error-msg" style="margin: 0 24px 24px; padding: 12px; background: #fee2e2; color: #b91c1c; border-radius: 8px;"><?php echo htmlspecialchars($error); ?></div>
    <?php endif; ?>

    <form method="POST" style="padding: 24px;">
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">First Name</label>
                <input type="text" name="FirstName" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" required>
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Last Name</label>
                <input type="text" name="LastName" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" required>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Email Address</label>
                <input type="email" name="Email" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Phone Number</label>
                <input type="text" name="PhoneNumber" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" required>
            </div>
        </div>

        <div class="form-group" style="margin-bottom: 24px;">
            <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Address</label>
            <input type="text" name="Address1" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Country</label>
                <select name="CountryCode" id="CountryCode" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" required onchange="onCountryChange()">
                    <option value="">Select Country</option>
                    <?php foreach($countries as $country): ?>
                        <option value="<?php echo htmlspecialchars($country['id']); ?>"><?php echo htmlspecialchars($country['name']); ?></option>
                    <?php endforeach; ?>
                </select>
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">City</label>
                <input type="text" name="CityCode" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)">
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px;">
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Relationship to Sender</label>
                <select name="RelationshipToSender" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" required>
                    <option value="">Select Relationship</option>
                    <?php foreach($relationships as $rel): ?>
                        <option value="<?php echo htmlspecialchars($rel['id']); ?>"><?php echo htmlspecialchars($rel['title']); ?></option>
                    <?php endforeach; ?>
                </select>
            </div>
            <div class="form-group">
                <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Transaction Type (Service)</label>
                <select name="TransactionType" id="TransactionType" class="form-control" style="background:#f9fafb; border:1px solid var(--border-color); color:var(--text-main)" required onchange="onTransactionTypeChange()">
                    <option value="">Select Country First</option>
                </select>
            </div>
        </div>

        <!-- Bank Details Section -->
        <div id="bank-section" style="display: none; padding: 20px; background: #f3f4f6; border-radius: 8px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 16px;">Bank Details</h3>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 16px;">
                <div class="form-group">
                    <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Bank Name</label>
                    <select name="bank_name" id="bank_name" class="form-control" style="background:#fff; border:1px solid var(--border-color); color:var(--text-main)" onchange="onBankChange()">
                        <option value="">Select Bank</option>
                    </select>
                </div>
                <div class="form-group">
                    <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Branch Name</label>
                    <select name="branch_name" id="branch_name" class="form-control" style="background:#fff; border:1px solid var(--border-color); color:var(--text-main)">
                        <option value="">Select Branch</option>
                    </select>
                </div>
            </div>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px;">
                <div class="form-group">
                    <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Account Type</label>
                    <select name="AccountType" class="form-control" style="background:#fff; border:1px solid var(--border-color); color:var(--text-main)">
                        <option value="">Select Account Type</option>
                        <?php foreach($accountTypes as $acc): ?>
                            <option value="<?php echo htmlspecialchars($acc['id']); ?>"><?php echo htmlspecialchars($acc['name']); ?></option>
                        <?php endforeach; ?>
                    </select>
                </div>
                <div class="form-group">
                    <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Account Number</label>
                    <input type="text" name="AccountNumber" class="form-control" style="background:#fff; border:1px solid var(--border-color); color:var(--text-main)">
                </div>
            </div>
        </div>

        <!-- Wallet Details Section -->
        <div id="wallet-section" style="display: none; padding: 20px; background: #f3f4f6; border-radius: 8px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 16px;">Wallet Details</h3>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px;">
                <div class="form-group">
                    <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Wallet Provider</label>
                    <select name="WalletProvider" id="WalletProvider" class="form-control" style="background:#fff; border:1px solid var(--border-color); color:var(--text-main)">
                        <option value="">Select Wallet Provider</option>
                    </select>
                </div>
                <div class="form-group">
                    <label style="color:var(--text-main); display:block; margin-bottom: 8px; font-weight: 500; font-size: 14px;">Wallet Number</label>
                    <input type="text" name="WalletNumber" class="form-control" style="background:#fff; border:1px solid var(--border-color); color:var(--text-main)">
                </div>
            </div>
        </div>

        <button type="submit" class="primary-btn" style="width: 100%;">Save Beneficiary</button>
    </form>
</div>

<script>
async function onCountryChange() {
    const countryId = document.getElementById('CountryCode').value;
    const typeSelect = document.getElementById('TransactionType');
    
    typeSelect.innerHTML = '<option value="">Loading services...</option>';
    document.getElementById('bank-section').style.display = 'none';
    document.getElementById('wallet-section').style.display = 'none';
    
    if (!countryId) {
        typeSelect.innerHTML = '<option value="">Select Country First</option>';
        return;
    }

    try {
        const response = await fetch(`beneficiaries.php?action=get_facilities&country_id=${countryId}`);
        const result = await response.json();
        
        typeSelect.innerHTML = '<option value="">Select Transaction Type</option>';
        if (result.success && result.data) {
            result.data.forEach(item => {
                const opt = document.createElement('option');
                opt.value = item.id;
                opt.textContent = item.name;
                typeSelect.appendChild(opt);
            });
        }
    } catch (e) {
        console.error(e);
        typeSelect.innerHTML = '<option value="">Failed to load services</option>';
    }
}

async function onTransactionTypeChange() {
    const typeSelect = document.getElementById('TransactionType');
    const selectedText = typeSelect.options[typeSelect.selectedIndex].text.toLowerCase();
    const countryId = document.getElementById('CountryCode').value;
    
    const bankSection = document.getElementById('bank-section');
    const walletSection = document.getElementById('wallet-section');
    const bankSelect = document.getElementById('bank_name');
    const walletSelect = document.getElementById('WalletProvider');
    
    bankSection.style.display = 'none';
    walletSection.style.display = 'none';
    
    if (selectedText.includes('bank') || selectedText.includes('account deposit')) {
        bankSection.style.display = 'block';
        bankSelect.innerHTML = '<option value="">Loading banks...</option>';
        
        try {
            const res = await fetch(`beneficiaries.php?action=get_banks&country_id=${countryId}`);
            const result = await res.json();
            
            bankSelect.innerHTML = '<option value="">Select Bank</option>';
            if (result.success && result.data) {
                result.data.forEach(item => {
                    const opt = document.createElement('option');
                    opt.value = item.id;
                    opt.textContent = item.bank_name;
                    bankSelect.appendChild(opt);
                });
            }
        } catch (e) {
            console.error(e);
            bankSelect.innerHTML = '<option value="">Failed to load banks</option>';
        }
    } 
    else if (selectedText.includes('wallet')) {
        walletSection.style.display = 'block';
        walletSelect.innerHTML = '<option value="">Loading providers...</option>';
        
        try {
            const res = await fetch(`beneficiaries.php?action=get_wallets&country_id=${countryId}`);
            const result = await res.json();
            
            walletSelect.innerHTML = '<option value="">Select Wallet Provider</option>';
            if (result.success && result.data) {
                result.data.forEach(item => {
                    const opt = document.createElement('option');
                    opt.value = item.name;
                    opt.textContent = item.name;
                    walletSelect.appendChild(opt);
                });
            }
        } catch (e) {
            console.error(e);
            walletSelect.innerHTML = '<option value="">Failed to load providers</option>';
        }
    }
}

async function onBankChange() {
    const bankId = document.getElementById('bank_name').value;
    const branchSelect = document.getElementById('branch_name');
    
    branchSelect.innerHTML = '<option value="">Loading branches...</option>';
    
    if (!bankId) {
        branchSelect.innerHTML = '<option value="">Select Branch</option>';
        return;
    }

    try {
        const response = await fetch(`beneficiaries.php?action=get_branches&bank_id=${bankId}`);
        const result = await response.json();
        
        branchSelect.innerHTML = '<option value="">Select Branch</option>';
        if (result.success && result.data) {
            result.data.forEach(item => {
                const opt = document.createElement('option');
                opt.value = item.id;
                opt.textContent = item.branch_name;
                branchSelect.appendChild(opt);
            });
        }
    } catch (e) {
        console.error(e);
        branchSelect.innerHTML = '<option value="">Failed to load branches</option>';
    }
}
</script>

<?php include __DIR__ . '/../../includes/footer.php'; ?>
