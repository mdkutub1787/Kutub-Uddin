package com.kutub.youngorganization;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.youngorganization.model.Bill;
import com.kutub.youngorganization.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBillActivity extends AppCompatActivity {

    private AutoCompleteTextView userNameAutoComplete, monthAutoComplete, yearAutoComplete;
    private EditText amountET;
    private Button addBillBtn;
    private ProgressBar progressBar;
    private DatabaseReference databaseBills, databaseUsers;
    private String selectedUserId = null;
    private List<User> userList = new ArrayList<>();
    private String currentYearForBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        databaseBills = FirebaseDatabase.getInstance().getReference("bills");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        userNameAutoComplete = findViewById(R.id.userNameAutoComplete);
        monthAutoComplete = findViewById(R.id.monthAutoComplete);
        yearAutoComplete = findViewById(R.id.yearAutoComplete);
        amountET = findViewById(R.id.amountET);
        addBillBtn = findViewById(R.id.addBillBtn);
        progressBar = findViewById(R.id.progressBar);

        loadUsersForDropdown();

        addBillBtn.setOnClickListener(v -> addBill());

        userNameAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            selectedUserId = userList.get(position).id;
            filterMonthsForUser(selectedUserId);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Just go back to the previous screen (AdminDashboard)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUsersForDropdown() {
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                List<String> userNames = new ArrayList<>();
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                        userNames.add(user.name + " (" + user.mobileNumber + ")");
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddBillActivity.this,
                        android.R.layout.simple_dropdown_item_1line, userNames);
                userNameAutoComplete.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void filterMonthsForUser(String userId) {
        databaseBills.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, List<String>> yearToMonths = new HashMap<>();
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    Bill bill = billSnapshot.getValue(Bill.class);
                    if (bill != null && bill.month != null && bill.year != null) {
                        if (!yearToMonths.containsKey(bill.year)) {
                            yearToMonths.put(bill.year, new ArrayList<>());
                        }
                        yearToMonths.get(bill.year).add(bill.month);
                    }
                }

                String[] allMonths = getResources().getStringArray(R.array.months_array);
                String targetYear = null;
                List<String> usedMonths = null;

                // Find the first year with less than 12 months
                for (String year : yearToMonths.keySet()) {
                    List<String> months = yearToMonths.get(year);
                    if (months.size() < 12) {
                        if (targetYear == null || Integer.parseInt(year) < Integer.parseInt(targetYear)) {
                            targetYear = year;
                            usedMonths = months;
                        }
                    }
                }

                // If all years are full, show next year, but if no bills, show default year
                if (targetYear == null) {
                    if (yearToMonths.isEmpty()) {
                        targetYear = "2025";
                        usedMonths = new ArrayList<>();
                    } else {
                        int maxYear = 2025;
                        for (String year : yearToMonths.keySet()) {
                            if (Integer.parseInt(year) > maxYear) {
                                maxYear = Integer.parseInt(year);
                            }
                        }
                        targetYear = String.valueOf(maxYear + 1);
                        usedMonths = new ArrayList<>();
                    }
                }

                // Find available months
                List<String> availableMonths = new ArrayList<>();
                for (String m : allMonths) {
                    if (usedMonths == null || !usedMonths.contains(m)) {
                        availableMonths.add(m);
                    }
                }

                yearAutoComplete.setText(targetYear, false);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(AddBillActivity.this, android.R.layout.simple_dropdown_item_1line, java.util.Collections.singletonList(targetYear));
                yearAutoComplete.setAdapter(yearAdapter);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddBillActivity.this,
                        android.R.layout.simple_dropdown_item_1line, availableMonths);
                monthAutoComplete.setAdapter(adapter);
                monthAutoComplete.setText(""); // reset previous selection
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void addBill() {
        String userId = selectedUserId;
        String month = monthAutoComplete.getText().toString();
        String year = yearAutoComplete.getText().toString();
        String amount = amountET.getText().toString().trim();

        if (userId == null || userId.isEmpty()) {
            userNameAutoComplete.setError("ইউজার নির্বাচন করুন");
            return;
        }
        if (TextUtils.isEmpty(month)) {
            monthAutoComplete.setError("মাস নির্বাচন করুন");
            return;
        }
        if (TextUtils.isEmpty(year)) {
            Toast.makeText(this, "বছর পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            amountET.setError("টাকার পরিমাণ লিখুন");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Check if bill already exists for this month and year
        databaseBills.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean monthYearExists = false;
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    Bill existingBill = billSnapshot.getValue(Bill.class);
                    if (existingBill != null && existingBill.month.equalsIgnoreCase(month)
                        && existingBill.year != null && existingBill.year.equalsIgnoreCase(year)) {
                        monthYearExists = true;
                        break;
                    }
                }

                if (monthYearExists) {
                    progressBar.setVisibility(View.GONE);
                    monthAutoComplete.setError("এই মাস ও বছরের বিল ইতিমধ্যে যোগ করা হয়েছে");
                    Toast.makeText(AddBillActivity.this, "এই মাস ও বছরের বিল ইতিমধ্যে যোগ করা হয়েছে", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with adding the bill
                    String billId = databaseBills.push().getKey();
                    if (billId != null) {
                        Bill bill = new Bill(userId, month, year, amount);
                        bill.id = billId;

                        databaseBills.child(billId).setValue(bill).addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(AddBillActivity.this, "বিল যোগ করা হয়েছে", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddBillActivity.this, "বিল যোগ করা যায়নি", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddBillActivity.this, "বিল যোগ করা যায়নি", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 