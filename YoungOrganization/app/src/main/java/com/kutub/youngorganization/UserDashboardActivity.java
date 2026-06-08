package com.kutub.youngorganization;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kutub.youngorganization.adapter.BillUserAdapter;
import com.kutub.youngorganization.model.Bill;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.widget.TextView;
import android.view.Menu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.view.WindowManager;
import android.util.DisplayMetrics;

public class UserDashboardActivity extends AppCompatActivity {

    private BillUserAdapter adapter;
    private List<Bill> allBills;
    private DatabaseReference databaseBills;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private TextView headerMonth, headerYear, headerAmount;
    private String selectedMonth = null, selectedYear = null, selectedAmount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        allBills = new ArrayList<>();
        adapter = new BillUserAdapter(this, allBills);

        recyclerView = findViewById(R.id.adminDashboardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        headerMonth = findViewById(R.id.headerMonth);
        headerYear = findViewById(R.id.headerYear);
        headerAmount = findViewById(R.id.headerAmount);

        // Set header texts in Bangla
        headerMonth.setText("মাস");
        headerYear.setText("বছর");
        headerAmount.setText("টাকা");

        headerMonth.setOnClickListener(v -> {
            List<String> uniqueMonths = new ArrayList<>(adapter.getUniqueMonths());
            uniqueMonths.sort((m1, m2) -> Integer.compare(getMonthIndex(m1), getMonthIndex(m2)));
            showFilterDialog("মাস", uniqueMonths, selectedMonth, value -> {
                selectedMonth = value;
                applyAllFilters();
            });
        });
        headerYear.setOnClickListener(v -> {
            List<String> uniqueYears = new ArrayList<>(adapter.getUniqueYears());
            try {
                uniqueYears.sort((y1, y2) -> Integer.compare(Integer.parseInt(y1), Integer.parseInt(y2)));
            } catch (NumberFormatException e) {
                Collections.sort(uniqueYears); // Fallback to alphabetical
            }
            showFilterDialog("বছর", uniqueYears, selectedYear, value -> {
                selectedYear = value;
                applyAllFilters();
            });
        });
        headerAmount.setOnClickListener(v -> {
            List<String> uniqueAmounts = new ArrayList<>(adapter.getUniqueAmounts());
            try {
                uniqueAmounts.sort((a1, a2) -> Double.compare(Double.parseDouble(a1), Double.parseDouble(a2)));
            } catch (NumberFormatException e) {
                Collections.sort(uniqueAmounts); // Fallback to alphabetical
            }
            showFilterDialog("টাকা", uniqueAmounts, selectedAmount, value -> {
                selectedAmount = value;
                applyAllFilters();
            });
        });



        if (currentUser != null) {
            databaseBills = FirebaseDatabase.getInstance().getReference("bills");
            Query userBillsQuery = databaseBills.orderByChild("userId").equalTo(currentUser.getUid());
            loadBills(userBillsQuery);
        } else {
            Toast.makeText(this, "You need to be logged in to see bills.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            clearAllFilters();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            databaseBills = FirebaseDatabase.getInstance().getReference("bills");
            Query userBillsQuery = databaseBills.orderByChild("userId").equalTo(mAuth.getCurrentUser().getUid());
            loadBills(userBillsQuery);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_dashboard, menu);
        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        if (refreshItem != null) {
            refreshItem.setOnMenuItemClickListener(item -> {
                clearAllFilters();
                return true;
            });
        }
        return true;
    }

    private interface OnFilterSelected {
        void onSelected(String value);
    }

    private void showFilterDialog(String title, List<String> options, String selected, OnFilterSelected callback) {
        options.add(0, "সব");
        String[] arr = options.toArray(new String[0]);
        int checked = selected == null ? 0 : options.indexOf(selected);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title + " ফিল্টার করুন")
                .setSingleChoiceItems(arr, checked, (dialogInterface, which) -> {
                    callback.onSelected(arr[which]);
                    dialogInterface.dismiss();
                })
                .setNegativeButton("বাতিল", null)
                .create();
        dialog.show();

        // Adjust dialog width to 75% of screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.75f);
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void loadBills(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allBills.clear();
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    Bill bill = billSnapshot.getValue(Bill.class);
                    if (bill != null) {
                        android.util.Log.d("FIREBASE_BILL", "Bill: " + bill.userId + ", " + bill.month + ", " + bill.year);
                        allBills.add(bill);
                    }
                }
                // Sort by year (asc), then month (asc, Bengali order)
                allBills.sort((b1, b2) -> {
                    // Null checks for year
                    if (b1.year == null && b2.year == null) return 0;
                    if (b1.year == null) return 1;
                    if (b2.year == null) return -1;
                    int yearCompare = 0;
                    try {
                        yearCompare = Integer.compare(Integer.parseInt(b1.year), Integer.parseInt(b2.year));
                    } catch (Exception e) {
                        yearCompare = b1.year.compareTo(b2.year);
                    }
                    if (yearCompare != 0) return yearCompare;
                    // Null checks for month
                    if (b1.month == null && b2.month == null) return 0;
                    if (b1.month == null) return 1;
                    if (b2.month == null) return -1;
                    int m1 = getMonthIndex(b1.month);
                    int m2 = getMonthIndex(b2.month);
                    return Integer.compare(m1, m2);
                });
                adapter.setData(allBills);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDashboardActivity.this, "Failed to load bills.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAllFilters() {
        selectedMonth = null;
        selectedYear = null;
        selectedAmount = null;
        adapter.applyFilters(null, null, null);
    }

    private void applyAllFilters() {
        adapter.applyFilters(selectedMonth, selectedYear, selectedAmount);
    }





    // Utility: Get Bengali month index (0-based, Jan=0)
    private int getMonthIndex(String month) {
        if (month == null) return -1;
        String[] months = getResources().getStringArray(R.array.months_array);
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(month)) return i;
        }
        return -1; // Not found
    }
}