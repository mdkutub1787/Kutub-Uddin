package com.kutub.youngorganization;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizationTotalIncomeActivity extends AppCompatActivity {
    private TextView totalIncomeTV, totalExpenseTV;
    private ProgressBar progressBar;
    private DatabaseReference billsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_total_income);

        totalIncomeTV = findViewById(R.id.totalIncomeTV);
        totalExpenseTV = findViewById(R.id.totalExpenseTV);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        totalIncomeTV.setVisibility(View.INVISIBLE);
        totalExpenseTV.setVisibility(View.INVISIBLE);

        billsRef = FirebaseDatabase.getInstance().getReference("bills");
        loadTotalIncomeAndExpense();
    }

    private void loadTotalIncomeAndExpense() {
        billsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalIncome = 0.0;
                double totalExpense = 0.0;
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    String amountStr = billSnapshot.child("amount").getValue(String.class);
                    if (amountStr != null) {
                        try {
                            double amount = Double.parseDouble(amountStr);
                            if (amount >= 0) {
                                totalIncome += amount;
                            } else {
                                totalExpense += Math.abs(amount);
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
                totalIncomeTV.setText(String.format("মোট আয়: %.2f টাকা", totalIncome));
                totalExpenseTV.setText(String.format("মোট ব্যয়: %.2f টাকা", totalExpense));
                progressBar.setVisibility(View.GONE);
                totalIncomeTV.setVisibility(View.VISIBLE);
                totalExpenseTV.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                totalIncomeTV.setVisibility(View.VISIBLE);
                totalExpenseTV.setVisibility(View.VISIBLE);
                totalIncomeTV.setText("Error");
                totalExpenseTV.setText("");
                Toast.makeText(OrganizationTotalIncomeActivity.this, "ডেটা লোড হয়নি", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 