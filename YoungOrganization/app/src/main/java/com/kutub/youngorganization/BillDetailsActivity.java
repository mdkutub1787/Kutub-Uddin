package com.kutub.youngorganization;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.youngorganization.model.Bill;
import com.kutub.youngorganization.model.User;

public class BillDetailsActivity extends AppCompatActivity {

    private TextView userNameTV, monthTV, amountTV;
    private TextView userDetailMobileTV, userTotalBalanceTV, userDetailEmailTV, userDetailNameTV;
    private TextView callBtn;
    private MaterialButton updateBtn, deleteBtn;
    private DatabaseReference billsRef, usersRef;
    private String billId;
    private Bill currentBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        // Initialize views
        userNameTV = findViewById(R.id.userNameTV);
        monthTV = findViewById(R.id.monthTV);
        amountTV = findViewById(R.id.amountTV);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        
        userDetailMobileTV = findViewById(R.id.userDetailMobileTV);
        userTotalBalanceTV = findViewById(R.id.userTotalBalanceTV);
        userDetailEmailTV = findViewById(R.id.userDetailEmailTV);
        userDetailNameTV = findViewById(R.id.userDetailNameTV);
        callBtn = findViewById(R.id.callBtn);

        // Get bill ID from intent
        billId = getIntent().getStringExtra("billId");
        
        // Initialize Firebase references
        billsRef = FirebaseDatabase.getInstance().getReference("bills");
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Load bill details
        if (billId != null) {
            loadBillDetails();
        } else {
            navigateToAdminDashboard();
        }

        // Setup button clicks
        updateBtn.setOnClickListener(v -> {
            if (currentBill != null) {
                Intent intent = new Intent(BillDetailsActivity.this, EditBillActivity.class);
                intent.putExtra("billId", billId);
                intent.putExtra("userId", currentBill.userId);
                intent.putExtra("month", currentBill.month);
                intent.putExtra("year", currentBill.year);
                intent.putExtra("amount", currentBill.amount);
                startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void loadBillDetails() {
        billsRef.child(billId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentBill = snapshot.getValue(Bill.class);
                if (currentBill != null) {
                    currentBill.id = snapshot.getKey();
                    monthTV.setText("মাস: " + currentBill.month);
                    amountTV.setText("টাকা: " + currentBill.amount);
                    loadUserDetails(currentBill.userId);
                    calculateTotalBalance(currentBill.userId);
                } else {
                    navigateToAdminDashboard();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                navigateToAdminDashboard();
            }
        });
    }

    private void loadUserDetails(String userId) {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    userNameTV.setText("নাম: " + user.name);
                    userDetailNameTV.setText("নাম: " + user.name);
                    userDetailMobileTV.setText("মোবাইল: " + user.mobileNumber);
                    userDetailEmailTV.setText("ইমেইল: " + user.email);
                    
                    callBtn.setOnClickListener(v -> {
                        if (user.mobileNumber != null && !user.mobileNumber.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + user.mobileNumber));
                            startActivity(intent);
                        } else {
                            Toast.makeText(BillDetailsActivity.this, "নম্বর পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    userNameTV.setText("নাম: অজানা");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userNameTV.setText("নাম: ত্রুটি হয়েছে");
            }
        });
    }

    private void calculateTotalBalance(String userId) {
        billsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalBalance = 0;
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    Bill bill = billSnapshot.getValue(Bill.class);
                    if (bill != null && bill.getAmount() != null) {
                        try {
                            totalBalance += Double.parseDouble(bill.getAmount());
                        } catch (NumberFormatException e) {
                            // Ignore if amount is not a valid number
                        }
                    }
                }
                userTotalBalanceTV.setText("ইউজারের মোট জমা: ৳" + String.format("%.0f", totalBalance));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userTotalBalanceTV.setText("মোট বিল লোড করা যায়নি");
            }
        });
    }

    private void navigateToAdminDashboard() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("নিশ্চিত করুন")
                .setMessage("আপনি কি এই বিল মুছে ফেলতে চান?")
                .setPositiveButton("হ্যাঁ", (dialog, which) -> deleteBill())
                .setNegativeButton("না", null)
                .show();
    }

    private void deleteBill() {
        if (billId != null && currentBill != null) {
            billsRef.child(billId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "বিল মুছে ফেলা হয়েছে", Toast.LENGTH_SHORT).show();
                    navigateToAdminDashboard();
                } else {
                    Toast.makeText(this, "বিল মুছে ফেলতে ব্যর্থ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 