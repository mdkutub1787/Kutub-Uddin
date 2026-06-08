package com.kutub.billcreate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class AddBillActivity extends AppCompatActivity {
    private Spinner spinnerBillMonth, spinnerBillAmount;
    private Button btnSaveBill;
    private DatabaseReference BillDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        spinnerBillMonth = findViewById(R.id.spinnerBillMonth);
        spinnerBillAmount = findViewById(R.id.spinnerBillAmount);
        btnSaveBill = findViewById(R.id.btnSaveBill);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        BillDatabase = FirebaseDatabase.getInstance().getReference("Bill").child(userId);

        // Populate spinners
        populateSpinners();

        btnSaveBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBill();
            }
        });
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

    private void saveBill() {
        String month = spinnerBillMonth.getSelectedItem().toString();
        String amountStr = spinnerBillAmount.getSelectedItem().toString();

        // Validate that the user has selected valid options
        if (month.equals("Select Month") || amountStr.equals("Select Amount")) {
            Toast.makeText(this, "Please select a valid month and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if a bill for the selected month already exists
        BillDatabase.orderByChild("month").equalTo(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // A bill for the selected month already exists
                    Toast.makeText(AddBillActivity.this, "A bill for " + month + " already exists. Please select a different month.", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed to save the bill
                    Double amount = Double.parseDouble(amountStr);

                    // Define the order of months starting from January
                    String[] monthsOrder = getResources().getStringArray(R.array.bill_months);
                    int selectedMonthIndex = -1;

                    // Find the index of the selected month, starting from January
                    for (int i = 1; i < monthsOrder.length; i++) { // Start from index 1 to skip "Select Month"
                        if (monthsOrder[i].equals(month)) {
                            selectedMonthIndex = i;
                            break;
                        }
                    }

                    if (selectedMonthIndex == -1) {
                        Toast.makeText(AddBillActivity.this, "Invalid month selected. Please select a valid month.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (selectedMonthIndex == 1) { // If January is selected, allow creation
                        saveBillToDatabase(month, amount);
                        return;
                    }

                    // Check if the previous month's bill exists
                    String previousMonth = monthsOrder[selectedMonthIndex - 1];
                    BillDatabase.orderByChild("month").equalTo(previousMonth).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                // Previous month's bill does not exist
                                Toast.makeText(AddBillActivity.this, "You must create a bill for " + previousMonth + " first.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Previous month's bill exists, allow creation
                                saveBillToDatabase(month, amount);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AddBillActivity.this, "Error checking previous month's bill: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddBillActivity.this, "Error checking existing bills: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBillToDatabase(String month, Double amount) {
        String billId = BillDatabase.push().getKey();
        Bill bill = new Bill(billId, month, amount);

        BillDatabase.child(billId).setValue(bill)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddBillActivity.this, "Bill saved Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddBillActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddBillActivity.this, "Failed to save bill: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}