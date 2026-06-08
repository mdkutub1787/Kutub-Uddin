package com.kutub.youngorganization;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.youngorganization.model.Bill;

public class EditBillActivity extends AppCompatActivity {

    private AutoCompleteTextView monthAutoComplete;
    private EditText amountET;
    private ProgressBar progressBar;
    private DatabaseReference billsRef;
    private String billId, userId, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);

        // Initialize Firebase
        billsRef = FirebaseDatabase.getInstance().getReference("bills");

        // Initialize views
        monthAutoComplete = findViewById(R.id.monthAutoComplete);
        amountET = findViewById(R.id.amountET);
        progressBar = findViewById(R.id.progressBar);

        // Get data from intent
        billId = getIntent().getStringExtra("billId");
        userId = getIntent().getStringExtra("userId");
        String month = getIntent().getStringExtra("month");
        String amount = getIntent().getStringExtra("amount");
        year = getIntent().getStringExtra("year");

        if (billId == null || userId == null || year == null) {
            Toast.makeText(this, "বিল খুঁজে পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup month spinner
        String[] months = getResources().getStringArray(R.array.months_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, months);
        monthAutoComplete.setAdapter(adapter);

        // Convert English month to Bengali and set it
        String bengaliMonth = convertToBengaliMonth(month);
        monthAutoComplete.setText(bengaliMonth, false);
        amountET.setText(amount);

        // Setup update button
        findViewById(R.id.updateBillBtn).setOnClickListener(v -> updateBill());
    }

    private String convertToBengaliMonth(String englishMonth) {
        String[] months = getResources().getStringArray(R.array.months_array);
        switch (englishMonth.toLowerCase()) {
            case "january": return months[0];
            case "february": return months[1];
            case "march": return months[2];
            case "april": return months[3];
            case "may": return months[4];
            case "june": return months[5];
            case "july": return months[6];
            case "august": return months[7];
            case "september": return months[8];
            case "october": return months[9];
            case "november": return months[10];
            case "december": return months[11];
            default: return englishMonth;
        }
    }

    private String convertToEnglishMonth(String bengaliMonth) {
        String[] months = getResources().getStringArray(R.array.months_array);
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(bengaliMonth)) {
                switch (i) {
                    case 0: return "January";
                    case 1: return "February";
                    case 2: return "March";
                    case 3: return "April";
                    case 4: return "May";
                    case 5: return "June";
                    case 6: return "July";
                    case 7: return "August";
                    case 8: return "September";
                    case 9: return "October";
                    case 10: return "November";
                    case 11: return "December";
                }
            }
        }
        return bengaliMonth;
    }

    private void updateBill() {
        String bengaliMonth = monthAutoComplete.getText().toString();
        String amount = amountET.getText().toString().trim();

        if (TextUtils.isEmpty(bengaliMonth)) {
            monthAutoComplete.setError("মাস নির্বাচন করুন");
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            amountET.setError("টাকার পরিমাণ লিখুন");
            return;
        }
        if (TextUtils.isEmpty(year)) {
            Toast.makeText(this, "বছর পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Convert Bengali month back to English for storage
        String englishMonth = convertToEnglishMonth(bengaliMonth);

        // Check if another bill exists for this month and year
        billsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean monthYearExists = false;
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    Bill existingBill = billSnapshot.getValue(Bill.class);
                    // Skip checking the current bill
                    if (existingBill != null && !existingBill.id.equals(billId)
                        && existingBill.month.equalsIgnoreCase(englishMonth)
                        && existingBill.year != null && existingBill.year.equalsIgnoreCase(year)) {
                        monthYearExists = true;
                        break;
                    }
                }

                if (monthYearExists) {
                    progressBar.setVisibility(View.GONE);
                    monthAutoComplete.setError("এই মাস ও বছরের বিল ইতিমধ্যে আছে");
                    Toast.makeText(EditBillActivity.this, "এই মাস ও বছরের বিল ইতিমধ্যে আছে", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with updating the bill
                    Bill updatedBill = new Bill(userId, englishMonth, year, amount);
                    updatedBill.id = billId;

                    billsRef.child(billId).setValue(updatedBill).addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(EditBillActivity.this, "বিল আপডেট করা হয়েছে", Toast.LENGTH_SHORT).show();
                            // Navigate back to AdminDashboard
                            Intent intent = new Intent(EditBillActivity.this, AdminDashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditBillActivity.this, "বিল আপডেট করা যায়নি", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditBillActivity.this, "বিল আপডেট করা যায়নি", Toast.LENGTH_SHORT).show();
            }
        });
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