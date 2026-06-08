package com.kutub.billcreate.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.billcreate.R;
import com.kutub.billcreate.model.Bill;

public class EditBillActivity extends AppCompatActivity {
    private Spinner spinnerBillMonth, spinnerBillAmount;
    private Button btnSaveBill;
    private DatabaseReference billsDatabase;
    private String billId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);

        // Initialize views
        spinnerBillMonth = findViewById(R.id.spinnerBillMonth);
        spinnerBillAmount = findViewById(R.id.spinnerBillAmount);
        btnSaveBill = findViewById(R.id.btnUpdateBill);

        // Get bill ID from intent
        billId = getIntent().getStringExtra("billId");
        if (TextUtils.isEmpty(billId)) {
            showToastAndFinish("Error: No bill ID provided");
            return;
        }

        // Initialize Firebase database reference
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        billsDatabase = FirebaseDatabase.getInstance().getReference("Bill").child(userId).child(billId);

        // Populate spinners
        populateSpinners();

        // Load bill data
        loadBillData();

        // Set update button click listener
        btnSaveBill.setOnClickListener(view -> updateBill());
    }

    private void populateSpinners() {
        // Populate month spinner from strings.xml
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.bill_months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBillMonth.setAdapter(monthAdapter);

        // Populate amount spinner from strings.xml
        ArrayAdapter<CharSequence> amountAdapter = ArrayAdapter.createFromResource(
                this, R.array.bill_amounts, android.R.layout.simple_spinner_item);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBillAmount.setAdapter(amountAdapter);
    }

    private void loadBillData() {
        billsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Bill bill = dataSnapshot.getValue(Bill.class);
                    if (bill != null) {
                        // Debugging logs
                        Log.d("EditBillActivity", "Bill Month: " + bill.getMonth());
                        Log.d("EditBillActivity", "Bill Amount: " + bill.getAmount());

                        // Set spinner selections
                        setSpinnerSelection(spinnerBillMonth, bill.getMonth());

                        // Format the amount to match spinner values
                        String formattedAmount = String.format("%.0f", bill.getAmount());
                        setSpinnerSelection(spinnerBillAmount, formattedAmount);
                    }
                } else {
                    showToastAndFinish("Bill not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToastAndFinish("Error loading bill: " + databaseError.getMessage());
            }
        });
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
    }

    private void updateBill() {
        // Retrieve selected values from the spinners
        String month = spinnerBillMonth.getSelectedItem().toString();
        String amountStr = spinnerBillAmount.getSelectedItem().toString();

        // Validate inputs
        if (month.equals("Select Month") || amountStr.equals("Select Amount")) {
            Toast.makeText(this, "Please select a valid month and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the bill in the database
        Bill bill = new Bill(billId, month, amount);
        billsDatabase.setValue(bill)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Bill updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update bill: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void showToastAndFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
}